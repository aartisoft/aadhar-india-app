package com.tailwebs.aadharindia.member.applicant;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.GroupMemberListingActivity;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.applicant.spinners.SecondaryIDSpinnerAdapter;
import com.tailwebs.aadharindia.member.coapplicant.CoApplicantDetailsActivity;
import com.tailwebs.aadharindia.member.models.LoanTakerApplicantDocumentDetailResponseModel;
import com.tailwebs.aadharindia.models.common.CACDSecondaryIDModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.ScalingUtilities;
import com.tailwebs.aadharindia.utils.UiUtils;
import com.tailwebs.aadharindia.utils.custom.BetterSpinner;
import com.tailwebs.aadharindia.utils.custom.cropper.CropImage;
import com.tailwebs.aadharindia.utils.custom.cropper.CropImageView;
import com.tailwebs.aadharindia.utils.custom.horizontalfullscreen.ImagePreviewSingleImageWithEditActivity;
import com.tailwebs.aadharindia.utils.custom.horizontalfullscreen.PreviewFile;
import com.tailwebs.aadharindia.utils.custom.newmultipleimageupload.NavigatorImage;
import com.tailwebs.aadharindia.utils.custom.newmultipleimageupload.model.Image;
import com.tailwebs.aadharindia.utils.custom.newmultipleimageupload.view.ImageGridView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantDocumentActivity extends BaseActivity implements View.OnClickListener {

    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    //ProfilePic

    @BindView(R.id.contact_person_upload_image)
    ImageView profilePhoto;

    @BindView(R.id.profile_pic_status_tv)
    TextView profilePicStatusTV;


    @BindView(R.id.profile_pic_progress_bar)
    ProgressBar profilePicProgressBar;

    //bank detail (first page)
    @BindView(R.id.bank_detail_first_page_image)
    ImageView bankDetailFirstPage;

    @BindView(R.id.bank_detail_status_tv)
    TextView bankDetailStatusTv;

    @BindView(R.id.bank_detail_applicant_not_available)
    TextView bankDetailNotAvailable;

    @BindView(R.id.bank_detail_first_page_progress_bar)
    ProgressBar bankDetailFirstPageProgressBar;

    //one year bank statement
    @BindView(R.id.one_year_bank_statement_grid)
    ImageGridView onYearBankStatementGrid;

    @BindView(R.id.one_year_bank_statement_status_tv)
    TextView oneYearBankStatementStatusTv;

    @BindView(R.id.one_year_bank_statement_document_not_available)
    TextView oneYearBankStatementNotAvailable;



    //aadhar card front
    @BindView(R.id.aadhar_card_front_image)
    ImageView aadharCardFrontImage;

    @BindView(R.id.aadhar_card_front_status_tv)
    TextView aadharCardFrontStatusTv;

    @BindView(R.id.aadhar_card_front_document_not_available)
    TextView aadharCardFrontNotAvailable;

    @BindView(R.id.aadhar_card_front_progress_bar)
    ProgressBar aadharCardFrontProgressBar;


    //aadhar card back
    @BindView(R.id.aadhar_card_back_image)
    ImageView aadharCardBackImage;

    @BindView(R.id.aadhar_card_back_status_tv)
    TextView aadharCardBackStatusTv;

    @BindView(R.id.aadhar_card_back_document_not_available)
    TextView aadharCardBackNotAvailable;

    @BindView(R.id.aadhar_card_back_progress_bar)
    ProgressBar aadharCardBackProgressBar;

    //secondary Id

    @BindView(R.id.input_layout_secondary_id)
    TextInputLayout secondaryIdLayout;

    @BindView(R.id.input_secondary_id)
    BetterSpinner secondaryIdET;


    //secondary Id
    @BindView(R.id.secondary_grid)
    ImageGridView secondaryGrid;

    @BindView(R.id.secondary_id_status_tv)
    TextView secondaryIdStatusTv;

    @BindView(R.id.secondary_id_document_not_available)
    TextView secondaryIdNotAvailable;


    @BindView(R.id.secondary_dummy_id_image)
    ImageView secondaryDummyImage;


    @BindView(R.id.relation_secondary)
    Spinner secondarySpinner;

    SecondaryIDSpinnerAdapter secondaryIDSpinnerAdapter;

    @BindView(R.id.secondary_spinner_error_tv)
    TextView secondarySpinnerErrorTv;

    //other loan cards
    @BindView(R.id.other_loan_grid)
    ImageGridView otherLoanCardGrid;


    @BindView(R.id.others_status_tv)
    TextView otherStatusTv;


    @BindView(R.id.continue_button)
    Button continueButton;

    private boolean isValidProfilePic = false, isValidBankFrontPage = false, isValidBankStatement = false, isValidAadharFront = false,
            isValidAadharBack = false, isValidSecondaryItemSelected = false, isValidSecondaryId = false, isValidOtherLoan = false,
    isSecondaryDocumentsUploaded=false;

    private boolean isProfilePicClicked = false, isBankFrontPageClicked = false, isBankStatementClicked = false, isAadharFrontClicked = false,
            isAadharBackClicked = false,  isValidSecondaryIdSelected = false, isOtherLoanClicked = false;

    String  commonUriPath = null;

    private ProgressDialog mProgressDialog;
    String loanTakerID = null;
    int secondaryIDValue= 0;

    public ArrayList<CACDSecondaryIDModel> cacdSecondaryIDModelArrayList = null,cacdSecondaryIDModelArrayListNew;
//    SecondaryDocumentSpinnerAdapter secondaryDocumentSpinnerAdapter;

    int desiredWidth = 800,desiredHeight = 800,desiredWidthSmall = 300,desiredHeightSmall=300;


    List<Image> secondarySelectedImages,otherSelectedImages,bankStatementSelectedImages;


    private boolean isProfilePicUploaded = false,isAadharFrontUploaded = false,isAadharBackUploaded=false,isBankDetailFirstPageUploaded=false;

    String profilePicUploadedUrl=null,aadharFrontUploadedUrl=null,aadharBackUploadedUrl=null,bankDetailFirstPageUploadedUrl=null;

    private FirebaseAnalytics mFirebaseAnalytics;


    ArrayList<String> secondaryIdArrayList = new ArrayList<>();
    ArrayList<String> bankIdArrayList  = new ArrayList<>();
    ArrayList<String> otherIdArrayList= new ArrayList<>();


    boolean isSecondaryClicked=false,isBankCLicked=false,isOtherLoanCardClicked=false;


    @BindView(R.id.secondaryLayout)
    LinearLayout secondaryLayout;


    @BindView(R.id.oneYearBankLayout)
    LinearLayout oneYearBankLayout;


    @BindView(R.id.otherLoanCardLayout)
    LinearLayout otherLoanCardLayout;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_document);
        ButterKnife.bind(this);

        loanTakerID = GlobalValue.loanTakerId;
        isStoragePermissionGranted();
//        secondaryIdArrayList = new ArrayList<>();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Applicant Documents", null);


        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Application Document Photos");
        headingTV.setTextAppearance(getApplicationContext(), R.style.MyActionBarHeading);


        cacdSecondaryIDModelArrayList =GlobalValue.applicantSecondaryIDArrayList;

        //make grids clickable false

        secondaryGrid.setClickable(false);
        otherLoanCardGrid.setClickable(false);
        onYearBankStatementGrid.setClickable(false);

        //make layouts clickable tru

        secondaryLayout.setClickable(true);
        secondaryLayout.setEnabled(true);
        otherLoanCardLayout.setClickable(true);
        otherLoanCardLayout.setEnabled(true);
        oneYearBankLayout.setClickable(true);
        oneYearBankLayout.setEnabled(true);




        secondaryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondaryGrid.setClickable(true);
                isSecondaryClicked=true;
                isBankCLicked=false;
                isOtherLoanCardClicked=false;
                secondaryGrid.setGRID_TYPE(5);
                secondaryGrid.setItemClicked("secondary");
                secondaryGrid.setMaxValue(GlobalValue.secondaryDocumentImageCount);
                secondaryLayout.setClickable(false);
                secondaryLayout.setEnabled(false);

                //set others
                otherLoanCardGrid.setClickable(false);
                onYearBankStatementGrid.setClickable(false);

                otherLoanCardLayout.setClickable(true);
                otherLoanCardLayout.setEnabled(true);
                oneYearBankLayout.setClickable(true);
                oneYearBankLayout.setEnabled(true);
            }
        });

        oneYearBankLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onYearBankStatementGrid.setClickable(true);
                isSecondaryClicked=false;
                isBankCLicked=true;
                isOtherLoanCardClicked=false;
                onYearBankStatementGrid.setGRID_TYPE(7);
                onYearBankStatementGrid.setItemClicked("bank");
                onYearBankStatementGrid.setMaxValue(GlobalValue.bankStatementImageCount);
                oneYearBankLayout.setClickable(false);
                oneYearBankLayout.setEnabled(false);

                //set others
                secondaryGrid.setClickable(false);
                otherLoanCardGrid.setClickable(false);


                secondaryLayout.setClickable(true);
                secondaryLayout.setEnabled(true);
                otherLoanCardLayout.setClickable(true);
                otherLoanCardLayout.setEnabled(true);



            }
        });
//
        otherLoanCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherLoanCardGrid.setClickable(true);
                isSecondaryClicked=false;
                isBankCLicked=false;
                isOtherLoanCardClicked=true;
                otherLoanCardGrid.setGRID_TYPE(6);
                otherLoanCardGrid.setItemClicked("other");
                otherLoanCardGrid.setMaxValue(GlobalValue.otherLoanCardImageCount);
                otherLoanCardLayout.setClickable(false);
                otherLoanCardLayout.setEnabled(false);

                //set others

                secondaryGrid.setClickable(false);
                onYearBankStatementGrid.setClickable(false);

                secondaryLayout.setClickable(true);
                secondaryLayout.setEnabled(true);
                oneYearBankLayout.setClickable(true);
                oneYearBankLayout.setEnabled(true);
            }
        });






        profilePhoto.setOnClickListener(this);
        bankDetailFirstPage.setOnClickListener(this);
        aadharCardFrontImage.setOnClickListener(this);
        aadharCardBackImage.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        secondaryDummyImage.setOnClickListener(this);

        bankDetailNotAvailable.setOnClickListener(this);
        oneYearBankStatementNotAvailable.setOnClickListener(this);
        aadharCardFrontNotAvailable.setOnClickListener(this);
        aadharCardBackNotAvailable.setOnClickListener(this);
        secondaryIdNotAvailable.setOnClickListener(this);

        init();
    }


    public void init(){
        //        set secondary id

        //set values for relation dropdown
        //initialize arraylist
        cacdSecondaryIDModelArrayListNew = new ArrayList<CACDSecondaryIDModel>();

        //add select to the drop down in position 0
        CACDSecondaryIDModel cacdSecondaryIDModel = new CACDSecondaryIDModel();
        cacdSecondaryIDModel.setId("0");
        cacdSecondaryIDModel.setName("Select");
        cacdSecondaryIDModel.setCode("0");
        cacdSecondaryIDModelArrayListNew.add(cacdSecondaryIDModel);

        //add rest of the relations
        for(int i=0;i<GlobalValue.applicantSecondaryIDArrayList.size();i++){
            CACDSecondaryIDModel cacdSecondaryIDModel1 = new CACDSecondaryIDModel();
            cacdSecondaryIDModel1.setId(GlobalValue.applicantSecondaryIDArrayList.get(i).getId());
            cacdSecondaryIDModel1.setName(GlobalValue.applicantSecondaryIDArrayList.get(i).getName());
            cacdSecondaryIDModel1.setCode(GlobalValue.applicantSecondaryIDArrayList.get(i).getCode());

            cacdSecondaryIDModelArrayListNew.add(cacdSecondaryIDModel1);
        }

        setLoanReasonSpinner(cacdSecondaryIDModelArrayListNew);


        secondarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                CACDSecondaryIDModel secondaryIDModel = secondaryIDSpinnerAdapter.getItem(position);

                Log.i("Shahana","--"+position+"--"+id);
                Log.i("Shahana","--"+secondaryIDModel.getName());
                // Here you can do the action you want to...

                if (position==0){
                    secondaryIDValue = 0;
                    isValidSecondaryId=false;
                }

                if(position!=0){

                    isValidSecondaryId = true;
                    secondaryIDValue= Integer.parseInt(secondaryIDModel.getId());
                    secondarySpinnerErrorTv.setVisibility(View.GONE);
                    secondaryDummyImage.setVisibility(View.GONE);
                    secondaryGrid.setVisibility(View.VISIBLE);

                    if(isSecondaryDocumentsUploaded){
                        if(secondaryIdArrayList.size()>0){
                            saveMultipleSecondaryDocuments();
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void setLoanReasonSpinner(ArrayList<CACDSecondaryIDModel> applicantLoanReasonArrayListNew) {



        Log.i("Shahana","--"+applicantLoanReasonArrayListNew.size());
        CACDSecondaryIDModel[] cacdSecondaryIDModels = new CACDSecondaryIDModel[applicantLoanReasonArrayListNew.size()];

        for(int i=0;i<applicantLoanReasonArrayListNew.size();i++){
            cacdSecondaryIDModels[i]= new CACDSecondaryIDModel();
            cacdSecondaryIDModels[i].setId(applicantLoanReasonArrayListNew.get(i).getId());
            cacdSecondaryIDModels[i].setCode(applicantLoanReasonArrayListNew.get(i).getCode());
            cacdSecondaryIDModels[i].setName(applicantLoanReasonArrayListNew.get(i).getName());

            Log.i("Shahana","-code-"+i+applicantLoanReasonArrayListNew.get(i).getCode());
        }


        secondaryIDSpinnerAdapter = new SecondaryIDSpinnerAdapter(ApplicantDocumentActivity.this,
                R.layout.ms__list_item,
                cacdSecondaryIDModels);

        secondarySpinner.setAdapter(secondaryIDSpinnerAdapter); // Set the custom adapter to the spinner

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(ApplicantDocumentActivity.this);
            getDocumentDetails();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

    }

    private void getDocumentDetails() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerApplicantDocumentDetailResponseModel> call = apiService.getDocumentDetails(loanTakerID);
            call.enqueue(new Callback<LoanTakerApplicantDocumentDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerApplicantDocumentDetailResponseModel> call, final Response<LoanTakerApplicantDocumentDetailResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            Log.i("Drools", "" + new Gson().toJson(response.body()));
                            setValuesFromResponse(response.body());



                        } else {
                            NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDocumentActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(LoanTakerApplicantDocumentDetailResponseModel body) {

        try {

            //profile pic
            String profileImage = body.getLoanTakerModel().getApplicantDocumentModel().isProfile_image_not_available();
            if( profileImage == null){
                //mark not available
            }else if(profileImage == "true"){
                //mark available
                isValidProfilePic=true;
                UiUtils.setMarkedNotAvailable(ApplicantDocumentActivity.this,profilePicStatusTV);
            }else if (profileImage == "false"){
                //file uploaded
                isValidProfilePic=true;
                isProfilePicUploaded = true;
                profilePicUploadedUrl = body.getLoanTakerModel().getApplicantDocumentModel().getProfileImages().getOriginal();
                UiUtils.setDocumentUploaded(ApplicantDocumentActivity.this,profilePicStatusTV);
                Picasso.with(ApplicantDocumentActivity.this)
                        .load(body.getLoanTakerModel().getApplicantDocumentModel().getProfileImages().getMedium())
                        .placeholder(R.drawable.userimg_placeholder)
                        .into(profilePhoto);
            }


            //bank first page
            String bank_first_page = body.getLoanTakerModel().getApplicantDocumentModel().isBank_detail_image_not_available();
            if( bank_first_page == null){
                //mark not available

            }else if(bank_first_page == "true"){
                //mark available
                isValidBankFrontPage=true;
                UiUtils.setMarkedNotAvailable(ApplicantDocumentActivity.this,bankDetailNotAvailable);
                bankDetailStatusTv.setVisibility(View.GONE);
            }else if (bank_first_page == "false"){
                isValidBankFrontPage=true;
                isBankDetailFirstPageUploaded=true;
                bankDetailFirstPageUploadedUrl = body.getLoanTakerModel().getApplicantDocumentModel().getBank_detail_images().getOriginal();
                //file uploaded
                UiUtils.setDocumentUploaded(ApplicantDocumentActivity.this,bankDetailStatusTv);
                bankDetailNotAvailable.setVisibility(View.GONE);
                Picasso.with(ApplicantDocumentActivity.this)
                        .load(body.getLoanTakerModel().getApplicantDocumentModel().getBank_detail_images().getMedium())
                        .placeholder(R.drawable.img_placeholder)
                        .into(bankDetailFirstPage);
            }


            //bank statement page
            String bank_statement = body.getLoanTakerModel().getApplicantDocumentModel().isBank_statement_images_not_available();
            if( bank_statement == null){
                //mark not available

            }else if(bank_statement == "true"){
                isValidBankStatement=true;
                //mark available
                UiUtils.setMarkedNotAvailable(ApplicantDocumentActivity.this,oneYearBankStatementNotAvailable);
                oneYearBankStatementStatusTv.setVisibility(View.GONE);
            }else if (bank_statement == "false"){
                isValidBankStatement=true;
                //file uploaded
                UiUtils.setDocumentUploaded(ApplicantDocumentActivity.this,oneYearBankStatementStatusTv);
                oneYearBankStatementNotAvailable.setVisibility(View.GONE);

                ArrayList<String> result = new ArrayList<>();
                bankIdArrayList = new ArrayList<>();

                for(int i=0;i<body.getLoanTakerModel().getApplicantDocumentModel().getBankStatementImagesModelArrayList().size();i++){
                    Log.i("Shahana","--"+body.getLoanTakerModel().getApplicantDocumentModel()
                            .getBankStatementImagesModelArrayList().get(i)
                            .getBankStatementImageModel().getUploadImages().getMedium());

                    result.add(body.getLoanTakerModel().getApplicantDocumentModel()
                            .getBankStatementImagesModelArrayList().get(i)
                            .getBankStatementImageModel().getUploadImages().getOriginal());
                    bankIdArrayList.add(body.getLoanTakerModel().getApplicantDocumentModel().getBankStatementImagesModelArrayList().get(i).
                            getBankStatementImageModel().getId());

                }
                onYearBankStatementGrid.setNetworkPhotosWithKey(result);
            }


            //aadhar front
            String aadhar_front = body.getLoanTakerModel().getApplicantDocumentModel().isAadhar_front_image_not_available();
            if( aadhar_front == null){
                //mark not available

            }else if(aadhar_front == "true"){
                //mark available
                isValidAadharFront=true;
                UiUtils.setMarkedNotAvailable(ApplicantDocumentActivity.this,aadharCardFrontNotAvailable);
                aadharCardFrontStatusTv.setVisibility(View.GONE);
            }else if (aadhar_front == "false"){
                isValidAadharFront=true;
                isAadharFrontUploaded=true;
                aadharFrontUploadedUrl = body.getLoanTakerModel().getApplicantDocumentModel().getAadhar_front_images().getOriginal();
                //file uploaded
                UiUtils.setDocumentUploaded(ApplicantDocumentActivity.this,aadharCardFrontStatusTv);
                aadharCardFrontNotAvailable.setVisibility(View.GONE);
                Picasso.with(ApplicantDocumentActivity.this)
                        .load(body.getLoanTakerModel().getApplicantDocumentModel().getAadhar_front_images().getMedium())
                        .placeholder(R.drawable.img_placeholder)
                        .into(aadharCardFrontImage);
            }

            //aadhar back
            String aadhar_back = body.getLoanTakerModel().getApplicantDocumentModel().isAadhar_back_image_not_available();
            if( aadhar_back == null){
                //mark not available

            }else if(aadhar_back == "true"){
                //mark available
                isValidAadharBack=true;
                UiUtils.setMarkedNotAvailable(ApplicantDocumentActivity.this,aadharCardBackNotAvailable);
                aadharCardBackStatusTv.setVisibility(View.GONE);
            }else if (aadhar_back == "false"){

                //file uploaded
                isValidAadharBack=true;
                isAadharBackUploaded = true;
                aadharBackUploadedUrl = body.getLoanTakerModel().getApplicantDocumentModel().getAadhar_back_images().getOriginal();
                UiUtils.setDocumentUploaded(ApplicantDocumentActivity.this,aadharCardBackStatusTv);
                aadharCardBackNotAvailable.setVisibility(View.GONE);
                Picasso.with(ApplicantDocumentActivity.this)
                        .load(body.getLoanTakerModel().getApplicantDocumentModel().getAadhar_back_images().getMedium())
                        .placeholder(R.drawable.img_placeholder)
                        .into(aadharCardBackImage);
            }

            //secondary

            String secondary_document = body.getLoanTakerModel().getApplicantDocumentModel().isSecondary_document_images_not_available();
            if( secondary_document == null){
                //mark not available

            }else if(secondary_document == "true"){
                //mark available
                isValidSecondaryItemSelected=true;
//                secondaryIDValue = Integer.parseInt(body.getLoanTakerModel().getApplicantDocumentModel().getSecondary_document_id());
//                secondaryIdET.setText(body.getLoanTakerModel().getApplicantDocumentModel().getSecondaryIDModel().getName());
                UiUtils.setMarkedNotAvailable(ApplicantDocumentActivity.this,secondaryIdNotAvailable);
                secondaryIdStatusTv.setVisibility(View.GONE);
            }else if (secondary_document == "false"){

                secondaryDummyImage.setVisibility(View.GONE);
                secondaryGrid.setVisibility(View.VISIBLE);

                //file uploaded
                isValidSecondaryItemSelected=true;
                isSecondaryDocumentsUploaded=true;
                UiUtils.setDocumentUploaded(ApplicantDocumentActivity.this,secondaryIdStatusTv);

                Log.i("Shahana", "sec - id" + body.getLoanTakerModel().getApplicantDocumentModel().getSecondary_document_id());
                Log.i("Shahana", "sec - name" + body.getLoanTakerModel().getApplicantDocumentModel().getSecondaryIDModel().getName());

                secondaryIDValue = Integer.parseInt(body.getLoanTakerModel().getApplicantDocumentModel().getSecondary_document_id());
                secondaryIdNotAvailable.setVisibility(View.GONE);


                for(int i=0;i<cacdSecondaryIDModelArrayListNew.size();i++){
                    if(cacdSecondaryIDModelArrayListNew.get(i).getId().equalsIgnoreCase(body.getLoanTakerModel().getApplicantDocumentModel().getSecondaryIDModel().getId())){
                        secondarySpinner.setSelection(i);
                    }
                }



                ArrayList<String> result_secondary = new ArrayList<>();
                secondaryIdArrayList = new ArrayList<>();


                Log.i("Shahana","-Sec-"+body.getLoanTakerModel().getApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().size());

                for(int i=0;i<body.getLoanTakerModel().getApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().size();i++){
                    Log.i("Shahana","-Sec-"+body.getLoanTakerModel().getApplicantDocumentModel()
                            .getSecondaryDocumentImageModelArrayList().get(i)
                            .getSecondaryDocumentImageModel().getUploadImages().getMedium());

                    result_secondary.add(body.getLoanTakerModel().getApplicantDocumentModel()
                            .getSecondaryDocumentImageModelArrayList().get(i)
                            .getSecondaryDocumentImageModel().getUploadImages().getOriginal());


                    secondaryIdArrayList.add(body.getLoanTakerModel().getApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().get(i).
                            getSecondaryDocumentImageModel().getId());

                }
                secondaryGrid.setNetworkPhotosWithKey(result_secondary);
            }


            //others

            if(body.getLoanTakerModel().getApplicantDocumentModel().getOtherLoanCardImagesModelArrayList().size()>0){

                UiUtils.setDocumentUploaded(ApplicantDocumentActivity.this,otherStatusTv);
                ArrayList<String> result_others = new ArrayList<>();
                otherIdArrayList = new ArrayList<>();

                for(int i=0;i<body.getLoanTakerModel().getApplicantDocumentModel().getOtherLoanCardImagesModelArrayList().size();i++){
                    Log.i("Shahana","-Others-"+body.getLoanTakerModel().getApplicantDocumentModel()
                            .getOtherLoanCardImagesModelArrayList().get(i)
                            .getOtherLoanCardImageModel().getUploadImages().getMedium());

                    result_others.add(body.getLoanTakerModel().getApplicantDocumentModel()
                            .getOtherLoanCardImagesModelArrayList().get(i)
                            .getOtherLoanCardImageModel().getUploadImages().getOriginal());
                    otherIdArrayList.add(body.getLoanTakerModel().getApplicantDocumentModel().getOtherLoanCardImagesModelArrayList().get(i).
                            getOtherLoanCardImageModel().getId());

                }
                otherLoanCardGrid.setNetworkPhotosWithKey(result_others);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void editShowPopup(final String picUploadedUrl, final String value){
        View view = getLayoutInflater().inflate(R.layout.custom_edit_image_yes_no_dialog, null);
        TextView editTV =(TextView)view.findViewById(R.id.edit_tv);
        TextView showTV =(TextView)view.findViewById(R.id.show_tv);
        Button yesButton =(Button)view.findViewById(R.id.yes_button);



        AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantDocumentActivity.this);
        builder.setCancelable(false)
                .setTitle("Would you like to :")
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
            }
        });
        editTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                if(value.equalsIgnoreCase("profile_pic")){
                    isProfilePicClicked = true;
                    isBankFrontPageClicked = false;
                    isAadharFrontClicked = false;
                    isAadharBackClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(ApplicantDocumentActivity.this);

                }else if(value.equalsIgnoreCase("bank_detail")){
                    isBankFrontPageClicked = true;
                    isProfilePicClicked = false;
                    isAadharFrontClicked = false;
                    isAadharBackClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(ApplicantDocumentActivity.this);
                }else if (value.equalsIgnoreCase("aadhar_front")){
                    isAadharFrontClicked = true;
                    isProfilePicClicked = false;
                    isBankFrontPageClicked = false;
                    isAadharBackClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(ApplicantDocumentActivity.this);

                }else if(value.equalsIgnoreCase("aadhar_back")){
                    isAadharBackClicked = true;
                    isProfilePicClicked = false;
                    isBankFrontPageClicked = false;
                    isAadharFrontClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(ApplicantDocumentActivity.this);
                }


            }
        });

        showTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                ArrayList<PreviewFile> imageList = new ArrayList<>();
                imageList.add(new PreviewFile(picUploadedUrl, ""));
                Intent intent = new Intent(ApplicantDocumentActivity.this,
                        ImagePreviewSingleImageWithEditActivity.class);
                intent.putExtra(ImagePreviewSingleImageWithEditActivity.IMAGE_LIST,
                        imageList);
                intent.putExtra(ImagePreviewSingleImageWithEditActivity.CURRENT_ITEM, "0");
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.contact_person_upload_image:
                if(isProfilePicUploaded){

                    if(profilePicUploadedUrl!=null){
                        editShowPopup(profilePicUploadedUrl,"profile_pic");
                    }

                }else {
                    isProfilePicClicked = true;
                    isBankFrontPageClicked = false;
                    isAadharFrontClicked = false;
                    isAadharBackClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
                }
                break;

            case R.id.bank_detail_first_page_image:
                if(isBankDetailFirstPageUploaded){

                    if(bankDetailFirstPageUploadedUrl!=null){
                        editShowPopup(bankDetailFirstPageUploadedUrl,"bank_detail");
                    }

                }else {
                    isBankFrontPageClicked = true;
                    isProfilePicClicked = false;
                    isAadharFrontClicked = false;
                    isAadharBackClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
                }
                break;

            case R.id.aadhar_card_front_image:
                if(isAadharFrontUploaded){

                    if(aadharFrontUploadedUrl!=null){
                        editShowPopup(aadharFrontUploadedUrl,"aadhar_front");
                    }

                }else {
                    isAadharFrontClicked = true;
                    isProfilePicClicked = false;
                    isBankFrontPageClicked = false;
                    isAadharBackClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
                }
                break;


            case R.id.aadhar_card_back_image:
                if(isAadharBackUploaded){

                    if(aadharBackUploadedUrl!=null){
                        editShowPopup(aadharBackUploadedUrl,"aadhar_back");
                    }

                }else {
                    isAadharBackClicked = true;
                    isProfilePicClicked = false;
                    isBankFrontPageClicked = false;
                    isAadharFrontClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
                }
                break;

            case R.id.bank_detail_applicant_not_available:
                updateBankDetailNotAvailable();
                break;

            case R.id.aadhar_card_front_document_not_available:
                updateAadharFrontNotAvailable();
                break;

            case R.id.aadhar_card_back_document_not_available:
                updateAadharBackNotAvailable();
                break;

            case R.id.one_year_bank_statement_document_not_available:
                updateBankStatementNotAvailable();
                break;

            case R.id.secondary_id_document_not_available:
//                if(isValidSecondaryId){
                updateSecondaryIDNotAvailable();
//                }else{
//                    Toast.makeText(this, "Please choose a valid Secondary Document", Toast.LENGTH_SHORT).show();
//                }

                break;

            case R.id.secondary_dummy_id_image:
                Toast.makeText(this, "Please choose a valid secondary ID", Toast.LENGTH_SHORT).show();
                break;

            case R.id.continue_button:
                checkValidationForImages();
                break;

        }
    }



    private void checkValidationForImages() {

        if((isValidProfilePic) && (isValidBankFrontPage) && (isValidBankStatement) &&
                (isValidAadharFront) && (isValidAadharBack) && (isValidSecondaryItemSelected)){


            goToCoApplicantPage();
            updateTheStatusInApplicantDetailPage();

            Bundle params = new Bundle();
            params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
            params.putString("status","applicant_documents_uploaded");
            mFirebaseAnalytics.logEvent("applicant_documents", params);

//            goToGroupListingPage();
//            updateTheStatusInApplicantDetailPage();

        }else{

            if(!isValidProfilePic){
                UiUtils.setDocumentNotUploaded(ApplicantDocumentActivity.this,profilePicStatusTV);

            }
            if(!isValidBankFrontPage){
                UiUtils.setDocumentNotUploaded(ApplicantDocumentActivity.this,bankDetailStatusTv);
            }

            if(!isValidBankStatement){
                UiUtils.setDocumentNotUploaded(ApplicantDocumentActivity.this,oneYearBankStatementStatusTv);
            }

            if(!isValidAadharFront){
                UiUtils.setDocumentNotUploaded(ApplicantDocumentActivity.this,aadharCardFrontStatusTv);
            }

            if(!isValidAadharBack){
                UiUtils.setDocumentNotUploaded(ApplicantDocumentActivity.this,aadharCardBackStatusTv);
            }

            if(!isValidSecondaryItemSelected){
                UiUtils.setDocumentNotUploaded(ApplicantDocumentActivity.this,secondaryIdStatusTv);
            }

        }

    }

    private void goToCoApplicantPage() {

        Intent intent = new Intent(ApplicantDocumentActivity.this,CoApplicantDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("applicant","finished");
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }

    private void goToGroupListingPage() {

        Intent intent = new Intent(ApplicantDocumentActivity.this,GroupMemberListingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void updateTheStatusInApplicantDetailPage() {
        ApplicantDetailActivity.getInstance().init();
        MemberDetailActivity.getInstance().init();
    }

    private void updateBankDetailNotAvailable() {
        try {
            Log.d("Bank  image", "start");
            //For Image
            RequestBody available_value = RequestBody.create(MediaType.parse("text/plain"),  "true");
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerApplicantDocumentDetailResponseModel> call;
            call = apiService.updateDocumentValidationFOrBankDetailFirstPage(
                    loanTakerID,
                    available_value);

            call.enqueue(new Callback<LoanTakerApplicantDocumentDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Response <LoanTakerApplicantDocumentDetailResponseModel> response) {
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            isValidBankFrontPage=true;
                            bankDetailStatusTv.setVisibility(View.GONE);
                            UiUtils.setMarkedNotAvailable(ApplicantDocumentActivity.this,bankDetailNotAvailable);
                            Toast.makeText(ApplicantDocumentActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDocumentActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void updateBankStatementNotAvailable() {
        try {
            Log.d("Bank  image", "start");
            //For Image
            RequestBody available_value = RequestBody.create(MediaType.parse("text/plain"),  "true");
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerApplicantDocumentDetailResponseModel> call;
            call = apiService.updateDocumentValidationForOneYearBankStatement(
                    loanTakerID,
                    available_value);

            call.enqueue(new Callback<LoanTakerApplicantDocumentDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Response <LoanTakerApplicantDocumentDetailResponseModel> response) {
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            isValidBankStatement=true;
                            oneYearBankStatementStatusTv.setVisibility(View.GONE);
                            UiUtils.setMarkedNotAvailable(ApplicantDocumentActivity.this,oneYearBankStatementNotAvailable);
                            Toast.makeText(ApplicantDocumentActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDocumentActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void updateAadharFrontNotAvailable() {
        try {
            Log.d("Bank  image", "start");
            //For Image
            RequestBody available_value = RequestBody.create(MediaType.parse("text/plain"),  "true");
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerApplicantDocumentDetailResponseModel> call;
            call = apiService.updateDocumentValidationFOrAadharFront(
                    loanTakerID,
                    available_value);

            call.enqueue(new Callback<LoanTakerApplicantDocumentDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Response <LoanTakerApplicantDocumentDetailResponseModel> response) {

                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            isValidAadharFront=true;
                            aadharCardFrontStatusTv.setVisibility(View.GONE);
                            UiUtils.setMarkedNotAvailable(ApplicantDocumentActivity.this,aadharCardFrontNotAvailable);
                            Toast.makeText(ApplicantDocumentActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDocumentActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void updateAadharBackNotAvailable() {
        try {
            Log.d("Bank  image", "start");
            //For Image
            RequestBody available_value = RequestBody.create(MediaType.parse("text/plain"),  "true");
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerApplicantDocumentDetailResponseModel> call;
            call = apiService.updateDocumentValidationFOrAadharBack(
                    loanTakerID,
                    available_value);

            call.enqueue(new Callback<LoanTakerApplicantDocumentDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Response <LoanTakerApplicantDocumentDetailResponseModel> response) {
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            isValidAadharBack=true;
                            aadharCardBackStatusTv.setVisibility(View.GONE);
                            UiUtils.setMarkedNotAvailable(ApplicantDocumentActivity.this,aadharCardBackNotAvailable);
                            Toast.makeText(ApplicantDocumentActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDocumentActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void updateSecondaryIDNotAvailable() {
        try {
            Log.d("Bank  image", "start");
            //For Image
            RequestBody available_value = RequestBody.create(MediaType.parse("text/plain"),  "true");
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerApplicantDocumentDetailResponseModel> call;
            call = apiService.updateDocumentValidationFOrSecondaryID(
                    loanTakerID,
                    available_value);

            call.enqueue(new Callback<LoanTakerApplicantDocumentDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Response <LoanTakerApplicantDocumentDetailResponseModel> response) {
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            isValidSecondaryItemSelected=true;
                            secondaryIdStatusTv.setVisibility(View.GONE);
                            UiUtils.setMarkedNotAvailable(ApplicantDocumentActivity.this,secondaryIdNotAvailable);
                            Toast.makeText(ApplicantDocumentActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDocumentActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                if(isProfilePicClicked){
                    isValidProfilePic =true;
                    showProgressDialog(ApplicantDocumentActivity.this);
                    new AsyncTaskRunnerForProfilePic().execute(String.valueOf(result.getUri()));
                }else if (isBankFrontPageClicked){
                    showProgressDialog(ApplicantDocumentActivity.this);
                    isValidBankFrontPage = true;
                    new AsyncTaskRunnerForBankFrontPage().execute(String.valueOf(result.getUri()));
                }else if(isAadharFrontClicked){
                    showProgressDialog(ApplicantDocumentActivity.this);
                    isValidAadharFront = true;
                    new AsyncTaskRunnerForAadharFront().execute(String.valueOf(result.getUri()));
                }else if(isAadharBackClicked){
                    showProgressDialog(ApplicantDocumentActivity.this);
                    new AsyncTaskRunnerForAadharBack().execute(String.valueOf(result.getUri()));
                    isValidAadharBack = true;
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }else {
            if(data!=null){

                ArrayList<String> deletedSeondaryIdsArrayList=new ArrayList<>();
                ArrayList<String> deletedBankIdsArrayList=new ArrayList<>();
                ArrayList<String> deletedOtherLoanIdsArrayList=new ArrayList<>();

                onYearBankStatementGrid.onParentResult(requestCode, data);
                secondaryGrid.onParentResult(requestCode, data);
                otherLoanCardGrid.onParentResult(requestCode, data);



                Log.d("Shahana", "isSecon"+isSecondaryClicked+"--"+isBankCLicked+"--"+data);



                if (resultCode==5){
                    Log.d("Shahana", "secondary");

                    if(data!=null) {

                        List<Image> images = (List<Image>) data.getSerializableExtra(NavigatorImage.EXTRA_PHOTOS_URL);
                        ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                        if (requestCode == NavigatorImage.RESULT_SELECT_PHOTOS && null != images) {
                            secondarySelectedImages = images;
                            Log.i("Shahana", "size" + images.size());
                            for (Image image : images) {
                                Log.i("Shahana", "size path" + image.url);

                            }
                            isValidSecondaryItemSelected = true;
                            showProgressDialog(ApplicantDocumentActivity.this);
                            saveMultipleSecondaryDocuments();

                        }else if (requestCode == NavigatorImage.RESULT_IMAGE_SWITCHER && null != positions && resultCode==5) {


                        }
                    }


                }else if (resultCode==6){
                    Log.d("Shahana", "other");
                    if(data!=null) {
                        List<Image> images = (List<Image>) data.getSerializableExtra(NavigatorImage.EXTRA_PHOTOS_URL);
                        ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                        if (requestCode == NavigatorImage.RESULT_SELECT_PHOTOS && null != images) {

                            otherSelectedImages = images;
                            Log.i("Shahana", "size" + images.size());
                            for (Image image : images) {
                                Log.i("Shahana", "size path" + image.url);

                            }

                            showProgressDialog(ApplicantDocumentActivity.this);
                            saveMultipleOtherDocuments();

                        }
                    }


                }else if(resultCode ==7) {
                    Log.d("Shahana", "bank");

                    List<Image> images = (List<Image>) data.getSerializableExtra(NavigatorImage.EXTRA_PHOTOS_URL);
                    ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                    if (requestCode == NavigatorImage.RESULT_SELECT_PHOTOS && null != images) {

                        bankStatementSelectedImages = images;
                        Log.i("Shahana", "size" + images.size());
                        for (Image image : images) {
                            Log.i("Shahana", "size path" + image.url);

                        }

                        isValidBankStatement = true;
                        showProgressDialog(ApplicantDocumentActivity.this);
                        saveMultipleBankStatements();

                    }
                }else{


                    if(data!=null) {

                        if(isSecondaryClicked){
                            ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                            Log.d("Shahana", "delete secondary");
                            Log.i("Suhail", "positionss sizes" + positions.size());
                            Log.i("Suhail", "positionsss" + positions);

                            if(positions.size()>0){
                                for (int position : positions) {
                                    Log.i("Suhail", "removed pos" + position);
                                    for(int j=0;j<secondaryIdArrayList.size();j++){
                                        Log.i("Suhail", "removed"+ j + secondaryIdArrayList.get(j));

                                        if(position==j){
                                            Log.i("Suhail", "delete"+secondaryIdArrayList.get(j) );

                                            deletedSeondaryIdsArrayList.add(secondaryIdArrayList.get(j));

                                        }

                                    }

                                }

                                if(deletedSeondaryIdsArrayList.size()>0){
                                    Log.i("Suhail", "removed success");
                                    deleteSecondaryDocuments(deletedSeondaryIdsArrayList);
                                }


                            }

                        }else if(isBankCLicked){
                            Log.d("Shahana", "delete bank");
                            ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                            Log.d("Shahana", "delete bank");
                            Log.i("Suhail", "positionss sizes" + positions.size());
                            Log.i("Suhail", "positionsss" + positions);

                            if(positions.size()>0){
                                for (int position : positions) {
                                    Log.i("Suhail", "removed pos" + position);
                                    for(int j=0;j<bankIdArrayList.size();j++){
                                        Log.i("Suhail", "removed"+ j + bankIdArrayList.get(j));

                                        if(position==j){
                                            Log.i("Suhail", "delete"+bankIdArrayList.get(j) );

                                            deletedBankIdsArrayList.add(bankIdArrayList.get(j));

                                        }

                                    }

                                }

                                if(deletedBankIdsArrayList.size()>0){
                                    Log.i("Suhail", "removed success");
                                    deleteBankDocuments(deletedBankIdsArrayList);
                                }


                            }

                        }else if(isOtherLoanCardClicked){
                            Log.d("Shahana", "delete loan card");
                            ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                            Log.d("Shahana", "delete bank");
                            Log.i("Suhail", "positionss sizes" + positions.size());
                            Log.i("Suhail", "positionsss" + positions);

                            if(positions.size()>0){
                                for (int position : positions) {
                                    Log.i("Suhail", "removed pos" + position);
                                    for(int j=0;j<otherIdArrayList.size();j++){
                                        Log.i("Suhail", "removed"+ j + otherIdArrayList.get(j));

                                        if(position==j){
                                            Log.i("Suhail", "delete"+otherIdArrayList.get(j) );

                                            deletedOtherLoanIdsArrayList.add(otherIdArrayList.get(j));

                                        }

                                    }

                                }

                                if(deletedOtherLoanIdsArrayList.size()>0){
                                    Log.i("Suhail", "removed success");
                                    deleteOtherLoanDocuments(deletedOtherLoanIdsArrayList);
                                }


                            }
                        }



                    }
                }
            }



        }
    }

    private void deleteOtherLoanDocuments(ArrayList<String> deletedIdsArrayList) {

        showProgressDialog(ApplicantDocumentActivity.this);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);


        for(int i=0;i<deletedIdsArrayList.size();i++){

            Log.d("Aadhar onResponse", "" + "document[other_loan_card_images_attributes]["+i+"][_destroy]");

            Log.d("Aadhar onResponse", "" + deletedIdsArrayList.get(i));

            builder.addFormDataPart("document[other_loan_card_images_attributes]["+i+"][_destroy]", "true");
            builder.addFormDataPart("document[other_loan_card_images_attributes]["+i+"][id]", deletedIdsArrayList.get(i));
        }


        RequestBody finalRequestBody = builder.build();
        ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
        Call<LoanTakerApplicantDocumentDetailResponseModel> call = apiService.deleteApplicantSeondaryMultipleImages(loanTakerID,finalRequestBody
        );
        call.enqueue(new Callback<LoanTakerApplicantDocumentDetailResponseModel>() {
            @Override
            public void onResponse(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Response<LoanTakerApplicantDocumentDetailResponseModel> response) {
                hideProgressDialog();
                Log.d("Aadhar onResponse", "" + response.code() + "--" + response.message());
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        //API Success is true
                        for(int i=0;i<response.body().getLoanTakerModel().getApplicantDocumentModel().getOtherLoanCardImagesModelArrayList().size();i++) {
                            otherIdArrayList.remove(response.body().getLoanTakerModel().getApplicantDocumentModel().getOtherLoanCardImagesModelArrayList().get(i).
                                    getOtherLoanCardImageModel().getId());
                        }
                        Toast.makeText(ApplicantDocumentActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        if(response.body().getLoanTakerModel().getApplicantDocumentModel().getOtherLoanCardImagesModelArrayList().size()==0){

                        }else{
                            UiUtils.setDocumentUploaded(ApplicantDocumentActivity.this,otherStatusTv);
                        }
                    } else {
                        Log.d("Aadhar onResponse", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDocumentActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Aadhar onResponse", "" + response.body().getErrors());
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                hideProgressDialog();
                NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
            }
        });
    }

    private void deleteBankDocuments(ArrayList<String> deletedIdsArrayList) {

        showProgressDialog(ApplicantDocumentActivity.this);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);


        for(int i=0;i<deletedIdsArrayList.size();i++){

            Log.d("Aadhar onResponse", "" + "document[bank_statement_images_attributes]["+i+"][_destroy]");

            Log.d("Aadhar onResponse", "" + deletedIdsArrayList.get(i));

            builder.addFormDataPart("document[bank_statement_images_attributes]["+i+"][_destroy]", "true");
            builder.addFormDataPart("document[bank_statement_images_attributes]["+i+"][id]", deletedIdsArrayList.get(i));
        }


        RequestBody finalRequestBody = builder.build();
        ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
        Call<LoanTakerApplicantDocumentDetailResponseModel> call = apiService.deleteApplicantSeondaryMultipleImages(loanTakerID,finalRequestBody
        );
        call.enqueue(new Callback<LoanTakerApplicantDocumentDetailResponseModel>() {
            @Override
            public void onResponse(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Response<LoanTakerApplicantDocumentDetailResponseModel> response) {
                hideProgressDialog();
                Log.d("Aadhar onResponse", "" + response.code() + "--" + response.message());
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        //API Success is true
                        for(int i=0;i<response.body().getLoanTakerModel().getApplicantDocumentModel().getBankStatementImagesModelArrayList().size();i++) {
                            bankIdArrayList.remove(response.body().getLoanTakerModel().getApplicantDocumentModel().getBankStatementImagesModelArrayList().get(i).
                                    getBankStatementImageModel().getId());
                        }
                        Toast.makeText(ApplicantDocumentActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        if(response.body().getLoanTakerModel().getApplicantDocumentModel().getBankStatementImagesModelArrayList().size()==0){
                            UiUtils.setDocumentNotUploaded(ApplicantDocumentActivity.this,oneYearBankStatementStatusTv);
                        }else{
                            UiUtils.setDocumentUploaded(ApplicantDocumentActivity.this,oneYearBankStatementStatusTv);
                        }
                    } else {
                        Log.d("Aadhar onResponse", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDocumentActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Aadhar onResponse", "" + response.body().getErrors());
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                hideProgressDialog();
                NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
            }
        });
    }

    private void deleteSecondaryDocuments(ArrayList<String> deletedIdsArrayList) {

        showProgressDialog(ApplicantDocumentActivity.this);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);


        for(int i=0;i<deletedIdsArrayList.size();i++){

            Log.d("Aadhar onResponse", "" + "document[secondary_document_images_attributes]["+i+"][_destroy]");

            Log.d("Aadhar onResponse", "" + deletedIdsArrayList.get(i));

            builder.addFormDataPart("document[secondary_document_images_attributes]["+i+"][_destroy]", "true");
            builder.addFormDataPart("document[secondary_document_images_attributes]["+i+"][id]", deletedIdsArrayList.get(i));
        }


        RequestBody finalRequestBody = builder.build();
        ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
        Call<LoanTakerApplicantDocumentDetailResponseModel> call = apiService.deleteApplicantSeondaryMultipleImages(loanTakerID,finalRequestBody
        );
        call.enqueue(new Callback<LoanTakerApplicantDocumentDetailResponseModel>() {
            @Override
            public void onResponse(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Response<LoanTakerApplicantDocumentDetailResponseModel> response) {
                hideProgressDialog();
                Log.d("Aadhar onResponse", "" + response.code() + "--" + response.message());
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        //API Success is true
                        for(int i=0;i<response.body().getLoanTakerModel().getApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().size();i++) {
                            secondaryIdArrayList.remove(response.body().getLoanTakerModel().getApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().get(i).
                                    getSecondaryDocumentImageModel().getId());
                        }
                        Toast.makeText(ApplicantDocumentActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        if(response.body().getLoanTakerModel().getApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().size()==0){
                            UiUtils.setDocumentNotUploaded(ApplicantDocumentActivity.this,secondaryIdStatusTv);
                            isValidSecondaryItemSelected=false;
                        }else{
                            UiUtils.setDocumentUploaded(ApplicantDocumentActivity.this,secondaryIdStatusTv);
                        }
                    } else {
                        Log.d("Aadhar onResponse", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDocumentActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Aadhar onResponse", "" + response.body().getErrors());
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                hideProgressDialog();
                NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
            }
        });
    }

    private void saveMultipleOtherDocuments() {


         UiUtils.setDocumentUploading(ApplicantDocumentActivity.this,otherStatusTv);

        try {
            Log.d("Drools bank image", "start");
            //For Image
            File file = null;
            MultipartBody.Part[] fileToUpload = new MultipartBody.Part[otherSelectedImages.size()];
            if (otherSelectedImages != null) {
                for (int i=0;i<otherSelectedImages.size();i++) {
                    Log.i("Drools","size path"+ otherSelectedImages.get(i).url);
                    Log.i("Drools","document[other_loan_card_images_attributes][" + i + "][image]");
//                    file = new File(decodeFile(otherSelectedImages.get(i).url,desiredWidth,desiredHeight));
                    file= new File(otherSelectedImages.get(i).url);
                    File compressedImageFile = new Compressor(this).compressToFile(file);
                    RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile);
                    fileToUpload[i] = MultipartBody.Part.createFormData("document[other_loan_card_images_attributes][" + i + "][image]", file.getName(), mFile);

                }
            }

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerApplicantDocumentDetailResponseModel> call;
            call = apiService.addApplicantMultipleImage(
                    loanTakerID,
                    fileToUpload);

            call.enqueue(new Callback<LoanTakerApplicantDocumentDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Response <LoanTakerApplicantDocumentDetailResponseModel> response) {
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                             UiUtils.setDocumentUploaded(ApplicantDocumentActivity.this,otherStatusTv);
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

//                            reload();
//                            Toast.makeText(ApplicantDocumentActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
//                            init();
                            secondaryIDSpinnerAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDocumentActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMultipleBankStatements() {

        oneYearBankStatementNotAvailable.setVisibility(View.GONE);
         UiUtils.setDocumentUploading(ApplicantDocumentActivity.this,oneYearBankStatementStatusTv);

        try {
            Log.d("Drools bank image", "start");
            //For Image
            File file = null;
            MultipartBody.Part[] fileToUpload = new MultipartBody.Part[bankStatementSelectedImages.size()];
            if (bankStatementSelectedImages != null) {
                for (int i=0;i<bankStatementSelectedImages.size();i++) {
                    Log.i("Drools","size path"+ bankStatementSelectedImages.get(i).url);
                    Log.i("Drools","document[bank_statement_images_attributes][" + i + "][image]");
//                    file = new File(decodeFile(bankStatementSelectedImages.get(i).url,desiredWidth,desiredHeight));
                    file= new File(bankStatementSelectedImages.get(i).url);
                    File compressedImageFile = new Compressor(this).compressToFile(file);
                    RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile);
                    fileToUpload[i] = MultipartBody.Part.createFormData("document[bank_statement_images_attributes][" + i + "][image]", file.getName(), mFile);

                }
            }

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerApplicantDocumentDetailResponseModel> call;
            call = apiService.addApplicantMultipleImage(
                    loanTakerID,
                    fileToUpload);

            call.enqueue(new Callback<LoanTakerApplicantDocumentDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Response <LoanTakerApplicantDocumentDetailResponseModel> response) {
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                             UiUtils.setDocumentUploaded(ApplicantDocumentActivity.this,oneYearBankStatementStatusTv);
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

//                            reload();
//                            Toast.makeText(ApplicantDocumentActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDocumentActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMultipleSecondaryDocuments() {

        secondaryIdNotAvailable.setVisibility(View.GONE);
         UiUtils.setDocumentUploading(ApplicantDocumentActivity.this,secondaryIdStatusTv);
        MultipartBody.Part[] fileToUpload = new MultipartBody.Part[0];

        try {
            Log.d("Drools Sec image", "start");
            //For Image
            File file = null;
          
            if (secondarySelectedImages != null) {
                fileToUpload = new MultipartBody.Part[secondarySelectedImages.size()];
                for (int i=0;i<secondarySelectedImages.size();i++) {
                    Log.i("Drools","size path"+ secondarySelectedImages.get(i).url);
//                    file = new File(decodeFile(secondarySelectedImages.get(i).url,desiredWidth,desiredHeight));
                    file= new File(secondarySelectedImages.get(i).url);
                    File compressedImageFile = new Compressor(this).compressToFile(file);
                    RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile);
                    fileToUpload[i] = MultipartBody.Part.createFormData("document[secondary_document_images_attributes][" + i + "][image]", file.getName(), mFile);

                }
            }

            RequestBody secondary_id = RequestBody.create(MediaType.parse("text/plain"),  String.valueOf(secondaryIDValue));
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerApplicantDocumentDetailResponseModel> call;

            if(secondarySelectedImages!=null){
                call = apiService.addApplicantSecondaryMultipleImage(
                        loanTakerID,
                        fileToUpload,secondary_id);
            }else{
                call = apiService.addApplicantSecondaryWithoutMultipleImage(
                        loanTakerID,
                        secondary_id);
            }


            call.enqueue(new Callback<LoanTakerApplicantDocumentDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Response <LoanTakerApplicantDocumentDetailResponseModel> response) {
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            isSecondaryDocumentsUploaded=true;
                            for(int i=0;i<response.body().getLoanTakerModel().getApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().size();i++) {
                                secondaryIdArrayList.add(response.body().getLoanTakerModel().getApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().get(i).
                                        getSecondaryDocumentImageModel().getId());
                            }
                             UiUtils.setDocumentUploaded(ApplicantDocumentActivity.this,secondaryIdStatusTv);
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

//                            reload();
//                            Toast.makeText(ApplicantDocumentActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDocumentActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class AsyncTaskRunnerForProfilePic extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {

                Uri myUri = Uri.parse(params[0]);
                ((ImageView) findViewById(R.id.contact_person_upload_image)).setImageURI(myUri);
            }  catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

            Bitmap bm=((BitmapDrawable)profilePhoto.getDrawable()).getBitmap();
            saveImageFile(bm);
            saveProfilePic();
            isProfilePicClicked = false;
        }

        @Override
        protected void onPreExecute() {
            profilePicProgressBar.setVisibility(View.VISIBLE);
             UiUtils.setDocumentUploading(ApplicantDocumentActivity.this,profilePicStatusTV);
        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

    private class AsyncTaskRunnerForBankFrontPage extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {
                Uri myUri = Uri.parse(params[0]);
                ((ImageView) findViewById(R.id.bank_detail_first_page_image)).setImageURI(myUri);
            }  catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

            Bitmap bm=((BitmapDrawable)bankDetailFirstPage.getDrawable()).getBitmap();
            saveImageFile(bm);
            saveBankDetailFirstPage();
            isBankFrontPageClicked = false;
        }


        @Override
        protected void onPreExecute() {
            bankDetailFirstPageProgressBar.setVisibility(View.VISIBLE);
             UiUtils.setDocumentUploading(ApplicantDocumentActivity.this,bankDetailStatusTv);
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

    private class AsyncTaskRunnerForAadharFront extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {

                Uri myUri = Uri.parse(params[0]);
                ((ImageView) findViewById(R.id.aadhar_card_front_image)).setImageURI(myUri);
            }  catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

            Bitmap bm=((BitmapDrawable)aadharCardFrontImage.getDrawable()).getBitmap();
            saveImageFile(bm);
            saveAadharFront();
            isAadharFrontClicked = false;
        }

        @Override
        protected void onPreExecute() {
            aadharCardFrontProgressBar.setVisibility(View.VISIBLE);
             UiUtils.setDocumentUploading(ApplicantDocumentActivity.this,aadharCardFrontStatusTv);
        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

    private class AsyncTaskRunnerForAadharBack extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {
                Uri myUri = Uri.parse(params[0]);
                ((ImageView) findViewById(R.id.aadhar_card_back_image)).setImageURI(myUri);
            }  catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

            Bitmap bm=((BitmapDrawable)aadharCardBackImage.getDrawable()).getBitmap();
            saveImageFile(bm);
            saveAadharBack();
            isAadharBackClicked = false;
        }


        @Override
        protected void onPreExecute() {
            aadharCardBackProgressBar.setVisibility(View.VISIBLE);
             UiUtils.setDocumentUploading(ApplicantDocumentActivity.this,aadharCardBackStatusTv);
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

    private void saveProfilePic() {

//         UiUtils.setDocumentUploading(ApplicantDocumentActivity.this,profilePicStatusTV);
        try {
            Log.d("Profile image", "start");
            //For Image
            File file = null;
            MultipartBody.Part fileToUpload = null;
            if (commonUriPath != null) {
//                file = new File(decodeFile(commonUriPath,desiredWidth,desiredHeight));
                file= new File(commonUriPath);
                File compressedImageFile = new Compressor(this).compressToFile(file);
                RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile);
                fileToUpload = MultipartBody.Part.createFormData("document[profile_image]", file.getName(), mFile);
            }

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerApplicantDocumentDetailResponseModel> call;
            call = apiService.addProfileImage(
                    loanTakerID,
                    fileToUpload);

            call.enqueue(new Callback<LoanTakerApplicantDocumentDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Response <LoanTakerApplicantDocumentDetailResponseModel> response) {
                    profilePicProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                             UiUtils.setDocumentUploaded(ApplicantDocumentActivity.this,profilePicStatusTV);
                            isProfilePicUploaded=true;
                            profilePicUploadedUrl = response.body().getLoanTakerModel().getApplicantDocumentModel().getProfileImages().getOriginal();

                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

//                            reload();
//                            Toast.makeText(ApplicantDocumentActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDocumentActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    profilePicProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBankDetailFirstPage() {

        //  UiUtils.setDocumentUploading(ApplicantDocumentActivity.this,bankDetailStatusTv);

        try {
            Log.d("Bank  image", "start");
            //For Image
            File file = null;
            MultipartBody.Part fileToUpload = null;
            if (commonUriPath != null) {
//                file = new File(decodeFile(commonUriPath,desiredWidth,desiredHeight));
                file= new File(commonUriPath);
                File compressedImageFile = new Compressor(this).compressToFile(file);
                RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile);
                fileToUpload = MultipartBody.Part.createFormData("document[bank_detail_image]", file.getName(), mFile);
            }

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerApplicantDocumentDetailResponseModel> call;
            call = apiService.addProfileImage(
                    loanTakerID,
                    fileToUpload);

            call.enqueue(new Callback<LoanTakerApplicantDocumentDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Response <LoanTakerApplicantDocumentDetailResponseModel> response) {
                    bankDetailFirstPageProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            bankDetailNotAvailable.setVisibility(View.GONE);
                             UiUtils.setDocumentUploaded(ApplicantDocumentActivity.this,bankDetailStatusTv);
                            isBankDetailFirstPageUploaded=true;
                            bankDetailFirstPageUploadedUrl = response.body().getLoanTakerModel().getApplicantDocumentModel().getBank_detail_images().getOriginal();

                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

//                            reload();
//                            Toast.makeText(ApplicantDocumentActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDocumentActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    bankDetailFirstPageProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveAadharFront() {

//         UiUtils.setDocumentUploading(ApplicantDocumentActivity.this,aadharCardFrontStatusTv);
        try {
            Log.d("Bank  image", "start");
            //For Image
            File file = null;
            MultipartBody.Part fileToUpload = null;
            if (commonUriPath != null) {
//                file = new File(decodeFile(commonUriPath,desiredWidth,desiredHeight));
                file= new File(commonUriPath);
                File compressedImageFile = new Compressor(this).compressToFile(file);
                RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile);
                fileToUpload = MultipartBody.Part.createFormData("document[aadhar_front_image]", file.getName(), mFile);
            }

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerApplicantDocumentDetailResponseModel> call;
            call = apiService.addProfileImage(
                    loanTakerID,
                    fileToUpload);

            call.enqueue(new Callback<LoanTakerApplicantDocumentDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Response <LoanTakerApplicantDocumentDetailResponseModel> response) {
                    aadharCardFrontProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            aadharCardFrontNotAvailable.setVisibility(View.GONE);
                             UiUtils.setDocumentUploaded(ApplicantDocumentActivity.this,aadharCardFrontStatusTv);
                            isAadharFrontUploaded=true;
                            aadharFrontUploadedUrl = response.body().getLoanTakerModel().getApplicantDocumentModel().getAadhar_front_images().getOriginal();

                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            reload();
//                            Toast.makeText(ApplicantDocumentActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDocumentActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    aadharCardFrontProgressBar.setVisibility(View.GONE);
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveAadharBack() {



        try {
            Log.d("Bank  image", "start");
            //For Image
            File file = null;
            MultipartBody.Part fileToUpload = null;
            if (commonUriPath != null) {
//                file = new File(decodeFile(commonUriPath,desiredWidth,desiredHeight));
                file= new File(commonUriPath);
                File compressedImageFile = new Compressor(this).compressToFile(file);
                RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile);
                fileToUpload = MultipartBody.Part.createFormData("document[aadhar_back_image]", file.getName(), mFile);
            }

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerApplicantDocumentDetailResponseModel> call;
            call = apiService.addProfileImage(
                    loanTakerID,
                    fileToUpload);

            call.enqueue(new Callback<LoanTakerApplicantDocumentDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Response <LoanTakerApplicantDocumentDetailResponseModel> response) {
                    aadharCardBackProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            aadharCardBackNotAvailable.setVisibility(View.GONE);
                             UiUtils.setDocumentUploaded(ApplicantDocumentActivity.this,aadharCardBackStatusTv);
                            isAadharBackUploaded=true;
                            aadharBackUploadedUrl = response.body().getLoanTakerModel().getApplicantDocumentModel().getAadhar_back_images().getOriginal();
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

//                            reload();
//                            Toast.makeText(ApplicantDocumentActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDocumentActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    aadharCardBackProgressBar.setVisibility(View.GONE);
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDocumentActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String saveImageFile(Bitmap bitmap) {
        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }

    private String getFilename() {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "AadharFolder");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".jpg");

        commonUriPath = uriSting;
        return uriSting;
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Aadhar","Permission is granted");
                return true;
            } else {

                Log.v("Aadhar","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Aadhar","Permission is granted");
            return true;
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



    private String decodeFile(String path,int DESIREDWIDTH, int DESIREDHEIGHT) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);


            Log.d("Aadhaar India im", "uw--"+unscaledBitmap.getWidth());
            Log.d("Aadhaar India im", "uh--"+unscaledBitmap.getHeight());
            Log.d("Aadhaar India im", "dw--"+DESIREDWIDTH);
            Log.d("Aadhaar India im", "dh--"+DESIREDHEIGHT);


            // Part 2: Scale image
            scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);


            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/TMMFOLDER");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = System.currentTimeMillis()+"tmp.png";

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Log.d("Aadhaar India", "PPP--"+fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

    }


    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

//    public void reload() {
//        Intent intent = getIntent();
//        overridePendingTransition(0, 0);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        finish();
//        overridePendingTransition(0, 0);
//        startActivity(intent);
//    }


}
