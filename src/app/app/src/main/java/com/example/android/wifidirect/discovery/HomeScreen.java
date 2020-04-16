package com.example.android.wifidirect.discovery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.example.android.wifidirect.discovery.AsyncRequests.HttpGetRequest;
import com.example.android.wifidirect.discovery.D2DSec.*;
import com.example.android.wifidirect.discovery.notification.NotificationToast;
import com.example.android.wifidirect.discovery.utils.Utility;

import it.unisa.dia.gas.jpbc.Element;

public class HomeScreen extends AppCompatActivity {

    public static final String WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int WRITE_PERM_REQ_CODE = 19;
    public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    public  static  String username;
    public  static  String group;

    public ArrayList<String> mpk;
    public  TextView tvUsername;
    public  Button fetchParamButton;
    public  Button searchModeButton;
    public  Button logoutButton;
    public TextView statusView;
    public String policy = null;
    public String privatekey;
    public boolean isPolicyFeched=false;
    public params homescreenparams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_new);
        tvUsername = findViewById(R.id.et_home_player_name);
        fetchParamButton = findViewById(R.id.fetch_param_button);
        searchModeButton = findViewById(R.id.search_mode_button);
        logoutButton = findViewById(R.id.logout_button);
        statusView = findViewById(R.id.status);

        //get Extras from login
        Intent LoginIntent = getIntent();
        username = LoginIntent.getStringExtra("username");
        group = LoginIntent.getStringExtra("group");
        mpk = (ArrayList<String>) LoginIntent.getSerializableExtra("mpk");
        Log.d("Homescreen","Mpk "+mpk.get(0));
        tvUsername.setText("Welcome "+username);

        if(group.equals("org")){
            searchModeButton.setText("Discoveree Mode");
            fetchParamButton.setText("Fetch or Update Policy");
            statusView.setText("You are Logged In as Organization");
        }else {
            searchModeButton.setText("Discoverer Mode");
            fetchParamButton.setText("Fetch or Update Private Key");
            statusView.setText("You are Logged In as Customer");
        }
        checkWritePermission();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            NotificationToast.showToast(HomeScreen.this, "This permission is needed for " +
                    "file sharing. But Whatever, if that's what you want...!!!");
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    private void saveUsername() {
        String userName = username;
        if (userName != null && userName.trim().length() > 0) {
            Utility.saveString(HomeScreen.this, "username", userName);
        }
    }

    private void checkWritePermission() {
        boolean isGranted = Utility.checkPermission(WRITE_PERMISSION, this);
        if (!isGranted) {
            Utility.requestPermission(WRITE_PERMISSION, WRITE_PERM_REQ_CODE, this);
        }
    }

    public void OnFetchParamCall(View v) {
        if(group.equals("org")){
            Log.d("Homescreen","Org Fetch Param");
            Toast.makeText(this,"Fetching Policy",Toast.LENGTH_LONG).show();
            //fetchPolicy();
            FetchUpdateAsync fetchparamasync = new FetchUpdateAsync();
            String myUrl = LoginActivity.server.concat("main.php?TYPE=get-public-key&ORGNAME=".concat(username));
            fetchparamasync.execute(myUrl);
            Toast.makeText(this,"Click Discoveree Mode To Broadcast Message",Toast.LENGTH_LONG).show();
        }else {
            Log.d("Homescreen","User Fetch Param");
            fetchPK();
            Toast.makeText(this,"Click Discoverer Mode To Receive Messages",Toast.LENGTH_LONG).show();
        }

    }

    private void showToast(String policy) {
        Log.d("Homescreen","Policy "+policy);
    }

    private void fetchPolicy() {
        String myUrl = LoginActivity.server.concat("main.php?TYPE=get-public-key&ORGNAME=".concat(username));
        //Instantiate new instance of our class
        FetchUpdateAsync fetchparamasync = new FetchUpdateAsync();
        try {
            policy = fetchparamasync.execute(myUrl).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("Homescreen","Policy "+this.policy);

    }

    private void fetchPK() {
        Toast.makeText(this,"Fetching Private key",Toast.LENGTH_LONG).show();
        String myUrl = LoginActivity.server.concat("main.php?TYPE=get-private-key&USERNAME=".concat(username).concat("&ORGNAME=test"));
        //Instantiate new instance of our class
        FetchUpdateAsync fetchparamasync = new FetchUpdateAsync();
        try {
            privatekey = fetchparamasync.execute(myUrl).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("Homescreen","Privatekey "+this.privatekey);

    }

    public void OnSearchModeCall(View v) {
        if (group.equals("org")) {
            Log.d("Homescreen", "Org Search Mode");

            Intent BroadcastActivityIntent = new Intent(HomeScreen.this, BroadcastActivity.class);
            startActivity(BroadcastActivityIntent);
            //finish();
        } else {
            Log.d("Homescreen", "user Search Mode");
            Intent ReceiverP2PSDIntent = new Intent(HomeScreen.this, ReceiverP2PSD.class);
            startActivity(ReceiverP2PSDIntent);
            //finish();
        }
    }


    public void OnLogoutCall(View v) {
        Log.d("Homescreen","Logout");
    }



    public class FetchUpdateAsync extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params){
            String stringUrl = params[0];
            String result = null;
            String inputLine;      try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);         //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();         //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();       //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());         //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();         //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }         //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();         //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            } return result;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Toast.makeText(HomeScreen.this,result,Toast.LENGTH_LONG).show();
            showToast(result);
        }
    }



}
