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

import java.awt.GridLayout;
import java.util.ConcurrentModificationException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;
import net.sourceforge.open_teradata_viewer.util.SwingUtil;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class WaitingDialog extends TimerTask {

    private JLabel message1;
    private JLabel message2;
    private JDialog dialog;
    private long startTime = System.currentTimeMillis();
    private Timer timer;

    private static final String BUTTON_CANCEL_KEY = "button.cancel";

    public WaitingDialog(final Runnable onCancel) throws InterruptedException {
        final LanguageManager langManager = LanguageManager.getInstance();

        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    message1 = new JLabel();
                    message2 = new JLabel();
                    JPanel panel = new JPanel(new GridLayout(2, 1));
                    panel.add(message1);
                    panel.add(message2);
                    final Dialog pane = new Dialog(panel, Dialog.PLAIN_MESSAGE,
                            Dialog.DEFAULT_OPTION, new Object[]{BUTTON_CANCEL_KEY}, BUTTON_CANCEL_KEY);
                    dialog = pane.createDialog(ApplicationFrame.getInstance(), null);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                UISupport.showDialog(dialog);
                            } catch (ConcurrentModificationException cme) {
                                ExceptionDialog.ignoreException(cme);
                            }
                            if ((onCancel != null) && langManager.getString(BUTTON_CANCEL_KEY).equals(pane.getValue())) {
                                onCancel.run();
                            }
                        }
                    }).start();
                }
            });
        } catch (java.lang.reflect.InvocationTargetException ite) {
            ExceptionDialog.ignoreException(ite);
        }

        boolean visible = false;
        while (!visible) {
            Thread.sleep(20);
            visible = SwingUtil.isVisible(dialog);
        }
        timer = new Timer();
        timer.schedule(this, 3000, 1000);
    }

    public void setText(String text) {
        message1.setText(text);
    }

    public boolean isVisible() {
        return SwingUtil.isVisible(dialog);
    }

    public void hide() {
        dialog.dispose();
        cancel();
        timer.cancel();
    }

    public String getExecutionTime() {
        LanguageManager langManager = LanguageManager.getInstance();
        long executionTime = (System.currentTimeMillis() - startTime) / 1000;
        long hours = executionTime / 60 / 60;
        long minutes = executionTime / 60 % 60;
        long seconds = executionTime % 60;
        StringBuilder text = new StringBuilder();
        if (hours > 0) {
            text.append(hours).append(hours == 1 ?
                langManager.getString("time.hour_singular") :
                langManager.getString("time.hour_plural")).append(" ");
        }
        if (minutes > 0) {
            text.append(minutes).append(minutes == 1 ?
                langManager.getString("time.minute_singular") :
                langManager.getString("time.minute_plural")).append(" ");
        }
        text.append(seconds).append(seconds == 1 ?
            langManager.getString("time.second_singular") :
            langManager.getString("time.second_plural"));
        return text.toString();
    }

    @Override
    public void run() {
        // TimerTask#run() executes on the Timer's own background thread,
        // not on the EDT: mutating a Swing component (JLabel#setText())
        // directly from here is a classic Swing thread-safety violation.
        // Every single query that runs longer than 3 seconds hits this,
        // since that is exactly when this callback starts firing
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                message2.setText(getExecutionTime());
            }
        });
    }
}