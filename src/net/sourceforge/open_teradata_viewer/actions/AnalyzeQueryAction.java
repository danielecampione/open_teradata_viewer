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

package net.sourceforge.open_teradata_viewer.actions;


import java.awt.Point;
import java.awt.event.ActionEvent;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.graphicviewer.AnimatedLink;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewer;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerBasicNode;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerDocument;
import test.net.sourceforge.open_teradata_viewer.tablesfinder.TablesNamesFinder;

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
    }

    public void actionPerformed(final ActionEvent e) {
        // The graphic viewer command can be performed altough other processes
        // are running. No ThreadAction object must be instantiated because the
        // focus must still remains on the graphic viewer frame.
        try {
            performThreaded(e);
        } catch (Throwable t) {
            ApplicationFrame.getInstance().printStackTraceOnGUI(t);
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected void performThreaded(ActionEvent e) throws Exception {
        try {
            CCJSqlParserManager pm = new CCJSqlParserManager();
            String sql = ApplicationFrame.getInstance().getText();
            if (sql.trim().length() > 0) {
                Statement statement = pm.parse(new StringReader(sql));

                //now you should use a class that implements StatementVisitor to decide what to do
                //based on the kind of the statement, that is SELECT or INSERT etc. but here we are only
                //interested in SELECTS

                if (statement instanceof Select) {
                    Select selectStatement = (Select) statement;
                    TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
                    List tableList = tablesNamesFinder
                            .getTableList(selectStatement);

                    GraphicViewerDocument graphicViewerDocument = ApplicationFrame
                            .getInstance().graphicViewer.myView.getDocument();

                    int i = 1;
                    Vector<GraphicViewerBasicNode> nodes = new Vector<GraphicViewerBasicNode>(
                            1, 1);
                    for (Iterator iter = tableList.iterator(); iter.hasNext(); i++) {
                        String tableName = (String) iter.next();
                        GraphicViewerBasicNode graphicViewerBasicNode = ApplicationFrame
                                .getInstance().graphicViewer.insertNode(
                                new Point(50, 50), !(i % 2 == 0));
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

                ApplicationFrame.getInstance().graphicViewer.LayeredDigraphAutoLayoutAction
                        .actionPerformed(new ActionEvent(this, 0, null));
            }
            ApplicationFrame.getInstance().graphicViewer.setVisible(true);
            GraphicViewer.updateActions();
        } catch (Throwable localThrowable) {
            ApplicationFrame.getInstance().printStackTraceOnGUI(localThrowable);
            ExceptionDialog.showException(localThrowable);
        }
    }
}
