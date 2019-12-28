package com.opcua.init;

import com.opcua.config.OpcUaProperties;
import lombok.Data;

import java.util.List;

/**
 * Created by mj on 2018/1/13.
 *
 */
@Data
public class OpcUaSubscribeNodes {

    private List<String> automationLineRobotStatusSubscribeNodes;// 保存单个节点

    public OpcUaSubscribeNodes(OpcUaProperties properties) {

        this.automationLineRobotStatusSubscribeNodes = getRealSubscribeNodesList(properties.getAutomationLineRobotStatusSubscribeNodes());

    }

    private List<String> getRealSubscribeNodesList(List<String> opcUaPropertiesList) {
        if (opcUaPropertiesList != null && opcUaPropertiesList.size() != 0 && opcUaPropertiesList.get(0).contains("NodeBase")) {
            String[] machineNoList = new String[0];
            String[] list = opcUaPropertiesList.get(0).split("-");
            String nodeBase = list[0].trim().substring(list[0].trim().indexOf("#")+1).trim();
            if (opcUaPropertiesList.get(0).contains("No#")) {
                machineNoList = list[1].trim().substring(list[1].trim().indexOf("#") + 1).split(",");
            }
            String[] subVarList = list[list.length-1].trim().substring(list[list.length-1].trim().indexOf("#") + 1).split(",");
            opcUaPropertiesList.remove(0);
            if( !(machineNoList.length == 0) ) {
                for(String machineNo : machineNoList) {
                    for(String subvar : subVarList) {
                        opcUaPropertiesList.add(nodeBase + machineNo.trim() + "." + subvar.trim());
                    }
                }
            } else {
                for(String subvar : subVarList) {
                    opcUaPropertiesList.add(nodeBase + "." + subvar.trim());
                }
            }
        }
        return opcUaPropertiesList;
    }
}
