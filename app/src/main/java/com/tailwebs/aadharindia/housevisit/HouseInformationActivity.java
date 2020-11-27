package com.tailwebs.aadharindia.housevisit;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.applicant.ApplicantDetailActivity;
import com.tailwebs.aadharindia.member.expenditure.OutsideBorrowingActivity;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HouseInformationActivity extends BaseActivity implements View.OnClickListener {


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;



    //user

    @BindView(R.id.user_name)
    TextView userNameTv;

    @BindView(R.id.profile_image)
    ImageView profileImageView;

    @BindView(R.id.user_status)
    TextView userStatusTv;

    @BindView(R.id.user_phone_no)
    TextView userPhoneTv;


    @BindView(R.id.member_yes_tv)
    TextView memberYesTv;


    @BindView(R.id.member_no_tv)
    TextView memberNoTv;

    @BindView(R.id.continue_button)
    Button continueButton;


    //
    @BindView(R.id.call_button)
    ImageButton callButton;

    //choose value from intent;
    String loanTakerID=null,buttonValue=null,phoneNumber=null;

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    Boolean isMemberChosed=null;


    TextView resheduleTv;
    TextView removeMemberTv;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_information);
        ButterKnife.bind(this);


        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("House Information");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "House Information", null);

        init();

        setValuesFromIntent(getIntent().getExtras());
    }

    private void setValuesFromIntent(Bundle intent) {

        userNameTv.setText(intent.getString("name"));
        userPhoneTv.setText(intent.getString("phone"));
        userStatusTv.setText(intent.getString("status"));
    }

    private void init() {

        loanTakerID = GlobalValue.loanTakerId;


        if (checkPermission(Manifest.permission.CALL_PHONE)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
        }

      
        callButton.setOnClickListener(this);
        memberYesTv.setOnClickListener(this);
        memberNoTv.setOnClickListener(this);
        continueButton.setOnClickListener(this);
      

    }

    public void call(String phoneNumber){
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            String dial = "tel:" + phoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        } else {
            Toast.makeText(HouseInformationActivity.this, "Permission Call Phone denied", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.call_button:
                call(phoneNumber);
                break;


            case R.id.member_yes_tv:
                chooseValue("Yes");
                break;


            case R.id.member_no_tv:
                chooseValue("No");
                showPopup();
                break;


            case R.id.continue_button:
                if(isMemberChosed!=null){
                    startActivity(new Intent(HouseInformationActivity.this,HouseDetailsActivity.class));
                }else {
                    Toast.makeText(this, "Please fill the field", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void showPopup() {
//        View view = OutsideBorrowingActivity.getInstance().getLayoutInflater().inflate(R.layout.custom_house_call_remove_dialog, null);
//        resheduleTv=(TextView) view.findViewById(R.id.reschedule_house_visit_tv);
//        removeMemberTv=(TextView) view.findViewById(R.id.remove_member_tv);
//
//
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false)
//                .setPositiveButton("", null)
//                .setNeutralButton("", null)
//                .setView(view);
//
//
//        final AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//
//        resheduleTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//
//            }
//        });
//
//
//        removeMemberTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });
    }


    private void chooseValue(String m) {

        if(m.equalsIgnoreCase("Yes")){
            memberYesTv.setTextAppearance(getApplicationContext(),R.style.MyRadioSelected);
            memberNoTv.setTextAppearance(getApplicationContext(),R.style.MyRadioNotSelected);
            memberYesTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_selected));
            memberNoTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_not_selected));
            isMemberChosed=true;


        }else{

            memberNoTv.setTextAppearance(getApplicationContext(),R.style.MyRadioSelected);
            memberYesTv.setTextAppearance(getApplicationContext(),R.style.MyRadioNotSelected);
            memberNoTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_selected));
            memberYesTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_not_selected));
            isMemberChosed = false;

        }
    }
}
