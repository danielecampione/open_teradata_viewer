/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2011, D. Campione
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
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

/**
 * Screen displayed while the long lasting startup
 * 
 * @author D. Campione
 *
 */
public class SplashScreen extends JWindow {

    private static final long serialVersionUID = -8630351955402071722L;

    public static String INFO_MESSAGE;

    /** the progressbar which has to be resized */
    private JProgressBar progressbar_;

    static {
        try {
            INFO_MESSAGE = "<html><table><tr><td>"
                    + Main.APPLICATION_NAME
                    + " "
                    + Config.getVersion()
                    + "</td><td align='right'>"
                    + Config.HOME_PAGE
                    + "</td></tr><tr><td>Copyright &copy 2011, D. Campione </td><td>A sourceforge.net project</td></tr></table></html>";
        } catch (IOException e) {
        }
    }

    //----------------------------------------------------------------------------  

    /** 
     * creates the new splashscreen window
     * @param frame the main window the splashscreen is depending on => if main window closed also
     * splash is closed
     * @param wait_time time how long the window is diplayed
     */
    public SplashScreen(JFrame frame, int wait_time) {
        super(frame);

        Dimension screen_size;
        Dimension label_size;
        JPanel main_pain = new JPanel();

        getContentPane().add(main_pain);

        main_pain.setBackground(Color.white);
        main_pain.setBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED));

        main_pain.setLayout(new BorderLayout(0, 10));

        addContainers(main_pain);
        pack();

        screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        label_size = getContentPane().getComponent(0).getPreferredSize();

        setLocation(screen_size.width / 2 - (label_size.width / 2),
                screen_size.height / 2 - (label_size.height / 2));
        addListeners();

        threadIt(wait_time * 1000);
    }

    //----------------------------------------------------------------------------
    /** adds a Listeners to the window */
    private void addListeners() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                setVisible(false);
                dispose();
            }
        });
    }

    //----------------------------------------------------------------------------
    /** 
     * Starts a thread to for the splash screen
     * @param wait_time time to wait until splashscreen vanishes if this param is set 0 the splash
     * screen does not vanish automatically. It has to be closed manually.
     */
    private void threadIt(int wait_time) {
        final int pause = wait_time;

        final Runnable closer_runner = new Runnable() {
            public void run() {
                setVisible(false);
                dispose();
            }
        };

        Runnable wait_runner = new Runnable() {
            public void run() {
                try {
                    if (pause > 0) {
                        Thread.sleep(pause);
                        SwingUtilities.invokeAndWait(closer_runner);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        setVisible(true);
        Thread splashThread = new Thread(wait_runner, "SplashThread");
        splashThread.start();
    }

    //----------------------------------------------------------------------------
    /** 
     * Adds containers the the main pane
     *
     * @param main_pane the main pane
     */
    private void addContainers(JPanel main_pane) {
        JLabel text_label = new JLabel(INFO_MESSAGE);
        text_label.setHorizontalAlignment(JLabel.CENTER);

        progressbar_ = new JProgressBar(1, 100);
        progressbar_.setStringPainted(true);

        // The ImageManager is not yet ready at this time.
        main_pane.add(new JLabel(new ImageIcon(getClass().getClassLoader()
                .getResource("icons/logo.png"))), BorderLayout.NORTH);
        main_pane.add(progressbar_, BorderLayout.CENTER);
        main_pane.add(text_label, BorderLayout.SOUTH);
    }

    //----------------------------------------------------------------------------
    /** 
     * The progressbar has the capability to display text. If the text displayed has to
     * changed only call this method.
     *
     * @param task new text displayed in progressbar
     */
    public void showNewTask(String task) {
        progressbar_.setString(task);
    }

    //----------------------------------------------------------------------------
    /** 
     * The progressbar has to grow while the application is loading. If a task has been
     * completed the progressbar has to be resized by invoking this method
     *
     * @param amount_of_progress portion of load has been down with this class
     */
    public void progress(int amount_of_progress) {
        int total_progress = progressbar_.getValue() + amount_of_progress;

        if (total_progress > 100) {
            total_progress = 100;
        }

        progressbar_.setValue(total_progress);
    }
}
