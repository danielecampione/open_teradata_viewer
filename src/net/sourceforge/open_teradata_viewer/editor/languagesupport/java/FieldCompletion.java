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

import javax.swing.Icon;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.FieldInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Field;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Type;

/**
 * A completion for a Java field. This completion gets its information from one
 * of two sources:
 * 
 * <ul>
 *    <li>A {@link FieldInfo} instance, which is loaded by parsing a class file.
 *        This is used when this completion represents a field found in a
 *        compiled library.</li>
 *    <li>A {@link Field} instance, which is created when parsing a Java source
 *        file. This is used when the completion represents a field found in
 *        uncompiled source, such as the source in an <tt>SyntaxTextArea</tt> or
 *        in a loose file on disk.</li>
 * </ul>
 *
 * @author D. Campione
 * 
 */
class FieldCompletion extends AbstractJavaSourceCompletion implements
        IMemberCompletion {

    private IData data;

    /**
     * The relevance of fields. This allows fields to be "higher" in the
     * completion list than other types.
     */
    private static final int RELEVANCE = 3;

    public FieldCompletion(ICompletionProvider provider, Field field) {
        super(provider, field.getName());
        this.data = new FieldData(field);
        setRelevance(RELEVANCE);
    }

    public FieldCompletion(ICompletionProvider provider, FieldInfo info) {
        super(provider, info.getName());
        this.data = new FieldInfoData(info, (SourceCompletionProvider) provider);
        setRelevance(RELEVANCE);
    }

    private FieldCompletion(ICompletionProvider provider, String text) {
        super(provider, text);
        setRelevance(RELEVANCE);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof FieldCompletion)
                && ((FieldCompletion) obj).getSignature()
                        .equals(getSignature());
    }

    public static FieldCompletion createLengthCompletion(
            ICompletionProvider provider, final Type type) {
        FieldCompletion fc = new FieldCompletion(provider, type.toString());
        fc.data = new IData() {

            @Override
            public String getEnclosingClassName(boolean fullyQualified) {
                return type.getName(fullyQualified);
            }

            @Override
            public String getIcon() {
                return IconFactory.FIELD_PUBLIC_ICON;
            }

            @Override
            public String getSignature() {
                return "length";
            }

            @Override
            public String getSummary() {
                return null;
            }

            @Override
            public String getType() {
                return "int";
            }

            @Override
            public boolean isConstructor() {
                return false;
            }

            @Override
            public boolean isDeprecated() {
                return false;
            }

            @Override
            public boolean isAbstract() {
                return false;
            }

            @Override
            public boolean isFinal() {
                return false;
            }

            @Override
            public boolean isStatic() {
                return false;
            }
        };
        return fc;
    }

    @Override
    public String getEnclosingClassName(boolean fullyQualified) {
        return data.getEnclosingClassName(fullyQualified);
    }

    @Override
    public Icon getIcon() {
        return IconFactory.get().getIcon(data);
    }

    @Override
    public String getSignature() {
        return data.getSignature();
    }

    @Override
    public String getSummary() {
        String summary = data.getSummary(); // Could be just the method name

        // If it's the Javadoc for the method..
        if (summary != null && summary.startsWith("/**")) {
            summary = net.sourceforge.open_teradata_viewer.editor.languagesupport.java.Util
                    .docCommentToHtml(summary);
        }

        return summary;
    }

    @Override
    public String getType() {
        return data.getType();
    }

    @Override
    public int hashCode() {
        return getSignature().hashCode();
    }

    @Override
    public boolean isDeprecated() {
        return data.isDeprecated();
    }

    @Override
    public void rendererText(Graphics g, int x, int y, boolean selected) {
        MethodCompletion.rendererText(this, g, x, y, selected);
    }
}