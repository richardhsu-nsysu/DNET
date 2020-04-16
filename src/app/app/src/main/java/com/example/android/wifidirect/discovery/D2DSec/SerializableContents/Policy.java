package com.example.android.wifidirect.discovery.D2DSec.SerializableContents;

import android.annotation.TargetApi;
import android.os.Build;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.example.android.wifidirect.discovery.D2DSec.COABE;
import it.unisa.dia.gas.jpbc.Element;

public class Policy extends StringElementTransition {
    private static final long serialVersionUID = 1L;
    private byte[][] policy;

    public Policy(byte[][] policy) {
        this.policy = policy;
    }

    public Policy(Element[] E) {
        this.policy = super.toByteArray(E);
    }
    /**
     * @param policy the policy to set
     */
    public void setPolicy(byte[][] policy) {
        this.policy = policy;
    }

    /**
     * @param policy the policy to set
     */
    public void setPolicy(Element[] policy) {
        this.policy = super.toByteArray(policy);
    }

    /**
     * @return the policy
     */
    public byte[][] getPolicy() {
        return policy;
    }



    @TargetApi(Build.VERSION_CODES.O)
    private Element[] toElement(List<String> transferableContent, int mNumAttributeType) {
        Element[] E = new Element[mNumAttributeType];
        for(int q = 0; q<E.length;q++){
            Element tempE = COABE.mZr.newRandomElement();
            tempE.setFromBytes(Base64.getDecoder().decode(transferableContent.get(q)));
            E[q] = (Element)tempE;
        }
        return E;
    }


    @TargetApi(Build.VERSION_CODES.O)
    private List<String> toTransferableList() {
        List<String> out = new ArrayList<String>();
        for(int i=0; i<this.policy.length;i++)
        {
            out.add(Base64.getEncoder().encodeToString(this.policy[i]));

        }
        return out;
    }


    public List<String> getPolicyString(){
        return this.toTransferableList();
    }

    public Element[] getPolicy(List<String> PolicyString, int mNumAttributeType){
        return this.toElement(PolicyString,mNumAttributeType);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static Policy getObjectBase64(String transferableString) throws IOException, ClassNotFoundException {
        final byte[] bytes = Base64.getDecoder().decode(transferableString);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInputStream in = new ObjectInputStream(bis)) {
            return (Policy) in.readObject();
        }
    }
}


