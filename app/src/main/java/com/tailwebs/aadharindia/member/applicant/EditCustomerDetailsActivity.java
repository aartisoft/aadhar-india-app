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
import com.tailwebs.aadharindia.member.AddNewMemberScanActivity;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.RelationsSpinnerAdapter;
import com.tailwebs.aadharindia.member.applicant.spinners.RelationSpinnerAdapter;
import com.tailwebs.aadharindia.member.models.LoanTakerCustomerDetailResponseModel;
import com.tailwebs.aadharindia.models.city.CityResponseModel;
import com.tailwebs.aadharindia.models.common.CACDLoanTakerRelationsModel;
import com.tailwebs.aadharindia.models.common.CustomerApplicantCommonDataResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;
import com.tailwebs.aadharindia.utils.custom.BetterSpinner;
import com.tailwebs.aadharindia.utils.custom.MaterialBetterSpinner;
import com.tailwebs.aadharindia.utils.custom.singleselecttoggle.SingleSelectToggleGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCustomerDetailsActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, GoogleApiClient.OnConnectionFailedListener {


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


    @BindView(R.id.relation_spinner)
    Spinner relationSpinner;

    RelationSpinnerAdapter relationSpinnerAdapter;

    @BindView(R.id.relation_spinner_error_tv)
    TextView relationSpinnerErrorTv;

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

    String dobValueFromIntent,genderValueFromIntent,addressId=null,residingAddressId=null;

    private boolean isValidAadhar = false, isValidNContactPerson = false, isValidDob = false,isValidGender = false,
            isValidAadharCo=false,isValidMobile =false,isValidAddressLine1=false,isValidCity=false,isValidDistrict=false,
            isValidPinCode=false,isValidState=false,isValidRelation=false
            ,isResidingChecked=false,isValidRelationOthers=false,isValidAddressAlternateLine1=false,isValidAlternateCity=false,isValidAlternateDistrict=false,
            isValidAlternatePinCode=false,isValidAlternateState=false,isScanned=false;

    DatePickerDialog picker;

    private FirebaseAnalytics mFirebaseAnalytics;

    //for city

    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private AlternatePlaceAutocompleteAdapter alternatemPlaceAutocompleteAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace,altPlace;

    //choose value from intent;
    String loanTakerID;
    private String blockCharacterSet = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";


    PlacesClient placesClient,alternatePlacesClient;
    AutoCompleteAdapter adapter;
    AlternateAutoCompleteAdapter alternateAutoCompleteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer_details);

        ButterKnife.bind(this);

        loanTakerID =  GlobalValue.loanTakerId;


        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(EditCustomerDetailsActivity.this);
            getApplicantCommonDatas();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Edit Customer Details", null);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Customer Details");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
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

        placesClient = com.google.android.libraries.places.api.Places.createClient(this);
        alternatePlacesClient = com.google.android.libraries.places.api.Places.createClient(this);
        initAutoCompleteTextView();

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
                    checkOthers(relationsModel.getCode());


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


        cityAlternateET.setOnItemClickListener(alternateAutocompleteClickListener);
        cityAlternateET.setAdapter(alternatemPlaceAutocompleteAdapter);

        cityET.setOnItemClickListener(mAutocompleteClickListener);
        cityET.setAdapter(mPlaceAutocompleteAdapter);
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
                            showProgressDialog(EditCustomerDetailsActivity.this);
                            alternateGoogleId = task.getPlace().getId();
                            getValuesFromAlternateCityID(alternateGoogleId);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText( EditCustomerDetailsActivity.this, "Cannot Retrieve Location", Toast.LENGTH_SHORT ).show();
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
                            showProgressDialog(EditCustomerDetailsActivity.this);
                            cityGoogleId = task.getPlace().getId();

                            getValuesFromCityID(cityGoogleId);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText( EditCustomerDetailsActivity.this, "Cannot Retrieve Location", Toast.LENGTH_SHORT ).show();
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
        showProgressDialog(EditCustomerDetailsActivity.this);
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
                        if(response.body().getCity().getPincode()!=null){
                            pinCodeAlternateET.setText(response.body().getCity().getPincode());
                        }


                    } else {
                        hideProgressDialog();
                        NetworkUtils.handleErrorsCasesForAPICalls(EditCustomerDetailsActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<CityResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(EditCustomerDetailsActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void getValuesFromCityID(String cityGoogleId) {
        showProgressDialog(EditCustomerDetailsActivity.this);
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
                        if(response.body().getCity().getPincode()!=null){
                            pinCodeET.setText(response.body().getCity().getPincode());
                        }


                    } else {
                        hideProgressDialog();
                        NetworkUtils.handleErrorsCasesForAPICalls(EditCustomerDetailsActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<CityResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(EditCustomerDetailsActivity.this, null, null);

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
            requestFocus(relationOthersET);
            isValidRelationOthers = true;
            isValidRelation=false;
        }else{
            requestFocus(addressLine1ET);
            relationOthersLayout.setVisibility(View.GONE);
            relationOthersET.setVisibility(View.GONE);
            Log.d("Aadhar","Not Other");
            isValidRelationOthers = false;
            isValidRelation=true;
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
                callAPIForEditCustomerDetails();
            }else{
                if((isValidAddressAlternateLine1) && (isValidAlternateCity) && (isValidAlternateDistrict)
                        && (isValidAlternateState) && (isValidAlternatePinCode)){
                    callAPIForEditCustomerDetails();
                }else{
                    UiUtils.checkValidation(EditCustomerDetailsActivity.this, addressLine1ALternateET, addressLine1AlternateLayout, new ArrayList<String>());
                    UiUtils.checkValidationForAutoCompleteTV(EditCustomerDetailsActivity.this, cityAlternateET, cityAlternateLayout, new ArrayList<String>());
                    UiUtils.checkValidation(EditCustomerDetailsActivity.this, districtAlternateET, districtAlternateLayout, new ArrayList<String>());
                    UiUtils.checkValidation(EditCustomerDetailsActivity.this, pinCodeAlternateET, pinCodeAlternateLayout, new ArrayList<String>());
                    UiUtils.checkValidation(EditCustomerDetailsActivity.this, stateAlternateET, stateAlternateLayout, new ArrayList<String>());

                }
            }


        }else{
            UiUtils.checkValidation(EditCustomerDetailsActivity.this, aadharNoET, aadharNumberLayout, new ArrayList<String>());
            UiUtils.checkValidation(EditCustomerDetailsActivity.this, contactPersonET, contactPersonLayout, new ArrayList<String>());
            UiUtils.checkValidation(EditCustomerDetailsActivity.this, aadharCoET, aadharCoLayout, new ArrayList<String>());
            UiUtils.checkValidation(EditCustomerDetailsActivity.this, mobileET, mobileLayout, new ArrayList<String>());
            UiUtils.checkValidation(EditCustomerDetailsActivity.this, addressLine1ET, addressLine1Layout, new ArrayList<String>());
            UiUtils.checkValidationForAutoCompleteTV(EditCustomerDetailsActivity.this, cityET, cityLayout, new ArrayList<String>());
            UiUtils.checkValidation(EditCustomerDetailsActivity.this, districtET, districtLayout, new ArrayList<String>());
            UiUtils.checkValidation(EditCustomerDetailsActivity.this, pinCodeET, pinCodeLayout, new ArrayList<String>());
            UiUtils.checkValidation(EditCustomerDetailsActivity.this, stateET, stateLayout, new ArrayList<String>());

            if(!isValidRelation){
                if(isValidRelationOthers){
                    UiUtils.checkValidation(EditCustomerDetailsActivity.this, relationOthersET, relationOthersLayout, new ArrayList<String>());


                }else{
                    relationSpinnerErrorTv.setText("Field Required");
                    relationSpinnerErrorTv.setVisibility(View.VISIBLE);
                    isValidRelation=false;
                }


            }

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

            if(isResidingChecked==false){

                UiUtils.checkValidation(EditCustomerDetailsActivity.this, addressLine1ALternateET, addressLine1AlternateLayout, new ArrayList<String>());
                UiUtils.checkValidationForAutoCompleteTV(EditCustomerDetailsActivity.this, cityAlternateET, cityAlternateLayout, new ArrayList<String>());
                UiUtils.checkValidation(EditCustomerDetailsActivity.this, districtAlternateET, districtAlternateLayout, new ArrayList<String>());
                UiUtils.checkValidation(EditCustomerDetailsActivity.this, pinCodeAlternateET, pinCodeAlternateLayout, new ArrayList<String>());
                UiUtils.checkValidation(EditCustomerDetailsActivity.this, stateAlternateET, stateAlternateLayout, new ArrayList<String>());

            }

        }
        
    }

    private void callAPIForEditCustomerDetails() {

        continueButton.setEnabled(false);
        continueButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(EditCustomerDetailsActivity.this);
        RequestBody is_residing = null;
        RequestBody relation_others =null;
        RequestBody alternate_address_id = null;
        RequestBody alternate_address_line_1 = null;
        RequestBody alternate_address_line_2= null;
        RequestBody alternate_city_google_id = null;
        RequestBody alternate_pincode = null;

        try {
            Log.d("Aadhar onResponse", "--start" );

//            Add Group Id TODO
            Log.d("Aadhar onResponse", "--start" +isValidGender+"--"+genderValue+"---"+dobValue);

            RequestBody aadharNo = RequestBody.create(MediaType.parse("text/plain"), aadharNoET.getText().toString());
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), contactPersonET.getText().toString());
            RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), dobValue);
            Log.d("Aadhar onResponse", "--dobstart" );
            RequestBody gender = RequestBody.create(MediaType.parse("text/plain"),genderValue);
            RequestBody co = RequestBody.create(MediaType.parse("text/plain"), aadharCoET.getText().toString());
            RequestBody co_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(relationValue));
            Log.d("Aadhar onResponse", "--co_idstart" );
            if(isValidRelationOthers){
                relation_others = RequestBody.create(MediaType.parse("text/plain"), relationOthersET.getText().toString());
            }

            Log.d("Aadhar onResponse", "--relation_othersstart" );
            RequestBody address_id = RequestBody.create(MediaType.parse("text/plain"), addressId);
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
                alternate_address_id = RequestBody.create(MediaType.parse("text/plain"), residingAddressId);
                alternate_address_line_1 = RequestBody.create(MediaType.parse("text/plain"), addressLine1ALternateET.getText().toString());
                alternate_address_line_2 = RequestBody.create(MediaType.parse("text/plain"), addressLine2AlternateET.getText().toString());
                alternate_city_google_id = RequestBody.create(MediaType.parse("text/plain"), alternateGoogleId);
                alternate_pincode = RequestBody.create(MediaType.parse("text/plain"), pinCodeAlternateET.getText().toString());
            }

            Log.d("Aadhar onResponse", "--end" );

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerCustomerDetailResponseModel> call;
            call = apiService.updateLoanTakerCustomerDetails(
                    loanTakerID,
                    aadharNo,
                    name,
                    dob,
                    gender,
                    co,
                    co_id,
                    relation_others,
                    address_id,
                    address_line_1,
                    address_line_2,
                    city_google_id,
                    pincode,
                    primary_phone,
                    pan,
                    voterId,
                    is_residing,
                    alternate_address_id,
                    alternate_address_line_1,
                    alternate_address_line_2,
                    alternate_city_google_id,
                    alternate_pincode
                    );
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
                            Log.d("Aadhar onResponse1", "" +  GlobalValue.loanTakerId);
                            Toast.makeText(EditCustomerDetailsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            finishThisPage();

                            updateTheStatusInApplicantDetailPage();
                        } else {
                            Log.d("Aadhar onResponse2", "" + response.body().getErrors());
                            continueButton.setEnabled(true);
                            continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                if(jsonObject.getJSONObject("errors").has("pan_number")){
                                    Toast.makeText(EditCustomerDetailsActivity.this, "Pan number is not in a format of AAAAA9999A", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("Drools onResponse", "" + jsonObject);

                            NetworkUtils.handleErrorsCasesForAPICalls(EditCustomerDetailsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        continueButton.setEnabled(true);
                        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        Log.d("Aadhar onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(EditCustomerDetailsActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCustomerDetailResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    continueButton.setEnabled(true);
                    continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(EditCustomerDetailsActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void finishThisPage() {
        finish();
    }

    private void updateTheStatusInApplicantDetailPage() {
        ApplicantDetailActivity.getInstance().init();
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
                case R.id.input_mobile:
                    itemPassed.clear();
                    itemPassed.add("mobile");
                    boolean status = UiUtils.checkValidation(EditCustomerDetailsActivity.this, mobileET, mobileLayout, itemPassed);

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
                    boolean aadharStatus = UiUtils.checkValidation(EditCustomerDetailsActivity.this, aadharNoET, aadharNumberLayout, itemPassed);

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
                    boolean coStatus = UiUtils.checkValidation(EditCustomerDetailsActivity.this, aadharCoET, aadharCoLayout, itemPassed);

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
                    boolean personStatus = UiUtils.checkValidation(EditCustomerDetailsActivity.this, contactPersonET, contactPersonLayout, itemPassed);

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
                    itemPassed.add("");
                    boolean addressLine1Status = UiUtils.checkValidation(EditCustomerDetailsActivity.this, addressLine1ET, addressLine1Layout, itemPassed);

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
                    boolean cityStatus = UiUtils.checkValidationForAutoCompleteTV(EditCustomerDetailsActivity.this, cityET, cityLayout, itemPassed);

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
                    boolean districtStatus = UiUtils.checkValidation(EditCustomerDetailsActivity.this, districtET, districtLayout, itemPassed);

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
                    boolean stateStatus = UiUtils.checkValidation(EditCustomerDetailsActivity.this, stateET, stateLayout, itemPassed);

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
                    boolean pinCodeStatus = UiUtils.checkValidation(EditCustomerDetailsActivity.this, pinCodeET, pinCodeLayout, itemPassed);

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
//                    boolean panStatus = UiUtils.checkValidation(EditCustomerDetailsActivity.this, panNoET, panNoLayout, itemPassed);
//
//                    if (panStatus == false) {
////                        isValidPan = false;
//                        requestFocus(panNoET);
//
//                    } else {
////                        isValidPan=true;
//                        panNoLayout.setErrorEnabled(false);
//                    }
//
//                    break;
//
//                case R.id.input_voter_id:
//                    itemPassed.clear();
//                    itemPassed.add("");
//                    boolean voterStatus = UiUtils.checkValidation(EditCustomerDetailsActivity.this, voterIdET, voterIdLayout, itemPassed);
//
//                    if (voterStatus == false) {
////                        isValidVoterId = false;
//                        requestFocus(voterIdET);
//
//                    } else {
////                        isValidVoterId=true;
//                        voterIdLayout.setErrorEnabled(false);
//                    }
//
//                    break;


                case R.id.input_aadhar_co_relation:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean relationSTatus = UiUtils.checkValidationForAutoCompleteTV(EditCustomerDetailsActivity.this, aadharCoRelationET, aadharCoRelationLayout, itemPassed);

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
                    boolean relationOthersSTatus = UiUtils.checkValidation(EditCustomerDetailsActivity.this, relationOthersET, relationOthersLayout, itemPassed);

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
                    itemPassed.add("");
                    boolean addressLine1AltStatus = UiUtils.checkValidation(EditCustomerDetailsActivity.this, addressLine1ALternateET, addressLine1AlternateLayout, itemPassed);

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
                    boolean cityAltStatus = UiUtils.checkValidationForAutoCompleteTV(EditCustomerDetailsActivity.this, cityAlternateET, cityAlternateLayout, itemPassed);

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
                    boolean districtAltStatus = UiUtils.checkValidation(EditCustomerDetailsActivity.this, districtAlternateET, districtAlternateLayout, itemPassed);

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
                    boolean stateAltStatus = UiUtils.checkValidation(EditCustomerDetailsActivity.this, stateAlternateET, stateAlternateLayout, itemPassed);

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
                    boolean pinCodeAltStatus = UiUtils.checkValidation(EditCustomerDetailsActivity.this, pinCodeAlternateET, pinCodeAlternateLayout, itemPassed);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(EditCustomerDetailsActivity.this);
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

        Intent intent = new Intent(EditCustomerDetailsActivity.this,AddNewMemberScanActivity.class);
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

    private void getCustomerDetails() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerCustomerDetailResponseModel> call = apiService.getCustomerDetails(loanTakerID);
            call.enqueue(new Callback<LoanTakerCustomerDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCustomerDetailResponseModel> call, final Response<LoanTakerCustomerDetailResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Log.i("Drools", "" + new Gson().toJson(response.body()));

                            setValuesFromResponse(response.body());




                        } else {
                            NetworkUtils.handleErrorsForAPICalls(EditCustomerDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(EditCustomerDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCustomerDetailResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(EditCustomerDetailsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(LoanTakerCustomerDetailResponseModel body) {

        if(body.getLoanTakerModel().getAadhar_number()!=null){
            aadharNoET.setText(body.getLoanTakerModel().getAadhar_number());
            isValidAadhar = true;
        }

        if(body.getLoanTakerModel().getName()!=null){
            contactPersonET.setText(body.getLoanTakerModel().getName());
            isValidNContactPerson = true;
        }

        if(body.getLoanTakerModel().getDob()!=null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = format.parse(body.getLoanTakerModel().getDob());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            calendar.setTime(date);
            System.out.println(calendar.get(Calendar.YEAR));
            System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
            System.out.println(new SimpleDateFormat("MM").format(calendar.getTime()));
            dayET.setText(String .valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            monthET.setText(String .valueOf(new SimpleDateFormat("MMM").format(calendar.getTime())));
            yearET.setText(String .valueOf(calendar.get(Calendar.YEAR)));

            pickedDay = calendar.get(Calendar.DAY_OF_MONTH);
            pickedMonth = Integer.parseInt(new SimpleDateFormat("MM").format(calendar.getTime()));
            pickedYear = calendar.get(Calendar.YEAR);

            dobValue = dayET.getText().toString().trim()+"/"+monthET.getText().toString().trim()+"/"+
            yearET.getText().toString().trim();
            isValidDob = true;
        }


        if(body.getLoanTakerModel().getCustomerModel().getGender()!=null){
            String genderVal = body.getLoanTakerModel().getCustomerModel().getGender();

            addRadioButtons(GlobalValue.applicantGenderList,genderVal);
            if(genderVal == "Male"){
                genderValue = "Male";

            }else if(genderVal =="Female"){
                genderValue = "Female";

            }else if(genderVal == "E")
            {
                genderValue = "E";
            }
            isValidGender = true;
        }


        if(body.getLoanTakerModel().getAadhar_co()!=null){
            aadharCoET.setText(body.getLoanTakerModel().getAadhar_co());
            isValidAadharCo = true;
        }

        if(body.getLoanTakerModel().getAadharCoRelationModel().getName()!=null){
            aadharCoRelationET.setText(body.getLoanTakerModel().getAadharCoRelationModel().getName());
            for(int i=0;i<applicantLoanTakerRelationsArrayListNew.size();i++){
                if(applicantLoanTakerRelationsArrayListNew.get(i).getId().equalsIgnoreCase(body.getLoanTakerModel().getAadharCoRelationModel().getId())){
                    relationSpinner.setSelection(i);
                }
            }

            relationValue = Integer.parseInt(body.getLoanTakerModel().getAadharCoRelationModel().getId());
            isValidRelation = true;
        }

        if(body.getLoanTakerModel().getAadharCoRelationModel().getName().equalsIgnoreCase("Other")){
            relationOthersET.setVisibility(View.VISIBLE);
            relationOthersLayout.setVisibility(View.VISIBLE);
            requestFocus(relationOthersET);
            isValidRelationOthers = true;
            isValidRelation=false;
            relationOthersET.setText(body.getLoanTakerModel().getAadhar_co_relation_name());
        }

        if(body.getLoanTakerModel().getHomeAddress().getLine1()!=null){
            addressId =  body.getLoanTakerModel().getHomeAddress().getId();
            addressLine1ET.setText(body.getLoanTakerModel().getHomeAddress().getLine1());
            isValidAddressLine1 = true;
        }

        if(body.getLoanTakerModel().getHomeAddress().getLine2()!=null){
            addressLine2ET.setText(body.getLoanTakerModel().getHomeAddress().getLine2());
        }

        if(body.getLoanTakerModel().getHomeAddress().getGooglePlaceId()!=null){
            cityGoogleId= body.getLoanTakerModel().getHomeAddress().getGooglePlaceId();
            cityET.setText(body.getLoanTakerModel().getHomeAddress().getCity_name());
            isValidCity= true;
            districtET.setText(body.getLoanTakerModel().getHomeAddress().getDistrictName());
            isValidDistrict = true;
            stateET.setText(body.getLoanTakerModel().getHomeAddress().getState_name());
            isValidState = true;
        }

        if(body.getLoanTakerModel().getHomeAddress().getPinCode()!=null){
            pinCodeET.setText(body.getLoanTakerModel().getHomeAddress().getPinCode());
            isValidPinCode=true;
        }

        if(body.getLoanTakerModel().getPan_number()!=null){
            panNoET.setText(body.getLoanTakerModel().getPan_number());
        }

        if(body.getLoanTakerModel().getVoter_id()!=null){
            voterIdET.setText(body.getLoanTakerModel().getVoter_id());
        }

        if(body.getLoanTakerModel().getPrimary_phone_number()!=null){
            mobileET.setText(body.getLoanTakerModel().getPrimary_phone_number());
            isValidMobile=true;
        }

        if(body.getLoanTakerModel().getIs_residing_address()){
            alternateAddressCheckbox.setChecked(true);
            isResidingChecked=true;
        }else {
            alternateAddressCheckbox.setChecked(false);

            if(body.getLoanTakerModel().getResidentAddressModel().getLine1()!=null){
                residingAddressId =  body.getLoanTakerModel().getResidentAddressModel().getId();
                addressLine1ALternateET.setText(body.getLoanTakerModel().getResidentAddressModel().getLine1());
                isValidAddressAlternateLine1 = true;
            }

            if(body.getLoanTakerModel().getResidentAddressModel().getLine2()!=null){
                addressLine2AlternateET.setText(body.getLoanTakerModel().getResidentAddressModel().getLine2());
            }

            if(body.getLoanTakerModel().getResidentAddressModel().getGoogle_place_id()!=null){
                cityGoogleId= body.getLoanTakerModel().getResidentAddressModel().getGoogle_place_id();
                cityAlternateET.setText(body.getLoanTakerModel().getResidentAddressModel().getCity_name());
                isValidAlternateCity= true;
                districtAlternateET.setText(body.getLoanTakerModel().getResidentAddressModel().getDistrict_name());
                isValidAlternateDistrict = true;
                stateAlternateET.setText(body.getLoanTakerModel().getResidentAddressModel().getState_name());
                isValidAlternateState = true;
            }

            if(body.getLoanTakerModel().getResidentAddressModel().getPincode()!=null){
                pinCodeAlternateET.setText(body.getLoanTakerModel().getResidentAddressModel().getPincode());
                isValidAlternatePinCode=true;
            }

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

                            GlobalValue.applicantGenderList = response.body().getGenders();


                            try {
                                JSONObject jsonObject =  new JSONObject(new Gson().toJson(response.body()));
                                Log.i("Drools", "" + jsonObject);
                                if (jsonObject.has("genders")){
                                    jsonObject.getJSONArray("genders").length();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                        }//public void run() {
                                    });

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



                                if (NetworkUtils.haveNetworkConnection(EditCustomerDetailsActivity.this)) {
                                    showProgressDialog(EditCustomerDetailsActivity.this);
                                    getCustomerDetails();
                                } else {
                                    UiUtils.showAlertDialogWithOKButton(EditCustomerDetailsActivity.this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                }
//                                if(response.body().getCacdLoanTakerRelationsModelArrayList().size() >0){
//
//                                    applicantLoanTakerRelationsArrayList =response.body().getCacdLoanTakerRelationsModelArrayList();
//                                    relationsSpinnerAdapter = new RelationsSpinnerAdapter(EditCustomerDetailsActivity.this, R.layout.spinner_item,applicantLoanTakerRelationsArrayList);
//                                    aadharCoRelationET.setAdapter(relationsSpinnerAdapter);
//
//                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {
                            NetworkUtils.handleErrorsForAPICalls(EditCustomerDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(EditCustomerDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<CustomerApplicantCommonDataResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(EditCustomerDetailsActivity.this, null, null);
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


        relationSpinnerAdapter = new RelationSpinnerAdapter(EditCustomerDetailsActivity.this,
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


    private void pickUpFromCalendar() {
        final Calendar cldr = Calendar.getInstance();
        int day = pickedDay;
        int month = pickedMonth-1;
        int year = pickedYear;

        //for calendar spinner
        picker = new DatePickerDialog(EditCustomerDetailsActivity.this,R.style.CustomDatePickerDialog,
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
            EditCustomerDetailsActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}
