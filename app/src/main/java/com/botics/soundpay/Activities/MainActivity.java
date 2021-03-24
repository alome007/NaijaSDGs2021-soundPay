package com.botics.soundpay.Activities;

import android.content.ContentResolver;
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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.botics.soundpay.Adapters.ContactAdapter;
import com.botics.soundpay.Models.Contacts;
import com.botics.soundpay.R;
import com.botics.soundpay.Utils.Utils;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity  extends AppCompatActivity {
    TextView name,balance;
    ArrayList<Contacts> arrayList=new ArrayList<>();
    ContactAdapter adapter;
    RecyclerView recyclerView;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initData();
    }

    private void initData() {
        Contacts contacts=new Contacts("Alome", "", "https://github.com/square/picasso/raw/master/website/static/sample.png", false);
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
        recyclerView=findViewById(R.id.contactRecyclerView);
        adapter=new ContactAdapter(this, arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        name=findViewById(R.id.name);
        balance=findViewById(R.id.balance);
        Spannable span = new SpannableString(Utils.getCurrencySymbol("NGN")+"100K");
        span.setSpan(new RelativeSizeSpan(0.5f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        balance.setText(span);
        String userName="Daniel";
        String sourceString = "Hello "+"<b>" + userName + "</b> ";
        name.setText(Html.fromHtml(sourceString));
        getContactList();
//        getWindow().setStatusBarColor(Color.parseColor("#F2F7FA"));
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
}
