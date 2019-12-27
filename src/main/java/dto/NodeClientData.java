package dto;

import dto.commdata.NodeCommDataObject;
import lombok.Data;

@Data
public class NodeClientData extends NodeCommDataObject {

    boolean torrentSlave;
    boolean encodeSlave;

}
