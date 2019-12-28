package com.opcua.connection;

import com.opcua.connection.listener.impl.UaConnectionListener;
import com.opcua.init.OpcUaClientTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by mj on 2018/1/13.
 *
 */
@Service
public class OpcUaService {


    @Autowired
    private OpcUaClientTemplate opcUaClientTemplate;

    @Autowired
    private UaConnectionListener uaConnectionListener;

    @PostConstruct
    public void opcUaClientConnect() {
        opcUaClientTemplate.addConnectionListener(uaConnectionListener);
        opcUaClientTemplate.connectAlwaysInBackend();

    }

}
