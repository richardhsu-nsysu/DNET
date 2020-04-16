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

public class PrivateKey extends StringElementTransition {

    private static final long serialVersionUID = 1L;
    private byte[][] privateKey;

    public PrivateKey(byte[][] privateKey) {
        this.privateKey = privateKey;
    }

    public PrivateKey(Element[] E) {
        this.privateKey = super.toByteArray(E);
    }

    /**
     * @param privateKey the privateKey to set
     */
    public void setPrivateKey(byte[][] privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * @param privateKey the privateKey to set
     */
    public void setPrivateKey(Element[] privateKey) {
        this.privateKey = super.toByteArray(privateKey);
    }

    /**
     * @return the privateKey
     */
    public byte[][] getPrivateKey() {
        return privateKey;
    }



    @TargetApi(Build.VERSION_CODES.O)
    public List<String> toTransferableList() {
        List<String> out = new ArrayList<String>();
        for(int i=0; i<this.privateKey.length;i++)
        {
            out.add(Base64.getEncoder().encodeToString(this.privateKey[i]));

        }
        return out;
    }

    public static Element[] toElement(List<String> TransferableString, int mNumAttributeType) {
        Element[] E = new Element[mNumAttributeType];
        for(int q = 0; q<E.length;q++){
                Element tempE = COABE.mG2.newRandomElement();
                tempE.setFromBytes(Base64.getDecoder().decode(TransferableString.get(q)));
                E[q] = (Element)tempE.getImmutable();
            }
        return E;
    }

    

    public List<String> getPKString(){
        return this.toTransferableList();
    }


    public static Element[] getPK(List<String> PrivateKeyString , int mNumAttributeType ){
        return toElement(PrivateKeyString, mNumAttributeType);
    }


    @TargetApi(Build.VERSION_CODES.O)
    public static PrivateKey getObjectBase64(String transferableString) throws IOException, ClassNotFoundException {
        final byte[] bytes = Base64.getDecoder().decode(transferableString);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInputStream in = new ObjectInputStream(bis)) {
            return (PrivateKey) in.readObject();
        }
    }
}