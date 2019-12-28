package com.opcua.init;

import com.opcua.exception.OpcUaClientException;
import com.prosysopc.ua.client.UaClient;

/**
 * Created by mj on 2018/1/13.
 *
 */
public interface OpcUaClientFactory {

    UaClient createUaClient() throws OpcUaClientException;

    String getUaAddress();

}
