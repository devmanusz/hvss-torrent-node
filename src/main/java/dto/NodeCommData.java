package dto;

import dto.commdata.NodeCommandDataTypeEnum;
import lombok.Data;

import java.util.List;

@Data
public class NodeCommData<T> {

    private NodeCommandDataTypeEnum commandData;
    private String clientOid;
    private List<T> data;
}
