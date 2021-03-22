package com.botics.soundpay.Activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
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

import java.util.ArrayList;

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
        Contacts contacts=new Contacts("Alome", "", "", false);
        arrayList.add(contacts);
        contacts=new Contacts("Sir Tolu", "", "", true);
        arrayList.add(contacts);
        contacts=new Contacts("Favour", "", "", true);
        arrayList.add(contacts);
        contacts=new Contacts("Chike", "", "", true);
        arrayList.add(contacts);
        contacts=new Contacts("John", "", "", true);
        arrayList.add(contacts);
        recyclerView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initUI() {
        recyclerView=findViewById(R.id.contactRecyclerView);
        adapter=new ContactAdapter(this, arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        name=findViewById(R.id.name);
        balance=findViewById(R.id.balance);
        Spannable span = new SpannableString(Utils.getCurrencySymbol("NGN")+"0.00");
        span.setSpan(new RelativeSizeSpan(0.5f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        balance.setText(span);
        String userName="Daniel";
        String sourceString = "Hello "+"<b>" + userName + "</b> ";
        name.setText(Html.fromHtml(sourceString));
//        getWindow().setStatusBarColor(Color.parseColor("#F2F7FA"));
    }
}
