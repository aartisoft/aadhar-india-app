package com.tailwebs.aadharindia.housevisit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.housevisit.models.PastEventsModel;
import com.tailwebs.aadharindia.housevisit.models.SelectedUpcomingModel;
import com.tailwebs.aadharindia.housevisit.models.UpcomingEventsModel;
import com.tailwebs.aadharindia.housevisit.models.houseinformationcreate.HouseInforationCreateResponseModel;
import com.tailwebs.aadharindia.housevisit.models.housepersonalinfo.PersonalInforationCreateResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalInformationActivity extends BaseActivity implements View.OnClickListener {



    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;


    @BindView(R.id.upcoming_events_list_view)
    ListView upcomingEventsListView;

    @BindView(R.id.past_events_list_view)
    ListView pastEventsListView;

    UpcomingListAdapter upcomingListAdapter;
    PastEventsListAdapter pastEventsListAdapter;

    private ArrayList<UpcomingEventsModel> upcomingEventsModelArrayList;
    private ArrayList<PastEventsModel> pastEventsModelArrayList;

    private ArrayList<SelectedUpcomingModel> selectedUpcomingArraylist;
    private ArrayList<SelectedUpcomingModel> selectedPastArraylist;

    @BindView(R.id.continue_button)
    Button continueButton;


    private ProgressDialog mProgressDialog;

    String loanTakerID=null;

    boolean isUpcomingNotesPresent =false,isPastNotesPresent = false,isUpcomingSelected=false,isPastSelected=false;


    static PersonalInformationActivity instance;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        ButterKnife.bind(this);
        instance = this;

        loanTakerID = GlobalValue.loanTakerId;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "HV Personal Info", null);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Personal Information");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);


        continueButton.setOnClickListener(this);

        loadListView();


//        upcomingEventsListView.setOnTouchListener(new View.OnTouchListener() {
//            // Setting on Touch Listener for handling the touch inside ScrollView
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Disallow the touch request for parent scroll on touch of child view
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
//
//
//        pastEventsListView.setOnTouchListener(new View.OnTouchListener() {
//            // Setting on Touch Listener for handling the touch inside ScrollView
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Disallow the touch request for parent scroll on touch of child view
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });

    }


    public void updateUpcomingListviewsize(){
        setListViewHeightForUpcomingEventsBasedOnChildren(upcomingEventsListView);
    }

    public void updatePastListviewsize(){
        setListViewHeightForPastEventsBasedOnChildren(pastEventsListView);
    }



    public static PersonalInformationActivity getInstance() {
        return instance;
    }

    public void setListViewHeightForUpcomingEventsBasedOnChildren(ListView listView) {


        int totalHeight = 0;
        for (int i = 0; i < upcomingListAdapter.getCount(); i++) {
            View mView = upcomingListAdapter.getView(i, null, listView);
            mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += mView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (upcomingListAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

//        if (upcomingListAdapter == null)
//            return;
//
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
//        int totalHeight = 0;
//        View view = null;
//        for (int i = 0; i < upcomingListAdapter.getCount(); i++) {
//            view = upcomingListAdapter.getView(i, view, listView);
//            if (i == 0)
//                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//            totalHeight += view.getMeasuredHeight();
//        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (upcomingListAdapter.getCount() - 1));
//        listView.setLayoutParams(params);



    }


    public void setListViewHeightForPastEventsBasedOnChildren(ListView listView) {
//
//        if (pastEventsListAdapter == null)
//            return;
//
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
//        int totalHeight = 0;
//        View view = null;
//        for (int i = 0; i < pastEventsListAdapter.getCount(); i++) {
//            view = pastEventsListAdapter.getView(i, view, listView);
//            if (i == 0)
//                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//            totalHeight += view.getMeasuredHeight();
//        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (pastEventsListAdapter.getCount() - 1));
//        listView.setLayoutParams(params);


        int totalHeight = 0;
        for (int i = 0; i < pastEventsListAdapter.getCount(); i++) {
            View mView = pastEventsListAdapter.getView(i, null, listView);
            mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += mView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (pastEventsListAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }



    private void loadListView() {

        upcomingEventsModelArrayList = GlobalValue.upcomingEventsModelArrayList;
        pastEventsModelArrayList = GlobalValue.pastEventsModelArrayList;

        upcomingListAdapter = new UpcomingListAdapter(PersonalInformationActivity.this, upcomingEventsModelArrayList, true);
        setListViewHeightForUpcomingEventsBasedOnChildren(upcomingEventsListView);
        upcomingEventsListView.setAdapter(upcomingListAdapter);

        pastEventsListAdapter = new PastEventsListAdapter(PersonalInformationActivity.this, pastEventsModelArrayList, true);
        setListViewHeightForPastEventsBasedOnChildren(pastEventsListView);
        pastEventsListView.setAdapter(pastEventsListAdapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){


            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.continue_button:

                ArrayList<UpcomingEventsModel> selectedUpcomingRows = upcomingListAdapter.getSelectedIds();//Get the selected ids from adapter
                ArrayList<PastEventsModel> selectedPastRows = pastEventsListAdapter.getSelectedIds();//Get the selected ids from adapter

                selectedUpcomingArraylist = new ArrayList<SelectedUpcomingModel>();
                selectedPastArraylist = new ArrayList<SelectedUpcomingModel>();
                //Check if item is selected or not via size
                if (selectedUpcomingRows.size() > 0) {


                    //Loop to all the selected rows array
                    for (int i = 0; i < selectedUpcomingRows.size(); i++) {

                        if(selectedUpcomingRows.get(i).getChecked()){
                            Log.i("Aadhar India","selected note"+selectedUpcomingRows.get(i).getNote());
                            Log.i("Aadhar India","selected id"+selectedUpcomingRows.get(i).getId());
                            Log.i("Aadhar India","selected name"+selectedUpcomingRows.get(i).getName());
                            SelectedUpcomingModel selectedUpcomingModel = new SelectedUpcomingModel();
                            selectedUpcomingModel.setId(selectedUpcomingRows.get(i).getId());
                            selectedUpcomingModel.setNote(selectedUpcomingRows.get(i).getNote());

                            if(!selectedUpcomingRows.get(i).getCode().equalsIgnoreCase("none")){

                                if(selectedUpcomingRows.get(i).getNote().trim().length()>0){

                                    selectedUpcomingArraylist.add(selectedUpcomingModel);
                                    isUpcomingNotesPresent = true;
                                    isUpcomingSelected=true;

                                }else{
                                    isUpcomingSelected=true;
                                    isUpcomingNotesPresent = false;
                                    Toast.makeText(instance, "Please make sure you have added notes for upcoming events", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                selectedUpcomingArraylist.add(selectedUpcomingModel);
                                isUpcomingNotesPresent=true;
                                isUpcomingSelected=true;

                            }





                        }
                    }

                    Log.i("Aadhar India","selectedUpcomingArraylist"+selectedUpcomingArraylist.size());
                }


                //Check if item is selected or not via size
                if (selectedPastRows.size() > 0) {


                    //Loop to all the selected rows array
                    for (int i = 0; i < selectedPastRows.size(); i++) {

                        if(selectedPastRows.get(i).getChecked()){
                            Log.i("Aadhar India","selected past note"+selectedPastRows.get(i).getNote());
                            Log.i("Aadhar India","selected past id"+selectedPastRows.get(i).getId());
                            Log.i("Aadhar India","selected past name"+selectedUpcomingRows.get(i).getName());
                            SelectedUpcomingModel selectedUpcomingModel = new SelectedUpcomingModel();
                            selectedUpcomingModel.setId(selectedPastRows.get(i).getId());
                            selectedUpcomingModel.setNote(selectedPastRows.get(i).getNote());


                            if(!selectedPastRows.get(i).getCode().equalsIgnoreCase("none")){

                                if(selectedPastRows.get(i).getNote().trim().length()>0){

                                    selectedPastArraylist.add(selectedUpcomingModel);
                                    isPastNotesPresent=true;
                                    isPastSelected=true;

                                }else{
                                    isPastSelected=true;
                                    isPastNotesPresent=false;
                                    Toast.makeText(instance, "Please make sure you have added notes for past events", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                selectedPastArraylist.add(selectedUpcomingModel);
                                isPastNotesPresent=true;
                                isPastSelected=true;

                            }

                        }


                    }

                }

                if(selectedPastArraylist.size()>0 && selectedUpcomingArraylist.size()>0){

                    if(isUpcomingNotesPresent && isPastNotesPresent){
                        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        callAPIForSubmittingPersonalInformation();
                    }else{
                        if(!isUpcomingNotesPresent){
                            Toast.makeText(instance, "Please make sure you have added notes for upcoming events", Toast.LENGTH_SHORT).show();
                        }

                        if(!isPastNotesPresent){
                            Toast.makeText(instance, "Please make sure you have added notes for past events", Toast.LENGTH_SHORT).show();
                        }
                    }




                }else{


                    if(selectedUpcomingRows.size()>0){
                        if(isUpcomingSelected){
                            if(isUpcomingNotesPresent){

                            }else{
                                Toast.makeText(instance, "Please make sure you have added notes for upcoming events", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(instance, "Please enter upcoming events", Toast.LENGTH_SHORT).show();
                        }
                    } else if(selectedPastRows.size()>0) {
                        if (isPastSelected){
                            if (isPastNotesPresent) {

                            } else {
                                Toast.makeText(instance, "Please make sure you have added notes for past events", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(instance, "Please enter past events", Toast.LENGTH_SHORT).show();
                        }


                    }

                    continueButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

                }



                break;
        }

    }

    private void callAPIForSubmittingPersonalInformation() {

        continueButton.setEnabled(false);
        continueButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));
        showProgressDialog(PersonalInformationActivity.this);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        for(int i=0;i<selectedUpcomingArraylist.size();i++){
            Log.i("Aadhar India","upcoming_events["+ i +"][]"+ String.valueOf(selectedUpcomingArraylist.get(i).getId()));
            Log.i("Aadhar India","upcoming_events["+ i +"][]"+ String.valueOf(selectedUpcomingArraylist.get(i).getNote()));
            builder.addFormDataPart("upcoming_events["+ i +"][]", String.valueOf(selectedUpcomingArraylist.get(i).getId()));
            builder.addFormDataPart("upcoming_events["+ i +"][]", String.valueOf(selectedUpcomingArraylist.get(i).getNote()));
        }

        for(int j=0;j<selectedPastArraylist.size();j++){

            Log.i("Aadhar India","past_events["+ j +"][]"+ String.valueOf(selectedPastArraylist.get(j).getId()));
            Log.i("Aadhar India","past_events["+ j +"][]"+ String.valueOf(selectedPastArraylist.get(j).getNote()));
            builder.addFormDataPart("past_events["+ j +"][]", String.valueOf(selectedPastArraylist.get(j).getId()));
            builder.addFormDataPart("past_events["+ j +"][]", String.valueOf(selectedPastArraylist.get(j).getNote()));
        }


        RequestBody finalRequestBody = builder.build();
        ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
        Call<PersonalInforationCreateResponseModel> call = apiService.createPersonalInformation(loanTakerID,finalRequestBody
        );
        call.enqueue(new Callback<PersonalInforationCreateResponseModel>() {
            @Override
            public void onResponse(Call<PersonalInforationCreateResponseModel> call, Response<PersonalInforationCreateResponseModel> response) {
                hideProgressDialog();
                Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        //API Success is true
                        continueButton.setEnabled(true);
                        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));


                        Bundle params = new Bundle();
                        params.putString("status","house_visit_personal_info_created");
                        params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
                        mFirebaseAnalytics.logEvent("house_visit_personal_information", params);


                        Toast.makeText(PersonalInformationActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                        updateTheStatusInHouseDetailPage();
                        goToHouseVisitApplicantDoc();
                    } else {
                        Log.d("Drools onResponse", "" + response.body().getErrors());
                        continueButton.setEnabled(true);
                        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        NetworkUtils.handleErrorsCasesForAPICalls(PersonalInformationActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Drools onResponse", "" + response.body().getErrors());
                    continueButton.setEnabled(true);
                    continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(PersonalInformationActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<PersonalInforationCreateResponseModel> call, Throwable t) {
                hideProgressDialog();
                continueButton.setEnabled(true);
                continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                NetworkUtils.handleErrorsForAPICalls(PersonalInformationActivity.this, null, null);
            }
        });
    }

    private void updateTheStatusInHouseDetailPage() {
        HouseDetailsActivity.getInstance().init();
    }

    private void goToHouseVisitApplicantDoc() {
        startActivity(new Intent(PersonalInformationActivity.this,HouseVisitApplicantDocActivity.class));
        finish();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInformationActivity.this);
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
