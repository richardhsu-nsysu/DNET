
package com.example.android.wifidirect.discovery;

import android.app.ListFragment;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.wifidirect.discovery.D2DSec.CipherManager;
import com.example.android.wifidirect.discovery.D2DSec.SessionManager;

import java.util.ArrayList;
import java.util.List;

import it.unisa.dia.gas.jpbc.Element;

/**
 * A simple ListFragment that shows the available services as published by the
 * peers
 */
public class WiFiDirectServicesList extends ListFragment {

    WiFiDevicesAdapter listAdapter = null;

    interface DeviceClickListener {
        public void connectP2p(WiFiP2pService wifiP2pService);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.devices_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listAdapter = new WiFiDevicesAdapter(this.getActivity(),
                android.R.layout.simple_list_item_2, android.R.id.text1,
                new ArrayList<WiFiP2pService>());
        setListAdapter(listAdapter);
        Log.d("Session","items "+ listAdapter.items.size());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        ((DeviceClickListener) getActivity()).connectP2p((WiFiP2pService) l
                .getItemAtPosition(position));
        ((TextView) v.findViewById(android.R.id.text2)).setText("Connecting");

    }



    public class WiFiDevicesAdapter extends ArrayAdapter<WiFiP2pService> {

        private List<WiFiP2pService> items;

        public WiFiDevicesAdapter(Context context, int resource,
                int textViewResourceId, List<WiFiP2pService> items) {
            super(context, resource, textViewResourceId, items);
            this.items = items;
            Log.d("Session","items d"+ items.size());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(android.R.layout.simple_list_item_2, null);
            }
            WiFiP2pService service = items.get(position);

//            if (service.instanceName.equals(BroadcasterP2PSD.SERVICE_INSTANCE)){
//                SessionExtractor sessionExtractor = new SessionExtractor(service);
//                sessionExtractor.start();
//            }

            if (service != null) {
                TextView nameText = (TextView) v
                        .findViewById(android.R.id.text1);

                if(nameText != null ) {
                    if (service.instanceName.equals(BroadcasterP2PSD.SERVICE_INSTANCE)) {
                        Log.d("Session","In View "+service.Sessionkey);
                        nameText.setText(service.device.deviceName + " - " + service.ReceivedContent.get(0));
                    } else {
                        nameText.setText(service.device.deviceName);
                    }
                }
                TextView statusText = (TextView) v
                        .findViewById(android.R.id.text2);
                statusText.setText(getDeviceStatus(service.device.status));
            }



            return v;
        }

    }

    public static String getDeviceStatus(int statusCode) {
        switch (statusCode) {
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknown";

        }
    }


//    class SessionExtractor extends Thread {
//        private  WiFiP2pService service;
//
//            SessionExtractor(WiFiP2pService service){
//                this.service = service;
//            }
//        @Override
//        public void run() {
//             this.service.Sessionkey = this.service.Sessionkey+this.extractSessionkey(this.service.ReceivedContent);
//            Log.d("Session", "In Run "+this.service.Sessionkey);
//        }
//
//        private String extractSessionkey(List<String> receivedCipher) {
//            Element[] CCnew = CipherManager.getPartialCipher(receivedCipher);
//            Element[] pk = HomeScreen.coabe.genKey(HomeScreen.xy_vec[0]); //TO-Do: load pk here
//            Element Mr = HomeScreen.coabe.Dec(pk, CCnew, HomeScreen.xy_vec[1]);
//            byte [] keyBytes = Mr.toBytes();
//            String keyBytesString = new String(keyBytes).replaceAll("\0", "");
//            System.out.println("keyBytesString " + keyBytesString);
//            keyBytes = keyBytesString.getBytes();
//
//            String receivedSessionKey = new String(keyBytes,HomeScreen.UTF8_CHARSET);
//            return receivedSessionKey;
//
//        }
//    }



//    public class SessionExtractorAsync extends AsyncTask<WiFiP2pService,String,String> {
//
//        private WiFiP2pService service;
//        private  String SessionKey;
//
//        @Override
//        protected String doInBackground(WiFiP2pService... params){
//            this.service = params[0];
//            this.SessionKey =this.extractSessionkey(this.service.ReceivedContent);
//
//            return this.SessionKey;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            SessionManager sessionManager = new SessionManager();
//            sessionManager.setSessionKey(result);
//
//
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            ProgressBar progressBar = findViewById(R.id.progressBar);
//            progressBar.animate();
//        }
//
//
//        private String extractSessionkey(List<String> receivedCipher) {
//            Element[] CCnew = CipherManager.getPartialCipher(receivedCipher);
//            Element[] pk = HomeScreen.xy_vec[0]; //TO-Do: load pk here
//            Element Mr = HomeScreen.coabe.Dec(pk, CCnew, HomeScreen.xy_vec[1]);
//            byte [] keyBytes = Mr.toBytes();
//            String keyBytesString = new String(keyBytes).replaceAll("\0", "");
//            System.out.println("keyBytesString " + keyBytesString);
//            keyBytes = keyBytesString.getBytes();
//
//            String receivedSessionKey = new String(keyBytes,HomeScreen.UTF8_CHARSET);
//            return receivedSessionKey;
//
//        }
//
//
//
//    }


}
