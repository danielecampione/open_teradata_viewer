/*
 * Open Teradata Viewer ( editor language support js completion )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion;

import javax.swing.Icon;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.FunctionCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.IconFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.SourceCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclaration;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class JavaScriptInScriptFunctionCompletion extends FunctionCompletion
        implements IJSCompletion {

    private TypeDeclaration returnType;

    public JavaScriptInScriptFunctionCompletion(ICompletionProvider provider,
            String name, TypeDeclaration returnType) {
        super(provider, name, null);
        setRelevance(DEFAULT_FUNCTION_RELEVANCE);
        this.returnType = returnType;
    }

    @Override
    public String getSummary() {
        String summary = super.getShortDescription(); // Could be just the
                                                      // method name

        // If it's the Javadoc for the method..
        if (summary != null && summary.startsWith("/**")) {
            summary = net.sourceforge.open_teradata_viewer.editor.languagesupport.java.Util
                    .docCommentToHtml(summary);
        }

        return summary;
    }

    @Override
    public Icon getIcon() {
        return IconFactory.getIcon(IconFactory.DEFAULT_FUNCTION_ICON);
    }

    @Override
    public String getLookupName() {
        StringBuilder sb = new StringBuilder(getName());
        sb.append('(');
        int count = getParamCount();
        for (int i = 0; i < count; i++) {
            sb.append("p");
            if (i < count - 1) {
                sb.append(",");
            }
        }
        sb.append(')');
        return sb.toString();
    }

    @Override
    public String getType() {
        String value = getType(true);
        return ((SourceCompletionProvider) getProvider()).getTypesFactory()
                .convertJavaScriptType(value, false);
    }

    @Override
    public String getType(boolean qualified) {
        String type = returnType != null ? returnType.getQualifiedName() : null;
        return ((SourceCompletionProvider) getProvider()).getTypesFactory()
                .convertJavaScriptType(type, qualified);
    }

    @Override
    public String getEnclosingClassName(boolean fullyQualified) {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof IJSCompletion) {
            IJSCompletion jsComp = (IJSCompletion) obj;
            return getLookupName().equals(jsComp.getLookupName());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getLookupName().hashCode();
    }

    @Override
    public String toString() {
        return getLookupName();
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(ICompletion other) {
        if (other == this) {
            return 0;
        } else if (other instanceof IJSCompletion) {
            IJSCompletion c2 = (IJSCompletion) other;
            return getLookupName().compareTo(c2.getLookupName());
        } else if (other != null) {
            return toString().compareTo(other.toString());
        }
        return -1;
    }
}