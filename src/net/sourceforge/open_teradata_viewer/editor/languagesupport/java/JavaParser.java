/*
 * Open Teradata Viewer ( editor language support java )
 * Copyright (C) 2015, D. Campione
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

import javax.swing.text.Element;

import net.sourceforge.open_teradata_viewer.DocumentReader;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CompilationUnit;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.Scanner;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.notices.ParserNotice;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.parser.ASTFactory;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.AbstractParser;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.DefaultParseResult;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.DefaultParserNotice;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParseResult;

/**
 * Parses Java code in an <tt>SyntaxTextArea</tt>.<p>
 *
 * Like all STA <tt>IParser</tt>s, a <tt>JavaParser</tt> instance is notified
 * when the STA's text content changes. After a small delay, it will parse the
 * content as Java code, building an AST and looking for any errors. When
 * parsing is complete, a property change event of type {@link
 * #PROPERTY_COMPILATION_UNIT} is fired. Listeners can check the new value of
 * the property for the {@link CompilationUnit} built that represents the source
 * code in the text area. Note that the <tt>CompilationUnit</tt> may be
 * incomplete if there were parsing/syntax errors (it will usually be complete
 * "up to" the error in the content).<p>
 *
 * This parser cannot be shared amongst multiple instances of
 * <tt>SyntaxTextArea</tt>.<p>
 * 
 * @author D. Campione
 * 
 */
public class JavaParser extends AbstractParser {

    /**
     * The property change event that's fired when the document is re-parsed.
     * Applications can listen for this property change and update themselves
     * accordingly.
     */
    public static final String PROPERTY_COMPILATION_UNIT = "CompilationUnit";

    private CompilationUnit cu;
    private PropertyChangeSupport support;
    private DefaultParseResult result;

    /** Ctor. */
    public JavaParser(SyntaxTextArea textArea) {
        support = new PropertyChangeSupport(this);
        result = new DefaultParseResult(this);
    }

    /** Adds all notices from the Java parser to the results object. */
    private void addNotices(SyntaxDocument doc) {
        result.clearNotices();
        int count = cu == null ? 0 : cu.getParserNoticeCount();

        if (count == 0) {
            return;
        }

        for (int i = 0; i < count; i++) {
            ParserNotice notice = cu.getParserNotice(i);
            int offs = getOffset(doc, notice);
            if (offs > -1) {
                int len = notice.getLength();
                result.addNotice(new DefaultParserNotice(this, notice
                        .getMessage(), notice.getLine(), offs, len));
            }
        }
    }

    public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        support.addPropertyChangeListener(prop, l);
    }

    /**
     * Returns the compilation unit from the last time the text area was parsed.
     *
     * @return The compilation unit or <code>null</code> if it hasn't yet been
     *         parsed or an unexpected error occurred while parsing.
     */
    public CompilationUnit getCompilationUnit() {
        return cu;
    }

    public int getOffset(SyntaxDocument doc, ParserNotice notice) {
        Element root = doc.getDefaultRootElement();
        Element elem = root.getElement(notice.getLine());
        int offs = elem.getStartOffset() + notice.getColumn();
        return offs >= elem.getEndOffset() ? -1 : offs;
    }

    /** {@inheritDoc} */
    @Override
    public IParseResult parse(SyntaxDocument doc, String style) {
        cu = null;
        result.clearNotices();
        // Always spell check all lines, for now
        int lineCount = doc.getDefaultRootElement().getElementCount();
        result.setParsedLines(0, lineCount - 1);

        DocumentReader r = new DocumentReader(doc);
        Scanner scanner = new Scanner(r);
        scanner.setDocument(doc);
        ASTFactory fact = new ASTFactory();
        long start = System.currentTimeMillis();
        try {
            cu = fact.getCompilationUnit("SomeFile.java", scanner);
            long time = System.currentTimeMillis() - start;
            result.setParseTime(time);
        } catch (IOException ioe) {
            result.setError(ioe);
        }

        r.close();

        addNotices(doc);
        support.firePropertyChange(PROPERTY_COMPILATION_UNIT, null, cu);
        return result;
    }

    public void removePropertyChangeListener(String prop,
            PropertyChangeListener l) {
        support.removePropertyChangeListener(prop, l);
    }
}