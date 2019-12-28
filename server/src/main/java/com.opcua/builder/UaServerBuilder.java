package com.opcua.builder;

import com.opcua.server.OpcUaServer;
import com.opcua.server.UaNodeManager;
import com.prosysopc.ua.server.UaServer;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by mj on 2017/12/12.
 * @author li wen ya
 */
public class UaServerBuilder {

    private Logger logger = LoggerFactory.getLogger(UaServerBuilder.class);

    private Optional<OpcUaServer> uaServer = Optional.empty();
    private Optional<UaNodeManager> nodeManager = Optional.empty();

    private Optional<FolderBuilder> folderBuilder = Optional.empty();
    private Optional<ObjectBuilder> objectBuilder = Optional.empty();
    private Optional<VariableBuilder> variableBuilder = Optional.empty();


    /**
     * 创建UaServer服务对象
     * @return
     */
    public UaServerBuilder uaServer(){

        logger.info("---> UaServer initializing <---");

        uaServer = Optional.of(new OpcUaServer());
        uaServer.get().setServer(new UaServer());
        return this;
    }

    public void start(){
        uaServer.get().start();
    }

    /**
     * 设置服务端口号 默认为 52520
     * @param port 端口号
     * @return
     */
    public UaServerBuilder port(Integer port){
        logger.info("---> set UaServer port <---");
        if(port == null){
            port = 52520;
        }
        uaServer.get().setTcpPort(port);
        return this;
    }

    /**
     * 设置应用名
     * @param name
     * @return
     */
    public UaServerBuilder applicationName(String name){
        uaServer.get().setApplicationName(name);
        return this;
    }

    public Optional<UaNodeManager> getnodemag(){
        return nodeManager;
    }

    /**
     * @return
     */
    public UaServerBuilder folder(){
        if(!validate()){//判断uaServer不为空
            return this;
        }
        nodeManager = Optional.of(uaServer.get().createNodeManager());
        folderBuilder = Optional.of(new FolderBuilder(nodeManager.get()));
        return this;
    }

    /**
     * 创建节点对象
     * @param idString
     * @return
     */
    public UaServerBuilder nodeId(String idString){
        if(folderBuilder.isPresent() && folderBuilder.get().getIdString() == null){
            folderBuilder.get().setNodeId(idString);
        }
        if(objectBuilder.isPresent() && objectBuilder.get().getIdString() == null){
            objectBuilder.get().setIdString(idString);
        }
        if(variableBuilder.isPresent() && variableBuilder.get().getIdString() == null){
            variableBuilder.get().setIdString(idString);
        }
        return this;
    }

    /**
     * @param browseName
     * @return
     */
    public UaServerBuilder browseName(String browseName){
        if(folderBuilder.isPresent() && folderBuilder.get().getBrowseName() == null){
            folderBuilder.get().setBrowseName(browseName);
        }
        if(objectBuilder.isPresent() && objectBuilder.get().getBrowseName() == null){
            objectBuilder.get().setBrowseName(browseName);
        }
        if(variableBuilder.isPresent() && variableBuilder.get().getBrowseName() == null){
            variableBuilder.get().setBrowseName(browseName);
        }
        return this;
    }
    public void setUaVariableNode(String value){
            variableBuilder.get().setDefaultValue(value);
    }
    /**
     * 构建对象
     * @return
     */
    public UaServerBuilder object(){
        if(!validate()){
            return this;
        }
        if(!nodeManager.isPresent()){
            logger.error("nodeManager not initialize!");
            return this;
        }
        objectBuilder = Optional.of(new ObjectBuilder(folderBuilder.get()));
        return this;
    }

    /**
     * 构建变量
     * @return
     */
    public UaServerBuilder variable(){
        logger.info("---> set UaServer variable <---");
        if(!validate()){
            return this;
        }
        if(!nodeManager.isPresent()){
            logger.error("nodeManager not initialize!");
            return this;
        }
        variableBuilder = Optional.of(new VariableBuilder(objectBuilder.get()));
        return this;
    }

    /**
     * 设置变量类型
     * @param dataTypeId Identifiers.Int16
     * @return
     */
    public UaServerBuilder dataType(NodeId dataTypeId){
         if(variableBuilder.isPresent() && variableBuilder.get().getDataTypeId() == null){
             variableBuilder.get().setDataTypeId(dataTypeId);
         }
         return this;
    }

    public UaServerBuilder defaultValue(Object defaultValue){
        if(variableBuilder.isPresent() && variableBuilder.get().getDefaultValue() == null){
            variableBuilder.get().setDefaultValue(defaultValue);
        }
        variableBuilder.get().create();
        return this;
    }

    /**
     * 获取指定节点的值
     * @param idString 指定的节点
     * @return
     */
    public String getNodeIdValue(String idString){
        if(uaServer.isPresent() && nodeManager != null){
            return nodeManager.get().getNodeIdValue(idString);
        }
        return null;
    }

    /**
     * 设置指定节点的值
     * @param idString 指定的节点
     * @param number 要设置的指必须为数字
     */
    public void setNodeIdValue(String idString, String number){
        if(uaServer.isPresent() && nodeManager != null){
            nodeManager.get().setNodeIdValue(idString, number);
        }
    }



    /**
     * 判断是否创建了服务
     * @return
     */
    private boolean validate(){
        if(!uaServer.isPresent()){
            logger.error("server not initialize!");
            return false;
        }
        return true;
    }


}
