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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.FileIO;
import net.sourceforge.open_teradata_viewer.UISupport;
import net.sourceforge.open_teradata_viewer.editor.sha1_sum_tools.SHA1SumCalculator;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class CalculateSHA1ChecksumOfAFileAction extends CustomAction {

    private static final long serialVersionUID = 8600398409228836371L;

    private final Insets insets = new Insets(0, 0, 0, 0);

    public CalculateSHA1ChecksumOfAFileAction() {
        super("Calculate the SHA1 Checksum of a file", null, null, "Calculate the SHA1 Checksum of the selected file.");
        setEnabled(true);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.open_teradata_viewer.actions.CustomAction#performThreaded(java.awt.event.ActionEvent)
     */
    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        ApplicationFrame app = ApplicationFrame.getInstance();
        File file = FileIO.chooseFile();
        if (file != null && file.exists()) {
            SHA1SumCalculator sha1SumCalculator = new SHA1SumCalculator();
            JPanel panel = new JPanel(new GridBagLayout());
            JLabel label = new JLabel("SHA1 sum: ");
            final JTextField textField = new JTextField(sha1SumCalculator.calculateSHA1ChecksumOfAFile(file));
            JLabel label2 = new JLabel("Compare: ");
            final JTextField textField2 = new JTextField();
            JButton compareButton = new JButton("Compare");
            textField.setEditable(false);
            JButton button = new JButton("OK");
            addComponent(panel, label, 0, 0, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
            addComponent(panel, textField, 0, 1, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
            addComponent(panel, label2, 0, 2, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
            addComponent(panel, textField2, 0, 3, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
            addComponent(panel, compareButton, 0, 4, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH);
            addComponent(panel, new JSeparator(), 0, 5, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
            addComponent(panel, button, 0, 6, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);

            Object[] options = new Object[] { button };
            final JOptionPane optionPane = new JOptionPane(panel, JOptionPane.INFORMATION_MESSAGE,
                    JOptionPane.OK_CANCEL_OPTION, null, options, options[0]);
            optionPane.setOptionType(JOptionPane.OK_OPTION);
            final JDialog dialog = optionPane.createDialog("SHA1 Sum");
            dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    optionPane.setValue(JOptionPane.OK_OPTION);
                    dialog.dispose();
                }
            });
            compareButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(app, "SHA1 Check Sums are "
                            + ((textField.getText().equals(textField2.getText())) ? "the same." : "different."));
                }
            });
            UISupport.showDialog(dialog);
        }
    }

    private void addComponent(Container container, Component component, int gridx, int gridy, int gridwidth,
            int gridheight, int anchor, int fill) {
        GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, 1.0, 1.0, anchor, fill,
                insets, 0, 0);
        container.add(component, gbc);
    }
}