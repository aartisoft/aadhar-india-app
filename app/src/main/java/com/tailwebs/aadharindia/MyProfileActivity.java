package com.tailwebs.aadharindia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tailwebs.aadharindia.loginandforgot.LoginActivity;
import com.tailwebs.aadharindia.member.applicant.ApplicantDetailActivity;
import com.tailwebs.aadharindia.models.User;
import com.tailwebs.aadharindia.utils.Constants;
import com.tailwebs.aadharindia.utils.SharedPreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProfileActivity extends BaseActivity
        implements View.OnClickListener {

    @BindView(R.id.user_name)
    TextView userName;

    @BindView(R.id.user_id)
    TextView userId;

    @BindView(R.id.phone_tv)
    TextView phoneTv;

    @BindView(R.id.email_tv)
    TextView emailTv;

    @BindView(R.id.address_tv)
    TextView addressTv;

    @BindView(R.id.profile_image)
    ImageView profileImage;


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("My Profile");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);


        User  user =  SharedPreferenceUtils.getUserData(MyProfileActivity.this, Constants.KEY_USER );

        if(user.getName()!=null){
            userName.setText(user.getName());
        }

        if(user.getUserid()!=null){
            userId.setText("ID : "+user.getUserid());
        }

        if(user.getPhoneNumber()!=null){
            phoneTv.setText(user.getPhoneNumber());
        }

        if(user.getEmail()!=null){
            emailTv.setText(user.getEmail());
        }

        if(user.getHomeAddressId()!=null){
            addressTv.setText(user.getHomeAddress().getFull_address());
        }else{
            addressTv.setText("-");
        }


        Picasso.with(MyProfileActivity.this)
                .load(user.getPdfFiles().getMedium())
                .placeholder(R.drawable.userimg_placeholder)
                .into(profileImage);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;


        }
    }
}
