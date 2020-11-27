package com.tailwebs.aadharindia.member.expenditure;

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
import com.tailwebs.aadharindia.center.CreateNewCenterActivity;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.applicant.EMICalculatorActivity;
import com.tailwebs.aadharindia.member.applicant.LoanDetailsActivity;
import com.tailwebs.aadharindia.member.cashincome.CashIncomeDetailsActivity;
import com.tailwebs.aadharindia.member.coapplicant.CoApplicantFamilyActivity;
import com.tailwebs.aadharindia.member.models.FamilyExpenditureResponseModel;
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
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  AddExpenditureActivity extends BaseActivity implements View.OnClickListener {
    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    private ProgressDialog mProgressDialog;

    //choose value from intent;
    String loanTakerID;

    //categories

    @BindView(R.id.input_layout_rent)
    TextInputLayout rentLayout;

    @BindView(R.id.input_rent)
    TextInputEditText rentET;

    @BindView(R.id.input_layout_food)
    TextInputLayout foodLayout;

    @BindView(R.id.input_food)
    TextInputEditText foodET;

    @BindView(R.id.input_layout_education)
    TextInputLayout educationLayout;

    @BindView(R.id.input_education)
    TextInputEditText educationET;

    @BindView(R.id.input_layout_medical)
    TextInputLayout medicalLayout;

    @BindView(R.id.input_medical)
    TextInputEditText medicalET;

    @BindView(R.id.input_layout_travel)
    TextInputLayout travelLayout;

    @BindView(R.id.input_travel)
    TextInputEditText travelET;

    @BindView(R.id.input_layout_clothing)
    TextInputLayout clothingLayout;

    @BindView(R.id.input_clothing)
    TextInputEditText clothingET;

    @BindView(R.id.input_layout_other)
    TextInputLayout otherLayout;

    @BindView(R.id.input_other)
    TextInputEditText otherET;


    @BindView(R.id.continue_button)
    Button continueButton;

    boolean isValidRent=false,isValidFood=false,isValidEducation=false,isValidMedical=false,
            isValidTravel=false,isValidClothing=false,isValidOther=false;

    private FirebaseAnalytics mFirebaseAnalytics;

    private GpsTracker gpsTracker;
    String currentLat=null,currentLong=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenditure);
        ButterKnife.bind(this);


        loanTakerID =  GlobalValue.loanTakerId;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Family Expenditure");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Applicant Expenditure", null);

        continueButton.setOnClickListener(this);

        rentET.addTextChangedListener(new InputLayoutTextWatcher(rentET));
        foodET.addTextChangedListener(new InputLayoutTextWatcher(foodET));
        educationET.addTextChangedListener(new InputLayoutTextWatcher(educationET));
        medicalET.addTextChangedListener(new InputLayoutTextWatcher(medicalET));
        clothingET.addTextChangedListener(new InputLayoutTextWatcher(clothingET));
        travelET.addTextChangedListener(new InputLayoutTextWatcher(travelET));
        otherET.addTextChangedListener(new InputLayoutTextWatcher(otherET));

        //show current Lat and Long

        try {
            gpsTracker = new GpsTracker(AddExpenditureActivity.this);
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
            showProgressDialog(AddExpenditureActivity.this);
            getExpenditure();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
    }

    private void getExpenditure() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<FamilyExpenditureResponseModel> call = apiService.getFamilyExpenditure(loanTakerID);
            call.enqueue(new Callback<FamilyExpenditureResponseModel>() {
                @Override
                public void onResponse(Call<FamilyExpenditureResponseModel> call, final Response<FamilyExpenditureResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            setValuesFromResponse(response.body());
                        } else {
                            NetworkUtils.handleErrorsForAPICalls(AddExpenditureActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(AddExpenditureActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<FamilyExpenditureResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(AddExpenditureActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(FamilyExpenditureResponseModel body) {

        if(body.getFamilyExpenditureModel()!=null){

            rentET.setText(body.getFamilyExpenditureModel().getRent());
            foodET.setText(body.getFamilyExpenditureModel().getFood());
            educationET.setText(body.getFamilyExpenditureModel().getEducation());
            medicalET.setText(body.getFamilyExpenditureModel().getMedical());
            travelET.setText(body.getFamilyExpenditureModel().getTravel());
            clothingET.setText(body.getFamilyExpenditureModel().getClothing());
            otherET.setText(body.getFamilyExpenditureModel().getOther());



        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.continue_button:
                submitFamilyExpenditure();
                break;

        }
    }

    private void submitFamilyExpenditure() {

        if((isValidRent) && (isValidFood) && (isValidEducation) && (isValidMedical) && (isValidClothing)
                && (isValidTravel) && (isValidOther))
        {
            callAPIForSubmittingFamilyExpenditure();

        }else{

            UiUtils.checkValidation(AddExpenditureActivity.this, rentET, rentLayout, new ArrayList<String>());
            UiUtils.checkValidation(AddExpenditureActivity.this, foodET, foodLayout, new ArrayList<String>());
            UiUtils.checkValidation(AddExpenditureActivity.this, educationET, educationLayout, new ArrayList<String>());
            UiUtils.checkValidation(AddExpenditureActivity.this, medicalET, medicalLayout, new ArrayList<String>());
            UiUtils.checkValidation(AddExpenditureActivity.this, clothingET, clothingLayout, new ArrayList<String>());
            UiUtils.checkValidation(AddExpenditureActivity.this, travelET, travelLayout, new ArrayList<String>());
            UiUtils.checkValidation(AddExpenditureActivity.this, otherET, otherLayout, new ArrayList<String>());
        }



    }

    private void callAPIForSubmittingFamilyExpenditure() {

        continueButton.setEnabled(false);
        continueButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(AddExpenditureActivity.this);
        try {
            Log.d("Aadhar onResponse", "--start" );

            RequestBody rent = RequestBody.create(MediaType.parse("text/plain"),   rentET.getText().toString());
            RequestBody food = RequestBody.create(MediaType.parse("text/plain"), foodET.getText().toString());
            RequestBody education = RequestBody.create(MediaType.parse("text/plain"), educationET.getText().toString());
            RequestBody medical = RequestBody.create(MediaType.parse("text/plain"), medicalET.getText().toString());
            Log.d("Aadhar onResponse", "--dobstart" );
            RequestBody travel = RequestBody.create(MediaType.parse("text/plain"),travelET.getText().toString());
            RequestBody clothing = RequestBody.create(MediaType.parse("text/plain"), clothingET.getText().toString());
            RequestBody other = RequestBody.create(MediaType.parse("text/plain"), otherET.getText().toString());
            Log.d("Aadhar onResponse", "--end" );

            ApiInterface apiService;
            if(currentLat !=null && currentLong !=null) {
                apiService = RetrofitClientWithHeadersForLocation.getClient().create(ApiInterface.class);
            }else{
                apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            }
            Call<FamilyExpenditureResponseModel> call;
            call = apiService.addFamilyExpenditures(
                    loanTakerID,
                    rent,
                    food,
                    education,
                    medical,
                    travel,
                    clothing,
                    other
            );
            call.enqueue(new Callback<FamilyExpenditureResponseModel>() {
                @Override
                public void onResponse(Call<FamilyExpenditureResponseModel> call, Response<FamilyExpenditureResponseModel> response) {
                    hideProgressDialog();
                    Log.d("Aadhar onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Aadhar onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Log.d("Aadhar onResponse1", "" +  GlobalValue.loanTakerId);
                            continueButton.setEnabled(true);
                            continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));


                            Bundle params = new Bundle();
                            params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
                            params.putString("status","applicant_family_expenditure_added");
                            mFirebaseAnalytics.logEvent("applicant_family_expenditure", params);

                            updateTheStatusInExpenditurePage();
                            goToOutsideBorrowing();
                            Toast.makeText(AddExpenditureActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Aadhar onResponse2", "" + response.body().getErrors());
                            continueButton.setEnabled(true);
                            continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                            NetworkUtils.handleErrorsCasesForAPICalls(AddExpenditureActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Aadhar onResponse3", "" + response.body().getErrors());
                        continueButton.setEnabled(true);
                        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        NetworkUtils.handleErrorsForAPICalls(AddExpenditureActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<FamilyExpenditureResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    continueButton.setEnabled(true);
                    continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(AddExpenditureActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }

    }

    private void updateTheStatusInExpenditurePage() {
        ExpenditureDetailsActivity.instance.init();
        MemberDetailActivity.getInstance().init();
    }

    private void goToOutsideBorrowing() {
        Intent intent = new Intent(AddExpenditureActivity.this,OutsideBorrowingActivity.class);
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


    public void requestFocus(View view) {
        if (view.requestFocus())
            AddExpenditureActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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


//        if ( &&   && (isValidDob) &&
//                (isValidGender) &&   &&  &&
//                && && (isValidProfilePicture) && (isValidCenterPhotos)){

        public void afterTextChanged(Editable editable) {

            switch (view.getId()) {
                case R.id.input_rent:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean rent_status = UiUtils.checkValidation(AddExpenditureActivity.this, rentET, rentLayout, itemPassed);

                    if (rent_status == false) {
                        isValidRent = false;
                        requestFocus(rentET);
                    } else {
                        isValidRent = true;

                    }
                    break;


                case R.id.input_food:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean food_Status = UiUtils.checkValidation(AddExpenditureActivity.this, foodET, foodLayout, itemPassed);

                    if (food_Status == false) {
                        isValidFood = false;
                        requestFocus(foodET);
                    } else {
                        isValidFood = true;
                        foodLayout.setErrorEnabled(false);
                    }

                    break;



                case R.id.input_education:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean education_Status = UiUtils.checkValidation(AddExpenditureActivity.this, educationET, educationLayout, itemPassed);

                    if (education_Status == false) {
                        isValidEducation = false;
                        requestFocus(educationET);
                    } else {
                        isValidEducation = true;
                        educationLayout.setErrorEnabled(false);
                    }

                    break;


                case R.id.input_medical:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean medical_Status = UiUtils.checkValidation(AddExpenditureActivity.this, medicalET, medicalLayout, itemPassed);

                    if (medical_Status == false) {
                        isValidMedical = false;
                        requestFocus(medicalET);
                    } else {
                        isValidMedical = true;
                        medicalLayout.setErrorEnabled(false);
                    }

                    break;


                case R.id.input_travel:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean travel_Status = UiUtils.checkValidation(AddExpenditureActivity.this, travelET, travelLayout, itemPassed);

                    if (travel_Status == false) {
                        isValidTravel = false;
                        requestFocus(travelET);
                    } else {
                        isValidTravel = true;
                        travelLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_clothing:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean clothing_Status = UiUtils.checkValidation(AddExpenditureActivity.this, clothingET, clothingLayout, itemPassed);

                    if (clothing_Status == false) {
                        isValidClothing = false;
                        requestFocus(clothingET);
                    } else {
                        isValidClothing = true;
                        clothingLayout.setErrorEnabled(false);
                    }

                    break;


                case R.id.input_other:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean other_Status = UiUtils.checkValidation(AddExpenditureActivity.this, otherET, otherLayout, itemPassed);

                    if (other_Status == false) {
                        isValidOther = false;
                        requestFocus(otherET);
                    } else {
                        isValidOther = true;
                        otherLayout.setErrorEnabled(false);
                    }

                    break;




            }
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

        AlertDialog.Builder builder = new AlertDialog.Builder(AddExpenditureActivity.this);
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
