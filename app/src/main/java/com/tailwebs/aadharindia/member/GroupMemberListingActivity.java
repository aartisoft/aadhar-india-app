package com.tailwebs.aadharindia.member;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.mukesh.OtpView;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.home.dashboard.TaskDashboardActivity;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupMemberListingActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.member_listing_recycler_view)
    RecyclerView memberListingRecyclerView;

    @BindView(R.id.submit_group_cpf)
    Button groupCPFButton;


    @BindView(R.id.add_new_customer_button)
    Button addNewCustomerButton;


//    @BindView(R.id.go_to_house_visit_tv)
//    Button goToHouseVisitTv;


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    private ProgressDialog mProgressDialog;

    String cityCenterID=null,groupID=null,passcode=null;
    static GroupMemberListingActivity instance;

    private Dialog dialog;

    OtpView passwordOTPView;
    TextView passwordErrorTV;


    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_listing);
        ButterKnife.bind(this);
        instance=this;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Group Members");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
        addNewCustomerButton.setOnClickListener(this);
        groupCPFButton.setOnClickListener(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Group Members Listing", null);


        init();


    }

    public static GroupMemberListingActivity getInstance() {
        return instance;
    }


    public void init(){

        cityCenterID =  GlobalValue.cityCenterId;
        groupID = GlobalValue.groupId;

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(GroupMemberListingActivity.this);
            getGroupMembers();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

    }

    private void getGroupMembers() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<GroupMemberListingResponseModel> call = apiService.getMembersInGroup(cityCenterID,groupID
            );
            call.enqueue(new Callback<GroupMemberListingResponseModel>() {
                @Override
                public void onResponse(Call<GroupMemberListingResponseModel> call, final Response<GroupMemberListingResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            loadListView(response.body());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(GroupMemberListingActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(GroupMemberListingActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<GroupMemberListingResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(GroupMemberListingActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void submitCPF(){


        Log.d("aadh onResp passcode", "" + passcode);

        showProgressDialog(GroupMemberListingActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);

            Call<GroupMemberResponseModel> call = apiService.submitCPF(groupID,
                    passcode);

            call.enqueue(new Callback<GroupMemberResponseModel>() {
                @Override
                public void onResponse(Call<GroupMemberResponseModel> call, Response<GroupMemberResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            dialog.dismiss();
                            goToDashboard();
                            Toast.makeText(GroupMemberListingActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("aadh onResponse", "" + response.body().getErrors());
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(new Gson().toJson(response.body()));

                                if(jsonObject.has("type")) {
                                    if (jsonObject.getString("type").equalsIgnoreCase("invalid_passcode")) {
                                        passwordOTPView.setLineColor(getResources().getColor(R.color.errorColor));
                                        passwordErrorTV.setVisibility(View.VISIBLE);
                                        passwordErrorTV.setText("Invalid Passcode");
                                        Toast.makeText(GroupMemberListingActivity.this, "Invalid passcode", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                if(jsonObject.has("group")) {

                                        dialog.dismiss();
                                        JSONObject  groupJSON = new JSONObject(new Gson().toJson(response.body().getErrors()));
                                        Log.d("aadh onResponse json", "" + groupJSON.getString("group"));

                                        String val = groupJSON.getString("group").substring(2);
                                        String final_val = val.substring(0, val.length() - 2);

                                        UiUtils.showAlertDialogWithOKButton(GroupMemberListingActivity.this, final_val, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                        Toast.makeText(GroupMemberListingActivity.this, "Invalid passcode", Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        hideProgressDialog();
                        NetworkUtils.handleErrorsCasesForAPICalls(GroupMemberListingActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<GroupMemberResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(GroupMemberListingActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }

    }

    private void goToDashboard() {
        Intent intent = new Intent(GroupMemberListingActivity.this,TaskDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private class CustomListener implements View.OnClickListener {

        public CustomListener(AlertDialog alertDialog) {
            dialog = alertDialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here
            passcode = passwordOTPView.getText().toString();
            if(UiUtils.checkValidationForOTP(GroupMemberListingActivity.this, passwordOTPView,passwordErrorTV)){
                submitCPF();
            }
        }
    }


    private void showPasswordDialog(){
        // title, custom view, actions dialog

        passcode= null;
        View view = getLayoutInflater().inflate(R.layout.custom_password_dialog, null);
        passwordOTPView=(OtpView) view.findViewById(R.id.password_otp_view);
        passwordErrorTV =(TextView)view.findViewById(R.id.password_error_tv);
        passwordOTPView.addTextChangedListener(new InputLayoutTextWatcher(passwordOTPView));
        AlertDialog.Builder builder = new AlertDialog.Builder(GroupMemberListingActivity.this);
        builder.setCancelable(false)
                .setTitle("Enter Passcode")
                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setView(view);


        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Button cancelButton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        theButton.setOnClickListener(new CustomListener(alertDialog));
        cancelButton.setOnClickListener(new CancelCustomListener(alertDialog));

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
                    boolean passwordStatus = UiUtils.checkValidationForOTP(GroupMemberListingActivity.this, passwordOTPView,passwordErrorTV);

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
            GroupMemberListingActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void loadListView(GroupMemberListingResponseModel body) {

        try {
            JSONObject jsonObject =  new JSONObject(new Gson().toJson(body));

            Log.i("Drools", "jsonObject" + jsonObject);

            if(jsonObject.getJSONObject("group").has("loan_takers")){

                memberListingRecyclerView.setVisibility(View.VISIBLE);
                addNewCustomerButton.setVisibility(View.GONE);
                // Add Adapter
                GroupMemberListingRecyclerAdapter adapter = new GroupMemberListingRecyclerAdapter(GlobalValue.placeName);
                adapter.setData(body.getGroupMemberListingModel().getLoanTakerModels(), false);
                memberListingRecyclerView.setItemAnimator(new DefaultItemAnimator());
                memberListingRecyclerView.addItemDecoration(new MyApplicantDividerItemDecoration(GroupMemberListingActivity.this, DividerItemDecoration.VERTICAL, 16));
                memberListingRecyclerView.setAdapter(adapter);
            }else{

                memberListingRecyclerView.setVisibility(View.GONE);
                addNewCustomerButton.setVisibility(View.VISIBLE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.add_new_customer_button:
                startActivity(new Intent(GroupMemberListingActivity.this,AddNewMemberScanActivity.class));
                break;


            case R.id.submit_group_cpf:
                startActivity(new Intent(GroupMemberListingActivity.this,SelectGroupLeaderActivity.class));
//                showPasswordDialog();
                break;


//            case R.id.go_to_house_visit_tv:
//                startActivity(new Intent(GroupMemberListingActivity.this,HouseVisitMemberListingActivity.class));
//                break;
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
