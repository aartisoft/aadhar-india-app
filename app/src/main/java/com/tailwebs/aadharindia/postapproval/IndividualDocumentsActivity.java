package com.tailwebs.aadharindia.postapproval;

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

public class IndividualDocumentsActivity extends BaseActivity implements View.OnClickListener {



    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    private ProgressDialog mProgressDialog;

    @BindView(R.id.member_listing_recycler_view)
    RecyclerView memberListingRecyclerView;

    static IndividualDocumentsActivity instance;
    String cityCenterID=null,groupID=null;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_documents);
        ButterKnife.bind(this);
        instance=this;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Individual Documents");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "PA Individual Documents", null);

        init();
    }



    public void init(){

        cityCenterID =  GlobalValue.cityCenterId;
        groupID = GlobalValue.groupId;

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(IndividualDocumentsActivity.this);
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
                            NetworkUtils.handleErrorsForAPICalls(IndividualDocumentsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(IndividualDocumentsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<GroupMemberListingResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(IndividualDocumentsActivity.this, null, null);
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

                IndividualMemberListingRecyclerAdapter adapter = new IndividualMemberListingRecyclerAdapter(GlobalValue.placeName);
                adapter.setData(body.getGroupMemberListingModel().getLoanTakerModels(), false);
                memberListingRecyclerView.setItemAnimator(new DefaultItemAnimator());
                memberListingRecyclerView.addItemDecoration(new MyApplicantDividerItemDecoration(IndividualDocumentsActivity.this, DividerItemDecoration.VERTICAL, 16));
                memberListingRecyclerView.setAdapter(adapter);
            }else{

                memberListingRecyclerView.setVisibility(View.GONE);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static IndividualDocumentsActivity getInstance() {
        return instance;
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
