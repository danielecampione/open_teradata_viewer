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
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
import javax.swing.SwingUtilities;
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

    /**
     * Reference to the OK button's panel, kept around so
     * {@link #reconcileContentHeight()} can measure its real, laid-out
     * position after the dialog is actually shown - see that method for
     * why this second pass exists.
     */
    private JPanel buttonPanel;

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
        
        packToFinalSize();
        scheduleContentHeightReconciliation();
    }
    
    /**
     * Sizes the dialog to fit every section that was actually added
     * (system info, and the database info section too, when connected),
     * OK button included, then locks that as the minimum size.
     * <p>
     * A single {@link #pack()} is not always enough: on some platform /
     * font combinations, a child component can still report a stale
     * preferred size right after the first layout pass - before the
     * dialog has a live native peer - which then undersizes the window
     * and clips the last row of the database info section until the
     * user manually resizes. Explicitly validating and packing a second
     * time forces every child to re-measure against its now-realized
     * state, so the size read back afterwards is always the real, final
     * one - no arbitrary extra padding needed to compensate.
     * <p>
     * Any slack introduced by the {@code 500x480} minimum-size floor
     * below lands after the OK button (see {@link #createContentPanel},
     * where the button is part of the same vertical flow as the content
     * above it), never between the content and the button.
     */
    private void packToFinalSize() {
        pack();
        validate();
        pack();
        
        Dimension packedSize = getSize();
        setMinimumSize(new Dimension(Math.max(packedSize.width, 500),
                                             Math.max(packedSize.height, 480)));
    }
    
    /**
     * Registers a one-shot correction that runs right after the dialog
     * is actually shown on screen, to guarantee the OK button - and
     * therefore every row above it - always ends up fully visible.
     * <p>
     * {@link #packToFinalSize()} already does the right thing with the
     * information available <em>before</em> the dialog is shown, but on
     * some platform / look-and-feel combinations a component's
     * {@code getPreferredSize()} only settles to its real value after
     * the component has actually been painted at least once - which
     * cannot happen before {@link #setVisible(boolean)} is called, no
     * matter how many times {@code pack()} is called beforehand. Rather
     * than continuing to guess at preferred sizes, this schedules
     * {@link #reconcileContentHeight()} to run once the dialog is
     * showing, where it measures the OK button's real, laid-out
     * position and grows the window if needed - so the guarantee holds
     * regardless of which look-and-feel is active.
     */
    private void scheduleContentHeightReconciliation() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                removeComponentListener(this);
                // Two nested invokeLater calls, not one: this lets any
                // paint/layout events already queued as part of the
                // dialog becoming visible run first, so the components
                // we are about to measure have settled.
                SwingUtilities.invokeLater(() -> SwingUtilities
                        .invokeLater(AboutDialog.this::reconcileContentHeight));
            }
        });
    }
    
    /**
     * Measures the real, laid-out bottom edge of {@link #buttonPanel}
     * and grows the dialog if that edge does not comfortably fit within
     * the current window - guaranteeing the OK button always sits below
     * the last filled row, with breathing room, regardless of what any
     * individual component's preferred size claimed before showing.
     * This only ever grows the window, never shrinks it.
     * <p>
     * Checking {@code buttonPanel} alone is enough: it is the last
     * non-glue child in the {@code BoxLayout.Y_AXIS} box built by
     * {@link #createContentPanel}, and a Y_AXIS box always stacks its
     * children in sequence with no overlap, so nothing else in that box
     * can end up lower on screen than it does.
     */
    private void reconcileContentHeight() {
        if (buttonPanel == null) {
            return;
        }
        
        validate();
        
        final int breathingRoom = SwingUtil.scale(15);
        Point buttonPanelBottom = SwingUtilities.convertPoint(buttonPanel.getParent(),
                new Point(0, buttonPanel.getY() + buttonPanel.getHeight()), getContentPane());
        int available = getContentPane().getHeight();
        int shortfall = (buttonPanelBottom.y + breathingRoom) - available;
        
        if (shortfall > 0) {
            setSize(getWidth(), getHeight() + shortfall);
            validate();
        }
    }
    
    /** Creates the main content panel for the dialog. */
    private JPanel createContentPanel(LanguageManager langManager) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        
        Box mainBox = Box.createVerticalBox();
        
        // Add header section
        mainBox.add(createHeaderSection(langManager));
        mainBox.add(Box.createVerticalStrut(5));
        
        // Add system information section
        int[] labelColumnWidth = new int[1];
        mainBox.add(createSystemInfoSection(langManager, labelColumnWidth));
        
        // Add database information section if connected
        JPanel dbInfoPanel = createDatabaseInfoSection(labelColumnWidth[0]);
        if (dbInfoPanel != null) {
            addLeftAligned(dbInfoPanel, mainBox);
        }
        
        // The OK button lives in the same vertical flow as the content
        // above it (as the very last element), rather than being pinned
        // to BorderLayout.SOUTH of a separate NORTH/SOUTH split. With a
        // NORTH/SOUTH split, any extra height the window ends up with
        // (e.g. from the minimum-size floor in packToFinalSize()) is
        // inserted as dead space BETWEEN the content and the button,
        // since BorderLayout hands unclaimed height to a CENTER region
        // that does not exist here - which is what used to push the OK
        // button away from the last row. Putting the button inside the
        // Box instead means any such slack lands after it, never before
        // it: the button is always immediately below the last filled
        // row, by construction.
        mainBox.add(Box.createVerticalStrut(10));
        JPanel buttonPanel = createButtonPanel(langManager);
        this.buttonPanel = buttonPanel;
        // Cap the height so BoxLayout never stretches this panel (and
        // therefore the button inside it) to absorb any leftover slack
        // from the minimum-size floor in packToFinalSize() - that slack
        // must land below the button (see the trailing glue right
        // after), never grow the button itself. Width stays flexible so
        // the button panel still spans the full dialog width and the
        // button stays right-aligned.
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, buttonPanel.getPreferredSize().height));
        mainBox.add(buttonPanel);
        mainBox.add(Box.createVerticalGlue());
        
        contentPanel.add(mainBox, BorderLayout.CENTER);
        
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
    private JPanel createSystemInfoSection(LanguageManager langManager, int[] labelColumnWidthOut) {
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
        
        // "javaVMLabel" sits in the label column (column 0), which
        // makeSpringCompactGrid has just sized to fit the widest component
        // sharing that column (here, the home page URL) - reading its
        // resolved width back lets other sections (e.g. the database info
        // panel) line their own value column up with this one
        labelColumnWidthOut[0] = springLayout.getConstraints(javaVMLabel).getWidth().getValue();
        
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
    private JPanel createDatabaseInfoSection(int labelColumnWidth) {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (!isConnected) {
            return null;
        }
        
        JPanel dbPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(2, 2, 2, 2);
        constraints.anchor = GridBagConstraints.WEST;
        
        try {
            DatabaseMetaData metaData = Context.getInstance().getConnectionData()
                    .getConnection().getMetaData();
            
            addDatabaseInfo(dbPanel, constraints, metaData, labelColumnWidth);
        } catch (Throwable t) {
            ExceptionDialog.hideException(t);
        }
        
        return dbPanel;
    }
    
    /** Adds database information to the panel. */
    private void addDatabaseInfo(JPanel panel, GridBagConstraints constraints, 
            DatabaseMetaData metaData, int labelColumnWidth) throws Exception {
        
        LanguageManager langManager = LanguageManager.getInstance();
        
        JLabel databaseLabel = new JLabel(langManager.getString("label.database") + ": ");
        padToWidth(databaseLabel, labelColumnWidth);
        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(databaseLabel, constraints);
        constraints.gridx = 1;
        panel.add(createTextField(metaData.getDatabaseProductName()), constraints);
        
        addMultilineValue(panel, constraints, metaData.getDatabaseProductVersion());
        
        JLabel driverLabel = new JLabel(langManager.getString("label.driver") + ": ");
        padToWidth(driverLabel, labelColumnWidth);
        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(driverLabel, constraints);
        constraints.gridx = 1;
        panel.add(createTextField(metaData.getDriverName()), constraints);
        
        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(new JLabel(""), constraints);
        constraints.gridx = 1;
        panel.add(createTextField(metaData.getDriverVersion()), constraints);
    }
    
    /**
     * Adds a (possibly multi-line) value under the current label
     * column, one plain-text row per line, advancing {@code gridy} as
     * needed. Deliberately avoids a single HTML-wrapped JLabel: Swing's
     * HTML text view can misreport its preferred size the first time
     * it is laid out - before the component has a live Graphics
     * context - which on some platform/JDK/font combinations makes the
     * enclosing dialog undersize itself. Plain JLabels do not have this
     * problem, so pack() always measures the real height.
     */
    private void addMultilineValue(JPanel panel, GridBagConstraints constraints, String value) {
        String[] lines = (value == null || value.isEmpty()) ? new String[] { "" } : value.split("\n");
        for (String line : lines) {
            constraints.gridx = 0;
            constraints.gridy++;
            panel.add(new JLabel(""), constraints);
            constraints.gridx = 1;
            panel.add(new JLabel(line), constraints);
        }
    }
    
    /**
     * Widens a label's preferred size to at least the given width, without
     * ever shrinking it below its own natural width. Used to line up the
     * database info panel's value column with the system info section's
     * value column above it, even though the two panels use different
     * layout managers and are otherwise sized independently.
     */
    private void padToWidth(JLabel label, int width) {
        Dimension natural = label.getPreferredSize();
        if (width > natural.width) {
            label.setPreferredSize(new Dimension(width, natural.height));
        }
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