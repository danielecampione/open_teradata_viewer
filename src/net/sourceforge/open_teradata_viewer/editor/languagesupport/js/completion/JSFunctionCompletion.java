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

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.FunctionCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.IParameterizedCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.MethodInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Method;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.IconFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.JavaScriptHelper;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.SourceCompletionProvider;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class JSFunctionCompletion extends FunctionCompletion implements
        IJSCompletion {

    private JSMethodData methodData;
    private String compareString;
    private String nameAndParameters;

    public JSFunctionCompletion(ICompletionProvider provider, MethodInfo method) {
        this(provider, method, false);
    }

    public JSFunctionCompletion(ICompletionProvider provider,
            MethodInfo methodInfo, boolean showParameterType) {
        super(provider, getMethodName(methodInfo, provider), null);
        this.methodData = new JSMethodData(methodInfo,
                ((SourceCompletionProvider) provider).getJarManager());
        List<Parameter> params = populateParams(methodData, showParameterType);
        setParams(params);
    }

    private static String getMethodName(MethodInfo info,
            ICompletionProvider provider) {
        if (info.isConstructor()) {
            return ((SourceCompletionProvider) provider).getTypesFactory()
                    .convertJavaScriptType(
                            info.getClassFile().getClassName(true), false);
        } else {
            return info.getName();
        }
    }

    private List<Parameter> populateParams(JSMethodData methodData,
            boolean showParameterType) {
        MethodInfo methodInfo = methodData.getMethodInfo();
        int count = methodInfo.getParameterCount();
        String[] paramTypes = methodInfo.getParameterTypes();
        List<Parameter> params = new ArrayList<Parameter>(count);
        for (int i = 0; i < count; i++) {
            String name = methodData.getParameterName(i);
            String type = methodData.getParameterType(paramTypes, i,
                    getProvider());
            params.add(new JSFunctionParam(type, name, showParameterType,
                    getProvider()));
        }

        return params;
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
            rc = toString().compareTo(other.toString());
            if (rc == 0) { // Same text value
                String clazz1 = getClass().getName();
                clazz1 = clazz1.substring(clazz1.lastIndexOf('.'));
                String clazz2 = other.getClass().getName();
                clazz2 = clazz2.substring(clazz2.lastIndexOf('.'));
                rc = clazz1.compareTo(clazz2);
            }
        }

        return rc;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof IJSCompletion)
                && ((IJSCompletion) obj).getLookupName()
                        .equals(getLookupName());
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

    private String getCompareString() {
        /*
         * This string compares the following parts of methods in this order, to
         * optimize sort order in completion lists.
         * 
         * 1. First, by name 2. Next, by number of parameters. 3. Finally, by
         * parameter type.
         */

        if (compareString == null) {
            compareString = getLookupName();
        }

        return compareString;
    }

    @Override
    public String getLookupName() {
        SourceCompletionProvider provider = (SourceCompletionProvider) getProvider();
        return provider.getJavaScriptEngine().getJavaScriptResolver(provider)
                .getLookupText(methodData, getName());
    }

    @Override
    public String getDefinitionString() {
        return getSignature();
    }

    private String getMethodSummary() {
        // String summary = methodData.getSummary(); // Could be just the method
        // name

        Method method = methodData.getMethod();
        String summary = method != null ? method.getDocComment() : null;
        // If it's the Javadoc for the method..
        if (summary != null && summary.startsWith("/**")) {
            summary = net.sourceforge.open_teradata_viewer.editor.languagesupport.java.Util
                    .docCommentToHtml(summary);
        }

        return summary != null ? summary : getNameAndParameters();
    }

    private String getNameAndParameters() {
        if (nameAndParameters == null) {
            nameAndParameters = formatMethodAtString(getName(), methodData);
        }
        return nameAndParameters;
    }

    private static String formatMethodAtString(String name, JSMethodData method) {
        StringBuilder sb = new StringBuilder(name);
        sb.append('(');
        int count = method.getParameterCount();
        for (int i = 0; i < count; i++) {
            sb.append(method.getParameterName(i));
            if (i < count - 1) {
                sb.append(", ");
            }
        }
        sb.append(')');
        return sb.toString();
    }

    public String getSignature() {
        return getNameAndParameters();
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

    @Override
    public int hashCode() {
        return getCompareString().hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getSignature();
    }

    @Override
    public String getType() {
        String value = getType(true);
        return ((SourceCompletionProvider) getProvider()).getTypesFactory()
                .convertJavaScriptType(value, false);
    }

    @Override
    public String getType(boolean qualified) {
        return ((SourceCompletionProvider) getProvider())
                .getTypesFactory()
                .convertJavaScriptType(methodData.getType(qualified), qualified);
    }

    @Override
    public Icon getIcon() {
        return methodData.isStatic() ? IconFactory
                .getIcon(IconFactory.PUBLIC_STATIC_FUNCTION_ICON) : IconFactory
                .getIcon(IconFactory.DEFAULT_FUNCTION_ICON);
    }

    @Override
    public int getRelevance() {
        return DEFAULT_FUNCTION_RELEVANCE;
    }

    @Override
    public String getEnclosingClassName(boolean fullyQualified) {
        return methodData.getEnclosingClassName(fullyQualified);
    }

    public JSMethodData getMethodData() {
        return methodData;
    }

    /**
     * Override the FunctionCompletion.Parameter to lookup the Javascript name
     * for the completion type.
     * 
     * @author D. Campione
     * 
     */
    public static class JSFunctionParam extends
            IParameterizedCompletion.Parameter {

        private boolean showParameterType;
        private ICompletionProvider provider;

        public JSFunctionParam(Object type, String name,
                boolean showParameterType, ICompletionProvider provider) {
            super(type, name);
            this.showParameterType = showParameterType;
            this.provider = provider;
        }

        @Override
        public String getType() {
            return showParameterType ? ((SourceCompletionProvider) provider)
                    .getTypesFactory().convertJavaScriptType(super.getType(),
                            false) : null;
        }
    }
}