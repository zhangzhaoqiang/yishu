package com.yishu.net;

public class NetHeartThread implements Runnable {
    private Thread thread = null;
    private boolean isLoop = false;
    private WorkTask workTask = null;
    private int timeloop;

    @Override
    public void run() {
        isLoop=true;
        while (isLoop) {
            if (workTask != null)
                workTask.run();
            isStart=false;
            try {
                Thread.sleep(timeloop);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    boolean isStart=false;
    public void startThread() {
        if(!isStart){
            isStart=true;
            if (thread != null)
                stopThread();
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stopThread() {
        isLoop = false;
        try {
            if (thread != null)
                thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread = null;
    }

    public WorkTask getWorkTask() {
        return workTask;
    }

    public void setWorkTask(int timeloop, WorkTask workTask) {
        this.timeloop = timeloop;
        this.workTask = workTask;
    }

    public interface WorkTask {
        void run();
    }
}
