/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphicviewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerImage extends GraphicViewerObject
        implements
            ImageObserver {

    private static final long serialVersionUID = -4747867706811080201L;

    public GraphicViewerImage() {
        b3 = null;
        b7 = null;
        b4 = null;
        b8 = null;
        b2 = null;
    }

    public GraphicViewerImage(Point point, Dimension dimension) {
        super(point, dimension);
        b3 = null;
        b7 = null;
        b4 = null;
        b8 = null;
        b2 = null;
    }

    public GraphicViewerImage(Rectangle rectangle) {
        super(rectangle);
        b3 = null;
        b7 = null;
        b4 = null;
        b8 = null;
        b2 = null;
    }

    public GraphicViewerObject copyObject(
            GraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerImage graphicviewerimage = (GraphicViewerImage) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewerimage != null) {
            graphicviewerimage.b3 = b3;
            if (b7 != null)
                graphicviewerimage.b7 = new Dimension(b7);
            graphicviewerimage.b4 = b4;
            graphicviewerimage.b8 = b8;
            graphicviewerimage.b2 = b2;
        }
        return graphicviewerimage;
    }

    public boolean loadImage(Image image, boolean flag) {
        setImage(image);
        return true;
    }

    @SuppressWarnings("unchecked")
    public Image getImage(URL url) {
        if (url == null)
            return null;
        Image image = (Image) getImageMap().get(url);
        if (image == null) {
            image = GraphicViewerGlobal.getToolkit().createImage(url);
            if (image != null)
                getImageMap().put(url, image);
        }
        return image;
    }

    @SuppressWarnings("unchecked")
    public Image getImage(String s) {
        if (s == null)
            return null;
        Image image = (Image) getImageMap().get(s);
        if (image == null) {
            image = GraphicViewerGlobal.getToolkit().createImage(s);
            if (image != null)
                getImageMap().put(s, image);
        }
        return image;
    }

    public static void resetImage(URL url) {
        getImageMap().remove(url);
    }

    public static void resetImage(String s) {
        getImageMap().remove(s);
    }

    public static void resetAllImages() {
        getImageMap().clear();
    }

    boolean _mthdo(boolean flag) {
        if (flag) {
            MediaTracker mediatracker = new MediaTracker(
                    GraphicViewerGlobal.getComponent());
            mediatracker.addImage(b3, 0);
            try {
                mediatracker.waitForID(0);
            } catch (InterruptedException interruptedexception) {
                return false;
            }
            return !mediatracker.isErrorID(0);
        } else {
            return true;
        }
    }

    public boolean loadImage(URL url, boolean flag) {
        URL url1 = b4;
        String s = b8;
        if (url1 == null || !url1.equals(url)) {
            b4 = url;
            b8 = null;
            if (url1 != null)
                update(904, 0, url1);
            else
                update(904, 0, s);
        }
        b7 = null;
        b3 = getImage(url);
        return _mthdo(flag);
    }

    public boolean loadImage(String s, boolean flag) {
        URL url = b4;
        String s1 = b8;
        if (s1 == null || !s1.equals(s)) {
            b4 = null;
            b8 = s;
            if (s1 != null)
                update(905, 0, s1);
            else
                update(905, 0, url);
        }
        Image image = null;
        if (getDefaultBase() != null) {
            URL url1 = null;
            try {
                url1 = new URL(getDefaultBase(), s);
            } catch (Exception exception) {
            }
            if (url1 != null)
                image = getImage(url1);
        }
        if (image == null)
            image = getImage(s);
        b7 = null;
        b3 = image;
        return _mthdo(flag);
    }

    public void setImage(Image image) {
        Image image1 = getImage();
        if (image1 != image) {
            b3 = image;
            b7 = null;
            update(901, 0, image1);
        }
    }

    public Image getImage() {
        return b3;
    }

    public URL getURL() {
        return b4;
    }

    public String getFilename() {
        return b8;
    }

    public Dimension getNaturalSize() {
        if (b7 == null)
            b7 = new Dimension(-1, -1);
        Image image = getImage();
        if (image != null && (b7.width < 0 || b7.height < 0)) {
            b7.width = image.getWidth(this);
            b7.height = image.getHeight(this);
        }
        return b7;
    }

    public Color getTransparentColor() {
        return b2;
    }

    public void setTransparentColor(Color color) {
        Color color1 = b2;
        if (color1 == null) {
            if (color != null) {
                b2 = color;
                update(903, 0, color1);
            }
        } else if (!color1.equals(color)) {
            b2 = color;
            update(903, 0, color1);
        }
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 901 :
                graphicviewerdocumentchangededit.setNewValue(getImage());
                return;

            case 903 :
                graphicviewerdocumentchangededit
                        .setNewValue(getTransparentColor());
                return;

            case 904 :
            case 905 :
                if (getURL() != null)
                    graphicviewerdocumentchangededit.setNewValue(getURL());
                else
                    graphicviewerdocumentchangededit.setNewValue(getFilename());
                return;

            case 902 :
            default :
                super.copyNewValueForRedo(graphicviewerdocumentchangededit);
                return;
        }
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 901 :
                setImage((Image) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 903 :
                setTransparentColor((Color) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 904 :
            case 905 :
                if (flag) {
                    if (graphicviewerdocumentchangededit.getOldValue() instanceof URL)
                        loadImage(
                                (URL) graphicviewerdocumentchangededit
                                        .getOldValue(),
                                false);
                    else if (graphicviewerdocumentchangededit.getOldValue() instanceof String)
                        loadImage(
                                (String) graphicviewerdocumentchangededit
                                        .getOldValue(),
                                false);
                } else if (graphicviewerdocumentchangededit.getNewValue() instanceof URL)
                    loadImage(
                            (URL) graphicviewerdocumentchangededit
                                    .getNewValue(),
                            false);
                else if (graphicviewerdocumentchangededit.getNewValue() instanceof String)
                    loadImage(
                            (String) graphicviewerdocumentchangededit
                                    .getNewValue(),
                            false);
                return;

            case 902 :
            default :
                super.changeValue(graphicviewerdocumentchangededit, flag);
                return;
        }
    }

    public void SVGWriteObject(DomDoc domdoc, DomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            DomElement domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerImage",
                    domelement);
            if (getFilename() != null)
                domelement1.setAttribute("imagefilename", getFilename());
            if (getURL() != null)
                domelement1.setAttribute("imageurl", getURL().toString());
            if (getTransparentColor() != null)
                domelement1.setAttribute("imagetransparentcolor",
                        Integer.toString(getTransparentColor().getRGB()));
        }
        if (domdoc.SVGOutputEnabled()) {
            DomElement domelement2 = domdoc.createElement("image");
            SVGWriteAttributes(domelement2);
            domelement.appendChild(domelement2);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public DomNode SVGReadObject(DomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, DomElement domelement,
            DomElement domelement1) {
        try {
            if (domelement1 != null) {
                if (domelement1.getAttribute("imagefilename").length() > 0)
                    loadImage(domelement1.getAttribute("imagefilename"), false);
                if (domelement1.getAttribute("imageurl").length() > 0)
                    loadImage(new URL(domelement1.getAttribute("imageurl")),
                            false);
                if (domelement1.getAttribute("imagetransparentcolor").length() > 0) {
                    int i = Integer.parseInt(domelement1
                            .getAttribute("imagetransparentcolor"));
                    setTransparentColor(new Color(i));
                }
                super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                        domelement1.getNextSiblingGraphicViewerClassElement());
            } else if (domelement.getTagName().equalsIgnoreCase("image"))
                SVGReadAttributes(domelement);
        } catch (Exception exception) {
        }
        return domelement.getNextSibling();
    }

    public void SVGWriteAttributes(DomElement domelement) {
        super.SVGWriteAttributes(domelement);
        domelement.setAttribute("x", Integer.toString(getTopLeft().x));
        domelement.setAttribute("y", Integer.toString(getTopLeft().y));
        domelement.setAttribute("width", Integer.toString(getWidth()));
        domelement.setAttribute("height", Integer.toString(getHeight()));
        if (getURL() != null)
            domelement.setAttribute("xlink:href", getURL().toString());
        else if (getFilename() != null) {
            String s = getFilename();
            if (getDefaultBase() != null) {
                String s1 = getDefaultBase().toString();
                domelement.setAttribute("xlink:href", s1 + s);
            } else {
                domelement.setAttribute("xlink:href", s);
            }
        }
    }

    public void SVGReadAttributes(DomElement domelement) {
        super.SVGReadAttributes(domelement);
        String s = domelement.getAttribute("x");
        String s1 = domelement.getAttribute("y");
        setTopLeft(new Point(Integer.parseInt(s), Integer.parseInt(s1)));
        String s2 = domelement.getAttribute("width");
        String s3 = domelement.getAttribute("height");
        setWidth(Integer.parseInt(s2));
        setHeight(Integer.parseInt(s3));
        String s4 = domelement.getAttribute("xlink:href");
        if (s4.length() > 0)
            try {
                loadImage(new URL(s4), false);
            } catch (MalformedURLException malformedurlexception) {
            }
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        if (getImage() == null)
            if (getURL() != null)
                loadImage(getURL(), false);
            else if (getFilename() != null)
                loadImage(getFilename(), false);
        if (getImage() != null) {
            Rectangle rectangle = getBoundingRect();
            if (getTransparentColor() == null)
                graphics2d.drawImage(getImage(), rectangle.x, rectangle.y,
                        rectangle.width, rectangle.height, this);
            else
                graphics2d.drawImage(getImage(), rectangle.x, rectangle.y,
                        rectangle.width, rectangle.height,
                        getTransparentColor(), this);
        }
    }

    public boolean imageUpdate(Image image, int i, int j, int k, int l, int i1) {
        if ((i & 0x20) != 0) {
            update();
            return false;
        }
        if ((i & 0x10) != 0) {
            try {
                Thread.sleep(10L);
            } catch (InterruptedException interruptedexception) {
                update();
                return false;
            }
            update();
            return true;
        } else {
            return true;
        }
    }

    public static URL getDefaultBase() {
        return b5;
    }

    public static void setDefaultBase(URL url) {
        b5 = url;
    }

    @SuppressWarnings("rawtypes")
    public static HashMap getImageMap() {
        return b6;
    }

    public static final int ChangedImage = 901;
    public static final int ChangedTransparentColor = 903;
    public static final int ChangedURL = 904;
    public static final int ChangedFilename = 905;
    private static URL b5 = null;
    @SuppressWarnings("rawtypes")
    private static HashMap b6 = new HashMap();
    private transient Image b3;
    private transient Dimension b7;
    private URL b4;
    private String b8;
    private Color b2;

}