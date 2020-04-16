package com.example.android.wifidirect.discovery;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.wifidirect.discovery.AsyncRequests.HttpGetRequest;
import com.example.android.wifidirect.discovery.AsyncRequests.HttpGetResponseArray;
import com.example.android.wifidirect.discovery.D2DSec.PrivateKey;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String[] users = { "Customer", "Organization" };
    String type = null;
    public final static String server = "http://192.168.50.78/DNETS/DNETS/";
    public ArrayList<String> mpk;
    public static final String CURVE_FILE_PATH = "assets/a.properties";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        //final ProgressBar loadingProgressBar = findViewById(R.id.loading);


        String myUrl = server.concat("main.php?TYPE=get-mpk");

        //String to place our result in
         mpk = new ArrayList<String>();

        //Instantiate new instance of our class
        HttpGetResponseArray getmpk = new HttpGetResponseArray();

        //Perform the doInBackground method, passing in our url
        try {
            mpk = getmpk.execute(myUrl).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("login","mpk size "+ mpk.size());

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadingProgressBar.setVisibility(View.VISIBLE);
                loginVerify(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), type );
            }
        });
    }

    private void loginVerify(String username, String password, String group) {
        //String myUrl = null;
        //Some url endpoint that you may have

        String myUrl = server.concat("main.php?TYPE=login&GROUP="+group+"&USERNAME="+username+"&PASSWORD="+password);

        //String to place our result in
        String result = null;

        //Instantiate new instance of our class
        HttpGetRequest getRequest = new HttpGetRequest();

        //Perform the doInBackground method, passing in our url
        try {
            result = getRequest.execute(myUrl).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("login",result);

        //group = "org";

        assert result != null;
        boolean test = false;
        if(result.equals("pass")|| test){
            Intent HomescreenIntent = new Intent(LoginActivity.this,HomeScreen.class);
            HomescreenIntent.putExtra("username",username);
            HomescreenIntent.putExtra("group",group);
            HomescreenIntent.putExtra("mpk",mpk);
            startActivity(HomescreenIntent);
            updateUiWithUser(username);
            finish();
        }else {
            showLoginFailed("Login Failed !!");
        }

//        if(username.equals("admin") && password.equals("admin")){
//            updateUiWithUser(username);
//            Intent HomescreenIntent = new Intent(LoginActivity.this,HomeScreen.class);
//            startActivity(HomescreenIntent);
//            finish();
//        }else {
//            showLoginFailed("Login Failed !!");
//        }
    }

    private void updateUiWithUser(String username) {
        String welcome =  getString(R.string.welcome) + username;
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Selected User: "+ users[position] ,Toast.LENGTH_SHORT).show();
        if(users[position].equals("Customer")){
            type = "user";
        }else{
            type = "org";
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
