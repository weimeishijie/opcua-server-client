package com.opcua.builder;

import com.opcua.server.UaNodeManager;
import com.prosysopc.ua.types.opcua.server.FolderTypeNode;
import org.springframework.stereotype.Component;

/**
 * Created by mj on 2017/12/12.
 *
 * @author li wen ya
 */
public class FolderBuilder {

    private final UaNodeManager nodeManager;
    private FolderTypeNode folderTypeNode;

    private String idString;
    private String browseName;


    public UaNodeManager getNodeManager(){
        return nodeManager;
    }

    public FolderBuilder(UaNodeManager nodeManager) {
        this.nodeManager = nodeManager;
    }



    public String getIdString(){
        return idString;
    }

    public FolderBuilder setNodeId(String idString){
        this.idString = idString;
        return this;
    }

    public String getBrowseName(){
        return browseName;
    }

    public FolderBuilder setBrowseName(String browseName){
        this.browseName = browseName;
        return this;
    }

    public FolderTypeNode create(){
        if(folderTypeNode != null){
            return folderTypeNode;
        }
        folderTypeNode = nodeManager.createFolder(idString, browseName);
        return folderTypeNode;
    }



}
