/*
 * Open Teradata Viewer ( animated assistant )
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

package net.sourceforge.open_teradata_viewer.animated_assistant;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class AnimatedAssistant {

    public static final AnimatedAssistant EMPTY = new AnimatedAssistant();
    private volatile Image image;
    private volatile String message;
    private long animatedAssistantTime;
    private int animatedAssistantMillis;
    private int progress;

    public AnimatedAssistant() {
        this.animatedAssistantTime = System.currentTimeMillis();
        this.progress = 100;
    }

    public AnimatedAssistant(int animatedAssistantMillis) {
        this();
        this.animatedAssistantMillis = animatedAssistantMillis;
    }

    public AnimatedAssistant(Image image, String message) {
        this(image, message, 0);
    }

    public AnimatedAssistant(ImageIcon icon, String message) {
        this(icon == null ? null : icon.getImage(), message);
    }

    public AnimatedAssistant(Image image, String message,
            int animatedAssistantMillis) {
        this(animatedAssistantMillis);
        this.image = image;
        this.message = message;
    }

    public AnimatedAssistant(ImageIcon icon, String message,
            int animatedAssistantMillis) {
        this(icon == null ? null : icon.getImage(), message,
                animatedAssistantMillis);
    }

    public static AnimatedAssistant createSqlAnimatedAssistant() {
        return new AnimatedAssistant((ImageIcon) null, null, 500);
    }

    public static AnimatedAssistant createSqlAnimatedAssistant(String message,
            int animatedAssistantMillis) {
        return new AnimatedAssistant((ImageIcon) null, message,
                animatedAssistantMillis);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isShowTime() {
        return System.currentTimeMillis() > this.animatedAssistantTime
                + this.animatedAssistantMillis;
    }

}