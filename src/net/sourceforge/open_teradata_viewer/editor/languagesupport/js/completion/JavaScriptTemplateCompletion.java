/*
 * Open Teradata Viewer ( editor language support js completion )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.JavaTemplateCompletion;

/**
 * Template completions specific to JavaScript.
 *
 * @author D. Campione
 * 
 */
public class JavaScriptTemplateCompletion extends JavaTemplateCompletion {

    public JavaScriptTemplateCompletion(ICompletionProvider provider,
            String inputText, String definitionString, String template) {
        this(provider, inputText, definitionString, template, null);
    }

    public JavaScriptTemplateCompletion(ICompletionProvider provider,
            String inputText, String definitionString, String template,
            String shortDesc) {
        this(provider, inputText, definitionString, template, shortDesc, null);
    }

    public JavaScriptTemplateCompletion(ICompletionProvider provider,
            String inputText, String definitionString, String template,
            String shortDesc, String summary) {
        super(provider, inputText, definitionString, template, shortDesc,
                summary);
    }
}