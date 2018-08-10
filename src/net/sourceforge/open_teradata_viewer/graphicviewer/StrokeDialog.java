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
public class StrokeDialog extends JDialog {

    private static final long serialVersionUID = 505151361909377527L;

    public StrokeDialog(Frame frame, String s, boolean flag,
            GraphicViewerStroke graphicviewerstroke) {
        super(frame, s, flag);
        panel1 = new JPanel();
        integerVerifier = new IntegerVerifier();
        doubleVerifier = new DoubleVerifier();
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
        startArrow = new JCheckBox();
        endArrow = new JCheckBox();
        JPanel3 = new JPanel();
        label9 = new JLabel();
        label10 = new JLabel();
        arrowLength = new JTextField();
        label11 = new JLabel();
        shaftLength = new JTextField();
        label12 = new JLabel();
        arrowWidth = new JTextField();
        isOrtho = new JCheckBox();
        JPanel4 = new JPanel();
        label13 = new JLabel();
        label14 = new JLabel();
        highlightWidth = new JTextField();
        highlightGroup = new ButtonGroup();
        solidHighlight = new JRadioButton();
        dashedHighlight = new JRadioButton();
        dottedHighlight = new JRadioButton();
        dashdotHighlight = new JRadioButton();
        dashdotdotHighlight = new JRadioButton();
        customHighlight = new JRadioButton();
        noHighlight = new JRadioButton();
        highlightColorButton = new JButton();
        isCubic = new JCheckBox();
        label15 = new JLabel();
        curviness = new JTextField();
        fComponentsAdjusted = false;
        JPanel5 = new JPanel();
        jLabel1 = new JLabel();
        isJumpsOver = new JCheckBox();
        isAvoidsNodes = new JCheckBox();
        isRelinkable = new JCheckBox();
        isResizingRelinks = new JCheckBox();
        jLabel2 = new JLabel();
        adjustingStyleGroup = new ButtonGroup();
        End = new JRadioButton();
        Scale = new JRadioButton();
        Stretch = new JRadioButton();
        Calculate = new JRadioButton();
        try {
            myObject = graphicviewerstroke;
            jbInit();
            pack();
            UpdateDialog();
        } catch (Exception exception) {
            exception.printStackTrace();
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
        highlightColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                highlightColorButton_actionPerformed(actionevent);
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
        OKButton.setBounds(new Rectangle(166, 293, 79, 22));
        CancelButton.setText("Cancel");
        CancelButton.setFont(new Font("Dialog", 0, 12));
        CancelButton.setBounds(new Rectangle(274, 293, 79, 22));
        label1.setText("Height:");
        label1.setHorizontalAlignment(4);
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
        setResizable(false);
        label8.setBounds(new Rectangle(16, 2, 91, 23));
        panel1.add(label1);
        label1.setBounds(new Rectangle(472, 42, 48, 24));
        panel1.add(heightField);
        heightField.setBounds(new Rectangle(532, 42, 36, 24));
        panel1.add(xField);
        xField.setBounds(new Rectangle(434, 18, 36, 24));
        label2.setText("x:");
        label2.setHorizontalAlignment(4);
        panel1.add(label2);
        label2.setBounds(new Rectangle(372, 18, 48, 24));
        panel1.add(yField);
        yField.setBounds(new Rectangle(434, 42, 36, 24));
        label3.setText("y:");
        label3.setHorizontalAlignment(4);
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
        label4.setHorizontalAlignment(4);
        panel1.add(label4);
        label4.setBounds(new Rectangle(472, 18, 48, 24));
        panel1.add(widthField);
        widthField.setBounds(new Rectangle(532, 18, 36, 24));
        brushColorButton.setText("Fill Color...");
        brushColorButton.setBackground(Color.lightGray);
        brushColorButton.setBounds(new Rectangle(315, 258, 108, 24));
        panel1.add(JPanel2);
        JPanel2.setBackground(Color.lightGray);
        JPanel2.setBounds(new Rectangle(307, 96, 124, 156));
        solidBrushBox.setText("Solid Brush");
        solidBrushBox.setBackground(Color.lightGray);
        JPanel2.add(label8);
        JPanel2.add(solidBrushBox);
        solidBrushBox.setBounds(new Rectangle(20, 31, 81, 23));
        JPanel3.setBackground(Color.lightGray);
        JPanel3.setBounds(new Rectangle(440, 96, 133, 224));
        JPanel3.add(label9);
        startArrow.setText("Start Arrow");
        JPanel3.add(startArrow);
        startArrow.setBounds(new Rectangle(10, 28, 84, 24));
        startArrow.setBackground(Color.lightGray);
        endArrow.setText("End Arrow");
        JPanel3.add(endArrow);
        endArrow.setBounds(new Rectangle(10, 49, 84, 24));
        endArrow.setBackground(Color.lightGray);
        highlightColorButton.setText("Highlight Color...");
        highlightColorButton.setBackground(Color.lightGray);
        highlightColorButton.setBounds(new Rectangle(10, 258, 128, 24));
        getContentPane().add(panel1);
        JPanel5.setBackground(Color.lightGray);
        JPanel5.setBounds(new Rectangle(581, 96, 136, 224));
        JPanel5.setLayout(null);
        jLabel1.setFont(new Font("Dialog", 2, 12));
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
        if (!(myObject instanceof GraphicViewerLink))
            JPanel5.setVisible(false);
        isCubic.setText("Cubic Bezier");
        panel1.add(isCubic);
        isCubic.setBounds(new Rectangle(430, 64, 124, 24));
        penColorButton.setText("Pen Color...");
        penColorButton.setBackground(Color.lightGray);
        penColorButton.setBounds(new Rectangle(172, 258, 108, 24));
        classNameLabel.setText("class name");
        panel1.add(classNameLabel);
        classNameLabel.setBounds(new Rectangle(26, 4, 264, 24));
        JPanel1.setLayout(null);
        panel1.add(JPanel1);
        JPanel1.setBackground(Color.lightGray);
        JPanel1.setBounds(new Rectangle(157, 96, 140, 156));
        JPanel4.setLayout(null);
        panel1.add(JPanel4);
        JPanel4.setBackground(Color.lightGray);
        JPanel4.setBounds(new Rectangle(7, 96, 140, 156));
        solidPenButton.setText("Solid Line Pen");
        solidPenButton.setBackground(Color.lightGray);
        JPanel1.add(solidPenButton);
        penGroup.add(solidPenButton);
        solidPenButton.setBounds(new Rectangle(12, 52, 120, 12));
        dashedPenButton.setText("Dashed Line Pen");
        JPanel1.add(dashedPenButton);
        penGroup.add(dashedPenButton);
        dashedPenButton.setBounds(new Rectangle(12, 68, 120, 12));
        dashedPenButton.setBackground(Color.lightGray);
        dottedPenButton.setText("Dotted Line Pen");
        dottedPenButton.setBackground(Color.lightGray);
        JPanel1.add(dottedPenButton);
        penGroup.add(dottedPenButton);
        dottedPenButton.setBounds(new Rectangle(12, 84, 120, 12));
        dashdotPenButton.setText("Dash Dot Pen");
        dashdotPenButton.setBackground(Color.lightGray);
        JPanel1.add(dashdotPenButton);
        penGroup.add(dashdotPenButton);
        dashdotPenButton.setBounds(new Rectangle(12, 100, 120, 12));
        dashdotdotPenButton.setText("Dash Dot Dot Pen");
        dashdotdotPenButton.setBackground(Color.lightGray);
        JPanel1.add(dashdotdotPenButton);
        penGroup.add(dashdotdotPenButton);
        dashdotdotPenButton.setBounds(new Rectangle(12, 116, 120, 12));
        customPenButton.setText("Custom Pen");
        customPenButton.setBackground(Color.lightGray);
        JPanel1.add(customPenButton);
        penGroup.add(customPenButton);
        customPenButton.setBounds(new Rectangle(12, 132, 120, 12));
        noPenButton.setText("No Pen");
        noPenButton.setBackground(Color.lightGray);
        JPanel1.add(noPenButton);
        JPanel1.add(label5);
        penGroup.add(noPenButton);
        noPenButton.setBounds(new Rectangle(12, 36, 120, 12));
        label5.setText("Pen Properties");
        label5.setFont(new Font("Dialog", 2, 12));
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
        label8.setFont(new Font("Dialog", 2, 12));
        JPanel3.setLayout(null);
        panel1.add(JPanel3);
        label9.setText("Arrow Properties");
        label9.setFont(new Font("Dialog", 2, 12));
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
        label13.setFont(new Font("Dialog", 2, 12));
        label13.setBounds(new Rectangle(12, 2, 130, 23));
        panel1.add(label14);
        label14.setText("Highlight Width:");
        label14.setBounds(new Rectangle(17, 35, 100, 27));
        panel1.add(highlightWidth);
        panel1.add(JPanel5, null);
        highlightWidth.setBounds(new Rectangle(17, 60, 48, 24));
        solidHighlight.setBackground(Color.lightGray);
        dashedHighlight.setBackground(Color.lightGray);
        dottedHighlight.setBackground(Color.lightGray);
        dashdotHighlight.setBackground(Color.lightGray);
        dashdotdotHighlight.setBackground(Color.lightGray);
        customHighlight.setBackground(Color.lightGray);
        noHighlight.setBackground(Color.lightGray);
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

    @SuppressWarnings("static-access")
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
        startArrow.setSelected(myObject.hasArrowAtStart());
        endArrow.setSelected(myObject.hasArrowAtEnd());
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
        arrowLength.setText(String.valueOf(myObject.getArrowLength()));
        shaftLength.setText(String.valueOf(myObject.getArrowShaftLength()));
        arrowWidth.setText(String.valueOf(myObject.getArrowWidth()));
        if (myObject instanceof GraphicViewerLink) {
            GraphicViewerLink graphicviewerlink = (GraphicViewerLink) myObject;
            isJumpsOver.setSelected(graphicviewerlink.isJumpsOver());
            isAvoidsNodes.setSelected(graphicviewerlink.isAvoidsNodes());
            isRelinkable.setSelected(graphicviewerlink.isRelinkable());
            isOrtho.setSelected(graphicviewerlink.isOrthogonal());
            isResizingRelinks.setSelected(graphicviewerlink
                    .isDefaultResizingRelinks());
            Calculate.setSelected(graphicviewerlink.getAdjustingStyle() == 0);
            End.setSelected(graphicviewerlink.getAdjustingStyle() == 3);
            Scale.setSelected(graphicviewerlink.getAdjustingStyle() == 1);
            Stretch.setSelected(graphicviewerlink.getAdjustingStyle() == 2);
            curviness.setText(String.valueOf(graphicviewerlink.getCurviness()));
        }
        isCubic.setSelected(myObject.isCubic());
        GraphicViewerPen graphicviewerpen = myObject.getHighlight();
        if (graphicviewerpen == null) {
            noHighlight.setSelected(true);
            highlightWidth.setText("0");
            myHighlightColor = Color.black;
        } else {
            myHighlightColor = graphicviewerpen.getColor();
            highlightWidth.setText(String.valueOf(graphicviewerpen.getWidth()));
            solidHighlight.setSelected(graphicviewerpen.getStyle() == 65535);
            dashedHighlight.setSelected(graphicviewerpen.getStyle() == 1);
            dottedHighlight.setSelected(graphicviewerpen.getStyle() == 2);
            dashdotHighlight.setSelected(graphicviewerpen.getStyle() == 3);
            dashdotdotHighlight.setSelected(graphicviewerpen.getStyle() == 4);
            customHighlight.setSelected(graphicviewerpen.getStyle() == 65534);
        }
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
        myObject.setArrowHeads(startArrow.isSelected(), endArrow.isSelected());
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
        int k = Integer.parseInt(penWidth.getText());
        Color color = myPenColor;
        myObject.setPen(GraphicViewerPen.make(i, k, color));
        if (solidBrushBox.isSelected())
            myObject.setBrush(GraphicViewerBrush.make(65535, myBrushColor));
        else if (myObject.getBrush() == null
                || !(myObject.getBrush().getPaint() instanceof GradientPaint))
            myObject.setBrush(null);
        myObject.setArrowLength(Double.parseDouble(arrowLength.getText()));
        myObject.setArrowShaftLength(Double.parseDouble(shaftLength.getText()));
        myObject.setArrowWidth(Double.parseDouble(arrowWidth.getText()));
        myObject.setCubic(isCubic.isSelected());
        if (myObject instanceof GraphicViewerLink) {
            GraphicViewerLink graphicviewerlink = (GraphicViewerLink) myObject;
            graphicviewerlink.setJumpsOver(isJumpsOver.isSelected());
            graphicviewerlink.setAvoidsNodes(isAvoidsNodes.isSelected());
            graphicviewerlink.setRelinkable(isRelinkable.isSelected());
            graphicviewerlink.setOrthogonal(isOrtho.isSelected());
            @SuppressWarnings("unused")
            GraphicViewerLink _tmp = graphicviewerlink;
            GraphicViewerLink.setDefaultResizingRelinks(isResizingRelinks
                    .isSelected());
            if (Calculate.isSelected())
                graphicviewerlink.setAdjustingStyle(0);
            if (End.isSelected())
                graphicviewerlink.setAdjustingStyle(3);
            if (Scale.isSelected())
                graphicviewerlink.setAdjustingStyle(1);
            if (Stretch.isSelected())
                graphicviewerlink.setAdjustingStyle(2);
            graphicviewerlink
                    .setCurviness(Integer.parseInt(curviness.getText()));
            graphicviewerlink.calculateStroke();
        }
        if (noHighlight.isSelected()
                || Integer.parseInt(highlightWidth.getText()) == 0) {
            myObject.setHighlight(null);
        } else {
            Color color1 = myHighlightColor;
            int j;
            if (solidHighlight.isSelected())
                j = 65535;
            else if (dashedHighlight.isSelected())
                j = 1;
            else if (dottedHighlight.isSelected())
                j = 2;
            else if (dashdotHighlight.isSelected())
                j = 3;
            else if (dashdotdotHighlight.isSelected())
                j = 4;
            else
                j = 65534;
            myObject.setHighlight(new GraphicViewerPen(j, Integer
                    .parseInt(highlightWidth.getText()), color1));
        }
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
            exception.printStackTrace();
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

    void highlightColorButton_actionPerformed(ActionEvent actionevent) {
        Color color = JColorChooser.showDialog(this, "Highlight Color",
                myHighlightColor);
        if (color != null)
            myHighlightColor = color;
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
    IntegerVerifier integerVerifier;
    DoubleVerifier doubleVerifier;
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
    JCheckBox startArrow;
    JCheckBox endArrow;
    JPanel JPanel3;
    JLabel label9;
    JLabel label10;
    JTextField arrowLength;
    JLabel label11;
    JTextField shaftLength;
    JLabel label12;
    JTextField arrowWidth;
    JCheckBox isOrtho;
    JPanel JPanel4;
    JLabel label13;
    JLabel label14;
    JTextField highlightWidth;
    ButtonGroup highlightGroup;
    JRadioButton solidHighlight;
    JRadioButton dashedHighlight;
    JRadioButton dottedHighlight;
    JRadioButton dashdotHighlight;
    JRadioButton dashdotdotHighlight;
    JRadioButton customHighlight;
    JRadioButton noHighlight;
    JButton highlightColorButton;
    JCheckBox isCubic;
    JLabel label15;
    JTextField curviness;
    boolean fComponentsAdjusted;
    JPanel JPanel5;
    JLabel jLabel1;
    JCheckBox isJumpsOver;
    JCheckBox isAvoidsNodes;
    JCheckBox isRelinkable;
    JCheckBox isResizingRelinks;
    JLabel jLabel2;
    ButtonGroup adjustingStyleGroup;
    JRadioButton End;
    JRadioButton Scale;
    JRadioButton Stretch;
    JRadioButton Calculate;
    Color myBrushColor;
    Color myPenColor;
    Color myHighlightColor;
    public GraphicViewerStroke myObject;
}