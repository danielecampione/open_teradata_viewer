/*
 * Open Teradata Viewer ( sql parser )
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

package test.net.sourceforge.open_teradata_viewer.sqlparser.select;

import java.io.StringReader;

import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class MemoryTest {

    public static void main(String[] args) throws Exception {
        System.gc();
        System.out.println(Runtime.getRuntime().totalMemory()
                - Runtime.getRuntime().freeMemory());
        CCJSqlParserManager parserManager = new CCJSqlParserManager();

        /*		String longQuery = new String(
        			"select  *  from  (  SELECT  intermediate.id  as  id  ,  intermediate.date  as          "
        				+ "date  FROM  (  SELECT  DISTINCT   (  id  )   FROM  (  SELECT                           "
        				+ "wct_workflows.workflow_id  as  id  ,  wct_transaction.date  as  date  FROM             "
        				+ "wct_audit_entry  ,  wct_transaction  ,  wct_workflows  WHERE                           "
        				+ "(  wct_audit_entry.privilege  =  'W'  or  wct_audit_entry.privilege  =                 "
        				+ "'C'  )  and  wct_audit_entry.outcome  =  't'  and                                      "
        				+ "wct_audit_entry.transaction_id  =  wct_transaction.transaction_id  and                 "
        				+ "wct_transaction.user_id  = 164 and  wct_audit_entry.object_id  =                       "
        				+ "wct_workflows.active_version_id ))) UNION  SELECT  wct_workflows.workflow_id  as       "
        				+ "id  ,  wct_transaction.date  as  date  FROM  wct_audit_entry  ,                        "
        				+ "wct_transaction  ,  wct_workflows  WHERE  (  wct_audit_entry.privilege  =              "
        				+ "'W'  or  wct_audit_entry.privilege  =  'C'  )  and  wct_audit_entry.outcome            "
        				+ "=  't'  and  wct_audit_entry.transaction_id  =                                         "
        				+ "wct_transaction.transaction_id  and  wct_transaction.user_id  = 164 and                "
        				+ "afdf=  (  select  wct_audit_entry.object_id  from  wct_audit_entry  ,                  "
        				+ "wct_workflow_archive  where  wct_audit_entry.object_id  =                              "
        				+ "wct_workflow_archive.archive_id  and  wct_workflows.workflow_id  =                     "
        				+ "wct_workflow_archive.workflow_id  )                                                    "
        				+ "UNION  SELECT  wct_workflows.workflow_id                                               "
        				+ "as  id  ,  wct_transaction.date  as  date  FROM  wct_audit_entry  ,                    "
        				+ "wct_transaction  ,  wct_workflows  WHERE  (  wct_audit_entry.privilege  =              "
        				+ "'W'  OR  wct_audit_entry.privilege  =  'E'  OR  wct_audit_entry.privilege  =           "
        				+ "'A'  )  and  wct_audit_entry.outcome  =  't'  and                                      "
        				+ "wct_audit_entry.transaction_id  =  wct_transaction.transaction_id  and                 "
        				+ "wct_transaction.user_id  = 164 and  wct_audit_entry.object_id  =                       "
        				+ "wct_workflows.workflow_id    UNION SELECT * FROM interm2  ,  wct_workflow_docs  WHERE  "
        				+ "interm2.id  =  wct_workflow_docs.document_id  ORDER BY  id  ,  date  DESC              ");
        */
        String longQuery = new String("select * from k where ID > 4");

        /*		String longQuery = "select  *  from  (  SELECT  intermediate.id  as  id  ,  intermediate.date  as          "
        				+ "date  FROM  (  SELECT  DISTINCT   (  id  )   FROM  (  SELECT                           "
        				+ "wct_workflows.workflow_id  as  id  ,  wct_transaction.date  as  date  FROM             "
        				+ "wct_audit_entry  ,  wct_transaction  ,  wct_workflows  WHERE                           "
        				+ "(  wct_audit_entry.privilege  =  'W'  or  wct_audit_entry.privilege  =                 "
        				+ "'C'  ))))";
        */
        /*		String longQuery = "select  *  from  d WHERE                           "
        				+ "(  wct_audit_entry.privilege  =  'W'  or  wct_audit_entry.privilege  =                 "
        				+ "'C'  )  and  wct_audit_entry.outcome  =  't'  and                                      "
        				+ "wct_audit_entry.transaction_id  =  wct_transaction.transaction_id  and                 "
        				+ "wct_transaction.user_id  = 164 and  wct_audit_entry.object_id  =                       "
        				+ "wct_workflows.active_version_id ";
        */
        StringReader stringReader = new StringReader(longQuery);
        Statement statement = parserManager.parse(stringReader);
        //	stringReader = new StringReader(longQuery);
        //	IStatement statement2 = parserManager.parse(stringReader);
        //	stringReader = null;
        // statement2 = null;
        statement = null;
        parserManager = null;
        longQuery = null;
        System.gc();
        System.out.println(Runtime.getRuntime().totalMemory()
                - Runtime.getRuntime().freeMemory());

    }
}