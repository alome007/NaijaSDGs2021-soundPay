package com.botics.soundpay.Activities;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.botics.soundpay.Adapters.ContactAdapter;
import com.botics.soundpay.Models.Contacts;
import com.botics.soundpay.R;
import com.botics.soundpay.Utils.AccountNumber;
import com.botics.soundpay.Utils.Constants;
import com.botics.soundpay.Utils.Utils;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity  extends AppCompatActivity {
    TextView name,balance;
    ArrayList<Contacts> arrayList=new ArrayList<>();
    ContactAdapter adapter;
    RecyclerView recyclerView;
    RelativeLayout fund;
    ImageView avatar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initData();
        setupProfile();
        fund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AccountNumber().show(getSupportFragmentManager(), "sss");

            }
        });
    }

    private void initData() {
        Contacts contacts=new Contacts("Alome", "", "https://github.com/square/picasso/raw/master/website/static/sample.png", false, "", "", "", "", "");
        arrayList.add(contacts);
//        contacts=new Contacts("Favour", R.drawable.fav, "https://pbs.twimg.com/profile_images/1291046814825811968/LHpCZmK7_400x400.jpg", true);
//        arrayList.add(contacts);
//        contacts=new Contacts("Albert", R.drawable.albeert, "https://pbs.twimg.com/profile_images/1369694863604785152/FU-ou5BJ_400x400.jpg", true);
//        arrayList.add(contacts);
//        contacts=new Contacts("Chike", R.drawable.daniel, "https://cdn.pixabay.com/photo/2018/05/03/00/54/blue-butterfly-flower-3370200_150.jpg", true);
//        arrayList.add(contacts);
//        contacts=new Contacts("John", R.drawable.daniel, "ddjjd", true);
//        arrayList.add(contacts);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initUI() {
        fund=findViewById(R.id.card);
        avatar=findViewById(R.id.profile_image);
        recyclerView=findViewById(R.id.contactRecyclerView);
        adapter=new ContactAdapter(this, arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        name=findViewById(R.id.name);
        balance=findViewById(R.id.balance);
        SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE );
        String crypto=sharedPreferences.getString(Constants.CRYPTO, "");
        String iv=sharedPreferences.getString(Constants.IV, "");
       Log.d("IVKEY", iv);
        String UID=sharedPreferences.getString(Constants.UID, "");
       String balanceObj= decrypt(crypto, "UIDSOUNPAY123456", iv );
        try {
            JSONObject object=new JSONObject(balanceObj);
            Spannable span = new SpannableString(Utils.getCurrencySymbol("NGN")+object.getString("balance")+"0");
            span.setSpan(new RelativeSizeSpan(0.5f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            balance.setText(span);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getContactList();
//        getWindow().setStatusBarColor(Color.parseColor("#F2F7FA"));
    }


    public void setupProfile(){
        SharedPreferences sharedPreferences=getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE );
        String UID=sharedPreferences.getString(Constants.UID, "");
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("users").document(UID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Contacts contacts=value.toObject(Contacts.class);
                String sourceString = "Hello "+"<b>" + contacts.getUsername() + "</b> ";
                name.setText(Html.fromHtml(sourceString));
                Picasso.get()
                        .load(contacts.getUrl())
                        .into(avatar);
            }
        });
        String userName="Daniel";


    }

    private void getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        Log.i("CONT", "Name: " + name);
//                        Log.i("CONT", "Phone Number: " + phoneNo);
                        checkFirebase(phoneNo.trim().replace(" ", ""), name);
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
    }

    private void checkFirebase(String phone, String name) {
        Log.d("CONT", phone.replace("+234","0"));
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        Query query=db.collection("users").whereEqualTo("number", phone).limit(8);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                arrayList.clear();
                for (DocumentSnapshot snapshot:value){
                    Contacts contacts=snapshot.toObject(Contacts.class);
                    contacts.setName(name);
                    contacts.setContact(true);
                    arrayList.add(contacts);
                    Set<Contacts> s= new HashSet<Contacts>();
                    s.addAll(arrayList);
                    arrayList = new ArrayList<Contacts>();
                    arrayList.addAll(s);

                    Log.d("CONT", new Gson().toJson(contacts));
                }
                if (arrayList.size()==1){
                    recyclerView.setVisibility(View.GONE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                }
                recyclerView.setAdapter(adapter);

            }
        });
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
}
