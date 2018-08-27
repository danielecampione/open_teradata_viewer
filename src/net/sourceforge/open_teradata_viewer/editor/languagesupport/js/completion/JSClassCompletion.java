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

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.BasicCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.Util;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath.ISourceLocation;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CompilationUnit;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ITypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.IconFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.JavaScriptHelper;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.SourceCompletionProvider;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class JSClassCompletion extends BasicCompletion implements IJSCompletion {

    private ClassFile cf;
    private boolean qualified;

    public JSClassCompletion(ICompletionProvider provider, ClassFile cf,
            boolean qualified) {
        super(provider, ((SourceCompletionProvider) provider).getTypesFactory()
                .convertJavaScriptType(cf.getClassName(true), qualified));
        this.cf = cf;
        this.qualified = qualified;
        setRelevance(DEFAULT_CLASS_RELEVANCE);
    }

    /*
     * Fixed error when comparing classes of the same name, which did not allow
     * classes with same name but different packages.
     */
    @Override
    public int compareTo(ICompletion c2) {
        if (c2 == this) {
            return 0;
        }
        // Check for classes with same name but in different packages
        else if (c2.toString().equalsIgnoreCase(toString())) {
            if (c2 instanceof JSClassCompletion) {
                JSClassCompletion jsc2 = (JSClassCompletion) c2;
                return getReplacementText()
                        .compareTo(jsc2.getReplacementText());
            }
        }
        return super.compareTo(c2);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof JSClassCompletion)
                && ((JSClassCompletion) obj).getReplacementText().equals(
                        getReplacementText());
    }

    @Override
    public String getAlreadyEntered(JTextComponent comp) {
        String temp = getProvider().getAlreadyEnteredText(comp);
        int lastDot = JavaScriptHelper
                .findLastIndexOfJavaScriptIdentifier(temp);
        if (lastDot > -1) {
            return temp.substring(lastDot + 1);
        }
        if (temp.indexOf("new") != -1) {
            return "";
        }

        return temp;
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
        return ((SourceCompletionProvider) getProvider()).getTypesFactory()
                .convertJavaScriptType(cf.getClassName(fullyQualified),
                        fullyQualified);
    }

    @Override
    public Icon getIcon() {
        return IconFactory.getIcon(IconFactory.DEFAULT_CLASS_ICON);
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
                        // Be cautious - might be no doc comment
                        if (summary != null && summary.startsWith("/**")) {
                            return Util.docCommentToHtml(summary);
                        }
                    }
                }
            }

        }

        // Default to the fully-qualified class name
        return ((SourceCompletionProvider) getProvider()).getTypesFactory()
                .convertJavaScriptType(cf.getClassName(true), qualified);
    }

    @Override
    public String getToolTipText() {
        return "type " + getReplacementText();
    }

    @Override
    public int hashCode() {
        return getReplacementText().hashCode();
    }

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

        if (qualified) {
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

    @Override
    public String getEnclosingClassName(boolean fullyQualified) {
        return cf.getClassName(fullyQualified);
    }

    @Override
    public String getLookupName() {
        return getReplacementText();
    }

    @Override
    public String getType(boolean qualified) {
        return getClassName(qualified);
    }
}