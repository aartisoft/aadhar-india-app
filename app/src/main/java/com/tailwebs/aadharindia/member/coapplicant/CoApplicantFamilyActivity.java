package com.tailwebs.aadharindia.member.coapplicant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
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
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.applicant.ApplicantDocumentActivity;
import com.tailwebs.aadharindia.member.cashincome.CashIncomeDetailsActivity;
import com.tailwebs.aadharindia.member.coapplicant.models.LoanTakerCoApplicantDetailsResponseModel;
import com.tailwebs.aadharindia.member.coapplicant.models.LoanTakerCoApplicantFamilyResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeadersForLocation;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.GpsTracker;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoApplicantFamilyActivity extends BaseActivity implements View.OnClickListener {

    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;


    @BindView(R.id.input_layout_father_name)
    TextInputLayout fatherNameLayout;

    @BindView(R.id.input_father_name)
    TextInputEditText fatherName;

    @BindView(R.id.input_layout_mother_name)
    TextInputLayout motherNameLayout;

    @BindView(R.id.input_mother_name)
    TextInputEditText motherName;

    @BindView(R.id.continue_button)
    Button continueButton;

    private ProgressDialog mProgressDialog;


    String loanTakerID = null;

    private boolean isValidFatherName = false, isValidMotherName = false;

    private FirebaseAnalytics mFirebaseAnalytics;


    private GpsTracker gpsTracker;
    String currentLat=null,currentLong=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_applicant_family);
        ButterKnife.bind(this);

        loanTakerID = GlobalValue.loanTakerId;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Family");
        headingTV.setTextAppearance(getApplicationContext(), R.style.MyActionBarHeading);

        continueButton.setOnClickListener(this);
        fatherName.addTextChangedListener(new InputLayoutTextWatcher(fatherName));
        motherName.addTextChangedListener(new InputLayoutTextWatcher(motherName));

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Co-Applicant Family", null);

        //show current Lat and Long

        try {
            gpsTracker = new GpsTracker(CoApplicantFamilyActivity.this);
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



        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(CoApplicantFamilyActivity.this);
            getCoApplicantDetails();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

    }

    private void getCoApplicantDetails() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerCoApplicantFamilyResponseModel> call = apiService.getFamily(loanTakerID);
            call.enqueue(new Callback<LoanTakerCoApplicantFamilyResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCoApplicantFamilyResponseModel> call, final Response<LoanTakerCoApplicantFamilyResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            if(response.body().getLoanTakerModel().getIs_married()){

                                fatherNameLayout.setHint(getResources().getString(R.string.hint_father_in_law_name));
                                motherNameLayout.setHint(getResources().getString(R.string.hint_mother_in_law_name));

                            }else{

                                fatherNameLayout.setHint(getResources().getString(R.string.hint_father_name));
                                motherNameLayout.setHint(getResources().getString(R.string.hint_mother_name));
                            }

                            if(response.body().getLoanTakerModel().getFamilyModel()!=null){
                                fatherName.setText(response.body().getLoanTakerModel().getFamilyModel().getFather_name());
                                motherName.setText(response.body().getLoanTakerModel().getFamilyModel().getMother_name());
                            }




                        } else {
                            NetworkUtils.handleErrorsForAPICalls(CoApplicantFamilyActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantFamilyActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCoApplicantFamilyResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantFamilyActivity.this, null, null);
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


            case R.id.continue_button:
                submitValues();
                break;
        }
    }

    private void submitValues() {

        if((isValidFatherName) && (isValidMotherName)){

            callAPIForValueSubmission();


        }else{
            UiUtils.checkValidation(CoApplicantFamilyActivity.this, fatherName, fatherNameLayout, new ArrayList<String>());
            UiUtils.checkValidation(CoApplicantFamilyActivity.this, motherName, motherNameLayout, new ArrayList<String>());
        }
    }

    private void callAPIForValueSubmission() {

        continueButton.setEnabled(false);
        continueButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(CoApplicantFamilyActivity.this);
        try {
            Log.d("Aadhar onResponse", "--start" );
            RequestBody father_name = RequestBody.create(MediaType.parse("text/plain"), fatherName.getText().toString());
            RequestBody mother_name = RequestBody.create(MediaType.parse("text/plain"), motherName.getText().toString());
            Log.d("Aadhar onResponse", "--end" );

            ApiInterface apiService;
            if(currentLat !=null && currentLong !=null) {
                apiService = RetrofitClientWithHeadersForLocation.getClient().create(ApiInterface.class);
            }else{
                apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            }
            Call<LoanTakerCoApplicantFamilyResponseModel> call;
            call = apiService.addCoApplicantFamily(loanTakerID,father_name,mother_name
                   );
            call.enqueue(new Callback<LoanTakerCoApplicantFamilyResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCoApplicantFamilyResponseModel> call, Response<LoanTakerCoApplicantFamilyResponseModel> response) {
                    hideProgressDialog();
                    Log.d("Aadhar onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Aadhar onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            continueButton.setEnabled(true);
                            continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));

                            updateTheStatusInCoApplicantDetailPage();

                            Bundle params = new Bundle();
                            params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
                            params.putString("status","co_applicant_family_created");
                            mFirebaseAnalytics.logEvent("co_applicant_family", params);

//                            Bundle co_params = new Bundle();
//                            co_params.putString("co_applicant_mother_name", response.body().getLoanTakerModel().getFamilyModel().getMother_name());
//                            co_params.putString("co_applicant_father_name",response.body().getLoanTakerModel().getFamilyModel().getFather_name());
//                            params.putString("level","co_applicant_creation_completed");
//                            mFirebaseAnalytics.logEvent("co_applicant", co_params);


                            Toast.makeText(CoApplicantFamilyActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            goToCoApplicantDetailsPage();

                        } else {
                            Log.d("Aadhar onResponse2", "" + response.body().getErrors());
                            continueButton.setEnabled(true);
                            continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));

                            JSONObject jsonObject = null;
                            NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantFamilyActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Aadhar onResponse3", "" + response.body().getErrors());
                        continueButton.setEnabled(true);
                        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));

                        NetworkUtils.handleErrorsForAPICalls(CoApplicantFamilyActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCoApplicantFamilyResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    continueButton.setEnabled(true);
                    continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));

                    NetworkUtils.handleErrorsForAPICalls(CoApplicantFamilyActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }


    }

    private void  updateTheStatusInCoApplicantDetailPage(){
        CoApplicantDetailsActivity.instance.init();
        MemberDetailActivity.getInstance().init();
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

                case R.id.input_father_name:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean fStatus = UiUtils.checkValidation(CoApplicantFamilyActivity.this, fatherName, fatherNameLayout, itemPassed);

                    if (fStatus == false) {
                        isValidFatherName = false;
                        requestFocus(fatherName);
                    } else {
                        isValidFatherName = true;
                        fatherNameLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_mother_name:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean mStatus = UiUtils.checkValidation(CoApplicantFamilyActivity.this, motherName, motherNameLayout, itemPassed);

                    if (mStatus == false) {
                        isValidMotherName = false;
                        requestFocus(motherName);
                    } else {
                        isValidMotherName = true;
                        motherNameLayout.setErrorEnabled(false);
                    }

                    break;

            }
        }

    }

    public void requestFocus(View view) {
        if (view.requestFocus())
            CoApplicantFamilyActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void goToCoApplicantDetailsPage() {
        Intent intent = new Intent(CoApplicantFamilyActivity.this,CashIncomeDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("applicant","finished");
        intent.putExtras(bundle);
        startActivity(intent);
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

    @Override
    public void onBackPressed() {

        View view = getLayoutInflater().inflate(R.layout.custom_message_yes_no_dialog, null);
        TextView messageTV =(TextView)view.findViewById(R.id.message_tv);
        Button yesButton =(Button)view.findViewById(R.id.yes_button);
        Button noButton =(Button)view.findViewById(R.id.no_button);
        messageTV.setText(getResources().getString(R.string.hint_are_you_leaving));
        yesButton.setText(getResources().getString(R.string.hint_leave_page));
        noButton.setText(getResources().getString(R.string.hint_stay_on_page));

        AlertDialog.Builder builder = new AlertDialog.Builder(CoApplicantFamilyActivity.this);
        builder.setCancelable(false)
                .setTitle(getResources().getString(R.string.hint_leave_page_title))
                .setPositiveButton("", null)
                .setNeutralButton("", null)
                .setView(view);


        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }
}
