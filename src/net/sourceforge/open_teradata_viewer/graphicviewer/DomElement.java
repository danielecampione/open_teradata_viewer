/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2011, D. Campione
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
public interface DomElement extends DomNode {

    public abstract void setAttribute(String s, String s1);

    public abstract String getAttribute(String s);

    public abstract String getSubAttribute(String s, String s1);

    public abstract String getTagName();

    public abstract String getLocalName();

    public abstract String getNamespaceURI();

    public abstract String getPrefix();
}