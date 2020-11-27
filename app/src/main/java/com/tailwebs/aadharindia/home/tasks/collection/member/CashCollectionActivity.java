package com.tailwebs.aadharindia.home.tasks.collection.member;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.UiUtils;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CashCollectionActivity extends BaseActivity implements View.OnClickListener {

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


    @BindView(R.id.co_aadhaar_tv)
    TextView coAadhaarTv;

    @BindView(R.id.date_tv)
    TextView dateTv;

    @BindView(R.id.total_amount_tv)
    TextView totalAmountTv;

    @BindView(R.id.input_layout_cash_received)
    TextInputLayout cashRecievedLayout;

    @BindView(R.id.input_cash_received)
    TextInputEditText cashRecievedET;

    @BindView(R.id.collect_button)
    Button collectButton;

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

    @BindView(R.id.nonCashLayout)
    LinearLayout nonCashLayout;

    //choose value from intent;
    String loanTakerID=null,dobValue=null,groupID=null,modeOfPaymentID;

    String pendingAmount;

    int pickedDay = 0;
    int pickedMonth = 0;
    int pickedYear = 0;


    private FirebaseAnalytics mFirebaseAnalytics;

    private boolean isValidTransationID = false,isValidTransationDate=false,isValidCashReceived=false;

    DatePickerDialog picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_collection);
        ButterKnife.bind(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Cash Collection", null);

        hideSoftKeyboard();
        loanTakerID = GlobalValue.loanTakerId;
        groupID =  GlobalValue.groupId;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Group Overview");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        setValuesFromBundle(getIntent().getExtras());

        collectButton.setOnClickListener(this);
        transactionIDET.addTextChangedListener(new InputLayoutTextWatcher(transactionIDET));
        transactionDateET.addTextChangedListener(new InputLayoutTextWatcher(transactionDateET));
        cashRecievedET.addTextChangedListener(new InputLayoutTextWatcher(cashRecievedET));
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


        if(extras.getString("name")!=null){
            taskTv.setText(extras.getString("name"));
        }


        if(extras.getString("status")!=null){
            statusTv.setText(extras.getString("status"));
        }


        if(extras.getString("co")!=null){
            coAadhaarTv.setText(extras.getString("co"));
        }


        if(extras.getString("emi_remaining")!=null){
            dateTv.setText(extras.getString("emi_remaining"));
        }


        if(extras.getString("pending_amount_format")!=null){
            totalAmountTv.setText(extras.getString("pending_amount_format"));
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

            if((isValidCashReceived)){

                Double cash = Double.parseDouble(cashRecievedET.getText().toString().trim());
                Double pending_amount = Double.parseDouble(pendingAmount);


                if(cash>pending_amount){

                    Toast.makeText(this, "Please check the entered amount", Toast.LENGTH_SHORT).show();
                }else{

                                    goToConfirmPage();
                }





            }else{

                UiUtils.checkValidation(CashCollectionActivity.this, cashRecievedET, cashRecievedLayout, new ArrayList<String>());
            }


        }else{

            if((isValidTransationID) && (isValidTransationDate) && (isValidCashReceived)){


                Double cash = Double.parseDouble(cashRecievedET.getText().toString().trim());
                Double pending_amount = Double.parseDouble(String.valueOf(pendingAmount));

                if(cash>pending_amount){

                    Toast.makeText(this, "Please check the entered amount", Toast.LENGTH_SHORT).show();
                }else{

                                    goToConfirmPage();
                }



            }else{

                UiUtils.checkValidation(CashCollectionActivity.this, cashRecievedET, cashRecievedLayout, new ArrayList<String>());
                UiUtils.checkValidation(CashCollectionActivity.this, transactionIDET, transactionIDLayout, new ArrayList<String>());
                UiUtils.checkValidation(CashCollectionActivity.this, transactionDateET, transactionDateLayout, new ArrayList<String>());
            }

        }
    }

    private void goToConfirmPage() {
        Intent intent = new Intent(CashCollectionActivity.this,CashCollectionConfirmationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("city",cityTv.getText().toString().trim());
        bundle.putString("name",taskTv.getText().toString().trim());
        bundle.putString("status",statusTv.getText().toString().trim());
        bundle.putString("co",coAadhaarTv.getText().toString().trim());
        bundle.putString("emi_remaining",dateTv.getText().toString().trim());
        bundle.putString("mode_of_payment_id",String.valueOf(modeOfPaymentID));
        bundle.putString("mode_of_payment",modeOfPaymentTv.getText().toString().trim());
        bundle.putString("pending_amount",pendingAmount);
        bundle.putString("pending_amount_format",totalAmountTv.getText().toString().trim());
        bundle.putString("cash_entered",cashRecievedET.getText().toString().trim());
        bundle.putString("transaction_id",transactionIDET.getText().toString());
        bundle.putString("transaction_date",transactionDateET.getText().toString().trim());
        bundle.putString("transaction_date_format",dobValue);
        intent.putExtras(bundle);
        startActivity(intent);
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

                case R.id.input_transaction_id:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean transactionIDStatus = UiUtils.checkValidation(CashCollectionActivity.this, transactionIDET,transactionIDLayout,itemPassed);

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
                    boolean dateStatus = UiUtils.checkValidation(CashCollectionActivity.this, transactionDateET, transactionDateLayout, itemPassed);

                    if (dateStatus == false) {
                        isValidTransationDate = false;
                        requestFocus(transactionDateET);

                    } else {
                        isValidTransationDate=true;
                        transactionDateLayout.setErrorEnabled(false);
                    }

                    break;


                case R.id.input_cash_received:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean cashStatus = UiUtils.checkValidation(CashCollectionActivity.this, cashRecievedET, cashRecievedLayout, itemPassed);

                    if (cashStatus == false) {
                        isValidCashReceived = false;
                        requestFocus(cashRecievedET);

                    } else {
                        isValidCashReceived=true;
                        cashRecievedLayout.setErrorEnabled(false);
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
            CashCollectionActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
        picker = new DatePickerDialog(CashCollectionActivity.this,R.style.CustomDatePickerDialog,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        String date = dayOfMonth+" - "+(monthOfYear+1)+" - "+year;
                        dobValue = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                        transactionDateET.setText(date);

                        pickedDay = dayOfMonth;
                        pickedMonth = monthOfYear+1;
                        pickedYear = year;

                        isValidTransationDate = true;


                    }
                }, year, month, day);

        picker.show();
    }

}
