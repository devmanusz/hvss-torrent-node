package dto;

import dto.commdata.NodeCommDataObject;
import lombok.Data;

@Data
public class NodeClientInfoData extends NodeCommDataObject {

    long uptime;
    long startTime;
    int threadCount;
    int peakThreadCount;
    int availableProcessors;
    double systemLoad;
    long heapMemoryUsage;
    long heapMemoryMax;
    long nonHeapMemoryUsage;
    long nonHeapMemoryMax;
    int torrentsRunning;
    int torrentsTotal;

}
