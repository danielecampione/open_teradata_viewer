/*
 * Open Teradata Viewer ( editor language support less )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.less;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.FunctionCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.css.PropertyValueCompletionProvider;

/**
 * The main completion provider for Less code.
 *
 * @author D. Campione
 *
 */
class LessCodeCompletionProvider extends PropertyValueCompletionProvider {

    private List<ICompletion> functionCompletions;

    /** Ctor. */
    LessCodeCompletionProvider() {
        super(true);
        try {
            this.functionCompletions = createFunctionCompletions();
        } catch (IOException ioe) { // Never happens
            throw new RuntimeException(ioe);
        }
    }

    /** Overridden to handle Less properly. */
    @Override
    protected boolean addLessCompletions(List<ICompletion> completions,
            LexerState state, JTextComponent comp, String alreadyEntered) {
        boolean modified = false;

        if (alreadyEntered != null && alreadyEntered.length() > 0
                && alreadyEntered.charAt(0) == '@') {
            addLessVariableCompletions(completions, comp, alreadyEntered);
            modified = true;
        }

        if (state == LexerState.VALUE) {
            addLessBuiltinFunctionCompletions(completions, alreadyEntered);
            modified = true;
        }

        return modified;
    }

    private void addLessBuiltinFunctionCompletions(
            List<ICompletion> completions, String alreadyEntered) {
        completions.addAll(functionCompletions);
    }

    private void addLessVariableCompletions(List<ICompletion> completions,
            JTextComponent comp, String alreadyEntered) {
    }

    private List<ICompletion> createFunctionCompletions() throws IOException {
        Icon functionIcon = loadIcon("methpub_obj");

        List<ICompletion> completions = new ArrayList<ICompletion>();
        completions = loadFromXML("res/less_functions.xml");
        for (ICompletion fc : completions) {
            ((FunctionCompletion) fc).setIcon(functionIcon);
        }

        Collections.sort(completions);
        return completions;
    }

    /**
     * Loads an icon by file name.
     * Note that, if Less completion support gets more icons, we should create
     * an IconFactory class and remove this method.
     *
     * @param name The icon file name.
     * @return The icon.
     */
    private Icon loadIcon(String name) {
        String imageFile = "img/" + name + ".gif";
        URL res = getClass().getResource(imageFile);
        if (res == null) {
            // IllegalArgumentException is what would be thrown if res was null
            // anyway, we're just giving the actual arg name to make the message
            // more descriptive
            throw new IllegalArgumentException("icon not found: " + imageFile);
        }
        return new ImageIcon(res);
    }
}