package com.tailwebs.aadharindia.member.applicant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.models.LanguageModel;
import com.tailwebs.aadharindia.member.models.LanguagesResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectLanguageActivity extends BaseActivity implements LanguagesAdapter.LanguageAdapterListener, View.OnClickListener {


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;


    @BindView(R.id.languages_recycler_view)
    RecyclerView languagesRecyclerView;

    @BindView(R.id.input_layout_search)
    TextInputLayout searchLayout;

    @BindView(R.id.input_search)
    TextInputEditText searchET;

    @BindView(R.id.continue_button)
    Button continueButton;

    private List<LanguageModel> languageModelList;

    private ProgressDialog mProgressDialog;

    private LanguagesAdapter languagesAdapter;
    private SearchView searchView;


    String content = null,id=null;


    private FirebaseAnalytics mFirebaseAnalytics;




    public static final String CUSTOM_SEARCH_TEXT = "text";
    public static final String CUSTOM_SEARCH_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        ButterKnife.bind(this);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Select Language");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Select Language", null);


        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(SelectLanguageActivity.this);
            getLanguageDatas();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
        searchET.addTextChangedListener(new InputLayoutTextWatcher(searchET));
        continueButton.setOnClickListener(this);
    }

    private void getLanguageDatas() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LanguagesResponseModel> call = apiService.getLanguages();
            call.enqueue(new Callback<LanguagesResponseModel>() {
                @Override
                public void onResponse(Call<LanguagesResponseModel> call, Response<LanguagesResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            languageModelList = response.body().getLanguageModelArrayList();
                            languagesAdapter = new LanguagesAdapter(SelectLanguageActivity.this, languageModelList, SelectLanguageActivity.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            languagesRecyclerView.setLayoutManager(mLayoutManager);
                            languagesRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            languagesRecyclerView.addItemDecoration(new MyLanguageDividerItemDecoration(SelectLanguageActivity.this, DividerItemDecoration.VERTICAL, 16));
                            languagesRecyclerView.setAdapter(languagesAdapter);


                        } else {
                            NetworkUtils.handleErrorsForAPICalls(SelectLanguageActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(SelectLanguageActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<LanguagesResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(SelectLanguageActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.continue_button:
                Intent intent = new Intent();
                intent.putExtra(SelectLanguageActivity.CUSTOM_SEARCH_TEXT, content);
                intent.putExtra(SelectLanguageActivity.CUSTOM_SEARCH_ID, id);
                setResult(RESULT_OK, intent);
                finish();

                break;


            case R.id.back_button:
                onBackPressed();
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
                case R.id.input_search:
                        languagesAdapter.getFilter().filter(searchET.getText().toString());
                    break;


            }
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
    public void onLanguageSelected(LanguageModel languageModel) {

        continueButton.setEnabled(true);
        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
        content = languageModel.getName();
        id = languageModel.getId();
    }
}
