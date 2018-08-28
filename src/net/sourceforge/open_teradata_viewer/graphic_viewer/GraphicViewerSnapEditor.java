/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C), D. Campione
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerSnapEditor extends PropertyEditorSupport
        implements
            PropertyEditor {

    public GraphicViewerSnapEditor() {
    }

    public String[] getTags() {
        String as[] = {"No Snap", "Snap After", "Snap Jump"};
        return as;
    }

    public String getAsText() {
        Integer integer = (Integer) getValue();
        switch (integer.intValue()) {
            case 0 : // '\0'
                return "No Snap";

            case 2 : // '\002'
                return "Snap After";

            case 1 : // '\001'
                return "Snap Jump";
        }
        return "No Snap";
    }

    public void setAsText(String s) throws IllegalArgumentException {
        if (s.equals("No Snap"))
            setValue(new Integer(0));
        if (s.equals("Snap After"))
            setValue(new Integer(2));
        if (s.equals("Snap Jump"))
            setValue(new Integer(1));
    }

    public String getJavaInitializationString() {
        Integer integer = (Integer) getValue();
        switch (integer.intValue()) {
            case 0 : // '\0'
                return "GraphicViewerView.NoSnap";

            case 2 : // '\002'
                return "GraphicViewerView.SnapAfter";

            case 1 : // '\001'
                return "GraphicViewerView.SnapJump";
        }
        return "GraphicViewerView.NoSnap";
    }
}