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
import java.io.IOException;
import java.net.Authenticator;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.sourceforge.open_teradata_viewer.actions.Actions;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;
import net.sourceforge.open_teradata_viewer.update.GitHubReleaseVersionProvider;
import net.sourceforge.open_teradata_viewer.update.IUpdateVersionProvider;
import net.sourceforge.open_teradata_viewer.update.ProxyResolver;

/**
 * Checks, on a background thread, whether a newer version of the application
 * has been published, and - if so - shows the "update available" menu
 * (built and colored exactly as before; only the way the remote version is
 * retrieved has changed).
 * <p>
 * The remote version is now retrieved from the "latest release" of the
 * project's official GitHub repository (see
 * {@link net.sourceforge.open_teradata_viewer.update.GitHubReleaseVersionProvider})
 * instead of a plain-text file previously hosted on SourceForge, which had
 * become an unreliable source (intermittent <code>403</code> responses,
 * broken proxy handling, etc.).
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

            // Whether the initial Yes/No/Cancel prompt below should be
            // remembered so it is not shown again on every startup, and -
            // if so - which of the two answers to reuse. This is
            // deliberately kept independent of "proxy_remember_configuration"
            // further down, which only ever covers the manual proxy's
            // host/port and credentials sub-dialogs (reachable exclusively
            // after a YES answer here) and is left completely untouched by
            // this feature, to avoid ever skipping those sub-dialogs with
            // empty/unset values on a first run
            String proxyChoiceRememberedKey = "proxy_choice_remembered";
            String proxyUseManualProxyKey = "proxy_use_manual_proxy";

            String choiceRememberedRawSetting = Config
                    .getSetting(proxyChoiceRememberedKey);
            boolean choiceRemembered = choiceRememberedRawSetting != null
                    && choiceRememberedRawSetting.trim().equalsIgnoreCase(
                            "true");

            int result;
            boolean rememberChoiceNow = false;

            if (choiceRemembered) {
                String useManualRawSetting = Config
                        .getSetting(proxyUseManualProxyKey);
                boolean rememberedUseManual = useManualRawSetting != null
                        && useManualRawSetting.trim().equalsIgnoreCase(
                                "true");
                result = rememberedUseManual ? JOptionPane.YES_OPTION
                        : JOptionPane.NO_OPTION;
            } else {
                // Show dialog on EDT
                final AtomicInteger resultRef = new AtomicInteger();
                final AtomicReference<Boolean> rememberChoiceRef = new AtomicReference<>(
                        Boolean.FALSE);

                try {
                    SwingUtilities.invokeAndWait(() -> {
                        // The checkbox must be created here, on the EDT,
                        // not before entering this block: constructing a
                        // Swing component off the EDT is a threading
                        // violation that some look and feels (e.g.
                        // Substance) actively detect and report
                        JCheckBox rememberChoiceCheckBox = new JCheckBox(
                                langManager.getString("proxy.dont_ask_again"));
                        Object[] message = new Object[] {
                                langManager
                                        .getString("update.configure_proxy"),
                                rememberChoiceCheckBox };
                        int dialogResult = JOptionPane.showConfirmDialog(
                                ApplicationFrame.getInstance(),
                                message,
                                langManager.getString("update.checker_title"),
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE);
                        resultRef.set(dialogResult);
                        rememberChoiceRef.set(rememberChoiceCheckBox
                                .isSelected());
                    });
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                result = resultRef.get();
                rememberChoiceNow = rememberChoiceRef.get();
            }

            if (result == JOptionPane.CLOSED_OPTION
                    || result == JOptionPane.CANCEL_OPTION) {
                return;
            }

            // A YES/NO answer is only persisted when the user explicitly
            // checked the "don't ask again" box; CLOSED/CANCEL is never
            // remembered (it means "decide later", not "never")
            if (!choiceRemembered && rememberChoiceNow) {
                Config.saveSetting(proxyChoiceRememberedKey,
                        Boolean.TRUE.toString());
                Config.saveSetting(proxyUseManualProxyKey, Boolean
                        .toString(result == JOptionPane.YES_OPTION));
            }

            // "result" gets reassigned further down for the proxy
            // sub-dialog's own outcome, so the original Yes/No/Cancel
            // choice is captured here once and for all
            final boolean useManualProxy = (result == JOptionPane.YES_OPTION);

            System.setProperty("java.net.useSystemProxies", new Boolean(
                    result == JOptionPane.NO_OPTION).toString());

            String proxyHost = "", proxyPort = "";

            if (result == JOptionPane.YES_OPTION) {
                String proxyHostKey = "proxy_host", proxyPortKey = "proxy_port";
                String proxyRememberConfigurationKey = "proxy_remember_configuration";

                String rawSetting = Config.getSetting(proxyRememberConfigurationKey);
                boolean proxyRememberConfiguration = rawSetting != null
                        && rawSetting.trim().equalsIgnoreCase("true");
                proxyHost = Config.getSetting(proxyHostKey);
                proxyPort = Config.getSetting(proxyPortKey);

                if (!proxyRememberConfiguration) {
                    // All Swing component creation and the modal dialog
                    // display/disposal must happen on the EDT - this used
                    // to run on the caller's background thread, a
                    // threading violation that some look and feels (e.g.
                    // Substance) actively detect and report
                    final String[] hostHolder = new String[] { proxyHost };
                    final String[] portHolder = new String[] { proxyPort };
                    final int[] resultHolder = new int[] { JOptionPane.CANCEL_OPTION };

                    try {
                        SwingUtilities.invokeAndWait(() -> {
                            final JTextField proxyHostField = new JTextField();
                            final JTextField proxyPortField = new JTextField();
                            proxyHostField.setText(hostHolder[0]);
                            proxyPortField.setText(portHolder[0]);

                            JOptionPane proxyPane = new JOptionPane(new Object[] {
                                    new JLabel(langManager.getString("update.host")), proxyHostField,
                                    new JLabel(langManager.getString("update.port")), proxyPortField },
                                    JOptionPane.QUESTION_MESSAGE,
                                    JOptionPane.OK_CANCEL_OPTION);
                            JDialog dialog = proxyPane.createDialog(langManager.getString("update.server_proxy_title"));

                            UISupport.showDialog(dialog);

                            Integer objResult = (Integer) proxyPane.getValue();
                            int dialogResult = JOptionPane.CANCEL_OPTION;
                            if (objResult != null) {
                                dialogResult = objResult;
                            }
                            dialog.dispose();

                            resultHolder[0] = dialogResult;
                            hostHolder[0] = proxyHostField.getText().trim();
                            portHolder[0] = proxyPortField.getText().trim();
                        });
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                    result = resultHolder[0];

                    if (result == JOptionPane.OK_OPTION) {
                        proxyHost = hostHolder[0];
                        proxyPort = portHolder[0];

                        Config.saveSetting(proxyHostKey, proxyHost);
                        Config.saveSetting(proxyPortKey, proxyPort);
                        Config.saveSetting(proxyRememberConfigurationKey,
                                new Boolean(proxyRememberConfiguration)
                                        .toString());
                    } else {
                        return;
                    }
                }

                HTTPAuthProxy httpAuthProxy = new HTTPAuthProxy(
                        proxyRememberConfiguration, rememberChoiceNow);
                if (httpAuthProxy.isAuthenticationNecessary()) {
                    Authenticator.setDefault(httpAuthProxy);
                }
            }

            IUpdateVersionProvider updateVersionProvider = new GitHubReleaseVersionProvider();

            DateFormat format = new SimpleDateFormat("(dd/MM/yyyy)");
            String localVersion = Config.getVersion();
            String latestVersion = null;
            try {
                Proxy proxy = useManualProxy
                        ? ProxyResolver.resolveManualProxy(proxyHost, proxyPort)
                        : ProxyResolver.resolveSystemProxy(new URL(
                                Config.GITHUB_LATEST_RELEASE_API_URL));

                latestVersion = updateVersionProvider
                        .getLatestVersionLabel(proxy);
            } catch (ProtocolException pe) { // The exception is caught if the Server has redirected too many times
                ExceptionDialog.ignoreException(pe);
            } catch (IOException ioe) { // Network, proxy or GitHub API error
                ApplicationFrame
                        .getInstance()
                        .getConsole()
                        .println(ioe.getMessage(),
                                ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            } finally {
                if (latestVersion == null) {
                    String rawMessage = langManager.getString("update.unable_determine_version");
                    String formattedMessage = MessageFormat.format(rawMessage, Config.GITHUB_REPO_URL);
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
