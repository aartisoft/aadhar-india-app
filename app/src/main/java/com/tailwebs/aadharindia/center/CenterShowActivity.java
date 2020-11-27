package com.tailwebs.aadharindia.center;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.center.adapters.CenterImagesHorizontalRecyclerViewAdapter;
import com.tailwebs.aadharindia.center.searchinmap.models.CityCenterImages;
import com.tailwebs.aadharindia.center.searchinmap.models.GroupResponseModel;
import com.tailwebs.aadharindia.home.tasks.creategroup.CreateGroupTaskDetailsActivity;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CenterShowActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "HorizontalImageScroll";


    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    @BindView(R.id.create_group_button)
    Button createGroupButton;

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

    @BindView(R.id.call_button)
    ImageButton callButton;

    @BindView(R.id.user_aadhar_no)
    TextView userAadharNoTV;

    @BindView(R.id.user_dob)
    TextView userDobTV;

    @BindView(R.id.user_gender)
    TextView userGenderTV;

    @BindView(R.id.user_address)
    TextView centerAddressTV;


    @BindView(R.id.member_in_center_tv)
    TextView membersInCenter;


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    String centerId=null,id=null,phoneNumber=null;

    public  ArrayList<CityCenterImages> ciTyCenterenterImages =null;

    private ProgressDialog mProgressDialog;
    Bundle bundle;

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_show);
        ButterKnife.bind(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Center Show", null);

        if (checkPermission(Manifest.permission.CALL_PHONE)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
        }

        bundle = getIntent().getExtras();

        showProfileData(bundle);


        createGroupButton.setOnClickListener(this);
        callButton.setOnClickListener(this);
        membersInCenter.setOnClickListener(this);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Create New Center");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

    }


    public void call(String phoneNumber){
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            String dial = "tel:" + phoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        } else {
            Toast.makeText(CenterShowActivity.this, "Permission Call Phone denied", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    Toast.makeText(this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    private void showProfileData(Bundle bundle) {



        id = bundle.getString("id");
        centerId = bundle.getString("center_id");

        GlobalValue.city_id = id;

        if (bundle.getString("username") != null) {
            userNameTV.setText(bundle.getString("username"));
        }

        if (bundle.getString("user_co_name") != null) {
            userCoTV.setText(bundle.getString("user_co_name"));
        }

        if (bundle.getString("phone") != null) {
            userPhoneTV.setText(bundle.getString("phone"));
            phoneNumber = bundle.getString("phone");
        }

        if (bundle.getString("aadhar_no") != null) {
            userAadharNoTV.setText(bundle.getString("aadhar_no"));
        }

        if (bundle.getString("dob") != null) {
            userDobTV.setText(UiUtils.parseDateToAadhaarFormat(bundle.getString("dob")));
        }


        if (bundle.getString("gender") != null) {
            userGenderTV.setText(bundle.getString("gender"));
        }


        if (bundle.getString("center_address") != null) {
            centerAddressTV.setText(bundle.getString("center_address"));
        }

        if (bundle.getString("profile_image") != null) {
            Picasso.with(CenterShowActivity.this)
                    .load(bundle.getString("profile_image"))
                    .placeholder(R.drawable.userimg_placeholder)
                    .into(profileImage);
        } else {
            Picasso
                    .with(CenterShowActivity.this)
                    .cancelRequest(profileImage);
            profileImage.setImageDrawable(getResources().getDrawable(R.drawable.userimg_placeholder));
        }




        ciTyCenterenterImages = GlobalValue.ciTyCenterenterImages ;

        if(ciTyCenterenterImages!=null){
            if(ciTyCenterenterImages.size()>0){
                getImages();
                centerImagesRecyclerView.setVisibility(View.VISIBLE);
            }else{
                centerImagesRecyclerView.setVisibility(View.GONE);
            }
        }






    }

    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        for(int i=0;i<ciTyCenterenterImages.size();i++){
            mImageUrls.add(ciTyCenterenterImages.get(i).getCityCenterImage().getOriginal());
            mNames.add(String.valueOf(i));
        }
        initRecyclerView();

    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        centerImagesRecyclerView.setLayoutManager(layoutManager);
        CenterImagesHorizontalRecyclerViewAdapter adapter = new CenterImagesHorizontalRecyclerViewAdapter(this, mNames, mImageUrls,"fromCenterShow");
        centerImagesRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.create_group_button:
                    goToCenterStartPageAfterSelection();
                break;


            case R.id.call_button:
                call(phoneNumber);
                break;


            case R.id.member_in_center_tv:
                Intent intent = new Intent(CenterShowActivity.this,ListOfGroupsInCityCenterActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("centerId",centerId);
                intent.putExtra("centerAddress",centerAddressTV.getText().toString());
                intent.putExtra("centerPerson",centerId+"/"+userNameTV.getText().toString());
                startActivity(intent);
                break;
        }

    }

//    private void createGroupAPICall() {
//
//
//        showProgressDialog(CenterShowActivity.this);
//        try {
//            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
//
//            Log.d("Shahana","Center id"+id);
//
//
//
//            Call<GroupResponseModel> call = apiService.createGroup(id);
//
//            call.enqueue(new Callback<GroupResponseModel>() {
//                @Override
//                public void onResponse(Call<GroupResponseModel> call, Response<GroupResponseModel> response) {
//                    hideProgressDialog();
//
//                    if (response.isSuccessful()) {
//
//                        if (response.body().getSuccess()) {
//
//
//                            Bundle params = new Bundle();
//                            params.putString("center_id", response.body().getGroupModel().getCity_center_id());
//                            params.putString("group_id", response.body().getGroupModel().getGroup_id());
//                            mFirebaseAnalytics.logEvent("group", params);
//                            GlobalValue.cityCenterId = response.body().getGroupModel().getCity_center_id();
//                            GlobalValue.groupId = response.body().getGroupModel().getId();
//                            Log.d("Shahana","Group id"+response.body().getGroupModel().getId());
//
//                            Toast.makeText(CenterShowActivity.this, ""+ GlobalValue.cityCenterId, Toast.LENGTH_SHORT).show();
//                            goToCenterStartPageAfterSelection();
//                        } else {
//                            NetworkUtils.handleErrorsForAPICalls(CenterShowActivity.this, response.body().getErrors(), response.body().getNotice());
//                        }
//
//                    } else {
//                        NetworkUtils.handleErrorsForAPICalls(CenterShowActivity.this, response.body().getErrors(), response.body().getNotice());
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<GroupResponseModel> call, Throwable t) {
//
//                    hideProgressDialog();
//                    NetworkUtils.handleErrorsForAPICalls(CenterShowActivity.this, null, null);
//
//                }
//            });
//        } catch (NullPointerException e) {
//            System.out.print("Caught the NullPointerException");
//        }
//    }

    private void goToCenterStartPageAfterSelection() {

        GlobalValue.selectedCenter = true;
        GlobalValue.chooseCenterFromList = true;
        GlobalValue.isGroupPresent = false;
        GlobalValue.centerId = centerId;
        GlobalValue.centerAddress = centerAddressTV.getText().toString();
        GlobalValue.centerPerson = centerId+"/"+userNameTV.getText().toString();
        Intent intent = new Intent(CenterShowActivity.this,CreateGroupTaskDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("task_id",  GlobalValue.taskId );
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
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
