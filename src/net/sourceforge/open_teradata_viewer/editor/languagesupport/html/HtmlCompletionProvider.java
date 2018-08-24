/*
 * Open Teradata Viewer ( editor language support html )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.html;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.DefaultCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.IParameterizedCompletion.Parameter;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.MarkupTagCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.Util;
import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxUtilities;

/**
 * Completion provider for HTML documents.
 *
 * @author D. Campione
 * 
 */
public class HtmlCompletionProvider extends DefaultCompletionProvider {

    /** A mapping of tag names to their legal attributes. */
    private Map<String, List<AttributeCompletion>> tagToAttrs;

    /**
     * Whether the text last grabbed via {@link
     * #getAlreadyEnteredText(JTextComponent)} was an HTML tag name.
     *
     * @see #lastTagName
     */
    private boolean isTagName;

    /**
     * Returns the last tag name grabbed via {@link
     * #getAlreadyEnteredText(JTextComponent)}. This value is only valid if
     * {@link #isTagName} is <code>false</code>.
     */
    private String lastTagName;

    /** Ctor. */
    public HtmlCompletionProvider() {
        initCompletions();

        tagToAttrs = new HashMap<String, List<AttributeCompletion>>();
        for (ICompletion comp : completions) {
            MarkupTagCompletion c = (MarkupTagCompletion) comp;
            String tag = c.getInputText();
            List<AttributeCompletion> attrs = new ArrayList<AttributeCompletion>();
            tagToAttrs.put(tag.toLowerCase(), attrs);
            for (int j = 0; j < c.getAttributeCount(); j++) {
                Parameter param = c.getAttribute(j);
                attrs.add(new AttributeCompletion(this, param));
            }
        }

        setAutoActivationRules(false, "<");
    }

    /**
     * This hack is just a hook for subclasses (e.g.
     * <code>PhpCompletionProvider</code>) to be able to get at the
     * <code>DefaultCompletionProvider</code> implementation.
     *
     * @param comp The text component.
     * @return The text or <code>null</code> if none.
     */
    protected String defaultGetAlreadyEnteredText(JTextComponent comp) {
        return super.getAlreadyEnteredText(comp);
    }

    /**
     * Locates the name of the tag a given offset is in. This method assumes
     * that the caller has already concluded that <code>offs</code> is in fact
     * inside a tag and that there is a little "text" just before it.
     *
     * @param doc The document being parsed.
     * @param tokenList The token list for the current line.
     * @param offs The offset into the document to check.
     * @return Whether a tag name was found.
     */
    private final boolean findLastTagNameBefore(SyntaxDocument doc,
            IToken tokenList, int offs) {
        lastTagName = null;
        boolean foundOpenTag = false;

        for (IToken t = tokenList; t != null; t = t.getNextToken()) {
            if (t.containsPosition(offs)) {
                break;
            } else if (t.getType() == IToken.MARKUP_TAG_NAME) {
                lastTagName = t.getLexeme();
            } else if (t.getType() == IToken.MARKUP_TAG_DELIMITER) {
                lastTagName = null;
                foundOpenTag = t.isSingleChar('<');
                t = t.getNextToken();
                // Don't check for MARKUP_TAG_NAME to allow for unknown tag
                // names, such as JSP tags
                if (t != null && !t.isWhitespace()) {
                    lastTagName = t.getLexeme();
                }
            }
        }

        if (lastTagName == null && !foundOpenTag) {
            Element root = doc.getDefaultRootElement();
            int prevLine = root.getElementIndex(offs) - 1;
            while (prevLine >= 0) {
                tokenList = doc.getTokenListForLine(prevLine);
                for (IToken t = tokenList; t != null; t = t.getNextToken()) {
                    if (t.getType() == IToken.MARKUP_TAG_NAME) {
                        lastTagName = t.getLexeme();
                    } else if (t.getType() == IToken.MARKUP_TAG_DELIMITER) {
                        lastTagName = null;
                        foundOpenTag = t.isSingleChar('<');
                        t = t.getNextToken();
                        // Don't check for MARKUP_TAG_NAME to allow for unknown
                        // tag names, such as JSP tags
                        if (t != null && !t.isWhitespace()) {
                            lastTagName = t.getLexeme();
                        }
                    }
                }
                if (lastTagName != null || foundOpenTag) {
                    break;
                }
                prevLine--;
            }
        }

        return lastTagName != null;
    }

    /** {@inheritDoc} */
    @Override
    public String getAlreadyEnteredText(JTextComponent comp) {
        isTagName = true;
        lastTagName = null;

        String text = super.getAlreadyEnteredText(comp);
        if (text != null) {
            // Check token just before caret (i.e., what we're typing after)
            int dot = comp.getCaretPosition();
            if (dot > 0) { // Must go back 1
                SyntaxTextArea textArea = (SyntaxTextArea) comp;

                try {
                    int line = textArea.getLineOfOffset(dot - 1);
                    IToken list = textArea.getTokenListForLine(line);

                    if (list != null) {
                        IToken t = SyntaxUtilities.getTokenAtOffset(list,
                                dot - 1);

                        if (t == null) {
                            text = null;
                        }
                        // If we're completing just after a tag delimiter, only
                        // offer suggestions for the "inside" of tags, e.g.
                        // after "<" and "</"
                        else if (t.getType() == IToken.MARKUP_TAG_DELIMITER) {
                            if (!isTagOpeningToken(t)) {
                                text = null;
                            }
                        }
                        // If we're completing after whitespace, we must
                        // determine whether we're "inside" a tag
                        else if (t.getType() == IToken.WHITESPACE) {
                            if (!insideMarkupTag(textArea, list, line, dot)) {
                                text = null;
                            }
                        }
                        // Otherwise, only auto-complete if we're appending to
                        // text already recognized as a markup tag name or
                        // attribute (e.g. we know we're in a tag)
                        else if (t.getType() != IToken.MARKUP_TAG_ATTRIBUTE
                                && t.getType() != IToken.MARKUP_TAG_NAME) {
                            // We also have the case where "dot" was the start
                            // offset of the line, so the token list we got was
                            // actually for the previous line. So here we must
                            // also check for an EOL token that means "we're in
                            // a tag".
                            // HACK: Using knowledge of HTML/JSP/PHPTokenMaker
                            if (t.getType() > -1 || t.getType() < -9) {
                                text = null;
                            }
                        }

                        if (text != null) { // We're going to auto-complete
                            t = getTokenBeforeOffset(list, dot - text.length());
                            isTagName = t != null && isTagOpeningToken(t);
                            if (!isTagName) {
                                SyntaxDocument doc = (SyntaxDocument) textArea
                                        .getDocument();
                                findLastTagNameBefore(doc, list, dot);
                            }
                        }
                    }
                } catch (BadLocationException ble) {
                    ExceptionDialog.hideException(ble);
                }
            } else {
                text = null; // No completions for offset 0
            }
        }

        return text;
    }

    /**
     * Returns the attributes that can be code-completed for the specified tag.
     * Subclasses can override this method to handle more than the standard set
     * of HTML 5 tags and their attributes.
     *
     * @param tagName The tag whose attributes are being code-completed.
     * @return A list of attributes or <code>null</code> if the tag is not
     *         recognized.
     */
    protected List<AttributeCompletion> getAttributeCompletionsForTag(
            String tagName) {
        return tagToAttrs.get(lastTagName);
    }

    /** {@inheritDoc} */
    @Override
    protected List<ICompletion> getCompletionsImpl(JTextComponent comp) {
        List<ICompletion> retVal = new ArrayList<ICompletion>();
        String text = getAlreadyEnteredText(comp);
        List<? extends ICompletion> completions = getTagCompletions();
        if (lastTagName != null) {
            lastTagName = lastTagName.toLowerCase();
            completions = getAttributeCompletionsForTag(lastTagName);
        }

        if (text != null && completions != null) {
            @SuppressWarnings("unchecked")
            int index = Collections.binarySearch(completions, text, comparator);
            if (index < 0) {
                index = -index - 1;
            }

            while (index < completions.size()) {
                ICompletion c = completions.get(index);
                if (Util.startsWithIgnoreCase(c.getInputText(), text)) {
                    retVal.add(c);
                    index++;
                } else {
                    break;
                }
            }
        }

        return retVal;
    }

    /**
     * Returns the completions for the basic tag set. This method is here so
     * subclasses can add to it if they provide additional tags (i.e. JSP).
     *
     * @return The completions for the standard tag set.
     */
    protected List<ICompletion> getTagCompletions() {
        return this.completions;
    }

    /**
     * Returns the token before the specified offset.
     *
     * @param tokenList A list of tokens containing the offset.
     * @param offs The offset.
     * @return The token before the offset or <code>null</code> if the offset
     *         was the first offset in the token list (or not in the token list
     *         at all, which would be an error).
     */
    private static final IToken getTokenBeforeOffset(IToken tokenList, int offs) {
        if (tokenList != null) {
            IToken prev = tokenList;
            for (IToken t = tokenList.getNextToken(); t != null; t = t
                    .getNextToken()) {
                if (t.containsPosition(offs)) {
                    return prev;
                }
                prev = t;
            }
        }
        return null;
    }

    /**
     * Calls {@link #loadFromXML(String)} to load all standard HTML completions.
     * Subclasses can override to also load additional standard tags (i.e. JSP's
     * <code>jsp:*</code> tags).
     */
    protected void initCompletions() {
        try {
            loadFromXML("res/html.xml");
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
        }
    }

    /**
     * Returns whether the given offset is inside a markup tag (and not in
     * string content, such as an attribute value).
     *
     * @param textArea The text area being parsed.
     * @param list The list of tokens for the current line (the line containing
     *        <code>offs</code>.
     * @param line The index of the line containing <code>offs</code>.
     * @param offs The offset into the text area's content to check.
     * @return Whether the offset is inside a markup tag.
     */
    private static final boolean insideMarkupTag(SyntaxTextArea textArea,
            IToken list, int line, int offs) {
        int inside = -1; // -1 => not determined, 0 => false, 1 => true

        for (IToken t = list; t != null; t = t.getNextToken()) {
            if (t.containsPosition(offs)) {
                break;
            }
            switch (t.getType()) {
            case IToken.MARKUP_TAG_NAME:
            case IToken.MARKUP_TAG_ATTRIBUTE:
                inside = 1;
                break;
            case IToken.MARKUP_TAG_DELIMITER:
                inside = t.isSingleChar('>') ? 0 : 1;
                break;
            }
        }

        // Still not determined - check how previous line ended
        if (inside == -1) {
            if (line == 0) {
                inside = 0;
            } else {
                SyntaxDocument doc = (SyntaxDocument) textArea.getDocument();
                int prevLastToken = doc.getLastTokenTypeOnLine(line - 1);
                // HACK: This code uses the insider knowledge that token types
                // -1 through -9 mean "something inside a tag" for all
                // applicable markup languages (HTML, JSP and PHP)
                if (prevLastToken <= -1 && prevLastToken >= -9) {
                    inside = 1;
                } else {
                    inside = 0;
                }
            }
        }

        return inside == 1;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAutoActivateOkay(JTextComponent tc) {
        boolean okay = super.isAutoActivateOkay(tc);

        if (okay) {
            SyntaxTextArea textArea = (SyntaxTextArea) tc;
            int dot = textArea.getCaretPosition();

            try {
                int line = textArea.getLineOfOffset(dot);
                IToken list = textArea.getTokenListForLine(line);

                if (list != null) {
                    return !insideMarkupTag(textArea, list, line, dot);
                }
            } catch (BadLocationException ble) {
                ExceptionDialog.hideException(ble);
            }
        }

        return okay;
    }

    /**
     * Returns whether this token's text is "<" or "</". It is assumed that
     * whether this is a markup delimiter token is checked elsewhere.
     *
     * @param t The token to check.
     * @return Whether it is a tag opening token.
     */
    private static final boolean isTagOpeningToken(IToken t) {
        return t.isSingleChar('<')
                || (t.length() == 2 && t.charAt(0) == '<' && t.charAt(1) == '/');
    }
}