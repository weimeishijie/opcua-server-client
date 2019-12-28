package com.opcua.connection.listener;

import com.prosysopc.ua.client.MonitoredDataItem;
import com.prosysopc.ua.client.MonitoredDataItemListener;
import lombok.extern.slf4j.Slf4j;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.springframework.stereotype.Component;

/**
 * Created by mj on 2018/1/13.
 * 监听订阅到的数据，业务逻辑在此处处理
 */
@Slf4j
@Component
public class AutomationLineRobotStatusListener implements MonitoredDataItemListener {

    @Override
    public void onDataChange(MonitoredDataItem item, DataValue oldValue, DataValue nowValue) {
        if(null != oldValue && null != nowValue){
            log.info("监听的节点: {}, 监听未变之前的值：{} 监听修改的值为：{}", item.getNodeId().getValue(), oldValue.getValue(), nowValue.getValue());
        }
    }
}
