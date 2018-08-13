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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
public class StrokeDialog extends JDialog {

    private static final long serialVersionUID = 505151361909377527L;

    JPanel panel1 = new JPanel();
    IntegerVerifier integerVerifier = new IntegerVerifier();
    DoubleVerifier doubleVerifier = new DoubleVerifier();
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
    JCheckBox startArrow = new JCheckBox();
    JCheckBox endArrow = new JCheckBox();
    JPanel JPanel3 = new JPanel();
    JLabel label9 = new JLabel();
    JLabel label10 = new JLabel();
    JTextField arrowLength = new JTextField();
    JLabel label11 = new JLabel();
    JTextField shaftLength = new JTextField();
    JLabel label12 = new JLabel();
    JTextField arrowWidth = new JTextField();
    JCheckBox isOrtho = new JCheckBox();
    JPanel JPanel4 = new JPanel();
    JLabel label13 = new JLabel();
    JLabel label14 = new JLabel();
    JTextField highlightWidth = new JTextField();
    ButtonGroup highlightGroup = new ButtonGroup();
    JRadioButton solidHighlight = new JRadioButton();
    JRadioButton dashedHighlight = new JRadioButton();
    JRadioButton dottedHighlight = new JRadioButton();
    JRadioButton dashdotHighlight = new JRadioButton();
    JRadioButton dashdotdotHighlight = new JRadioButton();
    JRadioButton customHighlight = new JRadioButton();
    JRadioButton noHighlight = new JRadioButton();
    JButton highlightColorButton = new JButton();
    JCheckBox isCubic = new JCheckBox();
    JLabel label15 = new JLabel();
    JTextField curviness = new JTextField();
    // Used for addNotify check
    boolean fComponentsAdjusted = false;
    JPanel JPanel5 = new JPanel();
    JLabel jLabel1 = new JLabel();
    JCheckBox isJumpsOver = new JCheckBox();
    JCheckBox isAvoidsNodes = new JCheckBox();
    JCheckBox isRelinkable = new JCheckBox();
    JCheckBox isResizingRelinks = new JCheckBox();
    JLabel jLabel2 = new JLabel();
    ButtonGroup adjustingStyleGroup = new ButtonGroup();
    JRadioButton End = new JRadioButton();
    JRadioButton Scale = new JRadioButton();
    JRadioButton Stretch = new JRadioButton();
    JRadioButton Calculate = new JRadioButton();

    Color myBrushColor;
    Color myPenColor;
    Color myHighlightColor;

    public GraphicViewerStroke myObject;

    public StrokeDialog(Frame frame, String title, boolean modal,
            GraphicViewerStroke obj) {
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

    public StrokeDialog() {
        this(null, "", false, null);
    }

    void jbInit() throws Exception {
        panel1.setLayout(null);
        if (myObject instanceof GraphicViewerLink) {
            panel1.setMinimumSize(new Dimension(725, 329));
            panel1.setPreferredSize(new Dimension(725, 329));
        } else {
            panel1.setMinimumSize(new Dimension(580, 329));
            panel1.setPreferredSize(new Dimension(580, 329));
        }
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
        highlightColorButton
                .addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        highlightColorButton_actionPerformed(e);
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
        OKButton.setBounds(new Rectangle(166, 293, 79, 22));
        CancelButton.setText("Cancel");
        CancelButton.setFont(new Font("Dialog", Font.PLAIN, 12));
        CancelButton.setBounds(new Rectangle(274, 293, 79, 22));
        label1.setText("Height:");
        label1.setHorizontalAlignment(JLabel.RIGHT);
        isResizingRelinks.setBackground(Color.lightGray);
        isResizingRelinks.setText("Resizing Relinks");
        isResizingRelinks.setBounds(new Rectangle(6, 102, 110, 23));
        jLabel2.setText("Adjusting Style:");
        jLabel2.setBounds(new Rectangle(4, 126, 85, 15));
        End.setBackground(Color.lightGray);
        End.setText("End");
        End.setBounds(new Rectangle(21, 154, 91, 17));
        Scale.setBackground(Color.lightGray);
        Scale.setText("Scale");
        Scale.setBounds(new Rectangle(21, 167, 91, 17));
        Stretch.setBackground(Color.lightGray);
        Stretch.setText("Stretch");
        Stretch.setBounds(new Rectangle(21, 181, 91, 18));
        Calculate.setBackground(Color.lightGray);
        Calculate.setText("Calculate");
        Calculate.setBounds(new Rectangle(21, 142, 91, 15));
        highlightWidth.setInputVerifier(integerVerifier);
        xField.setInputVerifier(integerVerifier);
        yField.setInputVerifier(integerVerifier);
        widthField.setInputVerifier(integerVerifier);
        heightField.setInputVerifier(integerVerifier);
        arrowLength.setInputVerifier(doubleVerifier);
        shaftLength.setInputVerifier(doubleVerifier);
        arrowWidth.setInputVerifier(doubleVerifier);
        curviness.setInputVerifier(integerVerifier);
        JPanel4.setBorder(BorderFactory.createRaisedBevelBorder());
        JPanel1.setBorder(BorderFactory.createRaisedBevelBorder());
        JPanel2.setBorder(BorderFactory.createRaisedBevelBorder());
        JPanel2.setLayout(null);
        JPanel3.setBorder(BorderFactory.createRaisedBevelBorder());
        JPanel5.setBorder(BorderFactory.createRaisedBevelBorder());
        this.setResizable(false);
        label8.setBounds(new Rectangle(16, 2, 91, 23));
        panel1.add(label1);
        label1.setBounds(new Rectangle(472, 42, 48, 24));
        panel1.add(heightField);
        heightField.setBounds(new Rectangle(532, 42, 36, 24));
        panel1.add(xField);
        xField.setBounds(new Rectangle(434, 18, 36, 24));
        label2.setText("x:");
        label2.setHorizontalAlignment(JLabel.RIGHT);
        panel1.add(label2);
        label2.setBounds(new Rectangle(372, 18, 48, 24));
        panel1.add(yField);
        yField.setBounds(new Rectangle(434, 42, 36, 24));
        label3.setText("y:");
        label3.setHorizontalAlignment(JLabel.RIGHT);
        panel1.add(label3);
        label3.setBounds(new Rectangle(372, 42, 48, 24));
        visibleBox.setText("Visible");
        panel1.add(visibleBox);
        visibleBox.setBounds(new Rectangle(236, 24, 72, 24));
        selectableBox.setText("Selectable");
        panel1.add(selectableBox);
        selectableBox.setBounds(new Rectangle(236, 44, 84, 24));
        resizableBox.setText("Resizable");
        panel1.add(resizableBox);
        resizableBox.setBounds(new Rectangle(324, 24, 84, 24));
        draggableBox.setText("Draggable");
        panel1.add(draggableBox);
        draggableBox.setBounds(new Rectangle(324, 44, 84, 24));
        label4.setText("Width:");
        label4.setHorizontalAlignment(JLabel.RIGHT);
        panel1.add(label4);
        label4.setBounds(new Rectangle(472, 18, 48, 24));
        panel1.add(widthField);
        widthField.setBounds(new Rectangle(532, 18, 36, 24));
        brushColorButton.setText("Fill Color...");
        brushColorButton.setBackground(java.awt.Color.lightGray);
        brushColorButton.setBounds(new Rectangle(315, 258, 108, 24));
        panel1.add(JPanel2);
        JPanel2.setBackground(java.awt.Color.lightGray);
        JPanel2.setBounds(new Rectangle(307, 96, 124, 156));
        solidBrushBox.setText("Solid Brush");
        solidBrushBox.setBackground(java.awt.Color.lightGray);
        JPanel2.add(label8);
        JPanel2.add(solidBrushBox);
        solidBrushBox.setBounds(new Rectangle(20, 31, 81, 23));
        JPanel3.setBackground(java.awt.Color.lightGray);
        JPanel3.setBounds(new Rectangle(440, 96, 133, 224));
        JPanel3.add(label9);
        startArrow.setText("Start Arrow");
        JPanel3.add(startArrow);
        startArrow.setBounds(new Rectangle(10, 28, 84, 24));
        startArrow.setBackground(java.awt.Color.lightGray);
        endArrow.setText("End Arrow");
        JPanel3.add(endArrow);
        endArrow.setBounds(new Rectangle(10, 49, 84, 24));
        endArrow.setBackground(java.awt.Color.lightGray);
        highlightColorButton.setText("Highlight Color...");
        highlightColorButton.setBackground(java.awt.Color.lightGray);
        highlightColorButton.setBounds(new Rectangle(10, 258, 128, 24));
        getContentPane().add(panel1);
        JPanel5.setBackground(Color.lightGray);
        JPanel5.setBounds(new Rectangle(581, 96, 136, 224));
        JPanel5.setLayout(null);
        jLabel1.setFont(new java.awt.Font("Dialog", 2, 12));
        jLabel1.setText("Link Properties");
        jLabel1.setBounds(new Rectangle(18, 5, 83, 17));
        isJumpsOver.setBackground(Color.lightGray);
        isJumpsOver.setText("Jumps Over");
        isJumpsOver.setBounds(new Rectangle(6, 25, 91, 25));
        isAvoidsNodes.setBackground(Color.lightGray);
        isAvoidsNodes.setText("Avoids Nodes");
        isAvoidsNodes.setBounds(new Rectangle(6, 45, 101, 25));
        isRelinkable.setBackground(Color.lightGray);
        isRelinkable.setText("Relinkable");
        isRelinkable.setBounds(new Rectangle(6, 66, 84, 21));
        isOrtho.setBackground(Color.lightGray);
        isOrtho.setText("Orthogonal");
        isOrtho.setBounds(new Rectangle(6, 82, 84, 24));
        label15.setText("Curviness:");
        label15.setBounds(new Rectangle(7, 198, 58, 20));
        curviness.setBounds(new Rectangle(84, 198, 30, 20));
        JPanel5.add(jLabel1, null);
        JPanel5.add(jLabel2, null);
        JPanel5.add(curviness, null);
        JPanel5.add(label15, null);
        JPanel5.add(Stretch, null);
        JPanel5.add(End, null);
        JPanel5.add(Scale, null);
        JPanel5.add(Calculate, null);
        JPanel5.add(isOrtho, null);
        JPanel5.add(isRelinkable, null);
        JPanel5.add(isAvoidsNodes, null);
        JPanel5.add(isResizingRelinks, null);
        JPanel5.add(isJumpsOver, null);
        if (!(myObject instanceof GraphicViewerLink)) {
            JPanel5.setVisible(false);
        }
        isCubic.setText("Cubic Bezier");
        panel1.add(isCubic);
        isCubic.setBounds(new Rectangle(430, 64, 124, 24));
        penColorButton.setText("Pen Color...");
        penColorButton.setBackground(java.awt.Color.lightGray);
        penColorButton.setBounds(new Rectangle(172, 258, 108, 24));
        classNameLabel.setText("class name");
        panel1.add(classNameLabel);
        classNameLabel.setBounds(new Rectangle(26, 4, 264, 24));
        JPanel1.setLayout(null);
        panel1.add(JPanel1);
        JPanel1.setBackground(java.awt.Color.lightGray);
        JPanel1.setBounds(new Rectangle(157, 96, 140, 156));
        JPanel4.setLayout(null);
        panel1.add(JPanel4);
        JPanel4.setBackground(java.awt.Color.lightGray);
        JPanel4.setBounds(new Rectangle(7, 96, 140, 156));
        solidPenButton.setText("Solid Line Pen");
        solidPenButton.setBackground(java.awt.Color.lightGray);
        JPanel1.add(solidPenButton);
        penGroup.add(solidPenButton);
        solidPenButton.setBounds(new Rectangle(12, 52, 120, 12));
        dashedPenButton.setText("Dashed Line Pen");
        JPanel1.add(dashedPenButton);
        penGroup.add(dashedPenButton);
        dashedPenButton.setBounds(new Rectangle(12, 68, 120, 12));
        dashedPenButton.setBackground(java.awt.Color.lightGray);
        dottedPenButton.setText("Dotted Line Pen");
        dottedPenButton.setBackground(java.awt.Color.lightGray);
        JPanel1.add(dottedPenButton);
        penGroup.add(dottedPenButton);
        dottedPenButton.setBounds(new Rectangle(12, 84, 120, 12));
        dashdotPenButton.setText("Dash Dot Pen");
        dashdotPenButton.setBackground(java.awt.Color.lightGray);
        JPanel1.add(dashdotPenButton);
        penGroup.add(dashdotPenButton);
        dashdotPenButton.setBounds(new Rectangle(12, 100, 120, 12));
        dashdotdotPenButton.setText("Dash Dot Dot Pen");
        dashdotdotPenButton.setBackground(java.awt.Color.lightGray);
        JPanel1.add(dashdotdotPenButton);
        penGroup.add(dashdotdotPenButton);
        dashdotdotPenButton.setBounds(new Rectangle(12, 116, 120, 12));
        customPenButton.setText("Custom Pen");
        customPenButton.setBackground(java.awt.Color.lightGray);
        JPanel1.add(customPenButton);
        penGroup.add(customPenButton);
        customPenButton.setBounds(new Rectangle(12, 132, 120, 12));
        noPenButton.setText("No Pen");
        noPenButton.setBackground(java.awt.Color.lightGray);
        JPanel1.add(noPenButton);
        JPanel1.add(label5);
        penGroup.add(noPenButton);
        noPenButton.setBounds(new Rectangle(12, 36, 120, 12));
        label5.setText("Pen Properties");
        label5.setFont(new Font("Dialog", Font.ITALIC, 12));
        label5.setBounds(new Rectangle(26, 2, 95, 23));
        label6.setText("Pen Width:");
        label6.setBounds(new Rectangle(163, 35, 67, 27));
        penWidth.setText("1");
        penWidth.setBounds(new Rectangle(163, 60, 48, 24));
        panel1.add(CancelButton);
        panel1.add(OKButton);
        panel1.add(brushColorButton);
        panel1.add(penColorButton);
        panel1.add(highlightColorButton);
        panel1.add(label6, null);
        panel1.add(penWidth, null);
        label8.setText("Brush Properties");
        label8.setFont(new Font("Dialog", Font.ITALIC, 12));
        JPanel3.setLayout(null);
        panel1.add(JPanel3);
        label9.setText("Arrow Properties");
        label9.setFont(new Font("Dialog", Font.ITALIC, 12));
        label9.setBounds(new Rectangle(22, 1, 95, 25));
        JPanel3.add(label10);
        label10.setText("Arrow Length");
        label10.setBounds(new Rectangle(10, 72, 95, 23));
        JPanel3.add(arrowLength);
        arrowLength.setBounds(new Rectangle(10, 91, 48, 24));
        JPanel3.add(label11);
        label11.setText("Shaft Length");
        label11.setBounds(new Rectangle(10, 119, 95, 23));
        JPanel3.add(shaftLength);
        shaftLength.setBounds(new Rectangle(10, 138, 48, 24));
        JPanel3.add(label12);
        label12.setText("Arrow Width");
        label12.setBounds(new Rectangle(10, 166, 128, 23));
        JPanel3.add(arrowWidth);
        arrowWidth.setBounds(new Rectangle(10, 185, 115, 24));
        JPanel4.add(label13);
        label13.setText("Highlight Properties");
        label13.setFont(new Font("Dialog", Font.ITALIC, 12));
        label13.setBounds(new Rectangle(12, 2, 130, 23));
        panel1.add(label14);
        label14.setText("Highlight Width:");
        label14.setBounds(new Rectangle(17, 35, 100, 27));
        panel1.add(highlightWidth);
        panel1.add(JPanel5, null);
        highlightWidth.setBounds(new Rectangle(17, 60, 48, 24));
        solidHighlight.setBackground(java.awt.Color.lightGray);
        dashedHighlight.setBackground(java.awt.Color.lightGray);
        dottedHighlight.setBackground(java.awt.Color.lightGray);
        dashdotHighlight.setBackground(java.awt.Color.lightGray);
        dashdotdotHighlight.setBackground(java.awt.Color.lightGray);
        customHighlight.setBackground(java.awt.Color.lightGray);
        noHighlight.setBackground(java.awt.Color.lightGray);
        solidHighlight.setText("Solid Highlight");
        dashedHighlight.setText("Dashed Line");
        dottedHighlight.setText("Dotted Line");
        dashdotHighlight.setText("Dash Dot Line");
        dashdotdotHighlight.setText("Dash Dot Dot Line");
        customHighlight.setText("Custom Highlight");
        noHighlight.setText("No Highlight");
        JPanel4.add(solidHighlight);
        solidHighlight.setBounds(new Rectangle(12, 52, 120, 14));
        JPanel4.add(dashedHighlight);
        dashedHighlight.setBounds(new Rectangle(12, 68, 120, 14));
        JPanel4.add(dottedHighlight);
        dottedHighlight.setBounds(new Rectangle(12, 84, 120, 14));
        JPanel4.add(dashdotHighlight);
        dashdotHighlight.setBounds(new Rectangle(12, 100, 120, 14));
        JPanel4.add(dashdotdotHighlight);
        dashdotdotHighlight.setBounds(new Rectangle(12, 116, 120, 14));
        JPanel4.add(customHighlight);
        customHighlight.setBounds(new Rectangle(12, 132, 120, 14));
        JPanel4.add(noHighlight);
        noHighlight.setBounds(new Rectangle(12, 36, 120, 14));
        highlightGroup.add(solidHighlight);
        highlightGroup.add(dashedHighlight);
        highlightGroup.add(dottedHighlight);
        highlightGroup.add(dashdotHighlight);
        highlightGroup.add(dashdotdotHighlight);
        highlightGroup.add(customHighlight);
        highlightGroup.add(noHighlight);
        adjustingStyleGroup.add(Calculate);
        adjustingStyleGroup.add(End);
        adjustingStyleGroup.add(Scale);
        adjustingStyleGroup.add(Stretch);
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
        startArrow.setSelected(myObject.hasArrowAtStart());
        endArrow.setSelected(myObject.hasArrowAtEnd());
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
        arrowLength.setText(String.valueOf(myObject.getArrowLength()));
        shaftLength.setText(String.valueOf(myObject.getArrowShaftLength()));
        arrowWidth.setText(String.valueOf(myObject.getArrowWidth()));
        if (myObject instanceof GraphicViewerLink) {
            GraphicViewerLink link = (GraphicViewerLink) myObject;
            isJumpsOver.setSelected(link.isJumpsOver());
            isAvoidsNodes.setSelected(link.isAvoidsNodes());
            isRelinkable.setSelected(link.isRelinkable());
            isOrtho.setSelected(link.isOrthogonal());
            isResizingRelinks.setSelected(link.isDefaultResizingRelinks());
            Calculate
                    .setSelected(link.getAdjustingStyle() == GraphicViewerLink.AdjustingStyleCalculate);
            End.setSelected(link.getAdjustingStyle() == GraphicViewerLink.AdjustingStyleEnd);
            Scale.setSelected(link.getAdjustingStyle() == GraphicViewerLink.AdjustingStyleScale);
            Stretch.setSelected(link.getAdjustingStyle() == GraphicViewerLink.AdjustingStyleStretch);
            curviness.setText(String.valueOf(link.getCurviness()));
        }
        isCubic.setSelected(myObject.isCubic());
        GraphicViewerPen myHighlight = myObject.getHighlight();
        if (myHighlight == null) {
            noHighlight.setSelected(true);
            highlightWidth.setText("0");
            myHighlightColor = Color.black;
        } else {
            myHighlightColor = myHighlight.getColor();
            highlightWidth.setText(String.valueOf(myHighlight.getWidth()));
            solidHighlight
                    .setSelected(myHighlight.getStyle() == GraphicViewerPen.SOLID);
            dashedHighlight
                    .setSelected(myHighlight.getStyle() == GraphicViewerPen.DASHED);
            dottedHighlight
                    .setSelected(myHighlight.getStyle() == GraphicViewerPen.DOTTED);
            dashdotHighlight
                    .setSelected(myHighlight.getStyle() == GraphicViewerPen.DASHDOT);
            dashdotdotHighlight
                    .setSelected(myHighlight.getStyle() == GraphicViewerPen.DASHDOTDOT);
            customHighlight
                    .setSelected(myHighlight.getStyle() == GraphicViewerPen.CUSTOM);
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
        myObject.setArrowHeads(startArrow.isSelected(), endArrow.isSelected());
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
            if (!(myObject.getBrush() != null && myObject.getBrush().getPaint() instanceof GradientPaint)) {
                myObject.setBrush(null);
            }
        }
        myObject.setArrowLength(Double.parseDouble(arrowLength.getText()));
        myObject.setArrowShaftLength(Double.parseDouble(shaftLength.getText()));
        myObject.setArrowWidth(Double.parseDouble(arrowWidth.getText()));
        myObject.setCubic(isCubic.isSelected());
        if (myObject instanceof GraphicViewerLink) {
            GraphicViewerLink link = (GraphicViewerLink) myObject;
            link.setJumpsOver(isJumpsOver.isSelected());
            link.setAvoidsNodes(isAvoidsNodes.isSelected());
            link.setRelinkable(isRelinkable.isSelected());
            link.setOrthogonal(isOrtho.isSelected());
            link.setDefaultResizingRelinks(isResizingRelinks.isSelected());
            if (Calculate.isSelected()) {
                link.setAdjustingStyle(GraphicViewerLink.AdjustingStyleCalculate);
            }
            if (End.isSelected()) {
                link.setAdjustingStyle(GraphicViewerLink.AdjustingStyleEnd);
            }
            if (Scale.isSelected()) {
                link.setAdjustingStyle(GraphicViewerLink.AdjustingStyleScale);
            }
            if (Stretch.isSelected()) {
                link.setAdjustingStyle(GraphicViewerLink.AdjustingStyleStretch);
            }

            link.setCurviness(Integer.parseInt(curviness.getText()));
            link.calculateStroke();
        }
        if (noHighlight.isSelected()
                || Integer.parseInt(highlightWidth.getText()) == 0) {
            myObject.setHighlight(null);
        } else {
            Color aColor = myHighlightColor;
            if (solidHighlight.isSelected()) {
                style = GraphicViewerPen.SOLID;
            } else if (dashedHighlight.isSelected()) {
                style = GraphicViewerPen.DASHED;
            } else if (dottedHighlight.isSelected()) {
                style = GraphicViewerPen.DOTTED;
            } else if (dashdotHighlight.isSelected()) {
                style = GraphicViewerPen.DASHDOT;
            } else if (dashdotdotHighlight.isSelected()) {
                style = GraphicViewerPen.DASHDOTDOT;
            } else {
                style = GraphicViewerPen.CUSTOM;
            }
            myObject.setHighlight(new GraphicViewerPen(style, Integer
                    .parseInt(highlightWidth.getText()), aColor));
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

    /**
     * Shows or hides the component depending on the boolean flag b.
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
            ExceptionDialog.hideException(e);
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

    void highlightColorButton_actionPerformed(ActionEvent e) {
        Color newcolor = JColorChooser.showDialog(this, "Highlight Color",
                myHighlightColor);
        if (newcolor != null) {
            myHighlightColor = newcolor;
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