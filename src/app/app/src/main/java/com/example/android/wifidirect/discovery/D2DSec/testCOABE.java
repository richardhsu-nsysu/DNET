package com.example.android.wifidirect.discovery.D2DSec;

import android.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import it.unisa.dia.gas.jpbc.Element;


public class testCOABE {


//    public void simulate() throws NoSuchAlgorithmException {
//          Element[][] xy_vec= EdgeModule.MasterServer();
//          String EncodedCiphertext = DiscovererModule.Discoverer(xy_vec);
//          DiscovereeModule.Dicoveree(EncodedCiphertext);
//
//    }



    public void simulate() throws NoSuchAlgorithmException {
        String curve_file_path = "assets/a.properties";

        COABE co_abe = new COABE(0, curve_file_path);
        Element[][] xy_vec = co_abe.genXYVec(160, 20);
        co_abe.genPairingParameters();
        co_abe.setNumAttType(20);
        co_abe.setup(512);

        Element[] pk = co_abe.genKey(xy_vec[0]);

        String private_key = ElementToCipher.ParctialCipherCreation(pk);
        Element[] pk_r = ElementToCipher.toPrivateKeyElementArray(private_key,20,co_abe);

        if(Arrays.equals(pk_r, pk)){
            System.out.println("Privatekey Key Matched");
        }
        else {
            System.out.println("No match found");
        }

        System.out.println("Privatekey Key " + private_key);



        int AESKeySize = 256;

        // Get and display the alphanumeric string AESkey
        String AESkey = RandomString.getAlphaNumericString(AESKeySize);

        String msg = "Hello I am Onkar c";

        String AESencrypted = AES.encrypt(msg, AESkey);
        // base64 AES encrypted string


        StringHash stringHash = new StringHash();
        String hashedKey = stringHash.Hash(AESkey);

        System.out.println("Sent Session Key " + AESkey);
        byte[] ogM = AESkey.getBytes();
        byte[] encoded = Base64.encode(ogM, Base64.NO_PADDING);
        // base64  AESkey

        Element M = co_abe.newRandomElement(2);
        M.setFromBytes(encoded);


        // Discoverer
        Element[] PC = co_abe.PreEnc(xy_vec[1], M);
        String partialCipherText = ElementToCipher.ParctialCipherCreation(PC);
        System.out.println("Partial Cipher Text " + partialCipherText);

        String policyString =  ElementToCipher.toPolicyString(xy_vec[1]);
        System.out.println("Policy String "+ policyString);

        String partialText = BroadcastMessageHandler.PartialTextEncoder(partialCipherText,policyString);

        //Post call to send partialCiphertext and get Complete Cipher Text
        String[] receivedContents = BroadcastMessageHandler.PartialTextDecoder(partialText);

        Element[] receivedPC = ElementToCipher.toElementArray(receivedContents[0], co_abe);

        if (Arrays.deepEquals(PC, receivedPC)) {
            System.out.println("Partial Cipher Text Recieved Properly");
        } else {
            System.out.println("Partial Cipher Text Changed");
        }


        Element [] policy =  ElementToCipher.toPolicy(receivedContents[1],20,co_abe);


        //Edge
        Element[] C = co_abe.CoEnc(receivedPC, policy);

        String CompleteCipherText = ElementToCipher.CompleteCipherCreation(C);
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
        Element Mp = co_abe.Dec(pk, receivedC, xy_vec[1]);
        byte [] encMp = Mp.toBytes();
        byte [] decoded = Base64.decode(encMp, Base64.NO_PADDING);
        String receivedSessionKey = new String(decoded);
        System.out.println("Received Session Key " + receivedSessionKey);

        if (values[3].equals(stringHash.Hash(receivedSessionKey))) {
            System.out.println("Session Key Verified by Hash");
        } else {
            System.out.println("Session Key Corrupted");
        }


        String decryptedString = AES.decrypt(values[2], receivedSessionKey);
        System.out.println("Decrypted Message using Session Key " + decryptedString);

    } 

}




