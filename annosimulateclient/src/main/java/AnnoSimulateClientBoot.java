import anno.thrift.client.DefaultConfigManager;
import org.apache.thrift.TException;
import oshiTest.oshiDemo;
import simulate.requestTest;
import storageTest.StorageDemo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class AnnoSimulateClientBoot {
  public static void main(String[] args) throws Exception {
            Rpc();
    //        new oshiDemo().Handle();
//    new StorageDemo().Request();
    System.out.println("End All");
  }

  private static void Rpc() throws TException, BrokenBarrierException, InterruptedException {
    DefaultConfigManager.SetDefaultConnectionPool(500, 4, 50);
    DefaultConfigManager.SetDefaultConfiguration("AnnoClient", "127.0.0.1", 6660);
    Integer count=10;
    CyclicBarrier cb = new CyclicBarrier(count + 1);
    for (int i = 0; i < count; i++) {
      int finalI = i;
      new Thread(
              () -> {
                try {
                  System.out.println("task begin:" + finalI);
                  new requestTest().Request1();//调用java service
                  new requestTest().Request1AnnoNet(finalI);//调用net service
                  System.out.println("task end:" + finalI);
                  cb.await();
                } catch (Exception e) {
                  e.printStackTrace();
                }
              })
          .start();
      //            new requestTest().Request1AnnoNet(i);
    }
    cb.await();
  }
}
