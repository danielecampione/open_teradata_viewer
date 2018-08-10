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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

import net.sourceforge.open_teradata_viewer.util.StringUtil;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SystemStatusBar extends StatusBar {

    private static final long serialVersionUID = -5478767078830027062L;

    public final static int STATUS_PANEL_HEIGHT = 20;
    public final static long startApp = System.nanoTime();

    private StatusPanel memoryPanel;
    private StatusPanel timePanel;
    private StatusPanel runTimePanel;

    private Timer timer;

    private boolean memoryStatus = true;
    private boolean gcStatus = true;
    private boolean timeStatus = true;
    private boolean runTimeStatus = true;
    private boolean dateTimeFormat = false;

    public SystemStatusBar() {
        super();
        timer = new Timer();
        initStatusBarMemmory();
        initStatusBarTime();
        initStatusBarRunTime();
    }

    public Timer getTimer() {
        return timer;
    }

    private void updateMemoryStatus() {
        if (memoryStatus) {
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    final Runtime rt = Runtime.getRuntime();
                    final long totalMemory = rt.totalMemory();
                    final long freeMemory = rt.freeMemory();
                    final long usedMemory = totalMemory - freeMemory;
                    StringBuilder buf = new StringBuilder();
                    buf.append(StringUtil.formatSize(usedMemory, 1))
                            .append("/")
                            .append(StringUtil.formatSize(totalMemory, 1) + " ");
                    memoryPanel.setText(buf.toString());
                }
            });
        }
    }

    private void initStatusBarMemmory() {
        addPanel("memory-panel", new StatusPanel() {

            private static final long serialVersionUID = -2644840311355493304L;

            public void paint(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                Runtime rt = Runtime.getRuntime();
                Stroke lastStroke = g2.getStroke();
                g2.setStroke(new BasicStroke(1));
                g2.setPaint(new Color(192, 192, 0, 190));
                g2.fillRect(2 + getIcon().getIconWidth(), 2,
                        (int) ((getWidth() - 5 - getIcon().getIconWidth())
                                * (rt.totalMemory() - rt.freeMemory()) / rt
                                .totalMemory()), getHeight() - 5);
                g2.setPaint(new Color(64, 64, 0, 190));
                g2.drawRect(2 + getIcon().getIconWidth(), 2, getWidth() - 5
                        - getIcon().getIconWidth(), getHeight() - 5);
                g2.setStroke(lastStroke);
                super.paint(g);
            }
        });
        memoryPanel = getPanel("memory-panel");
        memoryPanel
                .setToolTipText("<html>Shows the used and available memory.<br>Allows the system to perform memory cleanup program.</html>");
        memoryPanel.setIcon(ImageManager.getImage("/icons/memory.gif"));
        memoryPanel.setDisplayActivation(true);
        timer.schedule(new TimerTask() {
            public void run() {
                updateMemoryStatus();
            }
        }, 100, 1000);
        memoryPanel.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                System.gc();
                updateMemoryStatus();
            }
            public void mousePressed(MouseEvent e) {
            }
            public void mouseReleased(MouseEvent e) {
            }
            public void mouseEntered(MouseEvent e) {
            }
            public void mouseExited(MouseEvent e) {
            }
        });
        updateMemoryStatus();
    }

    private void initStatusBarTime() {
        addPanel("time-panel");
        timePanel = getPanel("time-panel");
        timePanel.setIcon(ImageManager.getImage("/icons/clock.png"));
        timePanel.setMinimumSize(new Dimension(STATUS_PANEL_HEIGHT,
                STATUS_PANEL_HEIGHT));
        timePanel.setHorizontalAlignment(JLabel.CENTER);
        timer.schedule(new TimerTask() {
            public void run() {
                if (timeStatus) {
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            timePanel.setText(String.format((dateTimeFormat
                                    ? "%1$tY-%1$tm-%1$td "
                                    : "") + "%1$tH:%1$tM:%1$tS",
                                    new Object[]{new Date().getTime()})
                                    + " ");
                        }
                    });
                }
            }
        }, 100, 1000);
    }

    private void initStatusBarRunTime() {
        addPanel("runtime-panel");
        runTimePanel = getPanel("runtime-panel");
        runTimePanel.setIcon(ImageManager.getImage("/icons/runner.gif"));
        runTimePanel.setHorizontalAlignment(JLabel.CENTER);
        timer.schedule(new TimerTask() {
            public void run() {
                if (runTimeStatus) {
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            runTimePanel.setText(StringUtil.formatTime(System
                                    .nanoTime() - startApp)
                                    + " ");
                        }
                    });
                }
            }
        }, 100, 1000);
    }

    public void setMemoryStatus(boolean memoryStatus) {
        this.memoryStatus = memoryStatus;
        memoryPanel.setVisible(this.memoryStatus);
    }

    public boolean isMemoryStatus() {
        return memoryStatus;
    }

    public boolean isGcStatus() {
        return gcStatus;
    }

    public void setTimeStatus(boolean timeStatus) {
        this.timeStatus = timeStatus;
        timePanel.setVisible(this.timeStatus);
    }

    public boolean isTimeStatus() {
        return timeStatus;
    }

    public void setDateTimeFormat(boolean dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    public boolean isDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setRunTimeStatus(boolean runTimeStatus) {
        this.runTimeStatus = runTimeStatus;
        runTimePanel.setVisible(this.runTimeStatus);
    }

    public boolean isRunTimeStatus() {
        return runTimeStatus;
    }

}
