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

/**
 * Holds what is shared by the whole application, like the database
 * connection, and gives access to the {@link DocumentState state} of the
 * document currently shown in the editor: the accessors of the file, the query
 * and the results below always refer to that document.
 *
 * @author D. Campione
 *
 */
public final class Context {

    private static final Context CONTEXT = new Context();

    private ConnectionData connectionData;
    private volatile DocumentState documentState = new DocumentState();
    private int fetchLimit = 2000;

    private Context() {
    }

    public static Context getInstance() {
        return CONTEXT;
    }

    public ConnectionData getConnectionData() {
        return connectionData;
    }

    public void setConnectionData(ConnectionData newConnectionData) {
        connectionData = newConnectionData;
    }

    /**
     * @return The state of the document currently shown in the editor.
     */
    public DocumentState getDocumentState() {
        return documentState;
    }

    /**
     * Makes the given state the one the accessors of this class refer to, that
     * is the state of the document currently shown in the editor.
     *
     * @param documentState The state of the document to show; never
     *        <code>null</code>.
     */
    public void setDocumentState(DocumentState documentState) {
        if (documentState == null) {
            throw new IllegalArgumentException("documentState == null");
        }
        this.documentState = documentState;
    }

    public ResultSet getResultSet() {
        return documentState.getResultSet();
    }

    /**
     * Replaces the {@link ResultSet} held for the current document, see
     * {@link DocumentState#setResultSet(ResultSet)}.
     *
     * @param resultSet the new {@link ResultSet} to hold, or
     *        <code>null</code>.
     */
    public void setResultSet(ResultSet resultSet) {
        documentState.setResultSet(resultSet);
    }

    public int[] getColumnTypes() {
        return documentState.getColumnTypes();
    }

    public void setColumnTypes(int[] columnTypes) {
        documentState.setColumnTypes(columnTypes);
    }

    public String[] getColumnTypeNames() {
        return documentState.getColumnTypeNames();
    }

    public void setColumnTypeNames(String[] columnTypeNames) {
        documentState.setColumnTypeNames(columnTypeNames);
    }

    public String getQuery() {
        return documentState.getQuery();
    }

    public void setQuery(String query) {
        documentState.setQuery(query);
    }

    public File getOpenedFile() {
        return documentState.getOpenedFile();
    }

    public void setOpenedFile(File openedFile) {
        documentState.setOpenedFile(openedFile);
    }

    public byte[][] getSavedLobs() {
        return documentState.getSavedLobs();
    }

    public void setSavedLobs(byte[][] savedLobs) {
        documentState.setSavedLobs(savedLobs);
    }

    public int getFetchLimit() {
        return fetchLimit;
    }

    public void setFetchLimit(int fetchLimit) {
        this.fetchLimit = fetchLimit;
    }
}
