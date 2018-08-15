/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import javax.swing.JDialog;
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

    private static InspectorTableModel myModel = null;

    private static JDialog myDialog = null;
    private static JTable myTable = null;
    private static boolean firstRun = true;

    public static JDialog getInspector() {
        return myDialog;
    }

    public static void inspect(Object obj) {
        try {
            if (myDialog == null) {
                myModel = new InspectorTableModel();
                myTable = new JTable(myModel);
                myTable.setPreferredScrollableViewportSize(new Dimension(300,
                        500));
                myTable.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent evt) {
                        myModel.mouseClicked(evt.getClickCount(),
                                myTable.rowAtPoint(evt.getPoint()),
                                myTable.columnAtPoint(evt.getPoint()));
                    }
                });
                JScrollPane myPane = new JScrollPane(myTable);
                JDialog dlg = new JDialog((Frame) GraphicViewer.getInstance(),
                        "Properties", false);
                dlg.getContentPane().setLayout(new BorderLayout());
                dlg.getContentPane().add(myPane, BorderLayout.CENTER);
                dlg.pack();
                myDialog = dlg;
            }
            myModel.setObject(obj);
            myTable.getColumnModel().getColumn(0).setPreferredWidth(35);
            if (!myDialog.isVisible() && !firstRun) {
                myDialog.setVisible(true);
            }
            firstRun = false;
        } catch (Exception e) {
            System.err.println(e);
            ExceptionDialog.hideException(e);
        }
    }

    public static void refresh() {
        if (myModel != null && myDialog != null && myDialog.isVisible()) {
            myModel.setObject(myModel.getObject());
        }
    }

    public static class InspectorTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 6532728079459927455L;

        private Object myObject = null;
        private ArrayList myGetters = new ArrayList();
        private ArrayList mySetters = new ArrayList();

        public InspectorTableModel() {
        }

        public Object getObject() {
            return myObject;
        }

        public void setObject(Object obj) {
            myObject = obj;
            myGetters = new ArrayList();
            mySetters = new ArrayList();
            if (myObject != null) {
                addMethods(myObject.getClass());
            }
            fireTableChanged(new TableModelEvent(this));
        }

        @Override
        public String getColumnName(int col) {
            if (col == 0) {
                return "Property";
            } else {
                return "Value";
            }
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public int getRowCount() {
            return myGetters.size();
        }

        @Override
        public Object getValueAt(int row, int col) {
            Method m = (Method) myGetters.get(row);
            if (col == 0) {
                return getPropertyName(m);
            } else {
                try {
                    Object val = m.invoke(myObject, new Object[] {});
                    if (val instanceof Point) {
                        Point p = (Point) val;
                        return Integer.toString(p.x) + ","
                                + Integer.toString(p.y);
                    } else if (val instanceof Dimension) {
                        Dimension d = (Dimension) val;
                        return Integer.toString(d.width) + "x"
                                + Integer.toString(d.height);
                    } else if (val instanceof Rectangle) {
                        Rectangle r = (Rectangle) val;
                        return Integer.toString(r.x) + ","
                                + Integer.toString(r.y) + ";"
                                + Integer.toString(r.width) + "x"
                                + Integer.toString(r.height);
                    }
                    return val;
                } catch (Exception x) {
                    return x.toString();
                }
            }
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            if (col == 1 && mySetters.get(row) != null) {
                Class type = myObject.getClass();
                if (type != Color.class && type != Insets.class
                        && type != GraphicViewerPen.class
                        && type != GraphicViewerBrush.class) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            Method setter = (Method) mySetters.get(row);
            if (setter == null) {
                return;
            }
            Class[] paramtypes = setter.getParameterTypes();
            if (paramtypes.length == 0) {
                return;
            }
            Class type = paramtypes[0];
            if (value instanceof String) {
                String v = (String) value;
                if (type == String.class) {
                    invokeSetter(setter, v);
                } else if (type == boolean.class) {
                    Boolean newval = Boolean.FALSE;
                    if (v.equals("true")) {
                        newval = Boolean.TRUE;
                    }
                    invokeSetter(setter, newval);
                } else if (type == int.class) {
                    try {
                        Integer newval = Integer.valueOf(v);
                        invokeSetter(setter, newval);
                    } catch (Exception x) {
                        UISupport.getDialogs().showErrorMessage(x.toString());
                    }
                } else if (type == float.class) {
                    try {
                        Float newval = Float.valueOf(v);
                        invokeSetter(setter, newval);
                    } catch (Exception x) {
                        UISupport.getDialogs().showErrorMessage(x.toString());
                    }
                } else if (type == double.class) {
                    try {
                        Double newval = Double.valueOf(v);
                        invokeSetter(setter, newval);
                    } catch (Exception x) {
                        UISupport.getDialogs().showErrorMessage(x.toString());
                    }
                } else if (type == Point.class) {
                    try {
                        int comma = v.indexOf(',');
                        int x = Integer.parseInt(v.substring(0, comma).trim());
                        int y = Integer.parseInt(v.substring(comma + 1).trim());
                        Point newval = new Point(x, y);
                        invokeSetter(setter, newval);
                    } catch (Exception x) {
                        UISupport.getDialogs().showErrorMessage(x.toString());
                    }
                } else if (type == Dimension.class) {
                    try {
                        int comma = v.indexOf('x');
                        int w = Integer.parseInt(v.substring(0, comma).trim());
                        int h = Integer.parseInt(v.substring(comma + 1).trim());
                        Dimension newval = new Dimension(w, h);
                        invokeSetter(setter, newval);
                    } catch (Exception x) {
                        UISupport.getDialogs().showErrorMessage(x.toString());

                    }
                } else if (type == Rectangle.class) {
                    try {
                        int comma = v.indexOf(',');
                        int x = Integer.parseInt(v.substring(0, comma).trim());
                        int semicolon = v.indexOf(';');
                        int y = Integer.parseInt(v.substring(comma + 1,
                                semicolon).trim());
                        int by = v.indexOf('x');
                        int w = Integer.parseInt(v.substring(semicolon + 1, by)
                                .trim());
                        int h = Integer.parseInt(v.substring(by + 1).trim());
                        Point newval = new Point(x, y);
                        invokeSetter(setter, newval);
                    } catch (Exception x) {
                        UISupport.getDialogs().showErrorMessage(x.toString());
                    }
                } else {
                    UISupport.getDialogs().showInfoMessage(
                            "not handled: type=" + type.toString() + " value="
                                    + v);
                }
            } else {
                UISupport.getDialogs().showInfoMessage(
                        "not handled: type=" + type.toString() + ", valuetype="
                                + value.getClass().toString() + " value="
                                + value.toString());
            }
            fireTableChanged(new TableModelEvent(this));
        }

        private void invokeSetter(Method setter, Object newval) {
            GraphicViewerDocument doc = null;
            if (myObject instanceof GraphicViewerDocument) {
                doc = (GraphicViewerDocument) myObject;
            } else if (myObject instanceof GraphicViewerObject) {
                doc = ((GraphicViewerObject) myObject).getDocument();
            } else if (myObject instanceof GraphicViewerLayer) {
                doc = ((GraphicViewerLayer) myObject).getDocument();
            } else if (myObject instanceof GraphicViewerView) {
                doc = ((GraphicViewerView) myObject).getDocument();
            } else if (myObject instanceof GraphicViewerSelection) {
                doc = ((GraphicViewerSelection) myObject).getView()
                        .getDocument();
            }
            if (doc != null) {
                doc.startTransaction();
            }
            try {
                setter.invoke(myObject, new Object[] { newval });
            } catch (Exception x) {
                UISupport.getDialogs().showErrorMessage(x.toString());
            }
            if (doc != null) {
                doc.endTransaction("modified property: " + setter.toString());
            }
        }

        public void mouseClicked(int count, int row, int column) {
            if (count >= 2) {
                Method getter = (Method) myGetters.get(row);
                Class type = getter.getReturnType();
                if (type != String.class && type != boolean.class
                        && type != int.class && type != float.class
                        && type != double.class && type != Class.class
                        && type != Point.class && type != Dimension.class
                        && type != Rectangle.class && type != Color.class
                        && type != Insets.class) {
                    Object value = getValueAt(row, 1);
                    if (value != null) {
                        setObject(value);
                    }
                }
            }
        }

        public boolean isEditableObject(Object obj) {
            if ((obj instanceof String) || (obj instanceof Boolean)
                    || (obj instanceof Integer) || (obj instanceof Float)
                    || (obj instanceof Double) || (obj instanceof Point)
                    || (obj instanceof Dimension) || (obj instanceof Rectangle)
                    || (obj instanceof Color) || (obj instanceof Insets)) {
                return true;
            }
            return false;
        }

        private void addMethods(Class c) {
            if (c.getSuperclass() != null) {
                addMethods(c.getSuperclass());
            }
            Method[] methods = c.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                Method m = methods[i];
                if (isDisplayable(m)) {
                    myGetters.add(m);
                    String setname = "set" + getPropertyName(m);
                    try {
                        Method setter = myObject.getClass().getMethod(setname,
                                new Class[] { m.getReturnType() });
                        mySetters.add(setter);
                    } catch (Exception x) {
                        mySetters.add(null);
                    }
                }
            }
        }

        private String getPropertyName(Method m) {
            String name = m.getName();
            if (name.length() > 3 && name.substring(0, 3).equals("get")) {
                return name.substring(3);
            } else if (name.length() > 2 && name.substring(0, 2).equals("is")) {
                return name.substring(2);
            } else {
                return null;
            }
        }

        private boolean isDisplayable(Method m) {
            return m.getParameterTypes().length == 0
                    && getPropertyName(m) != null
                    && Modifier.isPublic(m.getModifiers());
        }
    }
}