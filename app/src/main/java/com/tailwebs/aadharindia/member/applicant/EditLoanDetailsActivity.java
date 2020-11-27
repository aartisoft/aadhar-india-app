package com.tailwebs.aadharindia.member.applicant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.LoanReasonSpinnerAdapter;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.models.LoanTakerLoanDetailResponseModel;
import com.tailwebs.aadharindia.models.common.CACDCastesModel;
import com.tailwebs.aadharindia.models.common.CACDLoanReasonsModel;
import com.tailwebs.aadharindia.models.common.CACDMaritalStatusModel;
import com.tailwebs.aadharindia.models.common.CACDRationCardTypesModel;
import com.tailwebs.aadharindia.models.common.CACDReligionsModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;
import com.tailwebs.aadharindia.utils.custom.BetterSpinner;
import com.tailwebs.aadharindia.utils.custom.singleselecttoggle.SingleSelectToggleGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditLoanDetailsActivity extends BaseActivity
        implements View.OnClickListener {

    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;


    //Form Dats

    @BindView(R.id.continue_button)
    Button continueButton;


    @BindView(R.id.marital_status_toggle_radiobutton)
    SingleSelectToggleGroup maritalStatusToggleGroup;


    @BindView(R.id.caste_toggle_radiobutton)
    SingleSelectToggleGroup casteToggleGroup;


    @BindView(R.id.religion_toggle_radiobutton)
    SingleSelectToggleGroup religionToggleGroup;

    @BindView(R.id.ration_card_toggle_radiobutton)
    SingleSelectToggleGroup rationCardToggleGroup;


    @BindView(R.id.input_layout_preferred_language)
    TextInputLayout preferredLanguageLayout;

    @BindView(R.id.input_preferred_language)
    TextInputEditText preferredLanguageET;


    @BindView(R.id.input_layout_religion_others)
    TextInputLayout religionOthersLayout;

    @BindView(R.id.input_religion_others)
    TextInputEditText religionOthersET;

    @BindView(R.id.input_layout_ration_card_others)
    TextInputLayout rationCardOthersLayout;

    @BindView(R.id.input_ration_card_others)
    TextInputEditText rationCardOthersET;


    @BindView(R.id.input_layout_loan_reason)
    TextInputLayout loanReasonLayout;

    @BindView(R.id.input_loan_reason)
    BetterSpinner loanReasonET;

    @BindView(R.id.input_layout_loan_reason_other)
    TextInputLayout loanReasonOthersLayout;

    @BindView(R.id.input_loan_reason_other)
    TextInputEditText loanReasonOthersET;

    @BindView(R.id.input_layout_loan_description)
    TextInputLayout loanDescriptionLayout;

    @BindView(R.id.input_loan_description)
    TextInputEditText loanDescriptionET;

    //phone numbers

    @BindView(R.id.input_layout_primary_mobile)
    TextInputLayout primaryMobileLayout;

    @BindView(R.id.input_primary_mobile)
    TextInputEditText primaryMobileET;

    @BindView(R.id.input_layout_secondary_mobile)
    TextInputLayout secondaryMobileLayout;

    @BindView(R.id.input_secondary_mobile)
    TextInputEditText secondaryMobileET;

    @BindView(R.id.input_layout_land_line)
    TextInputLayout landlineLayout;

    @BindView(R.id.input_land_line)
    TextInputEditText landlineET;


    @BindView(R.id.marital_status_error_tv)
    TextView maritalStatusErrorTV;

    @BindView(R.id.caste_error_tv)
    TextView casteErrorTV;

    @BindView(R.id.religion_error_tv)
    TextView religionErrorTV;

    @BindView(R.id.ration_card_error_tv)
    TextView rationCardErrorTv;

    @BindView(R.id.value_amount_tenure_tv)
    TextView amountTenureTv;

    @BindView(R.id.emi_tv)
    TextView emiTv;


    @BindView(R.id.loan_reason_spinner)
    Spinner loanReasonSpinner;

    com.tailwebs.aadharindia.member.applicant.spinners.LoanReasonSpinnerAdapter newLoanReasonSpinnerAdapter;

    @BindView(R.id.loan_reason_spinner_error_tv)
    TextView loanReasonSpinnerErrorTv;


    private final int REQ_CODE = 1;



    String selectedLanguage =null,loanTakerId=null,loanDetailId=null;
    int loanReasonValue =0;
    public ArrayList<CACDLoanReasonsModel> cacdLoanReasonsModelArrayList = null,applicantLoanReasonArrayListNew;
    LoanReasonSpinnerAdapter loanReasonSpinnerAdapter;



    private boolean isValidLoanReason = false,isValidLoanReasonOthers=false,isValidLoanDescription=false,
            isValidLanguage=false,isValidReligionOthers=false
            ,isValidRationCardTypeOthers=false,isValidMaritalStatus=false,isValidCaste=false,
            isValidReligion=false,isValidRatioCardType=false,isValidPrimaryPhone=false;



    int isOtherIdForRatioCard=0,isOtherForReligion=0;
    int selectedMaritalStatusvalue,selectedCasteValue,selectedReligionValue,selectedRationCardTypeValue;
    private ProgressDialog mProgressDialog;

    private FirebaseAnalytics mFirebaseAnalytics;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_details);

        loanTakerId= GlobalValue.loanTakerId;

        ButterKnife.bind(this);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Loan Details");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Edit Loan Details", null);


        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(EditLoanDetailsActivity.this);
            getLoanDetails();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

        preferredLanguageET.setOnClickListener(this);
        continueButton.setOnClickListener(this);


        loanReasonET.addTextChangedListener(new InputLayoutTextWatcher(loanReasonET));
        loanReasonOthersET.addTextChangedListener(new InputLayoutTextWatcher(loanReasonOthersET));
        loanDescriptionET.addTextChangedListener(new InputLayoutTextWatcher(loanDescriptionET));
        primaryMobileET.addTextChangedListener(new InputLayoutTextWatcher(primaryMobileET));
//        secondaryMobileET.addTextChangedListener(new InputLayoutTextWatcher(secondaryMobileET));
//        landlineET.addTextChangedListener(new InputLayoutTextWatcher(landlineET));
        religionOthersET.addTextChangedListener(new InputLayoutTextWatcher(religionOthersET));
        rationCardOthersET.addTextChangedListener(new InputLayoutTextWatcher(rationCardOthersET));
        preferredLanguageET.addTextChangedListener(new InputLayoutTextWatcher(preferredLanguageET));


        cacdLoanReasonsModelArrayList =GlobalValue.applicantLoanReasonsArrayList;

//        set loan reasons

        //set values for relation dropdown
        //initialize arraylist
        applicantLoanReasonArrayListNew = new ArrayList<CACDLoanReasonsModel>();

        //add select to the drop down in position 0
        CACDLoanReasonsModel cacdLoanReasonsModel = new CACDLoanReasonsModel();
        cacdLoanReasonsModel.setId("0");
        cacdLoanReasonsModel.setTitle("Select");
        cacdLoanReasonsModel.setCode("0");
        applicantLoanReasonArrayListNew.add(cacdLoanReasonsModel);

        //add rest of the relations
        for(int i=0;i<GlobalValue.applicantLoanReasonsArrayList.size();i++){
            CACDLoanReasonsModel cacdLoanReasonsModel1 = new CACDLoanReasonsModel();
            cacdLoanReasonsModel1.setId(GlobalValue.applicantLoanReasonsArrayList.get(i).getId());
            cacdLoanReasonsModel1.setTitle(GlobalValue.applicantLoanReasonsArrayList.get(i).getTitle());
            cacdLoanReasonsModel1.setCode(GlobalValue.applicantLoanReasonsArrayList.get(i).getCode());

            applicantLoanReasonArrayListNew.add(cacdLoanReasonsModel1);
        }

        setLoanReasonSpinner(applicantLoanReasonArrayListNew);


        loanReasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CACDLoanReasonsModel relationsModel = newLoanReasonSpinnerAdapter.getItem(position);
                // Here you can do the action you want to...

                if (position==0){
                    loanReasonValue = 0;
                    isValidLoanReason=false;
                    checkOthers(relationsModel.getCode());
                }


                if(position!=0){

                    isValidLoanReason = true;
                    loanReasonValue= Integer.parseInt(relationsModel.getId());
                    loanReasonSpinnerErrorTv.setVisibility(View.GONE);
                    checkOthers(relationsModel.getCode());


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Marital Status

        maritalStatusToggleGroup.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidMaritalStatus = true;
                selectedMaritalStatusvalue = checkedId;
                maritalStatusErrorTV.setVisibility(View.GONE);

            }
        });


        //caste

        casteToggleGroup.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                selectedCasteValue = checkedId;
                casteErrorTV.setVisibility(View.GONE);
                isValidCaste=true;

            }
        });


        //Religion

        religionToggleGroup.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidReligion=true;
                religionErrorTV.setVisibility(View.GONE);
                selectedReligionValue = checkedId;
                if(checkedId == isOtherForReligion){
                    isValidReligionOthers =true;
                    isValidReligion=false;
                    religionOthersET.setVisibility(View.VISIBLE);
                    religionOthersLayout.setVisibility(View.VISIBLE);

                }else{
                    isValidReligionOthers=false;
                    isValidReligion=true;
                    religionOthersET.setVisibility(View.GONE);
                    religionOthersLayout.setVisibility(View.GONE);
                }


            }
        });

        //Ration Card Type

        rationCardToggleGroup.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidRatioCardType=true;
                rationCardErrorTv.setVisibility(View.GONE);
                selectedRationCardTypeValue = checkedId;
                if(checkedId == isOtherIdForRatioCard){
                    isValidRationCardTypeOthers=true;
                    isValidRatioCardType=false;
                    rationCardOthersET.setVisibility(View.VISIBLE);
                    rationCardOthersLayout.setVisibility(View.VISIBLE);

                }else{
                    isValidRationCardTypeOthers=false;
                    isValidRatioCardType=true;
                    rationCardOthersET.setVisibility(View.GONE);
                    rationCardOthersLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getLoanDetails() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerLoanDetailResponseModel> call = apiService.getLoanDetail(loanTakerId);
            call.enqueue(new Callback<LoanTakerLoanDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerLoanDetailResponseModel> call, final Response<LoanTakerLoanDetailResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            Log.d("Aadhar onResponse1", "" + new Gson().toJson(response.body()));

                            amountTenureTv.setText(response.body().getLoanTakerModel().getCalculatedEMIModel().getAmount_with_format()
                            +" for "+response.body().getLoanTakerModel().getCalculatedEMIModel().getTenure()+" Months");

                            emiTv.setText("EMI : "+response.body().getLoanTakerModel().getCalculatedEMIModel().getEmi_amount_with_format());
                            primaryMobileET.setText(response.body().getLoanTakerModel().getPrimary_phone_number());
                            isValidPrimaryPhone=true;
                                if(response.body().getLoanTakerModel().getLoanDetailModel()!=null){


                                    selectedMaritalStatusvalue = Integer.valueOf(response.body().getLoanTakerModel().getMaritalStatusModel().getId());
                                    selectedCasteValue = Integer.valueOf(response.body().getLoanTakerModel().getCastesModel().getId());
                                    selectedRationCardTypeValue = Integer.valueOf(response.body().getLoanTakerModel().getRationCardTypesModel().getId());
                                    selectedReligionValue = Integer.valueOf(response.body().getLoanTakerModel().getReligionsModel().getId());


                                    addRadioButtonsForMaritalStatus(GlobalValue.applicantMaritalStatusArrayList,
                                            response.body().getLoanTakerModel().getMaritalStatusModel().getId());
                                    addRadioButtonsForCaste(GlobalValue.applicantCastesArrayList,
                                            response.body().getLoanTakerModel().getCastesModel().getId());
                                    addRadioButtonsForReligions(GlobalValue.applicantReligionsArrayList,
                                            response.body().getLoanTakerModel().getReligionsModel().getId());
                                    addRadioButtonsForRationCardType(GlobalValue.applicantRationCardTypesArrayList,
                                            response.body().getLoanTakerModel().getRationCardTypesModel().getId());


                                    loanDetailId = response.body().getLoanTakerModel().getLoanDetailModel().getId();

                                    Log.i("Drools", "loanDetailId" + loanDetailId);
                                    for(int i=0;i<applicantLoanReasonArrayListNew.size();i++){
                                        if(applicantLoanReasonArrayListNew.get(i).getId().equalsIgnoreCase(response.body().getLoanTakerModel().getLoanDetailModel().getLoanReasonsModel().getId())){
                                            loanReasonSpinner.setSelection(i);
                                        }
                                    }

//
                                    if(response.body().getLoanTakerModel().getLoanDetailModel().getLoanReasonsModel().getTitle().equalsIgnoreCase("Other")){
                                        loanReasonOthersET.setVisibility(View.VISIBLE);
                                        loanReasonOthersLayout.setVisibility(View.VISIBLE);
                                        requestFocus(loanReasonOthersET);
                                        isValidLoanReasonOthers = true;
                                        isValidLoanReason=false;
                                        loanReasonOthersET.setText(response.body().getLoanTakerModel().getLoanDetailModel().getLoan_reason_title());
                                    }


//
                                    loanReasonValue=Integer.parseInt(response.body().getLoanTakerModel().getLoanDetailModel().getLoanReasonsModel().getId());

                                   loanDescriptionET.setText(response.body().getLoanTakerModel().getLoanDetailModel().getDescrption());
                                   primaryMobileET.setText(response.body().getLoanTakerModel().getPrimary_phone_number());


                                   if (response.body().getLoanTakerModel().getSecondary_phone_number()!=null){
                                       secondaryMobileET.setText(response.body().getLoanTakerModel().getSecondary_phone_number());
                                   }

                                    if (response.body().getLoanTakerModel().getLandline_phone_number()!=null){
                                        landlineET.setText(response.body().getLoanTakerModel().getLandline_phone_number());
                                    }
                                        preferredLanguageET.setText(response.body().getLoanTakerModel().getPreferred_language().getName());
                                   selectedLanguage = response.body().getLoanTakerModel().getPreferred_language_id();
                                }else{
//                                    Toast.makeText(EditLoanDetailsActivity.this, "null", Toast.LENGTH_SHORT).show();
                                }





                        } else {
                            NetworkUtils.handleErrorsForAPICalls(EditLoanDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(EditLoanDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerLoanDetailResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(EditLoanDetailsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }


    private void checkOthers(String code) {
        if(code.trim().equalsIgnoreCase("other")){
            Log.d("Aadhar","Other");
            loanReasonOthersET.setVisibility(View.VISIBLE);
            loanReasonOthersLayout.setVisibility(View.VISIBLE);
            requestFocus(loanReasonOthersET);
            isValidLoanReasonOthers = true;
            isValidLoanReason=false;
        }else{
            Log.d("Aadhar","Not Other");
            requestFocus(loanDescriptionET);
            loanReasonOthersET.setVisibility(View.GONE);
            loanReasonOthersLayout.setVisibility(View.GONE);
            isValidLoanReasonOthers = false;
            isValidLoanReason=true;
        }

    }

    private void setLoanReasonSpinner(ArrayList<CACDLoanReasonsModel> applicantLoanReasonArrayListNew) {


        CACDLoanReasonsModel[] cacdLoanReasonsModels = new CACDLoanReasonsModel[applicantLoanReasonArrayListNew.size()];

        for(int i=0;i<applicantLoanReasonArrayListNew.size();i++){
            cacdLoanReasonsModels[i]= new CACDLoanReasonsModel();
            cacdLoanReasonsModels[i].setId(applicantLoanReasonArrayListNew.get(i).getId());
            cacdLoanReasonsModels[i].setCode(applicantLoanReasonArrayListNew.get(i).getCode());
            cacdLoanReasonsModels[i].setTitle(applicantLoanReasonArrayListNew.get(i).getTitle());
        }


        newLoanReasonSpinnerAdapter = new com.tailwebs.aadharindia.member.applicant.spinners.LoanReasonSpinnerAdapter(EditLoanDetailsActivity.this,
                R.layout.ms__list_item,
                cacdLoanReasonsModels);

        loanReasonSpinner.setAdapter(newLoanReasonSpinnerAdapter); // Set the custom adapter to the spinner
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            preferredLanguageET.setText(data.getStringExtra(SelectLanguageActivity.CUSTOM_SEARCH_TEXT));
            selectedLanguage = data.getStringExtra(SelectLanguageActivity.CUSTOM_SEARCH_ID);
            isValidLanguage = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.input_preferred_language:
                Intent intent = new Intent(this, SelectLanguageActivity.class);
                startActivityForResult(intent,REQ_CODE);
                break;


            case R.id.continue_button:

                submitLoanDetails();

                break;
        }
    }

    private void submitLoanDetails() {

        if ((isValidLoanReason) && (isValidLoanDescription) && (isValidPrimaryPhone) &&
                (isValidMaritalStatus) &&
                (isValidCaste) && (isValidReligion) && (isValidRatioCardType) &&
                (isValidLanguage)) {

            submitLoanDetailsAPI();

        } else {

            UiUtils.checkValidationForAutoCompleteTV(EditLoanDetailsActivity.this, loanReasonET, loanReasonLayout, new ArrayList<String>());
            UiUtils.checkValidation(EditLoanDetailsActivity.this, loanDescriptionET, loanDescriptionLayout, new ArrayList<String>());
            UiUtils.checkValidation(EditLoanDetailsActivity.this, primaryMobileET, primaryMobileLayout, new ArrayList<String>());
            UiUtils.checkValidation(EditLoanDetailsActivity.this, preferredLanguageET, preferredLanguageLayout, new ArrayList<String>());

            if(selectedMaritalStatusvalue==0){
                maritalStatusErrorTV.setText("Field Required");
                maritalStatusErrorTV.setVisibility(View.VISIBLE);
                isValidMaritalStatus=false;
            }else{
                maritalStatusErrorTV.setVisibility(View.GONE);
                isValidMaritalStatus=true;
            }

            if(selectedCasteValue==0){
                casteErrorTV.setText("Field Required");
                casteErrorTV.setVisibility(View.VISIBLE);
                isValidCaste=false;
            }else{
                casteErrorTV.setVisibility(View.GONE);
                isValidCaste=true;
            }
            if(!isValidLoanReason){
                if(isValidLoanReasonOthers){
                    UiUtils.checkValidation(EditLoanDetailsActivity.this, loanReasonOthersET, loanReasonOthersLayout, new ArrayList<String>());


                }else{
                    loanReasonSpinnerErrorTv.setText("Field Required");
                    loanReasonSpinnerErrorTv.setVisibility(View.VISIBLE);
                    isValidLoanReason=false;
                }


            }



            if(!isValidReligion){
                if(isValidReligionOthers){
                    UiUtils.checkValidation(EditLoanDetailsActivity.this, religionOthersET, religionOthersLayout, new ArrayList<String>());

                }else{
                    religionErrorTV.setText("Field Required");
                    religionErrorTV.setVisibility(View.VISIBLE);
                    isValidReligion=false;
                }
            }


            if(!isValidRatioCardType){
                if(isValidRationCardTypeOthers){
                    UiUtils.checkValidation(EditLoanDetailsActivity.this, rationCardOthersET, rationCardOthersLayout, new ArrayList<String>());

                }else{
                    rationCardErrorTv.setText("Field Required");
                    rationCardErrorTv.setVisibility(View.VISIBLE);
                    isValidRatioCardType=false;
                }
            }

        }
    }

    private void updateTheStatusInApplicantDetailPage() {
        ApplicantDetailActivity.getInstance().init();
        MemberDetailActivity.getInstance().init();
    }

    private void submitLoanDetailsAPI() {

        continueButton.setEnabled(false);
        continueButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(EditLoanDetailsActivity.this);

        RequestBody religionName =null;
        RequestBody rationCardTypeName = null;
        RequestBody reasonTitle= null;

        try {
            Log.d("Aadhar onResponse", "--start" );
//            Add Loan taker Id TODO
            String loanTakerId =GlobalValue.loanTakerId;

            RequestBody primaryPhoneNo = RequestBody.create(MediaType.parse("text/plain"), primaryMobileET.getText().toString());
            RequestBody secondaryPhoneNo = RequestBody.create(MediaType.parse("text/plain"), secondaryMobileET.getText().toString());
            RequestBody landline = RequestBody.create(MediaType.parse("text/plain"), landlineET.getText().toString());
            RequestBody maritalStatusId = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(selectedMaritalStatusvalue));
            RequestBody casteId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectedCasteValue));
            RequestBody religionId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectedReligionValue));
            if(isValidReligionOthers){
                religionName = RequestBody.create(MediaType.parse("text/plain"), religionOthersET.getText().toString());
            }

            RequestBody rationCardTypeId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectedRationCardTypeValue));
            if(isValidRationCardTypeOthers){
                rationCardTypeName = RequestBody.create(MediaType.parse("text/plain"),rationCardOthersET.getText().toString());
            }
            RequestBody languageId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(selectedLanguage));
            RequestBody reasonId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(loanReasonValue));
            if(isValidLoanReasonOthers){
                reasonTitle= RequestBody.create(MediaType.parse("text/plain"),loanReasonOthersET.getText().toString());
            }
            RequestBody loanDescription = RequestBody.create(MediaType.parse("text/plain"), loanDescriptionET.getText().toString());
            RequestBody loanDetailid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(loanDetailId));

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerLoanDetailResponseModel> call;
            call = apiService.updateLoanTakerLoanDetails(
                    loanTakerId,
                    primaryPhoneNo,
                    secondaryPhoneNo,
                    landline,
                    maritalStatusId,
                    casteId,
                    religionId,
                    religionName,
                    rationCardTypeId,
                    rationCardTypeName,
                    languageId,
                    reasonId,
                    reasonTitle,
                    loanDescription,loanDetailid);
            call.enqueue(new Callback<LoanTakerLoanDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerLoanDetailResponseModel> call, Response<LoanTakerLoanDetailResponseModel> response) {
                    hideProgressDialog();
                    Log.d("Aadhar onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Aadhar onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {

                            continueButton.setEnabled(true);
                            continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                            //API Success is true
                            Toast.makeText(EditLoanDetailsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            updateTheStatusInApplicantDetailPage();
                            finishThisPage();
                        } else {
                            Log.d("Aadhar onResponse2", "" + response.body().getErrors());
                            continueButton.setEnabled(true);
                            continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                            NetworkUtils.handleErrorsCasesForAPICalls(EditLoanDetailsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Aadhar onResponse3", "" + response.body().getErrors());
                        continueButton.setEnabled(true);
                        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        NetworkUtils.handleErrorsForAPICalls(EditLoanDetailsActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerLoanDetailResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    continueButton.setEnabled(true);
                    continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(EditLoanDetailsActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }


    }

    private void finishThisPage() {
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

                case R.id.input_loan_reason:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean loanReasonStatus = UiUtils.checkValidationForAutoCompleteTV(EditLoanDetailsActivity.this, loanReasonET, loanReasonLayout, itemPassed);

                    if (loanReasonStatus == false) {
                        isValidLoanReason = false;
                        requestFocus(loanReasonET);
                    } else {
                        isValidLoanReason = true;
                        loanReasonLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_loan_description:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean loanDescriptionStatus = UiUtils.checkValidation(EditLoanDetailsActivity.this, loanDescriptionET, loanDescriptionLayout, itemPassed);

                    if (loanDescriptionStatus == false) {
                        isValidLoanDescription = false;
                        requestFocus(loanDescriptionET);
                    } else {
                        isValidLoanDescription = true;
                        loanDescriptionLayout.setErrorEnabled(false);
                    }

                    break;


                case R.id.input_primary_mobile:
                    itemPassed.clear();
                    itemPassed.add("mobile");
                    boolean primaryMobileStatus = UiUtils.checkValidation(EditLoanDetailsActivity.this, primaryMobileET, primaryMobileLayout, itemPassed);

                    if (primaryMobileStatus == false) {
                        isValidPrimaryPhone = false;
                        requestFocus(primaryMobileET);
                    } else {
                        isValidPrimaryPhone = true;
                        primaryMobileLayout.setErrorEnabled(false);


                    }
                    break;


//                case R.id.input_secondary_mobile:
//                    itemPassed.clear();
//                    itemPassed.add("mobile");
//                    boolean secondaryMobileStatus = UiUtils.checkValidation(LoanDetailsActivity.this, secondaryMobileET, secondaryMobileLayout, itemPassed);
//
//                    if (secondaryMobileStatus == false) {
//                        isValidSecondaryPhone = false;
//                        requestFocus(secondaryMobileET);
//                    } else {
//                        isValidSecondaryPhone = true;
//                        secondaryMobileLayout.setErrorEnabled(false);
//
//
//                    }
//                    break;
//
//                case R.id.input_land_line:
//                    itemPassed.clear();
//                    itemPassed.add("");
//                    boolean landLineStatus = UiUtils.checkValidation(LoanDetailsActivity.this, landlineET, landlineLayout, itemPassed);
//
//                    if (landLineStatus == false) {
//                        isValidLandLine = false;
//                        requestFocus(landlineET);
//
//                    } else {
//                        isValidLandLine=true;
//                        landlineLayout.setErrorEnabled(false);
//                    }
//
//                    break;


                case R.id.input_preferred_language:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean languageStatus = UiUtils.checkValidation(EditLoanDetailsActivity.this, preferredLanguageET, preferredLanguageLayout, itemPassed);

                    if (languageStatus == false) {
                        isValidLanguage = false;
                        requestFocus(preferredLanguageET);

                    } else {
                        isValidLanguage=true;
                        preferredLanguageLayout.setErrorEnabled(false);
                    }

                    break;
                case R.id.input_religion_others:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean religionStatus = UiUtils.checkValidation(EditLoanDetailsActivity.this, religionOthersET, religionOthersLayout, itemPassed);

                    if (religionStatus == false) {
                        isValidReligionOthers = false;
                        requestFocus(religionOthersET);

                    } else {
                        isValidReligionOthers=true;
                        isValidReligion= true;
                        religionOthersLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_ration_card_others:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean rationCardTypeStatus = UiUtils.checkValidation(EditLoanDetailsActivity.this, rationCardOthersET, rationCardOthersLayout, itemPassed);

                    if (rationCardTypeStatus == false) {
                        isValidRationCardTypeOthers = false;
                        requestFocus(rationCardOthersET);

                    } else {
                        isValidRationCardTypeOthers=true;
                        isValidRatioCardType = true;
                        rationCardOthersLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_loan_reason_other:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean reasonOthersStatus = UiUtils.checkValidation(EditLoanDetailsActivity.this, loanReasonOthersET, loanReasonOthersLayout, itemPassed);

                    if (reasonOthersStatus == false) {
                        isValidLoanReasonOthers = false;
                        requestFocus(loanReasonOthersET);

                    } else {
                        isValidLoanReasonOthers=true;
                        isValidLoanReason=true;
                        loanReasonOthersLayout.setErrorEnabled(false);
                    }

                    break;


            }
        }

    }

    public void addRadioButtonsForMaritalStatus(ArrayList<CACDMaritalStatusModel> value, String id) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getId()));
                rdbtn.setText(value.get(i).getName());
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                if(value.get(i).getId().equalsIgnoreCase(id)){
                    Log.i("Drools", "" + id);
                    rdbtn.setChecked(true);
                }
                rdbtn.setPadding(64,32,64,32);
                maritalStatusToggleGroup.addView(rdbtn);
            }
        }
    }

    public void addRadioButtonsForCaste(ArrayList<CACDCastesModel> value, String id) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getId()));
                rdbtn.setText(value.get(i).getName());
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                if(value.get(i).getId().equalsIgnoreCase(id)){
                    Log.i("Drools", "" + id);
                    rdbtn.setChecked(true);
                }
                rdbtn.setPadding(64,32,64,32);
                casteToggleGroup.addView(rdbtn);
            }
        }
    }


    public void addRadioButtonsForReligions(ArrayList<CACDReligionsModel> value, String id) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getId()));
                rdbtn.setText(value.get(i).getName());
                if(value.get(i).getCode().trim().equalsIgnoreCase("other")){
                    isOtherForReligion = Integer.valueOf(value.get(i).getId());
                }
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                if(value.get(i).getId().equalsIgnoreCase(id)){
                    Log.i("Drools", "" + id);
                    rdbtn.setChecked(true);
                }
                rdbtn.setPadding(64,32,64,32);
                religionToggleGroup.addView(rdbtn);
            }
        }
    }



    public void addRadioButtonsForRationCardType(ArrayList<CACDRationCardTypesModel> value, String id) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getId()));
                rdbtn.setText(value.get(i).getName());
                if(value.get(i).getCode().trim().equalsIgnoreCase("other")){
                    isOtherIdForRatioCard = Integer.valueOf(value.get(i).getId());
                }
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                if(value.get(i).getId().equalsIgnoreCase(id)){
                    Log.i("Drools", "" + id);
                    rdbtn.setChecked(true);
                }
                rdbtn.setPadding(64,32,64,32);
                rationCardToggleGroup.addView(rdbtn);
            }
        }
    }

    public void requestFocus(View view) {
        if (view.requestFocus())
            EditLoanDetailsActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
