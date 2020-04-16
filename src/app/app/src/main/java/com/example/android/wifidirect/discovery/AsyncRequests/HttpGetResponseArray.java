package com.example.android.wifidirect.discovery.AsyncRequests;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HttpGetResponseArray extends AsyncTask<String, Void, ArrayList<String>> {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    @Override
    protected ArrayList<String> doInBackground(String... params){
        String stringUrl = params[0];
        ArrayList<String> result = new ArrayList<String>();
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
            while((inputLine = reader.readLine()) != null){
                StringBuilder stringBuilder = new StringBuilder();
                result.add(stringBuilder.append(inputLine).toString());

            }         //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();         //Set our result equal to our stringBuilder
        }
        catch(IOException e){
            e.printStackTrace();
            result = null;
        } return result;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result){
        super.onPostExecute(result);
    }
}
