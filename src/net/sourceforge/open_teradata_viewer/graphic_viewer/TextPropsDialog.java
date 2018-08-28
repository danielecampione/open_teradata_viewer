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
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
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

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TextPropsDialog extends JDialog {

    private static final long serialVersionUID = 1166423308230767362L;

    JPanel panel1 = new JPanel();

    JButton OKButton = new JButton();
    JButton CancelButton = new JButton();
    JLabel label1 = new JLabel();
    JTextField heightField = new JTextField();
    JLabel jLabel1 = new JLabel();
    JTextField widthField = new JTextField();
    JTextField xField = new JTextField();
    JLabel label2 = new JLabel();
    JTextField yField = new JTextField();
    JLabel label3 = new JLabel();
    JCheckBox visibleBox = new JCheckBox();
    JCheckBox selectableBox = new JCheckBox();
    JCheckBox resizableBox = new JCheckBox();
    JCheckBox draggableBox = new JCheckBox();
    JLabel label4 = new JLabel();
    JCheckBox editableBox = new JCheckBox();
    JCheckBox boldBox = new JCheckBox();
    JCheckBox italicBox = new JCheckBox();
    JCheckBox underlineBox = new JCheckBox();
    JCheckBox strikeBox = new JCheckBox();
    JTextField textField = new JTextField();
    JLabel label5 = new JLabel();
    JTextField faceNameField = new JTextField();
    ButtonGroup alignGroup = new ButtonGroup();
    JRadioButton alignLeftRadio = new JRadioButton();
    JRadioButton alignCenterRadio = new JRadioButton();
    JRadioButton alignRightRadio = new JRadioButton();
    JCheckBox multilineBox = new JCheckBox();
    JLabel label6 = new JLabel();
    JTextField fontSizeField = new JTextField();
    JButton textColorButton = new JButton();
    JButton backgroundColorButton = new JButton();
    JCheckBox transparentBox = new JCheckBox();
    JTextArea textArea = new JTextArea();
    JScrollPane textAreaScroll = new JScrollPane(textArea);
    JLabel classNameLabel = new JLabel();
    JCheckBox editSingle = new JCheckBox();
    JCheckBox selectBack = new JCheckBox();
    JCheckBox twoDScale = new JCheckBox();
    JCheckBox clipping = new JCheckBox();
    JCheckBox autoResize = new JCheckBox();
    JCheckBox wrapping = new JCheckBox();
    JLabel label7 = new JLabel();
    JTextField wrapwidth = new JTextField();

    BorderLayout borderLayout1 = new BorderLayout();

    Color myTextColor;
    Color myBkColor;

    public GraphicViewerText myObject;

    public TextPropsDialog(Frame frame, String title, boolean modal,
            GraphicViewerText obj) {
        super(frame, title, modal);
        try {
            jbInit();
            myObject = obj;
            UpdateDialog();
            pack();
        } catch (Exception ex) {
            ExceptionDialog.hideException(ex);
        }
    }

    public TextPropsDialog() {
        this(null, "", false, null);
    }

    void jbInit() throws Exception {
        panel1.setLayout(null);
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
        textColorButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                textColorButton_actionPerformed(e);
            }
        });
        backgroundColorButton
                .addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        backgroundColorButton_actionPerformed(e);
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
        panel1.setMinimumSize(new Dimension(545, 310));
        panel1.setPreferredSize(new Dimension(545, 310));
        this.setResizable(false);
        getContentPane().add(panel1);

        setTitle("Text Properties");

        OKButton.setText("OK");
        panel1.add(OKButton);
        OKButton.setFont(new Font("Dialog", Font.PLAIN, 12));
        OKButton.setBounds(new Rectangle(168, 272, 79, 22));

        CancelButton.setText("Cancel");
        panel1.add(CancelButton);
        CancelButton.setFont(new Font("Dialog", Font.PLAIN, 12));
        CancelButton.setBounds(new Rectangle(276, 272, 79, 22));

        classNameLabel.setText("class name");
        classNameLabel.setBounds(new Rectangle(8, 4, 389, 24));
        panel1.add(classNameLabel);

        label1.setText("Height:");
        label1.setHorizontalAlignment(JLabel.RIGHT);
        label1.setBounds(new Rectangle(67, 36, 43, 24));
        heightField.setEditable(false);
        heightField.setBounds(new Rectangle(114, 37, 36, 24));

        jLabel1.setText("Width:");
        jLabel1.setHorizontalAlignment(JLabel.RIGHT);
        jLabel1.setBounds(new Rectangle(68, 70, 41, 17));
        widthField.setEditable(false);
        widthField.setBounds(new Rectangle(113, 66, 40, 23));

        label2.setText("x:");
        label2.setHorizontalAlignment(JLabel.RIGHT);
        label2.setBounds(new Rectangle(1, 35, 19, 24));
        xField.setBounds(new Rectangle(25, 36, 36, 24));

        label3.setText("y:");
        label3.setHorizontalAlignment(JLabel.RIGHT);
        label3.setBounds(new Rectangle(1, 67, 19, 24));
        yField.setBounds(new Rectangle(27, 67, 36, 24));

        label6.setText("Font Size:");
        label6.setHorizontalAlignment(JLabel.RIGHT);
        label6.setBounds(new Rectangle(12, 108, 64, 24));
        panel1.add(label6);
        fontSizeField.setBounds(new Rectangle(84, 108, 36, 24));
        panel1.add(fontSizeField);

        label4.setText("Text:");
        label4.setHorizontalAlignment(JLabel.RIGHT);
        label4.setBounds(new Rectangle(161, 36, 40, 24));
        textField.setBounds(new Rectangle(209, 36, 324, 24));
        textAreaScroll.setBounds(new Rectangle(209, 36, 209, 67));

        label5.setText("Face:");
        label5.setHorizontalAlignment(JLabel.RIGHT);
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
        textColorButton.setBackground(java.awt.Color.lightGray);
        textColorButton.setBounds(new Rectangle(420, 136, 117, 24));
        panel1.add(textColorButton);

        backgroundColorButton.setText("Background...");
        backgroundColorButton.setBackground(java.awt.Color.lightGray);
        backgroundColorButton.setBounds(new Rectangle(420, 166, 117, 24));
        panel1.add(backgroundColorButton);

        transparentBox.setText("Transparent");
        transparentBox.setBounds(new Rectangle(420, 192, 96, 24));
        panel1.add(transparentBox);

        selectBack.setText("Select Background");
        panel1.add(selectBack);
        selectBack.setBounds(new Rectangle(420, 216, 130, 24));

        label7.setText("Wrap Width:");
        label7.setHorizontalAlignment(JLabel.RIGHT);
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
        if (myObject == null) {
            return;
        }

        classNameLabel.setText(myObject.getClass().getName());
        Rectangle rect = myObject.getBoundingRect();
        heightField.setText(String.valueOf(rect.height));
        widthField.setText(String.valueOf(rect.width));
        Point pt = myObject.getLocation(); // dependent on alignment
        xField.setText(String.valueOf(pt.x));
        yField.setText(String.valueOf(pt.y));
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
        int align = myObject.getAlignment();
        if (align == GraphicViewerText.ALIGN_LEFT) {
            alignLeftRadio.setSelected(true);
        } else if (align == GraphicViewerText.ALIGN_RIGHT) {
            alignRightRadio.setSelected(true);
        } else {
            alignCenterRadio.setSelected(true);
        }
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
        if (myObject == null) {
            return;
        }

        Point newpt = new Point(Integer.parseInt(xField.getText()),
                Integer.parseInt(yField.getText()));
        myObject.setLocation(newpt); // do this before we change the alignment
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
        if (myObject.isMultiline()) {
            myObject.setText(textArea.getText());
        } else {
            myObject.setText(textField.getText());
        }
        myObject.setMultiline(multilineBox.isSelected());
        myObject.setFaceName(faceNameField.getText());
        int align;
        if (alignLeftRadio.isSelected()) {
            align = GraphicViewerText.ALIGN_LEFT;
        } else if (alignRightRadio.isSelected()) {
            align = GraphicViewerText.ALIGN_RIGHT;
        } else {
            align = GraphicViewerText.ALIGN_CENTER;
        }
        myObject.setAlignment(align);
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
            System.err.println(e.toString());
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

    void textColorButton_actionPerformed(ActionEvent e) {
        Color newcolor = JColorChooser.showDialog(this, "Foreground Color",
                myTextColor);
        if (newcolor != null) {
            myTextColor = newcolor;
        }
    }

    void backgroundColorButton_actionPerformed(ActionEvent e) {
        Color newcolor = JColorChooser.showDialog(this, "Foreground Color",
                myBkColor);
        if (newcolor != null) {
            myBkColor = newcolor;
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