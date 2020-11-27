package com.tailwebs.aadharindia.postapproval;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.home.tasks.collection.CollectionRatingActivity;
import com.tailwebs.aadharindia.member.applicant.AddCustomerDetailsActivity;
import com.tailwebs.aadharindia.member.coapplicant.CoApplicantFamilyActivity;
import com.tailwebs.aadharindia.postapproval.models.digio.IndividualPostApprovalDigioResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeadersForLocation;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.GpsTracker;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddContactsDigioActivity extends BaseActivity implements View.OnClickListener {


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    @BindView(R.id.input_layout_applicant_phone)
    TextInputLayout applicantPhoneLayout;

    @BindView(R.id.input_applicant_phone)
    TextInputEditText applicantPhoneET;

    @BindView(R.id.input_layout_co_applicant_phone)
    TextInputLayout coApplicantPhoneLayout;


    @BindView(R.id.input_co_applicant_phone)
    TextInputEditText coApplicantPhoneET;

    @BindView(R.id.continue_button)
    Button continueButton;


    private ProgressDialog mProgressDialog;


    String loanTakerID = null;

    private boolean isValidApplicantContact = false, isValidCoApplicantContact = false;

    private FirebaseAnalytics mFirebaseAnalytics;


    private GpsTracker gpsTracker;
    String currentLat=null,currentLong=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts_digio);
        ButterKnife.bind(this);

        loanTakerID = GlobalValue.loanTakerId;

        //show current Lat and Long

        try {
            gpsTracker = new GpsTracker(AddContactsDigioActivity.this);
            if (gpsTracker.canGetLocation()) {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                currentLat = (String.valueOf(latitude));
                currentLong = (String.valueOf(longitude));

                if(currentLat !=null && currentLong !=null) {

                    GlobalValue.latitude = currentLat;
                    GlobalValue.longitude = currentLong;
                }

                Log.i("Lat-Long", "" + currentLat + "-" + currentLong);

            } else {
                gpsTracker.showSettingsAlert();
            }
        }catch (Exception e){
            System.out.print("Caught the NullPointerException");
        }

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Add Contacts  ");
        headingTV.setTextAppearance(getApplicationContext(), R.style.MyActionBarHeading);

        continueButton.setOnClickListener(this);
        applicantPhoneET.addTextChangedListener(new InputLayoutTextWatcher(applicantPhoneET));
        coApplicantPhoneET.addTextChangedListener(new InputLayoutTextWatcher(coApplicantPhoneET));

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "PA Add Contacts Digio", null);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.continue_button:
                submitValues();
                break;
        }

    }

    private void submitValues() {

        if((isValidApplicantContact) && (isValidCoApplicantContact)){

            addContactsForDigio();

        }else{
            UiUtils.checkValidation(AddContactsDigioActivity.this, applicantPhoneET, applicantPhoneLayout, new ArrayList<String>());
            UiUtils.checkValidation(AddContactsDigioActivity.this, coApplicantPhoneET, coApplicantPhoneLayout, new ArrayList<String>());
        }
    }

    private void addContactsForDigio() {

        continueButton.setEnabled(false);
        continueButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));
        showProgressDialog(AddContactsDigioActivity.this);
        try {
            ApiInterface apiService;
            if(currentLat !=null && currentLong !=null) {
                apiService = RetrofitClientWithHeadersForLocation.getClient().create(ApiInterface.class);
            }else{
                apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            }

            Call<IndividualPostApprovalDigioResponseModel> call = apiService.addContactsDigio(loanTakerID,
                    applicantPhoneET.getText().toString().trim(),coApplicantPhoneET.getText().toString().trim());

            call.enqueue(new Callback<IndividualPostApprovalDigioResponseModel>() {
                @Override
                public void onResponse(Call<IndividualPostApprovalDigioResponseModel> call, Response<IndividualPostApprovalDigioResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {

                        continueButton.setEnabled(true);
                        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));


                        Bundle params = new Bundle();
                        params.putString("status","post_approval_digio_contacts_created");
                        params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
                        mFirebaseAnalytics.logEvent("post_approval_add_digio_contacts", params);

                        Log.i("AAdhar EresponseMI", "--" + new Gson().toJson(response.body()));
                        goToPDFPage(response.body().getPostApprovalDocumentModel().getLoan_agreement());

                    } else {
                        continueButton.setEnabled(true);
                        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        NetworkUtils.handleErrorsCasesForAPICalls(AddContactsDigioActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<IndividualPostApprovalDigioResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    continueButton.setEnabled(true);
                    continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(AddContactsDigioActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void goToPDFPage(String loan_agreement) {

        Intent l_intent = new Intent(AddContactsDigioActivity.this,ViewLoanAgreementPDFActivity.class);
        Bundle l_bundle = new Bundle();
        l_bundle.putString("title","Loan Agreement");
        l_bundle.putString("file",loan_agreement);
        l_intent.putExtras(l_bundle);
        startActivity(l_intent);
        finish();
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

            switch (view.getId()) {

                case R.id.input_applicant_phone:
                    itemPassed.clear();
                    itemPassed.add("mobile");
                    boolean fStatus = UiUtils.checkValidation(AddContactsDigioActivity.this, applicantPhoneET, applicantPhoneLayout, itemPassed);

                    if (fStatus == false) {
                        isValidApplicantContact = false;
                        requestFocus(applicantPhoneET);
                    } else {
                        isValidApplicantContact = true;
                        applicantPhoneLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_co_applicant_phone:
                    itemPassed.clear();
                    itemPassed.add("mobile");
                    boolean mStatus = UiUtils.checkValidation(AddContactsDigioActivity.this, coApplicantPhoneET, coApplicantPhoneLayout, itemPassed);

                    if (mStatus == false) {
                        isValidCoApplicantContact = false;
                        requestFocus(coApplicantPhoneET);
                    } else {
                        isValidCoApplicantContact = true;
                        coApplicantPhoneLayout.setErrorEnabled(false);
                    }

                    break;

            }
        }

    }

    public void requestFocus(View view) {
        if (view.requestFocus())
            AddContactsDigioActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
