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
import java.awt.Dimension;
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

import net.sourceforge.open_teradata_viewer.util.SwingUtil;
import net.sourceforge.open_teradata_viewer.util.Utilities;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 * The "About" dialog for the application.
 *
 * @author D. Campione
 *
 */
public class AboutDialog extends JDialog implements MouseListener {

    private static final long serialVersionUID = 5497242522081970155L;

    private final Border empty5Border = BorderFactory.createEmptyBorder(
            SwingUtil.scale(5), SwingUtil.scale(5), SwingUtil.scale(5), SwingUtil.scale(5));

    public AboutDialog(ApplicationFrame parent) {
        super(parent);
        initializeDialog();
    }
    
    /** Initializes the dialog components and layout. */
    private void initializeDialog() {
        LanguageManager langManager = LanguageManager.getInstance();
        
        setTitle(langManager.getString("dialog.about") + " " + Main.APPLICATION_NAME);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        
        JPanel contentPanel = createContentPanel(langManager);
        setContentPane(contentPanel);
        
        pack();
        
        setMinimumSize(new Dimension(Math.max(getWidth() + 50, 500), 
                                             Math.max(getHeight() + 30, 480)));
    }
    
    /** Creates the main content panel for the dialog. */
    private JPanel createContentPanel(LanguageManager langManager) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        
        Box mainBox = Box.createVerticalBox();
        
        // Add header section
        mainBox.add(createHeaderSection(langManager));
        mainBox.add(Box.createVerticalStrut(5));
        
        // Add system information section
        mainBox.add(createSystemInfoSection(langManager));
        
        // Add database information section if connected
        JPanel dbInfoPanel = createDatabaseInfoSection();
        if (dbInfoPanel != null) {
            mainBox.add(dbInfoPanel);
        }
        
        mainBox.add(Box.createVerticalGlue());
        
        contentPanel.add(mainBox, BorderLayout.NORTH);
        contentPanel.add(createButtonPanel(langManager), BorderLayout.SOUTH);
        
        return contentPanel;
    }
    
    /** Creates the header section with application name and description. */
    private JPanel createHeaderSection(LanguageManager langManager) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(true);
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new TopBorder());

        // Application name label
        JLabel nameLabel = new JLabel(Main.APPLICATION_NAME);
        nameLabel.setOpaque(true);
        nameLabel.setBackground(Color.WHITE);
        Font labelFont = nameLabel.getFont();
        nameLabel.setFont(labelFont.deriveFont(Font.BOLD, 20));
        addLeftAligned(nameLabel, headerPanel);
        headerPanel.add(Box.createVerticalStrut(5));

        // Description text area
        JTextArea descriptionArea = createDescriptionArea(langManager, labelFont);
        headerPanel.add(descriptionArea);
        
        return headerPanel;
    }
    
    /** Creates the description text area. */
    private JTextArea createDescriptionArea(LanguageManager langManager, Font baseFont) {
        JTextArea textArea = new JTextArea(6, 60);
        textArea.setFont(baseFont);
        
        try {
            textArea.setText(String.format(langManager.getString("about.description"), 
                    Config.getVersion()));
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
        }
        
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(null);
        
        return textArea;
    }
    
    /** Creates the system information section. */
    private JPanel createSystemInfoSection(LanguageManager langManager) {
        SpringLayout springLayout = new SpringLayout();
        JPanel systemPanel = new JPanel(springLayout);
        
        // Create labels and fields
        JLabel copyrightLabel = new JLabel(String.format(
                "<html><font style=\"color:gray\">%s</font></html>", 
                langManager.getString("about.copyright")));
        
        JLabel licenseLabel = createClickableLabel(langManager.getString("about.license"));
        licenseLabel.setName("license");
        JLabel homePageLabel = createClickableLabel(Config.HOME_PAGE);
        
        JLabel javaVMLabel = new JLabel(langManager.getString("about.java_vm"));
        JTextField javaVMField = createTextField(System.getProperty("java.version"));
        
        JLabel perlLabel = new JLabel(langManager.getString("about.perl_location"));
        File perlLocation = PerlLanguageSupport.getDefaultPerlInstallLocation();
        String perlPath = perlLocation == null ? null : perlLocation.getAbsolutePath();
        JTextField perlField = createTextField(perlPath);
        
        JLabel javaLabel = new JLabel(langManager.getString("about.java_home"));
        JTextField javaField = createTextField(getJavaHomePath());

        // Add components based on orientation
        addSystemInfoComponents(systemPanel, copyrightLabel, licenseLabel, homePageLabel,
                javaVMLabel, javaVMField, perlLabel, perlField, javaLabel, javaField);
        
        makeSpringCompactGrid(systemPanel, 6, 2, 5, 5, 15, 5);
        
        return systemPanel;
    }
    
    /** Creates a clickable label with hand cursor. */
    private JLabel createClickableLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.BLUE);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(this);
        return label;
    }
    
    /** Gets the Java home path from library info. */
    private String getJavaHomePath() {
        LibraryInfo info = LibraryInfo.getMainJreJarInfo();
        if (info != null) {
            File jarFile = ((JarLibraryInfo) info).getJarFile();
            return jarFile.getParentFile().getParentFile().getAbsolutePath();
        }
        return null;
    }
    
    /** Adds system information components to the panel based on orientation. */
    private void addSystemInfoComponents(JPanel panel, JLabel copyrightLabel, JLabel licenseLabel,
            JLabel homePageLabel, JLabel javaVMLabel, JTextField javaVMField, JLabel perlLabel,
            JTextField perlField, JLabel javaLabel, JTextField javaField) {
        
        if (getComponentOrientation().isLeftToRight()) {
            panel.add(copyrightLabel);
            panel.add(new JLabel());
            panel.add(licenseLabel);
            panel.add(new JLabel());
            panel.add(homePageLabel);
            panel.add(new JLabel());
            panel.add(javaVMLabel);
            panel.add(javaVMField);
            panel.add(perlLabel);
            panel.add(perlField);
            panel.add(javaLabel);
            panel.add(javaField);
        } else {
            panel.add(new JLabel());
            panel.add(copyrightLabel);
            panel.add(new JLabel());
            panel.add(licenseLabel);
            panel.add(new JLabel());
            panel.add(homePageLabel);
            panel.add(javaVMField);
            panel.add(javaVMLabel);
            panel.add(perlField);
            panel.add(perlLabel);
            panel.add(javaField);
            panel.add(javaLabel);
        }
    }
    
    /** Creates the database information section if connected. */
    private JPanel createDatabaseInfoSection() {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (!isConnected) {
            return null;
        }
        
        JPanel dbPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(2, 2, 2, 2);
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.SOUTHWEST;
        
        try {
            DatabaseMetaData metaData = Context.getInstance().getConnectionData()
                    .getConnection().getMetaData();
            
            addDatabaseInfo(dbPanel, constraints, metaData);
        } catch (Throwable t) {
            ExceptionDialog.hideException(t);
        }
        
        return dbPanel;
    }
    
    /** Adds database information to the panel. */
    private void addDatabaseInfo(JPanel panel, GridBagConstraints constraints, 
            DatabaseMetaData metaData) throws Exception {
        
        LanguageManager langManager = LanguageManager.getInstance();
        
        constraints.gridy++;
        panel.add(new JLabel(langManager.getString("label.database") + ": "), constraints);
        panel.add(createTextField(metaData.getDatabaseProductName()), constraints);
        
        constraints.gridy++;
        panel.add(new JLabel(""), constraints);
        String databaseVersion = metaData.getDatabaseProductVersion().replaceAll("\n", "<br>");
        panel.add(new JLabel(String.format("<html>%s</html>", databaseVersion)), constraints);
        
        constraints.gridy++;
        panel.add(new JLabel(langManager.getString("label.driver") + ": "), constraints);
        panel.add(createTextField(metaData.getDriverName()), constraints);
        
        constraints.gridy++;
        panel.add(new JLabel(""), constraints);
        panel.add(createTextField(metaData.getDriverVersion()), constraints);
    }
    
    /** Creates the button panel with OK button. */
    private JPanel createButtonPanel(LanguageManager langManager) {
        JButton okButton = new JButton(langManager.getString("button.ok"));
        okButton.addActionListener(e -> {
            setVisible(false);
            dispose();
        });
        
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(empty5Border);
        buttonPanel.add(okButton, BorderLayout.LINE_END);
        
        getRootPane().setDefaultButton(okButton);
        
        return buttonPanel;
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
                if ("license".equals(label.getName())) {
                    showLicenseDialog();
                } else {
                    Utilities.openURLWithDefaultBrowser(label.getText());
                }
            } catch (IOException e) {
                ExceptionDialog.showException(e);
            }
        }
    }
    
    /** Shows the license dialog with proper resource management. */
    private void showLicenseDialog() throws IOException {
        try (InputStream in = Config.class.getResourceAsStream("/license.txt");
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            if (in == null) {
                throw new IOException("License file not found");
            }
            
            byte[] buffer = new byte[8192]; // Increased buffer size for better performance
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            
            JTextArea textArea = new JTextArea(new String(out.toByteArray(), "UTF-8"));
            textArea.setEditable(false);
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));
            
            Dialog.show(LanguageManager.getInstance().getString("dialog.license"), 
                       scrollPane, Dialog.PLAIN_MESSAGE, Dialog.DEFAULT_OPTION);
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