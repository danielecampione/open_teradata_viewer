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

package net.sourceforge.open_teradata_viewer;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.sourceforge.open_teradata_viewer.actions.Actions;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class UpdateChecker implements Runnable {

    private JMenuBar menuBar;

    public UpdateChecker(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public void check() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            int result = JOptionPane.showConfirmDialog(
                    ApplicationFrame.getInstance(),
                    "Do you want to configure a Server proxy?",
                    "Update checker", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.CLOSED_OPTION
                    || result == JOptionPane.CANCEL_OPTION) {
                return;
            }

            System.setProperty("java.net.useSystemProxies", new Boolean(
                    result == JOptionPane.NO_OPTION).toString());

            if (result == JOptionPane.YES_OPTION) {
                String proxyHostKey = "proxy_host", proxyPortKey = "proxy_port";
                String proxyRememberConfigurationKey = "proxy_remember_configuration";

                final JTextField proxyHostField = new JTextField();
                final JTextField proxyPortField = new JTextField();
                String proxyHost = "", proxyPort = "";

                boolean proxyRememberConfiguration = Config
                        .getSetting(proxyRememberConfigurationKey).trim()
                        .equalsIgnoreCase("true");
                proxyHost = Config.getSetting(proxyHostKey);
                proxyPort = Config.getSetting(proxyPortKey);

                if (!proxyRememberConfiguration) {
                    proxyHostField.setText(proxyHost);
                    proxyPortField.setText(proxyPort);

                    JOptionPane proxyPane = new JOptionPane(new Object[] {
                            new JLabel("host"), proxyHostField,
                            new JLabel("port"), proxyPortField },
                            JOptionPane.QUESTION_MESSAGE,
                            JOptionPane.OK_CANCEL_OPTION);
                    JDialog dialog = proxyPane.createDialog("Server proxy");
                    UISupport.showDialog(dialog);

                    Integer objResult = (Integer) proxyPane.getValue();
                    result = JOptionPane.CANCEL_OPTION;
                    if (objResult != null) {
                        result = objResult;
                    }
                    dialog.dispose();

                    if (result == JOptionPane.OK_OPTION) {
                        proxyHost = proxyHostField.getText().trim();
                        proxyPort = proxyPortField.getText().trim();

                        Config.saveSetting(proxyHostKey, proxyHost);
                        Config.saveSetting(proxyPortKey, proxyPort);
                        Config.saveSetting(proxyRememberConfigurationKey,
                                new Boolean(proxyRememberConfiguration)
                                        .toString());
                    } else {
                        return;
                    }
                }

                System.setProperty("proxyHost", proxyHost);
                System.setProperty("proxyPort", proxyPort);

                HTTPAuthProxy httpAuthProxy = new HTTPAuthProxy(
                        proxyRememberConfiguration);
                if (httpAuthProxy.isAuthenticationNecessary()) {
                    Authenticator.setDefault(httpAuthProxy);
                }
            }

            DateFormat format = new SimpleDateFormat("(dd/MM/yyyy)");
            String localVersion = Config.getVersion();
            String latestVersion = null;
            try {
                latestVersion = new BufferedReader(new InputStreamReader(
                        new URL(Config.HOME_PAGE + "changes.txt").openStream()))
                        .readLine();
            } catch (ProtocolException pe) { // The exception is caught if the Server has redirected too many times
                ExceptionDialog.ignoreException(pe);
            } catch (IOException ioe) { // The authentication is required if the HTTP status is 407
                ApplicationFrame
                        .getInstance()
                        .getConsole()
                        .println(ioe.getMessage(),
                                ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            } finally {
                if (latestVersion == null) {
                    ApplicationFrame
                            .getInstance()
                            .getConsole()
                            .println(
                                    "Unable to determine the latest version of the client by querying the official repository.\n"
                                            + "Please visit the following URL to be sure you're using the latest version of the client:\n"
                                            + Config.SOURCEFORGE_MIRROR,
                                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                    return;
                }
            }
            int localVersionBracketIndex = localVersion.indexOf('('), latestVersionBracketIndex = latestVersion
                    .indexOf('(');
            if (localVersionBracketIndex != -1
                    && latestVersionBracketIndex != -1) {
                Date localVersionDate = format.parse(localVersion
                        .substring(localVersionBracketIndex));
                Date latestVersionDate = format.parse(latestVersion
                        .substring(latestVersionBracketIndex));
                if (localVersionDate.compareTo(latestVersionDate) < 0) {
                    JMenu menu;
                    menuBar.add(Box.createHorizontalGlue());
                    menu = new JMenu("Update");
                    menuBar.add(menu);
                    menu.setForeground(Color.RED);
                    menu.add(Actions.UPDATE);
                }
            }
        } catch (Throwable t) {
            ExceptionDialog.hideException(t);
        }
    }
}