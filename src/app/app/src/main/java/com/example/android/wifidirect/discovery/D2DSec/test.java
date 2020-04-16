package com.example.android.wifidirect.discovery.D2DSec;

import java.security.NoSuchAlgorithmException;

public class test {
    public static void main(String[] args) {
        simulationTest Coabe = new simulationTest();
        try {
            Coabe.simulate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}