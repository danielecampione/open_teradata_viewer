/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2015, D. Campione
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JEditorPane;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class HtmlText extends GraphicViewerObject {

    private static final long serialVersionUID = -3132164705942326902L;

    /** A CHANGED event hint: the text string has changed. */
    public static final int ChangedText = 2501;

    static JEditorPane myEditorPane = null;

    private String myString = "";
    transient BufferedImage myEditorImage = null;
    transient int myEditorImageType = 0;

    public HtmlText() {
    }

    public GraphicViewerObject copyObject(IGraphicViewerCopyEnvironment env) {
        HtmlText newobj = (HtmlText) super.copyObject(env);
        if (newobj != null) {
            newobj.myString = myString;
        }
        return newobj;
    }

    /**
     * Set the text to be displayed.
     *
     * @param str the text string to be displayed.
     */
    public void setText(String str) {
        String oldText = myString;
        if (!oldText.equals(str)) {
            myString = str;
            myEditorImage = null;
            update(ChangedText, 0, oldText);
        }
    }

    /**
     * Return the text that is currently being displayed.
     *
     * @return the current text string.
     */
    public String getText() {
        return myString;
    }

    protected void geometryChange(Rectangle prevRect) {
        Rectangle rect = getBoundingRect();
        if (rect.width != prevRect.width || rect.height != prevRect.height) {
            myEditorImage = null;
        }
        super.geometryChange(prevRect);
    }

    public void paint(Graphics2D g, GraphicViewerView view) {
        if (myEditorPane == null) {
            myEditorPane = new GraphicViewerEditorPane("text/html", getText());
            myEditorPane.setEditable(false);
            myEditorPane.setBounds(0, 0, 0, 0);
            myEditorPane.setVisible(false);
            view.setLayout(null);
            view.add(myEditorPane);
        }
        int graphicsImageType = g.getDeviceConfiguration().getDevice()
                .getType();
        boolean printing = view.isPrinting();
        if (myEditorImage == null || myEditorImageType != graphicsImageType) {
            int w = getWidth();
            int h = getHeight();
            if (w <= 0 || h <= 0) {
                return;
            }
            myEditorPane.setBounds(0, 0, w, h);
            double bw = w;
            double bh = h;
            if (printing) {
                bw *= 300;
                bw /= 72;
                bh *= 300;
                bh /= 72;
            }
            myEditorPane.setText(getText());
            myEditorImage = g.getDeviceConfiguration().createCompatibleImage(
                    (int) bw, (int) bh);
            myEditorImageType = graphicsImageType;
            Graphics2D g2 = myEditorImage.createGraphics();
            if (printing) {
                g2.scale(bw / w, bh / h);
            }
            myEditorPane.paint(g2);
            g2.dispose();
        }
        Rectangle rect = getBoundingRect();
        g.drawImage(myEditorImage, rect.x, rect.y, rect.width, rect.height,
                null);
    }

    public void copyNewValueForRedo(GraphicViewerDocumentChangedEdit e) {
        switch (e.getFlags()) {
            case ChangedText :
                e.setNewValue(getText());
                return;
            default :
                super.copyNewValueForRedo(e);
                return;
        }
    }

    public void changeValue(GraphicViewerDocumentChangedEdit e, boolean undo) {
        switch (e.getFlags()) {
            case ChangedText :
                setText((String) e.getValue(undo));
                return;
            default :
                super.changeValue(e, undo);
                return;
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class GraphicViewerEditorPane extends JEditorPane {

        private static final long serialVersionUID = -2940311865857831200L;

        GraphicViewerEditorPane(String t, String s) {
            super(t, s);
        }

        public void scrollRectToVisible(Rectangle r) {
        }
    }
}