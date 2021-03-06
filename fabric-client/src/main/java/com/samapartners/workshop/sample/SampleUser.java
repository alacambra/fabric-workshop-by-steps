package com.samapartners.workshop.sample;/*
 *  Copyright 2016 DTCC, Fujitsu Australia Software Technology - All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 	  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import com.sun.deploy.security.ruleset.RunRule;
import io.netty.util.internal.StringUtil;
import org.bouncycastle.util.encoders.Hex;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import java.io.*;
import java.util.List;
import java.util.Set;

public class SampleUser implements User, Serializable {
    private static final long serialVersionUID = 8077132186383604355L;

    private String name;
    private Set<String> roles;
    private String account;
    private String affiliation;
    private String organization;
    private String enrollmentSecret;
    Enrollment enrollment = null; //need access in test env.

    private transient SampleStore keyValStore;
    private String keyValStoreName;


    public SampleUser(String name, String org, SampleStore fs) {
        this.name = name;

        this.keyValStore = fs;
        this.organization = org;
        this.affiliation = org;
        this.keyValStoreName = toKeyValStoreName(this.name, org);
        String memberStr = keyValStore.getValue(keyValStoreName);
        if (null == memberStr) {
            saveState();
        } else {
            restoreState();
        }


    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Set<String> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<String> roles) {

        this.roles = roles;
        saveState();
    }

    @Override
    public String getAccount() {
        return this.account;
    }

    /**
     * Set the account.
     *
     * @param account The account.
     */
    public void setAccount(String account) {

        this.account = account;
        saveState();
    }

    @Override
    public String getAffiliation() {
        return this.affiliation;
    }

    /**
     * Set the affiliation.
     *
     * @param affiliation the affiliation.
     */
    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    @Override
    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    public String getMspId() {
        return getMSPID();
    }

    public String getMSPID() {
        return mspID;
    }

    /**
     * Determine if this name has been registered.
     *
     * @return {@code true} if registered; otherwise {@code false}.
     */
    public boolean isRegistered() {
        return !StringUtil.isNullOrEmpty(enrollmentSecret);
    }

    /**
     * Determine if this name has been enrolled.
     *
     * @return {@code true} if enrolled; otherwise {@code false}.
     */
    public boolean isEnrolled() {
        return this.enrollment != null;
    }


    private String getAttrsKey(List<String> attrs) {
        if (attrs == null || attrs.isEmpty()) {
            return null;
        }
        return String.join(",", attrs);
    }


    /**
     * Save the state of this user to the key value store.
     */
    public void saveState() {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        try {
//            ObjectOutputStream oos = new ObjectOutputStream(bos);
//            oos.writeObject(this);
//            oos.flush();
//            keyValStore.setValue(keyValStoreName, Hex.toHexString(bos.toByteArray()));
//            bos.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }


    /**
     * Restore the state of this user from the key value store (if found).  If not found, do nothing.
     */
    SampleUser restoreState() {
        String memberStr = keyValStore.getValue(keyValStoreName);
        if (null != memberStr) {
            // The user was found in the key value store, so restore the
            // state.
            byte[] serialized = Hex.decode(memberStr);
            ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
            try {
                ObjectInputStream ois = new ObjectInputStream(bis);
                SampleUser state = (SampleUser) ois.readObject();
                if (state != null) {
                    this.name = state.name;
                    this.roles = state.roles;
                    this.account = state.account;
                    this.affiliation = state.affiliation;
                    this.organization = state.organization;
                    this.enrollmentSecret = state.enrollmentSecret;
                    this.enrollment = state.enrollment;
                    this.mspID = state.mspID;
                    return this;
                }
            } catch (Exception e) {
                throw new RuntimeException(String.format("Could not restore state of member %s", this.name), e);
            }
        }
        return null;
    }

    public String getEnrollmentSecret() {
        return enrollmentSecret;
    }

    public void setEnrollmentSecret(String enrollmentSecret) {
        this.enrollmentSecret = enrollmentSecret;
        saveState();
    }


    public void setEnrollment(Enrollment enrollment) {

        this.enrollment = enrollment;
        saveState();

    }

    public static String toKeyValStoreName(String name, String org) {
        return "user." + name + org;
    }


    String mspID;

    public void setMPSID(String mspID) {
        this.mspID = mspID;
        saveState();

    }

    @Override
    public String toString() {
        return "com.samapartners.workshop.sample.SampleUser{" +
                "name='" + name + '\'' +
                ", roles=" + roles +
                ", account='" + account + '\'' +
                ", affiliation='" + affiliation + '\'' +
                ", organization='" + organization + '\'' +
                ", enrollmentSecret='" + enrollmentSecret + '\'' +
                ", keyValStoreName='" + keyValStoreName + '\'' +
                ", mspID='" + mspID + '\'' +
                '}';
    }
}
