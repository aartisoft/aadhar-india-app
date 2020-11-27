package com.tailwebs.aadharindia.center;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.mukesh.OtpView;
import com.squareup.picasso.Picasso;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.center.adapters.CenterImagesHorizontalRecyclerViewAdapter;
import com.tailwebs.aadharindia.center.searchinmap.models.CityCenterResponseModel;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.applicant.AddCustomerDetailsActivity;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeadersForLocation;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.GpsTracker;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.ScalingUtilities;
import com.tailwebs.aadharindia.utils.UiUtils;
import com.tailwebs.aadharindia.utils.custom.multipleimageupload.models.ImageUploadModel;
import com.tailwebs.aadharindia.utils.custom.newmultipleimageupload.model.Image;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class CenterConfirmationActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.create_center_button)
    Button createCenterButton;

    @BindView(R.id.center_images_recyclerView)
    RecyclerView centerImagesRecyclerView;

    @BindView(R.id.profile_image)
    ImageView profileImage;

    @BindView(R.id.user_name)
    TextView userNameTV;

    @BindView(R.id.user_co)
    TextView userCoTV;

    @BindView(R.id.user_phone_no)
    TextView userPhoneTV;

    @BindView(R.id.user_aadhar_no)
    TextView userAadharNoTV;

    @BindView(R.id.user_dob)
    TextView userDobTV;

    @BindView(R.id.user_gender)
    TextView userGenderTV;

    @BindView(R.id.user_address)
    TextView userAddressTV;


    List<Image> centerImages=null;

    //password

    OtpView passwordOTPView;
    TextView passwordErrorTV;

    Button yesButton, noButton;

    private ProgressDialog mProgressDialog;
    Bundle bundle;

    String addressLine1,addressLine2,cityGooglePlaceId,pincode,profileImageURI,passcode;

    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();


    private Dialog dialog;

    private FirebaseAnalytics mFirebaseAnalytics;



    int desiredWidth = 800,desiredHeight = 800,desiredWidthSmall = 300,desiredHeightSmall=300;

    private GpsTracker gpsTracker;
    String currentLat=null,currentLong=null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_confirmation);
        ButterKnife.bind(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Center Confirmation", null);

        centerImages = GlobalValue.centerImagesForConfirmationNew;
        createCenterButton.setOnClickListener(this);

      Log.d("Aada","--"+centerImages.get(0).url);

        if(centerImages.size()==0){
            centerImagesRecyclerView.setVisibility(View.GONE);
        }else{
            centerImagesRecyclerView.setVisibility(View.VISIBLE);
            getCenterImages();
        }


        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Create New Center");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        bundle = getIntent().getExtras();


        //show current Lat and Long

        try {
            gpsTracker = new GpsTracker(CenterConfirmationActivity.this);
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
        showProfileConfirmationData(bundle);


    }

    private void getCenterImages() {

        for(int i=0;i<centerImages.size();i++){
            mImageUrls.add(String.valueOf(centerImages.get(i).url));
            mNames.add(String.valueOf(centerImages.get(i)));

        }

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d("Aadhaar India", "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        centerImagesRecyclerView.setLayoutManager(layoutManager);
        CenterImagesHorizontalRecyclerViewAdapter adapter = new CenterImagesHorizontalRecyclerViewAdapter(this, mNames, mImageUrls,"fromConfirmation");
        centerImagesRecyclerView.setAdapter(adapter);
    }

    private void showProfileConfirmationData(Bundle bundle) {

        if (bundle.getString("username") != null) {
            userNameTV.setText(bundle.getString("username"));
        }

        if (bundle.getString("user_co_name") != null) {
            userCoTV.setText("C.o. "+bundle.getString("user_co_name"));
        }

        if (bundle.getString("phone") != null) {
            userPhoneTV.setText(bundle.getString("phone"));
        }

        if (bundle.getString("aadhar_no") != null) {
            userAadharNoTV.setText(bundle.getString("aadhar_no"));
        }

        if (bundle.getString("dob") != null) {
            userDobTV.setText(bundle.getString("dob"));
        }


        if (bundle.getString("gender") != null) {
            userGenderTV.setText(bundle.getString("gender"));
        }

        if (bundle.getString("city") != null) {
           cityGooglePlaceId = (bundle.getString("city_google_place_id"));
        }


        if (bundle.getString("address_line_1") != null) {
            addressLine1 = bundle.getString("address_line_1");
            addressLine2 = bundle.getString("address_line_2");
            pincode = bundle.getString("pin_code");
            userAddressTV.setText(bundle.getString("address_line_1")+", "+bundle.getString("address_line_2")
            +" "+bundle.getString("city")+", "+bundle.getString("district")+", "+bundle.getString("state")
            +", "+bundle.getString("pin_code"));
        }

        if (bundle.getString("profile_image_path") != null) {

            profileImageURI =bundle.getString("profile_image_path");
            profileImage.setImageURI(Uri.parse(bundle.getString("profile_image")));

        } else {
            Picasso
                    .with(CenterConfirmationActivity.this)
                    .cancelRequest(profileImage);
            profileImage.setImageDrawable(getResources().getDrawable(R.drawable.userimg_placeholder));
        }
    }


    private void addNewCenter() {

//        try {
//
//            RequestBody aadharNo = RequestBody.create(MediaType.parse("text/plain"), userAadharNoTV.getText().toString());
//            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), userNameTV.getText().toString());
//            RequestBody dob = RequestBody.create(MediaType.parse("text/plain"), userDobTV.getText().toString());
//            RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), userGenderTV.getText().toString());
//            RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), userPhoneTV.getText().toString());
//            RequestBody co = RequestBody.create(MediaType.parse("text/plain"), userCoTV.getText().toString());
//            RequestBody address_line1 = RequestBody.create(MediaType.parse("text/plain"), addressLine1);
//            RequestBody address_line2 = RequestBody.create(MediaType.parse("text/plain"), addressLine2);
//            RequestBody city_google_id = RequestBody.create(MediaType.parse("text/plain"), cityGooglePlaceId);
//            RequestBody pinCode = RequestBody.create(MediaType.parse("text/plain"), pincode);
//            RequestBody pass_code = RequestBody.create(MediaType.parse("text/plain"), passcode);
//
//            Log.d("Drools onResponse", "imgId" );
//            //For Image
//            File file = null;
//            MultipartBody.Part fileToUpload = null;
//            if (profileImageURI != null) {
//                file = new File(profileImageURI);
//                Log.d("Drools onResponse", "file"+file );
//                RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                Log.d("Drools onResponse", "mFile"+mFile );
//                fileToUpload = MultipartBody.Part.createFormData("city_center[contact_person_attributes][profile_image]", file.getName(), mFile);
//            }
//
//
//            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
//            Call<CityCenterResponseModel> call = apiService.addCityCenter(
//                        aadharNo,
//                        name,
//                        dob,
//                        gender,
//                        phone,
//                        co,
//                        address_line1,
//                        address_line2,
//                        city_google_id,
//                        pinCode,
//                        pass_code,
//                        fileToUpload
//                );
//
//
//
//            call.enqueue(new Callback<CityCenterResponseModel>() {
//                @Override
//                public void onResponse(Call<CityCenterResponseModel> call, Response<CityCenterResponseModel> response) {
//                    hideProgressDialog();
//                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
//                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
//                    if (response.isSuccessful()) {
//                        if (response.body().getSuccess()) {
//                            //API Success is true
//                            Toast.makeText(CenterConfirmationActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
//                            goToCenterShowPage(response.body());
//
//                        } else {
//                            Log.d("Drools onResponse2", "" + response.body().getErrors());
//                            NetworkUtils.handleErrorsCasesForAPICalls(CenterConfirmationActivity.this, response.code(), response.body().getErrors());
//                        }
//                    } else {
//                        Log.d("Drools onResponse3", "" + response.body().getErrors());
//                        NetworkUtils.handleErrorsForAPICalls(CenterConfirmationActivity.this, null, null);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<CityCenterResponseModel> call, Throwable t) {
//                    hideProgressDialog();
//                    NetworkUtils.handleErrorsForAPICalls(CenterConfirmationActivity.this, null, null);
//                }
//            });
//        } catch (NullPointerException e) {
//            System.out.print("Caught the NullPointerException");
//        }


        Log.d("Aadhaar India", "1--"+ userAadharNoTV.getText().toString().trim());
        Log.d("Aadhaar India", "2--"+ userNameTV.getText().toString().trim());
        Log.d("Aadhaar India", "3--"+ userDobTV.getText().toString().trim());
        Log.d("Aadhaar India", "4--"+ userGenderTV.getText().toString().trim());
        Log.d("Aadhaar India", "5--"+ userPhoneTV.getText().toString().trim());
        Log.d("Aadhaar India", "6--"+ userCoTV.getText().toString().trim());
        Log.d("Aadhaar India", "7--"+addressLine1);
        Log.d("Aadhaar India", "8--"+addressLine2);
        Log.d("Aadhaar India", "9--"+cityGooglePlaceId);
        Log.d("Aadhaar India", "10--"+pincode);
        Log.d("Aadhaar India", "11--"+profileImageURI);
        Log.d("Aadhaar India", "12--"+passcode);




        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("city_center[aadhar_number]", userAadharNoTV.getText().toString().trim());
        builder.addFormDataPart("city_center[contact_person_attributes][name]", userNameTV.getText().toString().trim());
        builder.addFormDataPart("city_center[contact_person_attributes][dob]", userDobTV.getText().toString().trim()); //dob
        builder.addFormDataPart("city_center[contact_person_attributes][gender]", userGenderTV.getText().toString().trim()); //gender

        builder.addFormDataPart("city_center[contact_person_attributes][phone_number]", userPhoneTV.getText().toString().trim());
        builder.addFormDataPart("city_center[contact_person_attributes][aadhar_co]", userCoTV.getText().toString().trim());

        builder.addFormDataPart("city_center[center_address_attributes][line1]", addressLine1);
        builder.addFormDataPart("city_center[center_address_attributes][line2]", addressLine2);
        builder.addFormDataPart("city_center[center_address_attributes][city_google_place_id]", cityGooglePlaceId);//google place id
        builder.addFormDataPart("city_center[center_address_attributes][pincode]", pincode);
        builder.addFormDataPart("user_passcode",passcode );

        Log.d("Aadhaar India", "pro--"+profileImageURI);

        if (profileImageURI != null) {
//        Create a file object using  file path

            File profile_file = new File(decodeFile(profileImageURI,desiredWidth,desiredHeight));
            // Create a request body with file and image media type
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), profile_file);
            // Create MultipartBody.Part using file request-body,file name and part name
            builder.addFormDataPart("city_center[contact_person_attributes][profile_image]", profile_file.getName(), fileReqBody);
        }


        // Multiple Images


        if (centerImages.size() > 0) {
            for (int i = 0; i < centerImages.size(); i++) {

                Log.d("Aadhaar India", ""+centerImages.get(i).url);
                File file = new File(decodeFile(centerImages.get(i).url,desiredWidth,desiredHeight));
                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                builder.addFormDataPart("city_center[city_center_images_attributes]["+ i +"][image]", file.getName(), mFile);
            }
        }


//        for (int i = 0, size = centerImages.size(); i < size; i++) {
//             File file1 = new File(centerImages.get(i).getUri().toString());
//            RequestBody requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
//            builder.addFormDataPart("city_center[city_center_images_attributes]" + "[" + i + "][image]", centerImages.get(i).getName(), requestBody1);
//        }


        RequestBody finalRequestBody = builder.build();
        ApiInterface apiService;
        if(currentLat !=null && currentLong !=null) {
             apiService = RetrofitClientWithHeadersForLocation.getClient().create(ApiInterface.class);
        }else{
            apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
        }
        Call<CityCenterResponseModel> call = apiService.createCenter(finalRequestBody
        );
        call.enqueue(new Callback<CityCenterResponseModel>() {
            @Override
            public void onResponse(Call<CityCenterResponseModel> call, Response<CityCenterResponseModel> response) {
                hideProgressDialog();
                Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        //API Success is true
                        dialog.dismiss();

                        Bundle params = new Bundle();
                        params.putString("center_id", response.body().getCenterModels().getCenter_id());
                        params.putString("center_city", response.body().getCenterModels().getCenterAddressModel().getCity_name()+" - "+
                                response.body().getCenterModels().getCenterAddressModel().getGoogle_place_id());
                        params.putString("center_district", response.body().getCenterModels().getCenterAddressModel().getDistrict_name());
                        params.putString("center_state", response.body().getCenterModels().getCenterAddressModel().getState_name());
                        params.putString("status","center_creation_completed");
                        mFirebaseAnalytics.logEvent("center", params);


                        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                        File file = new File(path);

                        try {
                            FileUtils.deleteDirectory(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }




                        Toast.makeText(CenterConfirmationActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                        goToCenterShowPage(response.body());
                    } else {
                        Log.d("Drools onResponse", "" + response.body().getErrors());
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(new Gson().toJson(response.body()));


                            Log.d("Drools onResponse type", "" + jsonObject.getString("type"));
                            Log.d("Drools onResponse json", "" + jsonObject);


                            if(jsonObject.getString("type").equalsIgnoreCase("invalid_passcode")){
                                passwordOTPView.setLineColor(getResources().getColor(R.color.errorColor));
                                passwordErrorTV.setVisibility(View.VISIBLE);
                                passwordErrorTV.setText("Invalid Passcode");
                                yesButton.setEnabled(true);
                                yesButton.setTextAppearance(CenterConfirmationActivity.this,R.style.MyBluePopUpButton);
                                Toast.makeText(CenterConfirmationActivity.this, "Invalid passcode", Toast.LENGTH_SHORT).show();


                            }


                            if(jsonObject.has("aadhar_number")) {

                                dialog.dismiss();
                                JSONObject  groupJSON = new JSONObject(new Gson().toJson(response.body().getErrors()));
                                Log.d("aadh onResponse json", "" + groupJSON.getString("aadhar_number"));

                                String val = groupJSON.getString("group").substring(1);
                                String final_val = val.substring(0, val.length() - 1);

                                UiUtils.showAlertDialogWithOKButton(CenterConfirmationActivity.this, final_val, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });


                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        NetworkUtils.handleErrorsCasesForAPICalls(CenterConfirmationActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Drools onResponse", "" + response.body().getErrors());
                    NetworkUtils.handleErrorsForAPICalls(CenterConfirmationActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<CityCenterResponseModel> call, Throwable t) {
                hideProgressDialog();
                NetworkUtils.handleErrorsForAPICalls(CenterConfirmationActivity.this, null, null);
            }
        });
    }

    private void goToCenterShowPage(CityCenterResponseModel body) {


        GlobalValue.selectedCenter = true;
        Intent intent = new Intent(CenterConfirmationActivity.this,CenterShowActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id",body.getCenterModels().getId());
        bundle.putString("center_id",body.getCenterModels().getCenter_id());
        bundle.putString("username", body.getCenterModels().getContactPersonModel().getName());
        bundle.putString("user_co_name", body.getCenterModels().getContactPersonModel().getAadhar_co());
        bundle.putString("phone", body.getCenterModels().getContactPersonModel().getPhone_number());
        bundle.putString("aadhar_no", body.getCenterModels().getContactPersonModel().getAadhar_number());
        bundle.putString("dob", body.getCenterModels().getContactPersonModel().getDob());
        bundle.putString("gender", body.getCenterModels().getContactPersonModel().getGender());
        bundle.putString("center_address", body.getCenterModels().getCenterAddressModel().getFull_address());
        bundle.putString("profile_image", body.getCenterModels().getContactPersonModel().getProfileImages().getOriginal());
        GlobalValue.ciTyCenterenterImages = body.getCenterModels().getCityCenterImages();
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }



    private String decodeFile(String path,int DESIREDWIDTH, int DESIREDHEIGHT) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;



        Log.d("Aadhaar India im", "path--"+path);

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);

            Log.d("Aadhaar India im", "uw--"+unscaledBitmap.getWidth());
            Log.d("Aadhaar India im", "uh--"+unscaledBitmap.getHeight());
            Log.d("Aadhaar India im", "dw--"+DESIREDWIDTH);
            Log.d("Aadhaar India im", "dh--"+DESIREDHEIGHT);

//            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
//                Log.d("Aadhaar India im if", "path--"+path);
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
//            } else {
//                Log.d("Aadhaar India im else", "path--"+path);
//                unscaledBitmap.recycle();
//                return path;
//            }

            Log.d("Aadhaar India im2", "path--"+path);

            // Store to tmp file

//            String extr = Environment.getExternalStorageDirectory().toString();
//            File mFolder = new File(extr + "/AadharFolder");
            File mFolder = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "AadharFolder");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = "tmp_"+System.currentTimeMillis();

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 40, fos);
                Log.d("Aadhaar India", "PPP--"+fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

    }

    private void showPasswordDialog(){
        // title, custom view, actions dialog

        passcode= null;
        View view = getLayoutInflater().inflate(R.layout.custom_password_dialog, null);
        passwordOTPView=(OtpView) view.findViewById(R.id.password_otp_view);
        passwordErrorTV =(TextView)view.findViewById(R.id.password_error_tv);
        RelativeLayout buttonLayout = (RelativeLayout)view.findViewById(R.id.button_layout);
        buttonLayout.setVisibility(View.VISIBLE);
        yesButton =(Button)view.findViewById(R.id.yes_button);
        noButton =(Button)view.findViewById(R.id.no_button);
        yesButton.setEnabled(true);
        yesButton.setTextAppearance(CenterConfirmationActivity.this,R.style.MyBluePopUpButton);
        yesButton.setText("CONFIRM");
        noButton.setText("CANCEL");
        passwordOTPView.addTextChangedListener(new InputLayoutTextWatcher(passwordOTPView));
        AlertDialog.Builder builder = new AlertDialog.Builder(CenterConfirmationActivity.this);
        builder.setCancelable(false)
                .setTitle("Enter Passcode")
                .setPositiveButton("", null)
                .setNeutralButton("", null)
                .setView(view);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        dialog = alertDialog;
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(CenterConfirmationActivity.this);
                yesButton.setEnabled(false);
                yesButton.setTextAppearance(CenterConfirmationActivity.this,R.style.MyDisabledPopUpButton);
                passcode = passwordOTPView.getText().toString();
                if(UiUtils.checkValidationForOTP(CenterConfirmationActivity.this, passwordOTPView,passwordErrorTV)){
                    if (NetworkUtils.haveNetworkConnection(CenterConfirmationActivity.this)) {
//                        showProgressDialog(CenterConfirmationActivity.this);
                        addNewCenter();

                    } else {
                        hideProgressDialog();
                        UiUtils.showAlertDialogWithOKButton(CenterConfirmationActivity.this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                yesButton.setEnabled(true);
                                yesButton.setTextAppearance(CenterConfirmationActivity.this,R.style.MyBluePopUpButton);
                            }
                        });
                    }

                }else{
                    hideProgressDialog();
                }
            }
        });

//        yesButton.setOnClickListener(new CustomListener(alertDialog));
        noButton.setOnClickListener(new CancelCustomListener(alertDialog));
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

                case R.id.password_otp_view:
                    itemPassed.clear();
                    itemPassed.add("otp");
                    boolean passwordStatus = UiUtils.checkValidationForOTP(CenterConfirmationActivity.this, passwordOTPView,passwordErrorTV);

                    if (passwordStatus == false) {

                        requestFocus(passwordOTPView);
                    } else {

                        passwordOTPView.setLineColor(getResources().getColor(R.color.primaryColor));

                    }
                    break;

            }
        }

    }


    public void requestFocus(View view) {
        if (view.requestFocus())
            CenterConfirmationActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.create_center_button:
                showPasswordDialog();
                break;

            case R.id.back_button:
                onBackPressed();
                break;
        }

    }


    private class CustomListener implements View.OnClickListener {

        public CustomListener(AlertDialog alertDialog) {
            dialog = alertDialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here
           passcode = passwordOTPView.getText().toString();
//            Toast.makeText(CenterConfirmationActivity.this, ""+mValue, Toast.LENGTH_SHORT).show();
            if(UiUtils.checkValidationForOTP(CenterConfirmationActivity.this, passwordOTPView,passwordErrorTV)){
                if (NetworkUtils.haveNetworkConnection(CenterConfirmationActivity.this)) {
                    showProgressDialog(CenterConfirmationActivity.this);
                    yesButton.setEnabled(false);
                    yesButton.setTextAppearance(CenterConfirmationActivity.this,R.style.MyDisabledFormHeadingTextView);
                    addNewCenter();

                } else {
                    UiUtils.showAlertDialogWithOKButton(CenterConfirmationActivity.this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                }

            }
        }
    }


    private class CancelCustomListener implements View.OnClickListener {

        public CancelCustomListener(AlertDialog alertDialog) {
            dialog = alertDialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here
         dialog.dismiss();
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
}
