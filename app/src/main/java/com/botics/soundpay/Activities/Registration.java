package com.botics.soundpay.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.botics.soundpay.Models.Contacts;
import com.botics.soundpay.R;
import com.botics.soundpay.Utils.Constants;
import com.botics.soundpay.Utils.Loader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.hash.Hashing;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Registration  extends AppCompatActivity {
    Button register;
    boolean usernameTaken,emailTaken;
    EditText username,email,password;
    TextView usernameError,emailError, passwordError;
    LinearLayout uploadPicture;
    static int SELECT_PHOTO=100;
    ImageView placeholder,image;
    Uri selected_photo;
    String url;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        init();
        initUI();
        initTextWatcher();

        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        getWindow().setStatusBarColor(Color.parseColor("#A39D9D"));
        register.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (verify()){
                    checkEmail(email.getText().toString(), emailError);
                    checkUsername(username.getText().toString(), usernameError);
                    if (usernameTaken||emailTaken){
                        if (usernameTaken){
                            usernameError.setVisibility(View.VISIBLE);
                            usernameError.setText("Username taken, please try another");
                        }

                        if (emailTaken){
                            emailError.setVisibility(View.VISIBLE);
                            emailError.setText("Email taken, please try another");
                        }
                    }else{
                        Loader loader=new Loader();
                        loader.setCancelable(false);
                        loader.show(getSupportFragmentManager(), "");
                        if (selected_photo==null){
                            Contacts contacts=new Contacts("", "", "https://github.com/square/picasso/raw/master/website/static/sample.png", false, email.getText().toString(), generateUID(username.getText().toString(), email.getText().toString()), username.getText().toString(), HashPassword(password.getText().toString()), "");
                            FirebaseFirestore db=FirebaseFirestore.getInstance();
                            db.collection("users").document(generateUID(username.getText().toString(), email.getText().toString())).set(contacts).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loader.dismiss();
                                    SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE );
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    editor.putString(Constants.EMAIL, email.getText().toString());
                                    editor.putString(Constants.UID, generateUID(username.getText().toString(), email.getText().toString()));
                                    editor.apply();
                                    startActivity(new Intent(Registration.this, BvnUpdate.class));
                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loader.dismiss();
                                }
                            });
                        }else{
                            uploadAndProceed(loader);
                        }
                    }


                }

            }
        });
    }

    private void init() {
        SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE );
        String crypto=sharedPreferences.getString(Constants.CRYPTO,"");
        if (crypto!=null&&!crypto.equalsIgnoreCase("")){
            startActivity(new Intent(Registration.this, MainActivity.class));
            finish();
        }
    }

    private void uploadAndProceed(Loader loader) {
        if (selected_photo != null) {
            //displaying a progress dialog while upload is going on

            StorageReference storageReference= FirebaseStorage.getInstance().getReference();
            StorageReference riversRef = storageReference.child("Users/Documents/"+generateUID(username.getText().toString(), email.getText().toString())+ "/Files/"+ "profile_pic"+".jpg");

            riversRef.putFile(selected_photo)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onSuccess(Uri uri) {
                                   loader.dismiss();
                                    url=uri.toString();
                                    Contacts contacts=new Contacts("", "", url, false, email.getText().toString(), generateUID(username.getText().toString(), email.getText().toString()), username.getText().toString(), HashPassword(password.getText().toString()), "");
                                    FirebaseFirestore db=FirebaseFirestore.getInstance();
                                    db.collection("users").document(generateUID(username.getText().toString(), email.getText().toString())).set(contacts).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            loader.dismiss();
                                            SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE );
                                            SharedPreferences.Editor editor=sharedPreferences.edit();
                                            editor.putString(Constants.EMAIL, email.getText().toString());
                                            editor.putString(Constants.UID, generateUID(username.getText().toString(), email.getText().toString()));
                                            editor.apply();
                                            startActivity(new Intent(Registration.this, BvnUpdate.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            loader.dismiss();
                                        }
                                    });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                           loader.dismiss();

                            //and displaying error message
                            Toast.makeText(Registration.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
//                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
    }
    }

    private boolean verify() {
        boolean verified;
        if (email.getText().toString().isEmpty()){
            verified=false;
            emailError.setVisibility(View.VISIBLE);
            emailError.setText("Email is Required");
        } if (username.getText().toString().isEmpty()){
            verified=false;
            usernameError.setVisibility(View.VISIBLE);
            usernameError.setText("Username is Required");
        }   if (password.getText().toString().isEmpty()){
            verified=false;
            passwordError.setVisibility(View.VISIBLE);
            passwordError.setText("Password is Required");
        }else{
            emailError.setVisibility(View.GONE);
            usernameError.setVisibility(View.GONE);
            passwordError.setVisibility(View.GONE);
            verified=true;
        }
        return verified;
    }

    private void initTextWatcher() {
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkUsername(s.toString(), usernameError);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkEmail(s.toString(), emailError);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkEmail(String toString, TextView emailError) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("email", toString).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        if (snapshots.isEmpty()){
                            emailTaken=false;
                            emailError.setVisibility(View.GONE);

                        }else {
                            emailError.setVisibility(View.VISIBLE);
                            emailTaken=true;
                            emailError.setText("Email taken, please try another");
                        }
                    }
                });

    }

    private void initUI() {
        register=findViewById(R.id.register);
        username=findViewById(R.id.username);
        usernameError=findViewById(R.id.user_error);
        email=findViewById(R.id.email);
        emailError=findViewById(R.id.email_error);
        uploadPicture=findViewById(R.id.upload_picture);
        image=findViewById(R.id.img);
        placeholder=findViewById(R.id.img_placeholder);
        password=findViewById(R.id.password);
        passwordError=findViewById(R.id.password_error);
    }

    public void checkUsername(String username, TextView error){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("username", username).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        if (snapshots.isEmpty()){
                            usernameTaken=false;
                            usernameError.setVisibility(View.GONE);

                        }else {
                            usernameError.setVisibility(View.VISIBLE);
                            usernameTaken=true;
                            error.setText("Username taken, please try another");
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==SELECT_PHOTO&&data!=null){
            placeholder.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
            image.setImageURI(data.getData());
            selected_photo=data.getData();
        }


    }

    public String generateUID(String username, String email) {
    return  username.substring(0, 3)+email.substring(0, 5);
}

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String HashPassword(String val) {
        String sha256hex = Hashing.sha256()
                .hashString(val, StandardCharsets.UTF_8)
                .toString();
        Log.d("HASH", "ConvertedPinAndWalletId: "+sha256hex);
        return sha256hex;
    }
}
