package com.opcua.config;

import com.opcua.exception.OpcUaClientException;
import com.opcua.init.AutoReconnectUaClientFactory;
import com.opcua.init.OpcUaClientTemplate;
import com.opcua.init.OpcUaSubscribeNodes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mj on 2018/1/13.
 *
 */
@Configuration
public class OpcUaConfiguration {

    @Bean
    AutoReconnectUaClientFactory opcUaClientFactory(OpcUaProperties opcUaProperties){
        AutoReconnectUaClientFactory autoReconnectUaClientFactory = new AutoReconnectUaClientFactory();
        autoReconnectUaClientFactory.setUaAddress(opcUaProperties.getAddress());
        return autoReconnectUaClientFactory;
    }

    @Bean
    OpcUaClientTemplate opcUaClientTemplate(
            AutoReconnectUaClientFactory autoReconnectUaClientFactory,
            OpcUaProperties opcUaProperties)
            throws OpcUaClientException {
        return new OpcUaClientTemplate(autoReconnectUaClientFactory, opcUaProperties);
    }

    @Bean
    OpcUaSubscribeNodes opcUaSubscribeNodes(OpcUaProperties opcUaProperties) throws OpcUaClientException {
        return new OpcUaSubscribeNodes(opcUaProperties);
    }


}
