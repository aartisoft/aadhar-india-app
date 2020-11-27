package com.tailwebs.aadharindia.loginandforgot;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.tailwebs.aadharindia.DashboardActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.home.dashboard.TaskDashboardActivity;
import com.tailwebs.aadharindia.models.login.Login;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.Constants;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.SharedPreferenceUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener ,
        OnOtpCompletionListener {

    @BindView(R.id.new_password_otp_view)
    OtpView newPasswordOTPView;

    @BindView(R.id.confirm_password_otp_view)
    OtpView confirmPasswordOTPView;

    @BindView(R.id.next_button)
    Button nextButton;

    @BindView(R.id.new_password_error_tv)
    TextView newPasswordErrorTV;

    @BindView(R.id.confirm_password_error_tv)
    TextView confirmPasswordErrorTV;



    String oldPassword;
    private boolean isValidPwd = false, isValidCPwd = false;
    private ProgressDialog mProgressDialog;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        GlobalValue.secret = SharedPreferenceUtils.getValue(ResetPasswordActivity.this, Constants.KEY_SECRET);
        GlobalValue.secret_id = SharedPreferenceUtils.getValue(ResetPasswordActivity.this, Constants.KEY_SECRET_ID);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + "Reset Password", null);


        Bundle arguments = getIntent().getExtras();
        oldPassword = arguments.getString("old_password");

        nextButton.setOnClickListener(this);
        newPasswordOTPView.addTextChangedListener(new InputLayoutTextWatcher(newPasswordOTPView));
        confirmPasswordOTPView.addTextChangedListener(new InputLayoutTextWatcher(confirmPasswordOTPView));
        newPasswordOTPView.setOtpCompletionListener(this);
        confirmPasswordOTPView.setOtpCompletionListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.next_button:
                if (NetworkUtils.haveNetworkConnection(ResetPasswordActivity.this)) {

                    resetPasswordBtnClick();
                } else {
                    UiUtils.showAlertDialogWithOKButton(ResetPasswordActivity.this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
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

    private void resetPasswordBtnClick() {

        if (isValidPwd == false && isValidCPwd == false) {

            UiUtils.checkValidationForOTP(ResetPasswordActivity.this, newPasswordOTPView,newPasswordErrorTV);
            UiUtils.checkValidationForOTP(ResetPasswordActivity.this, confirmPasswordOTPView,confirmPasswordErrorTV);

        } else {
            resetPasswordCall();
        }
    }

    private void resetPasswordCall() {

        showProgressDialog(ResetPasswordActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);

            Call<Login> call = apiService.doResetPassword(oldPassword,
                    newPasswordOTPView.getText().toString(),
                    confirmPasswordOTPView.getText().toString());

            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {

                        String data = new Gson().toJson(response.body());
                        JSONObject jsonObject = null;

                        Log.i("Drools", "--" + new Gson().toJson(response.body()));
                        if (response.body().getSuccess()) {

//                            SharedPreferenceUtils.setUserLoggedIn(LoginActivity.this, true, Constants.MOBILE);
//                            SharedPreferenceUtils.saveValue(LoginActivity.this,Constants.KEY_USER_NAME,response.body().getUser().getName());
//                            SharedPreferenceUtils.saveValue(LoginActivity.this, Constants.KEY_SECRET, response.body().getSecret());
//                            SharedPreferenceUtils.saveValue(LoginActivity.this, Constants.KEY_SECRET_ID, response.body().getSecretId());

//
                            GlobalValue.secret = response.body().getSecret();
                            GlobalValue.secret_id = response.body().getSecretId();
                            goToResetPasswordSuccess();

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(ResetPasswordActivity.this, response.body().getErrors(), response.body().getNotice());
                            clearFields();
                        }
                    } else {
                        hideProgressDialog();
                        NetworkUtils.handleErrorsCasesForAPICalls(ResetPasswordActivity.this, response.code(), response.body().getErrors());
                        clearFields();
                    }

                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(ResetPasswordActivity.this, null, null);
                    clearFields();
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void goToResetPasswordSuccess() {
        startActivity(new Intent(ResetPasswordActivity.this,TaskDashboardActivity.class));
        finish();
    }

    private class InputLayoutTextWatcher implements TextWatcher {

        private View view;

        private InputLayoutTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
//            registerButton.setEnabled(false);

            switch (view.getId()) {
                case R.id.new_password_otp_view:
                    boolean passwordStatus = UiUtils.checkValidationForOTP(ResetPasswordActivity.this, newPasswordOTPView,newPasswordErrorTV);

                    if (passwordStatus == false) {
                        isValidPwd = false;
                        requestFocus(newPasswordOTPView);
                    } else {
                        isValidPwd = true;
                        if (isValidCPwd && isValidPwd) {
                            nextButton.setEnabled(true);
                        }
                    }
                    break;
                case R.id.confirm_password_otp_view:
                    validateConfirmPassword();
                    break;

            }
        }

    }

    private boolean validateConfirmPassword() {
        String pwd = newPasswordOTPView.getText().toString().trim();
        String cPwd = confirmPasswordOTPView.getText().toString().trim();

        if (cPwd.length() >= 0 && cPwd.length() < 6) {
            confirmPasswordErrorTV.setText(getString(R.string.error_invalid_pwd));
            confirmPasswordErrorTV.setVisibility(View.VISIBLE);
            requestFocus(confirmPasswordOTPView);

            isValidCPwd = false;
            return false;
        } else {
            if (!pwd.equals(cPwd)) {
                confirmPasswordErrorTV.setText(getString(R.string.error_invalid_c_pwd));
                requestFocus(confirmPasswordOTPView);
                confirmPasswordErrorTV.setVisibility(View.VISIBLE);
                isValidCPwd = false;
                return false;
            } else {
                isValidCPwd = true;
                confirmPasswordErrorTV.setVisibility(View.GONE);
                confirmPasswordOTPView.setLineColor(getResources().getColor(R.color.editTextNotSelectedColor));
            }
            if (isValidCPwd && isValidPwd) {
                nextButton.setEnabled(true);
            }
        }
        return true;
    }

    public void requestFocus(View view) {
        if (view.requestFocus())
            ResetPasswordActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onOtpCompleted(String s) {
        if(newPasswordOTPView.isCursorVisible()){
            newPasswordOTPView.setLineColor(getResources().getColor(R.color.editTextNotSelectedColor));
            confirmPasswordOTPView.setFocusable(true);
            confirmPasswordOTPView.requestFocus();
            confirmPasswordOTPView.setLineColor(getResources().getColor(R.color.primaryColor));


        }else{
            confirmPasswordOTPView.setLineColor(getResources().getColor(R.color.editTextNotSelectedColor));
            hideSoftKeyboard();
        }


    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
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

    public void clearFields() {
        newPasswordOTPView.setText("");
        confirmPasswordOTPView.setText("");

        requestFocus(newPasswordOTPView);

    }
}
