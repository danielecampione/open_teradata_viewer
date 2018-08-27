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
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.JarManager;
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
public class JSBeanCompletion extends VariableCompletion implements
        IJSCompletion {

    private JSMethodData methodData;
    private Method method;

    public JSBeanCompletion(ICompletionProvider provider,
            MethodInfo methodInfo, JarManager jarManager) {
        super(provider, convertNameToBean(methodInfo.getName()), null);
        setRelevance(BEAN_METHOD_RELEVANCE);
        this.methodData = new JSMethodData(methodInfo, jarManager);
        this.method = methodData.getMethod();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof JSBeanCompletion)
                && ((JSBeanCompletion) obj).getName().equals(getName());
    }

    @Override
    public Icon getIcon() {
        return IconFactory.getIcon(IconFactory.GLOBAL_VARIABLE_ICON);
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

    private String getMethodSummary() {
        String docComment = method != null ? method.getDocComment() : getName();
        return docComment != null ? docComment : method != null ? method
                .toString() : null;
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
    public String getLookupName() {
        return getName();
    }

    @Override
    public String getEnclosingClassName(boolean fullyQualified) {
        return methodData.getEnclosingClassName(fullyQualified);
    }

    public JSMethodData getMethodData() {
        return methodData;
    }

    private static String convertNameToBean(String name) {
        boolean memberIsGetMethod = name.startsWith("get");
        boolean memberIsSetMethod = name.startsWith("set");
        boolean memberIsIsMethod = name.startsWith("is");
        if (memberIsGetMethod || memberIsIsMethod || memberIsSetMethod) {
            // Double check name component
            String nameComponent = name.substring(memberIsIsMethod ? 2 : 3);
            if (nameComponent.length() == 0) {
                return name; // Return name
            }

            // Make the bean property name
            String beanPropertyName = nameComponent;
            char ch0 = nameComponent.charAt(0);
            if (Character.isUpperCase(ch0)) {
                if (nameComponent.length() == 1) {
                    beanPropertyName = nameComponent.toLowerCase();
                } else {
                    char ch1 = nameComponent.charAt(1);
                    if (!Character.isUpperCase(ch1)) {
                        beanPropertyName = Character.toLowerCase(ch0)
                                + nameComponent.substring(1);
                    }
                }
            }
            name = beanPropertyName;
        }
        return name;
    }

    /**
     * Overridden since {@link #equals(Object)} is overridden.
     * 
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(ICompletion o) {
        if (o == this) {
            return 0;
        } else if (o instanceof JSBeanCompletion) {
            JSBeanCompletion c2 = (JSBeanCompletion) o;
            return getLookupName().compareTo(c2.getLookupName());
        }
        return super.compareTo(o);
    }
}