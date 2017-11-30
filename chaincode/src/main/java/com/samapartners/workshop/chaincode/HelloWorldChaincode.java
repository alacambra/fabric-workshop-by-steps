package com.samapartners.workshop.chaincode;

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;

import javax.json.Json;
import java.util.List;

/**
 * Created by alacambra on 28.11.17.
 */
public class HelloWorldChaincode extends ChaincodeBase {

    @Override
    public Response init(ChaincodeStub chaincodeStub) {
        System.out.println("Chaincode started");
        return newSuccessResponse("Hello world", "Hello world".getBytes());
    }

    public HelloWorldChaincode() {
    }

    @Override
    public Response invoke(ChaincodeStub stub) {

        List<String> args = stub.getParameters();
        String function = stub.getFunction();

        System.out.println(String.format("Received invocation . Function=%s, args=%s", function, args));
        byte[] response;
        switch (function) {
            case "put":
                System.out.println("Executing put: " + args.get(0));
                stub.putState("key", Json.createObjectBuilder().add("value", args.get(0)).build().toString().getBytes());
                response = Json.createObjectBuilder().add("value", "done!").build().toString().getBytes();
                break;
            case "get":
                System.out.println("Executing get: ");
                response = stub.getState("key");
                break;
            default:
                System.out.println("no valid function executed: " + function);
                response = Json.createObjectBuilder().add("value", "nothingtodo").build().toString().getBytes();
        }

        System.out.println("Sending response:" + new String(response));
        return newSuccessResponse("all ok", response);
    }

    public static void main(String[] args) {
        new HelloWorldChaincode().start(args);
    }

}
