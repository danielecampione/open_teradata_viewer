/*
 * Open Teradata Viewer ( editor language support js )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js;

import java.util.ResourceBundle;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.BasicCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.DefaultCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.ShorthandCompletionCache;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion.JavaScriptTemplateCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion.JavascriptBasicCompletion;

/**
 * Cache of template and comment completions for JavaScript.
 *
 * @author D. Campione
 * 
 */
public class JavaScriptShorthandCompletionCache extends
        ShorthandCompletionCache {

    private static final String MSG = "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.resources";
    private static final ResourceBundle msg = ResourceBundle.getBundle(MSG);

    public JavaScriptShorthandCompletionCache(
            DefaultCompletionProvider templateProvider,
            DefaultCompletionProvider commentsProvider, boolean e4xSuppport) {
        super(templateProvider, commentsProvider);

        // Add basic keywords
        addShorthandCompletion(new JavascriptBasicCompletion(templateProvider,
                "do"));
        addShorthandCompletion(new JavascriptBasicCompletion(templateProvider,
                "if"));
        addShorthandCompletion(new JavascriptBasicCompletion(templateProvider,
                "while"));
        addShorthandCompletion(new JavascriptBasicCompletion(templateProvider,
                "for"));
        addShorthandCompletion(new JavascriptBasicCompletion(templateProvider,
                "switch"));
        addShorthandCompletion(new JavascriptBasicCompletion(templateProvider,
                "try"));
        addShorthandCompletion(new JavascriptBasicCompletion(templateProvider,
                "catch"));
        addShorthandCompletion(new JavascriptBasicCompletion(templateProvider,
                "case"));

        // Add template completions
        // Iterate array
        String template = "for (var ${i} = 0; ${i} < ${array}.length; ${i}++) {\n\t${cursor}\n}";
        addShorthandCompletion(new JavaScriptTemplateCompletion(
                templateProvider, "for", "for-loop-array", template,
                msg.getString("for.array.shortDesc"),
                msg.getString("for.array.summary")));

        // Standard for
        template = "for (var ${i} = 0; ${i} < ${10}; ${i}++) {\n\t${cursor}\n}";
        addShorthandCompletion(new JavaScriptTemplateCompletion(
                templateProvider, "for", "for-loop", template,
                msg.getString("for.loop.shortDesc"),
                msg.getString("for.loop.summary")));

        // for in
        template = "for (var ${iterable_element} in ${iterable})\n{\n\t${cursor}\n}";
        addShorthandCompletion(new JavaScriptTemplateCompletion(
                templateProvider, "for", "for-loop-in", template,
                msg.getString("for.in.shortDesc"),
                msg.getString("for.in.summary")));

        // e4x specific
        if (e4xSuppport) {
            // for each
            template = "for each (var ${iterable_element} in ${iterable})\n{\n\t${cursor}\n}";
            addShorthandCompletion(new JavaScriptTemplateCompletion(
                    templateProvider, "for", "for-loop-in-each", template,
                    msg.getString("for.in.each.shortDesc"),
                    msg.getString("for.in.each.summary")));
        }

        // do while
        template = "do {\n\t${cursor}\n} while (${condition});";
        addShorthandCompletion(new JavaScriptTemplateCompletion(
                templateProvider, "do-while", "do-loop", template,
                msg.getString("do.shortDesc"), msg.getString("do.summary")));

        // if condition
        template = "if (${condition}) {\n\t${cursor}\n}";
        addShorthandCompletion(new JavaScriptTemplateCompletion(
                templateProvider, "if", "if-cond", template,
                msg.getString("if.cond.shortDesc"),
                msg.getString("if.cond.summary")));

        // if else condition
        template = "if (${condition}) {\n\t${cursor}\n} else {\n\t\n}";
        addShorthandCompletion(new JavaScriptTemplateCompletion(
                templateProvider, "if", "if-else", template,
                msg.getString("if.else.shortDesc"),
                msg.getString("if.else.summary")));

        // while condition
        template = "while (${condition}) {\n\t${cursor}\n}";
        addShorthandCompletion(new JavaScriptTemplateCompletion(
                templateProvider, "while", "while-cond", template,
                msg.getString("while.shortDesc"),
                msg.getString("while.summary")));

        // switch case statement
        template = "switch (${key}) {\n\tcase ${value}:\n\t\t${cursor}\n\t\tbreak;\n\tdefault:\n\t\tbreak;\n}";
        addShorthandCompletion(new JavaScriptTemplateCompletion(
                templateProvider, "switch", "switch-statement", template,
                msg.getString("switch.case.shortDesc"),
                msg.getString("switch.case.summary")));

        // try catch statement
        template = "try {\n\t ${cursor} \n} catch (${err}) {\n\t\n}";
        addShorthandCompletion(new JavaScriptTemplateCompletion(
                templateProvider, "try", "try-catch", template,
                msg.getString("try.catch.shortDesc"),
                msg.getString("try.catch.summary")));

        // catch block
        template = "catch (${err}) {\n\t${cursor}\n}";
        addShorthandCompletion(new JavaScriptTemplateCompletion(
                templateProvider, "catch", "catch-block", template,
                msg.getString("catch.block.shortDesc"),
                msg.getString("catch.block.summary")));

        /** Comments **/
        addCommentCompletion(new BasicCompletion(commentsProvider, "TODO:",
                null, msg.getString("todo")));
        addCommentCompletion(new BasicCompletion(commentsProvider, "FIXME:",
                null, msg.getString("fixme")));
    }
}