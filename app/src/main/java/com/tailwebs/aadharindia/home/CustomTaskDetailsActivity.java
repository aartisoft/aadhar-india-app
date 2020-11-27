package com.tailwebs.aadharindia.home;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.home.dashboard.TaskDashboardActivity;
import com.tailwebs.aadharindia.home.models.TaskModel;
import com.tailwebs.aadharindia.home.models.TaskResponseModel;
import com.tailwebs.aadharindia.home.tasks.collection.CollectionTaskDetailsActivity;
import com.tailwebs.aadharindia.home.tasks.creategroup.CreateGroupTaskDetailsActivity;
import com.tailwebs.aadharindia.housevisit.HouseDetailsActivity;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomTaskDetailsActivity extends BaseActivity implements View.OnClickListener {



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


    @BindView(R.id.notes_tv)
    TextView notesTv;

    TextView addEditTv,negativeCompleteTv,completeTv,cancelTv;
    boolean isNegativeCompleteClicked=false,isCompleteClicked=false,isCancelClicked=false;

    String taskId,reason;


    TextInputLayout reasonLayout;
    TextInputEditText reasonET;

    static CustomTaskDetailsActivity instance;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_task_details);
        ButterKnife.bind(this);

        instance=this;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Task Details");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
        rightActionBarButton.setVisibility(View.VISIBLE);
        rightActionBarButton.setOnClickListener(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Custom Task", null);


        init();



    }

    public static CustomTaskDetailsActivity getInstance() {
        return instance;
    }

    public void init() {

        if(getIntent().hasExtra("task_id")) {
            if (NetworkUtils.haveNetworkConnection(this)) {
                taskId = getIntent().getStringExtra("task_id");

                getTask(getIntent().getStringExtra("task_id"));

                GlobalValue.taskId = getIntent().getStringExtra("task_id");
            } else {
                UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            }

        }
    }


    private void getTask(String task_id) {

        showProgressDialog(CustomTaskDetailsActivity.this);
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<TaskResponseModel> call = apiService.getTask(task_id);
            call.enqueue(new Callback<TaskResponseModel>() {
                @Override
                public void onResponse(Call<TaskResponseModel> call, final Response<TaskResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            setValuesFromResponse(response.body().getTaskModel());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(CustomTaskDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(CustomTaskDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<TaskResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(CustomTaskDetailsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }


    private void setValuesFromResponse(TaskModel taskModel) {


        cityTv.setText(taskModel.getCity().getLong_name());
        taskTv.setText(taskModel.getTask_type().getName()+" - "+taskModel.getTask_number());
        if(taskModel.getState().equalsIgnoreCase("New")){
            statusTv.setText("Pending");
            statusTv.setTextColor(getResources().getColor(R.color.errorColor));
        }else{
            statusTv.setVisibility(View.GONE);
        }

        notesTv.setText(taskModel.getNote());
        dateTv.setText(taskModel.getCreated_at_in_format());



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.right_action_bar_button:
                showPopup();
                break;


        }

    }

    private void showPopup() {

        View view = getLayoutInflater().inflate(R.layout.custom_task_edit_cancel_dialog, null);
        addEditTv=(TextView) view.findViewById(R.id.add_edit_tv);
        negativeCompleteTv=(TextView) view.findViewById(R.id.negative_complete_tv);
        completeTv=(TextView) view.findViewById(R.id.complete_tv);
        cancelTv=(TextView) view.findViewById(R.id.cancel_tv);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setPositiveButton("", null)
                .setNeutralButton("", null)
                .setView(view);


        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        addEditTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialog.dismiss();
                Intent intent = new Intent(CustomTaskDetailsActivity.this,AddEditNotesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("notes",notesTv.getText().toString().trim());
                bundle.putString("activity","custom");
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


        negativeCompleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showAreYouSureNegativeCompletePopUp();

            }
        });

        completeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showAreYouSureCompletePopUp();
            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showAreYouSureCancelPopUp();
            }
        });
    }

    private void showAreYouSureCancelPopUp() {
        View view = getLayoutInflater().inflate(R.layout.custom_message_yes_no_dialog, null);
        TextView messageTV =(TextView)view.findViewById(R.id.message_tv);
        Button yesButton =(Button)view.findViewById(R.id.yes_button);
        Button noButton =(Button)view.findViewById(R.id.no_button);
        messageTV.setText(getResources().getString(R.string.hint_cancel_task));
        yesButton.setText(getResources().getString(R.string.hint_yes));
        noButton.setText(getResources().getString(R.string.hint_no));
        noButton.setTextAppearance(CustomTaskDetailsActivity.this,R.style.MyBluePopUpButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(CustomTaskDetailsActivity.this);
        builder.setCancelable(false)
                .setTitle(getResources().getString(R.string.hint_cancel_task_title))
                .setPositiveButton("", null)
                .setNeutralButton("", null)
                .setView(view);


        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                isCancelClicked=true;
                showReasonForRejectionPopup();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void showAreYouSureCompletePopUp() {
        View view = getLayoutInflater().inflate(R.layout.custom_message_yes_no_dialog, null);
        TextView messageTV =(TextView)view.findViewById(R.id.message_tv);
        Button yesButton =(Button)view.findViewById(R.id.yes_button);
        Button noButton =(Button)view.findViewById(R.id.no_button);
        messageTV.setText(getResources().getString(R.string.hint_complete_message));
        yesButton.setText(getResources().getString(R.string.hint_yes));
        noButton.setText(getResources().getString(R.string.hint_no));
        noButton.setTextAppearance(CustomTaskDetailsActivity.this,R.style.MyBluePopUpButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(CustomTaskDetailsActivity.this);
        builder.setCancelable(false)
                .setTitle(getResources().getString(R.string.hint_complete_task_title))
                .setPositiveButton("", null)
                .setNeutralButton("", null)
                .setView(view);


        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                isCompleteClicked=true;
                showReasonForRejectionPopup();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void showAreYouSureNegativeCompletePopUp() {
        View view = getLayoutInflater().inflate(R.layout.custom_message_yes_no_dialog, null);
        TextView messageTV =(TextView)view.findViewById(R.id.message_tv);
        Button yesButton =(Button)view.findViewById(R.id.yes_button);
        Button noButton =(Button)view.findViewById(R.id.no_button);
        messageTV.setText(getResources().getString(R.string.hint_negative_complete_message));
        yesButton.setText(getResources().getString(R.string.hint_yes));
        noButton.setText(getResources().getString(R.string.hint_no));
        noButton.setTextAppearance(CustomTaskDetailsActivity.this,R.style.MyBluePopUpButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(CustomTaskDetailsActivity.this);
        builder.setCancelable(false)
                .setTitle(getResources().getString(R.string.hint_negative_complete_task_title))
                .setPositiveButton("", null)
                .setNeutralButton("", null)
                .setView(view);


        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                isNegativeCompleteClicked=true;
                showReasonForRejectionPopup();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void showReasonForRejectionPopup(){
        View view = getLayoutInflater().inflate(R.layout.custom_rejection_dialog, null);
        reasonLayout =(TextInputLayout)view.findViewById(R.id.input_layout_reason);
        reasonET =(TextInputEditText)view.findViewById(R.id.input_reason);
        reasonET.addTextChangedListener(new InputLayoutTextWatcher(reasonET));
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomTaskDetailsActivity.this);
        builder.setCancelable(false)
                .setTitle("Reason")
                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(CustomTaskDetailsActivity.this, "yyy", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("", null)
                .setView(view);


        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new RejectListener(alertDialog));


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


                case R.id.input_reason:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean reasonStatus = UiUtils.checkValidation(CustomTaskDetailsActivity.this, reasonET,reasonLayout,itemPassed);

                    if (reasonStatus == false) {
                        requestFocus(reasonET);
                    } else {
                        reasonLayout.setErrorEnabled(false);
                    }
                    break;

            }
        }

    }

    public void requestFocus(View view) {
        if (view.requestFocus())
            CustomTaskDetailsActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    private class RejectListener implements View.OnClickListener {

        private Dialog dialog;

        public RejectListener(AlertDialog alertDialog) {
            this.dialog = alertDialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here

            if(UiUtils.checkValidation(CustomTaskDetailsActivity.this, reasonET,reasonLayout,new ArrayList<String>())) {
                reason = reasonET.getText().toString().trim();
                this.dialog.dismiss();

                if(isCancelClicked){
                    Log.i("Drools", "isCancelClicked" );
                    callCancelTaskAPI();
                }else if(isCompleteClicked){
                    Log.i("Drools", "isCompleteClicked" );
                    callCompleteTaskAPI();
                }else if(isNegativeCompleteClicked){
                    Log.i("Drools", "isNegativeCompleteClicked" );
                    callNegativeCompleteTaskAPI();

                }




            }
        }
    }

    private void callNegativeCompleteTaskAPI() {
        showProgressDialog(CustomTaskDetailsActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);

            Call<TaskResponseModel> call = apiService.negativeCancelCustomTask(taskId
                    ,reason);

            call.enqueue(new Callback<TaskResponseModel>() {
                @Override
                public void onResponse(Call<TaskResponseModel> call, Response<TaskResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Toast.makeText(CustomTaskDetailsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            goToTaskDashboard();
                        } else {
                            hideProgressDialog();
                            Log.d("aadh onResponse", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(CustomTaskDetailsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        hideProgressDialog();
                        Log.i("aadh Error", "--" + new Gson().toJson(response.body()));
                        NetworkUtils.handleErrorsCasesForAPICalls(CustomTaskDetailsActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<TaskResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(CustomTaskDetailsActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void callCompleteTaskAPI() {
        showProgressDialog(CustomTaskDetailsActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);

            Call<TaskResponseModel> call = apiService.completeCustomTask(taskId
                    ,reason);

            call.enqueue(new Callback<TaskResponseModel>() {
                @Override
                public void onResponse(Call<TaskResponseModel> call, Response<TaskResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Toast.makeText(CustomTaskDetailsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            goToTaskDashboard();
                        } else {
                            hideProgressDialog();
                            Log.d("aadh onResponse", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(CustomTaskDetailsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        hideProgressDialog();
                        Log.i("aadh Error", "--" + new Gson().toJson(response.body()));
                        NetworkUtils.handleErrorsCasesForAPICalls(CustomTaskDetailsActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<TaskResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(CustomTaskDetailsActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void callCancelTaskAPI() {

        showProgressDialog(CustomTaskDetailsActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);

            Call<TaskResponseModel> call = apiService.cancelCustomTask(taskId
                    ,reason);

            call.enqueue(new Callback<TaskResponseModel>() {
                @Override
                public void onResponse(Call<TaskResponseModel> call, Response<TaskResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Toast.makeText(CustomTaskDetailsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            goToTaskDashboard();
                        } else {
                            hideProgressDialog();
                            Log.d("aadh onResponse", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(CustomTaskDetailsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        hideProgressDialog();
                        Log.i("aadh Error", "--" + new Gson().toJson(response.body()));
                        NetworkUtils.handleErrorsCasesForAPICalls(CustomTaskDetailsActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<TaskResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(CustomTaskDetailsActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void goToTaskDashboard() {
        finish();
        startActivity(new Intent(CustomTaskDetailsActivity.this,TaskDashboardActivity.class));
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
