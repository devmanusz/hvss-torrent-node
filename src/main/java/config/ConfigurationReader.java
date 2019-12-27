package config;

import config.ClientConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Level;

@Configuration
public class ConfigurationReader {

    @Value("${socket.master.address}")
    private String masterAddress;

    @Value("${socket.master.port}")
    private int masterPort;

    @Value("${torrent.download.basepath}")
    private String torrentDownloadBasePath;

    @Value("${torrent.download.fileSavePath}")
    private String torrentFileSavePath;

    @Value("${application.logger.level}")
    private Level loggerLevel;

    @Value("${application.host.address}")
    private String hostAddress;

    @Value("${server.port}")
    private int hostPort;

    @Value("${application.ssl.enabled}")
    private Boolean ssl;

    @Bean
    public ClientConfiguration contentConfig() {

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMasterAddress(masterAddress);
        clientConfiguration.setMasterPort(masterPort);
        clientConfiguration.setTorrentDownloadBasePath(torrentDownloadBasePath);
        clientConfiguration.setTorrentDownloadFileSavePath(torrentFileSavePath);
        clientConfiguration.setLoggerLevel(loggerLevel);
        clientConfiguration.setHostAddress(hostAddress);
        clientConfiguration.setHostPort(hostPort);
        clientConfiguration.setSsl(ssl);
        return clientConfiguration;
    }


}
