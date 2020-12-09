package anno.thrift.sysInfo;

import com.sun.management.OperatingSystemMXBean;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryUsage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UseSysInfoWatch {
  private static long startTime;

  private static boolean _isWindows = isWindows();

  public static void SysInit() {
    startTime = System.currentTimeMillis();
  }

  public static ServerStatus GetServerStatus() {
    ServerStatus info = new ServerStatus();
    OperatingSystemMXBean osmxb =
        (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    info.MemoryTotal = osmxb.getTotalPhysicalMemorySize() / 1024.0 / 1024;
    info.MemoryTotal = Double.parseDouble(String.format("%.2f", info.MemoryTotal));

    info.MemoryTotalUse =
        (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / 1024 / 1024;
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
    long nice =
        ticks[CentralProcessor.TickType.NICE.getIndex()]
            - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
    long irq =
        ticks[CentralProcessor.TickType.IRQ.getIndex()]
            - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
    long softirq =
        ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()]
            - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
    long steal =
        ticks[CentralProcessor.TickType.STEAL.getIndex()]
            - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
    long cSys =
        ticks[CentralProcessor.TickType.SYSTEM.getIndex()]
            - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
    long user =
        ticks[CentralProcessor.TickType.USER.getIndex()]
            - prevTicks[CentralProcessor.TickType.USER.getIndex()];
    long iowait =
        ticks[CentralProcessor.TickType.IOWAIT.getIndex()]
            - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
    long idle =
        ticks[CentralProcessor.TickType.IDLE.getIndex()]
            - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
    long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;

    info.CpuTotalUse = ((totalCpu - idle) * 1.0 / totalCpu) * 100;
    info.CpuTotalUse = Double.parseDouble(String.format("%.2f", info.CpuTotalUse));

    info.CurrentTime = new Date();

    info.Cpu = (user * 1.0 / totalCpu) * 100;
    info.Cpu = Double.parseDouble(String.format("%.2f", info.Cpu));

    long runtime = System.currentTimeMillis() - startTime;

    long day = runtime / (24 * 60 * 60 * 1000);
    long hour = (runtime % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
    long minute = (runtime % ((60 * 60 * 1000))) / (60 * 1000);
    long second = (runtime % ((60 * 1000))) / (1000);
    info.RunTime = day + ":" + hour + ":" + minute + ":" + second;
    if (_isWindows) {

      return GetDrivesInfo(info);
    } else {
      return GetLinuxDrivesInfo(info);
    }
  }

  public static ServerStatus GetDrivesInfo(ServerStatus serverStatus) {
    File[] disks = File.listRoots();
    List<AnnoDrive> annoDrives = new ArrayList<>();
    for (File file : disks) {
      long totalSpace = file.getTotalSpace();
      // 磁盘大于1M
      if (totalSpace >= 1024 * 1024) {
        long freeSpace = file.getFreeSpace();
        AnnoDrive drive = new AnnoDrive();
        drive.setName(file.getPath().replace(":\\", ":"));
        drive.setFree(Math.round((freeSpace * 1000 / 1024 / 1024 / 1024)) / 1000.000);
        drive.setTotal(Math.round((totalSpace * 1000 / 1024 / 1024 / 1024)) / 1000.000);
        annoDrives.add(drive);
      }
    }
    serverStatus.setDrives(annoDrives);
    return serverStatus;
  }

  /*
  * 暂时使用和windows一致的算法，
  * */
  public static ServerStatus GetLinuxDrivesInfo(ServerStatus serverStatus) {
      return  GetDrivesInfo(serverStatus);
//    List<AnnoDrive> annoDrives = new ArrayList<>();
//
//    serverStatus.setDrives(annoDrives);
//    return serverStatus;
  }

  public static boolean isWindows() {
    return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
  }
}
