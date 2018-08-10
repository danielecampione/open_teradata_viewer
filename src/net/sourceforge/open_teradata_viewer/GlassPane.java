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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JComponent;

import net.sourceforge.open_teradata_viewer.actions.Actions;
import net.sourceforge.open_teradata_viewer.actions.AnimatedAssistantAction;
import net.sourceforge.open_teradata_viewer.animated_assistant.AnimatedAssistant;
import net.sourceforge.open_teradata_viewer.util.StreamUtil;
import net.sourceforge.open_teradata_viewer.util.StringUtil;
import net.sourceforge.open_teradata_viewer.util.array.StringList;
import net.sourceforge.open_teradata_viewer.util.timer.Timer;
import net.sourceforge.open_teradata_viewer.util.timer.TimerManager;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GlassPane extends JComponent {

    private static final long serialVersionUID = 95688700696534977L;

    protected JComponent component;
    private ArrayList<Image> animatedAssistantImageList;
    private int indexAnim;
    private Rectangle renderRectangle;

    private int visibleCount;
    private final ArrayList<AnimatedAssistant> animatedAssistantList;
    private Timer animatedAssistantTimer;
    private boolean animatedAssistantRendererOn = true;
    private AnimatedAssistant[] animatedAssistantArray;
    private boolean repainting;

    public GlassPane() {
        this.animatedAssistantImageList = new ArrayList<Image>();
        StringList sl = new StringList();
        try {
            sl.setText(StreamUtil.stream2String(getClass().getResourceAsStream(
                    "/res/anim.list")));
            for (int i = 0; i < sl.size(); i++) {
                if (StringUtil.isEmpty((String) sl.get(i)))
                    continue;
                try {
                    this.animatedAssistantImageList.add(ImageManager.getImage(
                            "/icons/anim/" + (String) sl.get(i)).getImage());
                } catch (Throwable ex) {
                    System.err.println((String) sl.get(i));
                }
            }
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
        }

        setIgnoreRepaint(true);
        setBackground(Color.WHITE);
        setDoubleBuffered(true);
        setFont(new Font("Default", 1, 11));
        this.animatedAssistantList = new ArrayList<AnimatedAssistant>();
        setComponent(this);
        this.animatedAssistantTimer = new Timer(100L) {
            public void run() {
                if (GlassPane.this.animatedAssistantRendererOn) {
                    if (!GlassPane.this.repainting) {
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                Rectangle rect = getRenderBounds();
                                if (rect != null) {
                                    GlassPane.this.repaint(rect);
                                } else {
                                    GlassPane.this.repaint();
                                }
                            }

                        });
                    }
                }
            }

        };
        TimerManager.getGlobal().add(this.animatedAssistantTimer);
    }
    public void beginProcess() {
        this.indexAnim = new Random().nextInt(this.animatedAssistantImageList
                .size());
        Image image = (Image) this.animatedAssistantImageList
                .get(this.indexAnim);
        int left = this.component.getWidth() - 50 - 30 - image.getWidth(null);
        int top = this.component.getHeight() - 60 - 30 - image.getHeight(null);
        this.renderRectangle = new Rectangle(left, top,
                this.component.getWidth() - left, this.component.getHeight()
                        - top);
    }

    public void setComponent(JComponent component) {
        this.component = component;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (((AnimatedAssistantAction) Actions.ANIMATED_ASSISTANT)
                .isAnimatedAssistantActived()) {
            Graphics2D g2 = (Graphics2D) g;

            Toolkit.getDefaultToolkit().sync();
            synchronized (this.animatedAssistantList) {
                if (this.animatedAssistantRendererOn) {
                    boolean showAnimatedAssistant = false;
                    for (AnimatedAssistant animatedAssistant : this.animatedAssistantList) {
                        if (animatedAssistant.isShowTime()) {
                            showAnimatedAssistant = true;
                            break;
                        }
                    }

                    if ((this.animatedAssistantList.size() > 0)
                            && (showAnimatedAssistant)) {
                        FontMetrics fm = g2.getFontMetrics();
                        int left = this.component.getWidth() - 50;
                        int top = this.component.getHeight() - 60;
                        Image image = (Image) this.animatedAssistantImageList
                                .get(this.indexAnim);
                        g2.drawImage(image, left - image.getWidth(null), top
                                - image.getHeight(null), null);

                        int picWidth = 0;
                        for (AnimatedAssistant animatedAssistant : animatedAssistantArray) {
                            if (animatedAssistant.getImage() != null) {
                                picWidth += animatedAssistant.getImage()
                                        .getWidth(null);
                            }
                        }
                        int picPos = 0;
                        for (AnimatedAssistant animatedAssistant : animatedAssistantArray) {
                            if (animatedAssistant.getImage() != null) {
                                g2.drawImage(animatedAssistant.getImage(), left
                                        - (image.getWidth(null) + picWidth) / 2
                                        + picPos, top + 2, null);

                                picPos += animatedAssistant.getImage()
                                        .getWidth(null);
                            }
                        }
                        int textPos = 0;
                        for (AnimatedAssistant animatedAssistant : animatedAssistantArray) {
                            if (animatedAssistant.getMessage() != null) {
                                g2.drawString(
                                        animatedAssistant.getMessage(),
                                        left
                                                - (image.getWidth(null) + fm
                                                        .stringWidth(animatedAssistant
                                                                .getMessage()))
                                                / 2, top + textPos + 20);

                                textPos += fm.getHeight() + 4;
                            }
                        }
                    }
                }
            }
            super.paintComponent(g);
        } else {
            super.paintComponent(g);
        }
    }

    public void setVisible(boolean aFlag) {
        if (aFlag) {
            if (this.visibleCount == 0) {
                super.setVisible(true);
            }
            this.visibleCount += 1;
        } else if (this.visibleCount > 0) {
            this.visibleCount -= 1;
            if (this.visibleCount == 0)
                super.setVisible(false);
        }
    }

    public void addAnimatedAssistant(AnimatedAssistant animatedAssistant) {
        addAnimatedAssistant(animatedAssistant, null);
    }

    public void addAnimatedAssistant(AnimatedAssistant animatedAssistant,
            String animatedAssistantRendererId) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                GlassPane.this.setVisible(true);
            }

        });
        synchronized (this.animatedAssistantList) {
            if (this.animatedAssistantList.indexOf(animatedAssistant) == -1) {
                if (this.animatedAssistantList.size() == 0) {
                    if (animatedAssistantRendererId != null) {
                        this.animatedAssistantRendererOn = true;
                    } else {
                        this.animatedAssistantRendererOn = ((AnimatedAssistantAction) Actions.ANIMATED_ASSISTANT)
                                .isAnimatedAssistantActived();
                    }
                    beginProcess();
                }
            }
            this.animatedAssistantList.add(animatedAssistant);
            this.animatedAssistantArray = ((AnimatedAssistant[]) this.animatedAssistantList
                    .toArray(new AnimatedAssistant[this.animatedAssistantList
                            .size()]));
        }
        this.animatedAssistantTimer.setEnabled((this.animatedAssistantList
                .size() > 0) && (this.animatedAssistantRendererOn));
    }

    public void removeAnimatedAssistant(AnimatedAssistant animatedAssistant) {
        synchronized (this.animatedAssistantList) {
            this.animatedAssistantList.remove(animatedAssistant);
            try {
                if (this.animatedAssistantList.size() > 0) {
                    this.animatedAssistantArray = ((AnimatedAssistant[]) this.animatedAssistantList
                            .toArray(new AnimatedAssistant[this.animatedAssistantList
                                    .size()]));
                } else {
                    this.animatedAssistantArray = null;
                }
            } finally {
                this.animatedAssistantTimer
                        .setEnabled((this.animatedAssistantList.size() > 0)
                                && (this.animatedAssistantRendererOn));
                if (this.animatedAssistantList.size() == 0) {
                    repaint();
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            GlassPane.this.setVisible(false);
                        }

                    });
                }
            }
        }
    }

    public boolean contains(int x, int y) {
        if ((getMouseListeners().length == 0)
                && (getMouseMotionListeners().length == 0)
                && (getMouseWheelListeners().length == 0)
                && (getCursor() == Cursor.getPredefinedCursor(0))) {
            return false;
        }
        return super.contains(x, y);
    }

    public Rectangle getRenderBounds() {
        return this.renderRectangle;
    }
}