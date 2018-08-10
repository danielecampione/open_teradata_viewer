/*
 * Open Teradata Viewer ( look and feel )
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

package com.incors.plaf.kunststoff;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalInternalFrameTitlePane;
import javax.swing.plaf.metal.MetalInternalFrameUI;

/**
 * 
 * 
 * @author Aljoscha Rittner
 * @author C.J. Kent
 * @author Christian Peter
 * @author Christoph Wilhelms
 * @author Eric Georges
 * @author Gerald Bauer
 * @author Ingo Kegel
 * @author Jamie LaScolea
 * @author <A HREF="mailto:jens@jensn.de">Jens Niemeyer</A>
 * @author Jerason Banes
 * @author Jim Wissner
 * @author Johannes Ernst
 * @author Jonas Kilian
 * @author <A HREF="mailto:julien@izforge.com">Julien Ponge</A>
 * @author Karsten Lentzsch
 * @author Matthew Philips
 * @author Romain Guy
 * @author Sebastian Ferreyra
 * @author Steve Varghese
 * @author Taoufik Romdhane
 * @author Timo Haberkern
 * 
 */
public class KunststoffInternalFrameUI extends MetalInternalFrameUI {

    private MetalInternalFrameTitlePane titlePane;
    private PropertyChangeListener paletteListener;

    private static String FRAME_TYPE = "JInternalFrame.frameType";
    private static String PALETTE_FRAME = "palette";
    private static String OPTION_DIALOG = "optionDialog";

    protected static String IS_PALETTE = "JInternalFrame.isPalette"; // added by Thomas Auinger
                                                                     // to solve a compiling problem

    public KunststoffInternalFrameUI(JInternalFrame b) {
        super(b);
    }

    public static ComponentUI createUI(JComponent c) {
        return new KunststoffInternalFrameUI((JInternalFrame) c);
    }

    public void installUI(JComponent c) {
        paletteListener = new PaletteListener();
        c.addPropertyChangeListener(paletteListener);

        super.installUI(c);
    }

    public void uninstallUI(JComponent c) {
        c.removePropertyChangeListener(paletteListener);
        super.uninstallUI(c);
    }

    protected JComponent createNorthPane(JInternalFrame w) {
        super.createNorthPane(w);
        titlePane = new KunststoffInternalFrameTitlePane(w);
        return titlePane;
    }

    public void setPalette(boolean isPalette) {
        super.setPalette(isPalette);
        titlePane.setPalette(isPalette);
    }

    private void setFrameType(String frameType) {
        if (frameType.equals(OPTION_DIALOG)) {
            LookAndFeel
                    .installBorder(frame, "InternalFrame.optionDialogBorder");
            titlePane.setPalette(false);
        } else if (frameType.equals(PALETTE_FRAME)) {
            LookAndFeel.installBorder(frame, "InternalFrame.paletteBorder");
            titlePane.setPalette(true);
        } else {
            LookAndFeel.installBorder(frame, "InternalFrame.border");
            titlePane.setPalette(false);
        }
    }

    /**
     * 
     * 
     * @author Aljoscha Rittner
     * @author C.J. Kent
     * @author Christian Peter
     * @author Christoph Wilhelms
     * @author Eric Georges
     * @author Gerald Bauer
     * @author Ingo Kegel
     * @author Jamie LaScolea
     * @author <A HREF="mailto:jens@jensn.de">Jens Niemeyer</A>
     * @author Jerason Banes
     * @author Jim Wissner
     * @author Johannes Ernst
     * @author Jonas Kilian
     * @author <A HREF="mailto:julien@izforge.com">Julien Ponge</A>
     * @author Karsten Lentzsch
     * @author Matthew Philips
     * @author Romain Guy
     * @author Sebastian Ferreyra
     * @author Steve Varghese
     * @author Taoufik Romdhane
     * @author Timo Haberkern
     * 
     */
    class PaletteListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent e) {
            String name = e.getPropertyName();

            if (name.equals(FRAME_TYPE)) {
                if (e.getNewValue() instanceof String)
                    setFrameType((String) e.getNewValue());
            } else if (name.equals(IS_PALETTE)) {
                if (e.getNewValue() != null)
                    setPalette(((Boolean) e.getNewValue()).booleanValue());
                else
                    setPalette(false);
            }
        }
    } // end class PaletteListener
}
