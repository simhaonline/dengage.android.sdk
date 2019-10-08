package com.dengage.application.dev;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.dengage.sdk.notification.dEngageMobileManager;
import com.dengage.sdk.notification.logging.Logger;
import com.dengage.sdk.notification.models.Message;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final String appAlias = "com.dengage.application.dev";
    private static dEngageMobileManager mobileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Context context = getApplicationContext();
        mobileManager = dEngageMobileManager.createInstance(appAlias, context);
        mobileManager.register();

        TextView txtEnvironment = (TextView)findViewById(R.id.txtEnvironment);
        TextView txtAlias = (TextView)findViewById(R.id.txtAlias);
        TextView txtAdvertisingId = (TextView)findViewById(R.id.txtAdvertisingId);
        TextView txtToken = (TextView)findViewById(R.id.txtToken);
        TextView txtContactKey = (TextView)findViewById(R.id.txtContactKey);

        txtEnvironment.setText(mobileManager.getEnvironment());
        txtAlias.setText(mobileManager.subscription.getAppAlias());
        txtAdvertisingId.setText(mobileManager.subscription.getUdid());
        txtToken.setText(mobileManager.subscription.getToken());
        txtContactKey.setText(mobileManager.subscription.getContactKey());

        Button btnContactKey = (Button) findViewById(R.id.btnContactKey);
        btnContactKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.Debug("Retrieving contactkey...");
                TextView txtContactKey = (TextView)findViewById(R.id.txtContactKey);
                String contactkey = txtContactKey.getText().toString();
                mobileManager.setContactKey(contactkey);
                mobileManager.sync();
                showMessage("Contact key has been successfully set.");
                Logger.Debug("Retrieved contactkey: "+ contactkey);
            }
        });

        Button btnAdvertisingId = (Button) findViewById(R.id.btnAdvertisingId);
        btnAdvertisingId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.Debug("Retrieving udid...");
                TextView txtAdvertisingId = (TextView)findViewById(R.id.txtAdvertisingId);
                txtAdvertisingId.setText("Retrieving...");
                String udid = mobileManager.subscription.getUdid();
                if(udid != "") {
                    txtAdvertisingId.setText(udid);
                } else {
                    txtAdvertisingId.setText("Not available yet. try again later.");
                }
                Logger.Debug("Retrieved udid: "+ udid);
            }
        });

        Button btnToken = (Button) findViewById(R.id.btnToken);
        btnToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.Debug("Retrieving token...");
                TextView txtToken = (TextView)findViewById(R.id.txtToken);
                txtToken.setText("Retrieving...");
                String token = mobileManager.subscription.getToken();
                if(token != "") {
                    txtToken.setText(token);
                } else {
                    txtToken.setText("Not available yet. try again later.");
                }
                Logger.Debug("Retrieved token: "+ token);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent.getExtras() != null)
            dEngageMobileManager.getInstance().open(new Message(intent.getExtras()));
    }

    private void showMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}