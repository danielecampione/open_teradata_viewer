/*
 * Open Teradata Viewer ( editor syntax )
 * Copyright (C) 2013, D. Campione
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

import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Stack;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Segment;
import javax.swing.text.TextAction;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.Gutter;
import net.sourceforge.open_teradata_viewer.editor.IconRowHeader;
import net.sourceforge.open_teradata_viewer.editor.RecordableTextAction;
import net.sourceforge.open_teradata_viewer.editor.TextArea;
import net.sourceforge.open_teradata_viewer.editor.TextAreaEditorKit;
import net.sourceforge.open_teradata_viewer.editor.syntax.folding.Fold;
import net.sourceforge.open_teradata_viewer.editor.syntax.folding.FoldCollapser;
import net.sourceforge.open_teradata_viewer.editor.syntax.folding.FoldManager;
import net.sourceforge.open_teradata_viewer.editor.syntax.templates.ICodeTemplate;

/**
 * An extension of <code>TextAreaEditorKit</code> that adds functionality for
 * programming-specific stuff. There are currently subclasses to handle:
 *
 * <ul>
 *   <li>Toggling code folds.</li>
 *   <li>Aligning "closing" curly braces with their matches, if the current
 *       programming language uses curly braces to identify code blocks.</li>
 *   <li>Copying the current selection as RTF.</li>
 *   <li>Block indentation (increasing the indent of one or multiple lines)</li>
 *   <li>Block un-indentation (decreasing the indent of one or multiple lines)
 *       </li>
 *   <li>Inserting a "code template" when a configurable key (e.g. a space) is
 *       pressed</li>
 *   <li>Decreasing the point size of all fonts in the text area</li>
 *   <li>Increasing the point size of all fonts in the text area</li>
 *   <li>Moving the caret to the "matching bracket" of the one at the current
 *       caret position</li>
 *   <li>Toggling whether the currently selected lines are commented out.</li>
 *   <li>Better selection of "words" on mouse double-clicks for programming
 *       languages.</li>
 *   <li>Better keyboard navigation via Ctrl+arrow keys for programming
 *       languages.</li>
 * </ul>
 *
 * @author D. Campione
 * 
 */
public class SyntaxTextAreaEditorKit extends TextAreaEditorKit {

    private static final long serialVersionUID = 1654151110724956743L;

    public static final String staCloseCurlyBraceAction = "STA.CloseCurlyBraceAction";
    public static final String staCloseMarkupTagAction = "STA.CloseMarkupTagAction";
    public static final String staCollapseAllFoldsAction = "STA.CollapseAllFoldsAction";
    public static final String staCollapseAllCommentFoldsAction = "STA.CollapseAllCommentFoldsAction";
    public static final String staCollapseFoldAction = "STA.CollapseFoldAction";
    public static final String staCopyAsRtfAction = "STA.CopyAsRtfAction";
    public static final String staDecreaseIndentAction = "STA.DecreaseIndentAction";
    public static final String staExpandAllFoldsAction = "STA.ExpandAllFoldsAction";
    public static final String staExpandFoldAction = "STA.ExpandFoldAction";
    public static final String staGoToMatchingBracketAction = "STA.GoToMatchingBracketAction";
    public static final String staPossiblyInsertTemplateAction = "STA.TemplateAction";
    public static final String staToggleCommentAction = "STA.ToggleCommentAction";
    public static final String staToggleCurrentFoldAction = "STA.ToggleCurrentFoldAction";

    /**
     * The actions that <code>SyntaxTextAreaEditorKit</code> adds to those of
     * <code>TextAreaEditorKit</code>.
     */
    private static final Action[] defaultActions = {
            new CloseCurlyBraceAction(), new CloseMarkupTagAction(),
            new BeginWordAction(beginWordAction, false),
            new BeginWordAction(selectionBeginWordAction, true),
            new ChangeFoldStateAction(staCollapseFoldAction, true),
            new ChangeFoldStateAction(staExpandFoldAction, false),
            new CollapseAllFoldsAction(), new CopyAsRtfAction(),
            new DecreaseIndentAction(), new DeletePrevWordAction(),
            new EndAction(endAction, false),
            new EndAction(selectionEndAction, true),
            new EndWordAction(endWordAction, false),
            new EndWordAction(endWordAction, true), new ExpandAllFoldsAction(),
            new GoToMatchingBracketAction(), new InsertBreakAction(),
            new InsertTabAction(), new NextWordAction(nextWordAction, false),
            new NextWordAction(selectionNextWordAction, true),
            new PossiblyInsertTemplateAction(),
            new PreviousWordAction(previousWordAction, false),
            new PreviousWordAction(selectionPreviousWordAction, true),
            new SelectWordAction(), new ToggleCommentAction(),};

    /** Ctor. */
    public SyntaxTextAreaEditorKit() {
    }

    /**
     * Returns the default document used by <code>SyntaxTextArea</code>s.
     *
     * @return The document.
     */
    public Document createDefaultDocument() {
        return new SyntaxDocument(ISyntaxConstants.SYNTAX_STYLE_NONE);
    }

    /**
     * Overridden to return a row header that is aware of folding.
     *
     * @param textArea The text area.
     * @return The icon row header.
     */
    public IconRowHeader createIconRowHeader(TextArea textArea) {
        return new FoldingAwareIconRowHeader((SyntaxTextArea) textArea);
    }

    /**
     * Fetches the set of commands that can be used on a text component that is
     * using a model and view produced by this kit.
     *
     * @return the command list
     */
    public Action[] getActions() {
        return TextAction.augmentList(super.getActions(),
                SyntaxTextAreaEditorKit.defaultActions);
    }

    /**
     * Positions the caret at the beginning of the word. This class is here to
     * better handle finding the "beginning of the word" for programming
     * languages.
     * 
     * @author D. Campione
     * 
     */
    protected static class BeginWordAction
            extends
                TextAreaEditorKit.BeginWordAction {

        private static final long serialVersionUID = 9158506996459873455L;
        private Segment seg;

        protected BeginWordAction(String name, boolean select) {
            super(name, select);
            seg = new Segment();
        }

        protected int getWordStart(TextArea textArea, int offs)
                throws BadLocationException {
            if (offs == 0) {
                return offs;
            }

            SyntaxDocument doc = (SyntaxDocument) textArea.getDocument();
            int line = textArea.getLineOfOffset(offs);
            int start = textArea.getLineStartOffset(line);
            if (offs == start) {
                return start;
            }
            int end = textArea.getLineEndOffset(line);
            if (line != textArea.getLineCount() - 1) {
                end--;
            }
            doc.getText(start, end - start, seg);

            // Determine the "type" of char at offs - lower case, upper case,
            // whitespace or other. We take special care here as we're starting
            // in the middle of the Segment to check whether we're already at
            // the "beginning" of a word
            int firstIndex = seg.getBeginIndex() + (offs - start) - 1;
            seg.setIndex(firstIndex);
            char ch = seg.current();
            char nextCh = offs == end ? 0 : seg.array[seg.getIndex() + 1];

            // The "word" is a group of letters and/or digits
            if (Character.isLetterOrDigit(ch)) {
                if (offs != end && !Character.isLetterOrDigit(nextCh)) {
                    return offs;
                }
                do {
                    ch = seg.previous();
                } while (Character.isLetterOrDigit(ch));
            } else if (Character.isWhitespace(ch)) { // The "word" is whitespace
                if (offs != end && !Character.isWhitespace(nextCh)) {
                    return offs;
                }
                do {
                    ch = seg.previous();
                } while (Character.isWhitespace(ch));
            }

            // Otherwise, the "word" a single "something else" char (operator,
            // etc.)

            offs -= firstIndex - seg.getIndex() + 1;
            if (ch != Segment.DONE && nextCh != '\n') {
                offs++;
            }

            return offs;
        }
    }

    /** Expands or collapses the nearest fold. */
    public static class ChangeFoldStateAction extends FoldRelatedAction {

        private static final long serialVersionUID = 5409538978475562168L;

        private boolean collapse;

        public ChangeFoldStateAction(String name, boolean collapse) {
            super(name);
            this.collapse = collapse;
        }

        public ChangeFoldStateAction(String name, Icon icon, String desc,
                Integer mnemonic, KeyStroke accelerator) {
            super(name, icon, desc, mnemonic, accelerator);
        }

        public void actionPerformedImpl(ActionEvent e, TextArea textArea) {
            SyntaxTextArea sta = (SyntaxTextArea) textArea;
            if (sta.isCodeFoldingEnabled()) {
                Fold fold = getClosestFold(sta);
                if (fold != null) {
                    fold.setCollapsed(collapse);
                }
                possiblyRepaintGutter(textArea);
            } else {
                UIManager.getLookAndFeel().provideErrorFeedback(sta);
            }
        }

        public final String getMacroID() {
            return getName();
        }
    }

    /**
     * Action that (optionally) aligns a closing curly brace with the line
     * containing its matching opening curly brace.
     * 
     * @author D. Campione
     * 
     */
    public static class CloseCurlyBraceAction extends RecordableTextAction {

        private static final long serialVersionUID = 2906527566500477427L;

        private Point bracketInfo;
        private Segment seg;

        public CloseCurlyBraceAction() {
            super(staCloseCurlyBraceAction);
            seg = new Segment();
        }

        public void actionPerformedImpl(ActionEvent e, TextArea textArea) {
            SyntaxTextArea sta = (SyntaxTextArea) textArea;
            SyntaxDocument doc = (SyntaxDocument) sta.getDocument();
            boolean alignCurlyBraces = sta.isAutoIndentEnabled()
                    && doc.getCurlyBracesDenoteCodeBlocks();

            if (alignCurlyBraces) {
                textArea.beginAtomicEdit();
            }

            try {
                textArea.replaceSelection("}");

                // If the user wants to align curly braces..
                if (alignCurlyBraces) {
                    Element root = doc.getDefaultRootElement();
                    int dot = sta.getCaretPosition() - 1; // Start before '{'
                    int line = root.getElementIndex(dot);
                    Element elem = root.getElement(line);
                    int start = elem.getStartOffset();

                    // Get the current line's text up to the '}' entered
                    try {
                        doc.getText(start, dot - start, seg);
                    } catch (BadLocationException ble) { // Never happens
                        ExceptionDialog.notifyException(ble);
                        return;
                    }

                    // Only attempt to align if there's only whitespace up to
                    // the '}' entered
                    for (int i = 0; i < seg.count; i++) {
                        char ch = seg.array[seg.offset + i];
                        if (!Character.isWhitespace(ch)) {
                            return;
                        }
                    }

                    // Locate the matching '{' bracket, and replace the leading
                    // whitespace for the '}' to match that of the '{' char's
                    // line
                    bracketInfo = SyntaxUtilities.getMatchingBracketPosition(
                            sta, bracketInfo);
                    if (bracketInfo.y > -1) {
                        try {
                            String ws = SyntaxUtilities.getLeadingWhitespace(
                                    doc, bracketInfo.y);
                            sta.replaceRange(ws, start, dot);
                        } catch (BadLocationException ble) {
                            ExceptionDialog.hideException(ble);
                            return;
                        }
                    }
                }
            } finally {
                if (alignCurlyBraces) {
                    textArea.endAtomicEdit();
                }
            }
        }

        public final String getMacroID() {
            return staCloseCurlyBraceAction;
        }
    }

    /**
     * (Optionally) Completes a closing markup tag.
     * 
     * @author D. Campione
     * 
     */
    public static class CloseMarkupTagAction extends RecordableTextAction {

        private static final long serialVersionUID = 5264707576821516059L;

        public CloseMarkupTagAction() {
            super(staCloseMarkupTagAction);
        }

        public void actionPerformedImpl(ActionEvent e, TextArea textArea) {
            if (!textArea.isEditable() || !textArea.isEnabled()) {
                UIManager.getLookAndFeel().provideErrorFeedback(textArea);
                return;
            }

            SyntaxTextArea sta = (SyntaxTextArea) textArea;
            SyntaxDocument doc = (SyntaxDocument) sta.getDocument();

            Caret c = sta.getCaret();
            boolean selection = c.getDot() != c.getMark();
            sta.replaceSelection("/");

            // Don't automatically complete a tag if there was a selection
            int dot = c.getDot();

            if (doc.getLanguageIsMarkup() && doc.getCompleteMarkupCloseTags()
                    && !selection && sta.getCloseMarkupTags() && dot > 1) {
                try {
                    // Check actual char before token type, since it's quicker
                    char ch = doc.charAt(dot - 2);
                    if (ch == '<' || ch == '[') {

                        Token t = doc.getTokenListForLine(sta
                                .getCaretLineNumber());
                        t = SyntaxUtilities.getTokenAtOffset(t, dot - 1);
                        if (t != null && t.type == Token.MARKUP_TAG_DELIMITER) {
                            String tagName = discoverTagName(doc, dot);
                            if (tagName != null) {
                                sta.replaceSelection(tagName + (char) (ch + 2));
                            }
                        }
                    }
                } catch (BadLocationException ble) { // Never happens
                    UIManager.getLookAndFeel().provideErrorFeedback(sta);
                    ExceptionDialog.notifyException(ble);
                }
            }
        }

        /**
         * Discovers the name of the tag being closed. Assumes standard
         * SGML-style markup tags.
         *
         * @param doc The document to parse.
         * @param dot The location of the caret. This should be right after the
         *            start of a closing tag token (e.g. "<code>&lt;/</code>" or
         *            "<code>[</code>" in the case of BBCode).
         * @return The name of the tag to close, or <code>null</code> if it
         *         could not be determined.
         */
        private String discoverTagName(SyntaxDocument doc, int dot) {
            Stack<String> stack = new Stack<String>();

            Element root = doc.getDefaultRootElement();
            int curLine = root.getElementIndex(dot);

            for (int i = 0; i <= curLine; i++) {
                Token t = doc.getTokenListForLine(i);
                while (t != null && t.isPaintable()) {

                    if (t.type == Token.MARKUP_TAG_DELIMITER) {
                        if (t.isSingleChar('<') || t.isSingleChar('[')) {
                            t = t.getNextToken();
                            while (t != null && t.isPaintable()) {
                                if (t.type == Token.MARKUP_TAG_NAME ||
                                // Being lenient here and also checking for
                                // attributes, in case they (incorrectly) have
                                // whitespace between the '<' char and the
                                // element name.
                                        t.type == Token.MARKUP_TAG_ATTRIBUTE) {
                                    stack.push(t.getLexeme());
                                    break;
                                }
                                t = t.getNextToken();
                            }
                        } else if (t.textCount == 2
                                && t.text[t.textOffset] == '/'
                                && (t.text[t.textOffset + 1] == '>' || t.text[t.textOffset + 1] == ']')) {
                            if (!stack.isEmpty()) { // Always true for valid XML
                                stack.pop();
                            }
                        } else if (t.textCount == 2
                                && (t.text[t.textOffset] == '<' || t.text[t.textOffset] == '[')
                                && t.text[t.textOffset + 1] == '/') {
                            String tagName = null;
                            if (!stack.isEmpty()) { // Always true for valid XML
                                tagName = (String) stack.pop();
                            }
                            if (t.offset + t.textCount >= dot) {
                                return tagName;
                            }
                        }
                    }

                    t = t.getNextToken();
                }
            }

            return null; // Should never happen
        }

        public String getMacroID() {
            return getName();
        }
    }

    /** Collapses all comment folds. */
    public static class CollapseAllCommentFoldsAction extends FoldRelatedAction {

        private static final long serialVersionUID = 5756304038608483906L;

        public CollapseAllCommentFoldsAction() {
            super(staCollapseAllCommentFoldsAction);
            setName("Collapse All Comments");
            setMnemonic("C".charAt(0));
            setShortDescription("Collapses all comment folds.");
        }

        public CollapseAllCommentFoldsAction(String name, Icon icon,
                String desc, Integer mnemonic, KeyStroke accelerator) {
            super(name, icon, desc, mnemonic, accelerator);
        }

        public void actionPerformedImpl(ActionEvent e, TextArea textArea) {
            SyntaxTextArea sta = (SyntaxTextArea) textArea;
            if (sta.isCodeFoldingEnabled()) {
                FoldCollapser collapser = new FoldCollapser();
                collapser.collapseFolds(sta.getFoldManager());
                possiblyRepaintGutter(textArea);
            } else {
                UIManager.getLookAndFeel().provideErrorFeedback(sta);
            }
        }

        public final String getMacroID() {
            return staCollapseAllCommentFoldsAction;
        }
    }

    /** Collapses all folds. */
    public static class CollapseAllFoldsAction extends FoldRelatedAction {

        private static final long serialVersionUID = -5847113039699771731L;

        public CollapseAllFoldsAction() {
            super(staCollapseAllFoldsAction);
            setName("Collapse All Folds");
            setMnemonic("O".charAt(0));
            setShortDescription("Collapses all folds.");
        }

        public CollapseAllFoldsAction(String name, Icon icon, String desc,
                Integer mnemonic, KeyStroke accelerator) {
            super(name, icon, desc, mnemonic, accelerator);
        }

        public void actionPerformedImpl(ActionEvent e, TextArea textArea) {
            SyntaxTextArea sta = (SyntaxTextArea) textArea;
            if (sta.isCodeFoldingEnabled()) {
                FoldCollapser collapser = new FoldCollapser() {
                    public boolean getShouldCollapse(Fold fold) {
                        return true;
                    }
                };
                collapser.collapseFolds(sta.getFoldManager());
                possiblyRepaintGutter(textArea);
            } else {
                UIManager.getLookAndFeel().provideErrorFeedback(sta);
            }
        }

        public final String getMacroID() {
            return staCollapseAllFoldsAction;
        }
    }

    /**
     * Action for copying text as RTF.
     * 
     * @author D. Campione
     * 
     */
    public static class CopyAsRtfAction extends RecordableTextAction {

        private static final long serialVersionUID = 4596159927068005918L;

        public CopyAsRtfAction() {
            super(staCopyAsRtfAction);
        }

        public CopyAsRtfAction(String name, Icon icon, String desc,
                Integer mnemonic, KeyStroke accelerator) {
            super(name, icon, desc, mnemonic, accelerator);
        }

        public void actionPerformedImpl(ActionEvent e, TextArea textArea) {
            ((SyntaxTextArea) textArea).copyAsRtf();
            textArea.requestFocusInWindow();
        }

        public final String getMacroID() {
            return getName();
        }
    }

    /**
     * Action for decreasing the font size of all fonts in the text area.
     * 
     * @author D. Campione
     * 
     */
    public static class DecreaseFontSizeAction
            extends
                TextAreaEditorKit.DecreaseFontSizeAction {

        private static final long serialVersionUID = -8170319818124509819L;

        public DecreaseFontSizeAction() {
            super();
        }

        public DecreaseFontSizeAction(String name, Icon icon, String desc,
                Integer mnemonic, KeyStroke accelerator) {
            super(name, icon, desc, mnemonic, accelerator);
        }

        public void actionPerformedImpl(ActionEvent e, TextArea textArea) {
            SyntaxTextArea sta = (SyntaxTextArea) textArea;
            SyntaxScheme scheme = sta.getSyntaxScheme();

            // All we need to do is update all of the fonts in syntax schemes,
            // then call setSyntaxHighlightingColorScheme with the same scheme
            // already being used. This relies on the fact that that method does
            // not check whether the new scheme is different from the old scheme
            // before updating

            boolean changed = false;
            int count = scheme.getStyleCount();
            for (int i = 0; i < count; i++) {
                Style ss = scheme.getStyle(i);
                if (ss != null) {
                    Font font = ss.font;
                    if (font != null) {
                        float oldSize = font.getSize2D();
                        float newSize = oldSize - decreaseAmount;
                        if (newSize >= MINIMUM_SIZE) {
                            // Shrink by decreaseAmount
                            ss.font = font.deriveFont(newSize);
                            changed = true;
                        } else if (oldSize > MINIMUM_SIZE) {
                            // Can't shrink by full decreaseAmount, but can
                            // shrink a little bit
                            ss.font = font.deriveFont(MINIMUM_SIZE);
                            changed = true;
                        }
                    }
                }
            }

            // Do the text area's font also
            Font font = sta.getFont();
            float oldSize = font.getSize2D();
            float newSize = oldSize - decreaseAmount;
            if (newSize >= MINIMUM_SIZE) {
                // Shrink by decreaseAmount
                sta.setFont(font.deriveFont(newSize));
                changed = true;
            } else if (oldSize > MINIMUM_SIZE) {
                // Can't shrink by full decreaseAmount, but can shrink a little
                // bit
                sta.setFont(font.deriveFont(MINIMUM_SIZE));
                changed = true;
            }

            // If we updated at least one font, update the screen. If all of the
            // fonts were already the minimum size, beep
            if (changed) {
                sta.setSyntaxScheme(scheme);
                // NOTE: This is a hack to get an encompassing TextScrollPane to
                // repaint its line numbers to account for a change in line
                // height due to a font change.
                // setSyntaxHighlightingColorScheme() calls revalidate() which
                // won't repaint the scroll pane if scrollbars don't change,
                // which is why we need this
                Component parent = sta.getParent();
                if (parent instanceof javax.swing.JViewport) {
                    parent = parent.getParent();
                    if (parent instanceof JScrollPane) {
                        parent.repaint();
                    }
                }
            } else
                UIManager.getLookAndFeel().provideErrorFeedback(sta);
        }
    }

    /**
     * Action for when un-indenting lines (either the current line if there is
     * selection, or all selected lines if there is one).
     * 
     * @author D. Campione
     * 
     */
    public static class DecreaseIndentAction extends RecordableTextAction {

        private static final long serialVersionUID = -301096986694097009L;

        private Segment s;

        public DecreaseIndentAction() {
            this(staDecreaseIndentAction);
        }

        public DecreaseIndentAction(String name) {
            super(name);
            s = new Segment();
        }

        public void actionPerformedImpl(ActionEvent e, TextArea textArea) {
            if (!textArea.isEditable() || !textArea.isEnabled()) {
                UIManager.getLookAndFeel().provideErrorFeedback(textArea);
                return;
            }

            Document document = textArea.getDocument();
            Element map = document.getDefaultRootElement();
            Caret c = textArea.getCaret();
            int dot = c.getDot();
            int mark = c.getMark();
            int line1 = map.getElementIndex(dot);
            int tabSize = textArea.getTabSize();

            // If there is a selection, indent all lines in the selection.
            // Otherwise, indent the line the caret is on
            if (dot != mark) {
                // Note that we cheaply reuse variables here, so don't take
                // their names to mean what they are
                int line2 = map.getElementIndex(mark);
                dot = Math.min(line1, line2);
                mark = Math.max(line1, line2);
                Element elem;
                try {
                    for (line1 = dot; line1 < mark; line1++) {
                        elem = map.getElement(line1);
                        handleDecreaseIndent(elem, document, tabSize);
                    }
                    // Don't do the last line if the caret is at its beginning.
                    // We must call getDot() again and not just use 'dot' as the
                    // caret's position may have changed due to the insertion of
                    // the tabs above
                    elem = map.getElement(mark);
                    int start = elem.getStartOffset();
                    if (Math.max(c.getDot(), c.getMark()) != start) {
                        handleDecreaseIndent(elem, document, tabSize);
                    }
                } catch (BadLocationException ble) {
                    UIManager.getLookAndFeel().provideErrorFeedback(textArea);
                    ExceptionDialog.notifyException(ble);
                }
            } else {
                Element elem = map.getElement(line1);
                try {
                    handleDecreaseIndent(elem, document, tabSize);
                } catch (BadLocationException ble) {
                    UIManager.getLookAndFeel().provideErrorFeedback(textArea);
                    ExceptionDialog.notifyException(ble);
                }
            }

        }

        public final String getMacroID() {
            return staDecreaseIndentAction;
        }

        /**
         * Actually does the "de-indentation". This method finds where the given
         * element's leading whitespace ends, then, if there is indeed leading
         * whitespace, removes either the last char in it (if it is a tab), or
         * removes up to the number of spaces equal to a tab in the specified
         * document (i.e., if the tab size was 5 and there were 3 spaces at the
         * end of the leading whitespace, the three will be removed; if there
         * were 8 spaces, only the first 5 would be removed).
         *
         * @param elem The element to "de-indent."
         * @param doc The document containing the specified element.
         * @param tabSize The size of a tab, in spaces.
         */
        private final void handleDecreaseIndent(Element elem, Document doc,
                int tabSize) throws BadLocationException {
            int start = elem.getStartOffset();
            int end = elem.getEndOffset() - 1;
            doc.getText(start, end - start, s);
            int i = s.offset;
            end = i + s.count;
            if (end > i) {
                // If the first character is a tab, remove it
                if (s.array[i] == '\t') {
                    doc.remove(start, 1);
                }
                // Otherwise, see if the first character is a space. If it is,
                // remove all contiguous whitespaces at the beginning of this
                // line, up to the tab size
                else if (s.array[i] == ' ') {
                    i++;
                    int toRemove = 1;
                    while (i < end && s.array[i] == ' ' && toRemove < tabSize) {
                        i++;
                        toRemove++;
                    }
                    doc.remove(start, toRemove);
                }
            }
        }
    }

    /**
     * Deletes the previous word, but differentiates symbols from "words" to
     * match the behavior of code editors.
     * 
     * @author D. Campione
     * 
     */
    public static class DeletePrevWordAction
            extends
                TextAreaEditorKit.DeletePrevWordAction {

        private static final long serialVersionUID = -3722509041510238421L;

        private Segment seg = new Segment();

        protected int getPreviousWordStart(TextArea textArea, int offs)
                throws BadLocationException {

            if (offs == 0) {
                return offs;
            }

            SyntaxDocument doc = (SyntaxDocument) textArea.getDocument();
            int line = textArea.getLineOfOffset(offs);
            int start = textArea.getLineStartOffset(line);
            if (offs == start) {
                return start - 1; // Just delete the newline
            }
            int end = textArea.getLineEndOffset(line);
            if (line != textArea.getLineCount() - 1) {
                end--;
            }
            doc.getText(start, end - start, seg);

            // Determine the "type" of char at offs - lower case, upper case,
            // whitespace or other. We take special care here as we're starting
            // in the middle of the Segment to check whether we're already at
            // the "beginning" of a word
            int firstIndex = seg.getBeginIndex() + (offs - start) - 1;
            seg.setIndex(firstIndex);
            char ch = seg.current();

            // Always strip off whitespace first
            if (Character.isWhitespace(ch)) {
                do {
                    ch = seg.previous();
                } while (Character.isWhitespace(ch));
            }

            // The "word" is a group of letters and/or digits
            if (Character.isLetterOrDigit(ch)) {
                do {
                    ch = seg.previous();
                } while (Character.isLetterOrDigit(ch));
            }

            // The "word" is a series of symbols
            else {
                while (!Character.isWhitespace(ch)
                        && !Character.isLetterOrDigit(ch) && ch != Segment.DONE) {
                    ch = seg.previous();
                }
            }

            if (ch == Segment.DONE) {
                return start; // Removed last "token" of the line
            }
            offs -= firstIndex - seg.getIndex();
            return offs;
        }
    }

    /**
     * Moves the caret to the end of the document, taking into account code
     * folding.
     * 
     * @author D. Campione
     * 
     */
    public static class EndAction extends TextAreaEditorKit.EndAction {

        private static final long serialVersionUID = 1438440461947654626L;

        public EndAction(String name, boolean select) {
            super(name, select);
        }

        protected int getVisibleEnd(TextArea textArea) {
            SyntaxTextArea sta = (SyntaxTextArea) textArea;
            return sta.getLastVisibleOffset();
        }
    }

    /**
     * Positions the caret at the end of the word. This class is here to better
     * handle finding the "end of the word" in programming languages.
     * 
     * @author D. Campione
     * 
     */
    protected static class EndWordAction
            extends
                TextAreaEditorKit.EndWordAction {

        private static final long serialVersionUID = -3860390771911403912L;

        private Segment seg;

        protected EndWordAction(String name, boolean select) {
            super(name, select);
            seg = new Segment();
        }

        protected int getWordEnd(TextArea textArea, int offs)
                throws BadLocationException {

            SyntaxDocument doc = (SyntaxDocument) textArea.getDocument();
            if (offs == doc.getLength()) {
                return offs;
            }

            int line = textArea.getLineOfOffset(offs);
            int end = textArea.getLineEndOffset(line);
            if (line != textArea.getLineCount() - 1) {
                end--; // Hide newline
            }
            if (offs == end) {
                return end;
            }
            doc.getText(offs, end - offs, seg);

            // Determine the "type" of char at offs - letter/digit, whitespace
            // or other
            char ch = seg.first();

            // The "word" is a group of letters and/or digits
            if (Character.isLetterOrDigit(ch)) {
                do {
                    ch = seg.next();
                } while (Character.isLetterOrDigit(ch));
            }

            // The "word" is whitespace
            else if (Character.isWhitespace(ch)) {

                do {
                    ch = seg.next();
                } while (Character.isWhitespace(ch));
            }

            // Otherwise, the "word" is a single character of some other type
            // (operator, etc.)

            offs += seg.getIndex() - seg.getBeginIndex();
            return offs;
        }
    }

    /** Expands all folds. */
    public static class ExpandAllFoldsAction extends FoldRelatedAction {

        private static final long serialVersionUID = -2098243894401554680L;

        public ExpandAllFoldsAction() {
            super(staExpandAllFoldsAction);
            setName("Expand All Folds");
            setMnemonic("E".charAt(0));
            setShortDescription("Expands all folds.");
        }

        public ExpandAllFoldsAction(String name, Icon icon, String desc,
                Integer mnemonic, KeyStroke accelerator) {
            super(name, icon, desc, mnemonic, accelerator);
        }

        public void actionPerformedImpl(ActionEvent e, TextArea textArea) {
            SyntaxTextArea sta = (SyntaxTextArea) textArea;
            if (sta.isCodeFoldingEnabled()) {
                FoldManager fm = sta.getFoldManager();
                for (int i = 0; i < fm.getFoldCount(); i++) {
                    expand(fm.getFold(i));
                }
                possiblyRepaintGutter(sta);
            } else {
                UIManager.getLookAndFeel().provideErrorFeedback(sta);
            }
        }

        private void expand(Fold fold) {
            fold.setCollapsed(false);
            for (int i = 0; i < fold.getChildCount(); i++) {
                expand(fold.getChild(i));
            }
        }

        public final String getMacroID() {
            return staExpandAllFoldsAction;
        }
    }

    /** Base class for folding-related actions. */
    static abstract class FoldRelatedAction extends RecordableTextAction {

        private static final long serialVersionUID = 1326629144290057256L;

        public FoldRelatedAction(String name) {
            super(name);
        }

        public FoldRelatedAction(String name, Icon icon, String desc,
                Integer mnemonic, KeyStroke accelerator) {
            super(name, icon, desc, mnemonic, accelerator);
        }

        protected Fold getClosestFold(SyntaxTextArea textArea) {
            int offs = textArea.getCaretPosition();
            int line = textArea.getCaretLineNumber();
            FoldManager fm = textArea.getFoldManager();
            Fold fold = fm.getFoldForLine(line);
            if (fold == null) {
                fold = fm.getDeepestOpenFoldContaining(offs);
            }
            return fold;
        }

        /**
         * Repaints the gutter in a text area's scroll pane, if necessary.
         *
         * @param textArea The text area.
         */
        protected void possiblyRepaintGutter(TextArea textArea) {
            Gutter gutter = SyntaxUtilities.getGutter(textArea);
            if (gutter != null) {
                gutter.repaint();
            }
        }
    }

    /**
     * Action for moving the caret to the "matching bracket" of the bracket at
     * the caret position (either before or after).
     * 
     * @author D. Campione
     * 
     */
    public static class GoToMatchingBracketAction extends RecordableTextAction {

        private static final long serialVersionUID = 8112036198256912568L;

        private Point bracketInfo;

        public GoToMatchingBracketAction() {
            super(staGoToMatchingBracketAction);
        }

        public GoToMatchingBracketAction(String name, Icon icon, String desc,
                Integer mnemonic, KeyStroke accelerator) {
            super(name, icon, desc, mnemonic, accelerator);
        }

        public void actionPerformedImpl(ActionEvent e, TextArea textArea) {
            SyntaxTextArea sta = (SyntaxTextArea) textArea;
            bracketInfo = SyntaxUtilities.getMatchingBracketPosition(sta,
                    bracketInfo);
            if (bracketInfo.y > -1) {
                // Go to the position AFTER the bracket so the previous bracket
                // (which we were just on) is highlighted
                sta.setCaretPosition(bracketInfo.y + 1);
            } else {
                UIManager.getLookAndFeel().provideErrorFeedback(sta);
            }
        }

        public final String getMacroID() {
            return staGoToMatchingBracketAction;
        }
    }

    /**
     * Action for increasing the font size of all fonts in the text area.
     * 
     * @author D. Campione
     * 
     */
    public static class IncreaseFontSizeAction
            extends
                TextAreaEditorKit.IncreaseFontSizeAction {

        private static final long serialVersionUID = -4731681872750673857L;

        public IncreaseFontSizeAction() {
            super();
        }

        public IncreaseFontSizeAction(String name, Icon icon, String desc,
                Integer mnemonic, KeyStroke accelerator) {
            super(name, icon, desc, mnemonic, accelerator);
        }

        public void actionPerformedImpl(ActionEvent e, TextArea textArea) {

            SyntaxTextArea sta = (SyntaxTextArea) textArea;
            SyntaxScheme scheme = sta.getSyntaxScheme();

            // All we need to do is update all of the fonts in syntax schemes,
            // then call setSyntaxHighlightingColorScheme with the same scheme
            // already being used. This relies on the fact that that method does
            // not check whether the new scheme is different from the old scheme
            // before updating.

            boolean changed = false;
            int count = scheme.getStyleCount();
            for (int i = 0; i < count; i++) {
                Style ss = scheme.getStyle(i);
                if (ss != null) {
                    Font font = ss.font;
                    if (font != null) {
                        float oldSize = font.getSize2D();
                        float newSize = oldSize + increaseAmount;
                        if (newSize <= MAXIMUM_SIZE) {
                            // Grow by increaseAmount
                            ss.font = font.deriveFont(newSize);
                            changed = true;
                        } else if (oldSize < MAXIMUM_SIZE) {
                            // Can't grow by full increaseAmount, but can grow a
                            // little bit
                            ss.font = font.deriveFont(MAXIMUM_SIZE);
                            changed = true;
                        }
                    }
                }
            }

            // Do the text area's font also
            Font font = sta.getFont();
            float oldSize = font.getSize2D();
            float newSize = oldSize + increaseAmount;
            if (newSize <= MAXIMUM_SIZE) {
                // Grow by increaseAmount
                sta.setFont(font.deriveFont(newSize));
                changed = true;
            } else if (oldSize < MAXIMUM_SIZE) {
                // Can't grow by full increaseAmount, but can grow a little bit
                sta.setFont(font.deriveFont(MAXIMUM_SIZE));
                changed = true;
            }

            // If we updated at least one font, update the screen. If all of the
            // fonts were already the minimum size, beep
            if (changed) {
                sta.setSyntaxScheme(scheme);
                // NOTE:  This is a hack to get an encompassing TextScrollPane
                // to repaint its line numbers to account for a change in line
                // height due to a font change
                Component parent = sta.getParent();
                if (parent instanceof javax.swing.JViewport) {
                    parent = parent.getParent();
                    if (parent instanceof JScrollPane) {
                        parent.repaint();
                    }
                }
            } else
                UIManager.getLookAndFeel().provideErrorFeedback(sta);
        }
    }

    /**
     * Action for when the user presses the Enter key. This is here so we can be
     * smart and "auto-indent" for programming languages.
     * 
     * @author D. Campione
     * 
     */
    public static class InsertBreakAction
            extends
                TextAreaEditorKit.InsertBreakAction {

        private static final long serialVersionUID = 4504481252627624845L;

        public void actionPerformedImpl(ActionEvent e, TextArea textArea) {

            if (!textArea.isEditable() || !textArea.isEnabled()) {
                UIManager.getLookAndFeel().provideErrorFeedback(textArea);
                return;
            }

            SyntaxTextArea sta = (SyntaxTextArea) textArea;
            boolean noSelection = sta.getSelectionStart() == sta
                    .getSelectionEnd();

            // First, see if this language wants to handle inserting newlines
            // itself
            boolean handled = false;
            if (noSelection) {
                SyntaxDocument doc = (SyntaxDocument) sta.getDocument();
                handled = doc.insertBreakSpecialHandling(e);
            }

            // If not..
            if (!handled) {
                handleInsertBreak(sta, noSelection);
            }

        }

        /**
         * @return The first location in the string past <code>pos</code> that
         *         is NOT a whitespace char, or <code>-1</code> if only
         *         whitespace chars follow <code>pos</code> (or it is the end
         *         position in the string).
         */
        private static final int atEndOfLine(int pos, String s, int sLen) {
            for (int i = pos; i < sLen; i++) {
                if (!SyntaxUtilities.isWhitespace(s.charAt(i)))
                    return i;
            }
            return -1;
        }

        private static final int getOpenBraceCount(SyntaxDocument doc) {
            int openCount = 0;
            Element root = doc.getDefaultRootElement();
            int lineCount = root.getElementCount();
            for (int i = 0; i < lineCount; i++) {
                Token t = doc.getTokenListForLine(i);
                while (t != null && t.isPaintable()) {
                    if (t.type == Token.SEPARATOR && t.textCount == 1) {
                        char ch = t.text[t.textOffset];
                        if (ch == '{') {
                            openCount++;
                        } else if (ch == '}') {
                            openCount--;
                        }
                    }
                    t = t.getNextToken();
                }
            }
            return openCount;
        }

        /**
         * Actually inserts the newline into the document, and auto-indents if
         * appropriate. This method can be called by token makers who implement
         * a custom action for inserting newlines.
         *
         * @param textArea
         * @param noSelection Whether there is no selection.
         */
        protected void handleInsertBreak(SyntaxTextArea textArea,
                boolean noSelection) {
            // If we're auto-indenting..
            if (noSelection && textArea.isAutoIndentEnabled()) {
                insertNewlineWithAutoIndent(textArea);
            } else {
                textArea.replaceSelection("\n");
                if (noSelection) {
                    possiblyCloseCurlyBrace(textArea, null);
                }
            }
        }

        private void insertNewlineWithAutoIndent(SyntaxTextArea sta) {
            try {
                int caretPos = sta.getCaretPosition();
                Document doc = sta.getDocument();
                Element map = doc.getDefaultRootElement();
                int lineNum = map.getElementIndex(caretPos);
                Element line = map.getElement(lineNum);
                int start = line.getStartOffset();
                int end = line.getEndOffset() - 1;
                int len = end - start;
                String s = doc.getText(start, len);

                // endWS is the end of the leading whitespace of the current
                // line
                String leadingWS = SyntaxUtilities.getLeadingWhitespace(s);
                StringBuffer sb = new StringBuffer("\n");
                sb.append(leadingWS);

                // If there is only whitespace between the caret and the EOL,
                // pressing Enter auto-indents the new line to the same place as
                // the previous line
                int nonWhitespacePos = atEndOfLine(caretPos - start, s, len);
                if (nonWhitespacePos == -1) {
                    if (leadingWS.length() == len
                            && sta.isClearWhitespaceLinesEnabled()) {
                        // If the line was nothing but whitespace, select it so
                        // its contents get removed
                        sta.setSelectionStart(start);
                        sta.setSelectionEnd(end);
                    }
                    sta.replaceSelection(sb.toString());
                }

                // If there is non-whitespace between the caret and the EOL,
                // pressing Enter takes that text to the next line and
                // auto-indents it to the same place as the last line
                else {
                    sb.append(s.substring(nonWhitespacePos));
                    sta.replaceRange(sb.toString(), caretPos, end);
                    sta.setCaretPosition(caretPos + leadingWS.length() + 1);
                }

                // Must do it after everything else, as the "smart indent"
                // calculation depends on the previous line's state AFTER the
                // Enter press (stuff may have been moved down)
                if (sta.getShouldIndentNextLine(lineNum)) {
                    sta.replaceSelection("\t");
                }

                possiblyCloseCurlyBrace(sta, leadingWS);

            } catch (BadLocationException ble) { // Never happens
                sta.replaceSelection("\n");
                ExceptionDialog.notifyException(ble);
            }
        }

        private void possiblyCloseCurlyBrace(SyntaxTextArea textArea,
                String leadingWS) {
            SyntaxDocument doc = (SyntaxDocument) textArea.getDocument();

            if (textArea.getCloseCurlyBraces()
                    && doc.getCurlyBracesDenoteCodeBlocks()) {

                int line = textArea.getCaretLineNumber();
                Token t = doc.getTokenListForLine(line - 1);
                t = t.getLastNonCommentNonWhitespaceToken();

                if (t != null && t.isLeftCurly()) {

                    if (getOpenBraceCount(doc) > 0) {
                        StringBuffer sb = new StringBuffer();
                        if (line == textArea.getLineCount() - 1) {
                            sb.append('\n');
                        }
                        if (leadingWS != null) {
                            sb.append(leadingWS);
                        }
                        sb.append("}\n");
                        int dot = textArea.getCaretPosition();
                        int end = textArea.getLineEndOffsetOfCurrentLine();
                        // Insert at end of line, not at dot: they may have
                        // pressed Enter in the middle of the line and brought
                        // some text (though it must be whitespace and/or
                        // comments) down onto the new line
                        textArea.insert(sb.toString(), end);
                        textArea.setCaretPosition(dot); // Caret may have moved
                    }
                }
            }
        }
    }

    /**
     * Action for inserting tabs. This is extended to "block indent" a group of
     * contiguous lines if they are selected.
     * 
     * @author D. Campione
     * 
     */
    public static class InsertTabAction extends RecordableTextAction {

        private static final long serialVersionUID = -1025902798452187126L;

        public InsertTabAction() {
            super(insertTabAction);
        }

        public InsertTabAction(String name) {
            super(name);
        }

        public void actionPerformedImpl(ActionEvent e, TextArea textArea) {
            if (!textArea.isEditable() || !textArea.isEnabled()) {
                UIManager.getLookAndFeel().provideErrorFeedback(textArea);
                return;
            }

            Document document = textArea.getDocument();
            Element map = document.getDefaultRootElement();
            Caret c = textArea.getCaret();
            int dot = c.getDot();
            int mark = c.getMark();
            int dotLine = map.getElementIndex(dot);
            int markLine = map.getElementIndex(mark);

            // If there is a multi-line selection, indent all lines in the
            // selection
            if (dotLine != markLine) {
                int first = Math.min(dotLine, markLine);
                int last = Math.max(dotLine, markLine);
                Element elem;
                int start;

                // Since we're using Document.insertString(), we must mimic the
                // soft tab behavior provided by TextArea.replaceSelection()
                String replacement = "\t";
                if (textArea.getTabsEmulated()) {
                    StringBuffer sb = new StringBuffer();
                    int temp = textArea.getTabSize();
                    for (int i = 0; i < temp; i++) {
                        sb.append(' ');
                    }
                    replacement = sb.toString();
                }

                textArea.beginAtomicEdit();
                try {
                    for (int i = first; i < last; i++) {
                        elem = map.getElement(i);
                        start = elem.getStartOffset();
                        document.insertString(start, replacement, null);
                    }
                    // Don't do the last line if the caret is at its beginning.
                    // We must call getDot() again and not just use 'dot' as the
                    // caret's position may have changed due to the insertion of
                    // the tabs above
                    elem = map.getElement(last);
                    start = elem.getStartOffset();
                    if (Math.max(c.getDot(), c.getMark()) != start) {
                        document.insertString(start, replacement, null);
                    }
                } catch (BadLocationException ble) { // Never happens
                    UIManager.getLookAndFeel().provideErrorFeedback(textArea);
                    ExceptionDialog.notifyException(ble);
                } finally {
                    textArea.endAtomicEdit();
                }
            } else {
                textArea.replaceSelection("\t");
            }
        }

        public final String getMacroID() {
            return insertTabAction;
        }
    }

    /**
     * Action to move the selection and/or caret. Constructor indicates
     * direction to use. This class overrides the behavior defined in {@link
     * TextAreaEditorKit} to better skip "words" in source code.
     * 
     * @author D. Campione
     * 
     */
    public static class NextWordAction extends TextAreaEditorKit.NextWordAction {

        private static final long serialVersionUID = -73979318608886742L;

        private Segment seg;

        public NextWordAction(String nm, boolean select) {
            super(nm, select);
            seg = new Segment();
        }

        /** Overridden to do better with skipping "words" in code. */
        protected int getNextWord(TextArea textArea, int offs)
                throws BadLocationException {
            SyntaxDocument doc = (SyntaxDocument) textArea.getDocument();
            if (offs == doc.getLength()) {
                return offs;
            }

            Element root = doc.getDefaultRootElement();
            int line = root.getElementIndex(offs);
            int end = root.getElement(line).getEndOffset() - 1;
            if (offs == end) { // If we're already at the end of the line..
                SyntaxTextArea sta = (SyntaxTextArea) textArea;
                if (sta.isCodeFoldingEnabled()) { // Start of next visible line
                    FoldManager fm = sta.getFoldManager();
                    int lineCount = root.getElementCount();
                    while (++line < lineCount && fm.isLineHidden(line));
                    if (line < lineCount) { // Found a lower visible line
                        offs = root.getElement(line).getStartOffset();
                    }
                    // No lower visible line - we're already at last visible offset
                    return offs;
                } else {
                    return offs + 1; // Start of next line
                }
            }
            doc.getText(offs, end - offs, seg);

            // Determine the "type" of char at offs - letter/digit, whitespace
            // or other
            char ch = seg.first();

            // Skip the group of letters and/or digits
            if (Character.isLetterOrDigit(ch)) {
                do {
                    ch = seg.next();
                } while (Character.isLetterOrDigit(ch));
            }

            // Skip groups of "anything else" (operators, etc.)
            else if (!Character.isWhitespace(ch)) {
                do {
                    ch = seg.next();
                } while (ch != Segment.DONE
                        && !(Character.isLetterOrDigit(ch) || Character
                                .isWhitespace(ch)));
            }

            // Skip any trailing whitespace
            while (Character.isWhitespace(ch)) {
                ch = seg.next();
            }

            offs += seg.getIndex() - seg.getBeginIndex();
            return offs;
        }
    }

    /**
     * Action for when the user tries to insert a template (that is, they've
     * typed a template ID and pressed the trigger character (a space) in an
     * attempt to do the substitution).
     * 
     * @author D. Campione
     * 
     */
    public static class PossiblyInsertTemplateAction
            extends
                RecordableTextAction {

        private static final long serialVersionUID = 1288772638117428457L;

        public PossiblyInsertTemplateAction() {
            super(staPossiblyInsertTemplateAction);
        }

        public void actionPerformedImpl(ActionEvent e, TextArea textArea) {
            if (!textArea.isEditable() || !textArea.isEnabled())
                return;

            SyntaxTextArea sta = (SyntaxTextArea) textArea;

            if (SyntaxTextArea.getTemplatesEnabled()) {
                Document doc = textArea.getDocument();
                if (doc != null) {
                    try {
                        CodeTemplateManager manager = SyntaxTextArea
                                .getCodeTemplateManager();
                        ICodeTemplate template = manager == null
                                ? null
                                : manager.getTemplate(sta);

                        // A non-null template means modify the text to insert
                        if (template != null) {
                            template.invoke(sta);
                        }

                        // No template - insert default text. This is exactly
                        // what DefaultKeyTypedAction does
                        else {
                            doDefaultInsert(sta);
                        }
                    } catch (BadLocationException ble) {
                        UIManager.getLookAndFeel().provideErrorFeedback(
                                textArea);
                    }
                } // End of if (doc!=null)
            } // End of if (textArea.getTemplatesEnabled())
            else { // If templates aren't enabled, just insert the text as usual
                doDefaultInsert(sta);
            }

        }

        private final void doDefaultInsert(TextArea textArea) {
            textArea.replaceSelection(" ");
        }

        public final String getMacroID() {
            return staPossiblyInsertTemplateAction;
        }
    }

    /**
     * Action to move the selection and/or caret. Constructor indicates
     * direction to use. This class overrides the behavior defined in {@link
     * TextAreaEditorKit} to better skip "words" in source code.
     * 
     * @author D. Campione
     * 
     */
    public static class PreviousWordAction
            extends
                TextAreaEditorKit.PreviousWordAction {

        private static final long serialVersionUID = 8745518206677338482L;

        private Segment seg;

        public PreviousWordAction(String nm, boolean select) {
            super(nm, select);
            seg = new Segment();
        }

        /** Overridden to do better with skipping "words" in code. */
        protected int getPreviousWord(TextArea textArea, int offs)
                throws BadLocationException {
            if (offs == 0) {
                return offs;
            }

            SyntaxDocument doc = (SyntaxDocument) textArea.getDocument();
            Element root = doc.getDefaultRootElement();
            int line = root.getElementIndex(offs);
            int start = root.getElement(line).getStartOffset();
            if (offs == start) { // If we're already at the start of the line..
                SyntaxTextArea sta = (SyntaxTextArea) textArea;
                if (sta.isCodeFoldingEnabled()) { // End of next visible line
                    FoldManager fm = sta.getFoldManager();
                    while (--line >= 0 && fm.isLineHidden(line));
                    if (line >= 0) { // Found an earlier visible line
                        offs = root.getElement(line).getEndOffset() - 1;
                    }
                    // No earlier visible line - we must be at offs==0..
                    return offs;
                } else {
                    return start - 1; // End of previous line
                }
            }
            doc.getText(start, offs - start, seg);

            // Determine the "type" of char at offs - lower case, upper case,
            // whitespace or other
            char ch = seg.last();

            // Skip any "leading" whitespace
            while (Character.isWhitespace(ch)) {
                ch = seg.previous();
            }

            // Skip the group of letters and/or digits
            if (Character.isLetterOrDigit(ch)) {
                do {
                    ch = seg.previous();
                } while (Character.isLetterOrDigit(ch));
            }

            // Skip groups of "anything else" (operators, etc.)
            else if (!Character.isWhitespace(ch)) {
                do {
                    ch = seg.previous();
                } while (ch != Segment.DONE
                        && !(Character.isLetterOrDigit(ch) || Character
                                .isWhitespace(ch)));
            }

            offs -= seg.getEndIndex() - seg.getIndex();
            if (ch != Segment.DONE) {
                offs++;
            }

            return offs;
        }
    }

    /**
     * Selects the word around the caret. This class is here to better handle
     * selecting "words" in programming languages.
     * 
     * @author D. Campione
     * 
     */
    public static class SelectWordAction
            extends
                TextAreaEditorKit.SelectWordAction {

        private static final long serialVersionUID = -6969112358471092026L;

        protected void createActions() {
            start = new BeginWordAction("pigdog", false);
            end = new EndWordAction("pigdog", true);
        }
    }

    /**
     * Action that toggles whether the currently selected lines are
     * commented.
     */
    public static class ToggleCommentAction extends RecordableTextAction {

        private static final long serialVersionUID = -7331129228472571101L;

        public ToggleCommentAction() {
            super(staToggleCommentAction);
        }

        public void actionPerformedImpl(ActionEvent e, TextArea textArea) {
            if (!textArea.isEditable() || !textArea.isEnabled()) {
                UIManager.getLookAndFeel().provideErrorFeedback(textArea);
                return;
            }

            SyntaxDocument doc = (SyntaxDocument) textArea.getDocument();
            String[] startEnd = doc.getLineCommentStartAndEnd();

            if (startEnd == null) {
                UIManager.getLookAndFeel().provideErrorFeedback(textArea);
                return;
            }

            Element map = doc.getDefaultRootElement();
            Caret c = textArea.getCaret();
            int dot = c.getDot();
            int mark = c.getMark();
            int line1 = map.getElementIndex(dot);
            int line2 = map.getElementIndex(mark);
            int start = Math.min(line1, line2);
            int end = Math.max(line1, line2);

            // Don't toggle comment on last line if there is no text selected on
            // it
            if (start != end) {
                Element elem = map.getElement(end);
                if (Math.max(dot, mark) == elem.getStartOffset()) {
                    end--;
                }
            }

            textArea.beginAtomicEdit();
            try {
                boolean add = getDoAdd(doc, map, start, end, startEnd);
                for (line1 = start; line1 <= end; line1++) {
                    Element elem = map.getElement(line1);
                    handleToggleComment(elem, doc, startEnd, add);
                }
            } catch (BadLocationException ble) {
                ExceptionDialog.hideException(ble);
                UIManager.getLookAndFeel().provideErrorFeedback(textArea);
            } finally {
                textArea.endAtomicEdit();
            }
        }

        private boolean getDoAdd(Document doc, Element map, int startLine,
                int endLine, String[] startEnd) throws BadLocationException {
            boolean doAdd = false;
            for (int i = startLine; i <= endLine; i++) {
                Element elem = map.getElement(i);
                int start = elem.getStartOffset();
                String t = doc.getText(start, elem.getEndOffset() - start - 1);
                if (!t.startsWith(startEnd[0])
                        || (startEnd[1] != null && !t.endsWith(startEnd[1]))) {
                    doAdd = true;
                    break;
                }
            }
            return doAdd;
        }

        private void handleToggleComment(Element elem, Document doc,
                String[] startEnd, boolean add) throws BadLocationException {
            int start = elem.getStartOffset();
            int end = elem.getEndOffset() - 1;
            if (add) {
                doc.insertString(start, startEnd[0], null);
                if (startEnd[1] != null) {
                    doc.insertString(end + startEnd[0].length(), startEnd[1],
                            null);
                }
            } else {
                doc.remove(start, startEnd[0].length());
                if (startEnd[1] != null) {
                    int temp = startEnd[1].length();
                    doc.remove(end - startEnd[0].length() - temp, temp);
                }
            }
        }

        public final String getMacroID() {
            return staToggleCommentAction;
        }
    }

    /** Toggles the fold at the current caret position or line. */
    public static class ToggleCurrentFoldAction extends FoldRelatedAction {

        private static final long serialVersionUID = -7653537898747779090L;

        public ToggleCurrentFoldAction() {
            super(staToggleCurrentFoldAction);
            setName("Toggle Current Fold");
            setMnemonic("F".charAt(0));
            setShortDescription("Toggles the fold at the caret position.");
        }

        public ToggleCurrentFoldAction(String name, Icon icon, String desc,
                Integer mnemonic, KeyStroke accelerator) {
            super(name, icon, desc, mnemonic, accelerator);
        }

        public void actionPerformedImpl(ActionEvent e, TextArea textArea) {
            SyntaxTextArea sta = (SyntaxTextArea) textArea;
            if (sta.isCodeFoldingEnabled()) {
                Fold fold = getClosestFold(sta);
                if (fold != null) {
                    fold.toggleCollapsedState();
                }
                possiblyRepaintGutter(textArea);
            } else {
                UIManager.getLookAndFeel().provideErrorFeedback(sta);
            }
        }

        public final String getMacroID() {
            return staToggleCurrentFoldAction;
        }
    }
}