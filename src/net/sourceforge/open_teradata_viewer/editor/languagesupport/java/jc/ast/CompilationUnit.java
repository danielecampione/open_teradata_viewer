/*
 * Open Teradata Viewer ( editor language support java jc ast )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Annotation;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.IJavaLexicalToken;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.IOffset;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.notices.ParserNotice;

/**
 * A <code>CompilationUnit</code> is the root node of an AST for a Java source
 * file.
 *
 * <pre>
 * CompilationUnit:
 *    [[Annotations] 'package' QualifiedIdentifier ';' ] {ImportDeclaration} {ITypeDeclaration}
 * </pre>
 *
 * @author D. Campione
 * 
 */
public class CompilationUnit extends AbstractASTNode implements
        ITypeDeclarationContainer {

    private List<Annotation> annotations;
    private Package pkg;
    private List<ImportDeclaration> imports;
    private List<ITypeDeclaration> typeDeclarations;
    private List<ParserNotice> notices;

    private static final IOffset ZERO_OFFSET = new ZeroOffset();

    public CompilationUnit(String name) {
        super(name, ZERO_OFFSET);
        imports = new ArrayList<ImportDeclaration>(3); // Usually not many
        typeDeclarations = new ArrayList<ITypeDeclaration>(1); // Usually only 1
    }

    public void addImportDeclaration(ImportDeclaration dec) {
        imports.add(dec);
    }

    /** Shorthand for "<tt>addParserNotice(new ParserNotice(t, msg))</tt>". */
    public void addParserNotice(IJavaLexicalToken t, String msg) {
        addParserNotice(new ParserNotice(t, msg));
    }

    public void addParserNotice(ParserNotice notice) {
        if (notices == null) {
            notices = new ArrayList<ParserNotice>();
            notices.add(notice);
        }
    }

    @Override
    public void addTypeDeclaration(ITypeDeclaration typeDec) {
        typeDeclarations.add(typeDec);
    }

    public int getAnnotationCount() {
        return annotations.size();
    }

    public Iterator<Annotation> getAnnotationIterator() {
        return annotations.iterator();
    }

    /**
     * Returns the deepest-nested type declaration that contains a given offset.
     *
     * @param offs The offset.
     * @return The deepest-nested type declaration containing the offset or
     *         <code>null</code> if the offset is outside of any type
     *         declaration (such as in the import statements, etc..).
     * @see #getTypeDeclarationAtOffset(int)
     */
    public ITypeDeclaration getDeepestTypeDeclarationAtOffset(int offs) {
        ITypeDeclaration td = getTypeDeclarationAtOffset(offs);

        if (td != null) {
            ITypeDeclaration next = td.getChildTypeAtOffset(offs);
            while (next != null) {
                td = next;
                next = td.getChildTypeAtOffset(offs);
            }
        }

        return td;
    }

    /** @return The starting and ending offset of the enclosing method range. */
    public Point getEnclosingMethodRange(int offs) {
        Point range = null;

        for (Iterator<ITypeDeclaration> i = getTypeDeclarationIterator(); i
                .hasNext();) {
            ITypeDeclaration td = i.next();
            int start = td.getBodyStartOffset();
            int end = td.getBodyEndOffset();

            if (offs >= start && offs <= end) {
                if (td instanceof NormalClassDeclaration) {
                    NormalClassDeclaration ncd = (NormalClassDeclaration) td;
                    for (Iterator<IMember> j = ncd.getMemberIterator(); j
                            .hasNext();) {
                        IMember m = j.next();
                        if (m instanceof Method) {
                            Method method = (Method) m;
                            CodeBlock body = method.getBody();
                            if (body != null) {
                                int start2 = method.getNameStartOffset();
                                //int start2 = body.getStartOffset();
                                int end2 = body.getNameEndOffset();
                                if (offs >= start2 && offs <= end2) {
                                    range = new Point(start2, end2);
                                    break;
                                }
                            }
                        }
                    }
                }

                if (range == null) { // Default to the entire class' body
                    range = new Point(start, end);
                }
            }
        }

        return range;
    }

    public int getImportCount() {
        return imports.size();
    }

    /**
     * Returns the import declarations of this compilation unit. This is a copy
     * of the list of imports but the actual individual {@link
     * ImportDeclaration}s are not copies, so modifying them will modify this
     * compilation unit.
     *
     * @return A list or imports or an empty list if there are none.
     */
    public List<ImportDeclaration> getImports() {
        return new ArrayList<ImportDeclaration>(imports);
    }

    public Iterator<ImportDeclaration> getImportIterator() {
        return imports.iterator();
    }

    /**
     * Returns the package of this compilation unit.
     *
     * @return The package of this compilation unit or <code>null</code> if this
     *         compilation unit is not in a package.
     * @see #getPackageName()
     */
    public Package getPackage() {
        return pkg;
    }

    /**
     * Returns the fully-qualified package name of this compilation unit.
     *
     * @return The package name or <code>null</code> if this compilation unit is
     *         not in a package (in the default package).
     * @see #getPackage()
     */
    public String getPackageName() {
        return pkg == null ? null : pkg.getName();
    }

    public ParserNotice getParserNotice(int index) {
        if (notices == null) {
            throw new IndexOutOfBoundsException("No parser notices available");
        }
        return notices.get(index);
    }

    public int getParserNoticeCount() {
        return notices == null ? 0 : notices.size();
    }

    public ITypeDeclaration getTypeDeclaration(int index) {
        return typeDeclarations.get(index);
    }

    /**
     * Returns the type declaration in this file that contains the specified
     * offset.
     *
     * @param offs The offset.
     * @return The type declaration or <code>null</code> if the offset is
     *         outside of any type declaration.
     * @see #getDeepestTypeDeclarationAtOffset(int)
     */
    public ITypeDeclaration getTypeDeclarationAtOffset(int offs) {
        ITypeDeclaration typeDec = null;

        for (ITypeDeclaration td : typeDeclarations) {
            if (td.getBodyContainsOffset(offs)) {
                typeDec = td;
                break;
            }
        }

        return typeDec;
    }

    public int getTypeDeclarationCount() {
        return typeDeclarations.size();
    }

    public Iterator<ITypeDeclaration> getTypeDeclarationIterator() {
        return typeDeclarations.iterator();
    }

    public void setPackage(Package pkg) {
        this.pkg = pkg;
    }

    /**
     * An offset that always returns 0.
     * 
     * @author D. Campione
     * 
     */
    private static class ZeroOffset implements IOffset {

        @Override
        public int getOffset() {
            return 0;
        }

    }
}