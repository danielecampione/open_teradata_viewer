/*
 * Open Teradata Viewer ( editor language support java )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.BasicCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.EmptyIcon;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.IParameterChoicesProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.IParameterizedCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.ILanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.LanguageSupportFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CodeBlock;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CompilationUnit;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Field;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.FormalParameter;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.IMember;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ITypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.LocalVariable;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Method;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.NormalClassDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.NormalInterfaceDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Package;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Type;
import net.sourceforge.open_teradata_viewer.editor.syntax.ISyntaxConstants;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * A parameter choices provider for Java methods.<p>
 * NOTE: This class is not thread-safe, but it's assumed that it is only ever
 * called on the EDT, so it should be a non-issue.
 *
 * @author D. Campione
 * 
 */
class SourceParamChoicesProvider implements IParameterChoicesProvider {

    /** The parent {@link JavaCompletionProvider}. */
    private ICompletionProvider provider;

    /**
     * Adds all accessible fields and getters of a specific type, from an
     * extended class or implemented interface.
     */
    private void addPublicAndProtectedFieldsAndGetters(Type type,
            JarManager jm, Package pkg, List list) {
    }

    /**
     * Gets all local variables, fields and simple getters defined in a class,
     * that are of a specific type and are accessible from a given offset.
     *
     * @param ncd The class.
     * @param type The type that the variables, fields and (return value of)
     *        getters must be. 
     * @param offs The offset of the caret.
     * @return The list of stuff or an empty list if none are found.
     */
    public List<ICompletion> getLocalVarsFieldsAndGetters(
            NormalClassDeclaration ncd, String type, int offs) {
        List<ICompletion> members = new ArrayList<ICompletion>();

        if (!ncd.getBodyContainsOffset(offs)) {
            return members;
        }

        // First, if the offset is in a method, get any local variables in that
        // method
        Method method = ncd.getMethodContainingOffset(offs);
        if (method != null) {
            // Parameters to the method
            Iterator<FormalParameter> i = method.getParameterIterator();
            while (i.hasNext()) {
                FormalParameter param = i.next();
                Type paramType = param.getType();
                if (isTypeCompatible(paramType, type)) {
                    members.add(new LocalVariableCompletion(provider, param));
                }
            }

            // Local variables in the method
            CodeBlock body = method.getBody();
            if (body != null) {
                CodeBlock block = body.getDeepestCodeBlockContaining(offs);
                List<LocalVariable> vars = block.getLocalVarsBefore(offs);
                for (LocalVariable var : vars) {
                    Type varType = var.getType();
                    if (isTypeCompatible(varType, type)) {
                        members.add(new LocalVariableCompletion(provider, var));
                    }
                }
            }
        }

        // Next, any fields/getters taking no parameters (for simplicity) in
        // this class
        for (Iterator<IMember> i = ncd.getMemberIterator(); i.hasNext();) {
            IMember member = i.next();

            if (member instanceof Field) {
                Type fieldType = member.getType();
                if (isTypeCompatible(fieldType, type)) {
                    members.add(new FieldCompletion(provider, (Field) member));
                }
            } else { // Method
                method = (Method) member;
                if (isSimpleGetter(method)) {
                    if (isTypeCompatible(method.getType(), type)) {
                        members.add(new MethodCompletion(provider, method));
                    }
                }
            }
        }

        return members;
    }

    /** {@inheritDoc} */
    @Override
    public List<ICompletion> getParameterChoices(JTextComponent tc,
            IParameterizedCompletion.Parameter param) {
        // Get the language support for Java
        LanguageSupportFactory lsf = LanguageSupportFactory.get();
        ILanguageSupport support = lsf
                .getSupportFor(ISyntaxConstants.SYNTAX_STYLE_JAVA);
        JavaLanguageSupport jls = (JavaLanguageSupport) support;
        JarManager jm = jls.getJarManager();

        // Get the deepest ITypeDeclaration AST containing the caret position
        // for the source code in the editor
        SyntaxTextArea textArea = (SyntaxTextArea) tc;
        JavaParser parser = jls.getParser(textArea);
        if (parser == null) {
            return null;
        }
        CompilationUnit cu = parser.getCompilationUnit();
        if (cu == null) {
            return null;
        }
        int dot = tc.getCaretPosition();
        ITypeDeclaration typeDec = cu.getDeepestTypeDeclarationAtOffset(dot);
        if (typeDec == null) {
            return null;
        }

        List<ICompletion> list = null;
        Package pkg = typeDec.getPackage();
        provider = jls.getCompletionProvider(textArea);

        // If we're in a class, we'll have to check for local variables, etc..
        if (typeDec instanceof NormalClassDeclaration) {
            // Get accessible members of this type
            NormalClassDeclaration ncd = (NormalClassDeclaration) typeDec;
            list = getLocalVarsFieldsAndGetters(ncd, param.getType(), dot);

            // Get accessible members of the extended type
            Type extended = ncd.getExtendedType();
            if (extended != null) {
                addPublicAndProtectedFieldsAndGetters(extended, jm, pkg, list);
            }

            // Get accessible members of any implemented interfaces
            for (Iterator<Type> i = ncd.getImplementedIterator(); i.hasNext();) {
                Type implemented = i.next();
                addPublicAndProtectedFieldsAndGetters(implemented, jm, pkg,
                        list);
            }
        }
        // If we're an interface, local vars, etc.. don't exist
        else if (typeDec instanceof NormalInterfaceDeclaration) {
            // Nothing to do
        }
        // If we're in an enum..
        else {//if (typeDec instanceof EnumDeclaration) {
        }

        // Check for any public/protected fields/getters in enclosing type
        if (!typeDec.isStatic()) {
        }

        // Add defaults for common types - "0" for primitive numeric types,
        // "null" for Objects, etc..
        Object typeObj = param.getTypeObject();
        if (typeObj instanceof Type) {
            Type type = (Type) typeObj;
            if (type.isBasicType()) {
                if (isPrimitiveNumericType(type)) {
                    list.add(new SimpleCompletion(provider, "0"));
                } else { // Is a "boolean" type
                    list.add(new SimpleCompletion(provider, "false"));
                    list.add(new SimpleCompletion(provider, "true"));
                }
            } else {
                list.add(new SimpleCompletion(provider, "null"));
            }
        }

        // And we're done
        return list;
    }

    private boolean isPrimitiveNumericType(Type type) {
        String str = type.getName(true);
        return "byte".equals(str) || "float".equals(str)
                || "double".equals(str) || "int".equals(str)
                || "short".equals(str) || "long".equals(str);
    }

    /**
     * Returns whether a method is a no-argument getter method.
     *
     * @param method The method.
     * @return Whether it is a no-argument getter.
     */
    private boolean isSimpleGetter(Method method) {
        return method.getParameterCount() == 0
                && method.getName().startsWith("get");
    }

    /**
     * Returns whether a <code>Type</code> and a type name are type compatible.
     */
    private boolean isTypeCompatible(Type type, String typeName) {
        String typeName2 = type.getName(false);

        // Remove generics info for now
        int lt = typeName2.indexOf('<');
        if (lt > -1) {
            String arrayDepth = null;
            int brackets = typeName2.indexOf('[', lt);
            if (brackets > -1) {
                arrayDepth = typeName2.substring(brackets);
            }
            typeName2 = typeName2.substring(lt);
            if (arrayDepth != null) {
                typeName2 += arrayDepth;
            }
        }

        return typeName2.equalsIgnoreCase(typeName);
    }

    /**
     * A very simple, low-relevance parameter choice completion. This is never
     * used as a general-purpose completion in Java code, as it cannot render
     * itself.
     * 
     * @author D. Campione
     * 
     */
    private static class SimpleCompletion extends BasicCompletion implements
            IJavaSourceCompletion {

        private Icon ICON = new EmptyIcon(16);

        public SimpleCompletion(ICompletionProvider provider, String text) {
            super(provider, text);
            setRelevance(-1);
        }

        @Override
        public Icon getIcon() {
            return ICON;
        }

        @Override
        public void rendererText(Graphics g, int x, int y, boolean selected) {
            // Never called
        }
    }
}