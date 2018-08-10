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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TextPropsDialog extends JDialog {

    private static final long serialVersionUID = 1166423308230767362L;

    public TextPropsDialog(Frame frame, String s, boolean flag,
            GraphicViewerText graphicviewertext) {
        super(frame, s, flag);
        panel1 = new JPanel();
        OKButton = new JButton();
        CancelButton = new JButton();
        label1 = new JLabel();
        heightField = new JTextField();
        jLabel1 = new JLabel();
        widthField = new JTextField();
        xField = new JTextField();
        label2 = new JLabel();
        yField = new JTextField();
        label3 = new JLabel();
        visibleBox = new JCheckBox();
        selectableBox = new JCheckBox();
        resizableBox = new JCheckBox();
        draggableBox = new JCheckBox();
        label4 = new JLabel();
        editableBox = new JCheckBox();
        boldBox = new JCheckBox();
        italicBox = new JCheckBox();
        underlineBox = new JCheckBox();
        strikeBox = new JCheckBox();
        textField = new JTextField();
        label5 = new JLabel();
        faceNameField = new JTextField();
        alignGroup = new ButtonGroup();
        alignLeftRadio = new JRadioButton();
        alignCenterRadio = new JRadioButton();
        alignRightRadio = new JRadioButton();
        multilineBox = new JCheckBox();
        label6 = new JLabel();
        fontSizeField = new JTextField();
        textColorButton = new JButton();
        backgroundColorButton = new JButton();
        transparentBox = new JCheckBox();
        textArea = new JTextArea();
        textAreaScroll = new JScrollPane(textArea);
        classNameLabel = new JLabel();
        editSingle = new JCheckBox();
        selectBack = new JCheckBox();
        twoDScale = new JCheckBox();
        clipping = new JCheckBox();
        autoResize = new JCheckBox();
        wrapping = new JCheckBox();
        label7 = new JLabel();
        wrapwidth = new JTextField();
        borderLayout1 = new BorderLayout();
        fComponentsAdjusted = false;
        try {
            jbInit();
            myObject = graphicviewertext;
            UpdateDialog();
            pack();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public TextPropsDialog() {
        this(null, "", false, null);
    }

    void jbInit() throws Exception {
        panel1.setLayout(null);
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
        textColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                textColorButton_actionPerformed(actionevent);
            }

        });
        backgroundColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                backgroundColorButton_actionPerformed(actionevent);
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
        panel1.setMinimumSize(new Dimension(545, 310));
        panel1.setPreferredSize(new Dimension(545, 310));
        setResizable(false);
        getContentPane().add(panel1);
        setTitle("Text Properties");
        OKButton.setText("OK");
        panel1.add(OKButton);
        OKButton.setFont(new Font("Dialog", 0, 12));
        OKButton.setBounds(new Rectangle(168, 272, 79, 22));
        CancelButton.setText("Cancel");
        panel1.add(CancelButton);
        CancelButton.setFont(new Font("Dialog", 0, 12));
        CancelButton.setBounds(new Rectangle(276, 272, 79, 22));
        classNameLabel.setText("class name");
        classNameLabel.setBounds(new Rectangle(8, 4, 389, 24));
        panel1.add(classNameLabel);
        label1.setText("Height:");
        label1.setHorizontalAlignment(4);
        label1.setBounds(new Rectangle(67, 36, 43, 24));
        heightField.setEditable(false);
        heightField.setBounds(new Rectangle(114, 37, 36, 24));
        jLabel1.setText("Width:");
        jLabel1.setHorizontalAlignment(4);
        jLabel1.setBounds(new Rectangle(68, 70, 41, 17));
        widthField.setEditable(false);
        widthField.setBounds(new Rectangle(113, 66, 40, 23));
        label2.setText("x:");
        label2.setHorizontalAlignment(4);
        label2.setBounds(new Rectangle(1, 35, 19, 24));
        xField.setBounds(new Rectangle(25, 36, 36, 24));
        label3.setText("y:");
        label3.setHorizontalAlignment(4);
        label3.setBounds(new Rectangle(1, 67, 19, 24));
        yField.setBounds(new Rectangle(27, 67, 36, 24));
        label6.setText("Font Size:");
        label6.setHorizontalAlignment(4);
        label6.setBounds(new Rectangle(12, 108, 64, 24));
        panel1.add(label6);
        fontSizeField.setBounds(new Rectangle(84, 108, 36, 24));
        panel1.add(fontSizeField);
        label4.setText("Text:");
        label4.setHorizontalAlignment(4);
        label4.setBounds(new Rectangle(161, 36, 40, 24));
        textField.setBounds(new Rectangle(209, 36, 324, 24));
        textAreaScroll.setBounds(new Rectangle(209, 36, 209, 67));
        label5.setText("Face:");
        label5.setHorizontalAlignment(4);
        label5.setBounds(new Rectangle(136, 108, 36, 24));
        panel1.add(label5);
        faceNameField.setBounds(new Rectangle(180, 108, 324, 24));
        panel1.add(faceNameField);
        visibleBox.setText("Visible");
        visibleBox.setBounds(new Rectangle(24, 144, 104, 24));
        panel1.add(visibleBox);
        selectableBox.setText("Selectable");
        selectableBox.setBounds(new Rectangle(24, 168, 104, 24));
        panel1.add(selectableBox);
        resizableBox.setText("Resizable");
        resizableBox.setBounds(new Rectangle(24, 192, 104, 24));
        panel1.add(resizableBox);
        draggableBox.setText("Draggable");
        draggableBox.setBounds(new Rectangle(24, 216, 104, 24));
        panel1.add(draggableBox);
        twoDScale.setText("2D Scale");
        twoDScale.setBounds(new Rectangle(24, 240, 104, 24));
        panel1.add(twoDScale);
        autoResize.setText("AutoResize");
        autoResize.setBounds(new Rectangle(132, 144, 94, 24));
        panel1.add(autoResize);
        multilineBox.setText("Multiline");
        multilineBox.setBounds(new Rectangle(132, 168, 94, 24));
        panel1.add(multilineBox);
        clipping.setText("Clipping");
        clipping.setBounds(new Rectangle(132, 192, 94, 24));
        panel1.add(clipping);
        wrapping.setText("Wrapping");
        wrapping.setBounds(new Rectangle(132, 216, 94, 24));
        panel1.add(wrapping);
        editableBox.setText("Editable");
        editableBox.setBounds(new Rectangle(132, 240, 94, 24));
        panel1.add(editableBox);
        editSingle.setText("Edit on Single Click");
        panel1.add(editSingle);
        editSingle.setBounds(new Rectangle(228, 240, 140, 24));
        boldBox.setText("Bold");
        boldBox.setBounds(new Rectangle(228, 144, 94, 24));
        panel1.add(boldBox);
        italicBox.setText("Italic");
        italicBox.setBounds(new Rectangle(228, 168, 94, 24));
        panel1.add(italicBox);
        underlineBox.setText("Underline");
        underlineBox.setBounds(new Rectangle(228, 192, 94, 24));
        panel1.add(underlineBox);
        strikeBox.setText("Strike");
        strikeBox.setBounds(new Rectangle(228, 216, 94, 24));
        panel1.add(strikeBox);
        alignLeftRadio.setText("Align Left");
        alignGroup.add(alignLeftRadio);
        alignLeftRadio.setBounds(new Rectangle(324, 144, 94, 24));
        panel1.add(alignLeftRadio);
        alignCenterRadio.setText("Center");
        alignGroup.add(alignCenterRadio);
        alignCenterRadio.setBounds(new Rectangle(324, 168, 94, 24));
        panel1.add(alignCenterRadio);
        alignRightRadio.setText("Align Right");
        alignGroup.add(alignRightRadio);
        alignRightRadio.setBounds(new Rectangle(324, 192, 94, 24));
        panel1.add(alignRightRadio);
        textColorButton.setText("Text Color...");
        textColorButton.setBackground(Color.lightGray);
        textColorButton.setBounds(new Rectangle(420, 136, 117, 24));
        panel1.add(textColorButton);
        backgroundColorButton.setText("Background...");
        backgroundColorButton.setBackground(Color.lightGray);
        backgroundColorButton.setBounds(new Rectangle(420, 166, 117, 24));
        panel1.add(backgroundColorButton);
        transparentBox.setText("Transparent");
        transparentBox.setBounds(new Rectangle(420, 192, 96, 24));
        panel1.add(transparentBox);
        selectBack.setText("Select Background");
        panel1.add(selectBack);
        selectBack.setBounds(new Rectangle(420, 216, 130, 24));
        label7.setText("Wrap Width:");
        label7.setHorizontalAlignment(4);
        label7.setBounds(new Rectangle(408, 240, 83, 24));
        wrapwidth.setBounds(new Rectangle(502, 240, 36, 24));
        panel1.add(label7);
        panel1.add(wrapwidth);
        panel1.add(yField);
        panel1.add(label2);
        panel1.add(xField);
        panel1.add(label3);
        panel1.add(label1);
        panel1.add(heightField);
        panel1.add(textAreaScroll);
        panel1.add(label4);
        panel1.add(textField);
        panel1.add(widthField, null);
        panel1.add(jLabel1, null);
    }

    void UpdateDialog() {
        if (myObject == null)
            return;
        classNameLabel.setText(myObject.getClass().getName());
        Rectangle rectangle = myObject.getBoundingRect();
        heightField.setText(String.valueOf(rectangle.height));
        widthField.setText(String.valueOf(rectangle.width));
        Point point = myObject.getLocation();
        xField.setText(String.valueOf(point.x));
        yField.setText(String.valueOf(point.y));
        fontSizeField.setText(String.valueOf(myObject.getFontSize()));
        visibleBox.setSelected(myObject.isVisible());
        selectableBox.setSelected(myObject.isSelectable());
        resizableBox.setSelected(myObject.isResizable());
        draggableBox.setSelected(myObject.isDraggable());
        editableBox.setSelected(myObject.isEditable());
        boldBox.setSelected(myObject.isBold());
        italicBox.setSelected(myObject.isItalic());
        underlineBox.setSelected(myObject.isUnderline());
        strikeBox.setSelected(myObject.isStrikeThrough());
        textField.setText(myObject.getText());
        textArea.setText(myObject.getText());
        if (myObject.isMultiline()) {
            textField.setVisible(false);
            textAreaScroll.setVisible(true);
        } else {
            textField.setVisible(true);
            textAreaScroll.setVisible(false);
        }
        multilineBox.setSelected(myObject.isMultiline());
        faceNameField.setText(myObject.getFaceName());
        int i = myObject.getAlignment();
        if (i == 1)
            alignLeftRadio.setSelected(true);
        else if (i == 3)
            alignRightRadio.setSelected(true);
        else
            alignCenterRadio.setSelected(true);
        myTextColor = myObject.getTextColor();
        myBkColor = myObject.getBkColor();
        transparentBox.setSelected(myObject.isTransparent());
        editSingle.setSelected(myObject.isEditOnSingleClick());
        twoDScale.setSelected(myObject.is2DScale());
        clipping.setSelected(myObject.isClipping());
        wrapping.setSelected(myObject.isWrapping());
        autoResize.setSelected(myObject.isAutoResize());
        selectBack.setSelected(myObject.isSelectBackground());
        wrapwidth.setText(String.valueOf(myObject.getWrappingWidth()));
    }

    void UpdateControl() {
        if (myObject == null)
            return;
        Point point = new Point(Integer.parseInt(xField.getText()),
                Integer.parseInt(yField.getText()));
        myObject.setLocation(point);
        myObject.setFontSize(Integer.parseInt(fontSizeField.getText()));
        myObject.setVisible(visibleBox.isSelected());
        myObject.setSelectable(selectableBox.isSelected());
        myObject.setResizable(resizableBox.isSelected());
        myObject.setDraggable(draggableBox.isSelected());
        myObject.setEditable(editableBox.isSelected());
        myObject.setBold(boldBox.isSelected());
        myObject.setItalic(italicBox.isSelected());
        myObject.setUnderline(underlineBox.isSelected());
        myObject.setStrikeThrough(strikeBox.isSelected());
        if (myObject.isMultiline())
            myObject.setText(textArea.getText());
        else
            myObject.setText(textField.getText());
        myObject.setMultiline(multilineBox.isSelected());
        myObject.setFaceName(faceNameField.getText());
        byte byte0;
        if (alignLeftRadio.isSelected())
            byte0 = 1;
        else if (alignRightRadio.isSelected())
            byte0 = 3;
        else
            byte0 = 2;
        myObject.setAlignment(byte0);
        myObject.setTextColor(myTextColor);
        myObject.setBkColor(myBkColor);
        myObject.setTransparent(transparentBox.isSelected());
        myObject.setEditOnSingleClick(editSingle.isSelected());
        myObject.set2DScale(twoDScale.isSelected());
        myObject.setClipping(clipping.isSelected());
        myObject.setWrapping(wrapping.isSelected());
        myObject.setAutoResize(autoResize.isSelected());
        myObject.setSelectBackground(selectBack.isSelected());
        myObject.setWrappingWidth(Integer.parseInt(wrapwidth.getText()));
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
            System.err.println(exception.toString());
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

    void textColorButton_actionPerformed(ActionEvent actionevent) {
        Color color = JColorChooser.showDialog(this, "Foreground Color",
                myTextColor);
        if (color != null)
            myTextColor = color;
    }

    void backgroundColorButton_actionPerformed(ActionEvent actionevent) {
        Color color = JColorChooser.showDialog(this, "Foreground Color",
                myBkColor);
        if (color != null)
            myBkColor = color;
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
    JLabel jLabel1;
    JTextField widthField;
    JTextField xField;
    JLabel label2;
    JTextField yField;
    JLabel label3;
    JCheckBox visibleBox;
    JCheckBox selectableBox;
    JCheckBox resizableBox;
    JCheckBox draggableBox;
    JLabel label4;
    JCheckBox editableBox;
    JCheckBox boldBox;
    JCheckBox italicBox;
    JCheckBox underlineBox;
    JCheckBox strikeBox;
    JTextField textField;
    JLabel label5;
    JTextField faceNameField;
    ButtonGroup alignGroup;
    JRadioButton alignLeftRadio;
    JRadioButton alignCenterRadio;
    JRadioButton alignRightRadio;
    JCheckBox multilineBox;
    JLabel label6;
    JTextField fontSizeField;
    JButton textColorButton;
    JButton backgroundColorButton;
    JCheckBox transparentBox;
    JTextArea textArea;
    JScrollPane textAreaScroll;
    JLabel classNameLabel;
    JCheckBox editSingle;
    JCheckBox selectBack;
    JCheckBox twoDScale;
    JCheckBox clipping;
    JCheckBox autoResize;
    JCheckBox wrapping;
    JLabel label7;
    JTextField wrapwidth;
    BorderLayout borderLayout1;
    Color myTextColor;
    Color myBkColor;
    public GraphicViewerText myObject;
    boolean fComponentsAdjusted;
}