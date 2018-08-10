/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2012, D. Campione
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

package net.sourceforge.open_teradata_viewer.graphicviewer;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerInternalMouseActionsEditor
        extends
            PropertyEditorSupport implements PropertyEditor {

    public GraphicViewerInternalMouseActionsEditor() {
    }

    public String[] getTags() {
        String as[] = {"ACTION_COPY_OR_MOVE", "ACTION_COPY", "ACTION_MOVE",
                "ACTION_NONE"};
        return as;
    }

    public String getAsText() {
        Integer integer = (Integer) getValue();
        switch (integer.intValue()) {
            case 3 : // '\003'
            default :
                return "ACTION_COPY_OR_MOVE";

            case 1 : // '\001'
                return "ACTION_COPY";

            case 2 : // '\002'
                return "ACTION_MOVE";

            case 0 : // '\0'
                return "ACTION_NONE";
        }
    }

    public void setAsText(String s) throws IllegalArgumentException {
        if (s.equals("ACTION_COPY_OR_MOVE"))
            setValue(new Integer(3));
        if (s.equals("ACTION_COPY"))
            setValue(new Integer(1));
        if (s.equals("ACTION_MOVE"))
            setValue(new Integer(2));
        if (s.equals("ACTION_NONE"))
            setValue(new Integer(0));
    }

    public String getJavaInitializationString() {
        Integer integer = (Integer) getValue();
        switch (integer.intValue()) {
            case 3 : // '\003'
            default :
                return "java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE";

            case 1 : // '\001'
                return "java.awt.dnd.DnDConstants.ACTION_COPY";

            case 2 : // '\002'
                return "java.awt.dnd.DnDConstants.ACTION_MOVE";

            case 0 : // '\0'
                return "java.awt.dnd.DnDConstants.ACTION_NONE";
        }
    }
}