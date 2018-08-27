/*
 * Open Teradata Viewer ( editor syntax folding )
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

package net.sourceforge.open_teradata_viewer.editor.syntax.folding;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.BadLocationException;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * Fold parser for XML. Any tags that span more than one line, as well as
 * comment regions spanning more than one line, are identified as foldable
 * regions.
 *
 * @author D. Campione
 * 
 */
public class XmlFoldParser implements IFoldParser {

    private static final char[] MARKUP_CLOSING_TAG_START = { '<', '/' };
    private static final char[] MARKUP_SHORT_TAG_END = { '/', '>' };
    private static final char[] MLC_END = { '-', '-', '>' };

    /** {@inheritDoc} */
    @Override
    public List<Fold> getFolds(SyntaxTextArea textArea) {
        List<Fold> folds = new ArrayList<Fold>();

        Fold currentFold = null;
        int lineCount = textArea.getLineCount();
        boolean inMLC = false;
        int mlcStart = 0;

        try {
            for (int line = 0; line < lineCount; line++) {
                IToken t = textArea.getTokenListForLine(line);

                while (t != null && t.isPaintable()) {
                    if (t.isComment()) {
                        // Continuing an MLC from a previous line
                        if (inMLC) {
                            // Found the end of the MLC starting on a previous line..
                            if (t.endsWith(MLC_END)) {
                                int mlcEnd = t.getEndOffset() - 1;
                                if (currentFold == null) {
                                    currentFold = new Fold(IFoldType.COMMENT,
                                            textArea, mlcStart);
                                    currentFold.setEndOffset(mlcEnd);
                                    folds.add(currentFold);
                                    currentFold = null;
                                } else {
                                    currentFold = currentFold.createChild(
                                            IFoldType.COMMENT, mlcStart);
                                    currentFold.setEndOffset(mlcEnd);
                                    currentFold = currentFold.getParent();
                                }
                                inMLC = false;
                                mlcStart = 0;
                            }
                            // Otherwise, this MLC is continuing on to yet
                            // another line
                        } else {
                            // If we're an MLC that ends on a later line..
                            if (t.getType() == IToken.COMMENT_MULTILINE
                                    && !t.endsWith(MLC_END)) {
                                inMLC = true;
                                mlcStart = t.getOffset();
                            }
                        }
                    } else if (t.isSingleChar(IToken.MARKUP_TAG_DELIMITER, '<')) {
                        if (currentFold == null) {
                            currentFold = new Fold(IFoldType.CODE, textArea,
                                    t.getOffset());
                            folds.add(currentFold);
                        } else {
                            currentFold = currentFold.createChild(
                                    IFoldType.CODE, t.getOffset());
                        }
                    } else if (t.is(IToken.MARKUP_TAG_DELIMITER,
                            MARKUP_SHORT_TAG_END)) {
                        if (currentFold != null) {
                            Fold parentFold = currentFold.getParent();
                            removeFold(currentFold, folds);
                            currentFold = parentFold;
                        }
                    } else if (t.is(IToken.MARKUP_TAG_DELIMITER,
                            MARKUP_CLOSING_TAG_START)) {
                        if (currentFold != null) {
                            currentFold.setEndOffset(t.getOffset());
                            Fold parentFold = currentFold.getParent();
                            // Don't add fold markers for single-line blocks
                            if (currentFold.isOnSingleLine()) {
                                removeFold(currentFold, folds);
                            }
                            currentFold = parentFold;
                        }
                    }

                    t = t.getNextToken();
                }
            }
        } catch (BadLocationException ble) {
            ExceptionDialog.notifyException(ble); // Should never happen
        }

        return folds;
    }

    /**
     * If this fold has a parent fold, this method removes it from its parent.
     * Otherwise, it's assumed to be the most recent (top-level) fold in the
     * <code>folds</code> list, and is removed from that.
     *
     * @param fold The fold to remove.
     * @param folds The list of top-level folds.
     */
    private static final void removeFold(Fold fold, List<Fold> folds) {
        if (!fold.removeFromParent()) {
            folds.remove(folds.size() - 1);
        }
    }
}