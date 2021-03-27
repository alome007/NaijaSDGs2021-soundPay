package com.botics.soundpay.Utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.botics.soundpay.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AccountNumber extends BottomSheetDialogFragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.account_number, container, false);
        return view;
    }
}
