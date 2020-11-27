package com.tailwebs.aadharindia.home.tasks.collection.group;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.mukesh.OtpView;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.home.NewGroupTaskActivity;
import com.tailwebs.aadharindia.home.dashboard.TaskDashboardActivity;
import com.tailwebs.aadharindia.home.models.GroupPaymentResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeadersForLocation;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.GpsTracker;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupCollectionActivity extends BaseActivity implements View.OnClickListener {

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

    @BindView(R.id.members_count_tv)
    TextView membersCountTv;

    @BindView(R.id.group_leader_tv)
    TextView groupLeaderTv;

    @BindView(R.id.pending_amount_collected_tv)
    TextView pendingAmountCollectedTv;

    @BindView(R.id.mode_of_payment_tv)
    TextView modeOfPaymentTv;

    @BindView(R.id.input_layout_transaction_id)
    TextInputLayout transactionIDLayout;

    @BindView(R.id.input_transaction_id)
    TextInputEditText transactionIDET;

    @BindView(R.id.input_layout_transaction_date)
    TextInputLayout transactionDateLayout;

    @BindView(R.id.input_transaction_date)
    TextInputEditText transactionDateET;

    @BindView(R.id.collect_button)
    Button collectButton;

    @BindView(R.id.nonCashLayout)
    LinearLayout nonCashLayout;


    //password

    OtpView passwordOTPView;
    TextView passwordErrorTV;

    Button yesButton, noButton;

    private Dialog dialog;
    String passcode =null;

    DatePickerDialog picker;


    //choose value from intent;
    String loanTakerID=null,dobValue=null,groupID=null,modeOfPaymentID,pendingAmount;

    private GpsTracker gpsTracker;
    String currentLat=null,currentLong=null;

    private FirebaseAnalytics mFirebaseAnalytics;


    int pickedDay = 0;
    int pickedMonth = 0;
    int pickedYear = 0;


    private boolean isValidTransationID = false,isValidTransationDate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_collection);
        ButterKnife.bind(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Group Collection", null);


        hideSoftKeyboard();
        loanTakerID = GlobalValue.loanTakerId;
        groupID =  GlobalValue.groupId;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Group Overview");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        //show current Lat and Long

        try {
            gpsTracker = new GpsTracker(GroupCollectionActivity.this);
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



        setValuesFromBundle(getIntent().getExtras());

        collectButton.setOnClickListener(this);
        transactionIDET.addTextChangedListener(new InputLayoutTextWatcher(transactionIDET));
        transactionDateET.addTextChangedListener(new InputLayoutTextWatcher(transactionDateET));
        transactionDateET.setInputType(InputType.TYPE_NULL);
        transactionDateET.setOnClickListener(this);


        transactionIDET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_NEXT){

                    //show calendar popup
                    hideSoftKeyboard();
                    pickUpFromCalendar();
                } else if(actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    hideSoftKeyboard();

                }

                return false;
            }
        });

    }

    private void setValuesFromBundle(Bundle extras) {

        if(extras.getString("city")!=null){
            cityTv.setText(extras.getString("city"));
        }


        if(extras.getString("group_leader")!=null){
            groupLeaderTv.setText("Group Leader : "+extras.getString("group_leader"));
        }


        if(extras.getString("members_count")!=null){
            membersCountTv.setText(extras.getString("members_count"));
        }


        if(extras.getString("pending_amount_format")!=null){
            pendingAmountCollectedTv.setText(extras.getString("pending_amount_format"));
            pendingAmount = extras.getString("pending_amount");
        }


        if(extras.getString("mode_of_payment")!=null){
            modeOfPaymentTv.setText(extras.getString("mode_of_payment"));
            modeOfPaymentID = extras.getString("mode_of_payment_id");
        }

        if(extras.getString("mode_of_payment").equalsIgnoreCase("Cash")){
            nonCashLayout.setVisibility(View.GONE);
        }else{
            nonCashLayout.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.collect_button:
                checkValidation();
                break;

            case R.id.input_transaction_date:
                hideSoftKeyboard();
                pickUpFromCalendar();
                break;

        }

    }

    private void checkValidation() {

        if(getIntent().getExtras().getString("mode_of_payment").equalsIgnoreCase("Cash")){

            showPasswordPopup();


        }else{

            if((isValidTransationID) && (isValidTransationDate)){

                goToConfirmPage();

            }else{

                UiUtils.checkValidation(GroupCollectionActivity.this, transactionIDET, transactionIDLayout, new ArrayList<String>());
                UiUtils.checkValidation(GroupCollectionActivity.this, transactionDateET, transactionDateLayout, new ArrayList<String>());
            }

        }
    }

    private void goToConfirmPage() {
        Intent intent = new Intent(GroupCollectionActivity.this,GroupCollectionConfirmActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("city",cityTv.getText().toString().trim());
        bundle.putString("group_leader",groupLeaderTv.getText().toString().trim());
        bundle.putString("pending_amount_format",pendingAmountCollectedTv.getText().toString().trim());
        bundle.putString("pending_amount",pendingAmount);
        bundle.putString("members_count",membersCountTv.getText().toString().trim());
        bundle.putString("mode_of_payment_id",String.valueOf(modeOfPaymentID));
        bundle.putString("mode_of_payment",modeOfPaymentTv.getText().toString().trim());
        bundle.putString("transaction_id",transactionIDET.getText().toString());
        bundle.putString("transaction_date",transactionDateET.getText().toString().trim());
        bundle.putString("transaction_date_format",dobValue);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void showPasswordPopup() {
        View view = getLayoutInflater().inflate(R.layout.custom_collection_password_dialog, null);
        TextView messageTv = (TextView)view.findViewById(R.id.message_tv);
        passwordOTPView=(OtpView) view.findViewById(R.id.password_otp_view);
        passwordErrorTV =(TextView)view.findViewById(R.id.password_error_tv);
        RelativeLayout buttonLayout = (RelativeLayout)view.findViewById(R.id.button_layout);
        buttonLayout.setVisibility(View.VISIBLE);
        yesButton =(Button)view.findViewById(R.id.yes_button);
        noButton =(Button)view.findViewById(R.id.no_button);
        yesButton.setEnabled(true);
        yesButton.setTextAppearance(GroupCollectionActivity.this,R.style.MyBluePopUpButton);
        yesButton.setText("CONFIRM");
        noButton.setText("CANCEL");
        messageTv.setText("Confirm cash of "+pendingAmountCollectedTv.getText().toString().trim()+" received");
        passwordOTPView.addTextChangedListener(new InputLayoutTextWatcher(passwordOTPView));
        AlertDialog.Builder builder = new AlertDialog.Builder(GroupCollectionActivity.this);
        builder.setCancelable(false)
                .setTitle("Enter Passcode")
                .setPositiveButton("", null)
                .setNeutralButton("", null)
                .setView(view);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        dialog = alertDialog;
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(GroupCollectionActivity.this);
                yesButton.setEnabled(false);
                yesButton.setTextAppearance(GroupCollectionActivity.this,R.style.MyDisabledPopUpButton);
                passcode = passwordOTPView.getText().toString();
                if(UiUtils.checkValidationForOTP(GroupCollectionActivity.this, passwordOTPView,passwordErrorTV)){
                    if (NetworkUtils.haveNetworkConnection(GroupCollectionActivity.this)) {
                        sendGroupPaymentAPI();

                    } else {
                        hideProgressDialog();
                        UiUtils.showAlertDialogWithOKButton(GroupCollectionActivity.this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                yesButton.setEnabled(true);
                                yesButton.setTextAppearance(GroupCollectionActivity.this,R.style.MyBluePopUpButton);
                            }
                        });
                    }

                }else{
                    hideProgressDialog();
                }
            }
        });


        noButton.setOnClickListener(new CancelCustomListener(alertDialog));
    }

    private void sendGroupPaymentAPI() {


        collectButton.setEnabled(false);
        collectButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(GroupCollectionActivity.this);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("user_passcode", String.valueOf(passcode));
        builder.addFormDataPart("payment[amount]", pendingAmount);
//        builder.addFormDataPart("payment[amount]", pendingAmountCollectedTv.getText().toString().trim().replaceAll("[^\\d.]", ""));
        builder.addFormDataPart("payment[payment_type_id]", modeOfPaymentID);
        if(transactionDateET.getText().toString().length()>0){
            builder.addFormDataPart("payment[transaction_date]", transactionDateET.getText().toString().trim());
        }

        if(transactionIDET.getText().toString().length()>0) {
            builder.addFormDataPart("payment[transaction_id]",transactionIDET.getText().toString().trim());
        }

        RequestBody finalRequestBody = builder.build();
        ApiInterface apiService;
        if(currentLat !=null && currentLong !=null) {
            apiService = RetrofitClientWithHeadersForLocation.getClient().create(ApiInterface.class);
        }else{
            apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
        }
        Call<GroupPaymentResponseModel> call = apiService.sendGroupPayment(groupID,finalRequestBody
        );
        call.enqueue(new Callback<GroupPaymentResponseModel>() {
            @Override
            public void onResponse(Call<GroupPaymentResponseModel> call, Response<GroupPaymentResponseModel> response) {
                hideProgressDialog();
                Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        //API Success is true
                        collectButton.setEnabled(true);
                        collectButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        Toast.makeText(GroupCollectionActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                       goToDashboard();
                    } else {
                        Log.d("Drools onResponse", "" + response.body().getErrors());
                        collectButton.setEnabled(true);
                        collectButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        NetworkUtils.handleErrorsCasesForAPICalls(GroupCollectionActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Drools onResponse", "" + response.body().getErrors());
                    collectButton.setEnabled(true);
                    collectButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(GroupCollectionActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<GroupPaymentResponseModel> call, Throwable t) {
                hideProgressDialog();
                collectButton.setEnabled(true);
                collectButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                NetworkUtils.handleErrorsForAPICalls(GroupCollectionActivity.this, null, null);
            }
        });
    }

    private void goToDashboard() {
        Intent intent = new Intent(GroupCollectionActivity.this, TaskDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private class CancelCustomListener implements View.OnClickListener {

        public CancelCustomListener(AlertDialog alertDialog) {
            dialog = alertDialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here
            dialog.dismiss();
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

                case R.id.password_otp_view:
                    itemPassed.clear();
                    itemPassed.add("otp");
                    boolean passwordStatus = UiUtils.checkValidationForOTP(GroupCollectionActivity.this, passwordOTPView,passwordErrorTV);

                    if (passwordStatus == false) {

                        requestFocus(passwordOTPView);
                    } else {

                        passwordOTPView.setLineColor(getResources().getColor(R.color.primaryColor));

                    }
                    break;


                case R.id.input_transaction_id:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean transactionIDStatus = UiUtils.checkValidation(GroupCollectionActivity.this, transactionIDET,transactionIDLayout,itemPassed);

                    if (transactionIDStatus == false) {
                        isValidTransationID = false;
                        requestFocus(transactionIDET);

                    } else {
                        isValidTransationID=true;
                        transactionIDLayout.setErrorEnabled(false);
                    }
                    break;

                case R.id.input_transaction_date:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean dateStatus = UiUtils.checkValidation(GroupCollectionActivity.this, transactionDateET, transactionDateLayout, itemPassed);

                    if (dateStatus == false) {
                        isValidTransationDate = false;
                        requestFocus(transactionDateET);

                    } else {
                        isValidTransationDate=true;
                        transactionDateLayout.setErrorEnabled(false);
                    }

                    break;
            }
        }

    }


    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public void requestFocus(View view) {
        if (view.requestFocus())
            GroupCollectionActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    private void pickUpFromCalendar() {

        final Calendar c = Calendar.getInstance();

        int year , month ,day ;

        if(pickedDay==0){

            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }else{
            day = pickedDay;
            month = pickedMonth-1;
            year = pickedYear;
        }




        //for calendar spinner
        picker = new DatePickerDialog(GroupCollectionActivity.this,R.style.CustomDatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        String date = dayOfMonth+" - "+(monthOfYear+1)+" - "+year;
                        dobValue = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                        transactionDateET.setText(date);
                        isValidTransationDate = true;

                        pickedDay = dayOfMonth;
                        pickedMonth = monthOfYear+1;
                        pickedYear = year;


                    }
                }, year, month, day);

        picker.show();
    }

}
