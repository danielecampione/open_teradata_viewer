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

import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.VariableCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.Util;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath.ISourceLocation;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CompilationUnit;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ITypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.IconFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.JavaScriptHelper;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.SourceCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.JavaScriptVariableDeclaration;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class JSVariableCompletion extends VariableCompletion implements
        IJSCompletionUI {

    private JavaScriptVariableDeclaration dec;
    private boolean localVariable;

    public JSVariableCompletion(ICompletionProvider provider,
            JavaScriptVariableDeclaration dec) {
        this(provider, dec, true);
    }

    public JSVariableCompletion(ICompletionProvider provider,
            JavaScriptVariableDeclaration dec, boolean localVariable) {
        super(provider, dec.getName(), dec.getJavaScriptTypeName());
        this.dec = dec;
        this.localVariable = localVariable;
    }

    /** @return The type name not qualified. */
    @Override
    public String getType() {
        return getType(false);
    }

    /**
     * @param qualified Whether to return the name as qualified.
     * @return The type name based on qualified.
     */
    public String getType(boolean qualified) {
        return ((SourceCompletionProvider) getProvider()).getTypesFactory()
                .convertJavaScriptType(dec.getJavaScriptTypeName(), qualified);
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
    public Icon getIcon() {
        return IconFactory
                .getIcon(localVariable ? IconFactory.LOCAL_VARIABLE_ICON
                        : IconFactory.GLOBAL_VARIABLE_ICON);
    }

    @Override
    public int getRelevance() {
        return localVariable ? LOCAL_VARIABLE_RELEVANCE
                : GLOBAL_VARIABLE_RELEVANCE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof VariableCompletion) {
            VariableCompletion comp = (VariableCompletion) obj;
            return getName().equals(comp.getName());
        }

        return super.equals(obj);
    }

    @Override
    public int compareTo(ICompletion c2) {
        if (c2 == this) {
            return 0;
        }
        return super.compareTo(c2);
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String getSummary() {
        SourceCompletionProvider scp = (SourceCompletionProvider) getProvider();
        ClassFile cf = scp.getJavaScriptTypesFactory().getClassFile(
                scp.getJarManager(),
                JavaScriptHelper.createNewTypeDeclaration(getType(true)));
        if (cf != null) {
            ISourceLocation loc = scp.getSourceLocForClass(cf
                    .getClassName(true));

            if (loc != null) {
                CompilationUnit cu = Util.getCompilationUnitFromDisk(loc, cf);
                if (cu != null) {
                    Iterator<ITypeDeclaration> i = cu
                            .getTypeDeclarationIterator();
                    while (i.hasNext()) {
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
            return cf.getClassName(true);
        }

        return super.getSummary();
    }
}