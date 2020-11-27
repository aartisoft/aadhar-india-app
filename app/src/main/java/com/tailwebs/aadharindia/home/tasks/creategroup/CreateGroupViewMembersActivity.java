 package com.tailwebs.aadharindia.home.tasks.creategroup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.GroupMemberListingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

 public class CreateGroupViewMembersActivity extends BaseActivity implements View.OnClickListener {


     //Action Bar

     @BindView(R.id.back_button)
     ImageView backButton;

     @BindView(R.id.heading_tv)
     TextView headingTV;

     @BindView(R.id.right_action_bar_button)
     ImageView rightActionBarButton;

     private ProgressDialog mProgressDialog;


     @BindView(R.id.distance_tv)
     TextView distanceTv;

     @BindView(R.id.city_tv)
     TextView cityTv;

     @BindView(R.id.task_tv)
     TextView taskTv;

     @BindView(R.id.status_tv)
     TextView statusTv;


     @BindView(R.id.by_tv)
     TextView byTv;

     @BindView(R.id.date_tv)
     TextView dateTv;

     @BindView(R.id.members_count_tv)
     TextView membersCountTv;

     @BindView(R.id.center_detail_tv)
     TextView centerDetailTv;

     @BindView(R.id.view_members_button)
     Button viewMembersButton;

     private FirebaseAnalytics mFirebaseAnalytics;



     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_view_members);
         ButterKnife.bind(this);

         //action bar
         backButton.setOnClickListener(this);
         headingTV.setText("Task Details");
         headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

         mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
         mFirebaseAnalytics.setCurrentScreen(this,  "Group Task ", null);

         viewMembersButton.setOnClickListener(this);





     }

     @Override
     public void onClick(View v) {
         switch (v.getId()){
             case R.id.back_button:
                 onBackPressed();
                 break;

             case R.id.view_members_button:
                 startActivity(new Intent(CreateGroupViewMembersActivity.this,GroupMemberListingActivity.class));
                 break;


             case R.id.create_group_button:
                 break;

         }


     }
 }
