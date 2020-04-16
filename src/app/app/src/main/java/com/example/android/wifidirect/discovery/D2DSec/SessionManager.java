package com.example.android.wifidirect.discovery.D2DSec;

public class SessionManager {
    private String sessionKey;


    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public static String chatEncrypty(String msg , String sessionKey){
        return msg+sessionKey;
    }

    public static String chatDecrypt(String rmsg , String sessionKey){
        return rmsg.replace(sessionKey,"");
    }
}
