package com.tailwebs.aadharindia.home.tasks.collection.group;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;
import com.tailwebs.aadharindia.utils.custom.singleselecttoggle.SingleSelectToggleGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupOverViewActivity extends BaseActivity implements View.OnClickListener {

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

    @BindView(R.id.members_count_tv)
    TextView membersCountTv;

    @BindView(R.id.group_leader_tv)
    TextView groupLeaderTv;

    @BindView(R.id.pending_amount_collected_tv)
    TextView pendingAmountCollectedTv;

    @BindView(R.id.collect_button)
    Button collectButton;

    @BindView(R.id.group_id_tv)
            TextView groupIdTv;


    //choose value from intent;
    String loanTakerID=null,groupID=null,selectedPayment=null,pending_amount=null;
    int selectedPaymentValue=0;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_over_view);
        ButterKnife.bind(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Group Overview", null);


        loanTakerID = GlobalValue.loanTakerId;
        groupID =  GlobalValue.groupId;



        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText(GlobalValue.taskName);
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        setValuesFromBundle(getIntent().getExtras());
        collectButton.setOnClickListener(this);


    }


    private void setValuesFromBundle(Bundle extras) {

        if(extras.getString("task")!=null){
            cityTv.setText(extras.getString("task"));
        }


        if(extras.getString("group_id")!=null){
            groupLeaderTv.setText("Group ID : "+extras.getString("group_id"));
        }


        if(extras.getString("group_leader")!=null){
            groupLeaderTv.setText("Group Leader : "+extras.getString("group_leader"));
        }


        if(extras.getString("members_count")!=null){
            membersCountTv.setText(extras.getString("members_count")+" Members");
        }


        if(extras.getString("pending_amount_format")!=null){
            pendingAmountCollectedTv.setText(extras.getString("pending_amount_format"));
            pending_amount = extras.getString("pending_amount");
        }


        if(pending_amount.equalsIgnoreCase("0.0")){
            collectButton.setEnabled(false);
            collectButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.collect_button:
                showPaymentMethodPopup();
                break;


        }
    }

    private void showPaymentMethodPopup() {
        selectedPaymentValue = 0;
        View view = getLayoutInflater().inflate(R.layout.custom_message_payment_type_dialog, null);
        final SingleSelectToggleGroup paymentTypeToggle =(SingleSelectToggleGroup) view.findViewById(R.id.payment_type_toggle);

        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < GlobalValue.paymentTypeModelArrayList.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setLayoutParams (new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT,RadioGroup.LayoutParams.WRAP_CONTENT));
                rdbtn.setText(GlobalValue.paymentTypeModelArrayList.get(i).getName());
                rdbtn.setTextSize(14);
                rdbtn.setId(GlobalValue.paymentTypeModelArrayList.get(i).getId());
                rdbtn.setTextColor(getResources().getColor(R.color.primaryColor));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rdbtn.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryColor)));
                }

                rdbtn.setPadding(32,32,32,32);
                paymentTypeToggle.addView(rdbtn);
            }
        }


        paymentTypeToggle.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                selectedPaymentValue = checkedId;


                for(int i=0;i<GlobalValue.paymentTypeModelArrayList.size();i++){
                    if(GlobalValue.paymentTypeModelArrayList.get(i).getId() == selectedPaymentValue){
                        selectedPayment = GlobalValue.paymentTypeModelArrayList.get(i).getName();
                    }
                }


            }
        });

        Button yesButton =(Button)view.findViewById(R.id.yes_button);
        Button noButton =(Button)view.findViewById(R.id.no_button);
        yesButton.setText(getResources().getString(R.string.action_continue));
        noButton.setText(getResources().getString(R.string.cancel_button));

        AlertDialog.Builder builder = new AlertDialog.Builder(GroupOverViewActivity.this);
        builder.setCancelable(false)
                .setTitle(getResources().getString(R.string.hint_collect_emi_by))
                .setPositiveButton("", null)
                .setNeutralButton("", null)
                .setView(view);


        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedPaymentValue!=0){

                    alertDialog.dismiss();

                    Intent intent = new Intent(GroupOverViewActivity.this,GroupCollectionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("city",cityTv.getText().toString().trim());
                    bundle.putString("group_leader",groupLeaderTv.getText().toString().trim());
                    bundle.putString("pending_amount_format",pendingAmountCollectedTv.getText().toString().trim());
                    bundle.putString("pending_amount",pending_amount);
                    bundle.putString("members_count",membersCountTv.getText().toString().trim());
                    bundle.putString("mode_of_payment_id",String.valueOf(selectedPaymentValue));
                    bundle.putString("mode_of_payment",selectedPayment);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }else{
                    Toast.makeText(GroupOverViewActivity.this, "Please choose a payment type", Toast.LENGTH_SHORT).show();
                }

            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}
