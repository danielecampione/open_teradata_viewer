/*
 * Open Teradata Viewer ( graphic viewer svg )
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

package net.sourceforge.open_teradata_viewer.graphicviewer.svg;


import net.sourceforge.open_teradata_viewer.graphicviewer.DomElement;
import net.sourceforge.open_teradata_viewer.graphicviewer.DomList;
import net.sourceforge.open_teradata_viewer.graphicviewer.DomNode;
import net.sourceforge.open_teradata_viewer.graphicviewer.DomText;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class AbstractNode implements DomNode {

    public AbstractNode(Node node) {
        a = node;
    }

    public Node getNode() {
        return a;
    }

    public DomNode appendChild(DomNode domnode) {
        AbstractNode abstractnode = (AbstractNode) domnode;
        AbstractNode abstractnode1 = this;
        Node node = abstractnode1.getNode().appendChild(abstractnode.getNode());
        AbstractNode abstractnode2 = new AbstractNode(node);
        return abstractnode2;
    }

    public boolean isElement() {
        return a.getNodeType() == 1;
    }

    public DomElement elementCast() {
        if (getNode().getNodeType() == 1) {
            Element element = (Element) getNode();
            return new DefaultElement(element);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unused")
    private DomList a() {
        org.w3c.dom.NodeList nodelist = a.getChildNodes();
        return new DefaultList(nodelist);
    }

    public DomNode getFirstChild() {
        Node node = a.getFirstChild();
        if (node == null)
            return null;
        else
            return new AbstractNode(node);
    }

    public DomElement getFirstChildElement() {
        for (Node node = a.getFirstChild(); node != null; node = node
                .getNextSibling())
            if (node.getNodeType() == 1)
                return new DefaultElement((Element) node);

        return null;
    }

    public DomText getFirstChildText() {
        for (Node node = a.getFirstChild(); node != null; node = node
                .getNextSibling())
            if (node.getNodeType() == 3)
                return new DefaultText((Text) node);

        return null;
    }

    public DomNode getParentNode() {
        Node node = a.getParentNode();
        if (node == null)
            return null;
        else
            return new AbstractNode(node);
    }

    public DomNode getNextSibling() {
        Node node = a.getNextSibling();
        if (node == null)
            return null;
        else
            return new AbstractNode(node);
    }

    public DomElement getNextSiblingElement() {
        for (Node node = a.getNextSibling(); node != null; node = node
                .getNextSibling())
            if (node.getNodeType() == 1)
                return new DefaultElement((Element) node);

        return null;
    }

    public DomElement getNextSiblingGraphicViewerClassElement() {
        for (Node node = a.getNextSibling(); node != null; node = node
                .getNextSibling()) {
            if (node.getNodeType() != 1)
                continue;
            Element element = (Element) node;
            if (element.getLocalName().equals("GraphicViewerClass"))
                return new DefaultElement(element);
        }

        return null;
    }

    public DomText getNextSiblingText() {
        for (Node node = a.getNextSibling(); node != null; node = node
                .getNextSibling())
            if (node.getNodeType() == 3)
                return new DefaultText((Text) node);

        return null;
    }

    private Node a;
}