package com.opcua.init;

import com.opcua.config.OpcUaProperties;
import com.opcua.connection.listener.OpcUaClientConnectionListener;
import com.opcua.exception.OpcUaClientException;
import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.client.MonitoredDataItem;
import com.prosysopc.ua.client.MonitoredDataItemListener;
import com.prosysopc.ua.client.Subscription;
import com.prosysopc.ua.client.UaClient;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.MonitoringMode;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by mj on 2018/1/13.
 *
 */
public class OpcUaClientTemplate {

    private UaClient uaClient;

    private RetryTemplate retryTemplate;

    private List<OpcUaClientConnectionListener> connectionListeners = new ArrayList<>();

    private OpcUaProperties properties;

    public OpcUaClientTemplate(AutoReconnectUaClientFactory autoReconnectUaClientFactory, OpcUaProperties properties) throws OpcUaClientException {

        uaClient = autoReconnectUaClientFactory.createUaClient();

        retryTemplate = new RetryTemplate();
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(properties.getRetry().getMaxAttempts());
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(properties.getRetry().getBackOffPeriod());
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
//        retryTemplate.setRetryPolicy(new AlwaysRetryPolicy());
        retryTemplate.setRetryPolicy(simpleRetryPolicy);

        this.properties = properties;

    }

    public synchronized boolean connect() throws OpcUaClientException {
        if (!uaClient.isConnected()) {
            try {
                uaClient.connect();
                fireConnectionListeners();
                return true;
            } catch (Exception e) {
                throw new OpcUaClientException("Error connecting ua server: ", e);
            }
        } else {
            return true;
        }
    }

    private void fireConnectionListeners() {
        this.connectionListeners.stream().forEach(OpcUaClientConnectionListener::onConnected);
    }

    public boolean subscribeNodeValue(NodeId id, MonitoredDataItemListener dataChangeListener)
            throws OpcUaClientException {
        return doSubscribeNodeValue(id, dataChangeListener);
    }

    private synchronized boolean doSubscribeNodeValue(NodeId id, MonitoredDataItemListener dataChangeListener)
            throws OpcUaClientException {
        try {
            Subscription subscription = new Subscription();
            subscription.setPublishingInterval(properties.getPublishingRate(), TimeUnit.MILLISECONDS);
            MonitoredDataItem item = new MonitoredDataItem(id, Attributes.Value,
                    MonitoringMode.Reporting, subscription.getPublishingInterval());
            item.setDataChangeListener(dataChangeListener);
            subscription.addItem(item);
            uaClient.addSubscription(subscription);
            return true;
        } catch (ServiceException | StatusException e) {
            throw new OpcUaClientException("Error subscribing node " + id + ", value: ", e);
        }
    }

    public void addConnectionListener(OpcUaClientConnectionListener connectionListener) {
        this.connectionListeners.add(connectionListener);
    }

    public void connectAlwaysInBackend() {
        try {
            this.retryTemplate.execute(context -> connect());
        } catch (OpcUaClientException e) {
            e.printStackTrace();
        }
    }



}
