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
 *
 *
 * @author D. Campione
 *
 */
public abstract class JavaScriptDeclaration {

    private String name;
    private int offset;
    private int start;
    private int end;
    private CodeBlock block;
    private TypeDeclarationOptions options;

    public JavaScriptDeclaration(String name, int offset, CodeBlock block) {
        this.name = name;
        this.offset = offset;
        this.block = block;
    }

    /** @return Name of the declaration. */
    public String getName() {
        return name;
    }

    /** @return Variable position within the script. */
    public int getOffset() {
        return offset;
    }

    /**
     * Gets the end offset of this declaration.
     *
     * @return The end offset.
     */
    public int getEndOffset() {
        return end;
    }

    /**
     * Sets the end offset of this declaration.
     *
     * @param end The end offset.
     * @see #getEndOffset()
     */
    public void setEndOffset(int end) {
        this.end = end;
    }

    /**
     * Sets the start offset of this declaration.
     *
     * @param start The start offset.
     * @see #getStartOffSet()
     */
    public void setStartOffset(int start) {
        this.start = start;
    }

    /**
     * Gets the start offset of this declaration.
     *
     * @return The start offset.
     */
    public int getStartOffSet() {
        return start;
    }

    /** @return CodeBlock associated with this declaration. */
    public CodeBlock getCodeBlock() {
        return block;
    }

    /**
     * Set the JavaScript options associated with this declaration.
     * These are used to defined whether options are available to.
     */
    public void setTypeDeclarationOptions(TypeDeclarationOptions options) {
        this.options = options;
    }

    /** @return The JavaScript options associated with this declaration. */
    public TypeDeclarationOptions getTypeDeclarationOptions() {
        return options;
    }
}