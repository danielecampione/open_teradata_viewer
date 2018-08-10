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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerGlobal {

    private GraphicViewerGlobal() {
    }

    public static void TRACE(String s) {
        ApplicationFrame.getInstance().getConsole()
                .print(s, ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
    }

    public static double getJavaVersion() {
        if (_flddo == -1D)
            try {
                _flddo = a(System.getProperty("java.version", "1.0"));
            } catch (SecurityException se) {
                _flddo = 1.0D;
            }
        return _flddo;
    }

    public static boolean isJavaVersion(double d) {
        return getJavaVersion() == d;
    }

    public static boolean isAtLeastJavaVersion(double d) {
        return getJavaVersion() >= d;
    }

    private static double a(String s) {
        int i = s.length();
        String s1 = "";
        boolean flag = false;
        for (int j = 0; j < i; j++) {
            char c = s.charAt(j);
            if (Character.isDigit(c) || c == '.' && !flag)
                s1 = s1 + c;
            if (c == '.')
                flag = true;
        }

        Double double1;
        try {
            double1 = new Double(s1);
        } catch (NumberFormatException nfe) {
            double1 = new Double(1.0D);
        }
        return double1.doubleValue();
    }

    public static Component getComponent() {
        return _fldif;
    }

    public static void setComponent(Component component) {
        if (_fldif == null || !_fldif.isDisplayable())
            _fldif = component;
    }

    public static Graphics2D getGraphics2D() {
        if (a == null) {
            BufferedImage bufferedimage = new BufferedImage(1, 1, 1);
            a = bufferedimage.createGraphics();
            a.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            a.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            a.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            a.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            if (isAtLeastJavaVersion(1.3999999999999999D))
                a.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        }
        return a;
    }

    public static void setup() {
        if (getComponent() == null || !getComponent().isDisplayable()) {
            JLabel jlabel = new JLabel();
            setComponent(jlabel);
        }
    }

    public static Toolkit getToolkit() {
        if (getComponent() != null)
            return getComponent().getToolkit();
        else
            return Toolkit.getDefaultToolkit();
    }

    private static double _flddo = -1D;
    private static Component _fldif = null;
    private static Graphics2D a = null;
}