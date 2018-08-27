/*
 * Open Teradata Viewer ( editor language support js completion )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion;

import javax.swing.Icon;
import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.VariableCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.FieldInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Field;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.IconFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.JavaScriptHelper;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.SourceCompletionProvider;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class JSFieldCompletion extends VariableCompletion implements
        IJSCompletion {

    private JSFieldData fieldData;
    private Field field;

    public JSFieldCompletion(ICompletionProvider provider, FieldInfo fieldInfo) {
        super(provider, fieldInfo.getName(), null);
        this.fieldData = new JSFieldData(fieldInfo,
                ((SourceCompletionProvider) provider).getJarManager());
        this.field = fieldData.getField();
        setRelevance(fieldData);
    }

    private void setRelevance(JSFieldData data) {
        if (data.isStatic()) {
            setRelevance(STATIC_FIELD_RELEVANCE);
        } else {
            setRelevance(GLOBAL_VARIABLE_RELEVANCE);
        }
    }

    @Override
    public String getSummary() {
        String summary = field != null ? field.getDocComment() : getName();

        // If it's the Javadoc for the method..
        if (summary != null && summary.startsWith("/**")) {
            summary = net.sourceforge.open_teradata_viewer.editor.languagesupport.java.Util
                    .docCommentToHtml(summary);
        }

        return summary;
    }

    @Override
    public Icon getIcon() {
        return fieldData.isStatic() ? IconFactory
                .getIcon(IconFactory.STATIC_VAR_ICON)
                : fieldData.isPublic() ? IconFactory
                        .getIcon(IconFactory.GLOBAL_VARIABLE_ICON)
                        : IconFactory
                                .getIcon(IconFactory.DEFAULT_VARIABLE_ICON);
    }

    @Override
    public String getEnclosingClassName(boolean fullyQualified) {
        return fieldData.getEnclosingClassName(fullyQualified);
    }

    @Override
    public String getAlreadyEntered(JTextComponent comp) {
        String temp = getProvider().getAlreadyEnteredText(comp);
        int lastDot = JavaScriptHelper
                .findLastIndexOfJavaScriptIdentifier(temp);
        if (lastDot > -1) {
            temp = temp.substring(lastDot + 1);
        }
        return temp;
    }

    @Override
    public String getLookupName() {
        return getReplacementText();
    }

    @Override
    public String getType() {
        return ((SourceCompletionProvider) getProvider()).getTypesFactory()
                .convertJavaScriptType(fieldData.getType(true), false);
    }

    @Override
    public String getType(boolean qualified) {
        return fieldData.getType(true); /*TypeDeclarationFactory.lookupJSType(fieldData.getType(true),
                                        qualified);*/
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof JSFieldCompletion) {
            JSFieldCompletion jsComp = (JSFieldCompletion) obj;
            return getLookupName().equals(jsComp.getLookupName());
        }
        return super.equals(obj);
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(ICompletion o) {
        if (o == this) {
            return 0;
        } else if (o instanceof JSFieldCompletion) {
            JSFieldCompletion c2 = (JSFieldCompletion) o;
            return getLookupName().compareTo(c2.getLookupName());
        }
        return super.compareTo(o);
    }

    @Override
    public int hashCode() {
        return getLookupName().hashCode();
    }
}