/*
 * Open Teradata Viewer ( editor language support java )
 * Copyright (C) 2014, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java;

import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

/**
 * An icon that can have an optional "decorations" icon beside of it.
 *
 * @author D. Campione
 * 
 */
public class DecoratableIcon implements Icon {

    /** The width of this icon. */
    private int width;

    /** The "main" icon (the icon that is decorated). */
    private Icon mainIcon;

    /** The "decoration" icons. */
    private List<Icon> decorations;

    /** Whether this icon is for a "deprecated" item. */
    private boolean deprecated;

    /** The width of a decoratable icon (16 + 8 + 8, - 8 for overlap). */
    private static final int DEFAULT_WIDTH = 24;

    /**
     * Ctor.
     *
     * @param mainIcon The "main" icon. This cannot be <code>null</code>.
     */
    public DecoratableIcon(Icon mainIcon) {
        this(DEFAULT_WIDTH, mainIcon);
    }

    /**
     * Ctor.
     *
     * @param width The width for this icon.
     * @param mainIcon The "main" icon. This cannot be <code>null</code>.
     */
    public DecoratableIcon(int width, Icon mainIcon) {
        setMainIcon(mainIcon);
        this.width = width;
    }

    /**
     * Adds a decoration icon.
     *
     * @param decoration A new decoration icon. This cannot be
     *        <code>null</code>.
     * @see #setMainIcon(Icon)
     */
    public void addDecorationIcon(Icon decoration) {
        if (decoration == null) {
            throw new IllegalArgumentException("decoration cannot be null");
        }
        if (decorations == null) {
            decorations = new ArrayList<Icon>(1); // Usually just 1
        }
        decorations.add(decoration);
    }

    /** {@inheritDoc} */
    @Override
    public int getIconHeight() {
        return mainIcon.getIconHeight();
    }

    /** {@inheritDoc} */
    @Override
    public int getIconWidth() {
        return width;
    }

    /** {@inheritDoc} */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (deprecated) {
            IconFactory.get().getIcon(IconFactory.DEPRECATED_ICON)
                    .paintIcon(c, g, x, y);
        }
        mainIcon.paintIcon(c, g, x, y);
        if (decorations != null) {
            x = x + getIconWidth() - 8;
            for (int i = decorations.size() - 1; i >= 0; i--) {
                Icon icon = decorations.get(i);
                icon.paintIcon(c, g, x, y);
                x -= 8;
            }
        }
    }

    /**
     * Sets whether this icon is for a deprecated item.
     *
     * @param deprecated Whether this icon is for a deprecated item.
     */
    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    /**
     * Sets the main icon.
     *
     * @param icon The "main" icon. This cannot be <code>null</code>.
     * @see #addDecorationIcon(Icon)
     */
    public void setMainIcon(Icon icon) {
        if (icon == null) {
            throw new IllegalArgumentException("icon cannot be null");
        }
        this.mainIcon = icon;
    }
}