package com.tailwebs.aadharindia.home.tasks.housevisit;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.home.AddEditNotesActivity;
import com.tailwebs.aadharindia.home.models.TaskModel;
import com.tailwebs.aadharindia.home.models.TaskResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseVisitTaskDetailsActivity extends BaseActivity implements View.OnClickListener {


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


    @BindView(R.id.members_count_tv)
    TextView membersCountTv;

    @BindView(R.id.hv_pending_tv)
    TextView hvPendingTv;

    @BindView(R.id.hv_completed_tv)
    TextView hvCompletedTv;

    @BindView(R.id.rejected_tv)
    TextView rejectedTv;

    @BindView(R.id.view_members_button)
    Button viewMembersButton;

    TextView addEditTv,negativeCompleteTv,completeTv,cancelTv;

    String notes=null;


    static HouseVisitTaskDetailsActivity instance;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_visit_task_details);
        ButterKnife.bind(this);

        instance=this;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "HV Group Task", null);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Task Details");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
        rightActionBarButton.setVisibility(View.VISIBLE);
        rightActionBarButton.setOnClickListener(this);



      init();

    }

    public static HouseVisitTaskDetailsActivity getInstance() {
        return instance;
    }

    public void init(){

            if (NetworkUtils.haveNetworkConnection(this)) {

                getTask(GlobalValue.taskId);

            } else {
                UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            }



        viewMembersButton.setOnClickListener(this);
    }


    private void getTask(String task_id) {

        showProgressDialog(HouseVisitTaskDetailsActivity.this);
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
                            NetworkUtils.handleErrorsForAPICalls(HouseVisitTaskDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitTaskDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<TaskResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitTaskDetailsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(TaskModel taskModel) {


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

        if(taskModel.getNote()!=null){
            notesTv.setVisibility(View.VISIBLE);
            notesTv.setText(taskModel.getNote());
        }else{
            notesTv.setVisibility(View.GONE);
        }

        notes = taskModel.getNote();
//        byTv.setText(item.getName()+" - "+item.getTask_number());
        dateTv.setText(taskModel.getCreated_at_in_format());

        GlobalValue.cityCenterId = taskModel.getGroupModel().getCity_center_id();
        GlobalValue.groupId = taskModel.getGroupModel().getId();

        membersCountTv.setText(taskModel.getGroupModel().getApplicantCountModel().getAll());
        hvPendingTv.setText(taskModel.getGroupModel().getApplicantCountModel().getPre_approved());
        hvCompletedTv.setText(taskModel.getGroupModel().getApplicantCountModel().getApproved());
        rejectedTv.setText(taskModel.getGroupModel().getApplicantCountModel().getRejected());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.view_members_button:
                goToGroupReviewPage();
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
                Intent intent = new Intent(HouseVisitTaskDetailsActivity.this,AddEditNotesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("notes",notes);
                bundle.putString("activity","house_visit");
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

    private void goToGroupReviewPage() {

        Intent intent = new Intent(HouseVisitTaskDetailsActivity.this,HouseVisitGroupReviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("task",taskTv.getText().toString().trim());
        bundle.putString("city",cityTv.getText().toString().trim());
        GlobalValue.taskName = cityTv.getText().toString().trim();
        bundle.putString("status",statusTv.getText().toString().trim());
        bundle.putString("by",byTv.getText().toString().trim());
        bundle.putString("date",dateTv.getText().toString().trim());
        bundle.putString("members_count",membersCountTv.getText().toString().trim());
        intent.putExtras(bundle);
        startActivity(intent);
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
