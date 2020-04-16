package com.example.android.wifidirect.discovery.D2DSec;





import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.android.wifidirect.discovery.D2DSec.SerializableContents.CompleteCipher;
import com.example.android.wifidirect.discovery.D2DSec.SerializableContents.PartialCipher;
import com.example.android.wifidirect.discovery.D2DSec.SerializableContents.PrivateKey;
import com.example.android.wifidirect.discovery.HomeScreen;
import com.example.android.wifidirect.discovery.HomeScreen_backup;

import it.unisa.dia.gas.jpbc.Element;


public class CipherManager {




    public static void sendCipher(List<String> cipher, Map<String, String> record) {
        record.put("C1", cipher.get(0));
        record.put("C2", cipher.get(1));
        record.put("C3", cipher.get(2));
        record.put("C4", cipher.get(3));
        record.put("C5", cipher.get(4));
    }

    public static List<String> receiveCiphertest(Map<String, String> record) {
        List<String> receivedCipherVal = new ArrayList<String>();
        receivedCipherVal.add(record.get("broadcast"));
        return receivedCipherVal;
    }

    public static List<String> receiveCipher(Map<String, String> record) {
        List<String> receivedCipherVal = new ArrayList<String>();
        receivedCipherVal.add(record.get("C1"));
        receivedCipherVal.add(record.get("C2"));
        receivedCipherVal.add(record.get("C3"));
        receivedCipherVal.add(record.get("C4"));
        receivedCipherVal.add(record.get("C5"));
       return receivedCipherVal;
    }


    public static Element[] getPartialCipher(List<String> receivedCipher){
        return PartialCipher.getPC(receivedCipher);
    }

    public static Element[] getCompleteCipher(List<String> receivedCipher){
        return CompleteCipher.getCC(receivedCipher);
    }

    public static String getSessionKey(Element[] pk, Element[] cipher, Element[] policy, COABE coabe){
        Element M = coabe.Dec(pk, cipher, policy);
        byte [] sessionKeyBytes = M.toBytes();
        return sessionKeyBytes.toString().replaceAll("\0", "");
    }

    public static String getPrivateKeyString(){
        Element[] pk = HomeScreen_backup.coabe.genKey(HomeScreen_backup.xy_vec[0]);
        String private_key = ElementToCipher.ParctialCipherCreation(pk);
        return private_key;
    }


    public static Element[] getPK(List<String> PrivateKeyString , int mNumAttributeType){
        return  PrivateKey.getPK(PrivateKeyString,mNumAttributeType);
    }

    public static String getPolicyString(){
        String policyString =  ElementToCipher.toPolicyString(HomeScreen_backup.xy_vec[1]);
        return policyString;
    }






}
