/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.Toolkit;
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

    private static HashMap<String, ImageIcon> imageList = new HashMap<String, ImageIcon>();

    public final static ImageIcon getImage(String resName) {
        return getImage(resName, Toolkit.getDefaultToolkit().getClass());
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
