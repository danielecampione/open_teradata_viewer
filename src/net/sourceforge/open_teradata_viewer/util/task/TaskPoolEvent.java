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

import java.util.EventObject;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class TaskPoolEvent extends EventObject {

    private static final long serialVersionUID = -1353001853867714183L;

    private TaskExecutor taskExecutor;
    private Task task;

    public TaskPoolEvent(TaskExecutor taskExecutor, Task task) {
        super(taskExecutor);
        this.taskExecutor = taskExecutor;
        this.task = task;
    }

    public TaskExecutor getTaskExecuter() {
        return taskExecutor;
    }

    public Task getTask() {
        return task;
    }
}
