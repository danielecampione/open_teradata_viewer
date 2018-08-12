/*
 * Open Teradata Viewer ( util task )
 * Copyright (C) 2013, D. Campione
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

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.event.EventListenerList;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class TaskPoolManager {

    private static HashMap<String, TaskPool> taskPoolList = new HashMap<String, TaskPool>();
    private static ArrayList<TaskPool> taskPoolArray = new ArrayList<TaskPool>();
    private static EventListenerList taskPoolListenerList = new EventListenerList();

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    public enum EventPool {
        BEGIN_POOL, END_POOL, BEGIN_TASK, END_TASK
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    public enum EventContainer {
        ADD_TASK, REMOVE_TASK,
    }

    public static int getTaskCount() {
        int result = 0;
        synchronized (taskPoolList) {
            for (int i = 0; i < getCount(); i++) {
                result += getTaskPool(i).getCount();
            }
        }
        return result;
    }

    public static TaskPool getGlobal() {
        return getTaskPool("net.sourceforge.open_teradata_viewer.task.Global");
    }

    public static TaskPool getTaskPool(String name) {
        TaskPool taskPool;
        synchronized (taskPoolList) {
            taskPool = taskPoolList.get(name);
            if (taskPool == null) {
                taskPool = new TaskPool(name);
                taskPoolList.put(name, taskPool);
                taskPoolArray.add(taskPool);
            }
        }
        return taskPool;
    }

    public static void remove(String name) {
        synchronized (taskPoolList) {
            taskPoolArray.remove(taskPoolList.get(name));
            taskPoolList.remove(name);
        }
    }

    public static int getCount() {
        return taskPoolArray.size();
    }

    public static TaskPool getTaskPool(int index) {
        synchronized (taskPoolList) {
            return taskPoolArray.get(index);
        }
    }

    public static Object getLock() {
        return taskPoolList;
    }

    public static void addTaskPoolListener(ITaskPoolListener listener) {
        taskPoolListenerList.add(ITaskPoolListener.class, listener);
    }

    public static void removeTaskPoolListener(ITaskPoolListener listener) {
        taskPoolListenerList.remove(ITaskPoolListener.class, listener);
    }

    public static void fireTaskPoolListener(EventPool event,
            TaskExecutor taskExecutor, Task task) {
        ITaskPoolListener[] listeners = taskPoolListenerList
                .getListeners(ITaskPoolListener.class);
        for (int i = 0; i < listeners.length; i++) {
            switch (event) {
                case BEGIN_POOL :
                    listeners[i]
                            .beginPool(new TaskPoolEvent(taskExecutor, task));
                    break;
                case END_POOL :
                    listeners[i].endPool(new TaskPoolEvent(taskExecutor, task));
                    break;
                case BEGIN_TASK :
                    listeners[i]
                            .beginTask(new TaskPoolEvent(taskExecutor, task));
                    break;
                case END_TASK :
                    listeners[i].endTask(new TaskPoolEvent(taskExecutor, task));
                    break;
            }
        }
    }

    public static void addTaskPoolContainerListener(
            ITaskPoolContainerListener listener) {
        taskPoolListenerList.add(ITaskPoolContainerListener.class, listener);
    }

    public static void removeTaskPoolContainerListener(
            ITaskPoolContainerListener listener) {
        taskPoolListenerList.remove(ITaskPoolContainerListener.class, listener);
    }

    public static void fireTaskPoolContainerListener(EventContainer event,
            Task task) {
        ITaskPoolContainerListener[] listeners = taskPoolListenerList
                .getListeners(ITaskPoolContainerListener.class);
        for (int i = 0; i < listeners.length; i++) {
            switch (event) {
                case ADD_TASK :
                    listeners[i].addTask(new TaskPoolContainerEvent(task));
                    break;
                case REMOVE_TASK :
                    listeners[i].removeTask(new TaskPoolContainerEvent(task));
                    break;
            }
        }
    }
}