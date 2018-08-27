/*
 * Open Teradata Viewer ( editor autocomplete )
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

package net.sourceforge.open_teradata_viewer.editor.autocomplete;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

/**
 * An <code>AutoCompletion</code> that adds the ability to cycle through a set
 * of <code>ICompletionProvider</code>s via the trigger key. This allows the
 * application to logically "group together" completions of similar kinds; for
 * example, Java code completions vs. template completions.<p>
 *
 * Usage:
 * <pre>
 * XPathDynamicCompletionProvider dynamicProvider = new XPathDynamicCompletionProvider();
 * RoundRobinAutoCompletion ac = new RoundRobinAutoCompletion(dynamicProvider);
 * XPathCompletionProvider staticProvider = new XPathCompletionProvider();
 * ac.addCompletionProvider(staticProvider);
 * ac.setXXX(..);
 * ...
 * ac.install(textArea);
 * </pre>
 *
 * @author D. Campione
 *
 */
public class RoundRobinAutoCompletion extends AutoCompletion {

    /** The List of ICompletionProviders to use. */
    private List<ICompletionProvider> cycle = new ArrayList<ICompletionProvider>();

    /**
     * Ctor.
     *
     * @param provider A single completion provider.
     * @see #addCompletionProvider(ICompletionProvider)
     */
    public RoundRobinAutoCompletion(ICompletionProvider provider) {
        super(provider);
        cycle.add(provider);

        // Principal requirement for round-robin
        setHideOnCompletionProviderChange(false);
        // This is required since otherwise, on empty list of completions for
        // one of the ICompletionProviders, round-robin completion would not
        // work
        setHideOnNoText(false);
        // This is required to prevent single choice of 1st provider to choose
        // the completion since the user may want the second provider to be
        // chosen
        setAutoCompleteSingleChoices(false);
    }

    /**
     * Adds an additional <code>ICompletionProvider</code> to the list to cycle
     * through.
     *
     * @param provider The new completion provider.
     */
    public void addCompletionProvider(ICompletionProvider provider) {
        cycle.add(provider);
    }

    /**
     * Moves to the next Provider internally. Needs refresh of the popup window
     * to display the changes.
     *
     * @return true if the next provider was the default one (thus returned to
     *         the default view). May be used in case you like to hide the popup
     *         in this case.
     */
    public boolean advanceProvider() {
        ICompletionProvider currentProvider = getCompletionProvider();
        int i = (cycle.indexOf(currentProvider) + 1) % cycle.size();
        setCompletionProvider(cycle.get(i));
        return i == 0;
    }

    /** Overridden to provide our own implementation of the action. */
    @Override
    protected Action createAutoCompleteAction() {
        return new CycleAutoCompleteAction();
    }

    /** Resets the cycle to use the default provider on next refresh. */
    public void resetProvider() {
        ICompletionProvider currentProvider = getCompletionProvider();
        ICompletionProvider defaultProvider = cycle.get(0);
        if (currentProvider != defaultProvider) {
            setCompletionProvider(defaultProvider);
        }
    }

    /**
     * An implementation of the auto-complete action that ensures the proper
     * <code>ICompletionProvider</code> is displayed based on the context in
     * which the user presses the trigger key.
     *
     * @author D. Campione
     *
     */
    private class CycleAutoCompleteAction extends AutoCompleteAction {

        private static final long serialVersionUID = 2278318851282400478L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isAutoCompleteEnabled()) {
                if (isPopupVisible()) {
                    // The popup is already visible and user pressed the
                    // trigger-key. In this case, move to next provider
                    advanceProvider();
                } else {
                    // Be sure to start with the default provider
                    resetProvider();
                }
                // Check if there are completions from the current provider. If
                // not, advance to the next provider and display that one.
                // A completion provider can force displaying "his" empty
                // completion pop-up by returning an empty BasicCompletion. This
                // is useful when the user is typing backspace and you like to
                // display the first provider always first
                for (int i = 1; i < cycle.size(); i++) {
                    List<ICompletion> completions = getCompletionProvider()
                            .getCompletions(getTextComponent());
                    if (completions.size() > 0) {
                        // Nothing to do, just let the current provider display
                        break;
                    } else {
                        // Search for non-empty completions
                        advanceProvider();
                    }
                }
            }
            super.actionPerformed(e);
        }
    }
}