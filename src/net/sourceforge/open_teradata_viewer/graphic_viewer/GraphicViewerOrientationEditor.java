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
public class GraphicViewerOrientationEditor extends PropertyEditorSupport
        implements
            PropertyEditor {

    public GraphicViewerOrientationEditor() {
    }

    public String[] getTags() {
        String as[] = {"Vertical", "Horizontal"};
        return as;
    }

    public String getAsText() {
        Integer integer = (Integer) getValue();
        switch (integer.intValue()) {
            case 1 : // '\001'
                return "Vertical";

            case 0 : // '\0'
                return "Horizontal";
        }
        return "Vertical";
    }

    public void setAsText(String s) throws IllegalArgumentException {
        if (s.equals("Vertical"))
            setValue(new Integer(1));
        if (s.equals("Horizontal"))
            setValue(new Integer(0));
    }

    public String getJavaInitializationString() {
        Integer integer = (Integer) getValue();
        switch (integer.intValue()) {
            case 1 : // '\001'
                return "GraphicViewerPalette.OrientationVertical";

            case 0 : // '\0'
                return "GraphicViewerPalette.OrientationHorizontal";
        }
        return "GraphicViewerPalette.OrientationVertical";
    }
}