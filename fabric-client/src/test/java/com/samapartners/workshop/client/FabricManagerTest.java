package com.samapartners.workshop.client;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class FabricManagerTest {

    FabricManager cut;

    @Before
    public void setUp() throws Exception {
        cut = new FabricManager();
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
        ChaincodeID chaincodeID = //TODO create chaincodeId
        String chaincodeSourceLocation = ; //TODO set source
        Collection<Peer> peers = cut.getPeers();
        List<ProposalResponse> responses = cut.installChaincode(chaincodeID, chaincodeSourceLocation, peers);
        ProposalResponse response = responses.stream().findAny().get();
        assertThat(response.getMessage(), response.getStatus(), is()); //TODO set correct status
    }
}