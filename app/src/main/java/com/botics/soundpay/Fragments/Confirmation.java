package com.botics.soundpay.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.botics.soundpay.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Confirmation extends BottomSheetDialogFragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.confirmation, container, false);
        return view;
    }
}
