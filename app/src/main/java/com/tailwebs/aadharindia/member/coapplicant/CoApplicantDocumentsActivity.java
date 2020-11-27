package com.tailwebs.aadharindia.member.coapplicant;

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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.applicant.spinners.SecondaryIDSpinnerAdapter;
import com.tailwebs.aadharindia.member.coapplicant.models.LoanTakerCoApplicantDocumentResponseModel;
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

public class CoApplicantDocumentsActivity extends BaseActivity implements View.OnClickListener {

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

    @BindView(R.id.profile_pic_document_not_available)
    TextView profilePicDocumentNotAvailable;


    @BindView(R.id.profile_pic_progress_bar)
    ProgressBar profilePicProgressBar;


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
//    @BindView(R.id.secondary_id_image)
//    ImageView secondaryIdImage;

    @BindView(R.id.secondary_id_status_tv)
    TextView secondaryIdStatusTv;

    @BindView(R.id.secondary_id_document_not_available)
    TextView secondaryIdNotAvailable;


    @BindView(R.id.secondary_grid)
    ImageGridView secondaryGrid;



    @BindView(R.id.continue_button)
    Button continueButton;

    private boolean isValidProfilePic = false,isValidAadharFront = false,
            isValidAadharBack = false, isValidSecondaryItemSelected = false, isValidSecondaryId = false, isSecondaryDocumentsUploaded=false;

    private boolean  isProfilePicClicked = false,isAadharFrontClicked = false,
            isAadharBackClicked = false,  isValidSecondaryIdClicked = false;

    private boolean isProfilePicUploaded = false,isAadharFrontUploaded = false,isAadharBackUploaded=false;

    String profilePicUploadedUrl=null,aadharFrontUploadedUrl=null,aadharBackUploadedUrl=null;

    String  commonUriPath = null;

    private ProgressDialog mProgressDialog;
    String loanTakerID = null;

    @BindView(R.id.spinner_secondary)
    Spinner secondarySpinner;

    int secondaryIDValue= 0;

    SecondaryIDSpinnerAdapter secondaryIDSpinnerAdapter;

    @BindView(R.id.secondary_spinner_error_tv)
    TextView secondarySpinnerErrorTv;

    public ArrayList<CACDSecondaryIDModel> cacdSecondaryIDModels = null,cacdSecondaryIDModelArrayListNew;

    List<Image> selectedImages;

    ArrayList<String> secondaryIdArrayList;

    int desiredWidth = 800,desiredHeight = 800,desiredWidthSmall = 300,desiredHeightSmall=300;

    private FirebaseAnalytics mFirebaseAnalytics;

    @BindView(R.id.secondary_dummy_id_image)
    ImageView secondaryDummyImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_applicant_documents);
        ButterKnife.bind(this);
        secondaryIdArrayList = new ArrayList<>();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        loanTakerID = GlobalValue.loanTakerId;
        isStoragePermissionGranted();

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Co-Applicant Document");
        headingTV.setTextAppearance(getApplicationContext(), R.style.MyActionBarHeading);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Co-Applicant Documents", null);

        cacdSecondaryIDModels =GlobalValue.applicantSecondaryIDArrayList;



        secondaryGrid.setGRID_TYPE(2);
        secondaryGrid.setMaxValue(10);


        profilePhoto.setOnClickListener(this);
        aadharCardFrontImage.setOnClickListener(this);
        aadharCardBackImage.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        secondaryDummyImage.setOnClickListener(this);

        profilePicDocumentNotAvailable.setOnClickListener(this);
        aadharCardFrontNotAvailable.setOnClickListener(this);
        aadharCardBackNotAvailable.setOnClickListener(this);
        secondaryIdNotAvailable.setOnClickListener(this);


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


        secondaryIDSpinnerAdapter = new SecondaryIDSpinnerAdapter(CoApplicantDocumentsActivity.this,
                R.layout.ms__list_item,
                cacdSecondaryIDModels);

        secondarySpinner.setAdapter(secondaryIDSpinnerAdapter); // Set the custom adapter to the spinner

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(CoApplicantDocumentsActivity.this);
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
            Call<LoanTakerCoApplicantDocumentResponseModel> call = apiService.getCoApplicantDocumentDetails(loanTakerID);
            call.enqueue(new Callback<LoanTakerCoApplicantDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCoApplicantDocumentResponseModel> call, final Response<LoanTakerCoApplicantDocumentResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                            Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                            setValuesFromResponse(response.body());
                        } else {
                            NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantDocumentsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCoApplicantDocumentResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(LoanTakerCoApplicantDocumentResponseModel body) {


        //profile pic
        String profileImage = body.getLoanTakerModel().getCoApplicantDocumentModel().isProfile_image_not_available();
        if( profileImage == null){
            //mark not available
        }else if(profileImage == "true"){
            //mark available
            isValidProfilePic=true;
            UiUtils.setMarkedNotAvailable(CoApplicantDocumentsActivity.this,profilePicDocumentNotAvailable);
            profilePicStatusTV.setVisibility(View.GONE);
        }else if (profileImage == "false"){
            //file uploaded
            isValidProfilePic=true;
            isProfilePicUploaded = true;
            profilePicUploadedUrl = body.getLoanTakerModel().getCoApplicantDocumentModel().getProfileImages().getOriginal();
            UiUtils.setDocumentUploaded(CoApplicantDocumentsActivity.this,profilePicStatusTV);
            profilePicDocumentNotAvailable.setVisibility(View.GONE);
            Picasso.with(CoApplicantDocumentsActivity.this)
                    .load(body.getLoanTakerModel().getCoApplicantDocumentModel().getProfileImages().getMedium())
                    .placeholder(R.drawable.userimg_placeholder)
                    .into(profilePhoto);
        }


        //aadhar front
        String aadhar_front = body.getLoanTakerModel().getCoApplicantDocumentModel().isAadhar_front_image_not_available();
        if( aadhar_front == null){
            //mark not available

        }else if(aadhar_front == "true"){
            //mark available
            isValidAadharFront=true;
            UiUtils.setMarkedNotAvailable(CoApplicantDocumentsActivity.this,aadharCardFrontNotAvailable);
            aadharCardFrontStatusTv.setVisibility(View.GONE);
        }else if (aadhar_front == "false"){
            isValidAadharFront=true;
            isAadharFrontUploaded=true;
            aadharFrontUploadedUrl = body.getLoanTakerModel().getCoApplicantDocumentModel().getAadhar_front_images().getOriginal();
            //file uploaded
            UiUtils.setDocumentUploaded(CoApplicantDocumentsActivity.this,aadharCardFrontStatusTv);
            aadharCardFrontNotAvailable.setVisibility(View.GONE);
            Picasso.with(CoApplicantDocumentsActivity.this)
                    .load(body.getLoanTakerModel().getCoApplicantDocumentModel().getAadhar_front_images().getMedium())
                    .placeholder(R.drawable.userimg_placeholder)
                    .into(aadharCardFrontImage);
        }

        //aadhar back
        String aadhar_back = body.getLoanTakerModel().getCoApplicantDocumentModel().isAadhar_back_image_not_available();
        if( aadhar_back == null){
            //mark not available

        }else if(aadhar_back == "true"){
            //mark available
            isValidAadharBack=true;
            UiUtils.setMarkedNotAvailable(CoApplicantDocumentsActivity.this,aadharCardBackNotAvailable);
            aadharCardBackStatusTv.setVisibility(View.GONE);
        }else if (aadhar_back == "false"){
            isValidAadharBack=true;
            isAadharBackUploaded = true;
            aadharBackUploadedUrl = body.getLoanTakerModel().getCoApplicantDocumentModel().getAadhar_back_images().getOriginal();
            //file uploaded
            UiUtils.setDocumentUploaded(CoApplicantDocumentsActivity.this,aadharCardBackStatusTv);
            aadharCardBackNotAvailable.setVisibility(View.GONE);
            Picasso.with(CoApplicantDocumentsActivity.this)
                    .load(body.getLoanTakerModel().getCoApplicantDocumentModel().getAadhar_back_images().getMedium())
                    .placeholder(R.drawable.userimg_placeholder)
                    .into(aadharCardBackImage);
        }

        //secondary
        String secondary_document = body.getLoanTakerModel().getCoApplicantDocumentModel().isSecondary_document_images_not_available();
        if( secondary_document == null){
            //mark not available

        }else if(secondary_document == "true"){
            //mark available
            isValidSecondaryItemSelected=true;
            UiUtils.setMarkedNotAvailable(CoApplicantDocumentsActivity.this,secondaryIdNotAvailable);
            secondaryIdStatusTv.setVisibility(View.GONE);
        }else if (secondary_document == "false"){

            secondaryDummyImage.setVisibility(View.GONE);
            secondaryGrid.setVisibility(View.VISIBLE);
            //file uploaded
            isValidSecondaryItemSelected=true;
            isSecondaryDocumentsUploaded=true;
            UiUtils.setDocumentUploaded(CoApplicantDocumentsActivity.this,secondaryIdStatusTv);
            secondaryIDValue = Integer.parseInt(body.getLoanTakerModel().getCoApplicantDocumentModel().getSecondary_document_id());
            secondaryIdNotAvailable.setVisibility(View.GONE);


            for(int i=0;i<cacdSecondaryIDModelArrayListNew.size();i++){
                if(cacdSecondaryIDModelArrayListNew.get(i).getId().equalsIgnoreCase(body.getLoanTakerModel().getCoApplicantDocumentModel().getSecondaryIDModel().getId())){
                    secondarySpinner.setSelection(i);
                }
            }

            ArrayList<String> result = new ArrayList<>();
            secondaryIdArrayList = new ArrayList<>();

            for(int i=0;i<body.getLoanTakerModel().getCoApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().size();i++){
                Log.i("Shahana","--"+body.getLoanTakerModel().getCoApplicantDocumentModel()
                        .getSecondaryDocumentImageModelArrayList().get(i)
                        .getSecondaryDocumentImageModel().getUploadImages().getMedium());

                result.add(body.getLoanTakerModel().getCoApplicantDocumentModel()
                        .getSecondaryDocumentImageModelArrayList().get(i)
                .getSecondaryDocumentImageModel().getUploadImages().getOriginal());
                secondaryIdArrayList.add(body.getLoanTakerModel().getCoApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().get(i).
                        getSecondaryDocumentImageModel().getId());

            }



            secondaryGrid.setNetworkPhotosWithKey(result);

        }


    }

    private void editShowPopup(final String picUploadedUrl, final String value){
        View view = getLayoutInflater().inflate(R.layout.custom_edit_image_yes_no_dialog, null);
        TextView editTV =(TextView)view.findViewById(R.id.edit_tv);
        TextView showTV =(TextView)view.findViewById(R.id.show_tv);
        Button yesButton =(Button)view.findViewById(R.id.yes_button);



        AlertDialog.Builder builder = new AlertDialog.Builder(CoApplicantDocumentsActivity.this);
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
                    isAadharFrontClicked=false;
                    isAadharBackClicked=false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(CoApplicantDocumentsActivity.this);

                }else if (value.equalsIgnoreCase("aadhar_front")){
                    isAadharFrontClicked = true;
                    isProfilePicClicked = false;
                    isAadharBackClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(CoApplicantDocumentsActivity.this);

                }else if(value.equalsIgnoreCase("aadhar_back")){
                    isAadharBackClicked = true;
                    isProfilePicClicked = false;
                    isAadharFrontClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(CoApplicantDocumentsActivity.this);
                }


            }
        });

        showTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                ArrayList<PreviewFile> imageList = new ArrayList<>();
                imageList.add(new PreviewFile(picUploadedUrl, ""));
                Intent intent = new Intent(CoApplicantDocumentsActivity.this,
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
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.contact_person_upload_image:
                if(isProfilePicUploaded){

                    if(profilePicUploadedUrl!=null){
                        editShowPopup(profilePicUploadedUrl,"profile_pic");
                    }

                }else{
                    isProfilePicClicked = true;
                    isAadharFrontClicked=false;
                    isAadharBackClicked=false;
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
                    isAadharFrontClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
                }
                break;



            case R.id.profile_pic_document_not_available:
                updateProfilePicNotAvailable();
                break;

            case R.id.aadhar_card_front_document_not_available:
                updateAadharFrontNotAvailable();
                break;

            case R.id.aadhar_card_back_document_not_available:
                updateAadharBackNotAvailable();
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

        if((isValidProfilePic) &&
                (isValidAadharFront) && (isValidAadharBack) && (isValidSecondaryItemSelected)){


            goToFamilyPage();
//            goToGroupListingPage();
            updateTheStatusInCoApplicantDetailPage();

            Bundle params = new Bundle();
            params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
            params.putString("status","co_applicant_documents_uploaded");
            mFirebaseAnalytics.logEvent("co_applicant_documents", params);

        }else{

            if(!isValidProfilePic){
                UiUtils.setDocumentNotUploaded(CoApplicantDocumentsActivity.this,profilePicStatusTV);

            }

            if(!isValidAadharFront){
                UiUtils.setDocumentNotUploaded(CoApplicantDocumentsActivity.this,aadharCardFrontStatusTv);
            }

            if(!isValidAadharBack){
                UiUtils.setDocumentNotUploaded(CoApplicantDocumentsActivity.this,aadharCardBackStatusTv);
            }

            if(!isValidSecondaryItemSelected){
                UiUtils.setDocumentNotUploaded(CoApplicantDocumentsActivity.this,secondaryIdStatusTv);
            }
        }
    }

    private void goToFamilyPage() {
        Intent intent = new Intent(CoApplicantDocumentsActivity.this,CoApplicantFamilyActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateTheStatusInCoApplicantDetailPage() {
        CoApplicantDetailsActivity.getInstance().init();
        MemberDetailActivity.getInstance().init();
    }


    private void updateProfilePicNotAvailable() {
        try {
            Log.d("Bank  image", "start");
            //For Image
            RequestBody available_value = RequestBody.create(MediaType.parse("text/plain"),  "true");
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerCoApplicantDocumentResponseModel> call;
            call = apiService.updateCoApplicantDocumentValidationForProfilePic(
                    loanTakerID,
                    available_value);

            call.enqueue(new Callback<LoanTakerCoApplicantDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCoApplicantDocumentResponseModel> call, Response <LoanTakerCoApplicantDocumentResponseModel> response) {

                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            isValidProfilePic=true;
                            profilePicStatusTV.setVisibility(View.GONE);
                            UiUtils.setMarkedNotAvailable(CoApplicantDocumentsActivity.this,profilePicDocumentNotAvailable);
                            Toast.makeText(CoApplicantDocumentsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantDocumentsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCoApplicantDocumentResponseModel> call, Throwable t) {
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
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
            Call<LoanTakerCoApplicantDocumentResponseModel> call;
            call = apiService.updateCoApplicantDocumentValidationForAadharFront(
                    loanTakerID,
                    available_value);

            call.enqueue(new Callback<LoanTakerCoApplicantDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCoApplicantDocumentResponseModel> call, Response <LoanTakerCoApplicantDocumentResponseModel> response) {

                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            isValidAadharFront=true;
                            aadharCardFrontStatusTv.setVisibility(View.GONE);
                            UiUtils.setMarkedNotAvailable(CoApplicantDocumentsActivity.this,aadharCardFrontNotAvailable);
                            Toast.makeText(CoApplicantDocumentsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantDocumentsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCoApplicantDocumentResponseModel> call, Throwable t) {
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
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
            Call<LoanTakerCoApplicantDocumentResponseModel> call;
            call = apiService.updateCoApplicantDocumentValidationForAadharBack(
                    loanTakerID,
                    available_value);

            call.enqueue(new Callback<LoanTakerCoApplicantDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCoApplicantDocumentResponseModel> call, Response <LoanTakerCoApplicantDocumentResponseModel> response) {
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            isValidAadharBack=true;
                            aadharCardBackStatusTv.setVisibility(View.GONE);
                            UiUtils.setMarkedNotAvailable(CoApplicantDocumentsActivity.this,aadharCardBackNotAvailable);
                            Toast.makeText(CoApplicantDocumentsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantDocumentsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCoApplicantDocumentResponseModel> call, Throwable t) {
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
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
            Call<LoanTakerCoApplicantDocumentResponseModel> call;
            call = apiService.updateCoApplicantDocumentValidationForSecondaryID(
                    loanTakerID,
                    available_value);

            call.enqueue(new Callback<LoanTakerCoApplicantDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCoApplicantDocumentResponseModel> call, Response <LoanTakerCoApplicantDocumentResponseModel> response) {
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            isValidSecondaryItemSelected=true;
                            secondaryIdStatusTv.setVisibility(View.GONE);
                            UiUtils.setMarkedNotAvailable(CoApplicantDocumentsActivity.this,secondaryIdNotAvailable);
                            Toast.makeText(CoApplicantDocumentsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantDocumentsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCoApplicantDocumentResponseModel> call, Throwable t) {
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
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
                    showProgressDialog(CoApplicantDocumentsActivity.this);
                    new AsyncTaskRunnerForProfilePic().execute(String.valueOf(result.getUri()));
                }else if(isAadharFrontClicked){
                    isValidAadharFront = true;
                    showProgressDialog(CoApplicantDocumentsActivity.this);
                    new AsyncTaskRunnerForAadharFront().execute(String.valueOf(result.getUri()));
                }else if(isAadharBackClicked){
                    showProgressDialog(CoApplicantDocumentsActivity.this);
                    new AsyncTaskRunnerForAadharBack().execute(String.valueOf(result.getUri()));
                    isValidAadharBack = true;
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }else if(resultCode == RESULT_OK){

            ArrayList<String> deletedIdsArrayList=new ArrayList<>();
            secondaryGrid.onParentResult(requestCode, data);
            if(data!=null) {

                List<Image> images = (List<Image>) data.getSerializableExtra(NavigatorImage.EXTRA_PHOTOS_URL);
                ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                if (requestCode == NavigatorImage.RESULT_SELECT_PHOTOS && null != images) {


                    if (selectedImages != null) {
                        for (Image image : images) {
                            selectedImages.add(image);
                            Log.i("Suhail", "added" + image);
                        }

                    } else {
                        selectedImages = images;
                        Log.i("Suhail", "added" + images);
                    }


                    Log.i("Suhail", "size" + images.size());
                    for (Image image : images) {
                        Log.i("Suhail", "size path" + image.url);
                    }

                    if(selectedImages!=null){
                        isValidSecondaryItemSelected = true;
                        showProgressDialog(CoApplicantDocumentsActivity.this);
                        saveMultipleSecondaryDocuments();
                    }else{
                        isValidSecondaryItemSelected  = false;
                        Toast.makeText(this, "Add Secondary images", Toast.LENGTH_SHORT).show();
                    }


                } else if (requestCode == NavigatorImage.RESULT_IMAGE_SWITCHER && null != positions) {

                    Log.i("Suhail", "positionss sizes" + positions.size());
                    Log.i("Suhail", "positionsss" + positions);


                    if(positions.size()>0){
                        for (int position : positions) {
                            Log.i("Suhail", "removed pos" + position);
                            for(int j=0;j<secondaryIdArrayList.size();j++){
                                Log.i("Suhail", "removed"+ j + secondaryIdArrayList.get(j));

                                if(position==j){
                                    Log.i("Suhail", "delete"+secondaryIdArrayList.get(j) );

                                    deletedIdsArrayList.add(secondaryIdArrayList.get(j));

//
                                }


                            }

                        }

                        if(deletedIdsArrayList.size()>0){
                            deleteSecondaryDocuments(deletedIdsArrayList);
                        }


                    }

                }

            }

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
            UiUtils.setDocumentUploading(CoApplicantDocumentsActivity.this,profilePicStatusTV);
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
            UiUtils.setDocumentUploading(CoApplicantDocumentsActivity.this,aadharCardFrontStatusTv);
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
            UiUtils.setDocumentUploading(CoApplicantDocumentsActivity.this,aadharCardBackStatusTv);
        }


        @Override
        protected void onProgressUpdate(String... text) {


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
            Call<LoanTakerCoApplicantDocumentResponseModel> call;
            call = apiService.addCoApplicantProfileImage(
                    loanTakerID,
                    fileToUpload);

            call.enqueue(new Callback<LoanTakerCoApplicantDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCoApplicantDocumentResponseModel> call, Response <LoanTakerCoApplicantDocumentResponseModel> response) {
                    aadharCardFrontProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            aadharCardFrontNotAvailable.setVisibility(View.GONE);
                            UiUtils.setDocumentUploaded(CoApplicantDocumentsActivity.this,aadharCardFrontStatusTv);
                            isAadharFrontUploaded=true;
                            aadharFrontUploadedUrl = response.body().getLoanTakerModel().getCoApplicantDocumentModel().getAadhar_front_images().getOriginal();
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

//                            reload();
//                            Toast.makeText(CoApplicantDocumentsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantDocumentsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCoApplicantDocumentResponseModel> call, Throwable t) {
                    aadharCardFrontProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
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
            Call<LoanTakerCoApplicantDocumentResponseModel> call;
            call = apiService.addCoApplicantProfileImage(
                    loanTakerID,
                    fileToUpload);

            call.enqueue(new Callback<LoanTakerCoApplicantDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCoApplicantDocumentResponseModel> call, Response <LoanTakerCoApplicantDocumentResponseModel> response) {
                    aadharCardBackProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            aadharCardBackNotAvailable.setVisibility(View.GONE);
                            UiUtils.setDocumentUploaded(CoApplicantDocumentsActivity.this,aadharCardBackStatusTv);
                            isAadharBackUploaded=true;
                            aadharBackUploadedUrl = response.body().getLoanTakerModel().getCoApplicantDocumentModel().getAadhar_back_images().getOriginal();
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

//                            reload();
//                            Toast.makeText(CoApplicantDocumentsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantDocumentsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCoApplicantDocumentResponseModel> call, Throwable t) {
                    aadharCardBackProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
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
            Call<LoanTakerCoApplicantDocumentResponseModel> call;
            call = apiService.addCoApplicantProfileImage(
                    loanTakerID,
                    fileToUpload);

            call.enqueue(new Callback<LoanTakerCoApplicantDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCoApplicantDocumentResponseModel> call, Response <LoanTakerCoApplicantDocumentResponseModel> response) {
                    profilePicProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            UiUtils.setDocumentUploaded(CoApplicantDocumentsActivity.this,profilePicStatusTV);
                            profilePicDocumentNotAvailable.setVisibility(View.GONE);
                            isProfilePicUploaded=true;
                            profilePicUploadedUrl = response.body().getLoanTakerModel().getCoApplicantDocumentModel().getProfileImages().getOriginal();
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

//                            reload();
//                            Toast.makeText(CoApplicantDocumentsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantDocumentsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCoApplicantDocumentResponseModel> call, Throwable t) {
                    profilePicProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void deleteSecondaryDocuments(ArrayList<String> deletedIdsArrayList) {

        showProgressDialog(CoApplicantDocumentsActivity.this);
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
        Call<LoanTakerCoApplicantDocumentResponseModel> call = apiService.deleteCoApplicantMultipleImages(loanTakerID,finalRequestBody
        );
        call.enqueue(new Callback<LoanTakerCoApplicantDocumentResponseModel>() {
            @Override
            public void onResponse(Call<LoanTakerCoApplicantDocumentResponseModel> call, Response<LoanTakerCoApplicantDocumentResponseModel> response) {
                hideProgressDialog();
                Log.d("Aadhar onResponse", "" + response.code() + "--" + response.message());
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        //API Success is true
                        for(int i=0;i<response.body().getLoanTakerModel().getCoApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().size();i++) {
                            secondaryIdArrayList.remove(response.body().getLoanTakerModel().getCoApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().get(i).
                                    getSecondaryDocumentImageModel().getId());
                        }
                        Toast.makeText(CoApplicantDocumentsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        if(response.body().getLoanTakerModel().getCoApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().size()==0){
                            UiUtils.setDocumentNotUploaded(CoApplicantDocumentsActivity.this,secondaryIdStatusTv);
                            isValidSecondaryItemSelected=false;
                        }else{
                            UiUtils.setDocumentUploaded(CoApplicantDocumentsActivity.this,secondaryIdStatusTv);
                        }
                    } else {
                        Log.d("Aadhar onResponse", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantDocumentsActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Aadhar onResponse", "" + response.body().getErrors());
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<LoanTakerCoApplicantDocumentResponseModel> call, Throwable t) {
                hideProgressDialog();
                NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
            }
        });
    }

    private void saveMultipleSecondaryDocuments() {

        secondaryIdNotAvailable.setVisibility(View.GONE);
        UiUtils.setDocumentUploading(CoApplicantDocumentsActivity.this,secondaryIdStatusTv);
        MultipartBody.Part[] fileToUpload = new MultipartBody.Part[0];

        try {
            Log.d("CHeck 3 Sec image", "start");
            //For Image
            File file = null;

            if (selectedImages != null) {
               fileToUpload = new MultipartBody.Part[selectedImages.size()];
                for (int i=0;i<selectedImages.size();i++) {
                    Log.i("CHeck 4","size path"+ selectedImages.get(i).url);
                    Log.i("CHeck 5","document[secondary_document_images_attributes][" + i + "][image]");
//                    file = new File(decodeFile(selectedImages.get(i).url,desiredWidth,desiredHeight));
                    file= new File(selectedImages.get(i).url);
                    File compressedImageFile = new Compressor(this).compressToFile(file);
                    RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), compressedImageFile);
                    fileToUpload[i] = MultipartBody.Part.createFormData("document[secondary_document_images_attributes][" + i + "][image]", file.getName(), mFile);

                }
            }

            RequestBody secondary_id = RequestBody.create(MediaType.parse("text/plain"),  String.valueOf(secondaryIDValue));
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerCoApplicantDocumentResponseModel> call;
            if(selectedImages!=null){
                call = apiService.addCoApplicantMultipleImages(
                        loanTakerID,
                        fileToUpload,secondary_id);

            }else{
                call = apiService.addCoApplicantWithoutMultipleImages(
                        loanTakerID,
                        secondary_id);

            }

            call.enqueue(new Callback<LoanTakerCoApplicantDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCoApplicantDocumentResponseModel> call, Response <LoanTakerCoApplicantDocumentResponseModel> response) {
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {

                            secondaryIdArrayList = new ArrayList<>();
                            //API Success is true
                            isSecondaryDocumentsUploaded=true;
                            for(int i=0;i<response.body().getLoanTakerModel().getCoApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().size();i++) {
                                secondaryIdArrayList.add(response.body().getLoanTakerModel().getCoApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().get(i).
                                        getSecondaryDocumentImageModel().getId());
                            }
                            UiUtils.setDocumentUploaded(CoApplicantDocumentsActivity.this,secondaryIdStatusTv);
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

//                            Toast.makeText(CoApplicantDocumentsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                            for(int i=0;i<response.body().getLoanTakerModel().getCoApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().size();i++) {
                                secondaryIdArrayList.add(response.body().getLoanTakerModel().getCoApplicantDocumentModel().getSecondaryDocumentImageModelArrayList().get(i).
                                        getSecondaryDocumentImageModel().getId());
                            }
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantDocumentsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCoApplicantDocumentResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantDocumentsActivity.this, null, null);
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


    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
}
