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

package net.sourceforge.open_teradata_viewer.graphic_viewer.svg;


import net.sourceforge.open_teradata_viewer.graphic_viewer.IDomElement;

import org.w3c.dom.Element;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DefaultElement extends AbstractNode implements IDomElement {

    public DefaultElement(Element element) {
        super(element);
    }

    public Element getElement() {
        return (Element) getNode();
    }

    public void setAttribute(String s, String s1) {
        getElement().setAttribute(s, s1);
    }

    public String getAttribute(String s) {
        return getElement().getAttribute(s);
    }

    public String getSubAttribute(String s, String s1) {
        int i = s.indexOf(s1 + ":");
        if (i == -1)
            return "";
        i = s.indexOf(":", i) + 1;
        int j = s.indexOf(";", i);
        String s2 = "";
        if (j == -1)
            s2 = s.substring(i);
        else
            s2 = s.substring(i, j);
        return s2;
    }

    public String getLocalName() {
        return getElement().getLocalName();
    }

    public String getNamespaceURI() {
        return getElement().getNamespaceURI();
    }

    public String getPrefix() {
        return getElement().getPrefix();
    }

    public String getTagName() {
        return getElement().getTagName();
    }
}