/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.DatabaseMetaData;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Config;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.Dialog;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.ImageManager;
import net.sourceforge.open_teradata_viewer.Main;
import net.sourceforge.open_teradata_viewer.UISupport;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class AboutAction extends CustomAction implements MouseListener {

    private static final long serialVersionUID = -4235652606704763545L;

    protected AboutAction() {
        super("About..");
        setEnabled(true);
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if (me.getSource() instanceof JLabel) {
            JLabel label = (JLabel) me.getSource();
            try {
                if (label.getText().startsWith("GNU")) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    InputStream in = Config.class
                            .getResourceAsStream("/license.txt");
                    byte[] bytes = new byte[1024];
                    int length = in.read(bytes);
                    while (length != -1) {
                        out.write(bytes, 0, length);
                        length = in.read(bytes);
                    }
                    in.close();
                    JTextArea textArea = new JTextArea(new String(
                            out.toByteArray()));
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    Dialog.show("License", scrollPane, Dialog.PLAIN_MESSAGE,
                            Dialog.DEFAULT_OPTION);
                } else {
                    openURL(label.getText());
                }
            } catch (Exception e) {
                ExceptionDialog.showException(e);
            }
        } else {
            ((JDialog) me.getSource()).setVisible(false);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void openURL(String uri) throws Exception {
        Desktop.getDesktop().browse(new URI(uri));
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        JPanel panel = new JPanel(new GridBagLayout()) {
            private static final long serialVersionUID = 5516027936581070147L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image background = ImageManager.getImage("/icons/logo.png")
                        .getImage();
                g.drawImage(background, 5, 17, null);
                g.setColor(getBackground());
                g.fillRect(getWidth() - 5, 0, 5,
                        background.getHeight(null) + 17);
            }
        };
        panel.setPreferredSize(new Dimension(400, 350));
        panel.setBackground(new Color(226, 226, 226));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2);
        c.gridy++;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.SOUTHWEST;
        try {
            panel.add(
                    new JLabel(String.format(
                            "<html><font style=\"font-weight:bold\">"
                                    + Main.APPLICATION_NAME
                                    + " %s</font></html>", Config.getVersion())),
                    c);
        } catch (IOException ioe) {
            throw ioe;
        }
        c.gridy++;
        panel.add(
                new JLabel(
                        "<html><font style=\"color:gray\">Copyright &copy 2014 D. Campione</font></html>"),
                c);
        c.gridy++;
        JLabel link = new JLabel("GNU General Public License");
        link.setForeground(Color.BLUE);
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        link.addMouseListener(this);
        panel.add(link, c);
        c.gridy++;
        link = new JLabel(Config.HOME_PAGE);
        link.setForeground(Color.BLUE);
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        link.addMouseListener(this);
        panel.add(link, c);
        c.gridy++;
        c.gridwidth = 1;
        panel.add(new JLabel("Java VM: "), c);
        panel.add(new JLabel(System.getProperty("java.version")), c);
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (isConnected) {
            try {
                DatabaseMetaData metaData = Context.getInstance()
                        .getConnectionData().getConnection().getMetaData();
                c.gridy++;
                panel.add(new JLabel("Database: "), c);
                panel.add(new JLabel(metaData.getDatabaseProductName()), c);
                c.gridy++;
                panel.add(new JLabel(""), c);
                String databaseProductVersion = metaData
                        .getDatabaseProductVersion().replaceAll("\n", "<br>");
                panel.add(
                        new JLabel(String.format("<html>%s</html>",
                                databaseProductVersion)), c);
                c.gridy++;
                panel.add(new JLabel("Driver: "), c);
                panel.add(new JLabel(metaData.getDriverName()), c);
                c.gridy++;
                panel.add(new JLabel(""), c);
                panel.add(new JLabel(metaData.getDriverVersion()), c);
            } catch (Throwable t) {
                ExceptionDialog.hideException(t);
            }
        }
        panel.setBorder(new BevelBorder(BevelBorder.RAISED) {

            private static final long serialVersionUID = 5762741907494684733L;

            private Insets insets = new Insets(65, 10, 10, 10);

            @Override
            public Insets getBorderInsets(Component c) {
                return insets;
            }
        });
        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(ApplicationFrame
                        .getInstance()), true);
        dialog.setUndecorated(true);
        dialog.addMouseListener(this);
        dialog.getContentPane().add(panel);
        dialog.setSize(panel.getPreferredSize());
        dialog.setMinimumSize(dialog.getSize());
        dialog.setLocationRelativeTo(dialog.getOwner());
        UISupport.showDialog(dialog);
    }
}