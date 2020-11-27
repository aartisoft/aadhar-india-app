package com.tailwebs.aadharindia.center;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.center.searchinmap.models.CityCenterResponseModel;
import com.tailwebs.aadharindia.center.searchinmap.models.GroupModel;
import com.tailwebs.aadharindia.home.tasks.creategroup.CreateGroupTaskDetailsActivity;
import com.tailwebs.aadharindia.member.applicant.MyLanguageDividerItemDecoration;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOfGroupsInCityCenterActivity extends BaseActivity implements View.OnClickListener,ListOfGroupsInCityAdapter.ListOfGroupsInCityAdapterListener{


    private ProgressDialog mProgressDialog;


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;


    private List<GroupModel> groupModels;



    String id;

    String content = null,content_id=null,content_group_id=null,content_city_id=null;


    @BindView(R.id.groups_recycler_view)
    RecyclerView groupsRecyclerView;


    @BindView(R.id.continue_button)
    Button continueButton;



    String membersCount=null,fullAdrress=null;

    ListOfGroupsInCityAdapter listOfGroupsInCityAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_groups_in_city_center);
        ButterKnife.bind(this);

        id=getIntent().getStringExtra("id");


        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Select Group");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);


        continueButton.setOnClickListener(this);




        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(ListOfGroupsInCityCenterActivity.this);
            getGroups();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
    }

    private void getGroups() {

        showProgressDialog(ListOfGroupsInCityCenterActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<CityCenterResponseModel> call = apiService.getCustomerByCityCenterId(id);
            call.enqueue(new Callback<CityCenterResponseModel>() {
                @Override
                public void onResponse(Call<CityCenterResponseModel> call, Response<CityCenterResponseModel> response) {
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {

                            loadListView(response.body());


                        } else {
                            NetworkUtils.handleErrorsForAPICalls(ListOfGroupsInCityCenterActivity.this, response.body().getErrors(), response.body().getNotice());
                        }

                    } else {
                        NetworkUtils.handleErrorsForAPICalls(ListOfGroupsInCityCenterActivity.this, response.body().getErrors(), response.body().getNotice());

                    }
                }

                @Override
                public void onFailure(Call<CityCenterResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(ListOfGroupsInCityCenterActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void loadListView(CityCenterResponseModel body) {

        try {
            JSONObject jsonObject =  new JSONObject(new Gson().toJson(body));

            if(jsonObject.getJSONObject("city_center").has("groups")){

                groupsRecyclerView.setVisibility(View.VISIBLE);
                continueButton.setVisibility(View.VISIBLE);
                // Add Adapter

                groupModels = body.getCenterModels().getGroupModelArrayList();
                listOfGroupsInCityAdapter = new ListOfGroupsInCityAdapter(ListOfGroupsInCityCenterActivity.this, groupModels, ListOfGroupsInCityCenterActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                groupsRecyclerView.setLayoutManager(mLayoutManager);
                groupsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                groupsRecyclerView.addItemDecoration(new MyLanguageDividerItemDecoration(ListOfGroupsInCityCenterActivity.this, DividerItemDecoration.VERTICAL, 16));
                groupsRecyclerView.setAdapter(listOfGroupsInCityAdapter);

            }else{

                Toast.makeText(this, "Nothing to display", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_button:
                GlobalValue.cityCenterId =content_city_id;
                GlobalValue.groupId = content_group_id;
                goToCenterStartPageAfterSelection();
                break;


            case R.id.back_button:
                onBackPressed();
                break;
        }
    }


    private void goToCenterStartPageAfterSelection() {
        GlobalValue.selectedCenter = true;
        GlobalValue.chooseCenterFromList = true;
        GlobalValue.isGroupPresent = true;
        GlobalValue.centerId = getIntent().getStringExtra("centerId");
        GlobalValue.centerAddress = getIntent().getStringExtra("centerAddress");
        GlobalValue.centerPerson = getIntent().getStringExtra("centerPerson");
        Intent intent = new Intent(ListOfGroupsInCityCenterActivity.this,CreateGroupTaskDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("task_id",  GlobalValue.taskId );
//        bundle.putString("member_count", membersCount );
//        bundle.putString("full_address", fullAdrress );
        bundle.putString("city_center_id", content_city_id );
        bundle.putString("group_id", content_group_id );
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onGroupSelected(GroupModel languageModel) {
        continueButton.setEnabled(true);
        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
        content = languageModel.getCity_center_id();
        content_id = languageModel.getId();
        content_city_id= languageModel.getCity_center_id();
        content_group_id=languageModel.getId();
//        membersCount = languageModel.getApplicantCountModel().getAll();
//        fullAdrress = languageModel.getCenterModel().getCenterAddressModel().getFull_address();


    }
}
