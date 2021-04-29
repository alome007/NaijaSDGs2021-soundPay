package com.botics.soundpay.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.botics.soundpay.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

public class profileViewer extends BottomSheetDialogFragment {
    View view;
    TextView userName;
    ImageView image;
    String name,url;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.profile_view, container, false);
        initUI();
        initData();
        return view;
    }

    private void initData() {
        userName.setText(name);
        Picasso.get()
                .load(url)
                .into(image);
    }

    private void initUI() {
        userName=view.findViewById(R.id.name);
        image=view.findViewById(R.id.profile_image);
    }

    public profileViewer(String name, String url){
        this.name=name;
        this.url=url;
    }
}
