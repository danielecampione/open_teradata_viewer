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

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface DomNode {

    public abstract DomNode appendChild(DomNode domnode);

    public abstract boolean isElement();

    public abstract DomElement elementCast();

    public abstract DomNode getFirstChild();

    public abstract DomElement getFirstChildElement();

    public abstract DomText getFirstChildText();

    public abstract DomNode getNextSibling();

    public abstract DomElement getNextSiblingElement();

    public abstract DomElement getNextSiblingGraphicViewerClassElement();

    public abstract DomText getNextSiblingText();

    public abstract DomNode getParentNode();
}