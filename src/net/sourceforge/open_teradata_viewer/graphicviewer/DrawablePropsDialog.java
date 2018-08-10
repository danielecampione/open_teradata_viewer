/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2011, D. Campione
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DrawablePropsDialog extends JDialog {

    private static final long serialVersionUID = 2951514599149081635L;

    public DrawablePropsDialog(Frame frame, String s, boolean flag,
            GraphicViewerDrawable graphicviewerdrawable) {
        super(frame, s, flag);
        panel1 = new JPanel();
        OKButton = new JButton();
        CancelButton = new JButton();
        label1 = new JLabel();
        heightField = new JTextField();
        xField = new JTextField();
        label2 = new JLabel();
        yField = new JTextField();
        label3 = new JLabel();
        visibleBox = new JCheckBox();
        selectableBox = new JCheckBox();
        resizableBox = new JCheckBox();
        draggableBox = new JCheckBox();
        label4 = new JLabel();
        widthField = new JTextField();
        brushColorButton = new JButton();
        solidBrushBox = new JCheckBox();
        penColorButton = new JButton();
        classNameLabel = new JLabel();
        penGroup = new ButtonGroup();
        solidPenButton = new JRadioButton();
        dashedPenButton = new JRadioButton();
        dottedPenButton = new JRadioButton();
        dashdotPenButton = new JRadioButton();
        dashdotdotPenButton = new JRadioButton();
        customPenButton = new JRadioButton();
        noPenButton = new JRadioButton();
        JPanel1 = new JPanel();
        label5 = new JLabel();
        label6 = new JLabel();
        penWidth = new JTextField();
        JPanel2 = new JPanel();
        label8 = new JLabel();
        label9 = new JLabel();
        label10 = new JLabel();
        shadowWidth = new JTextField();
        flapWidth = new JTextField();
        raised = new JCheckBox();
        fComponentsAdjusted = false;
        try {
            myObject = graphicviewerdrawable;
            jbInit();
            pack();
            UpdateDialog();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public DrawablePropsDialog() {
        this(null, "", false, null);
    }

    void jbInit() throws Exception {
        panel1.setLayout(null);
        panel1.setMinimumSize(new Dimension(416, 329));
        panel1.setPreferredSize(new Dimension(416, 329));
        OKButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                OKButton_actionPerformed(actionevent);
            }

        });
        CancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                CancelButton_actionPerformed(actionevent);
            }

        });
        penColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                penColorButton_actionPerformed(actionevent);
            }

        });
        brushColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                brushColorButton_actionPerformed(actionevent);
            }

        });
        OKButton.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent keyevent) {
                OKButton_keyPressed(keyevent);
            }

        });
        CancelButton.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent keyevent) {
                CancelButton_keyPressed(keyevent);
            }

        });
        OKButton.setText("OK");
        OKButton.setFont(new Font("Dialog", 0, 12));
        OKButton.setBounds(new Rectangle(106, 293, 79, 22));
        CancelButton.setText("Cancel");
        CancelButton.setFont(new Font("Dialog", 0, 12));
        CancelButton.setBounds(new Rectangle(214, 293, 79, 22));
        label1.setText("Height:");
        label1.setHorizontalAlignment(4);
        label1.setBounds(new Rectangle(132, 60, 48, 24));
        setModal(false);
        setResizable(false);
        JPanel1.setBorder(BorderFactory.createRaisedBevelBorder());
        JPanel2.setBackground(Color.lightGray);
        JPanel2.setAlignmentY(0.5F);
        JPanel2.setBorder(BorderFactory.createRaisedBevelBorder());
        panel1.add(label1);
        panel1.add(heightField);
        panel1.add(xField);
        panel1.add(label2);
        panel1.add(yField);
        panel1.add(label3);
        panel1.add(visibleBox);
        panel1.add(selectableBox);
        panel1.add(resizableBox);
        panel1.add(draggableBox);
        panel1.add(label4);
        panel1.add(widthField);
        panel1.add(solidBrushBox);
        panel1.add(classNameLabel);
        panel1.add(solidPenButton);
        panel1.add(dashedPenButton);
        panel1.add(dottedPenButton);
        panel1.add(dashdotPenButton);
        panel1.add(dashdotdotPenButton);
        panel1.add(customPenButton);
        panel1.add(noPenButton);
        panel1.add(JPanel1);
        JPanel1.add(label5);
        panel1.add(JPanel2);
        JPanel2.add(label8);
        panel1.add(CancelButton);
        panel1.add(OKButton);
        panel1.add(brushColorButton);
        panel1.add(penColorButton);
        panel1.add(label6, null);
        panel1.add(penWidth, null);
        heightField.setBounds(new Rectangle(192, 60, 36, 24));
        xField.setBounds(new Rectangle(84, 36, 36, 24));
        label2.setText("x:");
        label2.setHorizontalAlignment(4);
        label2.setBounds(new Rectangle(24, 36, 48, 24));
        yField.setBounds(new Rectangle(84, 60, 36, 24));
        label3.setText("y:");
        label3.setHorizontalAlignment(4);
        label3.setBounds(new Rectangle(24, 60, 48, 24));
        visibleBox.setText("Visible");
        visibleBox.setBounds(new Rectangle(24, 96, 72, 24));
        selectableBox.setText("Selectable");
        selectableBox.setBounds(new Rectangle(24, 120, 84, 24));
        resizableBox.setText("Resizable");
        resizableBox.setBounds(new Rectangle(24, 144, 84, 24));
        draggableBox.setText("Draggable");
        draggableBox.setBounds(new Rectangle(24, 168, 84, 24));
        label4.setText("Width:");
        label4.setHorizontalAlignment(4);
        label4.setBounds(new Rectangle(132, 36, 48, 24));
        widthField.setBounds(new Rectangle(192, 36, 36, 24));
        brushColorButton.setText("Fill Color...");
        brushColorButton.setBackground(Color.lightGray);
        brushColorButton.setBounds(new Rectangle(283, 258, 108, 24));
        solidBrushBox.setText("Solid Brush");
        solidBrushBox.setBackground(Color.lightGray);
        solidBrushBox.setBounds(new Rectangle(288, 126, 108, 24));
        penColorButton.setText("Pen Color...");
        penColorButton.setBackground(Color.lightGray);
        penColorButton.setBounds(new Rectangle(127, 258, 108, 24));
        classNameLabel.setText("class name");
        classNameLabel.setBounds(new Rectangle(26, 4, 364, 24));
        solidPenButton.setText("Solid Line Pen");
        solidPenButton.setBackground(Color.lightGray);
        penGroup.add(solidPenButton);
        solidPenButton.setBounds(new Rectangle(120, 147, 120, 12));
        dashedPenButton.setText("Dashed Line Pen");
        dashedPenButton.setBackground(Color.lightGray);
        penGroup.add(dashedPenButton);
        dashedPenButton.setBounds(new Rectangle(120, 163, 120, 12));
        dottedPenButton.setText("Dotted Line Pen");
        dottedPenButton.setBackground(Color.lightGray);
        penGroup.add(dottedPenButton);
        dottedPenButton.setBounds(new Rectangle(120, 178, 120, 12));
        dashdotPenButton.setText("Dash Dot Pen");
        dashdotPenButton.setBackground(Color.lightGray);
        penGroup.add(dashdotPenButton);
        dashdotPenButton.setBounds(new Rectangle(120, 193, 120, 12));
        dashdotdotPenButton.setText("Dash Dot Dot Pen");
        dashdotdotPenButton.setBackground(Color.lightGray);
        penGroup.add(dashdotdotPenButton);
        dashdotdotPenButton.setBounds(new Rectangle(120, 209, 120, 12));
        customPenButton.setText("Custom Pen");
        customPenButton.setBackground(Color.lightGray);
        penGroup.add(customPenButton);
        customPenButton.setBounds(new Rectangle(120, 224, 120, 12));
        noPenButton.setText("No Pen");
        noPenButton.setBackground(Color.lightGray);
        penGroup.add(noPenButton);
        noPenButton.setBounds(new Rectangle(120, 132, 120, 12));
        JPanel1.setLayout(null);
        JPanel1.setBackground(Color.lightGray);
        JPanel1.setBounds(new Rectangle(108, 96, 156, 156));
        label5.setText("Pen Properties");
        label5.setFont(new Font("Dialog", 2, 12));
        label5.setBounds(new Rectangle(35, 3, 95, 23));
        label6.setText("Pen Width:");
        label6.setBounds(new Rectangle(25, 217, 67, 27));
        penWidth.setText("1");
        penWidth.setBounds(new Rectangle(28, 240, 48, 24));
        JPanel2.setLayout(new FlowLayout(1, 5, 5));
        getContentPane().add(panel1, "South");
        JPanel2.setBounds(new Rectangle(276, 96, 132, 156));
        label8.setText("Brush Properties");
        label8.setFont(new Font("Dialog", 2, 12));
        if (myObject instanceof GraphicViewer3DNoteRect) {
            label9.setText("Shadow width:");
            label10.setText("Flap width:");
            panel1.add(label9);
            panel1.add(label10);
            label9.setBounds(new Rectangle(234, 36, 80, 24));
            label10.setBounds(new Rectangle(254, 60, 60, 24));
            panel1.add(shadowWidth);
            panel1.add(flapWidth);
            shadowWidth.setBounds(new Rectangle(326, 36, 40, 24));
            flapWidth.setBounds(new Rectangle(326, 60, 40, 24));
        }
        if (myObject instanceof GraphicViewer3DRect) {
            raised.setText("Raised?");
            panel1.add(raised);
            raised.setBounds(new Rectangle(24, 192, 84, 24));
        }
    }

    void UpdateDialog() {
        if (myObject == null)
            return;
        classNameLabel.setText(myObject.getClass().getName());
        Rectangle rectangle = myObject.getBoundingRect();
        xField.setText(String.valueOf(rectangle.x));
        yField.setText(String.valueOf(rectangle.y));
        heightField.setText(String.valueOf(rectangle.height));
        widthField.setText(String.valueOf(rectangle.width));
        visibleBox.setSelected(myObject.isVisible());
        selectableBox.setSelected(myObject.isSelectable());
        resizableBox.setSelected(myObject.isResizable());
        draggableBox.setSelected(myObject.isDraggable());
        noPenButton.setSelected(myObject.getPen().getStyle() == 0);
        solidPenButton.setSelected(myObject.getPen().getStyle() == 65535);
        dashedPenButton.setSelected(myObject.getPen().getStyle() == 1);
        dottedPenButton.setSelected(myObject.getPen().getStyle() == 2);
        dashdotPenButton.setSelected(myObject.getPen().getStyle() == 3);
        dashdotdotPenButton.setSelected(myObject.getPen().getStyle() == 4);
        customPenButton.setSelected(myObject.getPen().getStyle() == 65534);
        penWidth.setText(String.valueOf(myObject.getPen().getWidth()));
        myPenColor = myObject.getPen().getColor();
        GraphicViewerBrush graphicviewerbrush = myObject.getBrush();
        if (graphicviewerbrush != null
                && !(graphicviewerbrush.getPaint() instanceof GradientPaint)) {
            solidBrushBox.setSelected(true);
            if (myObject.getBrush().getPaint() instanceof Color)
                myBrushColor = (Color) myObject.getBrush().getPaint();
            else
                myBrushColor = Color.black;
        } else {
            solidBrushBox.setSelected(false);
            myBrushColor = Color.black;
        }
        if (myObject instanceof GraphicViewer3DNoteRect) {
            shadowWidth.setText(String
                    .valueOf(((GraphicViewer3DNoteRect) myObject)
                            .getShadowSize()));
            flapWidth
                    .setText(String
                            .valueOf(((GraphicViewer3DNoteRect) myObject)
                                    .getFlapSize()));
        }
        if (myObject instanceof GraphicViewer3DRect)
            raised.setSelected(((GraphicViewer3DRect) myObject).getState() == 0);
    }

    void UpdateControl() {
        if (myObject == null)
            return;
        Rectangle rectangle = new Rectangle(Integer.parseInt(xField.getText()),
                Integer.parseInt(yField.getText()), Integer.parseInt(widthField
                        .getText()), Integer.parseInt(heightField.getText()));
        myObject.setBoundingRect(rectangle);
        myObject.setVisible(visibleBox.isSelected());
        myObject.setSelectable(selectableBox.isSelected());
        myObject.setResizable(resizableBox.isSelected());
        myObject.setDraggable(draggableBox.isSelected());
        int i;
        if (solidPenButton.isSelected())
            i = 65535;
        else if (dashedPenButton.isSelected())
            i = 1;
        else if (dottedPenButton.isSelected())
            i = 2;
        else if (dashdotPenButton.isSelected())
            i = 3;
        else if (dashdotdotPenButton.isSelected())
            i = 4;
        else if (customPenButton.isSelected())
            i = 65534;
        else
            i = 0;
        int j = Integer.parseInt(penWidth.getText());
        Color color = myPenColor;
        myObject.setPen(GraphicViewerPen.make(i, j, color));
        if (solidBrushBox.isSelected())
            myObject.setBrush(GraphicViewerBrush.make(65535, myBrushColor));
        else if (myObject.getBrush() != null
                && !(myObject.getBrush().getPaint() instanceof GradientPaint))
            myObject.setBrush(null);
        if (myObject instanceof GraphicViewer3DNoteRect) {
            ((GraphicViewer3DNoteRect) myObject).setShadowSize(Integer
                    .parseInt(shadowWidth.getText()));
            ((GraphicViewer3DNoteRect) myObject).setFlapSize(Integer
                    .parseInt(flapWidth.getText()));
        }
        if (myObject instanceof GraphicViewer3DRect)
            if (raised.isSelected())
                ((GraphicViewer3DRect) myObject).setState(0);
            else
                ((GraphicViewer3DRect) myObject).setState(1);
    }

    public void addNotify() {
        Dimension dimension = getSize();
        super.addNotify();
        if (fComponentsAdjusted)
            return;
        Insets insets = getInsets();
        setSize(insets.left + insets.right + dimension.width, insets.top
                + insets.bottom + dimension.height);
        Component acomponent[] = getComponents();
        for (int i = 0; i < acomponent.length; i++) {
            Point point = acomponent[i].getLocation();
            point.translate(insets.left, insets.top);
            acomponent[i].setLocation(point);
        }

        fComponentsAdjusted = true;
    }

    public void setVisible(boolean flag) {
        if (flag) {
            Rectangle rectangle = getParent().getBounds();
            Rectangle rectangle1 = getBounds();
            setLocation(rectangle.x + (rectangle.width - rectangle1.width) / 2,
                    rectangle.y + (rectangle.height - rectangle1.height) / 2);
        }
        super.setVisible(flag);
    }

    void OKButton_actionPerformed(ActionEvent actionevent) {
        OnOK();
    }

    void OnOK() {
        try {
            UpdateControl();
            dispose();
        } catch (Exception exception) {
        }
    }

    void CancelButton_actionPerformed(ActionEvent actionevent) {
        OnCancel();
    }

    void OnCancel() {
        try {
            dispose();
        } catch (Exception exception) {
        }
    }

    void penColorButton_actionPerformed(ActionEvent actionevent) {
        Color color = JColorChooser.showDialog(this, "Pen Color", myPenColor);
        if (color != null)
            myPenColor = color;
    }

    void brushColorButton_actionPerformed(ActionEvent actionevent) {
        Color color = JColorChooser.showDialog(this, "Brush Color",
                myBrushColor);
        if (color != null)
            myBrushColor = color;
    }

    void OKButton_keyPressed(KeyEvent keyevent) {
        if (keyevent.getKeyCode() == 10)
            OnOK();
        else if (keyevent.getKeyCode() == 27)
            OnCancel();
    }

    void CancelButton_keyPressed(KeyEvent keyevent) {
        if (keyevent.getKeyCode() == 10)
            OnCancel();
        else if (keyevent.getKeyCode() == 27)
            OnCancel();
    }

    JPanel panel1;
    JButton OKButton;
    JButton CancelButton;
    JLabel label1;
    JTextField heightField;
    JTextField xField;
    JLabel label2;
    JTextField yField;
    JLabel label3;
    JCheckBox visibleBox;
    JCheckBox selectableBox;
    JCheckBox resizableBox;
    JCheckBox draggableBox;
    JLabel label4;
    JTextField widthField;
    JButton brushColorButton;
    JCheckBox solidBrushBox;
    JButton penColorButton;
    JLabel classNameLabel;
    ButtonGroup penGroup;
    JRadioButton solidPenButton;
    JRadioButton dashedPenButton;
    JRadioButton dottedPenButton;
    JRadioButton dashdotPenButton;
    JRadioButton dashdotdotPenButton;
    JRadioButton customPenButton;
    JRadioButton noPenButton;
    JPanel JPanel1;
    JLabel label5;
    JLabel label6;
    JTextField penWidth;
    JPanel JPanel2;
    JLabel label8;
    JLabel label9;
    JLabel label10;
    JTextField shadowWidth;
    JTextField flapWidth;
    JCheckBox raised;
    Color myBrushColor;
    Color myPenColor;
    public GraphicViewerDrawable myObject;
    boolean fComponentsAdjusted;
}