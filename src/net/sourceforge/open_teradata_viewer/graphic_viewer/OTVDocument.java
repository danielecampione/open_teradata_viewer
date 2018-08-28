/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.LinkedList;

import javax.swing.JFileChooser;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.UISupport;
import net.sourceforge.open_teradata_viewer.graphic_viewer.svg.DefaultDocument;

/**
 * 
 * 
 * @author D. Campione
 *
 */
// Implement the Graphic Viewer document model data structure container.
// This class is responsible for loading and storing Graphic Viewer documents to
// files, and for creating activities (nodes) and flows (links).
//
// OTVDocument has just a few properties:
// Name, Location, Orthogonal Flows and Link Pen.
// The latter two may appear to belong to a view instead of being part of a
// document, but here these attributes can be conveniently stored persistently.
public class OTVDocument extends GraphicViewerDocument {

    private static final long serialVersionUID = -2151570261461793379L;

    // Event hints
    public static final int NameChanged = GraphicViewerDocumentEvent.LAST + 1;
    public static final int LocationChanged = GraphicViewerDocumentEvent.LAST + 2;

    // State
    private GraphicViewerPen myPen = GraphicViewerPen.make(
            GraphicViewerPen.SOLID, 2, Color.black);

    private String myName = "";
    private String myLocation = "";
    private static boolean showArrows = true;
    private int myVersion = 1;

    private boolean changed = false;

    private transient boolean myIsLocationModifiable = true;
    private transient boolean myIsModified = false;

    /** Ctor. */
    public OTVDocument() {
    }

    public String getName() {
        return myName;
    }

    public void setName(String newname) {
        String oldName = myName;
        if (!oldName.equals(newname)) {
            myName = newname;
            fireUpdate(NameChanged, 0, null, 0, oldName);
        }
    }

    public String getLocation() {
        return myLocation;
    }

    public void setLocation(String newloc) {
        String oldLocation = myLocation;
        if (!oldLocation.equals(newloc)) {
            myLocation = newloc;
            fireUpdate(LocationChanged, 0, null, 0, oldLocation);
        }
    }

    // Creating a new basicLayoutNode.
    public BasicLayoutNode newNode(Point p, String s, int numPorts,
            boolean horiz) {
        BasicLayoutNode snode = new BasicLayoutNode();
        snode.initialize(p, s, numPorts, horiz);
        addObjectAtTail(snode);
        return snode;
    }

    // creating a new link between layout nodes.
    public GraphicViewerLink newLink(GraphicViewerPort from,
            GraphicViewerPort to) {
        if (from.getParent() == to.getParent()) {
            return null;
        }
        GraphicViewerLink ll = new GraphicViewerLink(from, to);
        ll.setPen(getLinkPen());
        getLinksLayer().addObjectAtTail(ll);
        ll.setArrowHeads(false, isShowArrows());
        return ll;
    }

    public GraphicViewerPen getLinkPen() {
        return myPen;
    }

    public void setLinkPen(GraphicViewerPen p) {
        if (!myPen.equals(p)) {
            myPen = p;
            // Now update all links
            GraphicViewerListPosition pos = getFirstObjectPos();
            while (pos != null) {
                GraphicViewerObject obj = getObjectAtPos(pos);
                // Only consider top-level objects
                pos = getNextObjectPosAtTop(pos);
                if (obj instanceof GraphicViewerLink) {
                    GraphicViewerLink link = (GraphicViewerLink) obj;
                    link.setPen(p);
                }
            }
        }
    }

    // For this module, read and write Graphic Viewer documents as files using
    // an ascii format. The default serialization is not used, because any
    // changes to the GraphicViewerLink, BasicLayoutNode, or BasicLayoutPort
    // would cause previously saved files to become unaccessable.
    public static OTVDocument open(Component parent, String defaultLocation) {
        JFileChooser chooser = new JFileChooser();
        if ((defaultLocation != null) && (!defaultLocation.equals(""))) {
            File currentFile = new File(defaultLocation);
            chooser.setCurrentDirectory(currentFile);
        } else {
            chooser.setCurrentDirectory(null);
        }
        int returnVal = chooser.showOpenDialog(null);
        OTVDocument doc = new OTVDocument();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String loc = chooser.getSelectedFile().getAbsolutePath();
            RandomAccessFile fstream = null;
            try {
                fstream = new RandomAccessFile(loc, "r");
                // Changing the open procedure..
                int version = fstream.readInt();
                doc.myVersion = version;
                String title = fstream.readLine();
                doc.setName(title);
                doc.setLocation(loc);
                boolean b = fstream.readBoolean();
                if (!b) {
                    doc.toggleArrows();
                }
                boolean more = fstream.readBoolean();
                while (more) {
                    boolean isNode = fstream.readBoolean();
                    if (isNode) {
                        doc.readNode(fstream);
                    } else {
                        doc.readLink(fstream);
                    }
                    more = fstream.readBoolean();
                }
                doc.changed = false;
                return doc;
            } catch (IOException x) {
                UISupport.getDialogs().showErrorMessage(x.getMessage());
                return null;
            } catch (Exception x) {
                UISupport.getDialogs().showErrorMessage(x.getMessage());
                return null;
            } finally {
                try {
                    if (fstream != null) {
                        fstream.close();
                    }
                } catch (Exception x) {
                }
            }
        } else {
            return null;
        }
    }

    public void save() {
        if (getLocation().equals("")) {
            saveAs();
        } else {
            store();
        }
    }

    public void store() {
        boolean oldchanged = changed;
        if (!getLocation().equals("")) {
            changed = false;
            RandomAccessFile fstream = null;
            try {
                fstream = new RandomAccessFile(getLocation(), "rw");
                fstream.setLength(0);
                fstream.writeInt(myVersion);
                fstream.writeBytes(getName() + "\r\n");
                fstream.writeBoolean(isShowArrows());
                // Now write out all of the nodes and links, starting with the
                // nodes
                GraphicViewerListPosition pos = getFirstObjectPos();
                LinkedList links = new LinkedList();
                while (pos != null) {
                    GraphicViewerObject obj = getObjectAtPos(pos);
                    pos = getNextObjectPosAtTop(pos);
                    if (obj instanceof BasicLayoutNode) {
                        BasicLayoutNode anode = (BasicLayoutNode) obj;
                        obj = null;
                        fstream.writeBoolean(true); // More objects to read
                        writeNode(anode, fstream);
                    } else if (obj instanceof GraphicViewerLink) {
                        // Add obj to linked list so that it can be added after
                        // the nodes
                        links.addLast(obj);
                    }
                }
                while (links.size() > 0) {
                    GraphicViewerLink ll = (GraphicViewerLink) links
                            .removeLast();
                    // Write ll to file
                    fstream.writeBoolean(true); // More objects to read
                    writeLink(ll, fstream);
                }
                fstream.writeBoolean(false); // No more object to read
            } catch (IOException x) {
                UISupport.getDialogs().showErrorMessage(x.getMessage());
                changed = oldchanged;
            } finally {
                try {
                    if (fstream != null) {
                        fstream.close();
                    }
                } catch (Exception x) {
                    changed = oldchanged;
                }
            }
        }
    }

    public void saveAs() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(null);
        int returnVal = chooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String name = chooser.getSelectedFile().getName();
            setName(name);
            String loc = chooser.getSelectedFile().getAbsolutePath();
            setLocation(loc);
            store();
        }
    }

    private void readNode(RandomAccessFile fstream) throws IOException {
        if (myVersion == 1) {
            boolean hasLabel = fstream.readBoolean();
            String label = null;
            if (hasLabel) {
                label = fstream.readLine();
            }
            char color = fstream.readChar();
            int numPorts = fstream.readInt();
            boolean hor = fstream.readBoolean();
            int x = fstream.readInt();
            int y = fstream.readInt();
            Point c = new Point(x, y);
            BasicLayoutNode anode = newNode(c, label, numPorts, hor);
            if (color == 'g') {
                anode.colorChange();
            } else if (color == 'b') {
                anode.colorChange();
                anode.colorChange();
            }
        }
    }

    private void readLink(RandomAccessFile fstream) throws IOException {
        if (myVersion == 1) {
            int numPoints = fstream.readInt();
            Point[] points = new Point[numPoints];
            for (int i = 0; i < numPoints; i++) {
                int x = fstream.readInt();
                int y = fstream.readInt();
                points[i] = new Point(x, y);
            }
            BasicLayoutPort fromPort = pickPort(points[0]);
            BasicLayoutPort toPort = pickPort(points[numPoints - 1]);
            GraphicViewerLink ll = newLink(fromPort, toPort);
            if (numPoints > 2) {
                points[0] = ll.getStartPoint();
                points[numPoints - 1] = ll.getEndPoint();
                ll.removeAllPoints();
                for (int i = 0; i < numPoints; i++) {
                    ll.addPoint(points[i]);
                }
            }
        }
    }

    private void writeNode(BasicLayoutNode anode, RandomAccessFile fstream)
            throws IOException {
        if (myVersion == 1) {
            fstream.writeBoolean(true); // Is a node
            if (anode.getLabel() != null) {
                fstream.writeBoolean(true); // There is a label
                fstream.writeBytes(anode.getLabel().getText() + "\r\n");
            } else {
                fstream.writeBoolean(false);
            } // No label
            if (anode.getColor() == Color.green) {
                fstream.writeChar('g');
            } else if (anode.getColor() == Color.blue) {
                fstream.writeChar('b');
            } else {
                fstream.writeChar('r');
            }
            fstream.writeInt(anode.getNumPorts());
            fstream.writeBoolean(anode.isHorizontal());
            Point center = anode.getSpotLocation(GraphicViewerObject.Center);
            fstream.writeInt(center.x);
            fstream.writeInt(center.y);
        }
    }

    private void writeLink(GraphicViewerLink ll, RandomAccessFile fstream)
            throws IOException {
        if (myVersion == 1) {
            fstream.writeBoolean(false); // Not a node (i.e. is a link)
            int numPoints = ll.getNumPoints();
            fstream.writeInt(numPoints);
            Point p = ll.getFromPort().getSpotLocation(
                    GraphicViewerObject.Center);
            fstream.writeInt(p.x);
            fstream.writeInt(p.y);
            for (int i = 1; i < numPoints - 1; i++) {
                p = ll.getPoint(i);
                fstream.writeInt(p.x);
                fstream.writeInt(p.y);
            }
            p = ll.getToPort().getSpotLocation(GraphicViewerObject.Center);
            fstream.writeInt(p.x);
            fstream.writeInt(p.y);
        }
    }

    public BasicLayoutPort pickPort(Point pointToCheck) {
        GraphicViewerListPosition pos = getLastObjectPos();
        while (pos != null) {
            GraphicViewerObject obj = getObjectAtPos(pos);
            pos = getPrevObjectPos(pos);

            if (obj.isVisible() && obj.isPointInObj(pointToCheck)) {
                if (obj instanceof GraphicViewerArea) {
                    // Handle inside area
                    GraphicViewerObject child = ((GraphicViewerArea) obj)
                            .pickObject(pointToCheck, false);
                    if (child != null) {
                        obj = child;
                    }
                }
                if (obj instanceof BasicLayoutPort) {
                    return (BasicLayoutPort) obj;
                }
            }
        }
        return null;
    }

    public boolean isShowArrows() {
        return showArrows;
    }

    public void toggleArrows() {
        showArrows = !showArrows;
        GraphicViewerListPosition pos = getFirstObjectPos();
        while (pos != null) {
            GraphicViewerObject obj = getObjectAtPos(pos);
            if (obj instanceof GraphicViewerLink) {
                ((GraphicViewerLink) obj).setArrowHeads(false, isShowArrows());
            }
            pos = getNextObjectPosAtTop(pos);
        }
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean b) {
        changed = b;
    }

    public void setModified(boolean b) {
        if (myIsModified != b) {
            myIsModified = b;
            // Don't need to notify document listeners
        }
    }

    // Read-only property--can the file be written?
    public boolean isLocationModifiable() {
        return myIsLocationModifiable; // Just return cached value
    }

    // There's no setLocationModifiable, because that's controlled externally
    // in the file system. But because we're caching the writableness, we need a
    // method to update the cache.
    public void updateLocationModifiable() {
        boolean canwrite = true;
        if (!getLocation().equals("")) {
            File file = new File(getLocation());
            if (file.exists() && !file.canWrite()) {
                canwrite = false;
            }
        }
        if (isLocationModifiable() != canwrite) {
            boolean oldIsModifiable = isModifiable();
            myIsLocationModifiable = canwrite;
            if (oldIsModifiable != isModifiable()) {
                fireUpdate(GraphicViewerDocumentEvent.MODIFIABLE_CHANGED, 0,
                        null, (oldIsModifiable ? 1 : 0), null);
            }
        }
    }

    public OTVDocument loadSVG1(InputStream ins) {
        OTVDocument doc = new OTVDocument();
        try {
            DefaultDocument svgDomDoc = new DefaultDocument();
            svgDomDoc.SVGReadDoc(ins, doc);
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
        return doc;
    }

    public void storeSVG1(OutputStream outs, boolean genXMLExtensions,
            boolean genSVG) {
        DefaultDocument svgDomDoc = new DefaultDocument();
        svgDomDoc.setGenerateGraphicViewerXML(genXMLExtensions);
        svgDomDoc.setGenerateSVG(genSVG);
        svgDomDoc.SVGWriteDoc(outs, this);
    }

    public void updatePaperColor() {
        if (isModifiable()) {
            setPaperColor(new Color(255, 255, 221));
        } else {
            setPaperColor(new Color(0xDD, 0xDD, 0xDD));
        }
    }
}