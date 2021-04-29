package com.botics.soundpay.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.botics.soundpay.R;
import com.botics.soundpay.Utils.Constants;
import com.botics.soundpay.Utils.Loader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import ru.bullyboo.encoder.Encoder;
import ru.bullyboo.encoder.methods.AES;

import static com.botics.soundpay.Utils.Constants.ALPHABET;

public class BvnUpdate  extends AppCompatActivity {
    EditText fullName,Bvn,Phone;
    TextView phoneError, nameError, bvnError;
    Button proceed;
    boolean phoneNumbertaken;
    private String encrypt,iv,uid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_virtual_account);
        initUI();
        generateIV(16);
        Phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkPhone(s.toString(), phoneError);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPhone(Phone.getText().toString(),phoneError);
                if (verify()){

                    SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE );
                    String email=sharedPreferences.getString(Constants.EMAIL,"");
                     uid=sharedPreferences.getString(Constants.UID,"");
                    JsonObject object=new JsonObject();

                    Loader loader=new Loader(null);
                    loader.setCancelable(false);
                    loader.show(getSupportFragmentManager(), "");



                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.BASE_URL+"account/create",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
Log.d("Crypto", response);
                                    loader.dismiss();

                                    if (response==null){

                                    }else {
                                        Loader loader = new Loader(null);
                                        loader.setCancelable(false);
                                        loader.show(getSupportFragmentManager(), "");
                                        JSONObject result ;
                                        Log.d("Crypto", encrypt("{\"uid\":\""+ FirebaseAuth.getInstance().getCurrentUser().getUid() +"\",\"balance\":"+1000.00+"}"));
                                        try {
                                            result = new JSONObject(response);
                                            if (result.getString("status").equalsIgnoreCase("success")){
                                                String bal = encrypt("{\"uid\":\""+uid+"\",\"balance\":"+1000.00+"}");
                                                Log.d("Crypto", "I got here");
                                                String accountNumber=result.getJSONObject("data").getString("account_number");
                                                FirebaseFirestore db=FirebaseFirestore.getInstance();
                                                db.collection("users").document(FirebaseAuth.getInstance().getUid())
                                                        .update("name", fullName.getText().toString(),
                                                                "accountNumber", accountNumber, "number", Phone.getText().toString(), "balance", bal)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                loader.dismiss();
                                                                SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE );
                                                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                                                editor.putString(Constants.ACCOUNT_NUMBER, accountNumber);
                                                                editor.putString(Constants.NUMBER, Phone.getText().toString());
                                                                String crypto=  encrypt("{\"uid\":\""+uid+"\",\"balance\":"+1000.00+"}");
                                                                editor.putString(Constants.CRYPTO, crypto);
                                                                editor.putString(Constants.IV, encodetoBase64(iv));
                                                                editor.apply();
                                                                startActivity(new Intent(BvnUpdate.this, MainActivity.class));
                                                                finish();
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("Errrr", e.getMessage());
                                                        loader.dismiss();
                                                    }
                                                });
                                            }else{
                                                loader.dismiss();
                                            }
                                        } catch (JSONException e) {
                                            Log.d("Errrr", e.getMessage());
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> parmas = new HashMap<>();

                            //here we pass params
                            parmas.put("firstname",fullName.getText().toString().split(" ")[0]);
                            parmas.put("lastname",fullName.getText().toString().split(" ")[1]);
                            parmas.put("phoneNumber",Phone.getText().toString());
                            parmas.put("bvn",Bvn.getText().toString());
                            parmas.put("email",email);
                            parmas.put("uid",uid);

                            return parmas;
                        }
                    };

                    int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

                    RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    stringRequest.setRetryPolicy(retryPolicy);

                    RequestQueue queue = Volley.newRequestQueue(BvnUpdate.this);

                    queue.add(stringRequest);
                }
            }
        });
    }

    private boolean verify() {
        boolean verified;
        if (fullName.getText().toString().isEmpty()){
            verified=false;
            nameError.setVisibility(View.VISIBLE);
            nameError.setText("Name is Required");
        }
        if (Bvn.getText().toString().isEmpty()){
            verified=false;
            bvnError.setVisibility(View.VISIBLE);
            bvnError.setText("Name is Required");
        }
        if (Phone.getText().toString().isEmpty()){
            verified=false;
            phoneError.setVisibility(View.VISIBLE);
            phoneError.setText("Name is Required");
        }
        else {
            nameError.setVisibility(View.GONE);
            bvnError.setVisibility(View.GONE);
            phoneError.setVisibility(View.GONE);
            verified=true;
        }
        return verified;
    }

    private void initUI() {
        fullName=findViewById(R.id.full_name);
        Bvn=findViewById(R.id.bvn);
        Phone=findViewById(R.id.phone);
        proceed=findViewById(R.id.submit);
        phoneError=findViewById(R.id.phone_error);
        bvnError=findViewById(R.id.bvn_error);
        nameError=findViewById(R.id.name_error);
    }

    public void checkPhone(String phone, TextView error){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("number", phone).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        if (snapshots.isEmpty()){
                            phoneNumbertaken=false;
                            phoneError.setVisibility(View.GONE);

                        }else {
                            phoneError.setVisibility(View.VISIBLE);
                            phoneNumbertaken=true;
                            error.setText("Phone number taken, please try another");
                        }
                    }
                });
    }



    public String encrypt(String message){
        SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE );
        String UID=sharedPreferences.getString(Constants.UID, "");
        encrypt = Encoder.BuilderAES()
                .message(message)
                .method(AES.Method.AES_CBC_PKCS5PADDING)
                .key("UIDSOUNPAY123456")
                .keySize(AES.Key.SIZE_128)
                .iVector(iv)
                .encrypt();
        return encrypt;
    }
    public static String decrypt(String encrypted, String key, String initVector) {
        try {
            byte[] decodedString = Base64.decode(initVector.getBytes(), Base64.DEFAULT);
            IvParameterSpec iv = new IvParameterSpec(decodedString);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decode(encrypted.getBytes(),Base64.DEFAULT));
            Log.i("Crypto", "decrypt: "+ new String(original));
            return new String(original);
        } catch (Exception ex) {
            Log.e("Error", "decrypt: " +ex.getMessage() );
            ex.printStackTrace();
        }

        return null;
    }

    public String encodetoBase64(String  key){
        String base64 = Base64.encodeToString(key.getBytes(), Base64.DEFAULT);
        return base64;
    }
    public String generateIV(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            builder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        iv=builder.toString();
        return iv;
    }
}
