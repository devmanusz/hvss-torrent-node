package dto;

import com.turn.ttorrent.client.Client;
import dto.commdata.NodeCommDataObject;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NodeTorrentInfoData extends NodeCommDataObject {

    public NodeTorrentInfoData(String torrentId){
        this.torrentId = torrentId;
    }

    String torrentId;
    Client.ClientState clientState;
    int completedPieces;
    int totalPieces;
    String completionPercent;


}
