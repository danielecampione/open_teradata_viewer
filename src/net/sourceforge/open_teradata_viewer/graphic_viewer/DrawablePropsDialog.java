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

import java.awt.BorderLayout;
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

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DrawablePropsDialog extends JDialog {

    private static final long serialVersionUID = 2951514599149081635L;

    JPanel panel1 = new JPanel();
    JButton OKButton = new JButton();
    JButton CancelButton = new JButton();
    JLabel label1 = new JLabel();
    JTextField heightField = new JTextField();
    JTextField xField = new JTextField();
    JLabel label2 = new JLabel();
    JTextField yField = new JTextField();
    JLabel label3 = new JLabel();
    JCheckBox visibleBox = new JCheckBox();
    JCheckBox selectableBox = new JCheckBox();
    JCheckBox resizableBox = new JCheckBox();
    JCheckBox draggableBox = new JCheckBox();
    JLabel label4 = new JLabel();
    JTextField widthField = new JTextField();
    JButton brushColorButton = new JButton();
    JCheckBox solidBrushBox = new JCheckBox();
    JButton penColorButton = new JButton();
    JLabel classNameLabel = new JLabel();
    ButtonGroup penGroup = new ButtonGroup();
    JRadioButton solidPenButton = new JRadioButton();
    JRadioButton dashedPenButton = new JRadioButton();
    JRadioButton dottedPenButton = new JRadioButton();
    JRadioButton dashdotPenButton = new JRadioButton();
    JRadioButton dashdotdotPenButton = new JRadioButton();
    JRadioButton customPenButton = new JRadioButton();
    JRadioButton noPenButton = new JRadioButton();
    JPanel JPanel1 = new JPanel();
    JLabel label5 = new JLabel();
    JLabel label6 = new JLabel();
    JTextField penWidth = new JTextField();
    JPanel JPanel2 = new JPanel();
    JLabel label8 = new JLabel();
    JLabel label9 = new JLabel();
    JLabel label10 = new JLabel();
    JTextField shadowWidth = new JTextField();
    JTextField flapWidth = new JTextField();
    JCheckBox raised = new JCheckBox();

    Color myBrushColor;
    Color myPenColor;

    public GraphicViewerDrawable myObject;

    public DrawablePropsDialog(Frame frame, String title, boolean modal,
            GraphicViewerDrawable obj) {
        super(frame, title, modal);
        try {
            myObject = obj;
            jbInit();
            pack();
            UpdateDialog();
        } catch (Exception ex) {
            ExceptionDialog.hideException(ex);
        }
    }

    public DrawablePropsDialog() {
        this(null, "", false, null);
    }

    void jbInit() throws Exception {
        panel1.setLayout(null);
        panel1.setMinimumSize(new Dimension(416, 329));
        panel1.setPreferredSize(new Dimension(416, 329));
        OKButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                OKButton_actionPerformed(e);
            }
        });
        CancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                CancelButton_actionPerformed(e);
            }
        });
        penColorButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                penColorButton_actionPerformed(e);
            }
        });
        brushColorButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                brushColorButton_actionPerformed(e);
            }
        });
        OKButton.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                OKButton_keyPressed(e);
            }
        });
        CancelButton.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                CancelButton_keyPressed(e);
            }
        });

        OKButton.setText("OK");
        OKButton.setFont(new Font("Dialog", Font.PLAIN, 12));
        OKButton.setBounds(new Rectangle(106, 293, 79, 22));
        CancelButton.setText("Cancel");
        CancelButton.setFont(new Font("Dialog", Font.PLAIN, 12));
        CancelButton.setBounds(new Rectangle(214, 293, 79, 22));
        label1.setText("Height:");
        label1.setHorizontalAlignment(JLabel.RIGHT);
        label1.setBounds(new Rectangle(132, 60, 48, 24));
        this.setModal(false);
        this.setResizable(false);
        JPanel1.setBorder(BorderFactory.createRaisedBevelBorder());
        JPanel2.setBackground(Color.lightGray);
        JPanel2.setAlignmentY((float) 0.5);
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
        label2.setHorizontalAlignment(JLabel.RIGHT);
        label2.setBounds(new Rectangle(24, 36, 48, 24));
        yField.setBounds(new Rectangle(84, 60, 36, 24));
        label3.setText("y:");
        label3.setHorizontalAlignment(JLabel.RIGHT);
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
        label4.setHorizontalAlignment(JLabel.RIGHT);
        label4.setBounds(new Rectangle(132, 36, 48, 24));
        widthField.setBounds(new Rectangle(192, 36, 36, 24));
        brushColorButton.setText("Fill Color...");
        brushColorButton.setBackground(java.awt.Color.lightGray);
        brushColorButton.setBounds(new Rectangle(283, 258, 108, 24));
        solidBrushBox.setText("Solid Brush");
        solidBrushBox.setBackground(java.awt.Color.lightGray);
        solidBrushBox.setBounds(new Rectangle(288, 126, 108, 24));
        penColorButton.setText("Pen Color...");
        penColorButton.setBackground(java.awt.Color.lightGray);
        penColorButton.setBounds(new Rectangle(127, 258, 108, 24));
        classNameLabel.setText("class name");
        classNameLabel.setBounds(new Rectangle(26, 4, 364, 24));
        solidPenButton.setText("Solid Line Pen");
        solidPenButton.setBackground(java.awt.Color.lightGray);
        penGroup.add(solidPenButton);
        solidPenButton.setBounds(new Rectangle(120, 147, 120, 12));
        dashedPenButton.setText("Dashed Line Pen");
        dashedPenButton.setBackground(java.awt.Color.lightGray);
        penGroup.add(dashedPenButton);
        dashedPenButton.setBounds(new Rectangle(120, 163, 120, 12));
        dottedPenButton.setText("Dotted Line Pen");
        dottedPenButton.setBackground(java.awt.Color.lightGray);
        penGroup.add(dottedPenButton);
        dottedPenButton.setBounds(new Rectangle(120, 178, 120, 12));
        dashdotPenButton.setText("Dash Dot Pen");
        dashdotPenButton.setBackground(java.awt.Color.lightGray);
        penGroup.add(dashdotPenButton);
        dashdotPenButton.setBounds(new Rectangle(120, 193, 120, 12));
        dashdotdotPenButton.setText("Dash Dot Dot Pen");
        dashdotdotPenButton.setBackground(java.awt.Color.lightGray);
        penGroup.add(dashdotdotPenButton);
        dashdotdotPenButton.setBounds(new Rectangle(120, 209, 120, 12));
        customPenButton.setText("Custom Pen");
        customPenButton.setBackground(java.awt.Color.lightGray);
        penGroup.add(customPenButton);
        customPenButton.setBounds(new Rectangle(120, 224, 120, 12));
        noPenButton.setText("No Pen");
        noPenButton.setBackground(java.awt.Color.lightGray);
        penGroup.add(noPenButton);
        noPenButton.setBounds(new Rectangle(120, 132, 120, 12));
        JPanel1.setLayout(null);
        JPanel1.setBackground(java.awt.Color.lightGray);
        JPanel1.setBounds(new Rectangle(108, 96, 156, 156));
        label5.setText("Pen Properties");
        label5.setFont(new Font("Dialog", Font.ITALIC, 12));
        label5.setBounds(new Rectangle(35, 3, 95, 23));
        label6.setText("Pen Width:");
        label6.setBounds(new Rectangle(25, 217, 67, 27));
        penWidth.setText("1");
        penWidth.setBounds(new Rectangle(28, 240, 48, 24));
        JPanel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        this.getContentPane().add(panel1, BorderLayout.SOUTH);
        JPanel2.setBounds(new Rectangle(276, 96, 132, 156));
        label8.setText("Brush Properties");
        label8.setFont(new Font("Dialog", Font.ITALIC, 12));
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
        if (myObject == null) {
            return;
        }

        classNameLabel.setText(myObject.getClass().getName());
        Rectangle rect = myObject.getBoundingRect();
        xField.setText(String.valueOf(rect.x));
        yField.setText(String.valueOf(rect.y));
        heightField.setText(String.valueOf(rect.height));
        widthField.setText(String.valueOf(rect.width));
        visibleBox.setSelected(myObject.isVisible());
        selectableBox.setSelected(myObject.isSelectable());
        resizableBox.setSelected(myObject.isResizable());
        draggableBox.setSelected(myObject.isDraggable());
        noPenButton
                .setSelected(myObject.getPen().getStyle() == GraphicViewerPen.NONE);
        solidPenButton
                .setSelected(myObject.getPen().getStyle() == GraphicViewerPen.SOLID);
        dashedPenButton
                .setSelected(myObject.getPen().getStyle() == GraphicViewerPen.DASHED);
        dottedPenButton
                .setSelected(myObject.getPen().getStyle() == GraphicViewerPen.DOTTED);
        dashdotPenButton
                .setSelected(myObject.getPen().getStyle() == GraphicViewerPen.DASHDOT);
        dashdotdotPenButton
                .setSelected(myObject.getPen().getStyle() == GraphicViewerPen.DASHDOTDOT);
        customPenButton
                .setSelected(myObject.getPen().getStyle() == GraphicViewerPen.CUSTOM);
        penWidth.setText(String.valueOf(myObject.getPen().getWidth()));
        myPenColor = myObject.getPen().getColor();
        GraphicViewerBrush brush = myObject.getBrush();
        if ((brush != null) && !(brush.getPaint() instanceof GradientPaint)) {
            solidBrushBox.setSelected(true);
            if (myObject.getBrush().getPaint() instanceof Color) {
                myBrushColor = (Color) myObject.getBrush().getPaint();
            } else {
                myBrushColor = Color.black;
            }
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
        if (myObject instanceof GraphicViewer3DRect) {
            raised.setSelected(((GraphicViewer3DRect) myObject).getState() == GraphicViewer3DRect.StateUp);
        }
    }

    void UpdateControl() {
        if (myObject == null) {
            return;
        }

        Rectangle rect = new Rectangle(Integer.parseInt(xField.getText()),
                Integer.parseInt(yField.getText()), Integer.parseInt(widthField
                        .getText()), Integer.parseInt(heightField.getText()));
        myObject.setBoundingRect(rect);
        myObject.setVisible(visibleBox.isSelected());
        myObject.setSelectable(selectableBox.isSelected());
        myObject.setResizable(resizableBox.isSelected());
        myObject.setDraggable(draggableBox.isSelected());
        int style;
        if (solidPenButton.isSelected()) {
            style = GraphicViewerPen.SOLID;
        } else if (dashedPenButton.isSelected()) {
            style = GraphicViewerPen.DASHED;
        } else if (dottedPenButton.isSelected()) {
            style = GraphicViewerPen.DOTTED;
        } else if (dashdotPenButton.isSelected()) {
            style = GraphicViewerPen.DASHDOT;
        } else if (dashdotdotPenButton.isSelected()) {
            style = GraphicViewerPen.DASHDOTDOT;
        } else if (customPenButton.isSelected()) {
            style = GraphicViewerPen.CUSTOM;
        } else {
            style = GraphicViewerPen.NONE;
        }
        int width = Integer.parseInt(penWidth.getText());
        Color color = myPenColor;
        myObject.setPen(GraphicViewerPen.make(style, width, color));
        if (solidBrushBox.isSelected()) {
            myObject.setBrush(GraphicViewerBrush.make(GraphicViewerBrush.SOLID,
                    myBrushColor));
        } else {
            if (myObject.getBrush() != null) {
                if (!(myObject.getBrush().getPaint() instanceof GradientPaint)) {
                    myObject.setBrush(null);
                }
            }
        }
        if (myObject instanceof GraphicViewer3DNoteRect) {
            ((GraphicViewer3DNoteRect) myObject).setShadowSize(Integer
                    .parseInt(shadowWidth.getText()));
            ((GraphicViewer3DNoteRect) myObject).setFlapSize(Integer
                    .parseInt(flapWidth.getText()));
        }
        if (myObject instanceof GraphicViewer3DRect) {
            if (raised.isSelected()) {
                ((GraphicViewer3DRect) myObject)
                        .setState(GraphicViewer3DRect.StateUp);
            } else {
                ((GraphicViewer3DRect) myObject)
                        .setState(GraphicViewer3DRect.StateDown);
            }
        }
    }

    public void addNotify() {
        // Record the size of the window prior to calling parents addNotify
        Dimension d = getSize();

        super.addNotify();

        if (fComponentsAdjusted) {
            return;
        }

        // Adjust components according to the insets
        Insets insets = getInsets();
        setSize(insets.left + insets.right + d.width, insets.top
                + insets.bottom + d.height);
        Component components[] = getComponents();
        for (int i = 0; i < components.length; i++) {
            Point p = components[i].getLocation();
            p.translate(insets.left, insets.top);
            components[i].setLocation(p);
        }
        fComponentsAdjusted = true;
    }

    // Used for addNotify check
    boolean fComponentsAdjusted = false;

    /**
     * Shows or hides the component depending on the boolean flag b.
     * 
     * @param b  if true, show the component; otherwise, hide the component.
     * @see javax.swing.JComponent#isVisible
     */
    public void setVisible(boolean b) {
        if (b) {
            Rectangle bounds = getParent().getBounds();
            Rectangle abounds = getBounds();

            setLocation(bounds.x + (bounds.width - abounds.width) / 2, bounds.y
                    + (bounds.height - abounds.height) / 2);
        }
        super.setVisible(b);
    }

    void OKButton_actionPerformed(ActionEvent e) {
        OnOK();
    }

    void OnOK() {
        try {
            UpdateControl();
            this.dispose(); // Free system resources
        } catch (Exception e) {
        }
    }

    void CancelButton_actionPerformed(ActionEvent e) {
        OnCancel();
    }

    void OnCancel() {
        try {
            this.dispose(); // Free system resources
        } catch (Exception e) {
        }
    }

    void penColorButton_actionPerformed(ActionEvent e) {
        Color newcolor = JColorChooser
                .showDialog(this, "Pen Color", myPenColor);
        if (newcolor != null) {
            myPenColor = newcolor;
        }
    }

    void brushColorButton_actionPerformed(ActionEvent e) {
        Color newcolor = JColorChooser.showDialog(this, "Brush Color",
                myBrushColor);
        if (newcolor != null) {
            myBrushColor = newcolor;
        }
    }

    void OKButton_keyPressed(KeyEvent e) {
        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            OnOK();
        } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
            OnCancel();
        }
    }

    void CancelButton_keyPressed(KeyEvent e) {
        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            OnCancel();
        } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
            OnCancel();
        }
    }
}