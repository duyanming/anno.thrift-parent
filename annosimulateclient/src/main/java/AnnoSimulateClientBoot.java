import anno.thrift.client.DefaultConfigManager;
import simulate.requestTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class AnnoSimulateClientBoot {
    public static void main(String[] args) throws Exception {
        DefaultConfigManager.SetDefaultConnectionPool(500, 4, 50);
        DefaultConfigManager.SetDefaultConfiguration("AnnoClient", "127.0.0.1", 6660);
        CyclicBarrier cb = new CyclicBarrier(2000+1 );
        for (int i = 0; i < 2000; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    System.out.println("task begin:" + finalI);
                    new requestTest().Request1();
                    System.out.println("task end:" + finalI);
                    cb.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
//            new requestTest().Request1AnnoNet(i);
        }
       cb.await();
        System.out.println("End All");
    }
}
