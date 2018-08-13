/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2013, D. Campione
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

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

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

    public static final int ChangedImage = 901;
    public static final int ChangedTransparentColor = 903;
    public static final int ChangedURL = 904;
    public static final int ChangedFilename = 905;
    private static URL myDefaultBase = null;
    private static HashMap myImageMap = new HashMap();
    private transient Image myImage = null;
    private transient Dimension myNaturalSize = null;
    private URL myURL = null;
    private String myFilename = null;
    private Color myTransparentColor = null;

    public GraphicViewerImage() {
    }

    public GraphicViewerImage(Point point, Dimension dimension) {
        super(point, dimension);
    }

    public GraphicViewerImage(Rectangle rectangle) {
        super(rectangle);
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerImage graphicviewerimage = (GraphicViewerImage) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewerimage != null) {
            graphicviewerimage.myImage = myImage;
            if (myNaturalSize != null) {
                graphicviewerimage.myNaturalSize = new Dimension(myNaturalSize);
            }
            graphicviewerimage.myURL = myURL;
            graphicviewerimage.myFilename = myFilename;
            graphicviewerimage.myTransparentColor = myTransparentColor;
        }
        return graphicviewerimage;
    }

    public boolean loadImage(Image image, boolean flag) {
        setImage(image);
        return true;
    }

    public Image getImage(URL url) {
        if (url == null) {
            return null;
        }
        Image image = (Image) getImageMap().get(url);
        if (image == null) {
            image = GraphicViewerGlobal.getToolkit().createImage(url);
            if (image != null) {
                getImageMap().put(url, image);
            }
        }
        return image;
    }

    public Image getImage(String s) {
        if (s == null) {
            return null;
        }
        Image image = (Image) getImageMap().get(s);
        if (image == null) {
            image = GraphicViewerGlobal.getToolkit().createImage(s);
            if (image != null) {
                getImageMap().put(s, image);
            }
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

    boolean waitForImage(boolean flag) {
        if (flag) {
            MediaTracker mediatracker = new MediaTracker(
                    GraphicViewerGlobal.getComponent());
            mediatracker.addImage(myImage, 0);
            try {
                mediatracker.waitForID(0);
            } catch (InterruptedException ie) {
                return false;
            }
            return !mediatracker.isErrorID(0);
        } else {
            return true;
        }
    }

    public boolean loadImage(URL url, boolean flag) {
        URL url1 = myURL;
        String s = myFilename;
        if (url1 == null || !url1.equals(url)) {
            myURL = url;
            myFilename = null;
            if (url1 != null) {
                update(904, 0, url1);
            } else {
                update(904, 0, s);
            }
        }
        myNaturalSize = null;
        myImage = getImage(url);
        return waitForImage(flag);
    }

    public boolean loadImage(String s, boolean flag) {
        URL url = myURL;
        String s1 = myFilename;
        if (s1 == null || !s1.equals(s)) {
            myURL = null;
            myFilename = s;
            if (s1 != null) {
                update(905, 0, s1);
            } else {
                update(905, 0, url);
            }
        }
        Image image = null;
        if (getDefaultBase() != null) {
            URL url1 = null;
            try {
                url1 = new URL(getDefaultBase(), s);
            } catch (Exception e) {
                ExceptionDialog.ignoreException(e);
            }
            if (url1 != null) {
                image = getImage(url1);
            }
        }
        if (image == null) {
            image = getImage(s);
        }
        myNaturalSize = null;
        myImage = image;
        return waitForImage(flag);
    }

    public void setImage(Image image) {
        Image image1 = getImage();
        if (image1 != image) {
            myImage = image;
            myNaturalSize = null;
            update(901, 0, image1);
        }
    }

    public Image getImage() {
        return myImage;
    }

    public URL getURL() {
        return myURL;
    }

    public String getFilename() {
        return myFilename;
    }

    public Dimension getNaturalSize() {
        if (myNaturalSize == null) {
            myNaturalSize = new Dimension(-1, -1);
        }
        Image image = getImage();
        if (image != null
                && (myNaturalSize.width < 0 || myNaturalSize.height < 0)) {
            myNaturalSize.width = image.getWidth(this);
            myNaturalSize.height = image.getHeight(this);
        }
        return myNaturalSize;
    }

    public Color getTransparentColor() {
        return myTransparentColor;
    }

    public void setTransparentColor(Color color) {
        Color color1 = myTransparentColor;
        if (color1 == null) {
            if (color != null) {
                myTransparentColor = color;
                update(903, 0, color1);
            }
        } else if (!color1.equals(color)) {
            myTransparentColor = color;
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
                if (getURL() != null) {
                    graphicviewerdocumentchangededit.setNewValue(getURL());
                } else {
                    graphicviewerdocumentchangededit.setNewValue(getFilename());
                }
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
                    if (graphicviewerdocumentchangededit.getOldValue() instanceof URL) {
                        loadImage(
                                (URL) graphicviewerdocumentchangededit
                                        .getOldValue(),
                                false);
                    } else if (graphicviewerdocumentchangededit.getOldValue() instanceof String) {
                        loadImage(
                                (String) graphicviewerdocumentchangededit
                                        .getOldValue(),
                                false);
                    }
                } else if (graphicviewerdocumentchangededit.getNewValue() instanceof URL) {
                    loadImage(
                            (URL) graphicviewerdocumentchangededit
                                    .getNewValue(),
                            false);
                } else if (graphicviewerdocumentchangededit.getNewValue() instanceof String) {
                    loadImage(
                            (String) graphicviewerdocumentchangededit
                                    .getNewValue(),
                            false);
                }
                return;

            case 902 :
            default :
                super.changeValue(graphicviewerdocumentchangededit, flag);
                return;
        }
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerImage",
                            domelement);
            if (getFilename() != null) {
                domelement1.setAttribute("imagefilename", getFilename());
            }
            if (getURL() != null) {
                domelement1.setAttribute("imageurl", getURL().toString());
            }
            if (getTransparentColor() != null) {
                domelement1.setAttribute("imagetransparentcolor",
                        Integer.toString(getTransparentColor().getRGB()));
            }
        }
        if (domdoc.SVGOutputEnabled()) {
            IDomElement domelement2 = domdoc.createElement("image");
            SVGWriteAttributes(domelement2);
            domelement.appendChild(domelement2);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        try {
            if (domelement1 != null) {
                if (domelement1.getAttribute("imagefilename").length() > 0) {
                    loadImage(domelement1.getAttribute("imagefilename"), false);
                }
                if (domelement1.getAttribute("imageurl").length() > 0) {
                    loadImage(new URL(domelement1.getAttribute("imageurl")),
                            false);
                }
                if (domelement1.getAttribute("imagetransparentcolor").length() > 0) {
                    int i = Integer.parseInt(domelement1
                            .getAttribute("imagetransparentcolor"));
                    setTransparentColor(new Color(i));
                }
                super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                        domelement1.getNextSiblingGraphicViewerClassElement());
            } else if (domelement.getTagName().equalsIgnoreCase("image")) {
                SVGReadAttributes(domelement);
            }
        } catch (Exception e) {
            ExceptionDialog.ignoreException(e);
        }
        return domelement.getNextSibling();
    }

    public void SVGWriteAttributes(IDomElement domelement) {
        super.SVGWriteAttributes(domelement);
        domelement.setAttribute("x", Integer.toString(getTopLeft().x));
        domelement.setAttribute("y", Integer.toString(getTopLeft().y));
        domelement.setAttribute("width", Integer.toString(getWidth()));
        domelement.setAttribute("height", Integer.toString(getHeight()));
        if (getURL() != null) {
            domelement.setAttribute("xlink:href", getURL().toString());
        } else if (getFilename() != null) {
            String s = getFilename();
            if (getDefaultBase() != null) {
                String s1 = getDefaultBase().toString();
                domelement.setAttribute("xlink:href", s1 + s);
            } else {
                domelement.setAttribute("xlink:href", s);
            }
        }
    }

    public void SVGReadAttributes(IDomElement domelement) {
        super.SVGReadAttributes(domelement);
        String s = domelement.getAttribute("x");
        String s1 = domelement.getAttribute("y");
        setTopLeft(new Point(Integer.parseInt(s), Integer.parseInt(s1)));
        String s2 = domelement.getAttribute("width");
        String s3 = domelement.getAttribute("height");
        setWidth(Integer.parseInt(s2));
        setHeight(Integer.parseInt(s3));
        String s4 = domelement.getAttribute("xlink:href");
        if (s4.length() > 0) {
            try {
                loadImage(new URL(s4), false);
            } catch (MalformedURLException murle) {
                ExceptionDialog.ignoreException(murle);
            }
        }
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        if (getImage() == null) {
            if (getURL() != null) {
                loadImage(getURL(), false);
            } else if (getFilename() != null) {
                loadImage(getFilename(), false);
            }
        }
        if (getImage() != null) {
            Rectangle rectangle = getBoundingRect();
            if (getTransparentColor() == null) {
                graphics2d.drawImage(getImage(), rectangle.x, rectangle.y,
                        rectangle.width, rectangle.height, this);
            } else {
                graphics2d.drawImage(getImage(), rectangle.x, rectangle.y,
                        rectangle.width, rectangle.height,
                        getTransparentColor(), this);
            }
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
            } catch (InterruptedException ie) {
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
        return myDefaultBase;
    }

    public static void setDefaultBase(URL url) {
        myDefaultBase = url;
    }

    public static HashMap getImageMap() {
        return myImageMap;
    }
}