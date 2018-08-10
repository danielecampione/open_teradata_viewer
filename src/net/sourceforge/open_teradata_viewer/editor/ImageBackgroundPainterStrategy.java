/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * A strategy for painting the background of an <code>TextAreaBase</code> as an
 * image. The image is always stretched to completely fill the
 * <code>TextAreaBase</code>.<p>
 *
 * You can set the scaling hint used when stretching/skewing the image to fit in
 * the <code>TextAreaBase</code>'s background via the
 * <code>setScalingHint</code> method, but keep in mind the more accurate the
 * scaling hint, the less responsive your application will be when stretching
 * the window (as that's the only time the image's size is recalculated).
 *
 * @author D. Campione
 * @see net.sourceforge.open_teradata_viewer.editor.BufferedImageBackgroundPainterStrategy
 * @see net.sourceforge.open_teradata_viewer.editor.VolatileImageBackgroundPainterStrategy
 * 
 */
public abstract class ImageBackgroundPainterStrategy
        implements
            IBackgroundPainterStrategy {

    protected MediaTracker tracker;

    private TextAreaBase textArea;
    private Image master;
    private int oldWidth, oldHeight;
    private int scalingHint;

    /**
     * Ctor.
     *
     * @param textArea The text area using this image as its background.
     */
    public ImageBackgroundPainterStrategy(TextAreaBase textArea) {
        this.textArea = textArea;
        tracker = new MediaTracker(textArea);
        scalingHint = Image.SCALE_FAST;
    }

    /**
     * Returns the text area using this strategy.
     *
     * @return The text area.
     */
    public TextAreaBase getTextAreaBase() {
        return textArea;
    }

    /**
     * Returns the "master" image; that is, the original, unscaled image. When
     * the image needs to be rescaled, scaling should be done from this image,
     * to prevent repeated scaling from distorting the image.
     *
     * @return The master image.
     */
    public Image getMasterImage() {
        return master;
    }

    /**
     * Returns the scaling hint being used.
     *
     * @return The scaling hint to use when scaling an image.
     * @see #setScalingHint
     */
    public int getScalingHint() {
        return scalingHint;
    }

    /**
     * Paints the image at the specified location and at the specified size.
     *
     * @param g The graphics context.
     * @param bounds The bounds in which to paint the image. The image will be
     *               scaled to fit exactly in these bounds if necessary.
     */
    public final void paint(Graphics g, Rectangle bounds) {
        if (bounds.width != oldWidth || bounds.height != oldHeight) {
            rescaleImage(bounds.width, bounds.height, getScalingHint());
            oldWidth = bounds.width;
            oldHeight = bounds.height;
        }
        paintImage(g, bounds.x, bounds.y);
    }

    /**
     * Paints the image at the specified location. This method assumes scaling
     * has already been done, and simply paints the background image "as-is."
     *
     * @param g The graphics context.
     * @param x The x-coordinate at which to paint.
     * @param y The y-coordinate at which to paint.
     */
    protected abstract void paintImage(Graphics g, int x, int y);

    /**
     * Rescales the displayed image to be the specified size.
     *
     * @param width The new width of the image.
     * @param height The new height of the image.
     * @param hint The scaling hint to use.
     */
    protected abstract void rescaleImage(int width, int height, int hint);

    /**
     * Sets the image this background painter displays.
     *
     * @param imageURL URL of a file containing the image to display.
     */
    public void setImage(URL imageURL) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageURL);
        } catch (Exception e) {
            ExceptionDialog.notifyException(e);
        }
        setImage(image);
    }

    /**
     * Sets the image this background painter displays.
     *
     * @param image The new image to use for the background.
     */
    public void setImage(Image image) {
        master = image;
        oldWidth = -1; // To trick us into fixing bgImage
    }

    /**
     * Sets the scaling hint to use when scaling the image.
     *
     * @param hint The hint to apply; e.g. <code>Image.SCALE_DEFAULT</code>.
     * @see #getScalingHint
     */
    public void setScalingHint(int hint) {
        this.scalingHint = hint;
    }
}