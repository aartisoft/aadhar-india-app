package com.tailwebs.aadharindia.member.applicant;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.tailwebs.aadharindia.home.NewGroupTaskActivity;
import com.tailwebs.aadharindia.home.tasks.creategroup.CreateGroupTaskDetailsActivity;
import com.tailwebs.aadharindia.member.AddNewMemberScanActivity;
import com.tailwebs.aadharindia.member.GroupMemberListingActivity;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.RelationsSpinnerAdapter;
import com.tailwebs.aadharindia.member.applicant.spinners.RelationSpinnerAdapter;
import com.tailwebs.aadharindia.member.models.LoanTakerCustomerDetailResponseModel;
import com.tailwebs.aadharindia.models.city.CityResponseModel;
import com.tailwebs.aadharindia.models.common.CACDLoanTakerRelationsModel;
import com.tailwebs.aadharindia.models.common.CustomerApplicantCommonDataResponseModel;
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

import java.text.ParseException;
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

public class AddCustomerDetailsActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = "AddCustomerDetails";
    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;



//    CARD 1

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


//    CARD 2

    @BindView(R.id.input_layout_pan)
    TextInputLayout panNoLayout;

    @BindView(R.id.input_pan)
    TextInputEditText panNoET;

    @BindView(R.id.input_layout_voter_id)
    TextInputLayout voterIdLayout;

    @BindView(R.id.input_voter_id)
    TextInputEditText voterIdET;

    @BindView(R.id.input_layout_mobile)
    TextInputLayout mobileLayout;

    @BindView(R.id.input_mobile)
    TextInputEditText mobileET;

    //CARD 3

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

    @BindView(R.id.dob_error_tv)
    TextView dobErrorTV;

    @BindView(R.id.gender_error_tv)
    TextView genderErrorTV;

    String dobValue =null,genderValue=null,profilePicUriPath=null,cityGoogleId=null,alternateGoogleId=null;

    int relationValue=0;

    int dayInitial = 01;
    int monthInital = 00;
    int yearInital = 1970;


    int pickedDay = 0;
    int pickedMonth = 0;
    int pickedYear = 0;

    private ProgressDialog mProgressDialog;

    public ArrayList<CACDLoanTakerRelationsModel> applicantLoanTakerRelationsArrayList = null,applicantLoanTakerRelationsArrayListNew;
    RelationsSpinnerAdapter relationsSpinnerAdapter;

    String dobValueFromIntent,genderValueFromIntent;

    private boolean isValidAadhar = false, isValidNContactPerson = false, isValidDob = false,isValidGender = false,
            isValidAadharCo=false,isValidMobile =false,isValidAddressLine1=false,isValidCity=false,isValidDistrict=false,
            isValidPinCode=false,isValidState=false,isValidRelation=false
            ,isResidingChecked=false,isValidRelationOthers=false,isValidAddressAlternateLine1=false,isValidAlternateCity=false,isValidAlternateDistrict=false,
            isValidAlternatePinCode=false,isValidAlternateState=false,isScanned=false,dobFoundFromScanning=false;



    @BindView(R.id.relation_spinner)
    Spinner relationSpinner;

    RelationSpinnerAdapter relationSpinnerAdapter;

    @BindView(R.id.relation_spinner_error_tv)
    TextView relationSpinnerErrorTv;

    DatePickerDialog picker;

    //for city

    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;

    private AlternatePlaceAutocompleteAdapter alternatemPlaceAutocompleteAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace,altPlace;


    // variables to store extracted xml data
    String uid=null,name=null,gname=null,gender=null,yearOfBirth=null,dateOfBirth=null,dateOfBirthGuess=null,careOf=null,villageTehsil=null,postOffice=null,district=null,state=null,
            postCode=null,lm=null
            ,house=null,street=null,sub_district=null,loc=null;

    private FirebaseAnalytics mFirebaseAnalytics;
    private String blockCharacterSet = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";


    private GpsTracker gpsTracker;
    String currentLat=null,currentLong=null;

    PlacesClient placesClient,alternatePlacesClient;
    AutoCompleteAdapter adapter;
    AlternateAutoCompleteAdapter alternateAutoCompleteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GlobalValue.loanTakerId = null;


        GlobalValue.applicantLoanAmountArrayList = null;
        GlobalValue.applicantLoanTenureArrayList = null;
        GlobalValue.applicantLoanTakerRelationsArrayList = null;
        GlobalValue.applicantLoanReasonsArrayList = null;
        GlobalValue.applicantMaritalStatusArrayList = null;
        GlobalValue.applicantReligionsArrayList = null;
        GlobalValue.applicantCastesArrayList = null;
        GlobalValue.applicantRationCardTypesArrayList = null;
        GlobalValue.applicantSecondaryIDArrayList = null;



        //set applicant signature
        GlobalValue.isApplicantDeclarationCompleted = false;

        setContentView(R.layout.activity_add_customer_details);
        ButterKnife.bind(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Add Applicant Form ", null);

        init();
        updateTheStatusInApplicantDetailPage();


        String apiKey = getString(R.string.google_maps_key);
        if(apiKey.isEmpty()){
            Toast.makeText( AddCustomerDetailsActivity.this, "Google Api key is missing.", Toast.LENGTH_SHORT ).show();
            return;
        }


        // Setup Places Client
        if (!com.google.android.libraries.places.api.Places.isInitialized()) {
            com.google.android.libraries.places.api.Places.initialize(getApplicationContext(), apiKey);
        }

        placesClient = com.google.android.libraries.places.api.Places.createClient(this);
        alternatePlacesClient = com.google.android.libraries.places.api.Places.createClient(this);
        initAutoCompleteTextView();


        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(AddCustomerDetailsActivity.this);
            getApplicantCommonDatas();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

        Log.d(TAG, "gender:: " +isValidGender+genderValue);

        hideSoftKeyboard();


        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Customer Details");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
        genderToggle.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidGender =  true;
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


        //show current Lat and Long

        try {
            gpsTracker = new GpsTracker(AddCustomerDetailsActivity.this);
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

        aadharScanImgView.setOnClickListener(this);
        alternateAddressCheckbox.setOnCheckedChangeListener(this);
        dayET.setOnClickListener(this);
        monthET.setOnClickListener(this);
        yearET.setOnClickListener(this);

        mobileET.addTextChangedListener(new InputLayoutTextWatcher(mobileET));
        aadharNoET.addTextChangedListener(new InputLayoutTextWatcher(aadharNoET));
        aadharCoET.addTextChangedListener(new InputLayoutTextWatcher(aadharCoET));
        contactPersonET.addTextChangedListener(new InputLayoutTextWatcher(contactPersonET));
        addressLine1ET.addTextChangedListener(new InputLayoutTextWatcher(addressLine1ET));
        districtET.addTextChangedListener(new InputLayoutTextWatcher(districtET));
        stateET.addTextChangedListener(new InputLayoutTextWatcher(stateET));
        pinCodeET.addTextChangedListener(new InputLayoutTextWatcher(pinCodeET));
        cityET.addTextChangedListener(new InputLayoutTextWatcher(cityET));
        panNoET.addTextChangedListener(new InputLayoutTextWatcher(panNoET));
        voterIdET.addTextChangedListener(new InputLayoutTextWatcher(voterIdET));
        aadharCoRelationET.addTextChangedListener(new InputLayoutTextWatcher(aadharCoRelationET));
        relationOthersET.addTextChangedListener(new InputLayoutTextWatcher(relationOthersET));
        addressLine1ALternateET.addTextChangedListener(new InputLayoutTextWatcher(addressLine1ALternateET));
        districtAlternateET.addTextChangedListener(new InputLayoutTextWatcher(districtAlternateET));
        stateAlternateET.addTextChangedListener(new InputLayoutTextWatcher(stateAlternateET));
        pinCodeAlternateET.addTextChangedListener(new InputLayoutTextWatcher(pinCodeAlternateET));
        cityAlternateET.addTextChangedListener(new InputLayoutTextWatcher(cityAlternateET));

        contactPersonET.setFilters(new InputFilter[] { filter });
        aadharCoET.setFilters(new InputFilter[] { filter });

        addressLine1ET.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (addressLine1ET.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });





//        addressLine1ET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // TODO Auto-generated method stub
//
//                if (hasFocus) {
//                    if (addressLine1ET.getText().toString().trim()     //**try using value.getText().length()<3**
//                        .length() < 3) {            // **instead of the value.getText().trim().length()**
//
//                        addressLine1ET.setError(getResources().getString(R.string.error_invalid_address));
//                    } else {
//                        addressLine1ET.setError(null);
//                    }}}});
        relationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CACDLoanTakerRelationsModel relationsModel = relationSpinnerAdapter.getItem(position);
                // Here you can do the action you want to...

                if (position==0){
                    relationValue = 0;
                    isValidRelation=false;
//                  checkOthers(relationsModel.getCode());
                }


                if(position!=0){

                    isValidRelation = true;
                    relationValue= Integer.parseInt(relationsModel.getId());
                    relationSpinnerErrorTv.setVisibility(View.GONE);
                    checkOthers(relationsModel.getCode());


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //not needed

//        if(GlobalValue.placeId!=null) {
//            getValuesFromCityID(GlobalValue.placeId);
//            cityGoogleId = GlobalValue.placeId;
//        }


//        cityET.setText(GlobalValue.placeAddress);


//
//        aadharCoRelationET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Object item = parent.getItemAtPosition(position);
//                if (item instanceof CACDLoanTakerRelationsModel){
//                    final CACDLoanTakerRelationsModel cacdLoanTakerRelationsModel=(CACDLoanTakerRelationsModel) item;
//                  aadharCoRelationET.setText(cacdLoanTakerRelationsModel.getName());
//                  relationValue = cacdLoanTakerRelationsModel.getId();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            checkOthers(cacdLoanTakerRelationsModel.getCode());
//                        }
//                    });
//
//                }
//            }
//        });



        continueButton.setOnClickListener(this);

        //for city auto complete

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();


//        alternateGoogleClient = new GoogleApiClient
//                .Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(this, this)
//                .build();


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
                            showProgressDialog(AddCustomerDetailsActivity.this);
                            alternateGoogleId = task.getPlace().getId();
                            getValuesFromAlternateCityID(alternateGoogleId);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText( AddCustomerDetailsActivity.this, "Cannot Retrieve Location", Toast.LENGTH_SHORT ).show();
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
                            showProgressDialog(AddCustomerDetailsActivity.this);
                            cityGoogleId = task.getPlace().getId();

                            getValuesFromCityID(cityGoogleId);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText( AddCustomerDetailsActivity.this, "Cannot Retrieve Location", Toast.LENGTH_SHORT ).show();
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

    }


      /*
        --------------------------- google places API autocomplete suggestions -----------------
     */




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

    private void getValuesFromAlternateCityID(String cityGoogleId) {
        showProgressDialog(AddCustomerDetailsActivity.this);
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
                        NetworkUtils.handleErrorsCasesForAPICalls(AddCustomerDetailsActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<CityResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(AddCustomerDetailsActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void getValuesFromCityID(String cityGoogleId) {
        showProgressDialog(AddCustomerDetailsActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);

            Call<CityResponseModel> call = apiService.getCityDatas(cityGoogleId);

            call.enqueue(new Callback<CityResponseModel>() {
                @Override
                public void onResponse(Call<CityResponseModel> call, Response<CityResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {


//                        cityET.setText(response.body().getCity().getLong_name());
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
                        NetworkUtils.handleErrorsCasesForAPICalls(AddCustomerDetailsActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<CityResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(AddCustomerDetailsActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }
    private void checkOthers(String code) {
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

    private void fillDetails(Bundle bundle) {


        if (bundle.getString("aadhar_no") != null) {
            aadharNoET.setText(bundle.getString("aadhar_no"));
            aadharNoET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
            isValidAadhar = true;
        }

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


//            if (bundle.getString("house") != null) {

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

//            }

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




        }



        } catch (Exception e){
            e.printStackTrace();
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

                submitCustomerDetails();
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

    private void submitCustomerDetails() {


        if ((isValidAadhar) && (isValidNContactPerson)  && (isValidDob) &&
                (isValidGender) && (isValidAadharCo)  && (isValidMobile) &&
                (isValidAddressLine1) && (isValidCity)  && (isValidDistrict) &&
                (isValidPinCode) && (isValidState) && (isValidRelation)  ){

            if(isResidingChecked){
                callAPIForAddingCustomerDetails();

            }else{
                if((isValidAddressAlternateLine1) && (isValidAlternateCity) && (isValidAlternateDistrict)
                        && (isValidAlternateState) && (isValidAlternatePinCode)){
                    callAPIForAddingCustomerDetails();

                }else{
                    UiUtils.checkValidation(AddCustomerDetailsActivity.this, addressLine1ALternateET, addressLine1AlternateLayout, new ArrayList<String>());
                    UiUtils.checkValidationForAutoCompleteTV(AddCustomerDetailsActivity.this, cityAlternateET, cityAlternateLayout, new ArrayList<String>());
                    UiUtils.checkValidation(AddCustomerDetailsActivity.this, districtAlternateET, districtAlternateLayout, new ArrayList<String>());
                    UiUtils.checkValidation(AddCustomerDetailsActivity.this, pinCodeAlternateET, pinCodeAlternateLayout, new ArrayList<String>());
                    UiUtils.checkValidation(AddCustomerDetailsActivity.this, stateAlternateET, stateAlternateLayout, new ArrayList<String>());

                }
            }


        }else{
            UiUtils.checkValidation(AddCustomerDetailsActivity.this, aadharNoET, aadharNumberLayout, new ArrayList<String>());
            UiUtils.checkValidation(AddCustomerDetailsActivity.this, contactPersonET, contactPersonLayout, new ArrayList<String>());
            UiUtils.checkValidation(AddCustomerDetailsActivity.this, aadharCoET, aadharCoLayout, new ArrayList<String>());
            UiUtils.checkValidation(AddCustomerDetailsActivity.this, mobileET, mobileLayout, new ArrayList<String>());
            UiUtils.checkValidation(AddCustomerDetailsActivity.this, addressLine1ET, addressLine1Layout, new ArrayList<String>());
            UiUtils.checkValidationForAutoCompleteTV(AddCustomerDetailsActivity.this, cityET, cityLayout, new ArrayList<String>());
            UiUtils.checkValidation(AddCustomerDetailsActivity.this, districtET, districtLayout, new ArrayList<String>());
            UiUtils.checkValidation(AddCustomerDetailsActivity.this, pinCodeET, pinCodeLayout, new ArrayList<String>());
            UiUtils.checkValidation(AddCustomerDetailsActivity.this, stateET, stateLayout, new ArrayList<String>());
//            UiUtils.checkValidationForAutoCompleteTV(AddCustomerDetailsActivity.this, aadharCoRelationET, aadharCoRelationLayout, new ArrayList<String>());


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
                    UiUtils.checkValidation(AddCustomerDetailsActivity.this, relationOthersET, relationOthersLayout, new ArrayList<String>());

                }else{
                    relationSpinnerErrorTv.setText("Field Required");
                    relationSpinnerErrorTv.setVisibility(View.VISIBLE);
                    isValidRelation=false;
                }
            }

//            if(relationValue == 0){
//                relationSpinnerErrorTv.setText("Field Required");
//                relationSpinnerErrorTv.setVisibility(View.VISIBLE);
//                isValidRelation=false;
//
//            }else{
//                relationSpinnerErrorTv.setVisibility(View.GONE);
//                isValidRelation=true;
//            }

            if(isResidingChecked==false){

                UiUtils.checkValidation(AddCustomerDetailsActivity.this, addressLine1ALternateET, addressLine1AlternateLayout, new ArrayList<String>());
                UiUtils.checkValidationForAutoCompleteTV(AddCustomerDetailsActivity.this, cityAlternateET, cityAlternateLayout, new ArrayList<String>());
                UiUtils.checkValidation(AddCustomerDetailsActivity.this, districtAlternateET, districtAlternateLayout, new ArrayList<String>());
                UiUtils.checkValidation(AddCustomerDetailsActivity.this, pinCodeAlternateET, pinCodeAlternateLayout, new ArrayList<String>());
                UiUtils.checkValidation(AddCustomerDetailsActivity.this, stateAlternateET, stateAlternateLayout, new ArrayList<String>());

            }

        }
    }

    private void callAPIForAddingCustomerDetails() {

        continueButton.setEnabled(false);
        continueButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(AddCustomerDetailsActivity.this);
        RequestBody is_residing = null;
        RequestBody relation_others =null;
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
                aadhar_postCode =null;

        try {
            Log.d("Aadhar onResponse", "--start" );

//            Add Group Id TODO
            Log.d("Aadhar onResponse", "--start" +isValidGender+"--"+genderValue);

            RequestBody group_id = RequestBody.create(MediaType.parse("text/plain"),   GlobalValue.groupId);
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

            Log.d("Aadhar onResponse", "--relation_othersstart" );
            RequestBody address_line_1 = RequestBody.create(MediaType.parse("text/plain"), addressLine1ET.getText().toString());
            RequestBody address_line_2 = RequestBody.create(MediaType.parse("text/plain"), addressLine2ET.getText().toString());
            RequestBody city_google_id = RequestBody.create(MediaType.parse("text/plain"), cityGoogleId);
            RequestBody pincode = RequestBody.create(MediaType.parse("text/plain"), pinCodeET.getText().toString());


            Log.d("Aadhar onResponse", "--pincodestart" );
            RequestBody primary_phone = RequestBody.create(MediaType.parse("text/plain"), mobileET.getText().toString());
            RequestBody pan = RequestBody.create(MediaType.parse("text/plain"), panNoET.getText().toString());
            RequestBody voterId = RequestBody.create(MediaType.parse("text/plain"), voterIdET.getText().toString());
            Log.d("Aadhar onResponse", "--isResidingCheckedstart" );

            if (isResidingChecked) {
                is_residing = RequestBody.create(MediaType.parse("text/plain"), "true");
            }else{
                is_residing = RequestBody.create(MediaType.parse("text/plain"), "false");
                alternate_address_line_1 = RequestBody.create(MediaType.parse("text/plain"), addressLine1ALternateET.getText().toString());
                alternate_address_line_2 = RequestBody.create(MediaType.parse("text/plain"), addressLine2AlternateET.getText().toString());
                alternate_city_google_id = RequestBody.create(MediaType.parse("text/plain"), alternateGoogleId);
                alternate_pincode = RequestBody.create(MediaType.parse("text/plain"), pinCodeAlternateET.getText().toString());
            }


            if(isScanned){

                Log.d("Aadhar onResponse", "--is Scanned" );

                if(uid!=null){
                    aadhar_uid = RequestBody.create(MediaType.parse("text/plain"), uid);
                }

                Log.d("Aadhar onResponse", "--aadhar_uid" );
                if(name!=null) {
                    aadhar_name = RequestBody.create(MediaType.parse("text/plain"), name);
                }
                if(gname!=null) {
                    aadhar_gname = RequestBody.create(MediaType.parse("text/plain"), gname);
                }
                if(gender!=null) {
                    aadhar_gender = RequestBody.create(MediaType.parse("text/plain"), gender);
                }
                if(yearOfBirth!=null) {
                    aadhar_yearOfBirth = RequestBody.create(MediaType.parse("text/plain"), yearOfBirth);
                }
                if(dateOfBirth!=null) {
                    aadhar_dateOfBirth = RequestBody.create(MediaType.parse("text/plain"), dateOfBirth);
                }
                if(careOf!=null){
                    aadhar_careOf = RequestBody.create(MediaType.parse("text/plain"), careOf);
                }
                if(lm!=null) {
                    aadhar_lm = RequestBody.create(MediaType.parse("text/plain"), lm);
                }
                if(house!=null) {
                    aadhar_house = RequestBody.create(MediaType.parse("text/plain"), house);
                }
                if(street!=null) {
                    aadhar_street = RequestBody.create(MediaType.parse("text/plain"), street);
                }
                if(loc!=null) {
                    aadhar_loc = RequestBody.create(MediaType.parse("text/plain"), loc);
                }
                if(villageTehsil!=null) {
                    aadhar_villageTehsil = RequestBody.create(MediaType.parse("text/plain"), villageTehsil);
                }
                if(postOffice!=null) {
                    aadhar_postOffice = RequestBody.create(MediaType.parse("text/plain"), postOffice);
                }
                if(district!=null) {
                    aadhar_district = RequestBody.create(MediaType.parse("text/plain"), district);
                }
                if(sub_district!=null) {
                    aadhar_sub_district = RequestBody.create(MediaType.parse("text/plain"), sub_district);
                }
                if(state!=null) {
                    aadhar_state = RequestBody.create(MediaType.parse("text/plain"), state);
                }
                if(postCode!=null) {
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
            Call<LoanTakerCustomerDetailResponseModel> call;
            if(isScanned){
                call = apiService.addLoanTakerCustomerDetailsIsScanned(
                        aadharNo,
                        aadharname,
                        dob,
                        aadhargender,
                        co,
                        co_id,
                        relation_others,
                        address_line_1,
                        address_line_2,
                        city_google_id,
                        pincode,
                        primary_phone,
                        pan,
                        voterId,
                        is_residing,
                        alternate_address_line_1,
                        alternate_address_line_2,
                        alternate_city_google_id,
                        alternate_pincode,
                        group_id,aadhar_uid,aadhar_name,aadhar_gender,aadhar_yearOfBirth,aadhar_dateOfBirth,aadhar_gname,aadhar_careOf,
                        aadhar_house,aadhar_street,aadhar_loc,aadhar_villageTehsil,aadhar_postOffice,aadhar_district,aadhar_sub_district,
                        aadhar_state,aadhar_postCode);
            }else {
                call = apiService.addLoanTakerCustomerDetails(
                        aadharNo,
                        aadharname,
                        dob,
                        aadhargender,
                        co,
                        co_id,
                        relation_others,
                        address_line_1,
                        address_line_2,
                        city_google_id,
                        pincode,
                        primary_phone,
                        pan,
                        voterId,
                        is_residing,
                        alternate_address_line_1,
                        alternate_address_line_2,
                        alternate_city_google_id,
                        alternate_pincode,
                        group_id);
            }
            call.enqueue(new Callback<LoanTakerCustomerDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCustomerDetailResponseModel> call, Response<LoanTakerCustomerDetailResponseModel> response) {
                    hideProgressDialog();
                    Log.d("Aadhar onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Aadhar onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            continueButton.setEnabled(true);
                            continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));


                            GlobalValue.loanTakerId = response.body().getLoanTakerModel().getId();
                            GlobalValue.loanTakerIdForAnalytics = response.body().getLoanTakerModel().getLoan_taker_id();
                            Log.d("Aadhar onResponse1", "" +  GlobalValue.loanTakerId);


                            Bundle params = new Bundle();
                            params.putString("applicant_id", response.body().getLoanTakerModel().getLoan_taker_id());
                            params.putString("applicant_name", response.body().getLoanTakerModel().getLoan_taker_id()+" - "+response.body().getLoanTakerModel().getName());
                            params.putString("applicant_city",response.body().getLoanTakerModel().getHomeAddress().getCity_name()+" - "+
                                    response.body().getLoanTakerModel().getHomeAddress().getGooglePlaceId());
                            params.putString("is_fresh",response.body().getLoanTakerModel().getIs_fresh_customer());
                            params.putString("status","applicant_creation_started");
                            mFirebaseAnalytics.logEvent("applicant_form", params);


                            Toast.makeText(AddCustomerDetailsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            goToCreditCheckReportPage();

                            updateTheStatusInApplicantDetailPage();
                        } else {
                            Log.d("Aadhar onResponse2", "" + response.body().getErrors());
                            continueButton.setEnabled(true);
                            continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                if(jsonObject.getJSONObject("errors").has("pan_number")){
                                    Toast.makeText(AddCustomerDetailsActivity.this, "Pan number is not in a format of AAAAA9999A", Toast.LENGTH_SHORT).show();
                                }

                                if(jsonObject.has("aadhar_number")) {

                                    JSONObject  groupJSON = new JSONObject(new Gson().toJson(response.body().getErrors()));
                                    Log.d("aadh onResponse json", "" + groupJSON.getString("aadhar_number"));

                                    String val = groupJSON.getString("group").substring(1);
                                    String final_val = val.substring(0, val.length() - 1);

                                    UiUtils.showAlertDialogWithOKButton(AddCustomerDetailsActivity.this, final_val, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("Drools onResponse", "" + jsonObject);

                            NetworkUtils.handleErrorsCasesForAPICalls(AddCustomerDetailsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        continueButton.setEnabled(true);
                        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        Log.d("Aadhar onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(AddCustomerDetailsActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCustomerDetailResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    continueButton.setEnabled(true);
                    continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(AddCustomerDetailsActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }


    }

    private void updateTheStatusInApplicantDetailPage() {
        try {
            ApplicantDetailActivity.getInstance().init();
            MemberDetailActivity.getInstance().init();
            GroupMemberListingActivity.getInstance().init();
        }
        catch (Exception e) {
            System.out.print("Caught the Exception");
        }
    }

    private void goToCreditCheckReportPage() {
        startActivity(new Intent(AddCustomerDetailsActivity.this,CreditCheckReportActivity.class));
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
                case R.id.input_mobile:
                    itemPassed.clear();
                    itemPassed.add("mobile");
                    boolean status = UiUtils.checkValidation(AddCustomerDetailsActivity.this, mobileET, mobileLayout, itemPassed);

                    if (status == false) {
                        isValidMobile = false;
                        requestFocus(mobileET);
                    } else {
                        isValidMobile = true;


                    }
                    break;
                case R.id.input_aadhar_number:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean aadharStatus = UiUtils.checkValidation(AddCustomerDetailsActivity.this, aadharNoET, aadharNumberLayout, itemPassed);

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
                    boolean coStatus = UiUtils.checkValidation(AddCustomerDetailsActivity.this, aadharCoET, aadharCoLayout, itemPassed);

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
                    boolean personStatus = UiUtils.checkValidation(AddCustomerDetailsActivity.this, contactPersonET, contactPersonLayout, itemPassed);

                    if (personStatus == false) {
                        isValidNContactPerson = false;
                        requestFocus(contactPersonET);

                    } else {
                        isValidNContactPerson=true;
                        contactPersonLayout.setErrorEnabled(false);
                    }

                    break;


                case R.id.input_address_line1:
                    itemPassed.clear();
                    itemPassed.add("address");
                    boolean addressLine1Status = UiUtils.checkValidation(AddCustomerDetailsActivity.this, addressLine1ET, addressLine1Layout, itemPassed);
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
                    boolean cityStatus = UiUtils.checkValidationForAutoCompleteTV(AddCustomerDetailsActivity.this, cityET, cityLayout, itemPassed);

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
                    boolean districtStatus = UiUtils.checkValidation(AddCustomerDetailsActivity.this, districtET, districtLayout, itemPassed);

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
                    boolean stateStatus = UiUtils.checkValidation(AddCustomerDetailsActivity.this, stateET, stateLayout, itemPassed);

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
                    boolean pinCodeStatus = UiUtils.checkValidation(AddCustomerDetailsActivity.this, pinCodeET, pinCodeLayout, itemPassed);

                    if (pinCodeStatus == false) {
                        isValidPinCode = false;
                        requestFocus(pinCodeET);

                    } else {
                        isValidPinCode=true;
                        pinCodeLayout.setErrorEnabled(false);
                    }

                    break;

//                case R.id.input_pan:
//                    itemPassed.clear();
//                    itemPassed.add("");
//                    boolean panStatus = UiUtils.checkValidation(AddCustomerDetailsActivity.this, panNoET, panNoLayout, itemPassed);
//
//                    if (panStatus == false) {
//                        isValidPan = false;
//                        requestFocus(panNoET);
//
//                    } else {
//                        isValidPan=true;
//                        panNoLayout.setErrorEnabled(false);
//                    }
//
//                    break;



                case R.id.input_aadhar_co_relation:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean relationSTatus = UiUtils.checkValidationForAutoCompleteTV(AddCustomerDetailsActivity.this, aadharCoRelationET, aadharCoRelationLayout, itemPassed);

                    if (relationSTatus == false) {
                        isValidRelation = false;
                        requestFocus(aadharCoRelationET);

                    } else {
                        isValidRelation = true;
                        aadharCoRelationLayout.setErrorEnabled(false);
                    }

                    break;
                case R.id.input_relation_others:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean relationOthersSTatus = UiUtils.checkValidation(AddCustomerDetailsActivity.this, relationOthersET, relationOthersLayout, itemPassed);

                    if (relationOthersSTatus == false) {
                        isValidRelationOthers = false;
                        requestFocus(relationOthersET);

                    } else {
                        isValidRelationOthers = true;
                        isValidRelation=true;
                        relationOthersLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_alternate_address_line1:
                    itemPassed.clear();
                    itemPassed.add("address");
                    boolean addressLine1AltStatus = UiUtils.checkValidation(AddCustomerDetailsActivity.this, addressLine1ALternateET, addressLine1AlternateLayout, itemPassed);

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
                    boolean cityAltStatus = UiUtils.checkValidationForAutoCompleteTV(AddCustomerDetailsActivity.this, cityAlternateET, cityAlternateLayout, itemPassed);

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
                    boolean districtAltStatus = UiUtils.checkValidation(AddCustomerDetailsActivity.this, districtAlternateET, districtAlternateLayout, itemPassed);

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
                    boolean stateAltStatus = UiUtils.checkValidation(AddCustomerDetailsActivity.this, stateAlternateET, stateAlternateLayout, itemPassed);

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
                    boolean pinCodeAltStatus = UiUtils.checkValidation(AddCustomerDetailsActivity.this, pinCodeAlternateET, pinCodeAlternateLayout, itemPassed);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddCustomerDetailsActivity.this);
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

        Intent intent = new Intent(AddCustomerDetailsActivity.this,AddNewMemberScanActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("place_name", GlobalValue.placeName);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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



                            try {
                                JSONObject jsonObject =  new JSONObject(new Gson().toJson(response.body()));
                                Log.i("Drools", "" + jsonObject);

                                if (jsonObject.has("genders")){
                                    jsonObject.getJSONArray("genders").length();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            genderToggle.removeAllViews();
                                            genderToggle.removeAllViewsInLayout();
                                            genderToggle.invalidate();
                                            Log.i("Drools", "" + response.body().getGenders());
                                            GlobalValue.applicantGenderList =  response.body().getGenders();

                                        }//public void run() {
                                    });

                                }

                                if(getIntent().getExtras().getBoolean("is_scanned")){

                                }else{
                                    addRadioButtons(response.body().getGenders());
                                }


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

                                if(response.body().getCacdLoanTakerRelationsModelArrayList().size() >0){

                                    applicantLoanTakerRelationsArrayList =response.body().getCacdLoanTakerRelationsModelArrayList();
                                    relationsSpinnerAdapter = new RelationsSpinnerAdapter(AddCustomerDetailsActivity.this, R.layout.spinner_item,applicantLoanTakerRelationsArrayList);
                                    aadharCoRelationET.setAdapter(relationsSpinnerAdapter);

                                }


                                fillDetails(getIntent().getExtras());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {
                            NetworkUtils.handleErrorsForAPICalls(AddCustomerDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(AddCustomerDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<CustomerApplicantCommonDataResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(AddCustomerDetailsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setRelationSpinner(ArrayList<CACDLoanTakerRelationsModel> applicantLoanTakerRelationsArrayList) {



        CACDLoanTakerRelationsModel[] cacdLoanTakerRelationsModel = new CACDLoanTakerRelationsModel[applicantLoanTakerRelationsArrayList.size()];

        for(int i=0;i<applicantLoanTakerRelationsArrayList.size();i++){
            cacdLoanTakerRelationsModel[i]= new CACDLoanTakerRelationsModel();
            cacdLoanTakerRelationsModel[i].setId(applicantLoanTakerRelationsArrayList.get(i).getId());
            cacdLoanTakerRelationsModel[i].setCode(applicantLoanTakerRelationsArrayList.get(i).getCode());
            cacdLoanTakerRelationsModel[i].setName(applicantLoanTakerRelationsArrayList.get(i).getName());
        }


        relationSpinnerAdapter = new RelationSpinnerAdapter(AddCustomerDetailsActivity.this,
                R.layout.ms__list_item,
                cacdLoanTakerRelationsModel);

        relationSpinner.setAdapter(relationSpinnerAdapter); // Set the custom adapter to the spinner
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
        picker = new DatePickerDialog(AddCustomerDetailsActivity.this,R.style.CustomDatePickerDialog,
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

                    }
                }, year, month, day);

        picker.show();
    }


    public void requestFocus(View view) {
        if (view.requestFocus())
            AddCustomerDetailsActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(AddCustomerDetailsActivity.this);
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



//    private void hideSoftKeyboard(){
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//    }


    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

}
