/*
 * Open Teradata Viewer ( editor syntax )
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.SmartHighlightPainter;

/**
 * Marks occurrences of the current token for HTML. Tags that require a closing
 * tag have their "opposite" tag closed.
 *
 * @author D. Campione
 * 
 */
public class HtmlOccurrenceMarker implements IOccurrenceMarker {

    private static final char[] CLOSE_TAG_START = { '<', '/' };
    private static final char[] TAG_SELF_CLOSE = { '/', '>' };

    private static final Set<String> TAGS_REQUIRING_CLOSING = getRequiredClosingTags();

    public static final Set<String> getRequiredClosingTags() {
        final String[] tags = { "html", "head", "title", "style", "script",
                "noscript", "body", "section", "nav", "article", "aside", "h1",
                "h2", "h3", "h4", "h5", "h6", "header", "footer", "address",
                "pre", "dialog", "blockquote", "ol", "ul", "dl", "a", "q",
                "cite", "em", "strong", "small", "mark", "dfn", "abbr", "time",
                "progress", "meter", "code", "var", "samp", "kbd", "sub",
                "sup", "span", "i", "b", "bdo", "ruby", "rt", "rp", "ins",
                "del", "figure", "iframe", "object", "video", "audio",
                "canvas", "map", "table", "caption", "form", "fieldset",
                "label", "button", "select", "datalist", "textarea", "output",
                "details", "bb", "menu",
                "legend",
                "div",
                // Obsolete elements
                "acronym", "applet", "big", "blink", "center", "dir", "font",
                "frame", "frameset", "isindex", "listing", "marquee", "nobr",
                "noembed", "noframes", "plaintext", "s", "spacer", "strike",
                "tt", "u", "xmp", };
        return new HashSet<String>(Arrays.asList(tags));
    }

    /** {@inheritDoc} */
    @Override
    public void markOccurrences(SyntaxDocument doc, IToken t,
            SyntaxTextAreaHighlighter h, SmartHighlightPainter p) {
        String lexemeStr = t.getLexeme();
        char[] lexeme = lexemeStr.toCharArray();
        lexemeStr = lexemeStr.toLowerCase();
        int tokenOffs = t.getOffset();
        Element root = doc.getDefaultRootElement();
        int lineCount = root.getElementCount();
        int curLine = root.getElementIndex(t.getOffset());

        // For now, we only check for tags on the current line, for simplicity.
        // Tags spanning multiple lines aren't common anyway
        boolean found = false;
        boolean forward = true;
        t = doc.getTokenListForLine(curLine);
        while (t != null && t.isPaintable()) {
            if (t.getType() == IToken.MARKUP_TAG_DELIMITER) {
                if (t.isSingleChar('<') && t.getOffset() + 1 == tokenOffs) {
                    // Don't try to match a tag that is optionally closed (or
                    // closing is forbidden entirely)
                    if (TAGS_REQUIRING_CLOSING.contains(lexemeStr)) {
                        found = true;
                    }
                    break;
                } else if (t.is(CLOSE_TAG_START)
                        && t.getOffset() + 2 == tokenOffs) {
                    // Searching backward, we assume we can find the opening
                    // tag. Don't really care if it's valid or not
                    found = true;
                    forward = false;
                    break;
                }
            }
            t = t.getNextToken();
        }

        if (!found) {
            return;
        }

        if (forward) {
            t = t.getNextToken().getNextToken();

            do {
                while (t != null && t.isPaintable()) {
                    if (t.getType() == IToken.MARKUP_TAG_DELIMITER) {
                        if (t.is(CLOSE_TAG_START)) {
                            IToken match = t.getNextToken();
                            if (match != null && match.is(lexeme)) {
                                try {
                                    int end = match.getOffset()
                                            + match.length();
                                    h.addMarkedOccurrenceHighlight(
                                            match.getOffset(), end, p);
                                    end = tokenOffs + match.length();
                                    h.addMarkedOccurrenceHighlight(tokenOffs,
                                            end, p);
                                } catch (BadLocationException ble) {
                                    ExceptionDialog.hideException(ble); // Never happens
                                }
                                return; // We're done
                            }
                        }
                    }
                    t = t.getNextToken();
                }

                if (++curLine < lineCount) {
                    t = doc.getTokenListForLine(curLine);
                }
            } while (curLine < lineCount);
        } else { // !forward
            Stack<IToken> matches = new Stack<IToken>();
            boolean inPossibleMatch = false;
            t = doc.getTokenListForLine(curLine);
            final int endBefore = tokenOffs - 2; // Stop before "</"

            do {
                while (t != null && t.getOffset() < endBefore
                        && t.isPaintable()) {
                    if (t.getType() == IToken.MARKUP_TAG_DELIMITER) {
                        if (t.isSingleChar('<')) {
                            IToken next = t.getNextToken();
                            if (next != null) {
                                if (next.is(lexeme)) {
                                    matches.push(next);
                                    inPossibleMatch = true;
                                } else {
                                    inPossibleMatch = false;
                                }
                                t = next;
                            }
                        } else if (t.isSingleChar('>')) {
                            inPossibleMatch = false;
                        } else if (inPossibleMatch && t.is(TAG_SELF_CLOSE)) {
                            matches.pop();
                        } else if (t.is(CLOSE_TAG_START)) {
                            IToken next = t.getNextToken();
                            if (next != null) {
                                // Invalid XML might not have a match
                                if (next.is(lexeme) && !matches.isEmpty()) {
                                    matches.pop();
                                }
                                t = next;
                            }
                        }
                    }
                    t = t.getNextToken();
                }

                if (!matches.isEmpty()) {
                    try {
                        IToken match = matches.pop();
                        int end = match.getOffset() + match.length();
                        h.addMarkedOccurrenceHighlight(match.getOffset(), end,
                                p);
                        end = tokenOffs + match.length();
                        h.addMarkedOccurrenceHighlight(tokenOffs, end, p);
                    } catch (BadLocationException ble) {
                        ExceptionDialog.hideException(ble); // Never happens
                    }
                    return;
                }

                if (--curLine >= 0) {
                    t = doc.getTokenListForLine(curLine);
                }

            } while (curLine >= 0);
        }
    }
}