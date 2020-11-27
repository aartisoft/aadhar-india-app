package com.tailwebs.aadharindia.member.cashincome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.MyApplicantDividerItemDecoration;
import com.tailwebs.aadharindia.member.cashincome.models.FamilyMemberListingResponseModel;
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

public class FamilyMembersListingActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.member_listing_recycler_view)
    RecyclerView memberListingRecyclerView;

    @BindView(R.id.continue_button)
    Button continueButton;


    @BindView(R.id.add_new_member_button)
    Button addNewMemberButton;


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    private ProgressDialog mProgressDialog;

    String cityCenterID=null,groupID=null;
    static FamilyMembersListingActivity instance;


    //choose value from intent;
    String loanTakerID;

    int countOfMembers=0;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_members_listing);
        ButterKnife.bind(this);
        instance=this;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Family Members & Income");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
        addNewMemberButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        loanTakerID =  GlobalValue.loanTakerId;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Family Members", null);

        init();

    }

    public static FamilyMembersListingActivity getInstance() {
        return instance;
    }

    public void init(){

        cityCenterID =  GlobalValue.cityCenterId;
        groupID = GlobalValue.groupId;

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(FamilyMembersListingActivity.this);
            getFamilyMembers();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

    }

    private void getFamilyMembers() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<FamilyMemberListingResponseModel> call = apiService.getAllFamilyGroups(loanTakerID
            );
            call.enqueue(new Callback<FamilyMemberListingResponseModel>() {
                @Override
                public void onResponse(Call<FamilyMemberListingResponseModel> call, final Response<FamilyMemberListingResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            loadListView(response.body());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(FamilyMembersListingActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(FamilyMembersListingActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<FamilyMemberListingResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(FamilyMembersListingActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void loadListView(FamilyMemberListingResponseModel body) {

        try {
            JSONObject jsonObject =  new JSONObject(new Gson().toJson(body));

            if(jsonObject.has("family_members")){

                if(body.getFamilyModelArrayList().size()>0){

                    countOfMembers = body.getFamilyModelArrayList().size();

                }

                if(body.getFamilyModelArrayList().size()>=2){
                    continueButton.setEnabled(true);
                    continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                }

                memberListingRecyclerView.setVisibility(View.VISIBLE);
                addNewMemberButton.setVisibility(View.GONE);
//                // Add Adapter
                FamilyMemberListingRecyclerAdapter adapter = new FamilyMemberListingRecyclerAdapter(GlobalValue.placeName);
                adapter.setData(body.getFamilyModelArrayList(), false);
                memberListingRecyclerView.setItemAnimator(new DefaultItemAnimator());
                memberListingRecyclerView.addItemDecoration(new MyApplicantDividerItemDecoration(FamilyMembersListingActivity.this, DividerItemDecoration.VERTICAL, 16));
                memberListingRecyclerView.setAdapter(adapter);
            }else{

                countOfMembers = 0;
                memberListingRecyclerView.setVisibility(View.GONE);
                addNewMemberButton.setVisibility(View.VISIBLE);

            }

            Bundle params = new Bundle();
            params.putString("status","applicant_member_information_created");
            params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
            params.putInt("applicant_members_count",countOfMembers);
            mFirebaseAnalytics.logEvent("applicant_member_information", params);

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

    public int  getMembersCount(){
        return countOfMembers;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.add_new_member_button:
                Intent intent = new Intent(FamilyMembersListingActivity.this,MemberInformationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("count",countOfMembers);
                intent.putExtras(bundle);
                startActivity(intent);
                break;


            case R.id.continue_button:
                startActivity(new Intent(FamilyMembersListingActivity.this,AlternateIncomeActivity.class));
                finish();
                break;
        }
    }
}
