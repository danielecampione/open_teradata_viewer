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

    public GraphicViewerText() {
        es = "";
        ep = eg;
        d8 = eO;
        eh = 1;
        eK = GraphicViewerBrush.ColorBlack;
        eu = GraphicViewerBrush.ColorWhite;
        eB = 0;
        ei = 0;
        ef = eD;
        ej = 0;
        eJ = null;
        eG = null;
        er = null;
        et = null;
        ez = null;
        em = null;
        a(null, eO, "", eg, false, false, false, 1, false, false);
    }

    public GraphicViewerText(String s) {
        es = "";
        ep = eg;
        d8 = eO;
        eh = 1;
        eK = GraphicViewerBrush.ColorBlack;
        eu = GraphicViewerBrush.ColorWhite;
        eB = 0;
        ei = 0;
        ef = eD;
        ej = 0;
        eJ = null;
        eG = null;
        er = null;
        et = null;
        ez = null;
        em = null;
        a(null, eO, s, eg, false, false, false, 1, false, false);
    }

    public GraphicViewerText(Point point, String s) {
        es = "";
        ep = eg;
        d8 = eO;
        eh = 1;
        eK = GraphicViewerBrush.ColorBlack;
        eu = GraphicViewerBrush.ColorWhite;
        eB = 0;
        ei = 0;
        ef = eD;
        ej = 0;
        eJ = null;
        eG = null;
        er = null;
        et = null;
        ez = null;
        em = null;
        a(point, eO, s, eg, false, false, false, 1, false, false);
    }

    public GraphicViewerText(Point point, int i, String s, String s1,
            boolean flag, boolean flag1, boolean flag2, int j, boolean flag3,
            boolean flag4) {
        es = "";
        ep = eg;
        d8 = eO;
        eh = 1;
        eK = GraphicViewerBrush.ColorBlack;
        eu = GraphicViewerBrush.ColorWhite;
        eB = 0;
        ei = 0;
        ef = eD;
        ej = 0;
        eJ = null;
        eG = null;
        er = null;
        et = null;
        ez = null;
        em = null;
        a(point, i, s, s1, flag, flag1, flag2, j, flag3, flag4);
    }

    private final void a(Point point, int i, String s, String s1, boolean flag,
            boolean flag1, boolean flag2, int j, boolean flag3, boolean flag4) {
        es = s;
        ep = s1;
        d8 = i;
        eh = j;
        eK = GraphicViewerBrush.ColorBlack;
        eu = GraphicViewerBrush.ColorWhite;
        int k = 16384;
        if (flag)
            k |= 2;
        if (flag1)
            k |= 4;
        if (flag2)
            k |= 8;
        if (flag3)
            k |= 0x10;
        if (flag4)
            k |= 0x20;
        if (isDefaultBetterPainting())
            k |= 0x10000;
        eB = k;
        ej = 1;
        eJ = null;
        eG = null;
        er = null;
        et = null;
        ez = null;
        em = null;
        setResizable(false);
        setAutoRescale(false);
        C();
        if (point != null)
            setLocation(point);
    }

    public GraphicViewerObject copyObject(
            GraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerText graphicviewertext = (GraphicViewerText) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewertext != null) {
            graphicviewertext.es = es;
            graphicviewertext.ep = ep;
            graphicviewertext.d8 = d8;
            graphicviewertext.eh = eh;
            graphicviewertext.eK = eK;
            graphicviewertext.eu = eu;
            graphicviewertext.eB = eB;
            graphicviewertext.ei = ei;
            graphicviewertext.ef = ef;
            graphicviewertext.ej = ej;
            graphicviewertext.eJ = null;
            graphicviewertext.eG = null;
            graphicviewertext.er = null;
            graphicviewertext.et = null;
            graphicviewertext.ez = null;
            graphicviewertext.em = null;
        }
        return graphicviewertext;
    }

    public void setText(String s) {
        String s1 = es;
        if (!s1.equals(s)) {
            es = s;
            _mthtry(isAutoResize() ? 512 : 1024);
            update(501, 0, s1);
        }
    }

    public String getText() {
        return es;
    }

    public void setAlignment(int i) {
        int j = eh;
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
            eh = i;
            update(502, j, null);
        }
    }

    public int getAlignment() {
        return eh;
    }

    public void setFontSize(int i) {
        int j = d8;
        if (i > 0 && j != i) {
            d8 = i;
            _mthtry(768);
            update(503, j, null);
        }
    }

    public int getFontSize() {
        return d8;
    }

    public void setFaceName(String s) {
        String s1 = ep;
        if (!s1.equals(s)) {
            ep = s;
            _mthtry(768);
            update(504, 0, s1);
        }
    }

    public String getFaceName() {
        return ep;
    }

    public void setTextColor(Color color) {
        Color color1 = eK;
        if (!color1.equals(color)) {
            eK = color;
            update(505, 0, color1);
        }
    }

    public Color getTextColor() {
        return eK;
    }

    public void setBkColor(Color color) {
        Color color1 = eu;
        if (!color1.equals(color)) {
            eu = color;
            update(506, 0, color1);
        }
    }

    public Color getBkColor() {
        return eu;
    }

    public final void setTextFlags(int i) {
        ei = i;
    }

    public final int getTextFlags() {
        return ei;
    }

    public void setTransparent(boolean flag) {
        boolean flag1 = (eB & 1) != 0;
        if (flag1 != flag) {
            if (flag)
                eB |= 1;
            else
                eB &= -2;
            update(507, flag1 ? 1 : 0, null);
        }
    }

    public boolean isTransparent() {
        return (eB & 1) != 0;
    }

    public void setBold(boolean flag) {
        boolean flag1 = (eB & 2) != 0;
        if (flag1 != flag) {
            if (flag)
                eB |= 2;
            else
                eB &= -3;
            _mthtry(768);
            update(508, flag1 ? 1 : 0, null);
        }
    }

    public boolean isBold() {
        return (eB & 2) != 0;
    }

    public void setUnderline(boolean flag) {
        boolean flag1 = (eB & 4) != 0;
        if (flag1 != flag) {
            if (flag)
                eB |= 4;
            else
                eB &= -5;
            _mthtry(768);
            update(509, flag1 ? 1 : 0, null);
        }
    }

    public boolean isUnderline() {
        return (eB & 4) != 0;
    }

    public void setStrikeThrough(boolean flag) {
        boolean flag1 = (eB & 0x2000) != 0;
        if (flag1 != flag) {
            if (flag)
                eB |= 0x2000;
            else
                eB &= 0xffffdfff;
            _mthtry(768);
            update(517, flag1 ? 1 : 0, null);
        }
    }

    public boolean isStrikeThrough() {
        return (eB & 0x2000) != 0;
    }

    public void setItalic(boolean flag) {
        boolean flag1 = (eB & 8) != 0;
        if (flag1 != flag) {
            if (flag)
                eB |= 8;
            else
                eB &= -9;
            _mthtry(768);
            update(510, flag1 ? 1 : 0, null);
        }
    }

    public boolean isItalic() {
        return (eB & 8) != 0;
    }

    public void setMultiline(boolean flag) {
        boolean flag1 = (eB & 0x10) != 0;
        if (flag1 != flag) {
            if (flag)
                eB |= 0x10;
            else
                eB &= 0xffffffef;
            if (!flag)
                ej = 1;
            _mthtry(512);
            update(511, flag1 ? 1 : 0, null);
        }
    }

    public boolean isMultiline() {
        return (eB & 0x10) != 0;
    }

    public void setEditable(boolean flag) {
        boolean flag1 = (eB & 0x20) != 0;
        if (flag1 != flag) {
            if (flag)
                eB |= 0x20;
            else
                eB &= 0xffffffdf;
            update(512, flag1 ? 1 : 0, null);
        }
    }

    public boolean isEditable() {
        return (eB & 0x20) != 0;
    }

    public void setEditOnSingleClick(boolean flag) {
        boolean flag1 = (eB & 0x40) != 0;
        if (flag1 != flag) {
            if (flag)
                eB |= 0x40;
            else
                eB &= 0xffffffbf;
            update(513, flag1 ? 1 : 0, null);
        }
    }

    public boolean isEditOnSingleClick() {
        return (eB & 0x40) != 0;
    }

    public void setSelectBackground(boolean flag) {
        boolean flag1 = (eB & 0x800) != 0;
        if (flag1 != flag) {
            if (flag)
                eB |= 0x800;
            else
                eB &= 0xfffff7ff;
            update(515, flag1 ? 1 : 0, null);
        }
    }

    public boolean isSelectBackground() {
        return (eB & 0x800) != 0;
    }

    public void set2DScale(boolean flag) {
        boolean flag1 = (eB & 0x80) != 0;
        if (flag1 != flag) {
            if (flag)
                eB |= 0x80;
            else
                eB &= 0xffffff7f;
            update(514, flag1 ? 1 : 0, null);
        }
    }

    public boolean is2DScale() {
        return (eB & 0x80) != 0;
    }

    public void setClipping(boolean flag) {
        boolean flag1 = (eB & 0x1000) != 0;
        if (flag1 != flag) {
            if (flag)
                eB |= 0x1000;
            else
                eB &= 0xffffefff;
            update(516, flag1 ? 1 : 0, null);
        }
    }

    public boolean isClipping() {
        return (eB & 0x1000) != 0;
    }

    public void setAutoResize(boolean flag) {
        boolean flag1 = (eB & 0x4000) != 0;
        if (flag1 != flag) {
            if (flag)
                eB |= 0x4000;
            else
                eB &= 0xffffbfff;
            update(518, flag1 ? 1 : 0, null);
        }
    }

    public boolean isAutoResize() {
        return (eB & 0x4000) != 0;
    }

    public void setBetterPainting(boolean flag) {
        boolean flag1 = (eB & 0x10000) != 0;
        if (flag1 != flag) {
            if (flag)
                eB |= 0x10000;
            else
                eB &= 0xfffeffff;
            update(521, flag1 ? 1 : 0, null);
        }
    }

    public boolean isBetterPainting() {
        return (eB & 0x10000) != 0;
    }

    public void setWrapping(boolean flag) {
        boolean flag1 = (eB & 0x8000) != 0;
        if (flag1 != flag) {
            if (flag)
                eB |= 0x8000;
            else
                eB &= 0xffff7fff;
            _mthtry(512);
            update(519, flag1 ? 1 : 0, null);
        }
    }

    public boolean isWrapping() {
        return (eB & 0x8000) != 0;
    }

    public void setWrappingWidth(int i) {
        int j = ef;
        if (j != i) {
            ef = i;
            _mthtry(512);
            update(520, j, null);
        }
    }

    public int getWrappingWidth() {
        return ef;
    }

    public void expandRectByPenWidth(Rectangle rectangle) {
        int i = 10;
        if (eG != null)
            i = eG.charWidth('M');
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
        if (d <= d1)
            return true;
        if (d <= d2) {
            Rectangle rectangle = getBoundingRect();
            a(graphics2d, graphicviewerview, rectangle.x, rectangle.y
                    + rectangle.height / 2, rectangle.width);
            return true;
        } else {
            return false;
        }
    }

    void a(Graphics2D graphics2d, GraphicViewerView graphicviewerview, int i,
            int j, int k) {
        if (getFontSize() < d9) {
            graphics2d.setStroke(GraphicViewerPen.black.getStroke());
            graphics2d.setColor(getTextColor());
        } else {
            int l = getFontSize() / d9 + 1;
            graphics2d.setStroke(new BasicStroke(l));
            graphics2d.setColor(getTextColor());
        }
        graphics2d.drawLine(i, j, i + k, j);
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        if (paintGreek(graphics2d, graphicviewerview))
            return;
        graphics2d.setFont(getFont());
        Rectangle rectangle = getBoundingRect();
        if (!isTransparent()) {
            graphics2d.setColor(getBkColor());
            graphics2d.fillRect(rectangle.x, rectangle.y, rectangle.width,
                    rectangle.height);
        }
        String s = getText();
        if (s.equals(""))
            return;
        graphics2d.setColor(getTextColor());
        Rectangle rectangle1 = null;
        if (isClipping()) {
            rectangle1 = graphics2d.getClipBounds();
            graphics2d.clipRect(rectangle.x, rectangle.y, rectangle.width,
                    rectangle.height);
        }
        int i = a(graphics2d).getHeight() - a(graphics2d).getMaxDescent();
        if (isMultiline()) {
            if (isWrapping()) {
                AttributedCharacterIterator attributedcharacteriterator = y();
                LineBreakMeasurer linebreakmeasurer = a(graphicviewerview
                        ._mthnull());
                float f = rectangle.y;
                int i1 = Math.min(getWrappingWidth(), getWidth());
                do {
                    if (linebreakmeasurer.getPosition() >= attributedcharacteriterator
                            .getEndIndex())
                        break;
                    TextLayout textlayout = linebreakmeasurer.nextLayout(i1);
                    float f1 = rectangle.x;
                    if (textlayout != null) {
                        @SuppressWarnings("unused")
                        Rectangle2D rectangle2d = textlayout.getBounds();
                        switch (getAlignment()) {
                            case 2 : // '\002'
                                f1 += ((float) rectangle.width - textlayout
                                        .getAdvance()) / 2.0F;
                                break;

                            case 3 : // '\003'
                                f1 += (float) rectangle.width
                                        - textlayout.getAdvance();
                                break;
                        }
                        f += textlayout.getAscent();
                        textlayout.draw(graphics2d, f1, f);
                        f += textlayout.getDescent() + textlayout.getLeading();
                    }
                } while (true);
            } else {
                int j = 0;
                @SuppressWarnings("unused")
                byte byte0 = -1;
                boolean flag = false;
                while (!flag) {
                    int l = s.indexOf('\n', j);
                    if (l == -1) {
                        l = s.length();
                        flag = true;
                    }
                    if (j < l) {
                        int j1 = a(j, l, graphicviewerview._mthnull());
                        if (isBetterPainting()) {
                            graphics2d.drawString(z().getIterator(null, j, l),
                                    j1, rectangle.y + i);
                        } else {
                            String s1 = s.substring(j, l);
                            graphics2d.drawString(s1, j1, rectangle.y + i);
                            if (isUnderline() || isStrikeThrough()) {
                                if (isUnderline())
                                    a(graphics2d, graphicviewerview, j1,
                                            rectangle.y + i, a(graphics2d)
                                                    .stringWidth(s1));
                                if (isStrikeThrough())
                                    a(graphics2d, graphicviewerview, j1,
                                            (rectangle.y + i)
                                                    - a(graphics2d).getAscent()
                                                    / 3, a(graphics2d)
                                                    .stringWidth(s1));
                            }
                        }
                    }
                    j = l + 1;
                    i += a(graphics2d).getHeight();
                }
            }
        } else {
            int k = a(0, s.length(), graphicviewerview._mthnull());
            if (isBetterPainting()) {
                graphics2d.drawString(y(), k, rectangle.y + i);
            } else {
                graphics2d.drawString(s, k, rectangle.y + i);
                if (isUnderline() || isStrikeThrough()) {
                    if (isUnderline())
                        a(graphics2d, graphicviewerview, k, rectangle.y + i,
                                a(graphics2d).stringWidth(s));
                    if (isStrikeThrough())
                        a(graphics2d, graphicviewerview, k, (rectangle.y + i)
                                - a(graphics2d).getAscent() / 3, a(graphics2d)
                                .stringWidth(s));
                }
            }
        }
        if (isClipping())
            graphics2d.setClip(rectangle1);
    }

    protected void gainedSelection(GraphicViewerSelection graphicviewerselection) {
        if (!isResizable()) {
            if (isSelectBackground())
                setTransparent(false);
            else
                graphicviewerselection.createBoundingHandle(this);
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
        if (isSelectBackground())
            setTransparent(true);
        super.lostSelection(graphicviewerselection);
    }

    private boolean a(Graphics2D graphics2d,
            FontRenderContext fontrendercontext, Font font) {
        Rectangle rectangle = getBoundingRect();
        int i = a(font, fontrendercontext);
        if (rectangle.width < i)
            return false;
        int j = a(graphics2d, fontrendercontext, font, i);
        return rectangle.height >= j;
    }

    private void A() {
        Graphics2D graphics2d = GraphicViewerGlobal.getGraphics2D();
        FontRenderContext fontrendercontext = graphics2d.getFontRenderContext();
        String s = getFont().getFontName();
        int i = getFont().getStyle();
        int j;
        @SuppressWarnings("unused")
        Font font;
        for (j = 13; a(graphics2d, fontrendercontext, font = new Font(s, i, j)); j += 14);
        Font font1;
        for (j--; !a(graphics2d, fontrendercontext, font1 = new Font(s, i, j))
                && j > 1; j--);
        if (j != getFontSize()) {
            d8 = j;
            eJ = font1;
            eG = null;
            er = null;
            et = null;
            ez = null;
        }
    }

    private boolean a(Graphics2D graphics2d,
            FontRenderContext fontrendercontext, String s, int i, int j, int k) {
        Font font = new Font(s, i, j);
        int l = a(graphics2d, fontrendercontext, font, getWidth());
        return l < k;
    }

    private int a(int i, Graphics2D graphics2d,
            FontRenderContext fontrendercontext) {
        int j = 0;
        if (isBold())
            j |= 1;
        if (isItalic())
            j |= 2;
        int k;
        for (k = 13; a(graphics2d, fontrendercontext, getFaceName(), j, k, i); k += 14);
        for (k--; !a(graphics2d, fontrendercontext, getFaceName(), j, k, i)
                && k > 1; k--);
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
                    if (point.y < rectangle.y + rectangle.height)
                        rectangle1.height += rectangle.y - point.y;
                    rectangle1.y = point.y;
                    break;

                case 6 : // '\006'
                    if (point.y > rectangle.y)
                        rectangle1.height = point.y - rectangle.y;
                    break;
            }
            if (rectangle1.height < 1)
                rectangle1.height = 1;
            if (rectangle1.height == rectangle.height)
                return null;
            if (ej < 1)
                ej = 1;
            int i1 = rectangle1.height / ej;
            FontRenderContext fontrendercontext = graphicviewerview._mthnull();
            setFontSize(a(i1, graphics2d, fontrendercontext));
            return null;
        } else {
            return super.handleResize(graphics2d, graphicviewerview, rectangle,
                    point, i, j, 1, 1);
        }
    }

    protected void geometryChange(Rectangle rectangle) {
        Rectangle rectangle1 = getBoundingRect();
        if (rectangle1.width != rectangle.width
                || rectangle1.height != rectangle.height)
            _mthtry(1024);
    }

    private void B() {
        int i = 0;
        if (isBold())
            i |= 1;
        if (isItalic())
            i |= 2;
        eJ = new Font(getFaceName(), i, getFontSize());
        eG = null;
        er = null;
        et = null;
        ez = null;
    }

    public Font getFont() {
        if (eJ == null)
            B();
        return eJ;
    }

    FontMetrics a(Graphics2D graphics2d) {
        if (eG == null)
            eG = graphics2d.getFontMetrics(getFont());
        return eG;
    }

    AttributedString z() {
        if (et == null) {
            AttributedString attributedstring = new AttributedString(getText());
            attributedstring.addAttribute(TextAttribute.FONT, getFont());
            if (isUnderline())
                attributedstring.addAttribute(TextAttribute.UNDERLINE,
                        TextAttribute.UNDERLINE_ON);
            if (isStrikeThrough())
                attributedstring.addAttribute(TextAttribute.STRIKETHROUGH,
                        TextAttribute.STRIKETHROUGH_ON);
            et = attributedstring;
        }
        return et;
    }

    AttributedCharacterIterator y() {
        if (er == null)
            er = z().getIterator();
        else
            er.first();
        return er;
    }

    LineBreakMeasurer a(FontRenderContext fontrendercontext) {
        if (ez == null)
            ez = new LineBreakMeasurer(y(), fontrendercontext);
        ez.setPosition(0);
        return ez;
    }

    private void _mthtry(int i) {
        er = null;
        et = null;
        ez = null;
        if ((i & 0x400) != 0 && !isInitializing() && isResizable())
            A();
        if ((i & 0x100) != 0)
            B();
        if ((i & 0x200) != 0 && !isInitializing() && isAutoResize())
            C();
    }

    void C() {
        Graphics2D graphics2d = GraphicViewerGlobal.getGraphics2D();
        FontRenderContext fontrendercontext = graphics2d.getFontRenderContext();
        int i = a(getFont(), fontrendercontext);
        if (i < 10)
            i = 10;
        int j = a(graphics2d, fontrendercontext, getFont(), i);
        if (i != getWidth() || j != getHeight()) {
            int k = g();
            _mthfor(k & 0xffffffef);
            setSizeKeepingLocation(i, j);
            _mthfor(k);
        }
    }

    private int a(Graphics2D graphics2d, FontRenderContext fontrendercontext,
            Font font, int i) {
        if (isMultiline()) {
            if (getText().equals("")) {
                FontMetrics fontmetrics = graphics2d.getFontMetrics(font);
                return fontmetrics.getHeight();
            }
            if (isWrapping()) {
                AttributedString attributedstring = new AttributedString(
                        getText());
                attributedstring.addAttribute(TextAttribute.FONT, font);
                if (isUnderline())
                    attributedstring.addAttribute(TextAttribute.UNDERLINE,
                            TextAttribute.UNDERLINE_ON);
                if (isStrikeThrough())
                    attributedstring.addAttribute(TextAttribute.STRIKETHROUGH,
                            TextAttribute.STRIKETHROUGH_ON);
                AttributedCharacterIterator attributedcharacteriterator = attributedstring
                        .getIterator();
                LineBreakMeasurer linebreakmeasurer = new LineBreakMeasurer(
                        attributedcharacteriterator, fontrendercontext);
                float f = 0.0F;
                do {
                    if (linebreakmeasurer.getPosition() >= attributedcharacteriterator
                            .getEndIndex())
                        break;
                    TextLayout textlayout = linebreakmeasurer.nextLayout(i);
                    if (textlayout != null)
                        f += textlayout.getAscent() + textlayout.getDescent()
                                + textlayout.getLeading();
                } while (true);
                return (int) Math.ceil(f);
            } else {
                FontMetrics fontmetrics1 = graphics2d.getFontMetrics(font);
                int j = Math.max(1, ej) * fontmetrics1.getHeight();
                return j;
            }
        } else {
            FontMetrics fontmetrics2 = graphics2d.getFontMetrics(font);
            return fontmetrics2.getHeight();
        }
    }

    private int a(Font font, FontRenderContext fontrendercontext) {
        if (getText().equals("")) {
            ej = 1;
            return 0;
        }
        String s = getText();
        if (isMultiline()) {
            int i = 0;
            int j = 0;
            boolean flag = false;
            for (ej = 0; !flag; ej++) {
                int k = s.indexOf('\n', j);
                if (k == -1) {
                    k = s.length();
                    flag = true;
                }
                int l = a(j, k, font, fontrendercontext);
                if (l > i)
                    i = l;
                j = k + 1;
            }

            if (isWrapping())
                return Math.min(i, getWrappingWidth());
            else
                return i;
        } else {
            ej = 1;
            return a(0, s.length(), font, fontrendercontext);
        }
    }

    private int a(int i, int j, Font font, FontRenderContext fontrendercontext) {
        if (i >= j)
            return 0;
        double d;
        if (isBetterPainting()) {
            if (isWrapping() && isMultiline()) {
                d = font.getStringBounds(y(), i, j, fontrendercontext)
                        .getWidth();
            } else {
                AttributedString attributedstring = new AttributedString(y(),
                        i, j);
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

    private int a(int i, int j, FontRenderContext fontrendercontext) {
        Rectangle rectangle = getBoundingRect();
        int l;
        switch (getAlignment()) {
            case 1 : // '\001'
            default :
                return rectangle.x;

            case 2 : // '\002'
                int k = a(i, j, getFont(), fontrendercontext);
                return rectangle.x + (rectangle.width - k) / 2;

            case 3 : // '\003'
                l = a(i, j, getFont(), fontrendercontext);
                break;
        }
        return (rectangle.x + rectangle.width) - l;
    }

    public Point getLocation(Point point) {
        Rectangle rectangle = getBoundingRect();
        if (point == null)
            point = new Point(0, 0);
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
        if (!isEditable())
            return false;
        if (!isEditOnSingleClick())
            return false;
        if (getLayer() != null && !getLayer().isModifiable()) {
            return false;
        } else {
            doStartEdit(graphicviewerview, point1);
            return true;
        }
    }

    public boolean doMouseDblClick(int i, Point point, Point point1,
            GraphicViewerView graphicviewerview) {
        if (!isEditable())
            return false;
        if (isEditOnSingleClick())
            return false;
        if (getLayer() != null && !getLayer().isModifiable()) {
            return false;
        } else {
            doStartEdit(graphicviewerview, point1);
            return true;
        }
    }

    public void doStartEdit(GraphicViewerView graphicviewerview, Point point) {
        if (graphicviewerview == null)
            return;
        if (graphicviewerview.getDocument() != null)
            graphicviewerview.getDocument().startTransaction();
        graphicviewerview.getSelection().clearSelectionHandles(this);
        Rectangle rectangle = getBoundingRect();
        Rectangle rectangle1 = new Rectangle(rectangle.x, rectangle.y,
                rectangle.width, rectangle.height);
        if (isMultiline())
            rectangle1.height += getHeight() / Math.max(1, ej);
        graphicviewerview.setEditControl(new GraphicViewerTextEdit(rectangle1,
                getText(), isMultiline(), this));
        em = graphicviewerview.getEditControl();
        if (em != null) {
            JTextComponent jtextcomponent = em
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
        if (em != null) {
            GraphicViewerTextEdit graphicviewertextedit = em;
            em = null;
            GraphicViewerView graphicviewerview = graphicviewertextedit
                    .getView();
            if (graphicviewerview != null)
                graphicviewerview.setEditControl(null);
            _mthtry(512);
            if (graphicviewerview != null) {
                GraphicViewerSelection graphicviewerselection = graphicviewerview
                        .getSelection();
                Object obj;
                for (obj = this; obj != null
                        && !graphicviewerselection
                                .isSelected(((GraphicViewerObject) (obj))); obj = ((GraphicViewerObject) (obj))
                        .getParent());
                if (obj != null)
                    graphicviewerselection
                            .restoreSelectionHandles(((GraphicViewerObject) (obj)));
                graphicviewerview.requestFocus();
                if (!graphicviewerview.hasFocus()
                        && GraphicViewerGlobal
                                .isAtLeastJavaVersion(1.3999999999999999D)) {
                    graphicviewerview
                            .addFocusListener(new GraphicViewerView.GraphicViewerViewHelper(
                                    graphicviewerview, this));
                    return;
                }
                graphicviewerview._mthint(this);
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
        if (s != null)
            eg = s;
    }

    public static String getDefaultFontFaceName() {
        return eg;
    }

    public static void setDefaultFontSize(int i) {
        if (i > 0)
            eO = i;
    }

    public static int getDefaultFontSize() {
        return eO;
    }

    public static double getDefaultPaintNothingScale() {
        return eE;
    }

    public static void setDefaultPaintNothingScale(double d) {
        eE = d;
    }

    public static double getDefaultPaintGreekScale() {
        return eM;
    }

    public static void setDefaultPaintGreekScale(double d) {
        eM = d;
    }

    public static void setDefaultWrappingWidth(int i) {
        if (i > 0)
            eD = i;
    }

    public static int getDefaultWrappingWidth() {
        return eD;
    }

    public static void setDefaultBetterPainting(boolean flag) {
        eH = flag;
    }

    public static boolean isDefaultBetterPainting() {
        return eH;
    }

    public void SVGWriteObject(DomDoc domdoc, DomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            DomElement domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerText",
                    domelement);
            domelement1.setAttribute("text", es);
            domelement1.setAttribute("facename", ep);
            domelement1.setAttribute("fontsize", Integer.toString(d8));
            domelement1.setAttribute("alignment", Integer.toString(eh));
            String s = "black";
            if (!eK.equals(GraphicViewerBrush.ColorBlack)) {
                int k = eK.getRed();
                int i1 = eK.getGreen();
                int l1 = eK.getBlue();
                int k2 = eK.getAlpha();
                s = "rgbalpha(" + Integer.toString(k) + ","
                        + Integer.toString(i1) + "," + Integer.toString(l1)
                        + "," + Integer.toString(k2) + ");";
            }
            domelement1.setAttribute("textcolor", s);
            String s2 = "white";
            if (!eu.equals(GraphicViewerBrush.ColorWhite)) {
                int j1 = eu.getRed();
                int i2 = eu.getGreen();
                int l2 = eu.getBlue();
                int j3 = eu.getAlpha();
                s2 = "rgbalpha(" + Integer.toString(j1) + ","
                        + Integer.toString(i2) + "," + Integer.toString(l2)
                        + "," + Integer.toString(j3) + ");";
            }
            domelement1.setAttribute("backcolor", s2);
            domelement1.setAttribute("textflags", Integer.toString(eB));
            domelement1.setAttribute("usertextflags", Integer.toString(ei));
            domelement1.setAttribute("wrappingwidth", Integer.toString(ef));
        }
        if (domdoc.SVGOutputEnabled()) {
            if (!getBkColor().equals(GraphicViewerBrush.ColorWhite)) {
                int i = getBkColor().getRed();
                int j = getBkColor().getGreen();
                int l = getBkColor().getBlue();
                DomElement domelement3 = domdoc.createElement("rect");
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
            DomElement domelement2 = domdoc.createElement("g");
            SVGWriteAttributes(domelement2);
            domelement.appendChild(domelement2);
            String s1 = "";
            if (isUnderline())
                s1 = s1 + "text-decoration:underline;";
            if (isStrikeThrough())
                s1 = s1 + "text-decoration:line-through;";
            String s3 = getText();
            int k1 = getLeft();
            int j2 = getTop() + getFontSize();
            int i3 = 0;
            @SuppressWarnings("unused")
            byte byte0 = -1;
            do {
                int k3 = s3.indexOf('\n', i3);
                if (k3 == -1) {
                    String s5 = s3.substring(i3);
                    DomElement domelement4 = domdoc.createElement("text");
                    domelement4.setAttribute("x", Integer.toString(k1));
                    domelement4.setAttribute("y", Integer.toString(j2));
                    if (s1.length() > 0)
                        domelement4.setAttribute("style", s1);
                    domelement2.appendChild(domelement4);
                    DomText domtext = domdoc.createText(s5);
                    domelement4.appendChild(domtext);
                    break;
                }
                String s6 = s3.substring(i3, k3);
                DomElement domelement5 = domdoc.createElement("text");
                domelement5.setAttribute("x", Integer.toString(k1));
                domelement5.setAttribute("y", Integer.toString(j2));
                j2 += (int) ((double) getFontSize() * 1.3D);
                if (s1.length() > 0)
                    domelement5.setAttribute("style", s1);
                domelement2.appendChild(domelement5);
                DomText domtext1 = domdoc.createText(s6);
                domelement5.appendChild(domtext1);
                i3 = k3 + 1;
            } while (true);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public DomNode SVGReadObject(DomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, DomElement domelement,
            DomElement domelement1) {
        if (domelement1 != null) {
            if (domelement1.getAttribute("textflags").length() > 0)
                eB = Integer.parseInt(domelement1.getAttribute("textflags"));
            if (domelement1.getAttribute("usertextflags").length() > 0)
                ei = Integer
                        .parseInt(domelement1.getAttribute("usertextflags"));
            es = domelement1.getAttribute("text");
            setFaceName(domelement1.getAttribute("facename"));
            setFontSize(Integer.parseInt(domelement1.getAttribute("fontsize")));
            String s = domelement1.getAttribute("allignment");
            if (s.length() > 0)
                eh = Integer.parseInt(s) + 1;
            else
                eh = Integer.parseInt(domelement1.getAttribute("alignment"));
            String s1 = domelement1.getAttribute("textcolor");
            if (s1.equals("black"))
                eK = GraphicViewerBrush.ColorBlack;
            else if (s1.startsWith("rgbalpha")) {
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
                eK = new Color(Integer.parseInt(s3), Integer.parseInt(s4),
                        Integer.parseInt(s6), Integer.parseInt(s8));
            }
            String s2 = domelement1.getAttribute("backcolor");
            if (s2.equals("white"))
                eu = GraphicViewerBrush.ColorWhite;
            else if (s2.startsWith("rgbalpha")) {
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
                eu = new Color(Integer.parseInt(s5), Integer.parseInt(s7),
                        Integer.parseInt(s9), Integer.parseInt(s10));
            }
            if (domelement1.getAttribute("wrappingwidth").length() > 0)
                ef = Integer
                        .parseInt(domelement1.getAttribute("wrappingwidth"));
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
            _mthtry(isAutoResize() ? 768 : 1280);
        } else if (domelement.getTagName().equalsIgnoreCase("text"))
            SVGReadAttributes(domelement);
        return domelement.getNextSibling();
    }

    public void SVGWriteAttributes(DomElement domelement) {
        super.SVGWriteAttributes(domelement);
        domelement.setAttribute("font-size", Integer.toString(getFontSize()));
        if (getFaceName().length() > 0)
            domelement.setAttribute("font-family", getFaceName());
        if (isBold())
            domelement.setAttribute("font-weight", "bold");
        if (isItalic())
            domelement.setAttribute("font-style", "italic");
        String s = "";
        if (!getTextColor().equals(GraphicViewerBrush.ColorBlack)) {
            int i = getTextColor().getRed();
            int j = getTextColor().getGreen();
            int k = getTextColor().getBlue();
            s = "fill:rgb(" + Integer.toString(i) + "," + Integer.toString(j)
                    + "," + Integer.toString(k) + ");";
        }
        if (s.length() > 0)
            domelement.setAttribute("style", s);
    }

    public void SVGReadAttributes(DomElement domelement) {
        super.SVGReadAttributes(domelement);
        String s = domelement.getAttribute("font-size");
        if (s.length() > 0)
            setFontSize(Integer.parseInt(s));
        String s1 = domelement.getAttribute("font-family");
        setFaceName(s1);
        String s2 = domelement.getAttribute("font-weight");
        if (s2.equalsIgnoreCase("bold"))
            setBold(true);
        String s3 = domelement.getAttribute("font-style");
        if (s3.equalsIgnoreCase("italic"))
            setItalic(true);
        String s4 = domelement.getAttribute("font-decoration");
        if (s4.equalsIgnoreCase("underline"))
            setUnderline(true);
    }

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
    @SuppressWarnings("unused")
    private static final int en = 1;
    @SuppressWarnings("unused")
    private static final int ed = 2;
    @SuppressWarnings("unused")
    private static final int ev = 4;
    @SuppressWarnings("unused")
    private static final int eP = 8;
    @SuppressWarnings("unused")
    private static final int ea = 16;
    @SuppressWarnings("unused")
    private static final int ek = 32;
    @SuppressWarnings("unused")
    private static final int eL = 64;
    @SuppressWarnings("unused")
    private static final int eN = 128;
    @SuppressWarnings("unused")
    private static final int ee = 256;
    @SuppressWarnings("unused")
    private static final int eC = 512;
    @SuppressWarnings("unused")
    private static final int eq = 1024;
    @SuppressWarnings("unused")
    private static final int eF = 2048;
    @SuppressWarnings("unused")
    private static final int eb = 4096;
    @SuppressWarnings("unused")
    private static final int el = 8192;
    @SuppressWarnings("unused")
    private static final int eI = 16384;
    @SuppressWarnings("unused")
    private static final int eo = 32768;
    @SuppressWarnings("unused")
    private static final int eA = 0x10000;
    @SuppressWarnings("unused")
    private static final int ew = 0x20000;
    @SuppressWarnings("unused")
    private static final int ex = 0x40000;
    @SuppressWarnings("unused")
    private static final int ey = 0x80000;
    private static String eg = "SansSerif";
    private static int eO = 12;
    private static int d9 = 20;
    private static double eE = 0.14999999999999999D;
    private static double eM = 0.29999999999999999D;
    private static int eD = 150;
    @SuppressWarnings("unused")
    private static boolean ec = false;
    private static boolean eH = GraphicViewerGlobal.isAtLeastJavaVersion(1.22D);
    private String es;
    private String ep;
    private int d8;
    private int eh;
    private Color eK;
    private Color eu;
    private int eB;
    private int ei;
    private int ef;
    private transient int ej;
    private transient Font eJ;
    private transient FontMetrics eG;
    private transient AttributedCharacterIterator er;
    private transient AttributedString et;
    private transient LineBreakMeasurer ez;
    private transient GraphicViewerTextEdit em;

}