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
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * What an editor document holds besides its text: the file it was opened from,
 * the last query run on it and the results of that query.
 * <p>
 * Everything that is shared by all the documents, like the database connection,
 * stays in {@link Context}, which exposes the state of the document currently
 * shown through its own accessors.
 *
 * @author D. Campione
 */
public final class DocumentState {

    private ResultSet resultSet;
    private int[] columnTypes;
    private String[] columnTypeNames;
    private String query;
    private File openedFile;
    private byte[][] savedLobs;

    public ResultSet getResultSet() {
        return resultSet;
    }

    /**
     * Replaces the currently held {@link ResultSet}, if any, with a new one
     * (or with <code>null</code>).
     * <p>
     * Before the reference is actually replaced, the {@link Statement} that
     * produced the <em>previous</em> {@link ResultSet} (if different from
     * the new one) is closed - which, per the JDBC specification, also
     * closes the {@link ResultSet} itself and releases the corresponding
     * server-side cursor. Without this, every successive query execution
     * (e.g. through <code>RunAction</code>) silently abandoned the previous
     * query's <code>Statement</code>/<code>ResultSet</code>, leaking JDBC
     * resources for the whole duration of the session.
     * <p>
     * The <em>current</em> {@link ResultSet} is never touched here, so
     * in-place grid editing/deletion (e.g. <code>DeleteAction</code>), which
     * relies on it staying open, is unaffected.
     *
     * @param resultSet the new {@link ResultSet} to hold, or
     *        <code>null</code>.
     */
    public void setResultSet(ResultSet resultSet) {
        if (this.resultSet != null && this.resultSet != resultSet) {
            try {
                Statement previousStatement = this.resultSet.getStatement();
                if (previousStatement != null) {
                    previousStatement.close();
                }
            } catch (Throwable t) {
                ExceptionDialog.hideException(t);
            }
        }
        this.resultSet = resultSet;
    }

    public int[] getColumnTypes() {
        return columnTypes;
    }

    public void setColumnTypes(int[] columnTypes) {
        this.columnTypes = columnTypes;
    }

    public String[] getColumnTypeNames() {
        return columnTypeNames;
    }

    public void setColumnTypeNames(String[] columnTypeNames) {
        this.columnTypeNames = columnTypeNames;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public File getOpenedFile() {
        return openedFile;
    }

    public void setOpenedFile(File openedFile) {
        this.openedFile = openedFile;
    }

    public byte[][] getSavedLobs() {
        return savedLobs;
    }

    public void setSavedLobs(byte[][] savedLobs) {
        this.savedLobs = savedLobs;
    }
}
