package com.example.android.wifidirect.discovery.D2DSec;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import it.unisa.dia.gas.jpbc.Element;


public class simulationCom {

    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    public void simulate() throws NoSuchAlgorithmException {
        String curve_file_path = "assets/a.properties";

        COABE co_abe = new COABE(0, curve_file_path);
        Element[][] xy_vec = co_abe.genXYVec(160, 20);
        co_abe.genPairingParameters();
        co_abe.setNumAttType(20);
        co_abe.setup(512);


        // Discoverer

        Element[] pk = co_abe.genKey(xy_vec[0]);
        
        int AESKeySize = 256;

        // Get and display the alphanumeric string AESkey
        String AESkey = RandomString.getAlphaNumericString(AESKeySize);

        String msg = "Hello I am Onkar c";

        String AESencrypted = AES.encrypt(msg, AESkey);

        StringHash stringHash = new StringHash();
        String hashedKey = stringHash.Hash(AESkey);

        System.out.println("Sent Session Key " + AESkey);

        Element M = co_abe.newRandomElement(2);
        M.setFromBytes(AESkey.getBytes(UTF8_CHARSET));
        
        Element[] PC = co_abe.PreEnc(xy_vec[1], M);

        //transer to edge
        String partialCipherText = ElementToCipher.ParctialCipherCreation(PC).replaceAll("\n", "");

        System.out.println("Partial Cipher Text " + partialCipherText);


        String policyString =  ElementToCipher.toPolicyString(xy_vec[1]);
        
        System.out.println("Policy String "+ policyString);
        
        String partialText = BroadcastMessageHandler.PartialTextEncoder(partialCipherText,policyString).replaceAll("\n", "");
        //transer to edge end

        //Received at edge
        //Post call to send partialCiphertext and get Complete Cipher Text
        String[] receivedContents = BroadcastMessageHandler.PartialTextDecoder(partialText);

        //Received at edge end

        //calculation at edge
        Element[] receivedPC = ElementToCipher.toElementArray(receivedContents[0].replaceAll("\n", ""), co_abe);
        
        if (Arrays.deepEquals(PC, receivedPC)) {
            System.out.println("Partial Cipher Text Recieved Properly");
        } else {
            System.out.println("Partial Cipher Text Changed");
        }


        Element [] policy =  ElementToCipher.toPolicy(receivedContents[1].replaceAll("\n", ""),20,co_abe);

        Element[] C = co_abe.CoEnc(receivedPC, policy);

        String CompleteCipherText = ElementToCipher.CompleteCipherCreation(C).replaceAll("\n", "");
        System.out.println("Complete Cipher Text " + CompleteCipherText);



        String device_name = "dummy";
        String broadcastMessage = BroadcastMessageHandler.BroadcastMessageCreation(device_name, CompleteCipherText, AESencrypted, hashedKey);
        String encodedBroadCastMessage = BroadcastMessageHandler.BroadcastMessageEncoder(broadcastMessage);

        int  broadcastMessageSize = encodedBroadCastMessage.length();
        System.out.println("broadcastMessageSize " + broadcastMessageSize);

        String[] values = BroadcastMessageHandler.BroadcastMessageDecoder(encodedBroadCastMessage);
        System.out.println("Device Name " + values[0]);
        System.out.println("Complete Cipher Text " + values[1]);
        System.out.println("Encrypted Text " + values[2]);
        System.out.println("Hashed Key " + values[3]);


        String receivedCompletekey = values[1];
        Element[] receivedC = ElementToCipher.toElementArray(receivedCompletekey, co_abe);

        if (Arrays.deepEquals(C, receivedC)) {
            System.out.println("Complete Cipher Text Recieved Properly");
        } else {
            System.out.println("Complete Cipher Text Changed");
        }


        //Discoveree
        Element Mr = co_abe.Dec(pk, receivedC, xy_vec[1]);


        String receivedSessionKey = new String(Mr.toBytes(),UTF8_CHARSET).replaceAll("\0", "");

        System.out.println("receivedSessionKey " + receivedSessionKey);

        if (hashedKey.equals(stringHash.Hash(receivedSessionKey))) {
            System.out.println("Session Key Verified by Hash");
        } else {
            System.out.println("Session Key Corrupted");
        }


        String decryptedString = AES.decrypt(AESencrypted, receivedSessionKey);
        System.out.println("Decrypted Message using Session Key " + decryptedString);

    } 

}