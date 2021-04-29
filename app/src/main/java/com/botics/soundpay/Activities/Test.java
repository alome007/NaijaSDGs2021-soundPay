package com.botics.soundpay.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.botics.soundpay.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import org.quietmodem.Quiet.FrameTransmitter;
import org.quietmodem.Quiet.FrameTransmitterConfig;
import org.quietmodem.Quiet.ModemException;

import java.io.IOException;

public class Test extends AppCompatActivity {

    private FrameTransmitter transmitter;
    private EditText sendMessage;
    private Spinner profileSpinner;
    private ArrayAdapter<String> spinnerArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmit);
//        profileSpinner=findViewById(R.id.profile);
//        findViewById(R.id.transmit).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handleSendClick();
//            }
//        });
//
//        sendMessage = (EditText) findViewById(R.id.message);
//        setupProfileSpinner();
//        setupTransmitter();
//        handleDataFromIntent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (transmitter != null) {
//            transmitter.close();
//        }
    }


    private void setupTransmitter() {
        FrameTransmitterConfig transmitterConfig;
        try {
            transmitterConfig = new FrameTransmitterConfig(
                    Test.this,"audible");
            transmitter = new FrameTransmitter(transmitterConfig);
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        } catch (ModemException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            throw new RuntimeException(e);
        }
    }

    private void handleSendClick() {
        if (transmitter == null) {
            setupTransmitter();
        }else{
            send();

        }
    }

    private void send() {
        String payload = sendMessage.getText().toString();
        try {
            transmitter.send(payload.getBytes());
        } catch (IOException e) {
            Log.d("Errororo", e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            // our message might be too long or the transmit queue full
        }
    }

    private void setupProfileSpinner() {
        spinnerArrayAdapter = ProfilesHelper.createArrayAdapter(this);
        profileSpinner.setAdapter(spinnerArrayAdapter);
        profileSpinner.setSelection(0, false);
        profileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transmitter = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private String getProfile() {
        String profile = spinnerArrayAdapter.getItem(profileSpinner.getSelectedItemPosition());
        return profile;
    }

    private void handleDataFromIntent() {

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                sendMessage.setText(sharedText);
            }
        }

    }
}
