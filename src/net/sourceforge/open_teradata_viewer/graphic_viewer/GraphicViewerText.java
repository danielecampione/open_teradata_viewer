/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.text.JTextComponent;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerText extends GraphicViewerObject {

    private static final long serialVersionUID = -148584492302838173L;

    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_CENTER = 2;
    public static final int ALIGN_RIGHT = 3;
    public static final int ChangedText = 501;
    public static final int ChangedAlignment = 502;
    public static final int ChangedFontSize = 503;
    public static final int ChangedFaceName = 504;
    public static final int ChangedTextColor = 505;
    public static final int ChangedBkColor = 506;
    public static final int ChangedTransparent = 507;
    public static final int ChangedBold = 508;
    public static final int ChangedUnderline = 509;
    public static final int ChangedItalic = 510;
    public static final int ChangedMultiline = 511;
    public static final int ChangedEditable = 512;
    public static final int ChangedEditOnSingleClick = 513;
    public static final int Changed2DScale = 514;
    public static final int ChangedSelectBackground = 515;
    public static final int ChangedClipping = 516;
    public static final int ChangedStrikeThrough = 517;
    public static final int ChangedAutoResize = 518;
    public static final int ChangedWrapping = 519;
    public static final int ChangedWrappingWidth = 520;
    public static final int ChangedBetterPainting = 521;
    private static final int flagTransparent = 1;
    private static final int flagBold = 2;
    private static final int flagUnderline = 4;
    private static final int flagItalic = 8;
    private static final int flagMultiline = 16;
    private static final int flagEditable = 32;
    private static final int flagEditOnSingleClick = 64;
    private static final int flag2DScale = 128;
    private static final int flagNeedNewFont = 256;
    private static final int flagNeedNewRect = 512;
    private static final int flagNeedRescale = 1024;
    private static final int flagSelectBackground = 2048;
    private static final int flagClipping = 4096;
    private static final int flagStrikeThrough = 8192;
    private static final int flagAutoResize = 16384;
    private static final int flagWrapping = 32768;
    private static final int flagBetterPainting = 65536;
    private static final int flagUnused3 = 131072;
    private static final int flagUnused2 = 262144;
    private static final int flagUnused1 = 524288;
    private static String myDefaultFontName = "SansSerif";
    private static int myDefaultFontSize = 12;
    private static int myThickerLineSize = 20;
    private static double myDefaultPaintNothingScale = 0.15D;
    private static double myDefaultPaintGreekScale = 0.3D;
    private static int myDefaultWrappingWidth = 150;
    private static boolean myMeasureWarning = false;
    private static boolean myDefaultBetterPainting = GraphicViewerGlobal
            .isAtLeastJavaVersion(1.22D);
    private String myString = "";
    private String myFaceName = myDefaultFontName;
    private int myFontSize = myDefaultFontSize;
    private int myAlignment = 1;
    private Color myTextColor = GraphicViewerBrush.ColorBlack;
    private Color myBkColor = GraphicViewerBrush.ColorWhite;
    private int myTextFlags = 0;
    private int myUserTextFlags = 0;
    private int myWrappingWidth = myDefaultWrappingWidth;
    private transient int myNumLines = 0;
    private transient Font myFont = null;
    private transient FontMetrics myFontMetrics = null;
    private transient AttributedCharacterIterator myAttrStringIter = null;
    private transient AttributedString myAttrString = null;
    private transient LineBreakMeasurer myLineBreakMeasurer = null;
    private transient GraphicViewerTextEdit myTextEdit = null;

    public GraphicViewerText() {
        init(null, myDefaultFontSize, "", myDefaultFontName, false, false,
                false, 1, false, false);
    }

    public GraphicViewerText(String s) {
        init(null, myDefaultFontSize, s, myDefaultFontName, false, false,
                false, 1, false, false);
    }

    public GraphicViewerText(Point point, String s) {
        myString = "";
        myFaceName = myDefaultFontName;
        myFontSize = myDefaultFontSize;
        myAlignment = 1;
        myTextColor = GraphicViewerBrush.ColorBlack;
        myBkColor = GraphicViewerBrush.ColorWhite;
        myTextFlags = 0;
        myUserTextFlags = 0;
        myWrappingWidth = myDefaultWrappingWidth;
        myNumLines = 0;
        myFont = null;
        myFontMetrics = null;
        myAttrStringIter = null;
        myAttrString = null;
        myLineBreakMeasurer = null;
        myTextEdit = null;
        init(point, myDefaultFontSize, s, myDefaultFontName, false, false,
                false, 1, false, false);
    }

    public GraphicViewerText(Point point, int i, String s, String s1,
            boolean flag, boolean flag1, boolean flag2, int j, boolean flag3,
            boolean flag4) {
        init(point, i, s, s1, flag, flag1, flag2, j, flag3, flag4);
    }

    private final void init(Point point, int i, String s, String s1,
            boolean flag, boolean flag1, boolean flag2, int j, boolean flag3,
            boolean flag4) {
        myString = s;
        myFaceName = s1;
        myFontSize = i;
        myAlignment = j;
        myTextColor = GraphicViewerBrush.ColorBlack;
        myBkColor = GraphicViewerBrush.ColorWhite;
        int k = 16384;
        if (flag) {
            k |= 2;
        }
        if (flag1) {
            k |= 4;
        }
        if (flag2) {
            k |= 8;
        }
        if (flag3) {
            k |= 0x10;
        }
        if (flag4) {
            k |= 0x20;
        }
        if (isDefaultBetterPainting()) {
            k |= 0x10000;
        }
        myTextFlags = k;
        myNumLines = 1;
        myFont = null;
        myFontMetrics = null;
        myAttrStringIter = null;
        myAttrString = null;
        myLineBreakMeasurer = null;
        myTextEdit = null;
        setResizable(false);
        setAutoRescale(false);
        recalcBoundingRect();
        if (point != null) {
            setLocation(point);
        }
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerText graphicviewertext = (GraphicViewerText) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewertext != null) {
            graphicviewertext.myString = myString;
            graphicviewertext.myFaceName = myFaceName;
            graphicviewertext.myFontSize = myFontSize;
            graphicviewertext.myAlignment = myAlignment;
            graphicviewertext.myTextColor = myTextColor;
            graphicviewertext.myBkColor = myBkColor;
            graphicviewertext.myTextFlags = myTextFlags;
            graphicviewertext.myUserTextFlags = myUserTextFlags;
            graphicviewertext.myWrappingWidth = myWrappingWidth;
            graphicviewertext.myNumLines = myNumLines;
            graphicviewertext.myFont = null;
            graphicviewertext.myFontMetrics = null;
            graphicviewertext.myAttrStringIter = null;
            graphicviewertext.myAttrString = null;
            graphicviewertext.myLineBreakMeasurer = null;
            graphicviewertext.myTextEdit = null;
        }
        return graphicviewertext;
    }

    public void setText(String s) {
        String s1 = myString;
        if (!s1.equals(s)) {
            myString = s;
            fixState(isAutoResize() ? 512 : 1024);
            update(501, 0, s1);
        }
    }

    public String getText() {
        return myString;
    }

    public void setAlignment(int i) {
        int j = myAlignment;
        switch (i) {
            case 0 : // '\0'
            default :
                i = 2;
                break;

            case 1 : // '\001'
                i = 1;
                break;

            case 2 : // '\002'
                i = 2;
                break;

            case 3 : // '\003'
                i = 3;
                break;

            case 4 : // '\004'
                i = 3;
                break;

            case 5 : // '\005'
                i = 3;
                break;

            case 6 : // '\006'
                i = 2;
                break;

            case 7 : // '\007'
                i = 1;
                break;

            case 8 : // '\b'
                i = 1;
                break;
        }
        if (j != i) {
            myAlignment = i;
            update(502, j, null);
        }
    }

    public int getAlignment() {
        return myAlignment;
    }

    public void setFontSize(int i) {
        int j = myFontSize;
        if (i > 0 && j != i) {
            myFontSize = i;
            fixState(768);
            update(503, j, null);
        }
    }

    public int getFontSize() {
        return myFontSize;
    }

    public void setFaceName(String s) {
        String s1 = myFaceName;
        if (!s1.equals(s)) {
            myFaceName = s;
            fixState(768);
            update(504, 0, s1);
        }
    }

    public String getFaceName() {
        return myFaceName;
    }

    public void setTextColor(Color color) {
        Color color1 = myTextColor;
        if (!color1.equals(color)) {
            myTextColor = color;
            update(505, 0, color1);
        }
    }

    public Color getTextColor() {
        return myTextColor;
    }

    public void setBkColor(Color color) {
        Color color1 = myBkColor;
        if (!color1.equals(color)) {
            myBkColor = color;
            update(506, 0, color1);
        }
    }

    public Color getBkColor() {
        return myBkColor;
    }

    public final void setTextFlags(int i) {
        myUserTextFlags = i;
    }

    public final int getTextFlags() {
        return myUserTextFlags;
    }

    public void setTransparent(boolean flag) {
        boolean flag1 = (myTextFlags & 1) != 0;
        if (flag1 != flag) {
            if (flag) {
                myTextFlags |= 1;
            } else {
                myTextFlags &= -2;
            }
            update(507, flag1 ? 1 : 0, null);
        }
    }

    public boolean isTransparent() {
        return (myTextFlags & 1) != 0;
    }

    public void setBold(boolean flag) {
        boolean flag1 = (myTextFlags & 2) != 0;
        if (flag1 != flag) {
            if (flag) {
                myTextFlags |= 2;
            } else {
                myTextFlags &= -3;
            }
            fixState(768);
            update(508, flag1 ? 1 : 0, null);
        }
    }

    public boolean isBold() {
        return (myTextFlags & 2) != 0;
    }

    public void setUnderline(boolean flag) {
        boolean flag1 = (myTextFlags & 4) != 0;
        if (flag1 != flag) {
            if (flag) {
                myTextFlags |= 4;
            } else {
                myTextFlags &= -5;
            }
            fixState(768);
            update(509, flag1 ? 1 : 0, null);
        }
    }

    public boolean isUnderline() {
        return (myTextFlags & 4) != 0;
    }

    public void setStrikeThrough(boolean flag) {
        boolean flag1 = (myTextFlags & 0x2000) != 0;
        if (flag1 != flag) {
            if (flag) {
                myTextFlags |= 0x2000;
            } else {
                myTextFlags &= 0xffffdfff;
            }
            fixState(768);
            update(517, flag1 ? 1 : 0, null);
        }
    }

    public boolean isStrikeThrough() {
        return (myTextFlags & 0x2000) != 0;
    }

    public void setItalic(boolean flag) {
        boolean flag1 = (myTextFlags & 8) != 0;
        if (flag1 != flag) {
            if (flag) {
                myTextFlags |= 8;
            } else {
                myTextFlags &= -9;
            }
            fixState(768);
            update(510, flag1 ? 1 : 0, null);
        }
    }

    public boolean isItalic() {
        return (myTextFlags & 8) != 0;
    }

    public void setMultiline(boolean flag) {
        boolean flag1 = (myTextFlags & 0x10) != 0;
        if (flag1 != flag) {
            if (flag) {
                myTextFlags |= 0x10;
            } else {
                myTextFlags &= 0xffffffef;
            }
            if (!flag) {
                myNumLines = 1;
            }
            fixState(512);
            update(511, flag1 ? 1 : 0, null);
        }
    }

    public boolean isMultiline() {
        return (myTextFlags & 0x10) != 0;
    }

    public void setEditable(boolean flag) {
        boolean flag1 = (myTextFlags & 0x20) != 0;
        if (flag1 != flag) {
            if (flag) {
                myTextFlags |= 0x20;
            } else {
                myTextFlags &= 0xffffffdf;
            }
            update(512, flag1 ? 1 : 0, null);
        }
    }

    public boolean isEditable() {
        return (myTextFlags & 0x20) != 0;
    }

    public void setEditOnSingleClick(boolean flag) {
        boolean flag1 = (myTextFlags & 0x40) != 0;
        if (flag1 != flag) {
            if (flag) {
                myTextFlags |= 0x40;
            } else {
                myTextFlags &= 0xffffffbf;
            }
            update(513, flag1 ? 1 : 0, null);
        }
    }

    public boolean isEditOnSingleClick() {
        return (myTextFlags & 0x40) != 0;
    }

    public void setSelectBackground(boolean flag) {
        boolean flag1 = (myTextFlags & 0x800) != 0;
        if (flag1 != flag) {
            if (flag) {
                myTextFlags |= 0x800;
            } else {
                myTextFlags &= 0xfffff7ff;
            }
            update(515, flag1 ? 1 : 0, null);
        }
    }

    public boolean isSelectBackground() {
        return (myTextFlags & 0x800) != 0;
    }

    public void set2DScale(boolean flag) {
        boolean flag1 = (myTextFlags & 0x80) != 0;
        if (flag1 != flag) {
            if (flag) {
                myTextFlags |= 0x80;
            } else {
                myTextFlags &= 0xffffff7f;
            }
            update(514, flag1 ? 1 : 0, null);
        }
    }

    public boolean is2DScale() {
        return (myTextFlags & 0x80) != 0;
    }

    public void setClipping(boolean flag) {
        boolean flag1 = (myTextFlags & 0x1000) != 0;
        if (flag1 != flag) {
            if (flag) {
                myTextFlags |= 0x1000;
            } else {
                myTextFlags &= 0xffffefff;
            }
            update(516, flag1 ? 1 : 0, null);
        }
    }

    public boolean isClipping() {
        return (myTextFlags & 0x1000) != 0;
    }

    public void setAutoResize(boolean flag) {
        boolean flag1 = (myTextFlags & 0x4000) != 0;
        if (flag1 != flag) {
            if (flag) {
                myTextFlags |= 0x4000;
            } else {
                myTextFlags &= 0xffffbfff;
            }
            update(518, flag1 ? 1 : 0, null);
        }
    }

    public boolean isAutoResize() {
        return (myTextFlags & 0x4000) != 0;
    }

    public void setBetterPainting(boolean flag) {
        boolean flag1 = (myTextFlags & 0x10000) != 0;
        if (flag1 != flag) {
            if (flag) {
                myTextFlags |= 0x10000;
            } else {
                myTextFlags &= 0xfffeffff;
            }
            update(521, flag1 ? 1 : 0, null);
        }
    }

    public boolean isBetterPainting() {
        return (myTextFlags & 0x10000) != 0;
    }

    public void setWrapping(boolean flag) {
        boolean flag1 = (myTextFlags & 0x8000) != 0;
        if (flag1 != flag) {
            if (flag) {
                myTextFlags |= 0x8000;
            } else {
                myTextFlags &= 0xffff7fff;
            }
            fixState(512);
            update(519, flag1 ? 1 : 0, null);
        }
    }

    public boolean isWrapping() {
        return (myTextFlags & 0x8000) != 0;
    }

    public void setWrappingWidth(int i) {
        int j = myWrappingWidth;
        if (j != i) {
            myWrappingWidth = i;
            fixState(512);
            update(520, j, null);
        }
    }

    public int getWrappingWidth() {
        return myWrappingWidth;
    }

    public void expandRectByPenWidth(Rectangle rectangle) {
        int i = 10;
        if (myFontMetrics != null) {
            i = myFontMetrics.charWidth('M');
        }
        rectangle.x -= i / 2;
        rectangle.y -= i / 2;
        rectangle.width += i;
        rectangle.height += i;
    }

    public boolean paintGreek(Graphics2D graphics2d,
            GraphicViewerView graphicviewerview) {
        double d = graphicviewerview.getScale();
        double d1 = getDefaultPaintNothingScale();
        double d2 = getDefaultPaintGreekScale();
        if (graphicviewerview.isPrinting()) {
            d1 /= 3D;
            d2 /= 3D;
        }
        if (d <= d1) {
            return true;
        }
        if (d <= d2) {
            Rectangle rectangle = getBoundingRect();
            paintLine(graphics2d, graphicviewerview, rectangle.x, rectangle.y
                    + rectangle.height / 2, rectangle.width);
            return true;
        } else {
            return false;
        }
    }

    void paintLine(Graphics2D graphics2d, GraphicViewerView graphicviewerview,
            int i, int j, int k) {
        if (getFontSize() < myThickerLineSize) {
            graphics2d.setStroke(GraphicViewerPen.black.getStroke());
            graphics2d.setColor(getTextColor());
        } else {
            int l = getFontSize() / myThickerLineSize + 1;
            graphics2d.setStroke(new BasicStroke(l));
            graphics2d.setColor(getTextColor());
        }
        graphics2d.drawLine(i, j, i + k, j);
    }

    public void paint(Graphics2D paramGraphics2D,
            GraphicViewerView paramGraphicViewerView) {
        if (paintGreek(paramGraphics2D, paramGraphicViewerView)) {
            return;
        }
        paramGraphics2D.setFont(getFont());
        Rectangle localRectangle1 = getBoundingRect();
        if (!isTransparent()) {
            paramGraphics2D.setColor(getBkColor());
            paramGraphics2D.fillRect(localRectangle1.x, localRectangle1.y,
                    localRectangle1.width, localRectangle1.height);
        }
        String str = getText();
        if (str.equals("")) {
            return;
        }
        paramGraphics2D.setColor(getTextColor());
        Rectangle localRectangle2 = null;
        if (isClipping()) {
            localRectangle2 = paramGraphics2D.getClipBounds();
            paramGraphics2D.clipRect(localRectangle1.x, localRectangle1.y,
                    localRectangle1.width, localRectangle1.height);
        }
        int i = getFontMetrics(paramGraphics2D).getHeight()
                - getFontMetrics(paramGraphics2D).getMaxDescent();
        int j;
        if (isMultiline()) {
            int n;
            Object localObject;
            if (isWrapping()) {
                AttributedCharacterIterator localAttributedCharacterIterator = getAttrStringIterator();
                LineBreakMeasurer localLineBreakMeasurer = getLineBreakMeasurer(paramGraphicViewerView
                        .getFontRenderContext());
                float f1 = localRectangle1.y;
                n = Math.min(getWrappingWidth(), getWidth());
                while (localLineBreakMeasurer.getPosition() < localAttributedCharacterIterator
                        .getEndIndex()) {
                    localObject = localLineBreakMeasurer.nextLayout(n);
                    float f2 = localRectangle1.x;
                    if (localObject != null) {
                        Rectangle2D localRectangle2D = ((TextLayout) localObject)
                                .getBounds();
                        switch (getAlignment()) {
                            case 1 :
                            default :
                                break;
                            case 2 :
                                f2 += (localRectangle1.width - ((TextLayout) localObject)
                                        .getAdvance()) / 2.0F;
                                break;
                            case 3 :
                                f2 += localRectangle1.width
                                        - ((TextLayout) localObject)
                                                .getAdvance();
                        }
                        f1 += ((TextLayout) localObject).getAscent();
                        ((TextLayout) localObject)
                                .draw(paramGraphics2D, f2, f1);
                        f1 += ((TextLayout) localObject).getDescent()
                                + ((TextLayout) localObject).getLeading();
                    }
                }
            } else {
                j = 0;
                int k = -1;
                int m = 0;
                while (m == 0) {
                    k = str.indexOf(10, j);
                    if (k == -1) {
                        k = str.length();
                        m = 1;
                    }
                    if (j < k) {
                        n = getXPosForAlign(j, k,
                                paramGraphicViewerView.getFontRenderContext());
                        if (isBetterPainting()) {
                            paramGraphics2D.drawString(getAttrString()
                                    .getIterator(null, j, k), n,
                                    localRectangle1.y + i);
                        } else {
                            localObject = str.substring(j, k);
                            paramGraphics2D.drawString((String) localObject, n,
                                    localRectangle1.y + i);
                            if ((isUnderline()) || (isStrikeThrough())) {
                                if (isUnderline()) {
                                    paintLine(
                                            paramGraphics2D,
                                            paramGraphicViewerView,
                                            n,
                                            localRectangle1.y + i,
                                            getFontMetrics(paramGraphics2D)
                                                    .stringWidth(
                                                            (String) localObject));
                                }
                                if (isStrikeThrough()) {
                                    paintLine(
                                            paramGraphics2D,
                                            paramGraphicViewerView,
                                            n,
                                            localRectangle1.y
                                                    + i
                                                    - getFontMetrics(
                                                            paramGraphics2D)
                                                            .getAscent() / 3,
                                            getFontMetrics(paramGraphics2D)
                                                    .stringWidth(
                                                            (String) localObject));
                                }
                            }
                        }
                    }
                    j = k + 1;
                    i += getFontMetrics(paramGraphics2D).getHeight();
                }
            }
        } else {
            j = getXPosForAlign(0, str.length(),
                    paramGraphicViewerView.getFontRenderContext());
            if (isBetterPainting()) {
                paramGraphics2D.drawString(getAttrStringIterator(), j,
                        localRectangle1.y + i);
            } else {
                paramGraphics2D.drawString(str, j, localRectangle1.y + i);
                if ((isUnderline()) || (isStrikeThrough())) {
                    if (isUnderline()) {
                        paintLine(paramGraphics2D, paramGraphicViewerView, j,
                                localRectangle1.y + i,
                                getFontMetrics(paramGraphics2D)
                                        .stringWidth(str));
                    }
                    if (isStrikeThrough()) {
                        paintLine(
                                paramGraphics2D,
                                paramGraphicViewerView,
                                j,
                                localRectangle1.y
                                        + i
                                        - getFontMetrics(paramGraphics2D)
                                                .getAscent() / 3,
                                getFontMetrics(paramGraphics2D)
                                        .stringWidth(str));
                    }
                }
            }
        }
        if (isClipping()) {
            paramGraphics2D.setClip(localRectangle2);
        }
    }

    protected void gainedSelection(GraphicViewerSelection graphicviewerselection) {
        if (!isResizable()) {
            if (isSelectBackground()) {
                setTransparent(false);
            } else {
                graphicviewerselection.createBoundingHandle(this);
            }
            return;
        }
        if (is2DScale()) {
            super.gainedSelection(graphicviewerselection);
            return;
        }
        Rectangle rectangle = getBoundingRect();
        int i = rectangle.x;
        int j = rectangle.x + rectangle.width / 2;
        int k = rectangle.x + rectangle.width;
        int l = rectangle.y;
        int i1 = rectangle.y + rectangle.height / 2;
        int j1 = rectangle.y + rectangle.height;
        graphicviewerselection.createResizeHandle(this, i, l, -1, false);
        graphicviewerselection.createResizeHandle(this, k, l, -1, false);
        graphicviewerselection.createResizeHandle(this, i, j1, -1, false);
        graphicviewerselection.createResizeHandle(this, k, j1, -1, false);
        if (!is4ResizeHandles()) {
            graphicviewerselection.createResizeHandle(this, j, l, 2, true);
            graphicviewerselection.createResizeHandle(this, k, i1, -1, false);
            graphicviewerselection.createResizeHandle(this, j, j1, 6, true);
            graphicviewerselection.createResizeHandle(this, i, i1, -1, false);
        }
    }

    protected void lostSelection(GraphicViewerSelection graphicviewerselection) {
        if (isSelectBackground()) {
            setTransparent(true);
        }
        super.lostSelection(graphicviewerselection);
    }

    private boolean fitsInBox(Graphics2D graphics2d,
            FontRenderContext fontrendercontext, Font font) {
        Rectangle rectangle = getBoundingRect();
        int i = calculateWidth(font, fontrendercontext);
        if (rectangle.width < i) {
            return false;
        }
        int j = calculateHeight(graphics2d, fontrendercontext, font, i);
        return rectangle.height >= j;
    }

    private void setToBiggestFont() {
        Graphics2D graphics2d = GraphicViewerGlobal.getGraphics2D();
        FontRenderContext fontrendercontext = graphics2d.getFontRenderContext();
        String s = getFont().getFontName();
        int i = getFont().getStyle();
        int j;
        for (j = 13; fitsInBox(graphics2d, fontrendercontext, new Font(s, i, j)); j += 14) {
            ;
        }
        Font font;
        for (j--; !fitsInBox(graphics2d, fontrendercontext, font = new Font(s,
                i, j))
                && j > 1; j--) {
            ;
        }
        if (j != getFontSize()) {
            myFontSize = j;
            myFont = font;
            myFontMetrics = null;
            myAttrStringIter = null;
            myAttrString = null;
            myLineBreakMeasurer = null;
        }
    }

    private boolean fitsInHeight(Graphics2D graphics2d,
            FontRenderContext fontrendercontext, String s, int i, int j, int k) {
        Font font = new Font(s, i, j);
        int l = calculateHeight(graphics2d, fontrendercontext, font, getWidth());
        return l < k;
    }

    private int getPointSizeForHeight(int i, Graphics2D graphics2d,
            FontRenderContext fontrendercontext) {
        int j = 0;
        if (isBold()) {
            j |= 1;
        }
        if (isItalic()) {
            j |= 2;
        }
        int k;
        for (k = 13; fitsInHeight(graphics2d, fontrendercontext, getFaceName(),
                j, k, i); k += 14) {
            ;
        }
        for (k--; !fitsInHeight(graphics2d, fontrendercontext, getFaceName(),
                j, k, i) && k > 1; k--) {
            ;
        }
        return k;
    }

    public Rectangle handleResize(Graphics2D graphics2d,
            GraphicViewerView graphicviewerview, Rectangle rectangle,
            Point point, int i, int j, int k, int l) {
        if (!is2DScale()) {
            Rectangle rectangle1 = new Rectangle(rectangle.x, rectangle.y,
                    rectangle.width, rectangle.height);
            switch (i) {
                case 2 : // '\002'
                    if (point.y < rectangle.y + rectangle.height) {
                        rectangle1.height += rectangle.y - point.y;
                    }
                    rectangle1.y = point.y;
                    break;

                case 6 : // '\006'
                    if (point.y > rectangle.y) {
                        rectangle1.height = point.y - rectangle.y;
                    }
                    break;
            }
            if (rectangle1.height < 1) {
                rectangle1.height = 1;
            }
            if (rectangle1.height == rectangle.height) {
                return null;
            }
            if (myNumLines < 1) {
                myNumLines = 1;
            }
            int i1 = rectangle1.height / myNumLines;
            FontRenderContext fontrendercontext = graphicviewerview
                    .getFontRenderContext();
            setFontSize(getPointSizeForHeight(i1, graphics2d, fontrendercontext));
            return null;
        } else {
            return super.handleResize(graphics2d, graphicviewerview, rectangle,
                    point, i, j, 1, 1);
        }
    }

    protected void geometryChange(Rectangle rectangle) {
        Rectangle rectangle1 = getBoundingRect();
        if (rectangle1.width != rectangle.width
                || rectangle1.height != rectangle.height) {
            fixState(1024);
        }
    }

    private void generateFont() {
        int i = 0;
        if (isBold()) {
            i |= 1;
        }
        if (isItalic()) {
            i |= 2;
        }
        myFont = new Font(getFaceName(), i, getFontSize());
        myFontMetrics = null;
        myAttrStringIter = null;
        myAttrString = null;
        myLineBreakMeasurer = null;
    }

    public Font getFont() {
        if (myFont == null) {
            generateFont();
        }
        return myFont;
    }

    FontMetrics getFontMetrics(Graphics2D graphics2d) {
        if (myFontMetrics == null) {
            myFontMetrics = graphics2d.getFontMetrics(getFont());
        }
        return myFontMetrics;
    }

    AttributedString getAttrString() {
        if (myAttrString == null) {
            AttributedString attributedstring = new AttributedString(getText());
            attributedstring.addAttribute(TextAttribute.FONT, getFont());
            if (isUnderline()) {
                attributedstring.addAttribute(TextAttribute.UNDERLINE,
                        TextAttribute.UNDERLINE_ON);
            }
            if (isStrikeThrough()) {
                attributedstring.addAttribute(TextAttribute.STRIKETHROUGH,
                        TextAttribute.STRIKETHROUGH_ON);
            }
            myAttrString = attributedstring;
        }
        return myAttrString;
    }

    AttributedCharacterIterator getAttrStringIterator() {
        if (myAttrStringIter == null) {
            myAttrStringIter = getAttrString().getIterator();
        } else {
            myAttrStringIter.first();
        }
        return myAttrStringIter;
    }

    LineBreakMeasurer getLineBreakMeasurer(FontRenderContext fontrendercontext) {
        if (myLineBreakMeasurer == null) {
            myLineBreakMeasurer = new LineBreakMeasurer(
                    getAttrStringIterator(), fontrendercontext);
        }
        myLineBreakMeasurer.setPosition(0);
        return myLineBreakMeasurer;
    }

    private void fixState(int i) {
        myAttrStringIter = null;
        myAttrString = null;
        myLineBreakMeasurer = null;
        if ((i & 0x400) != 0 && !isInitializing() && isResizable()) {
            setToBiggestFont();
        }
        if ((i & 0x100) != 0) {
            generateFont();
        }
        if ((i & 0x200) != 0 && !isInitializing() && isAutoResize()) {
            recalcBoundingRect();
        }
    }

    void recalcBoundingRect() {
        Graphics2D graphics2d = GraphicViewerGlobal.getGraphics2D();
        FontRenderContext fontrendercontext = graphics2d.getFontRenderContext();
        int i = calculateWidth(getFont(), fontrendercontext);
        if (i < 10) {
            i = 10;
        }
        int j = calculateHeight(graphics2d, fontrendercontext, getFont(), i);
        if (i != getWidth() || j != getHeight()) {
            int k = getInternalFlags();
            setInternalFlags(k & 0xffffffef);
            setSizeKeepingLocation(i, j);
            setInternalFlags(k);
        }
    }

    private int calculateHeight(Graphics2D graphics2d,
            FontRenderContext fontrendercontext, Font font, int i) {
        if (isMultiline()) {
            if (getText().equals("")) {
                FontMetrics fontmetrics = graphics2d.getFontMetrics(font);
                return fontmetrics.getHeight();
            }
            if (isWrapping()) {
                AttributedString attributedstring = new AttributedString(
                        getText());
                attributedstring.addAttribute(TextAttribute.FONT, font);
                if (isUnderline()) {
                    attributedstring.addAttribute(TextAttribute.UNDERLINE,
                            TextAttribute.UNDERLINE_ON);
                }
                if (isStrikeThrough()) {
                    attributedstring.addAttribute(TextAttribute.STRIKETHROUGH,
                            TextAttribute.STRIKETHROUGH_ON);
                }
                AttributedCharacterIterator attributedcharacteriterator = attributedstring
                        .getIterator();
                LineBreakMeasurer linebreakmeasurer = new LineBreakMeasurer(
                        attributedcharacteriterator, fontrendercontext);
                float f = 0.0F;
                do {
                    if (linebreakmeasurer.getPosition() >= attributedcharacteriterator
                            .getEndIndex()) {
                        break;
                    }
                    TextLayout textlayout = linebreakmeasurer.nextLayout(i);
                    if (textlayout != null) {
                        f += textlayout.getAscent() + textlayout.getDescent()
                                + textlayout.getLeading();
                    }
                } while (true);
                return (int) Math.ceil(f);
            } else {
                FontMetrics fontmetrics1 = graphics2d.getFontMetrics(font);
                int j = Math.max(1, myNumLines) * fontmetrics1.getHeight();
                return j;
            }
        } else {
            FontMetrics fontmetrics2 = graphics2d.getFontMetrics(font);
            return fontmetrics2.getHeight();
        }
    }

    private int calculateWidth(Font font, FontRenderContext fontrendercontext) {
        if (getText().equals("")) {
            myNumLines = 1;
            return 0;
        }
        String s = getText();
        if (isMultiline()) {
            int i = 0;
            int j = 0;
            boolean flag = false;
            for (myNumLines = 0; !flag; myNumLines++) {
                int k = s.indexOf('\n', j);
                if (k == -1) {
                    k = s.length();
                    flag = true;
                }
                int l = getStringWidth(j, k, font, fontrendercontext);
                if (l > i) {
                    i = l;
                }
                j = k + 1;
            }

            if (isWrapping()) {
                return Math.min(i, getWrappingWidth());
            } else {
                return i;
            }
        } else {
            myNumLines = 1;
            return getStringWidth(0, s.length(), font, fontrendercontext);
        }
    }

    private int getStringWidth(int i, int j, Font font,
            FontRenderContext fontrendercontext) {
        if (i >= j) {
            return 0;
        }
        double d;
        if (isBetterPainting()) {
            if (isWrapping() && isMultiline()) {
                d = font.getStringBounds(getAttrStringIterator(), i, j,
                        fontrendercontext).getWidth();
            } else {
                AttributedString attributedstring = new AttributedString(
                        getAttrStringIterator(), i, j);
                TextLayout textlayout = new TextLayout(
                        attributedstring.getIterator(), fontrendercontext);
                d = textlayout.getAdvance();
            }
        } else {
            String s = getText();
            if (i != 0 || j < s.length()) {
                String s1 = s.substring(i, j);
                d = font.getStringBounds(s1, fontrendercontext).getWidth();
            } else {
                d = font.getStringBounds(s, fontrendercontext).getWidth();
            }
        }
        int k = (int) Math.ceil(d + 0.99900001287460327D);
        return k;
    }

    private int getXPosForAlign(int i, int j,
            FontRenderContext fontrendercontext) {
        Rectangle rectangle = getBoundingRect();
        int l;
        switch (getAlignment()) {
            case 1 : // '\001'
            default :
                return rectangle.x;

            case 2 : // '\002'
                int k = getStringWidth(i, j, getFont(), fontrendercontext);
                return rectangle.x + (rectangle.width - k) / 2;

            case 3 : // '\003'
                l = getStringWidth(i, j, getFont(), fontrendercontext);
                break;
        }
        return (rectangle.x + rectangle.width) - l;
    }

    public Point getLocation(Point point) {
        Rectangle rectangle = getBoundingRect();
        if (point == null) {
            point = new Point(0, 0);
        }
        switch (getAlignment()) {
            case 1 : // '\001'
            default :
                point.x = rectangle.x;
                point.y = rectangle.y;
                break;

            case 2 : // '\002'
                point.x = rectangle.x + rectangle.width / 2;
                point.y = rectangle.y;
                break;

            case 3 : // '\003'
                point.x = rectangle.x + rectangle.width;
                point.y = rectangle.y;
                break;
        }
        return point;
    }

    public void setLocation(int i, int j) {
        Rectangle rectangle = getBoundingRect();
        switch (getAlignment()) {
            case 1 : // '\001'
            default :
                setBoundingRect(i, j, rectangle.width, rectangle.height);
                break;

            case 2 : // '\002'
                setBoundingRect(i - rectangle.width / 2, j, rectangle.width,
                        rectangle.height);
                break;

            case 3 : // '\003'
                setBoundingRect(i - rectangle.width, j, rectangle.width,
                        rectangle.height);
                break;
        }
    }

    public void setSizeKeepingLocation(int i, int j) {
        Rectangle rectangle = getBoundingRect();
        switch (getAlignment()) {
            case 1 : // '\001'
            default :
                setBoundingRect(rectangle.x, rectangle.y, i, j);
                break;

            case 2 : // '\002'
                setBoundingRect((rectangle.x + rectangle.width / 2) - i / 2,
                        rectangle.y, i, j);
                break;

            case 3 : // '\003'
                setBoundingRect((rectangle.x + rectangle.width) - i,
                        rectangle.y, i, j);
                break;
        }
    }

    public boolean doMouseClick(int i, Point point, Point point1,
            GraphicViewerView graphicviewerview) {
        if (!isEditable()) {
            return false;
        }
        if (!isEditOnSingleClick()) {
            return false;
        }
        if (getLayer() != null && !getLayer().isModifiable()) {
            return false;
        } else {
            doStartEdit(graphicviewerview, point1);
            return true;
        }
    }

    public boolean doMouseDblClick(int i, Point point, Point point1,
            GraphicViewerView graphicviewerview) {
        if (!isEditable()) {
            return false;
        }
        if (isEditOnSingleClick()) {
            return false;
        }
        if (getLayer() != null && !getLayer().isModifiable()) {
            return false;
        } else {
            doStartEdit(graphicviewerview, point1);
            return true;
        }
    }

    public void doStartEdit(GraphicViewerView graphicviewerview, Point point) {
        if (graphicviewerview == null) {
            return;
        }
        if (graphicviewerview.getDocument() != null) {
            graphicviewerview.getDocument().startTransaction();
        }
        graphicviewerview.getSelection().clearSelectionHandles(this);
        Rectangle rectangle = getBoundingRect();
        Rectangle rectangle1 = new Rectangle(rectangle.x, rectangle.y,
                rectangle.width, rectangle.height);
        if (isMultiline()) {
            rectangle1.height += getHeight() / Math.max(1, myNumLines);
        }
        graphicviewerview.setEditControl(new GraphicViewerTextEdit(rectangle1,
                getText(), isMultiline(), this));
        myTextEdit = graphicviewerview.getEditControl();
        if (myTextEdit != null) {
            JTextComponent jtextcomponent = myTextEdit
                    .getTextComponent(graphicviewerview);
            if (jtextcomponent != null) {
                jtextcomponent.selectAll();
                jtextcomponent.grabFocus();
            }
        }
    }

    public boolean doEdit(GraphicViewerView graphicviewerview, String s,
            String s1) {
        String s2 = computeEdit(s, s1);
        setText(s2);
        return true;
    }

    public String computeEdit(String s, String s1) {
        return s1;
    }

    public void doEndEdit() {
        if (myTextEdit != null) {
            GraphicViewerTextEdit graphicviewertextedit = myTextEdit;
            myTextEdit = null;
            GraphicViewerView graphicviewerview = graphicviewertextedit
                    .getView();
            if (graphicviewerview != null) {
                graphicviewerview.setEditControl(null);
            }
            fixState(512);
            if (graphicviewerview != null) {
                GraphicViewerSelection graphicviewerselection = graphicviewerview
                        .getSelection();
                Object obj;
                for (obj = this; obj != null
                        && !graphicviewerselection
                                .isSelected(((GraphicViewerObject) (obj))); obj = ((GraphicViewerObject) (obj))
                        .getParent()) {
                    ;
                }
                if (obj != null) {
                    graphicviewerselection
                            .restoreSelectionHandles(((GraphicViewerObject) (obj)));
                }
                graphicviewerview.requestFocus();
                if (!graphicviewerview.hasFocus()
                        && GraphicViewerGlobal
                                .isAtLeastJavaVersion(1.3999999999999999D)) {
                    graphicviewerview
                            .addFocusListener(new GraphicViewerView.GraphicViewerViewHelper(
                                    graphicviewerview, this));
                    return;
                }
                graphicviewerview.internalFinishedEdit(this);
            }
        }
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 501 :
                graphicviewerdocumentchangededit.setNewValue(getText());
                return;

            case 502 :
                graphicviewerdocumentchangededit.setNewValueInt(getAlignment());
                return;

            case 503 :
                graphicviewerdocumentchangededit.setNewValueInt(getFontSize());
                return;

            case 504 :
                graphicviewerdocumentchangededit.setNewValue(getFaceName());
                return;

            case 505 :
                graphicviewerdocumentchangededit.setNewValue(getTextColor());
                return;

            case 506 :
                graphicviewerdocumentchangededit.setNewValue(getBkColor());
                return;

            case 507 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isTransparent());
                return;

            case 508 :
                graphicviewerdocumentchangededit.setNewValueBoolean(isBold());
                return;

            case 509 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isUnderline());
                return;

            case 510 :
                graphicviewerdocumentchangededit.setNewValueBoolean(isItalic());
                return;

            case 511 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isMultiline());
                return;

            case 512 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isEditable());
                return;

            case 513 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isEditOnSingleClick());
                return;

            case 514 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(is2DScale());
                return;

            case 515 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isSelectBackground());
                return;

            case 516 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isClipping());
                return;

            case 517 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isStrikeThrough());
                return;

            case 518 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isAutoResize());
                return;

            case 519 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isWrapping());
                return;

            case 520 :
                graphicviewerdocumentchangededit
                        .setNewValueInt(getWrappingWidth());
                return;

            case 521 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isBetterPainting());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 501 :
                setInitializing(true);
                setText((String) graphicviewerdocumentchangededit
                        .getValue(flag));
                setInitializing(false);
                return;

            case 502 :
                setAlignment(graphicviewerdocumentchangededit.getValueInt(flag));
                return;

            case 503 :
                setInitializing(true);
                setFontSize(graphicviewerdocumentchangededit.getValueInt(flag));
                setInitializing(false);
                return;

            case 504 :
                setInitializing(true);
                setFaceName((String) graphicviewerdocumentchangededit
                        .getValue(flag));
                setInitializing(false);
                return;

            case 505 :
                setTextColor((Color) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 506 :
                setBkColor((Color) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 507 :
                setTransparent(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 508 :
                setInitializing(true);
                setBold(graphicviewerdocumentchangededit.getValueBoolean(flag));
                setInitializing(false);
                return;

            case 509 :
                setInitializing(true);
                setUnderline(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                setInitializing(false);
                return;

            case 510 :
                setInitializing(true);
                setItalic(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                setInitializing(false);
                return;

            case 511 :
                setInitializing(true);
                setMultiline(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                setInitializing(false);
                return;

            case 512 :
                setEditable(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 513 :
                setEditOnSingleClick(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 514 :
                set2DScale(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 515 :
                setSelectBackground(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 516 :
                setClipping(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 517 :
                setInitializing(true);
                setStrikeThrough(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                setInitializing(false);
                return;

            case 518 :
                setAutoResize(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 519 :
                setInitializing(true);
                setWrapping(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                setInitializing(false);
                return;

            case 520 :
                setInitializing(true);
                setWrappingWidth(graphicviewerdocumentchangededit
                        .getValueInt(flag));
                setInitializing(false);
                return;

            case 521 :
                setBetterPainting(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public static void setDefaultFontFaceName(String s) {
        if (s != null) {
            myDefaultFontName = s;
        }
    }

    public static String getDefaultFontFaceName() {
        return myDefaultFontName;
    }

    public static void setDefaultFontSize(int i) {
        if (i > 0) {
            myDefaultFontSize = i;
        }
    }

    public static int getDefaultFontSize() {
        return myDefaultFontSize;
    }

    public static double getDefaultPaintNothingScale() {
        return myDefaultPaintNothingScale;
    }

    public static void setDefaultPaintNothingScale(double d) {
        myDefaultPaintNothingScale = d;
    }

    public static double getDefaultPaintGreekScale() {
        return myDefaultPaintGreekScale;
    }

    public static void setDefaultPaintGreekScale(double d) {
        myDefaultPaintGreekScale = d;
    }

    public static void setDefaultWrappingWidth(int i) {
        if (i > 0) {
            myDefaultWrappingWidth = i;
        }
    }

    public static int getDefaultWrappingWidth() {
        return myDefaultWrappingWidth;
    }

    public static void setDefaultBetterPainting(boolean flag) {
        myDefaultBetterPainting = flag;
    }

    public static boolean isDefaultBetterPainting() {
        return myDefaultBetterPainting;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerText",
                            domelement);
            domelement1.setAttribute("text", myString);
            domelement1.setAttribute("facename", myFaceName);
            domelement1.setAttribute("fontsize", Integer.toString(myFontSize));
            domelement1
                    .setAttribute("alignment", Integer.toString(myAlignment));
            String s = "black";
            if (!myTextColor.equals(GraphicViewerBrush.ColorBlack)) {
                int k = myTextColor.getRed();
                int i1 = myTextColor.getGreen();
                int l1 = myTextColor.getBlue();
                int k2 = myTextColor.getAlpha();
                s = "rgbalpha(" + Integer.toString(k) + ","
                        + Integer.toString(i1) + "," + Integer.toString(l1)
                        + "," + Integer.toString(k2) + ");";
            }
            domelement1.setAttribute("textcolor", s);
            String s2 = "white";
            if (!myBkColor.equals(GraphicViewerBrush.ColorWhite)) {
                int j1 = myBkColor.getRed();
                int i2 = myBkColor.getGreen();
                int l2 = myBkColor.getBlue();
                int j3 = myBkColor.getAlpha();
                s2 = "rgbalpha(" + Integer.toString(j1) + ","
                        + Integer.toString(i2) + "," + Integer.toString(l2)
                        + "," + Integer.toString(j3) + ");";
            }
            domelement1.setAttribute("backcolor", s2);
            domelement1
                    .setAttribute("textflags", Integer.toString(myTextFlags));
            domelement1.setAttribute("usertextflags",
                    Integer.toString(myUserTextFlags));
            domelement1.setAttribute("wrappingwidth",
                    Integer.toString(myWrappingWidth));
        }
        if (domdoc.SVGOutputEnabled()) {
            if (!getBkColor().equals(GraphicViewerBrush.ColorWhite)) {
                int i = getBkColor().getRed();
                int j = getBkColor().getGreen();
                int l = getBkColor().getBlue();
                IDomElement domelement3 = domdoc.createElement("rect");
                domelement.appendChild(domelement3);
                domelement3.setAttribute("x", Integer.toString(getTopLeft().x));
                domelement3.setAttribute("y", Integer.toString(getTopLeft().y));
                domelement3.setAttribute("width", Integer.toString(getWidth()));
                domelement3.setAttribute("height",
                        Integer.toString(getHeight()));
                String s4 = "fill:rgb(" + Integer.toString(i) + ","
                        + Integer.toString(j) + "," + Integer.toString(l)
                        + ");" + "stroke:none;";
                domelement3.setAttribute("style", s4);
            }
            IDomElement domelement2 = domdoc.createElement("g");
            SVGWriteAttributes(domelement2);
            domelement.appendChild(domelement2);
            String s1 = "";
            if (isUnderline()) {
                s1 = s1 + "text-decoration:underline;";
            }
            if (isStrikeThrough()) {
                s1 = s1 + "text-decoration:line-through;";
            }
            String s3 = getText();
            int k1 = getLeft();
            int j2 = getTop() + getFontSize();
            int i3 = 0;
            do {
                int k3 = s3.indexOf('\n', i3);
                if (k3 == -1) {
                    String s5 = s3.substring(i3);
                    IDomElement domelement4 = domdoc.createElement("text");
                    domelement4.setAttribute("x", Integer.toString(k1));
                    domelement4.setAttribute("y", Integer.toString(j2));
                    if (s1.length() > 0) {
                        domelement4.setAttribute("style", s1);
                    }
                    domelement2.appendChild(domelement4);
                    IDomText domtext = domdoc.createText(s5);
                    domelement4.appendChild(domtext);
                    break;
                }
                String s6 = s3.substring(i3, k3);
                IDomElement domelement5 = domdoc.createElement("text");
                domelement5.setAttribute("x", Integer.toString(k1));
                domelement5.setAttribute("y", Integer.toString(j2));
                j2 += (int) ((double) getFontSize() * 1.3D);
                if (s1.length() > 0) {
                    domelement5.setAttribute("style", s1);
                }
                domelement2.appendChild(domelement5);
                IDomText domtext1 = domdoc.createText(s6);
                domelement5.appendChild(domtext1);
                i3 = k3 + 1;
            } while (true);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            if (domelement1.getAttribute("textflags").length() > 0) {
                myTextFlags = Integer.parseInt(domelement1
                        .getAttribute("textflags"));
            }
            if (domelement1.getAttribute("usertextflags").length() > 0) {
                myUserTextFlags = Integer.parseInt(domelement1
                        .getAttribute("usertextflags"));
            }
            myString = domelement1.getAttribute("text");
            setFaceName(domelement1.getAttribute("facename"));
            setFontSize(Integer.parseInt(domelement1.getAttribute("fontsize")));
            String s = domelement1.getAttribute("allignment");
            if (s.length() > 0) {
                myAlignment = Integer.parseInt(s) + 1;
            } else {
                myAlignment = Integer.parseInt(domelement1
                        .getAttribute("alignment"));
            }
            String s1 = domelement1.getAttribute("textcolor");
            if (s1.equals("black")) {
                myTextColor = GraphicViewerBrush.ColorBlack;
            } else if (s1.startsWith("rgbalpha")) {
                int i = s1.indexOf("(") + 1;
                int j = s1.indexOf(",", i);
                String s3 = s1.substring(i, j);
                i = j + 1;
                j = s1.indexOf(",", i);
                String s4 = s1.substring(i, j);
                i = j + 1;
                j = s1.indexOf(",", i);
                String s6 = s1.substring(i, j);
                i = j + 1;
                j = s1.indexOf(")", i);
                String s8 = s1.substring(i, j);
                myTextColor = new Color(Integer.parseInt(s3),
                        Integer.parseInt(s4), Integer.parseInt(s6),
                        Integer.parseInt(s8));
            }
            String s2 = domelement1.getAttribute("backcolor");
            if (s2.equals("white")) {
                myBkColor = GraphicViewerBrush.ColorWhite;
            } else if (s2.startsWith("rgbalpha")) {
                int k = s2.indexOf("(") + 1;
                int l = s2.indexOf(",", k);
                String s5 = s2.substring(k, l);
                k = l + 1;
                l = s2.indexOf(",", k);
                String s7 = s2.substring(k, l);
                k = l + 1;
                l = s2.indexOf(",", k);
                String s9 = s2.substring(k, l);
                k = l + 1;
                l = s2.indexOf(")", k);
                String s10 = s2.substring(k, l);
                myBkColor = new Color(Integer.parseInt(s5),
                        Integer.parseInt(s7), Integer.parseInt(s9),
                        Integer.parseInt(s10));
            }
            if (domelement1.getAttribute("wrappingwidth").length() > 0) {
                myWrappingWidth = Integer.parseInt(domelement1
                        .getAttribute("wrappingwidth"));
            }
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
            fixState(isAutoResize() ? 768 : 1280);
        } else if (domelement.getTagName().equalsIgnoreCase("text")) {
            SVGReadAttributes(domelement);
        }
        return domelement.getNextSibling();
    }

    public void SVGWriteAttributes(IDomElement domelement) {
        super.SVGWriteAttributes(domelement);
        domelement.setAttribute("font-size", Integer.toString(getFontSize()));
        if (getFaceName().length() > 0) {
            domelement.setAttribute("font-family", getFaceName());
        }
        if (isBold()) {
            domelement.setAttribute("font-weight", "bold");
        }
        if (isItalic()) {
            domelement.setAttribute("font-style", "italic");
        }
        String s = "";
        if (!getTextColor().equals(GraphicViewerBrush.ColorBlack)) {
            int i = getTextColor().getRed();
            int j = getTextColor().getGreen();
            int k = getTextColor().getBlue();
            s = "fill:rgb(" + Integer.toString(i) + "," + Integer.toString(j)
                    + "," + Integer.toString(k) + ");";
        }
        if (s.length() > 0) {
            domelement.setAttribute("style", s);
        }
    }

    public void SVGReadAttributes(IDomElement domelement) {
        super.SVGReadAttributes(domelement);
        String s = domelement.getAttribute("font-size");
        if (s.length() > 0) {
            setFontSize(Integer.parseInt(s));
        }
        String s1 = domelement.getAttribute("font-family");
        setFaceName(s1);
        String s2 = domelement.getAttribute("font-weight");
        if (s2.equalsIgnoreCase("bold")) {
            setBold(true);
        }
        String s3 = domelement.getAttribute("font-style");
        if (s3.equalsIgnoreCase("italic")) {
            setItalic(true);
        }
        String s4 = domelement.getAttribute("font-decoration");
        if (s4.equalsIgnoreCase("underline")) {
            setUnderline(true);
        }
    }
}