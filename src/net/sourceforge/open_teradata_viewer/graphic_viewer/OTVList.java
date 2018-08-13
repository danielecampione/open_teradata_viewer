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

import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class OTVList extends JList
        implements
            DragSourceListener,
            DragGestureListener {

    private static final long serialVersionUID = -4864290838503898544L;

    public void dragDropEnd(DragSourceDropEvent e) {
    }
    public void dragEnter(DragSourceDragEvent e) {
    }
    public void dragExit(DragSourceEvent e) {
    }
    public void dragOver(DragSourceDragEvent e) {
    }
    public void dropActionChanged(DragSourceDragEvent e) {
    }

    private DragSource myDragSource;
    private DragGestureRecognizer myRecognizer;

    public OTVList() {
        myDragSource = new DragSource();
        myRecognizer = myDragSource.createDefaultDragGestureRecognizer(this,
                DnDConstants.ACTION_COPY_OR_MOVE, this);
        // If the user double-clicks on a list item, execute the item's action
        // which will presumably create something in the main view
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = locationToIndex(e.getPoint());
                    AppAction item = (AppAction) getModel().getElementAt(index);
                    item.actionPerformed(null);
                }
            }
        });
    }

    // Users can drag an item from the list to drop it on the main view, thereby
    // invoking the action. Represent the item selected for the drag-and-drop
    // transfer by using a StringSelection. The main view will interpret the
    // string and determine the action to perform
    public void dragGestureRecognized(DragGestureEvent e) {
        AppAction item = (AppAction) getSelectedValue();
        if (item != null) {
            StringSelection sel = new StringSelection(item.toString());
            e.startDrag(DragSource.DefaultCopyDrop, sel, this);
        }
    }
}