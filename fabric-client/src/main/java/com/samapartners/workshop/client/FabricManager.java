package com.samapartners.workshop.client;

import com.samapartners.workshop.sample.SampleStore;
import com.samapartners.workshop.sample.SampleUser;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class FabricManager {

    private static final String HOST = "192.168.99.100";
    private static final String CHANNEL_PATH = "/Users/albertlacambra1/Downloads/scripts/channel-artifacts/";

    private final HFClient hfClient;
    private List<Peer> peers;
    private List<Orderer> orderers;
    private List<EventHub> eventHubs;
    private Channel channel;

    public FabricManager() {
        hfClient = HFClient.createNewInstance();
        initHFClient();
    }

    private void initHFClient() {
        try {
            hfClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            hfClient.setUserContext(getUserConext());
        } catch (CryptoException | InvalidArgumentException e) {
            e.printStackTrace();
        }

        eventHubs = initEventHubs();
        orderers = initOrderers();
        peers = initPeers();
    }

    private SampleUser getUserConext() {

        String organizationName = "org1";
        String organizationMspId = "Org1MSP";
        String username = "Admin";

        SampleUser sampleUser = new SampleUser(username, organizationName, SampleStore.load());
        sampleUser.setMPSID(organizationMspId);
        Enrollment enrollment = new WorkshopEnrollment();
        sampleUser.setEnrollment(enrollment);
        return sampleUser;
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

    public void createChannel() {
        String channelName = "mychannel";
        String path = CHANNEL_PATH + "channel.tx";

        try {
            ChannelConfiguration channelConfiguration = new ChannelConfiguration(new File(path));
            channel = hfClient.newChannel(channelName, orderers.get(0), channelConfiguration, hfClient.getChannelConfigurationSignature(channelConfiguration, hfClient.getUserContext()));
            channel.joinPeer(peers.get(0));
            initChannel();
        } catch (IOException | ProposalException | InvalidArgumentException | TransactionException e) {
            throw new RuntimeException(e);
        }
    }

    public void recreateChannel() {

        String channelName = "mychannel";

        try {
            channel = hfClient.newChannel(channelName);
            initChannel();
        } catch (InvalidArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    private void initChannel() {
        try {
            channel.addOrderer(orderers.get(0));
            channel.addEventHub(eventHubs.get(0));
            channel.initialize();
        } catch (InvalidArgumentException | TransactionException e) {
            e.printStackTrace();
        }
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

    public Channel getChannel() {
        return channel;
    }
}
