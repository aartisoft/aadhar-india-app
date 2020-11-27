package com.tailwebs.aadharindia.loginandforgot;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.models.forgotpassword.ForgotPassword;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitAPIClient;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.send_link_button)
    Button sendLinkButton;

    @BindView(R.id.input_layout_mobile)
    TextInputLayout mobileLayout;

    @BindView(R.id.input_mobile)
    TextInputEditText mobileET;

    private boolean isValidMobile = false;
    private ProgressDialog mProgressDialog;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + "Forgot Password", null);


        sendLinkButton.setOnClickListener(this);
        mobileET.addTextChangedListener(new InputLayoutTextWatcher(mobileET));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_link_button:
                if (NetworkUtils.haveNetworkConnection(ForgotPasswordActivity.this)) {
                    sendMobileBtnForForgotPassword();
                } else {
                    UiUtils.showAlertDialogWithOKButton(ForgotPasswordActivity.this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            finish();
                        }
                    });
                }
                break;
        }
    }

    private void sendMobileBtnForForgotPassword() {
        if (isValidMobile == false ) {

            UiUtils.checkValidation(ForgotPasswordActivity.this, mobileET, mobileLayout, new ArrayList<String>());


        } else {
            sendMobileBtnClick();
        }
    }


    public void showProgressDialog(Context context) {
        if (!((Activity) context).isFinishing()) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(context, R.style.DialogCustom);
                mProgressDialog.setMessage(context.getString(R.string.loading));
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
        }

    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    private void sendMobileBtnClick() {
        showProgressDialog(ForgotPasswordActivity.this);
        try{
            ApiInterface apiService = RetrofitAPIClient.getClient().create(ApiInterface.class);

            Call<ForgotPassword> call = apiService.doForgotPassword(mobileET.getText().toString().trim());
            call.enqueue(new Callback<ForgotPassword>() {
                @Override
                public void onResponse(Call<ForgotPassword> call, final Response<ForgotPassword> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            goToForgotPasswordSuccess(response.body().getNotice());
//                            UiUtils.showAlertDialogWithOKButton(ForgotPasswordActivity.this, response.body().getNotice(), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.cancel();
//                                    mobileET.setText("");
//                                    mobileLayout.setErrorEnabled(false);
//                                    sendLinkButton.setEnabled(false);
//
//
//                                }
//                            });
                        } else {
                            NetworkUtils.handleErrorsForAPICalls(ForgotPasswordActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {

                        NetworkUtils.handleErrorsForAPICalls(ForgotPasswordActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<ForgotPassword> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(ForgotPasswordActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void goToForgotPasswordSuccess(String notice) {

        finish();

        Intent intent = new Intent(ForgotPasswordActivity.this,ForgotPasswordSuccessActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("message",notice);
                intent.putExtras(bundle);
                startActivity(intent);
    }

    private class InputLayoutTextWatcher implements TextWatcher {

        private View view;
        ArrayList<String> itemPassed = new ArrayList<String>();

        private InputLayoutTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            sendLinkButton.setEnabled(false);

            switch (view.getId()) {
                case R.id.input_mobile:
                    itemPassed.clear();
                    itemPassed.add("mobile");

                    boolean mobileStatus = UiUtils.checkValidation(ForgotPasswordActivity.this, mobileET, mobileLayout, itemPassed);

                    if (mobileStatus == false) {
                        isValidMobile = false;
                        requestFocus(mobileET);
                    } else {
                        isValidMobile = true;
                        if (isValidMobile ) {
                            sendLinkButton.setEnabled(true);
                        }
                    }
                    break;

            }
        }

    }

    public void requestFocus(View view) {
        if (view.requestFocus())
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}
