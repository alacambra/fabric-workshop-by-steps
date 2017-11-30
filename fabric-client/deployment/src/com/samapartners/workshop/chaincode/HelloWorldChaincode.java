package com.samapartners.workshop.chaincode;

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;

/**
 * Created by alacambra on 28.11.17.
 */
public class HelloWorldChaincode extends ChaincodeBase {

    @Override
    public Response init(ChaincodeStub chaincodeStub) {
        System.out.println("Chaincode started");
        return newSuccessResponse("Hello world", "Hello world".getBytes());
    }

    @Override
    public Response invoke(ChaincodeStub chaincodeStub) {
        return newSuccessResponse("Hello world", "Hello world!".getBytes());
    }

    public static void main(String[] args) {
        new HelloWorldChaincode().start(args);
    }

}
