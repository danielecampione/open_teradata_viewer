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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

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
    JLabel classNameLabel = new JLabel();
    JLabel areaLabel = new JLabel();
    JCheckBox pickableBackgroundBox = new JCheckBox();

    public GraphicViewerObject myObject;

    public ObjectPropsDialog(Frame frame, String title, boolean modal,
            GraphicViewerObject obj) {
        super(frame, title, modal);
        try {
            jbInit();
            pack();
            myObject = obj;
            UpdateDialog();
        } catch (Exception ex) {
            ExceptionDialog.hideException(ex);
        }
    }

    public ObjectPropsDialog() {
        this(null, "", false, null);
    }

    void jbInit() throws Exception {
        panel1.setLayout(null);
        panel1.setMinimumSize(new Dimension(294, 241));
        panel1.setPreferredSize(new Dimension(294, 241));
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
        this.setResizable(false);
        getContentPane().add(panel1);

        OKButton.setText("OK");
        panel1.add(OKButton);
        OKButton.setFont(new Font("Dialog", Font.PLAIN, 12));
        OKButton.setBounds(new Rectangle(60, 204, 79, 22));
        CancelButton.setText("Cancel");
        panel1.add(CancelButton);
        CancelButton.setFont(new Font("Dialog", Font.PLAIN, 12));
        CancelButton.setBounds(new Rectangle(168, 204, 79, 22));
        label1.setText("Height:");
        label1.setHorizontalAlignment(JLabel.RIGHT);
        panel1.add(label1);
        label1.setBounds(new Rectangle(132, 60, 48, 24));
        panel1.add(heightField);
        heightField.setBounds(new Rectangle(192, 60, 36, 24));
        panel1.add(xField);
        xField.setBounds(new Rectangle(84, 36, 36, 24));
        label2.setText("x:");
        label2.setHorizontalAlignment(JLabel.RIGHT);
        panel1.add(label2);
        label2.setBounds(new Rectangle(24, 36, 48, 24));
        panel1.add(yField);
        yField.setBounds(new Rectangle(84, 60, 36, 24));
        label3.setText("y:");
        label3.setHorizontalAlignment(JLabel.RIGHT);
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
        label4.setHorizontalAlignment(JLabel.RIGHT);
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
        if (myObject instanceof GraphicViewerArea) {
            GraphicViewerArea area = (GraphicViewerArea) myObject;
            areaLabel.setVisible(true);
            pickableBackgroundBox.setVisible(true);
            pickableBackgroundBox.setSelected(area.isPickableBackground());
        } else {
            areaLabel.setVisible(false);
            pickableBackgroundBox.setVisible(false);
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
        if (myObject instanceof GraphicViewerArea) {
            GraphicViewerArea area = (GraphicViewerArea) myObject;
            area.setPickableBackground(pickableBackgroundBox.isSelected());
        }
    }

    public void addNotify() {
        // Record the size of the window prior to calling parents addNotify.
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
}