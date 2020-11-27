package com.tailwebs.aadharindia.member;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.applicant.ApplicantDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MemberScanResponseActivity extends BaseActivity implements View.OnClickListener{




    //user

    @BindView(R.id.user_name)
    TextView userNameTv;

    @BindView(R.id.profile_image)
    ImageView profileImageView;

    @BindView(R.id.customer_status_tv)
    TextView userStatusTv;


    //fields

    @BindView(R.id.loan_status_tv)
    TextView loanStatusTv;

    @BindView(R.id.outstanding_balance_tv)
    TextView outstandingBalanceTv;

    @BindView(R.id.over_due_amount_tv)
    TextView overDueAmountTv;

    @BindView(R.id.reason_tv)
    TextView reasonTv;

    @BindView(R.id.reason_description_tv)
    TextView reasonDescriptionTv;

    @BindView(R.id.customer_rating_tv)
    TextView customerRatingTv;

    @BindView(R.id.proceed_button)
    Button proceedButton;

    @BindView(R.id.reject_applicant_tv)
    TextView rejectApplicantTv;


    @BindView(R.id.layout_for_ongoing)
    LinearLayout onGoingLayout;

    @BindView(R.id.margin_for_ongoing)
    ConstraintLayout marginOngoing;

    @BindView(R.id.layout_for_reason)
    LinearLayout reasonLayout;

    @BindView(R.id.margin_for_reason)
    ConstraintLayout marginReason;

    @BindView(R.id.loan_status_margin)
    ConstraintLayout marginLoanStatus;


    @BindView(R.id.loan_status_layout)
    LinearLayout loanStatusLayout;


    private FirebaseAnalytics mFirebaseAnalytics;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_scan_response);
        ButterKnife.bind(this);
        rejectApplicantTv.setOnClickListener(this);
        proceedButton.setOnClickListener(this);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Applicant Scan Response", null);

        fillLoanStatus(getIntent().getExtras());

    }

    private void fillLoanStatus(Bundle extras) {

        userNameTv.setText(extras.getString("customer_name"));
        userStatusTv.setText(extras.getString("customer_status"));

        Picasso.with(MemberScanResponseActivity.this)
                .load(extras.getString("profile_image"))
                .placeholder(R.drawable.userimg_placeholder)
                .into(profileImageView);

        if(extras.getString("status_type").equalsIgnoreCase("rejected")){

            loanStatusTv.setText(extras.getString("status"));
            customerRatingTv.setText(extras.getString("rating"));
            reasonTv.setText(extras.getString("rejection_reason"));
            reasonDescriptionTv.setText(extras.getString("rejection_description"));
            onGoingLayout.setVisibility(View.GONE);
            marginOngoing.setVisibility(View.GONE);
            loanStatusLayout.setVisibility(View.GONE);
            marginLoanStatus.setVisibility(View.GONE);


        }else if(extras.getString("status_type").equalsIgnoreCase("on_going")){

            loanStatusTv.setText(extras.getString("status")+" / "+extras.getString("loan_status"));
            customerRatingTv.setText(extras.getString("rating"));
            outstandingBalanceTv.setText(extras.getString("outstanding_amount"));
            overDueAmountTv.setText(extras.getString("overdue_amount"));
            reasonLayout.setVisibility(View.GONE);
            marginReason.setVisibility(View.GONE);


        }else if(extras.getString("status_type").equalsIgnoreCase("closed")){

            loanStatusTv.setText(extras.getString("status"));
            customerRatingTv.setText(extras.getString("rating"));
            onGoingLayout.setVisibility(View.GONE);
            marginOngoing.setVisibility(View.GONE);
            reasonLayout.setVisibility(View.GONE);
            marginReason.setVisibility(View.GONE);

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.reject_applicant_tv:
                Intent intent = new Intent(MemberScanResponseActivity.this,GroupMemberListingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;


            case R.id.proceed_button:
                Intent intent1 = new Intent(MemberScanResponseActivity.this,MemberDetailActivity.class);
                intent1.putExtras(getIntent().getExtras());
                startActivity(intent1);
                finish();

                break;

        }
    }
}
