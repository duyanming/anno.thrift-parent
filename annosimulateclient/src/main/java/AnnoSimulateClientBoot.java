import anno.thrift.client.DefaultConfigManager;
import simulate.requestTest;

public class AnnoSimulateClientBoot {
    public static void main(String[] args) throws Exception {
        DefaultConfigManager.SetDefaultConnectionPool(200, 4, 50);
        DefaultConfigManager.SetDefaultConfiguration("AnnoClient", "127.0.0.1", 6660);

        for (int i = 0; i < 10000; i++) {
            new requestTest().Request1();
            System.out.printf("End"+i);
        }
        System.out.printf("End All");
    }
}
