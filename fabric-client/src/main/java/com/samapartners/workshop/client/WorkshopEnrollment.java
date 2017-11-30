package com.samapartners.workshop.client;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.hyperledger.fabric.sdk.Enrollment;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class WorkshopEnrollment implements Enrollment {

    @Override
    public PrivateKey getKey() {
        //"crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore"
        String pKey = "-----BEGIN PRIVATE KEY-----\n" +
                "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgBdjKqyzRJDfV/3L7\n" +
                "u9Qhfo/ONYEexi3ehggoYWSR7rqhRANCAAS0Moo/o5+uWcSJqoVGKGKWAeKQYRMz\n" +
                "4tyGDHzpPuKPpG3oWdDse8PB4X48ns8Ld8Wi8IrJJEvTAJVq3bvzQPxf\n" +
                "-----END PRIVATE KEY-----";
        return fromPemToPrivateKey(pKey);
    }

    @Override
    public String getCert() {

        //crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/Admin@org1.example.com-cert.pem
        return "-----BEGIN CERTIFICATE-----\n" +
                "MIICGTCCAb+gAwIBAgIQEUs4pEsoXq/FinVOqEkPQzAKBggqhkjOPQQDAjBzMQsw\n" +
                "CQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMNU2FuIEZy\n" +
                "YW5jaXNjbzEZMBcGA1UEChMQb3JnMS5leGFtcGxlLmNvbTEcMBoGA1UEAxMTY2Eu\n" +
                "b3JnMS5leGFtcGxlLmNvbTAeFw0xNzA5MDcyMDAzMjJaFw0yNzA5MDUyMDAzMjJa\n" +
                "MFsxCzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMRYwFAYDVQQHEw1T\n" +
                "YW4gRnJhbmNpc2NvMR8wHQYDVQQDDBZBZG1pbkBvcmcxLmV4YW1wbGUuY29tMFkw\n" +
                "EwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEtDKKP6OfrlnEiaqFRihilgHikGETM+Lc\n" +
                "hgx86T7ij6Rt6FnQ7HvDweF+PJ7PC3fFovCKySRL0wCVat2780D8X6NNMEswDgYD\n" +
                "VR0PAQH/BAQDAgeAMAwGA1UdEwEB/wQCMAAwKwYDVR0jBCQwIoAgubagHiRoR1VS\n" +
                "k7NCOwAqgo0K8BOvUOAQusBdhFKh8JMwCgYIKoZIzj0EAwIDSAAwRQIhAIGrQvMY\n" +
                "piqiX8mVbZ5QuO6bKg6WHXCjDTjcGYsG6UTZAiAToRH3oadJ6nrDmBS/+sEBEuVu\n" +
                "GKnLz1IqGLLePl1u3w==\n" +
                "-----END CERTIFICATE-----";
    }

    private PrivateKey fromPemToPrivateKey(String pemPrivateKey) {
        PemReader pemReader = new PemReader(new StringReader(pemPrivateKey));
        try {
            KeyFactory factory = KeyFactory.getInstance("ECDSA", "BC");
            PemObject pemObject = pemReader.readPemObject();
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(pemObject.getContent());
            return factory.generatePrivate(privKeySpec);
        } catch (IOException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
