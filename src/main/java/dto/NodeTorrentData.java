package dto;

import dto.commdata.NodeCommDataObject;
import lombok.Data;

@Data
public class NodeTorrentData extends NodeCommDataObject {
    private String torrentOid;
    private String torrentUrl;
    private String torrentStorageFolder;
    private String torrentName;
}
