package com.semihbkgr.gorun.util;

import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

public class IntervalTimer {

    private static final String TAG = IntervalTimer.class.getName();

    private final Thread thread;
    private final long timeInterval;

    //State
    //-1: Stopped
    //0: Waiting
    //1: OnTime
    private final AtomicInteger state;

    public IntervalTimer(long timeInterval) {
        this(timeInterval, "ExpirationTimerThread");
    }

    public IntervalTimer(long timeInterval, String name) {
        if (timeInterval < 1) throw new IllegalArgumentException("TimeInterval argument must be positive value");
        this.timeInterval = timeInterval;
        this.state = new AtomicInteger(0);
        this.thread = new Thread(() -> {
            Log.i(TAG, name + " thread has been started");
            while (!isStopped()) {
                if (!isWaiting()) {
                    try {
                        Thread.sleep(timeInterval);
                        makeWaiting();
                    } catch (InterruptedException ignore) {
                    }
                } else Thread.yield();
            }
            Log.i(TAG, name + " thread has been terminated");
        }, name);
        thread.start();
    }

    public void reflesh() {
        if (isStopped())
            throw new IllegalStateException(this.getClass().getName() + " " + this.thread.getName() + " has already been stopped");
        makeOnTime();
        thread.interrupt();
    }

    public boolean result() {
        if (isStopped())
            throw new IllegalStateException(this.getClass().getName() + " " + this.thread.getName() + " has already been stopped");
        return isOnTime();
    }

    public void stop() {
        if (isStopped())
            throw new IllegalStateException(this.getClass().getName() + " " + this.thread.getName() + " has already been stopped");
        makeStopped();
        thread.interrupt();
        Log.i(TAG, this.getClass().getName() + " " + this.thread.getName() + " has been stopped successfully");
    }

    private boolean isStopped() {
        return state.get() == -1;
    }

    private void makeStopped() {
        state.set(-1);
    }

    private boolean isWaiting() {
        return state.get() == 0;
    }

    private void makeWaiting() {
        state.set(0);
    }

    private boolean isOnTime() {
        return state.get() == 1;
    }

    private void makeOnTime() {
        state.set(1);
    }


}
