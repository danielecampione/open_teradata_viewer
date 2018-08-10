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
import javax.swing.JTextField;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GridOptionsDialog extends JDialog {

    private static final long serialVersionUID = 3486810311289326513L;

    public GridOptionsDialog(Frame frame, String s, boolean flag,
            GraphicViewerView graphicviewerview) {
        super(frame, s, flag);
        panel1 = new JPanel();
        OKButton = new JButton();
        CancelButton = new JButton();
        label4 = new JLabel();
        widthField = new JTextField();
        label1 = new JLabel();
        heightField = new JTextField();
        gridStyleGroup = new ButtonGroup();
        gridInvisibleRadio = new JRadioButton();
        gridDotsRadio = new JRadioButton();
        gridCrossesRadio = new JRadioButton();
        gridLinesRadio = new JRadioButton();
        label2 = new JLabel();
        label3 = new JLabel();
        paperColorButton = new JButton();
        moveSnapGroup = new ButtonGroup();
        moveNoSnapRadio = new JRadioButton();
        moveJumpRadio = new JRadioButton();
        moveAfterRadio = new JRadioButton();
        jLabel2 = new JLabel();
        resizeSnapGroup = new ButtonGroup();
        resizeAfterRadio = new JRadioButton();
        resizeJumpRadio = new JRadioButton();
        resizeNoSnapRadio = new JRadioButton();
        jLabel3 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        fontName = new JTextField();
        fontSize = new JTextField();
        myView = null;
        fComponentsAdjusted = false;
        hideDisabledScrollBars = new JCheckBox();
        dragEnabled = new JCheckBox();
        dragsSelectionImage = new JCheckBox();
        dropEnabled = new JCheckBox();
        dragsRealtime = new JCheckBox();
        includeNegativeCoords = new JCheckBox();
        mouseEnabled = new JCheckBox();
        keyEnabled = new JCheckBox();
        try {
            jbInit();
            pack();
            myView = graphicviewerview;
            UpdateDialog();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public GridOptionsDialog() {
        this(null, "", false, null);
    }

    void jbInit() throws Exception {
        panel1.setLayout(null);
        panel1.setMinimumSize(new Dimension(586, 265));
        panel1.setPreferredSize(new Dimension(586, 265));
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
        paperColorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionevent) {
                paperColorButton_actionPerformed(actionevent);
            }

        });
        moveNoSnapRadio.setText("No snap");
        moveNoSnapRadio.setBounds(new Rectangle(255, 36, 86, 24));
        moveJumpRadio.setText("Jump");
        moveJumpRadio.setBounds(new Rectangle(255, 60, 86, 24));
        moveAfterRadio.setText("Afterwards");
        moveAfterRadio.setBounds(new Rectangle(255, 84, 86, 24));
        jLabel2.setBounds(new Rectangle(243, 12, 98, 24));
        jLabel2.setBounds(new Rectangle(339, 13, 98, 24));
        jLabel2.setText("Snap On Move:");
        resizeAfterRadio.setText("Afterwards");
        resizeAfterRadio.setBounds(new Rectangle(354, 84, 86, 24));
        resizeJumpRadio.setText("Jump");
        resizeJumpRadio.setBounds(new Rectangle(354, 60, 86, 24));
        resizeNoSnapRadio.setText("No snap");
        resizeNoSnapRadio.setBounds(new Rectangle(354, 36, 86, 24));
        jLabel3.setText("Snap on Resize:");
        jLabel3.setBounds(new Rectangle(340, 12, 98, 24));
        hideDisabledScrollBars.setText("Hide Disabled Scrollbars");
        hideDisabledScrollBars.setBounds(new Rectangle(275, 155, 166, 20));
        dragEnabled.setText("Drag Enabled");
        dragEnabled.setBounds(new Rectangle(160, 140, 104, 20));
        dragsSelectionImage.setText("Drags Selection Image");
        dragsSelectionImage.setBounds(new Rectangle(160, 185, 155, 20));
        dropEnabled.setText("Drop Enabled");
        dropEnabled.setBounds(new Rectangle(160, 155, 100, 20));
        dragsRealtime.setText("Drags Realtime");
        dragsRealtime.setBounds(new Rectangle(160, 170, 110, 20));
        includeNegativeCoords.setText("Include Negative Coords");
        includeNegativeCoords.setBounds(new Rectangle(275, 140, 157, 20));
        mouseEnabled.setText("Mouse Enabled");
        mouseEnabled.setBounds(new Rectangle(25, 155, 111, 20));
        keyEnabled.setText("Key Enabled");
        keyEnabled.setBounds(new Rectangle(25, 140, 98, 20));
        setResizable(false);
        getContentPane().add(panel1);
        OKButton.setText("OK");
        OKButton.setFont(new Font("Dialog", 0, 12));
        OKButton.setBounds(new Rectangle(185, 228, 79, 22));
        CancelButton.setText("Cancel");
        CancelButton.setFont(new Font("Dialog", 0, 12));
        CancelButton.setBounds(new Rectangle(293, 228, 79, 22));
        label4.setText("Width:");
        label4.setHorizontalAlignment(4);
        panel1.add(label4);
        label4.setBounds(new Rectangle(30, 36, 48, 24));
        panel1.add(widthField);
        widthField.setBounds(new Rectangle(90, 36, 36, 24));
        label1.setText("Height:");
        label1.setHorizontalAlignment(4);
        panel1.add(label1);
        label1.setBounds(new Rectangle(30, 60, 48, 24));
        panel1.add(heightField);
        heightField.setBounds(new Rectangle(90, 60, 36, 24));
        gridInvisibleRadio.setText("Invisible");
        panel1.add(gridInvisibleRadio);
        gridStyleGroup.add(gridInvisibleRadio);
        gridInvisibleRadio.setBounds(new Rectangle(161, 36, 86, 24));
        gridDotsRadio.setText("Dots");
        gridStyleGroup.add(gridDotsRadio);
        panel1.add(gridDotsRadio);
        gridDotsRadio.setBounds(new Rectangle(161, 60, 86, 24));
        gridCrossesRadio.setText("Crosses");
        panel1.add(gridCrossesRadio);
        gridStyleGroup.add(gridCrossesRadio);
        gridCrossesRadio.setBounds(new Rectangle(161, 84, 86, 24));
        gridLinesRadio.setText("Lines");
        panel1.add(gridLinesRadio);
        gridStyleGroup.add(gridLinesRadio);
        gridLinesRadio.setBounds(new Rectangle(161, 108, 86, 24));
        label2.setText("Grid Style:");
        panel1.add(label2);
        label2.setBounds(new Rectangle(149, 12, 98, 24));
        label3.setText("Grid Size:");
        panel1.add(label3);
        label3.setBounds(new Rectangle(29, 12, 96, 24));
        paperColorButton.setText("Paper Color...");
        moveSnapGroup.add(moveNoSnapRadio);
        panel1.add(moveNoSnapRadio, null);
        moveSnapGroup.add(moveJumpRadio);
        panel1.add(moveJumpRadio, null);
        moveSnapGroup.add(moveAfterRadio);
        panel1.add(moveAfterRadio, null);
        panel1.add(jLabel2, null);
        resizeSnapGroup.add(resizeNoSnapRadio);
        panel1.add(resizeNoSnapRadio, null);
        resizeSnapGroup.add(resizeJumpRadio);
        panel1.add(resizeJumpRadio, null);
        resizeSnapGroup.add(resizeAfterRadio);
        panel1.add(resizeAfterRadio, null);
        panel1.add(jLabel3, null);
        paperColorButton.setBackground(Color.lightGray);
        paperColorButton.setBounds(new Rectangle(453, 146, 116, 24));
        jLabel2.setBounds(new Rectangle(243, 12, 98, 24));
        jLabel2.setBounds(new Rectangle(243, 12, 98, 24));
        label5.setText("Default Text Properties:");
        panel1.add(label5);
        label5.setBounds(new Rectangle(445, 12, 130, 24));
        label6.setText("Default Font Name:");
        panel1.add(label6);
        label6.setBounds(new Rectangle(445, 34, 130, 24));
        panel1.add(fontName);
        fontName.setBounds(new Rectangle(445, 58, 130, 24));
        label7.setText("Default Font Size:");
        panel1.add(label7);
        label7.setBounds(new Rectangle(445, 82, 130, 24));
        panel1.add(fontSize);
        panel1.add(paperColorButton);
        panel1.add(dragEnabled, null);
        panel1.add(dropEnabled, null);
        panel1.add(dragsRealtime, null);
        panel1.add(includeNegativeCoords, null);
        panel1.add(hideDisabledScrollBars, null);
        panel1.add(keyEnabled, null);
        panel1.add(mouseEnabled, null);
        panel1.add(CancelButton);
        panel1.add(OKButton);
        panel1.add(dragsSelectionImage, null);
        fontSize.setBounds(new Rectangle(445, 106, 130, 24));
    }

    void UpdateDialog() {
        if (myView == null)
            return;
        widthField.setText(String.valueOf(myView.getGridWidth()));
        heightField.setText(String.valueOf(myView.getGridHeight()));
        int i = myView.getGridStyle();
        if (i == 0)
            gridInvisibleRadio.setSelected(true);
        else if (i == 1)
            gridDotsRadio.setSelected(true);
        else if (i == 2)
            gridCrossesRadio.setSelected(true);
        else if (i == 3)
            gridLinesRadio.setSelected(true);
        int j = myView.getSnapMove();
        if (j == 0)
            moveNoSnapRadio.setSelected(true);
        else if (j == 1)
            moveJumpRadio.setSelected(true);
        else if (j == 2)
            moveAfterRadio.setSelected(true);
        int k = myView.getSnapResize();
        if (k == 0)
            resizeNoSnapRadio.setSelected(true);
        else if (k == 1)
            resizeJumpRadio.setSelected(true);
        else if (k == 2)
            resizeAfterRadio.setSelected(true);
        myPaperColor = myView.getDocument().getPaperColor();
        fontSize.setText(String.valueOf(GraphicViewerText.getDefaultFontSize()));
        fontName.setText(GraphicViewerText.getDefaultFontFaceName());
        keyEnabled.setSelected(myView.isKeyEnabled());
        mouseEnabled.setSelected(myView.isMouseEnabled());
        dragEnabled.setSelected(myView.isDragEnabled());
        dropEnabled.setSelected(myView.isDropEnabled());
        dragsRealtime.setSelected(myView.isDragsRealtime());
        dragsSelectionImage.setSelected(myView.isDragsSelectionImage());
        includeNegativeCoords.setSelected(myView.isIncludingNegativeCoords());
        hideDisabledScrollBars.setSelected(myView.isHidingDisabledScrollbars());
    }

    void UpdateControl() {
        if (myView == null)
            return;
        myView.setGridWidth(Integer.parseInt(widthField.getText()));
        myView.setGridHeight(Integer.parseInt(heightField.getText()));
        byte byte0 = 0;
        if (gridInvisibleRadio.isSelected())
            byte0 = 0;
        else if (gridDotsRadio.isSelected())
            byte0 = 1;
        else if (gridCrossesRadio.isSelected())
            byte0 = 2;
        else if (gridLinesRadio.isSelected())
            byte0 = 3;
        myView.setGridStyle(byte0);
        byte byte1 = 0;
        if (moveNoSnapRadio.isSelected())
            byte1 = 0;
        else if (moveJumpRadio.isSelected())
            byte1 = 1;
        else if (moveAfterRadio.isSelected())
            byte1 = 2;
        myView.setSnapMove(byte1);
        byte byte2 = 0;
        if (resizeNoSnapRadio.isSelected())
            byte2 = 0;
        else if (resizeJumpRadio.isSelected())
            byte2 = 1;
        else if (resizeAfterRadio.isSelected())
            byte2 = 2;
        myView.setSnapResize(byte2);
        myView.getDocument().setPaperColor(myPaperColor);
        GraphicViewerText.setDefaultFontFaceName(fontName.getText());
        GraphicViewerText.setDefaultFontSize(Integer.parseInt(fontSize
                .getText()));
        myView.setKeyEnabled(keyEnabled.isSelected());
        myView.setMouseEnabled(mouseEnabled.isSelected());
        myView.setDragEnabled(dragEnabled.isSelected());
        myView.setDropEnabled(dropEnabled.isSelected());
        myView.setDragsRealtime(dragsRealtime.isSelected());
        myView.setDragsSelectionImage(dragsSelectionImage.isSelected());
        myView.setIncludingNegativeCoords(includeNegativeCoords.isSelected());
        myView.setHidingDisabledScrollbars(hideDisabledScrollBars.isSelected());
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

    void paperColorButton_actionPerformed(ActionEvent actionevent) {
        Color color = JColorChooser.showDialog(this, "Paper Color",
                myPaperColor);
        if (color != null)
            myPaperColor = color;
    }

    JPanel panel1;
    JButton OKButton;
    JButton CancelButton;
    JLabel label4;
    JTextField widthField;
    JLabel label1;
    JTextField heightField;
    ButtonGroup gridStyleGroup;
    JRadioButton gridInvisibleRadio;
    JRadioButton gridDotsRadio;
    JRadioButton gridCrossesRadio;
    JRadioButton gridLinesRadio;
    JLabel label2;
    JLabel label3;
    JButton paperColorButton;
    ButtonGroup moveSnapGroup;
    JRadioButton moveNoSnapRadio;
    JRadioButton moveJumpRadio;
    JRadioButton moveAfterRadio;
    JLabel jLabel2;
    ButtonGroup resizeSnapGroup;
    JRadioButton resizeAfterRadio;
    JRadioButton resizeJumpRadio;
    JRadioButton resizeNoSnapRadio;
    JLabel jLabel3;
    JLabel label5;
    JLabel label6;
    JLabel label7;
    JTextField fontName;
    JTextField fontSize;
    Color myPaperColor;
    public GraphicViewerView myView;
    boolean fComponentsAdjusted;
    JCheckBox hideDisabledScrollBars;
    JCheckBox dragEnabled;
    JCheckBox dragsSelectionImage;
    JCheckBox dropEnabled;
    JCheckBox dragsRealtime;
    JCheckBox includeNegativeCoords;
    JCheckBox mouseEnabled;
    JCheckBox keyEnabled;
}