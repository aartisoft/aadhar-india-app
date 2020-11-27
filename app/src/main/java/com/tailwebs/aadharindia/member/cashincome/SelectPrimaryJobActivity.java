package com.tailwebs.aadharindia.member.cashincome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.applicant.LanguagesAdapter;
import com.tailwebs.aadharindia.member.applicant.MyLanguageDividerItemDecoration;
import com.tailwebs.aadharindia.member.applicant.SelectLanguageActivity;
import com.tailwebs.aadharindia.member.cashincome.models.PrimaryJobModel;
import com.tailwebs.aadharindia.member.cashincome.models.PrimaryJobResponseModel;
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

public class SelectPrimaryJobActivity extends BaseActivity implements View.OnClickListener {

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

    @BindView(R.id.fragment_back_stack_frame_layout)
    FrameLayout backStackFrameLayout;




    @BindView(R.id.input_layout_search)
    TextInputLayout searchLayout;

    @BindView(R.id.input_search)
    TextInputEditText searchET;

    @BindView(R.id.continue_button)
    Button continueButton;

//    private List<PrimaryJobModel> jobModelList;
//
//
//    private PrimaryJobAdapter primaryJobAdapter;
    private SearchView searchView;


    public static final String CUSTOM_SEARCH_TEXT = "text";
    public static final String CUSTOM_SEARCH_ID = "id";

    static SelectPrimaryJobActivity instance;
    String content = null,id=null;


    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_primary_job);
        ButterKnife.bind(this);
        instance=this;


        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Select Primary Job");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

//        loanTakerID =  "165";
        loanTakerID =  GlobalValue.loanTakerId;


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Select Primary Job", null);


        // Get FragmentManager and FragmentTransaction object.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Create FragmentOne instance.
        PrimaryJobFragment fragmentOne = new PrimaryJobFragment();

        Bundle bundle = new Bundle();
        bundle.putString("code", "root");
        fragmentOne.setArguments(bundle);
        // Add fragment one with tag name.
        fragmentTransaction.add(R.id.fragment_back_stack_frame_layout, fragmentOne, "Fragment One");
        fragmentTransaction.commit();
        FragmentUtil.printActivityFragmentList(fragmentManager);

        searchET.addTextChangedListener(new InputLayoutTextWatcher(searchET));
        continueButton.setOnClickListener(this);

        searchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }

    private void performSearch() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Create FragmentOne instance.
        PrimaryJobSearchFragment fragmentOne = new PrimaryJobSearchFragment();

        Bundle bundle = new Bundle();
        bundle.putString("string", searchET.getText().toString());
        fragmentOne.setArguments(bundle);
        // Add fragment one with tag name.
        fragmentTransaction.add(R.id.fragment_back_stack_frame_layout, fragmentOne, "Fragment Search");
        fragmentTransaction.commit();
        FragmentUtil.printActivityFragmentList(fragmentManager);

//        Toast.makeText(instance, ""+searchET.getText().toString(), Toast.LENGTH_SHORT).show();
    }


    public static SelectPrimaryJobActivity getInstance() {
        return instance;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.continue_button:
                Intent intent = new Intent();
                intent.putExtra(SelectPrimaryJobActivity.CUSTOM_SEARCH_TEXT, content);
                intent.putExtra(SelectPrimaryJobActivity.CUSTOM_SEARCH_ID, id);
                setResult(RESULT_OK, intent);
                finish();

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
//                    primaryJobAdapter.getFilter().filter(searchET.getText().toString());
                    break;

            }
        }

    }

    public void setContinueButtonClicked(String idVal, String contentVal){
        continueButton.setEnabled(true);
        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
        id=idVal;
        content = contentVal;

    }






}
