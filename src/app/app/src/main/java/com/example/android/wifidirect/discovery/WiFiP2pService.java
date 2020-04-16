
package com.example.android.wifidirect.discovery;

import android.net.wifi.p2p.WifiP2pDevice;

import java.util.List;

/**
 * A structure to hold service information.
 */
public class WiFiP2pService {
    String Sessionkey="";
    WifiP2pDevice device;
    String instanceName = null;
    String serviceRegistrationType = null;
    List<String> ReceivedContent ;
}
