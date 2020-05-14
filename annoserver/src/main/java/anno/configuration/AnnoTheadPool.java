package anno.configuration;

import java.util.concurrent.*;

/**
 * @author duyanming
 */
public class AnnoTheadPool {
    private static ExecutorService pool;

    public static ExecutorService getPool() {
        if(pool==null){
            int poolSize = Runtime.getRuntime().availableProcessors() * 2;
            BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(512);
            RejectedExecutionHandler policy = new ThreadPoolExecutor.DiscardPolicy();
            pool = new ThreadPoolExecutor(poolSize, poolSize,
                    0, TimeUnit.SECONDS,
                    queue,
                    policy);
        }
        return pool;
    }
}
