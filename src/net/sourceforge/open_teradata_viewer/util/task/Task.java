/*
 * Open Teradata Viewer ( util task )
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

package net.sourceforge.open_teradata_viewer.util.task;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public abstract class Task implements Runnable {

    private String description = null;
    private TaskPool taskPool;
    private volatile boolean canceled = false;
    private volatile int percenExecution = 0;
    private volatile boolean executing = false;

    public Task() {
    }

    public Task(String description) {
        this.description = description;
    }

    protected void startup() {
    }

    protected void finished() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    void setTaskPool(TaskPool taskPool) {
        this.taskPool = taskPool;
    }

    public TaskPool getTaskPool() {
        return taskPool;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
        if (this.canceled && taskPool != null && !executing) {
            taskPool.removeTask(this);
        }
    }

    public int getPercenExecution() {
        return percenExecution;
    }

    public void setPercenExecution(int percenExecution) {
        this.percenExecution = percenExecution;
        if (this.percenExecution > 100) {
            this.percenExecution = 100;
        }
    }

    public boolean isExecuting() {
        return executing;
    }

    void setExecuting(boolean executing) {
        this.executing = executing;
    }

}
