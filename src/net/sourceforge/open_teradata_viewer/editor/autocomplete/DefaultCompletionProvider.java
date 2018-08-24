/*
 * Open Teradata Viewer ( editor autocomplete )
 * Copyright (C) 2014, D. Campione
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sourceforge.open_teradata_viewer.editor.autocomplete;

import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Segment;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.xml.sax.SAXException;

/**
 * A basic completion provider implementation. This provider has no
 * understanding of language semantics. It simply checks the text entered up to
 * the caret position for a match against known completions. This is all that is
 * needed in the majority of cases.
 *
 * @author D. Campione
 * 
 */
public class DefaultCompletionProvider extends AbstractCompletionProvider {

    protected Segment seg;

    /** Used to speed up {@link #getCompletionsAt(JTextComponent, Point)}. */
    private String lastCompletionsAtText;

    /**
     * Used to speed up {@link #getCompletionsAt(JTextComponent, Point)}, since
     * this may be called multiple times in succession (this is usually called
     * by <tt>JTextComponent.getToolTipText()</tt>, and if the user wiggles the
     * mouse while a tool tip is displayed, this method gets repeatedly called.
     * It can be costly so we try to speed it up a tad).
     */
    private List<ICompletion> lastParameterizedCompletionsAt;

    /**
     * Ctor. The returned provider will not be aware of any completions.
     *
     * @see #addCompletion(ICompletion)
     */
    public DefaultCompletionProvider() {
        init();
    }

    /**
     * Creates a completion provider that provides completion for a simple list
     * of words.
     *
     * @param words The words to offer as completion suggestions. If this is
     *        <code>null</code>, no completions will be known.
     * @see BasicCompletion
     */
    public DefaultCompletionProvider(String[] words) {
        init();
        addWordCompletions(words);
    }

    /**
     * Returns the text just before the current caret position that could be the
     * start of something auto-completable.<p>
     *
     * This method returns all characters before the caret that are matched by
     * {@link #isValidChar(char)}.
     *
     * {@inheritDoc}
     */
    @Override
    public String getAlreadyEnteredText(JTextComponent comp) {
        Document doc = comp.getDocument();

        int dot = comp.getCaretPosition();
        Element root = doc.getDefaultRootElement();
        int index = root.getElementIndex(dot);
        Element elem = root.getElement(index);
        int start = elem.getStartOffset();
        int len = dot - start;
        try {
            doc.getText(start, len, seg);
        } catch (BadLocationException ble) {
            ExceptionDialog.hideException(ble);
            return EMPTY_STRING;
        }

        int segEnd = seg.offset + len;
        start = segEnd - 1;
        while (start >= seg.offset && isValidChar(seg.array[start])) {
            start--;
        }
        start++;

        len = segEnd - start;
        return len == 0 ? EMPTY_STRING : new String(seg.array, start, len);
    }

    /** {@inheritDoc} */
    @Override
    public List<ICompletion> getCompletionsAt(JTextComponent tc, Point p) {
        int offset = tc.viewToModel(p);
        if (offset < 0 || offset >= tc.getDocument().getLength()) {
            lastCompletionsAtText = null;
            return lastParameterizedCompletionsAt = null;
        }

        Segment s = new Segment();
        Document doc = tc.getDocument();
        Element root = doc.getDefaultRootElement();
        int line = root.getElementIndex(offset);
        Element elem = root.getElement(line);
        int start = elem.getStartOffset();
        int end = elem.getEndOffset() - 1;

        try {
            doc.getText(start, end - start, s);

            // Get the valid chars before the specified offset
            int startOffs = s.offset + (offset - start) - 1;
            while (startOffs >= s.offset && isValidChar(s.array[startOffs])) {
                startOffs--;
            }

            // Get the valid chars at and after the specified offset
            int endOffs = s.offset + (offset - start);
            while (endOffs < s.offset + s.count
                    && isValidChar(s.array[endOffs])) {
                endOffs++;
            }

            int len = endOffs - startOffs - 1;
            if (len <= 0) {
                return lastParameterizedCompletionsAt = null;
            }
            String text = new String(s.array, startOffs + 1, len);

            if (text.equals(lastCompletionsAtText)) {
                return lastParameterizedCompletionsAt;
            }

            // Get a list of all completions matching the text
            List<ICompletion> list = getCompletionByInputText(text);
            lastCompletionsAtText = text;
            return lastParameterizedCompletionsAt = list;

        } catch (BadLocationException ble) {
            ExceptionDialog.hideException(ble); // Never happens
        }

        lastCompletionsAtText = null;
        return lastParameterizedCompletionsAt = null;
    }

    /** {@inheritDoc} */
    @Override
    public List<IParameterizedCompletion> getParameterizedCompletions(
            JTextComponent tc) {
        List<IParameterizedCompletion> list = null;

        // If this provider doesn't support parameterized completions, bail out
        // now
        char paramListStart = getParameterListStart();
        if (paramListStart == 0) {
            return list; // null
        }

        int dot = tc.getCaretPosition();
        Segment s = new Segment();
        Document doc = tc.getDocument();
        Element root = doc.getDefaultRootElement();
        int line = root.getElementIndex(dot);
        Element elem = root.getElement(line);
        int offs = elem.getStartOffset();
        int len = dot - offs - 1/*paramListStart.length()*/;
        if (len <= 0) { // Not enough chars on line for a method
            return list; // null
        }

        try {
            doc.getText(offs, len, s);

            // Get the identifier preceding the '(', ignoring any whitespace
            // between them
            offs = s.offset + len - 1;
            while (offs >= s.offset && Character.isWhitespace(s.array[offs])) {
                offs--;
            }
            int end = offs;
            while (offs >= s.offset && isValidChar(s.array[offs])) {
                offs--;
            }

            String text = new String(s.array, offs + 1, end - offs);

            // Get a list of all completions matching the text, but then narrow
            // it down to just the ParameterizedCompletions
            List<ICompletion> l = getCompletionByInputText(text);
            if (l != null && !l.isEmpty()) {
                for (int i = 0; i < l.size(); i++) {
                    Object o = l.get(i);
                    if (o instanceof IParameterizedCompletion) {
                        if (list == null) {
                            list = new ArrayList<IParameterizedCompletion>(1);
                        }
                        list.add((IParameterizedCompletion) o);
                    }
                }
            }
        } catch (BadLocationException ble) {
            ExceptionDialog.hideException(ble); // Never happens
        }

        return list;
    }

    /** Initializes this completion provider. */
    protected void init() {
        seg = new Segment();
    }

    /**
     * Returns whether the specified character is valid in an auto-completion.
     * The default implementation is equivalent to
     * "<code>Character.isLetterOrDigit(ch) || ch=='_'</code>". Subclasses can
     * override this method to change what characters are matched.
     *
     * @param ch The character.
     * @return Whether the character is valid.
     */
    protected boolean isValidChar(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_';
    }

    /**
     * Loads completions from an XML file. The XML should validate against
     * <code>CompletionXml.dtd</code>.
     *
     * @param file An XML file to load from.
     * @throws IOException If an IO error occurs.
     */
    public void loadFromXML(File file) throws IOException {
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(
                file));
        try {
            loadFromXML(bin);
        } finally {
            bin.close();
        }
    }

    /**
     * Loads completions from an XML input stream. The XML should validate
     * against <code>CompletionXml.dtd</code>.
     *
     * @param in The input stream to read from.
     * @throws IOException If an IO error occurs.
     */
    public void loadFromXML(InputStream in) throws IOException {
        loadFromXML(in, null);
    }

    /**
     * Loads completions from an XML input stream. The XML should validate
     * against <code>CompletionXml.dtd</code>.
     *
     * @param in The input stream to read from.
     * @param cl The class loader to use when loading any extra classes defined
     *        in the XML, such as custom {@link FunctionCompletion}s. This may
     *        be <code>null</code> if the default is to be used, or if no custom
     *        completions are defined in the XML.
     * @throws IOException If an IO error occurs.
     */
    public void loadFromXML(InputStream in, ClassLoader cl) throws IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        CompletionXMLParser handler = new CompletionXMLParser(this, cl);
        BufferedInputStream bin = new BufferedInputStream(in);
        try {
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(bin, handler);
            List<ICompletion> completions = handler.getCompletions();
            addCompletions(completions);
            char startChar = handler.getParamStartChar();
            if (startChar != 0) {
                char endChar = handler.getParamEndChar();
                String sep = handler.getParamSeparator();
                if (endChar != 0 && sep != null && sep.length() > 0) { // Sanity
                    setParameterizedCompletionParams(startChar, sep, endChar);
                }
            }
        } catch (SAXException se) {
            throw new IOException(se.toString());
        } catch (ParserConfigurationException pce) {
            throw new IOException(pce.toString());
        } finally {
            bin.close();
        }
    }

    /**
     * Loads completions from an XML file. The XML should validate against
     * <code>CompletionXml.dtd</code>.
     *
     * @param resource A resource the current ClassLoader can get to.
     * @throws IOException If an IO error occurs.
     */
    public void loadFromXML(String resource) throws IOException {
        ClassLoader cl = getClass().getClassLoader();
        InputStream in = cl.getResourceAsStream(resource);
        if (in == null) {
            File file = new File(resource);
            if (file.isFile()) {
                in = new FileInputStream(file);
            } else {
                throw new IOException("No such resource: " + resource);
            }
        }
        BufferedInputStream bin = new BufferedInputStream(in);
        try {
            loadFromXML(bin);
        } finally {
            bin.close();
        }
    }
}