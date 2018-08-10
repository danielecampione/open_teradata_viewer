/*
 * Open Teradata Viewer ( util timer )
 * Copyright (C) 2011, D. Campione
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

package net.sourceforge.open_teradata_viewer.util.timer;

import java.util.HashMap;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class TimerManager {

    private static HashMap<String, TimerQueue> timerList = new HashMap<String, TimerQueue>();

    public static TimerQueue getGlobal() {
        return getTimer("net.sourceforge.open_teradata_viewer.timer.Global");
    }

    public static TimerQueue getTimer(String name) {
        synchronized (timerList) {
            TimerQueue timer = timerList.get(name);
            if (timer == null) {
                timer = new TimerQueue(name);
                timerList.put(name, timer);
            }
            return timer;
        }
    }

    public static void remove(String name) {
        synchronized (timerList) {
            TimerQueue timer = timerList.get(name);
            if (timer != null) {
                timer.dispose();
                timerList.remove(name);
            }
        }
    }

    public static HashMap<String, TimerQueue> getLock() {
        return timerList;
    }

}
