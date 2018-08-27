/*
 * Open Teradata Viewer ( editor language support php )
 * Copyright (C) 2015, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.php;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.CompletionXMLParser;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.Util;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.html.HtmlCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.modes.PHPTokenMaker;

import org.xml.sax.SAXException;

/**
 * Completion provider for PHP.
 *
 * @author D. Campione
 * 
 */
public class PhpCompletionProvider extends HtmlCompletionProvider {

    /**
     * Whether {@link #getAlreadyEnteredText(JTextComponent)} determined the
     * caret to be in a location where PHP completions were required (as opposed
     * to HTML completions).
     */
    private boolean phpCompletion;

    /** PHP function completions. */
    private List<ICompletion> phpCompletions;

    public PhpCompletionProvider() {
        // NOTE: If multiple instances of this provider are created, this rather
        // hefty XML file will be loaded each time. Better to share this
        // ICompletionProvider amongst all PHP editors (which is what
        // PhpLanguageSupport does)
        ClassLoader cl = getClass().getClassLoader();
        InputStream in = cl.getResourceAsStream("res/php.xml");
        try {
            if (in == null) { // Ghetto temporary workaround
                in = new FileInputStream("res/php.xml");
            }
            loadPhpCompletionsFromXML(in);
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
        }
    }

    /**
     * Loads completions from an XML input stream. The XML should validate
     * against the completion DTD found in the AutoComplete library.
     *
     * @param in The input stream to read from.
     * @throws IOException If an IO error occurs.
     */
    public void loadPhpCompletionsFromXML(InputStream in) throws IOException {
        long start = System.currentTimeMillis();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        CompletionXMLParser handler = new CompletionXMLParser(this);
        BufferedInputStream bin = new BufferedInputStream(in);
        try {
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(bin, handler);
            phpCompletions = handler.getCompletions();
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
            long time = System.currentTimeMillis() - start;
            System.out.println("XML loaded in: " + time + "ms");
            bin.close();
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getAlreadyEnteredText(JTextComponent comp) {
        phpCompletion = false;

        String text = super.getAlreadyEnteredText(comp);
        if (text == null) {
            if (inPhpBlock(comp)) {
                text = defaultGetAlreadyEnteredText(comp);
                phpCompletion = true;
            }
        }

        return text;
    }

    /** {@inheritDoc} */
    @Override
    protected List<ICompletion> getCompletionsImpl(JTextComponent comp) {
        List<ICompletion> list = null;
        String text = getAlreadyEnteredText(comp); // Sets phpCompletion

        if (phpCompletion) {
            if (text == null) {
                list = new ArrayList<ICompletion>(0);
            } else {
                list = new ArrayList<ICompletion>();

                @SuppressWarnings("unchecked")
                int index = Collections.binarySearch(phpCompletions, text,
                        comparator);
                if (index < 0) {
                    index = -index - 1;
                }

                while (index < phpCompletions.size()) {
                    ICompletion c = phpCompletions.get(index);
                    if (Util.startsWithIgnoreCase(c.getInputText(), text)) {
                        list.add(c);
                        index++;
                    } else {
                        break;
                    }
                }
            }
        } else {
            list = super.getCompletionsImpl(comp);
        }

        return list;
    }

    /**
     * Returns whether the caret is inside of a PHP block in this text
     * component.
     *
     * @param comp The <code>SyntaxTextAera</code>.
     * @return Whether the caret is inside a PHP block.
     */
    private boolean inPhpBlock(JTextComponent comp) {
        SyntaxTextArea textArea = (SyntaxTextArea) comp;
        int dot = comp.getCaretPosition();
        SyntaxDocument doc = (SyntaxDocument) comp.getDocument();
        int line;
        try {
            line = textArea.getLineOfOffset(dot);
        } catch (BadLocationException ble) {
            ExceptionDialog.hideException(ble);
            return false;
        }
        IToken token = doc.getTokenListForLine(line);

        boolean inPhp = false;

        // Check previous tokens on this line. We're looking to see if either
        // "<?php" or "<?" comes after any "?>" (before our caret position)
        while (token != null && token.isPaintable() && token.getOffset() <= dot) {
            if (token.getType() == IToken.SEPARATOR && token.length() >= 2) {
                char ch1 = token.charAt(0);
                char ch2 = token.charAt(1);
                if (ch1 == '<' && ch2 == '?') {
                    inPhp = true;
                } else if (ch1 == '?' && ch2 == '>') {
                    inPhp = false;
                }
            }
            token = token.getNextToken();
        }

        // Check if previous line ended in a PHP block.
        // HACK: This relies on insider knowledge of PhpTokenMaker. All
        // PHP-related states have the "lowest" token types
        if (!inPhp && line > 0) {
            int prevLineEndType = doc.getLastTokenTypeOnLine(line - 1);
            if (prevLineEndType <= PHPTokenMaker.INTERNAL_IN_PHP) {
                inPhp = true;
            }
        }

        return inPhp;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAutoActivateOkay(JTextComponent tc) {
        return inPhpBlock(tc) ? false : super.isAutoActivateOkay(tc);
    }
}