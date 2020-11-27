package com.tailwebs.aadharindia.home.tasks.collection.member;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.home.models.TaskModel;
import com.tailwebs.aadharindia.home.models.TaskResponseModel;
import com.tailwebs.aadharindia.home.tasks.collection.CollectionTaskDetailsActivity;
import com.tailwebs.aadharindia.member.GroupMemberListingResponseModel;
import com.tailwebs.aadharindia.member.MyApplicantDividerItemDecoration;
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

public class GroupMembersOverVIewActivity extends BaseActivity implements View.OnClickListener {


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

    @BindView(R.id.member_listing_recycler_view)
    RecyclerView memberListingRecyclerView;


    static GroupMembersOverVIewActivity instance;


    String cityCenterID=null,groupID=null,passcode=null;
    private FirebaseAnalytics mFirebaseAnalytics;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members_over_view);
        ButterKnife.bind(this);
        instance = this;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText(GlobalValue.taskName);
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Collection - Group Members", null);

//        setValuesFromBundle(getIntent().getExtras());

        cityCenterID =  GlobalValue.cityCenterId;
        groupID = GlobalValue.groupId;


        init();



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
    }


    private void getTask(String task_id) {

        showProgressDialog(GroupMembersOverVIewActivity.this);
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
                            NetworkUtils.handleErrorsForAPICalls(GroupMembersOverVIewActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(GroupMembersOverVIewActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<TaskResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(GroupMembersOverVIewActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponseForTask(TaskModel taskModel) {

        statusTv.setText("Group ID : "+taskModel.getGroupModel().getGroup_id());
        cityTv.setText(taskModel.getCity().getLong_name()+" - "+taskModel.getName());
        taskTv.setText(taskModel.getGroupModel().getApplicantCountModel().getDisbursed()+" Members");
        dateTv.setText(taskModel.getCreated_at_in_format());


        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(GroupMembersOverVIewActivity.this);
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
            Call<GroupMemberListingResponseModel> call = apiService.getMembersInGroupCollection(groupID
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
                            NetworkUtils.handleErrorsForAPICalls(GroupMembersOverVIewActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(GroupMembersOverVIewActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<GroupMemberListingResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(GroupMembersOverVIewActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void loadListView(GroupMemberListingResponseModel body) {

        try {
            JSONObject jsonObject =  new JSONObject(new Gson().toJson(body));

            if(jsonObject.getJSONObject("group").has("loan_takers")){

                memberListingRecyclerView.setVisibility(View.VISIBLE);

                // Add Adapter
                CollectionGroupMembersOverviewListingRecyclerAdapter adapter = new CollectionGroupMembersOverviewListingRecyclerAdapter();
                adapter.setData(body.getGroupMemberListingModel().getLoanTakerModels(), false);
                memberListingRecyclerView.setItemAnimator(new DefaultItemAnimator());
                memberListingRecyclerView.addItemDecoration(new MyApplicantDividerItemDecoration(GroupMembersOverVIewActivity.this, DividerItemDecoration.VERTICAL, 16));
                memberListingRecyclerView.setAdapter(adapter);
            }else{

                memberListingRecyclerView.setVisibility(View.GONE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


    public static GroupMembersOverVIewActivity getInstance() {
        return instance;
    }

    private void setValuesFromBundle(Bundle extras) {



        if(extras.getString("group_id")!=null){
            statusTv.setText("Group ID : "+extras.getString("group_id"));
        }


        if(extras.getString("task")!=null){
            cityTv.setText(extras.getString("task"));
        }


        if(extras.getString("members_count")!=null){
            taskTv.setText(extras.getString("members_count")+" Members");
        }

        if(extras.getString("date")!=null){
            dateTv.setText(extras.getString("date"));
        }

        if(extras.getString("by")!=null){
            byTv.setText(extras.getString("by"));
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
