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
        return ; //TODO set correct key
    }

    @Override
    public String getCert() {
        return ;  //TODO set correct cert
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
