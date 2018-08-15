/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.graphic_viewer.AnimatedLink;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewer;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerBasicNode;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerDocument;
import net.sourceforge.open_teradata_viewer.sqlparser.parser.CCSqlParserManager;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.IStatement;
import net.sourceforge.open_teradata_viewer.sqlparser.statement.select.Select;
import net.sourceforge.open_teradata_viewer.sqlparser.util.TablesNamesFinder;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class AnalyzeQueryAction extends CustomAction {

    private static final long serialVersionUID = 4156741775582762590L;

    public AnalyzeQueryAction() {
        super("Analyze query..", null, null,
                "Show table list of the SQL query in a graphic viewer.");
        setEnabled(true);
    }

    public void actionPerformed(final ActionEvent e) {
        // The "analyze query" process can be performed altough other processes
        // are running. No ThreadAction object must be instantiated because the
        // focus must still remains on the graphic viewer frame
        try {
            performThreaded(e);
        } catch (Throwable t) {
            ExceptionDialog.showException(t);
        }
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        try {
            CCSqlParserManager pm = new CCSqlParserManager();
            String sql = ApplicationFrame.getInstance().getText();
            if (sql.trim().length() > 0) {
                IStatement statement = pm.parse(new StringReader(sql));

                // Now you should use a class that implements IStatementVisitor
                // to decide what to do based on the kind of the statement, that
                // is SELECT or INSERT etc.. but here we are only interested in
                // SELECTS
                if (statement instanceof Select) {
                    Select selectStatement = (Select) statement;
                    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
                    List<String> tableList = tablesNamesFinder
                            .getTableList(selectStatement);

                    GraphicViewerDocument graphicViewerDocument = ApplicationFrame
                            .getInstance().getGraphicViewer().myView
                            .getDocument();

                    int i = 1;
                    Vector<GraphicViewerBasicNode> nodes = new Vector<GraphicViewerBasicNode>(
                            1, 1);
                    for (Iterator<?> iter = tableList.iterator(); iter
                            .hasNext(); i++) {
                        String tableName = (String) iter.next();
                        GraphicViewerBasicNode graphicViewerBasicNode = ApplicationFrame
                                .getInstance().getGraphicViewer()
                                .insertNode(new Point(50, 50), !(i % 2 == 0));
                        graphicViewerBasicNode.setText(tableName);
                        nodes.add(graphicViewerBasicNode);
                        if (i >= 2) {
                            GraphicViewerBasicNode node1 = nodes
                                    .elementAt(i - 2);
                            GraphicViewerBasicNode node2 = nodes
                                    .elementAt(i - 1);
                            graphicViewerDocument
                                    .addObjectAtTail(new AnimatedLink(node1
                                            .getPort(), node2.getPort()));
                        }
                    }
                }

                ApplicationFrame.getInstance().getGraphicViewer().LayeredDigraphAutoLayoutAction
                        .actionPerformed(new ActionEvent(this, 0, null));
            }
        } catch (Throwable t) {
            ExceptionDialog.showException(t);
        } finally {
            ApplicationFrame.getInstance().getGraphicViewer().setVisible(true);
            GraphicViewer.updateActions();
        }
    }
}