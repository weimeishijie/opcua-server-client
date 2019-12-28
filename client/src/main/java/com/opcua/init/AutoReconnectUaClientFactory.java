package com.opcua.init;

import com.opcua.exception.OpcUaClientException;
import com.prosysopc.ua.client.UaClient;
import org.opcfoundation.ua.transport.security.SecurityMode;

import java.net.URISyntaxException;

/**
 * Created by mj on 2018/1/13.
 *
 */
public class AutoReconnectUaClientFactory implements OpcUaClientFactory {

    private String uaAddress;

    @Override
    public UaClient createUaClient() throws OpcUaClientException {
        UaClient uaClient;
        try {
            uaClient = new UaClient(getUaAddress());
            uaClient.setSecurityMode(SecurityMode.NONE);
            uaClient.setAutoReconnect(true);
        } catch (URISyntaxException e) {
            throw new OpcUaClientException("Error creating ua client: ", e);
        }
        return uaClient;
    }

    @Override
    public String getUaAddress() {
        return uaAddress;
    }

    public void setUaAddress(String uaAddress) {
        this.uaAddress = uaAddress;
    }
}
