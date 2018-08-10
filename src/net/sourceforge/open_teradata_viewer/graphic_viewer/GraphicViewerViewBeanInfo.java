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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerViewBeanInfo extends SimpleBeanInfo {

    public GraphicViewerViewBeanInfo() {
        _fldfor = GraphicViewerView.class;
        a = "icons/logo16.png";
        _flddo = "icons/logo32.png";
        _fldif = "icons/logo16.png";
        _fldint = "icons/logo32.png";
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor apropertydescriptor[] = null;
        PropertyDescriptor propertydescriptor;
        try {
            propertydescriptor = new PropertyDescriptor("autoscrollInsets",
                    _fldfor, "getAutoscrollInsets", "setAutoscrollInsets");
            PropertyDescriptor propertydescriptor1 = new PropertyDescriptor(
                    "backgroundImage", _fldfor, "getBackgroundImage",
                    "setBackgroundImage");
            PropertyDescriptor propertydescriptor2 = new PropertyDescriptor(
                    "bounds", _fldfor, null, "setBounds");
            PropertyDescriptor propertydescriptor3 = new PropertyDescriptor(
                    "canvas", _fldfor, "getCanvas", null);
            PropertyDescriptor propertydescriptor4 = new PropertyDescriptor(
                    "corner", _fldfor, "getCorner", "setCorner");
            PropertyDescriptor propertydescriptor5 = new PropertyDescriptor(
                    "currentObject", _fldfor, "getCurrentObject",
                    "setCurrentObject");
            PropertyDescriptor propertydescriptor6 = new PropertyDescriptor(
                    "cursor", _fldfor, null, "setCursor");
            PropertyDescriptor propertydescriptor7 = new PropertyDescriptor(
                    "cursorImmediately", _fldfor, null, "setCursorImmediately");
            PropertyDescriptor propertydescriptor8 = new PropertyDescriptor(
                    "debugFlags", _fldfor, "getDebugFlags", "setDebugFlags");
            PropertyDescriptor propertydescriptor9 = new PropertyDescriptor(
                    "defaultCursor", _fldfor, "getDefaultCursor",
                    "setDefaultCursor");
            PropertyDescriptor propertydescriptor10 = new PropertyDescriptor(
                    "document", _fldfor, "getDocument", "setDocument");
            PropertyDescriptor propertydescriptor11 = new PropertyDescriptor(
                    "documentSize", _fldfor, "getDocumentSize", null);
            PropertyDescriptor propertydescriptor12 = new PropertyDescriptor(
                    "documentTopLeft", _fldfor, "getDocumentTopLeft", null);
            PropertyDescriptor propertydescriptor13 = new PropertyDescriptor(
                    "dragDropEnabled", _fldfor, "isDragDropEnabled",
                    "setDragDropEnabled");
            PropertyDescriptor propertydescriptor14 = new PropertyDescriptor(
                    "dragEnabled", _fldfor, "isDragEnabled", "setDragEnabled");
            PropertyDescriptor propertydescriptor15 = new PropertyDescriptor(
                    "dragsRealtime", _fldfor, "isDragsRealtime",
                    "setDragsRealtime");
            PropertyDescriptor propertydescriptor16 = new PropertyDescriptor(
                    "dragsSelectionImage", _fldfor, "isDragsSelectionImage",
                    "setDragsSelectionImage");
            PropertyDescriptor propertydescriptor17 = new PropertyDescriptor(
                    "dropEnabled", _fldfor, "isDropEnabled", "setDropEnabled");
            PropertyDescriptor propertydescriptor18 = new PropertyDescriptor(
                    "editControl", _fldfor, "getEditControl", "setEditControl");
            PropertyDescriptor propertydescriptor19 = new PropertyDescriptor(
                    "editingTextControl", _fldfor, "isEditingTextControl", null);
            PropertyDescriptor propertydescriptor20 = new PropertyDescriptor(
                    "empty", _fldfor, "isEmpty", null);
            PropertyDescriptor propertydescriptor21 = new PropertyDescriptor(
                    "extentSize", _fldfor, "getExtentSize", null);
            PropertyDescriptor propertydescriptor22 = new PropertyDescriptor(
                    "firstLayer", _fldfor, "getFirstLayer", null);
            PropertyDescriptor propertydescriptor23 = new PropertyDescriptor(
                    "firstObjectPos", _fldfor, "getFirstObjectPos", null);
            PropertyDescriptor propertydescriptor24 = new PropertyDescriptor(
                    "focusTraversable", _fldfor, "isFocusTraversable", null);
            PropertyDescriptor propertydescriptor25 = new PropertyDescriptor(
                    "frame", _fldfor, "getFrame", null);
            PropertyDescriptor propertydescriptor26 = new PropertyDescriptor(
                    "gridHeight", _fldfor, "getGridHeight", "setGridHeight");
            PropertyDescriptor propertydescriptor27 = new PropertyDescriptor(
                    "gridOrigin", _fldfor, "getGridOrigin", "setGridOrigin");
            PropertyDescriptor propertydescriptor28 = new PropertyDescriptor(
                    "gridPen", _fldfor, "getGridPen", "setGridPen");
            PropertyDescriptor propertydescriptor29 = new PropertyDescriptor(
                    "gridSpot", _fldfor, "getGridSpot", "setGridSpot");
            PropertyDescriptor propertydescriptor30 = new PropertyDescriptor(
                    "gridStyle", _fldfor, "getGridStyle", "setGridStyle");
            propertydescriptor30
                    .setPropertyEditorClass(GraphicViewerGridStyleEditor.class);
            PropertyDescriptor propertydescriptor31 = new PropertyDescriptor(
                    "gridWidth", _fldfor, "getGridWidth", "setGridWidth");
            PropertyDescriptor propertydescriptor32 = new PropertyDescriptor(
                    "hidingDisabledScrollbars", _fldfor,
                    "isHidingDisabledScrollbars", "setHidingDisabledScrollbars");
            PropertyDescriptor propertydescriptor33 = new PropertyDescriptor(
                    "horizontalScrollBar", _fldfor, "getHorizontalScrollBar",
                    "setHorizontalScrollBar");
            PropertyDescriptor propertydescriptor34 = new PropertyDescriptor(
                    "ignoreNextMouseDown", _fldfor, "isIgnoreNextMouseDown",
                    "setIgnoreNextMouseDown");
            PropertyDescriptor propertydescriptor35 = new PropertyDescriptor(
                    "includingNegativeCoords", _fldfor,
                    "isIncludingNegativeCoords", "setIncludingNegativeCoords");
            PropertyDescriptor propertydescriptor36 = new PropertyDescriptor(
                    "internalMouseActions", _fldfor, "getInternalMouseActions",
                    "setInternalMouseActions");
            propertydescriptor36
                    .setPropertyEditorClass(GraphicViewerInternalMouseActionsEditor.class);
            PropertyDescriptor propertydescriptor37 = new PropertyDescriptor(
                    "GraphicViewerGraphics", _fldfor,
                    "getGraphicViewerGraphics", null);
            PropertyDescriptor propertydescriptor38 = new PropertyDescriptor(
                    "keyEnabled", _fldfor, "isKeyEnabled", "setKeyEnabled");
            PropertyDescriptor propertydescriptor39 = new PropertyDescriptor(
                    "lastLayer", _fldfor, "getLastLayer", null);
            PropertyDescriptor propertydescriptor40 = new PropertyDescriptor(
                    "lastObjectPos", _fldfor, "getLastObjectPos", null);
            PropertyDescriptor propertydescriptor41 = new PropertyDescriptor(
                    "minimumSize", _fldfor, "getMinimumSize", null);
            PropertyDescriptor propertydescriptor42 = new PropertyDescriptor(
                    "mouseEnabled", _fldfor, "isMouseEnabled",
                    "setMouseEnabled");
            PropertyDescriptor propertydescriptor43 = new PropertyDescriptor(
                    "numObjects", _fldfor, "getNumObjects", null);
            PropertyDescriptor propertydescriptor44 = new PropertyDescriptor(
                    "portGravity", _fldfor, "getPortGravity", null);
            PropertyDescriptor propertydescriptor45 = new PropertyDescriptor(
                    "preferredSize", _fldfor, "getPreferredSize", null);
            PropertyDescriptor propertydescriptor46 = new PropertyDescriptor(
                    "primarySelectionColor", _fldfor,
                    "getPrimarySelectionColor", "setPrimarySelectionColor");
            PropertyDescriptor propertydescriptor47 = new PropertyDescriptor(
                    "printDocumentSize", _fldfor, "getPrintDocumentSize", null);
            PropertyDescriptor propertydescriptor48 = new PropertyDescriptor(
                    "printDocumentTopLeft", _fldfor, "getPrintDocumentTopLeft",
                    null);
            PropertyDescriptor propertydescriptor49 = new PropertyDescriptor(
                    "printing", _fldfor, "isPrinting", null);
            PropertyDescriptor propertydescriptor50 = new PropertyDescriptor(
                    "scale", _fldfor, "getScale", "setScale");
            PropertyDescriptor propertydescriptor51 = new PropertyDescriptor(
                    "secondarySelectionColor", _fldfor,
                    "getSecondarySelectionColor", "setSecondarySelectionColor");
            PropertyDescriptor propertydescriptor52 = new PropertyDescriptor(
                    "selection", _fldfor, "getSelection", null);
            PropertyDescriptor propertydescriptor53 = new PropertyDescriptor(
                    "size", _fldfor, null, "setSize");
            PropertyDescriptor propertydescriptor54 = new PropertyDescriptor(
                    "snapMove", _fldfor, "getSnapMove", "setSnapMove");
            propertydescriptor54
                    .setPropertyEditorClass(GraphicViewerSnapEditor.class);
            PropertyDescriptor propertydescriptor55 = new PropertyDescriptor(
                    "snapResize", _fldfor, "getSnapResize", "setSnapResize");
            propertydescriptor55
                    .setPropertyEditorClass(GraphicViewerSnapEditor.class);
            PropertyDescriptor propertydescriptor56 = new PropertyDescriptor(
                    "state", _fldfor, "getState", "setState");
            PropertyDescriptor propertydescriptor57 = new PropertyDescriptor(
                    "verticalScrollBar", _fldfor, "getVerticalScrollBar",
                    "setVerticalScrollBar");
            PropertyDescriptor propertydescriptor58 = new PropertyDescriptor(
                    "viewPosition", _fldfor, "getViewPosition",
                    "setViewPosition");
            PropertyDescriptor propertydescriptor59 = new PropertyDescriptor(
                    "viewRect", _fldfor, "getViewRect", null);
            apropertydescriptor = (new PropertyDescriptor[]{propertydescriptor,
                    propertydescriptor1, propertydescriptor2,
                    propertydescriptor3, propertydescriptor4,
                    propertydescriptor5, propertydescriptor6,
                    propertydescriptor7, propertydescriptor8,
                    propertydescriptor9, propertydescriptor10,
                    propertydescriptor11, propertydescriptor12,
                    propertydescriptor13, propertydescriptor14,
                    propertydescriptor15, propertydescriptor16,
                    propertydescriptor17, propertydescriptor18,
                    propertydescriptor19, propertydescriptor20,
                    propertydescriptor21, propertydescriptor22,
                    propertydescriptor23, propertydescriptor24,
                    propertydescriptor25, propertydescriptor26,
                    propertydescriptor27, propertydescriptor28,
                    propertydescriptor29, propertydescriptor30,
                    propertydescriptor31, propertydescriptor32,
                    propertydescriptor33, propertydescriptor34,
                    propertydescriptor35, propertydescriptor36,
                    propertydescriptor37, propertydescriptor38,
                    propertydescriptor39, propertydescriptor40,
                    propertydescriptor41, propertydescriptor42,
                    propertydescriptor43, propertydescriptor44,
                    propertydescriptor45, propertydescriptor46,
                    propertydescriptor47, propertydescriptor48,
                    propertydescriptor49, propertydescriptor50,
                    propertydescriptor51, propertydescriptor52,
                    propertydescriptor53, propertydescriptor54,
                    propertydescriptor55, propertydescriptor56,
                    propertydescriptor57, propertydescriptor58,
                    propertydescriptor59});
        } catch (IntrospectionException ie) {
            ExceptionDialog.hideException(ie);
        }
        return apropertydescriptor;
    }

    public Image getIcon(int i) {
        switch (i) {
            case 1 : // '\001'
                return a == null ? null : loadImage(a);

            case 2 : // '\002'
                return _flddo == null ? null : loadImage(_flddo);

            case 3 : // '\003'
                return _fldif == null ? null : loadImage(_fldif);

            case 4 : // '\004'
                return _fldint == null ? null : loadImage(_fldint);
        }
        return null;
    }

    public BeanInfo[] getAdditionalBeanInfo() {
        Class<?> class1 = _fldfor.getSuperclass();
        BeanInfo beaninfo = null;
        try {
            beaninfo = Introspector.getBeanInfo(class1);
        } catch (IntrospectionException ie) {
            ExceptionDialog.hideException(ie);
        }
        return (new BeanInfo[]{beaninfo});
    }

    Class<GraphicViewerView> _fldfor;
    String a;
    String _flddo;
    String _fldif;
    String _fldint;
}