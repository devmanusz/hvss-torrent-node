package web;

import dto.NodeClientData;
import dto.NodeCommData;
import dto.commdata.NodeCommDataObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.NodeService;


@Controller
@RestController
@RequestMapping("node")
public class NodeController <T> {

     @Autowired
    NodeService nodeService;

    @PostMapping(value = "/command")
    public ResponseEntity<Object> addContentGroup(@RequestBody NodeCommData<T> nodeClientData) {
        return new ResponseEntity<>(nodeService.executeCommand(nodeClientData), HttpStatus.ACCEPTED);
    }

}
