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

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.sourceforge.open_teradata_viewer.actions.Actions;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

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
            LanguageManager langManager = LanguageManager.getInstance();
            
            // Show dialog on EDT
            final java.util.concurrent.atomic.AtomicInteger resultRef = new java.util.concurrent.atomic.AtomicInteger();
            
            try {
                javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        int result = JOptionPane.showConfirmDialog(
                                ApplicationFrame.getInstance(),
                                langManager.getString("update.configure_proxy"),
                                langManager.getString("update.checker_title"), 
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE);
                        resultRef.set(result);
                    }
                });
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            
            int result = resultRef.get();
                    
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

                String rawSetting = Config.getSetting(proxyRememberConfigurationKey);
                boolean proxyRememberConfiguration = rawSetting != null
                        && rawSetting.trim().equalsIgnoreCase("true");
                proxyHost = Config.getSetting(proxyHostKey);
                proxyPort = Config.getSetting(proxyPortKey);

                if (!proxyRememberConfiguration) {
                    proxyHostField.setText(proxyHost);
                    proxyPortField.setText(proxyPort);

                    // Create proxy dialog on EDT
                    final AtomicReference<JOptionPane> proxyPaneRef = new AtomicReference<>();
                    final AtomicReference<JDialog> dialogRef = new AtomicReference<>();
                    
                    try {
                        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                JOptionPane proxyPane = new JOptionPane(new Object[] {
                                        new JLabel(langManager.getString("update.host")), proxyHostField,
                                        new JLabel(langManager.getString("update.port")), proxyPortField },
                                        JOptionPane.QUESTION_MESSAGE,
                                        JOptionPane.OK_CANCEL_OPTION);
                                JDialog dialog = proxyPane.createDialog(langManager.getString("update.server_proxy_title"));
                                
                                proxyPaneRef.set(proxyPane);
                                dialogRef.set(dialog);
                            }
                        });
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    
                    JOptionPane proxyPane = proxyPaneRef.get();
                    JDialog dialog = dialogRef.get();
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
                // Using HttpURLConnection to set a User-Agent and avoid SourceForge blocking default Java requests
                URL url = new URL(Config.HOME_PAGE + "changes.txt");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                
                // Identify as a browser to prevent 403 Forbidden errors
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        latestVersion = reader.readLine();
                    }
                }
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
                    String rawMessage = langManager.getString("update.unable_determine_version");
                    String formattedMessage = MessageFormat.format(rawMessage, Config.SOURCEFORGE_MIRROR);
                    ApplicationFrame
                            .getInstance()
                            .getConsole()
                            .println(
                                    formattedMessage,
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
                    menu = new JMenu(langManager.getString("update.menu"));
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