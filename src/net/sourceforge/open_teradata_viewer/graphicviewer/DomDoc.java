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
public interface DomDoc {

    public abstract double getGraphicViewerSVGVersion();

    public abstract DomElement createGraphicViewerClassElement(String s,
            DomElement domelement);

    public abstract DomElement createElement(String s);

    public abstract DomText createText(String s);

    public abstract DomCDATASection createCDATASection(String s);

    public abstract void registerObject(Object obj, DomElement domelement);

    public abstract void registerReferencingNode(DomElement domelement,
            String s, Object obj);

    public abstract boolean isRegisteredReference(Object obj);

    public abstract void registerTag(String s, Object obj);

    public abstract void registerReferencingObject(Object obj, String s,
            String s1);

    public abstract void SVGTraverseChildren(
            GraphicViewerDocument graphicviewerdocument, DomNode domnode,
            GraphicViewerArea graphicviewerarea, boolean flag);

    public abstract void setDisabledDrawing(boolean flag);

    public abstract boolean isDisabledDrawing();

    public abstract void setGenerateGraphicViewerXML(boolean flag);

    public abstract boolean isGenerateGraphicViewerXML();

    public abstract void setGenerateSVG(boolean flag);

    public abstract boolean isGenerateSVG();

    public abstract void setSVGTooltips(boolean flag);

    public abstract boolean isSVGTooltips();

    public abstract boolean GraphicViewerXMLOutputEnabled();

    public abstract boolean SVGOutputEnabled();
}