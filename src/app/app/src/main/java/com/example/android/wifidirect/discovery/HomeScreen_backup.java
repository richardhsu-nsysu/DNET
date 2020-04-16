package com.example.android.wifidirect.discovery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.wifidirect.discovery.D2DSec.COABE;
import com.example.android.wifidirect.discovery.notification.NotificationToast;
import com.example.android.wifidirect.discovery.utils.Utility;

import java.nio.charset.Charset;

import it.unisa.dia.gas.jpbc.Element;

public class HomeScreen_backup extends AppCompatActivity {

    public static final String WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int WRITE_PERM_REQ_CODE = 19;
    public static final String CURVE_FILE_PATH = "a.properties";
    public static COABE coabe;
    public  static  Element[][] xy_vec;
    public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    EditText etUsername;
    TextView tvPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        etUsername = (EditText) findViewById(R.id.et_home_player_name);

        initializeSecurityParam();
        String userNameHint = getString(R.string.enter_name_hint) + "(default = " + Build
                .MANUFACTURER + ")";
        etUsername.setHint(userNameHint);
        checkWritePermission();

    }

    private void initializeSecurityParam()
    {
        coabe = new COABE(0,CURVE_FILE_PATH);
        xy_vec = coabe.genXYVec(160, 20);
        coabe.genPairingParameters();
        coabe.setNumAttType(20);
        coabe.setup(512);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            NotificationToast.showToast(HomeScreen_backup.this, "This permission is needed for " +
                    "file sharing. But Whatever, if that's what you want...!!!");
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    private void saveUsername() {
        String userName = etUsername.getText().toString();
        if (userName != null && userName.trim().length() > 0) {
            Utility.saveString(HomeScreen_backup.this, "username", userName);
        }
    }

    private void checkWritePermission() {
        boolean isGranted = Utility.checkPermission(WRITE_PERMISSION, this);
        if (!isGranted) {
            Utility.requestPermission(WRITE_PERMISSION, WRITE_PERM_REQ_CODE, this);
        }
    }




    public void startWiFiDirectServiceBroadcast(View v) {
        if (Utility.isWiFiEnabled(HomeScreen_backup.this)) {
            saveUsername();
            Intent wifiDirectServiceDiscoveryIntent = new Intent(HomeScreen_backup.this, BroadcasterP2PSD.class);
            startActivity(wifiDirectServiceDiscoveryIntent);
            finish();
        } else {
            NotificationToast.showToast(HomeScreen_backup.this, getString(R.string
                    .wifi_not_enabled_error));
        }
    }

    public void startWiFiDirectServiceDiscovery(View v) {
        if (Utility.isWiFiEnabled(HomeScreen_backup.this)) {
            saveUsername();
            Intent wifiDirectServiceDiscoveryIntent = new Intent(HomeScreen_backup.this, ReceiverP2PSD.class);
            startActivity(wifiDirectServiceDiscoveryIntent);
            finish();
        } else {
            NotificationToast.showToast(HomeScreen_backup.this, getString(R.string
                    .wifi_not_enabled_error));
        }
    }
}
