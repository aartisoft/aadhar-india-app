package com.tailwebs.aadharindia.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
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
import com.tailwebs.aadharindia.center.searchinmap.adapters.PlaceAutocompleteAdapter;
import com.tailwebs.aadharindia.center.searchinmap.models.PlaceInfo;
import com.tailwebs.aadharindia.home.dashboard.TaskDashboardActivity;
import com.tailwebs.aadharindia.home.models.TaskResponseModel;
import com.tailwebs.aadharindia.member.GroupMemberListingActivity;
import com.tailwebs.aadharindia.member.GroupMemberListingResponseModel;
import com.tailwebs.aadharindia.models.city.CityResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeadersForLocation;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.GpsTracker;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewGroupTaskActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = "NewGroupTaskActivity";


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    @BindView(R.id.input_layout_task)
    TextInputLayout taskLayout;

    @BindView(R.id.input_task)
    TextInputEditText taskET;

    @BindView(R.id.input_layout_name)
    TextInputLayout nameLayout;

    @BindView(R.id.input_name)
    TextInputEditText nameET;

    @BindView(R.id.input_location_layout)
    TextInputLayout locationLayout;

    @BindView(R.id.input_location)
    AutoCompleteTextView locationET;

    @BindView(R.id.input_layout_date)
    TextInputLayout dateLayout;

    @BindView(R.id.input_date)
    TextInputEditText dateET;

    @BindView(R.id.input_layout_notes)
    TextInputLayout notesLayout;

    @BindView(R.id.input_notes)
    TextInputEditText notesET;

    @BindView(R.id.save_task_button)
    Button saveTaskButton;


    private ProgressDialog mProgressDialog;

    DatePickerDialog picker;

    String cityId=null,dobValue=null;


    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    //for city

    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;


    private boolean isValidLocation = false, isValidNotes = false, isValidDate = false;

    int pickedDay = 0;
    int pickedMonth = 0;
    int pickedYear = 0;

    private GpsTracker gpsTracker;
    String currentLat=null,currentLong=null;

    PlacesClient placesClient;
    AutoCompleteAdapter adapter;

    private FirebaseAnalytics mFirebaseAnalytics;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group_task);
        ButterKnife.bind(this);

//        getLocationPermission();


        String apiKey = getString(R.string.google_maps_key);
        if(apiKey.isEmpty()){
            Toast.makeText( NewGroupTaskActivity.this, "Google Api key is missing.", Toast.LENGTH_SHORT ).show();
            return;
        }


        // Setup Places Client
        if (!com.google.android.libraries.places.api.Places.isInitialized()) {
            com.google.android.libraries.places.api.Places.initialize(getApplicationContext(), apiKey);
        }

        placesClient = com.google.android.libraries.places.api.Places.createClient(this);
        initAutoCompleteTextView();




        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Create Group Task", null);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("New Group Task");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        //show current Lat and Long

        try {
            gpsTracker = new GpsTracker(NewGroupTaskActivity.this);
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

            getNewTask();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }



        dateET.setOnClickListener(this);
        saveTaskButton.setOnClickListener(this);


        locationET.addTextChangedListener(new InputLayoutTextWatcher(locationET));
        dateET.addTextChangedListener(new InputLayoutTextWatcher(dateET));
        notesET.addTextChangedListener(new InputLayoutTextWatcher(notesET));
        nameET.addTextChangedListener(new InputLayoutTextWatcher(nameET));
        taskET.addTextChangedListener(new InputLayoutTextWatcher(taskET));


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

//        locationET.setOnItemClickListener(mAutocompleteClickListener);
//        locationET.setAdapter(mPlaceAutocompleteAdapter);


    }


    private void initAutoCompleteTextView() {


        locationET.setThreshold(1);
        locationET.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(this, placesClient);
        locationET.setAdapter(adapter);
    }


    private void getLocationPermission(){

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){


            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    private void getNewTask() {

        saveTaskButton.setEnabled(false);
        saveTaskButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(NewGroupTaskActivity.this);
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Date currentTime = Calendar.getInstance().getTime();
            Log.i("Drools", "" + currentTime);
            Call<TaskResponseModel> call = apiService.getNewTask();
            call.enqueue(new Callback<TaskResponseModel>() {
                @Override
                public void onResponse(Call<TaskResponseModel> call, final Response<TaskResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            saveTaskButton.setEnabled(true);
                            saveTaskButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));

                            Date currentTime = Calendar.getInstance().getTime();
                            Log.i("Drools success", "" + currentTime);

                            taskET.setText(response.body().getTaskModel().getName());

                        } else {

                            saveTaskButton.setEnabled(true);
                            saveTaskButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                            Date currentTime = Calendar.getInstance().getTime();
                            Log.i("Drools error1", "" + currentTime);
                            NetworkUtils.handleErrorsForAPICalls(NewGroupTaskActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {

                        saveTaskButton.setEnabled(true);
                        saveTaskButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        Date currentTime = Calendar.getInstance().getTime();
                        Log.i("Drools error2", "" + currentTime);
                        NetworkUtils.handleErrorsCasesForAPICalls(NewGroupTaskActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<TaskResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    saveTaskButton.setEnabled(true);
                    saveTaskButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    Log.i("Drools", "" + t.getMessage());
                    Date currentTime = Calendar.getInstance().getTime();
                    Log.i("Drools fail", "" + currentTime);
                    NetworkUtils.handleErrorsForAPICalls(NewGroupTaskActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }


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
                            showProgressDialog(NewGroupTaskActivity.this);
                            getValuesFromCityID(task.getPlace().getId());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText( NewGroupTaskActivity.this, "Cannot Retrieve Location", Toast.LENGTH_SHORT ).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

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
                mPlace.setId(place.getId());
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


                showProgressDialog(NewGroupTaskActivity.this);
                getValuesFromCityID(place.getId());

            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }


            places.release();
        }
    };


    private void getValuesFromCityID(String cityGoogleId) {
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);

            Call<CityResponseModel> call = apiService.getCityDatas(cityGoogleId);

            call.enqueue(new Callback<CityResponseModel>() {
                @Override
                public void onResponse(Call<CityResponseModel> call, Response<CityResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {

                        try {
                            Log.i("Drools", "" + new JSONObject(new Gson().toJson(response.body())));
                            cityId= response.body().getCity().getId();
                            isValidLocation = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else {
                        hideProgressDialog();
                        NetworkUtils.handleErrorsCasesForAPICalls(NewGroupTaskActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<CityResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(NewGroupTaskActivity.this, null, null);

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


            case R.id.input_date:
                pickUpFromCalendar();
                break;



            case R.id.save_task_button:
                saveTask();
                break;
        }
    }

    private void saveTask() {


        if ((isValidLocation) && (isValidDate) ){
            
            callAPIForSavingTask();

        }else{
            UiUtils.checkValidationForAutoCompleteTV(NewGroupTaskActivity.this, locationET, locationLayout, new ArrayList<String>());
            UiUtils.checkValidation(NewGroupTaskActivity.this, dateET, dateLayout, new ArrayList<String>());

        }
    }


    private void callAPIForSavingTask() {

        showProgressDialog(NewGroupTaskActivity.this);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("task[name]", nameET.getText().toString().trim());
        builder.addFormDataPart("task[city_id]", cityId);
        builder.addFormDataPart("task[scheduled_on]", dobValue); //dob
        if(notesET.getText().toString().trim().length()>0){
            builder.addFormDataPart("task[note]", notesET.getText().toString().trim()); //notes
        }



        RequestBody finalRequestBody = builder.build();
        ApiInterface apiService;
        if(currentLat !=null && currentLong !=null) {
            apiService = RetrofitClientWithHeadersForLocation.getClient().create(ApiInterface.class);
        }else{
            apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
        }
        Call<TaskResponseModel> call = apiService.createNewTask(finalRequestBody
        );
        call.enqueue(new Callback<TaskResponseModel>() {
            @Override
            public void onResponse(Call<TaskResponseModel> call, Response<TaskResponseModel> response) {
                hideProgressDialog();
                Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        //API Success is true


                        finish();
                        TaskDashboardActivity.getInstance().init();

                        Toast.makeText(NewGroupTaskActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("Drools onResponse", "" + response.body().getErrors());
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(new Gson().toJson(response.body()));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        NetworkUtils.handleErrorsCasesForAPICalls(NewGroupTaskActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Drools onResponse", "" + response.body().getErrors());
                    NetworkUtils.handleErrorsForAPICalls(NewGroupTaskActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<TaskResponseModel> call, Throwable t) {
                hideProgressDialog();
                NetworkUtils.handleErrorsForAPICalls(NewGroupTaskActivity.this, null, null);
            }
        });
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

                case R.id.input_location:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean cityStatus = UiUtils.checkValidationForAutoCompleteTV(NewGroupTaskActivity.this, locationET, locationLayout, itemPassed);

                    if (cityStatus == false) {
                        isValidLocation = false;
                        requestFocus(locationET);

                    } else {
                        isValidLocation = true;
                        locationLayout.setErrorEnabled(false);
                    }

                    break;


                case R.id.input_date:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean dateStatus = UiUtils.checkValidation(NewGroupTaskActivity.this, dateET, dateLayout, itemPassed);

                    if (dateStatus == false) {
                        isValidDate = false;
                        requestFocus(dateET);

                    } else {
                        isValidDate=true;
                        dateLayout.setErrorEnabled(false);
                    }

                    break;

                case R.id.input_notes:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean noteStatus = UiUtils.checkValidation(NewGroupTaskActivity.this, notesET, notesLayout, itemPassed);

                    if (noteStatus == false) {
                        notesET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        notesLayout.setErrorEnabled(false);

                    } else {
                        notesET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                        notesLayout.setErrorEnabled(false);
                    }

                    break;


                case R.id.input_name:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean nameStatus = UiUtils.checkValidation(NewGroupTaskActivity.this, nameET, nameLayout, itemPassed);

                    if (nameStatus == false) {
                        nameET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        nameLayout.setErrorEnabled(false);

                    } else {
                        nameET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                        nameLayout.setErrorEnabled(false);
                    }


                    break;


                case R.id.input_task:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean taskStatus = UiUtils.checkValidation(NewGroupTaskActivity.this, taskET, taskLayout, itemPassed);


                    break;


            }
        }

    }


    private void pickUpFromCalendar() {

        final Calendar c = Calendar.getInstance();

        int year , month ,day ;

        if(pickedDay==0){

             year = c.get(Calendar.YEAR);
             month = c.get(Calendar.MONTH);
             day = c.get(Calendar.DAY_OF_MONTH);
        }else{
            day = pickedDay;
            month = pickedMonth-1;
            year = pickedYear;
        }
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);



        //for calendar spinner
        picker = new DatePickerDialog(NewGroupTaskActivity.this,R.style.CustomDatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                        SimpleDateFormat sdf = new SimpleDateFormat("MMM");
                        c.set(year, monthOfYear, dayOfMonth);
                        String month = sdf.format(c.getTime());
                        Log.i("Suhail",month);


                        dobValue = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                        dateET.setText(UiUtils.parseDateToTaskFormat(dobValue));

                        pickedDay = dayOfMonth;
                        pickedMonth = monthOfYear+1;
                        pickedYear = year;

//                        dobValue = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
//                        dateET.setText(UiUtils.parseDateToTaskFormat(dobValue));
                        isValidDate = true;


                          }
                }, year, month, day);

        picker.show();
    }


    public void requestFocus(View view) {
        if (view.requestFocus())
            NewGroupTaskActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
