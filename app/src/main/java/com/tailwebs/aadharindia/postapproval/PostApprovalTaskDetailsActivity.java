package com.tailwebs.aadharindia.postapproval;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.home.AddEditNotesActivity;
import com.tailwebs.aadharindia.home.models.TaskModel;
import com.tailwebs.aadharindia.home.models.TaskResponseModel;
import com.tailwebs.aadharindia.home.tasks.housevisit.HouseVisitTaskDetailsActivity;
import com.tailwebs.aadharindia.postapproval.models.groupstatus.GroupStatusResponseModel;
import com.tailwebs.aadharindia.housevisit.HouseDetailsActivity;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostApprovalTaskDetailsActivity extends BaseActivity implements View.OnClickListener {


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


    @BindView(R.id.group_documents_tv)
    TextView groupDocumentsTv;

    @BindView(R.id.individual_documents_tv)
    TextView individualDocumentsTv;


    private boolean isGroupDocumentsCompleted = false,isIndividualDocumentsCompleted=false;

    //choose value from intent;
    String loanTakerID=null,buttonValue=null,groupID=null;

    static PostApprovalTaskDetailsActivity instance;

    TextView addEditTv,negativeCompleteTv,completeTv,cancelTv;

    private FirebaseAnalytics mFirebaseAnalytics;
    String notes=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_approval_task_details);
        ButterKnife.bind(this);
        instance=this;

        Log.i("Drools", "groupID" + groupID);

        loanTakerID = GlobalValue.loanTakerId;
        groupID =  GlobalValue.groupId;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "PA Task Details", null);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Task Details");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
        rightActionBarButton.setVisibility(View.VISIBLE);
        rightActionBarButton.setOnClickListener(this);

        groupDocumentsTv.setOnClickListener(this);
        individualDocumentsTv.setOnClickListener(this);



        init();




    }


    public static PostApprovalTaskDetailsActivity getInstance() {
        return instance;
    }

    public void init(){
        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(PostApprovalTaskDetailsActivity.this);

            getTask(GlobalValue.taskId);
            getGroupStatus();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
    }




    private void getTask(String task_id) {

        showProgressDialog(PostApprovalTaskDetailsActivity.this);
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

                            setValuesFromResponseForTask(response.body().getTaskModel());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(PostApprovalTaskDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(PostApprovalTaskDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<TaskResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(PostApprovalTaskDetailsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponseForTask(TaskModel taskModel) {

        cityTv.setText(taskModel.getTask_type().getName());
        if(taskModel.getName().length()>0){
            taskTv.setText(taskModel.getCity().getLong_name()+" - "+taskModel.getName());
        }else{
            taskTv.setText(taskModel.getCity().getLong_name());
        }
        if(taskModel.getState().equalsIgnoreCase("New")){
            statusTv.setText("Pending");
            statusTv.setTextColor(getResources().getColor(R.color.errorColor));
        }else{
            statusTv.setVisibility(View.GONE);
        }

        notes = taskModel.getNote();
        if(taskModel.getNote()!=null){
            notesTv.setVisibility(View.VISIBLE);
            notesTv.setText(taskModel.getNote());
        }else{
            notesTv.setVisibility(View.GONE);
        }

        dateTv.setText(taskModel.getCreated_at_in_format());

        GlobalValue.cityCenterId = taskModel.getGroupModel().getCity_center_id();
        GlobalValue.groupId = taskModel.getGroupModel().getId();
    }

    private void getGroupStatus() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<GroupStatusResponseModel> call = apiService.getGroupStatus(groupID);
            call.enqueue(new Callback<GroupStatusResponseModel>() {
                @Override
                public void onResponse(Call<GroupStatusResponseModel> call, final Response<GroupStatusResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Log.i("Drools", "" + new Gson().toJson(response.body()));

                            setValuesFromResponse(response.body());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(PostApprovalTaskDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(PostApprovalTaskDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<GroupStatusResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(PostApprovalTaskDetailsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(GroupStatusResponseModel body) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new Gson().toJson(body));



        if(body.getGroupStatusModel().getgStatusResponseModel().getStatusModel().getGroupDocumentsModel().getActivated()){

            UiUtils.setProcessActivated(PostApprovalTaskDetailsActivity.this,groupDocumentsTv);
            if (jsonObject.getJSONObject("group").getJSONObject("status").getJSONObject("status").getJSONObject("group_documents").has("completed")) {

                if (jsonObject.getJSONObject("group").getJSONObject("status").getJSONObject("status").getJSONObject("group_documents").getString("completed") == null) {

                } else {
                    if (body.getGroupStatusModel().getgStatusResponseModel().getStatusModel().getGroupDocumentsModel().getCompleted().trim().equalsIgnoreCase("true")) {
                        UiUtils.setProcessCompleted(PostApprovalTaskDetailsActivity.this, groupDocumentsTv);
                        isGroupDocumentsCompleted = true;
                    } else if (body.getGroupStatusModel().getgStatusResponseModel().getStatusModel().getGroupDocumentsModel().getCompleted().trim().equalsIgnoreCase("false")) {
                        UiUtils.setProcessNotCompleteWithImaged(PostApprovalTaskDetailsActivity.this, groupDocumentsTv);
                    }
                }
            }

        }else{
            UiUtils.setProcessNotCompleted(PostApprovalTaskDetailsActivity.this,groupDocumentsTv);
        }

        if(body.getGroupStatusModel().getgStatusResponseModel().getStatusModel().getIndividualDocumentsModel().getActivated()){

            UiUtils.setProcessActivated(PostApprovalTaskDetailsActivity.this,individualDocumentsTv);
            if (jsonObject.getJSONObject("group").getJSONObject("status").getJSONObject("status").getJSONObject("individual_documents").has("completed")) {

                if (jsonObject.getJSONObject("group").getJSONObject("status").getJSONObject("status").getJSONObject("individual_documents").getString("completed") == null) {

                } else {
                    if (body.getGroupStatusModel().getgStatusResponseModel().getStatusModel().getIndividualDocumentsModel().getCompleted().trim().equalsIgnoreCase("true")) {
                        UiUtils.setProcessCompleted(PostApprovalTaskDetailsActivity.this, individualDocumentsTv);
                        isIndividualDocumentsCompleted = true;
                    } else if (body.getGroupStatusModel().getgStatusResponseModel().getStatusModel().getIndividualDocumentsModel().getCompleted().trim().equalsIgnoreCase("false")) {
                        UiUtils.setProcessNotCompleteWithImaged(PostApprovalTaskDetailsActivity.this, individualDocumentsTv);
                    }
                }
            }



        }else {
            UiUtils.setProcessNotCompleted(PostApprovalTaskDetailsActivity.this,individualDocumentsTv);
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


            case R.id.group_documents_tv:
                goToGroupDocuments();
                break;

            case R.id.individual_documents_tv:
                goToIndividualDocuments();
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

        negativeCompleteTv.setVisibility(View.GONE);
        completeTv.setVisibility(View.GONE);
        cancelTv.setVisibility(View.GONE);

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
                Intent intent = new Intent(PostApprovalTaskDetailsActivity.this,AddEditNotesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("notes",notes);
                bundle.putString("activity","post_approval");
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

    private void goToIndividualDocuments() {
        startActivity(new Intent(PostApprovalTaskDetailsActivity.this,IndividualDocumentsActivity.class));
    }

    private void goToGroupDocuments() {
        startActivity(new Intent(PostApprovalTaskDetailsActivity.this,GroupDocumentsActivity.class));
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



