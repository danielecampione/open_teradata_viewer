/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class HTTPAuthProxy extends Authenticator {

    private boolean authenticationNecessary;
    private String proxyUser;
    private String proxyPassword;
    private final Insets insets = new Insets(0, 0, 0, 0);

    public HTTPAuthProxy(boolean proxyRememberConfiguration) throws Throwable {
        String proxyUserKey = "proxy_user", proxyPasswordKey = "proxy_password";

        if (!proxyRememberConfiguration) {
            String proxyRememberConfigurationKey = "proxy_remember_configuration";

            authenticationNecessary = true;
            final JCheckBox isAuthenticationRequired = new JCheckBox(
                    "Authentication required"), proxyRememberConfigurationField = new JCheckBox(
                    "Don't ask again");

            isAuthenticationRequired.setSelected(authenticationNecessary);
            proxyRememberConfigurationField
                    .setSelected(proxyRememberConfiguration);

            final JTextField proxyUserField = new JTextField();
            proxyUserField.setText(Config.getSetting(proxyUserKey));
            proxyUserField.setEnabled(isAuthenticationRequired.isSelected());

            final JPasswordField proxyPasswordField = new JPasswordField();
            proxyPasswordField.setText(Config.decrypt(Config
                    .getSetting(proxyPasswordKey)));
            proxyPasswordField
                    .setEnabled(isAuthenticationRequired.isSelected());

            proxyUserField
                    .addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent arg0) {
                            authenticationNecessary = isAuthenticationRequired
                                    .isSelected();
                        }
                    });
            proxyPasswordField
                    .addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent e) {
                            authenticationNecessary = isAuthenticationRequired
                                    .isSelected();
                        }
                    });
            isAuthenticationRequired.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    proxyUserField.setEnabled(!proxyUserField.isEnabled());
                    proxyPasswordField.setEnabled(!proxyPasswordField
                            .isEnabled());
                }
            });

            JPanel panel = new JPanel(new GridBagLayout());
            addComponent(panel, isAuthenticationRequired, 0, 0, 2, 1,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH);
            addComponent(panel, new JSeparator(), 0, 1, 2, 1,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH);
            addComponent(panel, new JLabel("proxy user"), 0, 2, 1, 1,
                    GridBagConstraints.WEST, GridBagConstraints.BOTH);
            addComponent(panel, proxyUserField, 1, 2, 1, 1,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH);
            addComponent(panel, new JLabel("proxy password"), 0, 3, 1, 1,
                    GridBagConstraints.WEST, GridBagConstraints.BOTH);
            addComponent(panel, proxyPasswordField, 1, 3, 1, 1,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH);
            addComponent(panel, new JSeparator(), 0, 4, 2, 1,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH);
            addComponent(panel, proxyRememberConfigurationField, 0, 5, 2, 1,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH);

            JButton button = new JButton("OK");
            Object[] options = new Object[] { button };
            final JOptionPane optionPane = new JOptionPane(panel,
                    JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION,
                    null, options, options[0]);
            optionPane.setOptionType(JOptionPane.OK_OPTION);
            final JDialog dialog = optionPane.createDialog("System proxy");
            dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    optionPane.setValue(JOptionPane.OK_OPTION);
                    dialog.dispose();
                }
            });
            UISupport.showDialog(dialog);

            Integer objResult = (Integer) optionPane.getValue();
            int result = JOptionPane.CANCEL_OPTION;
            if (objResult != null) {
                result = objResult;
            }

            if (result == JOptionPane.OK_OPTION) {
                proxyUser = proxyUserField.getText();
                proxyPassword = new String(proxyPasswordField.getPassword());
                proxyRememberConfiguration = proxyRememberConfigurationField
                        .isSelected();
                Config.saveSetting(proxyUserKey, proxyUser);
                Config.saveSetting(proxyPasswordKey,
                        Config.encrypt(proxyPassword));
                Config.saveSetting(proxyRememberConfigurationKey, new Boolean(
                        proxyRememberConfiguration).toString());
            }
        } else {
            proxyUser = Config.getSetting(proxyUserKey);
            proxyPassword = Config.decrypt(Config.getSetting(proxyPasswordKey));
            authenticationNecessary = (proxyUser.length() > 0);
        }
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return (proxyPassword == null ? null : new PasswordAuthentication(
                proxyUser, proxyPassword.toCharArray()));
    }

    protected PasswordAuthentication setAuthentication() {
        return authenticationNecessary ? new PasswordAuthentication(proxyUser,
                proxyPassword.toCharArray()) : null;
    }

    public boolean isAuthenticationNecessary() {
        return authenticationNecessary;
    }

    public void setAuthenticationNecessary(boolean authenticationNecessary) {
        this.authenticationNecessary = authenticationNecessary;
    }

    private void addComponent(Container container, Component component,
            int gridx, int gridy, int gridwidth, int gridheight, int anchor,
            int fill) {
        GridBagConstraints gbc = new GridBagConstraints(gridx, gridy,
                gridwidth, gridheight, 1.0, 1.0, anchor, fill, insets, 0, 0);
        container.add(component, gbc);
    }
}