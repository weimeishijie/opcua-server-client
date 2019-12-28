package com.opcua.connection.listener.impl;

import com.opcua.connection.listener.AutomationLineRobotStatusListener;
import com.opcua.connection.listener.OpcUaClientConnectionListener;
import com.opcua.exception.OpcUaClientException;
import com.opcua.init.OpcUaClientTemplate;
import com.opcua.init.OpcUaSubscribeNodes;
import com.prosysopc.ua.client.MonitoredDataItemListener;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mj on 2018/1/13.
 *
 */
@Component
public class UaConnectionListener implements OpcUaClientConnectionListener {

    private static final Map<String, MonitoredDataItemListener> LISTENER_MAP = new ConcurrentHashMap<>();

    @Autowired
    private OpcUaSubscribeNodes opcUaSubscribeNodes;

    @Autowired
    private OpcUaClientTemplate opcUaClientTemplate;

    @Autowired
    private AutomationLineRobotStatusListener automationLineRobotStatusListener;

    @Override
    public void onConnected() {

        subscribeNodesValue(opcUaSubscribeNodes.getAutomationLineRobotStatusSubscribeNodes(), automationLineRobotStatusListener);

    }

    private synchronized void subscribeNodesValue(List<String> StrList, MonitoredDataItemListener listener) {
        try {
            for (String nodeIdStr : StrList) {
                if (LISTENER_MAP.containsKey(nodeIdStr + ":" + listener.getClass().toString())) {
                    return;
                }
                opcUaClientTemplate.subscribeNodeValue(new NodeId(2, nodeIdStr), listener);
                LISTENER_MAP.put(nodeIdStr + ":" + listener.getClass().toString(), listener);
            }
        } catch (OpcUaClientException e) {
            System.out.println("OpcUa Client Exception when subscribeNodesValue");
        }
    }
}
