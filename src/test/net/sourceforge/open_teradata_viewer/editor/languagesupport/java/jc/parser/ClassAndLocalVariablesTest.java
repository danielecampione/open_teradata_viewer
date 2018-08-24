/*
 * Open Teradata Viewer ( editor language support java jc parser )
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

package test.net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CodeBlock;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CompilationUnit;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Field;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.FormalParameter;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.IMember;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ITypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ImportDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.LocalVariable;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Method;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.Scanner;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.parser.ASTFactory;

/**
 * Simple test case verifying parsing common cases of:
 * 
 * <ul>
 *    <li>Class members (methods and fields)
 *    <li>Local variables
 *    <li>Documentation comments for methods and fields
 * </ul>
 *
 * @author D. Campione
 * 
 */
public class ClassAndLocalVariablesTest extends TestCase {

    private CompilationUnit cu;

    public ClassAndLocalVariablesTest() {
        try {
            cu = createCompilationUnit();
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
        }
    }

    private CompilationUnit createCompilationUnit() throws IOException {
        InputStream in = getClass().getResourceAsStream(
                "/res/testfiles/editor/SimpleClass.txt");
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        Scanner s = new Scanner(r);
        ASTFactory fact = new ASTFactory();
        CompilationUnit cu = fact.getCompilationUnit("SimpleClass", s);
        r.close();
        return cu;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testImports() throws IOException {
        assertEquals(4, cu.getImportCount());

        // Imports should be returned in the order in which they are found
        Iterator<ImportDeclaration> i = cu.getImportIterator();

        ImportDeclaration id = i.next();
        assertEquals("java.io.*", id.getName());
        assertEquals(true, id.isWildcard());
        assertEquals(false, id.isStatic());

        id = i.next();
        assertEquals("java.util.ArrayList", id.getName());
        assertEquals(false, id.isWildcard());
        assertEquals(false, id.isStatic());

        id = i.next();
        assertEquals("java.util.List", id.getName());
        assertEquals(false, id.isWildcard());
        assertEquals(false, id.isStatic());
    }

    public void testMembers() throws IOException {

        // A single class is defined
        assertEquals(1, cu.getTypeDeclarationCount());

        // The class is named "SimpleClass"
        ITypeDeclaration typeDec = cu.getTypeDeclarationIterator().next();
        assertEquals("SimpleClass", typeDec.getName());

        // 4 fields, 1 constructor and 3 methods
        int memberCount = typeDec.getMemberCount();
        assertEquals(8, memberCount);

        // Iterate through members.  They should be returned in the
        // order they are found in.
        Iterator<IMember> i = typeDec.getMemberIterator();

        IMember member = i.next();
        assertTrue(member instanceof Field);
        Field field = (Field) member;
        assertEquals("int", field.getType().toString());
        assertEquals("classInt1", field.getName());
        assertTrue(field.getModifiers().isPublic());
        assertTrue(field.getDocComment() != null
                && field.getDocComment().indexOf("A member int variable.") >= 0);

        member = i.next();
        assertTrue(member instanceof Field);
        field = (Field) member;
        assertEquals("int", field.getType().toString());
        assertEquals("classInt2", field.getName());
        assertTrue(field.getModifiers().isProtected());
        assertEquals(null, field.getDocComment());

        member = i.next();
        assertTrue(member instanceof Field);
        field = (Field) member;
        assertEquals("String", field.getType().toString());
        assertEquals("classStr1", field.getName());
        assertTrue(field.getModifiers().isPrivate());
        assertTrue(field.getDocComment() != null
                && field.getDocComment().indexOf("A string member variable.") >= 0);

        member = i.next();
        assertTrue(member instanceof Field);
        field = (Field) member;
        assertEquals("list", field.getName());
        assertEquals("List<String>", field.getType().toString());
        assertTrue(field.getModifiers().isPrivate());
        assertEquals(field.getDocComment(), null);

        member = i.next();
        assertTrue(member instanceof Method);
        Method method = (Method) member;
        assertEquals("SimpleClass", method.getName());
        assertTrue(method.getModifiers().isPublic());
        assertTrue(method.isConstructor());

        member = i.next();
        assertTrue(member instanceof Method);
        method = (Method) member;
        assertEquals("getValue", method.getName());
        assertTrue(method.getModifiers().isPublic());
        assertTrue(method.getDocComment() != null
                && method.getDocComment().indexOf("Returns a value.") >= 0);

        member = i.next();
        assertTrue(member instanceof Method);
        method = (Method) member;
        assertEquals("localVarsComplex", method.getName());
        assertTrue(method.getModifiers().isPublic());
        // This method takes two parameters.
        assertEquals(2, method.getParameterCount());
        FormalParameter param = method.getParameter(0);
        assertEquals("newValue", param.getName());
        param = method.getParameter(1);
        assertEquals("unused", param.getName());

        member = i.next();
        assertTrue(member instanceof Method);
        method = (Method) member;
        assertEquals("localVarsSimple", method.getName());
        assertTrue(method.getModifiers().isPublic());

    }

    public void testLocalVariablesComplex() {

        ITypeDeclaration td = cu.getTypeDeclaration(0);
        List<Method> methods = td.getMethodsByName("localVarsComplex");
        assertEquals(1, methods.size());
        Method method = methods.get(0);
        CodeBlock body = method.getBody();
        assertEquals(1, body.getLocalVarCount());

        LocalVariable var = body.getLocalVar(0);
        assertEquals("int", var.getType().getName(false));
        assertEquals("foo", var.getName());

        for (int i = 1; i < 1; i++) {
            var = body.getLocalVar(i);
            assertEquals("double", var.getType().getName(false));
            assertEquals("val" + i, var.getName());
        }

    }

    public void testLocalVariablesSimple() {

        ITypeDeclaration td = cu.getTypeDeclaration(0);
        List<Method> methods = td.getMethodsByName("localVarsSimple");
        assertEquals(1, methods.size());
        Method method = methods.get(0);
        CodeBlock body = method.getBody();
        assertEquals(2, body.getLocalVarCount());

        LocalVariable var = body.getLocalVar(0);
        assertEquals("int", var.getType().getName(false));
        assertEquals("temp", var.getName());

        var = body.getLocalVar(1);
        assertEquals("boolean", var.getType().getName(false));
        assertEquals("unnecessary", var.getName());

        assertEquals(1, body.getChildBlockCount());
        CodeBlock ifStatementBlock = body.getChildBlock(0);
        assertEquals(1, ifStatementBlock.getLocalVarCount());
        var = ifStatementBlock.getLocalVar(0);
        assertEquals("float", var.getType().getName(false));
        assertEquals("f", var.getName());

    }

}