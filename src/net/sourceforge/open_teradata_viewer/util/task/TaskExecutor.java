/*
 * Open Teradata Viewer ( util task )
 * Copyright (C) 2012, D. Campione
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

package net.sourceforge.open_teradata_viewer.util.task;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.util.Generator;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class TaskExecutor extends Thread {

    private TaskPool taskPool = null;
    private static Generator sequence = new Generator();
    private Task currentTask;

    public TaskExecutor(TaskPool taskSpool) {
        super("TaskExecuter-" + (new Long(sequence.getNextVal())).toString());
        this.taskPool = taskSpool;
        setPriority(Thread.MIN_PRIORITY);
        setDaemon(true);
    }

    static void pool(TaskPool pool) {
        (new TaskExecutor(pool)).start();
    }

    public void run() {
        taskPool.beginPool(this);
        try {
            while (true) {
                currentTask = taskPool.nextTask();
                if (currentTask != null && !currentTask.isCanceled()) {
                    try {
                        currentTask.startup();
                        taskPool.beginTask(this, currentTask);
                        currentTask.setExecuting(true);
                        try {
                            currentTask.run();
                        } finally {
                            currentTask.setExecuting(false);
                            taskPool.endTask(this, currentTask);
                            currentTask.finished();
                            currentTask = null;
                        }
                    } catch (Throwable t) {
                        ExceptionDialog.notifyException(t);
                    }
                } else {
                    break;
                }
            }
        } finally {
            taskPool.endPool(this);
        }
    }

    public Task getCurrentTask() {
        return currentTask;
    }
}