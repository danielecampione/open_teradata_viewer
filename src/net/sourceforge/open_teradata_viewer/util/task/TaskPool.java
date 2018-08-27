/*
 * Open Teradata Viewer ( util task )
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

package net.sourceforge.open_teradata_viewer.util.task;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.Action;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class TaskPool {

    private final static String GLOBAL = "net.sourceforge.open_teradata_viewer.util.task.Global";

    private ArrayList<Task> taskList = new ArrayList<Task>();
    private String name = null;
    private boolean pooling = false;

    public TaskPool() {
        super();
    }

    public TaskPool(String name) {
        super();
        this.name = name;
    }

    public static TaskPool getTaskPool(String name) {
        return TaskPoolManager.getTaskPool(name);
    }

    /**
     * Globally accessible TaskPool.
     */
    public static TaskPool getTaskPool() {
        return getTaskPool(GLOBAL);
    }

    public int getCount() {
        synchronized (taskList) {
            return taskList.size() + (pooling ? 1 : 0);
        }
    }

    public int getTaskCount() {
        synchronized (taskList) {
            return taskList.size();
        }
    }

    public Task getTask(int index) {
        synchronized (taskList) {
            return taskList.get(index);
        }
    }

    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Null Runnable passed");
        }
        synchronized (taskList) {
            taskList.add(task);
            task.setTaskPool(this);
            TaskPoolManager.fireTaskPoolContainerListener(
                    TaskPoolManager.EventContainer.ADD_TASK, task);
            if (!pooling) {
                pooling = true;
                TaskExecutor.pool(this);
            } else {
                Thread.yield();
            }
        }
    }

    public void addTask(final Action action) {
        Object name = action.getValue(Action.NAME);
        addTask(new Task(name == null ? "Noname" : name.toString()) {
            public void run() {
                action.actionPerformed(new ActionEvent(this, 0, action
                        .getValue(Action.NAME).toString()));
            }
        });
    }

    public void removeTask(Task task) {
        synchronized (taskList) {
            taskList.remove(task);
            TaskPoolManager.fireTaskPoolContainerListener(
                    TaskPoolManager.EventContainer.REMOVE_TASK, task);
        }
    }

    synchronized void beginPool(TaskExecutor taskExecuter) {
        TaskPoolManager.fireTaskPoolListener(
                TaskPoolManager.EventPool.BEGIN_POOL, taskExecuter, null);
    }

    synchronized void endPool(TaskExecutor taskExecuter) {
        pooling = false;
        TaskPoolManager.fireTaskPoolListener(
                TaskPoolManager.EventPool.END_POOL, taskExecuter, null);
    }

    synchronized void beginTask(TaskExecutor taskExecuter, Task task) {
        TaskPoolManager.fireTaskPoolListener(
                TaskPoolManager.EventPool.BEGIN_TASK, taskExecuter, task);
    }

    synchronized void endTask(TaskExecutor taskExecuter, Task task) {
        TaskPoolManager.fireTaskPoolListener(
                TaskPoolManager.EventPool.END_TASK, taskExecuter, task);
    }

    Task nextTask() {
        Task task = null;
        synchronized (taskList) {
            if (taskList.size() > 0) {
                task = taskList.remove(0);
            }
        }
        if (task != null) {
            TaskPoolManager.fireTaskPoolContainerListener(
                    TaskPoolManager.EventContainer.REMOVE_TASK, task);
        }
        return task;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isPooling() {
        return this.pooling;
    }

    public Object getLock() {
        return taskList;
    }

}
