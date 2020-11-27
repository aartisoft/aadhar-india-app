package com.tailwebs.aadharindia.center;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import com.tailwebs.aadharindia.aadharscan.AadharScanActivity;
import com.tailwebs.aadharindia.center.searchinmap.adapters.PlaceAutocompleteAdapter;
import com.tailwebs.aadharindia.center.searchinmap.models.PlaceInfo;
import com.tailwebs.aadharindia.home.NewGroupTaskActivity;
import com.tailwebs.aadharindia.models.city.CityCenterCommonDataResponseModel;
import com.tailwebs.aadharindia.models.city.CityResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.Constants;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.SharedPreferenceUtils;
import com.tailwebs.aadharindia.utils.UiUtils;
import com.tailwebs.aadharindia.utils.custom.cropper.CropImage;
import com.tailwebs.aadharindia.utils.custom.cropper.CropImageView;
import com.tailwebs.aadharindia.utils.custom.multipleimageupload.models.ImageUploadModel;
import com.tailwebs.aadharindia.utils.custom.newmultipleimageupload.NavigatorImage;
import com.tailwebs.aadharindia.utils.custom.newmultipleimageupload.model.Image;
import com.tailwebs.aadharindia.utils.custom.newmultipleimageupload.view.ImageGridView;
import com.tailwebs.aadharindia.utils.custom.singleselecttoggle.SingleSelectToggleGroup;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewCenterActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "CreateNewCenterActivity";

    //Contact Person

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

    @BindView(R.id.input_layout_mobile)
    TextInputLayout mobileLayout;

    @BindView(R.id.input_mobile)
    TextInputEditText mobileET;

    //Address

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

    //images

    @BindView(R.id.contact_person_upload_image)
    ImageView contactPersonUploadImage;

//    @BindView(R.id.multiple_image_upload)
//    ImageView multipleImageUpload;

    @BindView(R.id.submit_button)
    Button submitButton;


    DatePickerDialog picker;

    @BindView(R.id.center_images_grid_view)
    ImageGridView centerImagesGridView;

    @BindView(R.id.center_images_error_tv)
    TextView centerImagesErrorTV;

    @BindView(R.id.profile_pic_error_tv)
    TextView profilePicImageErrorTV;

    @BindView(R.id.dob_error_tv)
    TextView dobErrorTV;

    @BindView(R.id.gender_error_tv)
    TextView genderErrorTV;


    int pickedDay = 0;
    int pickedMonth = 0;
    int pickedYear = 0;


    String dobValue =null,genderValue=null,profilePicUriPath=null,profilePicUri=null,cityGoogleId=null;

    private FirebaseAnalytics mFirebaseAnalytics;

    ArrayList<ImageUploadModel> centerImagesUpload=null;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};





    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    private ProgressDialog mProgressDialog;

    @BindView(R.id.gender_group_single_radiobutton)
    SingleSelectToggleGroup genderToggle;




    //multiple image upload
    private static final int REQUEST_CODE_MULTIPLE_IMAGE_UPLOAD = 732;
    private ArrayList<String> mResults = new ArrayList<>();
    String dobValueFromIntent,genderValueFromIntent;


    private boolean isValidAadhar = false, isValidNContactPerson = false, isValidDob = false,isValidGender = false,
            isValidAadharCo=false,isValidMobile =false,isValidAddressLine1=false,isValidCity=false,isValidDistrict=false,
            isValidPinCode=false,isValidState=false,isValidProfilePicture=false,isValidCenterPhotos=false,isScanned=false;

    private boolean isMale=false,isFemale=false;

    int dayInitial = 01;
    int monthInital = 00;
    int yearInital = 1970;


    //for city

    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;
    List<Image> selectedImages;


    PlacesClient placesClient;
    AutoCompleteAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);
        setContentView(R.layout.activity_create_new_center);
        ButterKnife.bind(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Add Center", null);

        String apiKey = getString(R.string.google_maps_key);
        if(apiKey.isEmpty()){
            Toast.makeText( CreateNewCenterActivity.this, "Google Api key is missing.", Toast.LENGTH_SHORT ).show();
            return;
        }


        // Setup Places Client
        if (!com.google.android.libraries.places.api.Places.isInitialized()) {
            com.google.android.libraries.places.api.Places.initialize(getApplicationContext(), apiKey);
        }

        placesClient = com.google.android.libraries.places.api.Places.createClient(this);
        initAutoCompleteTextView();


        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Create New Center");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        GlobalValue.secret = SharedPreferenceUtils.getValue(CreateNewCenterActivity.this, Constants.KEY_SECRET);
        GlobalValue.secret_id = SharedPreferenceUtils.getValue(CreateNewCenterActivity.this, Constants.KEY_SECRET_ID);

        submitButton.setOnClickListener(this);
        dayET.setInputType(InputType.TYPE_NULL);
        monthET.setInputType(InputType.TYPE_NULL);
        yearET.setInputType(InputType.TYPE_NULL);

        dayET.setOnClickListener(this);
        monthET.setOnClickListener(this);
        yearET.setOnClickListener(this);





        aadharScanImgView.setOnClickListener(this);


        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(CreateNewCenterActivity.this);

            getCityCenterCommonData();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }


        hideSoftKeyboard();

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


        mobileET.addTextChangedListener(new InputLayoutTextWatcher(mobileET));
        aadharNoET.addTextChangedListener(new InputLayoutTextWatcher(aadharNoET));
        aadharCoET.addTextChangedListener(new InputLayoutTextWatcher(aadharCoET));
        contactPersonET.addTextChangedListener(new InputLayoutTextWatcher(contactPersonET));
        addressLine1ET.addTextChangedListener(new InputLayoutTextWatcher(addressLine1ET));
        districtET.addTextChangedListener(new InputLayoutTextWatcher(districtET));
        stateET.addTextChangedListener(new InputLayoutTextWatcher(stateET));
        pinCodeET.addTextChangedListener(new InputLayoutTextWatcher(pinCodeET));
        cityET.addTextChangedListener(new InputLayoutTextWatcher(cityET));


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


        // No need

//        getValuesFromCityID(GlobalValue.placeId);
//        cityGoogleId = GlobalValue.placeId;
//        cityET.setText(GlobalValue.placeAddress);

        contactPersonUploadImage.setOnClickListener(this);


        contactPersonET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_NEXT){

                    //show calendar popup
                    hideSoftKeyboard();
                    pickUpFromCalendar();
                } else if(actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    hideSoftKeyboard();

                }

                return false;
            }
        });


        aadharCoET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_NEXT){

                    requestFocus(mobileET);

                } else if(actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    hideSoftKeyboard();

                }

                return false;
            }
        });


        mobileET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_NEXT){

                    requestFocus(addressLine1ET);

                } else if(actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    hideSoftKeyboard();

                }

                return false;
            }
        });


        //citycenter Grid
        centerImagesGridView.setGRID_TYPE(2);




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

//        cityET.setOnItemClickListener(mAutocompleteClickListener);
//        cityET.setAdapter(mPlaceAutocompleteAdapter);
    }


    private void initAutoCompleteTextView() {


        cityET.setThreshold(1);
        cityET.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(this, placesClient);
        cityET.setAdapter(adapter);
    }


    /*
        --------------------------- google places API autocomplete suggestions -----------------
     */



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
                            showProgressDialog(CreateNewCenterActivity.this);
                            getValuesFromCityID(task.getPlace().getId());
                            cityGoogleId = task.getPlace().getId();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText( CreateNewCenterActivity.this, "Cannot Retrieve Location", Toast.LENGTH_SHORT ).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);


        }
    };

    private void getValuesFromCityID(String cityGoogleId) {
        showProgressDialog(CreateNewCenterActivity.this);
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
//                       if(response.body().getCity().getPincode()!=null){
//                           pinCodeET.setText(response.body().getCity().getPincode());
//                       }else{
//
//                           if(pinCodeET.getText().toString().length()>0)
//                           pinCodeET.setText("");
//                       }


                    } else {
                        hideProgressDialog();
                        NetworkUtils.handleErrorsCasesForAPICalls(CreateNewCenterActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<CityResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(CreateNewCenterActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void getCityCenterCommonData() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<CityCenterCommonDataResponseModel> call = apiService.getCityCenterCommonDatas();
            call.enqueue(new Callback<CityCenterCommonDataResponseModel>() {
                @Override
                public void onResponse(Call<CityCenterCommonDataResponseModel> call, final Response<CityCenterCommonDataResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            centerImagesGridView.setMaxValue(response.body().getCity_center_image_count());

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


                                            showProfileData(getIntent().getExtras());

                                        }//public void run() {
                                    });

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {
                            NetworkUtils.handleErrorsForAPICalls(CreateNewCenterActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(CreateNewCenterActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<CityCenterCommonDataResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(CreateNewCenterActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
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
                String result = address.substring(address.lastIndexOf(',') + 1).trim();
                String[] bits = address.split(",");
                String lastWord = bits[bits.length - 2];
//                stateET.setText(lastWord);
                mPlace.setId(place.getId());
                cityGoogleId = place.getId();

                getValuesFromCityID(cityGoogleId);
                Log.d(TAG, "onResult: id:" + result+"--"+lastWord);
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

    private void showProfileData(Bundle bundle) {





        if (bundle.getString("aadhar_no") != null) {
            aadharNoET.setText(bundle.getString("aadhar_no"));
            isValidAadhar = true;
        }


try{

        if(bundle.getBoolean("is_scanned")) {
            isScanned = true;

            if (bundle.getString("username") != null) {
                contactPersonET.setText(bundle.getString("username"));
                isValidNContactPerson = true;
            }


            if (bundle.getString("user_co_name") != null) {

                if( (bundle.getString("user_co_name").contains(":"))){
                    String s =bundle.getString("user_co_name");
                    String s1 = s.substring(s.indexOf(":")+1);
                    s1.trim();
                    aadharCoET.setText(s1);
                }else{
                    aadharCoET.setText(bundle.getString("user_co_name"));
                }



                isValidAadharCo = true;
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

                }else if (dobValueFromIntent.contains("-")){
                    dayET.setText(UiUtils.getDateDash(dobValueFromIntent));
                    dayInitial = Integer.valueOf(UiUtils.getDateDash(dobValueFromIntent));

                    monthET.setText(UiUtils.getMonthDash(dobValueFromIntent));
                    monthInital = Integer.valueOf(UiUtils.getMonthDash(dobValueFromIntent));

                    yearET.setText(UiUtils.getYearDash(dobValueFromIntent));
                    yearInital = Integer.valueOf(UiUtils.getYearDash(dobValueFromIntent));

                    dobValue = dayInitial + "/" + monthInital + "/" + yearInital;
                    isValidDob = true;

                }else {

                    isValidDob = false;
                }
//

            }

            if (bundle.getString("gender") != null) {
                genderValueFromIntent = (bundle.getString("gender"));

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
                } else{
                    isValidGender = false;
                    addRadioButtons( GlobalValue.applicantGenderList);
                }

            }


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


            if (bundle.getString("pin_code") != null) {

                pinCodeET.setText( bundle.getString("pin_code"));
                isValidPinCode = true;
            }
        }else{
            isValidGender = false;
            addRadioButtons(GlobalValue.applicantGenderList);
        }
} catch (Exception e){
    e.printStackTrace();
}

//
//        if (bundle.getString("city") != null) {
//
//            cityET.setText( bundle.getString("city"));
//          isValidCity = true;
//
//        }
//
//        if (bundle.getString("district") != null) {
//
//            districtET.setText( bundle.getString("district"));
//         isValidDistrict=true;
//
//        }
//

//
//        if (bundle.getString("state") != null) {
//          isValidState = true;
//            stateET.setText( bundle.getString("state"));
//
//        }


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.submit_button:

                addNewCenterCall();
                break;

            case R.id.input_day:
                hideSoftKeyboard();
                pickUpFromCalendar();
                break;

            case R.id.input_month:
                hideSoftKeyboard();
                pickUpFromCalendar();
                break;

            case R.id.input_year:
                hideSoftKeyboard();
                pickUpFromCalendar();
                break;

            case R.id.contact_person_upload_image:

                CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);

                break;


//            case R.id.male_tv:
//
//                chooseGender("M");
//
//                break;
//
//            case R.id.female_tv:
//
//                chooseGender("F");
//                break;

//            case R.id.multiple_image_upload:
//
//                Intent intent = new Intent(CreateNewCenterActivity.this, ImagesSelectorActivity.class);
//                // max number of images to be selected
//                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 10);
//                // min size of image which will be shown; to filter tiny images (mainly icons)
//                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
//                // show camera or not
//                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
//                // pass current selected images as the initial value
//                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
//                // start the selector
//                startActivityForResult(intent, REQUEST_CODE_MULTIPLE_IMAGE_UPLOAD);
//
//                break;

            case R.id.back_button:
                AadharScanActivity.getInstance().setIsReadToFalse();
                onBackPressed();
                break;

            case R.id.scan_aadhar_img:
                showAlertDialogForScan();
                break;
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

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateNewCenterActivity.this);
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

    private void showAlertDialogForScan(){
        // title, custom view, actions dialog


        View view = getLayoutInflater().inflate(R.layout.custom_message_dialog, null);

        TextView messageTV =(TextView)view.findViewById(R.id.message_tv);
        messageTV.setText(getResources().getString(R.string.hint_restart_process));
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateNewCenterActivity.this);
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

        Intent intent = new Intent(CreateNewCenterActivity.this,AadharScanActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("place_name", GlobalValue.placeName);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

//    private void chooseGender(String m) {
//
//        if(m.equalsIgnoreCase("M")){
//            maleTV.setTextAppearance(getApplicationContext(),R.style.MyRadioSelected);
//            femaleTV.setTextAppearance(getApplicationContext(),R.style.MyRadioNotSelected);
//            maleTV.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_selected));
//            femaleTV.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_not_selected));
//            genderValue="Male";
//
//        }else{
//
//            femaleTV.setTextAppearance(getApplicationContext(),R.style.MyRadioSelected);
//            maleTV.setTextAppearance(getApplicationContext(),R.style.MyRadioNotSelected);
//            femaleTV.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_selected));
//            maleTV.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_not_selected));
//            genderValue = "Female";
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                isValidProfilePicture =true;
                submitButton.setEnabled(false);
                hideSoftKeyboardAfter();
                pinCodeET.setCursorVisible(false);
                addressLine1ET.setCursorVisible(false);
                new AsyncTaskRunnerForProfilePic().execute(String.valueOf(result.getUri()));

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }else if(resultCode == RESULT_OK){
            centerImagesGridView.onParentResult(requestCode, data);

            if(data!=null) {


                List<Image> images = (List<Image>) data.getSerializableExtra(NavigatorImage.EXTRA_PHOTOS_URL);
                ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                if (requestCode == NavigatorImage.RESULT_SELECT_PHOTOS && null != images) {


                    if (selectedImages != null) {
                        for (Image image : images) {
                            selectedImages.add(image);
                            Log.i("CHeck 1", "added" + images.size());
                        }

                    } else {
                        selectedImages = images;
                    }


                    Log.i("CHeck 1", "size" + images.size());
                    for (Image image : images) {
                        Log.i("CHeck 2", "size path" + image.url);

                    }
                    isValidCenterPhotos = true;
                    centerImagesErrorTV.setVisibility(View.GONE);

                } else if (requestCode == NavigatorImage.RESULT_IMAGE_SWITCHER && null != positions) {

                    Log.i("CHeck", "positionsss" + positions.size());
                    Log.i("CHeck", "positionsss" + positions);

//                for (int i=0;i>positions.size();i++){

                    for (int position : positions) {
                        selectedImages.remove(selectedImages.get(position));
                    }

                    Log.i("CHeck", "latest" + selectedImages.size());

                }
            }

        }
    }


    private class AsyncTaskRunnerForProfilePic extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {

                Uri myUri = Uri.parse(params[0]);
                profilePicUri = String.valueOf(myUri);
                ((ImageView) findViewById(R.id.contact_person_upload_image)).setImageURI(myUri);
            }  catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

            Bitmap bm=((BitmapDrawable)contactPersonUploadImage.getDrawable()).getBitmap();
            saveImageFile(bm);
            hideProgressDialog();
            hideSoftKeyboardAfter();
            profilePicImageErrorTV.setVisibility(View.GONE);


        }

        @Override
        protected void onPreExecute() {
            showProgressDialog(CreateNewCenterActivity.this);

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }


    public String saveImageFile(Bitmap bitmap) {
        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }

    private String getFilename() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "AadharFolder");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".png");

        profilePicUriPath = uriSting;
//        Toast.makeText(this, "Profile pic saved", Toast.LENGTH_SHORT).show();
        submitButton.setEnabled(true);
        return uriSting;
    }


    private void pickUpFromCalendar() {
        final Calendar cldr = Calendar.getInstance();
//        int day = dayInitial;
//        int month = monthInital-1;
//        int year = yearInital;


        int day,month,year;

//        if(isScanned){
//            day = dayInitial;
//            month = monthInital-1;
//            year = yearInital;
//        }else{
//            day = 01;
//            month = 00;
//            year = 1970;
//        }

        if(isScanned){
            if(pickedDay==0){

                day = dayInitial;
                month = monthInital-1;
                year = yearInital;
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


        //for calendar spinner
        picker = new DatePickerDialog(CreateNewCenterActivity.this,R.style.CustomDatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        dayET.setText(String.valueOf(dayOfMonth));
//                        monthET.setText(String.valueOf(monthOfYear + 1));
//                        yearET.setText(String.valueOf(year));
//
//                        dobValue = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;


                        SimpleDateFormat sdf = new SimpleDateFormat("MMM");
                        cldr.set(year, monthOfYear, dayOfMonth);
                        String month = sdf.format(cldr.getTime());


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

    private void addNewCenterCall() {


        GlobalValue.centerImagesForConfirmationNew = selectedImages;



        //dob,gender,city,profilepic,centre photos

        if ((isValidAadhar) && (isValidNContactPerson)  && (isValidDob) &&
                (isValidGender) && (isValidAadharCo)  && (isValidMobile) &&
                (isValidAddressLine1) && (isValidCity)  && (isValidDistrict) &&
                (isValidPinCode) && (isValidState)  && (isValidProfilePicture) && (isValidCenterPhotos)){


            Intent intent = new Intent(CreateNewCenterActivity.this,CenterConfirmationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("username", contactPersonET.getText().toString());
            bundle.putString("user_co_name",aadharCoET.getText().toString());
            bundle.putString("phone", mobileET.getText().toString());
            bundle.putString("aadhar_no", aadharNoET.getText().toString());
            bundle.putString("dob", dobValue);
            bundle.putString("gender", genderValue);
            bundle.putString("address_line_1", addressLine1ET.getText().toString());
            bundle.putString("address_line_2", addressLine2ET.getText().toString());
            bundle.putString("city", cityET.getText().toString());
            bundle.putString("city_google_place_id", cityGoogleId);
            bundle.putString("district", districtET.getText().toString());
            bundle.putString("state", stateET.getText().toString());
            bundle.putString("pin_code", pinCodeET.getText().toString());
            bundle.putString("profile_image", profilePicUri);
            bundle.putString("profile_image_path", profilePicUriPath);

            Log.d("Shahana ProfPath",""+profilePicUriPath);


            Bundle params = new Bundle();
            params.putString("center_level","started");

            mFirebaseAnalytics.logEvent("center", params);

            intent.putExtras(bundle);
            startActivity(intent);



        }else{
            UiUtils.checkValidation(CreateNewCenterActivity.this, aadharNoET, aadharNumberLayout, new ArrayList<String>());
            UiUtils.checkValidation(CreateNewCenterActivity.this, contactPersonET, contactPersonLayout, new ArrayList<String>());
            UiUtils.checkValidation(CreateNewCenterActivity.this, aadharCoET, aadharCoLayout, new ArrayList<String>());
            UiUtils.checkValidation(CreateNewCenterActivity.this, mobileET, mobileLayout, new ArrayList<String>());
            UiUtils.checkValidation(CreateNewCenterActivity.this, addressLine1ET, addressLine1Layout, new ArrayList<String>());
            UiUtils.checkValidationForAutoCompleteTV(CreateNewCenterActivity.this, cityET, cityLayout, new ArrayList<String>());
            UiUtils.checkValidation(CreateNewCenterActivity.this, districtET, districtLayout, new ArrayList<String>());
            UiUtils.checkValidation(CreateNewCenterActivity.this, pinCodeET, pinCodeLayout, new ArrayList<String>());
            UiUtils.checkValidation(CreateNewCenterActivity.this, stateET, stateLayout, new ArrayList<String>());

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
                genderErrorTV.setVisibility(View.VISIBLE);
                isValidGender=true;
            }

            if(profilePicUriPath==null){
                profilePicImageErrorTV.setText("Field Required");
                profilePicImageErrorTV.setVisibility(View.VISIBLE);
                isValidProfilePicture=false;
            }else{
                profilePicImageErrorTV.setVisibility(View.VISIBLE);
                isValidProfilePicture=true;
            }

            if(selectedImages==null){
                centerImagesErrorTV.setText("Atleast one image is required");
                centerImagesErrorTV.setVisibility(View.VISIBLE);
                isValidCenterPhotos=false;
            }else{
                centerImagesErrorTV.setVisibility(View.VISIBLE);
                isValidCenterPhotos=true;
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                case R.id.input_mobile:
                    itemPassed.clear();
                    itemPassed.add("mobile");
                    boolean status = UiUtils.checkValidation(CreateNewCenterActivity.this, mobileET, mobileLayout, itemPassed);

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
                    hideSoftKeyboard();
                    boolean aadharStatus = UiUtils.checkValidation(CreateNewCenterActivity.this, aadharNoET, aadharNumberLayout, itemPassed);

                    if (aadharStatus == false) {
                        isValidAadhar = false;
//                        requestFocus(aadharNoET);
                    } else {
                        isValidAadhar = true;
//                        aadharNumberLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_aadhar_co:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean coStatus = UiUtils.checkValidation(CreateNewCenterActivity.this, aadharCoET, aadharCoLayout, itemPassed);

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
                    boolean personStatus = UiUtils.checkValidation(CreateNewCenterActivity.this, contactPersonET, contactPersonLayout, itemPassed);

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
                    boolean addressLine1Status = UiUtils.checkValidation(CreateNewCenterActivity.this, addressLine1ET, addressLine1Layout, itemPassed);

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
                    boolean cityStatus = UiUtils.checkValidationForAutoCompleteTV(CreateNewCenterActivity.this, cityET, cityLayout, itemPassed);

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
                    boolean districtStatus = UiUtils.checkValidation(CreateNewCenterActivity.this, districtET, districtLayout, itemPassed);

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
                    boolean stateStatus = UiUtils.checkValidation(CreateNewCenterActivity.this, stateET, stateLayout, itemPassed);

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
                    boolean pinCodeStatus = UiUtils.checkValidation(CreateNewCenterActivity.this, pinCodeET, pinCodeLayout, itemPassed);

                    if (pinCodeStatus == false) {
                        isValidPinCode = false;
                        requestFocus(pinCodeET);

                    } else {
                        isValidPinCode=true;
                        pinCodeLayout.setErrorEnabled(false);
                    }

                    break;
            }
        }

    }


    public void requestFocus(View view) {
        if (view.requestFocus())
            CreateNewCenterActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(CreateNewCenterActivity.this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    private void hideSoftKeyboardAfter(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


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
