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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.UISupport;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Inspector {

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    public static class InspectorTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 6532728079459927455L;

        public Object getObject() {
            return myObject;
        }

        public void setObject(Object obj) {
            myObject = obj;
            myGetters = new ArrayList<Method>();
            mySetters = new ArrayList<Method>();
            if (myObject != null)
                addMethods(myObject.getClass());
            fireTableChanged(new TableModelEvent(this));
        }

        public String getColumnName(int i) {
            if (i == 0)
                return "Property";
            else
                return "Value";
        }

        public int getColumnCount() {
            return 2;
        }

        public int getRowCount() {
            return myGetters.size();
        }

        public Object getValueAt(int i, int j) {
            Method method;
            method = (Method) myGetters.get(i);
            if (j == 0)
                return getPropertyName(method);
            Object obj = null;
            Object obj1;
            try {
                obj = method.invoke(myObject, new Object[0]);
            } catch (IllegalArgumentException iae) {
                ExceptionDialog.hideException(iae);
                return obj;
            } catch (IllegalAccessException iae) {
                ExceptionDialog.hideException(iae);
                return obj;
            } catch (InvocationTargetException ite) {
                ExceptionDialog.hideException(ite);
                return obj;
            }
            if (obj instanceof Point) {
                obj1 = (Point) obj;
                return Integer.toString(((Point) (obj1)).x) + ","
                        + Integer.toString(((Point) (obj1)).y);
            }
            if (obj instanceof Dimension) {
                obj1 = (Dimension) obj;
                return Integer.toString(((Dimension) (obj1)).width) + "x"
                        + Integer.toString(((Dimension) (obj1)).height);
            }
            if (obj instanceof Rectangle) {
                obj1 = (Rectangle) obj;
                return Integer.toString(((Rectangle) (obj1)).x) + ","
                        + Integer.toString(((Rectangle) (obj1)).y) + ";"
                        + Integer.toString(((Rectangle) (obj1)).width) + "x"
                        + Integer.toString(((Rectangle) (obj1)).height);
            }
            return obj;
        }

        public boolean isCellEditable(int i, int j) {
            if (j == 1 && mySetters.get(i) != null) {
                Class<?> class1 = myObject.getClass();
                if (class1 != (Inspector.class$java$awt$Color != null
                        ? Inspector.class$java$awt$Color
                        : (Inspector.class$java$awt$Color = Inspector
                                ._mthclass$("java.awt.Color")))
                        && class1 != (Inspector.class$java$awt$Insets != null
                                ? Inspector.class$java$awt$Insets
                                : (Inspector.class$java$awt$Insets = Inspector
                                        ._mthclass$("java.awt.Insets")))
                        && class1 != (Inspector.class$net$sourceforge$open_teradata_viewer$graphicviewer$GraphicViewerPen != null
                                ? Inspector.class$net$sourceforge$open_teradata_viewer$graphicviewer$GraphicViewerPen
                                : (Inspector.class$net$sourceforge$open_teradata_viewer$graphicviewer$GraphicViewerPen = Inspector
                                        ._mthclass$("net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerPen")))
                        && class1 != (Inspector.class$net$sourceforge$open_teradata_viewer$graphicviewer$GraphicViewerBrush != null
                                ? Inspector.class$net$sourceforge$open_teradata_viewer$graphicviewer$GraphicViewerBrush
                                : (Inspector.class$net$sourceforge$open_teradata_viewer$graphicviewer$GraphicViewerBrush = Inspector
                                        ._mthclass$("net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerBrush"))))
                    return true;
            }
            return false;
        }

        public void setValueAt(Object obj, int i, int j) {
            Method method = (Method) mySetters.get(i);
            if (method == null)
                return;
            Class<?> aclass[] = method.getParameterTypes();
            if (aclass.length == 0)
                return;
            Class<?> class1 = aclass[0];
            if (obj instanceof String) {
                String s = (String) obj;
                if (class1 == (Inspector.class$java$lang$String != null
                        ? Inspector.class$java$lang$String
                        : (Inspector.class$java$lang$String = Inspector
                                ._mthclass$("java.lang.String"))))
                    invokeSetter(method, s);
                else if (class1 == Boolean.TYPE) {
                    Boolean boolean1 = Boolean.FALSE;
                    if (s.equals("true"))
                        boolean1 = Boolean.TRUE;
                    invokeSetter(method, boolean1);
                } else if (class1 == Integer.TYPE)
                    try {
                        Integer integer = Integer.valueOf(s);
                        invokeSetter(method, integer);
                    } catch (Exception e) {
                        String msg = (e.getMessage() == null)
                                ? e.toString()
                                : e.getMessage();
                        UISupport.getDialogs().showErrorMessage(msg);
                    }
                else if (class1 == Float.TYPE)
                    try {
                        Float float1 = Float.valueOf(s);
                        invokeSetter(method, float1);
                    } catch (Exception e) {
                        String msg = (e.getMessage() == null)
                                ? e.toString()
                                : e.getMessage();
                        UISupport.getDialogs().showErrorMessage(msg);
                    }
                else if (class1 == Double.TYPE)
                    try {
                        Double double1 = Double.valueOf(s);
                        invokeSetter(method, double1);
                    } catch (Exception e) {
                        String msg = (e.getMessage() == null)
                                ? e.toString()
                                : e.getMessage();
                        UISupport.getDialogs().showErrorMessage(msg);
                    }
                else if (class1 == (Inspector.class$java$awt$Point != null
                        ? Inspector.class$java$awt$Point
                        : (Inspector.class$java$awt$Point = Inspector
                                ._mthclass$("java.awt.Point"))))
                    try {
                        int k = s.indexOf(',');
                        int j1 = Integer.parseInt(s.substring(0, k).trim());
                        int i2 = Integer.parseInt(s.substring(k + 1).trim());
                        Point point = new Point(j1, i2);
                        invokeSetter(method, point);
                    } catch (Exception e) {
                        String msg = (e.getMessage() == null)
                                ? e.toString()
                                : e.getMessage();
                        UISupport.getDialogs().showErrorMessage(msg);
                    }
                else if (class1 == (Inspector.class$java$awt$Dimension != null
                        ? Inspector.class$java$awt$Dimension
                        : (Inspector.class$java$awt$Dimension = Inspector
                                ._mthclass$("java.awt.Dimension"))))
                    try {
                        int l = s.indexOf('x');
                        int k1 = Integer.parseInt(s.substring(0, l).trim());
                        int j2 = Integer.parseInt(s.substring(l + 1).trim());
                        Dimension dimension = new Dimension(k1, j2);
                        invokeSetter(method, dimension);
                    } catch (Exception e) {
                        String msg = (e.getMessage() == null)
                                ? e.toString()
                                : e.getMessage();
                        UISupport.getDialogs().showErrorMessage(msg);
                    }
                else if (class1 == (Inspector.class$java$awt$Rectangle != null
                        ? Inspector.class$java$awt$Rectangle
                        : (Inspector.class$java$awt$Rectangle = Inspector
                                ._mthclass$("java.awt.Rectangle"))))
                    try {
                        int i1 = s.indexOf(',');
                        int l1 = Integer.parseInt(s.substring(0, i1).trim());
                        int k2 = s.indexOf(';');
                        int l2 = Integer.parseInt(s.substring(i1 + 1, k2)
                                .trim());
                        int i3 = s.indexOf('x');
                        Integer.parseInt(s.substring(k2 + 1, i3).trim());
                        Integer.parseInt(s.substring(i3 + 1).trim());
                        Point point1 = new Point(l1, l2);
                        invokeSetter(method, point1);
                    } catch (Exception e) {
                        String msg = (e.getMessage() == null)
                                ? e.toString()
                                : e.getMessage();
                        UISupport.getDialogs().showErrorMessage(msg);
                    }
                else {
                    String msg = "not handled: type=" + class1.toString()
                            + " value=" + s;
                    UISupport.getDialogs().showInfoMessage(msg);
                }
            } else {
                String msg = "not handled: type=" + class1.toString()
                        + ", valuetype=" + obj.getClass().toString()
                        + " value=" + obj.toString();
                UISupport.getDialogs().showInfoMessage(msg);
            }
            fireTableChanged(new TableModelEvent(this));
        }

        private void invokeSetter(Method method, Object obj) {
            GraphicViewerDocument graphicviewerdocument = null;
            if (myObject instanceof GraphicViewerDocument)
                graphicviewerdocument = (GraphicViewerDocument) myObject;
            else if (myObject instanceof GraphicViewerObject)
                graphicviewerdocument = ((GraphicViewerObject) myObject)
                        .getDocument();
            else if (myObject instanceof GraphicViewerLayer)
                graphicviewerdocument = ((GraphicViewerLayer) myObject)
                        .getDocument();
            else if (myObject instanceof GraphicViewerView)
                graphicviewerdocument = ((GraphicViewerView) myObject)
                        .getDocument();
            else if (myObject instanceof GraphicViewerSelection)
                graphicviewerdocument = ((GraphicViewerSelection) myObject)
                        .getView().getDocument();
            if (graphicviewerdocument != null)
                graphicviewerdocument.startTransaction();
            try {
                method.invoke(myObject, new Object[]{obj});
            } catch (Exception e) {
                String msg = (e.getMessage() == null) ? e.toString() : e
                        .getMessage();
                UISupport.getDialogs().showErrorMessage(msg);
            }
            if (graphicviewerdocument != null)
                graphicviewerdocument.endTransaction("modified property: "
                        + method.toString());
        }

        public void mouseClicked(int i, int j, int k) {
            if (i >= 2) {
                Method method = (Method) myGetters.get(j);
                Class<?> class1 = method.getReturnType();
                if (class1 != (Inspector.class$java$lang$String != null
                        ? Inspector.class$java$lang$String
                        : (Inspector.class$java$lang$String = Inspector
                                ._mthclass$("java.lang.String")))
                        && (class1 != Boolean.TYPE && class1 != Integer.TYPE
                                && class1 != Float.TYPE && class1 != Double.TYPE)
                        && class1 != (Inspector.class$java$lang$Class != null
                                ? Inspector.class$java$lang$Class
                                : (Inspector.class$java$lang$Class = Inspector
                                        ._mthclass$("java.lang.Class")))
                        && class1 != (Inspector.class$java$awt$Point != null
                                ? Inspector.class$java$awt$Point
                                : (Inspector.class$java$awt$Point = Inspector
                                        ._mthclass$("java.awt.Point")))
                        && class1 != (Inspector.class$java$awt$Dimension != null
                                ? Inspector.class$java$awt$Dimension
                                : (Inspector.class$java$awt$Dimension = Inspector
                                        ._mthclass$("java.awt.Dimension")))
                        && class1 != (Inspector.class$java$awt$Rectangle != null
                                ? Inspector.class$java$awt$Rectangle
                                : (Inspector.class$java$awt$Rectangle = Inspector
                                        ._mthclass$("java.awt.Rectangle")))
                        && class1 != (Inspector.class$java$awt$Color != null
                                ? Inspector.class$java$awt$Color
                                : (Inspector.class$java$awt$Color = Inspector
                                        ._mthclass$("java.awt.Color")))
                        && class1 != (Inspector.class$java$awt$Insets != null
                                ? Inspector.class$java$awt$Insets
                                : (Inspector.class$java$awt$Insets = Inspector
                                        ._mthclass$("java.awt.Insets")))) {
                    Object obj = getValueAt(j, 1);
                    if (obj != null)
                        setObject(obj);
                }
            }
        }

        public boolean isEditableObject(Object obj) {
            return (obj instanceof String) || (obj instanceof Boolean)
                    || (obj instanceof Integer) || (obj instanceof Float)
                    || (obj instanceof Double) || (obj instanceof Point)
                    || (obj instanceof Dimension) || (obj instanceof Rectangle)
                    || (obj instanceof Color) || (obj instanceof Insets);
        }

        private void addMethods(Class<?> class1) {
            if (class1.getSuperclass() != null)
                addMethods(class1.getSuperclass());
            Method amethod[] = class1.getDeclaredMethods();
            for (int i = 0; i < amethod.length; i++) {
                Method method = amethod[i];
                if (!isDisplayable(method))
                    continue;
                myGetters.add(method);
                String s = "set" + getPropertyName(method);
                try {
                    Method method1 = myObject.getClass().getMethod(s,
                            new Class[]{method.getReturnType()});
                    mySetters.add(method1);
                } catch (Exception e) {
                    mySetters.add(null);
                }
            }

        }

        private String getPropertyName(Method method) {
            String s = method.getName();
            if (s.length() > 3 && s.substring(0, 3).equals("get"))
                return s.substring(3);
            if (s.length() > 2 && s.substring(0, 2).equals("is"))
                return s.substring(2);
            else
                return null;
        }

        private boolean isDisplayable(Method method) {
            return method.getParameterTypes().length == 0
                    && getPropertyName(method) != null
                    && Modifier.isPublic(method.getModifiers());
        }

        private Object myObject;
        private ArrayList<Method> myGetters;
        private ArrayList<Method> mySetters;

        public InspectorTableModel() {
            myObject = null;
            myGetters = new ArrayList<Method>();
            mySetters = new ArrayList<Method>();
        }
    }

    public Inspector() {
    }

    public static void inspect(Object obj) {
        try {
            if (myDialog == null) {
                myModel = new InspectorTableModel();
                myTable = new JTable(myModel);
                myTable.setPreferredScrollableViewportSize(new Dimension(500,
                        150));
                myTable.addMouseListener(new MouseAdapter() {

                    public void mouseClicked(MouseEvent mouseevent) {
                        Inspector.myModel.mouseClicked(mouseevent
                                .getClickCount(), Inspector.myTable
                                .rowAtPoint(mouseevent.getPoint()),
                                Inspector.myTable.columnAtPoint(mouseevent
                                        .getPoint()));
                    }

                });
                JScrollPane jscrollpane = new JScrollPane(myTable);
                JDialog jdialog = new JDialog(
                        (JFrame) GraphicViewer.getInstance(), "Properties",
                        false);
                jdialog.getContentPane().setLayout(new BorderLayout());
                jdialog.getContentPane().add(jscrollpane, "Center");
                jdialog.pack();
                myDialog = jdialog;
            }
            myModel.setObject(obj);
            myTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
    }

    public static void refresh() {
        if (myModel != null && myDialog != null)
            myModel.setObject(myModel.getObject());
    }

    public static JDialog getInspector() {
        return myDialog;
    }

    static Class<?> _mthclass$(String s) {
        try {
            return Class.forName(s);
        } catch (ClassNotFoundException cnfe) {
            ExceptionDialog.hideException(cnfe);
            return null;
        }
    }

    private static InspectorTableModel myModel = null;
    private static JDialog myDialog = null;
    private static JTable myTable = null;

    static Class<?> class$java$awt$Color; /* synthetic field */

    static Class<?> class$java$awt$Insets; /* synthetic field */

    static Class<?> class$net$sourceforge$open_teradata_viewer$graphicviewer$GraphicViewerPen; /* synthetic field */

    static Class<?> class$net$sourceforge$open_teradata_viewer$graphicviewer$GraphicViewerBrush; /* synthetic field */

    static Class<?> class$java$lang$String; /* synthetic field */

    static Class<?> class$java$awt$Point; /* synthetic field */

    static Class<?> class$java$awt$Dimension; /* synthetic field */

    static Class<?> class$java$awt$Rectangle; /* synthetic field */

    static Class<?> class$java$lang$Class; /* synthetic field */

}