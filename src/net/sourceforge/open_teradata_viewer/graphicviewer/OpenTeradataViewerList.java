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

import java.awt.datatransfer.StringSelection;
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
public class OpenTeradataViewerList extends JList<Object>
        implements
            DragSourceListener,
            DragGestureListener {

    private static final long serialVersionUID = -4864290838503898544L;

    public OpenTeradataViewerList() {
        myDragSource = new DragSource();
        myRecognizer = myDragSource.createDefaultDragGestureRecognizer(this, 3,
                this);
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent mouseevent) {
                if (mouseevent.getClickCount() == 2) {
                    int i = locationToIndex(mouseevent.getPoint());
                    AppAction appaction = (AppAction) getModel()
                            .getElementAt(i);
                    appaction.actionPerformed(null);
                }
            }

        });
    }

    public void dragGestureRecognized(DragGestureEvent draggestureevent) {
        AppAction appaction = (AppAction) getSelectedValue();
        if (appaction != null) {
            StringSelection stringselection = new StringSelection(
                    appaction.toString());
            draggestureevent.startDrag(DragSource.DefaultCopyDrop,
                    stringselection, this);
        }
    }

    public void dragDropEnd(DragSourceDropEvent dragsourcedropevent) {
    }

    public void dragEnter(DragSourceDragEvent dragsourcedragevent) {
    }

    public void dragExit(DragSourceEvent dragsourceevent) {
    }

    public void dragOver(DragSourceDragEvent dragsourcedragevent) {
    }

    public void dropActionChanged(DragSourceDragEvent dragsourcedragevent) {
    }

    private DragSource myDragSource;
    @SuppressWarnings("unused")
    private DragGestureRecognizer myRecognizer;
}