package com.tailwebs.aadharindia.center;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.center.searchinmap.SearchVillageActivity;
import com.tailwebs.aadharindia.housevisit.HouseVisitMemberListingActivity;
import com.tailwebs.aadharindia.member.AddNewMemberScanActivity;
import com.tailwebs.aadharindia.member.GroupMemberListingActivity;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateCenterStartActivity extends BaseActivity implements View.OnClickListener {


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    @BindView(R.id.select_center_tv)
    TextView selectCenterTv;

    @BindView(R.id.center_person_tv)
    TextView centerPersonTV;


    @BindView(R.id.center_person_address_tv)
    TextView centerAddressTV;

    @BindView(R.id.add_members_tv)
    TextView addMembersTV;


    @BindView(R.id.select_center_layout)
    LinearLayout selectCenterLayout;

    @BindView(R.id.add_members_layout)
    LinearLayout addMembersLayout;

    @BindView(R.id.house_visit_layout)
    LinearLayout houseVisitLayout;

    @BindView(R.id.house_visit_tv)
    TextView houseVisitTv;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int MY_CAMERA_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_center_start);
        ButterKnife.bind(this);


        //action bar
        backButton.setOnClickListener(this);

        headingTV.setText("Create New Group");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);


        selectCenterLayout.setOnClickListener(this);
        addMembersLayout.setOnClickListener(this);
        houseVisitLayout.setOnClickListener(this);



        UiUtils.setProcessActivated(CreateCenterStartActivity.this,selectCenterTv);
        centerPersonTV.setVisibility(View.GONE);
        centerAddressTV.setVisibility(View.GONE);

        if(!GlobalValue.selectedCenter){
            addMembersTV.setEnabled(false);
            addMembersLayout.setEnabled(false);
            addMembersTV.setTextAppearance(getApplicationContext(),R.style.MyDisabledFormHeadingTextView);


            houseVisitTv.setEnabled(false);
            houseVisitLayout.setEnabled(false);
            houseVisitTv.setTextAppearance(getApplicationContext(),R.style.MyDisabledFormHeadingTextView);
        }else{
            selectCenterLayout.setEnabled(false);
            selectCenterTv.setEnabled(false);
            addMembersTV.setEnabled(true);
            addMembersLayout.setEnabled(true);
            addMembersTV.setTextAppearance(getApplicationContext(),R.style.MyFormHeadingTextView);
            centerPersonTV.setVisibility(View.VISIBLE);
            centerAddressTV.setVisibility(View.VISIBLE);
            centerPersonTV.setText(GlobalValue.centerPerson);
            centerAddressTV.setText(GlobalValue.centerAddress);
            UiUtils.setProcessCompleted(CreateCenterStartActivity.this,selectCenterTv);


            houseVisitTv.setEnabled(true);
            houseVisitLayout.setEnabled(true);
            houseVisitTv.setTextAppearance(getApplicationContext(),R.style.MyFormHeadingTextView);
        }


        getLocationPermission();
        isStoragePermissionGranted();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.select_center_layout:
                startActivity(new Intent(CreateCenterStartActivity.this,SearchVillageActivity.class));
                break;


            case R.id.add_members_layout:
                Log.i("Group ID",""+GlobalValue.groupId);
                startActivity(new Intent(CreateCenterStartActivity.this,GroupMemberListingActivity.class));
                break;


            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.house_visit_layout:
                startActivity(new Intent(CreateCenterStartActivity.this,HouseVisitMemberListingActivity.class));
                break;
        }

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



    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Aadhar","Permission is granted");
                return true;
            } else {

                Log.v("Aadhar","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Aadhar","Permission is granted");
            return true;
        }
    }
}
