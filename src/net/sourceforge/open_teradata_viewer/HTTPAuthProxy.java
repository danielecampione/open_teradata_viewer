/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2012, D. Campione
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
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

    public HTTPAuthProxy() {
        authenticationNecessary = true;
        final JCheckBox isAuthenticationRequired = new JCheckBox(
                "Authentication required");
        String proxyUserKey = "proxy_user", proxyPasswordKey = "proxy_password";

        isAuthenticationRequired.setSelected(authenticationNecessary);

        final JTextField proxyUserField = new JTextField();
        try {
            proxyUserField.setText(Config.getSetting(proxyUserKey));
        } catch (Exception e) {
            // ignore.
        }
        proxyUserField.setEnabled(isAuthenticationRequired.isSelected());

        final JPasswordField proxyPasswordField = new JPasswordField();
        try {
            proxyPasswordField.setText(Config.decrypt(Config
                    .getSetting(proxyPasswordKey)));
        } catch (Exception e) {
            // ignore.
        }
        proxyPasswordField.setEnabled(isAuthenticationRequired.isSelected());

        proxyUserField.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent arg0) {
                authenticationNecessary = isAuthenticationRequired.isSelected();
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
                proxyPasswordField.setEnabled(!proxyPasswordField.isEnabled());
            }
        });
        JOptionPane passwordPane = new JOptionPane(new Object[]{
                isAuthenticationRequired, new JSeparator(),
                new JLabel("proxy user"), proxyUserField,
                new JLabel("proxy password"), proxyPasswordField},
                JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = passwordPane.createDialog("Input");
        dialog.setVisible(true);
        Integer objResult = (Integer) passwordPane.getValue();
        int result = JOptionPane.CANCEL_OPTION;
        if (objResult != null) {
            result = objResult;
        }
        dialog.dispose();
        if (result == JOptionPane.OK_OPTION) {
            proxyUser = proxyUserField.getText();
            proxyPassword = new String(proxyPasswordField.getPassword());
            try {
                Config.saveSetting(proxyUserKey, proxyUser);
                Config.saveSetting(proxyPasswordKey,
                        Config.encrypt(proxyPassword));
            } catch (Exception e) {
                // ignore.
            }
        }
    }
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
}
