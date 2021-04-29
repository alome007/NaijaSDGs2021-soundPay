package com.botics.soundpay.Utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.botics.soundpay.R;

public class Loader extends DialogFragment {
    View view;
    String text;
    TextView txt;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.loader, container, false);
        txt=view.findViewById(R.id.txt);
        if (text!=null){
            txt.setVisibility(View.VISIBLE);
            txt.setText(text);
        }
        return view;
    }

    public Loader(String text){
        this.text=text;
    }
}
