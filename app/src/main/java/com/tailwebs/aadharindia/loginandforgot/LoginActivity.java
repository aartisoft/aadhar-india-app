package com.tailwebs.aadharindia.loginandforgot;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.mukesh.OtpView;
import com.tailwebs.aadharindia.DashboardActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.geotracking.TrackerService;
import com.tailwebs.aadharindia.home.dashboard.TaskDashboardActivity;
import com.tailwebs.aadharindia.models.User;
import com.tailwebs.aadharindia.models.login.Login;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitAPIClient;
import com.tailwebs.aadharindia.utils.Constants;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.SharedPreferenceUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.log_in_button)
    Button loginButton;

    @BindView(R.id.tv_forgot_password)
    TextView forgotPasswordTV;

    @BindView(R.id.input_layout_mobile)
    TextInputLayout mobileLayout;

    @BindView(R.id.input_mobile)
    TextInputEditText mobileET;

    @BindView(R.id.password_otp_view)
    OtpView passwordET;

    @BindView(R.id.password_error_tv)
    TextView passwordErorTV;

    private FirebaseAnalytics mFirebaseAnalytics;

    private boolean isValidMobile = false, isValidPwd = false;
    private ProgressDialog mProgressDialog;

    private static final int PERMISSIONS_REQUEST = 1;
    private static String[] PERMISSIONS_REQUIRED = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private SharedPreferences mPrefs;
    private Snackbar mSnackbarPermissions;
    private Snackbar mSnackbarGps;
    private FirebaseAuth mAuth;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + "Login", null);



        mPrefs = getSharedPreferences(getString(R.string.prefs), MODE_PRIVATE);
        String transportID = mPrefs.getString(getString(R.string.transport_id), "");
        String email = mPrefs.getString(getString(R.string.email), "");
        String password = mPrefs.getString(getString(R.string.password), "");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

//        if (isServiceRunning(TrackerService.class)) {
//            // If service already running, simply update UI.
//            Toast.makeText(this, "tracking", Toast.LENGTH_SHORT).show();
//        } else if (transportID.length() > 0 && email.length() > 0 && password.length() > 0) {
//            // Inputs have previously been stored, start validation.
//            checkLocationPermission();
//        } else {
//
//        }


        loginButton.setOnClickListener(this);
        forgotPasswordTV.setOnClickListener(this);
        mobileET.addTextChangedListener(new InputLayoutTextWatcher(mobileET));
        passwordET.addTextChangedListener(new InputLayoutTextWatcher(passwordET));
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.log_in_button:
                if (NetworkUtils.haveNetworkConnection(LoginActivity.this)) {

                    loginBtnClick();
                } else {
                    UiUtils.showAlertDialogWithOKButton(LoginActivity.this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            finish();
                        }
                    });
                }
                break;


            case R.id.tv_forgot_password:
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
                break;
        }
    }

    private void loginBtnClick() {
        if (isValidMobile == false && isValidPwd == false) {

            UiUtils.checkValidation(LoginActivity.this, mobileET, mobileLayout, new ArrayList<String>());
            UiUtils.checkValidationForOTP(LoginActivity.this, passwordET,passwordErorTV);

        } else {
            loginCall();
        }
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
            loginButton.setEnabled(false);

            switch (view.getId()) {

                case R.id.input_mobile:
                    itemPassed.clear();
                    itemPassed.add("mobile");

                    boolean mobileStatus = UiUtils.checkValidation(LoginActivity.this, mobileET, mobileLayout, itemPassed);

                    if (mobileStatus == false) {
                        isValidMobile = false;
                        requestFocus(mobileET);
                    } else {
                        isValidMobile = true;
                        if (isValidMobile && isValidPwd) {
                            loginButton.setEnabled(true);
                        }
                    }
                    break;

                case R.id.password_otp_view:
                    itemPassed.clear();
                    itemPassed.add("otp");
                    boolean passwordStatus = UiUtils.checkValidationForOTP(LoginActivity.this, passwordET,passwordErorTV);

                    if (passwordStatus == false) {
                        isValidPwd = false;
                        requestFocus(passwordET);
                    } else {
                        isValidPwd = true;
                        passwordET.setLineColor(getResources().getColor(R.color.primaryColor));
                        if (isValidMobile && isValidPwd) {
                            loginButton.setEnabled(true);
                        }
                    }
                    break;

            }
        }

    }

    private void loginCall() {

        showProgressDialog(LoginActivity.this);
        try {
            ApiInterface apiService = RetrofitAPIClient.getClient().create(ApiInterface.class);

            Call<Login> call = apiService.doLogin(
                    mobileET.getText().toString(),
                    passwordET.getText().toString());

            call.enqueue(new Callback<Login>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {

                    if (response.isSuccessful()) {

                        String data = new Gson().toJson(response.body());
                        JSONObject jsonObject = null;

                        Log.i("Drools", "--" + new Gson().toJson(response.body()));
                        if (response.body().getSuccess()) {

                            response.body().getUser().getId();

                            // Store values.
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString(getString(R.string.transport_id),  response.body().getUser().getId());
                            editor.putString(getString(R.string.email),  response.body().getUser().getId()+"@aadharindia.in");
                            editor.putString(getString(R.string.password), "123456");
                            editor.apply();


                            mAuth.createUserWithEmailAndPassword(response.body().getUser().getId()+"@aadharindia.in", "123456")
                                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d("Firebase", "signInWithEmail:success");
                                                FirebaseUser user = mAuth.getCurrentUser();
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w("Firebase", "signInWithEmail:failure", task.getException());
                                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });


                            Bundle params = new Bundle();
                            params.putString("agent_id", response.body().getUser().getUserid());
                            params.putString("agent_name", response.body().getUser().getName());
                            params.putString("agent_phone", response.body().getUser().getPhoneNumber());
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, params);

                            mFirebaseAnalytics.setUserId(response.body().getUser().getUserid());
                            mFirebaseAnalytics.setUserProperty("User ID", response.body().getUser().getUserid());
                            if(response.body().getUser().getName()!=null){
                                mFirebaseAnalytics.setUserProperty("Name", response.body().getUser().getName());
                            }else{
                                mFirebaseAnalytics.setUserProperty("Name", "Guest");
                            }
                            mFirebaseAnalytics.setUserProperty("Phone", response.body().getUser().getPhoneNumber());
                            mFirebaseAnalytics.setUserProperty("Email", response.body().getUser().getEmail());
                            mFirebaseAnalytics.setUserProperty("City", response.body().getUser().getHomeAddress().getCity_name()+" - "+
                            response.body().getUser().getHomeAddress().getGooglePlaceId());
                            mFirebaseAnalytics.setUserProperty("State", response.body().getUser().getState());

                            SharedPreferenceUtils.setUserLoggedIn(LoginActivity.this, true, Constants.MOBILE);
//                            SharedPreferenceUtils.saveValue(LoginActivity.this,Constants.KEY_USER_NAME,response.body().getUser().getName());
                            SharedPreferenceUtils.saveValue(LoginActivity.this, Constants.KEY_SECRET, response.body().getSecret());
                            SharedPreferenceUtils.saveValue(LoginActivity.this, Constants.KEY_SECRET_ID, response.body().getSecretId());
                            SharedPreferenceUtils.saveValue(LoginActivity.this,Constants.KEY_USER_NAME,response.body().getUser().getName());
                            SharedPreferenceUtils.saveValue(LoginActivity.this,Constants.KEY_USER_ID,response.body().getUser().getPhoneNumber());
                            SharedPreferenceUtils.saveDataObject(LoginActivity.this, Constants.KEY_USER, response.body().getUser());
//                            SharedPreferenceUtils.saveValue(LoginActivity.this,Constants.KEY_USER_IMAGE,response.body().getUser().getPdfFiles().getMedium());


                            GlobalValue.secret = response.body().getSecret();
                            GlobalValue.secret_id = response.body().getSecretId();

                            hideProgressDialog();

                            goToDashBoard(response.body().getUser());

                        } else {
                            NetworkUtils.handleErrorsForAPICallsForLoginOnly(LoginActivity.this, response.body().getErrors(), response.body().getNotice());
                            clearFields();
                        }
                    } else {
                        hideProgressDialog();
                        NetworkUtils.handleErrorsCasesForAPICalls(LoginActivity.this, response.code(), response.body().getErrors());
                        clearFields();
                    }

                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(LoginActivity.this, null, null);
                    clearFields();
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void goToDashBoard(User user) {

        if(user.getPasswordChanged()){

            startActivity(new Intent(LoginActivity.this,TaskDashboardActivity.class));
            finish();

        }else{
            Intent intent = new Intent(LoginActivity.this,ResetPasswordActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("old_password", passwordET.getText().toString().trim());
            intent.putExtras(bundle);
            startActivity(intent);
        }
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
        mobileET.setText("");
        passwordET.setText("");
        mobileLayout.setErrorEnabled(false);

        requestFocus(mobileET);

    }

    public void requestFocus(View view) {
        if (view.requestFocus())
            LoginActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }




}
