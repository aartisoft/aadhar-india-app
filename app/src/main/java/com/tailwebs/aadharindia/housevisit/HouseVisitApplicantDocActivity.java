package com.tailwebs.aadharindia.housevisit;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.tailwebs.aadharindia.member.SecondaryDocumentSpinnerAdapter;
import com.tailwebs.aadharindia.member.applicant.spinners.SecondaryIDSpinnerAdapter;
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

public class HouseVisitApplicantDocActivity extends BaseActivity implements View.OnClickListener {

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


    @BindView(R.id.bank_detail_first_page_progress_bar)
    ProgressBar bankDetailFirstPageProgressBar;

    //one year bank statement
    @BindView(R.id.one_year_bank_statement_grid)
    ImageGridView onYearBankStatementGrid;

    @BindView(R.id.one_year_bank_statement_status_tv)
    TextView oneYearBankStatementStatusTv;





    //aadhar card front
    @BindView(R.id.aadhar_card_front_image)
    ImageView aadharCardFrontImage;

    @BindView(R.id.aadhar_card_front_status_tv)
    TextView aadharCardFrontStatusTv;


    @BindView(R.id.aadhar_card_front_progress_bar)
    ProgressBar aadharCardFrontProgressBar;


    //aadhar card back
    @BindView(R.id.aadhar_card_back_image)
    ImageView aadharCardBackImage;

    @BindView(R.id.aadhar_card_back_status_tv)
    TextView aadharCardBackStatusTv;


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


    //other documents
    @BindView(R.id.other_documents_grid)
    ImageGridView otherDocumentsGrid;


    @BindView(R.id.others_documents_tv)
    TextView otherDocumentsTv;


    //address proof
    @BindView(R.id.address_proof_grid)
    ImageGridView addressProofGrid;


    @BindView(R.id.address_proof_tv)
    TextView addressProofStatusTv;



    @BindView(R.id.continue_button)
    Button continueButton;

    private boolean isValidProfilePic = false, isValidBankFrontPage = false, isValidBankStatement = false, isValidAadharFront = false,
            isValidAadharBack = false, isValidSecondaryItemSelected = false, isValidSecondaryId = false, isValidOtherLoan = false,isValidAddressproof = false,
            isSecondaryDocumentsUploaded=false;

    private boolean isProfilePicClicked = false, isBankFrontPageClicked = false, isBankStatementClicked = false, isAadharFrontClicked = false,
            isAadharBackClicked = false,  isValidSecondaryIdClicked = false, isOtherLoanClicked = false;

    String  commonUriPath = null;

    private ProgressDialog mProgressDialog;
    String loanTakerID = null;
    int secondaryIDValue= 0;


    public ArrayList<CACDSecondaryIDModel> cacdSecondaryIDModels = null,cacdSecondaryIDModelArrayListNew;
    SecondaryDocumentSpinnerAdapter secondaryDocumentSpinnerAdapter;

    private FirebaseAnalytics mFirebaseAnalytics;

    List<Image> secondarySelectedImages,otherLoanCardsSelectedImages,bankStatementSelectedImages,otherDocumentsImages,addressProofImages;

    int desiredWidth = 800,desiredHeight = 800,desiredWidthSmall = 300,desiredHeightSmall=300;

    private boolean isProfilePicUploaded = false,isAadharFrontUploaded = false,isAadharBackUploaded=false,isBankDetailFirstPageUploaded=false;
    String profilePicUploadedUrl=null,aadharFrontUploadedUrl=null,aadharBackUploadedUrl=null,bankDetailFirstPageUploadedUrl=null;

    @BindView(R.id.secondary_dummy_id_image)
    ImageView secondaryDummyImage;

    ArrayList<String> secondaryIdArrayList = new ArrayList<>();
    ArrayList<String> otherDocumentsIdArrayList = new ArrayList<>();
    ArrayList<String> bankIdArrayList  = new ArrayList<>();
    ArrayList<String> otherIdArrayList= new ArrayList<>();
    ArrayList<String> addressProofArrayList= new ArrayList<>();

    @BindView(R.id.otherLoanCardLayout)
    LinearLayout otherLoanCardLayout;

    @BindView(R.id.otherDocumentsLayout)
    LinearLayout otherDocumentsLayout;

    @BindView(R.id.addressProofLayout)
    LinearLayout addressProofLayout;

    @BindView(R.id.secondaryLayout)
    LinearLayout secondaryLayout;

    @BindView(R.id.oneYearBankLayout)
    LinearLayout oneYearBankLayout;


    boolean isSecondaryClicked=false,isBankCLicked=false,isOtherLoanCardClicked=false,isAddressProofClicked=false,
            isOtherDocumentsClicked=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_visit_applicant_doc);
        ButterKnife.bind(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        loanTakerID = GlobalValue.loanTakerId;
        isStoragePermissionGranted();

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Application Document Photos");
        headingTV.setTextAppearance(getApplicationContext(), R.style.MyActionBarHeading);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "HV Applicant Docs", null);


        cacdSecondaryIDModels =GlobalValue.applicantSecondaryIDArrayList;
//        secondaryDocumentSpinnerAdapter = new SecondaryDocumentSpinnerAdapter(HouseVisitApplicantDocActivity.this, R.layout.spinner_item,cacdSecondaryIDModels);
//        secondaryIdET.setAdapter(secondaryDocumentSpinnerAdapter);
//        secondaryIdET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Object item = parent.getItemAtPosition(position);
//                if (item instanceof CACDSecondaryIDModel){
//                    final CACDSecondaryIDModel cacdSecondaryIDModel=(CACDSecondaryIDModel) item;
//                    secondaryIdET.setText(cacdSecondaryIDModel.getName());
//                    secondaryIDValue = cacdSecondaryIDModel.getId();
//                    isValidSecondaryId =  true;
//                }
//            }
//        });


        //make grids clickable false

        secondaryGrid.setClickable(false);
        otherLoanCardGrid.setClickable(false);
        otherDocumentsGrid.setClickable(false);
        onYearBankStatementGrid.setClickable(false);
        addressProofGrid.setClickable(false);

        //make layouts clickable tru

        secondaryLayout.setClickable(true);
        secondaryLayout.setEnabled(true);
        otherLoanCardLayout.setClickable(true);
        otherLoanCardLayout.setEnabled(true);
        oneYearBankLayout.setClickable(true);
        oneYearBankLayout.setEnabled(true);
        addressProofLayout.setClickable(true);
        addressProofLayout.setEnabled(true);
        otherDocumentsLayout.setClickable(true);
        otherDocumentsLayout.setEnabled(true);


        //changes in  AlbumActivity for setting values

        secondaryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondaryGrid.setClickable(true);

                isSecondaryClicked=true;
                isBankCLicked=false;
                isOtherLoanCardClicked=false;
                isOtherDocumentsClicked = false;
                isAddressProofClicked = false;

                secondaryGrid.setGRID_TYPE(5);
                secondaryGrid.setItemClicked("secondary");
                secondaryGrid.setMaxValue(GlobalValue.secondaryDocumentImageCount);

                secondaryLayout.setClickable(false);
                secondaryLayout.setEnabled(false);

                //set others
                otherLoanCardGrid.setClickable(false);
                onYearBankStatementGrid.setClickable(false);
                addressProofGrid.setClickable(false);
                otherDocumentsGrid.setClickable(false);

                otherLoanCardLayout.setClickable(true);
                otherLoanCardLayout.setEnabled(true);

                oneYearBankLayout.setClickable(true);
                oneYearBankLayout.setEnabled(true);

                addressProofLayout.setClickable(true);
                addressProofLayout.setEnabled(true);

                otherDocumentsLayout.setClickable(true);
                otherDocumentsLayout.setEnabled(true);
            }
        });


        oneYearBankLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onYearBankStatementGrid.setClickable(true);

                isSecondaryClicked=false;
                isBankCLicked=true;
                isOtherLoanCardClicked=false;
                isOtherDocumentsClicked = false;
                isAddressProofClicked = false;

                onYearBankStatementGrid.setGRID_TYPE(9);
                onYearBankStatementGrid.setItemClicked("bank");
                onYearBankStatementGrid.setMaxValue(GlobalValue.bankStatementImageCount);

                oneYearBankLayout.setClickable(false);
                oneYearBankLayout.setEnabled(false);

                //set others
                otherLoanCardGrid.setClickable(false);
                secondaryGrid.setClickable(false);
                addressProofGrid.setClickable(false);
                otherDocumentsGrid.setClickable(false);

                otherLoanCardLayout.setClickable(true);
                otherLoanCardLayout.setEnabled(true);

                secondaryLayout.setClickable(true);
                secondaryLayout.setEnabled(true);

                addressProofLayout.setClickable(true);
                addressProofLayout.setEnabled(true);

                otherDocumentsLayout.setClickable(true);
                otherDocumentsLayout.setEnabled(true);
            }
        });

        addressProofLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressProofGrid.setClickable(true);

                isSecondaryClicked=false;
                isBankCLicked=false;
                isOtherLoanCardClicked=false;
                isOtherDocumentsClicked = false;
                isAddressProofClicked = true;

                addressProofGrid.setGRID_TYPE(8);
                addressProofGrid.setItemClicked("address");
                addressProofGrid.setMaxValue(GlobalValue.otherLoanCardImageCount);

                addressProofLayout.setClickable(false);
                addressProofLayout.setEnabled(false);

                //set others
                otherLoanCardGrid.setClickable(false);
                secondaryGrid.setClickable(false);
                onYearBankStatementGrid.setClickable(false);
                otherDocumentsGrid.setClickable(false);

                otherLoanCardLayout.setClickable(true);
                otherLoanCardLayout.setEnabled(true);

                secondaryLayout.setClickable(true);
                secondaryLayout.setEnabled(true);

                oneYearBankLayout.setClickable(true);
                oneYearBankLayout.setEnabled(true);

                otherDocumentsLayout.setClickable(true);
                otherDocumentsLayout.setEnabled(true);
            }
        });

        otherDocumentsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherDocumentsGrid.setClickable(true);

                isSecondaryClicked=false;
                isBankCLicked=false;
                isOtherLoanCardClicked=false;
                isOtherDocumentsClicked = true;
                isAddressProofClicked = false;

                otherDocumentsGrid.setGRID_TYPE(7);
                otherDocumentsGrid.setItemClicked("documents");
                otherDocumentsGrid.setMaxValue(GlobalValue.secondaryDocumentImageCount);

                otherDocumentsLayout.setClickable(false);
                otherDocumentsLayout.setEnabled(false);

                //set others
                otherLoanCardGrid.setClickable(false);
                secondaryGrid.setClickable(false);
                onYearBankStatementGrid.setClickable(false);
                addressProofGrid.setClickable(false);

                otherLoanCardLayout.setClickable(true);
                otherLoanCardLayout.setEnabled(true);

                secondaryLayout.setClickable(true);
                secondaryLayout.setEnabled(true);

                oneYearBankLayout.setClickable(true);
                oneYearBankLayout.setEnabled(true);

                addressProofLayout.setClickable(true);
                addressProofLayout.setEnabled(true);
            }
        });

        otherLoanCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherLoanCardGrid.setClickable(true);

                isSecondaryClicked=false;
                isBankCLicked=false;
                isOtherLoanCardClicked=true;
                isOtherDocumentsClicked = false;
                isAddressProofClicked = false;

                otherLoanCardGrid.setGRID_TYPE(6);
                otherLoanCardGrid.setItemClicked("other");
                otherLoanCardGrid.setMaxValue(GlobalValue.otherLoanCardImageCount);

                otherLoanCardLayout.setClickable(false);
                otherLoanCardLayout.setEnabled(false);

                //set others
                otherDocumentsGrid.setClickable(false);
                secondaryGrid.setClickable(false);
                onYearBankStatementGrid.setClickable(false);
                addressProofGrid.setClickable(false);

                otherDocumentsLayout.setClickable(true);
                otherDocumentsLayout.setEnabled(true);

                secondaryLayout.setClickable(true);
                secondaryLayout.setEnabled(true);

                oneYearBankLayout.setClickable(true);
                oneYearBankLayout.setEnabled(true);

                addressProofLayout.setClickable(true);
                addressProofLayout.setEnabled(true);
            }
        });



        profilePhoto.setOnClickListener(this);
        bankDetailFirstPage.setOnClickListener(this);
        aadharCardFrontImage.setOnClickListener(this);
        aadharCardBackImage.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        secondaryDummyImage.setOnClickListener(this);


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


        CACDSecondaryIDModel[] cacdSecondaryIDModels = new CACDSecondaryIDModel[applicantLoanReasonArrayListNew.size()];

        for(int i=0;i<applicantLoanReasonArrayListNew.size();i++){
            cacdSecondaryIDModels[i]= new CACDSecondaryIDModel();
            cacdSecondaryIDModels[i].setId(applicantLoanReasonArrayListNew.get(i).getId());
            cacdSecondaryIDModels[i].setCode(applicantLoanReasonArrayListNew.get(i).getCode());
            cacdSecondaryIDModels[i].setName(applicantLoanReasonArrayListNew.get(i).getName());
        }


        secondaryIDSpinnerAdapter = new SecondaryIDSpinnerAdapter(HouseVisitApplicantDocActivity.this,
                R.layout.ms__list_item,
                cacdSecondaryIDModels);

        secondarySpinner.setAdapter(secondaryIDSpinnerAdapter); // Set the custom adapter to the spinner

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(HouseVisitApplicantDocActivity.this);
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
                            NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitApplicantDocActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
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

                //Document missing .Upload Again in red
                isValidProfilePic=false;
                UiUtils.setDocumentUploadedisMissing(HouseVisitApplicantDocActivity.this,profilePicStatusTV);

                //mark not available
            }else if(profileImage == "true"){
                //mark available
                isValidProfilePic=false;
                UiUtils.setMarkedNotAvailable(HouseVisitApplicantDocActivity.this,profilePicStatusTV);
            }else if (profileImage == "false"){
                //file uploaded
                String profileImageIsCorrect =body.getLoanTakerModel().getApplicantDocumentModel().getProfileImages().getIs_correct();
                if(profileImageIsCorrect==null){

                    UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,profilePicStatusTV);
                    isValidProfilePic=true;

                }else if(profileImageIsCorrect=="true"){

                    UiUtils.setDocumentApproved(HouseVisitApplicantDocActivity.this,profilePicStatusTV);
                    isValidProfilePic=true;

                }else if (profileImageIsCorrect =="false"){

                    isValidProfilePic=false;
                    UiUtils.setDocumentUploadedisRejected(HouseVisitApplicantDocActivity.this,profilePicStatusTV,
                            body.getLoanTakerModel().getApplicantDocumentModel().getProfileImages().getReject_reason());

                }

                isProfilePicUploaded = true;
                profilePicUploadedUrl = body.getLoanTakerModel().getApplicantDocumentModel().getProfileImages().getOriginal();

                Picasso.with(HouseVisitApplicantDocActivity.this)
                        .load(body.getLoanTakerModel().getApplicantDocumentModel().getProfileImages().getMedium())
                        .placeholder(R.drawable.img_placeholder)
                        .into(profilePhoto);
            }




            //bank first page

            String bank_first_page = body.getLoanTakerModel().getApplicantDocumentModel().isBank_detail_image_not_available();
            if( bank_first_page == null){

                //Document missing .Upload Again in red
                isValidBankFrontPage=false;
                UiUtils.setDocumentUploadedisMissing(HouseVisitApplicantDocActivity.this,bankDetailStatusTv);

                //mark not available
            }else if(bank_first_page == "true"){
                //mark available
                isValidBankFrontPage=false;
                UiUtils.setMarkedNotAvailable(HouseVisitApplicantDocActivity.this,bankDetailStatusTv);
            }else if (bank_first_page == "false"){
                //file uploaded
                String bank_first_pageImageIsCorrect =body.getLoanTakerModel().getApplicantDocumentModel().getBank_detail_images().getIs_correct();
                if(bank_first_pageImageIsCorrect==null){

                    UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,bankDetailStatusTv);
                    isValidBankFrontPage=true;

                }else if(bank_first_pageImageIsCorrect=="true"){

                    UiUtils.setDocumentApproved(HouseVisitApplicantDocActivity.this,bankDetailStatusTv);
                    isValidBankFrontPage=true;

                }else if (bank_first_pageImageIsCorrect =="false"){

                    isValidBankFrontPage=false;
                    UiUtils.setDocumentUploadedisRejected(HouseVisitApplicantDocActivity.this,bankDetailStatusTv,
                            body.getLoanTakerModel().getApplicantDocumentModel().getBank_detail_images().getReject_reason());

                }
                isBankDetailFirstPageUploaded=true;
                bankDetailFirstPageUploadedUrl = body.getLoanTakerModel().getApplicantDocumentModel().getBank_detail_images().getOriginal();
                Picasso.with(HouseVisitApplicantDocActivity.this)
                        .load(body.getLoanTakerModel().getApplicantDocumentModel().getBank_detail_images().getMedium())
                        .placeholder(R.drawable.img_placeholder)
                        .into(bankDetailFirstPage);
            }



            //bank statement page

            String bank_statement = body.getLoanTakerModel().getApplicantDocumentModel().isBank_statement_images_not_available();
            if( bank_statement == null){

                //Document missing .Upload Again in red
                isValidBankStatement=false;
                UiUtils.setDocumentUploadedisMissing(HouseVisitApplicantDocActivity.this,oneYearBankStatementStatusTv);

                //mark not available
            }else if(bank_statement == "true"){
                //mark available
                isValidBankStatement=false;
                UiUtils.setMarkedNotAvailable(HouseVisitApplicantDocActivity.this,oneYearBankStatementStatusTv);
            }else if (bank_statement == "false"){
                //file uploaded
                String bank_first_pageImageIsCorrect =body.getLoanTakerModel().getApplicantDocumentModel().getBank_statement_images_is_correct();
                if(bank_first_pageImageIsCorrect==null){

                    UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,oneYearBankStatementStatusTv);
                    isValidBankStatement=true;

                }else if(bank_first_pageImageIsCorrect=="true"){

                    UiUtils.setDocumentApproved(HouseVisitApplicantDocActivity.this,oneYearBankStatementStatusTv);
                    isValidBankStatement=true;

                }else if (bank_first_pageImageIsCorrect =="false"){

                    isValidBankStatement=false;
                    UiUtils.setDocumentUploadedisRejected(HouseVisitApplicantDocActivity.this,oneYearBankStatementStatusTv,
                            body.getLoanTakerModel().getApplicantDocumentModel().getBank_statement_images_reject_reason());

                }

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

                //Document missing .Upload Again in red
                isValidAadharFront=false;
                UiUtils.setDocumentUploadedisMissing(HouseVisitApplicantDocActivity.this,aadharCardFrontStatusTv);

                //mark not available
            }else if(aadhar_front == "true"){
                //mark available
                isValidAadharFront=false;
                UiUtils.setMarkedNotAvailable(HouseVisitApplicantDocActivity.this,aadharCardFrontStatusTv);
            }else if (aadhar_front == "false"){
                //file uploaded
                String aadhar_frontImageIsCorrect =body.getLoanTakerModel().getApplicantDocumentModel().getAadhar_front_images().getIs_correct();
                if(aadhar_frontImageIsCorrect==null){

                    UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,aadharCardFrontStatusTv);
                    isValidAadharFront=true;

                }else if(aadhar_frontImageIsCorrect=="true"){

                    UiUtils.setDocumentApproved(HouseVisitApplicantDocActivity.this,aadharCardFrontStatusTv);
                    isValidAadharFront=true;

                }else if (aadhar_frontImageIsCorrect =="false"){

                    isValidAadharFront=false;
                    UiUtils.setDocumentUploadedisRejected(HouseVisitApplicantDocActivity.this,aadharCardFrontStatusTv,
                            body.getLoanTakerModel().getApplicantDocumentModel().getAadhar_front_images().getReject_reason());

                }
                isAadharFrontUploaded=true;
                aadharFrontUploadedUrl = body.getLoanTakerModel().getApplicantDocumentModel().getAadhar_front_images().getOriginal();
                Picasso.with(HouseVisitApplicantDocActivity.this)
                        .load(body.getLoanTakerModel().getApplicantDocumentModel().getAadhar_front_images().getMedium())
                        .placeholder(R.drawable.img_placeholder)
                        .into(aadharCardFrontImage);
            }



            //aadhar back

            String aadhar_back = body.getLoanTakerModel().getApplicantDocumentModel().isAadhar_back_image_not_available();
            if( aadhar_back == null){

                //Document missing .Upload Again in red
                isValidAadharBack=false;
                UiUtils.setDocumentUploadedisMissing(HouseVisitApplicantDocActivity.this,aadharCardBackStatusTv);

                //mark not available
            }else if(aadhar_back == "true"){
                //mark available
                isValidAadharBack=false;
                UiUtils.setMarkedNotAvailable(HouseVisitApplicantDocActivity.this,aadharCardBackStatusTv);
            }else if (aadhar_back == "false"){
                //file uploaded
                String aadhar_frontImageIsCorrect =body.getLoanTakerModel().getApplicantDocumentModel().getAadhar_back_images().getIs_correct();
                if(aadhar_frontImageIsCorrect==null){

                    UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,aadharCardBackStatusTv);
                    isValidAadharBack=true;

                }else if(aadhar_frontImageIsCorrect=="true"){

                    UiUtils.setDocumentApproved(HouseVisitApplicantDocActivity.this,aadharCardBackStatusTv);
                    isValidAadharBack=true;

                }else if (aadhar_frontImageIsCorrect =="false"){

                    isValidAadharBack=false;
                    UiUtils.setDocumentUploadedisRejected(HouseVisitApplicantDocActivity.this,aadharCardBackStatusTv,
                            body.getLoanTakerModel().getApplicantDocumentModel().getAadhar_back_images().getReject_reason());

                }
                isAadharBackUploaded = true;
                aadharBackUploadedUrl = body.getLoanTakerModel().getApplicantDocumentModel().getAadhar_back_images().getOriginal();

                Picasso.with(HouseVisitApplicantDocActivity.this)
                        .load(body.getLoanTakerModel().getApplicantDocumentModel().getAadhar_back_images().getMedium())
                        .placeholder(R.drawable.img_placeholder)
                        .into(aadharCardBackImage);
            }



            //secondary
            String secondary_document = body.getLoanTakerModel().getApplicantDocumentModel().isSecondary_document_images_not_available();
            if( secondary_document == null){

                //Document missing .Upload Again in red
                isValidSecondaryItemSelected=false;
                UiUtils.setDocumentUploadedisMissing(HouseVisitApplicantDocActivity.this,secondaryIdStatusTv);

                //mark not available
            }else if(secondary_document == "true"){
                //mark available
                isValidSecondaryItemSelected=false;
                UiUtils.setMarkedNotAvailable(HouseVisitApplicantDocActivity.this,secondaryIdStatusTv);
            }else if (secondary_document == "false"){
                //file uploaded

                secondaryDummyImage.setVisibility(View.GONE);
                secondaryGrid.setVisibility(View.VISIBLE);

                String secondary_documentImageIsCorrect =body.getLoanTakerModel().getApplicantDocumentModel().getSecondary_document_images_is_correct();
                if(secondary_documentImageIsCorrect==null){

                    UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,secondaryIdStatusTv);
                    isValidSecondaryItemSelected=true;

                }else if(secondary_documentImageIsCorrect=="true"){

                    UiUtils.setDocumentApproved(HouseVisitApplicantDocActivity.this,secondaryIdStatusTv);
                    isValidSecondaryItemSelected=true;

                }else if (secondary_documentImageIsCorrect =="false"){

                    isValidSecondaryItemSelected=false;
                    UiUtils.setDocumentUploadedisRejected(HouseVisitApplicantDocActivity.this,secondaryIdStatusTv,
                            body.getLoanTakerModel().getApplicantDocumentModel().getSecondary_document_images_reject_reason());

                }

                isSecondaryDocumentsUploaded=true;

                secondaryIDValue = Integer.parseInt(body.getLoanTakerModel().getApplicantDocumentModel().getSecondary_document_id());
//                secondaryIdET.setText(body.getLoanTakerModel().getApplicantDocumentModel().getSecondaryIDModel().getName());
//                secondaryIdNotAvailable.setVisibility(View.GONE);

                for(int i=0;i<cacdSecondaryIDModelArrayListNew.size();i++){
                    if(cacdSecondaryIDModelArrayListNew.get(i).getId().equalsIgnoreCase(body.getLoanTakerModel().getApplicantDocumentModel().getSecondaryIDModel().getId())){
                        secondarySpinner.setSelection(i);
                    }
                }

                ArrayList<String> result_secondary = new ArrayList<>();
                secondaryIdArrayList = new ArrayList<>();

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





            //address proof

            String address_proof = body.getLoanTakerModel().getApplicantDocumentModel().getAddress_proof_not_available();
            if( address_proof == null){

                //Document missing .Upload Again in red
                isValidAddressproof=false;
//                UiUtils.setDocumentUploadedisMissing(HouseVisitApplicantDocActivity.this,addressProofStatusTv);

                //mark not available
            }else if(address_proof == "true"){
                //mark available
                isValidAddressproof=false;
                UiUtils.setMarkedNotAvailable(HouseVisitApplicantDocActivity.this,addressProofStatusTv);
            }else if (address_proof == "false"){
                //file uploaded
                String address_proofImageIsCorrect =body.getLoanTakerModel().getApplicantDocumentModel().getAddress_proof_images_is_correct();
                if(address_proofImageIsCorrect==null){

                    UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,addressProofStatusTv);
                    isValidAddressproof=true;

                }else if(address_proofImageIsCorrect=="true"){

                    UiUtils.setDocumentApproved(HouseVisitApplicantDocActivity.this,addressProofStatusTv);
                    isValidAddressproof=true;

                }else if (address_proofImageIsCorrect =="false"){

                    isValidAddressproof=false;
                    UiUtils.setDocumentUploadedisRejected(HouseVisitApplicantDocActivity.this,addressProofStatusTv,
                            body.getLoanTakerModel().getApplicantDocumentModel().getAddress_proof_images_reject_reason());

                }

                ArrayList<String> result = new ArrayList<>();
                addressProofArrayList = new ArrayList<>();

                for(int i=0;i<body.getLoanTakerModel().getApplicantDocumentModel().getAddressProofImagesModelArrayList().size();i++){
                    Log.i("Shahana","-AP-"+body.getLoanTakerModel().getApplicantDocumentModel()
                            .getAddressProofImagesModelArrayList().get(i)
                            .getAddressProofImageModel().getUploadImages().getMedium());

                    result.add(body.getLoanTakerModel().getApplicantDocumentModel()
                            .getAddressProofImagesModelArrayList().get(i)
                            .getAddressProofImageModel().getUploadImages().getOriginal());
                    addressProofArrayList.add(body.getLoanTakerModel().getApplicantDocumentModel().getAddressProofImagesModelArrayList().get(i).
                            getAddressProofImageModel().getId());

                }
                addressProofGrid.setNetworkPhotosWithKey(result);
            }


//            String other_documents_is_correct =body.getLoanTakerModel().getApplicantDocumentModel().getOth();

            //others loan cards

            if(body.getLoanTakerModel().getApplicantDocumentModel().getOtherLoanCardImagesModelArrayList().size()>0){

                UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,otherStatusTv);
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



            //other documents
            if(body.getLoanTakerModel().getApplicantDocumentModel().getOtherFormDataImagesModelArrayList().size()>0){

                UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,otherDocumentsTv);
                ArrayList<String> result_documents_others = new ArrayList<>();
                otherDocumentsIdArrayList = new ArrayList<>();

                for(int i=0;i<body.getLoanTakerModel().getApplicantDocumentModel().getOtherFormDataImagesModelArrayList().size();i++){
                    Log.i("Shahana","-Others-"+body.getLoanTakerModel().getApplicantDocumentModel()
                            .getOtherFormDataImagesModelArrayList().get(i)
                            .getOtherFormDataImageModel().getUploadImages().getMedium());

                    result_documents_others.add(body.getLoanTakerModel().getApplicantDocumentModel()
                            .getOtherFormDataImagesModelArrayList().get(i)
                            .getOtherFormDataImageModel().getUploadImages().getOriginal());
                    otherDocumentsIdArrayList.add(body.getLoanTakerModel().getApplicantDocumentModel().getOtherFormDataImagesModelArrayList().get(i).
                            getOtherFormDataImageModel().getId());

                }
                otherDocumentsGrid.setNetworkPhotosWithKey(result_documents_others);
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



        AlertDialog.Builder builder = new AlertDialog.Builder(HouseVisitApplicantDocActivity.this);
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
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(HouseVisitApplicantDocActivity.this);

                }else if(value.equalsIgnoreCase("bank_detail")){
                    isBankFrontPageClicked = true;
                    isProfilePicClicked = false;
                    isAadharFrontClicked = false;
                    isAadharBackClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(HouseVisitApplicantDocActivity.this);
                }else if (value.equalsIgnoreCase("aadhar_front")){
                    isAadharFrontClicked = true;
                    isProfilePicClicked = false;
                    isBankFrontPageClicked = false;
                    isAadharBackClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(HouseVisitApplicantDocActivity.this);

                }else if(value.equalsIgnoreCase("aadhar_back")){
                    isAadharBackClicked = true;
                    isProfilePicClicked = false;
                    isBankFrontPageClicked = false;
                    isAadharFrontClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(HouseVisitApplicantDocActivity.this);
                }


            }
        });

        showTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                ArrayList<PreviewFile> imageList = new ArrayList<>();
                imageList.add(new PreviewFile(picUploadedUrl, ""));
                Intent intent = new Intent(HouseVisitApplicantDocActivity.this,
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


            case R.id.continue_button:
                checkValidationForImages();
                break;


            case R.id.secondary_dummy_id_image:
                Toast.makeText(this, "Please choose a valid secondary ID", Toast.LENGTH_SHORT).show();
                break;

        }

    }


    private void checkValidationForImages() {

        if((isValidProfilePic) && (isValidBankFrontPage) && (isValidBankStatement) &&
                (isValidAadharFront) && (isValidAadharBack) && (isValidSecondaryItemSelected) && (isValidAddressproof)){


            Bundle params = new Bundle();
            params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
            params.putString("status","house_visit_applicant_doc_created");
            mFirebaseAnalytics.logEvent("house_visit_applicant_docs", params);

            goToHousVisitCoApplicantPage();
            updateTheStatusInHouseDetailPage();
//            goToGroupListingPage();
//            updateTheStatusInApplicantDetailPage();

        }else{

            if(!isValidProfilePic){
                UiUtils.setDocumentNotUploaded(HouseVisitApplicantDocActivity.this,profilePicStatusTV);

            }
            if(!isValidBankFrontPage){
                UiUtils.setDocumentNotUploaded(HouseVisitApplicantDocActivity.this,bankDetailStatusTv);
            }

            if(!isValidBankStatement){
                UiUtils.setDocumentNotUploaded(HouseVisitApplicantDocActivity.this,oneYearBankStatementStatusTv);
            }

            if(!isValidAadharFront){
                UiUtils.setDocumentNotUploaded(HouseVisitApplicantDocActivity.this,aadharCardFrontStatusTv);
            }

            if(!isValidAadharBack){
                UiUtils.setDocumentNotUploaded(HouseVisitApplicantDocActivity.this,aadharCardBackStatusTv);
            }

            if(!isValidSecondaryItemSelected){
                UiUtils.setDocumentNotUploaded(HouseVisitApplicantDocActivity.this,secondaryIdStatusTv);
            }

            if(!isValidAddressproof){
                UiUtils.setDocumentNotUploaded(HouseVisitApplicantDocActivity.this,addressProofStatusTv);
            }
        }

    }

    private void updateTheStatusInHouseDetailPage() {
        HouseDetailsActivity.getInstance().init();
    }

    private void goToHousVisitCoApplicantPage() {

        startActivity(new Intent(HouseVisitApplicantDocActivity.this,HouseVisitCoApplicantDocActivity.class));
        finish();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                if(isProfilePicClicked){
                    isValidProfilePic =true;
                    showProgressDialog(HouseVisitApplicantDocActivity.this);
                    new AsyncTaskRunnerForProfilePic().execute(String.valueOf(result.getUri()));
                }else if (isBankFrontPageClicked){
                    isValidBankFrontPage = true;
                    showProgressDialog(HouseVisitApplicantDocActivity.this);
                    new AsyncTaskRunnerForBankFrontPage().execute(String.valueOf(result.getUri()));
                }else if(isAadharFrontClicked){
                    isValidAadharFront = true;
                    showProgressDialog(HouseVisitApplicantDocActivity.this);
                    new AsyncTaskRunnerForAadharFront().execute(String.valueOf(result.getUri()));
                }else if(isAadharBackClicked){
                    showProgressDialog(HouseVisitApplicantDocActivity.this);
                    new AsyncTaskRunnerForAadharBack().execute(String.valueOf(result.getUri()));
                    isValidAadharBack = true;
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }else {

            ArrayList<String> deletedSeondaryIdsArrayList=new ArrayList<>();
            ArrayList<String> deletedBankIdsArrayList=new ArrayList<>();
            ArrayList<String> deletedOtherLoanIdsArrayList=new ArrayList<>();
            ArrayList<String> deletedAddressProofArrayList=new ArrayList<>();
            ArrayList<String> deletedOtherDocumentIdsArrayList=new ArrayList<>();

            onYearBankStatementGrid.onParentResult(requestCode, data);
            secondaryGrid.onParentResult(requestCode, data);
            otherLoanCardGrid.onParentResult(requestCode, data);
            otherDocumentsGrid.onParentResult(requestCode, data);
            addressProofGrid.onParentResult(requestCode, data);


            if (resultCode==5){
                Log.d("Shahana", "secondary");

                if(data!=null) {


                    List<Image> secondary_images = (List<Image>) data.getSerializableExtra(NavigatorImage.EXTRA_PHOTOS_URL);
                    ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                    if (requestCode == NavigatorImage.RESULT_SELECT_PHOTOS && null != secondary_images) {

                        secondarySelectedImages = secondary_images;
                        Log.i("Shahana", "size" + secondary_images.size());
                        for (Image image : secondary_images) {
                            Log.i("Shahana", "size path" + image.url);

                        }


                        isValidSecondaryItemSelected = true;
                        showProgressDialog(HouseVisitApplicantDocActivity.this);
                        saveMultipleSecondaryDocuments();

                    }
                }


            }else if (resultCode==6){
                Log.d("Shahana", "other");
                if(data!=null) {

                    List<Image> other_images = (List<Image>) data.getSerializableExtra(NavigatorImage.EXTRA_PHOTOS_URL);
                    ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                    if (requestCode == NavigatorImage.RESULT_SELECT_PHOTOS && null != other_images) {

                        otherLoanCardsSelectedImages = other_images;
                        Log.i("Shahana", "size" + other_images.size());
                        for (Image image : other_images) {
                            Log.i("Shahana", "size path" + image.url);

                        }

                        showProgressDialog(HouseVisitApplicantDocActivity.this);
                        saveMultipleLoanCardOtherImages();
                    }

                }


            }else if (resultCode==7){
                Log.d("Shahana", "other_doc");

                if(data!=null) {

                    List<Image> other_doc_images = (List<Image>) data.getSerializableExtra(NavigatorImage.EXTRA_PHOTOS_URL);
                    ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                    if (requestCode == NavigatorImage.RESULT_SELECT_PHOTOS && null != other_doc_images) {

                        otherDocumentsImages = other_doc_images;
                        Log.i("Shahana", "size" + other_doc_images.size());
                        for (Image image : other_doc_images) {
                            Log.i("Shahana", "size path" + image.url);

                        }

                        showProgressDialog(HouseVisitApplicantDocActivity.this);
                        saveMultipleOtherDocuments();

                    }
                }


            }else if (resultCode==8){
                Log.d("Shahana", "other");

                if(data!=null) {

                    List<Image> address_images = (List<Image>) data.getSerializableExtra(NavigatorImage.EXTRA_PHOTOS_URL);
                    ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                    if (requestCode == NavigatorImage.RESULT_SELECT_PHOTOS && null != address_images) {

                        addressProofImages = address_images;

                        isValidAddressproof=true;
                        Log.i("Shahana", "size" + address_images.size());
                        for (Image image : address_images) {
                            Log.i("Shahana", "size path" + image.url);

                        }

                        showProgressDialog(HouseVisitApplicantDocActivity.this);
                        saveMultipleAddressProof();

                    }

                }

            }else if(resultCode == 9) {
                Log.d("Shahana", "bank");
                if(data!=null) {
                    List<Image> images = (List<Image>) data.getSerializableExtra(NavigatorImage.EXTRA_PHOTOS_URL);
                    ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                    if (requestCode == NavigatorImage.RESULT_SELECT_PHOTOS && null != images) {

                        bankStatementSelectedImages = images;
                        Log.i("Shahana", "size" + images.size());
                        for (Image image : images) {
                            Log.i("Shahana", "size path" + image.url);

                        }

                        isValidBankStatement = true;
                        showProgressDialog(HouseVisitApplicantDocActivity.this);
                        saveMultipleBankStatements();

                    }
                }
            }else{
                if(data!=null) {

                    if (isSecondaryClicked) {
                        ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                        Log.d("Shahana", "delete secondary");
                        Log.i("Suhail", "positionss sizes" + positions.size());
                        Log.i("Suhail", "positionsss" + positions);

                        if (positions.size() > 0) {
                            for (int position : positions) {
                                Log.i("Suhail", "removed pos" + position);
                                for (int j = 0; j < secondaryIdArrayList.size(); j++) {
                                    Log.i("Suhail", "removed" + j + secondaryIdArrayList.get(j));

                                    if (position == j) {
                                        Log.i("Suhail", "delete" + secondaryIdArrayList.get(j));

                                        deletedSeondaryIdsArrayList.add(secondaryIdArrayList.get(j));

                                    }

                                }

                            }

                            if (deletedSeondaryIdsArrayList.size() > 0) {
                                Log.i("Suhail", "removed success");
                                deleteSecondaryDocuments(deletedSeondaryIdsArrayList);
                            }


                        }

                    } else if (isBankCLicked) {
                        Log.d("Shahana", "delete bank");
                        ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                        Log.d("Shahana", "delete bank");
                        Log.i("Suhail", "positionss sizes" + positions.size());
                        Log.i("Suhail", "positionsss" + positions);

                        if (positions.size() > 0) {
                            for (int position : positions) {
                                Log.i("Suhail", "removed pos" + position);
                                for (int j = 0; j < bankIdArrayList.size(); j++) {
                                    Log.i("Suhail", "removed" + j + bankIdArrayList.get(j));

                                    if (position == j) {
                                        Log.i("Suhail", "delete" + bankIdArrayList.get(j));

                                        deletedBankIdsArrayList.add(bankIdArrayList.get(j));

                                    }

                                }

                            }

                            if (deletedBankIdsArrayList.size() > 0) {
                                Log.i("Suhail", "removed success");
                                deleteBankDocuments(deletedBankIdsArrayList);
                            }


                        }

                    } else if (isOtherLoanCardClicked) {
                        Log.d("Shahana", "delete loan card");
                        ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                        Log.d("Shahana", "delete bank");
                        Log.i("Suhail", "positionss sizes" + positions.size());
                        Log.i("Suhail", "positionsss" + positions);

                        if (positions.size() > 0) {
                            for (int position : positions) {
                                Log.i("Suhail", "removed pos" + position);
                                for (int j = 0; j < otherIdArrayList.size(); j++) {
                                    Log.i("Suhail", "removed" + j + otherIdArrayList.get(j));

                                    if (position == j) {
                                        Log.i("Suhail", "delete" + otherIdArrayList.get(j));

                                        deletedOtherLoanIdsArrayList.add(otherIdArrayList.get(j));

                                    }

                                }

                            }

                            if (deletedOtherLoanIdsArrayList.size() > 0) {
                                Log.i("Suhail", "removed success");
                                deleteOtherLoanDocuments(deletedOtherLoanIdsArrayList);
                            }


                        }
                    }else if (isOtherDocumentsClicked) {
                        Log.d("Shahana", "delete loan docuemnts");
                        ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                        Log.d("Shahana", "delete docuemnts");
                        Log.i("Suhail", "positionss sizes" + positions.size());
                        Log.i("Suhail", "positionsss" + positions);

                        if (positions.size() > 0) {
                            for (int position : positions) {
                                Log.i("Suhail", "removed pos" + position);
                                for (int j = 0; j < otherDocumentsIdArrayList.size(); j++) {
                                    Log.i("Suhail", "removed" + j + otherDocumentsIdArrayList.get(j));

                                    if (position == j) {
                                        Log.i("Suhail", "delete" + otherDocumentsIdArrayList.get(j));

                                        deletedOtherDocumentIdsArrayList.add(otherDocumentsIdArrayList.get(j));

                                    }

                                }

                            }

                            if (deletedOtherLoanIdsArrayList.size() > 0) {
                                Log.i("Suhail", "removed success");
                                deleteOtherDocuments(deletedOtherDocumentIdsArrayList);
                            }


                        }
                    }else if (isAddressProofClicked) {
                        Log.d("Shahana", "delete address");
                        ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                        Log.d("Shahana", "delete address");
                        Log.i("Suhail", "positionss sizes" + positions.size());
                        Log.i("Suhail", "positionsss" + positions);

                        if (positions.size() > 0) {
                            for (int position : positions) {
                                Log.i("Suhail", "removed pos" + position);
                                for (int j = 0; j < addressProofArrayList.size(); j++) {
                                    Log.i("Suhail", "removed" + j + addressProofArrayList.get(j));

                                    if (position == j) {
                                        Log.i("Suhail", "delete" + addressProofArrayList.get(j));

                                        deletedAddressProofArrayList.add(addressProofArrayList.get(j));

                                    }

                                }

                            }

                            if (deletedOtherLoanIdsArrayList.size() > 0) {
                                Log.i("Suhail", "removed success");
                                deleteAddressProofDocuments(deletedAddressProofArrayList);
                            }


                        }
                    }
                }


                }









        }
    }

    private void deleteOtherDocuments(ArrayList<String> deletedIdsArrayList) {

        showProgressDialog(HouseVisitApplicantDocActivity.this);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);


        for(int i=0;i<deletedIdsArrayList.size();i++){

            Log.d("Aadhar onResponse", "" + "document[other_form_data_images_attributes]["+i+"][_destroy]");

            Log.d("Aadhar onResponse", "" + deletedIdsArrayList.get(i));

            builder.addFormDataPart("document[other_form_data_images_attributes]["+i+"][_destroy]", "true");
            builder.addFormDataPart("document[other_form_data_images_attributes]["+i+"][id]", deletedIdsArrayList.get(i));
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
                        for(int i=0;i<response.body().getLoanTakerModel().getApplicantDocumentModel().getOtherFormDataImagesModelArrayList().size();i++) {
                            otherDocumentsIdArrayList.remove(response.body().getLoanTakerModel().getApplicantDocumentModel().getOtherFormDataImagesModelArrayList().get(i).
                                    getOtherFormDataImageModel().getId());
                        }
                        Toast.makeText(HouseVisitApplicantDocActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        if(response.body().getLoanTakerModel().getApplicantDocumentModel().getOtherFormDataImagesModelArrayList().size()==0){

                        }else{
                            UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,otherDocumentsTv);
                        }
                    } else {
                        Log.d("Aadhar onResponse", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitApplicantDocActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Aadhar onResponse", "" + response.body().getErrors());
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                hideProgressDialog();
                NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
            }
        });
    }

    private void deleteOtherLoanDocuments(ArrayList<String> deletedIdsArrayList) {

        showProgressDialog(HouseVisitApplicantDocActivity.this);
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
                        Toast.makeText(HouseVisitApplicantDocActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        if(response.body().getLoanTakerModel().getApplicantDocumentModel().getOtherLoanCardImagesModelArrayList().size()==0){

                        }else{
                            UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,otherStatusTv);
                        }
                    } else {
                        Log.d("Aadhar onResponse", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitApplicantDocActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Aadhar onResponse", "" + response.body().getErrors());
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                hideProgressDialog();
                NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
            }
        });
    }

    private void deleteAddressProofDocuments(ArrayList<String> deletedIdsArrayList) {

        showProgressDialog(HouseVisitApplicantDocActivity.this);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);


        for(int i=0;i<deletedIdsArrayList.size();i++){

            Log.d("Aadhar onResponse", "" + "document[address_proof_images_attributes]["+i+"][_destroy]");

            Log.d("Aadhar onResponse", "" + deletedIdsArrayList.get(i));

            builder.addFormDataPart("document[address_proof_images_attributes]["+i+"][_destroy]", "true");
            builder.addFormDataPart("document[address_proof_images_attributes]["+i+"][id]", deletedIdsArrayList.get(i));
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
                        for(int i=0;i<response.body().getLoanTakerModel().getApplicantDocumentModel().getAddressProofImagesModelArrayList().size();i++) {
                            addressProofArrayList.remove(response.body().getLoanTakerModel().getApplicantDocumentModel().getAddressProofImagesModelArrayList().get(i).
                                    getAddressProofImageModel().getId());
                        }
                        Toast.makeText(HouseVisitApplicantDocActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        if(response.body().getLoanTakerModel().getApplicantDocumentModel().getAddressProofImagesModelArrayList().size()==0){
                            UiUtils.setDocumentNotUploaded(HouseVisitApplicantDocActivity.this,addressProofStatusTv);
                        }else{
                            UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,addressProofStatusTv);
                        }
                    } else {
                        Log.d("Aadhar onResponse", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitApplicantDocActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Aadhar onResponse", "" + response.body().getErrors());
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                hideProgressDialog();
                NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
            }
        });
    }

    private void deleteBankDocuments(ArrayList<String> deletedIdsArrayList) {

        showProgressDialog(HouseVisitApplicantDocActivity.this);
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
                        Toast.makeText(HouseVisitApplicantDocActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        if(response.body().getLoanTakerModel().getApplicantDocumentModel().getBankStatementImagesModelArrayList().size()==0){
                            UiUtils.setDocumentNotUploaded(HouseVisitApplicantDocActivity.this,oneYearBankStatementStatusTv);
                        }else{
                            UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,oneYearBankStatementStatusTv);
                        }
                    } else {
                        Log.d("Aadhar onResponse", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitApplicantDocActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Aadhar onResponse", "" + response.body().getErrors());
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                hideProgressDialog();
                NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
            }
        });
    }

    private void deleteSecondaryDocuments(ArrayList<String> deletedIdsArrayList) {

        showProgressDialog(HouseVisitApplicantDocActivity.this);
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
                        Toast.makeText(HouseVisitApplicantDocActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        if(response.body().getLoanTakerModel().getApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().size()==0){
                            UiUtils.setDocumentNotUploaded(HouseVisitApplicantDocActivity.this,secondaryIdStatusTv);
                            isValidSecondaryItemSelected=false;
                        }else{
                            UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,secondaryIdStatusTv);
                        }
                    } else {
                        Log.d("Aadhar onResponse", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitApplicantDocActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Aadhar onResponse", "" + response.body().getErrors());
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                hideProgressDialog();
                NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
            }
        });
    }

    private void saveMultipleAddressProof() {


        UiUtils.setDocumentUploading(HouseVisitApplicantDocActivity.this,addressProofStatusTv);

        try {
            Log.d("Drools bank image", "start");
            //For Image
            File file = null;
            MultipartBody.Part[] fileToUpload = new MultipartBody.Part[addressProofImages.size()];
            if (addressProofImages != null) {
                for (int i=0;i<addressProofImages.size();i++) {
                    Log.i("Drools","size path"+ addressProofImages.get(i).url);
                    Log.i("Drools","document[address_proof_images_attributes][" + i + "][image]");
//                    file = new File(decodeFile(addressProofImages.get(i).url,desiredWidth,desiredHeight));
                    file= new File(addressProofImages.get(i).url);
                    File compressedImageFile = new Compressor(this).compressToFile(file);
                    RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile);
                    fileToUpload[i] = MultipartBody.Part.createFormData("document[address_proof_images_attributes][" + i + "][image]", file.getName(), mFile);
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
                            UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,addressProofStatusTv);
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            reload();
//                            Toast.makeText(HouseVisitApplicantDocActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitApplicantDocActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMultipleOtherDocuments() {


        UiUtils.setDocumentUploading(HouseVisitApplicantDocActivity.this,otherDocumentsTv);

        try {
            Log.d("Drools bank image", "start");
            //For Image
            File file = null;
            MultipartBody.Part[] fileToUpload = new MultipartBody.Part[otherDocumentsImages.size()];
            if (otherDocumentsImages != null) {
                for (int i=0;i<otherDocumentsImages.size();i++) {
                    Log.i("Drools","size path"+ otherDocumentsImages.get(i).url);
                    Log.i("Drools","document[other_form_data_images_attributes][" + i + "][image]");
//                    file = new File(decodeFile(otherDocumentsImages.get(i).url,desiredWidth,desiredHeight));
                    file= new File(otherDocumentsImages.get(i).url);
                    File compressedImageFile = new Compressor(this).compressToFile(file);
                    RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile);
                    fileToUpload[i] = MultipartBody.Part.createFormData("document[other_form_data_images_attributes][" + i + "][image]", file.getName(), mFile);

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
                            UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,otherDocumentsTv);
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            reload();
//                            Toast.makeText(HouseVisitApplicantDocActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitApplicantDocActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMultipleLoanCardOtherImages() {


        UiUtils.setDocumentUploading(HouseVisitApplicantDocActivity.this,otherStatusTv);

        try {
            Log.d("Drools bank image", "start");
            //For Image
            File file = null;
            MultipartBody.Part[] fileToUpload = new MultipartBody.Part[otherLoanCardsSelectedImages.size()];
            if (otherLoanCardsSelectedImages != null) {
                for (int i=0;i<otherLoanCardsSelectedImages.size();i++) {
                    Log.i("Drools","size path"+ otherLoanCardsSelectedImages.get(i).url);
                    Log.i("Drools","document[other_loan_card_images_attributes][" + i + "][image]");
//                    file = new File(decodeFile(otherLoanCardsSelectedImages.get(i).url,desiredWidth,desiredHeight));
                    file= new File(otherLoanCardsSelectedImages.get(i).url);
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
                            UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,otherStatusTv);
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            reload();
//                            Toast.makeText(HouseVisitApplicantDocActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitApplicantDocActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void saveMultipleBankStatements() {


        UiUtils.setDocumentUploading(HouseVisitApplicantDocActivity.this,oneYearBankStatementStatusTv);

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
                            UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,oneYearBankStatementStatusTv);
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            reload();
//                            Toast.makeText(HouseVisitApplicantDocActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitApplicantDocActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMultipleSecondaryDocuments() {


        UiUtils.setDocumentUploading(HouseVisitApplicantDocActivity.this,secondaryIdStatusTv);
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
//            call = apiService.addApplicantSecondaryMultipleImage(
//                    loanTakerID,
//                    fileToUpload,secondary_id);


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
                            UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,secondaryIdStatusTv);
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
//                            Toast.makeText(HouseVisitApplicantDocActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitApplicantDocActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
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
            UiUtils.setDocumentUploading(HouseVisitApplicantDocActivity.this,profilePicStatusTV);
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
            UiUtils.setDocumentUploading(HouseVisitApplicantDocActivity.this,bankDetailStatusTv);
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
            UiUtils.setDocumentUploading(HouseVisitApplicantDocActivity.this,aadharCardFrontStatusTv);
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
            UiUtils.setDocumentUploading(HouseVisitApplicantDocActivity.this,aadharCardBackStatusTv);
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

    private void saveProfilePic() {


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
                            UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,profilePicStatusTV);
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
//                            Toast.makeText(HouseVisitApplicantDocActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitApplicantDocActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    profilePicProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBankDetailFirstPage() {



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

                            UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,bankDetailStatusTv);
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
//                            Toast.makeText(HouseVisitApplicantDocActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitApplicantDocActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    bankDetailFirstPageProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveAadharFront() {



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

                            UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,aadharCardFrontStatusTv);
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
//                            Toast.makeText(HouseVisitApplicantDocActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitApplicantDocActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    aadharCardFrontProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
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
//                file= new File(compressImage(commonUriPath));
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

                            UiUtils.setDocumentUploaded(HouseVisitApplicantDocActivity.this,aadharCardBackStatusTv);
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
//                            Toast.makeText(HouseVisitApplicantDocActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitApplicantDocActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantDocumentDetailResponseModel> call, Throwable t) {
                    aadharCardBackProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitApplicantDocActivity.this, null, null);
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


    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilenameNew();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilenameNew() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

}
