/*
 * Open Teradata Viewer ( graphic viewer svg )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer.svg;

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

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerArea;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerDocument;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerEllipse;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerImage;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerLayer;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerObject;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerRectangle;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerStroke;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerText;
import net.sourceforge.open_teradata_viewer.graphic_viewer.IDomCDATASection;
import net.sourceforge.open_teradata_viewer.graphic_viewer.IDomDoc;
import net.sourceforge.open_teradata_viewer.graphic_viewer.IDomElement;
import net.sourceforge.open_teradata_viewer.graphic_viewer.IDomNode;
import net.sourceforge.open_teradata_viewer.graphic_viewer.IDomText;
import net.sourceforge.open_teradata_viewer.graphic_viewer.IGraphicViewerXMLSaveRestore;

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
public class DefaultDocument implements IDomDoc {

    private Document myDoc;
    private GraphicViewerLayer myCurrentLayer;
    private double myGraphicViewerSVGVersion;
    private boolean myDisabledDrawing;
    private boolean myGenerateGraphicViewerXML = true;
    private boolean myGenerateSVG = true;
    private boolean myReadSVG = false;
    private boolean mySVGTooltips = true;
    private HashMap myGraphicViewerObjectToSVGNodeMap;
    private Vector myReferencingNode;
    private Vector myReferenceName;
    private Vector myReferencedObj;
    private HashMap myTagToGraphicViewerObjectMap;
    private Vector myReferencingObject;
    private Vector myReferencingAttr;
    private Vector myReferencedTag;

    public DefaultDocument() {
        resetDocument();
    }

    public void resetDocument() {
        try {
            DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentbuilder = documentbuilderfactory
                    .newDocumentBuilder();
            myDoc = documentbuilder.newDocument();
            myGraphicViewerObjectToSVGNodeMap = new HashMap();
            myReferencingNode = new Vector();
            myReferenceName = new Vector();
            myReferencedObj = new Vector();
            myReferencedTag = new Vector();
            myReferencingAttr = new Vector();
            myReferencingObject = new Vector();
            myTagToGraphicViewerObjectMap = new HashMap();
            myGraphicViewerSVGVersion = 3D;
            myDisabledDrawing = false;
        } catch (Exception e) {
            ExceptionDialog.ignoreException(e);
        }
    }

    public IDomElement createElement(String s) {
        return new DefaultElement(myDoc.createElement(s));
    }

    public IDomText createText(String s) {
        return new DefaultText(myDoc.createTextNode(s));
    }

    public IDomCDATASection createCDATASection(String s) {
        return new DefaultCDATASection(myDoc.createCDATASection(s));
    }

    private IDomElement appendChild(IDomElement domelement) {
        DefaultElement defaultelement = (DefaultElement) domelement;
        Node node = myDoc.appendChild(defaultelement.getElement());
        Element element = (Element) node;
        DefaultElement defaultelement1 = new DefaultElement(element);
        return defaultelement1;
    }

    private void writeFile(Writer writer) {
        try {
            TransformerFactory transformerfactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerfactory.newTransformer();
            transformer.setOutputProperty("method", "xml");
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("omit-xml-declaration", "no");
            transformer.transform(new DOMSource(myDoc),
                    new StreamResult(writer));
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
    }

    private void readFile(Reader reader) {
        try {
            DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory
                    .newInstance();
            documentbuilderfactory.setNamespaceAware(true);
            DocumentBuilder documentbuilder = documentbuilderfactory
                    .newDocumentBuilder();
            myDoc = documentbuilder.parse(new InputSource(reader));
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
    }

    public IDomElement getDocumentElement() {
        Element element = myDoc.getDocumentElement();
        return new DefaultElement(element);
    }

    public void registerObject(Object obj, IDomElement domelement) {
        myGraphicViewerObjectToSVGNodeMap.put(obj, domelement);
    }

    private IDomElement getNode(Object obj) {
        return (IDomElement) myGraphicViewerObjectToSVGNodeMap.get(obj);
    }

    public void registerReferencingNode(IDomElement domelement, String s,
            Object obj) {
        myReferencingNode.addElement(domelement);
        myReferenceName.addElement(s);
        myReferencedObj.addElement(obj);
    }

    public boolean isRegisteredReference(Object obj) {
        for (int i = 0; i < myReferencedObj.size(); i++) {
            Object obj1 = myReferencedObj.elementAt(i);
            if (obj1 == obj) {
                return true;
            }
        }

        return false;
    }

    private IDomElement getReferencingNode(int i) {
        if (i >= myReferencingNode.size()) {
            return null;
        } else {
            return (IDomElement) myReferencingNode.get(i);
        }
    }

    private String getReferenceName(int i) {
        return (String) myReferenceName.get(i);
    }

    private Object getReferencedObject(int i) {
        return myReferencedObj.get(i);
    }

    public void registerTag(String s, Object obj) {
        myTagToGraphicViewerObjectMap.put(s, obj);
    }

    private Object getRegisteredTagObj(String s) {
        return myTagToGraphicViewerObjectMap.get(s);
    }

    public void registerReferencingObject(Object obj, String s, String s1) {
        if (s1.length() <= 0) {
            return;
        } else {
            myReferencingObject.addElement(obj);
            myReferencingAttr.addElement(s);
            myReferencedTag.addElement(s1);
            return;
        }
    }

    private Object getReferencingObject(int i) {
        if (i >= myReferencingObject.size()) {
            return null;
        } else {
            return myReferencingObject.get(i);
        }
    }

    private String getReferencingAttr(int i) {
        return (String) myReferencingAttr.get(i);
    }

    private String getReferencedTag(int i) {
        return (String) myReferencedTag.get(i);
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
        writeFile(writer);
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
        readFile(reader);
        buildGraphicViewerDoc(graphicViewerDocument);
        graphicViewerDocument
                .removeLayer(graphicViewerDocument.getFirstLayer());
    }

    public void buildSVGDoc(GraphicViewerDocument graphicViewerDocument) {
        IDomElement domelement = null;
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
                    Double.toString(myGraphicViewerSVGVersion));
        }
        domelement.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
        appendChild(domelement);
        if (isGenerateGraphicViewerXML()) {
            IDomElement domelement1 = createElement("g");
            domelement.appendChild(domelement1);
            graphicViewerDocument.SVGWriteObject(this, domelement1);
        }
        IDomElement domelement2 = domelement;
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
            IDomElement domelement3 = getReferencingNode(j);
            if (domelement3 != null) {
                String s = getReferenceName(j);
                Object obj = getReferencedObject(j);
                IDomElement domelement4 = getNode(obj);
                String s1 = "null";
                if (domelement4 != null) {
                    s1 = domelement4.getAttribute("id");
                    if (s1.length() == 0) {
                        s1 = "graphicviewerid" + Integer.toString(i++);
                        domelement4.setAttribute("id", s1);
                    }
                }
                String s2 = domelement3.getAttribute(s);
                if (s2.length() == 0) {
                    domelement3.setAttribute(s, s1);
                } else {
                    domelement3.setAttribute(s, s2 + " " + s1);
                }
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
        IDomElement domelement = getDocumentElement();
        String s = domelement.getAttribute("graphicviewersvgversion");
        if (s.length() > 0) {
            myGraphicViewerSVGVersion = Double.parseDouble(domelement
                    .getAttribute("graphicviewersvgversion"));
            if (myGraphicViewerSVGVersion > 3D) {
                ApplicationFrame
                        .getInstance()
                        .getConsole()
                        .println(
                                "Warning: attempting to read unknown future version GraphicViewer SVG.");
                ApplicationFrame
                        .getInstance()
                        .getConsole()
                        .println(
                                "This application supports version 3.0 and earlier.");
            }
        }
        SVGTraverseChildren(graphicViewerDocument, domelement, null, true);
        int i = 0;
        do {
            Object obj = getReferencingObject(i);
            if (obj != null) {
                String s1 = getReferencingAttr(i);
                String s2 = getReferencedTag(i);
                SVGUpdateReferences(s1, s2, obj);
                i++;
            } else {
                return;
            }
        } while (true);
    }

    private void SVGUpdateReferences(String s, String s1, Object obj) {
        if (obj instanceof GraphicViewerObject) {
            GraphicViewerObject graphicViewerObject = (GraphicViewerObject) obj;
            graphicViewerObject.SVGUpdateReference(s, getRegisteredTagObj(s1));
        }
    }

    public void SVGTraverseChildren(
            GraphicViewerDocument graphicViewerDocument, IDomNode domnode,
            GraphicViewerArea graphicViewerArea, boolean flag) {
        for (IDomNode domnode1 = domnode.getFirstChild(); domnode1 != null;) {
            try {
                domnode1 = SVGVisitNode(graphicViewerDocument, domnode1,
                        graphicViewerArea, flag);
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
                domnode1 = domnode.getNextSibling();
            }
        }
    }

    private IDomNode SVGVisitNode(GraphicViewerDocument graphicViewerDocument,
            IDomNode domnode, GraphicViewerArea graphicViewerArea, boolean flag) {
        if (domnode.isElement()) {
            IDomElement domelement = domnode.elementCast();
            IDomElement domelement1 = null;
            String s = domelement.getLocalName();
            String s1 = "";
            if (s.equalsIgnoreCase("g")) {
                IDomNode domnode1 = domnode.getFirstChild();
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

    public IDomNode SVGReadElement(
            GraphicViewerDocument paramGraphicViewerDocument,
            String paramString1, IDomElement paramDomElement1,
            String paramString2, IDomElement paramDomElement2,
            GraphicViewerArea paramGraphicViewerArea, boolean paramBoolean) {
        Object localObject1 = null;
        IDomNode localDomNode1 = null;
        if (paramString2.equals("GraphicViewerClass")) {
            Object localObject2 = null;
            String str = paramDomElement2.getAttribute("class");
            try {
                if (getGraphicViewerSVGVersion() < 2.0D) {
                    if (str.equals("net.sourceforge.open_teradata_viewer.graphic_viewer.TextNode")) {
                        str = "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerTextNode";
                    }
                    if (str.equals("net.sourceforge.open_teradata_viewer.graphic_viewer.BasicNode")) {
                        str = "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerBasicNode";
                    }
                    if (str.equals("net.sourceforge.open_teradata_viewer.graphic_viewer.IconicNode")) {
                        str = "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerIconicNode";
                    }
                    if (str.equals("net.sourceforge.open_teradata_viewer.graphic_viewer.IconicNodePort")) {
                        return SVGReadElement(paramGraphicViewerDocument,
                                paramString1, paramDomElement1,
                                paramDomElement2.getNextSiblingElement()
                                        .getLocalName(),
                                paramDomElement2.getNextSiblingElement(),
                                paramGraphicViewerArea, paramBoolean);
                    }
                    if (str.equals("net.sourceforge.open_teradata_viewer.graphic_viewer.BasicNodePort")) {
                        return SVGReadElement(paramGraphicViewerDocument,
                                paramString1, paramDomElement1,
                                paramDomElement2.getNextSiblingElement()
                                        .getLocalName(),
                                paramDomElement2.getNextSiblingElement(),
                                paramGraphicViewerArea, paramBoolean);
                    }
                    if (str.equals("net.sourceforge.open_teradata_viewer.graphic_viewer.SubGraph")) {
                        str = "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerSubGraph";
                    }
                }
                Class localClass = Class.forName(str);
                localObject2 = localClass.newInstance();
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }
            if (localObject2 != null) {
                if (localObject2 instanceof GraphicViewerObject) {
                    localObject1 = (GraphicViewerObject) localObject2;
                    localDomNode1 = ((GraphicViewerObject) localObject1)
                            .SVGReadObject(this, paramGraphicViewerDocument,
                                    paramDomElement1, paramDomElement2);
                } else {
                    IDomNode localDomNode2 = initializeObjectFromXML(
                            paramGraphicViewerDocument, paramDomElement1,
                            paramDomElement2, localObject2);
                    if (localDomNode2 != null) {
                        return localDomNode2;
                    }
                }
            }
        } else {
            if (paramString1.equalsIgnoreCase("g")) {
                SVGTraverseChildren(paramGraphicViewerDocument,
                        paramDomElement1, null, true);
            }
            if (isReadSVG()) {
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
        }
        if (localObject1 != null) {
            if (paramGraphicViewerArea != null) {
                paramGraphicViewerArea
                        .addObjectAtTail((GraphicViewerObject) localObject1);
            } else if (paramBoolean) {
                if (this.myCurrentLayer != null) {
                    this.myCurrentLayer
                            .addObjectAtTail((GraphicViewerObject) localObject1);
                } else {
                    paramGraphicViewerDocument
                            .addObjectAtTail((GraphicViewerObject) localObject1);
                }
            }
            return localDomNode1;
        }
        return ((IDomNode) paramDomElement1.getNextSibling());
    }

    public IDomNode initializeObjectFromXML(
            GraphicViewerDocument graphicViewerDocument,
            IDomElement domelement, IDomElement domelement1, Object obj) {
        if (obj instanceof GraphicViewerLayer) {
            myCurrentLayer = graphicViewerDocument.addLayerAfter(null);
            graphicViewerDocument.SVGReadLayer(this, graphicViewerDocument,
                    domelement, domelement1, myCurrentLayer);
            return domelement.getNextSiblingElement();
        }
        if (obj instanceof IGraphicViewerXMLSaveRestore) {
            IGraphicViewerXMLSaveRestore iGraphicViewerXMLSaveRestore = (IGraphicViewerXMLSaveRestore) obj;
            iGraphicViewerXMLSaveRestore.SVGReadObject(this,
                    graphicViewerDocument, domelement, domelement1);
            return domelement.getNextSiblingElement();
        } else {
            return null;
        }
    }

    public IDomElement createGraphicViewerClassElement(String s,
            IDomElement domelement) {
        IDomElement domelement1 = createElement("graphicviewerxml:GraphicViewerClass");
        domelement1.setAttribute("class", s);
        domelement.appendChild(domelement1);
        return domelement1;
    }

    public void setDisabledDrawing(boolean flag) {
        myDisabledDrawing = flag;
    }

    public boolean isDisabledDrawing() {
        return myDisabledDrawing;
    }

    public double getGraphicViewerSVGVersion() {
        return myGraphicViewerSVGVersion;
    }

    public void setGenerateGraphicViewerXML(boolean flag) {
        myGenerateGraphicViewerXML = flag;
    }

    public boolean isGenerateGraphicViewerXML() {
        return myGenerateGraphicViewerXML;
    }

    public void setGenerateSVG(boolean flag) {
        myGenerateSVG = flag;
    }

    public boolean isGenerateSVG() {
        return myGenerateSVG;
    }

    public void setReadSVG(boolean flag) {
        myReadSVG = flag;
    }

    public boolean isReadSVG() {
        return myReadSVG;
    }

    public void setSVGTooltips(boolean flag) {
        mySVGTooltips = flag;
    }

    public boolean isSVGTooltips() {
        return mySVGTooltips;
    }

    public boolean SVGOutputEnabled() {
        return isGenerateSVG() && !isDisabledDrawing();
    }

    public boolean GraphicViewerXMLOutputEnabled() {
        return isGenerateGraphicViewerXML();
    }

    public Node importNode(Node node, boolean flag) throws DOMException {
        return myDoc.importNode(node, flag);
    }

    public Document getDocument() {
        return myDoc;
    }

    protected void GenerateTooltipInitialization(IDomElement domelement) {
        if (!isGenerateSVG() || !isSVGTooltips()) {
            return;
        } else {
            domelement.setAttribute("contentScriptType", "text/ecmascript");
            domelement.setAttribute("onload",
                    "AddTitleEvents(GetSvgDoc(evt).getDocumentElement())");
            return;
        }
    }

    protected void RenderTooltip(IDomElement domelement) {
        if (!isGenerateSVG() || !isSVGTooltips()) {
            return;
        } else {
            IDomElement domelement1 = createElement("g");
            domelement1.setAttribute("id", "GoSvgTooltip");
            domelement1.setAttribute("display", "none");
            domelement1.setAttribute("onmouseover", "ShowTooltip(evt)");
            domelement1.setAttribute("onmouseout", "HideTooltip(evt)");
            domelement.appendChild(domelement1);
            IDomElement domelement2 = createElement("rect");
            domelement2.setAttribute("id", "GoSvgTooltipRect");
            domelement2.setAttribute("fill", "rgb(255,255,220)");
            domelement2.setAttribute("stroke", "rgb(0,0,0)");
            domelement2.setAttribute("height", "20");
            domelement1.appendChild(domelement2);
            IDomElement domelement3 = createElement("text");
            domelement3.setAttribute("id", "GoSvgTooltipText");
            domelement3.setAttribute("x", "5");
            domelement3.setAttribute("y", "14");
            domelement1.appendChild(domelement3);
            IDomText domtext = createText("ttt");
            domelement3.appendChild(domtext);
            return;
        }
    }

    protected void GenerateTooltipScript(IDomElement domelement) {
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
            IDomElement domelement1 = createElement("script");
            domelement.appendChild(domelement1);
            IDomText domtext = createText(s);
            domelement1.appendChild(domtext);
            return;
        }
    }
}