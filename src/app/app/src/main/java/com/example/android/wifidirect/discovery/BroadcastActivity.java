package com.example.android.wifidirect.discovery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BroadcastActivity extends AppCompatActivity {

    public Button broadcast_button;
    public EditText etBroadcastMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        broadcast_button = findViewById(R.id.broadcast_button);
        etBroadcastMessage = findViewById(R.id.et_broadcast_message);

        broadcast_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadingProgressBar.setVisibility(View.VISIBLE);
                String message = etBroadcastMessage.getText().toString();
                Intent BroadcasterP2PSDIntent = new Intent(BroadcastActivity.this, BroadcasterP2PSD.class);
                BroadcasterP2PSDIntent.putExtra("broadcast_message",message);
                startActivity(BroadcasterP2PSDIntent);
                //finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
