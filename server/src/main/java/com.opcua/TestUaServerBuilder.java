package com.opcua;

import com.opcua.builder.UaServerBuilder;
import org.opcfoundation.ua.core.Identifiers;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by mj on 2017/12/12.
 *
 * @author li wen ya
 *
 */
public class TestUaServerBuilder extends UaServerBuilder {

    UaServerBuilder uaServer;

    public TestUaServerBuilder(){}

    @PostConstruct
    public void initialization(){
        uaServer = uaServer();

        uaServer.port(52520).applicationName("BosonOPCUAServer")
                .folder().nodeId("MyObjects").browseName("MyObjects")
                .object().nodeId("MyObjects.Robot").browseName("Robot");
        String[] vehicles = getAllVehicleNumber();
        for(int i = 0; i < vehicles.length; i++){
            uaServer.variable().nodeId(vehicles[i].trim()).browseName(vehicles[i].trim()).dataType(Identifiers.String).defaultValue("0");
        }

        uaServer.start();
    }

    private String[] getAllVehicleNumber(){
        Properties prop = new Properties();
        try (InputStream inputStream = this.getClass().getResourceAsStream("/vehicle.properties")){
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String vehicles = prop.getProperty("vehicleNumber");
        return vehicles.trim().split(",");
    }


//     测试写入节点值
    public void write(){
        uaServer.setNodeIdValue("agv/131/msg", "15");
        uaServer.setNodeIdValue("agv/132/msg", "20");
        uaServer.setNodeIdValue("agv/133/msg", "25");
    }

    // 测试读取节点
    public void read(){
        System.out.println(uaServer.getNodeIdValue("agv/131/msg"));
        System.out.println(uaServer.getNodeIdValue("agv/132/msg"));
        System.out.println(uaServer.getNodeIdValue("agv/133/msg"));
    }


    // 测试方法
    public static void main(String[] args) {
//        PropertyConfigurator.configureAndWatch(TestUaServerBuilder.class
//                .getResource("log.properties").getFile(), 5000);
        TestUaServerBuilder testUaServerBuilder = new TestUaServerBuilder();
        testUaServerBuilder.initialization();
        testUaServerBuilder.start();
//        testUaServerBuilder.write();
//        testUaServerBuilder.read();
    }



}
