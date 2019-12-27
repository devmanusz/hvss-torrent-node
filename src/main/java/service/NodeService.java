package service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ClientConfiguration;
import dto.*;
import dto.commdata.NodeCommDataObject;
import dto.commdata.NodeCommandDataTypeEnum;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.attribute.AclFileAttributeView;
import java.util.Arrays;
import java.util.logging.Logger;

@Component
public class NodeService <T>  {

    @Autowired
    ClientConfiguration clientConfiguration;

    @Autowired
    TorrentService torrentService;

    @Autowired
    ClientInfoService clientInfoService;

    private final static Logger LOGGER = Logger.getLogger(NodeService.class.getName());
    static ObjectMapper mapper = new ObjectMapper();

    public void registerNodeOnServer() throws UnknownHostException {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        NodeCommData<NodeClientData> response = null;
        NodeClientData data = null;

        NodeCommData<NodeClientRegisterData> nodeCommData = new NodeCommData<>();
        nodeCommData.setCommandData(NodeCommandDataTypeEnum.CLIENT_REGISTER);
        NodeClientRegisterData registerData = new NodeClientRegisterData();
        registerData.setIp(clientConfiguration.getHostAddress());
        registerData.setPort(clientConfiguration.getHostPort());
        registerData.setSslEnabled(clientConfiguration.getSsl());
        nodeCommData.setData(Arrays.asList(registerData));
        //Type cast for container keeping generic which will be a Linked list
        response = (NodeCommData<NodeClientData>) sendNodeServiceMessages((NodeCommData<T>) nodeCommData, "/node/register");
        //Map linked list to become <object extends NodeCommDataObject>
        data = mapper.convertValue(response.getData().get(0), NodeClientData.class);
        if(response != null) {
            clientConfiguration.setClientRegistered(true);
            clientConfiguration.setClientOid(response.getClientOid());
            clientConfiguration.setEncodeSlave(data.isEncodeSlave());
            clientConfiguration.setTorrentSlave(data.isTorrentSlave());
            if (clientConfiguration.isEncodeSlave()) LOGGER.info("CLIENT become ENCODE-SLAVE");
            if (clientConfiguration.isTorrentSlave()) LOGGER.info("CLIENT become TORRENT-SLAVE");
        }
    }

    public NodeCommData<T> executeCommand(NodeCommData<T> nodeCommData) {
        if(nodeCommData.getCommandData() == NodeCommandDataTypeEnum.CLIENT_INFO){
            NodeCommData<NodeClientInfoData> clientInfoData = new NodeCommData<>();
            clientInfoData.setCommandData( NodeCommandDataTypeEnum.CLIENT_INFO);
            clientInfoData.setData(Arrays.asList(clientInfoService.getClientInfo()));
            clientInfoData.getData().get(0).setTorrentsTotal(torrentService.getTotalTorrentCount());
            clientInfoData.getData().get(0).setTorrentsRunning(torrentService.getRunningTorrentCount());
            return (NodeCommData<T>) clientInfoData;
        }

        if (clientConfiguration.isTorrentSlave()) {
            NodeTorrentData data = mapper.convertValue(nodeCommData.getData().get(0), NodeTorrentData.class);
            NodeCommData<NodeTorrentInfoData> nodeCommDataObject = new NodeCommData<>();
            if (nodeCommData.getCommandData() == NodeCommandDataTypeEnum.TORRENT_NEW) {
                nodeCommDataObject.setCommandData(NodeCommandDataTypeEnum.TORRENT_NEW);
                nodeCommDataObject.setClientOid(clientConfiguration.getClientOid());
                torrentService.addNewTorrent(data);
                nodeCommDataObject.setData(Arrays.asList(new NodeTorrentInfoData(data.getTorrentOid())));
                //nodeCommDataObject.setData(Arrays.asList(torrentService.createTorrentInfoData(data.getTorrentOid(), ));
                return (NodeCommData<T>) nodeCommDataObject;

            } else if (nodeCommData.getCommandData() == NodeCommandDataTypeEnum.TORRENT_INFO) {
                nodeCommDataObject.setCommandData(NodeCommandDataTypeEnum.TORRENT_INFO);
                nodeCommDataObject.setClientOid(clientConfiguration.getClientOid());
                nodeCommDataObject.setData(Arrays.asList(torrentService.createTorrentInfoData(data.getTorrentOid(), torrentService.getTorrentClientByOid(data.getTorrentOid()))));
                return (NodeCommData<T>) nodeCommDataObject;
            } else if (nodeCommData.getCommandData() == NodeCommandDataTypeEnum.TORRENT_INFO_ALL) {
                nodeCommDataObject.setCommandData(NodeCommandDataTypeEnum.TORRENT_INFO_ALL);
                nodeCommDataObject.setClientOid(clientConfiguration.getClientOid());
                nodeCommDataObject.setData(Arrays.asList(torrentService.createTorrentInfoData(data.getTorrentOid(), torrentService.getTorrentClientByOid(data.getTorrentOid()))));
                return (NodeCommData<T>) nodeCommDataObject;
            } else if (nodeCommData.getCommandData() == NodeCommandDataTypeEnum.TORRENT_DELETE) {
                nodeCommDataObject.setCommandData(NodeCommandDataTypeEnum.TORRENT_DELETE);
                nodeCommDataObject.setClientOid(clientConfiguration.getClientOid());
                torrentService.deleteTorrent(data.getTorrentOid());
                return (NodeCommData<T>) nodeCommDataObject;
            }
        }

        if(clientConfiguration.isEncodeSlave()){

        }
        return new NodeCommData<T>();
    }


    private  NodeCommData<T> sendNodeServiceMessages(NodeCommData<T> nodeCommData, String endpoint) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String remoteAddress = ( clientConfiguration.getSsl() ? "https://" : "http://" ) + clientConfiguration.getMasterAddress() + ":" + clientConfiguration.getMasterPort() + endpoint;
        HttpEntity<String> entity = null;
        try {
            entity = new HttpEntity<String>(mapper.writeValueAsString(nodeCommData), headers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return (NodeCommData<T>) restTemplate.exchange(remoteAddress, HttpMethod.POST, entity, new ParameterizedTypeReference<NodeCommData<?>>() {}).getBody();
    }

    @PostConstruct
    public void init() throws UnknownHostException {
        clientInfoService.getClientInfo();
        registerNodeOnServer();
    }
}
