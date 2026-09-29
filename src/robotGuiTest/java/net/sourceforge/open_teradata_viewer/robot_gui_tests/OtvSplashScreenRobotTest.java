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

    public static void main(String[] args) throws Exception {
        final SplashScreen[] splash = new SplashScreen[1];

        SwingUtilities.invokeAndWait(() -> {
            splash[0] = new SplashScreen("icons/logo.png", "Test in progress...");
            splash[0].setVisible(true);
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
            Thread.sleep(200);
            Toolkit.getDefaultToolkit().sync();

            BufferedImage after = robot.createScreenCapture(bounds);

            if (imagesEqual(before, after)) {
                throw new AssertionError("The real screenshot before and after updateStatus() is identical: "
                        + "the repaint did not actually occur on the screen.");
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
