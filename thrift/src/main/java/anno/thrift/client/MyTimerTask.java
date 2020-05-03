package anno.thrift.client;

import java.util.TimerTask;


public class MyTimerTask extends TimerTask {

    private Runnable task;
    private Thread thread;

    public MyTimerTask(Runnable task) {
        this.task = task;
        thread = new Thread(task);
    }

    @Override
    public void run() {

        if (thread.isAlive()) {
            return;
        }
        thread = new Thread(task);
        thread.start();
    }
}
