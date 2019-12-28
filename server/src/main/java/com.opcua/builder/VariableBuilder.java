package com.opcua.builder;

import com.prosysopc.ua.server.nodes.UaVariableNode;
import org.opcfoundation.ua.builtintypes.NodeId;

/**
 * Created by mj on 2017/12/12.
 *
 * @author li wen ya
 */
public class VariableBuilder {

    private final ObjectBuilder object;

    private UaVariableNode uaVariableNode;

    private String idString;
    private String browseName;
    private NodeId dataTypeId;
    private Object defaultValue;


    public VariableBuilder(ObjectBuilder object) {
        this.object = object;
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

    public VariableBuilder setBrowseName(String browseName){
        this.browseName = browseName;
        return this;
    }

    public NodeId getDataTypeId(){
        return dataTypeId;
    }

    public VariableBuilder setDataTypeId(NodeId dataTypeId){
        this.dataTypeId = dataTypeId;
        return this;
    }

    public Object getDefaultValue(){
        return defaultValue;
    }

    public VariableBuilder setDefaultValue(Object defaultValue){
        this.defaultValue = defaultValue;
        return this;
    }

    public UaVariableNode create(){
        if(uaVariableNode != null){
            return uaVariableNode;
        }
        uaVariableNode = object.getFolder().getNodeManager().createVariable(idString, browseName, dataTypeId, defaultValue, object.create());
        return uaVariableNode;
    }



}
