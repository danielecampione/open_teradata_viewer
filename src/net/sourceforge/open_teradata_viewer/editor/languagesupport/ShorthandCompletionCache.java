/*
 * Open Teradata Viewer ( editor language support )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.AbstractCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;

/**
 * A cache to store completions for Template completions and Comment
 * completions. Template completions should extend
 * <code>TemplateCompletion</code> that uses parameterized variables/values.<p> 
 * 
 * While template completion example:
 * <pre>
 * while --&gt; while(condition) {
 *              //cursor here
 *           }
 * </pre>
 * 
 * Comment completion example:
 * <pre>
 * null --&gt; &lt;code&gt;null&lt;/code&gt;
 * </pre> 
 * 
 * This is really a convenient place to store these types of completions that
 * are re-used.
 * 
 * @author D. Campione
 * 
 */
public class ShorthandCompletionCache {

    private List<ICompletion> shorthandCompletion;
    private List<ICompletion> commentCompletion;

    private AbstractCompletionProvider templateProvider, commentProvider;

    public ShorthandCompletionCache(
            AbstractCompletionProvider templateProvider,
            AbstractCompletionProvider commentProvider) {
        shorthandCompletion = new ArrayList<ICompletion>();
        commentCompletion = new ArrayList<ICompletion>();
        this.templateProvider = templateProvider;
        this.commentProvider = commentProvider;
    }

    public void addShorthandCompletion(ICompletion completion) {
        addSorted(shorthandCompletion, completion);
    }

    private static final void addSorted(List<ICompletion> list,
            ICompletion completion) {
        int index = Collections.binarySearch(list, completion);
        if (index < 0) {
            index = -(index + 1);
        }
        list.add(index, completion);
    }

    public List<ICompletion> getShorthandCompletions() {
        return shorthandCompletion;
    }

    public void removeShorthandCompletion(ICompletion completion) {
        shorthandCompletion.remove(completion);
    }

    public void clearCache() {
        shorthandCompletion.clear();
    }

    // Comments
    public void addCommentCompletion(ICompletion completion) {
        addSorted(commentCompletion, completion);
    }

    public List<ICompletion> getCommentCompletions() {
        return commentCompletion;
    }

    public void removeCommentCompletion(ICompletion completion) {
        commentCompletion.remove(completion);
    }

    public AbstractCompletionProvider getTemplateProvider() {
        return templateProvider;
    }

    public AbstractCompletionProvider getCommentProvider() {
        return commentProvider;
    }
}