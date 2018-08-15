/*
 * Open Teradata Viewer ( editor syntax folding )
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

package net.sourceforge.open_teradata_viewer.editor.syntax.folding;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.BadLocationException;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.ITokenMaker;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * A basic fold parser that can be used for languages such as C, that use
 * curly braces to denote code blocks. This parser searches for curly brace
 * pairs and creates code folds out of them. It can also optionally find C-style
 * multi-line comments ("<code>/* ... *&#47;</code>") and make them foldable as
 * well.<p>
 * 
 * This parser knows nothing about language semantics; it uses
 * <code>SyntaxTextArea</code>'s syntax highlighting tokens to identify curly
 * braces. By default, it looks for single-char tokens of type
 * {@link Token#SEPARATOR}, with lexemes '<code>{</code>' or '<code>}</code>'.
 * If your {@link ITokenMaker} uses a different token type for curly braces, you
 * should override the {@link #isLeftCurly(Token)} and
 * {@link #isRightCurly(Token)} methods with your own definitions. In theory,
 * you could extend this fold parser to parse languages that use completely
 * different tokens than curly braces to denote foldable regions by overriding
 * those two methods.<p>
 *
 * Note also that this class may impose somewhat of a performance penalty on
 * large source files, since it re-parses the entire document each time folds
 * are reevaluated.
 *
 * @author D. Campione
 * 
 */
public class CurlyFoldParser implements IFoldParser {

    /**
     * Whether to scan for C-style multi-line comments and make them foldable.
     */
    private boolean foldableMultiLineComments;

    /** Whether this parser is folding Java. */
    private final boolean java;

    /** Used to find import statements when folding Java code. */
    private static final char[] KEYWORD_IMPORT = "import".toCharArray();

    /** Ending of a multi-line comment in C, C++, Java, etc.. */
    protected static final char[] C_MLC_END = "*/".toCharArray();

    /**
     * Creates a fold parser that identifies foldable regions via curly braces
     * as well as C-style multi-line comments.
     */
    public CurlyFoldParser() {
        this(true, false);
    }

    /**
     * Ctor.
     *
     * @param cStyleMultiLineComments Whether to scan for C-style multi-line
     *                                comments and make them foldable.
     * @param java Whether this parser is folding Java. This adds extra parsing
     *             rules, such as grouping all import statements into a fold
     *             section.
     */
    public CurlyFoldParser(boolean cStyleMultiLineComments, boolean java) {
        this.foldableMultiLineComments = cStyleMultiLineComments;
        this.java = java;
    }

    /**
     * Returns whether multi-line comments are foldable with this parser.
     *
     * @return Whether multi-line comments are foldable.
     * @see #setFoldableMultiLineComments(boolean)
     */
    public boolean getFoldableMultiLineComments() {
        return foldableMultiLineComments;
    }

    /** {@inheritDoc} */
    @Override
    public List<Fold> getFolds(SyntaxTextArea textArea) {
        List<Fold> folds = new ArrayList<Fold>();

        Fold currentFold = null;
        int lineCount = textArea.getLineCount();
        boolean inMLC = false;
        int mlcStart = 0;
        int importStartLine = -1;
        int lastSeenImportLine = -1;
        int importGroupStartOffs = -1;
        int importGroupEndOffs = -1;

        try {
            for (int line = 0; line < lineCount; line++) {
                IToken t = textArea.getTokenListForLine(line);
                while (t != null && t.isPaintable()) {
                    if (getFoldableMultiLineComments() && t.isComment()) {
                        // Java-specific stuff
                        if (java) {
                            if (importStartLine > -1) {
                                if (lastSeenImportLine > importStartLine) {
                                    Fold fold = null;
                                    // Any imports found *should* be a top-level fold,
                                    // but we're extra lenient here and allow groups
                                    // of them anywhere to keep our parser better-behaved
                                    // if they have random "imports" throughout code
                                    if (currentFold == null) {
                                        fold = new Fold(IFoldType.IMPORTS,
                                                textArea, importGroupStartOffs);
                                        folds.add(fold);
                                    } else {
                                        fold = currentFold.createChild(
                                                IFoldType.IMPORTS,
                                                importGroupStartOffs);
                                    }
                                    fold.setEndOffset(importGroupEndOffs);
                                }
                                importStartLine = lastSeenImportLine = importGroupStartOffs = importGroupEndOffs = -1;
                            }
                        }

                        if (inMLC) {
                            // If we found the end of an MLC that started on
                            // a previous line..
                            if (t.endsWith(C_MLC_END)) {
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
                            if (t.getType() != IToken.COMMENT_EOL
                                    && !t.endsWith(C_MLC_END)) {
                                inMLC = true;
                                mlcStart = t.getOffset();
                            }
                        }
                    } else if (isLeftCurly(t)) {
                        // Java-specific stuff
                        if (java) {
                            if (importStartLine > -1) {
                                if (lastSeenImportLine > importStartLine) {
                                    Fold fold = null;
                                    // Any imports found *should* be a top-level fold,
                                    // but we're extra lenient here and allow groups
                                    // of them anywhere to keep our parser better-behaved
                                    // if they have random "imports" throughout code
                                    if (currentFold == null) {
                                        fold = new Fold(IFoldType.IMPORTS,
                                                textArea, importGroupStartOffs);
                                        folds.add(fold);
                                    } else {
                                        fold = currentFold.createChild(
                                                IFoldType.IMPORTS,
                                                importGroupStartOffs);
                                    }
                                    fold.setEndOffset(importGroupEndOffs);
                                }
                                importStartLine = lastSeenImportLine = importGroupStartOffs = importGroupEndOffs = -1;
                            }
                        }

                        if (currentFold == null) {
                            currentFold = new Fold(IFoldType.CODE, textArea,
                                    t.getOffset());
                            folds.add(currentFold);
                        } else {
                            currentFold = currentFold.createChild(
                                    IFoldType.CODE, t.getOffset());
                        }
                    } else if (isRightCurly(t)) {
                        if (currentFold != null) {
                            currentFold.setEndOffset(t.getOffset());
                            Fold parentFold = currentFold.getParent();
                            // Don't add fold markers for single-line blocks
                            if (currentFold.isOnSingleLine()) {
                                if (!currentFold.removeFromParent()) {
                                    folds.remove(folds.size() - 1);
                                }
                            }
                            currentFold = parentFold;
                        }
                    }
                    // Java-specific folding rules
                    else if (java) {
                        if (t.is(IToken.RESERVED_WORD, KEYWORD_IMPORT)) {
                            if (importStartLine == -1) {
                                importStartLine = line;
                                importGroupStartOffs = t.getOffset();
                                importGroupEndOffs = t.getOffset();
                            }
                            lastSeenImportLine = line;
                        } else if (importStartLine > -1 && t.isIdentifier() && //SEPARATOR &&
                                t.isSingleChar(';')) {
                            importGroupEndOffs = t.getOffset();
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
     * Returns whether the token is a left curly brace. This method exists so
     * subclasses can provide their own curly brace definition.
     *
     * @param t The token.
     * @return Whether it is a left curly brace.
     * @see #isRightCurly(IToken)
     */
    public boolean isLeftCurly(IToken t) {
        return t.isLeftCurly();
    }

    /**
     * Returns whether the token is a right curly brace. This method exists so
     * subclasses can provide their own curly brace definition.
     *
     * @param t The token.
     * @return Whether it is a right curly brace.
     * @see #isLeftCurly(IToken)
     */
    public boolean isRightCurly(IToken t) {
        return t.isRightCurly();
    }

    /**
     * Sets whether multi-line comments are foldable with this parser.
     *
     * @param foldable Whether multi-line comments are foldable.
     * @see #getFoldableMultiLineComments()
     */
    public void setFoldableMultiLineComments(boolean foldable) {
        this.foldableMultiLineComments = foldable;
    }
}