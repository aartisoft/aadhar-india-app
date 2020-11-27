package com.tailwebs.aadharindia.home.tasks.creategroup;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.center.CenterConfirmationActivity;
import com.tailwebs.aadharindia.center.CreateCenterStartActivity;
import com.tailwebs.aadharindia.center.searchinmap.SearchVillageActivity;
import com.tailwebs.aadharindia.center.searchinmap.models.GroupResponseModel;
import com.tailwebs.aadharindia.home.AddEditNotesActivity;
import com.tailwebs.aadharindia.home.dashboard.TaskDashboardActivity;
import com.tailwebs.aadharindia.home.models.TaskModel;
import com.tailwebs.aadharindia.home.models.TaskResponseModel;
import com.tailwebs.aadharindia.member.GroupMemberListingActivity;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.models.CalculateEMIResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeadersForLocation;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.GpsTracker;
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

public class CreateGroupTaskDetailsActivity extends BaseActivity implements View.OnClickListener {

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

    @BindView(R.id.select_center_layout)
    LinearLayout selectCenterLayout;

    @BindView(R.id.select_center_tv)
    TextView selectCenterTv;

    @BindView(R.id.center_person_tv)
    TextView centerPersonTV;


    @BindView(R.id.center_person_address_tv)
    TextView centerAddressTV;

    @BindView(R.id.create_group_button)
    Button createGroupButton;


    @BindView(R.id.member_count_layout)
    LinearLayout memberCountLayout;


    @BindView(R.id.members_count_tv)
    TextView membersCountTv;

    @BindView(R.id.center_detail_tv)
    TextView centerDetailTv;

    @BindView(R.id.arrow_image_view)
    ImageView arrowImageView;

    private Dialog dialog;

    TextInputLayout reasonLayout;
    TextInputEditText reasonET;


    private GpsTracker gpsTracker;
    String currentLat=null,currentLong=null;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int MY_CAMERA_REQUEST_CODE = 100;


    String membersCount=null,fullAdrress=null,reason=null,taskId=null;

    private FirebaseAnalytics mFirebaseAnalytics;

    boolean isGroupAttached=false;


    TextView addEditTv,negativeCompleteTv,completeTv,cancelTv;
    String notes=null;

    static CreateGroupTaskDetailsActivity instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_task_details);
        ButterKnife.bind(this);
        instance=this;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Group Task", null);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Task Details");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
        rightActionBarButton.setVisibility(View.VISIBLE);
        rightActionBarButton.setOnClickListener(this);

        selectCenterLayout.setOnClickListener(this);
        createGroupButton.setOnClickListener(this);


        //show current Lat and Long

        try {
            gpsTracker = new GpsTracker(CreateGroupTaskDetailsActivity.this);
            if (gpsTracker.canGetLocation()) {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                currentLat = (String.valueOf(latitude));
                currentLong = (String.valueOf(longitude));

                if(currentLat !=null && currentLong !=null) {

                    GlobalValue.latitude = currentLat;
                    GlobalValue.longitude = currentLong;
                }

                Log.i("Lat-Long", "" + currentLat + "-" + currentLong);

            } else {
                gpsTracker.showSettingsAlert();
            }
        }catch (Exception e){
            System.out.print("Caught the NullPointerException");
        }


        init();
    }


    public void init(){

        if(!GlobalValue.chooseCenterFromList){

//            getLocationPermission();

            try {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            isStoragePermissionGranted();


        }else{
            if(!GlobalValue.selectedCenter){

            }else{

                if(!GlobalValue.isGroupPresent){
                    selectCenterLayout.setEnabled(false);
                    selectCenterTv.setEnabled(false);
                    centerPersonTV.setVisibility(View.VISIBLE);
                    centerAddressTV.setVisibility(View.VISIBLE);
                    centerPersonTV.setText(GlobalValue.centerPerson);
                    centerAddressTV.setText(GlobalValue.centerAddress);
                    arrowImageView.setImageDrawable(getResources().getDrawable(R.drawable.green_tick));
//                    UiUtils.setProcessCompleted(CreateGroupTaskDetailsActivity.this,selectCenterTv);
                    //enable button
                    createGroupButton.setEnabled(true);
                    createGroupButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                }else{


                    selectCenterLayout.setEnabled(false);
                    selectCenterTv.setEnabled(false);
                    centerPersonTV.setVisibility(View.VISIBLE);
                    centerAddressTV.setVisibility(View.VISIBLE);
                    centerPersonTV.setText(GlobalValue.centerPerson);
                    centerAddressTV.setText(GlobalValue.centerAddress);
                    arrowImageView.setImageDrawable(getResources().getDrawable(R.drawable.green_tick));
//                    UiUtils.setProcessCompleted(CreateGroupTaskDetailsActivity.this,selectCenterTv);
                    //enable button
                    createGroupButton.setEnabled(true);
                    createGroupButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));


                    GlobalValue.cityCenterId = getIntent().getStringExtra("city_center_id");
                    GlobalValue.groupId =getIntent().getStringExtra("group_id");


                    createGroupButton.setText("View Members");


                    //members count layout
                    memberCountLayout.setVisibility(View.VISIBLE);
                    membersCount = getIntent().getStringExtra("member_count");
                    fullAdrress = getIntent().getStringExtra("full_address");

                    membersCountTv.setText(membersCount);
                    centerDetailTv.setText(fullAdrress);
                }

            }
        }





        if(getIntent().hasExtra("task_id")) {
            if (NetworkUtils.haveNetworkConnection(this)) {

                getTask(getIntent().getStringExtra("task_id"));
            } else {
                UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            }

        }

        taskId = GlobalValue.taskId;


    }



    public static CreateGroupTaskDetailsActivity getInstance() {
        return instance;
    }

    private void getTask(String task_id) {

        showProgressDialog(CreateGroupTaskDetailsActivity.this);
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
                            NetworkUtils.handleErrorsForAPICalls(CreateGroupTaskDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(CreateGroupTaskDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<TaskResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(CreateGroupTaskDetailsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }



    private void createGroupAPICall() {

        showProgressDialog(CreateGroupTaskDetailsActivity.this);
        try {
            ApiInterface apiService;
            if(currentLat !=null && currentLong !=null) {
                apiService = RetrofitClientWithHeadersForLocation.getClient().create(ApiInterface.class);
            }else{
                apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            }
            Call<GroupResponseModel> call = apiService.createGroup(GlobalValue.city_id,GlobalValue.taskId);

            call.enqueue(new Callback<GroupResponseModel>() {
                @Override
                public void onResponse(Call<GroupResponseModel> call, Response<GroupResponseModel> response) {
                    hideProgressDialog();

                    if (response.isSuccessful()) {

                        if (response.body().getSuccess()) {




                            Bundle params = new Bundle();
                            params.putString("center_id", response.body().getGroupModel().getCity_center_id());
                            params.putString("group_id", response.body().getGroupModel().getGroup_id());
                            mFirebaseAnalytics.logEvent("group", params);
                            GlobalValue.cityCenterId = response.body().getGroupModel().getCity_center_id();
                            GlobalValue.groupId = response.body().getGroupModel().getId();
                            Log.d("Shahana","Group id"+response.body().getGroupModel().getId());

                            createGroupButton.setText("View Members");


                            //members count layout
                            memberCountLayout.setVisibility(View.VISIBLE);
                            membersCount = response.body().getGroupModel().getApplicantCountModel().getAll();
                            fullAdrress = response.body().getGroupModel().getCenterModel().getCenterAddressModel().getFull_address();

                            membersCountTv.setText(membersCount);
                            centerDetailTv.setText(fullAdrress);
                            Toast.makeText(CreateGroupTaskDetailsActivity.this, response.body().getNotice(), Toast.LENGTH_SHORT).show();
//                            goToViewMembersTaskDetails();
                        } else {
                            NetworkUtils.handleErrorsForAPICalls(CreateGroupTaskDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }

                    } else {
                        NetworkUtils.handleErrorsForAPICalls(CreateGroupTaskDetailsActivity.this, response.body().getErrors(), response.body().getNotice());

                    }
                }

                @Override
                public void onFailure(Call<GroupResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(CreateGroupTaskDetailsActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void goToViewMembersTaskDetails() {


        Intent intent = new Intent(CreateGroupTaskDetailsActivity.this,CreateGroupViewMembersActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("city",cityTv.getText().toString().trim());
//                    bundle.putString("distance",distanceTv.getText().toString().trim());
        bundle.putString("task",taskTv.getText().toString().trim());
        bundle.putString("status",statusTv.getText().toString().trim());
        bundle.putString("by",byTv.getText().toString().trim());
        bundle.putString("date",dateTv.getText().toString().trim());

        bundle.putString("members_count",membersCount);
        bundle.putString("full_address",fullAdrress);
        intent.putExtras(bundle);
        startActivity(intent);



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


        if(!GlobalValue.selectedCenter) {

            if (taskModel.getGroupModel() != null) {
                //enable button
                createGroupButton.setText("View Members");
                createGroupButton.setEnabled(true);
                createGroupButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));


                selectCenterLayout.setEnabled(false);
                selectCenterTv.setEnabled(false);
                centerPersonTV.setVisibility(View.VISIBLE);
                centerAddressTV.setVisibility(View.VISIBLE);
                centerPersonTV.setText(taskModel.getGroupModel().getCenterModel().getCenter_id() + "/" +
                        taskModel.getGroupModel().getCenterModel().getContactPersonModel().getName());
                centerAddressTV.setText(taskModel.getGroupModel().getCenterModel().getCenterAddressModel().getFull_address());

                membersCount = taskModel.getGroupModel().getApplicantCountModel().getAll();
                fullAdrress = taskModel.getGroupModel().getCenterModel().getCenterAddressModel().getFull_address();


                //members count layout
                memberCountLayout.setVisibility(View.VISIBLE);
                membersCountTv.setText(membersCount);
                centerDetailTv.setText(fullAdrress);
                GlobalValue.cityCenterId = taskModel.getGroupModel().getCity_center_id();
                GlobalValue.groupId = taskModel.getGroupModel().getId();

//                GlobalValue.placeId = taskModel.getGroupModel().getCenterModel().getCenterAddressModel().getGoogle_place_id();
//                GlobalValue.placeName = taskModel.getGroupModel().getCenterModel().getCenterAddressModel().getCity_name();

                arrowImageView.setImageDrawable(getResources().getDrawable(R.drawable.green_tick));
//                UiUtils.setProcessCompleted(CreateGroupTaskDetailsActivity.this, selectCenterTv);
                isGroupAttached = true;

            } else {
                isGroupAttached =false;
                UiUtils.setProcessActivated(CreateGroupTaskDetailsActivity.this, selectCenterTv);
                centerPersonTV.setVisibility(View.GONE);
                centerAddressTV.setVisibility(View.GONE);
                selectCenterLayout.setEnabled(true);
                selectCenterTv.setEnabled(true);

                //disable button
                createGroupButton.setEnabled(false);
                createGroupButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));
            }
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

            case R.id.select_center_layout:
                startActivity(new Intent(CreateGroupTaskDetailsActivity.this,SearchVillageActivity.class));
                break;


            case R.id.create_group_button:

                if(createGroupButton.getText().toString().trim().equalsIgnoreCase("View Members")){

                    goToMembersListingPage();

                }else{

                    if (NetworkUtils.haveNetworkConnection(CreateGroupTaskDetailsActivity.this)) {

                        createGroupAPICall();
                    } else {
                        UiUtils.showAlertDialogWithOKButton(CreateGroupTaskDetailsActivity.this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                finish();
                            }
                        });
                    }
                }


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
                Intent intent = new Intent(CreateGroupTaskDetailsActivity.this,AddEditNotesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("notes",notes);
                bundle.putString("activity","group");
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showCancelPopup();

            }
        });
    }

    private void showCancelPopup() {
        View view = getLayoutInflater().inflate(R.layout.custom_message_yes_no_dialog, null);
        TextView messageTV =(TextView)view.findViewById(R.id.message_tv);
        Button yesButton =(Button)view.findViewById(R.id.yes_button);
        Button noButton =(Button)view.findViewById(R.id.no_button);
        messageTV.setText("Are you sure you want to cancel this task?");
        yesButton.setText(getResources().getString(R.string.hint_yes));
        noButton.setText(getResources().getString(R.string.hint_no));
        noButton.setTextAppearance(CreateGroupTaskDetailsActivity.this,R.style.MyBluePopUpButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateGroupTaskDetailsActivity.this);
        builder.setCancelable(false)
                .setTitle("CANCEL")
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
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateGroupTaskDetailsActivity.this);
        builder.setCancelable(false)
                .setTitle("Reason for Cancel ")
                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(CreateGroupTaskDetailsActivity.this, "yyy", Toast.LENGTH_SHORT).show();
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
                    boolean reasonStatus = UiUtils.checkValidation(CreateGroupTaskDetailsActivity.this, reasonET,reasonLayout,itemPassed);

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
            CreateGroupTaskDetailsActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    private class RejectListener implements View.OnClickListener {

        private Dialog dialog;

        public RejectListener(AlertDialog alertDialog) {
            this.dialog = alertDialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here

            if(UiUtils.checkValidation(CreateGroupTaskDetailsActivity.this, reasonET,reasonLayout,new ArrayList<String>())) {
                reason = reasonET.getText().toString().trim();
                this.dialog.dismiss();
                if(isGroupAttached){
                    callNegativeCancelGroupTaskAPI();
                }else{
                    callCancelGroupTaskAPI();
                }
//
            }
        }
    }

    private void callCancelGroupTaskAPI() {

        showProgressDialog(CreateGroupTaskDetailsActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);

            Call<TaskResponseModel> call = apiService.cancelGroupTask(taskId
                    ,reason);

            call.enqueue(new Callback<TaskResponseModel>() {
                @Override
                public void onResponse(Call<TaskResponseModel> call, Response<TaskResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Toast.makeText(CreateGroupTaskDetailsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            goToTaskDashboard();
                        } else {
                            hideProgressDialog();
                            Log.d("aadh onResponse", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(CreateGroupTaskDetailsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        hideProgressDialog();
                        Log.i("aadh Error", "--" + new Gson().toJson(response.body()));
                        NetworkUtils.handleErrorsCasesForAPICalls(CreateGroupTaskDetailsActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<TaskResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(CreateGroupTaskDetailsActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void goToTaskDashboard() {
        finish();
        startActivity(new Intent(CreateGroupTaskDetailsActivity.this,TaskDashboardActivity.class));
    }


    private void callNegativeCancelGroupTaskAPI() {
        showProgressDialog(CreateGroupTaskDetailsActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);

            Call<TaskResponseModel> call = apiService.negativeCancelGroupTask(taskId
                    ,reason);

            call.enqueue(new Callback<TaskResponseModel>() {
                @Override
                public void onResponse(Call<TaskResponseModel> call, Response<TaskResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Toast.makeText(CreateGroupTaskDetailsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            goToTaskDashboard();
                        } else {
                            hideProgressDialog();
                            Log.d("aadh onResponse", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(CreateGroupTaskDetailsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        hideProgressDialog();
                        Log.i("aadh Error", "--" + new Gson().toJson(response.body()));
                        NetworkUtils.handleErrorsCasesForAPICalls(CreateGroupTaskDetailsActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<TaskResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(CreateGroupTaskDetailsActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void goToMembersListingPage() {
        startActivity(new Intent(CreateGroupTaskDetailsActivity.this,GroupMemberListingActivity.class));
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


    private void getLocationPermission(){

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){


            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }



    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Aadhar","Permission is granted");
                return true;
            } else {

                Log.v("Aadhar","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Aadhar","Permission is granted");
            return true;
        }
    }
}
