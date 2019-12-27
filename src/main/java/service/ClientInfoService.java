package service;

import dto.NodeClientInfoData;
import org.springframework.stereotype.Component;

import java.lang.management.*;

@Component
public class ClientInfoService {

    OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
    MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    public NodeClientInfoData getClientInfo(){
        NodeClientInfoData data = new NodeClientInfoData();
        data.setUptime(runtimeMXBean.getUptime());
        data.setStartTime(runtimeMXBean.getStartTime());
        data.setThreadCount(threadMXBean.getThreadCount());
        data.setPeakThreadCount(threadMXBean.getPeakThreadCount());
        data.setAvailableProcessors(operatingSystemMXBean.getAvailableProcessors());
        data.setSystemLoad(operatingSystemMXBean.getSystemLoadAverage());
        data.setHeapMemoryUsage(memoryMXBean.getHeapMemoryUsage().getUsed());
        data.setHeapMemoryMax(memoryMXBean.getHeapMemoryUsage().getMax());
        data.setNonHeapMemoryUsage(memoryMXBean.getNonHeapMemoryUsage().getUsed());
        data.setNonHeapMemoryMax(memoryMXBean.getNonHeapMemoryUsage().getMax());
        return data;
    }

}
