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

package net.sourceforge.open_teradata_viewer.actions;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.History;
import net.sourceforge.open_teradata_viewer.ResultSetTable;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public final class Actions implements ListSelectionListener, TableColumnModelListener {

    public static final CustomAction CONNECT = new ConnectAction();
    public static final CustomAction DISCONNECT = new DisconnectAction();
    public static final CustomAction COMMIT = new CommitAction();
    public static final CustomAction ROLLBACK = new RollbackAction();
    public static final CustomAction CUT = new CutAction();
    public static final CustomAction COPY = new CopyAction();
    public static final CustomAction COPY_AS_STYLED_TEXT = new CopyAsStyledTextAction();
    public static final CustomAction PASTE = new PasteAction();
    public static final CustomAction VIEW_LINE_HIGHLIGHT = new ViewLineHighlightAction();
    public static final CustomAction FADE_CURRENT_LINE_HIGHLIGHT = new FadeCurrentLineHighlightAction();
    public static final CustomAction VIEW_LINE_NUMBERS = new ViewLineNumbersAction();
    public static final CustomAction BOOKMARKS = new BookmarksAction();
    public static final CustomAction WORD_WRAP = new WordWrapAction();
    public static final CustomAction ANTIALIASING = new AntiAliasingAction();
    public static final CustomAction MARK_OCCURRENCES = new MarkOccurrencesAction();
    public static final CustomAction RIGHT_TO_LEFT = new RightToLeftAction();
    public static final CustomAction TAB_LINES = new TabLinesAction();
    public static final CustomAction ANIMATE_BRACKET_MATCHING = new AnimateBracketMatchingAction();
    public static final CustomAction PAINT_MATCHED_BRACKET_PAIR = new PaintMatchedBracketPairAction();
    public static final CustomAction TABS_EMULATED_BY_SPACES = new TabsEmulatedBySpacesAction();
    public static final CustomAction COMMENT = new CommentAction();
    public static final CustomAction UNCOMMENT = new UncommentAction();
    public static final CustomAction DATE_TIME = new DateTimeAction();
    public static final CustomAction INCREASE_INDENT = new IncreaseIndentAction();
    public static final CustomAction DECREASE_INDENT = new DecreaseIndentAction();
    public static final CustomAction INCREASE_FONT_SIZES = new IncreaseFontSizesAction();
    public static final CustomAction DECREASE_FONT_SIZES = new DecreaseFontSizesAction();
    public static final CustomAction INVERT_SELECTION_CASE = new InvertSelectionCaseAction();
    public static final CustomAction UPPER_SELECTION_CASE = new UpperSelectionCaseAction();
    public static final CustomAction LOWER_SELECTION_CASE = new LowerSelectionCaseAction();
    public static final CustomAction TOGGLE_SPELLING_PARSER = new ToggleSpellCheckingAction();
    public static final CustomAction SHOW_FIND_DIALOG = new ShowFindDialogAction();
    public static final CustomAction SHOW_REPLACE_DIALOG = new ShowReplaceDialogAction();
    public static final CustomAction GO_TO_LINE = new GoToLineAction();
    public static final CustomAction RUN = new RunAction();
    public static final CustomAction RUN_SCRIPT = new RunScriptAction();
    public static final CustomAction SCHEMA_BROWSER = new SchemaBrowserAction();
    public static final CustomAction FILE_OPEN = new FileOpenAction();
    public static final CustomAction FILE_SAVE = new FileSaveAction();
    public static final CustomAction CALCULATE_MD5_CHECKSUM = new CalculateMD5ChecksumAction();
    public static final CustomAction CALCULATE_SHA1_CHECKSUM_OF_A_FILE = new CalculateSHA1ChecksumOfAFileAction();
    public static final CustomAction CALCULATE_SHA1_CHECKSUM_OF_A_TEXT = new CalculateSHA1ChecksumOfATextAction();
    public static final CustomAction PRINT_PREVIEW = new PrintPreviewAction();
    public static final CustomAction FAVORITES = new FavoritesAction();
    public static final CustomAction HISTORY_PREVIOUS = new HistoryPreviousAction();
    public static final CustomAction HISTORY_NEXT = new HistoryNextAction();
    public static final CustomAction FORMAT_SQL = new FormatSQLAction();
    public static final CustomAction INDENT_XML = new IndentXMLAction();
    public static final CustomAction INSERT = new InsertAction();
    public static final CustomAction EDIT = new EditAction();
    public static final CustomAction DUPLICATE = new DuplicateAction();
    public static final CustomAction DELETE = new DeleteAction();
    public static final CustomAction LOB_IMPORT = new LobImportAction();
    public static final CustomAction LOB_EXPORT = new LobExportAction();
    public static final CustomAction LOB_COPY = new LobCopyAction();
    public static final CustomAction LOB_PASTE = new LobPasteAction();
    public static final GroupAction LOB_GROUP = new LobGroupAction();
    public static final CustomAction EXPORT_EXCEL = new ExportExcelAction();
    public static final CustomAction EXPORT_PDF = new ExportPdfAction();
    public static final CustomAction EXPORT_FLAT_FILE = new ExportFlatFileAction();
    public static final CustomAction EXPORT_INSERTS = new ExportInsertsAction();
    public static final GroupAction EXPORT_GROUP = new ExportGroupAction();
    public static final CustomAction FETCH_LIMIT = new FetchLimitAction();
    public static final CustomAction HELP = new HelpAction();
    public static final CustomAction SELECT_FROM = new SelectFromAction();
    public static final CustomAction DRIVERS = new DriversAction();
    public static final CustomAction ABOUT = new AboutAction();
    public static final CustomAction UPDATE = new UpdateAction();
    public static final CustomAction SHOW_TABLE = new ShowTableAction();
    public static final CustomAction SHOW_VIEW = new ShowViewAction();
    public static final CustomAction SHOW_PROCEDURE = new ShowProcedureAction();
    public static final CustomAction SHOW_MACRO = new ShowMacroAction();
    public static final GroupAction SHOW_OBJECT_GROUP = new ShowObjectGroupAction();
    public static final CustomAction ANALYZE_QUERY = new AnalyzeQueryAction();
    public static final CustomAction EXPLAIN_REQUEST = new ExplainRequestAction();
    public static final CustomAction FANCY_CELL_RENDERING = new FancyCellRenderingAction();
    public static final CustomAction SHOW_DESCRIPTION_WINDOW = new ShowDescriptionWindowAction();
    public static final CustomAction PARAMETER_ASSISTANCE = new ParameterAssistanceAction();
    public static final CustomAction MATCHED_BRACKET_POPUP = new MatchedBracketPopupAction();
    public static final CustomAction FULL_SCREEN = new FullScreenAction();
    public static final CustomAction NEW_MACRO = new NewMacroAction();
    public static final CustomAction EDIT_MACRO = new EditMacroAction();

    private static final Actions ACTIONS = new Actions();

    private Actions() {
    }

    public static Actions getInstance() {
        return ACTIONS;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        validateActions();
    }

    @Override
    public void columnSelectionChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            validateActions();
        }
    }

    @Override
    public void columnAdded(TableColumnModelEvent e) {
    }

    @Override
    public void columnRemoved(TableColumnModelEvent e) {
    }

    @Override
    public void columnMoved(TableColumnModelEvent e) {
    }

    @Override
    public void columnMarginChanged(ChangeEvent e) {
    }

    protected void validateTextActions() {
        HISTORY_PREVIOUS.setEnabled(History.getInstance().hasPrevious());
        HISTORY_NEXT.setEnabled(History.getInstance().hasNext());
    }

    protected void validateActions() {
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        DISCONNECT.setEnabled(isConnected);
        COMMIT.setEnabled(isConnected);
        ROLLBACK.setEnabled(isConnected);
        RUN.setEnabled(isConnected);
        RUN_SCRIPT.setEnabled(isConnected);
        SHOW_PROCEDURE.setEnabled(isConnected);
        SHOW_MACRO.setEnabled(isConnected);
        SHOW_VIEW.setEnabled(isConnected);
        SHOW_TABLE.setEnabled(isConnected);
        SHOW_OBJECT_GROUP.setEnabled(isConnected);
        EXPLAIN_REQUEST.setEnabled(isConnected);

        boolean hasResultSet = isConnected && Context.getInstance().getResultSet() != null;
        EXPORT_EXCEL.setEnabled(hasResultSet);
        EXPORT_PDF.setEnabled(hasResultSet);
        EXPORT_FLAT_FILE.setEnabled(hasResultSet);
        EXPORT_INSERTS.setEnabled(hasResultSet);
        EXPORT_GROUP.setEnabled(hasResultSet);

        boolean hasUpdatableResultSet;
        try {
            hasUpdatableResultSet = hasResultSet
                    && Context.getInstance().getResultSet().getConcurrency() == ResultSet.CONCUR_UPDATABLE;
        } catch (SQLException sqle) {
            hasUpdatableResultSet = false;
        }
        INSERT.setEnabled(hasUpdatableResultSet);

        boolean isRowSelected = hasResultSet && !ResultSetTable.getInstance().getSelectionModel().isSelectionEmpty();
        EDIT.setEnabled(isRowSelected);

        boolean isUpdatableRowSelected = hasUpdatableResultSet && isRowSelected;
        DUPLICATE.setEnabled(isUpdatableRowSelected);
        DELETE.setEnabled(isUpdatableRowSelected);

        boolean isLobSelected = hasResultSet && ResultSetTable.isLob(ResultSetTable.getInstance().getSelectedColumn());
        LOB_EXPORT.setEnabled(isLobSelected);
        LOB_COPY.setEnabled(isLobSelected);
        LOB_GROUP.setEnabled(isLobSelected);

        boolean isUpdatableLobSelected = hasUpdatableResultSet
                && ResultSetTable.isLob(ResultSetTable.getInstance().getSelectedColumn());
        LOB_IMPORT.setEnabled(isUpdatableLobSelected);

        boolean canImportFromMemory = Context.getInstance().getSavedLobs() != null && isUpdatableLobSelected
                && ResultSetTable.getInstance().getSelectedRowCount() == Context.getInstance().getSavedLobs().length;
        LOB_PASTE.setEnabled(canImportFromMemory);
    }
}