/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DatabaseMetaData;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

import org.fife.rsta.ac.java.buildpath.JarLibraryInfo;
import org.fife.rsta.ac.java.buildpath.LibraryInfo;
import org.fife.rsta.ac.perl.PerlLanguageSupport;

import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * The "About" dialog for the application.
 *
 * @author D. Campione
 *
 */
public class AboutDialog extends JDialog implements MouseListener {

    private static final long serialVersionUID = 5497242522081970155L;

    private final Border empty5Border = BorderFactory.createEmptyBorder(5, 5, 5, 5);

    public AboutDialog(ApplicationFrame parent) {
        super(parent);

        JPanel cp = new JPanel(new BorderLayout());

        Box box = Box.createVerticalBox();

        // Don't use a Box, as some JVM's won't have the resulting component
        // honor its opaque property
        JPanel box2 = new JPanel();
        box2.setLayout(new BoxLayout(box2, BoxLayout.Y_AXIS));
        box2.setOpaque(true);
        box2.setBackground(Color.white);
        box2.setBorder(new TopBorder());

        JLabel label = new JLabel(Main.APPLICATION_NAME);
        label.setOpaque(true);
        label.setBackground(Color.white);
        Font labelFont = label.getFont();
        label.setFont(labelFont.deriveFont(Font.BOLD, 20));
        addLeftAligned(label, box2);
        box2.add(Box.createVerticalStrut(5));

        JTextArea textArea = new JTextArea(6, 60);
        // Windows LAF picks a bad font for text areas, for some reason
        textArea.setFont(labelFont);
        try {
            textArea.setText("Version " + Config.getVersion() + "\n\n"
                    + "A database administration tool, suitable as front-end for your Teradata "
                    + "relational database. Used to easily query, update and administer your "
                    + "database, create reports and synchronize data.\n\n"
                    + "Note that some features for some languages may not work unless your system "
                    + "is set up properly.\nFor example, Java code completion requries a JRE on "
                    + "your PATH and Perl completion requires the Perl executable to be on your " + "PATH.");
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
        }
        textArea.setEditable(false);
        textArea.setBackground(Color.white);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(null);
        box2.add(textArea);

        box.add(box2);
        box.add(Box.createVerticalStrut(5));

        SpringLayout sl = new SpringLayout();
        JPanel temp = new JPanel(sl);
        JLabel copyrightLabel = new JLabel(
                "<html><font style=\"color:gray\">Copyright &copy 2019 D. Campione</font></html>");
        JLabel licenseLabel = new JLabel("GNU General Public License");
        licenseLabel.setForeground(Color.BLUE);
        licenseLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        licenseLabel.addMouseListener(this);
        JLabel homePageLabel = new JLabel(Config.HOME_PAGE);
        homePageLabel.setForeground(Color.BLUE);
        homePageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        homePageLabel.addMouseListener(this);
        JLabel javaVMLabel = new JLabel("Java VM: ");
        JTextField javaVMField = createTextField(System.getProperty("java.version"));
        JLabel perlLabel = new JLabel("Perl install location:");
        File loc = PerlLanguageSupport.getDefaultPerlInstallLocation();
        String text = loc == null ? null : loc.getAbsolutePath();
        JTextField perlField = createTextField(text);
        JLabel javaLabel = new JLabel("Java home:");
        String jre = null;
        LibraryInfo info = LibraryInfo.getMainJreJarInfo();
        if (info != null) { // Should always be true
            File jarFile = ((JarLibraryInfo) info).getJarFile();
            jre = jarFile.getParentFile().getParentFile().getAbsolutePath();
        }
        JTextField javaField = createTextField(jre);

        if (getComponentOrientation().isLeftToRight()) {
            temp.add(copyrightLabel);
            temp.add(new JLabel());
            temp.add(licenseLabel);
            temp.add(new JLabel());
            temp.add(homePageLabel);
            temp.add(new JLabel());
            temp.add(javaVMLabel);
            temp.add(javaVMField);
            temp.add(perlLabel);
            temp.add(perlField);
            temp.add(javaLabel);
            temp.add(javaField);
        } else {
            temp.add(new JLabel());
            temp.add(copyrightLabel);
            temp.add(new JLabel());
            temp.add(licenseLabel);
            temp.add(new JLabel());
            temp.add(homePageLabel);
            temp.add(javaVMField);
            temp.add(javaVMLabel);
            temp.add(perlField);
            temp.add(perlLabel);
            temp.add(javaField);
            temp.add(javaLabel);
        }
        makeSpringCompactGrid(temp, 6, 2, 5, 5, 15, 5);
        box.add(temp);

        temp = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2);
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.SOUTHWEST;
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (isConnected) {
            try {
                DatabaseMetaData metaData = Context.getInstance().getConnectionData().getConnection().getMetaData();
                c.gridy++;
                temp.add(new JLabel("Database: "), c);
                temp.add(createTextField(metaData.getDatabaseProductName()), c);
                c.gridy++;
                temp.add(new JLabel(""), c);
                String databaseProductVersion = metaData.getDatabaseProductVersion().replaceAll("\n", "<br>");
                temp.add(new JLabel(String.format("<html>%s</html>", databaseProductVersion)), c);
                c.gridy++;
                temp.add(new JLabel("Driver: "), c);
                temp.add(createTextField(metaData.getDriverName()), c);
                c.gridy++;
                temp.add(new JLabel(""), c);
                temp.add(createTextField(metaData.getDriverVersion()), c);
            } catch (Throwable t) {
                ExceptionDialog.hideException(t);
            }
            box.add(temp);
        }

        box.add(Box.createVerticalGlue());

        cp.add(box, BorderLayout.NORTH);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }

        });
        temp = new JPanel(new BorderLayout());
        temp.setBorder(empty5Border);
        temp.add(okButton, BorderLayout.LINE_END);
        cp.add(temp, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(okButton);
        setTitle("About " + Main.APPLICATION_NAME);
        setContentPane(cp);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        pack();
    }

    private JPanel addLeftAligned(Component toAdd, Container addTo) {
        JPanel temp = new JPanel(new BorderLayout());
        temp.setOpaque(false); // For ones on white background
        temp.add(toAdd, BorderLayout.LINE_START);
        addTo.add(temp);
        return temp;
    }

    private JTextField createTextField(String text) {
        JTextField field = new JTextField(text);
        field.setEditable(false);
        field.setBorder(null);
        field.setOpaque(false);
        return field;
    }

    /**
     * Used by makeSpringCompactGrid. This is ripped off directly from
     * <code>SpringUtilities.java</code> in the Sun Java Tutorial.
     *
     * @param parent The container whose layout must be an instance of
     *        <code>SpringLayout</code>.
     * @return The spring constraints for the specified component contained
     *         in <code>parent</code>.
     */
    private static final SpringLayout.Constraints getConstraintsForCell(int row, int col, Container parent, int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }

    /**
     * This method is ripped off from <code>SpringUtilities.java</code> found on
     * Sun's Java Tutorial pages. It takes a component whose layout is
     * <code>SpringLayout</code> and organizes the components it contains into a
     * nice grid.
     * Aligns the first <code>rows</code> * <code>cols</code> components of
     * <code>parent</code> in a grid. Each component in a column is as wide as
     * the maximum preferred width of the components in that column; height is
     * similarly determined for each row. The parent is made just big enough to
     * fit them all.
     *
     * @param parent The container whose layout is <code>SpringLayout</code>.
     * @param rows The number of rows of components to make in the container.
     * @param cols The number of columns of components to make.
     * @param initialX The x-location to start the grid at.
     * @param initialY The y-location to start the grid at.
     * @param xPad The x-padding between cells.
     * @param yPad The y-padding between cells.
     */
    public static final void makeSpringCompactGrid(Container parent, int rows, int cols, int initialX, int initialY,
            int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout) parent.getLayout();
        } catch (ClassCastException cce) {
            System.err.println("The first argument to makeCompactGrid " + "must use SpringLayout.");
            return;
        }

        // Align all cells in each column and make them the same width
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width, getConstraintsForCell(r, c, parent, cols).getWidth());
            }
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints = getConstraintsForCell(r, c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        // Align all cells in each row and make them the same height
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height, getConstraintsForCell(r, c, parent, cols).getHeight());
            }
            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints = getConstraintsForCell(r, c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }

        // Set the parent's size
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    }

    /**
     * The border of the "top section" of the About dialog.
     *
     * @author D. Campione
     *
     */
    private static class TopBorder extends AbstractBorder {

        private static final long serialVersionUID = 5485691579345543789L;

        @Override
        public Insets getBorderInsets(Component c) {
            return getBorderInsets(c, new Insets(0, 0, 0, 0));
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.top = insets.left = insets.right = 5;
            insets.bottom = 6;
            return insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Color color = UIManager.getColor("controlShadow");
            if (color == null) {
                color = SystemColor.controlShadow;
            }
            g.setColor(color);
            g.drawLine(x, y + height - 1, x + width, y + height - 1);
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if (me.getSource() instanceof JLabel) {
            JLabel label = (JLabel) me.getSource();
            try {
                if (label.getText().startsWith("GNU")) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    InputStream in = Config.class.getResourceAsStream("/license.txt");
                    byte[] bytes = new byte[1024];
                    int length = in.read(bytes);
                    while (length != -1) {
                        out.write(bytes, 0, length);
                        length = in.read(bytes);
                    }
                    in.close();
                    JTextArea textArea = new JTextArea(new String(out.toByteArray()));
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    Dialog.show("License", scrollPane, Dialog.PLAIN_MESSAGE, Dialog.DEFAULT_OPTION);
                } else {
                    Utilities.openURLWithDefaultBrowser(label.getText());
                }
            } catch (Exception e) {
                ExceptionDialog.showException(e);
            }
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
}