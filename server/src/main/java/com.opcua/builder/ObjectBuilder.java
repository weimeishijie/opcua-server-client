package com.opcua.builder;

import com.prosysopc.ua.server.nodes.UaObjectNode;
import org.springframework.stereotype.Component;

/**
 * Created by mj on 2017/12/12.
 *
 * @author li wen ya
 */
public class ObjectBuilder {

//    private final ProtocolComponent component;
    private final FolderBuilder folder;

    private UaObjectNode uaObjectNode;

    private String idString;
    private String browseName;

    public ObjectBuilder(FolderBuilder folder) {
        this.folder = folder;
    }


    public FolderBuilder getFolder(){
        return folder;
    }

    public String getIdString(){
        return idString;
    }

    public void setIdString(String idString){
        this.idString = idString;
    }

    public String getBrowseName(){
        return browseName;
    }

    public ObjectBuilder setBrowseName(String browseName){
        this.browseName = browseName;
        return this;
    }

    public UaObjectNode create(){
        if(uaObjectNode != null){
            return uaObjectNode;
        }
        uaObjectNode = folder.getNodeManager().createObject(idString, browseName, folder.create());
        return uaObjectNode;
    }


}
