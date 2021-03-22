package com.botics.soundpay.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.botics.soundpay.Models.Contacts;
import com.botics.soundpay.R;

import java.util.ArrayList;

public class ContactAdapter  extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    public static int SEARCH=100;
    public static int CONTACTS=101;
    ArrayList<Contacts> arrayList=new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==SEARCH){
           View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_view_button, parent, false);
           return new ViewHolder(view);

        } else {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
            return new ViewHolder(view);
        }
    }

    public  ContactAdapter(Context context, ArrayList<Contacts> arrayList){
        this.context=context;
        this.arrayList=arrayList;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contacts model=arrayList.get(position);
        if (holder.getItemViewType()==SEARCH){

        }else {
            holder.name.setText(model.getName());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public ViewHolder(@NonNull View view) {
            super(view);
            name=view.findViewById(R.id.name);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position).isContact()){
            return CONTACTS;
        }else {
            return SEARCH;
        }
    }
}
