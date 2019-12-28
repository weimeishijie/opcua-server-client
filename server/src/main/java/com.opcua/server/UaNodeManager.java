package com.opcua.server;

import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.nodes.UaObject;
import com.prosysopc.ua.nodes.UaType;
import com.prosysopc.ua.server.NodeManagerUaNode;
import com.prosysopc.ua.server.UaServer;
import com.prosysopc.ua.server.nodes.CacheVariable;
import com.prosysopc.ua.server.nodes.UaObjectNode;
import com.prosysopc.ua.server.nodes.UaVariableNode;
import com.prosysopc.ua.types.opcua.server.FolderTypeNode;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Identifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Created by mj on 2017/12/12.
 *
 * @author li wen ya
 */
public class UaNodeManager extends NodeManagerUaNode {

    private static Logger logger = LoggerFactory.getLogger(UaNodeManager.class);

    public static final String NAMESPACE = "http://www.prosysopc.com/OPCUA/SampleAddressSpace";

    /* UaFolder and UaType where we will use */
    private final int ns;
    private final UaObject objectsFolder;
    private final UaType baseObjectType;
    private final UaType baseDataVariableType;
    private UaVariableNode myVariable;
    private static Map<NodeId, UaVariableNode> map = new HashMap<NodeId, UaVariableNode>();

    public UaNodeManager(UaServer server, String namespaceUri ) throws StatusException {
        super(server, namespaceUri);
        this.ns = getNamespaceIndex();
        this.objectsFolder = server.getNodeManagerRoot().getObjectsFolder();
        this.baseObjectType = server.getNodeManagerRoot().getType(Identifiers.BaseObjectType);
        this.baseDataVariableType = server.getNodeManagerRoot().getType(Identifiers.BaseDataVariableType);
    }


    /**
     * 创建文件夹对象
     *
     * @param idString
     * @param browseName
     * @return
     */
    public FolderTypeNode createFolder(String idString, String browseName){
        final NodeId myObjectsFolderId = new NodeId(ns, idString);
        FolderTypeNode myObjectsFolder = createInstance(FolderTypeNode.class, browseName, myObjectsFolderId);
        try {
            this.addNodeAndReference(objectsFolder, myObjectsFolder, Identifiers.Organizes);
        } catch (StatusException e) {
            e.printStackTrace();
        }
        return myObjectsFolder;
    }

    /**
     * 在某个节点下创建对象
     *
     * @param idString
     * @param browseName
     * @param folder
     * @return
     */
    public UaObjectNode createObject(String idString, String browseName, FolderTypeNode folder) {
        final NodeId myObjectId = new NodeId(ns, idString);
        UaObjectNode myObject = new UaObjectNode(this, myObjectId, browseName, Locale.ENGLISH);
        folder.addReference(myObject, Identifiers.HasComponent, false);
        return myObject;
    }

    /**
     * 为某个对象创建变量
     *
     * @param idString
     * @param browseName
     * @param datatypeId
     * @param defaultValue
     * @param object
     * @return
     */
    public UaVariableNode createVariable(String idString, String browseName,
                                         NodeId datatypeId, Object defaultValue, UaObjectNode object){
        final NodeId myVariableId = new NodeId(ns, idString);
        myVariable = new CacheVariable(this, myVariableId, browseName, LocalizedText.NO_LOCALE);
        myVariable.setDataTypeId(datatypeId);
        try {
            myVariable.setValue(defaultValue);
        } catch (StatusException e) {
            e.printStackTrace();
        }

        object.addComponent(myVariable);
        map.put(myVariableId, myVariable);
        return myVariable;
    }

    public void setNodeIdValue(String idString, Object number){
        NodeId nodeId = new NodeId(ns, idString);
        setNodeValue(nodeId, number);
    }

    private void setNodeValue(NodeId id, Object number){
        if(!map.isEmpty() && map.containsKey(id)){
            try {
                map.get(id).setValue(number);
            } catch (StatusException e) {
                e.printStackTrace();
            }
        }

    }

    public String getNodeIdValue(String idString){
        NodeId nodeId = new NodeId(ns, idString);
        return getNodeValue(nodeId);
    }

    private String getNodeValue(NodeId id){
        if(!map.isEmpty() && map.containsKey(id)){
            return map.get(id).getValue().getValue().toString();
        }
        return null;
    }




}


















