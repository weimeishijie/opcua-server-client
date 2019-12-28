package com.opcua.server;

import com.prosysopc.ua.*;
import com.prosysopc.ua.server.UaServer;
import com.prosysopc.ua.server.UaServerException;
import com.prosysopc.ua.types.opcua.server.BuildInfoTypeNode;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.UserTokenPolicy;
import org.opcfoundation.ua.transport.security.HttpsSecurityPolicy;
import org.opcfoundation.ua.transport.security.SecurityMode;
import org.opcfoundation.ua.utils.EndpointUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by mj on 2017/12/12.
 *
 * @author li wen ya
 */
public class OpcUaServer {

    private static Logger logger = LoggerFactory.getLogger(OpcUaServer.class);

    private UaServer server;
    private int tcpPort;
    private int httpsPort = 52443;
    private String applicationName;
    private UaNodeManager nodeManager;

    public OpcUaServer() {}

    /**
     * 创建 UaNodeManager
     * @return
     */
    public UaNodeManager createNodeManager(){
        if(nodeManager != null){
            return nodeManager;
        }
        try {
            init();
            nodeManager = new UaNodeManager(server, UaNodeManager.NAMESPACE);
        } catch (UaServerException | IOException | SecureIdentityException e) {
            e.printStackTrace();
        } catch (StatusException e) {
            e.printStackTrace();
        }
        return nodeManager;
    }


    public void init() throws IOException, SecureIdentityException, UaServerException {

        final PkiFileBasedCertificateValidator validator = new PkiFileBasedCertificateValidator();
        server.setCertificateValidator(validator);
        validator.setValidationListener(new UaCertificateValidationListener());

        // *** Application Description is sent to the clients
        ApplicationDescription appDescription = new ApplicationDescription();
        appDescription.setApplicationName(new LocalizedText(this.getApplicationName(), Locale.ENGLISH));
        appDescription.setApplicationUri("urn:localhost:OPCUA:" + applicationName);
        appDescription.setProductUri("urn:prosysopc.com:OPCUA:" + applicationName);
        appDescription.setApplicationType(ApplicationType.Server);

        // *** Server Endpoints
        server.setPort(UaApplication.Protocol.OpcTcp, tcpPort);
        server.setPort(UaApplication.Protocol.Https, httpsPort);

        // optional server name part of the URI (default for all protocols)
        server.setServerName("OPCUA/" + this.getApplicationName());

        server.setBindAddresses(EndpointUtil.getInetAddresses(server.isEnableIPv6()));

        // *** Certificates
        File privatePath = new File(validator.getBaseDir(), "private");
        logger.info("privatePath exists: {}" + privatePath.exists());

        // KeyPair issuerCertificate = ApplicationIdentity.loadOrCreateIssuerCertificate("ProsysSampleCA",
        //   privatePath, "opcua", 3650, false);


        int[] keySizes = null;


        // *** Application Identity

        final ApplicationIdentity identity = ApplicationIdentity.loadOrCreateCertificate(appDescription,
                "Sample Organisation", "opcua", privatePath, null, keySizes, true);

        // Create the HTTPS certificate bound to the hostname.
//    String hostName = ApplicationIdentity.getActualHostName();
//    identity.setHttpsCertificate(ApplicationIdentity.loadOrCreateHttpsCertificate(appDescription,
//        hostName, "opcua", issuerCertificate, privatePath, true));

        server.setApplicationIdentity(identity);

        // *** Security settings
        server.setSecurityModes(SecurityMode.ALL);
        server.getHttpsSettings().setHttpsSecurityPolicies(HttpsSecurityPolicy.ALL);


        server.getHttpsSettings().setCertificateValidator(validator);


        server.addUserTokenPolicy(UserTokenPolicy.ANONYMOUS);
        server.addUserTokenPolicy(UserTokenPolicy.SECURE_USERNAME_PASSWORD);
        server.addUserTokenPolicy(UserTokenPolicy.SECURE_CERTIFICATE);
        server.setUserValidator(new UaUserValidator());


        // *** init() creates the service handlers and the default endpoints according to the above settings
        server.init();

        initBuildInfo();

        // "Safety limits" for ill-behaving clients
        server.getSessionManager().setMaxSessionCount(500);
        server.getSessionManager().setMaxSessionTimeout(3600000); // one hour
        server.getSubscriptionManager().setMaxSubscriptionCount(50);

    }


    protected void initBuildInfo() {
        final BuildInfoTypeNode buildInfo = server.getNodeManagerRoot().getServerData().getServerStatusNode().getBuildInfoNode();
        buildInfo.setProductName(applicationName);
    }


    /**
     * start server
     */
    public void start(){
        try {
            server.start();
            System.out.println("完成开启");
        } catch (UaServerException e) {
            e.printStackTrace();
        }
    }

    /**
     * shutdown server
     */
    public void stop(){
        server.shutdown(1000, "OpcuaServer will be shutdown in one sencond!");
    }

    // static utility tools
    public static void println(String str) {
        logger.info(str);
    }


    public String getApplicationName(){
        return applicationName;
    }

    public OpcUaServer setServer(UaServer server){
        this.server = server;
        return this;
    }

    public OpcUaServer setTcpPort(Integer tcpPort){
        this.tcpPort = tcpPort;
        return this;
    }

    public OpcUaServer setApplicationName(String applicationName){
        this.applicationName = applicationName;
        return this;
    }


}
