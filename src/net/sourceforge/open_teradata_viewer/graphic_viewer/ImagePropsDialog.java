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
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

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
public class ImagePropsDialog extends JDialog {

    private static final long serialVersionUID = -1971740034036973365L;

    public ImagePropsDialog(Frame frame, String s, boolean flag,
            GraphicViewerImage graphicviewerimage) {
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
        classNameLabel = new JLabel();
        fromLabel = new JLabel();
        location = new JTextField();
        group1 = new ButtonGroup();
        fromFile = new JRadioButton();
        fromURL = new JRadioButton();
        label5 = new JLabel();
        transparent = new JCheckBox();
        transButton = new JButton();
        myFile = null;
        myURL = null;
        myImage = null;
        myColor = null;
        fComponentsAdjusted = false;
        try {
            myObject = graphicviewerimage;
            jbInit();
            pack();
            UpdateDialog();
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
    }

    public ImagePropsDialog() {
        this(null, "", false, null);
    }

    void jbInit() throws Exception {
        panel1.setLayout(null);
        panel1.setMinimumSize(new Dimension(294, 331));
        panel1.setPreferredSize(new Dimension(294, 331));
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
        transButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                transButton_actionPerformed(actionevent);
            }

        });
        setResizable(false);
        getContentPane().add(panel1);
        OKButton.setText("OK");
        panel1.add(OKButton);
        OKButton.setFont(new Font("Dialog", 0, 12));
        OKButton.setBounds(new Rectangle(56, 302, 79, 22));
        CancelButton.setText("Cancel");
        panel1.add(CancelButton);
        CancelButton.setFont(new Font("Dialog", 0, 12));
        CancelButton.setBounds(new Rectangle(164, 302, 79, 22));
        label1.setText("Height:");
        label1.setHorizontalAlignment(4);
        panel1.add(label1);
        label1.setBounds(new Rectangle(132, 60, 48, 24));
        panel1.add(heightField);
        heightField.setBounds(new Rectangle(192, 60, 36, 24));
        panel1.add(xField);
        xField.setBounds(new Rectangle(84, 36, 36, 24));
        label2.setText("x:");
        label2.setHorizontalAlignment(4);
        panel1.add(label2);
        label2.setBounds(new Rectangle(24, 36, 48, 24));
        panel1.add(yField);
        yField.setBounds(new Rectangle(84, 60, 36, 24));
        label3.setText("y:");
        label3.setHorizontalAlignment(4);
        panel1.add(label3);
        label3.setBounds(new Rectangle(24, 60, 48, 24));
        visibleBox.setText("Visible");
        panel1.add(visibleBox);
        visibleBox.setBounds(new Rectangle(24, 96, 96, 24));
        selectableBox.setText("Selectable");
        panel1.add(selectableBox);
        selectableBox.setBounds(new Rectangle(24, 120, 96, 24));
        resizableBox.setText("Resizable");
        panel1.add(resizableBox);
        resizableBox.setBounds(new Rectangle(24, 144, 96, 24));
        draggableBox.setText("Draggable");
        panel1.add(draggableBox);
        draggableBox.setBounds(new Rectangle(24, 168, 96, 24));
        label4.setText("Width:");
        label4.setHorizontalAlignment(4);
        panel1.add(label4);
        label4.setBounds(new Rectangle(132, 36, 48, 24));
        panel1.add(widthField);
        widthField.setBounds(new Rectangle(192, 36, 36, 24));
        classNameLabel.setText("class name");
        panel1.add(classNameLabel);
        classNameLabel.setBounds(new Rectangle(23, 5, 264, 24));
        fromLabel.setText("Image Location:");
        panel1.add(fromLabel);
        fromLabel.setBounds(new Rectangle(156, 120, 100, 24));
        group1.add(fromFile);
        group1.add(fromURL);
        fromFile.setText("From File");
        panel1.add(fromFile);
        fromFile.setBounds(new Rectangle(156, 144, 100, 24));
        fromURL.setText("From URL");
        panel1.add(fromURL);
        fromURL.setBounds(new Rectangle(156, 168, 100, 24));
        label5.setText("URL or Pathname:");
        panel1.add(label5);
        label5.setBounds(new Rectangle(24, 192, 150, 24));
        panel1.add(location);
        location.setBounds(new Rectangle(24, 216, 246, 24));
        transparent.setText("Transparent Background Filled?");
        panel1.add(transparent);
        transparent.setBounds(new Rectangle(50, 246, 200, 24));
        transButton.setText("Transparent Fill Color...");
        panel1.add(transButton);
        transButton.setBounds(new Rectangle(64, 270, 166, 24));
    }

    void UpdateDialog() {
        if (myObject == null)
            return;
        myFile = myObject.getFilename();
        myURL = myObject.getURL();
        myImage = myObject.getImage();
        myColor = myObject.getTransparentColor();
        if (myColor != null)
            transparent.setSelected(true);
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
        if (myFile != null) {
            location.setText(myFile);
            fromFile.setSelected(true);
        } else if (myURL != null) {
            location.setText(myURL.toString());
            fromURL.setSelected(true);
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
        if (fromFile.isSelected()) {
            if (!myObject.loadImage(location.getText(), true))
                setPrevious();
        } else {
            try {
                if (!myObject.loadImage(new URL(location.getText()), true))
                    setPrevious();
            } catch (MalformedURLException murle) {
                setPrevious();
            }
        }
        if (!transparent.isSelected())
            myColor = null;
        myObject.setTransparentColor(myColor);
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

    private void setPrevious() {
        if (myURL != null)
            myObject.loadImage(myURL, true);
        else if (myFile != null)
            myObject.loadImage(myFile, true);
        else
            myObject.loadImage(myImage, true);
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
        } catch (Exception e) {
            ExceptionDialog.ignoreException(e);
        }
    }

    void CancelButton_actionPerformed(ActionEvent actionevent) {
        OnCancel();
    }

    void OnCancel() {
        try {
            dispose();
        } catch (Exception e) {
            ExceptionDialog.ignoreException(e);
        }
    }

    void transButton_actionPerformed(ActionEvent actionevent) {
        Color color = JColorChooser.showDialog(this, "Transparent Color",
                myColor);
        if (color != null)
            myColor = color;
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
    JLabel classNameLabel;
    JLabel fromLabel;
    JTextField location;
    ButtonGroup group1;
    JRadioButton fromFile;
    JRadioButton fromURL;
    JLabel label5;
    JCheckBox transparent;
    JButton transButton;
    public GraphicViewerImage myObject;
    private String myFile;
    private URL myURL;
    private Image myImage;
    private Color myColor;
    boolean fComponentsAdjusted;
}