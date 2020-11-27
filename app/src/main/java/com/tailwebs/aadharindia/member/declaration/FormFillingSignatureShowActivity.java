package com.tailwebs.aadharindia.member.declaration;

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
import com.tailwebs.aadharindia.home.tasks.collection.CollectionTaskDetailsActivity;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.applicant.ApplicantDocumentActivity;
import com.tailwebs.aadharindia.member.models.CalculateEMIResponseModel;
import com.tailwebs.aadharindia.member.rating.RatingActivity;
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

public class FormFillingSignatureShowActivity extends BaseActivity implements View.OnClickListener {


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
        setContentView(R.layout.activity_form_filling_signature_confirmation);

        ButterKnife.bind(this);


        loanTakerID =  GlobalValue.loanTakerId;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Signature");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
        finishButton.setText("Continue");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Applicant Signature Show", null);

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(FormFillingSignatureShowActivity.this);
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
            Call<CalculateEMIResponseModel> call = apiService.getLoanTaker(loanTakerID);
            call.enqueue(new Callback<CalculateEMIResponseModel>() {
                @Override
                public void onResponse(Call<CalculateEMIResponseModel> call, final Response<CalculateEMIResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            Picasso.with(FormFillingSignatureShowActivity.this)
                                    .load(response.body().getCustomerModel().getSignature().getOriginal())
                                    .placeholder(R.drawable.img_placeholder)
                                    .into(signatureImage);




                        } else {
                            NetworkUtils.handleErrorsForAPICalls(FormFillingSignatureShowActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(FormFillingSignatureShowActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<CalculateEMIResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(FormFillingSignatureShowActivity.this, null, null);
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

                Intent intent = new Intent(FormFillingSignatureShowActivity.this,MemberDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("page_value","declaration");
                intent.putExtras(bundle);
                startActivity(intent);
                break;

        }
    }


    private void updateStatusinMemberDetailPage() {
        MemberDetailActivity.getInstance().init();
    }

    private void goToCustomerRating(){

        startActivity(new Intent(FormFillingSignatureShowActivity.this,RatingActivity.class));
        finish();

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
