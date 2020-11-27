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
import com.tailwebs.aadharindia.member.models.LoanTakerCalculatedEMIModel;
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

public class SelectGroupLeaderActivity extends BaseActivity implements SelectGroupLeaderRecyclerAdapter.SelectGroupLeaderAdapterListener,View.OnClickListener {


    @BindView(R.id.member_listing_recycler_view)
    RecyclerView memberListingRecyclerView;

    @BindView(R.id.submit_group_cpf)
    Button groupCPFButton;


    @BindView(R.id.add_new_customer_button)
    Button addNewCustomerButton;


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    private ProgressDialog mProgressDialog;

    String cityCenterID=null,groupID=null,passcode=null;
    static SelectGroupLeaderActivity instance;

    private Dialog dialog;

    OtpView passwordOTPView;
    TextView passwordErrorTV;

    String content = null,id=null;


    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group_leader);
        ButterKnife.bind(this);
        instance=this;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Select Group Leader");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
        groupCPFButton.setOnClickListener(this);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Select Group Leader", null);


        init();

    }

    public static SelectGroupLeaderActivity getInstance() {
        return instance;
    }


    public void init(){

        cityCenterID =  GlobalValue.cityCenterId;
        groupID = GlobalValue.groupId;

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(SelectGroupLeaderActivity.this);
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
                            NetworkUtils.handleErrorsForAPICalls(SelectGroupLeaderActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(SelectGroupLeaderActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<GroupMemberListingResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(SelectGroupLeaderActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void submitCPF(){


        Log.d("aadh onResp passcode", "" + passcode);

        showProgressDialog(SelectGroupLeaderActivity.this);
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
                            Toast.makeText(SelectGroupLeaderActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

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
                                        Toast.makeText(SelectGroupLeaderActivity.this, "Invalid passcode", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                if(jsonObject.has("group")) {

                                    dialog.dismiss();
                                    JSONObject  groupJSON = new JSONObject(new Gson().toJson(response.body().getErrors()));
                                    Log.d("aadh onResponse json", "" + groupJSON.getString("group"));

                                    String val = groupJSON.getString("group").substring(2);
                                    String final_val = val.substring(0, val.length() - 2);

                                    UiUtils.showAlertDialogWithOKButton(SelectGroupLeaderActivity.this, final_val, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    Toast.makeText(SelectGroupLeaderActivity.this, "Invalid passcode", Toast.LENGTH_SHORT).show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        hideProgressDialog();
                        NetworkUtils.handleErrorsCasesForAPICalls(SelectGroupLeaderActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<GroupMemberResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(SelectGroupLeaderActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }

    }

    private void goToDashboard() {
        Intent intent = new Intent(SelectGroupLeaderActivity.this,TaskDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onLeaderSelected(LoanTakerCalculatedEMIModel loanTakerCalculatedEMIModel) {

        content = loanTakerCalculatedEMIModel.getName();
        id = loanTakerCalculatedEMIModel.getId();
        showSelectLeaderPopup(content,id);


    }

    private void showSelectLeaderPopup(String content, final String id) {

        View view = getLayoutInflater().inflate(R.layout.custom_message_yes_no_dialog, null);
        TextView messageTV =(TextView)view.findViewById(R.id.message_tv);
        Button yesButton =(Button)view.findViewById(R.id.yes_button);
        Button noButton =(Button)view.findViewById(R.id.no_button);
        messageTV.setText("Are you sure you want to select "+content+" as group leader?");
        yesButton.setText(getResources().getString(R.string.hint_yes));
        noButton.setText(getResources().getString(R.string.hint_no));

        AlertDialog.Builder builder = new AlertDialog.Builder(SelectGroupLeaderActivity.this);
        builder.setCancelable(false)
                .setTitle(content+" - Group Leader?")
                .setPositiveButton("", null)
                .setNeutralButton("", null)
                .setView(view);


        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkUtils.haveNetworkConnection(SelectGroupLeaderActivity.this)) {
                    showProgressDialog(SelectGroupLeaderActivity.this);


                    alertDialog.dismiss();
                    callGroupLeaderAPI(id);


                } else {
                    UiUtils.showAlertDialogWithOKButton(SelectGroupLeaderActivity.this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                }



            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupCPFButton.setEnabled(false);
                groupCPFButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

                alertDialog.dismiss();
            }
        });
    }

    private void  callGroupLeaderAPI(String groupLeaderId){

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<GroupLeaderResponseModel> call = apiService.addGroupLeader(groupID,groupLeaderId
            );
            call.enqueue(new Callback<GroupLeaderResponseModel>() {
                @Override
                public void onResponse(Call<GroupLeaderResponseModel> call, final Response<GroupLeaderResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            Toast.makeText(SelectGroupLeaderActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                            groupCPFButton.setEnabled(true);
                            groupCPFButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));

                        } else {
                            Log.d("aadh onResponse", "" + response.body().getErrors());
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(new Gson().toJson(response.body()));

                                if(jsonObject.has("group")) {

                                    JSONObject  groupJSON = new JSONObject(new Gson().toJson(response.body().getErrors()));
                                    Log.d("aadh onResponse json", "" + groupJSON.getString("group"));

                                    String val = groupJSON.getString("group").substring(2);
                                    String final_val = val.substring(0, val.length() - 2);

                                    UiUtils.showAlertDialogWithOKButton(SelectGroupLeaderActivity.this, final_val, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });

                                }else{
                                    NetworkUtils.handleErrorsForAPICalls(SelectGroupLeaderActivity.this, response.body().getErrors(), response.body().getNotice());

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                              }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(SelectGroupLeaderActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<GroupLeaderResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(SelectGroupLeaderActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }

    }


    private class CustomListener implements View.OnClickListener {

        public CustomListener(AlertDialog alertDialog) {
            dialog = alertDialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here
            passcode = passwordOTPView.getText().toString();
            if(UiUtils.checkValidationForOTP(SelectGroupLeaderActivity.this, passwordOTPView,passwordErrorTV)){
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectGroupLeaderActivity.this);
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
                    boolean passwordStatus = UiUtils.checkValidationForOTP(SelectGroupLeaderActivity.this, passwordOTPView,passwordErrorTV);

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
            SelectGroupLeaderActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void loadListView(GroupMemberListingResponseModel body) {

        try {
            JSONObject jsonObject =  new JSONObject(new Gson().toJson(body));

            if(jsonObject.getJSONObject("group").has("loan_takers")){

                memberListingRecyclerView.setVisibility(View.VISIBLE);
                addNewCustomerButton.setVisibility(View.GONE);
                // Add Adapter
                SelectGroupLeaderRecyclerAdapter adapter = new SelectGroupLeaderRecyclerAdapter(body.getGroupMemberListingModel().getLoanTakerModels(),SelectGroupLeaderActivity.this);
                adapter.setData(body.getGroupMemberListingModel().getLoanTakerModels(), false);
                memberListingRecyclerView.setItemAnimator(new DefaultItemAnimator());
                memberListingRecyclerView.addItemDecoration(new MyApplicantDividerItemDecoration(SelectGroupLeaderActivity.this, DividerItemDecoration.VERTICAL, 16));
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
                

            case R.id.submit_group_cpf:
                showPasswordDialog();
                break;
                
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
