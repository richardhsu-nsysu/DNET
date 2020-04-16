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

public class PartialCipher extends StringElementTransition {
    private static final long serialVersionUID = 1L;
    private byte[][] partialCipher;

    public PartialCipher(byte[][] partialCipher){
        this.partialCipher = partialCipher;
    }

    public PartialCipher(Element[] E) {
        this.partialCipher = super.toByteArray(E);
    }

    /**
     * @param partialCipher the partialCipher to set
     */
    public void setPartialCipher(byte[][] partialCipher) {
        this.partialCipher = partialCipher;
    }

    /**
     * @param partialCipher the partialCipher to set
     */
    public void setPartialCipher(Element[] partialCipher) {
        this.partialCipher = super.toByteArray(partialCipher);
    }

    /**
     * @return the partialCipher
     */
    public byte[][] getPartialCipher() {
        return partialCipher;
    }


    @TargetApi(Build.VERSION_CODES.O)
    private static Element[] toElement(List<String> transferableString) {
        Element[] E = new Element[5];
        for(int q = 0; q<E.length;q++){
            if(q==2){
                Element tempE = COABE.mGT.newRandomElement();
                tempE.setFromBytes(Base64.getDecoder().decode(transferableString.get(q)));
                E[q] = (Element)tempE.getImmutable();
            }else{
                Element tempE = COABE.mG1.newRandomElement();
                tempE.setFromBytes(Base64.getDecoder().decode(transferableString.get(q)));
                E[q] = (Element)tempE.getImmutable();
            }
        }
        return E;
    }

    
    @TargetApi(Build.VERSION_CODES.O)
    private List<String> toTransferableList() {
        List<String> out = new ArrayList<String>();
        for(int i=0; i<this.partialCipher.length;i++)
        {
            out.add(Base64.getEncoder().encodeToString(this.partialCipher[i]));

        }
        return out;
    }


    
    public List<String> getPCString(){
        return this.toTransferableList();
    }

    
    public static Element[] getPC(List<String> PCString){
        return toElement(PCString);
    }


    
    @TargetApi(Build.VERSION_CODES.O)
    public static PartialCipher getObjectBase64(String transferableString) throws IOException, ClassNotFoundException {
        final byte[] bytes = Base64.getDecoder().decode(transferableString);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInputStream in = new ObjectInputStream(bis)) {
            return (PartialCipher) in.readObject();
        }
    }
}