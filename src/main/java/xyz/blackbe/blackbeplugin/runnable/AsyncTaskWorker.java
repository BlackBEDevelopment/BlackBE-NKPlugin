package xyz.blackbe.blackbeplugin.runnable;

import xyz.blackbe.blackbeplugin.task.BlackBETask;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;

@SuppressWarnings("InfiniteLoopStatement")
public class AsyncTaskWorker extends Thread {
    private final Queue<BlackBETask> workQueue = new LinkedBlockingQueue<>();

    public AsyncTaskWorker() {
        this.setName("BlackBE-API-Connection-Thread");
    }

    @Override
    public void run() {
        while (true) {
            BlackBETask task = workQueue.poll();
            if (task != null) {
                task.invoke();
            } else {
                LockSupport.park();
            }
        }
    }

    public void submitTask(BlackBETask task) {
        workQueue.offer(task);
        LockSupport.unpark(this);
    }
}
