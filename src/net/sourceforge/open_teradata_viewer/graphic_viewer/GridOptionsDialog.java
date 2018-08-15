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
import javax.swing.JTextField;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GridOptionsDialog extends JDialog {

    private static final long serialVersionUID = 3486810311289326513L;

    JPanel panel1 = new JPanel();
    JButton OKButton = new JButton();
    JButton CancelButton = new JButton();
    JLabel label4 = new JLabel();
    JTextField widthField = new JTextField();
    JLabel label1 = new JLabel();
    JTextField heightField = new JTextField();
    ButtonGroup gridStyleGroup = new ButtonGroup();
    JRadioButton gridInvisibleRadio = new JRadioButton();
    JRadioButton gridDotsRadio = new JRadioButton();
    JRadioButton gridCrossesRadio = new JRadioButton();
    JRadioButton gridLinesRadio = new JRadioButton();
    JLabel label2 = new JLabel();
    JLabel label3 = new JLabel();
    JButton paperColorButton = new JButton();
    ButtonGroup moveSnapGroup = new ButtonGroup();
    JRadioButton moveNoSnapRadio = new JRadioButton();
    JRadioButton moveJumpRadio = new JRadioButton();
    JRadioButton moveAfterRadio = new JRadioButton();
    JLabel jLabel2 = new JLabel();
    ButtonGroup resizeSnapGroup = new ButtonGroup();
    JRadioButton resizeAfterRadio = new JRadioButton();
    JRadioButton resizeJumpRadio = new JRadioButton();
    JRadioButton resizeNoSnapRadio = new JRadioButton();
    JLabel jLabel3 = new JLabel();
    JLabel label5 = new JLabel();
    JLabel label6 = new JLabel();
    JLabel label7 = new JLabel();
    JTextField fontName = new JTextField();
    JTextField fontSize = new JTextField();

    Color myPaperColor;

    public GraphicViewerView myView = null;

    public GridOptionsDialog(Frame frame, String title, boolean modal,
            GraphicViewerView view) {
        super(frame, title, modal);
        try {
            jbInit();
            pack();
            myView = view;
            UpdateDialog();
        } catch (Exception ex) {
            ExceptionDialog.hideException(ex);
        }
    }

    public GridOptionsDialog() {
        this(null, "", false, null);
    }

    void jbInit() throws Exception {
        panel1.setLayout(null);
        panel1.setMinimumSize(new Dimension(586, 265));
        panel1.setPreferredSize(new Dimension(586, 265));
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
        paperColorButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                paperColorButton_actionPerformed(e);
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
        this.setResizable(false);
        getContentPane().add(panel1);

        OKButton.setText("OK");
        OKButton.setFont(new Font("Dialog", Font.PLAIN, 12));
        OKButton.setBounds(new Rectangle(185, 228, 79, 22));
        CancelButton.setText("Cancel");
        CancelButton.setFont(new Font("Dialog", Font.PLAIN, 12));
        CancelButton.setBounds(new Rectangle(293, 228, 79, 22));
        label4.setText("Width:");
        label4.setHorizontalAlignment(JLabel.RIGHT);
        panel1.add(label4);
        label4.setBounds(new Rectangle(30, 36, 48, 24));
        panel1.add(widthField);
        widthField.setBounds(new Rectangle(90, 36, 36, 24));
        label1.setText("Height:");
        label1.setHorizontalAlignment(JLabel.RIGHT);
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
        paperColorButton.setBackground(java.awt.Color.lightGray);
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
        if (myView == null) {
            return;
        }

        widthField.setText(String.valueOf(myView.getGridWidth()));
        heightField.setText(String.valueOf(myView.getGridHeight()));
        int style = myView.getGridStyle();
        if (style == GraphicViewerView.GridInvisible) {
            gridInvisibleRadio.setSelected(true);
        } else if (style == GraphicViewerView.GridDot) {
            gridDotsRadio.setSelected(true);
        } else if (style == GraphicViewerView.GridCross) {
            gridCrossesRadio.setSelected(true);
        } else if (style == GraphicViewerView.GridLine) {
            gridLinesRadio.setSelected(true);
        }
        int snapmove = myView.getSnapMove();
        if (snapmove == GraphicViewerView.NoSnap) {
            moveNoSnapRadio.setSelected(true);
        } else if (snapmove == GraphicViewerView.SnapJump) {
            moveJumpRadio.setSelected(true);
        } else if (snapmove == GraphicViewerView.SnapAfter) {
            moveAfterRadio.setSelected(true);
        }
        int snapresize = myView.getSnapResize();
        if (snapresize == GraphicViewerView.NoSnap) {
            resizeNoSnapRadio.setSelected(true);
        } else if (snapresize == GraphicViewerView.SnapJump) {
            resizeJumpRadio.setSelected(true);
        } else if (snapresize == GraphicViewerView.SnapAfter) {
            resizeAfterRadio.setSelected(true);
        }
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
        if (myView == null) {
            return;
        }

        myView.setGridWidth(Integer.parseInt(widthField.getText()));
        myView.setGridHeight(Integer.parseInt(heightField.getText()));
        int style = 0;
        if (gridInvisibleRadio.isSelected()) {
            style = GraphicViewerView.GridInvisible;
        } else if (gridDotsRadio.isSelected()) {
            style = GraphicViewerView.GridDot;
        } else if (gridCrossesRadio.isSelected()) {
            style = GraphicViewerView.GridCross;
        } else if (gridLinesRadio.isSelected()) {
            style = GraphicViewerView.GridLine;
        }
        myView.setGridStyle(style);
        int snapmove = 0;
        if (moveNoSnapRadio.isSelected()) {
            snapmove = GraphicViewerView.NoSnap;
        } else if (moveJumpRadio.isSelected()) {
            snapmove = GraphicViewerView.SnapJump;
        } else if (moveAfterRadio.isSelected()) {
            snapmove = GraphicViewerView.SnapAfter;
        }
        myView.setSnapMove(snapmove);
        int snapresize = 0;
        if (resizeNoSnapRadio.isSelected()) {
            snapresize = GraphicViewerView.NoSnap;
        } else if (resizeJumpRadio.isSelected()) {
            snapresize = GraphicViewerView.SnapJump;
        } else if (resizeAfterRadio.isSelected()) {
            snapresize = GraphicViewerView.SnapAfter;
        }
        myView.setSnapResize(snapresize);
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
    JCheckBox hideDisabledScrollBars = new JCheckBox();
    JCheckBox dragEnabled = new JCheckBox();
    JCheckBox dragsSelectionImage = new JCheckBox();
    JCheckBox dropEnabled = new JCheckBox();
    JCheckBox dragsRealtime = new JCheckBox();
    JCheckBox includeNegativeCoords = new JCheckBox();
    JCheckBox mouseEnabled = new JCheckBox();
    JCheckBox keyEnabled = new JCheckBox();

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

    void paperColorButton_actionPerformed(ActionEvent e) {
        Color newcolor = JColorChooser.showDialog(this, "Paper Color",
                myPaperColor);
        if (newcolor != null) {
            myPaperColor = newcolor;
        }
    }
}