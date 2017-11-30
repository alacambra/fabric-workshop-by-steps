package com.samapartners.workshop.client;

import org.hyperledger.fabric.sdk.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class FabricManagerTest {

    FabricManager cut;
    private ChaincodeID chaincodeID;

    @Before
    public void setUp() throws Exception {
        cut = new FabricManager();
        chaincodeID = ChaincodeID.newBuilder()
                .setName("hello")
                .setVersion("5")
                .build();
    }

    @Test
    public void testInit() {
        cut = new FabricManager();
    }

    @Test
    public void testChannelCreation() {
        cut.createChannel();
        Channel channel = cut.getChannel();
        assertThat(channel, notNullValue());
        assertThat(channel.isInitialized(), is(true));
    }

    @Test
    public void testChannelInitialization() {
        cut.recreateChannel();
        Channel channel = cut.getChannel();
        assertThat(channel, notNullValue());
        assertThat(channel.isInitialized(), is(true));
    }

    @Test
    public void installChaincode() {
        cut.recreateChannel();
        String chaincodeSourceLocation = "/Users/albertlacambra1/git/fabric-workshop-by-steps/fabric-client/deployment";
        Collection<Peer> peers = cut.getPeers();
        List<ProposalResponse> responses = cut.installChaincode(chaincodeID, chaincodeSourceLocation, peers);
        ProposalResponse response = responses.stream().findAny().get();
        assertThat(response.getMessage(), response.getStatus(), is(ChaincodeResponse.Status.SUCCESS));
    }

    @Test
    public void instantiateChaincode() {
        cut.recreateChannel();
        List<ProposalResponse> responses = cut.instantiateChaincode(chaincodeID);
        ProposalResponse response = responses.stream().findAny().get();
        assertThat(response.getMessage(), response.getStatus(), is(ChaincodeResponse.Status.SUCCESS));
    }

    @Test
    public void testQuery() {
        cut.recreateChannel();
        String response = cut.query(chaincodeID, "functionName", new String[]{""});
        assertThat(response, is("Hello world!"));
    }
}