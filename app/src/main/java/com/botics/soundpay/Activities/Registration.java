package com.botics.soundpay.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.botics.soundpay.R;

public class Registration  extends AppCompatActivity {
    Button register;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        initUI();
        getWindow().setStatusBarColor(Color.parseColor("#A39D9D"));
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, MainActivity.class));
            }
        });
    }

    private void initUI() {
        register=findViewById(R.id.register);
    }
}
