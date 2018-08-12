/*
 * Open Teradata Viewer ( editor autocomplete )
 * Copyright (C) 2013, D. Campione
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

/**
 * A piece of a <code>TemplateCompletion</code>. You add instances of this class
 * to template completions to define them.
 *
 * @author D. Campione
 * @see TemplateCompletion
 */
interface ITemplatePiece {

    String getText();

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    public class Text implements ITemplatePiece {

        private String text;

        public Text(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public String toString() {
            return "[ITemplatePiece.Text: text=" + text + "]";
        }

    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    public class Param implements ITemplatePiece {

        String text;

        public Param(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public String toString() {
            return "[ITemplatePiece.Param: param=" + text + "]";
        }

    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    public class ParamCopy implements ITemplatePiece {

        private String text;

        public ParamCopy(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public String toString() {
            return "[ITemplatePiece.ParamCopy: param=" + text + "]";
        }
    }
}