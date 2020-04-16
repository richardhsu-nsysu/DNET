// package com.example.android.wifidirect.discovery.D2DSec.SerializableContents;

// import android.os.Build;
// import android.support.annotation.RequiresApi;

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

public class CompleteCipher extends StringElementTransition{

    private static final long serialVersionUID = 1L;
    private byte[][] completeCipher;

    public CompleteCipher(byte[][] completeCipher){
        this.completeCipher = completeCipher;

    }


    public CompleteCipher(Element[] E) {
        this.completeCipher = super.toByteArray(E);
    }


    /**
     * @param completeCipher the completeCipher to set
     */
    public void setCompleteCipher(byte[][] completeCipher) {
        this.completeCipher = completeCipher;
    }


    /**
     * @param completeCipher the completeCipher to set
     */
    public void setCompleteCipher(Element[] completeCipher) {
        this.completeCipher = super.toByteArray(completeCipher);
    }


    /**
     * @return the completeCipher
     */
    public byte[][] getCompleteCipher() {
        return completeCipher;
    }


    @TargetApi(Build.VERSION_CODES.O)
    public static Element[] toElement(List<String> transferableString) {
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
    public List<String> toTransferableList() {
        List<String> out = new ArrayList<String>();
        for(int i=0; i<this.completeCipher.length;i++)
        {
            out.add(Base64.getEncoder().encodeToString(this.completeCipher[i]));

        }
        return out;
    }


    public List<String> getCCString(){
        return this.toTransferableList();
    }


    public static Element[] getCC(List<String> CCString){
        return toElement(CCString);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static CompleteCipher getObjectBase64(String transferableString) throws IOException, ClassNotFoundException {
        final byte[] bytes = Base64.getDecoder().decode(transferableString);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInputStream in = new ObjectInputStream(bis)) {
            return (CompleteCipher) in.readObject();
        }
    }
}