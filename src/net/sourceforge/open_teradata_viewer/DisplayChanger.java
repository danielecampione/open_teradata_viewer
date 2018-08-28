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

import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

import net.sourceforge.open_teradata_viewer.util.SwingUtil;

/**
 * 
 *  
 * @author D. Campione
 * 
 */
public class DisplayChanger {

    private GraphicsEnvironment graphicsEnvironment;
    private GraphicsDevice graphicsDevice;
    private GraphicsDevice[] graphicsDevices;
    private boolean fullScreenSupported;
    private boolean displayChangeSupported;
    private Window window;
    private DisplayMode[] displayModes;
    private DisplayMode originalDisplayMode = null;
    private DisplayMode displayMode;
    private boolean undecorated;
    private int originalWidth;
    private int originalHeight;
    private boolean undecorable = true;
    private boolean exclusiveMode = true;

    public DisplayChanger(Window window) {
        this.window = window;
        graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphicsDevices = graphicsEnvironment.getScreenDevices();
        if (graphicsDevices == null) {
            System.err.println("No Graphics Devices.");
            System.exit(-1);
        }
        setGraphicsDevice(graphicsEnvironment.getDefaultScreenDevice());
    }

    public boolean isUndecorable() {
        return undecorable;
    }

    public void setUndecorable(boolean undecorable) {
        this.undecorable = undecorable;
    }

    public boolean isExclusiveMode() {
        return exclusiveMode;
    }

    public void setExclusiveMode(boolean exclusiveMode) {
        this.exclusiveMode = exclusiveMode;
    }

    public GraphicsDevice getGraphicsDevice() {
        return graphicsDevice;
    }

    public void setGraphicsDevice(GraphicsDevice graphicsDevice) {
        this.graphicsDevice = graphicsDevice;
        displayModes = this.graphicsDevice.getDisplayModes();
        fullScreenSupported = this.graphicsDevice.isFullScreenSupported();
        displayChangeSupported = this.graphicsDevice.isDisplayChangeSupported();
    }

    public GraphicsEnvironment getGraphicsEnvironment() {
        return graphicsEnvironment;
    }

    public boolean isFullScreenSupported() {
        return fullScreenSupported;
    }

    public boolean isDisplayChangeSupported() {
        return displayChangeSupported;
    }

    public DisplayMode[] getDisplayModes() {
        return displayModes;
    }

    public DisplayMode getOriginalDisplayMode() {
        return originalDisplayMode;
    }

    public DisplayMode getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public GraphicsDevice[] getGraphicsDevices() {
        return graphicsDevices;
    }

    public void setDisplayMode(boolean fullScreen) {
        boolean disposed = false;
        if (fullScreen) {
            if (SwingUtil.isVisible(window)) {
                window.dispose();
                disposed = true;
            }
            setDisplayMode(-1, -1, -1, fullScreen);
            if (disposed) {
                window.setVisible(true);
                window.repaint();
            }
        } else {
            if (SwingUtil.isVisible(window)) {
                window.dispose();
                disposed = true;
            }
            restoreDisplayMode();
            if (disposed) {
                window.setVisible(true);
                window.repaint();
            }
        }
    }
    public void setDisplayMode(int width, int height, int depth) {
        setDisplayMode(width, height, depth,
                (graphicsDevice.getDisplayMode() == null ? 60 : graphicsDevice
                        .getDisplayMode().getRefreshRate()), true);
    }

    public void setDisplayMode(int width, int height, int depth,
            boolean fullScreen) {
        setDisplayMode(width, height, depth,
                (graphicsDevice.getDisplayMode() == null ? 60 : graphicsDevice
                        .getDisplayMode().getRefreshRate()), fullScreen);
    }

    public void setDisplayMode(int width, int height, int depth,
            int refreshRate, boolean fullScreen) {
        originalWidth = window.getWidth();
        originalHeight = window.getHeight();
        if (width != -1 && height != -1) {
            window.setSize(width, height);
            SwingUtil.centerWithinScreen(window);
        } else {
            window.setSize(graphicsDevice.getDisplayMode().getWidth(),
                    graphicsDevice.getDisplayMode().getHeight());
            SwingUtil.centerWithinScreen(window);
        }
        if (fullScreenSupported && fullScreen) {
            if (width != -1 && height != -1) {
                originalDisplayMode = graphicsDevice.getDisplayMode();
                displayMode = new DisplayMode(width, height, depth, refreshRate);
            }
            if (window instanceof Frame && !SwingUtil.isVisible(window)
                    && undecorable) {
                undecorated = ((Frame) window).isUndecorated();
                ((Frame) window).setUndecorated(true);
            } else {
                undecorated = false;
            }
            try {
                if (exclusiveMode) {
                    graphicsDevice.setFullScreenWindow(window);
                }
                if (width != -1 && height != -1) {
                    graphicsDevice.setDisplayMode(displayMode);
                }
            } catch (Throwable e) {
                graphicsDevice.setFullScreenWindow(null);
                originalDisplayMode = null;
            }
        }
    }

    public void restoreDisplayMode() {
        if (originalDisplayMode != null) {
            graphicsDevice.setDisplayMode(originalDisplayMode);
            if (exclusiveMode) {
                graphicsDevice.setFullScreenWindow(null);
            }
        }
        if (window instanceof Frame && !SwingUtil.isVisible(window)
                && undecorable) {
            ((Frame) window).setUndecorated(undecorated);
        }
        window.setSize(originalWidth, originalHeight);
        SwingUtil.centerWithinScreen(window);
    }

}
