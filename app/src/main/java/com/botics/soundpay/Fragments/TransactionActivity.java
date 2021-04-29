package com.botics.soundpay.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.botics.soundpay.Fragments.Confirmation;
import com.botics.soundpay.R;
import com.botics.soundpay.Utils.Loader;
import com.botics.soundpay.Utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;



public class TransactionActivity  extends BottomSheetDialogFragment implements View.OnClickListener,
        View.OnLongClickListener{
    private EditText mPhoneNumberField;
    private TextView mOneTextView;
    private TextView mTwoTextView;
    private TextView mThreeTextView;
    private TextView mFourTextView;
    private TextView mFiveTextView;
    private TextView mSixTextView;
    private TextView mSevenTextView;
    private TextView mEightTextView;
    String CHIRP_APP_KEY = "F33eB46c707F4CC38Ecc6BD27";
    String CHIRP_APP_SECRET = "FfFe7fEBdA0b165005258448B52Da74bb4B29EFFD6BEaf2b84";
    String CHIRP_APP_CONFIG = "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";
    String TAG = "ChirpSDKDemoApp";
    LinearLayout ln;
    Button con;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private String key,iv;
    private Context context;
    ImageView qr;
    EditText name,amount;
    private static final int RESULT_REQUEST_RECORD_AUDIO = 1;
    private TextView mNineTextView;
    private TextView mZeroTextView;
    private TextView mZeroTextView_d;
    private ImageView deleteImageView;
    Button send,receive;
    TextView balance;
    private static final int DURATION = 50; // Vibrate duration
View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.transcations, container, false);
        initUI();

        return  view;
    }



    private void initUI() {
        initializeViews();
        setClickListeners();
        mPhoneNumberField.setHint(Utils.getCurrencySymbol("NGN")+"0.00");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Loader("Waiting for Receiver").show(getFragmentManager(), "");
            }
        });
    }

    private void setClickListeners() {
        mOneTextView.setOnClickListener(this);
        mTwoTextView.setOnClickListener(this);
        mThreeTextView.setOnClickListener(this);
        mFourTextView.setOnClickListener(this);
        mFiveTextView.setOnClickListener(this);
        mSixTextView.setOnClickListener(this);
        mSevenTextView.setOnClickListener(this);
        mEightTextView.setOnClickListener(this);
        mNineTextView.setOnClickListener(this);
        deleteImageView.setOnClickListener(this);
        mZeroTextView.setOnClickListener(this);
        mZeroTextView_d.setOnClickListener(this);
    }

    private void initializeViews() {
        mPhoneNumberField = (EditText) view.findViewById(R.id.amount);
        mPhoneNumberField.setInputType(android.text.InputType.TYPE_NULL);
        send=view.findViewById(R.id.send);
        mOneTextView =  view.findViewById(R.id.one);
        mTwoTextView =  view.findViewById(R.id.two);
        mThreeTextView =  view.findViewById(R.id.three);
        mFourTextView =  view.findViewById(R.id.four);
        mFiveTextView =  view.findViewById(R.id.five);
        mSixTextView =  view.findViewById(R.id.six);
        mSevenTextView =  view.findViewById(R.id.seven);
        mEightTextView =  view.findViewById(R.id.eight);
        mNineTextView =  view.findViewById(R.id.nine);
        mZeroTextView =  view.findViewById(R.id.zero);
        mZeroTextView_d =  view.findViewById(R.id.zero_d);
        deleteImageView =  view.findViewById(R.id.del);
        balance=view.findViewById(R.id.balance);
        balance.setText(Utils.getCurrencySymbol("NGN")+"100.00");
    }

    private void keyPressed(int keyCode) {
//        mVibrator.vibrate(DURATION);
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        mPhoneNumberField.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.one: {
                keyPressed(KeyEvent.KEYCODE_1);
                return;
            }
            case R.id.two: {
                keyPressed(KeyEvent.KEYCODE_2);
                return;
            }
            case R.id.three: {
                keyPressed(KeyEvent.KEYCODE_3);
                return;
            }
            case R.id.four: {
                keyPressed(KeyEvent.KEYCODE_4);
                return;
            }
            case R.id.five: {
                keyPressed(KeyEvent.KEYCODE_5);
                return;
            }
            case R.id.six: {
                keyPressed(KeyEvent.KEYCODE_6);
                return;
            }
            case R.id.seven: {
                keyPressed(KeyEvent.KEYCODE_7);
                return;
            }
            case R.id.eight: {
                keyPressed(KeyEvent.KEYCODE_8);
                return;
            }
            case R.id.nine: {
                keyPressed(KeyEvent.KEYCODE_9);
                return;
            }
            case R.id.zero: {
                keyPressed(KeyEvent.KEYCODE_0);
                return;
            }

            case R.id.zero_d: {
                keyPressed(KeyEvent.KEYCODE_0);
                keyPressed(KeyEvent.KEYCODE_0);
                return;
            }



            case R.id.del: {
                keyPressed(KeyEvent.KEYCODE_DEL);
                return;
            }


        }

    }

    /**
     * Long Click Listener
     */
    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.del: {
                Editable digits = mPhoneNumberField.getText();
                digits.clear();
                return true;
            }
            case R.id.zero: {
                keyPressed(KeyEvent.KEYCODE_PLUS);
                return true;
            }
        }
        return false;
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return  dialog;
    }

}
