/*
 * Open Teradata Viewer ( graphic viewer svg )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer.svg;

import net.sourceforge.open_teradata_viewer.graphic_viewer.IDomElement;
import net.sourceforge.open_teradata_viewer.graphic_viewer.IDomList;
import net.sourceforge.open_teradata_viewer.graphic_viewer.IDomNode;
import net.sourceforge.open_teradata_viewer.graphic_viewer.IDomText;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class AbstractNode implements IDomNode {

    private Node myNode;

    public AbstractNode(Node node) {
        myNode = node;
    }

    public Node getNode() {
        return myNode;
    }

    public IDomNode appendChild(IDomNode domnode) {
        AbstractNode abstractnode = (AbstractNode) domnode;
        AbstractNode abstractnode1 = this;
        Node node = abstractnode1.getNode().appendChild(abstractnode.getNode());
        AbstractNode abstractnode2 = new AbstractNode(node);
        return abstractnode2;
    }

    public boolean isElement() {
        return myNode.getNodeType() == 1;
    }

    public IDomElement elementCast() {
        if (getNode().getNodeType() == 1) {
            Element element = (Element) getNode();
            return new DefaultElement(element);
        } else {
            return null;
        }
    }

    private IDomList getChildNodes() {
        NodeList localNodeList = myNode.getChildNodes();
        return new DefaultList(localNodeList);
    }

    public IDomNode getFirstChild() {
        Node node = myNode.getFirstChild();
        if (node == null) {
            return null;
        } else {
            return new AbstractNode(node);
        }
    }

    public IDomElement getFirstChildElement() {
        for (Node node = myNode.getFirstChild(); node != null; node = node
                .getNextSibling()) {
            if (node.getNodeType() == 1) {
                return new DefaultElement((Element) node);
            }
        }

        return null;
    }

    public IDomText getFirstChildText() {
        for (Node node = myNode.getFirstChild(); node != null; node = node
                .getNextSibling()) {
            if (node.getNodeType() == 3) {
                return new DefaultText((Text) node);
            }
        }

        return null;
    }

    public IDomNode getParentNode() {
        Node node = myNode.getParentNode();
        if (node == null) {
            return null;
        } else {
            return new AbstractNode(node);
        }
    }

    public IDomNode getNextSibling() {
        Node node = myNode.getNextSibling();
        if (node == null) {
            return null;
        } else {
            return new AbstractNode(node);
        }
    }

    public IDomElement getNextSiblingElement() {
        for (Node node = myNode.getNextSibling(); node != null; node = node
                .getNextSibling()) {
            if (node.getNodeType() == 1) {
                return new DefaultElement((Element) node);
            }
        }

        return null;
    }

    public IDomElement getNextSiblingGraphicViewerClassElement() {
        for (Node node = myNode.getNextSibling(); node != null; node = node
                .getNextSibling()) {
            if (node.getNodeType() != 1) {
                continue;
            }
            Element element = (Element) node;
            if (element.getLocalName().equals("GraphicViewerClass")) {
                return new DefaultElement(element);
            }
        }

        return null;
    }

    public IDomText getNextSiblingText() {
        for (Node node = myNode.getNextSibling(); node != null; node = node
                .getNextSibling()) {
            if (node.getNodeType() == 3) {
                return new DefaultText((Text) node);
            }
        }

        return null;
    }
}