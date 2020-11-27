package com.tailwebs.aadharindia.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.tailwebs.aadharindia.home.models.TaskResponseModel;
import com.tailwebs.aadharindia.home.tasks.collection.CollectionTaskDetailsActivity;
import com.tailwebs.aadharindia.home.tasks.creategroup.CreateGroupTaskDetailsActivity;
import com.tailwebs.aadharindia.home.tasks.housevisit.HouseVisitTaskDetailsActivity;
import com.tailwebs.aadharindia.postapproval.PostApprovalTaskDetailsActivity;
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

public class AddEditNotesActivity extends BaseActivity implements View.OnClickListener {


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    private ProgressDialog mProgressDialog;

    @BindView(R.id.input_layout_notes)
    TextInputLayout notesLayout;

    @BindView(R.id.input_notes)
    TextInputEditText notesET;

    @BindView(R.id.save_button)
    Button saveButton;


    boolean isValidNotes=false;

    String taskId,fromActivity;

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_notes);
        ButterKnife.bind(this);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Add/Edit Notes");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
        saveButton.setOnClickListener(this);
        notesET.addTextChangedListener(new InputLayoutTextWatcher(notesET));

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Add/Edit Notes", null);

        taskId = GlobalValue.taskId;

        fromActivity = getIntent().getStringExtra("activity");

        if(getIntent().hasExtra("notes")) {

            if(getIntent().getStringExtra("notes")==null){
                notesLayout.setErrorEnabled(false);
                isValidNotes = false;
            }else{

            }
            notesET.setText(getIntent().getStringExtra("notes"));

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.save_button:
                saveNotes();
                break;
        }

    }

    private void saveNotes() {

        if ((isValidNotes)){

            callAPIForSavingNotes();

        }else{

            UiUtils.checkValidation(AddEditNotesActivity.this, notesET, notesLayout, new ArrayList<String>());


        }
    }

    private void callAPIForSavingNotes() {

        saveButton.setEnabled(false);
        saveButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(AddEditNotesActivity.this);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        if(notesET.getText().toString().trim().length()>0){
            builder.addFormDataPart("task[note]", notesET.getText().toString().trim()); //notes
        }



        RequestBody finalRequestBody = builder.build();
        ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
        Call<TaskResponseModel> call = apiService.updateCustomTaskNotes(taskId,finalRequestBody
        );
        call.enqueue(new Callback<TaskResponseModel>() {
            @Override
            public void onResponse(Call<TaskResponseModel> call, Response<TaskResponseModel> response) {
                hideProgressDialog();
                Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        //API Success is true

                        saveButton.setEnabled(true);
                        saveButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        finish();

                        goToPreviousPage();


                        Toast.makeText(AddEditNotesActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("Drools onResponse", "" + response.body().getErrors());
                        saveButton.setEnabled(true);
                        saveButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(new Gson().toJson(response.body()));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        NetworkUtils.handleErrorsCasesForAPICalls(AddEditNotesActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Drools onResponse", "" + response.body().getErrors());
                    saveButton.setEnabled(true);
                    saveButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(AddEditNotesActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<TaskResponseModel> call, Throwable t) {
                hideProgressDialog();
                saveButton.setEnabled(true);
                saveButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                NetworkUtils.handleErrorsForAPICalls(AddEditNotesActivity.this, null, null);
            }
        });
    }

    private void goToPreviousPage() {
        if(fromActivity.equalsIgnoreCase("collection")){
            CollectionTaskDetailsActivity.getInstance().init();
        }else if(fromActivity.equalsIgnoreCase("custom")){
            CustomTaskDetailsActivity.getInstance().init();
        }else if(fromActivity.equalsIgnoreCase("group")) {
            CreateGroupTaskDetailsActivity.getInstance().init();
        }else if(fromActivity.equalsIgnoreCase("house_visit")) {
            HouseVisitTaskDetailsActivity.getInstance().init();
        }else if(fromActivity.equalsIgnoreCase("post_approval")) {
            PostApprovalTaskDetailsActivity.getInstance().init();
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


                case R.id.input_notes:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean noteStatus = UiUtils.checkValidation(AddEditNotesActivity.this, notesET, notesLayout, itemPassed);

                    if (noteStatus == false) {
                        isValidNotes = false;
                        requestFocus(notesET);

                    } else {
                        isValidNotes=true;
                        notesLayout.setErrorEnabled(false);
                    }

                    break;



            }
        }

    }


    public void requestFocus(View view) {
        if (view.requestFocus())
            AddEditNotesActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
