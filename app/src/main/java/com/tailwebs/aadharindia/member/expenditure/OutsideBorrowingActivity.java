package com.tailwebs.aadharindia.member.expenditure;

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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
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
import com.tailwebs.aadharindia.center.CenterConfirmationActivity;
import com.tailwebs.aadharindia.member.GroupMemberListingActivity;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.MyApplicantDividerItemDecoration;
import com.tailwebs.aadharindia.member.coapplicant.CoApplicantFamilyActivity;
import com.tailwebs.aadharindia.member.declaration.DeclarationActivity;
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
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutsideBorrowingActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.loan_listing_recycler_view)
    RecyclerView loanListingRecyclerView;

    @BindView(R.id.continue_button)
    Button continueButton;


    @BindView(R.id.add_new_loan_button)
    Button addLoanMemberButton;


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    private ProgressDialog mProgressDialog;

    //choose value from intent;
    String loanTakerID;

    static OutsideBorrowingActivity instance;


    TextInputLayout bankNameLayout;
    TextInputEditText bankET;

    TextInputLayout amountLayout;
    TextInputEditText amountET;

    ArrayList<OutstandingLoanModel> outstandingLoanModelArrayList=null;


    boolean isValidAmount=false,isValidBank=false;
    private FirebaseAnalytics mFirebaseAnalytics;

    private GpsTracker gpsTracker;
    String currentLat=null,currentLong=null;

    int count=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_borrowing);
        ButterKnife.bind(this);

        instance=this;
        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Outside Borrowing");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
        addLoanMemberButton.setOnClickListener(this);
        loanTakerID =  GlobalValue.loanTakerId;

        //show current Lat and Long

        try {
            gpsTracker = new GpsTracker(OutsideBorrowingActivity.this);
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


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Outside Borrowing", null);

        continueButton.setOnClickListener(this);


        init();
    }

    public static OutsideBorrowingActivity getInstance() {
        return instance;
    }
    private void init() {


        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(OutsideBorrowingActivity.this);
            getLoanDatas();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

    }

    private void getLoanDatas() {


        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<OutstandingLoanResponseModel> call = apiService.getAllOutstandingBorrowings(loanTakerID
            );
            call.enqueue(new Callback<OutstandingLoanResponseModel>() {
                @Override
                public void onResponse(Call<OutstandingLoanResponseModel> call, final Response<OutstandingLoanResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            loadListView(response.body());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(OutsideBorrowingActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(OutsideBorrowingActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<OutstandingLoanResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(OutsideBorrowingActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }


    private void loadListView(OutstandingLoanResponseModel body) {

        try {
            JSONObject jsonObject =  new JSONObject(new Gson().toJson(body));

            if(jsonObject.has("outside_borrowings")){


                loanListingRecyclerView.setVisibility(View.VISIBLE);
                addLoanMemberButton.setVisibility(View.GONE);

                count = body.getOutstandingLoanModels().size();
//                // Add Adapter
                OutstandingLoanListingRecyclerAdapter adapter = new OutstandingLoanListingRecyclerAdapter(GlobalValue.placeName);
                outstandingLoanModelArrayList = body.getOutstandingLoanModels();
                adapter.setData(body.getOutstandingLoanModels(), false);
                loanListingRecyclerView.setItemAnimator(new DefaultItemAnimator());
                loanListingRecyclerView.addItemDecoration(new MyApplicantDividerItemDecoration(OutsideBorrowingActivity.this, DividerItemDecoration.VERTICAL, 16));
                loanListingRecyclerView.setAdapter(adapter);
            }else{

                loanListingRecyclerView.setVisibility(View.GONE);
                addLoanMemberButton.setVisibility(View.VISIBLE);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void updateTheStatusInExpenditurePage() {
        ExpenditureDetailsActivity.instance.init();
        MemberDetailActivity.getInstance().init();
    }

    private void goToDeclaration() {

        Bundle params = new Bundle();
        params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
        params.putString("status","applicant_outside_borrowing_added");
        params.putInt("outside_borrowing_count",count);
        mFirebaseAnalytics.logEvent("applicant_outside_borrowing", params);

        Intent intent = new Intent(OutsideBorrowingActivity.this,DeclarationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.add_new_loan_button:
                showLoanAddDialog();
                break;


            case R.id.continue_button:
                goToDeclaration();
                break;
        }
        

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

                case R.id.input_bank_name:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean bankStatus = UiUtils.checkValidation(OutsideBorrowingActivity.this, bankET,bankNameLayout,itemPassed);

                    if (bankStatus == false) {

                        isValidBank = false;

                    } else {

                        isValidBank = true;
                        bankNameLayout.setErrorEnabled(false);

                    }
                    break;

                case R.id.input_amount:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean amountStatus = UiUtils.checkValidation(OutsideBorrowingActivity.this, amountET,amountLayout,itemPassed);

                    if (amountStatus == false) {

                        isValidAmount = false;

                    } else {

                        isValidAmount = true;
                        amountLayout.setErrorEnabled(false);

                    }
                    break;
            }
        }

    }


    private void showLoanAddDialog(){
        // title, custom view, actions dialog


        View view = OutsideBorrowingActivity.getInstance().getLayoutInflater().inflate(R.layout.custom_loan_dialog, null);
        bankNameLayout=(TextInputLayout) view.findViewById(R.id.input_layout_bank_name);
        bankET=(TextInputEditText) view.findViewById(R.id.input_bank_name);

        amountLayout=(TextInputLayout) view.findViewById(R.id.input_layout_amount);
        amountET=(TextInputEditText) view.findViewById(R.id.input_amount);

        bankET.addTextChangedListener(new InputLayoutTextWatcher(bankET));
        amountET.addTextChangedListener(new InputLayoutTextWatcher(amountET));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setPositiveButton("ADD LOAN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(OutsideBorrowingActivity.this, "yyy", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("", null)
                .setView(view);


        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialog));

    }




    public void requestFocus(View view) {
        if (view.requestFocus())
            OutsideBorrowingActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    private class CustomListener implements View.OnClickListener {
        private final Dialog dialog;
        public CustomListener(AlertDialog alertDialog) {
            this.dialog = alertDialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here

            if((isValidAmount)&&(isValidBank)) {
                callAPIForSubmittingOutsideBorrowing(bankET.getText().toString(), amountET.getText().toString());

                dialog.dismiss();
            }else{
                UiUtils.checkValidation(OutsideBorrowingActivity.this, bankET, bankNameLayout, new ArrayList<String>());
                UiUtils.checkValidation(OutsideBorrowingActivity.this, amountET, amountLayout, new ArrayList<String>());
            }

        }
    }


    public void callAPIForSubmittingOutsideBorrowing(String bankName, String amount) {

        showProgressDialog(OutsideBorrowingActivity.this);
        try {
            Log.d("Aadhar onResponse", "--start" );
            RequestBody name_val = RequestBody.create(MediaType.parse("text/plain"),  bankName);
            RequestBody amount_val = RequestBody.create(MediaType.parse("text/plain"), amount);

            Log.d("Aadhar onResponse", "--end" );

            ApiInterface apiService;
            if(currentLat !=null && currentLong !=null) {
                apiService = RetrofitClientWithHeadersForLocation.getClient().create(ApiInterface.class);
            }else{
                apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            }
            Call<OutstandingLoanResponseModel> call;
            call = apiService.addOutsideBorrowing(
                    loanTakerID,
                    name_val,
                    amount_val
            );
            call.enqueue(new Callback<OutstandingLoanResponseModel>() {
                @Override
                public void onResponse(Call<OutstandingLoanResponseModel> call, Response<OutstandingLoanResponseModel> response) {
                    hideProgressDialog();
                    Log.d("Aadhar onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Aadhar onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Log.d("Aadhar onResponse1", "" +  GlobalValue.loanTakerId);
                            init();
                            updateTheStatusInExpenditurePage();
                            Toast.makeText(OutsideBorrowingActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Aadhar onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(OutsideBorrowingActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Aadhar onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(OutsideBorrowingActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<OutstandingLoanResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(OutsideBorrowingActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
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
