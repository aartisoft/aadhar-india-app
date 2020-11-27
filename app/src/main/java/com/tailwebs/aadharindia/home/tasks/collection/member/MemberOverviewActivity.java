package com.tailwebs.aadharindia.home.tasks.collection.member;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.home.models.MemberCollectionResponseModel;
import com.tailwebs.aadharindia.home.models.PendingPenaltyModel;
import com.tailwebs.aadharindia.home.tasks.collection.group.GroupCollectionActivity;
import com.tailwebs.aadharindia.member.MyApplicantDividerItemDecoration;
import com.tailwebs.aadharindia.member.models.LoanTakerCalculatedEMIModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;
import com.tailwebs.aadharindia.utils.custom.singleselecttoggle.SingleSelectToggleGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberOverviewActivity extends BaseActivity implements View.OnClickListener {


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


    @BindView(R.id.co_aadhaar_tv)
    TextView coAadhaarTv;

    @BindView(R.id.date_tv)
    TextView dateTv;

    @BindView(R.id.collection_listing_recycler_view)
    RecyclerView collectionListingRecyclerView;


    static MemberOverviewActivity instance;

    ArrayList<PendingPenaltyModel> pendingPenaltyModelArrayList;


    private boolean isTotalPenaltyPresent=false;

    private FirebaseAnalytics mFirebaseAnalytics;

    @BindView(R.id.collect_button)
    Button collectButton;

    int selectedPaymentValue=0;

    String cityCenterID=null,groupID=null,passcode=null,loanTakerId=null,selectedPayment=null,modeOfPaymentID,pendingAmount,pendingAmountFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_overview);
        ButterKnife.bind(this);
        instance = this;

        loanTakerId = GlobalValue.loanTakerId;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Collection Member Overview", null);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Member Overview");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
        init();

            collectButton.setOnClickListener(this);

    }

    public void init(){

        if (NetworkUtils.haveNetworkConnection(this)) {
            showMemberDetails(loanTakerId);
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

    }

    private void showMemberDetails(String loanTakerId) {

        showProgressDialog(MemberOverviewActivity.this);
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<MemberCollectionResponseModel> call = apiService.showMember(loanTakerId,"collection");
            call.enqueue(new Callback<MemberCollectionResponseModel>() {
                @Override
                public void onResponse(Call<MemberCollectionResponseModel> call, final Response<MemberCollectionResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            setValuesFromResponse(response.body().getLoanTakerModels());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(MemberOverviewActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(MemberOverviewActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<MemberCollectionResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(MemberOverviewActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(LoanTakerCalculatedEMIModel loanTakerModels) {

        cityTv.setText(loanTakerModels.getCity().getLong_name());
        taskTv.setText(loanTakerModels.getName());
        statusTv.setText(loanTakerModels.getCollectionModel().getDisplay_status());
        if(loanTakerModels.getCollectionModel().getState().equalsIgnoreCase("Overdue")){
            statusTv.setTextColor(GroupMembersOverVIewActivity.getInstance().getResources().getColor(R.color.errorColor));
        }else if(loanTakerModels.getCollectionModel().getState().equalsIgnoreCase("Ongoing - Collection")){
            statusTv.setTextColor(GroupMembersOverVIewActivity.getInstance().getResources().getColor(R.color.headingPopupMessageColor));
        }else if(loanTakerModels.getCollectionModel().getState().equalsIgnoreCase("PaidBack")){
            statusTv.setTextColor(GroupMembersOverVIewActivity.getInstance().getResources().getColor(R.color.successColor));
        }else if(loanTakerModels.getCollectionModel().getState().equalsIgnoreCase("Ongoing - Paidback")){
            statusTv.setTextColor(GroupMembersOverVIewActivity.getInstance().getResources().getColor(R.color.successColor));
        }
        coAadhaarTv.setText("C.o. "+loanTakerModels.getAadhar_co());
        dateTv.setText("EMI's Remaining: "+loanTakerModels.getCollectionModel().getEmi_remaining());
        pendingAmount = loanTakerModels.getCollectionModel().getPending_amount();
        pendingAmountFormat = loanTakerModels.getCollectionModel().getPending_amount_in_format();

        GlobalValue.collectionId = loanTakerModels.getCollectionModel().getId();

        if(pendingAmount.equalsIgnoreCase("0.0")){
            collectButton.setEnabled(false);
            collectButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));
        }


        loadListView(loanTakerModels);
    }

    private void loadListView(LoanTakerCalculatedEMIModel loanTakerModels) {


        pendingPenaltyModelArrayList =  new ArrayList<PendingPenaltyModel>();

        PendingPenaltyModel pendingPenaltyModel = new PendingPenaltyModel();

        pendingPenaltyModel.setHeading("Pending Amount");
        pendingPenaltyModel.setAmount(loanTakerModels.getCollectionModel().getPending_amount());
        pendingPenaltyModel.setAmount_in_format(loanTakerModels.getCollectionModel().getPending_amount_in_format());

        pendingPenaltyModelArrayList.add(pendingPenaltyModel);

        if(loanTakerModels.getCollectionModel().getPendingInstallmentsModelArrayList().size()>0){
            for(int i=0;i<loanTakerModels.getCollectionModel().getPendingInstallmentsModelArrayList().size();i++){
                PendingPenaltyModel pendingPenaltyModel1 = new PendingPenaltyModel();
                pendingPenaltyModel1.setHeading("EMI for "+loanTakerModels.getCollectionModel().getPendingInstallmentsModelArrayList().
                        get(i).getPendingInstallmentModels().getEmi_for()+" ("+loanTakerModels.getCollectionModel().getPendingInstallmentsModelArrayList().
                        get(i).getPendingInstallmentModels().getEmi_detail()+")");

                pendingPenaltyModel1.setAmount_in_format(loanTakerModels.getCollectionModel().getPendingInstallmentsModelArrayList().get(i).
                getPendingInstallmentModels().getCurrent_pending_payment_in_format());
                pendingPenaltyModel1.setAmount(loanTakerModels.getCollectionModel().getPendingInstallmentsModelArrayList().get(i).
                        getPendingInstallmentModels().getCurrent_pending_payment());

                pendingPenaltyModelArrayList.add(pendingPenaltyModel1);
            }
        }

        Log.i("Drools","call"+loanTakerModels.getCollectionModel().getCurrent_pending_penalties()+
                loanTakerModels.getCollectionModel().getCurrent_pending_penalties_in_format());

        if(loanTakerModels.getCollectionModel().getCurrent_pending_penalties().equalsIgnoreCase("0.0")){

            isTotalPenaltyPresent=false;
        }else{
            isTotalPenaltyPresent =true;
            PendingPenaltyModel pendingPenaltyModel2 = new PendingPenaltyModel();

            pendingPenaltyModel2.setHeading("Total Penalty");
            pendingPenaltyModel2.setAmount(loanTakerModels.getCollectionModel().getCurrent_pending_penalties());
            pendingPenaltyModel2.setAmount_in_format(loanTakerModels.getCollectionModel().getCurrent_pending_penalties_in_format());

            pendingPenaltyModelArrayList.add(pendingPenaltyModel2);
        }








                // Add Adapter
        PendingPenaltyListingRecyclerAdapter adapter = new PendingPenaltyListingRecyclerAdapter();
                adapter.setData(pendingPenaltyModelArrayList,isTotalPenaltyPresent);
        collectionListingRecyclerView .setItemAnimator(new DefaultItemAnimator());
        collectionListingRecyclerView.addItemDecoration(new MyApplicantDividerItemDecoration(MemberOverviewActivity.this, DividerItemDecoration.VERTICAL, 16));
        collectionListingRecyclerView.setAdapter(adapter);



    }


    public static MemberOverviewActivity getInstance() {
        return instance;
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

        AlertDialog.Builder builder = new AlertDialog.Builder(MemberOverviewActivity.this);
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

                    Intent intent = new Intent(MemberOverviewActivity.this,CashCollectionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("city",cityTv.getText().toString().trim());
                    bundle.putString("name",taskTv.getText().toString().trim());
                    bundle.putString("status",statusTv.getText().toString().trim());
                    bundle.putString("co",coAadhaarTv.getText().toString().trim());
                    bundle.putString("emi_remaining",dateTv.getText().toString().trim());
                    bundle.putString("mode_of_payment_id",String.valueOf(selectedPaymentValue));
                    bundle.putString("mode_of_payment",selectedPayment);
                    bundle.putString("pending_amount",pendingAmount);
                    bundle.putString("pending_amount_format",pendingAmountFormat);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }else{
                    Toast.makeText(MemberOverviewActivity.this, "Please choose a payment type", Toast.LENGTH_SHORT).show();
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
