package com.example.android.wifidirect.discovery.D2DSec;

public class params {
    public String privatekey;
    public String username;
    public String organizationname;
    public String group;
    public String policy;


    public void setGroup(String group) {
        this.group = group;
    }

    public void setOrganizationname(String organizationname) {
        this.organizationname = organizationname;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getGroup() {
        return group;
    }

    public String getOrganizationname() {
        return organizationname;
    }

    public String getPrivatekey() {
        return privatekey;
    }
}
