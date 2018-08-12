/*
 * Open Teradata Viewer ( util timer )
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

package net.sourceforge.open_teradata_viewer.util.timer;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public abstract class Timer implements Runnable {

    long interval = 0;
    private boolean enabled = true;
    private boolean cancel = false;
    private boolean once = false;
    TimerQueue timerManager = null;
    long lastExecuted = System.nanoTime(); //System.currentTimeMillis();
    long nextExecution;

    public Timer(long interval) {
        super();
        setInterval(interval);
        nextExecution = lastExecuted + this.interval;
    }

    public Timer(long interval, boolean once) {
        this(interval);
        this.once = once;
    }

    public void setInterval(long interval) {
        this.interval = interval * 1000000L;
    }

    public long getInterval() {
        return interval / 1000000L;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (this.enabled) {
                lastExecuted = System.nanoTime(); //System.currentTimeMillis();
                nextExecution = lastExecuted + this.interval;
                synchronized (timerManager) {
                    timerManager.notify();
                }
            }
        }
    }

    public void restart() {
        setEnabled(false);
        setEnabled(true);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void cancel() {
        this.cancel = true;
    }

    public boolean isCancel() {
        return cancel;
    }

    public boolean isOnce() {
        return once;
    }

}
