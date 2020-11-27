package com.tailwebs.aadharindia.housevisit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.center.CenterConfirmationActivity;
import com.tailwebs.aadharindia.center.CreateNewCenterActivity;
import com.tailwebs.aadharindia.housevisit.models.AgriculturalLandsModel;
import com.tailwebs.aadharindia.housevisit.models.FamilyTypesModel;
import com.tailwebs.aadharindia.housevisit.models.HouseTypesModel;
import com.tailwebs.aadharindia.housevisit.models.HouseVisitCommonDataResponseModel;
import com.tailwebs.aadharindia.housevisit.models.IllMembersModel;
import com.tailwebs.aadharindia.housevisit.models.KitchenTypesModel;
import com.tailwebs.aadharindia.housevisit.models.MarriedStatusModel;
import com.tailwebs.aadharindia.housevisit.models.NoOfCattlesModel;
import com.tailwebs.aadharindia.housevisit.models.NoOfRoomsModel;
import com.tailwebs.aadharindia.housevisit.models.RoofTypesModel;
import com.tailwebs.aadharindia.housevisit.models.SocialParametersModel;
import com.tailwebs.aadharindia.housevisit.models.StayLengthsModel;
import com.tailwebs.aadharindia.housevisit.models.ToiletsModel;
import com.tailwebs.aadharindia.housevisit.models.WallTypesModel;
import com.tailwebs.aadharindia.housevisit.models.houseinformationcreate.HouseInforationCreateResponseModel;
import com.tailwebs.aadharindia.member.RelationsSpinnerAdapter;
import com.tailwebs.aadharindia.member.applicant.AddCustomerDetailsActivity;
import com.tailwebs.aadharindia.member.applicant.LoanDetailsActivity;
import com.tailwebs.aadharindia.member.coapplicant.CoApplicantFamilyActivity;
import com.tailwebs.aadharindia.models.common.CACDMaritalStatusModel;
import com.tailwebs.aadharindia.models.common.CustomerApplicantCommonDataResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeadersForLocation;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.GpsTracker;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;
import com.tailwebs.aadharindia.utils.custom.multiselecttoggle.MultiSelectToggleGroup;
import com.tailwebs.aadharindia.utils.custom.singleselecttoggle.SingleSelectToggleGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseInfoCreateActivity extends BaseActivity implements View.OnClickListener {


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;



    //    house ownership
    @BindView(R.id.house_ownership_toggle_radiobutton)
    SingleSelectToggleGroup houseOwnerShipToggleButton;

    @BindView(R.id.house_ownership_error_tv)
    TextView houseOwerShipErrorTv;

    //roof structure

    @BindView(R.id.roof_structure_toggle_radiobutton)
    SingleSelectToggleGroup roofStructureToggleButton;

    @BindView(R.id.roof_structure_error_tv)
    TextView roofStructureErrorTv;


    ///wall structure

    @BindView(R.id.wall_structure_toggle_radiobutton)
    SingleSelectToggleGroup wallStructureToggleButton;

    @BindView(R.id.wall_structure_error_tv)
    TextView wallStructureErrorTv;


    //kitchen type
    @BindView(R.id.kitchen_type_toggle_radiobutton)
    SingleSelectToggleGroup kitchenTypeToggleButton;

    @BindView(R.id.kitchen_type_error_tv)
    TextView kitchenTypeErrorTv;



    //toilet
    @BindView(R.id.toilet_toggle_radiobutton)
    SingleSelectToggleGroup toiletToggleButton;

    @BindView(R.id.toilet_error_tv)
    TextView toiletErrorTv;


    //no of rooms
    @BindView(R.id.no_of_rooms_toggle_radiobutton)
    SingleSelectToggleGroup noOfRoomsToggleButton;

    @BindView(R.id.no_of_rooms_error_tv)
    TextView noOfRoomsErrorTv;


    //staty current
    @BindView(R.id.stay_current_toggle_radiobutton)
    SingleSelectToggleGroup stayCurrentToggleButton;

    @BindView(R.id.stay_current_error_tv)
    TextView stayCurrentErrorTv;


    //married since
    @BindView(R.id.married_since_toggle_radiobutton)
    SingleSelectToggleGroup marriedSinceToggleButton;

    @BindView(R.id.married_since_error_tv)
    TextView marriedSinceErrorTv;

    //family owned agriculture
    @BindView(R.id.family_owned_agriculture_toggle_radiobutton)
    SingleSelectToggleGroup familyOwnedAgricultureToggleButton;

    @BindView(R.id.family_owned_agriculture_error_tv)
    TextView familyOwnedAgricultureErrorTv;


    //no of cattle
    @BindView(R.id.no_of_cattle_toggle_radiobutton)
    SingleSelectToggleGroup noOfCattleToggleButton;

    @BindView(R.id.no_of_cattle_error_tv)
    TextView noOfCattleErrorTv;


    @BindView(R.id.social_parameters_toggle_radiobutton)
    MultiSelectToggleGroup socialParametersToggleButton;

    @BindView(R.id.social_parameter_error_tv)
    TextView socialParametersErrorTv;




    //family type
    @BindView(R.id.family_type_toggle_radiobutton)
    SingleSelectToggleGroup familyTypeToggleButton;

    @BindView(R.id.family_type_error_tv)
    TextView familyTypeErrorTv;


    //ill members
    @BindView(R.id.ill_members_toggle_radiobutton)
    SingleSelectToggleGroup illMembersToggleButton;

    @BindView(R.id.ill_members_error_tv)
    TextView illMembersErrorTv;

    @BindView(R.id.input_layout_members_in_the_family)
    TextInputLayout membersInTheFamilyLayout;

    @BindView(R.id.input_members_in_the_family)
    TextInputEditText membersInTheFamilyET;


    @BindView(R.id.input_layout_dependent_members)
    TextInputLayout dependentMembersLayout;

    @BindView(R.id.input_dependent_members)
    TextInputEditText dependentMembersET;

    @BindView(R.id.input_layout_smartphones)
    TextInputLayout smartPhonesLayout;

    @BindView(R.id.input_smartphones)
    TextInputEditText smartPhonesET;


    @BindView(R.id.continue_button)
    Button continueButton;

    private ProgressDialog mProgressDialog;

    String loanTakerID=null;


    ArrayList<String> values=new ArrayList<String>();


    int selectedHouseOwnerShipValue,selectedRoofStructureValue,selectedWallStructureValue,
    selectedKitchenValue,selectedToiletValue,selectedNOfRomsValue,selectedStayLocationValue,selectedMarriedSInceValue,
    selectedFamilyOwnedAgricultureValue,selectedNoOfCattleValue,selectedFamilyTypeValue,selectedIllMemberValue;


    private boolean isValidHouseOwnerShip = false,isValidRoofStructure=false,isValidWallStructure=false,
            isValidkitchenType=false,isValidToilet=false
            ,isValidNoOfRooms=false,isValidStayLocation=false,isValidMarriedSince=false,
            isValidFamilyOwnedAgriculture=false,isValidNoOfCattle=false,isValidSocialParameter=false,isValidFamilyType=false,
    isValidNoOfmemersInfamily=false,isValidIllMembers=false,isValidNoOfDependent=false,isValidNoOfSmartPhones=false;

    private GpsTracker gpsTracker;
    String currentLat=null,currentLong=null;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_info_create);
        ButterKnife.bind(this);


//        loanTakerID="165";
        loanTakerID = GlobalValue.loanTakerId;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("House Information");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "HV Info Create", null);


        //show current Lat and Long

        try {
            gpsTracker = new GpsTracker(HouseInfoCreateActivity.this);
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
            showProgressDialog(HouseInfoCreateActivity.this);
            getHouseVisitCommonDatas();
            getApplicantCommonDatas();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }


        membersInTheFamilyET.addTextChangedListener(new InputLayoutTextWatcher(membersInTheFamilyET));
        dependentMembersET.addTextChangedListener(new InputLayoutTextWatcher(dependentMembersET));
        smartPhonesET.addTextChangedListener(new InputLayoutTextWatcher(smartPhonesET));

        //house ownership
        houseOwnerShipToggleButton.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidHouseOwnerShip = true;
                selectedHouseOwnerShipValue = checkedId;
                houseOwerShipErrorTv.setVisibility(View.GONE);

            }
        });


        //roof structure
        roofStructureToggleButton.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidRoofStructure = true;
                selectedRoofStructureValue = checkedId;
                roofStructureErrorTv.setVisibility(View.GONE);

            }
        });


        //wall structure
        wallStructureToggleButton.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidWallStructure = true;
                selectedWallStructureValue = checkedId;
                wallStructureErrorTv.setVisibility(View.GONE);

            }
        });


        //kitchen type
        kitchenTypeToggleButton.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidkitchenType = true;
                selectedKitchenValue = checkedId;
                kitchenTypeErrorTv.setVisibility(View.GONE);

            }
        });


        //toilet
        toiletToggleButton.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidToilet = true;
                selectedToiletValue = checkedId;
                toiletErrorTv.setVisibility(View.GONE);

            }
        });


        //No of rooms
        noOfRoomsToggleButton.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidNoOfRooms = true;
                selectedNOfRomsValue = checkedId;
                noOfRoomsErrorTv.setVisibility(View.GONE);

            }
        });


        //stay location
        stayCurrentToggleButton.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidStayLocation = true;
                selectedStayLocationValue = checkedId;
                stayCurrentErrorTv.setVisibility(View.GONE);

            }
        });


        //married since
         marriedSinceToggleButton.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidMarriedSince = true;
                selectedMarriedSInceValue = checkedId;
                marriedSinceErrorTv.setVisibility(View.GONE);

            }
        });


        //family owned agriculture
        familyOwnedAgricultureToggleButton.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidFamilyOwnedAgriculture = true;
                selectedFamilyOwnedAgricultureValue = checkedId;
                familyOwnedAgricultureErrorTv.setVisibility(View.GONE);

            }
        });


        //no of cattle
        noOfCattleToggleButton.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidNoOfCattle = true;
                selectedNoOfCattleValue = checkedId;
                noOfCattleErrorTv.setVisibility(View.GONE);

            }
        });


        //family type
        familyTypeToggleButton.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidFamilyType = true;
                selectedFamilyTypeValue = checkedId;
                familyTypeErrorTv.setVisibility(View.GONE);

            }
        });


        //ill members
        illMembersToggleButton.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidIllMembers = true;
                selectedIllMemberValue = checkedId;
                illMembersErrorTv.setVisibility(View.GONE);

            }
        });

        //social parameters
        socialParametersToggleButton.setOnCheckedChangeListener(new MultiSelectToggleGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedStateChanged(MultiSelectToggleGroup group, int checkedId, boolean isChecked) {
                isValidSocialParameter = true;
                socialParametersErrorTv.setVisibility(View.GONE);
                Log.v("AAdhar", "onCheckedStateChanged(): " + checkedId + ", isChecked = " + isChecked);

                String chk;

                chk = Integer.toString(checkedId);
                if(isChecked){

                   values.add(chk);

                }else {
                  values.remove(chk);
                }

                Log.v("AAdhar", "onCheckedStateChanged(): " +values.size());

            }
        });


        continueButton.setOnClickListener(this);

    }


    private void getApplicantCommonDatas() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<CustomerApplicantCommonDataResponseModel> call = apiService.getCustomerApplicantCommonDatas();
            call.enqueue(new Callback<CustomerApplicantCommonDataResponseModel>() {
                @Override
                public void onResponse(Call<CustomerApplicantCommonDataResponseModel> call, final Response<CustomerApplicantCommonDataResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            GlobalValue.applicantLoanAmountArrayList = response.body().getCacdLoanAmountsModelArrayList();
                            GlobalValue.applicantLoanTenureArrayList = response.body().getCacdLoanTenuresModelArrayList();
                            GlobalValue.applicantLoanTakerRelationsArrayList =  response.body().getCacdLoanTakerRelationsModelArrayList();
                            GlobalValue.applicantLoanReasonsArrayList = response.body().getCacdLoanReasonsModelArrayList();
                            GlobalValue.applicantMaritalStatusArrayList = response.body().getCacdMaritalStatusModelArrayList();
                            GlobalValue.applicantCastesArrayList = response.body().getCacdCastesModelArrayList();
                            GlobalValue.applicantReligionsArrayList = response.body().getCacdReligionsModelArrayList();
                            GlobalValue.applicantRationCardTypesArrayList =  response.body().getCacdRationCardTypesModels();
                            GlobalValue.applicantSecondaryIDArrayList =  response.body().getCacdSecondaryIDModels();

                            GlobalValue.secondaryDocumentImageCount = response.body().getSecondary_document_image_count();
                            GlobalValue.bankStatementImageCount = response.body().getBank_statement_image_count();
                            GlobalValue.otherLoanCardImageCount = response.body().getOther_loan_card_image_count();




                        } else {
                            NetworkUtils.handleErrorsForAPICalls(HouseInfoCreateActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseInfoCreateActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<CustomerApplicantCommonDataResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(HouseInfoCreateActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void getHouseVisitCommonDatas() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<HouseVisitCommonDataResponseModel> call = apiService.getHouseVisitCommonDatas();
            call.enqueue(new Callback<HouseVisitCommonDataResponseModel>() {
                @Override
                public void onResponse(Call<HouseVisitCommonDataResponseModel> call, final Response<HouseVisitCommonDataResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true


                            GlobalValue.houseTypesModelArrayList = response.body().getHouseTypesModels();
                            GlobalValue.roofTypesModelArrayList = response.body().getRoofTypesModels();
                            GlobalValue.wallTypesModelArrayList = response.body().getWallTypesModels();
                            GlobalValue.kitchenTypesModelArrayList = response.body().getKitchenTypesModels();
                            GlobalValue.toiletsModelArrayList = response.body().getToiletsModels();
                            GlobalValue.noOfRoomsModelArrayList = response.body().getNoOfRoomsModels();
                            GlobalValue.stayLengthsModelArrayList =  response.body().getStayLengthsModels();
                            GlobalValue.marriedStatusModelArrayList = response.body().getMarriedStatusModels();
                            GlobalValue.agriculturalLandsModelArrayList = response.body().getAgriculturalLandsModels();
                            GlobalValue.cattlesModelArrayList = response.body().getNoOfCattlesModels();
                            GlobalValue.familyTypesModelArrayList = response.body().getFamilyTypesModels();
                            GlobalValue.illMembersModelArrayList = response.body().getIllMembersModels();
                            GlobalValue.houseImageCount = response.body().getHouse_image_count();
                            GlobalValue.socialParametersModelArrayList = response.body().getSocialParametersModels();


                            GlobalValue.pastEventsModelArrayList =  response.body().getPastEventsModels();
                            GlobalValue.upcomingEventsModelArrayList = response.body().getUpcomingEventsModels();


                            addRadioButtonsForHouseOwnerShip(GlobalValue.houseTypesModelArrayList);
                            addRadioButtonsForRoofStructure(GlobalValue.roofTypesModelArrayList);
                            addRadioButtonsForWallStructure(GlobalValue.wallTypesModelArrayList);
                            addRadioButtonsForKitchenType(GlobalValue.kitchenTypesModelArrayList);
                            addRadioButtonsForToilets(GlobalValue.toiletsModelArrayList);
                            addRadioButtonsForNoOfRooms(GlobalValue.noOfRoomsModelArrayList);
                            addRadioButtonsForStayLengths(GlobalValue.stayLengthsModelArrayList);
                            addRadioButtonsForMarriedSince(GlobalValue.marriedStatusModelArrayList);
                            addRadioButtonsForFamilyOwnedAgriculture(GlobalValue.agriculturalLandsModelArrayList);
                            addRadioButtonsForNoOfCattle(GlobalValue.cattlesModelArrayList);
                            addRadioButtonsForFamilyType(GlobalValue.familyTypesModelArrayList);
                            addRadioButtonsForIllMembers(GlobalValue.illMembersModelArrayList);
                            addCHeckBoxesForSocialParameters(GlobalValue.socialParametersModelArrayList);





                        } else {
                            NetworkUtils.handleErrorsForAPICalls(HouseInfoCreateActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseInfoCreateActivity.this, response.code(), response.body().getErrors());
                    }
                }



                @Override
                public void onFailure(Call<HouseVisitCommonDataResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(HouseInfoCreateActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
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



    public void addRadioButtonsForHouseOwnerShip(ArrayList<HouseTypesModel> value) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getId()));
                rdbtn.setText(value.get(i).getName());
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                houseOwnerShipToggleButton.addView(rdbtn);
            }
        }
    }


    public void addRadioButtonsForRoofStructure(ArrayList<RoofTypesModel> value) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getId()));
                rdbtn.setText(value.get(i).getName());
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                roofStructureToggleButton.addView(rdbtn);
            }
        }
    }


    public void addRadioButtonsForWallStructure(ArrayList<WallTypesModel> value) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getId()));
                rdbtn.setText(value.get(i).getName());
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                wallStructureToggleButton.addView(rdbtn);
            }
        }
    }


    public void addRadioButtonsForKitchenType(ArrayList<KitchenTypesModel> value) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getId()));
                rdbtn.setText(value.get(i).getName());
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                kitchenTypeToggleButton.addView(rdbtn);
            }
        }
    }


    public void addRadioButtonsForToilets(ArrayList<ToiletsModel> value) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getId()));
                rdbtn.setText(value.get(i).getName());
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                toiletToggleButton.addView(rdbtn);
            }
        }
    }

    public void addRadioButtonsForNoOfRooms(ArrayList<NoOfRoomsModel> value) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getId()));
                rdbtn.setText(value.get(i).getName());
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                noOfRoomsToggleButton.addView(rdbtn);
            }
        }
    }

    public void addRadioButtonsForStayLength(ArrayList<StayLengthsModel> value) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getId()));
                rdbtn.setText(value.get(i).getName());
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                stayCurrentToggleButton.addView(rdbtn);
            }
        }
    }

    public void addRadioButtonsForMarriedSince(ArrayList<MarriedStatusModel> value) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getId()));
                rdbtn.setText(value.get(i).getName());
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                marriedSinceToggleButton.addView(rdbtn);
            }
        }
    }

    public void addRadioButtonsForFamilyOwnedAgriculture(ArrayList<AgriculturalLandsModel> value) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getId()));
                rdbtn.setText(value.get(i).getName());
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                familyOwnedAgricultureToggleButton.addView(rdbtn);
            }
        }
    }

    public void addRadioButtonsForNoOfCattle(ArrayList<NoOfCattlesModel> value) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getId()));
                rdbtn.setText(value.get(i).getName());
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                noOfCattleToggleButton.addView(rdbtn);
            }
        }
    }

    public void addRadioButtonsForFamilyType(ArrayList<FamilyTypesModel> value) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getId()));
                rdbtn.setText(value.get(i).getName());
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                familyTypeToggleButton.addView(rdbtn);
            }
        }
    }

    public void addRadioButtonsForIllMembers(ArrayList<IllMembersModel> value) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getId()));
                rdbtn.setText(value.get(i).getName());
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                illMembersToggleButton.addView(rdbtn);
            }
        }
    }


    public void addRadioButtonsForStayLengths(ArrayList<StayLengthsModel> value) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getId()));
                rdbtn.setText(value.get(i).getName());
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                stayCurrentToggleButton.addView(rdbtn);
            }
        }
    }


    public void addCHeckBoxesForSocialParameters(ArrayList<SocialParametersModel> value) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {



                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                // left, top, right, bottom
                params.setMargins(0, 2 , 0, 2);
                params.gravity = Gravity.NO_GRAVITY;
                CheckBox checkBox = new CheckBox(this);
                checkBox.setId(Integer.valueOf(value.get(i).getId()));
                checkBox.setLayoutParams(params);
                checkBox.setText(value.get(i).getName());
                checkBox.setBackground(getResources().getDrawable(R.drawable.selector_bg_check_box));
                checkBox.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_check_box));
                checkBox.setPadding(64,48,64,48);
                checkBox.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                socialParametersToggleButton.addView(checkBox);

            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_button:

//                goToPersonaInformation();
                submitHouseInformationValue();
                break;


            case R.id.back_button:
                onBackPressed();
                break;
        }
    }

    private void submitHouseInformationValue() {
        if((isValidHouseOwnerShip) && (isValidRoofStructure) && (isValidWallStructure) && (isValidkitchenType) && (isValidToilet)
                && (isValidNoOfRooms) && (isValidStayLocation) && (isValidMarriedSince) && (isValidFamilyOwnedAgriculture) &&
        (isValidNoOfCattle) && (isValidFamilyType) && (isValidNoOfmemersInfamily) && (isValidIllMembers) && (isValidNoOfDependent)
                && (isValidNoOfSmartPhones)){

            callAPIForHouseInformation();

        }else{
            UiUtils.checkValidation(HouseInfoCreateActivity.this, membersInTheFamilyET, membersInTheFamilyLayout, new ArrayList<String>());
            UiUtils.checkValidation(HouseInfoCreateActivity.this, dependentMembersET, dependentMembersLayout, new ArrayList<String>());
            UiUtils.checkValidation(HouseInfoCreateActivity.this, smartPhonesET, smartPhonesLayout, new ArrayList<String>());

            if(selectedHouseOwnerShipValue==0){
                houseOwerShipErrorTv.setText("Field Required");
                houseOwerShipErrorTv.setVisibility(View.VISIBLE);
                isValidHouseOwnerShip=false;
            }else{
                houseOwerShipErrorTv.setVisibility(View.GONE);
                isValidHouseOwnerShip=true;
            }


            if(selectedRoofStructureValue==0){
                roofStructureErrorTv.setText("Field Required");
                roofStructureErrorTv.setVisibility(View.VISIBLE);
                isValidRoofStructure=false;
            }else{
                roofStructureErrorTv.setVisibility(View.GONE);
                isValidRoofStructure=true;
            }

            if(selectedWallStructureValue==0){
                wallStructureErrorTv.setText("Field Required");
                wallStructureErrorTv.setVisibility(View.VISIBLE);
                isValidWallStructure=false;
            }else{
                wallStructureErrorTv.setVisibility(View.GONE);
                isValidWallStructure=true;
            }


            if(selectedKitchenValue==0){
                kitchenTypeErrorTv.setText("Field Required");
                kitchenTypeErrorTv.setVisibility(View.VISIBLE);
                isValidkitchenType=false;
            }else{
                kitchenTypeErrorTv.setVisibility(View.GONE);
                isValidkitchenType=true;
            }

            if(selectedToiletValue==0){
                toiletErrorTv.setText("Field Required");
                toiletErrorTv.setVisibility(View.VISIBLE);
                isValidToilet=false;
            }else{
                toiletErrorTv.setVisibility(View.GONE);
                isValidToilet=true;
            }

            if(selectedNOfRomsValue==0){
                noOfRoomsErrorTv.setText("Field Required");
                noOfRoomsErrorTv.setVisibility(View.VISIBLE);
                isValidNoOfRooms=false;
            }else{
                noOfRoomsErrorTv.setVisibility(View.GONE);
                isValidNoOfRooms=true;
            }

            if(selectedStayLocationValue==0){
                stayCurrentErrorTv.setText("Field Required");
                stayCurrentErrorTv.setVisibility(View.VISIBLE);
                isValidStayLocation=false;
            }else{
                stayCurrentErrorTv.setVisibility(View.GONE);
                isValidStayLocation=true;
            }

            if(selectedMarriedSInceValue==0){
                marriedSinceErrorTv.setText("Field Required");
                marriedSinceErrorTv.setVisibility(View.VISIBLE);
                isValidMarriedSince=false;
            }else{
                marriedSinceErrorTv.setVisibility(View.GONE);
                isValidMarriedSince=true;
            }

            if(selectedFamilyOwnedAgricultureValue==0){
                familyOwnedAgricultureErrorTv.setText("Field Required");
                familyOwnedAgricultureErrorTv.setVisibility(View.VISIBLE);
                isValidFamilyOwnedAgriculture=false;
            }else{
                familyOwnedAgricultureErrorTv.setVisibility(View.GONE);
                isValidFamilyOwnedAgriculture=true;
            }

            if(selectedNoOfCattleValue==0){
                noOfCattleErrorTv.setText("Field Required");
                noOfCattleErrorTv.setVisibility(View.VISIBLE);
                isValidNoOfCattle=false;
            }else{
                noOfCattleErrorTv.setVisibility(View.GONE);
                isValidNoOfCattle=true;
            }

            if(selectedFamilyTypeValue==0){
                familyTypeErrorTv.setText("Field Required");
                familyTypeErrorTv.setVisibility(View.VISIBLE);
                isValidFamilyType=false;
            }else{
                familyTypeErrorTv.setVisibility(View.GONE);
                isValidFamilyType=true;
            }

            if(selectedIllMemberValue==0){
                illMembersErrorTv.setText("Field Required");
                illMembersErrorTv.setVisibility(View.VISIBLE);
                isValidIllMembers=false;
            }else{
                illMembersErrorTv.setVisibility(View.GONE);
                isValidIllMembers=true;
            }
        }

    }

    private void callAPIForHouseInformation() {

        continueButton.setEnabled(false);
        continueButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(HouseInfoCreateActivity.this);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("house_visit[house_type_id]", String.valueOf(selectedHouseOwnerShipValue));
        builder.addFormDataPart("house_visit[roof_type_id]", String.valueOf(selectedRoofStructureValue));
        builder.addFormDataPart("house_visit[wall_type_id]", String.valueOf(selectedWallStructureValue));
        builder.addFormDataPart("house_visit[kitchen_type_id]", String.valueOf(selectedKitchenValue));
        builder.addFormDataPart("house_visit[toilet_id]", String.valueOf(selectedToiletValue));
        builder.addFormDataPart("house_visit[num_of_room_id]", String.valueOf(selectedNOfRomsValue));
        builder.addFormDataPart("house_visit[stay_length_id]", String.valueOf(selectedStayLocationValue));
        builder.addFormDataPart("house_visit[married_since_id]", String.valueOf(selectedMarriedSInceValue));
        builder.addFormDataPart("house_visit[agricultural_land_id]", String.valueOf(selectedFamilyOwnedAgricultureValue));
        builder.addFormDataPart("house_visit[ill_member_id]", String.valueOf(selectedIllMemberValue));
        builder.addFormDataPart("house_visit[num_of_cattle_id]", String.valueOf(selectedNoOfCattleValue));
        builder.addFormDataPart("house_visit[family_type_id]", String.valueOf(selectedFamilyTypeValue));
        builder.addFormDataPart("house_visit[num_of_dependents_in_the_family]", dependentMembersET.getText().toString());
        builder.addFormDataPart("house_visit[num_of_smartphones_in_the_family]", smartPhonesET.getText().toString());
        builder.addFormDataPart("house_visit[num_of_members_in_the_family]", membersInTheFamilyET.getText().toString());

        // Multiple Social Parameters

        if (values.size() > 0) {
            for (int i = 0; i < values.size(); i++) {
                Log.d("Aadhaar India", ""+values.get(i));
                builder.addFormDataPart("house_visit[social_parameter_list][]", values.get(i));
            }
        }

        RequestBody finalRequestBody = builder.build();
        ApiInterface apiService;
        if(currentLat !=null && currentLong !=null) {
            apiService = RetrofitClientWithHeadersForLocation.getClient().create(ApiInterface.class);
        }else{
            apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
        }
        Call<HouseInforationCreateResponseModel> call = apiService.createHouseInformation(loanTakerID,finalRequestBody
        );
        call.enqueue(new Callback<HouseInforationCreateResponseModel>() {
            @Override
            public void onResponse(Call<HouseInforationCreateResponseModel> call, Response<HouseInforationCreateResponseModel> response) {
                hideProgressDialog();
                Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        //API Success is true
                        continueButton.setEnabled(true);
                        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));


                        Bundle params = new Bundle();
                        params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
                        params.putString("status","house_visit_info_created");
                        mFirebaseAnalytics.logEvent("house_visit_information", params);

                        Toast.makeText(HouseInfoCreateActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                        updateTheStatusInHouseDetailPage();
                        goToPersonaInformation();
                    } else {
                        Log.d("Drools onResponse", "" + response.body().getErrors());
                        continueButton.setEnabled(true);
                        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseInfoCreateActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Drools onResponse", "" + response.body().getErrors());
                    continueButton.setEnabled(true);
                    continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(HouseInfoCreateActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<HouseInforationCreateResponseModel> call, Throwable t) {
                hideProgressDialog();
                continueButton.setEnabled(true);
                continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                NetworkUtils.handleErrorsForAPICalls(HouseInfoCreateActivity.this, null, null);
            }
        });
    }

    private void goToPersonaInformation() {
        startActivity(new Intent(HouseInfoCreateActivity.this,PersonalInformationActivity.class));
        finish();

    }

    private void updateTheStatusInHouseDetailPage() {
        HouseDetailsActivity.getInstance().init();

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

                case R.id.input_members_in_the_family:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean membersStatus = UiUtils.checkValidation(HouseInfoCreateActivity.this, membersInTheFamilyET, membersInTheFamilyLayout, itemPassed);

                    if (membersStatus == false) {
                        isValidNoOfmemersInfamily = false;
                        requestFocus(membersInTheFamilyET);
                    } else {
                        isValidNoOfmemersInfamily = true;
                        membersInTheFamilyLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_dependent_members:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean dependentMembersStatus = UiUtils.checkValidation(HouseInfoCreateActivity.this, dependentMembersET, dependentMembersLayout, itemPassed);

                    if (dependentMembersStatus == false) {
                        isValidNoOfDependent = false;
                        requestFocus(dependentMembersET);
                    } else {
                        isValidNoOfDependent = true;
                        dependentMembersLayout.setErrorEnabled(false);
                    }

                    break;


                case R.id.input_smartphones:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean mobileStatus = UiUtils.checkValidation(HouseInfoCreateActivity.this, smartPhonesET, smartPhonesLayout, itemPassed);

                    if (mobileStatus == false) {
                        isValidNoOfSmartPhones = false;
                        requestFocus(smartPhonesET);
                    } else {
                        isValidNoOfSmartPhones = true;
                        smartPhonesLayout.setErrorEnabled(false);


                    }
                    break;




            }
        }

    }


    public void requestFocus(View view) {
        if (view.requestFocus())
            HouseInfoCreateActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(HouseInfoCreateActivity.this);
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
