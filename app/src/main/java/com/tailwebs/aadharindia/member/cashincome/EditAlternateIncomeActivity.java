package com.tailwebs.aadharindia.member.cashincome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.applicant.SelectLanguageActivity;
import com.tailwebs.aadharindia.member.cashincome.models.AlternateIncomeResponseModel;
import com.tailwebs.aadharindia.member.cashincome.models.SourceIncomeModel;
import com.tailwebs.aadharindia.member.expenditure.ExpenditureDetailsActivity;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAlternateIncomeActivity extends BaseActivity implements View.OnClickListener {


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternate_income);
        ButterKnife.bind(this);


        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Alternate Income");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        loanTakerID =  GlobalValue.loanTakerId;
        yesTv.setOnClickListener(this);
        noTv.setOnClickListener(this);
        addLayout.setOnClickListener(this);
        continueButton.setOnClickListener(this);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Edit Alternate Income", null);



        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(EditAlternateIncomeActivity.this);
            getAlternativeIncome();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

    }

    private void getAlternativeIncome() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<AlternateIncomeResponseModel> call = apiService.getAlternateIncome(loanTakerID);
            call.enqueue(new Callback<AlternateIncomeResponseModel>() {
                @Override
                public void onResponse(Call<AlternateIncomeResponseModel> call, final Response<AlternateIncomeResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            Log.d("Aadhar onResponse1", "" + new Gson().toJson(response.body()));

                            try {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                                if(jsonObject.has("alternate_incomes")){
                                    if(response.body().getSourceIncomeModelArrayList().size()>0){
                                        chooseAlternateIncome("Yes");
                                        duplicatingLayout.setVisibility(View.VISIBLE);
                                        addLayout.setVisibility(View.VISIBLE);


                                        for(int j = 0;j<response.body().getSourceIncomeModelArrayList().size();j++){

                                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            final View rowView = inflater.inflate(R.layout.duplicating_alternate_income_layout, null);
                                            // Add the new row before the add field button.
                                            duplicatingLayout.addView(rowView, j);
                                            View chidLayout = duplicatingLayout.getChildAt(j);
                                            final TextInputEditText inputSource = (TextInputEditText) chidLayout.findViewById(R.id.input_source);
                                            final TextInputEditText inputAmount = (TextInputEditText) chidLayout.findViewById(R.id.input_income_amount);
                                            final TextInputLayout inputLayout = (TextInputLayout)rowView.findViewById(R.id.input_layout_income_amount);
                                            final TextInputLayout inputSourceLayout = (TextInputLayout)rowView.findViewById(R.id.input_layout_source);
                                            inputSource.setText(response.body().getSourceIncomeModelArrayList().get(j).getSource());
                                            inputAmount.setText(response.body().getSourceIncomeModelArrayList().get(j).getIncome());
                                            inputSource.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                                            inputAmount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);


                                            inputSource.addTextChangedListener(new TextWatcher() {
                                                ArrayList<String> itemPassed = new ArrayList<String>();
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                    itemPassed.clear();
                                                    itemPassed.add("");
                                                    boolean classStatus = UiUtils.checkValidation(EditAlternateIncomeActivity.this, inputSource, inputSourceLayout, itemPassed);

                                                    if (classStatus == false) {

                                                        isValidSource=false;
                                                        requestFocus(inputSource);

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
                                                    boolean classStatus = UiUtils.checkValidation(EditAlternateIncomeActivity.this, inputSource, inputSourceLayout, itemPassed);

                                                    if (classStatus == false) {

                                                        isValidSource=false;
                                                        requestFocus(inputSource);

                                                    } else {

                                                        isValidSource=true;

                                                        inputSourceLayout.setErrorEnabled(false);
                                                    }


                                                }
                                            });

                                            inputAmount.addTextChangedListener(new TextWatcher() {
                                                ArrayList<String> itemPassed = new ArrayList<String>();
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                    itemPassed.clear();
                                                    itemPassed.add("");
                                                    boolean classStatus = UiUtils.checkValidation(EditAlternateIncomeActivity.this, inputAmount, inputLayout, itemPassed);

                                                    if (classStatus == false) {

                                                        isValidAmount=false;
                                                        requestFocus(inputAmount);

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
                                                    boolean classStatus = UiUtils.checkValidation(EditAlternateIncomeActivity.this, inputAmount, inputLayout, itemPassed);

                                                    if (classStatus == false) {

                                                        isValidAmount=false;
                                                        requestFocus(inputAmount);

                                                    } else {
                                                        isValidAmount=true;
                                                        inputLayout.setErrorEnabled(false);
                                                    }
                                                }
                                            });

                                        }

                                    }
                                }else{
                                    chooseAlternateIncome("No");
                                    duplicatingLayout.setVisibility(View.GONE);
                                    addLayout.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }





                        } else {
                            NetworkUtils.handleErrorsForAPICalls(EditAlternateIncomeActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(EditAlternateIncomeActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<AlternateIncomeResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(EditAlternateIncomeActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
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
            isValidAlternateIncome = false;
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
                        boolean classStatus = UiUtils.checkValidation(EditAlternateIncomeActivity.this, sourceEt, inputSourceLayout, itemPassed);

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
                        boolean classStatus = UiUtils.checkValidation(EditAlternateIncomeActivity.this, sourceEt, inputSourceLayout, itemPassed);

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
                        boolean classStatus = UiUtils.checkValidation(EditAlternateIncomeActivity.this, amountEt, inputLayout, itemPassed);

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
                        boolean classStatus = UiUtils.checkValidation(EditAlternateIncomeActivity.this, amountEt, inputLayout, itemPassed);

                        if (classStatus == false) {

                            isValidAmount=false;
                            requestFocus(amountEt);

                        } else {
                            isValidAmount=true;
                            inputLayout.setErrorEnabled(false);
                        }
                    }
                });

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
                            inputSLayout.setError(EditAlternateIncomeActivity.this.getString(R.string.field_required));
                            isValidSource=false;
                        }

                        if (inputAmount.getText().toString().trim().isEmpty()) {
                            inputAmount.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            inputALayout.setError(EditAlternateIncomeActivity.this.getString(R.string.field_required));
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

    public void requestFocus(View view) {
        if (view.requestFocus())
            EditAlternateIncomeActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void submitAlternateIncome() {
        callAPIForAlternateIncome();
    }

    private void callAPIForAlternateIncome() {

        continueButton.setEnabled(false);
        continueButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(EditAlternateIncomeActivity.this);
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
        ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
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

                        goToExpenditurePage();
                        updateStatusInCashIncomePage();
                        Toast.makeText(EditAlternateIncomeActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("Aadhar onResponse", "" + response.body().getErrors());
                        continueButton.setEnabled(true);
                        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        NetworkUtils.handleErrorsCasesForAPICalls(EditAlternateIncomeActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Aadhar onResponse", "" + response.body().getErrors());
                    continueButton.setEnabled(true);
                    continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(EditAlternateIncomeActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<AlternateIncomeResponseModel> call, Throwable t) {
                hideProgressDialog();
                continueButton.setEnabled(true);
                continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                NetworkUtils.handleErrorsForAPICalls(EditAlternateIncomeActivity.this, null, null);
            }
        });
    }

    private void goToExpenditurePage() {

//        Intent intent = new Intent(EditAlternateIncomeActivity.this,ExpenditureDetailsActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("applicant","finished");
//        intent.putExtras(bundle);
//        startActivity(intent);
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
}
