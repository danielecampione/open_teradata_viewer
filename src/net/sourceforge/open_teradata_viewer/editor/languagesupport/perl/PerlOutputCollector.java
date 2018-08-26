/*
 * Open Teradata Viewer ( editor language support perl )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.perl;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.Element;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.OutputCollector;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.DefaultParseResult;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.DefaultParserNotice;

/**
 * Listens to stderr from Perl to determine syntax errors in code.
 *
 * @author D. Campione
 * 
 */
class PerlOutputCollector extends OutputCollector {

    private PerlParser parser;
    private DefaultParseResult result;
    private Element root;

    private static final Pattern ERROR_PATTERN = Pattern
            .compile(" at .+ line (\\d+)\\.$");

    /**
     * Ctor.
     *
     * @param in The input stream.
     */
    public PerlOutputCollector(InputStream in, PerlParser parser,
            DefaultParseResult result, Element root) {
        super(in);
        this.parser = parser;
        this.result = result;
        this.root = root;
    }

    /** {@inheritDoc} */
    @Override
    protected void handleLineRead(String line) throws IOException {
        Matcher m = ERROR_PATTERN.matcher(line);

        if (m.find()) {
            line = line.substring(0, line.length() - m.group().length());

            int lineNumber = Integer.parseInt(m.group(1)) - 1;
            Element elem = root.getElement(lineNumber);
            int start = elem.getStartOffset();
            int end = elem.getEndOffset();

            DefaultParserNotice pn = new DefaultParserNotice(parser, line,
                    lineNumber, start, end - start);

            result.addNotice(pn);
        }
    }
}