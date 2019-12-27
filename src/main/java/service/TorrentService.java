package service;

import config.ClientConfiguration;
import config.ConfigurationReader;
import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;
import dto.NodeTorrentData;
import dto.NodeTorrentInfoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TorrentService {

    @Autowired
    ClientConfiguration clientConfiguration;

    //String - torrentId,  Client - client
    Map<String, Client> torrentClients = new HashMap<>();

    //getTorrentStorageFolder is an uuid from the server and stored in the database as well
    public Client addNewTorrent(NodeTorrentData nodeTorrentData) {
        String savePath = clientConfiguration.getTorrentDownloadBasePath() + "/" + nodeTorrentData.getTorrentStorageFolder();
        try (BufferedInputStream in = new BufferedInputStream(new URL(nodeTorrentData.getTorrentUrl()).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(clientConfiguration.getTorrentDownloadFileSavePath() + "/" + nodeTorrentData.getTorrentName())) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if(!new File(savePath).exists()){
                new File(savePath).mkdirs();
            }
            Client client = new Client(
                    InetAddress.getLocalHost(),
                    SharedTorrent.fromFile(
                            new File(clientConfiguration.getTorrentDownloadFileSavePath() + "/" + nodeTorrentData.getTorrentName()),
                            new File(savePath)));

            //precheck storage size and move files if needed
            long storageFreeSpace = new File(clientConfiguration.getTorrentDownloadBasePath()+"/").getFreeSpace();


            client.getTorrent().getSize();
            client.setMaxDownloadRate(8000.0);
            client.setMaxUploadRate(8000.0);
            client.download();
            torrentClients.put(nodeTorrentData.getTorrentOid(), client);
            return client;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void downloadStartTorrentFile(Long id) {
        //TODO Delete torrent from bittorrent client
        //TODO Delete torrent from file sys

    }

    public void downloadStopTorrentFile(Long id) {
        //TODO Delete torrent from bittorrent client
        //TODO Delete torrent from file sys

    }

    public void downloadStart() {
        //TODO Delete torrent from bittorrent client
        //TODO Delete torrent from file sys

    }

    public void downloadStop() {
        //TODO Delete torrent from bittorrent client
        //TODO Delete torrent from file sys

    }

    public void deleteTorrent(String torrentId){
        torrentClients.get(torrentId).stop();
        torrentClients.remove(torrentId);
    }

    public NodeTorrentInfoData createTorrentInfoData(String torrentId, Client client){
        NodeTorrentInfoData torrentInfoData = new NodeTorrentInfoData();

        torrentInfoData.setClientState(client.getState());
        torrentInfoData.setCompletedPieces(client.getTorrent().getCompletedPieces().cardinality());
        torrentInfoData.setTotalPieces(client.getTorrent().getPieceCount());
        torrentInfoData.setCompletionPercent(String.format("%.2f", client.getTorrent().getCompletion()));
        torrentInfoData.setTorrentId(torrentId);
        return torrentInfoData;
    }

    public Client getTorrentClientByOid(String oid){
        return torrentClients.get(oid);
    }

    public List<NodeTorrentInfoData> getAllTorrentInfoData(){
        List<NodeTorrentInfoData> returnList = new ArrayList<>();
        torrentClients.entrySet().stream().forEach((e) -> {
            returnList.add(createTorrentInfoData(e.getKey(), e.getValue()));
        });
        return returnList;
    }

    public int getRunningTorrentCount(){
        return (int) torrentClients.entrySet().stream().filter(e -> e.getValue().getState() != Client.ClientState.DONE).count();

    }

    public int getTotalTorrentCount(){
        return torrentClients.size();
    }
}
