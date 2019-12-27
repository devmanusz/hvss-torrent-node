package config;

import lombok.Data;

import java.util.logging.Level;

@Data
public class ClientConfiguration {
    String clientOid = "";
    String hostAddress = "";
    int hostPort;
    Boolean ssl;
    boolean clientRegistered = false;
    boolean clientRegistrationSent = false;
    boolean torrentSlave = false;
    boolean encodeSlave = false;
    String masterAddress;
    int masterPort;
    String torrentDownloadBasePath;
    String torrentDownloadFileSavePath;
    Level loggerLevel;


}
