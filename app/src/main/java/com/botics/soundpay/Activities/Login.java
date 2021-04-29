package com.botics.soundpay.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.botics.soundpay.R;
import com.botics.soundpay.Utils.Constants;
import com.botics.soundpay.Utils.Loader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

import ru.bullyboo.encoder.Encoder;
import ru.bullyboo.encoder.methods.AES;

import static com.botics.soundpay.Utils.Constants.ALPHABET;

public class Login extends AppCompatActivity {
    EditText email,password;
    Button login;
    TextView emailError,passwordError, newAcct;
    private String encrypt;
    private String iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
        initUI();
        newAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
            }
        });
        generateIV(16);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify()){
                    Loader loader=new Loader(null);
                    loader.setCancelable(false);
                    loader.show(getSupportFragmentManager(), "");
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
                            email.getText().toString(),
                            password.getText().toString()
                    ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            loader.dismiss();
                            FirebaseFirestore db=FirebaseFirestore.getInstance();
                            db.collection("users")
                                    .document(FirebaseAuth.getInstance().getUid())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()){
                                                String balance=documentSnapshot.get("balance").toString();
                                                SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE );
                                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                                editor.putString(Constants.EMAIL, email.getText().toString());
                                                editor.putString(Constants.UID, authResult.getUser().getUid());
                                                editor.putString(Constants.CRYPTO, balance);
                                                editor.putString(Constants.IV, encodetoBase64(iv));
                                                editor.apply();
                                                startActivity(new Intent(Login.this, MainActivity.class));
                                                finish();
                                            }else{
                                                emailError.setVisibility(View.VISIBLE);
                                                emailError.setText("Something went Wrong. Please Contact Support");
                                            }
                                        }
                                    });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loader.dismiss();

                            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                passwordError.setVisibility(View.VISIBLE);
                                passwordError.setText(e.getMessage());
                            } else if (e instanceof FirebaseAuthInvalidUserException) {
                                emailError.setVisibility(View.VISIBLE);
                                emailError.setText(e.getMessage());
                            } else {
                                emailError.setVisibility(View.VISIBLE);
                                emailError.setText(e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    private void init() {
        SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE );
        String crypto=sharedPreferences.getString(Constants.CRYPTO,"");
        if (crypto!=null&&!crypto.equalsIgnoreCase("")){
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
    }

    public String encrypt(String message){
        SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE );
        encrypt = Encoder.BuilderAES()
                .message(message)
                .method(AES.Method.AES_CBC_PKCS5PADDING)
                .key("UIDSOUNPAY123456")
                .keySize(AES.Key.SIZE_128)
                .iVector(iv)
                .encrypt();
        return encrypt;
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

    private boolean verify() {
        boolean isCorrect;
        if (email.getText().toString().isEmpty()){
            emailError.setText("Email is Required");
            emailError.setVisibility(View.VISIBLE);
            isCorrect=false;
        }else  if (password.getText().toString().isEmpty()){
          isCorrect=false;
          passwordError.setText("Password is Required");
          passwordError.setVisibility(View.VISIBLE);
    }else {
            emailError.setVisibility(View.GONE);
            isCorrect=true;
        }
        return  isCorrect;
    }

    private void initUI() {
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        emailError=findViewById(R.id.email_error);
        login=findViewById(R.id.submit);
        passwordError=findViewById(R.id.password_error);
        newAcct=findViewById(R.id.new_acc);
    }
}
