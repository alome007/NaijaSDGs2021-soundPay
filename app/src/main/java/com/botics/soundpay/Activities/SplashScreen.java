 package com.botics.soundpay.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.botics.soundpay.R;
import com.botics.soundpay.Utils.Constants;

import ru.bullyboo.encoder.Encoder;
import ru.bullyboo.encoder.methods.AES;

 public class SplashScreen  extends AppCompatActivity {
     String  encrypt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
//        String en=encrypt("DanielALomeeee");
//        Log.d("Encrypt", en);

//        SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE );
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        editor.clear().apply();

//        String iv=sharedPreferences.getString(Constants.IV,"");
//        String uid=sharedPreferences.getString(Constants.UID,"");
//        String crypto=  encrypt("{balance:"+0.00+", uid:"+uid+"}");
//        editor.putString(Constants.CRYPTO, crypto);
//        editor.putString(Constants.IV, encodetoBase64(iv));
//        editor.apply();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, Registration.class));
                finish();
            }
        },1500);
    }
     public String encrypt(String message){
         SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE );
         String UID=sharedPreferences.getString(Constants.UID, "");
         String iv=sharedPreferences.getString(Constants.IV, "");
         encrypt = Encoder.BuilderAES()
                 .message(message)
                 .method(AES.Method.AES_CBC_PKCS5PADDING)
                 .key("UIDSOUNPAY123456")
                 .keySize(AES.Key.SIZE_128)
                 .iVector("dksjsieh5jnej209")
                 .encrypt();
         return encrypt;
     }

     public String encodetoBase64(String  key){
         String base64 = Base64.encodeToString(key.getBytes(), Base64.DEFAULT);
         return base64;
     }
}
