package anno.thrift.sysInfo;

import com.sun.management.OperatingSystemMXBean;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryUsage;
import java.text.NumberFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UseSysInfoWatch {
    private static long startTime;

    public static void SysInit() {
        startTime = System.currentTimeMillis();
    }

    public static ServerStatus GetServerStatus() {
        ServerStatus info = new ServerStatus();
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        info.MemoryTotal = osmxb.getTotalPhysicalMemorySize() / 1024.0 / 1024;
        info.MemoryTotal = Double.parseDouble(String.format("%.2f", info.MemoryTotal));

        info.MemoryTotalUse = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / 1024 / 1024;
        info.MemoryTotalUse = Double.parseDouble(String.format("%.2f", info.MemoryTotalUse));

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();

        long usedMemorySize = memoryUsage.getUsed();
        info.Memory = usedMemorySize / 1024 / 1024;

        SystemInfo systemInfo = new SystemInfo();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();

        try {
            // 睡眠1s
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception ignored) {
        }
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()]
                - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()]
                - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()]
                - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()]
                - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()]
                - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()]
                - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()]
                - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()]
                - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;

        info.CpuTotalUse = ((totalCpu- idle) * 1.0 / totalCpu) * 100;
        info.CpuTotalUse= Double.parseDouble(String.format("%.2f",info.CpuTotalUse));

        info.CurrentTime = new Date();

        info.Cpu = (user * 1.0 / totalCpu) * 100;
        info.Cpu= Double.parseDouble(String.format("%.2f",info.Cpu));

        long runtime = System.currentTimeMillis() - startTime;

        long day = runtime / (24 * 60 * 60 * 1000);
        long hour = (runtime % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
        long minute = (runtime % ((60 * 60 * 1000))) / (60 * 1000);
        long second = (runtime % ((60 * 1000))) / (1000);
        info.RunTime = day + ":" + hour + ":" + minute + ":" + second;
        return info;
    }
}
