/*
 * Open Teradata Viewer ( editor language support )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.AutoCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.CompletionCellRenderer;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * A base class for language support implementations.
 *
 * @author D. Campione
 * 
 */
public abstract class AbstractLanguageSupport implements ILanguageSupport {

    /**
     * Map of all text areas using this language support to their installed
     * {@link AutoCompletion} instances. This should be maintained by subclasses
     * by adding to, and removing from, it in their {@link
     * #install(net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea)}
     * and {@link
     * #uninstall(net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea)}
     * methods.
     */
    private Map<SyntaxTextArea, AutoCompletion> textAreaToAutoCompletion;

    /** Whether auto-completion is enabled for this language. */
    private boolean autoCompleteEnabled;

    /**
     * Whether auto-activation for completion choices is enabled for this
     * language. Note that this parameter only matters if {@link
     * #autoCompleteEnabled} is <code>true</code>.
     */
    private boolean autoActivationEnabled;

    /**
     * The delay for auto-activation, in milliseconds. This parameter is only
     * honored if both {@link #autoCompleteEnabled} and {@link
     * #autoActivationEnabled} are <code>true</code>.
     */
    private int autoActivationDelay;

    /** Whether parameter assistance should be enabled for this language. */
    private boolean parameterAssistanceEnabled;

    /**
     * Whether the description window is displayed when the completion list
     * window is displayed.
     */
    private boolean showDescWindow;

    /** The default renderer for the completion list. */
    private ListCellRenderer renderer;

    /** Ctor. */
    protected AbstractLanguageSupport() {
        setDefaultCompletionCellRenderer(null); // Force default
        textAreaToAutoCompletion = new HashMap<SyntaxTextArea, AutoCompletion>();
        autoCompleteEnabled = true;
        autoActivationEnabled = false;
        autoActivationDelay = 300;
    }

    /**
     * Creates an auto-completion instance pre-configured and usable by most
     * <code>ILanguageSupport</code>s.
     *
     * @param p The completion provider.
     * @return The auto-completion instance.
     */
    protected AutoCompletion createAutoCompletion(ICompletionProvider p) {
        AutoCompletion ac = new AutoCompletion(p);
        ac.setListCellRenderer(getDefaultCompletionCellRenderer());
        ac.setAutoCompleteEnabled(isAutoCompleteEnabled());
        ac.setAutoActivationEnabled(isAutoActivationEnabled());
        ac.setAutoActivationDelay(getAutoActivationDelay());
        ac.setParameterAssistanceEnabled(isParameterAssistanceEnabled());
        ac.setShowDescWindow(getShowDescWindow());
        return ac;
    }

    /**
     * Creates the default cell renderer to use when none is specified.
     * Subclasses can override this method if there is a "better" default
     * renderer for a specific language.
     *
     * @return The default renderer for the completion list.
     */
    protected ListCellRenderer createDefaultCompletionCellRenderer() {
        return new DefaultListCellRenderer();
    }

    /**
     * Attempts to delegate rendering to a Substance cell renderer.
     *
     * @param ccr The cell renderer to modify.
     */
    private void delegateToSubstanceRenderer(CompletionCellRenderer ccr) {
        try {
            ccr.delegateToSubstanceRenderer();
        } catch (Exception e) {
            // Never happens if Substance is on the classpath
            ExceptionDialog.hideException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public int getAutoActivationDelay() {
        return autoActivationDelay;
    }

    /**
     * Returns the auto completion instance used by a text area.
     *
     * @param textArea The text area.
     * @return The auto completion instance or <code>null</code> if none is
     *         installed on the text area.
     */
    protected AutoCompletion getAutoCompletionFor(SyntaxTextArea textArea) {
        return textAreaToAutoCompletion.get(textArea);
    }

    /** {@inheritDoc} */
    @Override
    public ListCellRenderer getDefaultCompletionCellRenderer() {
        return renderer;
    }

    /** {@inheritDoc} */
    @Override
    public boolean getShowDescWindow() {
        return showDescWindow;
    }

    /**
     * Returns the text areas with this language support currently installed.
     *
     * @return The text areas.
     */
    protected Set<SyntaxTextArea> getTextAreas() {
        return textAreaToAutoCompletion.keySet();
    }

    /**
     * Registers an auto-completion instance. This should be called by
     * subclasses in their {@link
     * #install(net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea)}
     * methods so that this language support can update all of them at once.
     *
     * @param textArea The text area that just installed the auto completion.
     * @param ac The auto completion instance.
     * @see #uninstallImpl(SyntaxTextArea)
     */
    protected void installImpl(SyntaxTextArea textArea, AutoCompletion ac) {
        textAreaToAutoCompletion.put(textArea, ac);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAutoActivationEnabled() {
        return autoActivationEnabled;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAutoCompleteEnabled() {
        return autoCompleteEnabled;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isParameterAssistanceEnabled() {
        return parameterAssistanceEnabled;
    }

    /** {@inheritDoc} */
    @Override
    public void setAutoActivationDelay(int ms) {
        ms = Math.max(0, ms);
        if (ms != autoActivationDelay) {
            autoActivationDelay = ms;
            for (AutoCompletion ac : textAreaToAutoCompletion.values()) {
                ac.setAutoActivationDelay(autoActivationDelay);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setAutoActivationEnabled(boolean enabled) {
        if (enabled != autoActivationEnabled) {
            autoActivationEnabled = enabled;
            for (AutoCompletion ac : textAreaToAutoCompletion.values()) {
                ac.setAutoActivationEnabled(enabled);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setAutoCompleteEnabled(boolean enabled) {
        if (enabled != autoCompleteEnabled) {
            autoCompleteEnabled = enabled;
            for (AutoCompletion ac : textAreaToAutoCompletion.values()) {
                ac.setAutoCompleteEnabled(enabled);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setDefaultCompletionCellRenderer(ListCellRenderer r) {
        if (r == null) {
            r = createDefaultCompletionCellRenderer();
        }
        if (r instanceof CompletionCellRenderer
                && Utilities.getUseSubstanceRenderers()) {
            if (UIManager.getLookAndFeel().getClass().getName()
                    .contains(".Substance")) {
                CompletionCellRenderer ccr = (CompletionCellRenderer) r;
                delegateToSubstanceRenderer(ccr);
            }
        }
        renderer = r;
    }

    /** {@inheritDoc} */
    @Override
    public void setParameterAssistanceEnabled(boolean enabled) {
        if (enabled != parameterAssistanceEnabled) {
            parameterAssistanceEnabled = enabled;
            for (AutoCompletion ac : textAreaToAutoCompletion.values()) {
                ac.setParameterAssistanceEnabled(enabled);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setShowDescWindow(boolean show) {
        if (show != showDescWindow) {
            showDescWindow = show;
            for (AutoCompletion ac : textAreaToAutoCompletion.values()) {
                ac.setShowDescWindow(show);
            }
        }
    }

    /**
     * Unregisters a textArea. This should be called by subclasses in their
     * {@link #uninstall(net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea)}
     * methods.
     * This method will also call the <code>uninstall</code> method on the
     * <code>AutoCompletion</code>.
     *
     * @param textArea The text area.
     * @see #installImpl(SyntaxTextArea, AutoCompletion)
     */
    protected void uninstallImpl(SyntaxTextArea textArea) {
        AutoCompletion ac = getAutoCompletionFor(textArea);
        if (ac != null) {
            ac.uninstall();
        }
        textAreaToAutoCompletion.remove(textArea);
    }
}