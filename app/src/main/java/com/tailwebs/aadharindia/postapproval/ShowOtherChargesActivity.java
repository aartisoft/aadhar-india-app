package com.tailwebs.aadharindia.postapproval;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowOtherChargesActivity extends BaseActivity implements View.OnClickListener {


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    private ProgressDialog mProgressDialog;


    @BindView(R.id.user_name)
    TextView userNameTv;

    @BindView(R.id.user_co)
    TextView userCoTv;

    @BindView(R.id.approved_loan_tv)
    TextView approvedLoanTv;


    @BindView(R.id.processing_fees_tv)
    TextView processingFeesTv;


    @BindView(R.id.admin_charges_tv)
    TextView adminChargesTv;


    @BindView(R.id.insurance_amounts_tv)
    TextView insuranceAmountsTv;

    @BindView(R.id.disbursal_amount_tv)
    TextView disbursalAmountTv;

    @BindView(R.id.emi_amount_tv)
    TextView emiAmountTv;

    @BindView(R.id.first_emi_amount_tv)
    TextView firstEmiAmountTv;

    @BindView(R.id.emi_collection_date_tv)
    TextView emiCollectionDateTv;

    @BindView(R.id.interest_amount_tv)
    TextView interestAmountTv;


    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_other_charges);

        ButterKnife.bind(this);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Loan Details");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "PA Show Other Charges", null);


        setValuesFromBundle(getIntent().getExtras());
    }

    private void setValuesFromBundle(Bundle extras) {

        if(extras.getString("name")!=null){
            userNameTv.setText(extras.getString("name"));
        }


        if(extras.getString("co_name")!=null){
            userCoTv.setText(extras.getString("co_name"));
        }


        if(extras.getString("approved_loan")!=null){
            approvedLoanTv.setText(extras.getString("approved_loan"));
        }


        if(extras.getString("processing_fee")!=null){
            processingFeesTv.setText(extras.getString("processing_fee"));
        }

        if(extras.getString("admin_charges")!=null){
            adminChargesTv.setText(extras.getString("admin_charges"));
        }


        if(extras.getString("insurance")!=null){
            insuranceAmountsTv.setText(extras.getString("insurance"));
        }

        if(extras.getString("disbursal")!=null){
            disbursalAmountTv.setText(extras.getString("disbursal"));
        }

        if(extras.getString("emi_amount")!=null){
            emiAmountTv.setText(extras.getString("emi_amount"));
        }

        if(extras.getString("first_emi_amount")!=null){
            firstEmiAmountTv.setText(extras.getString("first_emi_amount"));
        }

        if(extras.getString("emi_collection_date")!=null){
            emiCollectionDateTv.setText(extras.getString("emi_collection_date"));
        }

        if(extras.getString("interest_amount")!=null){
            interestAmountTv.setText(extras.getString("interest_amount"));
        }
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
