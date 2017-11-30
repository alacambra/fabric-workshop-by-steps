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
                "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgvd1k1R2/xqnwIP3E\n" +
                "G2vPSvR0p9jNDBsyGQo1crYEgDKhRANCAASWHAJtzDyAIl/rzIUHs57qWpvis0ht\n" +
                "RUPcetHHOLnG8aVRRcNH624BXNQSIfGdGrs3LjsW+B3O7GK/0KJ/DBUN\n" +
                "-----END PRIVATE KEY-----";
        return fromPemToPrivateKey(pKey);
    }

    @Override
    public String getCert() {

        //crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/Admin@org1.example.com-cert.pem
        return "-----BEGIN CERTIFICATE-----\n" +
                "MIICGTCCAcCgAwIBAgIRAIuOQz6wbj5ImyKmf2lH7GUwCgYIKoZIzj0EAwIwczEL\n" +
                "MAkGA1UEBhMCVVMxEzARBgNVBAgTCkNhbGlmb3JuaWExFjAUBgNVBAcTDVNhbiBG\n" +
                "cmFuY2lzY28xGTAXBgNVBAoTEG9yZzEuZXhhbXBsZS5jb20xHDAaBgNVBAMTE2Nh\n" +
                "Lm9yZzEuZXhhbXBsZS5jb20wHhcNMTcxMTMwMDc1NjQ2WhcNMjcxMTI4MDc1NjQ2\n" +
                "WjBbMQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMN\n" +
                "U2FuIEZyYW5jaXNjbzEfMB0GA1UEAwwWQWRtaW5Ab3JnMS5leGFtcGxlLmNvbTBZ\n" +
                "MBMGByqGSM49AgEGCCqGSM49AwEHA0IABJYcAm3MPIAiX+vMhQeznupam+KzSG1F\n" +
                "Q9x60cc4ucbxpVFFw0frbgFc1BIh8Z0auzcuOxb4Hc7sYr/Qon8MFQ2jTTBLMA4G\n" +
                "A1UdDwEB/wQEAwIHgDAMBgNVHRMBAf8EAjAAMCsGA1UdIwQkMCKAIGcW49PV2p1V\n" +
                "XUKL4R3WtNCyNrUpz9h/0dvvoLPuo4cWMAoGCCqGSM49BAMCA0cAMEQCIFf8+0YL\n" +
                "gM6ePeOn47Mqw+wUTHFxWgY3Z5eWc28lOkGsAiAUa/8i6jXzZDmwF3SO2BAhtFcL\n" +
                "/gTSinnkgj2IyZ8vnA==\n" +
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
