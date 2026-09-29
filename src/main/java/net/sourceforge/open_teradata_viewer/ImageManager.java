/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C), D. Campione
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

package net.sourceforge.open_teradata_viewer;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public final class ImageManager {

    private static HashMap<String, ImageIcon> imageList = new HashMap<>();

    public final static ImageIcon getImage(String resName) {
        // NOTE: this used to default to Toolkit.getDefaultToolkit()
        // .getClass() (e.g. sun.awt.X11.XToolkit) as the root class for
        // resource resolution. As of Java 9, Class#getResource() on a
        // class belonging to a named module (such an AWT toolkit class
        // is part of the java.desktop module) looks for the resource
        // *inside that module* and no longer falls back to the
        // application's own classpath - so this always returned null on
        // Java 9+ for every one of OTV's own bundled icons, causing a
        // NullPointerException in every caller. It happened to work on
        // Java 8 only as a side effect of that toolkit class being
        // bootstrap-loaded there, which made Class#getResource() fall
        // back to the system class loader. Using this very class
        // instead - part of OTV's own, unnamed-module classpath - gives
        // the identical, correct resolution on both Java 8 and Java 9+.
        return getImage(resName, ImageManager.class);
    }

    public final static ImageIcon getImage(String resName, Class<?> rootClass) {
        synchronized (imageList) {
            if (imageList.containsKey(resName)) {
                return imageList.get(resName);
            } else {
                try {
                    URL url;
                    File file = new File(resName);
                    if (file.exists()) {
                        url = file.toURI().toURL();
                    } else {
                        url = rootClass.getResource(resName);
                    }
                    ImageIcon ii = new ImageIcon(url);
                    imageList.put(resName, ii);
                    return ii;
                } catch (Throwable e) {
                    imageList.put(resName, null);
                }
                return null;
            }
        }
    }

}
