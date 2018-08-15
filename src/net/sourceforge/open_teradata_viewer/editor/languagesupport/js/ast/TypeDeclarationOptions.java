/*
 * Open Teradata Viewer ( editor language support js ast )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast;

/**
 * Object that represents options associated with JavaScriptDeclaration. These
 * can be used to ask questions:
 * 
 * 1) Which script the JavaScriptDeclaration belongs to.
 * 2) Whether the JavaScriptDeclaration supports searchable hyperlinks.
 * 
 * @author D. Campione
 *
 */
public class TypeDeclarationOptions {

    private String ownerScriptName;
    private boolean supportsLinks;
    private boolean preProcessing;

    public TypeDeclarationOptions(String ownerScriptName,
            boolean supportsLinks, boolean preProcessing) {
        this.ownerScriptName = ownerScriptName;
        this.supportsLinks = supportsLinks;
        this.preProcessing = preProcessing;
    }

    /** @return The owner script name. */
    public String getOwnerScriptName() {
        return ownerScriptName;
    }

    /** Set the owner script name. */
    public void setOwnerScriptName(String ownerScriptName) {
        this.ownerScriptName = ownerScriptName;
    }

    /** @return Whether the type declaration supports hyperlinks. */
    public boolean isSupportsLinks() {
        return supportsLinks;
    }

    /** Set whether the type declaration supports hyperlinks. */
    public void setSupportsLinks(boolean supportsLinks) {
        this.supportsLinks = supportsLinks;
    }

    /**
     * @return Whether the type declaration has been created from a pre
     *         processed script.
     */
    public boolean isPreProcessing() {
        return preProcessing;
    }

    /**
     * Set whether the type declaration has been created from a pre processed
     * script.
     */
    public void setPreProcessing(boolean preProcessing) {
        this.preProcessing = preProcessing;
    }
}