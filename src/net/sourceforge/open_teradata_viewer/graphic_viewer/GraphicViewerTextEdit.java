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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.text.JTextComponent;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerTextEdit extends GraphicViewerControl {

    private static final long serialVersionUID = -8723751771146279882L;

    private boolean myMultiline;
    private String myOriginalText;
    private int myStyle;
    private GraphicViewerText myTextObject;

    public GraphicViewerTextEdit() {
        myOriginalText = "";
        myMultiline = false;
        myTextObject = null;
    }

    public GraphicViewerTextEdit(Point point, Dimension dimension, String s,
            boolean flag, GraphicViewerText graphicviewertext) {
        super(point, dimension);
        myOriginalText = s;
        myTextObject = graphicviewertext;
        myMultiline = flag;
    }

    public GraphicViewerTextEdit(Rectangle rectangle, String s, boolean flag,
            GraphicViewerText graphicviewertext) {
        super(rectangle);
        myOriginalText = s;
        myTextObject = graphicviewertext;
        myMultiline = flag;
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerTextEdit graphicviewertextedit = (GraphicViewerTextEdit) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewertextedit != null) {
            graphicviewertextedit.myMultiline = myMultiline;
            graphicviewertextedit.myOriginalText = myOriginalText;
            graphicviewertextedit.myTextObject = (GraphicViewerText) graphicviewercopyenvironment
                    .get(myTextObject);
            graphicviewertextedit.myStyle = myStyle;
        }
        return graphicviewertextedit;
    }

    public GraphicViewerText getTextObject() {
        return myTextObject;
    }

    public void setTextObject(GraphicViewerText graphicviewertext) {
        myTextObject = graphicviewertext;
    }

    public JComponent createComponent(GraphicViewerView graphicviewerview) {
        if (myMultiline) {
            GraphicViewerJTextArea graphicviewerjtextarea = new GraphicViewerJTextArea(
                    myOriginalText, this, graphicviewerview);
            return new JScrollPane(graphicviewerjtextarea);
        } else {
            GraphicViewerJTextField graphicviewerjtextfield = new GraphicViewerJTextField(
                    myOriginalText, this, graphicviewerview);
            return graphicviewerjtextfield;
        }
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        JComponent jcomponent = getComponent(graphicviewerview);
        if (jcomponent != null) {
            JTextComponent jtextcomponent = getTextComponent(graphicviewerview);
            if (jtextcomponent != null) {
                Font font = getTextObject().getFont();
                if (Math.abs(graphicviewerview.getScale() - 1.0D) >= 0.01D) {
                    String s = font.getFontName();
                    int i = font.getSize();
                    int j = font.getStyle();
                    font = new Font(s, j,
                            (int) ((double) i * graphicviewerview.getScale()));
                }
                jtextcomponent.setFont(font);
            }
            Rectangle rectangle = graphicviewerview.getTempRectangle();
            Rectangle rectangle1 = getBoundingRect();
            rectangle.x = rectangle1.x - 4;
            rectangle.y = rectangle1.y - 1;
            rectangle.width = rectangle1.width + 12;
            rectangle.height = rectangle1.height + 4;
            graphicviewerview.convertDocToView(rectangle);
            rectangle.x -= 11;
            rectangle.width += 22;
            if (jcomponent instanceof JScrollPane) {
                rectangle.y -= 11;
                rectangle.height += 22;
            }
            jcomponent.setBounds(rectangle);
        }
    }

    public JTextComponent getTextComponent(GraphicViewerView graphicviewerview) {
        JComponent jcomponent = getComponent(graphicviewerview);
        JTextComponent jtextcomponent = null;
        if (jcomponent instanceof JTextComponent) {
            jtextcomponent = (JTextComponent) jcomponent;
        } else if (jcomponent instanceof JScrollPane) {
            JScrollPane jscrollpane = (JScrollPane) jcomponent;
            if (jscrollpane.getViewport().getView() instanceof JTextComponent) {
                jtextcomponent = (JTextComponent) jscrollpane.getViewport()
                        .getView();
            }
        }
        return jtextcomponent;
    }

    public boolean setEditedText(String s, GraphicViewerView graphicviewerview) {
        return getTextObject().doEdit(graphicviewerview,
                getTextObject().getText(), s);
    }

    public void doEndEdit() {
        getTextObject().doEndEdit();
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    class GraphicViewerJTextArea extends JTextArea {

        private static final long serialVersionUID = -2021395713134577406L;

        GraphicViewerTextEdit myTextEdit;
        GraphicViewerView myView;

        GraphicViewerJTextArea(String s,
                GraphicViewerTextEdit graphicviewertextedit1,
                GraphicViewerView graphicviewerview) {
            super(s);
            myTextEdit = graphicviewertextedit1;
            myView = graphicviewerview;
            setBorder(new CompoundBorder(new EtchedBorder(1),
                    new BevelBorder(1)));
            enableEvents(12L);
            setLineWrap(myTextEdit.getTextObject().isWrapping());
        }

        protected void processKeyEvent(KeyEvent keyevent) {
            GraphicViewerTextEdit graphicviewertextedit = myTextEdit;
            if (keyevent.getID() == 401 && keyevent.getKeyCode() == 10
                    && graphicviewertextedit != null && getLineWrap()) {
                myTextEdit = null;
                if (graphicviewertextedit.setEditedText(getText(), myView)) {
                    graphicviewertextedit.doEndEdit();
                } else {
                    myTextEdit = graphicviewertextedit;
                }
                return;
            }
            if (keyevent.getID() == 401 && keyevent.getKeyCode() == 27
                    && graphicviewertextedit != null) {
                myTextEdit = null;
                graphicviewertextedit.doEndEdit();
                return;
            } else {
                super.processKeyEvent(keyevent);
                return;
            }
        }

        protected void processFocusEvent(FocusEvent focusevent) {
            GraphicViewerTextEdit graphicviewertextedit = myTextEdit;
            if (focusevent.getID() == 1005 && graphicviewertextedit != null) {
                myTextEdit = null;
                if (graphicviewertextedit.setEditedText(getText(), myView)) {
                    graphicviewertextedit.doEndEdit();
                } else {
                    myTextEdit = graphicviewertextedit;
                }
            }
            super.processFocusEvent(focusevent);
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    class GraphicViewerJTextField extends JTextField {

        private static final long serialVersionUID = -838000637426911997L;

        GraphicViewerTextEdit myTextEdit;
        GraphicViewerView myView;

        GraphicViewerJTextField(String s,
                GraphicViewerTextEdit graphicviewertextedit1,
                GraphicViewerView graphicviewerview) {
            super(s);
            myTextEdit = graphicviewertextedit1;
            myView = graphicviewerview;
            enableEvents(12L);
            switch (graphicviewertextedit1.getTextObject().getAlignment()) {
                case 1 : // '\001'
                    setHorizontalAlignment(2);
                    break;

                case 2 : // '\002'
                    setHorizontalAlignment(0);
                    break;

                case 3 : // '\003'
                    setHorizontalAlignment(4);
                    break;
            }
        }

        protected void processKeyEvent(KeyEvent keyevent) {
            GraphicViewerTextEdit graphicviewertextedit = myTextEdit;
            if (keyevent.getID() == 401 && keyevent.getKeyCode() == 10
                    && graphicviewertextedit != null) {
                myTextEdit = null;
                if (graphicviewertextedit.setEditedText(getText(), myView)) {
                    graphicviewertextedit.doEndEdit();
                } else {
                    myTextEdit = graphicviewertextedit;
                }
                return;
            }
            if (keyevent.getID() == 401 && keyevent.getKeyCode() == 27
                    && graphicviewertextedit != null) {
                myTextEdit = null;
                graphicviewertextedit.doEndEdit();
                return;
            } else {
                super.processKeyEvent(keyevent);
                return;
            }
        }

        protected void processFocusEvent(FocusEvent focusevent) {
            GraphicViewerTextEdit graphicviewertextedit = myTextEdit;
            if (focusevent.getID() == 1005 && graphicviewertextedit != null) {
                myTextEdit = null;
                if (graphicviewertextedit.setEditedText(getText(), myView)) {
                    graphicviewertextedit.doEndEdit();
                } else {
                    myTextEdit = graphicviewertextedit;
                }
            }
            super.processFocusEvent(focusevent);
        }
    }
}