/*
 * Open Teradata Viewer ( editor language support java )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java;

import java.awt.Graphics;

import javax.swing.Icon;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.TemplateCompletion;

/**
 * A template completion for Java.
 *
 * @author D. Campione
 * 
 */
public class JavaTemplateCompletion extends TemplateCompletion implements
        IJavaSourceCompletion {

    private String icon;

    public JavaTemplateCompletion(ICompletionProvider provider,
            String inputText, String definitionString, String template) {
        this(provider, inputText, definitionString, template, null);
    }

    public JavaTemplateCompletion(ICompletionProvider provider,
            String inputText, String definitionString, String template,
            String shortDesc) {
        this(provider, inputText, definitionString, template, shortDesc, null);
    }

    public JavaTemplateCompletion(ICompletionProvider provider,
            String inputText, String definitionString, String template,
            String shortDesc, String summary) {
        super(provider, inputText, definitionString, template, shortDesc,
                summary);
        setIcon(IconFactory.TEMPLATE_ICON);
    }

    @Override
    public Icon getIcon() {
        return IconFactory.get().getIcon(icon);
    }

    @Override
    public void rendererText(Graphics g, int x, int y, boolean selected) {
        JavaShorthandCompletion.renderText(g, getInputText(),
                getShortDescription(), x, y, selected);
    }

    public void setIcon(String iconId) {
        this.icon = iconId;
    }
}