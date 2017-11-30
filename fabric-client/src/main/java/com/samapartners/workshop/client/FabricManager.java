package com.samapartners.workshop.client;

import com.samapartners.workshop.sample.SampleStore;
import com.samapartners.workshop.sample.SampleUser;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.util.List;

public class FabricManager {

    private static final String HOST = "192.168.99.100"; //TODO: set url

    private final HFClient hfClient;
    private List<Peer> peers;
    private List<Orderer> orderers;
    private List<EventHub> eventHubs;

    public FabricManager() {
        hfClient = HFClient.createNewInstance();
        initHFClient();
    }

    private void initHFClient() {
        try {
            hfClient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            //todo Set enrolled user
        } catch (CryptoException | InvalidArgumentException e) {
            e.printStackTrace();
        }
    }

    private SampleUser getWorkshopEnrollment() {

        String organizationName = "org1";
        String organizationMspId = "Org1MSP";
        String username = "Admin";

        SampleUser sampleUser = new SampleUser(username, organizationName, SampleStore.load());
        sampleUser.setMPSID(organizationMspId);
        Enrollment enrollment = new WorkshopEnrollment();
        sampleUser.setEnrollment(enrollment);
        return sampleUser;
    }
}
