/*
 * Open Teradata Viewer ( editor )
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

package test.net.sourceforge.open_teradata_viewer.editor;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * Runs Swing unit tests on the EDT. Stolen from <a
 * href="https://community.oracle.com/thread/1350403">
 * https://community.oracle.com/thread/1350403</a>.<p>
 * This particular class is public domain.
 *
 * @author D. Campione
 *
 */
public class SwingRunner extends BlockJUnit4ClassRunner {

    public SwingRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public void run(final RunNotifier runNotifier) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    SwingRunner.super.run(runNotifier);
                }
            });
        } catch (InterruptedException e) {
            ExceptionDialog.hideException(e);
        } catch (InvocationTargetException e) {
            ExceptionDialog.hideException(e);
        }
    }
}