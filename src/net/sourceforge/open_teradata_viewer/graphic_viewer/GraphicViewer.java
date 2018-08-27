/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.Main;
import net.sourceforge.open_teradata_viewer.UISupport;
import net.sourceforge.open_teradata_viewer.graphic_viewer.layout.GraphicViewerLayeredDigraphAutoLayout;
import net.sourceforge.open_teradata_viewer.graphic_viewer.layout.GraphicViewerNetwork;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewer extends JFrame
        implements
            Runnable,
            IGraphicViewerViewListener,
            IGraphicViewerDocumentListener {

    private static final long serialVersionUID = 2780998114828445957L;

    private static GraphicViewer GRAPHIC_VIEWER;
    private int myDocCount;
    UndoMgr myUndoMgr;
    static final String graphicViewerSVGXML = "Graphic Viewer SVG with XML extensions (*.xhtml)";
    private String myDescription = "";
    public AppAction FileNewAction;
    public AppAction FileOpenAction;
    public AppAction FileSaveAsAction;
    public AppAction ToggleLogAction;
    public AppAction InsertStuffAction;
    public AppAction InsertRectangleAction;
    public AppAction InsertRoundedRectangleAction;
    public AppAction InsertEllipseAction;
    public AppAction InsertMultiPortNodeAction;
    public AppAction InsertSimpleNodeAction;
    public AppAction InsertIconicNodeAction;
    public AppAction InsertDraggableLabelIconicNodeAction;
    public AppAction InsertBasicNodeAction;
    public AppAction InsertCenterLabelBasicNodeAction;
    public AppAction InsertFixedSizeCenterLabelBasicNodeAction;
    public AppAction InsertRectBasicNodeAction;
    public AppAction InsertSelfLoopBasicNodeAction;
    public AppAction InsertMultiSpotNodeAction;
    public AppAction InsertGeneralNodeAction;
    public AppAction AddLeftPortAction;
    public AppAction AddRightPortAction;
    public AppAction InsertTextNodeAction;
    public AppAction InsertFixedSizeTextNodeAction;
    public AppAction InsertRoundTextNodeAction;
    public AppAction InsertMultiTextNodeAction;
    public AppAction InsertListAreaAction;
    public AppAction InsertRecordNodeAction;
    public AppAction InsertStrokeAction;
    public AppAction InsertPolygonAction;
    public AppAction InsertDiamondAction;
    public AppAction InsertTextAction;
    public AppAction Insert3DRectAction;
    public AppAction InsertCommentAction;
    public AppAction Insert10000Action;
    public AppAction InsertDrawingStrokeAction;
    public AppAction InsertGraphOfGraphsAction;
    public AppAction CutAction;
    public AppAction CopyAction;
    public AppAction PasteAction;
    public AppAction SelectAllAction;
    public AppAction MoveToFrontAction;
    public AppAction MoveToBackAction;
    public AppAction ChangeLayersAction;
    public AppAction GroupAction;
    public AppAction SubgraphAction;
    public AppAction UngroupAction;
    public AppAction InspectAction;
    public AppAction PropertiesAction;
    public AppAction ZoomInAction;
    public AppAction ZoomOutAction;
    public AppAction ZoomNormalAction;
    public AppAction ZoomToFitAction;
    public AppAction PrintPreviewAction;
    public AppAction PrintAction;
    public AppAction ToggleLinkIconicNodesAction;
    public AppAction GridAction;
    public AppAction OverviewAction;
    public AppAction LeftAction;
    public AppAction HorizontalAction;
    public AppAction RightAction;
    public AppAction TopAction;
    public AppAction BottomAction;
    public AppAction VerticalAction;
    public AppAction SameWidthAction;
    public AppAction SameHeightAction;
    public AppAction SameBothAction;
    public AppAction LayeredDigraphAutoLayoutAction;
    JMenuItem UndoMenuItem;
    AppAction UndoAction;
    JMenuItem RedoMenuItem;
    AppAction RedoAction;
    private boolean myUpdatingSelection;

    // Let each newly created GraphicViewerBasicNode show an integer label
    int basicNodeCounter;

    static boolean myRNorg = true;
    static int myLAorg = 0;

    // Use a flag on GraphicViewerObject to recognize a particular kind of area
    // (could have implemented our own subclass of GraphicViewerArea, but didn't
    //  want to bother this time)
    public static final int flagMultiSpotNode = 0x10000;

    boolean myIconicNodesAreLinkable;

    protected JSplitPane myPaletteSplitPane;
    protected JDialog myOverviewDialog;

    protected GraphicViewerPalette myPalette;

    protected GraphicViewerOverview myOverview;

    // Menu bar
    protected JMenuBar mainMenuBar;
    protected JMenu filemenu;
    protected JMenu editmenu;
    protected JMenu insertmenu;
    protected JMenu layoutmenu;
    protected JMenu helpmenu;

    protected Point myDefaultLocation;
    protected boolean myRandom;

    // Basic components for this app
    public OTVView myView;
    protected OTVDocument myDoc;
    protected GraphicViewerLayer myMainLayer;
    protected GraphicViewerLayer myForegroundLayer;
    protected GraphicViewerLayer myBackgroundLayer;

    private int myNodeCounter = 0;

    /** Constructor for the Graphic Viewer module. */
    public GraphicViewer() {
        GRAPHIC_VIEWER = this;

        //==============================================================
        // Create the main graphics window
        //==============================================================

        this.myView = new AnimatedView();
        getContentPane().add(this.myView, "Center");

        myDocCount = 1;
        initActions();

        myUpdatingSelection = false;
        basicNodeCounter = 1;
        myIconicNodesAreLinkable = false;
        mainMenuBar = new JMenuBar();
        filemenu = new JMenu();
        editmenu = new JMenu();
        insertmenu = new JMenu();
        layoutmenu = new JMenu();
        helpmenu = new JMenu();
        myDefaultLocation = new Point(10, 10);
        myRandom = true;
        Container container = getContentPane();
        container.setBackground(new Color(255, 204, 204));
        myDoc = myView.getDocument();
        String t = "Untitled" + Integer.toString(myDocCount++);
        myDoc.setName(t);
        setTitle(myDoc.getName() + " - " + Main.APPLICATION_NAME + " ( " + this
                + " )");
        myDoc.setMaintainsPartID(true);
        myView.setBackground(new Color(255, 255, 221));

        // Set up a background image
        //myView.setBackgroundImage((new javax.swing.ImageIcon("C:/jdk1.3.1/demo/applets/ImageMap/images/jim.graham.gif")).getImage());

        myMainLayer = myDoc.getFirstLayer();
        myForegroundLayer = myDoc.addLayerAfter(myMainLayer);
        myForegroundLayer.setIdentifier("in foreground layer");
        myBackgroundLayer = myDoc.addLayerBefore(myMainLayer);
        myBackgroundLayer
                .setIdentifier("in read-only semitransparent background layer");
        myBackgroundLayer.setTransparency(0.5F);
        myBackgroundLayer.setModifiable(true);
        // By default new user-drawn links go into this layer:
        myDoc.setLinksLayer(myDoc.addLayerBefore(myMainLayer));
        myView.setGridWidth(20);
        myView.setGridHeight(20);
        myView.setBorder(new TitledBorder("Zoom: "
                + (int) (myView.getScale() * 100D) + '%'));

        //==============================================================
        // Add event listeners
        //==============================================================

        // Note: An alternate way to get notification of events from a
        // GraphicViewerDocument is to create a subclass of GraphicViewerView
        // and override the documentChanged() method
        myDoc.addDocumentListener(new IGraphicViewerDocumentListener() {

            public void documentChanged(
                    GraphicViewerDocumentEvent graphicviewerdocumentevent) {
                processDocChange(graphicviewerdocumentevent);
            }

        });

        // This will handle selection changes and double clicks
        myView.addViewListener(new IGraphicViewerViewListener() {

            public void viewChanged(
                    GraphicViewerViewEvent graphicviewerviewevent) {
                processViewChange(graphicviewerviewevent);
            }

        });

        // Process KeyEvents from the GraphicViewerView--handle the Delete key.
        //
        // Normally one overrides GraphicViewerView.onKeyEvent() to make use of
        // the default behavior supplied by GraphicViewerView. Using a listener,
        // as this example does, may lead to duplicate and/or conflicting
        // actions if the same key is handled, and does not allow inhibiting the
        // default behavior
        myView.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent keyevent) {
                int i = keyevent.getKeyCode();
                if (i == 127) {
                    myView.deleteSelection();
                } else if (keyevent.isControlDown() && i == 81) {
                    System.exit(0);
                } else if (keyevent.isControlDown() && i == 83) {
                    if (keyevent.isShiftDown()) {
                        FileInputStream fileinputstream = null;
                        try {
                            fileinputstream = new FileInputStream(
                                    "GraphicViewer.serialized");
                            OTVDocument graphicviewerdocument = GraphicViewer
                                    .loadObjects(fileinputstream);
                            if (graphicviewerdocument != null) {
                                getCurrentView().setDocument(
                                        graphicviewerdocument);
                            }
                        } catch (Exception e) {
                            ExceptionDialog.hideException(e);
                        } finally {
                            try {
                                if (fileinputstream != null) {
                                    fileinputstream.close();
                                }
                            } catch (Exception e) {
                                ExceptionDialog.ignoreException(e);
                            }
                        }
                    } else {
                        FileOutputStream fileoutputstream = null;
                        try {
                            fileoutputstream = new FileOutputStream(
                                    "GraphicViewer.serialized");
                            storeObjects(fileoutputstream);
                        } catch (Exception e) {
                            ExceptionDialog.hideException(e);
                        } finally {
                            try {
                                if (fileoutputstream != null) {
                                    fileoutputstream.close();
                                }
                            } catch (Exception e) {
                                ExceptionDialog.ignoreException(e);
                            }
                        }
                    }
                }
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent windowevent) {
                Object obj = windowevent.getSource();
                if (obj == GraphicViewer.this) {
                    setVisible(false); // Hide the frame
                }
            }

        });

        initMenus();

        //==============================================================
        // Create the palette
        // Add a bunch of objects to it in initPalette()
        //==============================================================
        myPalette = new GraphicViewerPalette();

        myPaletteSplitPane = new JSplitPane(0);
        myPaletteSplitPane.setContinuousLayout(true);
        myPaletteSplitPane.setTopComponent(myPalette);
        container.setLayout(new BorderLayout());
        container.add(myView, "Center");
        container.add(myPaletteSplitPane, "West");
        Inspector.inspect(myView.getSelection().getPrimarySelection());
        container.validate();
        myDoc.setUndoManager(myUndoMgr = new UndoMgr());

        setSize(500, 400);

        // Init
        myView.setPrimarySelectionColor(GraphicViewerBrush.ColorMagenta);
        myView.setSecondarySelectionColor(this.myView
                .getPrimarySelectionColor());
        myView.setIncludingNegativeCoords(true);
        myView.setHidingDisabledScrollbars(true);
        myView.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent paramKeyEvent) {
                GraphicViewer.this.handleKeyPressed(paramKeyEvent);
            }
        });
        myView.addViewListener(this);

        start();

        initPalette();
    }

    public void start() {
        new Thread(this).start();
    }

    void initActions() {
        FileNewAction = new AppAction("New", this) {

            private static final long serialVersionUID = 1414804103634873529L;

            public void actionPerformed(ActionEvent e) {
                newProcess();
            }

            public boolean canAct() {
                return true; // doesn't depend on a view
            }
        };
        FileOpenAction = new AppAction("Open...", this) {
            private static final long serialVersionUID = 1414804103634873529L;

            public void actionPerformed(ActionEvent e) {
                openProcess();
            }

            public boolean canAct() {
                return true; // doesn't depend on a view
            }

        };
        FileSaveAsAction = new AppAction("Save As...", this) {
            private static final long serialVersionUID = -8992470956687528154L;

            public void actionPerformed(ActionEvent e) {
                saveAsProcess();
            }
        };
        InsertStuffAction = new AppAction("Lots of stuff", this) {

            private static final long serialVersionUID = 7824327533127229483L;

            public void actionPerformed(ActionEvent actionevent) {
                stuffAction();
            }

        };
        InsertRectangleAction = new AppAction("Rectangle", this) {

            private static final long serialVersionUID = -7734244329928963779L;

            public void actionPerformed(ActionEvent actionevent) {
                rectangleAction();
            }

        };
        InsertRoundedRectangleAction = new AppAction("Rounded Rectangle", this) {

            private static final long serialVersionUID = -5750114374594012980L;

            public void actionPerformed(ActionEvent actionevent) {
                roundedRectangleAction();
            }

        };
        InsertEllipseAction = new AppAction("Ellipse", this) {

            private static final long serialVersionUID = 2350403706812354245L;

            public void actionPerformed(ActionEvent actionevent) {
                ellipseAction();
            }

        };
        InsertMultiPortNodeAction = new AppAction("MultiPort Node", this) {

            private static final long serialVersionUID = 5138435614630320870L;

            public void actionPerformed(ActionEvent actionevent) {
                multiPortNodeAction();
            }

        };
        InsertSimpleNodeAction = new AppAction("Simple Node", this) {

            private static final long serialVersionUID = 7737685033924499665L;

            public void actionPerformed(ActionEvent actionevent) {
                simpleNodeAction();
            }

        };
        InsertIconicNodeAction = new AppAction("Iconic Node", this) {

            private static final long serialVersionUID = 294111230286315144L;

            public void actionPerformed(ActionEvent actionevent) {
                iconicNodeAction();
            }

        };
        InsertDraggableLabelIconicNodeAction = new AppAction(
                "Iconic Node with Draggable Label", this) {

            private static final long serialVersionUID = 5808714647784737878L;

            public void actionPerformed(ActionEvent actionevent) {
                draggableLabelIconicNodeAction();
            }

        };
        InsertBasicNodeAction = new AppAction("Basic Node", this) {

            private static final long serialVersionUID = -399074925553917646L;

            public void actionPerformed(ActionEvent actionevent) {
                basicNodeAction();
            }

        };
        InsertCenterLabelBasicNodeAction = new AppAction(
                "Basic Node with Center Label", this) {

            private static final long serialVersionUID = -3285395314645682410L;

            public void actionPerformed(ActionEvent actionevent) {
                centerLabelBasicNodeAction();
            }

        };
        InsertFixedSizeCenterLabelBasicNodeAction = new AppAction(
                "Fixed Size Basic Node", this) {

            private static final long serialVersionUID = -4534607186034571843L;

            public void actionPerformed(ActionEvent actionevent) {
                fixedSizeCenterLabelBasicNodeAction();
            }

        };
        InsertRectBasicNodeAction = new AppAction("Rectangular Basic Node",
                this) {

            private static final long serialVersionUID = 8209521760670971679L;

            public void actionPerformed(ActionEvent actionevent) {
                rectBasicNodeAction();
            }

        };
        InsertSelfLoopBasicNodeAction = new AppAction(
                "Basic Node with Self Loop", this) {

            private static final long serialVersionUID = -6111490891502565336L;

            public void actionPerformed(ActionEvent actionevent) {
                selfLoopBasicNodeAction();
            }

        };
        InsertMultiSpotNodeAction = new AppAction("MultiSpot Node", this) {

            private static final long serialVersionUID = -8271066125492537578L;

            public void actionPerformed(ActionEvent actionevent) {
                multiSpotNodeAction();
            }

        };
        InsertGeneralNodeAction = new AppAction("General Node", this) {

            private static final long serialVersionUID = 8549766769239283317L;

            public void actionPerformed(ActionEvent actionevent) {
                generalNodeAction();
            }

        };
        AddLeftPortAction = new AppAction("Add Left Port", this) {

            private static final long serialVersionUID = -3870235105758306841L;

            public void actionPerformed(ActionEvent actionevent) {
                addLeftPortAction();
            }

        };
        AddRightPortAction = new AppAction("Add Right Port", this) {

            private static final long serialVersionUID = 950608518073769584L;

            public void actionPerformed(ActionEvent actionevent) {
                addRightPortAction();
            }

        };
        InsertTextNodeAction = new AppAction("Text Node", this) {

            private static final long serialVersionUID = 6784464396775214436L;

            public void actionPerformed(ActionEvent actionevent) {
                textNodeAction();
            }

        };
        InsertFixedSizeTextNodeAction = new AppAction("Fixed Size Text Node",
                this) {

            private static final long serialVersionUID = 7645476463263072508L;

            public void actionPerformed(ActionEvent actionevent) {
                fixedSizeTextNodeAction();
            }

        };
        InsertRoundTextNodeAction = new AppAction("Rounded Text Node", this) {

            private static final long serialVersionUID = 2864794450759408050L;

            public void actionPerformed(ActionEvent actionevent) {
                roundTextNodeAction();
            }

        };
        InsertMultiTextNodeAction = new AppAction("MultiText Node", this) {

            private static final long serialVersionUID = 8552018778759542590L;

            public void actionPerformed(ActionEvent actionevent) {
                multiTextNodeAction();
            }

        };
        InsertListAreaAction = new AppAction("List Area", this) {

            private static final long serialVersionUID = -9063319089227319635L;

            public void actionPerformed(ActionEvent actionevent) {
                listAreaAction();
            }

        };
        InsertRecordNodeAction = new AppAction("Record Nodes", this) {

            private static final long serialVersionUID = 4697530519715949009L;

            public void actionPerformed(ActionEvent actionevent) {
                recordNodeAction();
            }

        };
        InsertStrokeAction = new AppAction("Stroke", this) {

            private static final long serialVersionUID = -7097043041168044421L;

            public void actionPerformed(ActionEvent actionevent) {
                strokeAction();
            }

        };
        InsertPolygonAction = new AppAction("Polygon", this) {

            private static final long serialVersionUID = 7217907977680263758L;

            public void actionPerformed(ActionEvent actionevent) {
                polygonAction();
            }

        };
        InsertDiamondAction = new AppAction("Diamond", this) {

            private static final long serialVersionUID = 8529623323726339626L;

            public void actionPerformed(ActionEvent actionevent) {
                diamondAction();
            }

        };
        InsertTextAction = new AppAction("Text", this) {

            private static final long serialVersionUID = -7992724108770079232L;

            public void actionPerformed(ActionEvent actionevent) {
                textAction();
            }

        };
        Insert3DRectAction = new AppAction("3D Rectangle", this) {

            private static final long serialVersionUID = -883620064977884699L;

            public void actionPerformed(ActionEvent actionevent) {
                threeDRectAction();
            }

        };
        InsertCommentAction = new AppAction("Comment", this) {

            private static final long serialVersionUID = -122196234878236573L;

            public void actionPerformed(ActionEvent actionevent) {
                commentAction();
            }

        };
        Insert10000Action = new AppAction("10000 Objects", this) {

            private static final long serialVersionUID = -1210129709740108709L;

            public void actionPerformed(ActionEvent actionevent) {
                insert10000Action();
            }

        };
        InsertDrawingStrokeAction = new AppAction("Draw Stroke", this) {

            private static final long serialVersionUID = -1797867180828907051L;

            public void actionPerformed(ActionEvent actionevent) {
                insertDrawingStroke();
            }

        };
        InsertGraphOfGraphsAction = new AppAction("Graph of Graphs", this) {

            private static final long serialVersionUID = -39748531827914446L;

            public void actionPerformed(ActionEvent actionevent) {
                insertGraphOfGraphs();
            }

        };
        CutAction = new AppAction("Cut", this) {

            private static final long serialVersionUID = 8320792511130210987L;

            public void actionPerformed(ActionEvent actionevent) {
                cutAction();
            }

            public boolean canAct() {
                return super.canAct() && !getView().getSelection().isEmpty();
            }

        };
        CopyAction = new AppAction("Copy", this) {

            private static final long serialVersionUID = -1391954706276049234L;

            public void actionPerformed(ActionEvent actionevent) {
                getView().copy();
            }

            public boolean canAct() {
                return super.canAct() && !getView().getSelection().isEmpty();
            }

        };
        PasteAction = new AppAction("Paste", this) {

            private static final long serialVersionUID = 7726438029238472529L;

            public void actionPerformed(ActionEvent actionevent) {
                pasteAction();
            }

        };
        SelectAllAction = new AppAction("Select All", this) {

            private static final long serialVersionUID = -94753359164386311L;

            public void actionPerformed(ActionEvent actionevent) {
                getView().selectAll();
            }

        };
        MoveToFrontAction = new AppAction("Move to Front", this) {

            private static final long serialVersionUID = 867310512731307069L;

            public void actionPerformed(ActionEvent actionevent) {
                moveToFrontAction();
            }

            public boolean canAct() {
                return super.canAct() && !getView().getSelection().isEmpty();
            }

        };
        MoveToBackAction = new AppAction("Move to Back", this) {

            private static final long serialVersionUID = -5222661550993554992L;

            public void actionPerformed(ActionEvent actionevent) {
                moveToBackAction();
            }

            public boolean canAct() {
                return super.canAct() && !getView().getSelection().isEmpty();
            }

        };
        ChangeLayersAction = new AppAction("Change Layers", this) {

            private static final long serialVersionUID = -4760944129508589979L;

            public void actionPerformed(ActionEvent actionevent) {
                changeLayersAction();
            }

        };
        GroupAction = new AppAction("Group", this) {

            private static final long serialVersionUID = 4200991881326798174L;

            public void actionPerformed(ActionEvent actionevent) {
                groupAction();
            }

            public boolean canAct() {
                return super.canAct()
                        && getView().getSelection().getNumObjects() >= 2;
            }

        };
        SubgraphAction = new AppAction("Make SubGraph", this) {

            private static final long serialVersionUID = -4010986694049376449L;

            public void actionPerformed(ActionEvent actionevent) {
                subgraphAction();
            }

            public boolean canAct() {
                return super.canAct()
                        && getView().getSelection().getNumObjects() >= 2;
            }

        };
        UngroupAction = new AppAction("Ungroup", this) {

            private static final long serialVersionUID = -9101271013112289670L;

            public void actionPerformed(ActionEvent actionevent) {
                ungroupAction();
            }

            public boolean canAct() {
                return super.canAct()
                        && !getView().getSelection().isEmpty()
                        && (getView().getSelection().getPrimarySelection() instanceof GraphicViewerArea);
            }

        };
        InspectAction = new AppAction("Inspect", this) {

            private static final long serialVersionUID = 4628419768518358843L;

            public void actionPerformed(ActionEvent actionevent) {
                inspectAction();
            }

        };
        PropertiesAction = new AppAction("Properties", this) {

            private static final long serialVersionUID = 5060694950361021930L;

            public void actionPerformed(ActionEvent actionevent) {
                propertiesAction();
            }

            public boolean canAct() {
                return super.canAct() && !getView().getSelection().isEmpty();
            }

        };
        ZoomInAction = new AppAction("Zoom In", this) {

            private static final long serialVersionUID = -5529252815471243299L;

            public void actionPerformed(ActionEvent actionevent) {
                zoomInAction();
            }

            public boolean canAct() {
                return super.canAct() && getView().getScale() < 8D;
            }

        };
        ZoomOutAction = new AppAction("Zoom Out", this) {

            private static final long serialVersionUID = 4100420909989446212L;

            public void actionPerformed(ActionEvent actionevent) {
                zoomOutAction();
            }

            public boolean canAct() {
                return super.canAct()
                        && getView().getScale() > 0.12999999523162842D;
            }

        };
        ZoomNormalAction = new AppAction("Zoom Normal Size", this) {

            private static final long serialVersionUID = 8701123078885648918L;

            public void actionPerformed(ActionEvent actionevent) {
                zoomNormalAction();
            }

        };
        ZoomToFitAction = new AppAction("Zoom To Fit", this) {

            private static final long serialVersionUID = 3137572118754680732L;

            public void actionPerformed(ActionEvent actionevent) {
                zoomToFitAction();
            }

        };
        PrintPreviewAction = new AppAction("Print Preview", this) {

            private static final long serialVersionUID = 2896981114073240329L;

            public void actionPerformed(ActionEvent actionevent) {
                PrinterJob printerjob = PrinterJob.getPrinterJob();
                java.awt.print.PageFormat pageformat = printerjob
                        .validatePage(printerjob.defaultPage());
                // Ask the user what page size they're using, what the margins
                // are and whether they are printing in landscape or portrait
                // mode
                java.awt.print.PageFormat pageformat1 = printerjob
                        .pageDialog(pageformat);
                if (pageformat1 != pageformat) {
                    getView().printPreview("Print Preview", pageformat1);
                }
            }

        };
        PrintAction = new AppAction("Print", this) {

            private static final long serialVersionUID = 4415255746348666691L;

            public void actionPerformed(ActionEvent actionevent) {
                getView().print();
            }

        };
        ToggleLinkIconicNodesAction = new AppAction(
                "Toggle Link IconicNodes Mode", this) {

            private static final long serialVersionUID = -6967709996480015508L;

            public void actionPerformed(ActionEvent actionevent) {
                toggleLinkIconicNodesAction();
            }

        };
        GridAction = new AppAction("Grid", this) {

            private static final long serialVersionUID = -5503002814477578749L;

            public void actionPerformed(ActionEvent actionevent) {
                gridAction();
            }

        };
        OverviewAction = new AppAction("Overview", this) {

            private static final long serialVersionUID = 6722294542396754343L;

            public void actionPerformed(ActionEvent actionevent) {
                overviewAction();
            }

        };
        LeftAction = new AppAction("Align Left Sides", this) {

            private static final long serialVersionUID = 3212592007640043891L;

            public void actionPerformed(ActionEvent actionevent) {
                leftAction();
            }

            public boolean canAct() {
                return super.canAct()
                        && getView().getSelection().getNumObjects() >= 2;
            }

        };
        HorizontalAction = new AppAction("Align Horizontal Centers", this) {

            private static final long serialVersionUID = -1369902587875184042L;

            public void actionPerformed(ActionEvent actionevent) {
                horizontalAction();
            }

            public boolean canAct() {
                return super.canAct()
                        && getView().getSelection().getNumObjects() >= 2;
            }

        };
        RightAction = new AppAction("Align Right Sides", this) {

            private static final long serialVersionUID = 382509548069206211L;

            public void actionPerformed(ActionEvent actionevent) {
                rightAction();
            }

            public boolean canAct() {
                return super.canAct()
                        && getView().getSelection().getNumObjects() >= 2;
            }

        };
        TopAction = new AppAction("Align Tops", this) {

            private static final long serialVersionUID = 7728435179793277793L;

            public void actionPerformed(ActionEvent actionevent) {
                topAction();
            }

            public boolean canAct() {
                return super.canAct()
                        && getView().getSelection().getNumObjects() >= 2;
            }

        };
        BottomAction = new AppAction("Align Bottoms", this) {

            private static final long serialVersionUID = 5567282088716420732L;

            public void actionPerformed(ActionEvent actionevent) {
                bottomAction();
            }

            public boolean canAct() {
                return super.canAct()
                        && getView().getSelection().getNumObjects() >= 2;
            }

        };
        VerticalAction = new AppAction("Align Vertical Centers", this) {

            private static final long serialVersionUID = -7623195699157226758L;

            public void actionPerformed(ActionEvent actionevent) {
                verticalAction();
            }

            public boolean canAct() {
                return super.canAct()
                        && getView().getSelection().getNumObjects() >= 2;
            }

        };
        SameWidthAction = new AppAction("Make Same Size Widths", this) {

            private static final long serialVersionUID = -3456044941221751098L;

            public void actionPerformed(ActionEvent actionevent) {
                sameWidthAction();
            }

            public boolean canAct() {
                return super.canAct()
                        && getView().getSelection().getNumObjects() >= 2;
            }

        };
        SameHeightAction = new AppAction("Make Same Size Heights", this) {

            private static final long serialVersionUID = -3813018331047630619L;

            public void actionPerformed(ActionEvent actionevent) {
                sameHeightAction();
            }

            public boolean canAct() {
                return super.canAct()
                        && getView().getSelection().getNumObjects() >= 2;
            }

        };
        SameBothAction = new AppAction("Make Same Size Both", this) {

            private static final long serialVersionUID = -5412308482846372221L;

            public void actionPerformed(ActionEvent actionevent) {
                sameBothAction();
            }

            public boolean canAct() {
                return super.canAct()
                        && getView().getSelection().getNumObjects() >= 2;
            }

        };
        LayeredDigraphAutoLayoutAction = new AppAction("AutoLayout", this) {

            private static final long serialVersionUID = 8400897852369657505L;

            public void actionPerformed(ActionEvent actionevent) {
                layeredDigraphAutoLayoutAction();
            }

        };
        UndoMenuItem = null;
        UndoAction = new AppAction("Undo", this) {

            private static final long serialVersionUID = 6203990439611925515L;

            public void actionPerformed(ActionEvent actionevent) {
                getView().getDocument().undo();
                AppAction.updateAllActions();
            }

            public boolean canAct() {
                return super.canAct() && getView().getDocument().canUndo();
            }

            public void updateEnabled() {
                super.updateEnabled();
                if (UndoMenuItem != null && getView() != null) {
                    GraphicViewerUndoManager graphicviewerundomanager = getView()
                            .getDocument().getUndoManager();
                    if (graphicviewerundomanager != null) {
                        UndoMenuItem.setText(graphicviewerundomanager
                                .getUndoPresentationName());
                    }
                }
            }

        };
        RedoMenuItem = null;
        RedoAction = new AppAction("Redo", this) {

            private static final long serialVersionUID = -2944349594857618168L;

            public void actionPerformed(ActionEvent actionevent) {
                getView().getDocument().redo();
                AppAction.updateAllActions();
            }

            public boolean canAct() {
                return super.canAct() && getView().getDocument().canRedo();
            }

            public void updateEnabled() {
                super.updateEnabled();
                if (RedoMenuItem != null && getView() != null) {
                    GraphicViewerUndoManager graphicviewerundomanager = getView()
                            .getDocument().getUndoManager();
                    if (graphicviewerundomanager != null) {
                        RedoMenuItem.setText(graphicviewerundomanager
                                .getRedoPresentationName());
                    }
                }
            }

        };
    }

    void newProcess() {
        myDoc.deleteContents();
        myDoc.removeLayer(myDoc.getFirstLayer());
        myDoc = new OTVDocument();
        String t = "Untitled" + Integer.toString(myDocCount++);
        myDoc.setName(t);
        setTitle(myDoc.getName() + " - " + Main.APPLICATION_NAME + " ( " + this
                + " )");
        myMainLayer = myDoc.getFirstLayer();
        myForegroundLayer = myDoc.addLayerAfter(myMainLayer);
        myForegroundLayer.setIdentifier("in foreground layer");
        myBackgroundLayer = myDoc.addLayerBefore(myMainLayer);
        myBackgroundLayer
                .setIdentifier("in read-only semitransparent background layer");
        myBackgroundLayer.setTransparency(0.5F);
        myBackgroundLayer.setModifiable(true);
        myDoc.setLinksLayer(myDoc.addLayerBefore(myMainLayer));
        myDoc.addDocumentListener(new IGraphicViewerDocumentListener() {
            public void documentChanged(
                    GraphicViewerDocumentEvent graphicviewerdocumentevent) {
                processDocChange(graphicviewerdocumentevent);
            }
        });
        myDoc.setUndoManager(myUndoMgr);
        myView.setDocument(myDoc);
        myView.requestFocus();

        if (myOverviewDialog != null && myOverviewDialog.isVisible()) {
            myOverview.setObserved(myView);
            myOverview.repaint();
            myDoc.addDocumentListener(myOverview);
        }

        myDoc.setModified(false);
        AppAction.updateAllActions();
    }

    //==============================================================
    // Process GraphicViewerDocumentEvents from the GraphicViewerDocument
    //==============================================================
    public void processDocChange(GraphicViewerDocumentEvent e) {
        switch (e.getHint()) {
            case GraphicViewerDocumentEvent.STARTED_TRANSACTION :
                //          System.err.println("Started Transaction");
                break;
            case GraphicViewerDocumentEvent.FINISHED_TRANSACTION :
                //          if (e.getPreviousValue() != null)
                //            System.err.println("Finished Transaction " + e.getPreviousValue().toString());
                //          else
                //            System.err.println("Finished Transaction");
                //          // Look at all of the GraphicViewerDocumentChangedEdits that
                // happened in this transaction
                //          if (e.getObject() instanceof GraphicViewerUndoManager.GraphicViewerCompoundEdit) {
                //            GraphicViewerUndoManager.GraphicViewerCompoundEdit cedit = (GraphicViewerUndoManager.GraphicViewerCompoundEdit)e.getObject();
                //            Vector edits = cedit.getAllEdits();
                //            for (int i = 0; i < edits.size(); i++) {
                //              if (edits.get(i) instanceof GraphicViewerDocumentChangedEdit) {
                //                GraphicViewerDocumentChangedEdit edit = (GraphicViewerDocumentChangedEdit)edits.get(i);
                //                System.err.println("  " + edit.toString());
                //              }
                //            }
                //          }
                break;
            case GraphicViewerDocumentEvent.ABORTED_TRANSACTION :
                //          System.err.println("Aborted Transaction");
                break;
            case GraphicViewerDocumentEvent.STARTING_UNDO :
                //          if (e.getObject() instanceof UndoableEdit)
                //            System.err.println("Starting Undo " + ((UndoableEdit)e.getObject()).getUndoPresentationName());
                //          else
                //            System.err.println("Starting Undo");
                break;
            case GraphicViewerDocumentEvent.FINISHED_UNDO :
                //          System.err.println("Finished Undo");
                break;
            case GraphicViewerDocumentEvent.STARTING_REDO :
                //          if (e.getObject() instanceof UndoableEdit)
                //            System.err.println("Starting Redo " + ((UndoableEdit)e.getObject()).getRedoPresentationName());
                //          else
                //            System.err.println("Starting Redo");
                break;
            case GraphicViewerDocumentEvent.FINISHED_REDO :
                //          System.err.println("Finished Redo");
                break;
            case GraphicViewerDocumentEvent.INSERTED :
                if (e.getGraphicViewerObject() instanceof GraphicViewerLink) {
                    // Make sure each link that's created has an arrowhead at its front,
                    // if the "to" port is a port on a GraphicViewerBasicNode
                    GraphicViewerLink link = (GraphicViewerLink) e
                            .getGraphicViewerObject();
                    GraphicViewerPort port = link.getToPort();
                    if (port != null) {
                        if (port instanceof MultiPortNodePort) {
                            link.setArrowHeads(false, true);
                        } else if (port.getParent() instanceof GraphicViewerBasicNode) {
                            // Only have an arrowhead at the "to" end
                            link.setArrowHeads(false, true);

                            // The default values produce a traditional looking
                            // arrowhead; the following values produce a diamond
                            // instead
                            //link.setArrowAngle(Math.PI/3);
                            //link.setArrowLength(8);
                            //link.setArrowShaftLength(16);
                        }

                        // If the "from" port is the same as the "to" port, make
                        // sure it doesn't have labels at either end, and change
                        // its middle label
                        if (port == link.getFromPort()
                                && link instanceof GraphicViewerLabeledLink) {
                            GraphicViewerLabeledLink ll = (GraphicViewerLabeledLink) link;
                            GraphicViewerObject mid = ll.getMidLabel();
                            ll.setFromLabel(null);
                            ll.setToLabel(null);
                            if (mid != null && mid instanceof GraphicViewerText) {
                                ((GraphicViewerText) mid).setText("loop");
                            }
                        }
                    }

                    // If it's connected to a MultiSpotNode, make it
                    // orthogonally routed (not implemented as a separate class,
                    // so we're using a flag on the object as a marker)
                    if (port != null && port.getParent() != null) {
                        if ((port.getParent().getFlags() & flagMultiSpotNode) != 0) {
                            link.setOrthogonal(true);
                            link.setAvoidsNodes(true);
                            link.setJumpsOver(true);
                        } else if (port.getParent() instanceof MultiTextNode) {
                            link.setOrthogonal(true);
                            link.setArrowHeads(false, true);
                            link.setArrowShaftLength(0);
                        }
                    }

                    // If the link connects ports on different kinds of objects,
                    // highlight it in red
                    if (link.getFromPort() != null
                            && port.getClass() != link.getFromPort().getClass()) {
                        link.setHighlight(GraphicViewerPen.make(
                                GraphicViewerPen.SOLID, link.getPen()
                                        .getWidth() + 4, Color.red));
                    }
                }
                break;
        }
    }

    public void run() {
        this.myView.initializeDragDropHandling();
    }

    void handleKeyPressed(KeyEvent paramKeyEvent) {
        int i = paramKeyEvent.getKeyCode();
        if (i == 127) {
            this.myView.deleteSelection();
        } else {
            Rectangle localRectangle;
            if (i == 155) {
                localRectangle = this.myView.getViewRect();
                insertNode(new Point(localRectangle.x + localRectangle.width
                        / 2, localRectangle.y + localRectangle.height / 2),
                        this.myNodeCounter % 2 == 0);
            } else if (i == 36) {
                localRectangle = this.myView.getDocument().computeBounds();
                this.myView.setViewPosition(localRectangle.x, localRectangle.y);
            } else if (i == 35) {
                localRectangle = this.myView.getDocument().computeBounds();
                Dimension localDimension = this.myView.getExtentSize();
                this.myView
                        .setViewPosition(
                                Math.max(localRectangle.x, localRectangle.x
                                        + localRectangle.width
                                        - localDimension.width),
                                Math.max(localRectangle.y, localRectangle.y
                                        + localRectangle.height
                                        - localDimension.height));
            } else if ((paramKeyEvent.isControlDown()) && (i == 65)) {
                this.myView.selectAll();
            } else if ((paramKeyEvent.isControlDown()) && (i == 88)) {
                this.myView.cut();
            } else if ((paramKeyEvent.isControlDown()) && (i == 67)) {
                this.myView.copy();
            } else if ((paramKeyEvent.isControlDown()) && (i == 86)) {
                this.myView.paste();
            } else if ((paramKeyEvent.isControlDown()) && (i == 90)) {
                this.myView.getDocument().undo();
            } else if ((paramKeyEvent.isControlDown()) && (i == 89)) {
                this.myView.getDocument().redo();
            }
        }
    }

    public void viewChanged(GraphicViewerViewEvent paramGraphicViewerViewEvent) {
        if (paramGraphicViewerViewEvent.getHint() == 2) {
            if ((paramGraphicViewerViewEvent.getGraphicViewerObject() instanceof GraphicViewerLink)) {
                GraphicViewerLink localGraphicViewerLink = (GraphicViewerLink) paramGraphicViewerViewEvent
                        .getGraphicViewerObject();
                localGraphicViewerLink.setPen(GraphicViewerPen.make(65535, 2,
                        GraphicViewerBrush.ColorGray));
                localGraphicViewerLink.setBrush(GraphicViewerBrush.gray);
                localGraphicViewerLink.setArrowHeads(false, true);
            }
        } else if (paramGraphicViewerViewEvent.getHint() == 25) {
            insertNode(paramGraphicViewerViewEvent.getPointDocCoords(),
                    this.myNodeCounter % 2 == 0);
        }
    }

    public void documentChanged(
            GraphicViewerDocumentEvent paramGraphicViewerDocumentEvent) {
        if ((paramGraphicViewerDocumentEvent.getHint() == 202)
                && ((paramGraphicViewerDocumentEvent.getGraphicViewerObject() instanceof GraphicViewerLink))) {
            GraphicViewerLink localGraphicViewerLink = (GraphicViewerLink) paramGraphicViewerDocumentEvent
                    .getGraphicViewerObject();
            localGraphicViewerLink.setBrush(GraphicViewerBrush
                    .makeStockBrush(getRandomColor(100)));
            localGraphicViewerLink.setPen(GraphicViewerPen.make(65535, 2,
                    localGraphicViewerLink.getBrush().getColor()));
            localGraphicViewerLink.setArrowHeads(false, true);
            localGraphicViewerLink.setCubic(true);
            localGraphicViewerLink.calculateStroke();
        }
    }

    public GraphicViewerBasicNode insertNode(Point paramPoint,
            boolean paramBoolean) {
        GraphicViewerDocument localGraphicViewerDocument = this.myView
                .getDocument();
        localGraphicViewerDocument.startTransaction();
        GraphicViewerBasicNode localGraphicViewerBasicNode = new GraphicViewerBasicNode(
                Integer.toString(++this.myNodeCounter));
        localGraphicViewerBasicNode.setLabelSpot(0);
        localGraphicViewerBasicNode.setLocation(paramPoint);
        if (paramBoolean) {
            localGraphicViewerBasicNode
                    .setDrawable(new GraphicViewerRectangle());
        }
        localGraphicViewerBasicNode.setBrush(GraphicViewerBrush
                .makeStockBrush(getRandomColor(100)));
        localGraphicViewerBasicNode.setPen(GraphicViewerPen.make(65535, 2,
                getRandomColor(130)));
        localGraphicViewerBasicNode.getLabel().setEditable(true);
        localGraphicViewerDocument.addObjectAtTail(localGraphicViewerBasicNode);
        localGraphicViewerDocument.endTransaction("inserted node");
        return localGraphicViewerBasicNode;
    }

    Color getRandomColor(int paramInt) {
        return new Color(paramInt + (int) ((250 - paramInt) * Math.random()),
                paramInt + (int) ((250 - paramInt) * Math.random()), paramInt
                        + (int) ((250 - paramInt) * Math.random()));
    }

    public OTVView getCurrentView() {
        return myView;
    }

    public void startTransaction() {
        myDoc.startTransaction();
    }

    public void endTransaction(String s) {
        myDoc.endTransaction(s);
    }

    // This calls the appropriate dialog box for the type of object that obj is.
    void callDialog(GraphicViewerObject graphicviewerobject) {
        startTransaction();
        myView.getSelection().clearSelectionHandles(graphicviewerobject);
        String s = null;
        if (graphicviewerobject instanceof GraphicViewerStroke) {
            s = "Stroke Properties";
            StrokeDialog strokedialog = new StrokeDialog(myView.getFrame(), s,
                    true, (GraphicViewerStroke) graphicviewerobject);
            UISupport.showDialog(strokedialog);
        } else if (graphicviewerobject instanceof GraphicViewerText) {
            s = "Text Properties";
            TextPropsDialog textpropsdialog = new TextPropsDialog(
                    myView.getFrame(), s, true,
                    (GraphicViewerText) graphicviewerobject);
            UISupport.showDialog(textpropsdialog);
        } else if (graphicviewerobject instanceof GraphicViewerPort) {
            s = "Port Properties";
            PortPropsDialog portpropsdialog = new PortPropsDialog(
                    myView.getFrame(), s, true,
                    (GraphicViewerPort) graphicviewerobject);
            UISupport.showDialog(portpropsdialog);
        } else if (graphicviewerobject instanceof GraphicViewerDrawable) {
            s = "Drawable Properties";
            DrawablePropsDialog drawablepropsdialog = new DrawablePropsDialog(
                    myView.getFrame(), s, true,
                    (GraphicViewerDrawable) graphicviewerobject);
            UISupport.showDialog(drawablepropsdialog);
        } else if (graphicviewerobject instanceof GraphicViewerImage) {
            s = "Image Properties";
            ImagePropsDialog imagepropsdialog = new ImagePropsDialog(
                    myView.getFrame(), s, true,
                    (GraphicViewerImage) graphicviewerobject);
            UISupport.showDialog(imagepropsdialog);
        } else if (graphicviewerobject instanceof GraphicViewerObject) {
            s = "Object Properties";
            ObjectPropsDialog objectpropsdialog = new ObjectPropsDialog(
                    myView.getFrame(), s, true, graphicviewerobject);
            UISupport.showDialog(objectpropsdialog);
        }
        myDoc.update();
        myView.getSelection().restoreSelectionHandles(graphicviewerobject);
        endTransaction(s);
    }

    void moveToBackAction() {
        startTransaction();
        GraphicViewerSelection graphicviewerselection = myView.getSelection();
        for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerselection
                .getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerselection
                    .getNextObjectPos(graphicviewerlistposition);
            if (graphicviewerobject.getParent() != null) {
                graphicviewerobject.getParent().sendObjectToBack(
                        graphicviewerobject);
            } else {
                myDoc.sendObjectToBack(graphicviewerobject);
            }
        }

        endTransaction(MoveToBackAction.toString());
    }

    void threeDRectAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(100, 150);
        }
        GraphicViewer3DRect graphicviewer3drect = new GraphicViewer3DRect(
                point, new Dimension(55, 25));
        graphicviewer3drect.setBrush(GraphicViewerBrush
                .makeStockBrush(Color.cyan));
        myMainLayer.addObjectAtTail(graphicviewer3drect);
        endTransaction(Insert3DRectAction.toString());
    }

    // Align all object's vertical centers to the vertical center of the primary
    // selection.
    void verticalAction() {
        startTransaction();
        GraphicViewerSelection graphicviewerselection = myView.getSelection();
        GraphicViewerObject graphicviewerobject = graphicviewerselection
                .getPrimarySelection();
        GraphicViewerListPosition graphicviewerlistposition;
        for (graphicviewerlistposition = graphicviewerselection
                .getFirstObjectPos(); graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerLink)
                && graphicviewerlistposition != null; graphicviewerlistposition = graphicviewerselection
                .getNextObjectPos(graphicviewerlistposition)) {
            graphicviewerobject = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
        }

        if (graphicviewerobject == null) {
            return;
        }
        Point point = graphicviewerobject.getSpotLocation(0);
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject1 = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerselection
                    .getNextObjectPos(graphicviewerlistposition);
            if (!(graphicviewerobject1 instanceof GraphicViewerLink)) {
                graphicviewerobject1.setSpotLocation(8, new Point(
                        graphicviewerobject1.getLeft(), point.y));
            }
        } while (true);
        endTransaction(VerticalAction.toString());
    }

    // The default place to put stuff if not dragged there
    Point getDefaultLocation() {
        // To avoid constantly putting things in the same place, keep shifting
        // the default location
        if (myDefaultLocation != null) {
            myDefaultLocation.x += 4;
            myDefaultLocation.y += 4;
        }
        return myDefaultLocation;
    }

    void propertiesAction() {
        startTransaction();
        try {
            doEditProperties();
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
        endTransaction(PropertiesAction.toString());
    }

    public void insertGraphOfGraphs() {
        startTransaction();
        GraphicViewerSubGraph graphicviewersubgraph = makeSubGraph("one", "1");
        graphicviewersubgraph.setTopLeft(300, 100);
        GraphicViewerSubGraph graphicviewersubgraph1 = makeSubGraph("two", "2");
        graphicviewersubgraph1.setCollapsible(false);
        graphicviewersubgraph1.setTopLeft(100, 300);
        graphicviewersubgraph1.setBackgroundColor(new Color(255, 200, 200, 63));
        graphicviewersubgraph1.setBorderPen(GraphicViewerPen.make(3, 2,
                Color.gray));
        GraphicViewerSubGraph graphicviewersubgraph2 = makeSubGraph("three",
                "3");
        graphicviewersubgraph2.setTopLeft(500, 300);
        graphicviewersubgraph2.setBackgroundColor(new Color(200, 255, 200, 63));
        graphicviewersubgraph2.setBorderPen(GraphicViewerPen
                .makeStockPen(Color.magenta));
        GraphicViewerLabeledLink graphicviewerlabeledlink = new GraphicViewerLabeledLink(
                findBasicNode(graphicviewersubgraph, "1b").getPort(),
                findBasicNode(graphicviewersubgraph1, "2a").getPort());
        GraphicViewerLinkLabel graphicviewerlinklabel = new GraphicViewerLinkLabel(
                "one to two");
        graphicviewerlabeledlink.setMidLabel(graphicviewerlinklabel);
        GraphicViewerLabeledLink graphicviewerlabeledlink1 = new GraphicViewerLabeledLink(
                findBasicNode(graphicviewersubgraph, "1c").getPort(),
                findBasicNode(graphicviewersubgraph2, "3a").getPort());
        GraphicViewerLinkLabel graphicviewerlinklabel1 = new GraphicViewerLinkLabel(
                "one to three");
        graphicviewerlabeledlink1.setMidLabel(graphicviewerlinklabel1);
        myMainLayer.addObjectAtTail(graphicviewersubgraph);
        myMainLayer.addObjectAtTail(graphicviewersubgraph1);
        myMainLayer.addObjectAtTail(graphicviewersubgraph2);
        myMainLayer.addObjectAtTail(graphicviewerlabeledlink);
        myMainLayer.addObjectAtTail(graphicviewerlabeledlink1);
        GraphicViewerSubGraph graphicviewersubgraph3 = makeSubGraph("four", "4");
        graphicviewersubgraph3.setTopLeft(550, 350);
        graphicviewersubgraph2.addObjectAtTail(graphicviewersubgraph3);
        endTransaction(InsertGraphOfGraphsAction.toString());
    }

    void commentAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(100, 200);
        }
        Comment comment = new Comment("This is a\nmultiline comment.");
        comment.setTopLeft(point.x, point.y);
        myMainLayer.addObjectAtTail(comment);
        endTransaction(InsertCommentAction.toString());
    }

    // Make the overview window visible.
    void overviewAction() {
        if (myOverview == null) {
            myOverview = new GraphicViewerOverview();
            myOverview.setObserved(myView);
            myOverview.setPreferredSize(new Dimension(400, 400));
            myOverviewDialog = new JDialog(this, "Overview", false);
            myOverviewDialog.getContentPane().setLayout(new BorderLayout());
            myOverviewDialog.getContentPane().add(myOverview, "Center");
        }
        myOverviewDialog.pack();
        UISupport.showDialog(myOverviewDialog);
    }

    /** Display a form to edit the selected GraphicViewerObject's properties. */
    public void doEditProperties() {
        GraphicViewerSelection graphicviewerselection = myView.getSelection();
        if (!graphicviewerselection.isEmpty()) {
            for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerselection
                    .getFirstObjectPos(); graphicviewerlistposition != null;) {
                GraphicViewerObject graphicviewerobject = graphicviewerselection
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewerselection
                        .getNextObjectPos(graphicviewerlistposition);
                callDialog(graphicviewerobject);
            }
        }
    }

    // These two actions now work for selected objects that are part of areas as
    // well as for top-level document objects.
    void moveToFrontAction() {
        startTransaction();
        GraphicViewerSelection graphicviewerselection = myView.getSelection();
        for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerselection
                .getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerselection
                    .getNextObjectPos(graphicviewerlistposition);
            if (graphicviewerobject.getParent() != null) {
                graphicviewerobject.getParent().bringObjectToFront(
                        graphicviewerobject);
            } else {
                myDoc.bringObjectToFront(graphicviewerobject);
            }
        }

        endTransaction(MoveToFrontAction.toString());
    }

    void insertDrawingStroke() {
        UISupport
                .getDialogs()
                .showInfoMessage(
                        "Starting a mode to allow you to draw a stroke by specifying its points.\nThis mode stops when you type ENTER to accept the stroke or ESCAPE to cancel the new link.",
                        "Drawing Stroke Mode");
        myView.startDrawingStroke();
    }

    void textAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(150, 50);
        }
        GraphicViewerText graphicviewertext = new GraphicViewerText(point, 12,
                "Sample Text", "Serif", true, true, true, 2, false, true);
        graphicviewertext.setResizable(true);
        graphicviewertext.setEditOnSingleClick(true);
        myMainLayer.addObjectAtTail(graphicviewertext);
        endTransaction(InsertTextAction.toString());
    }

    void polygonAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(10, 80);
        }
        GraphicViewerPolygon graphicviewerpolygon = new GraphicViewerPolygon();
        graphicviewerpolygon.setBrush(GraphicViewerBrush
                .makeStockBrush(Color.red));
        graphicviewerpolygon.setPen(GraphicViewerPen.make(65535, 3,
                Color.lightGray));
        graphicviewerpolygon.addPoint(new Point(point.x + 10, point.y + 0));
        graphicviewerpolygon.addPoint(new Point(point.x + 20, point.y + 0));
        graphicviewerpolygon.addPoint(new Point(point.x + 30, point.y + 10));
        graphicviewerpolygon.addPoint(new Point(point.x + 30, point.y + 20));
        graphicviewerpolygon.addPoint(new Point(point.x + 20, point.y + 30));
        graphicviewerpolygon.addPoint(new Point(point.x + 10, point.y + 30));
        graphicviewerpolygon.addPoint(new Point(point.x + 0, point.y + 20));
        graphicviewerpolygon.addPoint(new Point(point.x + 0, point.y + 10));
        myMainLayer.addObjectAtTail(graphicviewerpolygon);
        endTransaction(InsertPolygonAction.toString());
    }

    public ListArea listAreaAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(420, 350);
        }
        ListArea listarea = new ListArea();
        listarea.initialize();
        // Have fun trying different orientations and scroll bar arrangements
        switch (myLAorg) {
            case 0 : // '\0'
            default :
                listarea.setVertical(true);
                listarea.setScrollBarOnRight(true);
                myLAorg++;
                break;

            case 1 : // '\001'
                listarea.setVertical(true);
                listarea.setScrollBarOnRight(false);
                myLAorg++;
                break;

            case 2 : // '\002'
                listarea.setVertical(false);
                listarea.setScrollBarOnRight(true);
                myLAorg++;
                break;

            case 3 : // '\003'
                listarea.setVertical(false);
                listarea.setScrollBarOnRight(false);
                myLAorg = 0;
                break;
        }
        for (int i = 0; i < 10; i++) {
            listarea.addItem(makeListItem(i), null, null, null);
        }

        myMainLayer.addObjectAtTail(listarea);
        listarea.setTopLeft(point.x, point.y);
        // Start it not with the first object (0)
        listarea.setFirstVisibleIndex(3);
        listarea.setAlignment(0);
        endTransaction(InsertListAreaAction.toString());
        return listarea;
    }

    void insert10000Action() {
        startTransaction();
        GraphicViewerPen graphicviewerpen = GraphicViewerPen
                .makeStockPen(Color.red);
        GraphicViewerPen graphicviewerpen1 = GraphicViewerPen
                .makeStockPen(Color.green);
        GraphicViewerPen graphicviewerpen2 = GraphicViewerPen
                .makeStockPen(Color.blue);
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                // GraphicViewerDrawable obj = new GraphicViewerRectangle();
                // obj.setResizable(false);
                // obj.setTopLeft(i*40, j*40);
                // obj.setSize(20, 20);
                GraphicViewerBasicNode graphicviewerbasicnode = new GraphicViewerBasicNode(
                        Integer.toString(i) + "," + Integer.toString(j));
                graphicviewerbasicnode.setLocation(new Point(i * 40, j * 40));
                switch ((i * 100 + j) % 6) {
                    case 0 : // '\0'
                    default :
                        graphicviewerbasicnode.setPen(GraphicViewerPen.black);
                        break;

                    case 1 : // '\001'
                        graphicviewerbasicnode.setPen(graphicviewerpen);
                        break;

                    case 2 : // '\002'
                        graphicviewerbasicnode.setPen(GraphicViewerPen.gray);
                        break;

                    case 3 : // '\003'
                        graphicviewerbasicnode.setPen(GraphicViewerPen.black);
                        break;

                    case 4 : // '\004'
                        graphicviewerbasicnode.setPen(graphicviewerpen1);
                        break;

                    case 5 : // '\005'
                        graphicviewerbasicnode.setPen(graphicviewerpen2);
                        break;
                }
                myMainLayer.addObjectAtTail(graphicviewerbasicnode);
            }

        }

        endTransaction(Insert10000Action.toString());
    }

    // Align all object's left sides to the left side of the primary selection.
    void leftAction() {
        startTransaction();
        GraphicViewerSelection graphicviewerselection = myView.getSelection();
        GraphicViewerObject graphicviewerobject = graphicviewerselection
                .getPrimarySelection();
        GraphicViewerListPosition graphicviewerlistposition;
        for (graphicviewerlistposition = graphicviewerselection
                .getFirstObjectPos(); graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerLink)
                && graphicviewerlistposition != null; graphicviewerlistposition = graphicviewerselection
                .getNextObjectPos(graphicviewerlistposition)) {
            graphicviewerobject = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
        }

        if (graphicviewerobject == null) {
            return;
        }
        Point point = graphicviewerobject.getSpotLocation(1);
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject1 = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerselection
                    .getNextObjectPos(graphicviewerlistposition);
            if (!(graphicviewerobject1 instanceof GraphicViewerLink)) {
                graphicviewerobject1.setSpotLocation(1, new Point(point.x,
                        graphicviewerobject1.getTop()));
            }
        } while (true);
        endTransaction(LeftAction.toString());
    }

    void zoomNormalAction() {
        double d = 1.0D;
        myView.setScale(d);
        updateActions();
    }

    // Make a GraphicViewerSubGraph out of the current selection.
    void subgraphAction() {
        startTransaction();
        GraphicViewerSelection graphicviewerselection = myView.getSelection();
        GraphicViewerObject graphicviewerobject = graphicviewerselection
                .getPrimarySelection();
        GraphicViewerSubGraph graphicviewersubgraph = new GraphicViewerSubGraph();
        graphicviewersubgraph.initialize("subgraph!");
        // Now add the area to the document
        graphicviewerobject.getLayer().addObjectAtTail(graphicviewersubgraph);
        // Add the selected objects to the area
        graphicviewersubgraph.addCollection(graphicviewerselection, true,
                myMainLayer);
        // Update the selection too, for the user's convenience
        graphicviewerselection.selectObject(graphicviewersubgraph);
        endTransaction(SubgraphAction.toString());
    }

    void zoomToFitAction() {
        double d = 1.0D;
        if (!myDoc.isEmpty()) {
            double d1 = myView.getExtentSize().width;
            double d2 = myView.getPrintDocumentSize().width;
            double d3 = myView.getExtentSize().height;
            double d4 = myView.getPrintDocumentSize().height;
            d = Math.min(d1 / d2, d3 / d4);
        }
        if (d > 2D) {
            d = 1.0D;
        }
        d *= myView.getScale();
        myView.setScale(d);
        myView.setViewPosition(0, 0);
        updateActions();
    }

    void zoomOutAction() {
        double d = Math.rint(myView.getScale() * 0.89999997615814209D * 100D) / 100D;
        myView.setScale(d);
        updateActions();
    }

    GraphicViewerPort textNodeAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(120, 20);
        }
        GraphicViewerTextNode graphicviewertextnode = new GraphicViewerTextNode(
                "text node");
        graphicviewertextnode.setTopLeft(point);
        graphicviewertextnode.setBrush(GraphicViewerBrush
                .makeStockBrush(Color.pink));
        myMainLayer.addObjectAtTail(graphicviewertextnode);
        endTransaction(InsertTextNodeAction.toString());
        return graphicviewertextnode.getBottomPort();
    }

    // Align all object's right sides to the right side of the primary
    // selection.
    void rightAction() {
        startTransaction();
        GraphicViewerSelection graphicviewerselection = myView.getSelection();
        GraphicViewerObject graphicviewerobject = graphicviewerselection
                .getPrimarySelection();
        GraphicViewerListPosition graphicviewerlistposition;
        for (graphicviewerlistposition = graphicviewerselection
                .getFirstObjectPos(); graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerLink)
                && graphicviewerlistposition != null; graphicviewerlistposition = graphicviewerselection
                .getNextObjectPos(graphicviewerlistposition)) {
            graphicviewerobject = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
        }

        if (graphicviewerobject == null) {
            return;
        }
        Point point = graphicviewerobject.getSpotLocation(3);
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject1 = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerselection
                    .getNextObjectPos(graphicviewerlistposition);
            if (!(graphicviewerobject1 instanceof GraphicViewerLink)) {
                graphicviewerobject1.setSpotLocation(3, new Point(point.x,
                        graphicviewerobject1.getTop()));
            }
        } while (true);
        endTransaction(RightAction.toString());
    }

    void inspectAction() {
        Inspector.inspect(myView.getSelection().getPrimarySelection());
    }

    // This action only works on the primary selection, which should be a
    // GraphicViewerSubGraph
    void ungroupAction() {
        GraphicViewerSelection graphicviewerselection = myView.getSelection();
        GraphicViewerObject graphicviewerobject = graphicviewerselection
                .getPrimarySelection();
        if (graphicviewerobject == null) {
            return;
        }
        if (!(graphicviewerobject instanceof GraphicViewerArea)) {
            return;
        }
        GraphicViewerLayer graphicviewerlayer = graphicviewerobject.getLayer();
        if (graphicviewerlayer == null) {
            return;
        }
        startTransaction();
        GraphicViewerArea graphicviewerarea = (GraphicViewerArea) graphicviewerobject;
        ArrayList<?> arraylist = null;

        // Special cases for subgraphs--don't promote Handle or Label or other
        // special subgraph children as stand-alone objects
        if (graphicviewerarea instanceof GraphicViewerSubGraph) {
            GraphicViewerSubGraph graphicviewersubgraph = (GraphicViewerSubGraph) graphicviewerarea;
            ArrayList<GraphicViewerObject> arraylist1 = new ArrayList<GraphicViewerObject>();
            GraphicViewerListPosition graphicviewerlistposition = graphicviewersubgraph
                    .getFirstObjectPos();
            do {
                if (graphicviewerlistposition == null) {
                    break;
                }
                GraphicViewerObject graphicviewerobject2 = graphicviewersubgraph
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewersubgraph
                        .getNextObjectPosAtTop(graphicviewerlistposition);
                // Check for special subgraph children
                if (graphicviewerobject2 != graphicviewersubgraph.getHandle()
                        && graphicviewerobject2 != graphicviewersubgraph
                                .getLabel()
                        && !(graphicviewerobject2 instanceof GraphicViewerPort)
                        && graphicviewerobject2 != graphicviewersubgraph
                                .getCollapsedObject()) {
                    arraylist1.add(graphicviewerobject2);
                }
            } while (true);
            arraylist = graphicviewerlayer.addCollection(arraylist1, true,
                    myMainLayer);
        } else {
            arraylist = graphicviewerlayer.addCollection(graphicviewerarea,
                    false, myMainLayer);
        }
        for (int i = 0; i < arraylist.size(); i++) {
            GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) arraylist
                    .get(i);
            // Update the selectability and visibility, just in case
            graphicviewerobject1.setSelectable(true);
            graphicviewerobject1.setVisible(true);
            // Update the selection too, for the user's convenience
            graphicviewerselection.extendSelection(graphicviewerobject1);
        }

        // Delete the area
        graphicviewerlayer.removeObject(graphicviewerarea);
        endTransaction(UngroupAction.toString());
    }

    // This makes the widths of all objects equal to the width of the main
    // selection.
    void sameWidthAction() {
        startTransaction();
        GraphicViewerSelection graphicviewerselection = myView.getSelection();
        GraphicViewerObject graphicviewerobject = graphicviewerselection
                .getPrimarySelection();
        GraphicViewerListPosition graphicviewerlistposition;
        for (graphicviewerlistposition = graphicviewerselection
                .getFirstObjectPos(); graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerLink)
                && graphicviewerlistposition != null; graphicviewerlistposition = graphicviewerselection
                .getNextObjectPos(graphicviewerlistposition)) {
            graphicviewerobject = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
        }

        if (graphicviewerobject == null) {
            return;
        }
        int i = graphicviewerobject.getWidth();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject1 = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerselection
                    .getNextObjectPos(graphicviewerlistposition);
            if (graphicviewerobject1.isTopLevel()
                    && !(graphicviewerobject1 instanceof GraphicViewerLink)) {
                graphicviewerobject1.setWidth(i);
            }
        } while (true);
        endTransaction(SameWidthAction.toString());
    }

    // Align all object's tops to the top of the primary selection.
    void topAction() {
        startTransaction();
        GraphicViewerSelection graphicviewerselection = myView.getSelection();
        GraphicViewerObject graphicviewerobject = graphicviewerselection
                .getPrimarySelection();
        GraphicViewerListPosition graphicviewerlistposition;
        for (graphicviewerlistposition = graphicviewerselection
                .getFirstObjectPos(); graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerLink)
                && graphicviewerlistposition != null; graphicviewerlistposition = graphicviewerselection
                .getNextObjectPos(graphicviewerlistposition)) {
            graphicviewerobject = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
        }

        if (graphicviewerobject == null) {
            return;
        }
        Point point = graphicviewerobject.getSpotLocation(2);
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject1 = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerselection
                    .getNextObjectPos(graphicviewerlistposition);
            if (!(graphicviewerobject1 instanceof GraphicViewerLink)) {
                graphicviewerobject1.setSpotLocation(1, new Point(
                        graphicviewerobject1.getLeft(), point.y));
            }
        } while (true);
        endTransaction(TopAction.toString());
    }

    void gridAction() {
        try {
            GridOptionsDialog gridoptionsdialog = new GridOptionsDialog(this,
                    "Grid View Options", true, myView);
            UISupport.showDialog(gridoptionsdialog);
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
    }

    // Align all object's horizontal centers to the horizontal center of the
    // primary selection.
    void horizontalAction() {
        startTransaction();
        GraphicViewerSelection graphicviewerselection = myView.getSelection();
        GraphicViewerObject graphicviewerobject = graphicviewerselection
                .getPrimarySelection();
        GraphicViewerListPosition graphicviewerlistposition;
        for (graphicviewerlistposition = graphicviewerselection
                .getFirstObjectPos(); graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerLink)
                && graphicviewerlistposition != null; graphicviewerlistposition = graphicviewerselection
                .getNextObjectPos(graphicviewerlistposition)) {
            graphicviewerobject = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
        }

        if (graphicviewerobject == null) {
            return;
        }
        Point point = graphicviewerobject.getSpotLocation(0);
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject1 = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerselection
                    .getNextObjectPos(graphicviewerlistposition);
            if (!(graphicviewerobject1 instanceof GraphicViewerLink)) {
                graphicviewerobject1.setSpotLocation(2, new Point(point.x,
                        graphicviewerobject1.getTop()));
            }
        } while (true);
        endTransaction(HorizontalAction.toString());
    }

    void zoomInAction() {
        double d = Math.rint((myView.getScale() / 0.89999997615814209D) * 100D) / 100D;
        myView.setScale(d);
        updateActions();
    }

    // Make a GraphicViewerNode out of the current selection.
    void groupAction() {
        startTransaction();
        GraphicViewerSelection graphicviewerselection = myView.getSelection();
        GraphicViewerObject graphicviewerobject = graphicviewerselection
                .getPrimarySelection();
        GraphicViewerNode graphicviewernode = new GraphicViewerNode();
        // Now add the area to the document
        graphicviewerobject.getLayer().addObjectAtTail(graphicviewernode);
        // Add the selected objects to the area
        ArrayList<GraphicViewerObject> arraylist = graphicviewernode
                .addCollection(graphicviewerselection, false, myMainLayer);
        // Update the selection too, for the user's convenience
        graphicviewerselection.selectObject(graphicviewernode);
        // Make each object not Selectable
        for (int i = 0; i < arraylist.size(); i++) {
            GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) arraylist
                    .get(i);
            graphicviewerobject1.setSelectable(false);
        }

        endTransaction(GroupAction.toString());
    }

    GraphicViewerSubGraph makeSubGraph(String s, String s1) {
        GraphicViewerBasicNode graphicviewerbasicnode = new GraphicViewerBasicNode(
                s1 + "a");
        graphicviewerbasicnode.setLocation(new Point(30, 0));
        GraphicViewerBasicNode graphicviewerbasicnode1 = new GraphicViewerBasicNode(
                s1 + "b");
        graphicviewerbasicnode1.setLocation(new Point(0, 60));
        GraphicViewerBasicNode graphicviewerbasicnode2 = new GraphicViewerBasicNode(
                s1 + "c");
        graphicviewerbasicnode2.setLocation(new Point(60, 60));
        GraphicViewerLink graphicviewerlink = new GraphicViewerLink(
                graphicviewerbasicnode.getPort(),
                graphicviewerbasicnode1.getPort());
        GraphicViewerLink graphicviewerlink1 = new GraphicViewerLink(
                graphicviewerbasicnode.getPort(),
                graphicviewerbasicnode2.getPort());
        GraphicViewerSubGraph graphicviewersubgraph = new GraphicViewerSubGraph(
                s);
        graphicviewersubgraph.addObjectAtTail(graphicviewerbasicnode);
        graphicviewersubgraph.addObjectAtTail(graphicviewerbasicnode1);
        graphicviewersubgraph.addObjectAtTail(graphicviewerbasicnode2);
        graphicviewersubgraph.addObjectAtTail(graphicviewerlink);
        graphicviewersubgraph.addObjectAtTail(graphicviewerlink1);
        return graphicviewersubgraph;
    }

    // Create a GeneralNode with a random number of input and output ports
    void generalNodeAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(10, 230);
        }
        GeneralNode generalnode = new GeneralNode();
        GraphicViewerImage graphicviewerimage = new GraphicViewerImage(
                new Rectangle(0, 0, 50, 50));
        graphicviewerimage.loadImage(
                (GraphicViewer.class).getResource("document.png"), true);
        generalnode.initialize(point, new Dimension(50, 50),
                graphicviewerimage, "top", "general node",
                (int) (getRandom() * 5D), (int) (getRandom() * 5D));
        myMainLayer.addObjectAtTail(generalnode);
        endTransaction(InsertGeneralNodeAction.toString());
    }

    void pasteAction() {
        startTransaction();
        myView.paste();
        endTransaction(PasteAction.toString());
    }

    GraphicViewerPort roundTextNodeAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(320, 10);
        }
        GraphicViewerTextNode graphicviewertextnode = new GraphicViewerTextNode(
                "rounded text node\ndisplaying three lines\nwith wider insets");
        graphicviewertextnode.setTopLeft(new Point(point.x + 40, point.y));
        graphicviewertextnode.getLabel().setEditable(true);
        GraphicViewerRoundRect graphicviewerroundrect = new GraphicViewerRoundRect(
                new Dimension(10, 10));
        graphicviewerroundrect.setBrush(GraphicViewerBrush.white);
        graphicviewertextnode.setBackground(graphicviewerroundrect);
        graphicviewertextnode.setInsets(new Insets(4, 10, 4, 10));
        myMainLayer.addObjectAtTail(graphicviewertextnode);
        endTransaction(InsertRoundTextNodeAction.toString());
        return graphicviewertextnode.getTopPort();
    }

    void strokeAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(10, 10);
        }
        GraphicViewerStroke graphicviewerstroke = new GraphicViewerStroke();
        graphicviewerstroke.addPoint(new Point(point.x + 0, point.y + 0));
        graphicviewerstroke.addPoint(new Point(point.x + 68, point.y + 46));
        graphicviewerstroke.addPoint(new Point(point.x + 68, point.y + 90));
        graphicviewerstroke.addPoint(new Point(point.x + 53, point.y + 57));
        graphicviewerstroke.addPoint(new Point(point.x + 85, point.y + 34));
        myMainLayer.addObjectAtTail(graphicviewerstroke);
        endTransaction(InsertStrokeAction.toString());
    }

    void toggleLinkIconicNodesAction() {
        myIconicNodesAreLinkable = !myIconicNodesAreLinkable;
        GraphicViewerDocument graphicviewerdocument = myView.getDocument();
        GraphicViewerListPosition graphicviewerlistposition = graphicviewerdocument
                .getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject = graphicviewerdocument
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerdocument
                    .getNextObjectPosAtTop(graphicviewerlistposition);
            if (graphicviewerobject instanceof GraphicViewerIconicNode) {
                GraphicViewerIconicNode graphicviewericonicnode = (GraphicViewerIconicNode) graphicviewerobject;
                graphicviewericonicnode.getPort().setVisible(
                        myIconicNodesAreLinkable);
            }
        } while (true);
    }

    // This makes the heights and widths of all objects equal to the height and
    // width of the main selection.
    void sameBothAction() {
        startTransaction();
        sameHeightAction();
        sameWidthAction();
        endTransaction(SameBothAction.toString());
    }

    // This enables/disables all known actions.
    public static void updateActions() {
        AppAction.updateAllActions();
    }

    // This makes the heights of all objects equal to the height of the main
    // selection.
    void sameHeightAction() {
        startTransaction();
        GraphicViewerSelection graphicviewerselection = myView.getSelection();
        GraphicViewerObject graphicviewerobject = graphicviewerselection
                .getPrimarySelection();
        GraphicViewerListPosition graphicviewerlistposition;
        for (graphicviewerlistposition = graphicviewerselection
                .getFirstObjectPos(); graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerLink)
                && graphicviewerlistposition != null; graphicviewerlistposition = graphicviewerselection
                .getNextObjectPos(graphicviewerlistposition)) {
            graphicviewerobject = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
        }

        if (graphicviewerobject == null) {
            return;
        }
        int i = graphicviewerobject.getHeight();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject1 = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerselection
                    .getNextObjectPos(graphicviewerlistposition);
            if (graphicviewerobject1.isTopLevel()
                    && !(graphicviewerobject1 instanceof GraphicViewerLink)) {
                graphicviewerobject1.setHeight(i);
            }
        } while (true);
        endTransaction(SameHeightAction.toString());
    }

    void layeredDigraphAutoLayoutAction() {
        GraphicViewerDocument graphicviewerdocument = myView.getDocument();
        graphicviewerdocument.startTransaction();
        layoutCollection(graphicviewerdocument, graphicviewerdocument);
        graphicviewerdocument.endTransaction("LayeredDigraph AutoLayout");
    }

    // Align all object's bottoms to the bottom of the primary selection.
    void bottomAction() {
        startTransaction();
        GraphicViewerSelection graphicviewerselection = myView.getSelection();
        GraphicViewerObject graphicviewerobject = graphicviewerselection
                .getPrimarySelection();
        GraphicViewerListPosition graphicviewerlistposition;
        for (graphicviewerlistposition = graphicviewerselection
                .getFirstObjectPos(); graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerLink)
                && graphicviewerlistposition != null; graphicviewerlistposition = graphicviewerselection
                .getNextObjectPos(graphicviewerlistposition)) {
            graphicviewerobject = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
        }

        if (graphicviewerobject == null) {
            return;
        }
        Point point = graphicviewerobject.getSpotLocation(6);
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject1 = graphicviewerselection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerselection
                    .getNextObjectPos(graphicviewerlistposition);
            if (!(graphicviewerobject1 instanceof GraphicViewerLink)) {
                graphicviewerobject1.setSpotLocation(7, new Point(
                        graphicviewerobject1.getLeft(), point.y));
            }
        } while (true);
        endTransaction(BottomAction.toString());
    }

    protected double getRandom() {
        if (isRandom()) {
            return Math.random();
        } else {
            return 0.56999999999999995D;
        }
    }

    public static OTVDocument loadObjects(InputStream inputstream)
            throws IOException, ClassNotFoundException {
        ObjectInputStream objectinputstream = new ObjectInputStream(inputstream);
        Object obj = objectinputstream.readObject();
        if (obj instanceof GraphicViewerDocument) {
            OTVDocument graphicviewerdocument = (OTVDocument) obj;
            return graphicviewerdocument;
        } else {
            return null;
        }
    }

    //==============================================================
    // Process GraphicViewerViewEvents from the GraphicViewerView
    //==============================================================
    public void processViewChange(GraphicViewerViewEvent e) {
        // If the selection changed, maybe some commands need to be disabled or
        // re-enabled
        switch (e.getHint()) {
            case GraphicViewerViewEvent.UPDATE_ALL :
            case GraphicViewerViewEvent.SELECTION_GAINED :
            case GraphicViewerViewEvent.SELECTION_LOST :
                UpdateControls();
                break;
            case GraphicViewerViewEvent.SELECTION_STARTING :
                myUpdatingSelection = true;
                break;
            case GraphicViewerViewEvent.SELECTION_FINISHED :
                myUpdatingSelection = false;
                UpdateControls();
                break;
            case GraphicViewerViewEvent.SCALE_CHANGED :
                myView.updateBorder();
                break;
        }
    }

    void multiSpotNodeAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(280, 175);
        }
        GraphicViewerNode graphicviewernode = new GraphicViewerNode();
        graphicviewernode.setFlags(graphicviewernode.getFlags() | 0x10000);
        GraphicViewerRectangle graphicviewerrectangle = new GraphicViewerRectangle(
                point, new Dimension(50, 50));
        graphicviewerrectangle.setSelectable(false);
        graphicviewerrectangle.setBrush(GraphicViewerBrush
                .makeStockBrush(Color.gray));
        graphicviewernode.addObjectAtHead(graphicviewerrectangle);
        addMultiSpotPort(graphicviewernode, graphicviewerrectangle, 0);
        addMultiSpotPort(graphicviewernode, graphicviewerrectangle, 1);
        addMultiSpotPort(graphicviewernode, graphicviewerrectangle, 2);
        addMultiSpotPort(graphicviewernode, graphicviewerrectangle, 3);
        addMultiSpotPort(graphicviewernode, graphicviewerrectangle, 4);
        addMultiSpotPort(graphicviewernode, graphicviewerrectangle, 5);
        addMultiSpotPort(graphicviewernode, graphicviewerrectangle, 6);
        addMultiSpotPort(graphicviewernode, graphicviewerrectangle, 7);
        addMultiSpotPort(graphicviewernode, graphicviewerrectangle, 8);
        myMainLayer.addObjectAtTail(graphicviewernode);
        endTransaction(InsertMultiSpotNodeAction.toString());
    }

    void multiTextNodeAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(370, 160);
        }
        MultiTextNode multitextnode = new MultiTextNode();
        multitextnode.initialize();
        multitextnode.setTopLeft(point);
        GraphicViewerText graphicviewertext = (GraphicViewerText) multitextnode
                .addString("MultiTextNode");
        graphicviewertext.setAlignment(2);
        graphicviewertext = (GraphicViewerText) multitextnode
                .addString("second line");
        graphicviewertext.setAlignment(2);
        graphicviewertext.setBold(true);
        graphicviewertext = (GraphicViewerText) multitextnode
                .addString("third line");
        graphicviewertext.setAlignment(2);
        multitextnode.setLinePen(GraphicViewerPen.darkGray);
        //GraphicViewerRoundRect rr = new GraphicViewerRoundRect();
        //rr.setSelectable(false);
        //rr.setArcDimension(new Dimension(20, 20));
        //tnode.setBackground(rr);
        //tnode.setInsets(new Insets(5, 5, 5, 5));  // Make room for corners
        multitextnode.setInsets(new Insets(0, 2, 0, 2)); // Add space on sides
        multitextnode.setItemWidth(100);
        myForegroundLayer.addObjectAtTail(multitextnode);
        endTransaction(InsertMultiTextNodeAction.toString());
    }

    void addRightPortAction() {
        GraphicViewerObject graphicviewerobject = myView.getSelection()
                .getPrimarySelection();
        if (graphicviewerobject != null
                && (graphicviewerobject.getParentNode() instanceof GeneralNode)) {
            startTransaction();
            GeneralNode generalnode = (GeneralNode) graphicviewerobject
                    .getParentNode();
            String s = "out" + Integer.toString(generalnode.getNumRightPorts());
            GeneralNodePort generalnodeport = new GeneralNodePort(false, s,
                    generalnode);
            new GeneralNodePortLabel(s, generalnodeport);
            generalnode.addRightPort(generalnodeport);
            endTransaction(AddRightPortAction.toString());
        }
    }

    public GraphicViewerPort selfLoopBasicNodeAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(270, 100);
        }
        GraphicViewerBasicNode graphicviewerbasicnode = new GraphicViewerBasicNode(
                "self loop");
        graphicviewerbasicnode.setInsets(new Insets(10, 18, 10, 18));
        graphicviewerbasicnode.getLabel().setMultiline(true);
        graphicviewerbasicnode.setLabelSpot(0);
        graphicviewerbasicnode.setLocation(point);
        graphicviewerbasicnode.setBrush(GraphicViewerBrush
                .makeStockBrush(new Color(128, 0, 128)));
        graphicviewerbasicnode.getLabel().setBold(true);
        graphicviewerbasicnode.getLabel().setTextColor(Color.white);
        graphicviewerbasicnode.getPort().setValidSelfNode(true);
        myMainLayer.addObjectAtTail(graphicviewerbasicnode);
        GraphicViewerLink graphicviewerlink = new GraphicViewerLink(
                graphicviewerbasicnode.getPort(),
                graphicviewerbasicnode.getPort());
        graphicviewerlink.setArrowShaftLength(0.0D);
        graphicviewerlink.setArrowLength(6D);
        graphicviewerlink.setArrowWidth(8D);
        graphicviewerlink.setPen(GraphicViewerPen.makeStockPen(new Color(128,
                0, 128)));
        myMainLayer.addObjectAtTail(graphicviewerlink);
        graphicviewerlink.calculateStroke();
        endTransaction(InsertSelfLoopBasicNodeAction.toString());
        return graphicviewerbasicnode.getPort();
    }

    void addLeftPortAction() {
        GraphicViewerObject graphicviewerobject = myView.getSelection()
                .getPrimarySelection();
        if (graphicviewerobject != null
                && (graphicviewerobject.getParentNode() instanceof GeneralNode)) {
            startTransaction();
            GeneralNode generalnode = (GeneralNode) graphicviewerobject
                    .getParentNode();
            String s = "in" + Integer.toString(generalnode.getNumLeftPorts());
            GeneralNodePort generalnodeport = new GeneralNodePort(true, s,
                    generalnode);
            new GeneralNodePortLabel(s, generalnodeport);
            generalnode.addLeftPort(generalnodeport);
            endTransaction(AddLeftPortAction.toString());
        }
    }

    public void recordNodeAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(500, 100);
        }
        RecordNode recordnode = makeRecordNode(true, myRNorg);
        recordnode.setTopLeft(point.x + 200, point.y);
        recordnode.setHeight(150);
        recordnode.setLinePen(GraphicViewerPen.gray);
        recordnode.setSpacing(3);
        RecordNode recordnode1 = makeRecordNode(false, myRNorg);
        recordnode1.setTopLeft(point);
        recordnode1.setHeight(150);
        myRNorg = !myRNorg;
        GraphicViewerPort graphicviewerport = recordnode1.getRightPort(5);
        GraphicViewerPort graphicviewerport1 = recordnode1.getRightPort(6);
        GraphicViewerPort graphicviewerport2 = recordnode1.getRightPort(7);
        GraphicViewerPort graphicviewerport3 = recordnode1.getRightPort(8);
        GraphicViewerPort graphicviewerport4 = recordnode.getLeftPort(5);
        GraphicViewerPort graphicviewerport5 = recordnode.getLeftPort(10);
        GraphicViewerPort graphicviewerport6 = recordnode.getLeftPort(0);
        GraphicViewerPort graphicviewerport7 = recordnode.getLeftPort(19);
        if (graphicviewerport != null && graphicviewerport4 != null) {
            myForegroundLayer.addObjectAtTail(new GraphicViewerLink(
                    graphicviewerport, graphicviewerport4));
        }
        GraphicViewerImage graphicviewerimage = new GraphicViewerImage();
        graphicviewerimage.loadImage(
                (GraphicViewer.class).getResource("star.gif"), true);
        if (graphicviewerport4 != null) {
            graphicviewerport4.setSize(7, 7);
            graphicviewerport4.setStyle(1);
            graphicviewerport4.setPortObject(graphicviewerimage);
        }
        if (graphicviewerport1 != null && graphicviewerport5 != null) {
            myForegroundLayer.addObjectAtTail(new GraphicViewerLink(
                    graphicviewerport1, graphicviewerport5));
        }
        if (graphicviewerport5 != null) {
            graphicviewerport5.setSize(7, 7);
            graphicviewerport5.setPortObject(graphicviewerimage);
            graphicviewerport5.setStyle(1);
        }
        if (graphicviewerport2 != null && graphicviewerport6 != null) {
            myForegroundLayer.addObjectAtTail(new GraphicViewerLink(
                    graphicviewerport2, graphicviewerport6));
        }
        if (graphicviewerport6 != null) {
            graphicviewerport6.setSize(7, 7);
            graphicviewerport6.setPortObject(graphicviewerimage);
            graphicviewerport6.setStyle(1);
        }
        if (graphicviewerport3 != null && graphicviewerport7 != null) {
            myForegroundLayer.addObjectAtTail(new GraphicViewerLink(
                    graphicviewerport3, graphicviewerport7));
        }
        if (graphicviewerport7 != null) {
            graphicviewerport7.setSize(7, 7);
            graphicviewerport7.setPortObject(graphicviewerimage);
            graphicviewerport7.setStyle(1);
        }
        if (graphicviewerport != null && graphicviewerport6 != null) {
            myForegroundLayer.addObjectAtTail(new GraphicViewerLink(
                    graphicviewerport, graphicviewerport6));
        }
        GraphicViewerPort graphicviewerport8 = recordnode1.getRightPort(13);
        if (graphicviewerport8 != null) {
            graphicviewerport8.setSize(9, 9);
            graphicviewerport8.setStyle(5);
            graphicviewerport8.setBrush(GraphicViewerBrush
                    .makeStockBrush(Color.magenta));
        }
        endTransaction(InsertRecordNodeAction.toString());
    }

    void cutAction() {
        startTransaction();
        myView.cut();
        endTransaction(CutAction.toString());
    }

    GraphicViewerPort fixedSizeTextNodeAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(290, 230);
        }
        GraphicViewerTextNode graphicviewertextnode = new GraphicViewerTextNode(
                "resizable text node with AutoResize false");
        graphicviewertextnode.setResizable(true);
        graphicviewertextnode.setAutoResize(false);
        graphicviewertextnode.setTopLeft(point);
        graphicviewertextnode.setSize(100, 100);
        graphicviewertextnode.setBrush(GraphicViewerBrush
                .makeStockBrush(Color.CYAN));
        graphicviewertextnode.getLabel().setEditable(true);
        graphicviewertextnode.getLabel().setAlignment(2);
        myMainLayer.addObjectAtTail(graphicviewertextnode);
        endTransaction(InsertTextNodeAction.toString());
        return graphicviewertextnode.getLeftPort();
    }

    public GraphicViewerPort basicNodeAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(40, 140);
        }
        GraphicViewerBasicNode graphicviewerbasicnode = new GraphicViewerBasicNode(
                Integer.toString(basicNodeCounter));
        if (basicNodeCounter % 18 >= 9) {
            GraphicViewerRectangle graphicviewerrectangle = new GraphicViewerRectangle();
            graphicviewerrectangle.setSize(20, 20);
            graphicviewerbasicnode.setDrawable(graphicviewerrectangle);
        }
        graphicviewerbasicnode.setLocation(point);
        graphicviewerbasicnode.setBrush(GraphicViewerBrush
                .makeStockBrush(GraphicViewerBrush.ColorOrange));
        graphicviewerbasicnode.setLabelSpot(basicNodeCounter % 9);
        myMainLayer.addObjectAtTail(graphicviewerbasicnode);
        basicNodeCounter++;
        endTransaction(InsertBasicNodeAction.toString());
        return graphicviewerbasicnode.getPort();
    }

    GraphicViewerPort fixedSizeCenterLabelBasicNodeAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(340, 350);
        }
        GraphicViewerBasicNode graphicviewerbasicnode = new GraphicViewerBasicNode(
                "resizable basic node with AutoResize false");
        graphicviewerbasicnode.setInsets(new Insets(10, 18, 10, 18));
        graphicviewerbasicnode.getLabel().setMultiline(true);
        graphicviewerbasicnode.setLabelSpot(0);
        graphicviewerbasicnode.setBrush(GraphicViewerBrush
                .makeStockBrush(Color.PINK));
        graphicviewerbasicnode.setLocation(point);
        graphicviewerbasicnode.setResizable(true);
        graphicviewerbasicnode.setAutoResize(false);
        graphicviewerbasicnode.setSize(200, 50);
        graphicviewerbasicnode.getLabel().setEditable(true);
        myMainLayer.addObjectAtTail(graphicviewerbasicnode);
        endTransaction(InsertFixedSizeCenterLabelBasicNodeAction.toString());
        return graphicviewerbasicnode.getPort();
    }

    void diamondAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(175, 100);
        }
        Diamond diamond = new Diamond(point, new Dimension(70, 40));
        diamond.setPen(GraphicViewerPen.make(65535, 3, Color.magenta));
        myMainLayer.addObjectAtTail(diamond);
        endTransaction(InsertDiamondAction.toString());
    }

    public void ellipseAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(100, 250);
        }
        GraphicViewerEllipse graphicviewerellipse = new GraphicViewerEllipse(
                point, new Dimension(50, 75));
        graphicviewerellipse.setBrush(GraphicViewerBrush
                .makeStockBrush(Color.green));
        myMainLayer.addObjectAtTail(graphicviewerellipse);
        endTransaction(InsertEllipseAction.toString());
    }

    void changeLayersAction() {
        startTransaction();
        GraphicViewerSelection graphicviewerselection = myView.getSelection();
        if (graphicviewerselection.isEmpty()) {
            myMainLayer.setTransparency((float) getRandom());
            myBackgroundLayer.setVisible(getRandom() >= 0.5D);
        } else {
            // Find which layer the selection is in (well, the first object in
            // the selection)
            GraphicViewerObject graphicviewerobject = graphicviewerselection
                    .getObjectAtPos(graphicviewerselection.getFirstObjectPos());
            GraphicViewerLayer graphicviewerlayer = graphicviewerobject
                    .getLayer();
            // Let's choose the layer behind that one
            graphicviewerlayer = graphicviewerlayer.getPrevLayer();
            if (graphicviewerlayer == null) {
                graphicviewerlayer = myForegroundLayer;
            }
            // Now move all the selected objects to that layer
            for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerselection
                    .getFirstObjectPos(); graphicviewerlistposition != null;) {
                GraphicViewerObject graphicviewerobject1 = graphicviewerselection
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewerselection
                        .getNextObjectPosAtTop(graphicviewerlistposition);
                graphicviewerlayer.addObjectAtTail(graphicviewerobject1);
            }

            myBackgroundLayer.setVisible(true);
        }
        endTransaction(ChangeLayersAction.toString());
    }

    GraphicViewerPort rectBasicNodeAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(90, 350);
        }
        GraphicViewerBasicNode graphicviewerbasicnode = new GraphicViewerBasicNode(
                "rectangular basic node");
        graphicviewerbasicnode.setLabelSpot(0);
        graphicviewerbasicnode.setDrawable(new GraphicViewerRectangle());
        graphicviewerbasicnode.setBrush(GraphicViewerBrush
                .makeStockBrush(new Color(192, 0, 192)));
        graphicviewerbasicnode.setLocation(point);
        myMainLayer.addObjectAtTail(graphicviewerbasicnode);
        endTransaction(InsertRectBasicNodeAction.toString());
        return graphicviewerbasicnode.getPort();
    }

    void roundedRectangleAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(180, 150);
        }
        OTVRoundRect OTVRoundRect = new OTVRoundRect(point, new Dimension(80,
                80), new Dimension(15, 30));
        OTVRoundRect.setPen(GraphicViewerPen.make(4, 3, Color.darkGray));
        // Brush gets determined by GraphicViewerRoundRect
        myMainLayer.addObjectAtTail(OTVRoundRect);
        endTransaction(InsertRoundedRectangleAction.toString());
    }

    GraphicViewerPort draggableLabelIconicNodeAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(440, 110);
        }
        GraphicViewerIconicNode graphicviewericonicnode = new GraphicViewerIconicNode(
                "draggable label");
        // Replace the iconic node's standard image with a new one displaying a
        // GIF
        GraphicViewerImage graphicviewerimage = new GraphicViewerImage(
                new Rectangle(0, 0, 50, 50));
        graphicviewerimage.loadImage(
                (GraphicViewer.class).getResource("document.png"), true);
        graphicviewericonicnode.setIcon(graphicviewerimage);
        graphicviewericonicnode.setLocation(point);
        graphicviewericonicnode.setDraggableLabel(true);
        myMainLayer.addObjectAtTail(graphicviewericonicnode);
        endTransaction(InsertDraggableLabelIconicNodeAction.toString());
        return graphicviewericonicnode.getPort();
    }

    void multiPortNodeAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(10, 170);
        }
        MultiPortNode multiportnode = new MultiPortNode();
        GraphicViewerImage graphicviewerimage = new GraphicViewerImage(
                new Rectangle(0, 0, 40, 40));
        graphicviewerimage.loadImage(
                (GraphicViewer.class).getResource("document.png"), true);
        multiportnode.initialize(point, graphicviewerimage, "multiport node");
        int i = (int) (getRandom() * 5D) + 1;
        for (int j = 0; j < i; j++) {
            Point point1 = new Point(8 * ((int) (getRandom() * 6D) - 1),
                    8 * ((int) (getRandom() * 6D) - 1));
            byte byte0 = 0;
            if (point1.x > point1.y) {
                if (point1.x < 40 - point1.y) {
                    byte0 = 2;
                } else {
                    byte0 = 4;
                }
            } else if (point1.x > 40 - point1.y) {
                byte0 = 6;
            } else {
                byte0 = 8;
            }
            new MultiPortNodePort(true, true, byte0, point1,
                    new Dimension(8, 8), graphicviewerimage, multiportnode);
        }

        myMainLayer.addObjectAtTail(multiportnode);
        endTransaction(InsertMultiPortNodeAction.toString());
    }

    public void stuffAction() {
        startTransaction();
        Point point = getDefaultLocation();
        setDefaultLocation(null); // Position everything at absolute default
                                  // locations
        GraphicViewerPort graphicviewerport2 = fixedSizeCenterLabelBasicNodeAction();
        GraphicViewerPort graphicviewerport4 = fixedSizeTextNodeAction();
        multiPortNodeAction();
        simpleNodeAction();
        GraphicViewerPort graphicviewerport = iconicNodeAction();
        GraphicViewerPort graphicviewerport1 = draggableLabelIconicNodeAction();
        GraphicViewerPort graphicviewerport3 = rectBasicNodeAction();
        basicNodeAction();
        selfLoopBasicNodeAction();
        multiSpotNodeAction();
        generalNodeAction();
        textNodeAction();
        centerLabelBasicNodeAction();
        GraphicViewerLink graphicviewerlink = new GraphicViewerLink();
        graphicviewerlink.setArrowHeads(false, true);
        graphicviewerlink.setBrush(GraphicViewerBrush
                .makeStockBrush(Color.magenta));
        graphicviewerlink.setCubic(true);
        graphicviewerlink.setFromPort(graphicviewerport);
        graphicviewerlink.setToPort(graphicviewerport1);
        myMainLayer.addObjectAtTail(graphicviewerlink);
        GraphicViewerLink graphicviewerlink1 = new GraphicViewerLink(
                graphicviewerport2, graphicviewerport4);
        myMainLayer.addObjectAtTail(graphicviewerlink1);
        GraphicViewerLabeledLink graphicviewerlabeledlink = new GraphicViewerLabeledLink(
                graphicviewerport3, graphicviewerport2);
        GraphicViewerLinkLabel graphicviewerlinklabel = new GraphicViewerLinkLabel(
                "mid");
        graphicviewerlabeledlink.setArrowHeads(false, true);
        graphicviewerlabeledlink.setArrowShaftLength(0.0D);
        graphicviewerlabeledlink.setMidLabel(graphicviewerlinklabel);
        graphicviewerlabeledlink.setBrush(null);
        myMainLayer.addObjectAtTail(graphicviewerlabeledlink);
        GraphicViewerImage graphicviewerimage = new GraphicViewerImage(
                new Rectangle(25, 375, 50, 50));
        graphicviewerimage.loadImage(
                (GraphicViewer.class).getResource("star.gif"), false);
        myDoc.addObjectAtTail(graphicviewerimage);
        TestIconicNode testiconicnode = new TestIconicNode("test");
        testiconicnode.getImage().loadImage(
                (GraphicViewer.class).getResource("star.gif"), false);
        testiconicnode.setTopLeft(10, 450);
        myDoc.addObjectAtTail(testiconicnode);
        TestSubGraph testsubgraph = new TestSubGraph("test subgraph");
        GraphicViewerImage graphicviewerimage1 = new GraphicViewerImage();
        graphicviewerimage1.loadImage(
                (GraphicViewer.class).getResource("star.gif"), true);
        graphicviewerimage1.setSize(40, 40);
        graphicviewerimage1.setSelectable(false);
        graphicviewerimage1.setDragsNode(true);
        graphicviewerimage1.setVisible(false);
        testsubgraph.setCollapsedObject(graphicviewerimage1);
        GraphicViewerBasicNode graphicviewerbasicnode = new GraphicViewerBasicNode(
                "bn1");
        graphicviewerbasicnode.setLocation(100, 500);
        testsubgraph.addObjectAtTail(graphicviewerbasicnode);
        GraphicViewerBasicNode graphicviewerbasicnode1 = new GraphicViewerBasicNode(
                "bn2");
        graphicviewerbasicnode1.setLocation(200, 500);
        testsubgraph.addObjectAtTail(graphicviewerbasicnode1);
        GraphicViewerLabeledLink graphicviewerlabeledlink1 = new GraphicViewerLabeledLink(
                graphicviewerbasicnode.getPort(),
                graphicviewerbasicnode1.getPort());
        GraphicViewerLinkLabel graphicviewerlinklabel1 = new GraphicViewerLinkLabel();
        graphicviewerlinklabel1.setText("middle");
        graphicviewerlabeledlink1.setMidLabel(graphicviewerlinklabel1);
        graphicviewerlabeledlink1.setArrowHeads(false, true);
        testsubgraph.addObjectAtTail(graphicviewerlabeledlink1);
        myDoc.addObjectAtTail(testsubgraph);
        TestSubGraph2 testsubgraph2 = new TestSubGraph2("test subgraph2");
        GraphicViewerImage graphicviewerimage2 = new GraphicViewerImage();
        graphicviewerimage2.loadImage(
                (GraphicViewer.class).getResource("star.gif"), true);
        graphicviewerimage2.setSize(40, 40);
        graphicviewerimage2.setSelectable(false);
        graphicviewerimage2.setDragsNode(true);
        graphicviewerimage2.setVisible(false);
        testsubgraph2.setCollapsedObject(graphicviewerimage2);
        GraphicViewerBasicNode graphicviewerbasicnode2 = new GraphicViewerBasicNode(
                "bn1");
        graphicviewerbasicnode2.setLocation(300, 500);
        testsubgraph2.addObjectAtTail(graphicviewerbasicnode2);
        GraphicViewerBasicNode graphicviewerbasicnode3 = new GraphicViewerBasicNode(
                "bn2");
        graphicviewerbasicnode3.setLocation(400, 500);
        testsubgraph2.addObjectAtTail(graphicviewerbasicnode3);
        GraphicViewerLink graphicviewerlink2 = new GraphicViewerLink(
                graphicviewerbasicnode2.getPort(),
                graphicviewerbasicnode3.getPort());
        graphicviewerlink2.setArrowHeads(false, true);
        testsubgraph2.addObjectAtTail(graphicviewerlink2);
        myDoc.addObjectAtTail(testsubgraph2);
        roundTextNodeAction();
        multiTextNodeAction();
        strokeAction();
        polygonAction();
        diamondAction();
        textAction();
        threeDRectAction();
        commentAction();
        insertSanKeyNodes();
        DecoratedTextNode decoratedtextnode = new DecoratedTextNode("decorated");
        decoratedtextnode.setLocation(123, 123);
        myDoc.addObjectAtTail(decoratedtextnode);
        LinearGradientEllipse lineargradientellipse = new LinearGradientEllipse();
        lineargradientellipse.setBoundingRect(100, 380, 100, 50);
        myDoc.addObjectAtTail(lineargradientellipse);
        rectangleAction();
        ellipseAction();
        roundedRectangleAction();
        setDefaultLocation(point);
        endTransaction(InsertStuffAction.toString());
    }

    public GraphicViewerPort iconicNodeAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(340, 110);
        }
        GraphicViewerIconicNode graphicviewericonicnode = new GraphicViewerIconicNode(
                "an iconic node");
        // Modify the iconic node's standard image to display a GIF
        graphicviewericonicnode.getImage().setSize(50, 50);
        graphicviewericonicnode.getImage().loadImage(
                (GraphicViewer.class).getResource("document.png"), true);
        graphicviewericonicnode.setLocation(point);
        myMainLayer.addObjectAtTail(graphicviewericonicnode);
        endTransaction(InsertIconicNodeAction.toString());
        return graphicviewericonicnode.getPort();
    }

    public void simpleNodeAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(100, 70);
        }
        SimpleNode simplenode = new SimpleNode();
        GraphicViewerImage graphicviewerimage = new GraphicViewerImage(
                new Rectangle(0, 0, 50, 50));
        graphicviewerimage.loadImage(
                (GraphicViewer.class).getResource("document.png"), true);
        simplenode.initialize(point, new Dimension(50, 50), graphicviewerimage,
                "a simple node", true, true);
        myMainLayer.addObjectAtTail(simplenode);
        endTransaction(InsertSimpleNodeAction.toString());
    }

    public void rectangleAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(180, 250);
        }
        GraphicViewerRectangle graphicviewerrectangle = new GraphicViewerRectangle(
                point, new Dimension(75, 75));
        graphicviewerrectangle.setBrush(GraphicViewerBrush
                .makeStockBrush(Color.red));
        myMainLayer.addObjectAtTail(graphicviewerrectangle);
        endTransaction(InsertRectangleAction.toString());
    }

    GraphicViewerPort centerLabelBasicNodeAction() {
        startTransaction();
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(270, 30);
        }
        GraphicViewerBasicNode graphicviewerbasicnode = new GraphicViewerBasicNode(
                "basic node\ncenter label");
        graphicviewerbasicnode.setInsets(new Insets(10, 18, 10, 18));
        graphicviewerbasicnode.getLabel().setMultiline(true);
        graphicviewerbasicnode.setLabelSpot(0);
        graphicviewerbasicnode.setBrush(GraphicViewerBrush
                .makeStockBrush(new Color(144, 238, 144)));
        graphicviewerbasicnode.setPen(GraphicViewerPen.makeStockPen(new Color(
                255, 20, 147)));
        graphicviewerbasicnode.setLocation(point);
        myMainLayer.addObjectAtTail(graphicviewerbasicnode);
        endTransaction(InsertCenterLabelBasicNodeAction.toString());
        return graphicviewerbasicnode.getPort();
    }

    void openProcess() {
        OTVDocument doc = myView.getDocument();
        String defaultLoc = doc.getLocation();
        doc = open(this, defaultLoc);
        if (doc != null) {
            myDoc = doc;
            setTitle(myDoc.getName() + " - " + Main.APPLICATION_NAME + " ( "
                    + this + " )");
            myDoc.setUndoManager(myUndoMgr);
            myDoc.setDefaultLayer(myDoc.getLastLayer());
            myDoc.setLinksLayer(myDoc.getLastLayer());
            myView.setDocument(myDoc);
            myView.requestFocus();

            if (myOverviewDialog != null && myOverviewDialog.isVisible()) {
                myOverview.setObserved(myView);
                myOverview.repaint();
                myDoc.addDocumentListener(myOverview);
            }

            AppAction.updateAllActions();
        }
    }

    void saveAsProcess() {
        if (getCurrentView() != null) {
            getCurrentView().getDocument();
            saveAs(".wfl");
        }
    }

    public void storeObjects(OutputStream outputstream) throws IOException {
        ObjectOutputStream objectoutputstream = new ObjectOutputStream(
                outputstream);
        objectoutputstream.writeObject(getCurrentView().getDocument());
        objectoutputstream.flush();
    }

    GraphicViewerBasicNode findBasicNode(
            IGraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection,
            String s) {
        for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerobjectsimplecollection
                .getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = graphicviewerobjectsimplecollection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerobjectsimplecollection
                    .getNextObjectPosAtTop(graphicviewerlistposition);
            if (graphicviewerobject instanceof GraphicViewerBasicNode) {
                GraphicViewerBasicNode graphicviewerbasicnode = (GraphicViewerBasicNode) graphicviewerobject;
                if (graphicviewerbasicnode.getLabel().getText().equals(s)) {
                    return graphicviewerbasicnode;
                }
            }
        }

        return null;
    }

    GraphicViewerObject makeListItem(int i) {
        // Just for fun, have a differently sized (and looking) item
        if (i == 7) {
            GraphicViewerPolygon graphicviewerpolygon = new GraphicViewerPolygon();
            graphicviewerpolygon.setBrush(GraphicViewerBrush
                    .makeStockBrush(Color.red));
            graphicviewerpolygon.setPen(GraphicViewerPen.make(65535, 3,
                    Color.white));
            graphicviewerpolygon.addPoint(new Point(10, 0));
            graphicviewerpolygon.addPoint(new Point(20, 0));
            graphicviewerpolygon.addPoint(new Point(30, 10));
            graphicviewerpolygon.addPoint(new Point(30, 20));
            graphicviewerpolygon.addPoint(new Point(20, 30));
            graphicviewerpolygon.addPoint(new Point(10, 30));
            graphicviewerpolygon.addPoint(new Point(0, 20));
            graphicviewerpolygon.addPoint(new Point(0, 10));
            return graphicviewerpolygon;
        }
        if (i == 13) {
            GraphicViewerText graphicviewertext = new GraphicViewerText(
                    "start a link");
            graphicviewertext.setDraggable(false);
            graphicviewertext.setTransparent(true);
            graphicviewertext.setSelectBackground(true);
            graphicviewertext.setTextColor(Color.blue);
            graphicviewertext.setBkColor(myView.getSecondarySelectionColor());
            graphicviewertext.setItalic(true);
            graphicviewertext.setFontSize(16);
            return graphicviewertext;
        }
        if (i == 14) {
            GraphicViewerText graphicviewertext1 = new GraphicViewerText(
                    "not selectable");
            graphicviewertext1.setSelectable(false);
            graphicviewertext1.setTransparent(true);
            graphicviewertext1.setSelectBackground(true);
            graphicviewertext1.setTextColor(Color.red);
            graphicviewertext1.setBkColor(myView.getSecondarySelectionColor());
            graphicviewertext1.setItalic(true);
            return graphicviewertext1;
        }
        String s = "Item " + i;
        GraphicViewerText graphicviewertext2 = new GraphicViewerText(s);
        graphicviewertext2.setTransparent(true);
        graphicviewertext2.setSelectBackground(true);
        graphicviewertext2.setBkColor(myView.getSecondarySelectionColor());
        graphicviewertext2.setFaceName("Helvetica");
        // For fun, if it's a prime number, make it bold
        if (i == 2 || i == 3 || i == 5 || i == 7 || i == 11 || i == 13
                || i == 17 || i == 19) {
            graphicviewertext2.setBold(true);
        }
        return graphicviewertext2;
    }

    public boolean isRandom() {
        return myRandom;
    }

    public void layoutCollection(
            GraphicViewerDocument graphicviewerdocument,
            IGraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection) {
        GraphicViewerListPosition graphicviewerlistposition = graphicviewerobjectsimplecollection
                .getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject = graphicviewerobjectsimplecollection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerobjectsimplecollection
                    .getNextObjectPosAtTop(graphicviewerlistposition);
            if (graphicviewerobject instanceof GraphicViewerSubGraph) { // Recursively lay out subgraphs; make sure they are expanded
                GraphicViewerSubGraph graphicviewersubgraph = (GraphicViewerSubGraph) graphicviewerobject;
                boolean flag = !graphicviewersubgraph.isExpanded();
                if (flag) {
                    graphicviewersubgraph.expand();
                }
                layoutCollection(
                        graphicviewerdocument,
                        ((IGraphicViewerObjectSimpleCollection) (graphicviewersubgraph)));
                if (flag) {
                    graphicviewersubgraph.collapse();
                }
            }
        } while (true);

        // Create a GraphicViewerNetwork so we can control exactly which nodes
        // (and links) are to be laid out
        GraphicViewerNetwork graphicviewernetwork = new GraphicViewerNetwork(
                graphicviewerobjectsimplecollection);
        graphicviewerlistposition = graphicviewerobjectsimplecollection
                .getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject1 = graphicviewerobjectsimplecollection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerobjectsimplecollection
                    .getNextObjectPosAtTop(graphicviewerlistposition);
            if (graphicviewerobject1 instanceof GraphicViewerLabeledLink) { // Don't lay out the labels of a labeled link
                GraphicViewerLabeledLink graphicviewerlabeledlink = (GraphicViewerLabeledLink) graphicviewerobject1;
                graphicviewernetwork.deleteNode(graphicviewerlabeledlink
                        .getFromLabel());
                graphicviewernetwork.deleteNode(graphicviewerlabeledlink
                        .getMidLabel());
                graphicviewernetwork.deleteNode(graphicviewerlabeledlink
                        .getToLabel());
            } else if (!(graphicviewerobject1 instanceof GraphicViewerNode)
                    && !(graphicviewerobject1 instanceof GraphicViewerLink)) { // Ignore other objects
                graphicviewernetwork.deleteNode(graphicviewerobject1);
            }
        } while (true);
        if (graphicviewerobjectsimplecollection instanceof GraphicViewerSubGraph) { // Don't lay out any of the special subgraph children
            GraphicViewerSubGraph graphicviewersubgraph1 = (GraphicViewerSubGraph) graphicviewerobjectsimplecollection;
            graphicviewernetwork.deleteNode(graphicviewersubgraph1.getHandle());
            graphicviewernetwork.deleteNode(graphicviewersubgraph1.getLabel());
            graphicviewernetwork.deleteNode(graphicviewersubgraph1.getPort());
            graphicviewernetwork.deleteNode(graphicviewersubgraph1
                    .getCollapsedObject());
        }
        GraphicViewerLayeredDigraphAutoLayout graphicviewerlayereddigraphautolayout = new GraphicViewerLayeredDigraphAutoLayout(
                graphicviewerdocument, graphicviewernetwork);
        graphicviewerlayereddigraphautolayout.setDirectionOption(3);
        graphicviewerlayereddigraphautolayout.performLayout();
    }

    public void UpdateControls() {
        if (myUpdatingSelection) {
            return;
        }
        myView.updateBorder();
        if (Inspector.getInspector() != null) {
            Inspector.inspect(myView.getSelection().getPrimarySelection());
        }
        updateActions();
    }

    void addMultiSpotPort(GraphicViewerArea graphicviewerarea,
            GraphicViewerObject graphicviewerobject, int i) {
        GraphicViewerPort graphicviewerport = new GraphicViewerPort();
        graphicviewerport.setSize(6, 6);
        graphicviewerport.setStyle(5);
        graphicviewerport.setBrush(GraphicViewerBrush
                .makeStockBrush(Color.magenta));
        graphicviewerport.setSpotLocation(i, graphicviewerobject, i);
        graphicviewerport.setFromSpot(i);
        graphicviewerport.setToSpot(i);
        graphicviewerarea.addObjectAtTail(graphicviewerport);
    }

    RecordNode makeRecordNode(boolean flag, boolean flag1) {
        Point point = getDefaultLocation();
        if (point == null) {
            point = new Point(20, 250);
        }
        RecordNode recordnode = new RecordNode();
        recordnode.initialize();
        recordnode.setScrollBarOnRight(flag);
        recordnode.setVertical(flag1);
        //tnode.setAlignment(ListArea.ALIGN_RIGHT);  // Icons on left, text on
        //                                           // right

        GraphicViewerText graphicviewertext = new GraphicViewerText("a Record");
        graphicviewertext.setAlignment(2);
        graphicviewertext.setBkColor(Color.blue);
        graphicviewertext.setTextColor(Color.white);
        graphicviewertext.setBold(true);
        graphicviewertext.setAutoResize(false);
        graphicviewertext.setClipping(true);
        if (!recordnode.isVertical() && flag) {
            recordnode.setFooter(graphicviewertext);
        } else {
            recordnode.setHeader(graphicviewertext);
        }
        for (int i = 0; i < 20; i++) {
            GraphicViewerObject graphicviewerobject = makeListItem(i);
            GraphicViewerImage graphicviewerimage = new GraphicViewerImage();
            graphicviewerimage.loadImage(
                    (GraphicViewer.class).getResource("document.png"), true);
            graphicviewerimage.setSelectable(false);
            if (recordnode.isVertical()) {
                graphicviewerimage.setSize(10, 12);
            } else {
                graphicviewerimage.setSize(30, 40);
            }
            GraphicViewerPort graphicviewerport = null;
            GraphicViewerPort graphicviewerport1 = null;
            if (recordnode.isScrollBarOnRight()) {
                graphicviewerport = new GraphicViewerPort();
                graphicviewerport.setSize(5, 5);
                graphicviewerport.setFromSpot(8);
                graphicviewerport.setToSpot(8);
            } else {
                graphicviewerport1 = new GraphicViewerPort();
                graphicviewerport1.setSize(5, 5);
                graphicviewerport1.setFromSpot(4);
                graphicviewerport1.setToSpot(4);
            }
            recordnode.addItem(graphicviewerobject, graphicviewerport,
                    graphicviewerport1, graphicviewerimage);
        }

        myForegroundLayer.addObjectAtTail(recordnode);
        return recordnode;
    }

    public void setDefaultLocation(Point point) {
        myDefaultLocation = point;
    }

    public void saveAs(String fileType) {
        String ext = ".xhtml";
        JFileChooser chooser = new JFileChooser();
        String loc = myDoc.getLocation();
        if (loc.equals("")) {
            loc = getCurrentView().getDocument().getLocation();
        }
        File currentFile = new File(loc);
        chooser.setCurrentDirectory(currentFile);
        chooser.setAcceptAllFileFilterUsed(false);
        Filter svgFilter1 = new Filter(ext, graphicViewerSVGXML);
        chooser.addChoosableFileFilter(svgFilter1);
        if (fileType.equalsIgnoreCase(ext)) {
            chooser.setFileFilter(svgFilter1);
        }
        int returnVal = chooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            Filter FileFilter = (Filter) (chooser.getFileFilter());
            myDescription = FileFilter.getDescription();
            ext = FileFilter.getExtension();
            String name = chooser.getSelectedFile().getName();
            setName(name.endsWith(ext) ? name : (name += ext));
            loc = chooser.getSelectedFile().getAbsolutePath();
            String loc2 = loc.toLowerCase();
            if (loc2.indexOf(".") == -1) {
                loc += ext;
            }

            myDoc.setLocation(loc);
            myDoc.setName(name);
            store();
        }
    }

    public OTVDocument open(Component parent, String defaultLocation) {
        JFileChooser chooser = new JFileChooser();
        if ((defaultLocation != null) && (!defaultLocation.equals(""))) {
            File currentFile = new File(defaultLocation);
            chooser.setCurrentDirectory(currentFile);
        } else {
            chooser.setCurrentDirectory(null);
        }
        chooser.setAcceptAllFileFilterUsed(false);
        Filter svgInputFilter = new Filter(".xhtml",
                "Graphic Viewer SVG with XML extensions (*.xhtml)");
        chooser.addChoosableFileFilter(svgInputFilter);
        chooser.setFileFilter(svgInputFilter);
        int returnVal = chooser.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String name = chooser.getSelectedFile().getName();
            String loc = chooser.getSelectedFile().getAbsolutePath();
            loc.toLowerCase();
            FileInputStream fstream = null;
            try {
                OTVDocument doc = null;
                fstream = new FileInputStream(loc);
                if (loc.endsWith(".xhtml")) {
                    doc = myDoc.loadSVG1(fstream);
                }
                if (doc == null) {
                    return null;
                }
                doc.setName(name);
                setTitle(myDoc.getName() + " - " + Main.APPLICATION_NAME
                        + " ( " + this + " )");
                doc.updateLocationModifiable();
                doc.updatePaperColor();
                doc.setModified(false);
                // the UndoManager is transient and must be setup again when
                // created from serialization
                // but we also need to ignore all changes up to now anyway,
                // so we'll just throw away the old manager and create a new one
                doc.setUndoManager(new GraphicViewerUndoManager());
                return doc;
            } catch (IOException ioe) {
                String msg = "Open Document Error: "
                        + ((ioe.getMessage() == null) ? ioe.toString() : ioe
                                .getMessage());
                UISupport.getDialogs().showErrorMessage(msg);
                return null;
            } catch (Exception e) {
                String msg = "Loading Document Exception: "
                        + ((e.getMessage() == null) ? e.toString() : e
                                .getMessage());
                UISupport.getDialogs().showErrorMessage(msg);
                return null;
            } finally {
                try {
                    if (fstream != null) {
                        fstream.close();
                    }
                } catch (Exception e) {
                    ExceptionDialog.ignoreException(e);
                }
            }
        } else {
            return null;
        }
    }

    public void store() {
        if (!myDoc.getLocation().equals("")) {
            FileOutputStream fstream = null;
            try {
                fstream = new FileOutputStream(myDoc.getLocation());
                if (myDescription.equals(graphicViewerSVGXML)) {
                    myDoc.storeSVG1(fstream, true, true);
                    setTitle(myDoc.getName() + " - " + Main.APPLICATION_NAME
                            + " ( " + this + " )");
                }
            } catch (Exception e) {
                String msg = "Save Document Error: "
                        + ((e.getMessage() == null) ? e.toString() : e
                                .getMessage());
                UISupport.getDialogs().showErrorMessage(msg);
            } finally {
                try {
                    if (fstream != null) {
                        fstream.close();
                    }
                } catch (Exception e) {
                    ExceptionDialog.ignoreException(e);
                }
                myDoc.setModified(false);
            }
        }
    }

    public void initPalette() {
        OTVDocument graphicviewerdocument = myDoc;
        GraphicViewerLayer graphicviewerlayer = myBackgroundLayer;
        GraphicViewerLayer graphicviewerlayer1 = myMainLayer;
        GraphicViewerLayer graphicviewerlayer2 = myForegroundLayer;

        OTVDocument graphicviewerdocument1 = myPalette.getDocument();

        // Don't care about undo/redo for the palette, so don't need to call
        // paletteDoc.fireForedate here
        graphicviewerdocument1.setSuspendUpdates(true);

        myDoc = graphicviewerdocument1; // Temporarily, so objects get created
                                        // in palette
        myBackgroundLayer = graphicviewerdocument1.getDefaultLayer();
        myMainLayer = graphicviewerdocument1.getDefaultLayer();
        myForegroundLayer = graphicviewerdocument1.getDefaultLayer();
        stuffAction(); // Make a bunch of stuff in the palette, into myDoc

        // Remove all links between nodes from palette
        ArrayList<GraphicViewerObject> arraylist = new ArrayList<GraphicViewerObject>();
        GraphicViewerListPosition graphicviewerlistposition = graphicviewerdocument1
                .getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null) {
                break;
            }
            GraphicViewerObject graphicviewerobject = graphicviewerdocument1
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerdocument1
                    .getNextObjectPosAtTop(graphicviewerlistposition);
            if (graphicviewerobject instanceof GraphicViewerLink) {
                arraylist.add(graphicviewerobject);
            }
        } while (true);
        for (int i = 0; i < arraylist.size(); i++) {
            graphicviewerdocument1.removeObject((GraphicViewerObject) arraylist
                    .get(i));
        }

        myDoc = graphicviewerdocument; // Switch back to normal document
        myBackgroundLayer = graphicviewerlayer;
        myMainLayer = graphicviewerlayer1;
        myForegroundLayer = graphicviewerlayer2;

        graphicviewerdocument1.setSuspendUpdates(false);
        // Don't care about undo/redo for the palette, so don't need to call
        // paletteDoc.fireUpdate here
    }

    void initMenus() {
        //==============================================================
        // Set up menu bar
        //==============================================================
        filemenu.setText("File");
        filemenu.add(FileNewAction);
        filemenu.add(FileOpenAction);
        filemenu.addSeparator();
        filemenu.add(PrintPreviewAction).setAccelerator(
                KeyStroke.getKeyStroke(80, 3));
        filemenu.add(PrintAction).setAccelerator(KeyStroke.getKeyStroke(80, 2));
        filemenu.addSeparator();
        filemenu.add(FileSaveAsAction);
        mainMenuBar.add(filemenu);

        editmenu.setText("Edit");
        editmenu.add(CutAction).setAccelerator(KeyStroke.getKeyStroke(88, 2));
        editmenu.add(CopyAction).setAccelerator(KeyStroke.getKeyStroke(67, 2));
        editmenu.add(PasteAction).setAccelerator(KeyStroke.getKeyStroke(86, 2));
        editmenu.add(SelectAllAction).setAccelerator(
                KeyStroke.getKeyStroke(65, 2));
        editmenu.addSeparator();

        // Add commands for undo/redo
        UndoMenuItem = editmenu.add(UndoAction);
        UndoMenuItem.setAccelerator(KeyStroke.getKeyStroke(90, 2));
        UndoMenuItem.setMnemonic('U');

        RedoMenuItem = editmenu.add(RedoAction);
        RedoMenuItem.setAccelerator(KeyStroke.getKeyStroke(89, 2));
        RedoMenuItem.setMnemonic('R');

        editmenu.addSeparator();

        // turn on undo/redo
        // Because this module does not have a GraphicViewerDocument subclass
        // where we could override endTransaction(String), we do it here as part
        // of the undo manager. We need to update all of the action menu items
        // after each transaction
        myDoc.setUndoManager(new GraphicViewerUndoManager() {

            private static final long serialVersionUID = 4120351778624595021L;

            public void endTransaction(String s) {
                super.endTransaction(s);
                Inspector.refresh();
                GraphicViewer.updateActions();
            }

        });
        editmenu.add(MoveToFrontAction).setAccelerator(
                KeyStroke.getKeyStroke(70, 2));
        editmenu.add(MoveToBackAction).setAccelerator(
                KeyStroke.getKeyStroke(66, 2));
        editmenu.add(ChangeLayersAction).setAccelerator(
                KeyStroke.getKeyStroke(82, 2));
        editmenu.addSeparator();
        editmenu.add(ZoomOutAction).setAccelerator(
                KeyStroke.getKeyStroke(117, 1));
        editmenu.add(ZoomInAction).setAccelerator(
                KeyStroke.getKeyStroke(117, 2));
        editmenu.add(ZoomNormalAction).setAccelerator(
                KeyStroke.getKeyStroke(117, 3));
        editmenu.add(ZoomToFitAction).setAccelerator(
                KeyStroke.getKeyStroke(117, 0));
        editmenu.addSeparator();
        editmenu.add(GroupAction);
        editmenu.add(SubgraphAction);
        editmenu.add(UngroupAction);
        editmenu.addSeparator();
        editmenu.add(InsertDrawingStrokeAction);
        editmenu.add(ToggleLinkIconicNodesAction);
        editmenu.addSeparator();
        editmenu.add(PropertiesAction).setAccelerator(
                KeyStroke.getKeyStroke(10, 2));
        editmenu.add(GridAction).setAccelerator(KeyStroke.getKeyStroke(71, 2));
        editmenu.add(OverviewAction).setAccelerator(
                KeyStroke.getKeyStroke(79, 2));
        mainMenuBar.add(editmenu);

        insertmenu.setText("Insert");
        insertmenu.add(InsertStuffAction);
        insertmenu.add(InsertStrokeAction);
        insertmenu.add(InsertPolygonAction);
        insertmenu.add(InsertDiamondAction);
        insertmenu.add(InsertRectangleAction);
        insertmenu.add(InsertRoundedRectangleAction);
        insertmenu.add(Insert3DRectAction);
        insertmenu.add(InsertCommentAction);
        insertmenu.add(InsertEllipseAction);
        insertmenu.add(InsertMultiPortNodeAction);
        insertmenu.add(InsertSimpleNodeAction);
        insertmenu.add(InsertIconicNodeAction);
        insertmenu.add(InsertDraggableLabelIconicNodeAction);
        insertmenu.add(InsertBasicNodeAction);
        insertmenu.add(InsertCenterLabelBasicNodeAction);
        insertmenu.add(InsertFixedSizeCenterLabelBasicNodeAction);
        insertmenu.add(InsertRectBasicNodeAction);
        insertmenu.add(InsertSelfLoopBasicNodeAction);
        insertmenu.add(InsertMultiSpotNodeAction);
        insertmenu.add(InsertGeneralNodeAction);
        insertmenu.add(InsertTextNodeAction);
        insertmenu.add(InsertFixedSizeTextNodeAction);
        insertmenu.add(InsertRoundTextNodeAction);
        insertmenu.add(InsertMultiTextNodeAction);
        insertmenu.add(InsertTextAction);
        insertmenu.add(InsertListAreaAction);
        insertmenu.add(InsertRecordNodeAction);
        insertmenu.add(Insert10000Action);
        insertmenu.add(InsertGraphOfGraphsAction);
        insertmenu.addSeparator();
        insertmenu.add(AddLeftPortAction);
        insertmenu.add(AddRightPortAction);
        mainMenuBar.add(insertmenu);

        layoutmenu.setText("Layout");
        layoutmenu.add(LeftAction);
        layoutmenu.add(HorizontalAction);
        layoutmenu.add(RightAction);
        layoutmenu.add(TopAction);
        layoutmenu.add(BottomAction);
        layoutmenu.add(VerticalAction);
        layoutmenu.addSeparator();
        layoutmenu.add(SameWidthAction);
        layoutmenu.add(SameHeightAction);
        layoutmenu.add(SameBothAction);
        layoutmenu.addSeparator();
        layoutmenu.add(LayeredDigraphAutoLayoutAction).setAccelerator(
                KeyStroke.getKeyStroke(76, 2));
        mainMenuBar.add(layoutmenu);
        setJMenuBar(mainMenuBar);
    }

    void insertSanKeyNodes() {
        startTransaction();
        SanKeyNode snode = new SanKeyNode("star");
        // Modify the iconic node's standard image to display a GIF
        snode.getImage().setSize(50, 50);
        snode.getImage().loadImage(GraphicViewer.class.getResource("star.gif"),
                true);
        snode.setLocation(450, 350);
        myForegroundLayer.addObjectAtTail(snode);

        SanKeyNode snode2 = new SanKeyNode("doc1");
        snode2.getImage().setSize(50, 50);
        snode2.getImage().loadImage(
                GraphicViewer.class.getResource("doc1.png"), true);
        snode2.setLocation(600, 300);
        myForegroundLayer.addObjectAtTail(snode2);

        SanKeyNode snode3 = new SanKeyNode("doc2");
        snode3.getImage().setSize(50, 50);
        snode3.getImage().loadImage(
                GraphicViewer.class.getResource("doc2.png"), true);
        snode3.setLocation(600, 400);
        myForegroundLayer.addObjectAtTail(snode3);

        GraphicViewerLink link1 = new GraphicViewerLink(snode.getPort(),
                snode2.getPort());
        link1.setPen(GraphicViewerPen.make(GraphicViewerPen.SOLID, 20,
                Color.blue));
        myMainLayer.addObjectAtTail(link1);

        GraphicViewerLink link2 = new GraphicViewerLink(snode.getPort(),
                snode3.getPort());
        link2.setPen(GraphicViewerPen.make(GraphicViewerPen.SOLID, 10,
                new Color(0, 128, 0)));
        myMainLayer.addObjectAtTail(link2);
        endTransaction(InsertGraphOfGraphsAction.toString());
    }

    public static GraphicViewer getInstance() {
        return GRAPHIC_VIEWER;
    }

    @Override
    public String toString() {
        return "Graphic Viewer";
    }

    /**
     * Shows or hides the component depending on the boolean flag b.
     * @param b  if true, show the component; otherwise, hide the component.
     * @see javax.swing.JComponent#isVisible
     */
    public void setVisible(boolean b) {
        if (b) {
            setLocation(50, 50);
        }
        super.setVisible(b);
    }

    // This public method exists so that any public GraphicViewer action methods
    // can be shared by other applications. In particular, the GraphicViewer
    // regression test application makes used of several of these methods.
    public void setMainLayer(GraphicViewerLayer layer) {
        myMainLayer = layer;
    }

    // Create an image for a GraphicViewerObject.
    public BufferedImage getObjectImage(GraphicViewerObject obj,
            GraphicViewerView view) {
        Rectangle r = new Rectangle(obj.getBoundingRect());
        obj.expandRectByPenWidth(r);
        BufferedImage img = new BufferedImage(r.width, r.height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.translate(-r.x, -r.y);
        view.applyRenderingHints(g2);
        obj.paint(g2, view);
        g2.dispose();
        return img;
    }
}