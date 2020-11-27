package com.tailwebs.aadharindia.home.tasks.housevisit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
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
import com.tailwebs.aadharindia.member.AddNewMemberScanActivity;
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

public class HouseVisitGroupReviewActivity extends BaseActivity implements View.OnClickListener {


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

    @BindView(R.id.house_visit_listing_recycler_view)
    RecyclerView houseVisitListingRecyclerView;

    TextView addEditTv,negativeCompleteTv,completeTv,cancelTv;


    static HouseVisitGroupReviewActivity instance;


    String cityCenterID=null,groupID=null,passcode=null;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_visit_group_review);
        ButterKnife.bind(this);
        instance = this;


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "HV Group Review", null);


        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText(GlobalValue.taskName);
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        rightActionBarButton.setVisibility(View.VISIBLE);
        rightActionBarButton.setOnClickListener(this);


        init();

    }


    public static HouseVisitGroupReviewActivity getInstance() {
        return instance;
    }

    private void setValuesFromBundle(Bundle extras) {

        if(extras.getString("city")!=null){
            cityTv.setText(extras.getString("city"));
        }


        if(extras.getString("task")!=null){
            taskTv.setText(extras.getString("task"));
        }


        if(extras.getString("members_count")!=null){
            statusTv.setText(extras.getString("members_count")+" Members");
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

            case R.id.right_action_bar_button:
                showPopup();
                break;
        }

    }


    public void init(){

        cityCenterID =  GlobalValue.cityCenterId;
        groupID = GlobalValue.groupId;

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(HouseVisitGroupReviewActivity.this);
            getTask( GlobalValue.taskId);
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

        showProgressDialog(HouseVisitGroupReviewActivity.this);
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
                            NetworkUtils.handleErrorsForAPICalls(HouseVisitGroupReviewActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitGroupReviewActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<TaskResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitGroupReviewActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(TaskModel taskModel) {

//        cityTv.setText(taskModel.getCity().getLong_name());
//        taskTv.setText(taskModel.getName()+" - "+taskModel.getTask_number());


//        cityTv.setText(taskModel.getTask_type().getName());
        if(taskModel.getName().length()>0){
            cityTv.setText(taskModel.getCity().getLong_name()+" - "+taskModel.getName());
        }else{
            cityTv.setText(taskModel.getCity().getLong_name());
        }

        dateTv.setText(taskModel.getCreated_at_in_format());

        GlobalValue.cityCenterId = taskModel.getGroupModel().getCity_center_id();
        GlobalValue.groupId = taskModel.getGroupModel().getId();

        statusTv.setText("Group ID : "+taskModel.getGroupModel().getGroup_id());
        taskTv.setText(taskModel.getGroupModel().getApplicantCountModel().getAll()+" Members");

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(HouseVisitGroupReviewActivity.this);
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
                            NetworkUtils.handleErrorsForAPICalls(HouseVisitGroupReviewActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitGroupReviewActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<GroupMemberListingResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitGroupReviewActivity.this, null, null);
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

                houseVisitListingRecyclerView.setVisibility(View.VISIBLE);

                // Add Adapter
                HouseVisitMemberGroupReviewListingRecyclerAdapter adapter = new HouseVisitMemberGroupReviewListingRecyclerAdapter();
                adapter.setData(body.getGroupMemberListingModel().getLoanTakerModels(), false);
                houseVisitListingRecyclerView.setItemAnimator(new DefaultItemAnimator());
                houseVisitListingRecyclerView.addItemDecoration(new MyApplicantDividerItemDecoration(HouseVisitGroupReviewActivity.this, DividerItemDecoration.VERTICAL, 16));
                houseVisitListingRecyclerView.setAdapter(adapter);
            }else{

                houseVisitListingRecyclerView.setVisibility(View.GONE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
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


    private void showPopup() {

        View view = getLayoutInflater().inflate(R.layout.custom_task_edit_cancel_dialog, null);
        addEditTv = (TextView) view.findViewById(R.id.add_edit_tv);
        negativeCompleteTv = (TextView) view.findViewById(R.id.negative_complete_tv);
        completeTv = (TextView) view.findViewById(R.id.complete_tv);
        cancelTv = (TextView) view.findViewById(R.id.cancel_tv);

        negativeCompleteTv.setVisibility(View.GONE);
        completeTv.setVisibility(View.GONE);
        cancelTv.setVisibility(View.GONE);

        Drawable leftDrawable = this.getResources().getDrawable(R.drawable.add);
        Drawable[] drawables = addEditTv.getCompoundDrawables();
        addEditTv.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, drawables[1], drawables[2], drawables[3]);
        addEditTv.setText(getText(R.string.hint_create_new_customer));

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
                addNewCustomer();
            }
        });
    }


    private void addNewCustomer() {
        Intent intent = new Intent(HouseVisitGroupReviewActivity.getInstance(), AddNewMemberScanActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("place_name", "");
        intent.putExtras(bundle);
        HouseVisitGroupReviewActivity.getInstance().startActivity(intent);
    }
}
