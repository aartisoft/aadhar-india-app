package com.tailwebs.aadharindia.housevisit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.housevisit.models.houseinformationcreate.HouseInforationCreateResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignatureConfirmationActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.finish_button)
    Button finishButton;

    //Action Bar


    @BindView(R.id.signature_image)
    ImageView signatureImage;

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;
    private ProgressDialog mProgressDialog;

    private FirebaseAnalytics mFirebaseAnalytics;

    //choose value from intent;
    String loanTakerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_confirmation);
        ButterKnife.bind(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "HV Signature Confirm", null);


        loanTakerID =  GlobalValue.loanTakerId;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Confirm Signature");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
            signatureImage.setImageURI( GlobalValue.signatureUri);
        finishButton.setOnClickListener(this);
    }


    private void submitDeclaration() {
        finishButton.setEnabled(false);
        finishButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));
        showProgressDialog(SignatureConfirmationActivity.this);
        try {
            Log.d("Profile image", "start");
            //For Image
            File file = null;
            MultipartBody.Part fileToUpload = null;
            if (GlobalValue.signatureUriPath != null) {
                file = new File(GlobalValue.signatureUriPath);
                RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                fileToUpload = MultipartBody.Part.createFormData("house_visit[signature_image]", file.getName(), mFile);
            }

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<HouseInforationCreateResponseModel> call;
            call = apiService.addSignature(
                    loanTakerID,
                    fileToUpload);

            call.enqueue(new Callback<HouseInforationCreateResponseModel>() {
                @Override
                public void onResponse(Call<HouseInforationCreateResponseModel> call, Response<HouseInforationCreateResponseModel> response) {
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {

                            finishButton.setEnabled(true);
                            finishButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                            //API Success is true

                            Bundle params = new Bundle();
                            params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
                            params.putString("status","house_visit_declaration_created");
                            mFirebaseAnalytics.logEvent("house_visit_declaration", params);

                            updateStatusinHouseDetailPage();
                            goToCustomerRating();
                            Toast.makeText(SignatureConfirmationActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharSignaturePad";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            finishButton.setEnabled(true);
                            finishButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                            NetworkUtils.handleErrorsCasesForAPICalls(SignatureConfirmationActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        finishButton.setEnabled(true);
                        finishButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        NetworkUtils.handleErrorsForAPICalls(SignatureConfirmationActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<HouseInforationCreateResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    finishButton.setEnabled(true);
                    finishButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(SignatureConfirmationActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }


    private void updateStatusinHouseDetailPage() {
        HouseDetailsActivity.getInstance().init();
    }

    private void goToCustomerRating(){

        startActivity(new Intent(SignatureConfirmationActivity.this,HouseVisitRatingActivity.class));
        finish();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.finish_button:
                if (NetworkUtils.haveNetworkConnection(this)) {
                    showProgressDialog(SignatureConfirmationActivity.this);
                    submitDeclaration();
                } else {
                    UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                }
                break;

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
}
