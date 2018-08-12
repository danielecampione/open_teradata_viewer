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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ObjectPropsDialog extends JDialog {

    private static final long serialVersionUID = -5572916216695814118L;

    public ObjectPropsDialog(Frame frame, String s, boolean flag,
            GraphicViewerObject graphicviewerobject) {
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
        areaLabel = new JLabel();
        pickableBackgroundBox = new JCheckBox();
        fComponentsAdjusted = false;
        try {
            jbInit();
            pack();
            myObject = graphicviewerobject;
            UpdateDialog();
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
    }

    public ObjectPropsDialog() {
        this(null, "", false, null);
    }

    void jbInit() throws Exception {
        panel1.setLayout(null);
        panel1.setMinimumSize(new Dimension(294, 241));
        panel1.setPreferredSize(new Dimension(294, 241));
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
        setResizable(false);
        getContentPane().add(panel1);
        OKButton.setText("OK");
        panel1.add(OKButton);
        OKButton.setFont(new Font("Dialog", 0, 12));
        OKButton.setBounds(new Rectangle(60, 204, 79, 22));
        CancelButton.setText("Cancel");
        panel1.add(CancelButton);
        CancelButton.setFont(new Font("Dialog", 0, 12));
        CancelButton.setBounds(new Rectangle(168, 204, 79, 22));
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
        areaLabel.setText("a GraphicViewerArea:");
        panel1.add(areaLabel);
        areaLabel.setBounds(new Rectangle(156, 108, 100, 24));
        pickableBackgroundBox.setText("PickableBackground");
        panel1.add(pickableBackgroundBox);
        pickableBackgroundBox.setBounds(new Rectangle(156, 132, 144, 24));
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
        if (myObject instanceof GraphicViewerArea) {
            GraphicViewerArea graphicviewerarea = (GraphicViewerArea) myObject;
            areaLabel.setVisible(true);
            pickableBackgroundBox.setVisible(true);
            pickableBackgroundBox.setSelected(graphicviewerarea
                    .isPickableBackground());
        } else {
            areaLabel.setVisible(false);
            pickableBackgroundBox.setVisible(false);
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
        if (myObject instanceof GraphicViewerArea) {
            GraphicViewerArea graphicviewerarea = (GraphicViewerArea) myObject;
            graphicviewerarea.setPickableBackground(pickableBackgroundBox
                    .isSelected());
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
    JLabel areaLabel;
    JCheckBox pickableBackgroundBox;
    public GraphicViewerObject myObject;
    boolean fComponentsAdjusted;
}