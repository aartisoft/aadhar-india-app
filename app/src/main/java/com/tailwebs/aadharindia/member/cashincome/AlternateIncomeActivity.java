package com.tailwebs.aadharindia.member.cashincome;

import android.app.Activity;
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
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.center.CreateNewCenterActivity;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.cashincome.models.AlternateIncomeResponseModel;
import com.tailwebs.aadharindia.member.cashincome.models.SourceIncomeModel;
import com.tailwebs.aadharindia.member.coapplicant.CoApplicantFamilyActivity;
import com.tailwebs.aadharindia.member.expenditure.ExpenditureDetailsActivity;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeadersForLocation;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.GpsTracker;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlternateIncomeActivity extends BaseActivity implements View.OnClickListener {


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    @BindView(R.id.yes_tv)
    TextView yesTv;


    @BindView(R.id.no_tv)
    TextView noTv;


    @BindView(R.id.duplicating_layout)
    LinearLayout duplicatingLayout;

    Boolean alternateIncomeValue;

    @BindView(R.id.add_alternate_income_tv)
    TextView addLayout;

    @BindView(R.id.continue_button)
    Button continueButton;



    private ProgressDialog mProgressDialog;

    //choose value from intent;
    String loanTakerID;

    boolean isValidAlternateIncome=false,isValidSource=false,isValidAmount=false;

    ArrayList<SourceIncomeModel>  sourceIncomeModels = null;

    private FirebaseAnalytics mFirebaseAnalytics;

    private GpsTracker gpsTracker;
    String currentLat=null,currentLong=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternate_income);
        ButterKnife.bind(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Alternate Income", null);


        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Alternate Income");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
        loanTakerID =  GlobalValue.loanTakerId;
        yesTv.setOnClickListener(this);
        noTv.setOnClickListener(this);
        addLayout.setOnClickListener(this);
        continueButton.setOnClickListener(this);

        //show current Lat and Long

        try {
            gpsTracker = new GpsTracker(AlternateIncomeActivity.this);
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

        chooseAlternateIncome("No");




    }


    private void chooseAlternateIncome(String m) {

        if(m.equalsIgnoreCase("Yes")){
            yesTv.setTextAppearance(getApplicationContext(),R.style.MyRadioSelected);
            noTv.setTextAppearance(getApplicationContext(),R.style.MyRadioNotSelected);
            yesTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_selected));
            noTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_not_selected));
            alternateIncomeValue=true;
            isValidAlternateIncome = true;

        }else{

            noTv.setTextAppearance(getApplicationContext(),R.style.MyRadioSelected);
            yesTv.setTextAppearance(getApplicationContext(),R.style.MyRadioNotSelected);
            noTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_selected));
            yesTv.setBackground(getResources().getDrawable(R.drawable.bordered_bg_radio_not_selected));
            alternateIncomeValue = false;
            isValidAlternateIncome=false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.yes_tv:

                chooseAlternateIncome("Yes");
                duplicatingLayout.setVisibility(View.VISIBLE);
                addLayout.setVisibility(View.VISIBLE);


                break;


            case R.id.no_tv:

                chooseAlternateIncome("No");
                duplicatingLayout.setVisibility(View.GONE);
                addLayout.setVisibility(View.GONE);

                break;


            case R.id.add_alternate_income_tv:
                isValidSource = false;isValidAmount=false;
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.duplicating_alternate_income_layout, null);
                // Add the new row before the add field button.
                duplicatingLayout.addView(rowView, duplicatingLayout.getChildCount() - 1);
                final TextInputEditText sourceEt = (TextInputEditText)rowView.findViewById(R.id.input_source);
                final TextInputEditText  amountEt = (TextInputEditText)rowView.findViewById(R.id.input_income_amount);
                final TextInputLayout inputLayout = (TextInputLayout)rowView.findViewById(R.id.input_layout_income_amount);
                final TextInputLayout inputSourceLayout = (TextInputLayout)rowView.findViewById(R.id.input_layout_source);

                sourceEt.addTextChangedListener(new TextWatcher() {
                    ArrayList<String> itemPassed = new ArrayList<String>();
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        itemPassed.clear();
                        itemPassed.add("");
                        boolean classStatus = UiUtils.checkValidation(AlternateIncomeActivity.this, sourceEt, inputSourceLayout, itemPassed);

                        if (classStatus == false) {

                            isValidSource=false;
                            requestFocus(sourceEt);

                        } else {
                            
                            isValidSource=true;

                            inputSourceLayout.setErrorEnabled(false);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        itemPassed.clear();
                        itemPassed.add("");
                        boolean classStatus = UiUtils.checkValidation(AlternateIncomeActivity.this, sourceEt, inputSourceLayout, itemPassed);

                        if (classStatus == false) {

                            isValidSource=false;
                            requestFocus(sourceEt);

                        } else {

                            isValidSource=true;

                            inputSourceLayout.setErrorEnabled(false);
                        }


                    }
                });

                amountEt.addTextChangedListener(new TextWatcher() {
                    ArrayList<String> itemPassed = new ArrayList<String>();
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        itemPassed.clear();
                        itemPassed.add("");
                        boolean classStatus = UiUtils.checkValidation(AlternateIncomeActivity.this, amountEt, inputLayout, itemPassed);

                        if (classStatus == false) {

                            isValidAmount=false;
                            requestFocus(amountEt);

                        } else {

                            isValidAmount=true;
                            inputLayout.setErrorEnabled(false);
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        itemPassed.clear();
                        itemPassed.add("");
                        boolean classStatus = UiUtils.checkValidation(AlternateIncomeActivity.this, amountEt, inputLayout, itemPassed);

                        if (classStatus == false) {

                            isValidAmount=false;
                            requestFocus(amountEt);

                        } else {
                            isValidAmount=true;
                            inputLayout.setErrorEnabled(false);
                        }
                    }
                });


//
//                myArray.set(duplicatingLayout.getChildCount()-1,inputSource);


                Log.i("AAdhar", "" + duplicatingLayout.getChildCount());


                break;


            case R.id.continue_button:

                sourceIncomeModels = new ArrayList<SourceIncomeModel>();

                SourceIncomeModel sourceIncomeModel = null;

                    for (int i = 0; i < duplicatingLayout.getChildCount() - 1; i++) {
                        View chidLayout = duplicatingLayout.getChildAt(i);
                        TextInputEditText inputSource = (TextInputEditText) chidLayout.findViewById(R.id.input_source);
                        TextInputEditText inputAmount = (TextInputEditText) chidLayout.findViewById(R.id.input_income_amount);
                         TextInputLayout inputALayout = (TextInputLayout)chidLayout.findViewById(R.id.input_layout_income_amount);
                         TextInputLayout inputSLayout = (TextInputLayout)chidLayout.findViewById(R.id.input_layout_source);
                        if (inputSource.getText().toString().length() >0 && inputAmount.getText().toString().length() >0) {
                            sourceIncomeModel = new SourceIncomeModel();
                            sourceIncomeModel.setIncome(inputAmount.getText().toString());
                            sourceIncomeModel.setSource(inputSource.getText().toString());
                            Log.i("AAdhar", "" + inputAmount.getText().toString() + inputSource.getText().toString());
                            sourceIncomeModels.add(sourceIncomeModel);
                        }else{
                            if (inputSource.getText().toString().trim().isEmpty()) {
                                inputSource.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                inputSLayout.setError(AlternateIncomeActivity.this.getString(R.string.field_required));
                                isValidSource=false;
                            }

                            if (inputAmount.getText().toString().trim().isEmpty()) {
                                inputAmount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                inputALayout.setError(AlternateIncomeActivity.this.getString(R.string.field_required));
                                isValidAmount=false;
                            }

                        }
                        Log.i("AAdhar", "" + sourceIncomeModels.size());

                    }


              
                    
                    if(isValidAlternateIncome){
                        if((isValidSource) && (isValidAmount) ){
                            submitAlternateIncome();

                        }else{

                            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        submitAlternateIncome();
                    }



                break;
        }
    }

    private void submitAlternateIncome() {
        callAPIForAlternateIncome();
    }

    private void callAPIForAlternateIncome() {

        continueButton.setEnabled(false);
        continueButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(AlternateIncomeActivity.this);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("alternate_income_flag", String.valueOf(isValidAlternateIncome));
        if(isValidAlternateIncome){

            for(int i=0;i<sourceIncomeModels.size();i++){

                Log.d("Aadhar onResponse", "" + "alternate_incomes["+i+"][source]");

                Log.d("Aadhar onResponse", "" + sourceIncomeModels.get(i).getSource()+sourceIncomeModels.get(i).getIncome());

                builder.addFormDataPart("alternate_incomes["+i+"][source]", sourceIncomeModels.get(i).getSource());
                builder.addFormDataPart("alternate_incomes["+i+"][income]", sourceIncomeModels.get(i).getIncome());
            }

        }

        RequestBody finalRequestBody = builder.build();
        ApiInterface apiService;
        if(currentLat !=null && currentLong !=null) {
            apiService = RetrofitClientWithHeadersForLocation.getClient().create(ApiInterface.class);
        }else{
            apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
        }
        Call<AlternateIncomeResponseModel> call = apiService.addAlternateIncome(loanTakerID,finalRequestBody
        );
        call.enqueue(new Callback<AlternateIncomeResponseModel>() {
            @Override
            public void onResponse(Call<AlternateIncomeResponseModel> call, Response<AlternateIncomeResponseModel> response) {
                hideProgressDialog();
                Log.d("Aadhar onResponse", "" + response.code() + "--" + response.message());
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        //API Success is true

                        continueButton.setEnabled(true);
                        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        Bundle params = new Bundle();
                        params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
                        params.putString("status","applicant_member_alternate_income_created");
                        mFirebaseAnalytics.logEvent("applicant_alternate_income", params);

                        goToExpenditurePage();
                        updateStatusInCashIncomePage();
                        Toast.makeText(AlternateIncomeActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("Aadhar onResponse", "" + response.body().getErrors());
                        continueButton.setEnabled(true);
                        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        NetworkUtils.handleErrorsCasesForAPICalls(AlternateIncomeActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Aadhar onResponse", "" + response.body().getErrors());
                    continueButton.setEnabled(true);
                    continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(AlternateIncomeActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<AlternateIncomeResponseModel> call, Throwable t) {
                hideProgressDialog();
                continueButton.setEnabled(true);
                continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                NetworkUtils.handleErrorsForAPICalls(AlternateIncomeActivity.this, null, null);
            }
        });
    }


    public void requestFocus(View view) {
        if (view.requestFocus())
            AlternateIncomeActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void goToExpenditurePage() {

        Intent intent = new Intent(AlternateIncomeActivity.this,ExpenditureDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("applicant","finished");
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    private void updateStatusInCashIncomePage() {

        CashIncomeDetailsActivity.instance.init();
        MemberDetailActivity.getInstance().init();
    }

    public void onDelete(View view) {

        duplicatingLayout.removeView((View) view.getParent());
        isValidAmount=true;
        isValidSource=true;
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
    public void onBackPressed() {

        View view = getLayoutInflater().inflate(R.layout.custom_message_yes_no_dialog, null);
        TextView messageTV =(TextView)view.findViewById(R.id.message_tv);
        Button yesButton =(Button)view.findViewById(R.id.yes_button);
        Button noButton =(Button)view.findViewById(R.id.no_button);
        messageTV.setText(getResources().getString(R.string.hint_are_you_leaving));
        yesButton.setText(getResources().getString(R.string.hint_leave_page));
        noButton.setText(getResources().getString(R.string.hint_stay_on_page));

        AlertDialog.Builder builder = new AlertDialog.Builder(AlternateIncomeActivity.this);
        builder.setCancelable(false)
                .setTitle(getResources().getString(R.string.hint_leave_page_title))
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
                finish();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }
}
