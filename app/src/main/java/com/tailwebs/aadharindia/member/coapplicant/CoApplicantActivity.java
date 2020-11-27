package com.tailwebs.aadharindia.member.coapplicant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.center.searchinmap.adapters.AlternatePlaceAutocompleteAdapter;
import com.tailwebs.aadharindia.center.searchinmap.adapters.PlaceAutocompleteAdapter;
import com.tailwebs.aadharindia.center.searchinmap.models.PlaceInfo;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.RelationsSpinnerAdapter;
import com.tailwebs.aadharindia.member.applicant.AddCustomerDetailsActivity;
import com.tailwebs.aadharindia.member.applicant.EMICalculatorActivity;
import com.tailwebs.aadharindia.member.applicant.LoanDetailsActivity;
import com.tailwebs.aadharindia.member.applicant.spinners.RelationSpinnerAdapter;
import com.tailwebs.aadharindia.member.applicant.spinners.RelationWithApplicantSpinnerAdapter;
import com.tailwebs.aadharindia.member.coapplicant.models.LoanTakerCoApplicantDetailsResponseModel;
import com.tailwebs.aadharindia.models.city.CityResponseModel;
import com.tailwebs.aadharindia.models.common.CACDLoanTakerRelationsModel;
import com.tailwebs.aadharindia.models.common.CCoARelationModel;
import com.tailwebs.aadharindia.models.common.CustomerCoRelationResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeadersForLocation;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.GpsTracker;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;
import com.tailwebs.aadharindia.utils.custom.BetterSpinner;
import com.tailwebs.aadharindia.utils.custom.MaterialBetterSpinner;
import com.tailwebs.aadharindia.utils.custom.singleselecttoggle.SingleSelectToggleGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoApplicantActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = "CoApplicantActivity";
    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    //    CARD 1


    @BindView(R.id.input_layout_relation_with_applicant)
    TextInputLayout relationApplicantLayout;

    @BindView(R.id.input_relation_with_applicant)
    BetterSpinner relationApplicantET;

    @BindView(R.id.scan_aadhar_img)
    ImageView aadharScanImgView;

    @BindView(R.id.input_layout_aadhar_number)
    TextInputLayout aadharNumberLayout;

    @BindView(R.id.input_aadhar_number)
    TextInputEditText aadharNoET;

    @BindView(R.id.input_layout_contact_person)
    TextInputLayout contactPersonLayout;

    @BindView(R.id.input_contact_person)
    TextInputEditText contactPersonET;

    @BindView(R.id.input_day)
    TextInputEditText dayET;

    @BindView(R.id.input_month)
    TextInputEditText monthET;

    @BindView(R.id.input_year)
    TextInputEditText yearET;

    @BindView(R.id.input_layout_aadhar_co)
    TextInputLayout aadharCoLayout;

    @BindView(R.id.input_aadhar_co)
    TextInputEditText aadharCoET;

    @BindView(R.id.input_layout_aadhar_co_relation)
    TextInputLayout aadharCoRelationLayout;

    @BindView(R.id.input_aadhar_co_relation)
    BetterSpinner aadharCoRelationET;

    //address

    @BindView(R.id.input_layout_address_line1)
    TextInputLayout addressLine1Layout;

    @BindView(R.id.input_address_line1)
    TextInputEditText addressLine1ET;

    @BindView(R.id.input_layout_address_line2)
    TextInputLayout addressLine2Layout;

    @BindView(R.id.input_address_line2)
    TextInputEditText addressLine2ET;

    @BindView(R.id.input_city_layout)
    TextInputLayout cityLayout;

    @BindView(R.id.input_city)
    AutoCompleteTextView cityET;

    @BindView(R.id.input_layout_pin_code)
    TextInputLayout pinCodeLayout;

    @BindView(R.id.input_pin_code)
    TextInputEditText pinCodeET;

    @BindView(R.id.input_layout_district)
    TextInputLayout districtLayout;

    @BindView(R.id.input_district)
    TextInputEditText districtET;

    @BindView(R.id.input_layout_state)
    TextInputLayout stateLayout;

    @BindView(R.id.input_state)
    TextInputEditText stateET;


    @BindView(R.id.gender_group_single_radiobutton)
    SingleSelectToggleGroup genderToggle;




    //CARD 2

    //alternate address check box

    @BindView(R.id.alternate_layout)
    RelativeLayout alternateLayout;

    @BindView(R.id.alternate_address_check_box)
    CheckBox alternateAddressCheckbox;

    @BindView(R.id.alternate_check_box_tv)
    TextView alternateAddressTV;

    @BindView(R.id.alternate_address_layout)
    LinearLayout alternateAddressContentLayout;

    //alternate address

    @BindView(R.id.input_layout_alternate_address_line1)
    TextInputLayout addressLine1AlternateLayout;

    @BindView(R.id.input_alternate_address_line1)
    TextInputEditText addressLine1ALternateET;

    @BindView(R.id.input_layout_alternate_address_line2)
    TextInputLayout addressLine2AlternateLayout;

    @BindView(R.id.input_alternate_address_line2)
    TextInputEditText addressLine2AlternateET;

    @BindView(R.id.input_alternate_city_layout)
    TextInputLayout cityAlternateLayout;

    @BindView(R.id.input_alternate_city)
    AutoCompleteTextView cityAlternateET;

    @BindView(R.id.input_layout_alternate_pin_code)
    TextInputLayout pinCodeAlternateLayout;

    @BindView(R.id.input_alternate_pin_code)
    TextInputEditText pinCodeAlternateET;

    @BindView(R.id.input_layout_alternate_district)
    TextInputLayout districtAlternateLayout;

    @BindView(R.id.input_alternate_district)
    TextInputEditText districtAlternateET;

    @BindView(R.id.input_layout_alternate_state)
    TextInputLayout stateAlternateLayout;

    @BindView(R.id.input_alternate_state)
    TextInputEditText stateAlternateET;

    @BindView(R.id.continue_button)
    Button continueButton;


    //relation others

    @BindView(R.id.input_layout_relation_others)
    TextInputLayout relationOthersLayout;

    @BindView(R.id.input_relation_others)
    TextInputEditText relationOthersET;

    //applicant relation others

    @BindView(R.id.input_layout_relation_with_applicant_others)
    TextInputLayout relationWithApplicantOthersLayout;

    @BindView(R.id.input_relation_with_applicant_others)
    TextInputEditText relationWithApplicantOthersET;


    @BindView(R.id.dob_error_tv)
    TextView dobErrorTV;

    @BindView(R.id.gender_error_tv)
    TextView genderErrorTV;


    int relationValue=0,relationWithApplicantValue=0;

    String dobValue =null,genderValue=null,profilePicUriPath=null,cityGoogleId=null,alternateGoogleId=null;

    // variables to store extracted xml data
    String uid,name,gname,gender,yearOfBirth,dateOfBirth,dateOfBirthGuess,careOf,villageTehsil,postOffice,district,state,postCode,lm
            ,house,street,sub_district,loc;

    int dayInitial = 01;
    int monthInital = 00;
    int yearInital = 1970;



    int pickedDay = 0;
    int pickedMonth = 0;
    int pickedYear = 0;

    String dobValueFromIntent,genderValueFromIntent;
    private ProgressDialog mProgressDialog;


    public ArrayList<CCoARelationModel> relationWithApplicantModelArrayList = null,relationWithApplicantModelArrayListNew;
    public ArrayList<CACDLoanTakerRelationsModel> applicantLoanTakerRelationsArrayList = null,applicantLoanTakerRelationsArrayListNew;



    @BindView(R.id.relation_spinner)
    Spinner relationSpinner;

    RelationSpinnerAdapter relationSpinnerAdapter;

    @BindView(R.id.relation_spinner_error_tv)
    TextView relationSpinnerErrorTv;


    @BindView(R.id.relation_with_applicant_spinner)
    Spinner relationWithApplicantSpinner;

    RelationWithApplicantSpinnerAdapter relationWithApplicantSpinnerAdapter;

    @BindView(R.id.relation_with_applicant_spinner_error_tv)
    TextView relationWithApplicantSpinnerErrorTv;

    @BindView(R.id.input_layout_primary_mobile)
    TextInputLayout primaryMobileLayout;

    @BindView(R.id.input_primary_mobile)
    TextInputEditText primaryMobileET;

    @BindView(R.id.input_layout_secondary_mobile)
    TextInputLayout secondaryMobileLayout;

    @BindView(R.id.input_secondary_mobile)
    TextInputEditText secondaryMobileET;



    private boolean isValidRelationWithAPplicant = false,isValidAadhar = false, isValidNContactPerson = false, isValidDob = false,isValidGender = false,
            isValidAadharCo=false,isValidAddressLine1=false,isValidCity=false,isValidDistrict=false,
            isValidPinCode=false,isValidState=false,isValidRelation=false
            ,isResidingChecked=false,isValidRelationOthers=false,isScanned=false,isValidRelationWithAPplicantOthers=false,
            isValidAddressAlternateLine1=false,isValidAlternateCity=false,isValidAlternateDistrict=false,
            isValidAlternatePinCode=false,isValidAlternateState=false,dobFoundFromScanning=false;


    DatePickerDialog picker;

    //choose value from intent;
    String loanTakerID;


    private FirebaseAnalytics mFirebaseAnalytics;

    PlacesClient placesClient,alternatePlacesClient;
    AutoCompleteAdapter adapter;
    AlternateAutoCompleteAdapter alternateAutoCompleteAdapter;

    //for city

    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private AlternatePlaceAutocompleteAdapter alternatemPlaceAutocompleteAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace,altPlace;

    private GpsTracker gpsTracker;
    String currentLat=null,currentLong=null;
    private String blockCharacterSet = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_applicant);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        updateTheStatusInCoApplicantDetailPage();
        init();
        fillDetails(getIntent().getExtras());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Add Co-Applicant", null);
        loanTakerID =  GlobalValue.loanTakerId;
        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(CoApplicantActivity.this);
            getRelationDropDown();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }


        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Co-Applicant");
        headingTV.setTextAppearance(getApplicationContext(), R.style.MyActionBarHeading);



        String apiKey = getString(R.string.google_maps_key);
        if(apiKey.isEmpty()){
            Toast.makeText( CoApplicantActivity.this, "Google Api key is missing.", Toast.LENGTH_SHORT ).show();
            return;
        }


        // Setup Places Client
        if (!com.google.android.libraries.places.api.Places.isInitialized()) {
            com.google.android.libraries.places.api.Places.initialize(getApplicationContext(), apiKey);
        }

        placesClient = com.google.android.libraries.places.api.Places.createClient(this);
        alternatePlacesClient = com.google.android.libraries.places.api.Places.createClient(this);
        initAutoCompleteTextView();



        //show current Lat and Long

        try {
            gpsTracker = new GpsTracker(CoApplicantActivity.this);
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


        genderToggle.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {

                if(checkedId == 1){
                    genderValue = "Male";

                }else if(checkedId ==2){
                    genderValue = "Female";

                }else if(checkedId == 3)
                {
                    genderValue = "E";
                }
            }
        });

        aadharScanImgView.setOnClickListener(this);
        alternateAddressCheckbox.setOnCheckedChangeListener(this);
        dayET.setOnClickListener(this);
        monthET.setOnClickListener(this);
        yearET.setOnClickListener(this);

       
        aadharNoET.addTextChangedListener(new InputLayoutTextWatcher(aadharNoET));
        aadharCoET.addTextChangedListener(new InputLayoutTextWatcher(aadharCoET));
        contactPersonET.addTextChangedListener(new InputLayoutTextWatcher(contactPersonET));
        addressLine1ET.addTextChangedListener(new InputLayoutTextWatcher(addressLine1ET));
        districtET.addTextChangedListener(new InputLayoutTextWatcher(districtET));
        stateET.addTextChangedListener(new InputLayoutTextWatcher(stateET));
        pinCodeET.addTextChangedListener(new InputLayoutTextWatcher(pinCodeET));
        cityET.addTextChangedListener(new InputLayoutTextWatcher(cityET));
        aadharCoRelationET.addTextChangedListener(new InputLayoutTextWatcher(aadharCoRelationET));
        relationApplicantET.addTextChangedListener(new InputLayoutTextWatcher(relationApplicantET));
        relationOthersET.addTextChangedListener(new InputLayoutTextWatcher(relationOthersET));
        relationWithApplicantOthersET.addTextChangedListener(new InputLayoutTextWatcher(relationWithApplicantOthersET));
        addressLine1ALternateET.addTextChangedListener(new InputLayoutTextWatcher(addressLine1ALternateET));
        districtAlternateET.addTextChangedListener(new InputLayoutTextWatcher(districtAlternateET));
        stateAlternateET.addTextChangedListener(new InputLayoutTextWatcher(stateAlternateET));
        pinCodeAlternateET.addTextChangedListener(new InputLayoutTextWatcher(pinCodeAlternateET));
        cityAlternateET.addTextChangedListener(new InputLayoutTextWatcher(cityAlternateET));
        primaryMobileET.addTextChangedListener(new InputLayoutTextWatcher(primaryMobileET));
//        secondaryMobileET.addTextChangedListener(new InputLayoutTextWatcher(secondaryMobileET));


        contactPersonET.setFilters(new InputFilter[] { filter });
        aadharCoET.setFilters(new InputFilter[] { filter });


        //not needed
//
//        if(GlobalValue.placeId!=null){
//            cityGoogleId = GlobalValue.placeId;
//            getValuesFromCityID(GlobalValue.placeId);
//            cityET.setText(GlobalValue.placeAddress);
//        }
//



        relationWithApplicantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CCoARelationModel relationsModel = relationWithApplicantSpinnerAdapter.getItem(position);
                // Here you can do the action you want to...

                if (position==0){
                    relationWithApplicantValue = 0;
                    isValidRelationWithAPplicant=false;

                }


                if(position!=0){

                    isValidRelationWithAPplicant = true;
                    relationWithApplicantValue= Integer.parseInt(relationsModel.getId());
                    relationWithApplicantSpinnerErrorTv.setVisibility(View.GONE);
                    checkOthersApplicantRelation(relationsModel.getCode());


                }

                Log.i("Suhail","--"+relationWithApplicantValue);
                Log.i("Suhail","-id-"+relationsModel.getId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        relationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CACDLoanTakerRelationsModel relationsModel = relationSpinnerAdapter.getItem(position);
                // Here you can do the action you want to...

                if (position==0){
                    relationValue = 0;
                    isValidRelation=false;
                }


                if(position!=0){

                    isValidRelation = true;
                    relationValue= Integer.parseInt(relationsModel.getId());
                    relationSpinnerErrorTv.setVisibility(View.GONE);
                    checkOthersCoRelation(relationsModel.getCode());


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        continueButton.setOnClickListener(this);

        //for city auto complete

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("IN")
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();


        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, filter);

        alternatemPlaceAutocompleteAdapter = new AlternatePlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, filter);


//        cityAlternateET.setOnItemClickListener(alternateAutocompleteClickListener);
//        cityAlternateET.setAdapter(alternatemPlaceAutocompleteAdapter);
//
//        cityET.setOnItemClickListener(mAutocompleteClickListener);
//        cityET.setAdapter(mPlaceAutocompleteAdapter);

    }


    private void initAutoCompleteTextView() {


        cityET.setThreshold(1);
        cityET.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(this, placesClient);
        cityET.setAdapter(adapter);

        cityAlternateET.setThreshold(1);
        cityAlternateET.setOnItemClickListener(alternateautocompleteClickListener);
        alternateAutoCompleteAdapter = new AlternateAutoCompleteAdapter(this, alternatePlacesClient);
        cityAlternateET.setAdapter(alternateAutoCompleteAdapter);
    }

    private AdapterView.OnItemClickListener alternateautocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            try {
                final com.google.android.libraries.places.api.model.AutocompletePrediction item = alternateAutoCompleteAdapter.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }

//                To specify which data types to return, pass an array of Place.Fields in your FetchPlaceRequest
//                Use only those fields which are required.

                List<com.google.android.libraries.places.api.model.Place.Field> placeFields = Arrays.asList( com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.ADDRESS
                        , com.google.android.libraries.places.api.model.Place.Field.LAT_LNG, com.google.android.libraries.places.api.model.Place.Field.TYPES);

                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }

                if (request != null) {
                    alternatePlacesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {
//                            Toast.makeText( gpsTracker, "mdmdmd"+task.getPlace().getName() + "\n" +
//                                    task.getPlace().getAddress()+task.getPlace().getLatLng(), Toast.LENGTH_SHORT ).show();


                            Log.d(TAG, "onResult: name: " + task.getPlace().getName());
                            Log.d(TAG, "onResult: name: " + task.getPlace().getId());
                            Log.d(TAG, "onResult: name: " + task.getPlace().getAddress());
                            showProgressDialog(CoApplicantActivity.this);
                            alternateGoogleId = task.getPlace().getId();
                            getValuesFromAlternateCityID(alternateGoogleId);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText( CoApplicantActivity.this, "Cannot Retrieve Location", Toast.LENGTH_SHORT ).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    private AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            try {
                final com.google.android.libraries.places.api.model.AutocompletePrediction item = adapter.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();
                }

//                To specify which data types to return, pass an array of Place.Fields in your FetchPlaceRequest
//                Use only those fields which are required.

                List<com.google.android.libraries.places.api.model.Place.Field> placeFields = Arrays.asList( com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.ADDRESS
                        , com.google.android.libraries.places.api.model.Place.Field.LAT_LNG, com.google.android.libraries.places.api.model.Place.Field.TYPES);

                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }

                if (request != null) {
                    placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {
//                            Toast.makeText( gpsTracker, "mdmdmd"+task.getPlace().getName() + "\n" +
//                                    task.getPlace().getAddress()+task.getPlace().getLatLng(), Toast.LENGTH_SHORT ).show();


                            Log.d(TAG, "onResult: name: " + task.getPlace().getName());
                            Log.d(TAG, "onResult: name: " + task.getPlace().getId());
                            Log.d(TAG, "onResult: name: " + task.getPlace().getAddress());
                            showProgressDialog(CoApplicantActivity.this);
                            cityGoogleId = task.getPlace().getId();

                            getValuesFromCityID(cityGoogleId);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText( CoApplicantActivity.this, "Cannot Retrieve Location", Toast.LENGTH_SHORT ).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    private void init() {


        alternateAddressCheckbox.setChecked(true);
        isResidingChecked =true;
        alternateAddressTV.setTextColor(getResources().getColor(R.color.primaryColor));
        alternateLayout.setBackground(getResources().getDrawable(R.drawable.blue_bordered_box_bg));
        alternateAddressContentLayout.setVisibility(View.GONE);


//        relationsSpinnerAdapter = new RelationsSpinnerAdapter(CoApplicantActivity.this, R.layout.spinner_item,GlobalValue.applicantLoanTakerRelationsArrayList);
//        aadharCoRelationET.setAdapter(relationsSpinnerAdapter);

       if(getIntent().hasExtra("is_scanned")){
           if(getIntent().getExtras().getBoolean("is_scanned")){

           }else{
               addRadioButtons(GlobalValue.applicantGenderList);
           }
       }else{
           addRadioButtons(GlobalValue.applicantGenderList);
       }



    }

    private void fillDetails(Bundle bundle) {

        if(getIntent().hasExtra("is_scanned")){

            try {
            if(bundle.getBoolean("is_scanned")){
                isScanned = true;

                //aadhar
                uid= bundle.getString("aadhar_no");
                name = bundle.getString("username");
                gname = bundle.getString ("user_co_name");
                gender=  bundle.getString("gender");
                yearOfBirth= bundle.getString("yob");
                dateOfBirth=bundle.getString("dob");
                dateOfBirthGuess=bundle.getString("dobGuess");
                careOf=bundle.getString("co");
                lm=bundle.getString("lm");
                house=bundle.getString("house");
                street=bundle.getString("street");
                loc=bundle.getString("loc");
                villageTehsil=bundle.getString("vtc");
                postOffice=bundle.getString("po");
                district=bundle.getString("dist");
                sub_district=bundle.getString("subdist");
                state=bundle.getString("state");
                postCode=bundle.getString("pc");

                if (bundle.getString("aadhar_no") != null) {
                    aadharNoET.setText(bundle.getString("aadhar_no"));
                    aadharNoET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                    isValidAadhar = true;
                }
                if (bundle.getString("username") != null) {
                    contactPersonET.setText(bundle.getString("username"));
                    isValidNContactPerson =true;
                }

                if (bundle.getString("co") != null) {
                    if( (bundle.getString("co").contains(":"))){
                        String s =bundle.getString("co");
                        String s1 = s.substring(s.indexOf(":")+1);
                        s1.trim();
                        aadharCoET.setText(s1);
                    }else{
                        aadharCoET.setText(bundle.getString("co"));
                    }

                    isValidAadharCo = true;
                }






                if (bundle.getString("gender") != null) {
                    genderValueFromIntent= (bundle.getString("gender"));
                    if(genderValueFromIntent.equalsIgnoreCase("M")){
                        genderValue = "Male";
                        addRadioButtons( GlobalValue.applicantGenderList,genderValue);
                        isValidGender = true;
                    } else if(genderValueFromIntent.equalsIgnoreCase("F")) {
                        genderValue = "Female";
                        addRadioButtons( GlobalValue.applicantGenderList,genderValue);
                        isValidGender = true;
                    }else if(genderValueFromIntent.equalsIgnoreCase("Female")) {
                        genderValue = "Female";
                        addRadioButtons( GlobalValue.applicantGenderList,genderValue);
                        isValidGender = true;
                    }else if(genderValueFromIntent.equalsIgnoreCase("Male")){
                        genderValue = "Male";
                        addRadioButtons( GlobalValue.applicantGenderList,genderValue);
                        isValidGender = true;
                    }else{
                        isValidGender = false;
                        addRadioButtons( GlobalValue.applicantGenderList);
                    }
                }else{
                    isValidGender = false;
                    addRadioButtons(GlobalValue.applicantGenderList);
                }

//                if (bundle.getString("street") != null) {
//                    addressLine1ET.setText(bundle.getString("house")+","+bundle.getString("street"));
//                    isValidAddressLine1=true;
//
//                }
//                if (bundle.getString("loc") != null) {
//                    addressLine2ET.setText(bundle.getString("loc"));
//
//                }

//                if (bundle.getString("house") != null) {
//                    addressLine1ET.setText( bundle.getString("house"));
//                    addressLine2ET.setText( bundle.getString("street"));
//                    isValidAddressLine1 = true;
//
//                }



                isValidAddressLine1 = true;

                StringBuilder strBuilder = new StringBuilder();

                if(bundle.getString("house")!=null){
                    strBuilder.append(bundle.getString("house")+", ");
                }
                if(bundle.getString("street")!=null){
                    strBuilder.append(bundle.getString("street")+", ");
                }
                if(bundle.getString("lm")!=null){
                    strBuilder.append(bundle.getString("lm")+", ");
                }
                if(bundle.getString("loc")!=null){
                    strBuilder.append(bundle.getString("loc")+", ");
                }

                if(bundle.getString("po")!=null){
                    strBuilder.append(bundle.getString("po"));
                }
                String str = strBuilder.toString();

                addressLine1ET.setText(str);


                Log.d(TAG, "lm" + bundle.getString("lm"));
                Log.d(TAG, "house" + bundle.getString("house"));
                Log.d(TAG, "street" + bundle.getString("street"));
                Log.d(TAG, "loc" + bundle.getString("loc"));
                Log.d(TAG, "po" + bundle.getString("po"));

                if (bundle.getString("pc") != null) {
                    pinCodeET.setText( bundle.getString("pc"));
                    isValidPinCode = true;

                }

                if (bundle.getString("dob") != null) {


                    dobValueFromIntent=(bundle.getString("dob"));
                    if(dobValueFromIntent.contains("/")){
                        dayET.setText(UiUtils.getDate(dobValueFromIntent));
                        dayInitial = Integer.valueOf(UiUtils.getDate(dobValueFromIntent));

                        monthET.setText(UiUtils.getMonth(dobValueFromIntent));
                        monthInital = Integer.valueOf(UiUtils.getMonth(dobValueFromIntent));


                        yearET.setText(UiUtils.getYear(dobValueFromIntent));
                        yearInital = Integer.valueOf(UiUtils.getYear(dobValueFromIntent));
                        dobValue = dayInitial + "/" + monthInital + "/" + yearInital;
                        isValidDob = true;
                        dobFoundFromScanning=true;
                    }else if (dobValueFromIntent.contains("-")){
                        dayET.setText(UiUtils.getDateDash(dobValueFromIntent));
                        dayInitial = Integer.valueOf(UiUtils.getDateDash(dobValueFromIntent));

                        monthET.setText(UiUtils.getMonthDash(dobValueFromIntent));
                        monthInital = Integer.valueOf(UiUtils.getMonthDash(dobValueFromIntent));

                        yearET.setText(UiUtils.getYearDash(dobValueFromIntent));
                        yearInital = Integer.valueOf(UiUtils.getYearDash(dobValueFromIntent));

                        dobValue = dayInitial + "/" + monthInital + "/" + yearInital;
                        isValidDob = true;
                        dobFoundFromScanning=true;
                    }else {
                        dobFoundFromScanning=false;
                        isValidDob = false;
                    }
//

                }else{
                    dobFoundFromScanning=false;
                }


            }else{
                if (bundle.getString("aadhar_no") != null) {
                    aadharNoET.setText(bundle.getString("aadhar_no"));
                    aadharNoET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                    isValidAadhar = true;
                }
            }
            } catch (Exception e){
                e.printStackTrace();
            }
        }



    }

  /*
        --------------------------- google places API autocomplete suggestions -----------------
     */


    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    private AdapterView.OnItemClickListener alternateAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            hideSoftKeyboard();

            final AutocompletePrediction item = alternatemPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mAlternateUpdatePlaceDetailsCallback);
        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mAlternateUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {

            if(!places.getStatus().isSuccess()){
                Log.d("Aadhaar", "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{
                altPlace = new PlaceInfo();
                altPlace.setName(place.getName().toString());
                Log.d(TAG, "onResult: name: " + place.getName());
                altPlace.setAddress(place.getName().toString());
                Log.d(TAG, "onResult: address: " + place.getAddress());
                String address = place.getAddress().toString();

                //setting values
//                String result = address.substring(address.lastIndexOf(',') + 1).trim();
//                String[] bits = address.split(",");
//                String lastWord = bits[bits.length - 2];
//                stateAlternateET.setText(lastWord);
                altPlace.setId(place.getId());
                alternateGoogleId = place.getId();
                getValuesFromAlternateCityID(alternateGoogleId);

//                Log.d(TAG, "onResult: id:" + result+"--"+lastWord);
                altPlace.setLatlng(place.getLatLng());
                Log.d(TAG, "onResult: latlng: " + place.getLatLng());
                altPlace.setRating(place.getRating());
                Log.d(TAG, "onResult: rating: " + place.getRating());
                altPlace.setPhoneNumber(place.getPhoneNumber().toString());
                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());
                altPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: website uri: " + place.getWebsiteUri());
                Log.d(TAG, "onResult: place: " + altPlace.toString());

            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }


            places.release();
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {

            if(!places.getStatus().isSuccess()){
                Log.d("Aadhaar", "onResult: Place query did not complete successfully: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                Log.d(TAG, "onResult: name: " + place.getName());
                mPlace.setAddress(place.getName().toString());
                Log.d(TAG, "onResult: address: " + place.getAddress());
                String address = place.getAddress().toString();

                //setting values
//                String result = address.substring(address.lastIndexOf(',') + 1).trim();
//                String[] bits = address.split(",");
//                String lastWord = bits[bits.length - 2];
//                stateET.setText(lastWord);
                mPlace.setId(place.getId());
                cityGoogleId = place.getId();

                getValuesFromCityID(cityGoogleId);

//                Log.d(TAG, "onResult: id:" + result+"--"+lastWord);
                mPlace.setLatlng(place.getLatLng());
                Log.d(TAG, "onResult: latlng: " + place.getLatLng());
                mPlace.setRating(place.getRating());
                Log.d(TAG, "onResult: rating: " + place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                Log.d(TAG, "onResult: phone number: " + place.getPhoneNumber());
                mPlace.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: website uri: " + place.getWebsiteUri());
                Log.d(TAG, "onResult: place: " + mPlace.toString());

            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }


            places.release();
        }
    };


    private void checkOthersApplicantRelation(String code) {
        if(code.trim().equalsIgnoreCase("other")){

            Log.d("Aadhar","Other");

            relationWithApplicantOthersLayout.setVisibility(View.VISIBLE);
            relationWithApplicantOthersET.setVisibility(View.VISIBLE);
//            requestFocus(relationWithApplicantOthersET);
            isValidRelationWithAPplicantOthers = true;
            isValidRelationWithAPplicant=false;
        }else{
//            requestFocus(contactPersonET);
            relationWithApplicantOthersLayout.setVisibility(View.GONE);
            relationWithApplicantOthersET.setVisibility(View.GONE);
            Log.d("Aadhar","Not Other");
            isValidRelationWithAPplicantOthers = false;
            isValidRelationWithAPplicant=true;
        }

    }

    private void checkOthersCoRelation(String code) {
        if(code.trim().equalsIgnoreCase("other") || code.trim().equalsIgnoreCase("O")){

            Log.d("Aadhar","Other");

            relationOthersLayout.setVisibility(View.VISIBLE);
            relationOthersET.setVisibility(View.VISIBLE);
//            requestFocus(relationOthersET);
            isValidRelationOthers = true;
            isValidRelation=false;
        }else{
//            requestFocus(addressLine1ET);
            relationOthersLayout.setVisibility(View.GONE);
            relationOthersET.setVisibility(View.GONE);
            Log.d("Aadhar","Not Other");
            isValidRelationOthers = false;
            isValidRelation=true;
        }

    }
    private void getValuesFromAlternateCityID(String cityGoogleId) {
        showProgressDialog(CoApplicantActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);

            Call<CityResponseModel> call = apiService.getCityDatas(cityGoogleId);

            call.enqueue(new Callback<CityResponseModel>() {
                @Override
                public void onResponse(Call<CityResponseModel> call, Response<CityResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {

                        stateAlternateET.setText(response.body().getCity().getLocation_state_name());
                        districtAlternateET.setText(response.body().getCity().getDistrict_name());
//                        if(response.body().getCity().getPincode()!=null){
//                            pinCodeAlternateET.setText(response.body().getCity().getPincode());
//                        }


                    } else {
                        hideProgressDialog();
                        NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<CityResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantActivity.this, null, null);

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


            case R.id.scan_aadhar_img:
                showAlertDialogForScan();
                break;


            case R.id.continue_button:

                submitCoApplicantDetails();
                break;

            case R.id.input_day:
                pickUpFromCalendar();
                break;

            case R.id.input_month:
                pickUpFromCalendar();
                break;

            case R.id.input_year:
                pickUpFromCalendar();
                break;
        }
    }

    private void submitCoApplicantDetails() {
        if ((isValidRelationWithAPplicant) && (isValidAadhar) && (isValidNContactPerson)  && (isValidDob) &&
                (isValidGender) && (isValidAadharCo)  &&
                (isValidAddressLine1) && (isValidCity)  && (isValidDistrict) &&
                (isValidPinCode) && (isValidState) && (isValidRelation)    ){


            if(isResidingChecked){
                
                callAPIForCoApplicantSubmission();
                
               
            }else{
                if((isValidAddressAlternateLine1) && (isValidAlternateCity) && (isValidAlternateDistrict)
                        && (isValidAlternateState) && (isValidAlternatePinCode)){
                    callAPIForCoApplicantSubmission();
                    
                }else{
                    UiUtils.checkValidation(CoApplicantActivity.this, addressLine1ALternateET, addressLine1AlternateLayout, new ArrayList<String>());
                    UiUtils.checkValidationForAutoCompleteTV(CoApplicantActivity.this, cityAlternateET, cityAlternateLayout, new ArrayList<String>());
                    UiUtils.checkValidation(CoApplicantActivity.this, districtAlternateET, districtAlternateLayout, new ArrayList<String>());
                    UiUtils.checkValidation(CoApplicantActivity.this, pinCodeAlternateET, pinCodeAlternateLayout, new ArrayList<String>());
                    UiUtils.checkValidation(CoApplicantActivity.this, stateAlternateET, stateAlternateLayout, new ArrayList<String>());

                }
            }



        }else{
           UiUtils.checkValidation(CoApplicantActivity.this, aadharNoET, aadharNumberLayout, new ArrayList<String>());
            UiUtils.checkValidation(CoApplicantActivity.this, contactPersonET, contactPersonLayout, new ArrayList<String>());
            UiUtils.checkValidation(CoApplicantActivity.this, aadharCoET, aadharCoLayout, new ArrayList<String>());
            UiUtils.checkValidation(CoApplicantActivity.this, addressLine1ET, addressLine1Layout, new ArrayList<String>());
            UiUtils.checkValidationForAutoCompleteTV(CoApplicantActivity.this, cityET, cityLayout, new ArrayList<String>());
            UiUtils.checkValidation(CoApplicantActivity.this, districtET, districtLayout, new ArrayList<String>());
            UiUtils.checkValidation(CoApplicantActivity.this, pinCodeET, pinCodeLayout, new ArrayList<String>());
            UiUtils.checkValidation(CoApplicantActivity.this, stateET, stateLayout, new ArrayList<String>());

            if(dobValue==null){

                dobErrorTV.setText("Field Required");
                dobErrorTV.setVisibility(View.VISIBLE);
                isValidDob=false;
            }else{
                dobErrorTV.setVisibility(View.GONE);
                isValidDob=true;
            }

            if (genderValue==null){
                genderErrorTV.setText("Field Required");
                genderErrorTV.setVisibility(View.VISIBLE);
                isValidGender=false;
            }else{
                genderErrorTV.setVisibility(View.GONE);
                isValidGender=true;
            }


            if(!isValidRelation){
                if(isValidRelationOthers){
                    UiUtils.checkValidation(CoApplicantActivity.this, relationOthersET, relationOthersLayout, new ArrayList<String>());


                }else{
                    relationSpinnerErrorTv.setText("Field Required");
                    relationSpinnerErrorTv.setVisibility(View.VISIBLE);
                    isValidRelation=false;
                }


            }


            if(!isValidRelationWithAPplicant){
                if(isValidRelationWithAPplicantOthers){
                    UiUtils.checkValidation(CoApplicantActivity.this, relationWithApplicantOthersET, relationWithApplicantOthersLayout, new ArrayList<String>());


                }else{
                    relationWithApplicantSpinnerErrorTv.setText("Field Required");
                    relationWithApplicantSpinnerErrorTv.setVisibility(View.VISIBLE);
                    isValidRelationWithAPplicant=false;
                }


            }



            if(isResidingChecked==false){

                UiUtils.checkValidation(CoApplicantActivity.this, addressLine1ALternateET, addressLine1AlternateLayout, new ArrayList<String>());
                UiUtils.checkValidationForAutoCompleteTV(CoApplicantActivity.this, cityAlternateET, cityAlternateLayout, new ArrayList<String>());
                UiUtils.checkValidation(CoApplicantActivity.this, districtAlternateET, districtAlternateLayout, new ArrayList<String>());
                UiUtils.checkValidation(CoApplicantActivity.this, pinCodeAlternateET, pinCodeAlternateLayout, new ArrayList<String>());
                UiUtils.checkValidation(CoApplicantActivity.this, stateAlternateET, stateAlternateLayout, new ArrayList<String>());

            }

        }


    }

    private void callAPIForCoApplicantSubmission() {

        continueButton.setEnabled(false);
        continueButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(CoApplicantActivity.this);
        RequestBody is_residing = null;
        RequestBody relation_others =null;
        RequestBody application_relation_others =null;
        RequestBody alternate_address_line_1 = null;
        RequestBody alternate_address_line_2= null;
        RequestBody alternate_city_google_id = null;
        RequestBody alternate_pincode = null;
        RequestBody aadhar_uid=null,aadhar_name=null,aadhar_gname=null,aadhar_gender=null,aadhar_yearOfBirth=null,aadhar_dateOfBirth=null, aadhar_careOf=null,
                aadhar_lm =null,
                aadhar_house =null,
                aadhar_street=null,
                aadhar_loc =null,
                aadhar_villageTehsil =null,
                aadhar_postOffice =null,
                aadhar_district =null,
                aadhar_sub_district =null,
                aadhar_state=null,
                aadhar_postCode =null;;


        try {
            Log.d("Aadhar onResponse", "--start" );

//            Add Group Id TODO
            Log.d("Aadhar onResponse", "--start" +isValidGender+"--"+genderValue);

            RequestBody applicantRelationValue = RequestBody.create(MediaType.parse("text/plain"),   String.valueOf(relationWithApplicantValue));
            if(isValidRelationWithAPplicantOthers){
                application_relation_others = RequestBody.create(MediaType.parse("text/plain"), relationWithApplicantOthersET.getText().toString());
            }
            RequestBody aadharNo = RequestBody.create(MediaType.parse("text/plain"), aadharNoET.getText().toString());
            RequestBody aadharname = RequestBody.create(MediaType.parse("text/plain"), contactPersonET.getText().toString());
            RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), dobValue);
            Log.d("Aadhar onResponse", "--dobstart" );
            RequestBody aadhargender = RequestBody.create(MediaType.parse("text/plain"),genderValue);
            RequestBody co = RequestBody.create(MediaType.parse("text/plain"), aadharCoET.getText().toString());
            RequestBody co_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(relationValue));
            Log.d("Aadhar onResponse", "--co_idstart" );
            if(isValidRelationOthers){
                relation_others = RequestBody.create(MediaType.parse("text/plain"), relationOthersET.getText().toString());
            }

            RequestBody primaryPhoneNo = RequestBody.create(MediaType.parse("text/plain"), primaryMobileET.getText().toString());
            RequestBody secondaryPhoneNo = RequestBody.create(MediaType.parse("text/plain"), secondaryMobileET.getText().toString());

            Log.d("Aadhar onResponse", "--relation_othersstart" );
            RequestBody address_line_1 = RequestBody.create(MediaType.parse("text/plain"), addressLine1ET.getText().toString());
            RequestBody address_line_2 = RequestBody.create(MediaType.parse("text/plain"), addressLine2ET.getText().toString());
            RequestBody city_google_id = RequestBody.create(MediaType.parse("text/plain"), cityGoogleId);
            RequestBody pincode = RequestBody.create(MediaType.parse("text/plain"), pinCodeET.getText().toString());
            
            if (isResidingChecked) {
                is_residing = RequestBody.create(MediaType.parse("text/plain"), "true");
            }else{
                is_residing = RequestBody.create(MediaType.parse("text/plain"), "false");
                alternate_address_line_1 = RequestBody.create(MediaType.parse("text/plain"), addressLine1ALternateET.getText().toString());
                alternate_address_line_2 = RequestBody.create(MediaType.parse("text/plain"), addressLine2AlternateET.getText().toString());
                alternate_city_google_id = RequestBody.create(MediaType.parse("text/plain"), alternateGoogleId);
                alternate_pincode = RequestBody.create(MediaType.parse("text/plain"), pinCodeAlternateET.getText().toString());
            }

            if(isScanned) {

                Log.d("Aadhar onResponse", "--is Scanned");

                if (uid != null) {
                    aadhar_uid = RequestBody.create(MediaType.parse("text/plain"), uid);
                }

                Log.d("Aadhar onResponse", "--aadhar_uid");
                if (name != null) {
                    aadhar_name = RequestBody.create(MediaType.parse("text/plain"), name);
                }
                if (gname != null) {
                    aadhar_gname = RequestBody.create(MediaType.parse("text/plain"), gname);
                }
                if (gender != null) {
                    aadhar_gender = RequestBody.create(MediaType.parse("text/plain"), gender);
                }
                if (yearOfBirth != null) {
                    aadhar_yearOfBirth = RequestBody.create(MediaType.parse("text/plain"), yearOfBirth);
                }
                if (dateOfBirth != null) {
                    aadhar_dateOfBirth = RequestBody.create(MediaType.parse("text/plain"), dateOfBirth);
                }
                if (careOf != null) {
                    aadhar_careOf = RequestBody.create(MediaType.parse("text/plain"), careOf);
                }
                if (lm != null) {
                    aadhar_lm = RequestBody.create(MediaType.parse("text/plain"), lm);
                }
                if (house != null) {
                    aadhar_house = RequestBody.create(MediaType.parse("text/plain"), house);
                }
                if (street != null) {
                    aadhar_street = RequestBody.create(MediaType.parse("text/plain"), street);
                }
                if (loc != null) {
                    aadhar_loc = RequestBody.create(MediaType.parse("text/plain"), loc);
                }
                if (villageTehsil != null) {
                    aadhar_villageTehsil = RequestBody.create(MediaType.parse("text/plain"), villageTehsil);
                }
                if (postOffice != null) {
                    aadhar_postOffice = RequestBody.create(MediaType.parse("text/plain"), postOffice);
                }
                if (district != null) {
                    aadhar_district = RequestBody.create(MediaType.parse("text/plain"), district);
                }
                if (sub_district != null) {
                    aadhar_sub_district = RequestBody.create(MediaType.parse("text/plain"), sub_district);
                }
                if (state != null) {
                    aadhar_state = RequestBody.create(MediaType.parse("text/plain"), state);
                }
                if (postCode != null) {
                    aadhar_postCode = RequestBody.create(MediaType.parse("text/plain"), postCode);
                }
            }


            Log.d("Aadhar onResponse", "--end" );

            ApiInterface apiService;
            if(currentLat !=null && currentLong !=null) {
                apiService = RetrofitClientWithHeadersForLocation.getClient().create(ApiInterface.class);
            }else{
                apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            }
            Call<LoanTakerCoApplicantDetailsResponseModel> call;
            if(isScanned){
                call = apiService.addCoApplicantDetailsifScanned(
                        loanTakerID,
                        applicantRelationValue,
                        application_relation_others,
                        aadharNo,
                        aadharname,
                        dob,
                        aadhargender,
                        co,
                        co_id,
                        relation_others,
                        primaryPhoneNo,
                        secondaryPhoneNo,
                        address_line_1,
                        address_line_2,
                        city_google_id,
                        pincode,
                        is_residing,
                        alternate_address_line_1,
                        alternate_address_line_2,
                        alternate_city_google_id,
                        alternate_pincode,aadhar_uid,aadhar_name,aadhar_gender,aadhar_yearOfBirth,aadhar_dateOfBirth,aadhar_gname,aadhar_careOf,
                        aadhar_house,aadhar_street,aadhar_loc,aadhar_villageTehsil,aadhar_postOffice,aadhar_district,aadhar_sub_district,
                        aadhar_state,aadhar_postCode
                );
            }else{
                call = apiService.addCoApplicantDetails(
                        loanTakerID,
                        applicantRelationValue,
                        application_relation_others,
                        aadharNo,
                        aadharname,
                        dob,
                        aadhargender,
                        co,
                        co_id,
                        relation_others,
                        primaryPhoneNo,
                        secondaryPhoneNo,
                        address_line_1,
                        address_line_2,
                        city_google_id,
                        pincode,
                        is_residing,
                        alternate_address_line_1,
                        alternate_address_line_2,
                        alternate_city_google_id,
                        alternate_pincode
                );
            }

            call.enqueue(new Callback<LoanTakerCoApplicantDetailsResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCoApplicantDetailsResponseModel> call, Response<LoanTakerCoApplicantDetailsResponseModel> response) {
                    hideProgressDialog();
                    Log.d("Aadhar onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Aadhar onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            continueButton.setEnabled(true);
                            continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                            Log.d("Aadhar onResponse1", "" +  GlobalValue.loanTakerId);


                            Bundle params = new Bundle();
                            params.putString("co_applicant_name", response.body().getLoanTakerModel().getName());
                            params.putString("applicant_relation", response.body().getLoanTakerModel().getRelationModel().getName());
                            params.putString("co_applicant_city",response.body().getLoanTakerModel().getResidentAddressModel().getCity_name()+" - "+
                            response.body().getLoanTakerModel().getResidentAddressModel().getGoogle_place_id());
                            params.putString("co_applicant_gender",response.body().getLoanTakerModel().getGender());
                            params.putString("status","co_applicant_creation_started");
                            params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
                            mFirebaseAnalytics.logEvent("co_applicant", params);


                            Toast.makeText(CoApplicantActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            goToCoApplicantDocumentsPage();
                            updateTheStatusInCoApplicantDetailPage();


                        } else {
                            Log.d("Aadhar onResponse2", "" + response.body().getErrors());
                            continueButton.setEnabled(true);
                            continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                           

                            NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Aadhar onResponse3", "" + response.body().getErrors());
                        continueButton.setEnabled(true);
                        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        NetworkUtils.handleErrorsForAPICalls(CoApplicantActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCoApplicantDetailsResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    continueButton.setEnabled(true);
                    continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }


    }

    private void goToCoApplicantDocumentsPage() {
        Intent intent = new Intent(CoApplicantActivity.this,CoApplicantDocumentsActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateTheStatusInCoApplicantDetailPage() {
        CoApplicantDetailsActivity.getInstance().init();
        MemberDetailActivity.getInstance().init();
    }


    private void getValuesFromCityID(String cityGoogleId) {
        showProgressDialog(CoApplicantActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);

            Call<CityResponseModel> call = apiService.getCityDatas(cityGoogleId);

            call.enqueue(new Callback<CityResponseModel>() {
                @Override
                public void onResponse(Call<CityResponseModel> call, Response<CityResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {

                        stateET.setText(response.body().getCity().getLocation_state_name());
                        districtET.setText(response.body().getCity().getDistrict_name());
//                        if(response.body().getCity().getPincode()!=null){
//                            pinCodeET.setText(response.body().getCity().getPincode());
//                        }else{
//
//                            if(pinCodeET.getText().toString().length()>0)
//                                pinCodeET.setText("");
//                        }


                    } else {
                        hideProgressDialog();
                        NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<CityResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
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

                case R.id.input_aadhar_number:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean aadharStatus = UiUtils.checkValidation(CoApplicantActivity.this, aadharNoET, aadharNumberLayout, itemPassed);

                    if (aadharStatus == false) {
                        isValidAadhar = false;
                        requestFocus(aadharNoET);
                    } else {
                        isValidAadhar = true;
                        aadharNumberLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_aadhar_co:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean coStatus = UiUtils.checkValidation(CoApplicantActivity.this, aadharCoET, aadharCoLayout, itemPassed);

                    if (coStatus == false) {
                        isValidAadharCo = false;
                        requestFocus(aadharCoET);
                    } else {
                        isValidAadharCo = true;
                        aadharCoLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_contact_person:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean personStatus = UiUtils.checkValidation(CoApplicantActivity.this, contactPersonET, contactPersonLayout, itemPassed);

                    if (personStatus == false) {
                        isValidNContactPerson = false;
                        requestFocus(contactPersonET);

                    } else {
                        isValidNContactPerson=true;
                        contactPersonLayout.setErrorEnabled(false);
                    }

                    break;


                case R.id.input_primary_mobile:
                    itemPassed.clear();
                    itemPassed.add("mobile");
                    boolean primaryMobileStatus = UiUtils.checkValidation(CoApplicantActivity.this, primaryMobileET, primaryMobileLayout, itemPassed);

                    if (primaryMobileStatus == false) {
                        requestFocus(primaryMobileET);
                    } else {
                        primaryMobileLayout.setErrorEnabled(false);


                    }
                    break;



                case R.id.input_secondary_mobile:
                    itemPassed.clear();
                    itemPassed.add("mobile");
                    boolean secondaryMobileStatus = UiUtils.checkValidation(CoApplicantActivity.this, secondaryMobileET, secondaryMobileLayout, itemPassed);

                    if (secondaryMobileStatus == false) {
                        requestFocus(secondaryMobileET);
                    } else {
                        secondaryMobileLayout.setErrorEnabled(false);


                    }
                    break;


                case R.id.input_address_line1:
                    itemPassed.clear();
                    itemPassed.add("address");
                    boolean addressLine1Status = UiUtils.checkValidation(CoApplicantActivity.this, addressLine1ET, addressLine1Layout, itemPassed);

                    if (addressLine1Status == false) {
                        isValidAddressLine1 = false;
                        requestFocus(addressLine1ET);

                    } else {
                        isValidAddressLine1=true;
                        addressLine1Layout.setErrorEnabled(false);
                    }

                    break;




                case R.id.input_city:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean cityStatus = UiUtils.checkValidationForAutoCompleteTV(CoApplicantActivity.this, cityET, cityLayout, itemPassed);

                    if (cityStatus == false) {
                        isValidCity = false;
                        requestFocus(cityET);

                    } else {
                        isValidCity = true;
                        cityLayout.setErrorEnabled(false);
                    }

                    break;


                case R.id.input_district:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean districtStatus = UiUtils.checkValidation(CoApplicantActivity.this, districtET, districtLayout, itemPassed);

                    if (districtStatus == false) {
                        isValidDistrict = false;
                        requestFocus(districtET);

                    } else {
                        isValidDistrict=true;
                        districtLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_state:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean stateStatus = UiUtils.checkValidation(CoApplicantActivity.this, stateET, stateLayout, itemPassed);

                    if (stateStatus == false) {
                        isValidState = false;
                        requestFocus(stateET);

                    } else {
                        isValidState=true;
                        stateLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_pin_code:
                    itemPassed.clear();
                    itemPassed.add("zipCode");
                    boolean pinCodeStatus = UiUtils.checkValidation(CoApplicantActivity.this, pinCodeET, pinCodeLayout, itemPassed);

                    if (pinCodeStatus == false) {
                        isValidPinCode = false;
                        requestFocus(pinCodeET);

                    } else {
                        isValidPinCode=true;
                        pinCodeLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_aadhar_co_relation:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean relationSTatus = UiUtils.checkValidationForAutoCompleteTV(CoApplicantActivity.this, aadharCoRelationET, aadharCoRelationLayout, itemPassed);

                    if (relationSTatus == false) {
                        isValidRelation = false;
                        requestFocus(aadharCoRelationET);

                    } else {
                        isValidRelation = true;
                        aadharCoRelationLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_relation_with_applicant:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean corelationSTatus = UiUtils.checkValidationForAutoCompleteTV(CoApplicantActivity.this, relationApplicantET, relationApplicantLayout, itemPassed);

                    if (corelationSTatus == false) {
                        isValidRelationWithAPplicant = false;
                        requestFocus(relationApplicantET);

                    } else {
                        isValidRelationWithAPplicant = true;
                        relationApplicantLayout.setErrorEnabled(false);
                    }

                    break;
                case R.id.input_relation_others:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean relationOthersSTatus = UiUtils.checkValidation(CoApplicantActivity.this, relationOthersET, relationOthersLayout, itemPassed);

                    if (relationOthersSTatus == false) {
                        isValidRelationOthers = false;
                        requestFocus(relationOthersET);

                    } else {
                        isValidRelationOthers = true;
                        isValidRelation=true;
                        relationOthersLayout.setErrorEnabled(false);
                    }

                    break;


                case R.id.input_relation_with_applicant_others:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean relationWithApplicantOthersSTatus = UiUtils.checkValidation(CoApplicantActivity.this, relationWithApplicantOthersET, relationWithApplicantOthersLayout, itemPassed);

                    if (relationWithApplicantOthersSTatus == false) {
                        isValidRelationWithAPplicantOthers = false;
                        requestFocus(relationWithApplicantOthersET);

                    } else {
                        isValidRelationWithAPplicantOthers = true;
                        isValidRelationWithAPplicant=true;
                        relationWithApplicantOthersLayout.setErrorEnabled(false);
                    }
                    break;


                case R.id.input_alternate_address_line1:
                    itemPassed.clear();
                    itemPassed.add("address");
                    boolean addressLine1AltStatus = UiUtils.checkValidation(CoApplicantActivity.this, addressLine1ALternateET, addressLine1AlternateLayout, itemPassed);

                    if (addressLine1AltStatus == false) {
                        isValidAddressAlternateLine1 = false;
                        requestFocus(addressLine1ALternateET);

                    } else {
                        isValidAddressAlternateLine1=true;
                        addressLine1AlternateLayout.setErrorEnabled(false);
                    }

                    break;




                case R.id.input_alternate_city:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean cityAltStatus = UiUtils.checkValidationForAutoCompleteTV(CoApplicantActivity.this, cityAlternateET, cityAlternateLayout, itemPassed);

                    if (cityAltStatus == false) {
                        isValidAlternateCity = false;
                        requestFocus(cityAlternateET);

                    } else {
                        isValidAlternateCity = true;
                        cityAlternateLayout.setErrorEnabled(false);
                    }

                    break;


                case R.id.input_alternate_district:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean districtAltStatus = UiUtils.checkValidation(CoApplicantActivity.this, districtAlternateET, districtAlternateLayout, itemPassed);

                    if (districtAltStatus == false) {
                        isValidAlternateDistrict = false;
                        requestFocus(districtAlternateET);

                    } else {
                        isValidAlternateDistrict=true;
                        districtAlternateLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_alternate_state:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean stateAltStatus = UiUtils.checkValidation(CoApplicantActivity.this, stateAlternateET, stateAlternateLayout, itemPassed);

                    if (stateAltStatus == false) {
                        isValidAlternateState = false;
                        requestFocus(stateAlternateET);

                    } else {
                        isValidAlternateState=true;
                        stateAlternateLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_alternate_pin_code:
                    itemPassed.clear();
                    itemPassed.add("zipCode");
                    boolean pinCodeAltStatus = UiUtils.checkValidation(CoApplicantActivity.this, pinCodeAlternateET, pinCodeAlternateLayout, itemPassed);

                    if (pinCodeAltStatus == false) {
                        isValidAlternatePinCode = false;
                        requestFocus(pinCodeAlternateET);

                    } else {
                        isValidAlternatePinCode=true;
                        pinCodeAlternateLayout.setErrorEnabled(false);
                    }

                    break;




            }
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private static class SingleSelectListener implements SingleSelectToggleGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
            Log.v("Aadhar", "onCheckedChanged(): " + checkedId);
        }
    }

    private void showAlertDialogForScan(){
        // title, custom view, actions dialog


        View view = getLayoutInflater().inflate(R.layout.custom_message_dialog, null);

        TextView messageTV =(TextView)view.findViewById(R.id.message_tv);
        messageTV.setText(getResources().getString(R.string.hint_restart_process));
        AlertDialog.Builder builder = new AlertDialog.Builder(CoApplicantActivity.this);
        builder.setCancelable(false)
                .setTitle("Restart Process !")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        goToAadharScanPage();
                        dialog.dismiss();

                    }
                })
                .setNeutralButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setView(view);


        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

    }

    private void goToAadharScanPage() {
        Intent intent = new Intent(CoApplicantActivity.this,AddNewCoApplicantScanActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("place_name", GlobalValue.placeName);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(buttonView.isChecked()){

            isResidingChecked =true;
            alternateAddressTV.setTextColor(getResources().getColor(R.color.primaryColor));
            alternateLayout.setBackground(getResources().getDrawable(R.drawable.blue_bordered_box_bg));
            alternateAddressContentLayout.setVisibility(View.GONE);

        }else{
            isResidingChecked = false;

            alternateAddressTV.setTextColor(getResources().getColor(R.color.editTextTitleColor));
            alternateLayout.setBackground(getResources().getDrawable(R.drawable.bordered_bg));
            alternateAddressContentLayout.setVisibility(View.VISIBLE);

        }

    }


    private void getRelationDropDown() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<CustomerCoRelationResponseModel> call = apiService.getCoApplicantRelation(loanTakerID);
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


                                //set values for relation dropdown
                                //initialize arraylist
                                applicantLoanTakerRelationsArrayListNew = new ArrayList<CACDLoanTakerRelationsModel>();

                                //add select to the drop down in position 0
                                CACDLoanTakerRelationsModel cacdLoanTakerRelationsModel = new CACDLoanTakerRelationsModel();
                                cacdLoanTakerRelationsModel.setId("0");
                                cacdLoanTakerRelationsModel.setName("Select");
                                cacdLoanTakerRelationsModel.setCode("0");
                                applicantLoanTakerRelationsArrayListNew.add(cacdLoanTakerRelationsModel);

                                //add rest of the relations
                                for(int i=0;i<GlobalValue.applicantLoanTakerRelationsArrayList.size();i++){
                                    CACDLoanTakerRelationsModel cacdLoanTakerRelationsModel1 = new CACDLoanTakerRelationsModel();
                                    cacdLoanTakerRelationsModel1.setId(GlobalValue.applicantLoanTakerRelationsArrayList.get(i).getId());
                                    cacdLoanTakerRelationsModel1.setName(GlobalValue.applicantLoanTakerRelationsArrayList.get(i).getName());
                                    cacdLoanTakerRelationsModel1.setCode(GlobalValue.applicantLoanTakerRelationsArrayList.get(i).getCode());

                                    applicantLoanTakerRelationsArrayListNew.add(cacdLoanTakerRelationsModel1);
                                }

                                setRelationSpinner(applicantLoanTakerRelationsArrayListNew);

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
//                                corelationsSpinnerAdapter = new CoApplicantRelationsSpinnerAdapter(CoApplicantActivity.this, R.layout.spinner_item,response.body().getRelationModels());
//                                relationApplicantET.setAdapter(corelationsSpinnerAdapter);
                                Log.i("Drools", "" + "done");
                            }



                        } else {
                            NetworkUtils.handleErrorsForAPICalls(CoApplicantActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call <CustomerCoRelationResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantActivity.this, null, null);
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


        relationWithApplicantSpinnerAdapter = new RelationWithApplicantSpinnerAdapter(CoApplicantActivity.this,
                R.layout.ms__list_item,
                cCoARelationModels);

        relationWithApplicantSpinner.setAdapter(relationWithApplicantSpinnerAdapter); // Set the custom adapter to the spinner
    }


    private void setRelationSpinner(ArrayList<CACDLoanTakerRelationsModel> applicantLoanTakerRelationsArrayList) {



        CACDLoanTakerRelationsModel[] cacdLoanTakerRelationsModel = new CACDLoanTakerRelationsModel[applicantLoanTakerRelationsArrayList.size()];

        for(int i=0;i<applicantLoanTakerRelationsArrayList.size();i++){
            cacdLoanTakerRelationsModel[i]= new CACDLoanTakerRelationsModel();
            cacdLoanTakerRelationsModel[i].setId(applicantLoanTakerRelationsArrayList.get(i).getId());
            cacdLoanTakerRelationsModel[i].setCode(applicantLoanTakerRelationsArrayList.get(i).getCode());
            cacdLoanTakerRelationsModel[i].setName(applicantLoanTakerRelationsArrayList.get(i).getName());
        }


        relationSpinnerAdapter = new RelationSpinnerAdapter(CoApplicantActivity.this,
                R.layout.ms__list_item,
                cacdLoanTakerRelationsModel);

        relationSpinner.setAdapter(relationSpinnerAdapter); // Set the custom adapter to the spinner
    }


    public void addRadioButtons(ArrayList<String> gender, String genderVal) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < gender.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setText(gender.get(i));
                rdbtn.setId(i+1);
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                if(gender.get(i).equalsIgnoreCase(genderVal)){
                    Log.i("Drools", "" + genderVal);
                    rdbtn.setChecked(true);
                }
                rdbtn.setPadding(64,32,64,32);
                genderToggle.addView(rdbtn);
            }
        }
    }

    public void addRadioButtons(ArrayList<String> gender) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < gender.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setText(gender.get(i));
                rdbtn.setId(i+1);
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                genderToggle.addView(rdbtn);
            }
        }
    }

    private void pickUpFromCalendar() {
        final Calendar cldr = Calendar.getInstance();


        int day,month,year;

        if(isScanned){
            if(pickedDay==0){

                if(dobFoundFromScanning) {

                    day = dayInitial;
                    month = monthInital - 1;
                    year = yearInital;
                }else{
                    day = 01;
                    month = 00;
                    year = 1970;
                }
            }else{
                day = pickedDay;
                month = pickedMonth-1;
                year = pickedYear;
            }
        }else{
            if(pickedDay==0){

                day = 01;
                month = 00;
                year = 1970;
            }else{
                day = pickedDay;
                month = pickedMonth-1;
                year = pickedYear;
            }
        }
//        if(isScanned){
//            day = dayInitial;
//            month = monthInital-1;
//            year = yearInital;
//        }else{
//            day = 01;
//            month = 00;
//            year = 1970;
//        }


        //for calendar spinner
        picker = new DatePickerDialog(CoApplicantActivity.this,R.style.CustomDatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                        SimpleDateFormat sdf = new SimpleDateFormat("MMM");
                        cldr.set(year, monthOfYear, dayOfMonth);
                        String month = sdf.format(cldr.getTime());
                        Log.i("Suhail",month);

                        dayET.setText(String.valueOf(dayOfMonth));
                        monthET.setText(month);
                        yearET.setText(String.valueOf(year));
                        dobValue = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;

                        pickedDay = dayOfMonth;
                        pickedMonth = monthOfYear+1;
                        pickedYear = year;
//                        dayET.setText(String.valueOf(dayOfMonth));
//                        monthET.setText(String.valueOf(monthOfYear + 1));
//                        yearET.setText(String.valueOf(year));
//
//                        dobValue = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                    }
                }, year, month, day);

        picker.show();
    }

    public void requestFocus(View view) {
        if (view.requestFocus())
            CoApplicantActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(CoApplicantActivity.this);
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
