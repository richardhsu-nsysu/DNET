package com.example.android.wifidirect.discovery.D2DSec;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

import it.unisa.dia.gas.jpbc.Element;


public class testBasic {
    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    public void simulate() throws NoSuchAlgorithmException {
        String curve_file_path = "assets/a.properties";

        COABE co_abe = new COABE(0, curve_file_path);
        Element[][] xy_vec = co_abe.genXYVec(160, 20);
        co_abe.genPairingParameters();
        co_abe.setNumAttType(20);
        co_abe.setup(512);

        Element[] pk = co_abe.genKey(xy_vec[0]);

        int AESKeySize = 256;

        // Get and display the alphanumeric string AESkey
        String AESkey = RandomString.getAlphaNumericString(AESKeySize);

        String msg = "Hello I am Onkar c";

        String AESencrypted = AES.encrypt(msg, AESkey);
        // base64 AES encrypted string


        StringHash stringHash = new StringHash();
        String hashedKey = stringHash.Hash(AESkey);

        System.out.println("Sent Session Key " + AESkey);

        Element M = co_abe.newRandomElement(2);
        M.setFromBytes(AESkey.getBytes(UTF8_CHARSET));


        // Discoverer
        Element[] PC = co_abe.PreEnc(xy_vec[1], M);


        //Edge
        Element[] C = co_abe.CoEnc(PC, xy_vec[1]);



        //Discoveree
        Element Mr = co_abe.Dec(pk, C, xy_vec[1]);

        byte [] keyBytes = Mr.toBytes();
        String keyByteStringDirect = keyBytes.toString().replaceAll("\0", "");
        System.out.println("keyByteStringDirect " + keyByteStringDirect);

        String keyBytesString = new String(keyBytes).replaceAll("\0", "");
        System.out.println("keyBytesString " + keyBytesString);

        keyBytes = keyBytesString.getBytes();
    
        String receivedSessionKey = new String(keyBytes,UTF8_CHARSET);
        System.out.println("Received Session Key " + receivedSessionKey);

        if (hashedKey.equals(stringHash.Hash(receivedSessionKey))) {
            System.out.println("Session Key Verified by Hash");
        } else {
            System.out.println("Session Key Corrupted");
        }


        String decryptedString = AES.decrypt(AESencrypted, receivedSessionKey);
        System.out.println("Decrypted Message using Session Key " + decryptedString);

    } 

}