package dto;

import dto.commdata.NodeCommDataObject;
import lombok.Data;

@Data
public class NodeClientRegisterData extends NodeCommDataObject {
    String ip;
    int port;
    Boolean sslEnabled;
}
