package com.tailwebs.aadharindia.home.tasks.collection;

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
import com.tailwebs.aadharindia.home.models.PaymentTypeResponseModel;
import com.tailwebs.aadharindia.home.models.TaskModel;
import com.tailwebs.aadharindia.home.models.TaskResponseModel;
import com.tailwebs.aadharindia.home.tasks.collection.group.GroupOverViewActivity;
import com.tailwebs.aadharindia.home.tasks.collection.member.GroupMembersOverVIewActivity;
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

public class CollectionTaskDetailsActivity extends BaseActivity implements View.OnClickListener {


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

    @BindView(R.id.total_collection_tv)
    TextView totalCollectionTv;

    @BindView(R.id.amount_collected_tv)
    TextView amountCollectedTv;

    @BindView(R.id.pending_amount_collected_tv)
    TextView pendingAmountCollectedTv;

    @BindView(R.id.group_collection_button)
    Button groupCollectionButton;

    @BindView(R.id.view_members_button)
            Button viewMembersButton;

    //choose value from intent;
    String loanTakerID=null,buttonValue=null,groupID=null,groupLeader=null,pending_amount=null;

    TextView addEditTv,negativeCompleteTv,completeTv,cancelTv;

    String notes=null,groupIDVal=null;

    static CollectionTaskDetailsActivity instance;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_task_details);
        ButterKnife.bind(this);

        instance=this;

        loanTakerID = GlobalValue.loanTakerId;
        groupID =  GlobalValue.groupId;


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Collection Task", null);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Task Details");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
        rightActionBarButton.setVisibility(View.VISIBLE);
        rightActionBarButton.setOnClickListener(this);

        groupCollectionButton.setOnClickListener(this);
        viewMembersButton.setOnClickListener(this);

        init();


    }

    public void init(){
        if(getIntent().hasExtra("task_id")) {
            if (NetworkUtils.haveNetworkConnection(this)) {

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

    public static CollectionTaskDetailsActivity getInstance() {
        return instance;
    }

    private void getPaymentTypes() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<PaymentTypeResponseModel> call = apiService.getPaymentTypes(groupID);
            call.enqueue(new Callback<PaymentTypeResponseModel>() {
                @Override
                public void onResponse(Call<PaymentTypeResponseModel> call, final Response<PaymentTypeResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            GlobalValue.paymentTypeModelArrayList = response.body().getPaymentTypeModel();




                        } else {
                            NetworkUtils.handleErrorsForAPICalls(CollectionTaskDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(CollectionTaskDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<PaymentTypeResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(CollectionTaskDetailsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }


    private void getTask(String task_id) {

        showProgressDialog(CollectionTaskDetailsActivity.this);
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
                            NetworkUtils.handleErrorsForAPICalls(CollectionTaskDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(CollectionTaskDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<TaskResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(CollectionTaskDetailsActivity.this, null, null);
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
        groupIDVal = taskModel.getGroupModel().getGroup_id();
//        byTv.setText(item.getName()+" - "+item.getTask_number());
        dateTv.setText(taskModel.getCreated_at_in_format());

        GlobalValue.cityCenterId = taskModel.getGroupModel().getCity_center_id();
        GlobalValue.groupId = taskModel.getGroupModel().getId();

        membersCountTv.setText(taskModel.getGroupModel().getApplicantCountModel().getDisbursed());
        totalCollectionTv.setText(taskModel.getGroupModel().getTotal_collection_in_format());
        amountCollectedTv.setText(taskModel.getGroupModel().getAmount_collected_in_format());
        pendingAmountCollectedTv.setText(taskModel.getGroupModel().getPending_amount_in_format());
        pending_amount = taskModel.getGroupModel().getPending_amount();
        groupLeader=taskModel.getGroupModel().getGroup_leader_name();

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(CollectionTaskDetailsActivity.this);
            getPaymentTypes();

        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.group_collection_button:
                goToGroupCollectionPage();

                break;

            case R.id.view_members_button:
                goToViewMembersPage();
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

                Intent intent = new Intent(CollectionTaskDetailsActivity.this,AddEditNotesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("notes",notes);
                bundle.putString("activity","collection");
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }


    private void goToViewMembersPage() {

        Intent intent = new Intent(CollectionTaskDetailsActivity.this,GroupMembersOverVIewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("city",cityTv.getText().toString().trim());
        GlobalValue.taskName = cityTv.getText().toString().trim();
        bundle.putString("task",taskTv.getText().toString().trim());
        bundle.putString("status",statusTv.getText().toString().trim());
        bundle.putString("group_id",groupIDVal);
        bundle.putString("by",byTv.getText().toString().trim());
        bundle.putString("date",dateTv.getText().toString().trim());
        bundle.putString("members_count",membersCountTv.getText().toString().trim());
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void goToGroupCollectionPage() {

        Intent intent = new Intent(CollectionTaskDetailsActivity.this,GroupOverViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("city",cityTv.getText().toString().trim());
        GlobalValue.taskName = cityTv.getText().toString().trim();
        bundle.putString("task",taskTv.getText().toString().trim());
        bundle.putString("group_leader",groupLeader);
        bundle.putString("group_id",groupIDVal);
        bundle.putString("pending_amount",pending_amount);
        bundle.putString("pending_amount_format",pendingAmountCollectedTv.getText().toString().trim());
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
