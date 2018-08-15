/*
 * Open Teradata Viewer ( editor language support java tree )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.tree;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.AbstractSourceTree;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.ILanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.LanguageSupportFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.IconFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.JavaLanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.JavaParser;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CodeBlock;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CompilationUnit;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Field;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.IASTNode;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.IMember;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ITypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ImportDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.LocalVariable;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Method;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.NormalClassDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.NormalInterfaceDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Package;
import net.sourceforge.open_teradata_viewer.editor.syntax.ISyntaxConstants;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * A tree view showing the outline of Java source, similar to the "Outline" view
 * in the Eclipse JDT. It also uses Eclipse's icons, just like the rest of this
 * code completion library.<p>
 *
 * You can get this tree automatically updating in response to edits in an
 * <code>SyntaxTextArea</code> with {@link JavaLanguageSupport} installed by
 * calling {@link #listenTo(SyntaxTextArea)}. Note that an instance of this
 * class can only listen to a single editor at a time, so if your application
 * contains multiple instances of SyntaxTextArea, you'll either need a separate
 * <code>JavaOutlineTree</code> for each one or call <code>uninstall()</code>
 * and <code>listenTo(SyntaxTextArea)</code> each time a new STA receives focus.
 * 
 * @author D. Campione
 * 
 */
public class JavaOutlineTree extends AbstractSourceTree {

    private static final long serialVersionUID = 1506881304979950443L;

    private DefaultTreeModel model;
    private SyntaxTextArea textArea;
    private JavaParser parser;
    private Listener listener;

    /**
     * Ctor. The tree created will not have its elements sorted alphabetically.
     */
    public JavaOutlineTree() {
        this(false);
    }

    /**
     * Ctor.
     *
     * @param sorted Whether the tree should sort its elements alphabetically.
     *        Note that outline trees will likely group nodes by type before
     *        sorting (i.e. methods will be sorted in one group, fields in
     *        another group, etc..).
     */
    public JavaOutlineTree(boolean sorted) {
        setSorted(sorted);
        setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        setRootVisible(false);
        setCellRenderer(new AstTreeCellRenderer());
        model = new DefaultTreeModel(new DefaultMutableTreeNode("Nothing"));
        setModel(model);
        listener = new Listener();
        addTreeSelectionListener(listener);
    }

    /**
     * Refreshes this tree.
     *
     * @param cu The parsed compilation unit. If this is <code>null</code> then
     *        the tree is cleared.
     */
    private void update(CompilationUnit cu) {
        JavaTreeNode root = new JavaTreeNode("Remove me!",
                IconFactory.SOURCE_FILE_ICON);
        root.setSortable(false);
        if (cu == null) {
            model.setRoot(root);
            return;
        }

        Package pkg = cu.getPackage();
        if (pkg != null) {
            String iconName = IconFactory.PACKAGE_ICON;
            root.add(new JavaTreeNode(pkg, iconName, false));
        }

        if (!getShowMajorElementsOnly()) {
            JavaTreeNode importNode = new JavaTreeNode("Imports",
                    IconFactory.IMPORT_ROOT_ICON);
            Iterator<ImportDeclaration> i = cu.getImportIterator();
            while (i.hasNext()) {
                ImportDeclaration idec = i.next();
                JavaTreeNode iNode = new JavaTreeNode(idec,
                        IconFactory.IMPORT_ICON);
                importNode.add(iNode);
            }
            root.add(importNode);
        }

        Iterator<ITypeDeclaration> i = cu.getTypeDeclarationIterator();
        while (i.hasNext()) {
            ITypeDeclaration td = i.next();
            TypeDeclarationTreeNode dmtn = createTypeDeclarationNode(td);
            root.add(dmtn);
        }

        model.setRoot(root);
        root.setSorted(isSorted());
        refresh();
    }

    /** Refreshes listeners on the text area when its syntax style changes. */
    private void checkForJavaParsing() {
        // Remove possible listener on old Java parser (in case they're just
        // changing syntax style away from Java)
        if (parser != null) {
            parser.removePropertyChangeListener(
                    JavaParser.PROPERTY_COMPILATION_UNIT, listener);
            parser = null;
        }

        // Get the Java language support (shared by all STA instances editing
        // Java that were registered with the LanguageSupportFactory)
        LanguageSupportFactory lsf = LanguageSupportFactory.get();
        ILanguageSupport support = lsf
                .getSupportFor(ISyntaxConstants.SYNTAX_STYLE_JAVA);
        JavaLanguageSupport jls = (JavaLanguageSupport) support;

        // Listen for re-parsing of the editor and update the tree accordingly
        parser = jls.getParser(textArea);
        if (parser != null) { // Should always be true
            parser.addPropertyChangeListener(
                    JavaParser.PROPERTY_COMPILATION_UNIT, listener);
            // Populate with any already-existing CompilationUnit
            CompilationUnit cu = parser.getCompilationUnit();
            update(cu);
        } else {
            update((CompilationUnit) null); // Clear the tree
        }
    }

    private MemberTreeNode createMemberNode(IMember member) {
        MemberTreeNode node = null;
        if (member instanceof CodeBlock) {
            node = new MemberTreeNode((CodeBlock) member);
        } else if (member instanceof Field) {
            node = new MemberTreeNode((Field) member);
        } else {
            node = new MemberTreeNode((Method) member);
        }

        CodeBlock body = null;
        if (member instanceof CodeBlock) {
            body = (CodeBlock) member;
        } else if (member instanceof Method) {
            body = ((Method) member).getBody();
        }

        if (body != null && !getShowMajorElementsOnly()) {
            for (int i = 0; i < body.getLocalVarCount(); i++) {
                LocalVariable var = body.getLocalVar(i);
                LocalVarTreeNode varNode = new LocalVarTreeNode(var);
                node.add(varNode);
            }
        }

        return node;
    }

    private TypeDeclarationTreeNode createTypeDeclarationNode(
            ITypeDeclaration td) {
        TypeDeclarationTreeNode dmtn = new TypeDeclarationTreeNode(td);

        if (td instanceof NormalClassDeclaration) {
            NormalClassDeclaration ncd = (NormalClassDeclaration) td;
            for (int j = 0; j < ncd.getChildTypeCount(); j++) {
                ITypeDeclaration td2 = ncd.getChildType(j);
                TypeDeclarationTreeNode tdn = createTypeDeclarationNode(td2);
                dmtn.add(tdn);
            }
            Iterator<IMember> i = ncd.getMemberIterator();
            while (i.hasNext()) {
                dmtn.add(createMemberNode(i.next()));
            }
        } else if (td instanceof NormalInterfaceDeclaration) {
            NormalInterfaceDeclaration nid = (NormalInterfaceDeclaration) td;
            for (int j = 0; j < nid.getChildTypeCount(); j++) {
                ITypeDeclaration td2 = nid.getChildType(j);
                TypeDeclarationTreeNode tdn = createTypeDeclarationNode(td2);
                dmtn.add(tdn);
            }
            Iterator<IMember> i = nid.getMemberIterator();
            while (i.hasNext()) {
                dmtn.add(createMemberNode(i.next()));
            }
        }

        return dmtn;
    }

    /** {@inheritDoc} */
    @Override
    public void expandInitialNodes() {
        // First, collapse all rows
        int j = 0;
        while (j < getRowCount()) {
            collapseRow(j++);
        }

        // Expand only type declarations
        expandRow(0);
        j = 1;
        while (j < getRowCount()) {
            TreePath path = getPathForRow(j);
            Object comp = path.getLastPathComponent();
            if (comp instanceof TypeDeclarationTreeNode) {
                expandPath(path);
            }
            j++;
        }
    }

    private void gotoElementAtPath(TreePath path) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
                .getLastPathComponent();
        Object obj = node.getUserObject();
        if (obj instanceof IASTNode) {
            IASTNode astNode = (IASTNode) obj;
            int start = astNode.getNameStartOffset();
            int end = astNode.getNameEndOffset();
            textArea.select(start, end);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean gotoSelectedElement() {
        TreePath path = getLeadSelectionPath();//e.getNewLeadSelectionPath();
        if (path != null) {
            gotoElementAtPath(path);
            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void listenTo(SyntaxTextArea textArea) {
        if (this.textArea != null) {
            uninstall();
        }

        // Nothing new to listen to
        if (textArea == null) {
            return;
        }

        // Listen for future language changes in the text editor
        this.textArea = textArea;
        textArea.addPropertyChangeListener(
                SyntaxTextArea.SYNTAX_STYLE_PROPERTY, listener);

        // Check whether we're currently editing Java
        checkForJavaParsing();
    }

    /** {@inheritDoc} */
    @Override
    public void uninstall() {
        if (parser != null) {
            parser.removePropertyChangeListener(
                    JavaParser.PROPERTY_COMPILATION_UNIT, listener);
            parser = null;
        }

        if (textArea != null) {
            textArea.removePropertyChangeListener(
                    SyntaxTextArea.SYNTAX_STYLE_PROPERTY, listener);
            textArea = null;
        }
    }

    /** Overridden to also update the UI of the child cell renderer. */
    @Override
    public void updateUI() {
        super.updateUI();
        // DefaultTreeCellRenderer caches colors, so we can't just call
        // ((JComponent)getCellRenderer()).updateUI()
        setCellRenderer(new AstTreeCellRenderer());
    }

    /**
     * Listens for events this tree is interested in (events in the associated
     * editor, for example), as well as events in this tree.
     * 
     * @author D. Campione
     * 
     */
    private class Listener implements PropertyChangeListener,
            TreeSelectionListener {

        /**
         * Called whenever the text area's syntax style changes, as well as when
         * it is re-parsed.
         */
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            String name = e.getPropertyName();

            // If the text area is changing the syntax style it is editing
            if (SyntaxTextArea.SYNTAX_STYLE_PROPERTY.equals(name)) {
                checkForJavaParsing();
            } else if (JavaParser.PROPERTY_COMPILATION_UNIT.equals(name)) {
                CompilationUnit cu = (CompilationUnit) e.getNewValue();
                update(cu);
            }
        }

        /**
         * Selects the corresponding element in the text editor when a user
         * clicks on a node in this tree.
         */
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            if (getGotoSelectedElementOnClick()) {
                TreePath newPath = e.getNewLeadSelectionPath();
                if (newPath != null) {
                    gotoElementAtPath(newPath);
                }
            }
        }
    }
}