/*
 * Open Teradata Viewer ( editor language support xml )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.xml;

import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.ITokenTypes;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ValidationConfigSniffer {

    public IValidationConfig sniff(SyntaxDocument doc) {
        IValidationConfig config = null;

        OUTER: for (IToken token : doc) {
            switch (token.getType()) {
            case ITokenTypes.MARKUP_DTD:
                break OUTER;
            case ITokenTypes.MARKUP_TAG_NAME:
                // We only care about the first element
                break OUTER;
            }
        }

        return config;
    }

}