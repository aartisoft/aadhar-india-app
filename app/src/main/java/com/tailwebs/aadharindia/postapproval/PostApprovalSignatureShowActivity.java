package com.tailwebs.aadharindia.postapproval;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.postapproval.models.LoanTakerPostApprovalDocumentResponseModel;
import com.tailwebs.aadharindia.housevisit.SignatureShowActivity;
import com.tailwebs.aadharindia.housevisit.models.houseinformationcreate.HouseInforationEditResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostApprovalSignatureShowActivity extends BaseActivity implements View.OnClickListener {


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

    //choose value from intent;
    String loanTakerID;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_approval_signature_confirmation);
        ButterKnife.bind(this);


        loanTakerID =  GlobalValue.loanTakerId;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Signature");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "PA Signature Show", null);

        finishButton.setText("Continue");

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(PostApprovalSignatureShowActivity.this);
            getSignature();

        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

        finishButton.setOnClickListener(this);
    }

    private void getSignature() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerPostApprovalDocumentResponseModel> call = apiService.getPostApprovalDocuments(loanTakerID);
            call.enqueue(new Callback<LoanTakerPostApprovalDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerPostApprovalDocumentResponseModel> call, final Response<LoanTakerPostApprovalDocumentResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            Picasso.with(PostApprovalSignatureShowActivity.this)
                                    .load(response.body().getLoanTakerModel().getPostApprovalDocumentModel().getSignature().getOriginal())
                                    .placeholder(R.drawable.img_placeholder)
                                    .into(signatureImage);




                        } else {
                            NetworkUtils.handleErrorsForAPICalls(PostApprovalSignatureShowActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(PostApprovalSignatureShowActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerPostApprovalDocumentResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(PostApprovalSignatureShowActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.finish_button:
                onBackPressed();

                break;

        }
    }


    private void updateStatusinIndividualDetailPage() {
        IndividualMemberDetailActivity.getInstance().init();
    }


    private  void goToInsuranceForm(){

        finish();
        Intent intent = new Intent(PostApprovalSignatureShowActivity.this,IndividualMemberDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

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
