package com.tailwebs.aadharindia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tailwebs.aadharindia.home.dashboard.TaskDashboardActivity;
import com.tailwebs.aadharindia.loginandforgot.LoginActivity;
import com.tailwebs.aadharindia.models.Logout;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.Constants;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.SharedPreferenceUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends BaseActivity
        implements View.OnClickListener {

    @BindView(R.id.logout_button)
    Button logoutButton;

    @BindView(R.id.center_creation_button)
    Button createCenterButton;

    private ProgressDialog mProgressDialog;

    boolean doubleBackToExitPressedOnce = false;

    static DashboardActivity instance;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);


        ButterKnife.bind(this);
        instance=this;
        GlobalValue.secret = SharedPreferenceUtils.getValue(DashboardActivity.this, Constants.KEY_SECRET);
        GlobalValue.secret_id = SharedPreferenceUtils.getValue(DashboardActivity.this, Constants.KEY_SECRET_ID);


        Log.d("secret"+ GlobalValue.secret,"--secret id"+ GlobalValue.secret_id);

        logoutButton.setOnClickListener(this);
        createCenterButton.setOnClickListener(this);

    }
    public static DashboardActivity getInstance() {
        return instance;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.logout_button:

                if (NetworkUtils.haveNetworkConnection(DashboardActivity.this)) {


                    doLogoutAndNavigateToLoginScreen();
                } else {
                    UiUtils.showAlertDialogWithOKButton(DashboardActivity.this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            finish();
                        }
                    });
                }
                break;

            case R.id.center_creation_button:
                startActivity(new Intent(DashboardActivity.this,TaskDashboardActivity.class));
                break;
        }
    }

    public void doLogoutAndNavigateToLoginScreen() {
        showProgressDialog(DashboardActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<Logout> call = apiService.doLogout();
            call.enqueue(new Callback<Logout>() {
                @Override
                public void onResponse(Call<Logout> call, Response<Logout> response) {
                    SharedPreferenceUtils.clearUserLoggedIn(DashboardActivity.this);
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            UiUtils.showAlertDialogWithOKButton(DashboardActivity.this, response.body().getNotice().toString(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SharedPreferenceUtils.clearUserLoggedIn(DashboardActivity.this);
                                    Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {

                            NetworkUtils.handleErrorsCasesForAPICalls(DashboardActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {

                        NetworkUtils.handleErrorsForAPICalls(DashboardActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<Logout> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(DashboardActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
