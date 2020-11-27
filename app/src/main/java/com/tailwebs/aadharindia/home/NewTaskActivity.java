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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewTaskActivity extends BaseActivity implements View.OnClickListener , GoogleApiClient.OnConnectionFailedListener{


    private static final String TAG = "NewTaskActivity";


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


    int pickedDay = 0;
    int pickedMonth = 0;
    int pickedYear = 0;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;




    //for city

    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlace;

    PlacesClient placesClient;

    AutoCompleteAdapter adapter;


    private boolean isValidLocation = false, isValidNotes = false, isValidDate = false;

    private GpsTracker gpsTracker;
    String currentLat=null,currentLong=null;
    private FirebaseAnalytics mFirebaseAnalytics;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        ButterKnife.bind(this);


        String apiKey = getString(R.string.google_maps_key);
        if(apiKey.isEmpty()){
            Toast.makeText( gpsTracker, "Google Api key is missing.", Toast.LENGTH_SHORT ).show();
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
        headingTV.setText("New Task");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Create Task", null);

//        getLocationPermission();


        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (NetworkUtils.haveNetworkConnection(this)) {

            getNewCustomTask();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }



        //show current Lat and Long

        try {
            gpsTracker = new GpsTracker(NewTaskActivity.this);
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


        dateET.setOnClickListener(this);
        saveTaskButton.setOnClickListener(this);


        locationET.addTextChangedListener(new InputLayoutTextWatcher(locationET));
        dateET.addTextChangedListener(new InputLayoutTextWatcher(dateET));
        notesET.addTextChangedListener(new InputLayoutTextWatcher(notesET));
        nameET.addTextChangedListener(new InputLayoutTextWatcher(nameET));
        taskET.addTextChangedListener(new InputLayoutTextWatcher(taskET));


        //for city auto complete

//        mGoogleApiClient = new GoogleApiClient
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
//
//        locationET.setOnItemClickListener(mAutocompleteClickListener);
//        locationET.setAdapter(mPlaceAutocompleteAdapter);

    }

    private void initAutoCompleteTextView() {


        locationET.setThreshold(1);
        locationET.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(this, placesClient);
        locationET.setAdapter(adapter);
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
                            showProgressDialog(NewTaskActivity.this);
                            getValuesFromCityID(task.getPlace().getId());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText( gpsTracker, "mdmdmd", Toast.LENGTH_SHORT ).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    private void getNewCustomTask() {

        showProgressDialog(NewTaskActivity.this);
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<TaskResponseModel> call = apiService.getNewCustomTask();
            call.enqueue(new Callback<TaskResponseModel>() {
                @Override
                public void onResponse(Call<TaskResponseModel> call, final Response<TaskResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            taskET.setText(response.body().getTaskModel().getName());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(NewTaskActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(NewTaskActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<TaskResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(NewTaskActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

//    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            hideSoftKeyboard();
//
//            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
//            final String placeId = item.getPlaceId();
//
//            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
//                    .getPlaceById(mGoogleApiClient, placeId);
//            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
//
//
//        }
//    };

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


                showProgressDialog(NewTaskActivity.this);
                getValuesFromCityID(place.getId());

            }catch (NullPointerException e){
                Log.e(TAG, "onResult: NullPointerException: " + e.getMessage() );
            }


            places.release();
        }
    };


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
                        NetworkUtils.handleErrorsCasesForAPICalls(NewTaskActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<CityResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(NewTaskActivity.this, null, null);

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


        if ((isValidLocation) && (isValidDate)){

            callAPIForSavingTask();

        }else{
            UiUtils.checkValidationForAutoCompleteTV(NewTaskActivity.this, locationET, locationLayout, new ArrayList<String>());
            UiUtils.checkValidation(NewTaskActivity.this, dateET, dateLayout, new ArrayList<String>());


        }
    }

    private void callAPIForSavingTask() {

        saveTaskButton.setEnabled(false);
        saveTaskButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(NewTaskActivity.this);

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
        Call<TaskResponseModel> call = apiService.createNewCustomTask(finalRequestBody
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
                        saveTaskButton.setEnabled(true);
                        saveTaskButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));


                        Bundle params = new Bundle();
                        params.putString("task_id", response.body().getTaskModel().getTask_number());
                        params.putString("status","custom_task_created");
                        params.putString("task_city",response.body().getTaskModel().getCity().getLong_name()+" - "+
                                response.body().getTaskModel().getCity().getGoogle_place_id());
                        mFirebaseAnalytics.logEvent("custom_task", params);

                        finish();
                        TaskDashboardActivity.getInstance().init();

                        Toast.makeText(NewTaskActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("Drools onResponse", "" + response.body().getErrors());
                        saveTaskButton.setEnabled(true);
                        saveTaskButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(new Gson().toJson(response.body()));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        NetworkUtils.handleErrorsCasesForAPICalls(NewTaskActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Drools onResponse", "" + response.body().getErrors());
                    saveTaskButton.setEnabled(true);
                    saveTaskButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(NewTaskActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<TaskResponseModel> call, Throwable t) {
                hideProgressDialog();
                saveTaskButton.setEnabled(true);
                saveTaskButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                NetworkUtils.handleErrorsForAPICalls(NewTaskActivity.this, null, null);
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
                    boolean cityStatus = UiUtils.checkValidationForAutoCompleteTV(NewTaskActivity.this, locationET, locationLayout, itemPassed);

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
                    boolean dateStatus = UiUtils.checkValidation(NewTaskActivity.this, dateET, dateLayout, itemPassed);

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
                    boolean noteStatus = UiUtils.checkValidation(NewTaskActivity.this, notesET, notesLayout, itemPassed);

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
                    boolean nameStatus = UiUtils.checkValidation(NewTaskActivity.this, nameET, nameLayout, itemPassed);

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
                    boolean taskStatus = UiUtils.checkValidation(NewTaskActivity.this, taskET, taskLayout, itemPassed);


                    break;


            }
        }

    }


    private void pickUpFromCalendar() {

//        int day,month,year;
//
//
//        day = 01;
//        month = 00;
//        year = 1970;


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



        //for calendar spinner
//        picker = new DatePickerDialog(NewTaskActivity.this,R.style.CustomDatePickerDialog,
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//                        dobValue = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
//                        dateET.setText(UiUtils.parseDateToTaskFormat(dobValue));
//                        isValidDate = true;
//
//
//                    }
//                }, year, month, day);
//
//        picker.show();


        picker = new DatePickerDialog(NewTaskActivity.this,R.style.CustomDatePickerDialog,
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
            NewTaskActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
