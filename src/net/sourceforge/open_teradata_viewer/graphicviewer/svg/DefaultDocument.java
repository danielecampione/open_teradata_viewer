/*
 * Open Teradata Viewer ( graphic viewer svg )
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

package net.sourceforge.open_teradata_viewer.graphicviewer.svg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sourceforge.open_teradata_viewer.graphicviewer.DomCDATASection;
import net.sourceforge.open_teradata_viewer.graphicviewer.DomDoc;
import net.sourceforge.open_teradata_viewer.graphicviewer.DomElement;
import net.sourceforge.open_teradata_viewer.graphicviewer.DomNode;
import net.sourceforge.open_teradata_viewer.graphicviewer.DomText;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerArea;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerDocument;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerEllipse;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerImage;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerLayer;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerObject;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerRectangle;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerStroke;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerText;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerXMLSaveRestore;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DefaultDocument implements DomDoc {

    public DefaultDocument() {
        _fldnull = true;
        _fldvoid = true;
        b = true;
        _fldint = false;
        resetDocument();
    }

    @SuppressWarnings("rawtypes")
    public void resetDocument() {
        try {
            DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentbuilder = documentbuilderfactory
                    .newDocumentBuilder();
            _fldgoto = documentbuilder.newDocument();
            _fldbyte = new HashMap();
            _fldlong = new Vector();
            _fldchar = new Vector();
            _fldcase = new Vector();
            _fldif = new Vector();
            _flddo = new Vector();
            _fldtry = new Vector();
            _fldelse = new HashMap();
            _fldfor = 3D;
            a = false;
        } catch (Exception exception) {
        }
    }

    public DomElement createElement(String s) {
        return new DefaultElement(_fldgoto.createElement(s));
    }

    public DomText createText(String s) {
        return new DefaultText(_fldgoto.createTextNode(s));
    }

    public DomCDATASection createCDATASection(String s) {
        return new a(_fldgoto.createCDATASection(s));
    }

    private DomElement a(DomElement domelement) {
        DefaultElement defaultelement = (DefaultElement) domelement;
        Node node = _fldgoto.appendChild(defaultelement.getElement());
        Element element = (Element) node;
        DefaultElement defaultelement1 = new DefaultElement(element);
        return defaultelement1;
    }

    private void a(Writer writer) {
        try {
            TransformerFactory transformerfactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerfactory.newTransformer();
            transformer.setOutputProperty("method", "xml");
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("omit-xml-declaration", "no");
            transformer.transform(new DOMSource(_fldgoto), new StreamResult(
                    writer));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void a(Reader reader) {
        try {
            DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory
                    .newInstance();
            documentbuilderfactory.setNamespaceAware(true);
            DocumentBuilder documentbuilder = documentbuilderfactory
                    .newDocumentBuilder();
            _fldgoto = documentbuilder.parse(new InputSource(reader));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public DomElement getDocumentElement() {
        Element element = _fldgoto.getDocumentElement();
        return new DefaultElement(element);
    }

    @SuppressWarnings("unchecked")
    public void registerObject(Object obj, DomElement domelement) {
        _fldbyte.put(obj, domelement);
    }

    private DomElement a(Object obj) {
        return (DomElement) _fldbyte.get(obj);
    }

    @SuppressWarnings("unchecked")
    public void registerReferencingNode(DomElement domelement, String s,
            Object obj) {
        _fldlong.addElement(domelement);
        _fldchar.addElement(s);
        _fldcase.addElement(obj);
    }

    public boolean isRegisteredReference(Object obj) {
        for (int i = 0; i < _fldcase.size(); i++) {
            Object obj1 = _fldcase.elementAt(i);
            if (obj1 == obj)
                return true;
        }

        return false;
    }

    private DomElement _mthnew(int i) {
        if (i >= _fldlong.size())
            return null;
        else
            return (DomElement) _fldlong.get(i);
    }

    private String _mthfor(int i) {
        return (String) _fldchar.get(i);
    }

    private Object _mthif(int i) {
        return _fldcase.get(i);
    }

    @SuppressWarnings("unchecked")
    public void registerTag(String s, Object obj) {
        _fldelse.put(s, obj);
    }

    private Object a(String s) {
        return _fldelse.get(s);
    }

    @SuppressWarnings("unchecked")
    public void registerReferencingObject(Object obj, String s, String s1) {
        if (s1.length() <= 0) {
            return;
        } else {
            _fldtry.addElement(obj);
            _flddo.addElement(s);
            _fldif.addElement(s1);
            return;
        }
    }

    private Object _mthdo(int i) {
        if (i >= _fldtry.size())
            return null;
        else
            return _fldtry.get(i);
    }

    private String _mthint(int i) {
        return (String) _flddo.get(i);
    }

    private String a(int i) {
        return (String) _fldif.get(i);
    }

    public void SVGWriteDoc(OutputStream outputstream,
            GraphicViewerDocument graphicViewerDocument) {
        BufferedWriter bufferedwriter = new BufferedWriter(
                new OutputStreamWriter(outputstream));
        SVGWriteDoc(((Writer) (bufferedwriter)), graphicViewerDocument);
    }

    public void SVGWriteDoc(Writer writer,
            GraphicViewerDocument graphicViewerDocument) {
        resetDocument();
        buildSVGDoc(graphicViewerDocument);
        a(writer);
    }

    public void SVGReadDoc(InputStream inputstream,
            GraphicViewerDocument graphicViewerDocument) throws Exception {
        BufferedReader bufferedreader = new BufferedReader(
                new InputStreamReader(inputstream));
        SVGReadDoc(((Reader) (bufferedreader)), graphicViewerDocument);
    }

    public void SVGReadDoc(Reader reader,
            GraphicViewerDocument graphicViewerDocument) throws Exception {
        graphicViewerDocument.deleteContents();
        a(reader);
        buildGraphicViewerDoc(graphicViewerDocument);
        graphicViewerDocument
                .removeLayer(graphicViewerDocument.getFirstLayer());
    }

    public void buildSVGDoc(GraphicViewerDocument graphicViewerDocument) {
        DomElement domelement = null;
        if (isGenerateSVG()) {
            domelement = createElement("svg");
            domelement.setAttribute("xmlns", "http://www.w3.org/2000/svg");
        } else {
            domelement = createElement("xml");
        }
        if (isGenerateGraphicViewerXML()) {
            domelement.setAttribute("xmlns:graphicviewerxml",
                    "http://sourceforge.net/graphicviewerxml");
            domelement.setAttribute("graphicviewersvgversion",
                    Double.toString(_fldfor));
        }
        domelement.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
        a(domelement);
        if (isGenerateGraphicViewerXML()) {
            DomElement domelement1 = createElement("g");
            domelement.appendChild(domelement1);
            graphicViewerDocument.SVGWriteObject(this, domelement1);
        }
        DomElement domelement2 = domelement;
        for (GraphicViewerLayer graphicviewerlayer = graphicViewerDocument
                .getFirstLayer(); graphicviewerlayer != null; graphicviewerlayer = graphicViewerDocument
                .getNextLayer(graphicviewerlayer)) {
            if (isGenerateGraphicViewerXML()) {
                domelement2 = createElement("g");
                domelement.appendChild(domelement2);
            }
            graphicViewerDocument.SVGWriteLayer(this, domelement2,
                    graphicviewerlayer);
        }

        int i = 1;
        int j = 0;
        do {
            DomElement domelement3 = _mthnew(j);
            if (domelement3 != null) {
                String s = _mthfor(j);
                Object obj = _mthif(j);
                DomElement domelement4 = a(obj);
                String s1 = "null";
                if (domelement4 != null) {
                    s1 = domelement4.getAttribute("id");
                    if (s1.length() == 0) {
                        s1 = "graphicviewerid" + Integer.toString(i++);
                        domelement4.setAttribute("id", s1);
                    }
                }
                String s2 = domelement3.getAttribute(s);
                if (s2.length() == 0)
                    domelement3.setAttribute(s, s1);
                else
                    domelement3.setAttribute(s, s2 + " " + s1);
                j++;
            } else {
                GenerateTooltipInitialization(domelement);
                GenerateTooltipScript(domelement);
                RenderTooltip(domelement);
                return;
            }
        } while (true);
    }

    public void buildGraphicViewerDoc(
            GraphicViewerDocument graphicViewerDocument) {
        DomElement domelement = getDocumentElement();
        String s = domelement.getAttribute("graphicviewersvgversion");
        if (s.length() > 0) {
            _fldfor = Double.parseDouble(domelement
                    .getAttribute("graphicviewersvgversion"));
            if (_fldfor > 3D) {
                System.out
                        .println("Warning: attempting to read unknown future version GraphicViewer SVG.");
                System.out
                        .println("This application supports version 3.0 and earlier.");
            }
        }
        SVGTraverseChildren(graphicViewerDocument, domelement, null, true);
        @SuppressWarnings("unused")
        boolean flag = true;
        int i = 0;
        do {
            Object obj = _mthdo(i);
            if (obj != null) {
                String s1 = _mthint(i);
                String s2 = a(i);
                a(s1, s2, obj);
                i++;
            } else {
                return;
            }
        } while (true);
    }

    private void a(String s, String s1, Object obj) {
        if (obj instanceof GraphicViewerObject) {
            GraphicViewerObject graphicViewerObject = (GraphicViewerObject) obj;
            graphicViewerObject.SVGUpdateReference(s, a(s1));
        }
    }

    public void SVGTraverseChildren(
            GraphicViewerDocument graphicViewerDocument, DomNode domnode,
            GraphicViewerArea graphicViewerArea, boolean flag) {
        for (DomNode domnode1 = domnode.getFirstChild(); domnode1 != null;)
            try {
                domnode1 = a(graphicViewerDocument, domnode1,
                        graphicViewerArea, flag);
            } catch (Exception exception) {
                exception.printStackTrace();
                domnode1 = domnode.getNextSibling();
            }

    }

    private DomNode a(GraphicViewerDocument graphicViewerDocument,
            DomNode domnode, GraphicViewerArea graphicViewerArea, boolean flag) {
        if (domnode.isElement()) {
            DomElement domelement = domnode.elementCast();
            DomElement domelement1 = null;
            String s = domelement.getLocalName();
            String s1 = "";
            if (s.equalsIgnoreCase("g")) {
                DomNode domnode1 = domnode.getFirstChild();
                domnode1 = domnode1.getNextSibling();
                if (domnode1 != null && domnode1.isElement()) {
                    domelement1 = domnode1.elementCast();
                    s1 = domelement1.getLocalName();
                }
            }
            return SVGReadElement(graphicViewerDocument, s, domelement, s1,
                    domelement1, graphicViewerArea, flag);
        } else {
            return domnode.getNextSibling();
        }
    }

    @SuppressWarnings("rawtypes")
    public DomNode SVGReadElement(
            GraphicViewerDocument paramGraphicViewerDocument,
            String paramString1, DomElement paramDomElement1,
            String paramString2, DomElement paramDomElement2,
            GraphicViewerArea paramGraphicViewerArea, boolean paramBoolean) {
        Object localObject1 = null;
        DomNode localDomNode1 = null;
        if (paramString2.equals("GraphicViewerClass")) {
            Object localObject2 = null;
            String str = paramDomElement2.getAttribute("class");
            try {
                if (getGraphicViewerSVGVersion() < 2.0D) {
                    if (str.equals("net.sourceforge.open_teradata_viewer.graphicviewer.TextNode"))
                        str = "net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerTextNode";
                    if (str.equals("net.sourceforge.open_teradata_viewer.graphicviewer.BasicNode"))
                        str = "net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerBasicNode";
                    if (str.equals("net.sourceforge.open_teradata_viewer.graphicviewer.IconicNode"))
                        str = "net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerIconicNode";
                    if (str.equals("net.sourceforge.open_teradata_viewer.graphicviewer.IconicNodePort"))
                        return SVGReadElement(paramGraphicViewerDocument,
                                paramString1, paramDomElement1,
                                paramDomElement2.getNextSiblingElement()
                                        .getLocalName(),
                                paramDomElement2.getNextSiblingElement(),
                                paramGraphicViewerArea, paramBoolean);
                    if (str.equals("net.sourceforge.open_teradata_viewer.graphicviewer.BasicNodePort"))
                        return SVGReadElement(paramGraphicViewerDocument,
                                paramString1, paramDomElement1,
                                paramDomElement2.getNextSiblingElement()
                                        .getLocalName(),
                                paramDomElement2.getNextSiblingElement(),
                                paramGraphicViewerArea, paramBoolean);
                    if (str.equals("net.sourceforge.open_teradata_viewer.graphicviewer.SubGraph"))
                        str = "net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerSubGraph";
                }
                Class localClass = Class.forName(str);
                localObject2 = localClass.newInstance();
            } catch (Exception localException) {
                localException.printStackTrace();
            }
            if (localObject2 != null)
                if (localObject2 instanceof GraphicViewerObject) {
                    localObject1 = (GraphicViewerObject) localObject2;
                    localDomNode1 = ((GraphicViewerObject) localObject1)
                            .SVGReadObject(this, paramGraphicViewerDocument,
                                    paramDomElement1, paramDomElement2);
                } else {
                    DomNode localDomNode2 = initializeObjectFromXML(
                            paramGraphicViewerDocument, paramDomElement1,
                            paramDomElement2, localObject2);
                    if (localDomNode2 != null)
                        return localDomNode2;
                }
        } else {
            if (paramString1.equalsIgnoreCase("g"))
                SVGTraverseChildren(paramGraphicViewerDocument,
                        paramDomElement1, null, true);
            if (isReadSVG())
                if (paramString1.equalsIgnoreCase("rect")) {
                    localObject1 = new GraphicViewerRectangle();
                    localDomNode1 = ((GraphicViewerObject) localObject1)
                            .SVGReadObject(this, paramGraphicViewerDocument,
                                    paramDomElement1, null);
                } else if (paramString1.equalsIgnoreCase("ellipse")) {
                    localObject1 = new GraphicViewerEllipse();
                    localDomNode1 = ((GraphicViewerObject) localObject1)
                            .SVGReadObject(this, paramGraphicViewerDocument,
                                    paramDomElement1, null);
                } else if (paramString1.equalsIgnoreCase("circle")) {
                    localObject1 = new GraphicViewerEllipse();
                    localDomNode1 = ((GraphicViewerObject) localObject1)
                            .SVGReadObject(this, paramGraphicViewerDocument,
                                    paramDomElement1, null);
                } else if (paramString1.equalsIgnoreCase("text")) {
                    localObject1 = new GraphicViewerText();
                    localDomNode1 = ((GraphicViewerObject) localObject1)
                            .SVGReadObject(this, paramGraphicViewerDocument,
                                    paramDomElement1, null);
                } else if (paramString1.equalsIgnoreCase("line")) {
                    localObject1 = new GraphicViewerStroke();
                    localDomNode1 = ((GraphicViewerObject) localObject1)
                            .SVGReadObject(this, paramGraphicViewerDocument,
                                    paramDomElement1, null);
                } else if (paramString1.equalsIgnoreCase("image")) {
                    localObject1 = new GraphicViewerImage();
                    localDomNode1 = ((GraphicViewerObject) localObject1)
                            .SVGReadObject(this, paramGraphicViewerDocument,
                                    paramDomElement1, null);
                }
        }
        if (localObject1 != null) {
            if (paramGraphicViewerArea != null)
                paramGraphicViewerArea
                        .addObjectAtTail((GraphicViewerObject) localObject1);
            else if (paramBoolean)
                if (this._fldnew != null)
                    this._fldnew
                            .addObjectAtTail((GraphicViewerObject) localObject1);
                else
                    paramGraphicViewerDocument
                            .addObjectAtTail((GraphicViewerObject) localObject1);
            return localDomNode1;
        }
        return ((DomNode) paramDomElement1.getNextSibling());
    }

    public DomNode initializeObjectFromXML(
            GraphicViewerDocument graphicViewerDocument, DomElement domelement,
            DomElement domelement1, Object obj) {
        if (obj instanceof GraphicViewerLayer) {
            _fldnew = graphicViewerDocument.addLayerAfter(null);
            graphicViewerDocument.SVGReadLayer(this, graphicViewerDocument,
                    domelement, domelement1, _fldnew);
            return domelement.getNextSiblingElement();
        }
        if (obj instanceof GraphicViewerXMLSaveRestore) {
            GraphicViewerXMLSaveRestore graphicViewerXMLSaveRestore = (GraphicViewerXMLSaveRestore) obj;
            graphicViewerXMLSaveRestore.SVGReadObject(this,
                    graphicViewerDocument, domelement, domelement1);
            return domelement.getNextSiblingElement();
        } else {
            return null;
        }
    }

    public DomElement createGraphicViewerClassElement(String s,
            DomElement domelement) {
        DomElement domelement1 = createElement("graphicviewerxml:GraphicViewerClass");
        domelement1.setAttribute("class", s);
        domelement.appendChild(domelement1);
        return domelement1;
    }

    public void setDisabledDrawing(boolean flag) {
        a = flag;
    }

    public boolean isDisabledDrawing() {
        return a;
    }

    public double getGraphicViewerSVGVersion() {
        return _fldfor;
    }

    public void setGenerateGraphicViewerXML(boolean flag) {
        _fldnull = flag;
    }

    public boolean isGenerateGraphicViewerXML() {
        return _fldnull;
    }

    public void setGenerateSVG(boolean flag) {
        _fldvoid = flag;
    }

    public boolean isGenerateSVG() {
        return _fldvoid;
    }

    public void setReadSVG(boolean flag) {
        _fldint = flag;
    }

    public boolean isReadSVG() {
        return _fldint;
    }

    public void setSVGTooltips(boolean flag) {
        b = flag;
    }

    public boolean isSVGTooltips() {
        return b;
    }

    public boolean SVGOutputEnabled() {
        return isGenerateSVG() && !isDisabledDrawing();
    }

    public boolean GraphicViewerXMLOutputEnabled() {
        return isGenerateGraphicViewerXML();
    }

    public Node importNode(Node node, boolean flag) throws DOMException {
        return _fldgoto.importNode(node, flag);
    }

    public Document getDocument() {
        return _fldgoto;
    }

    protected void GenerateTooltipInitialization(DomElement domelement) {
        if (!isGenerateSVG() || !isSVGTooltips()) {
            return;
        } else {
            domelement.setAttribute("contentScriptType", "text/ecmascript");
            domelement.setAttribute("onload",
                    "AddTitleEvents(GetSvgDoc(evt).getDocumentElement())");
            return;
        }
    }

    protected void RenderTooltip(DomElement domelement) {
        if (!isGenerateSVG() || !isSVGTooltips()) {
            return;
        } else {
            DomElement domelement1 = createElement("g");
            domelement1.setAttribute("id", "GoSvgTooltip");
            domelement1.setAttribute("display", "none");
            domelement1.setAttribute("onmouseover", "ShowTooltip(evt)");
            domelement1.setAttribute("onmouseout", "HideTooltip(evt)");
            domelement.appendChild(domelement1);
            DomElement domelement2 = createElement("rect");
            domelement2.setAttribute("id", "GoSvgTooltipRect");
            domelement2.setAttribute("fill", "rgb(255,255,220)");
            domelement2.setAttribute("stroke", "rgb(0,0,0)");
            domelement2.setAttribute("height", "20");
            domelement1.appendChild(domelement2);
            DomElement domelement3 = createElement("text");
            domelement3.setAttribute("id", "GoSvgTooltipText");
            domelement3.setAttribute("x", "5");
            domelement3.setAttribute("y", "14");
            domelement1.appendChild(domelement3);
            DomText domtext = createText("ttt");
            domelement3.appendChild(domtext);
            return;
        }
    }

    protected void GenerateTooltipScript(DomElement domelement) {
        if (!isGenerateSVG() || !isSVGTooltips()) {
            return;
        } else {
            String s = "";
            s = s + "\nvar goLastTitle = null;";
            s = s + "\nfunction ShowTooltip(e) {";
            s = s + "\n  var d = GetSvgDoc(e);";
            s = s + "\n  var tt = d.getElementById('GoSvgTooltip');";
            s = s + "\n  if (tt != null) {";
            s = s + "\n    var title = TitleElementOf(e.getCurrentTarget());";
            s = s + "\n    if (title != null) {";
            s = s
                    + "\n      var ttText = d.getElementById('GoSvgTooltipText');";
            s = s + "\n      if (ttText != null && goLastTitle != title) {";
            s = s + "\n        goLastTitle = title;";
            s = s
                    + "\n        var newtextnode = d.createTextNode(TextOf(title));";
            s = s
                    + "\n        ttText.replaceChild(newtextnode, ttText.getFirstChild());";
            s = s
                    + "\n        var ttRect = d.getElementById('GoSvgTooltipRect');";
            s = s + "\n        if (ttRect != null) {";
            s = s
                    + "\n          ttRect.setAttribute('width', ttText.getComputedTextLength() + 2*ttText.getAttribute('x'));";
            s = s + "\n        }";
            s = s + "\n        var newy = e.getClientY()-20;";
            s = s
                    + "\n        tt.setAttribute('transform', 'translate(' + e.getClientX() + ',' + newy + ')');";
            s = s + "\n      }";
            s = s + "\n    }";
            s = s + "\n    tt.setAttribute('display', 'inherit')";
            s = s + "\n  }";
            s = s + "\n}";
            s = s + "\nfunction HideTooltip(e) {";
            s = s + "\n  var d = GetSvgDoc(e);";
            s = s + "\n  var tt = d.getElementById('GoSvgTooltip');";
            s = s + "\n  if (tt != null) {";
            s = s + "\n    tt.setAttribute('display', 'none')";
            s = s + "\n  }";
            s = s + "\n}";
            s = s + "\nfunction GetSvgDoc(e) {";
            s = s + "\n  var t = e.getTarget();";
            s = s + "\n  if (t.getNodeType() != 9)";
            s = s + "\n    return t.getOwnerDocument();";
            s = s + "\n  else";
            s = s + "\n    return t;";
            s = s + "\n}";
            s = s + "\nfunction AddTitleEvents(elt) {";
            s = s + "\n  var c = elt.getChildNodes();";
            s = s + "\n  for (var i = 0; i < c.getLength(); i++) {";
            s = s + "\n    if (c.item(i).getNodeType() == 1)";
            s = s + "\n      AddTitleEvents(c.item(i));";
            s = s + "\n  }";
            s = s + "\n  if (TitleElementOf(elt) != null) {";
            s = s
                    + "\n    elt.addEventListener('mouseover', ShowTooltip, false);";
            s = s
                    + "\n    elt.addEventListener('mouseout', HideTooltip, false);";
            s = s + "\n  }";
            s = s + "\n}";
            s = s + "\nfunction TitleElementOf(elt) {";
            s = s + "\n  var c = elt.getChildNodes();";
            s = s + "\n  for (var i = 0; i < c.getLength(); i++)";
            s = s
                    + "\n    if (c.item(i).getNodeType() == 1 && c.item(i).getNodeName() == 'title')";
            s = s + "\n      return c.item(i);";
            s = s + "\n  return null;";
            s = s + "\n}";
            s = s + "\nfunction TextOf(elt) {";
            s = s + "\n  var c = elt.getChildNodes();";
            s = s + "\n  for (var i = 0; c && i < c.getLength(); i++)";
            s = s + "\n    if (c.item(i).getNodeType() == 3)";
            s = s + "\n      return c.item(i).getNodeValue();";
            s = s + "\n  return '';";
            s = s + "\n}";
            DomElement domelement1 = createElement("script");
            domelement.appendChild(domelement1);
            DomText domtext = createText(s);
            domelement1.appendChild(domtext);
            return;
        }
    }

    private Document _fldgoto;
    private GraphicViewerLayer _fldnew;
    private double _fldfor;
    private boolean a;
    private boolean _fldnull;
    private boolean _fldvoid;
    private boolean _fldint;
    private boolean b;
    @SuppressWarnings("rawtypes")
    private HashMap _fldbyte;
    @SuppressWarnings("rawtypes")
    private Vector _fldlong;
    @SuppressWarnings("rawtypes")
    private Vector _fldchar;
    @SuppressWarnings("rawtypes")
    private Vector _fldcase;
    @SuppressWarnings("rawtypes")
    private HashMap _fldelse;
    @SuppressWarnings("rawtypes")
    private Vector _fldtry;
    @SuppressWarnings("rawtypes")
    private Vector _flddo;
    @SuppressWarnings("rawtypes")
    private Vector _fldif;
}