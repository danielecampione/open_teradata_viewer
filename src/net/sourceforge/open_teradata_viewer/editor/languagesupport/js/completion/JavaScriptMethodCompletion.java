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

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.FunctionCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.FormalParameter;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Method;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.IconFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.SourceCompletionProvider;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class JavaScriptMethodCompletion extends FunctionCompletion implements
        IJSCompletion {

    private Method method;

    private String compareString;
    private boolean systemFunction;
    private String nameAndParameters;

    public JavaScriptMethodCompletion(ICompletionProvider provider,
            Method method) {
        super(provider, method.getName(), null);
        this.method = method;
        int count = method.getParameterCount();
        List<Parameter> params = new ArrayList<Parameter>(count);
        for (int i = 0; i < count; i++) {
            FormalParameter param = method.getParameter(i);
            String name = param.getName();
            params.add(new FunctionCompletion.Parameter(null, name));
        }
        setParams(params);
    }

    private String createNameAndParameters() {
        StringBuilder sb = new StringBuilder(getName());
        sb.append('(');
        int count = method.getParameterCount();
        for (int i = 0; i < count; i++) {
            FormalParameter fp = method.getParameter(i);
            sb.append(fp.getName());
            if (i < count - 1) {
                sb.append(", ");
            }
        }
        sb.append(')');
        return sb.toString();
    }

    @Override
    public Icon getIcon() {
        return IconFactory.getIcon(systemFunction ? IconFactory.FUNCTION_ICON
                : IconFactory.DEFAULT_FUNCTION_ICON);
    }

    @Override
    public int getRelevance() {
        return systemFunction ? GLOBAL_FUNCTION_RELEVANCE
                : DEFAULT_FUNCTION_RELEVANCE;
    }

    public void setSystemFunction(boolean systemFunction) {
        this.systemFunction = systemFunction;
    }

    public boolean isSystemFunction() {
        return systemFunction;
    }

    @Override
    public String getSummary() {
        String summary = getMethodSummary(); // Could be just the method name

        // If it's the Javadoc for the method..
        if (summary != null && summary.startsWith("/**")) {
            summary = net.sourceforge.open_teradata_viewer.editor.languagesupport.java.Util
                    .docCommentToHtml(summary);
        }

        return summary;
    }

    public String getSignature() {
        if (nameAndParameters == null) {
            nameAndParameters = createNameAndParameters();
        }
        return nameAndParameters;
    }

    /** Overridden since <code>equals()</code> is overridden. */
    @Override
    public int hashCode() {
        return getCompareString().hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getSignature();
    }

    private String getMethodSummary() {
        String docComment = method.getDocComment();
        return docComment != null ? docComment : method.toString();
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(ICompletion other) {
        int rc = -1;
        if (other == this) {
            rc = 0;
        } else if (other instanceof IJSCompletion) {
            IJSCompletion c2 = (IJSCompletion) other;
            rc = getLookupName().compareTo(c2.getLookupName());
        } else if (other != null) {
            ICompletion c2 = other;
            rc = toString().compareTo(c2.toString());
            if (rc == 0) { // Same text value
                String clazz1 = getClass().getName();
                clazz1 = clazz1.substring(clazz1.lastIndexOf('.'));
                String clazz2 = c2.getClass().getName();
                clazz2 = clazz2.substring(clazz2.lastIndexOf('.'));
                rc = clazz1.compareTo(clazz2);
            }
        }

        return rc;
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

    private String getCompareString() {
        /*
         * This string compares the following parts of methods in this order, to
         * optimize sort order in completion lists.
         * 
         * 1. First, by name 2. Next, by number of parameters. 3. Finally, by
         * parameter type.
         */

        if (compareString == null) {
            StringBuilder sb = new StringBuilder(getName());
            int paramCount = getParamCount();
            if (paramCount < 10) {
                sb.append('0');
            }
            sb.append(paramCount);
            for (int i = 0; i < paramCount; i++) {
                String type = getParam(i).getType();
                sb.append(type);
                if (i < paramCount - 1) {
                    sb.append(',');
                }
            }
            compareString = sb.toString();
        }

        return compareString;
    }

    @Override
    public String getDefinitionString() {
        return getSignature();
    }

    @Override
    public String getType(boolean qualified) {
        return ((SourceCompletionProvider) getProvider()).getTypesFactory()
                .convertJavaScriptType("void", qualified);
    }

    @Override
    public String getEnclosingClassName(boolean fullyQualified) {
        return null;
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
}