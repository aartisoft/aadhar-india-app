package com.tailwebs.aadharindia.postapproval;

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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.postapproval.models.GroupDocumentResponseModel;
import com.tailwebs.aadharindia.postapproval.models.LoanTakerPostApprovalDocumentResponseModel;
import com.tailwebs.aadharindia.member.models.LoanTakerApplicantDocumentDetailResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.ScalingUtilities;
import com.tailwebs.aadharindia.utils.UiUtils;
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

public class InsuranceFormActivity extends BaseActivity implements View.OnClickListener {



    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    private ProgressDialog mProgressDialog;


    //Applicant insurance Photo

    @BindView(R.id.applicant_insurance_form_image)
    ImageView applicantInsuranceFormImage;

    @BindView(R.id.applicant_insurance_form_status_tv)
    TextView applicantInsuranceFormStatusTv;


    @BindView(R.id.applicant_insurance_form_progress_bar)
    ProgressBar applicantInsuranceFormProgressBar;


    //applicant health Photo

    @BindView(R.id.applicant_health_declaration_form_page_image)
    ImageView applicantHealthDeclarationFormPageImage;

    @BindView(R.id.applicant_health_declaration_form_status_tv)
    TextView applicantHealthDeclarationFormStatusTv;


    @BindView(R.id.applicant_health_declaration_form_page_progress_bar)
    ProgressBar applicantHealthDeclarationFormPageProgressBar;



    //CoApplicant insurance Photo

    @BindView(R.id.co_applicant_insurance_form_image)
    ImageView coApplicantInsuranceFormImage;

    @BindView(R.id.co_applicant_insurance_form_status_tv)
    TextView coApplicantInsuranceFormStatusTv;


    @BindView(R.id.co_applicant_insurance_form_progress_bar)
    ProgressBar coApplicantInsuranceFormProgressBar;


    //Coapplicant health Photo

    @BindView(R.id.co_applicant_health_declaration_form_page_image)
    ImageView coApplicantHealthDeclarationFormPageImage;

    @BindView(R.id.co_applicant_health_declaration_form_status_tv)
    TextView coApplicantHealthDeclarationFormStatusTv;


    @BindView(R.id.co_applicant_health_declaration_form_page_progress_bar)
    ProgressBar coApplicantHealthDeclarationFormPageProgressBar;



    //other documents
    @BindView(R.id.other_documents_grid)
    ImageGridView otherDocumentsGrid;


    @BindView(R.id.others_documents_tv)
    TextView otherDocumentsTv;


    @BindView(R.id.continue_button)
    Button continueButton;

    String  commonUriPath = null;


    private boolean isValidApplicantInsurance = false,isValidApplicantHealthDeclaration = false,
            isValidCoApplicantInsurance = false,isValidCoApplicantHealthDeclaration = false;


    private boolean isApplicantInsuranceClicked = false, isApplicantHealthDeclarationClicked = false,
            isCoApplicantInsuranceClicked = false, isCoApplicantHealthDeclarationClicked = false;

    List<Image> otherDocumentsSelectedImages;
    ArrayList<String> otherDocumentsIdArrayList;

    String loanTakerID = null;

    int desiredWidth = 800,desiredHeight = 800,desiredWidthSmall = 300,desiredHeightSmall=300;

    private boolean isApplicantInsuranceUploaded = false,isApplicantHealthDeclarationUploaded = false, isCoApplicantInsuranceUploaded=false,
            isCoApplicantHealthDeclarationUploaded=false;

    String applicantInsuranceUploadedUrl=null,applicantHealthDeclarationUploadedUrl=null,coApplicantInsuranceUploadedUrl=null,
            coApplicantHealthDeclarationUploadedUrl=null;

    static InsuranceFormActivity instance;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_form);
        ButterKnife.bind(this);
        instance= this;

        loanTakerID = GlobalValue.loanTakerId;
        isStoragePermissionGranted();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "PA Insurance Form", null);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Insurance Forms");
        headingTV.setTextAppearance(getApplicationContext(), R.style.MyActionBarHeading);


        otherDocumentsGrid.setGRID_TYPE(2);
        otherDocumentsGrid.setItemClicked("others");
        otherDocumentsGrid.setMaxValue(5);


        applicantInsuranceFormImage.setOnClickListener(this);
        applicantHealthDeclarationFormPageImage.setOnClickListener(this);
        coApplicantInsuranceFormImage.setOnClickListener(this);
        coApplicantHealthDeclarationFormPageImage.setOnClickListener(this);
        continueButton.setOnClickListener(this);


        init();

    }

    public static InsuranceFormActivity getInstance() {
        return instance;
    }

    private void init() {
        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(InsuranceFormActivity.this);
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
            Call<LoanTakerPostApprovalDocumentResponseModel> call = apiService.getPostApprovalDocuments(loanTakerID);
            call.enqueue(new Callback<LoanTakerPostApprovalDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerPostApprovalDocumentResponseModel> call, final Response<LoanTakerPostApprovalDocumentResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            Log.i("Drools", "" + new Gson().toJson(response.body()));
                            setValuesFromResponse(response.body());



                        } else {
                            NetworkUtils.handleErrorsForAPICalls(InsuranceFormActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(InsuranceFormActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerPostApprovalDocumentResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(InsuranceFormActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(LoanTakerPostApprovalDocumentResponseModel body) {


        try {

            //applicant insurance
            String applicantInsuranceImage = body.getLoanTakerModel().getPostApprovalDocumentModel().getApplicant_photo_insurance_form_not_available();
            if( applicantInsuranceImage == null){

                //Document missing .Upload Again in red
                isValidApplicantInsurance=false;
//                UiUtils.setDocumentUploadedisMissing(InsuranceFormActivity.this,applicantInsuranceFormStatusTv);

                //mark not available
            }else if(applicantInsuranceImage == "true"){
                //mark available
                isValidApplicantInsurance=true;
                UiUtils.setMarkedNotAvailable(InsuranceFormActivity.this,applicantInsuranceFormStatusTv);
            }else if (applicantInsuranceImage == "false"){
                //file uploaded
                String applicantInsuranceImageIsCorrect =body.getLoanTakerModel().getPostApprovalDocumentModel().getApplicant_photo_insurance_form().getIs_correct();
                if(applicantInsuranceImageIsCorrect==null){

                    UiUtils.setDocumentUploaded(InsuranceFormActivity.this,applicantInsuranceFormStatusTv);
                    isValidApplicantInsurance=true;

                }else if(applicantInsuranceImageIsCorrect=="true"){

                    UiUtils.setDocumentApproved(InsuranceFormActivity.this,applicantInsuranceFormStatusTv);
                    isValidApplicantInsurance=true;

                }else if (applicantInsuranceImageIsCorrect =="false"){

                    isValidApplicantInsurance=false;
                    UiUtils.setDocumentUploadedisRejected(InsuranceFormActivity.this,applicantInsuranceFormStatusTv,
                            body.getLoanTakerModel().getPostApprovalDocumentModel().getApplicant_photo_insurance_form().getReject_reason());

                }

                isApplicantInsuranceUploaded = true;
                applicantInsuranceUploadedUrl = body.getLoanTakerModel().getPostApprovalDocumentModel().getApplicant_photo_insurance_form().getOriginal();

                Picasso.with(InsuranceFormActivity.this)
                        .load(body.getLoanTakerModel().getPostApprovalDocumentModel().getApplicant_photo_insurance_form().getMedium())
                        .placeholder(R.drawable.img_placeholder)
                        .into(applicantInsuranceFormImage);
            }



            //applicant  health declaration insurance
            String applicantHealthDeclarationImage = body.getLoanTakerModel().getPostApprovalDocumentModel().getApplicant_health_declaration_form_not_available();
            if( applicantHealthDeclarationImage == null){

                //Document missing .Upload Again in red
                isValidApplicantHealthDeclaration=false;
//                UiUtils.setDocumentUploadedisMissing(InsuranceFormActivity.this,applicantHealthDeclarationFormStatusTv);

                //mark not available
            }else if(applicantHealthDeclarationImage == "true"){
                //mark available
                isValidApplicantHealthDeclaration=true;
                UiUtils.setMarkedNotAvailable(InsuranceFormActivity.this,applicantHealthDeclarationFormStatusTv);
            }else if (applicantHealthDeclarationImage == "false"){
                //file uploaded
                String applicantHealthDeclarationIsCorrect =body.getLoanTakerModel().getPostApprovalDocumentModel().getApplicant_health_declaration_form().getIs_correct();
                if(applicantHealthDeclarationIsCorrect==null){

                    UiUtils.setDocumentUploaded(InsuranceFormActivity.this,applicantHealthDeclarationFormStatusTv);
                    isValidApplicantHealthDeclaration=true;

                }else if(applicantHealthDeclarationIsCorrect=="true"){

                    UiUtils.setDocumentApproved(InsuranceFormActivity.this,applicantHealthDeclarationFormStatusTv);
                    isValidApplicantHealthDeclaration=true;

                }else if (applicantHealthDeclarationIsCorrect =="false"){

                    isValidApplicantHealthDeclaration=false;
                    UiUtils.setDocumentUploadedisRejected(InsuranceFormActivity.this,applicantHealthDeclarationFormStatusTv,
                            body.getLoanTakerModel().getPostApprovalDocumentModel().getApplicant_health_declaration_form().getReject_reason());

                }

                isApplicantHealthDeclarationUploaded = true;
                applicantHealthDeclarationUploadedUrl = body.getLoanTakerModel().getPostApprovalDocumentModel().getApplicant_health_declaration_form().getOriginal();


                Picasso.with(InsuranceFormActivity.this)
                        .load(body.getLoanTakerModel().getPostApprovalDocumentModel().getApplicant_health_declaration_form().getMedium())
                        .placeholder(R.drawable.img_placeholder)
                        .into(applicantHealthDeclarationFormPageImage);
            }


            //coapplicant insurance
            String coapplicantInsuranceImage = body.getLoanTakerModel().getPostApprovalDocumentModel().getCoapplicant_photo_insurance_form_not_available();
            if( coapplicantInsuranceImage == null){

                //Document missing .Upload Again in red
                isValidCoApplicantInsurance=false;
//                UiUtils.setDocumentUploadedisMissing(InsuranceFormActivity.this,coApplicantInsuranceFormStatusTv);

                //mark not available
            }else if(coapplicantInsuranceImage == "true"){
                //mark available
                isValidCoApplicantInsurance=true;
                UiUtils.setMarkedNotAvailable(InsuranceFormActivity.this,coApplicantInsuranceFormStatusTv);
            }else if (coapplicantInsuranceImage == "false"){
                //file uploaded
                String coapplicantInsuranceImageIsCorrect =body.getLoanTakerModel().getPostApprovalDocumentModel().getCoapplicant_photo_insurance_form().getIs_correct();
                if(coapplicantInsuranceImageIsCorrect==null){

                    UiUtils.setDocumentUploaded(InsuranceFormActivity.this,coApplicantInsuranceFormStatusTv);
                    isValidCoApplicantInsurance=true;

                }else if(coapplicantInsuranceImageIsCorrect=="true"){

                    UiUtils.setDocumentApproved(InsuranceFormActivity.this,coApplicantInsuranceFormStatusTv);
                    isValidCoApplicantInsurance=true;

                }else if (coapplicantInsuranceImageIsCorrect =="false"){

                    isValidCoApplicantInsurance=false;
                    UiUtils.setDocumentUploadedisRejected(InsuranceFormActivity.this,coApplicantInsuranceFormStatusTv,
                            body.getLoanTakerModel().getPostApprovalDocumentModel().getCoapplicant_photo_insurance_form().getReject_reason());

                }


                isCoApplicantInsuranceUploaded = true;
                coApplicantInsuranceUploadedUrl = body.getLoanTakerModel().getPostApprovalDocumentModel().getCoapplicant_photo_insurance_form().getOriginal();


                Picasso.with(InsuranceFormActivity.this)
                        .load(body.getLoanTakerModel().getPostApprovalDocumentModel().getCoapplicant_photo_insurance_form().getMedium())
                        .placeholder(R.drawable.img_placeholder)
                        .into(coApplicantInsuranceFormImage);
            }




            //coapplicant  health declaration insurance
            String coapplicantHealthDeclarationImage = body.getLoanTakerModel().getPostApprovalDocumentModel().getCoapplicant_health_declaration_form_not_available();
            if( coapplicantHealthDeclarationImage == null){

                //Document missing .Upload Again in red
                isValidCoApplicantHealthDeclaration=false;
//                UiUtils.setDocumentUploadedisMissing(InsuranceFormActivity.this,coApplicantHealthDeclarationFormStatusTv);

                //mark not available
            }else if(coapplicantHealthDeclarationImage == "true"){
                //mark available
                isValidCoApplicantHealthDeclaration=true;
                UiUtils.setMarkedNotAvailable(InsuranceFormActivity.this,coApplicantHealthDeclarationFormStatusTv);
            }else if (coapplicantHealthDeclarationImage == "false"){
                //file uploaded
                String coapplicantHealthDeclarationIsCorrect =body.getLoanTakerModel().getPostApprovalDocumentModel().getCoapplicant_health_declaration_form().getIs_correct();
                if(coapplicantHealthDeclarationIsCorrect==null){

                    UiUtils.setDocumentUploaded(InsuranceFormActivity.this,coApplicantHealthDeclarationFormStatusTv);
                    isValidCoApplicantHealthDeclaration=true;

                }else if(coapplicantHealthDeclarationIsCorrect=="true"){

                    UiUtils.setDocumentApproved(InsuranceFormActivity.this,coApplicantHealthDeclarationFormStatusTv);
                    isValidCoApplicantHealthDeclaration=true;

                }else if (coapplicantHealthDeclarationIsCorrect =="false"){

                    isValidCoApplicantHealthDeclaration=false;
                    UiUtils.setDocumentUploadedisRejected(InsuranceFormActivity.this,coApplicantHealthDeclarationFormStatusTv,
                            body.getLoanTakerModel().getPostApprovalDocumentModel().getCoapplicant_health_declaration_form().getReject_reason());

                }

                isCoApplicantHealthDeclarationUploaded = true;
                coApplicantHealthDeclarationUploadedUrl = body.getLoanTakerModel().getPostApprovalDocumentModel().getCoapplicant_health_declaration_form().getOriginal();



                Picasso.with(InsuranceFormActivity.this)
                        .load(body.getLoanTakerModel().getPostApprovalDocumentModel().getCoapplicant_health_declaration_form().getMedium())
                        .placeholder(R.drawable.img_placeholder)
                        .into(coApplicantHealthDeclarationFormPageImage);
            }






            //others
//
//            String other_document = body.getLoanTakerModel().getPostApprovalDocumentModel().getPost_approval_other_document_not_available();
//            if( other_document == null){
//                //mark not available
//
//            }else if(other_document == "true"){
//                //mark available
//
//                UiUtils.setMarkedNotAvailable(InsuranceFormActivity.this,otherDocumentsTv);
//                otherDocumentsTv.setVisibility(View.GONE);
//            }else if (other_document == "false"){
//
//                //file uploaded
//
//                UiUtils.setDocumentUploaded(InsuranceFormActivity.this,otherDocumentsTv);
//
//                ArrayList<String> result_secondary = new ArrayList<>();
//
//                for(int i=0;i<body.getLoanTakerModel().getPostApprovalDocumentModel().getPostApprovalOtherDocumentsModelArrayList().size();i++){
//                    Log.i("Shahana","-Sec-"+body.getLoanTakerModel().getPostApprovalDocumentModel()
//                            .getPostApprovalOtherDocumentsModelArrayList().get(i)
//                            .getUploadImages().getMedium());
//
//                    result_secondary.add(body.getLoanTakerModel().getPostApprovalDocumentModel()
//                            .getPostApprovalOtherDocumentsModelArrayList().get(i)
//                          .getUploadImages().getOriginal());
//
//                }
//                otherDocumentsGrid.setNetworkPhotosWithKey(result_secondary);
//            }


            //others

            if(body.getLoanTakerModel().getPostApprovalDocumentModel().getPostApprovalOtherDocumentsModelArrayList().size()>0){

                UiUtils.setDocumentUploaded(InsuranceFormActivity.this,otherDocumentsTv);
                ArrayList<String> result_others = new ArrayList<>();
                otherDocumentsIdArrayList = new ArrayList<>();

                for(int i=0;i<body.getLoanTakerModel().getPostApprovalDocumentModel().getPostApprovalOtherDocumentsModelArrayList().size();i++){
                    Log.i("Shahana","-Others-"+body.getLoanTakerModel().getPostApprovalDocumentModel()
                            .getPostApprovalOtherDocumentsModelArrayList().get(i)
                           .getUploadImages().getMedium());

                    result_others.add(body.getLoanTakerModel().getPostApprovalDocumentModel()
                            .getPostApprovalOtherDocumentsModelArrayList().get(i)
                          .getUploadImages().getOriginal());
                    otherDocumentsIdArrayList.add(body.getLoanTakerModel().getPostApprovalDocumentModel().getPostApprovalOtherDocumentsModelArrayList().get(i).
                            getId());

                }
                otherDocumentsGrid.setNetworkPhotosWithKey(result_others);
            }








        } catch (Exception e) {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            if (resultCode == RESULT_OK) {

                if(isApplicantInsuranceClicked){
                    isValidApplicantInsurance =true;
                    showProgressDialog(InsuranceFormActivity.this);
                    new AsyncTaskRunnerForApplicantInsurance().execute(String.valueOf(result.getUri()));
                }else if (isApplicantHealthDeclarationClicked){
                    isValidApplicantHealthDeclaration = true;
                    showProgressDialog(InsuranceFormActivity.this);
                    new AsyncTaskRunnerForApplicantHealthDeclaration().execute(String.valueOf(result.getUri()));
                }else if(isCoApplicantInsuranceClicked){
                    isValidCoApplicantInsurance = true;
                    showProgressDialog(InsuranceFormActivity.this);
                    new AsyncTaskRunnerForCoApplicantInsurance().execute(String.valueOf(result.getUri()));
                }else if(isCoApplicantHealthDeclarationClicked){
                    isValidCoApplicantHealthDeclaration = true;
                    showProgressDialog(InsuranceFormActivity.this);
                    new AsyncTaskRunnerForCoApplicantHealthDeclaration().execute(String.valueOf(result.getUri()));

                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }else {


            otherDocumentsGrid.onParentResult(requestCode, data);
            ArrayList<String> deletedIdsArrayList=new ArrayList<>();

            if(data!=null) {

                Log.d("Shahana", "bank");
//                List<Image> images = (List<Image>) data.getSerializableExtra(NavigatorImage.EXTRA_PHOTOS_URL);
//                ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
//                if (requestCode == NavigatorImage.RESULT_SELECT_PHOTOS && null != images) {
//
//                    otherDocumentsSelectedImages = images;
//                    Log.i("Shahana", "size" + images.size());
//                    for (Image image : images) {
//                        Log.i("Shahana", "size path" + image.url);
//
//                    }
//
//
//                    saveMultipleOtherDocuments();
//
//                }


                List<Image> images = (List<Image>) data.getSerializableExtra(NavigatorImage.EXTRA_PHOTOS_URL);
                ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                if (requestCode == NavigatorImage.RESULT_SELECT_PHOTOS && null != images) {


                    otherDocumentsSelectedImages = images;
//                    Log.i("Shahana", "size" + images.size());
//                    for (Image image : images) {
//                        Log.i("Shahana", "size path" + image.url);
//
//                    }


                    Log.i("Suhail", "size" + images.size());
                    for (Image image : images) {
                        Log.i("Suhail", "size path" + image.url);
                    }

                    if(otherDocumentsSelectedImages!=null){
                        showProgressDialog(InsuranceFormActivity.this);
                        saveMultipleOtherDocuments();
                    }else{

                        Toast.makeText(this, "Add Images to Other Documents", Toast.LENGTH_SHORT).show();
                    }


                } else if (requestCode == NavigatorImage.RESULT_IMAGE_SWITCHER && null != positions) {

                    Log.i("Suhail", "positionss sizes" + positions.size());
                    Log.i("Suhail", "positionsss" + positions);


                    if(positions.size()>0){
                        for (int position : positions) {
                            Log.i("Suhail", "removed pos" + position);
                            for(int j=0;j<otherDocumentsIdArrayList.size();j++){
                                Log.i("Suhail", "removed"+ j + otherDocumentsIdArrayList.get(j));

                                if(position==j){
                                    Log.i("Suhail", "delete"+otherDocumentsIdArrayList.get(j) );

                                    deletedIdsArrayList.add(otherDocumentsIdArrayList.get(j));

//
                                }


                            }

                        }

                        if(deletedIdsArrayList.size()>0){
                            deleteOtherDocuments(deletedIdsArrayList);
                        }


                    }

                }


        }

        }
    }

    private void deleteOtherDocuments(ArrayList<String> deletedIdsArrayList) {

        Log.d("Suhail ", "l response"+deletedIdsArrayList);

        showProgressDialog(InsuranceFormActivity.this);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);


        for(int i=0;i<deletedIdsArrayList.size();i++){

            Log.d("Aadhar onResponse", "" + "document[post_approval_other_documents_attributes]["+i+"][_destroy]");

            Log.d("Aadhar onResponse", "" + deletedIdsArrayList.get(i));

            builder.addFormDataPart("document[post_approval_other_documents_attributes]["+i+"][_destroy]", "true");
            builder.addFormDataPart("document[post_approval_other_documents_attributes]["+i+"][id]", deletedIdsArrayList.get(i));
        }


        RequestBody finalRequestBody = builder.build();
        ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
        Call<LoanTakerPostApprovalDocumentResponseModel> call = apiService.deletePostApprovalMultipleOtherImage(loanTakerID,finalRequestBody
        );
        call.enqueue(new Callback<LoanTakerPostApprovalDocumentResponseModel>() {
            @Override
            public void onResponse(Call<LoanTakerPostApprovalDocumentResponseModel> call, Response<LoanTakerPostApprovalDocumentResponseModel> response) {
                hideProgressDialog();
                Log.d("Aadhar onResponse", "" + response.code() + "--" + response.message());
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        //API Success is true
                        for(int i=0;i<response.body().getLoanTakerModel().getPostApprovalDocumentModel().getPostApprovalOtherDocumentsModelArrayList().size();i++) {
                            otherDocumentsIdArrayList.remove(response.body().getLoanTakerModel().getPostApprovalDocumentModel().getPostApprovalOtherDocumentsModelArrayList().get(i).
                                    getId());
                        }
                        Toast.makeText(InsuranceFormActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        if(response.body().getLoanTakerModel().getPostApprovalDocumentModel().getPostApprovalOtherDocumentsModelArrayList().size()==0){
                            UiUtils.setDocumentNotUploaded(InsuranceFormActivity.this,otherDocumentsTv);

                        }else{
                            UiUtils.setDocumentUploaded(InsuranceFormActivity.this,otherDocumentsTv);
                        }
                    } else {
                        Log.d("Aadhar onResponse", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsCasesForAPICalls(InsuranceFormActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Aadhar onResponse", "" + response.body().getErrors());
                    NetworkUtils.handleErrorsForAPICalls(InsuranceFormActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<LoanTakerPostApprovalDocumentResponseModel> call, Throwable t) {
                hideProgressDialog();
                NetworkUtils.handleErrorsForAPICalls(InsuranceFormActivity.this, null, null);
            }
        });
    }


    // other documents

    private void saveMultipleOtherDocuments() {


        UiUtils.setDocumentUploading(InsuranceFormActivity.this,otherDocumentsTv);

        try {
            Log.d("Drools bank image", "start");
            //For Image
            File file = null;
            MultipartBody.Part[] fileToUpload = new MultipartBody.Part[otherDocumentsSelectedImages.size()];
            if (otherDocumentsSelectedImages != null) {
                for (int i=0;i<otherDocumentsSelectedImages.size();i++) {
                    Log.i("Drools","size path"+ otherDocumentsSelectedImages.get(i).url);
                    Log.i("Drools","document[other_loan_card_images_attributes][" + i + "][image]");
//                    file = new File(decodeFile(otherDocumentsSelectedImages.get(i).url,desiredWidth,desiredHeight));
                    file= new File(otherDocumentsSelectedImages.get(i).url);
                    File compressedImageFile = new Compressor(this).compressToFile(file);
                    RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile);
                    fileToUpload[i] = MultipartBody.Part.createFormData("document[post_approval_other_documents_attributes][" + i + "][image]", file.getName(), mFile);

                }
            }

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerPostApprovalDocumentResponseModel> call;
            call = apiService.addPostApprovalMultipleImage(
                    loanTakerID,
                    fileToUpload);

            call.enqueue(new Callback<LoanTakerPostApprovalDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerPostApprovalDocumentResponseModel> call, Response <LoanTakerPostApprovalDocumentResponseModel> response) {
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            UiUtils.setDocumentUploaded(InsuranceFormActivity.this,otherDocumentsTv);
                            InsuranceFormActivity.getInstance().init();
                            Toast.makeText(InsuranceFormActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(InsuranceFormActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(InsuranceFormActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerPostApprovalDocumentResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(InsuranceFormActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //applicant insurance form

    private class AsyncTaskRunnerForApplicantInsurance extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {

                Uri myUri = Uri.parse(params[0]);
                ((ImageView) findViewById(R.id.applicant_insurance_form_image)).setImageURI(myUri);
            }  catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

            Bitmap bm=((BitmapDrawable)applicantInsuranceFormImage.getDrawable()).getBitmap();
            saveImageFile(bm);
            saveApplicantInsurance();
            isApplicantInsuranceClicked = false;
        }

        @Override
        protected void onPreExecute() {
            applicantInsuranceFormProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

    private void saveApplicantInsurance() {

        UiUtils.setDocumentUploading(InsuranceFormActivity.this,applicantInsuranceFormStatusTv);
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
                fileToUpload = MultipartBody.Part.createFormData("document[applicant_photo_insurance_form]", file.getName(), mFile);
            }

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerPostApprovalDocumentResponseModel> call;
            call = apiService.addPostApprovalDocuments(
                    loanTakerID,
                    fileToUpload);

            call.enqueue(new Callback<LoanTakerPostApprovalDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerPostApprovalDocumentResponseModel> call, Response<LoanTakerPostApprovalDocumentResponseModel> response) {
                    applicantInsuranceFormProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            UiUtils.setDocumentUploaded(InsuranceFormActivity.this,applicantInsuranceFormStatusTv);
                            isApplicantInsuranceUploaded = true;
                            applicantInsuranceUploadedUrl = response.body().getLoanTakerModel().getPostApprovalDocumentModel().getApplicant_photo_insurance_form().getOriginal();
                            Toast.makeText(InsuranceFormActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(InsuranceFormActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(InsuranceFormActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerPostApprovalDocumentResponseModel> call, Throwable t) {
                    applicantInsuranceFormProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(InsuranceFormActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //applicant health declaration form

    private class AsyncTaskRunnerForApplicantHealthDeclaration extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {
                Uri myUri = Uri.parse(params[0]);
                ((ImageView) findViewById(R.id.applicant_health_declaration_form_page_image)).setImageURI(myUri);
            }  catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

            Bitmap bm=((BitmapDrawable)applicantHealthDeclarationFormPageImage.getDrawable()).getBitmap();
            saveImageFile(bm);
            saveApplicantHealthDeclaration();
            isApplicantHealthDeclarationClicked = false;
        }


        @Override
        protected void onPreExecute() {
            applicantHealthDeclarationFormPageProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

    private void saveApplicantHealthDeclaration() {

        UiUtils.setDocumentUploading(InsuranceFormActivity.this,applicantHealthDeclarationFormStatusTv);

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
                fileToUpload = MultipartBody.Part.createFormData("document[applicant_health_declaration_form]", file.getName(), mFile);
            }

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerPostApprovalDocumentResponseModel> call;
            call = apiService.addPostApprovalDocuments(
                    loanTakerID,
                    fileToUpload);

            call.enqueue(new Callback<LoanTakerPostApprovalDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerPostApprovalDocumentResponseModel> call, Response<LoanTakerPostApprovalDocumentResponseModel> response) {
                    applicantHealthDeclarationFormPageProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            UiUtils.setDocumentUploaded(InsuranceFormActivity.this,applicantHealthDeclarationFormStatusTv);
                            isApplicantHealthDeclarationUploaded = true;
                            applicantHealthDeclarationUploadedUrl = response.body().getLoanTakerModel().getPostApprovalDocumentModel().getApplicant_health_declaration_form().getOriginal();
                            Toast.makeText(InsuranceFormActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(InsuranceFormActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(InsuranceFormActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerPostApprovalDocumentResponseModel> call, Throwable t) {
                    applicantHealthDeclarationFormPageProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(InsuranceFormActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //co-applicant insurance form

    private class AsyncTaskRunnerForCoApplicantInsurance extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {

                Uri myUri = Uri.parse(params[0]);
                ((ImageView) findViewById(R.id.co_applicant_insurance_form_image)).setImageURI(myUri);
            }  catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

            Bitmap bm=((BitmapDrawable)coApplicantInsuranceFormImage.getDrawable()).getBitmap();
            saveImageFile(bm);
            saveCoApplicantInsurance();
            isCoApplicantInsuranceClicked = false;
        }

        @Override
        protected void onPreExecute() {
            coApplicantInsuranceFormProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

    private void saveCoApplicantInsurance() {

        UiUtils.setDocumentUploading(InsuranceFormActivity.this,coApplicantInsuranceFormStatusTv);
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
                fileToUpload = MultipartBody.Part.createFormData("document[coapplicant_photo_insurance_form]", file.getName(), mFile);
            }

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerPostApprovalDocumentResponseModel> call;
            call = apiService.addPostApprovalDocuments(
                    loanTakerID,
                    fileToUpload);

            call.enqueue(new Callback<LoanTakerPostApprovalDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerPostApprovalDocumentResponseModel> call, Response<LoanTakerPostApprovalDocumentResponseModel> response) {
                    coApplicantInsuranceFormProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            UiUtils.setDocumentUploaded(InsuranceFormActivity.this,coApplicantInsuranceFormStatusTv);
                            isCoApplicantInsuranceUploaded = true;
                            coApplicantInsuranceUploadedUrl = response.body().getLoanTakerModel().getPostApprovalDocumentModel().getCoapplicant_photo_insurance_form().getOriginal();
                            Toast.makeText(InsuranceFormActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(InsuranceFormActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(InsuranceFormActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerPostApprovalDocumentResponseModel> call, Throwable t) {
                    coApplicantInsuranceFormProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(InsuranceFormActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //co-applicant health declaration form

    private class AsyncTaskRunnerForCoApplicantHealthDeclaration extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {
                Uri myUri = Uri.parse(params[0]);
                ((ImageView) findViewById(R.id.co_applicant_health_declaration_form_page_image)).setImageURI(myUri);
            }  catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

            Bitmap bm=((BitmapDrawable)coApplicantHealthDeclarationFormPageImage.getDrawable()).getBitmap();
            saveImageFile(bm);
            saveCoApplicantHealthDeclaration();
            isCoApplicantHealthDeclarationClicked = false;
        }


        @Override
        protected void onPreExecute() {
            coApplicantHealthDeclarationFormPageProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

    private void saveCoApplicantHealthDeclaration() {

        UiUtils.setDocumentUploading(InsuranceFormActivity.this,coApplicantHealthDeclarationFormStatusTv);

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
                fileToUpload = MultipartBody.Part.createFormData("document[coapplicant_health_declaration_form]", file.getName(), mFile);
            }

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerPostApprovalDocumentResponseModel> call;
            call = apiService.addPostApprovalDocuments(
                    loanTakerID,
                    fileToUpload);

            call.enqueue(new Callback<LoanTakerPostApprovalDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerPostApprovalDocumentResponseModel> call, Response<LoanTakerPostApprovalDocumentResponseModel> response) {
                    coApplicantHealthDeclarationFormPageProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            UiUtils.setDocumentUploaded(InsuranceFormActivity.this,coApplicantHealthDeclarationFormStatusTv);
                            isCoApplicantHealthDeclarationUploaded = true;
                            coApplicantHealthDeclarationUploadedUrl = response.body().getLoanTakerModel().getPostApprovalDocumentModel().getCoapplicant_health_declaration_form().getOriginal();
                            Toast.makeText(InsuranceFormActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(InsuranceFormActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(InsuranceFormActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerPostApprovalDocumentResponseModel> call, Throwable t) {
                    coApplicantHealthDeclarationFormPageProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(InsuranceFormActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
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

        switch (v.getId()) {
            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.applicant_insurance_form_image:

                if(isApplicantInsuranceUploaded){

                    if(applicantInsuranceUploadedUrl!=null){
                        editShowPopup(applicantInsuranceUploadedUrl,"applicant_insurance");
                    }

                }else {
                    isApplicantInsuranceClicked = true;
                    isApplicantHealthDeclarationClicked = false;
                    isCoApplicantInsuranceClicked = false;
                    isCoApplicantHealthDeclarationClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
                }
                break;

            case R.id.applicant_health_declaration_form_page_image:
                if(isApplicantHealthDeclarationUploaded){

                    if(applicantHealthDeclarationUploadedUrl!=null){
                        editShowPopup(applicantHealthDeclarationUploadedUrl,"applicant_health");
                    }

                }else {
                    isApplicantHealthDeclarationClicked = true;
                    isApplicantInsuranceClicked = false;
                    isCoApplicantInsuranceClicked = false;
                    isCoApplicantHealthDeclarationClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
                }
                break;

            case R.id.co_applicant_insurance_form_image:
                if(isCoApplicantInsuranceUploaded){

                    if(coApplicantInsuranceUploadedUrl!=null){
                        editShowPopup(coApplicantInsuranceUploadedUrl,"co_applicant_insurance");
                    }

                }else {
                    isCoApplicantInsuranceClicked = true;
                    isApplicantHealthDeclarationClicked = false;
                    isApplicantInsuranceClicked = false;
                    isCoApplicantHealthDeclarationClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
                }
                break;


            case R.id.co_applicant_health_declaration_form_page_image:
                if(isCoApplicantHealthDeclarationUploaded){

                    if(coApplicantHealthDeclarationUploadedUrl!=null){
                        editShowPopup(coApplicantHealthDeclarationUploadedUrl,"co_applicant_health");
                    }

                }else {
                    isCoApplicantHealthDeclarationClicked = true;
                    isApplicantHealthDeclarationClicked = false;
                    isCoApplicantInsuranceClicked = false;
                    isApplicantInsuranceClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
                }
                break;



            case R.id.continue_button:
                checkValidationForImages();
                break;

        }

    }



    private void editShowPopup(final String picUploadedUrl, final String value){
        View view = getLayoutInflater().inflate(R.layout.custom_edit_image_yes_no_dialog, null);
        TextView editTV =(TextView)view.findViewById(R.id.edit_tv);
        TextView showTV =(TextView)view.findViewById(R.id.show_tv);
        Button yesButton =(Button)view.findViewById(R.id.yes_button);



        AlertDialog.Builder builder = new AlertDialog.Builder(InsuranceFormActivity.this);
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

                if(value.equalsIgnoreCase("applicant_insurance")){
                    isApplicantInsuranceClicked = true;
                    isApplicantHealthDeclarationClicked = false;
                    isCoApplicantInsuranceClicked = false;
                    isCoApplicantHealthDeclarationClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(InsuranceFormActivity.this);

                }else if (value.equalsIgnoreCase("applicant_health")){
                    isApplicantHealthDeclarationClicked = true;
                    isApplicantInsuranceClicked = false;
                    isCoApplicantInsuranceClicked = false;
                    isCoApplicantHealthDeclarationClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(InsuranceFormActivity.this);

                }else if (value.equalsIgnoreCase("co_applicant_insurance")){
                    isCoApplicantInsuranceClicked = true;
                    isApplicantHealthDeclarationClicked = false;
                    isApplicantInsuranceClicked = false;
                    isCoApplicantHealthDeclarationClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(InsuranceFormActivity.this);

                }else if (value.equalsIgnoreCase("co_applicant_health")){
                    isCoApplicantHealthDeclarationClicked = true;
                    isApplicantHealthDeclarationClicked = false;
                    isCoApplicantInsuranceClicked = false;
                    isApplicantInsuranceClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(InsuranceFormActivity.this);

                }


            }
        });

        showTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                ArrayList<PreviewFile> imageList = new ArrayList<>();
                imageList.add(new PreviewFile(picUploadedUrl, ""));
                Intent intent = new Intent(InsuranceFormActivity.this,
                        ImagePreviewSingleImageWithEditActivity.class);
                intent.putExtra(ImagePreviewSingleImageWithEditActivity.IMAGE_LIST,
                        imageList);
                intent.putExtra(ImagePreviewSingleImageWithEditActivity.CURRENT_ITEM, "0");
                startActivity(intent);
            }
        });
    }


    private void checkValidationForImages() {

        if((isValidApplicantInsurance) && (isValidApplicantHealthDeclaration) && (isValidCoApplicantInsurance) &&
                (isValidCoApplicantHealthDeclaration)){


            Bundle params = new Bundle();
            params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
            params.putString("status","post_approval_insurance_form_created");
            mFirebaseAnalytics.logEvent("post_approval_insurance_form", params);

            goToMemberDetailPage();
//            updateTheStatusInApplicantDetailPage();


        }else{

            if(!isValidApplicantInsurance){
                UiUtils.setDocumentNotUploaded(InsuranceFormActivity.this,applicantInsuranceFormStatusTv);

            }
            if(!isValidApplicantHealthDeclaration){
                UiUtils.setDocumentNotUploaded(InsuranceFormActivity.this,applicantHealthDeclarationFormStatusTv);
            }

            if(!isValidCoApplicantInsurance){
                UiUtils.setDocumentNotUploaded(InsuranceFormActivity.this,coApplicantInsuranceFormStatusTv);
            }

            if(!isValidCoApplicantHealthDeclaration){
                UiUtils.setDocumentNotUploaded(InsuranceFormActivity.this,coApplicantHealthDeclarationFormStatusTv);
            }


        }

    }

    private void goToMemberDetailPage() {

        IndividualDocumentsActivity.getInstance().init();
        IndividualMemberDetailActivity.getInstance().init();
        PostApprovalTaskDetailsActivity.getInstance().init();
        Intent intent = new Intent(InsuranceFormActivity.this,IndividualMemberDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

}
