package com.opcua.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by mj on 2018/1/13.
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "opcua")
public class OpcUaProperties {

    private String address;

    private Retry retry;

    private long publishingRate;

    private List<String> automationLineRobotStatusSubscribeNodes;

    public static class Retry{

        private int maxAttempts = 3;
        private long backOffPeriod = 1000L;

        public int getMaxAttempts() {
            return maxAttempts;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        public long getBackOffPeriod() {
            return backOffPeriod;
        }

        public void setBackOffPeriod(long backOffPeriod) {
            this.backOffPeriod = backOffPeriod;
        }

    }

}
