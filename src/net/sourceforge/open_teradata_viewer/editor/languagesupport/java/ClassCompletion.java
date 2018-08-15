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

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Iterator;

import javax.swing.Icon;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath.ISourceLocation;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.IAccessFlags;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CompilationUnit;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ITypeDeclaration;

/**
 * Completion for a Java class, interface or enum.
 *
 * @author D. Campione
 * 
 */
class ClassCompletion extends AbstractJavaSourceCompletion {

    private ClassFile cf;

    public ClassCompletion(ICompletionProvider provider, ClassFile cf) {
        super(provider, cf.getClassName(false));
        this.cf = cf;
    }

    @Override
    public int compareTo(ICompletion c2) {
        if (c2 == this) {
            return 0;
        }
        // Check for classes with same name but in different packages
        else if (c2.toString().equalsIgnoreCase(toString())) {
            if (c2 instanceof ClassCompletion) {
                ClassCompletion cc2 = (ClassCompletion) c2;
                return getClassName(true).compareTo(cc2.getClassName(true));
            }
        }
        return super.compareTo(c2);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ClassCompletion)
                && ((ClassCompletion) obj).getReplacementText().equals(
                        getReplacementText());
    }

    /**
     * Returns the name of the class represented by this completion.
     *
     * @param fullyQualified Whether the returned name should be fully
     *        qualified.
     * @return The class name.
     * @see #getPackageName()
     */
    public String getClassName(boolean fullyQualified) {
        return cf.getClassName(fullyQualified);
    }

    /** {@inheritDoc} */
    @Override
    public Icon getIcon() {
        boolean isInterface = false;
        boolean isPublic = false;
        boolean isDefault = false;

        int access = cf.getAccessFlags();
        if ((access & IAccessFlags.ACC_INTERFACE) > 0) {
            isInterface = true;
        } else if (net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.Util
                .isPublic(access)) {
            isPublic = true;
        } else {
            isDefault = true;
        }

        IconFactory fact = IconFactory.get();
        String key = null;

        if (isInterface) {
            if (isDefault) {
                key = IconFactory.DEFAULT_INTERFACE_ICON;
            } else {
                key = IconFactory.INTERFACE_ICON;
            }
        } else {
            if (isDefault) {
                key = IconFactory.DEFAULT_CLASS_ICON;
            } else if (isPublic) {
                key = IconFactory.CLASS_ICON;
            }
        }

        return fact.getIcon(key, cf.isDeprecated());
    }

    /**
     * Returns the package this class or interface is in.
     *
     * @return The package or <code>null</code> if it is not in a package.
     * @see #getClassName(boolean)
     */
    public String getPackageName() {
        return cf.getPackageName();
    }

    @Override
    public String getSummary() {
        SourceCompletionProvider scp = (SourceCompletionProvider) getProvider();
        ISourceLocation loc = scp.getSourceLocForClass(cf.getClassName(true));

        if (loc != null) {
            CompilationUnit cu = Util.getCompilationUnitFromDisk(loc, cf);
            if (cu != null) {
                Iterator<ITypeDeclaration> i = cu.getTypeDeclarationIterator();
                for (; i.hasNext();) {
                    ITypeDeclaration td = i.next();
                    String typeName = td.getName();
                    // Avoid inner classes, etc..
                    if (typeName.equals(cf.getClassName(false))) {
                        String summary = td.getDocComment();
                        if (summary != null && summary.startsWith("/**")) {
                            return Util.docCommentToHtml(summary);
                        }
                    }
                }
            }
        }

        // Default to the fully-qualified class name
        return cf.getClassName(true);
    }

    @Override
    public String getToolTipText() {
        return "class " + getReplacementText();
    }

    @Override
    public int hashCode() {
        return getReplacementText().hashCode();
    }

    @Override
    public void rendererText(Graphics g, int x, int y, boolean selected) {
        String s = cf.getClassName(false);
        g.drawString(s, x, y);
        FontMetrics fm = g.getFontMetrics();
        int newX = x + fm.stringWidth(s);
        if (cf.isDeprecated()) {
            int midY = y + fm.getDescent() - fm.getHeight() / 2;
            g.drawLine(x, midY, newX, midY);
        }
        x = newX;

        s = " - ";
        g.drawString(s, x, y);
        x += fm.stringWidth(s);

        String pkgName = cf.getClassName(true);
        int lastIndexOf = pkgName.lastIndexOf('.');
        if (lastIndexOf != -1) { // Class may not be in a package
            pkgName = pkgName.substring(0, lastIndexOf);
            Color origColor = g.getColor();
            if (!selected) {
                g.setColor(Color.GRAY);
            }
            g.drawString(pkgName, x, y);
            if (!selected) {
                g.setColor(origColor);
            }
        }
    }
}