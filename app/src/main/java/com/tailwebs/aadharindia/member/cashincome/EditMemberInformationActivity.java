package com.tailwebs.aadharindia.member.cashincome;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.applicant.SelectLanguageActivity;
import com.tailwebs.aadharindia.member.applicant.spinners.RelationWithApplicantSpinnerAdapter;
import com.tailwebs.aadharindia.member.cashincome.models.FamilyMemberResponseModel;
import com.tailwebs.aadharindia.member.cashincome.models.FamilyModel;
import com.tailwebs.aadharindia.member.cashincome.models.JobModel;
import com.tailwebs.aadharindia.member.coapplicant.CoApplicantRelationsSpinnerAdapter;
import com.tailwebs.aadharindia.models.common.CCoARelationModel;
import com.tailwebs.aadharindia.models.common.CIEducationLevelsStatusModel;
import com.tailwebs.aadharindia.models.common.CIEducationStatusModel;
import com.tailwebs.aadharindia.models.common.CIHealthStatusModel;
import com.tailwebs.aadharindia.models.common.CIManagementTypesStatusModel;
import com.tailwebs.aadharindia.models.common.CashIncomeCommonDataResponseModel;
import com.tailwebs.aadharindia.models.common.CustomerCoRelationResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;
import com.tailwebs.aadharindia.utils.custom.BetterSpinner;
import com.tailwebs.aadharindia.utils.custom.singleselecttoggle.SingleSelectToggleGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMemberInformationActivity extends BaseActivity implements View.OnClickListener {

    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;


    private ProgressDialog mProgressDialog;

    @BindView(R.id.education_layout)
    LinearLayout educationLayout;

    @BindView(R.id.study_yes_tv)
    TextView studyYesTv;


    @BindView(R.id.study_no_tv)
    TextView studyNoTv;


    @BindView(R.id.income_yes_tv)
    TextView incomeYesTv;

    @BindView(R.id.income_no_tv)
    TextView incomeNoTv;


    @BindView(R.id.typeOfSchoolHeading)
    TextView typeOfSchoolHeading;

    @BindView(R.id.health_status_error_tv)
    TextView healthErrorTv;

    @BindView(R.id.study_status_error_tv)
    TextView stuadyStatusErrorTv;

    @BindView(R.id.income_status_error_tv)
    TextView incomeStatusErrorTV;



    @BindView(R.id.health_toggle_radiobutton)
    SingleSelectToggleGroup healthToggle;

    @BindView(R.id.education_toggle_radiobutton)
    SingleSelectToggleGroup educationToggle;

    @BindView(R.id.type_of_school_toggle_radiobutton)
    SingleSelectToggleGroup typeOfSchoolToggle;


    @BindView(R.id.education_level_toggle_radiobutton)
    SingleSelectToggleGroup educationLevelToggle;


    public ArrayList<CCoARelationModel> relationModels = null;
    CoApplicantRelationsSpinnerAdapter corelationsSpinnerAdapter;

    @BindView(R.id.input_layout_name)
    TextInputLayout nameLayout;

    @BindView(R.id.input_name)
    TextInputEditText nameET;

    @BindView(R.id.input_layout_age)
    TextInputLayout ageLayout;


    @BindView(R.id.input_age)
    TextInputEditText ageET;


    @BindView(R.id.input_layout_class)
    TextInputLayout classLayout;


    @BindView(R.id.input_class)
    TextInputEditText classET;


    @BindView(R.id.input_layout_relation_with_applicant)
    TextInputLayout relationWithApplicantLayout;

    @BindView(R.id.input_relation_with_applicant)
    BetterSpinner relationWithApplicantET;

    @BindView(R.id.add_member_button)
    Button addMemberButton;

    @BindView(R.id.education_status_error_tv)
    TextView educationStatusErrorTv;

    @BindView(R.id.type_of_school_status_error_tv)
    TextView typeOfSchoolErrorTV;

    @BindView(R.id.education_level_status_error_tv)
    TextView educationLevelStatusErrorTv;

    //choose value from intent;
    String loanTakerID,familyMemberId;

    int relationWithApplicantValue=0;

    int healthValue,educationValue,educationLevelValue,typeOfSchoolValue,countOfMembers;

    Boolean isValidHealth=false,isValidEducation=false,isValidEducationLevel=false,isValidTypeOfSchool=false,
            isValidName=false,isValidAge=false,isValidRelationWithApplicant=false,isValidClass=false,isValidRelationWithAPplicantOthers=false,
            isValidPrimaryJob=false,isValidIncomeAmount=false,isValidCashFlow=false,isValidJob=false;

    String studyChosed=null,incomeChosed=null;



    @BindView(R.id.duplicating_layout)
    LinearLayout duplicatingLayout;

    Boolean alternateIncomeValue;

    @BindView(R.id.add_job_tv)
    TextView addJobTv;

    ArrayList<JobModel>  jobModelArrayList = null;

    @BindView(R.id.relation_with_applicant_spinner)
    Spinner relationWithApplicantSpinner;

    RelationWithApplicantSpinnerAdapter relationWithApplicantSpinnerAdapter;

    @BindView(R.id.relation_with_applicant_spinner_error_tv)
    TextView relationWithApplicantSpinnerErrorTv;


    //applicant relation others

    @BindView(R.id.input_layout_relation_with_applicant_others)
    TextInputLayout relationWithApplicantOthersLayout;

    @BindView(R.id.input_relation_with_applicant_others)
    TextInputEditText relationWithApplicantOthersET;

    public ArrayList<CCoARelationModel> relationWithApplicantModelArrayList = null,relationWithApplicantModelArrayListNew,relationWithApplicantModelArrayListApplicant;
    private final int REQ_CODE = 1;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_information);
        ButterKnife.bind(this);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("New Family Member");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Edit Family Member", null);


        addMemberButton.setText(getResources().getString(R.string.hint_update_member));
        familyMemberId = getIntent().getExtras().getString("family_member_id");
        loanTakerID =  GlobalValue.loanTakerId;

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(EditMemberInformationActivity.this);

            getCashIncomeCommonDatas();

            if(getIntent().hasExtra("position")){
                countOfMembers=getIntent().getExtras().getInt("position");

                if(countOfMembers==1){
                    setApplicantData();
                }else  if(countOfMembers == 2){
                    setCoApplicantData();
                }else{
                    getRelationDropDown();
                }


            }


        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }


        studyYesTv.setOnClickListener(this);
        studyNoTv.setOnClickListener(this);
        incomeYesTv.setOnClickListener(this);
        incomeNoTv.setOnClickListener(this);
        addMemberButton.setOnClickListener(this);
        addJobTv.setOnClickListener(this);

        nameET.addTextChangedListener(new InputLayoutTextWatcher(nameET));
        ageET.addTextChangedListener(new InputLayoutTextWatcher(ageET));
        relationWithApplicantET.addTextChangedListener(new InputLayoutTextWatcher(relationWithApplicantET));
        classET.addTextChangedListener(new InputLayoutTextWatcher(classET));
        relationWithApplicantOthersET.addTextChangedListener(new InputLayoutTextWatcher(relationWithApplicantOthersET));


        healthToggle.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                healthValue = checkedId;
                healthErrorTv.setVisibility(View.GONE);
                isValidHealth=true;

            }
        });


        educationToggle.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                educationValue = checkedId;
                educationStatusErrorTv.setVisibility(View.GONE);
                isValidEducation=true;
                isValidEducationLevel=false;

            }
        });

        educationLevelToggle.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                educationLevelValue = checkedId;
                educationLevelStatusErrorTv.setVisibility(View.GONE);
                isValidEducationLevel=true;
                isValidEducation=false;

            }
        });


        typeOfSchoolToggle.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                typeOfSchoolValue = checkedId;
                typeOfSchoolErrorTV.setVisibility(View.GONE);
                isValidTypeOfSchool=true;

            }
        });


        relationWithApplicantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CCoARelationModel relationsModel = relationWithApplicantSpinnerAdapter.getItem(position);
                // Here you can do the action you want to...

                if (position==0){
                    relationWithApplicantValue = 0;
                    isValidRelationWithApplicant=false;
                }


                if(position!=0){

                    isValidRelationWithApplicant = true;
                    relationWithApplicantValue= Integer.parseInt(relationsModel.getId());
                    relationWithApplicantSpinnerErrorTv.setVisibility(View.GONE);
                    checkOthersApplicantRelation(relationsModel.getCode());


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void checkOthersApplicantRelation(String code) {
        if(code.trim().equalsIgnoreCase("other")){

            Log.d("Aadhar","Other");

            relationWithApplicantOthersLayout.setVisibility(View.VISIBLE);
            relationWithApplicantOthersET.setVisibility(View.VISIBLE);
            requestFocus(relationWithApplicantOthersET);
            isValidRelationWithAPplicantOthers = true;
            isValidRelationWithApplicant=false;
        }else{
            relationWithApplicantOthersLayout.setVisibility(View.GONE);
            relationWithApplicantOthersET.setVisibility(View.GONE);
            Log.d("Aadhar","Not Other");
            isValidRelationWithAPplicantOthers = false;
            isValidRelationWithApplicant=true;
        }

    }

    public void requestFocus(View view) {
        if (view.requestFocus())
            EditMemberInformationActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void getMemberInformation() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<FamilyMemberResponseModel> call = apiService.getAFamilyMember(loanTakerID,familyMemberId);
            call.enqueue(new Callback<FamilyMemberResponseModel>() {
                @Override
                public void onResponse(Call<FamilyMemberResponseModel> call, final Response<FamilyMemberResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            Log.d("Aadhar onResponse1", "" + new Gson().toJson(response.body()));

                            nameET.setText(response.body().getFamilyModel().getName());
                            ageET.setText(response.body().getFamilyModel().getAge());


                            if(getIntent().getExtras().getInt("position")>2) {
                                for (int i = 0; i < relationWithApplicantModelArrayListNew.size(); i++) {
                                    if (relationWithApplicantModelArrayListNew.get(i).getId().equalsIgnoreCase(response.body().getFamilyModel().getRelationModel().getId())) {
                                        relationWithApplicantSpinner.setSelection(i);
                                    }
                                }
                            }

//                            TODO : relation value
                            if(response.body().getFamilyModel().getName().equalsIgnoreCase("Other")){
                                relationWithApplicantOthersLayout.setVisibility(View.VISIBLE);
                                relationWithApplicantOthersET.setVisibility(View.VISIBLE);
                                requestFocus(relationWithApplicantOthersET);
                                isValidRelationWithAPplicantOthers = true;
                                isValidRelationWithApplicant=false;
                                relationWithApplicantOthersET.setText(response.body().getFamilyModel().getRelation_name());
                            }

                            relationWithApplicantValue = Integer.parseInt(response.body().getFamilyModel().getRelation_id());

                            healthValue = response.body().getFamilyModel().getHealth_id();
                            addRadioButtonsForHealth(GlobalValue.applicantHealthArrayList,
                                    response.body().getFamilyModel().getHealthStatusModel().getId());

                            if(response.body().getFamilyModel().getEducation_id()==0){

                                chooseStudy("Yes");
                                educationLayout.setVisibility(View.VISIBLE);
                                educationToggle.setVisibility(View.GONE);
                                educationLevelToggle.setVisibility(View.VISIBLE);
                                typeOfSchoolToggle.setVisibility(View.VISIBLE);
                                typeOfSchoolHeading.setVisibility(View.VISIBLE);
                                classET.setVisibility(View.VISIBLE);
                                classLayout.setVisibility(View.VISIBLE);

                                educationLevelValue = response.body().getFamilyModel().getEducation_level_id();
                                addRadioButtonsForEducationLevel(GlobalValue.applicantEducationLevelArrayList,
                                        response.body().getFamilyModel().getEducationLevelsStatusModel().getId());

                                typeOfSchoolValue = response.body().getFamilyModel().getManagement_type_id();
                                addRadioButtonsForManagementType(GlobalValue.applicantManagementTypesList,
                                        response.body().getFamilyModel().getManagementTypesStatusModel().getId());
                                addRadioButtonsForEducationNoVal(GlobalValue.applicantEducationArrayList);
                                classET.setText(response.body().getFamilyModel().getStudying_class());

                            }else{

                                chooseStudy("No");
                                educationLayout.setVisibility(View.VISIBLE);
                                educationToggle.setVisibility(View.VISIBLE);
                                educationLevelToggle.setVisibility(View.GONE);
                                typeOfSchoolToggle.setVisibility(View.GONE);
                                typeOfSchoolHeading.setVisibility(View.GONE);
                                classET.setVisibility(View.GONE);
                                classLayout.setVisibility(View.GONE);

                                educationValue = response.body().getFamilyModel().getEducation_id();
                                addRadioButtonsForEducation(GlobalValue.applicantEducationArrayList,
                                        response.body().getFamilyModel().getEducationStatusModel().getId());
                                addRadioButtonsForManagementTypeNoVal(GlobalValue.applicantManagementTypesList);
                                addRadioButtonsForEducationLevelNoVal(GlobalValue.applicantEducationLevelArrayList);
                            }

                            try {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                                if(jsonObject.getJSONObject("family_member").has("jobs")){
                                    if(response.body().getFamilyModel().getJobModelArrayList().size()>0){
                                        chooseIncome("Yes");
                                        duplicatingLayout.setVisibility(View.VISIBLE);
                                        addJobTv.setVisibility(View.VISIBLE);


                                        for(int j = 0;j<response.body().getFamilyModel().getJobModelArrayList().size();j++){

                                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            final View rowView = inflater.inflate(R.layout.duplicating_member_information_layout, null);
                                            // Add the new row before the add field button.
                                            duplicatingLayout.addView(rowView, j);
                                            View chidLayout = duplicatingLayout.getChildAt(j);
                                            TextInputEditText inputJob = (TextInputEditText) chidLayout.findViewById(R.id.input_primary_job);
                                            TextView jobId = (TextView) chidLayout.findViewById(R.id.primary_job_id);
                                            TextInputEditText inputAmount = (TextInputEditText) chidLayout.findViewById(R.id.input_income_amount);
                                            TextView fixedTv = (TextView)chidLayout.findViewById(R.id.fixed_tv);
                                            TextView variableTv = (TextView)chidLayout.findViewById(R.id.variable_tv);

                                            inputJob.setText(response.body().getFamilyModel().getJobModelArrayList().get(j).getPrimaryJobModel().getName());
                                            jobId.setText(response.body().getFamilyModel().getJobModelArrayList().get(j).getPrimaryJobModel().getId());
                                            inputAmount.setText(response.body().getFamilyModel().getJobModelArrayList().get(j).getIncome());
                                            if(response.body().getFamilyModel().getJobModelArrayList().get(j).getCash_flow().equalsIgnoreCase("Fixed")){
                                                fixedTv.setTextAppearance(getApplicationContext(),R.style.MyRadioSelected);
                                                variableTv.setTextAppearance(getApplicationContext(),R.style.MyRadioNotSelected);
                                                fixedTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_selected));
                                                variableTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_not_selected));
                                            }else{

                                                variableTv.setTextAppearance(getApplicationContext(),R.style.MyRadioSelected);
                                                fixedTv.setTextAppearance(getApplicationContext(),R.style.MyRadioNotSelected);
                                                variableTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_selected));
                                                fixedTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_not_selected));

                                            }

                                        }

                                    }else{
                                        chooseIncome("No");
                                        duplicatingLayout.setVisibility(View.GONE);
                                        addJobTv.setVisibility(View.GONE);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }





                        } else {
                            NetworkUtils.handleErrorsForAPICalls(EditMemberInformationActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(EditMemberInformationActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<FamilyMemberResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(EditMemberInformationActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void addRadioButtonsForEducationNoVal(ArrayList<CIEducationStatusModel> applicantEducationArrayList) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < applicantEducationArrayList.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setText(applicantEducationArrayList.get(i).getName());
                rdbtn.setId(Integer.valueOf(applicantEducationArrayList.get(i).getId()));
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                educationToggle.addView(rdbtn);
            }
        }
    }


    private void addRadioButtonsForManagementTypeNoVal(ArrayList<CIManagementTypesStatusModel> applicantManagementTypesList){
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < applicantManagementTypesList.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setText(applicantManagementTypesList.get(i).getName());
                rdbtn.setId(Integer.valueOf(applicantManagementTypesList.get(i).getId()));
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                typeOfSchoolToggle.addView(rdbtn);
            }
        }

    }
    private void addRadioButtonsForEducationLevelNoVal(ArrayList<CIEducationLevelsStatusModel> applicantEducationLevelArrayList) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < applicantEducationLevelArrayList.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setText(applicantEducationLevelArrayList.get(i).getName());
                rdbtn.setId(Integer.valueOf(applicantEducationLevelArrayList.get(i).getId()));
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                educationLevelToggle.addView(rdbtn);
            }
        }
    }

    private void setCoApplicantData() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<FamilyMemberResponseModel> call = apiService.getCoApplicantData(loanTakerID
            );
            call.enqueue(new Callback<FamilyMemberResponseModel>() {
                @Override
                public void onResponse(Call<FamilyMemberResponseModel> call, final Response<FamilyMemberResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            setDataToForm(response.body().getFamilyModel());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(EditMemberInformationActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(EditMemberInformationActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<FamilyMemberResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(EditMemberInformationActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setApplicantData() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<FamilyMemberResponseModel> call = apiService.getApplicantData(loanTakerID
            );
            call.enqueue(new Callback<FamilyMemberResponseModel>() {
                @Override
                public void onResponse(Call<FamilyMemberResponseModel> call, final Response<FamilyMemberResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            setDataToForm(response.body().getFamilyModel());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(EditMemberInformationActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(EditMemberInformationActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<FamilyMemberResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(EditMemberInformationActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setDataToForm(FamilyModel familyModel) {


        relationWithApplicantModelArrayListApplicant = new ArrayList<CCoARelationModel>();

        //add select to the drop down in position 0

        CCoARelationModel cCoARelationModel = new CCoARelationModel();
        cCoARelationModel.setId("0");
        cCoARelationModel.setName("Select");
        cCoARelationModel.setCode("0");
        relationWithApplicantModelArrayListApplicant.add(cCoARelationModel);

        CCoARelationModel cCoARelationModel1 = new CCoARelationModel();
        cCoARelationModel1.setId(familyModel.getRelationModel().getId());
        cCoARelationModel1.setName(familyModel.getRelationModel().getName());
        cCoARelationModel1.setCode(familyModel.getRelationModel().getCode());
        relationWithApplicantModelArrayListApplicant.add(cCoARelationModel1);

        setRelationWithApplicantSpinnerForApplicant(relationWithApplicantModelArrayListApplicant);

        nameET.setText(familyModel.getName());
        ageET.setText(familyModel.getAge());
        relationWithApplicantValue = Integer.parseInt(familyModel.getRelation_id());
//        relationWithApplicantET.setText(familyModel.getRelationModel().getName());

        nameET.setEnabled(false);
        ageET.setEnabled(false);
        relationWithApplicantSpinner.setEnabled(false);
//        relationWithApplicantET.setEnabled(false);
    }

    private void setRelationWithApplicantSpinnerForApplicant(ArrayList<CCoARelationModel> relationWithApplicantModelArrayListNew) {

        CCoARelationModel[] cCoARelationModels = new CCoARelationModel[relationWithApplicantModelArrayListNew.size()];

        for(int i=0;i<relationWithApplicantModelArrayListNew.size();i++){
            cCoARelationModels[i]= new CCoARelationModel();
            cCoARelationModels[i].setId(relationWithApplicantModelArrayListNew.get(i).getId());
            cCoARelationModels[i].setCode(relationWithApplicantModelArrayListNew.get(i).getCode());
            cCoARelationModels[i].setName(relationWithApplicantModelArrayListNew.get(i).getName());
        }


        relationWithApplicantSpinnerAdapter = new RelationWithApplicantSpinnerAdapter(EditMemberInformationActivity.this,
                R.layout.ms__list_item,
                cCoARelationModels);

        relationWithApplicantSpinner.setAdapter(relationWithApplicantSpinnerAdapter); // Set the custom adapter to the spinner


        relationWithApplicantSpinner.setSelection(1);

    }

    private void getRelationDropDown() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<CustomerCoRelationResponseModel> call = apiService.getRelationFamilyMembers();
            call.enqueue(new Callback<CustomerCoRelationResponseModel>() {
                @Override
                public void onResponse(Call<CustomerCoRelationResponseModel> call, final Response<CustomerCoRelationResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            Log.i("Drools", "" + response.body().getRelationModels().size());
                            if(response.body().getRelationModels().size() >0){
//
//                                relationModels =response.body().getRelationModels();
//                                GlobalValue.relationModels = relationModels;
//                                corelationsSpinnerAdapter = new CoApplicantRelationsSpinnerAdapter(EditMemberInformationActivity.this, R.layout.spinner_item,response.body().getRelationModels());
//                                relationWithApplicantET.setAdapter(corelationsSpinnerAdapter);

                                relationWithApplicantModelArrayList =response.body().getRelationModels();
                                GlobalValue.relationModels = relationWithApplicantModelArrayList;



                                //set values for relation with applicant dropdown
                                //initialize arraylist
                                relationWithApplicantModelArrayListNew = new ArrayList<CCoARelationModel>();

                                //add select to the drop down in position 0
                                CCoARelationModel cCoARelationModel = new CCoARelationModel();
                                cCoARelationModel.setId("0");
                                cCoARelationModel.setName("Select");
                                cCoARelationModel.setCode("0");
                                relationWithApplicantModelArrayListNew.add(cCoARelationModel);

                                //add rest of the relations
                                for(int i=0;i<GlobalValue.relationModels.size();i++){
                                    CCoARelationModel cCoARelationModel1 = new CCoARelationModel();
                                    cCoARelationModel1.setId(GlobalValue.relationModels.get(i).getId());
                                    cCoARelationModel1.setName(GlobalValue.relationModels.get(i).getName());
                                    cCoARelationModel1.setCode(GlobalValue.relationModels.get(i).getCode());

                                    relationWithApplicantModelArrayListNew.add(cCoARelationModel1);
                                }

                                setRelationWithApplicantSpinner(relationWithApplicantModelArrayListNew);
                                Log.i("Drools", "" + "done");
                            }



                        } else {
                            NetworkUtils.handleErrorsForAPICalls(EditMemberInformationActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(EditMemberInformationActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call <CustomerCoRelationResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(EditMemberInformationActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setRelationWithApplicantSpinner(ArrayList<CCoARelationModel> relationWithApplicantModelArrayListNew) {

        CCoARelationModel[] cCoARelationModels = new CCoARelationModel[relationWithApplicantModelArrayListNew.size()];

        for(int i=0;i<relationWithApplicantModelArrayListNew.size();i++){
            cCoARelationModels[i]= new CCoARelationModel();
            cCoARelationModels[i].setId(relationWithApplicantModelArrayListNew.get(i).getId());
            cCoARelationModels[i].setCode(relationWithApplicantModelArrayListNew.get(i).getCode());
            cCoARelationModels[i].setName(relationWithApplicantModelArrayListNew.get(i).getName());
        }


        relationWithApplicantSpinnerAdapter = new RelationWithApplicantSpinnerAdapter(EditMemberInformationActivity.this,
                R.layout.ms__list_item,
                cCoARelationModels);

        relationWithApplicantSpinner.setAdapter(relationWithApplicantSpinnerAdapter); // Set the custom adapter to the spinner
    }

    private void getCashIncomeCommonDatas() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<CashIncomeCommonDataResponseModel> call = apiService.getCashIncomeCommonDatas();
            call.enqueue(new Callback<CashIncomeCommonDataResponseModel>() {
                @Override
                public void onResponse(Call<CashIncomeCommonDataResponseModel> call, final Response<CashIncomeCommonDataResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            GlobalValue.applicantHealthArrayList = response.body().getHealthStatusModelArrayList();
                            GlobalValue.applicantEducationArrayList = response.body().getEducationStatusModelArrayList();
                            GlobalValue.applicantEducationLevelArrayList =  response.body().getEducationLevelsStatusModelArrayList();
                            GlobalValue.applicantManagementTypesList = response.body().getManagementTypesStatusModelArrayList();

                            getMemberInformation();


                        } else {
                            NetworkUtils.handleErrorsForAPICalls(EditMemberInformationActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(EditMemberInformationActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<CashIncomeCommonDataResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(EditMemberInformationActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void addRadioButtonsForEducationLevel(ArrayList<CIEducationLevelsStatusModel> applicantEducationLevelArrayList, String id) {

        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < applicantEducationLevelArrayList.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setText(applicantEducationLevelArrayList.get(i).getName());
                rdbtn.setId(Integer.valueOf(applicantEducationLevelArrayList.get(i).getId()));
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                if(applicantEducationLevelArrayList.get(i).getId().equalsIgnoreCase(id)){
                    Log.i("Drools", "" + id);
                    rdbtn.setChecked(true);
                }
                rdbtn.setPadding(64,32,64,32);
                educationLevelToggle.addView(rdbtn);
            }
        }
    }

    private void addRadioButtonsForEducation(ArrayList<CIEducationStatusModel> applicantEducationArrayList, String id) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < applicantEducationArrayList.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setText(applicantEducationArrayList.get(i).getName());
                rdbtn.setId(Integer.valueOf(applicantEducationArrayList.get(i).getId()));
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                if(applicantEducationArrayList.get(i).getId().equalsIgnoreCase(id)){
                    Log.i("Drools", "" + id);
                    rdbtn.setChecked(true);
                }
                rdbtn.setPadding(64,32,64,32);
                educationToggle.addView(rdbtn);
            }
        }
    }

    private void addRadioButtonsForManagementType(ArrayList<CIManagementTypesStatusModel> applicantManagementTypesList, String id) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < applicantManagementTypesList.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setText(applicantManagementTypesList.get(i).getName());
                rdbtn.setId(Integer.valueOf(applicantManagementTypesList.get(i).getId()));
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                if(applicantManagementTypesList.get(i).getId().equalsIgnoreCase(id)){
                    Log.i("Drools", "" + id);
                    rdbtn.setChecked(true);
                }
                rdbtn.setPadding(64,32,64,32);
                typeOfSchoolToggle.addView(rdbtn);
            }
        }
    }

    private void addRadioButtonsForHealth(ArrayList<CIHealthStatusModel> applicantHealthArrayList,String id) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < applicantHealthArrayList.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setText(applicantHealthArrayList.get(i).getName());
                rdbtn.setId(Integer.valueOf(applicantHealthArrayList.get(i).getId()));
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                if(applicantHealthArrayList.get(i).getId().equalsIgnoreCase(id)){
                    Log.i("Drools", "" + id);
                    rdbtn.setChecked(true);
                }
                rdbtn.setPadding(64,32,64,32);
                healthToggle.addView(rdbtn);
            }
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
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.study_yes_tv:
                chooseStudy("Yes");
                educationLayout.setVisibility(View.VISIBLE);

                educationToggle.setVisibility(View.GONE);
                educationLevelStatusErrorTv.setVisibility(View.VISIBLE);
                educationLevelToggle.setVisibility(View.VISIBLE);

                typeOfSchoolToggle.setVisibility(View.VISIBLE);
                typeOfSchoolHeading.setVisibility(View.VISIBLE);
                typeOfSchoolErrorTV.setVisibility(View.VISIBLE);

                educationStatusErrorTv.setVisibility(View.GONE);
                classET.setVisibility(View.VISIBLE);
                classLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.study_no_tv:
                chooseStudy("No");
                educationLayout.setVisibility(View.VISIBLE);
                educationToggle.setVisibility(View.VISIBLE);
                educationLevelToggle.setVisibility(View.GONE);
                educationLevelStatusErrorTv.setVisibility(View.GONE);

                typeOfSchoolToggle.setVisibility(View.GONE);
                typeOfSchoolHeading.setVisibility(View.GONE);
                educationStatusErrorTv.setVisibility(View.VISIBLE);
                typeOfSchoolErrorTV.setVisibility(View.GONE);
                classET.setVisibility(View.GONE);
                classLayout.setVisibility(View.GONE);
                break;

            case R.id.income_yes_tv:
                chooseIncome("Yes");
                duplicatingLayout.setVisibility(View.VISIBLE);
                addJobTv.setVisibility(View.VISIBLE);
                isValidJob = true;

                break;

            case R.id.income_no_tv:
                chooseIncome("No");
                duplicatingLayout.setVisibility(View.GONE);
                addJobTv.setVisibility(View.GONE);
                isValidJob = false;
                break;


            case R.id.add_member_button:

                jobModelArrayList = new ArrayList<JobModel>();
                JobModel jobModel = null;
                for (int i = 0; i < duplicatingLayout.getChildCount() - 1; i++) {
                    View chidLayout = duplicatingLayout.getChildAt(i);
                    final TextInputEditText inputJob = (TextInputEditText) chidLayout.findViewById(R.id.input_primary_job);
                    TextView jobId = (TextView) chidLayout.findViewById(R.id.primary_job_id);
                    final TextInputLayout jobLayout = (TextInputLayout)chidLayout.findViewById(R.id.input_layout_primary_job);
                    final TextInputEditText inputAmount = (TextInputEditText) chidLayout.findViewById(R.id.input_income_amount);
                    final TextInputLayout inputLayout = (TextInputLayout)chidLayout.findViewById(R.id.input_layout_income_amount);
                    final TextView cashFlowStatus = (TextView)chidLayout.findViewById(R.id.cash_flow_status_error_tv);



                    TextView fixedTv = (TextView)chidLayout.findViewById(R.id.fixed_tv);
                    TextView variableTv = (TextView)chidLayout.findViewById(R.id.variable_tv);
                    if (inputJob.getText().toString().length()>0 && inputAmount.getText().toString().length()>0 && isValidCashFlow) {
                        jobModel = new JobModel();
                        jobModel.setPrimary_job_id(jobId.getText().toString());
                        jobModel.setIncome(inputAmount.getText().toString());
                        if(fixedTv.getCurrentTextColor()==getResources().getColor(R.color.white)){
                            jobModel.setCash_flow("Fixed");
                        }else{
                            jobModel.setCash_flow("Variable");
                        }
                        Log.i("AAdhar", "" + jobModel.getCash_flow() + jobModel.getPrimary_job_id()+jobModel.getIncome());
                        jobModelArrayList.add(jobModel);
                    }else{
                        if (inputJob.getText().toString().trim().isEmpty()) {
                            inputJob.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            inputJob.setError(EditMemberInformationActivity.this.getString(R.string.field_required));
                            isValidPrimaryJob=false;
                        }

                        if (inputAmount.getText().toString().trim().isEmpty()) {
                            inputAmount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            inputLayout.setError(EditMemberInformationActivity.this.getString(R.string.field_required));
                            isValidIncomeAmount=false;
                        }

                        if(fixedTv.getCurrentTextColor()!=getResources().getColor(R.color.white) && variableTv.getCurrentTextColor()!=getResources().getColor(R.color.white)){
                            cashFlowStatus.setVisibility(View.VISIBLE);

                        }

                    }
                    Log.i("AAdhar", "" + jobModelArrayList.size());


                }

                if (NetworkUtils.haveNetworkConnection(this)) {
                    checkValidation();

                } else {
                    UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                }


                break;


            case R.id.add_job_tv:
                isValidPrimaryJob=false;
                isValidIncomeAmount=false;
                isValidCashFlow=false;
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.duplicating_member_information_layout, null);
                // Add the new row before the add field button.
                duplicatingLayout.addView(rowView, duplicatingLayout.getChildCount() - 1);

                final TextInputEditText jobTv = (TextInputEditText)rowView.findViewById(R.id.input_primary_job);
                final TextInputLayout jobLayout = (TextInputLayout)rowView.findViewById(R.id.input_layout_primary_job);
                final TextInputEditText inputAmount = (TextInputEditText) rowView.findViewById(R.id.input_income_amount);
                final TextInputLayout inputLayout = (TextInputLayout)rowView.findViewById(R.id.input_layout_income_amount);
                final TextView cashFlowStatus = (TextView)rowView.findViewById(R.id.cash_flow_status_error_tv);

                inputAmount.addTextChangedListener(new TextWatcher() {
                    ArrayList<String> itemPassed = new ArrayList<String>();
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        itemPassed.clear();
                        itemPassed.add("");
                        boolean amountStatus = UiUtils.checkValidation(EditMemberInformationActivity.this, inputAmount, inputLayout, itemPassed);

                        if (amountStatus == false) {

                            isValidIncomeAmount=false;
                            requestFocus(inputAmount);

                        } else {

                            isValidIncomeAmount=true;
                            inputLayout.setErrorEnabled(false);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        itemPassed.clear();
                        itemPassed.add("");
                        boolean amountStatus = UiUtils.checkValidation(EditMemberInformationActivity.this, inputAmount, inputLayout, itemPassed);

                        if (amountStatus == false) {

                            isValidIncomeAmount=false;
                            requestFocus(inputAmount);

                        } else {

                            isValidIncomeAmount=true;
                            inputLayout.setErrorEnabled(false);
                        }
                    }
                });


                jobTv.addTextChangedListener(new TextWatcher() {
                    ArrayList<String> itemPassed = new ArrayList<String>();
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        itemPassed.clear();
                        itemPassed.add("");
                        boolean jobStatus = UiUtils.checkValidation(EditMemberInformationActivity.this, jobTv, inputLayout, itemPassed);

                        if (jobStatus == false) {

                            isValidPrimaryJob=false;
                            requestFocus(jobTv);

                        } else {

                            isValidPrimaryJob=true;
                            inputLayout.setErrorEnabled(false);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        itemPassed.clear();
                        itemPassed.add("");
                        boolean jobStatus = UiUtils.checkValidation(EditMemberInformationActivity.this, jobTv, inputLayout, itemPassed);

                        if (jobStatus == false) {

                            isValidPrimaryJob=false;
                            requestFocus(jobTv);

                        } else {
                            isValidPrimaryJob=true;
                            inputLayout.setErrorEnabled(false);
                        }
                    }
                });



                jobTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(EditMemberInformationActivity.this, SelectPrimaryJobActivity.class);
                        intent.putExtra("code","root");
                        startActivityForResult(intent,duplicatingLayout.getChildCount()-1);
                    }
                });

                final TextView fixedTv = (TextView)rowView.findViewById(R.id.fixed_tv);
                final TextView variableTv = (TextView)rowView.findViewById(R.id.variable_tv);

                fixedTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fixedTv.setTextAppearance(getApplicationContext(),R.style.MyRadioSelected);
                        variableTv.setTextAppearance(getApplicationContext(),R.style.MyRadioNotSelected);
                        fixedTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_selected));
                        variableTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_not_selected));
                        isValidCashFlow=true;
                        cashFlowStatus.setVisibility(View.GONE);
                    }
                });

                variableTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        variableTv.setTextAppearance(getApplicationContext(),R.style.MyRadioSelected);
                        fixedTv.setTextAppearance(getApplicationContext(),R.style.MyRadioNotSelected);
                        variableTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_selected));
                        fixedTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_not_selected));
                        isValidCashFlow=true;
                        cashFlowStatus.setVisibility(View.GONE);
                    }
                });



                Log.i("AAdhar", "" + duplicatingLayout.getChildCount());



                break;





        }

    }

    private void checkValidation(){


        if((isValidName) && (isValidAge) && (isValidRelationWithApplicant) && (isValidHealth) && (incomeChosed!=null) && (studyChosed!=null)){

            if(studyChosed.equalsIgnoreCase("true")){

                if((isValidEducationLevel) && (isValidTypeOfSchool) && (isValidClass)){

                    if(isValidJob){
                        if((isValidPrimaryJob) && (isValidIncomeAmount)  && (isValidCashFlow)){
                            callAPIForAddingMember();

                        }else{

                            Toast.makeText(this, "Please fill all the fields for job", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        callAPIForAddingMember();
                    }

                }else{

                    UiUtils.checkValidation(EditMemberInformationActivity.this, classET, classLayout, new ArrayList<String>());
                    if(educationLevelValue==0){
                        educationLevelStatusErrorTv.setText("Field Required");
                        educationLevelStatusErrorTv.setVisibility(View.VISIBLE);
                        isValidEducationLevel=false;
                    }else{
                        educationLevelStatusErrorTv.setVisibility(View.GONE);
                        isValidEducationLevel=true;
                    }

                    if(typeOfSchoolValue==0){
                        typeOfSchoolErrorTV.setText("Field Required");
                        typeOfSchoolErrorTV.setVisibility(View.VISIBLE);
                        isValidTypeOfSchool=false;
                    }else{
                        typeOfSchoolErrorTV.setVisibility(View.GONE);
                        isValidTypeOfSchool=true;
                    }

                }

            }else{



                if((isValidEducation)) {

                    if(isValidJob){
                        if((isValidPrimaryJob) && (isValidIncomeAmount)  && (isValidCashFlow)){
                            callAPIForAddingMember();

                        }else{

                            Toast.makeText(this, "Please fill all the fields for job", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        callAPIForAddingMember();
                    }

                }else{
                    if(educationValue==0){
                        educationStatusErrorTv.setText("Field Required");
                        educationStatusErrorTv.setVisibility(View.VISIBLE);
                        isValidEducation=false;
                    }else{
                        educationStatusErrorTv.setVisibility(View.GONE);
                        isValidEducation=true;
                    }

                }

            }






        }else{

            UiUtils.checkValidation(EditMemberInformationActivity.this, nameET, nameLayout, new ArrayList<String>());
            UiUtils.checkValidation(EditMemberInformationActivity.this, ageET, ageLayout, new ArrayList<String>());

            if(healthValue==0){
                healthErrorTv.setText("Field Required");
                healthErrorTv.setVisibility(View.VISIBLE);
                isValidHealth=false;
            }else{
                healthErrorTv.setVisibility(View.GONE);
                isValidHealth=true;
            }

            if(incomeChosed==null){
                incomeStatusErrorTV.setText("Field Required");
                incomeStatusErrorTV.setVisibility(View.VISIBLE);

            }

            if(studyChosed==null){
                stuadyStatusErrorTv.setText("Field Required");
                stuadyStatusErrorTv.setVisibility(View.VISIBLE);
            }


            if(!isValidRelationWithApplicant){
                if(isValidRelationWithAPplicantOthers){
                    UiUtils.checkValidation(EditMemberInformationActivity.this, relationWithApplicantOthersET, relationWithApplicantOthersLayout, new ArrayList<String>());


                }else{
                    relationWithApplicantSpinnerErrorTv.setText("Field Required");
                    relationWithApplicantSpinnerErrorTv.setVisibility(View.VISIBLE);
                    isValidRelationWithApplicant=false;
                }


            }


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

            switch (view.getId()) {
                case R.id.input_name:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean status = UiUtils.checkValidation(EditMemberInformationActivity.this, nameET, nameLayout, itemPassed);

                    if (status == false) {
                        isValidName = false;
                        requestFocus(nameET);
                    } else {
                        isValidName = true;
                        nameLayout.setErrorEnabled(false);


                    }
                    break;
                case R.id.input_age:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean ageStatus = UiUtils.checkValidation(EditMemberInformationActivity.this, ageET, ageLayout, itemPassed);

                    if (ageStatus == false) {
                        isValidAge = false;
                        requestFocus(ageET);
                    } else {
                        isValidAge = true;
                        ageLayout.setErrorEnabled(false);
                    }

                    break;




                case R.id.input_class:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean classStatus = UiUtils.checkValidation(EditMemberInformationActivity.this, classET, classLayout, itemPassed);

                    if (classStatus == false) {
                        isValidClass = false;
                        requestFocus(classET);

                    } else {
                        isValidClass = true;
                        classLayout.setErrorEnabled(false);
                    }

                    break;




                case R.id.input_relation_with_applicant:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean relationSTatus = UiUtils.checkValidationForAutoCompleteTV(EditMemberInformationActivity.this, relationWithApplicantET, relationWithApplicantLayout, itemPassed);

                    if (relationSTatus == false) {
                        isValidRelationWithApplicant = false;
                        requestFocus(relationWithApplicantET);

                    } else {
                        isValidRelationWithApplicant = true;
                        relationWithApplicantLayout.setErrorEnabled(false);
                    }

                    break;




                case R.id.input_relation_with_applicant_others:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean relationOthersSTatus = UiUtils.checkValidation(EditMemberInformationActivity.this, relationWithApplicantOthersET, relationWithApplicantOthersLayout, itemPassed);

                    if (relationOthersSTatus == false) {
                        isValidRelationWithAPplicantOthers = false;
                        requestFocus(relationWithApplicantET);

                    } else {
                        isValidRelationWithAPplicantOthers = true;
                        isValidRelationWithApplicant=true;
                        relationWithApplicantOthersLayout.setErrorEnabled(false);
                    }

                    break;



            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == duplicatingLayout.getChildCount()-1 && resultCode == RESULT_OK) {
            Log.i("AAdhar", "requestCode" + requestCode);
            for (int i = 0; i < duplicatingLayout.getChildCount() - 1; i++) {
                View chidLayout = duplicatingLayout.getChildAt(i);
                TextInputEditText inputJob = (TextInputEditText) chidLayout.findViewById(R.id.input_primary_job);
                TextView jobId = (TextView)chidLayout.findViewById(R.id.primary_job_id);
                if(requestCode == i+1){
                    inputJob.setText((data.getStringExtra(SelectLanguageActivity.CUSTOM_SEARCH_TEXT)));
                    jobId.setText((data.getStringExtra(SelectLanguageActivity.CUSTOM_SEARCH_ID)));
                }



            }
        }
    }


    public void onDelete(View view) {

        duplicatingLayout.removeView((View) view.getParent());
        isValidCashFlow=true;
        isValidPrimaryJob=true;
        isValidIncomeAmount=true;
    }


    private void callAPIForAddingMember() {

        addMemberButton.setEnabled(false);
        addMemberButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(EditMemberInformationActivity.this);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("family_member[name]", nameET.getText().toString().trim());
        builder.addFormDataPart("family_member[age]", ageET.getText().toString().trim());
        builder.addFormDataPart("family_member[relation_id]", String.valueOf(relationWithApplicantValue));
        builder.addFormDataPart("family_member[health_id]", String.valueOf(healthValue));
        if(isValidEducation){
            builder.addFormDataPart("family_member[education_id]", String.valueOf(educationValue));
        }else{
            builder.addFormDataPart("family_member[education_id]", "0");
        }

        if(isValidEducationLevel){
            builder.addFormDataPart("family_member[education_level_id]", String.valueOf(educationLevelValue));
        }else {
            builder.addFormDataPart("family_member[education_level_id]", "0");
        }



        builder.addFormDataPart("family_member[management_type_id]", String.valueOf(typeOfSchoolValue));
        builder.addFormDataPart("family_member[studying_class]", classET.getText().toString().trim());

        if(incomeChosed=="true") {
            for (int i = 0; i < jobModelArrayList.size(); i++) {

                Log.d("Aadhar onResponse", "" + "jobs[" + i + "][primary_job_id]");

                Log.d("Aadhar onResponse", "" + jobModelArrayList.get(i).getIncome() + jobModelArrayList.get(i).getCash_flow());

                builder.addFormDataPart("jobs[" + i + "][primary_job_id]", jobModelArrayList.get(i).getPrimary_job_id());
                builder.addFormDataPart("jobs[" + i + "][income]", jobModelArrayList.get(i).getIncome());
                builder.addFormDataPart("jobs[" + i + "][cash_flow]", jobModelArrayList.get(i).getCash_flow());
            }
        }

//        builder.addFormDataPart("jobs[0][primary_job_id]", "1");
//        builder.addFormDataPart("jobs[0][income]", "23");
//        builder.addFormDataPart("jobs[0][cash_flow]", "Fixed");
//
//        builder.addFormDataPart("jobs[1][primary_job_id]", "1");
//        builder.addFormDataPart("jobs[1][income]", "30");
//        builder.addFormDataPart("jobs[1][cash_flow]", "Variable");
//        builder.addFormDataPart("family_member[relation_name]", pincode);


        RequestBody finalRequestBody = builder.build();
        ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
        Call<FamilyMemberResponseModel> call = apiService.updateFamilyMember(loanTakerID,finalRequestBody,familyMemberId
        );
        call.enqueue(new Callback<FamilyMemberResponseModel>() {
            @Override
            public void onResponse(Call<FamilyMemberResponseModel> call, Response<FamilyMemberResponseModel> response) {
                hideProgressDialog();
                Log.d("Aadhar onResponse", "" + response.code() + "--" + response.message());
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        //API Success is true

                        addMemberButton.setEnabled(true);
                        addMemberButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        Toast.makeText(EditMemberInformationActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        goToFamilyMemberListing();
                        updateStatusInCashIncomePage();

                    } else {
                        Log.d("Aadhar onResponse", "" + response.body().getErrors());
                        addMemberButton.setEnabled(true);
                        addMemberButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        NetworkUtils.handleErrorsCasesForAPICalls(EditMemberInformationActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Aadhar onResponse", "" + response.body().getErrors());
                    addMemberButton.setEnabled(true);
                    addMemberButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(EditMemberInformationActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<FamilyMemberResponseModel> call, Throwable t) {
                hideProgressDialog();
                addMemberButton.setEnabled(true);
                addMemberButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                NetworkUtils.handleErrorsForAPICalls(EditMemberInformationActivity.this, null, null);
            }
        });
    }

    private void goToFamilyMemberListing() {

        finish();
    }

    private void  updateStatusInCashIncomePage(){
        FamilyMembersListingActivity.instance.init();
        CashIncomeDetailsActivity.instance.init();
        MemberDetailActivity.getInstance().init();
    }




    private void chooseIncome(String m) {

        if(m.equalsIgnoreCase("Yes")){
            incomeYesTv.setTextAppearance(getApplicationContext(),R.style.MyRadioSelected);
            incomeNoTv.setTextAppearance(getApplicationContext(),R.style.MyRadioNotSelected);
            incomeYesTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_selected));
            incomeNoTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_not_selected));
            incomeChosed="true";
            incomeStatusErrorTV.setVisibility(View.GONE);

        }else{

            incomeNoTv.setTextAppearance(getApplicationContext(),R.style.MyRadioSelected);
            incomeYesTv.setTextAppearance(getApplicationContext(),R.style.MyRadioNotSelected);
            incomeNoTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_selected));
            incomeYesTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_not_selected));
            incomeChosed = "false";
            incomeStatusErrorTV.setVisibility(View.GONE);
        }
    }


    private void chooseStudy(String m) {

        if(m.equalsIgnoreCase("Yes")){
            studyYesTv.setTextAppearance(getApplicationContext(),R.style.MyRadioSelected);
            studyNoTv.setTextAppearance(getApplicationContext(),R.style.MyRadioNotSelected);
            studyYesTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_selected));
            studyNoTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_not_selected));
            studyChosed="true";
            stuadyStatusErrorTv.setVisibility(View.GONE);

        }else{

            studyNoTv.setTextAppearance(getApplicationContext(),R.style.MyRadioSelected);
            studyYesTv.setTextAppearance(getApplicationContext(),R.style.MyRadioNotSelected);
            studyNoTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_selected));
            studyYesTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_not_selected));
            studyChosed = "false";
            stuadyStatusErrorTv.setVisibility(View.GONE);
        }
    }
}
