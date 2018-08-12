/*
 * Open Teradata Viewer ( graphic viewer )
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
public class GraphicViewerOverviewBeanInfo extends SimpleBeanInfo {

    public GraphicViewerOverviewBeanInfo() {
        _fldint = GraphicViewerOverview.class;
        _flddo = "icons/logo16.png";
        _fldfor = "icons/logo32.png";
        a = "icons/logo16.png";
        _fldif = "icons/logo32.png";
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor apropertydescriptor[] = null;
        PropertyDescriptor propertydescriptor;
        try {
            propertydescriptor = new PropertyDescriptor("document", _fldint,
                    "getDocument", null);
            PropertyDescriptor propertydescriptor1 = new PropertyDescriptor(
                    "documentSize", _fldint, "getDocumentSize", null);
            PropertyDescriptor propertydescriptor2 = new PropertyDescriptor(
                    "includingNegativeCoords", _fldint,
                    "isIncludingNegativeCoords", null);
            PropertyDescriptor propertydescriptor3 = new PropertyDescriptor(
                    "observed", _fldint, "getObserved", "setObserved");
            PropertyDescriptor propertydescriptor4 = new PropertyDescriptor(
                    "overviewRect", _fldint, "getOverviewRect", null);
            apropertydescriptor = (new PropertyDescriptor[]{propertydescriptor,
                    propertydescriptor1, propertydescriptor2,
                    propertydescriptor3, propertydescriptor4});
        } catch (IntrospectionException ie) {
            ExceptionDialog.hideException(ie);
        }
        return apropertydescriptor;
    }

    public Image getIcon(int i) {
        switch (i) {
            case 1 : // '\001'
                return _flddo == null ? null : loadImage(_flddo);

            case 2 : // '\002'
                return _fldfor == null ? null : loadImage(_fldfor);

            case 3 : // '\003'
                return a == null ? null : loadImage(a);

            case 4 : // '\004'
                return _fldif == null ? null : loadImage(_fldif);
        }
        return null;
    }

    public BeanInfo[] getAdditionalBeanInfo() {
        Class<?> class1 = _fldint.getSuperclass();
        BeanInfo beaninfo = null;
        try {
            beaninfo = Introspector.getBeanInfo(class1);
        } catch (IntrospectionException ie) {
            ExceptionDialog.hideException(ie);
        }
        return (new BeanInfo[]{beaninfo});
    }

    Class<GraphicViewerOverview> _fldint;
    String _flddo;
    String _fldfor;
    String a;
    String _fldif;
}