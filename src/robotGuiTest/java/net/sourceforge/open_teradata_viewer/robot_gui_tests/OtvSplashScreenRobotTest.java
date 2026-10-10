/*
 * Open Teradata Viewer ( test robot gui tests )
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

package net.sourceforge.open_teradata_viewer.robot_gui_tests;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

import net.sourceforge.open_teradata_viewer.SplashScreen;

/**
 * First graphical test (real, non-headless desktop) of the
 * Gradle migration.
 * <p>
 * Verifies, using a real {@link Robot} on a real screen, that
 * {@link SplashScreen} is actually rendered on the screen and that a
 * call to {@link SplashScreen#updateStatus(String, int)} produces a
 * repaint that is actually visible - not just a state change in memory.
 * <p>
 * Completely isolated: no database connection, no Internet access, and no
 * other OTV subsystem involved - only {@code icons/logo.png} and the
 * internationalization bundle, both local resources.
 *
 * @author D. Campione
 */
public final class OtvSplashScreenRobotTest {

    /** How long the screen is polled for the repaint to show up, in milliseconds. */
    private static final long REPAINT_TIMEOUT_MS = 3000;

    /** The delay between two screen captures while polling, in milliseconds. */
    private static final long POLL_INTERVAL_MS = 100;

    public static void main(String[] args) throws Exception {
        final SplashScreen[] splash = new SplashScreen[1];

        SwingUtilities.invokeAndWait(() -> {
            splash[0] = new SplashScreen("icons/logo.png", "Test in progress...");
            // A window hidden behind another one can never show a repaint on
            // the real screen, so the splash screen is kept above the others
            if (splash[0].isAlwaysOnTopSupported()) {
                splash[0].setAlwaysOnTop(true);
            }
            splash[0].setVisible(true);
            splash[0].toFront();
        });

        Robot robot = new Robot();
        robot.setAutoWaitForIdle(true);
        Thread.sleep(300);

        try {
            Rectangle bounds = boundsOf(splash[0]);

            // Move the REAL mouse pointer over the window, as further proof
            // that a real desktop/Robot session is being used
            Point center = new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
            robot.mouseMove(center.x, center.y);
            robot.waitForIdle();

            // waitForIdle() only guarantees that Swing's own event queue (and
            // therefore paintImmediately()) has finished; it says nothing
            // about whether the window system has actually flushed that
            // paint to the screen the Robot is about to capture. Toolkit
            // sync() is what forces that flush, and without it the "before"
            // and "after" captures below can end up reading the same,
            // not-yet-updated frame from the compositor - a false negative
            // that has nothing to do with SplashScreen's own repaint logic.
            Toolkit.getDefaultToolkit().sync();
            BufferedImage before = robot.createScreenCapture(bounds);

            SwingUtilities.invokeAndWait(() -> splash[0].updateStatus("Almost finished...", 80));
            robot.waitForIdle();

            // The window system may take a while to put the repaint on the
            // screen, so the screen is captured again and again until it
            // differs from the "before" capture or the time is over: a single
            // capture after a fixed delay is at the mercy of the machine load
            BufferedImage after;
            long deadline = System.currentTimeMillis() + REPAINT_TIMEOUT_MS;
            do {
                Thread.sleep(POLL_INTERVAL_MS);
                Toolkit.getDefaultToolkit().sync();
                after = robot.createScreenCapture(bounds);
            } while (imagesEqual(before, after) && System.currentTimeMillis() < deadline);

            if (imagesEqual(before, after)) {
                throw new AssertionError("The real screenshot before and after updateStatus() is identical for "
                        + REPAINT_TIMEOUT_MS + " ms: the repaint did not actually occur on the screen "
                        + "(splash screen showing: " + splash[0].isShowing() + ", always on top: "
                        + splash[0].isAlwaysOnTop() + ", bounds: " + bounds + "). Make sure that no other window "
                        + "covers it and that the desktop session is neither locked nor disconnected.");
            }

            System.out.println("OK - SplashScreen displayed and repainted on a real desktop (Robot).");
        } finally {
            SwingUtilities.invokeAndWait(() -> splash[0].dispose());
        }
    }

    private static Rectangle boundsOf(SplashScreen splash) throws Exception {
        final Rectangle[] result = new Rectangle[1];
        SwingUtilities.invokeAndWait(() -> {
            Point location = splash.getLocationOnScreen();
            result[0] = new Rectangle(location.x, location.y, splash.getWidth(), splash.getHeight());
        });
        return result[0];
    }

    private static boolean imagesEqual(BufferedImage a, BufferedImage b) {
        if (a.getWidth() != b.getWidth() || a.getHeight() != b.getHeight()) {
            return false;
        }
        for (int y = 0; y < a.getHeight(); y++) {
            for (int x = 0; x < a.getWidth(); x++) {
                if (a.getRGB(x, y) != b.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }
}
