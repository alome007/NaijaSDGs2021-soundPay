package com.botics.soundpay.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.botics.soundpay.Models.Contacts;
import com.botics.soundpay.Models.HistoryModel;
import com.botics.soundpay.R;
import com.botics.soundpay.Utils.Utils;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    ArrayList<HistoryModel> arrayList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_helper, parent, false);
        return  new ViewHolder(view);
    }

    public HistoryAdapter(Context context, ArrayList<HistoryModel> arrayList){
        this.context=context;
        this.arrayList=arrayList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryModel model=arrayList.get(position);
        getName(holder.name, model.getUid());
        if (model.getType().equalsIgnoreCase("debit")){
            holder.amount.setText("-"+Utils.getCurrencySymbol("NGN")+model.getAmount());
        } else{
            holder.amount.setText("+"+Utils.getCurrencySymbol("NGN")+model.getAmount());

        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd, H:mm aa");
        String formatted = formatter.format(System.currentTimeMillis());
        System.out.println(formatted);
        holder.date.setText(formatted);


    }

    private void getName(TextView name, String uid) {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("users").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error==null){
//                    Contacts contacts=value.toObject(Contacts.class);
//                    name.setText(contacts.getUsername());
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder  extends  RecyclerView.ViewHolder{
        TextView name,amount,date;
        ImageView avatar;
        public ViewHolder(@NonNull View view) {
            super(view);
            name=view.findViewById(R.id.name);
            amount=view.findViewById(R.id.amount);
            avatar=view.findViewById(R.id.avatar);
            date=view.findViewById(R.id.date);
        }
    }
}
