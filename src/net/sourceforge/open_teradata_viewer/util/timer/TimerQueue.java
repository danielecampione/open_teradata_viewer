/*
 * Open Teradata Viewer ( util timer )
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

package net.sourceforge.open_teradata_viewer.util.timer;

import java.util.ArrayList;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class TimerQueue extends Thread {

    private ArrayList<Timer> timerArray = new ArrayList<Timer>();
    private boolean stopExecute = false;

    public TimerQueue(String name) {
        super(name);
        setDaemon(true);
        start();
    }

    public void dispose() {
        stopExecute = true;
        synchronized (this) {
            this.notify();
        }
        while (stopExecute) {
            yield();
        }
        synchronized (timerArray) {
            while (timerArray.size() > 0) {
                timerArray.remove(0);
            }
        }
        timerArray = null;
    }

    public void add(Timer timer) {
        synchronized (timerArray) {
            timerArray.add(timer);
            timer.lastExecuted = System.nanoTime();
            timer.timerManager = this;
        }
        synchronized (this) {
            this.notify();
        }
    }

    public void run(Timer timer) {
        timer.lastExecuted = 0;
        synchronized (this) {
            this.notify();
        }
    }

    public int getCount() {
        synchronized (timerArray) {
            return timerArray.size();
        }
    }

    public Timer getTimer(int index) {
        synchronized (timerArray) {
            return timerArray.get(index);
        }
    }

    private long calcWait() {
        long waitTime = 10000000000L;
        long currTime = System.nanoTime();

        synchronized (timerArray) {
            for (int i = 0; i < timerArray.size(); i++) {
                Timer timer = timerArray.get(i);
                if (timer.isEnabled()) {
                    if (waitTime > timer.nextExecution - currTime) {
                        waitTime = timer.nextExecution - currTime;
                    }
                }
            }
        }
        return waitTime < 1 ? 1 : waitTime;
    }

    public void run() {
        stopExecute = false;
        while (!stopExecute) {
            long waitTime = calcWait();

            try {
                synchronized (this) {
                    wait(waitTime / 1000000L, (int) (waitTime % 1000000L));
                }
            } catch (InterruptedException ie) {
                ExceptionDialog.ignoreException(ie);
            }

            if (!stopExecute) {
                synchronized (timerArray) {
                    int i = 0;
                    while (i < timerArray.size()) {
                        Timer timer = timerArray.get(i);
                        if (timer.isCancel()) {
                            timerArray.remove(i);
                        } else {
                            long time = System.nanoTime();
                            if (timer.isEnabled()
                                    && timer.nextExecution <= time) {
                                timer.nextExecution = time + timer.interval;
                                try {
                                    timer.run();
                                } catch (Throwable t) {
                                    ExceptionDialog.notifyException(t);
                                }
                                timer.lastExecuted = time;
                                if (timer.isOnce()) {
                                    timer.cancel();
                                }
                            }
                            if (timer.isCancel()) {
                                timerArray.remove(i);
                            } else {
                                i++;
                            }
                        }
                    }
                }
            }
        }
        stopExecute = false;
    }
}