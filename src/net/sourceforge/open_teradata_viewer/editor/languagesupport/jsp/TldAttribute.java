/*
 * Open Teradata Viewer ( editor language support jsp )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.jsp;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.IParameterizedCompletion.Parameter;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.html.AttributeCompletion;

/**
 * An attribute of an element defined in a TLD.
 *
 * @author D. Campione
 * 
 */
class TldAttribute extends AttributeCompletion {

    public boolean required;
    public boolean rtexprvalue;

    public TldAttribute(JspCompletionProvider provider, TldAttributeParam param) {
        super(provider, param);
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    public static class TldAttributeParam extends Parameter {

        private boolean required;
        private boolean rtextprvalue;

        public TldAttributeParam(Object type, String name, boolean required,
                boolean rtextprvalue) {
            super(type, name);
            this.required = required;
            this.rtextprvalue = rtextprvalue;
        }

        public boolean isRequired() {
            return required;
        }

        public boolean getRtextprvalue() {
            return rtextprvalue;
        }
    }
}