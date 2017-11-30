package com.samapartners.workshop.client;

import org.hyperledger.fabric.sdk.EventHub;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class FabricManager {

    private static final String HOST = "192.168.99.100"; //TODO: set url

    private final HFClient hfClient;
    private List<Peer> peers;
    private List<Orderer> orderers;
    private List<EventHub> eventHubs;

    public FabricManager() {
        hfClient = HFClient.createNewInstance();
        initEventHubs();
        initOrderers();
        initPeers();
    }

    public List<EventHub> initEventHubs() {

        List<EventHub> eventHubs = new ArrayList<>();
        String evenHubUrl = "grpc://" + HOST + ":7053";
        String eventHubName = "peer0.eventhub.org1.example.com";
        Properties properties = new Properties();
        properties.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[]{5L, TimeUnit.MINUTES});
        properties.put("grpc.NettyChannelBuilderOption.keepAliveTimeout", new Object[]{8L, TimeUnit.SECONDS});

        try {
            eventHubs.add(hfClient.newEventHub(eventHubName, evenHubUrl, properties));
        } catch (InvalidArgumentException ex) {
            throw new IllegalArgumentException(ex);
        }

        return eventHubs;
    }

    public List<Peer> initPeers() {

        Properties properties = new Properties();
        String peerUrl = "grpc://" + HOST + ":7051";
        String peerName = "peer0.org1.example.com";

        List<Peer> peers = new ArrayList<>();
        properties.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[]{5L, TimeUnit.MINUTES});
        properties.put("grpc.NettyChannelBuilderOption.keepAliveTimeout", new Object[]{8L, TimeUnit.SECONDS});

        try {
            peers.add(hfClient.newPeer(peerName, peerUrl, properties));
        } catch (InvalidArgumentException ex) {
            throw new IllegalArgumentException(ex);
        }
        return peers;
    }

    public List<Orderer> initOrderers() {

        String ordererUrl = "grpc://" + HOST + ":7050";

        List<Orderer> orderers = new ArrayList<>();
        Properties ordererProperties = new Properties();
        ordererProperties.setProperty("trustServerCertificate", "true"); //testing environment only NOT FOR PRODUCTION!
        ordererProperties.setProperty("hostnameOverride", "orderer.example.com");
        ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[]{5L, TimeUnit.MINUTES});
        ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTimeout", new Object[]{8L, TimeUnit.SECONDS});
        String ordererName = "orderer.example.com";

        try {
            orderers.add(hfClient.newOrderer(ordererName, ordererUrl, ordererProperties));
        } catch (InvalidArgumentException ex) {
            throw new IllegalArgumentException(ex);
        }

        return orderers;
    }

}
