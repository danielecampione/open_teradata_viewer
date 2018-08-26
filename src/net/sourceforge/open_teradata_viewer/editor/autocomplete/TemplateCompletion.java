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

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import javax.swing.text.Position;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ITemplatePiece.Param;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ITemplatePiece.ParamCopy;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ITemplatePiece.Text;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxUtilities;

/**
 * A completion made up of a template with arbitrary parameters that the user
 * can tab through and fill in. This completion type is useful for inserting
 * common boilerplate code, such as for-loops.<p>
 *
 * The format of a template is similar to those in Eclipse. The following
 * example would be the format for a for-loop template:
 *
 * <pre>
 * for (int ${i} = 0; ${i} &lt; ${array}.length; ${i}++) {
 *    ${cursor}
 * }
 * </pre>
 *
 * In the above example, the first <code>${i}</code> is a parameter for the user
 * to type into; all the other <code>${i}</code> instances are automatically
 * changed to what the user types in the first one. The parameter named
 * <code>${cursor}</code> is the "ending position" of the template. It's where
 * the caret moves after it cycles through all other parameters. If the user
 * types into it, template mode terminates. If more than one
 * <code>${cursor}</code> parameter is specified, behavior is undefined.<p>
 *
 * Two dollar signs in a row ("<code>$$</code>") will be evaluated as a single
 * dollar sign. Otherwise, the template parsing is pretty straightforward and
 * fault-tolerant.<p>
 *
 * Leading whitespace is automatically added to lines if the template spans more
 * than one line, and if used with a text component using a
 * <code>PlainDocument</code>, tabs will be converted to spaces if requested.
 *
 * @author D. Campione
 *
 */
public class TemplateCompletion extends AbstractCompletion implements
        IParameterizedCompletion {

    private List<ITemplatePiece> pieces;

    private String inputText;

    private String definitionString;

    private String shortDescription;

    private String summary;

    /** The template's parameters. */
    private List<Parameter> params;

    public TemplateCompletion(ICompletionProvider provider, String inputText,
            String definitionString, String template) {
        this(provider, inputText, definitionString, template, null, null);
    }

    public TemplateCompletion(ICompletionProvider provider, String inputText,
            String definitionString, String template, String shortDescription,
            String summary) {
        super(provider);
        this.inputText = inputText;
        this.definitionString = definitionString;
        this.shortDescription = shortDescription;
        this.summary = summary;
        pieces = new ArrayList<ITemplatePiece>(3);
        params = new ArrayList<Parameter>(3);
        parse(template);
    }

    private void addTemplatePiece(ITemplatePiece piece) {
        pieces.add(piece);
        if (piece instanceof Param && !"cursor".equals(piece.getText())) {
            final String type = null;
            Parameter param = new Parameter(type, piece.getText());
            params.add(param);
        }
    }

    @Override
    public String getInputText() {
        return inputText;
    }

    private String getPieceText(int index, String leadingWS) {
        ITemplatePiece piece = pieces.get(index);
        String text = piece.getText();
        if (text.indexOf('\n') > -1) {
            text = text.replaceAll("\n", "\n" + leadingWS);
        }
        return text;
    }

    /**
     * Returns <code>null</code>; template completions insert all of their text
     * via <code>getInsertionInfo()</code>.
     *
     * @return <code>null</code> always.
     */
    @Override
    public String getReplacementText() {
        return null;
    }

    @Override
    public String getSummary() {
        return summary;
    }

    @Override
    public String getDefinitionString() {
        return definitionString;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    /** {@inheritDoc} */
    @Override
    public boolean getShowParameterToolTip() {
        return false;
    }

    @Override
    public ParameterizedCompletionInsertionInfo getInsertionInfo(
            JTextComponent tc, boolean replaceTabsWithSpaces) {
        ParameterizedCompletionInsertionInfo info = new ParameterizedCompletionInsertionInfo();

        StringBuilder sb = new StringBuilder();
        int dot = tc.getCaretPosition();

        // Get the range in which the caret can move before we hide this tool
        // tip
        int minPos = dot;
        Position maxPos = null;
        int defaultEndOffs = -1;
        try {
            maxPos = tc.getDocument().createPosition(dot);
        } catch (BadLocationException ble) {
            ExceptionDialog.hideException(ble); // Never happens
        }
        info.setCaretRange(minPos, maxPos);
        int selStart = dot; // Default value
        int selEnd = selStart;

        Document doc = tc.getDocument();
        String leadingWS = null;
        try {
            leadingWS = SyntaxUtilities.getLeadingWhitespace(doc, dot);
        } catch (BadLocationException ble) { // Never happens
            ExceptionDialog.hideException(ble);
            leadingWS = "";
        }

        // Create the text to insert (keep it one completion for performance and
        // simplicity of undo/redo)
        int start = dot;
        for (int i = 0; i < pieces.size(); i++) {
            ITemplatePiece piece = pieces.get(i);
            String text = getPieceText(i, leadingWS);
            if (piece instanceof Text) {
                if (replaceTabsWithSpaces) {
                    start = possiblyReplaceTabsWithSpaces(sb, text, tc, start);
                } else {
                    sb.append(text);
                    start += text.length();
                }
            } else if (piece instanceof Param && "cursor".equals(text)) {
                defaultEndOffs = start;
            } else {
                int end = start + text.length();
                sb.append(text);
                if (piece instanceof Param) {
                    info.addReplacementLocation(start, end);
                    if (selStart == dot) {
                        selStart = start;
                        selEnd = selStart + text.length();
                    }
                } else if (piece instanceof ParamCopy) {
                    info.addReplacementCopy(piece.getText(), start, end);
                }
                start = end;
            }
        }

        // Highlight the first parameter. If no params were specified, move the
        // caret to the ${cursor} location, if specified
        if (selStart == minPos && selStart == selEnd && getParamCount() == 0) {
            if (defaultEndOffs > -1) { // ${cursor} specified
                selStart = selEnd = defaultEndOffs;
            }
        }
        info.setInitialSelection(selStart, selEnd);

        if (defaultEndOffs > -1) {
            // Keep this location "after" all others when tabbing
            info.addReplacementLocation(defaultEndOffs, defaultEndOffs);
        }
        info.setDefaultEndOffs(defaultEndOffs);
        info.setTextToInsert(sb.toString());

        return info;
    }

    /** {@inheritDoc} */
    @Override
    public Parameter getParam(int index) {
        return params.get(index);
    }

    /** {@inheritDoc} */
    @Override
    public int getParamCount() {
        return params == null ? 0 : params.size();
    }

    /**
     * Returns whether a parameter is already defined with a specific name.
     *
     * @param name The name.
     * @return Whether a parameter is defined with that name.
     */
    private boolean isParamDefined(String name) {
        for (int i = 0; i < getParamCount(); i++) {
            Parameter param = getParam(i);
            if (name.equals(param.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Parses a template string into logical pieces used by this class.
     *
     * @param template The template to parse.
     */
    private void parse(String template) {
        int offs = 0;
        int lastOffs = 0;

        while ((offs = template.indexOf('$', lastOffs)) > -1
                && offs < template.length() - 1) {
            char next = template.charAt(offs + 1);
            switch (next) {
            case '$': // "$$" => escaped single dollar sign
                addTemplatePiece(new ITemplatePiece.Text(template.substring(
                        lastOffs, offs + 1)));
                lastOffs = offs + 2;
                break;
            case '{': // "${...}" => variable
                int closingCurly = template.indexOf('}', offs + 2);
                if (closingCurly > -1) {
                    addTemplatePiece(new ITemplatePiece.Text(
                            template.substring(lastOffs, offs)));
                    String varName = template.substring(offs + 2, closingCurly);
                    if (!"cursor".equals(varName) && isParamDefined(varName)) {
                        addTemplatePiece(new ITemplatePiece.ParamCopy(varName));
                    } else {
                        addTemplatePiece(new ITemplatePiece.Param(varName));
                    }
                    lastOffs = closingCurly + 1;
                }
                break;
            }
        }

        if (lastOffs < template.length()) {
            String text = template.substring(lastOffs);
            addTemplatePiece(new ITemplatePiece.Text(text));
        }
    }

    private int possiblyReplaceTabsWithSpaces(StringBuilder sb, String text,
            JTextComponent tc, int start) {
        int tab = text.indexOf('\t');
        if (tab > -1) {
            int startLen = sb.length();

            int size = 4;
            Document doc = tc.getDocument();
            if (doc != null) {
                Integer i = (Integer) doc
                        .getProperty(PlainDocument.tabSizeAttribute);
                if (i != null) {
                    size = i.intValue();
                }
            }
            String tabStr = "";
            for (int i = 0; i < size; i++) {
                tabStr += " ";
            }

            int lastOffs = 0;
            do {
                sb.append(text.substring(lastOffs, tab));
                sb.append(tabStr);
                lastOffs = tab + 1;
            } while ((tab = text.indexOf('\t', lastOffs)) > -1);
            sb.append(text.substring(lastOffs));

            start += sb.length() - startLen;
        } else {
            sb.append(text);
            start += text.length();
        }

        return start;
    }

    /**
     * Sets the short description of this template completion.
     *
     * @param shortDesc The new short description.
     * @see #getShortDescription()
     */
    public void setShortDescription(String shortDesc) {
        this.shortDescription = shortDesc;
    }

    @Override
    public String toString() {
        return getDefinitionString();
    }
}