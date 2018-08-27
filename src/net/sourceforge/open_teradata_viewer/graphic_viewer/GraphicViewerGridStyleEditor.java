/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerGridStyleEditor extends PropertyEditorSupport
        implements
            PropertyEditor {

    public GraphicViewerGridStyleEditor() {
    }

    public String[] getTags() {
        String as[] = {"Invisible", "Dot", "Cross", "Line"};
        return as;
    }

    public String getAsText() {
        Integer integer = (Integer) getValue();
        switch (integer.intValue()) {
            case 0 : // '\0'
                return "Invisible";

            case 1 : // '\001'
                return "Dot";

            case 2 : // '\002'
                return "Cross";

            case 3 : // '\003'
                return "Line";
        }
        return "Invisible";
    }

    public void setAsText(String s) throws IllegalArgumentException {
        if (s.equals("Invisible"))
            setValue(new Integer(0));
        if (s.equals("Dot"))
            setValue(new Integer(1));
        if (s.equals("Cross"))
            setValue(new Integer(2));
        if (s.equals("Line"))
            setValue(new Integer(3));
    }

    public String getJavaInitializationString() {
        Integer integer = (Integer) getValue();
        switch (integer.intValue()) {
            case 0 : // '\0'
                return "GraphicViewerView.GridInvisible";

            case 1 : // '\001'
                return "GraphicViewerView.GridDot";

            case 2 : // '\002'
                return "GraphicViewerView.GridCross";

            case 3 : // '\003'
                return "GraphicViewerView.GridLine";
        }
        return "GraphicViewerView.GridInvisible";
    }
}