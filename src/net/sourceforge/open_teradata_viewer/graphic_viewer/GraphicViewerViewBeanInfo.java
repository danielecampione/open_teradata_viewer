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

    Class beanClass = GraphicViewerView.class;
    String iconColor16x16Filename = "icons/logo16.png";
    String iconColor32x32Filename = "icons/logo32.png";
    String iconMono16x16Filename = "icons/logo16.png";
    String iconMono32x32Filename = "icons/logo32.png";

    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor apropertydescriptor[] = null;
        PropertyDescriptor propertydescriptor;
        try {
            propertydescriptor = new PropertyDescriptor("autoscrollInsets",
                    beanClass, "getAutoscrollInsets", "setAutoscrollInsets");
            PropertyDescriptor propertydescriptor1 = new PropertyDescriptor(
                    "backgroundImage", beanClass, "getBackgroundImage",
                    "setBackgroundImage");
            PropertyDescriptor propertydescriptor2 = new PropertyDescriptor(
                    "bounds", beanClass, null, "setBounds");
            PropertyDescriptor propertydescriptor3 = new PropertyDescriptor(
                    "canvas", beanClass, "getCanvas", null);
            PropertyDescriptor propertydescriptor4 = new PropertyDescriptor(
                    "corner", beanClass, "getCorner", "setCorner");
            PropertyDescriptor propertydescriptor5 = new PropertyDescriptor(
                    "currentObject", beanClass, "getCurrentObject",
                    "setCurrentObject");
            PropertyDescriptor propertydescriptor6 = new PropertyDescriptor(
                    "cursor", beanClass, null, "setCursor");
            PropertyDescriptor propertydescriptor7 = new PropertyDescriptor(
                    "cursorImmediately", beanClass, null,
                    "setCursorImmediately");
            PropertyDescriptor propertydescriptor8 = new PropertyDescriptor(
                    "debugFlags", beanClass, "getDebugFlags", "setDebugFlags");
            PropertyDescriptor propertydescriptor9 = new PropertyDescriptor(
                    "defaultCursor", beanClass, "getDefaultCursor",
                    "setDefaultCursor");
            PropertyDescriptor propertydescriptor10 = new PropertyDescriptor(
                    "document", beanClass, "getDocument", "setDocument");
            PropertyDescriptor propertydescriptor11 = new PropertyDescriptor(
                    "documentSize", beanClass, "getDocumentSize", null);
            PropertyDescriptor propertydescriptor12 = new PropertyDescriptor(
                    "documentTopLeft", beanClass, "getDocumentTopLeft", null);
            PropertyDescriptor propertydescriptor13 = new PropertyDescriptor(
                    "dragDropEnabled", beanClass, "isDragDropEnabled",
                    "setDragDropEnabled");
            PropertyDescriptor propertydescriptor14 = new PropertyDescriptor(
                    "dragEnabled", beanClass, "isDragEnabled", "setDragEnabled");
            PropertyDescriptor propertydescriptor15 = new PropertyDescriptor(
                    "dragsRealtime", beanClass, "isDragsRealtime",
                    "setDragsRealtime");
            PropertyDescriptor propertydescriptor16 = new PropertyDescriptor(
                    "dragsSelectionImage", beanClass, "isDragsSelectionImage",
                    "setDragsSelectionImage");
            PropertyDescriptor propertydescriptor17 = new PropertyDescriptor(
                    "dropEnabled", beanClass, "isDropEnabled", "setDropEnabled");
            PropertyDescriptor propertydescriptor18 = new PropertyDescriptor(
                    "editControl", beanClass, "getEditControl",
                    "setEditControl");
            PropertyDescriptor propertydescriptor19 = new PropertyDescriptor(
                    "editingTextControl", beanClass, "isEditingTextControl",
                    null);
            PropertyDescriptor propertydescriptor20 = new PropertyDescriptor(
                    "empty", beanClass, "isEmpty", null);
            PropertyDescriptor propertydescriptor21 = new PropertyDescriptor(
                    "extentSize", beanClass, "getExtentSize", null);
            PropertyDescriptor propertydescriptor22 = new PropertyDescriptor(
                    "firstLayer", beanClass, "getFirstLayer", null);
            PropertyDescriptor propertydescriptor23 = new PropertyDescriptor(
                    "firstObjectPos", beanClass, "getFirstObjectPos", null);
            PropertyDescriptor propertydescriptor24 = new PropertyDescriptor(
                    "focusTraversable", beanClass, "isFocusTraversable", null);
            PropertyDescriptor propertydescriptor25 = new PropertyDescriptor(
                    "frame", beanClass, "getFrame", null);
            PropertyDescriptor propertydescriptor26 = new PropertyDescriptor(
                    "gridHeight", beanClass, "getGridHeight", "setGridHeight");
            PropertyDescriptor propertydescriptor27 = new PropertyDescriptor(
                    "gridOrigin", beanClass, "getGridOrigin", "setGridOrigin");
            PropertyDescriptor propertydescriptor28 = new PropertyDescriptor(
                    "gridPen", beanClass, "getGridPen", "setGridPen");
            PropertyDescriptor propertydescriptor29 = new PropertyDescriptor(
                    "gridSpot", beanClass, "getGridSpot", "setGridSpot");
            PropertyDescriptor propertydescriptor30 = new PropertyDescriptor(
                    "gridStyle", beanClass, "getGridStyle", "setGridStyle");
            propertydescriptor30
                    .setPropertyEditorClass(GraphicViewerGridStyleEditor.class);
            PropertyDescriptor propertydescriptor31 = new PropertyDescriptor(
                    "gridWidth", beanClass, "getGridWidth", "setGridWidth");
            PropertyDescriptor propertydescriptor32 = new PropertyDescriptor(
                    "hidingDisabledScrollbars", beanClass,
                    "isHidingDisabledScrollbars", "setHidingDisabledScrollbars");
            PropertyDescriptor propertydescriptor33 = new PropertyDescriptor(
                    "horizontalScrollBar", beanClass, "getHorizontalScrollBar",
                    "setHorizontalScrollBar");
            PropertyDescriptor propertydescriptor34 = new PropertyDescriptor(
                    "ignoreNextMouseDown", beanClass, "isIgnoreNextMouseDown",
                    "setIgnoreNextMouseDown");
            PropertyDescriptor propertydescriptor35 = new PropertyDescriptor(
                    "includingNegativeCoords", beanClass,
                    "isIncludingNegativeCoords", "setIncludingNegativeCoords");
            PropertyDescriptor propertydescriptor36 = new PropertyDescriptor(
                    "internalMouseActions", beanClass,
                    "getInternalMouseActions", "setInternalMouseActions");
            propertydescriptor36
                    .setPropertyEditorClass(GraphicViewerInternalMouseActionsEditor.class);
            PropertyDescriptor propertydescriptor37 = new PropertyDescriptor(
                    "GraphicViewerGraphics", beanClass,
                    "getGraphicViewerGraphics", null);
            PropertyDescriptor propertydescriptor38 = new PropertyDescriptor(
                    "keyEnabled", beanClass, "isKeyEnabled", "setKeyEnabled");
            PropertyDescriptor propertydescriptor39 = new PropertyDescriptor(
                    "lastLayer", beanClass, "getLastLayer", null);
            PropertyDescriptor propertydescriptor40 = new PropertyDescriptor(
                    "lastObjectPos", beanClass, "getLastObjectPos", null);
            PropertyDescriptor propertydescriptor41 = new PropertyDescriptor(
                    "minimumSize", beanClass, "getMinimumSize", null);
            PropertyDescriptor propertydescriptor42 = new PropertyDescriptor(
                    "mouseEnabled", beanClass, "isMouseEnabled",
                    "setMouseEnabled");
            PropertyDescriptor propertydescriptor43 = new PropertyDescriptor(
                    "numObjects", beanClass, "getNumObjects", null);
            PropertyDescriptor propertydescriptor44 = new PropertyDescriptor(
                    "portGravity", beanClass, "getPortGravity", null);
            PropertyDescriptor propertydescriptor45 = new PropertyDescriptor(
                    "preferredSize", beanClass, "getPreferredSize", null);
            PropertyDescriptor propertydescriptor46 = new PropertyDescriptor(
                    "primarySelectionColor", beanClass,
                    "getPrimarySelectionColor", "setPrimarySelectionColor");
            PropertyDescriptor propertydescriptor47 = new PropertyDescriptor(
                    "printDocumentSize", beanClass, "getPrintDocumentSize",
                    null);
            PropertyDescriptor propertydescriptor48 = new PropertyDescriptor(
                    "printDocumentTopLeft", beanClass,
                    "getPrintDocumentTopLeft", null);
            PropertyDescriptor propertydescriptor49 = new PropertyDescriptor(
                    "printing", beanClass, "isPrinting", null);
            PropertyDescriptor propertydescriptor50 = new PropertyDescriptor(
                    "scale", beanClass, "getScale", "setScale");
            PropertyDescriptor propertydescriptor51 = new PropertyDescriptor(
                    "secondarySelectionColor", beanClass,
                    "getSecondarySelectionColor", "setSecondarySelectionColor");
            PropertyDescriptor propertydescriptor52 = new PropertyDescriptor(
                    "selection", beanClass, "getSelection", null);
            PropertyDescriptor propertydescriptor53 = new PropertyDescriptor(
                    "size", beanClass, null, "setSize");
            PropertyDescriptor propertydescriptor54 = new PropertyDescriptor(
                    "snapMove", beanClass, "getSnapMove", "setSnapMove");
            propertydescriptor54
                    .setPropertyEditorClass(GraphicViewerSnapEditor.class);
            PropertyDescriptor propertydescriptor55 = new PropertyDescriptor(
                    "snapResize", beanClass, "getSnapResize", "setSnapResize");
            propertydescriptor55
                    .setPropertyEditorClass(GraphicViewerSnapEditor.class);
            PropertyDescriptor propertydescriptor56 = new PropertyDescriptor(
                    "state", beanClass, "getState", "setState");
            PropertyDescriptor propertydescriptor57 = new PropertyDescriptor(
                    "verticalScrollBar", beanClass, "getVerticalScrollBar",
                    "setVerticalScrollBar");
            PropertyDescriptor propertydescriptor58 = new PropertyDescriptor(
                    "viewPosition", beanClass, "getViewPosition",
                    "setViewPosition");
            PropertyDescriptor propertydescriptor59 = new PropertyDescriptor(
                    "viewRect", beanClass, "getViewRect", null);
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
                return iconColor16x16Filename == null
                        ? null
                        : loadImage(iconColor16x16Filename);

            case 2 : // '\002'
                return iconColor32x32Filename == null
                        ? null
                        : loadImage(iconColor32x32Filename);

            case 3 : // '\003'
                return iconMono16x16Filename == null
                        ? null
                        : loadImage(iconMono16x16Filename);

            case 4 : // '\004'
                return iconMono32x32Filename == null
                        ? null
                        : loadImage(iconMono32x32Filename);
        }
        return null;
    }

    public BeanInfo[] getAdditionalBeanInfo() {
        Class class1 = beanClass.getSuperclass();
        BeanInfo beaninfo = null;
        try {
            beaninfo = Introspector.getBeanInfo(class1);
        } catch (IntrospectionException ie) {
            ExceptionDialog.hideException(ie);
        }
        return (new BeanInfo[]{beaninfo});
    }
}