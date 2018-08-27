/*
 * Open Teradata Viewer ( editor language support )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * A class that eats the stdout or stderr of a running <tt>Process</tt> to
 * prevent deadlock.
 *
 * @author D. Campione
 * 
 */
public class OutputCollector implements Runnable {

    private InputStream in;
    private StringBuffer sb;

    /**
     * Ctor.
     *
     * @param in The input stream.
     */
    public OutputCollector(InputStream in) {
        this(in, true);
    }

    /**
     * Ctor.
     *
     * @param in The input stream.
     * @param sb The buffer in which to collect the output.
     */
    public OutputCollector(InputStream in, StringBuffer sb) {
        this.in = in;
        this.sb = sb;
    }

    /**
     * Ctor.
     *
     * @param in The input stream.
     * @param collect Whether to actually collect the output in a buffer.
     *        If this is <code>false</code>, then {@link #getOutput()} will
     *        return <code>null</code>. This parameter can be used if you want
     *        to eat, but ignore, stdout or stderr for a process.
     */
    public OutputCollector(InputStream in, boolean collect) {
        this.in = in;
        if (collect) {
            sb = new StringBuffer();
        }
    }

    /**
     * Returns the output collected from the stream.
     *
     * @return The output.
     */
    public StringBuffer getOutput() {
        return sb;
    }

    /**
     * Called every time a line is read from the stream. This allows subclasses
     * to handle lines differently. They can also call into the super
     * implementation if they want to log the lines into the buffer.
     *
     * @param line The line read.
     * @throws IOException If an IO error occurs.
     */
    protected void handleLineRead(String line) throws IOException {
        if (sb != null) {
            sb.append(line).append('\n');
        }
    }

    @Override
    public void run() {
        String line = null;

        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(in));

            try {
                while ((line = r.readLine()) != null) {
                    handleLineRead(line);
                }
            } finally {
                r.close();
            }

        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
        }
    }
}