package com.example.android.wifidirect.discovery.D2DSec;


import android.util.Base64;

class BroadcastMessageHandler {

    static String BroadcastMessageCreation(String DeviceName, String CompleteCipherText, String AESencryptedText, String hashedKey){
        String BrodcastMessage = String.join("!!..!!",DeviceName,CompleteCipherText,AESencryptedText,hashedKey);
        return BrodcastMessage;
    }

    static String BroadcastMessageEncoder(String BroadcastMessage){
        return Base64.encodeToString(BroadcastMessage.getBytes(),Base64.DEFAULT);
    }

    static String[] BroadcastMessageDecoder(String EncodedBroadcastMessage){
        String temp = new String(Base64.decode(EncodedBroadcastMessage.getBytes(), Base64.DEFAULT));
         String[] list= temp.split("!!..!!");
        return list;
    }


    static String PartialTextEncoder(String partialCipherText,String  policyString){
        String BrodcastMessage = String.join("!!..!!",partialCipherText,policyString);
        return Base64.encodeToString(BrodcastMessage.getBytes(),Base64.DEFAULT);
    }

    static String[] PartialTextDecoder(String EncodedBroadcastMessage){
        String temp = new String(Base64.decode(EncodedBroadcastMessage.getBytes(), Base64.DEFAULT));
        String[] list= temp.split("!!..!!");
        return list;
    }
}
