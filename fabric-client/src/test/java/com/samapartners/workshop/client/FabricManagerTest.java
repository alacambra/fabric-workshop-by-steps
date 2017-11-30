package com.samapartners.workshop.client;

import org.hyperledger.fabric.sdk.Channel;
import org.junit.Before;
import org.junit.Test;

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


}