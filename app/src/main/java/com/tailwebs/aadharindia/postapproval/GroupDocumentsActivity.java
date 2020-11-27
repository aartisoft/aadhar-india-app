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

public class GroupDocumentsActivity extends BaseActivity implements View.OnClickListener {


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    private ProgressDialog mProgressDialog;


    //Group Photo

    @BindView(R.id.group_photo_upload_image)
    ImageView groupPhoto;

    @BindView(R.id.group_photo_status_tv)
    TextView groupPicStatusTV;


    @BindView(R.id.group_photo_progress_bar)
    ProgressBar groupPicProgressBar;


    //Group Photo

    @BindView(R.id.nach_form_page_image)
    ImageView nachFormPhoto;

    @BindView(R.id.nach_form_status_tv)
    TextView nachFormStatusTV;


    @BindView(R.id.nach_form_page_progress_bar)
    ProgressBar nachFormProgressBar;



    //other documents
    @BindView(R.id.other_documents_grid)
    ImageGridView otherDocumentsGrid;


    @BindView(R.id.others_documents_tv)
    TextView otherDocumentsTv;


    @BindView(R.id.continue_button)
    Button continueButton;


    private boolean isValidGroupPhoto = false;


    private boolean isGroupPhotoClicked = false, isNachFormClicked = false;

    List<Image> otherDocumentsSelectedImages;
    ArrayList<String> otherDocumentsIdArrayList;

    String groupId = null;


    String  commonUriPath = null;


    int desiredWidth = 800,desiredHeight = 800,desiredWidthSmall = 300,desiredHeightSmall=300;

    private boolean isGroupPhotoUploaded = false,isNachFormUploaded = false;

    String groupPhotoUploadedUrl=null,nachFormUploadedUrl=null;
    static GroupDocumentsActivity instance;

    private FirebaseAnalytics mFirebaseAnalytics;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_documents);
        ButterKnife.bind(this);
        instance= this;

        isStoragePermissionGranted();

        groupId = GlobalValue.groupId;


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "PA Group Documents", null);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Group Documents");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);


        groupPhoto.setOnClickListener(this);
        nachFormPhoto.setOnClickListener(this);


        otherDocumentsGrid.setGRID_TYPE(2);
        otherDocumentsGrid.setItemClicked("bank");
        otherDocumentsGrid.setMaxValue(5);
        continueButton.setOnClickListener(this);


        init();

    }

    public static GroupDocumentsActivity getInstance() {
        return instance;
    }

    private void init() {
        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(GroupDocumentsActivity .this);
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

        showProgressDialog(GroupDocumentsActivity.this);

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<GroupDocumentResponseModel> call = apiService.getGroupPostApprovalDocuments(groupId);
            call.enqueue(new Callback<GroupDocumentResponseModel>() {
                @Override
                public void onResponse(Call<GroupDocumentResponseModel> call, final Response<GroupDocumentResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            Log.i("Drools", "" + new Gson().toJson(response.body()));
                            setValuesFromResponse(response.body());



                        } else {
                            NetworkUtils.handleErrorsForAPICalls(GroupDocumentsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(GroupDocumentsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<GroupDocumentResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(GroupDocumentsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(GroupDocumentResponseModel body) {

        try {


            //profile pic

            String group_Photo = body.getLoanTakerModel().getPostApprovalDocumentModel().getGroup_photo_not_available();
            if( group_Photo == null){

                //Document missing .Upload Again in red
                isValidGroupPhoto=false;
//                UiUtils.setDocumentUploadedisMissing(GroupDocumentsActivity.this,groupPicStatusTV);

                //mark not available
            }else if(group_Photo == "true"){
                //mark available
                isValidGroupPhoto=true;
                UiUtils.setMarkedNotAvailable(GroupDocumentsActivity.this,groupPicStatusTV);
            }else if (group_Photo == "false"){
                //file uploaded
                String profileImageIsCorrect =body.getLoanTakerModel().getPostApprovalDocumentModel().getGroup_photo().getIs_correct();
                if(profileImageIsCorrect==null){

                    UiUtils.setDocumentUploaded(GroupDocumentsActivity.this,groupPicStatusTV);
                    isValidGroupPhoto=true;

                }else if(profileImageIsCorrect=="true"){

                    UiUtils.setDocumentApproved(GroupDocumentsActivity.this,groupPicStatusTV);
                    isValidGroupPhoto=true;

                }else if (profileImageIsCorrect =="false"){

                    isValidGroupPhoto=false;
                    UiUtils.setDocumentUploadedisRejected(GroupDocumentsActivity.this,groupPicStatusTV,
                            body.getLoanTakerModel().getPostApprovalDocumentModel().getGroup_photo().getReject_reason());

                }

                isGroupPhotoUploaded = true;
                groupPhotoUploadedUrl = body.getLoanTakerModel().getPostApprovalDocumentModel().getGroup_photo().getOriginal();
                Picasso.with(GroupDocumentsActivity.this)
                        .load(body.getLoanTakerModel().getPostApprovalDocumentModel().getGroup_photo().getMedium())
                        .placeholder(R.drawable.img_placeholder)
                        .into(groupPhoto);
            }



            //nach form

            String nach_form = body.getLoanTakerModel().getPostApprovalDocumentModel().getNach_photo_not_available();
            if( nach_form == null){

                //Document missing .Upload Again in red
             
//                UiUtils.setDocumentUploadedisMissing(GroupDocumentsActivity.this,nachFormStatusTV);

                //mark not available
            }else if(nach_form == "true"){
                //mark available
            
                UiUtils.setMarkedNotAvailable(GroupDocumentsActivity.this,nachFormStatusTV);
            }else if (nach_form == "false"){
                //file uploaded
                String bank_first_pageImageIsCorrect =body.getLoanTakerModel().getPostApprovalDocumentModel().getNach_photo().getIs_correct();
                if(bank_first_pageImageIsCorrect==null){

                    UiUtils.setDocumentUploaded(GroupDocumentsActivity.this,nachFormStatusTV);
                 

                }else if(bank_first_pageImageIsCorrect=="true"){

                    UiUtils.setDocumentApproved(GroupDocumentsActivity.this,nachFormStatusTV);
                  

                }else if (bank_first_pageImageIsCorrect =="false"){

                  
                    UiUtils.setDocumentUploadedisRejected(GroupDocumentsActivity.this,nachFormStatusTV,
                            body.getLoanTakerModel().getPostApprovalDocumentModel().getNach_photo().getReject_reason());

                }

                isNachFormUploaded = true;
                nachFormUploadedUrl = body.getLoanTakerModel().getPostApprovalDocumentModel().getNach_photo().getOriginal();

                Picasso.with(GroupDocumentsActivity.this)
                        .load(body.getLoanTakerModel().getPostApprovalDocumentModel().getNach_photo().getMedium())
                        .placeholder(R.drawable.img_placeholder)
                        .into(nachFormPhoto);
            }


            //others

            if(body.getLoanTakerModel().getPostApprovalDocumentModel().getPostApprovalOtherDocumentsModelArrayList().size()>0){

                UiUtils.setDocumentUploaded(GroupDocumentsActivity.this,otherDocumentsTv);
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


    private void editShowPopup(final String picUploadedUrl, final String value){
        View view = getLayoutInflater().inflate(R.layout.custom_edit_image_yes_no_dialog, null);
        TextView editTV =(TextView)view.findViewById(R.id.edit_tv);
        TextView showTV =(TextView)view.findViewById(R.id.show_tv);
        Button yesButton =(Button)view.findViewById(R.id.yes_button);



        AlertDialog.Builder builder = new AlertDialog.Builder(GroupDocumentsActivity.this);
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

                if(value.equalsIgnoreCase("group_photo")){
                    isGroupPhotoClicked = true;
                    isNachFormClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(GroupDocumentsActivity.this);

                }else if (value.equalsIgnoreCase("nach")){
                    isGroupPhotoClicked = false;
                    isNachFormClicked = true;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(GroupDocumentsActivity.this);

                }


            }
        });

        showTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                ArrayList<PreviewFile> imageList = new ArrayList<>();
                imageList.add(new PreviewFile(picUploadedUrl, ""));
                Intent intent = new Intent(GroupDocumentsActivity.this,
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

            case R.id.group_photo_upload_image:
                if(isGroupPhotoUploaded){

                    if(groupPhotoUploadedUrl!=null){
                        editShowPopup(groupPhotoUploadedUrl,"group_photo");
                    }

                }else {
                    isGroupPhotoClicked = true;
                    isNachFormClicked = false;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
                }
                break;

            case R.id.nach_form_page_image:
                if(isNachFormUploaded){

                    if(nachFormUploadedUrl!=null){
                        editShowPopup(nachFormUploadedUrl,"nach");
                    }

                }else {
                    isGroupPhotoClicked = false;
                    isNachFormClicked = true;
                    CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
                }
                break;

            case R.id.continue_button:

                if((isValidGroupPhoto)){

                    Bundle params = new Bundle();
                    params.putString("status","post_approval_group_documents_created");
                    mFirebaseAnalytics.logEvent("post_approval_group_documents", params);


                    Intent intent = new Intent(GroupDocumentsActivity.this,PostApprovalTaskDetailsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                } else
                    if(!isValidGroupPhoto){
                UiUtils.setDocumentNotUploaded(GroupDocumentsActivity.this,groupPicStatusTV);

            }
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                if(isGroupPhotoClicked){
                    isValidGroupPhoto =true;
                    showProgressDialog(GroupDocumentsActivity.this);
                    new AsyncTaskRunnerForGroupPhoto().execute(String.valueOf(result.getUri()));
                }else if (isNachFormClicked){
                    showProgressDialog(GroupDocumentsActivity.this);
                    new AsyncTaskRunnerForNachForm().execute(String.valueOf(result.getUri()));
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
//                    saveMultipleOtherImages();
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
                        showProgressDialog(GroupDocumentsActivity.this);
                        saveMultipleOtherImages();
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

        showProgressDialog(GroupDocumentsActivity.this);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);


        for(int i=0;i<deletedIdsArrayList.size();i++){

            Log.d("Aadhar onResponse", "" + "document[group_post_approval_other_documents_attributes]["+i+"][_destroy]");

            Log.d("Aadhar onResponse", "" + deletedIdsArrayList.get(i));

            builder.addFormDataPart("document[group_post_approval_other_documents_attributes]["+i+"][_destroy]", "true");
            builder.addFormDataPart("document[group_post_approval_other_documents_attributes]["+i+"][id]", deletedIdsArrayList.get(i));
        }


        RequestBody finalRequestBody = builder.build();
        ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
        Call<GroupDocumentResponseModel> call = apiService.deleteMultipleGroupPostApprovalDocuments(groupId,finalRequestBody
        );
        call.enqueue(new Callback<GroupDocumentResponseModel>() {
            @Override
            public void onResponse(Call<GroupDocumentResponseModel> call, Response<GroupDocumentResponseModel> response) {
                hideProgressDialog();
                Log.d("Aadhar onResponse", "" + response.code() + "--" + response.message());
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        //API Success is true
                        for(int i=0;i<response.body().getLoanTakerModel().getPostApprovalDocumentModel().getPostApprovalOtherDocumentsModelArrayList().size();i++) {
                            otherDocumentsIdArrayList.remove(response.body().getLoanTakerModel().getPostApprovalDocumentModel().getPostApprovalOtherDocumentsModelArrayList().get(i).
                                    getId());
                        }
                        Toast.makeText(GroupDocumentsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        if(response.body().getLoanTakerModel().getPostApprovalDocumentModel().getPostApprovalOtherDocumentsModelArrayList().size()==0){
                            UiUtils.setDocumentNotUploaded(GroupDocumentsActivity.this,otherDocumentsTv);

                        }else{
                            UiUtils.setDocumentUploaded(GroupDocumentsActivity.this,otherDocumentsTv);
                        }
                    } else {
                        Log.d("Aadhar onResponse", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsCasesForAPICalls(GroupDocumentsActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Aadhar onResponse", "" + response.body().getErrors());
                    NetworkUtils.handleErrorsForAPICalls(GroupDocumentsActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<GroupDocumentResponseModel> call, Throwable t) {
                hideProgressDialog();
                NetworkUtils.handleErrorsForAPICalls(GroupDocumentsActivity.this, null, null);
            }
        });
    }


    // other documents

    private void saveMultipleOtherImages() {

        UiUtils.setDocumentUploading(GroupDocumentsActivity.this,otherDocumentsTv);

        try {
            Log.d("Drools bank image", "start");
            //For Image
            File file = null;
            MultipartBody.Part[] fileToUpload = new MultipartBody.Part[otherDocumentsSelectedImages.size()];
            if (otherDocumentsSelectedImages != null) {
                for (int i=0;i<otherDocumentsSelectedImages.size();i++) {
                    Log.i("Drools","size path"+ otherDocumentsSelectedImages.get(i).url);
                    Log.i("Drools","document[group_post_approval_other_documents_attributes][" + i + "][image]");
//                    file = new File(decodeFile(otherDocumentsSelectedImages.get(i).url,desiredWidth,desiredHeight));
                    file= new File(otherDocumentsSelectedImages.get(i).url);
                    File compressedImageFile = new Compressor(this).compressToFile(file);
                    RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile);
                    fileToUpload[i] = MultipartBody.Part.createFormData("document[group_post_approval_other_documents_attributes][" + i + "][image]", file.getName(), mFile);

                }
            }

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<GroupDocumentResponseModel> call;
            call = apiService.addMultipleGroupPostApprovalDocuments(
                    groupId,
                    fileToUpload);

            call.enqueue(new Callback<GroupDocumentResponseModel>() {
                @Override
                public void onResponse(Call<GroupDocumentResponseModel> call, Response <GroupDocumentResponseModel> response) {
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            UiUtils.setDocumentUploaded(GroupDocumentsActivity.this,otherDocumentsTv);
                            GroupDocumentsActivity.getInstance().init();
                            Toast.makeText(GroupDocumentsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(GroupDocumentsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(GroupDocumentsActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<GroupDocumentResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(GroupDocumentsActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //nach form

    private class AsyncTaskRunnerForNachForm extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {
                Uri myUri = Uri.parse(params[0]);
                ((ImageView) findViewById(R.id.nach_form_page_image)).setImageURI(myUri);
            }  catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

            Bitmap bm=((BitmapDrawable)nachFormPhoto.getDrawable()).getBitmap();
            saveImageFile(bm);
            saveNachForm();
            isNachFormClicked = false;
        }


        @Override
        protected void onPreExecute() {
            nachFormProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }

    //group photo

    private class AsyncTaskRunnerForGroupPhoto extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {

            try {
                Uri myUri = Uri.parse(params[0]);
                ((ImageView) findViewById(R.id.group_photo_upload_image)).setImageURI(myUri);
            }  catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

            Bitmap bm=((BitmapDrawable)groupPhoto.getDrawable()).getBitmap();
            saveImageFile(bm);
            saveGroupPhoto();
            isGroupPhotoClicked = false;
        }


        @Override
        protected void onPreExecute() {
            groupPicProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onProgressUpdate(String... text) {


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

    private void saveGroupPhoto() {


        UiUtils.setDocumentUploading(GroupDocumentsActivity.this,groupPicStatusTV);

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
                fileToUpload = MultipartBody.Part.createFormData("document[group_photo]", file.getName(), mFile);
            }

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<GroupDocumentResponseModel> call;
            call = apiService.addGroupPostApprovalDocuments(
                    groupId,
                    fileToUpload);

            call.enqueue(new Callback<GroupDocumentResponseModel>() {
                @Override
                public void onResponse(Call<GroupDocumentResponseModel> call, Response<GroupDocumentResponseModel> response) {
                    groupPicProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            UiUtils.setDocumentUploaded(GroupDocumentsActivity.this,groupPicStatusTV);
                            isGroupPhotoUploaded = true;
                            groupPhotoUploadedUrl = response.body().getLoanTakerModel().getPostApprovalDocumentModel().getGroup_photo().getOriginal();
                            Toast.makeText(GroupDocumentsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(GroupDocumentsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(GroupDocumentsActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<GroupDocumentResponseModel> call, Throwable t) {
                    groupPicProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(GroupDocumentsActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveNachForm() {

        UiUtils.setDocumentUploading(GroupDocumentsActivity.this,nachFormStatusTV);

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
                fileToUpload = MultipartBody.Part.createFormData("document[nach_photo]", file.getName(), mFile);
            }

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<GroupDocumentResponseModel> call;
            call = apiService.addGroupPostApprovalDocuments(
                    groupId,
                    fileToUpload);

            call.enqueue(new Callback<GroupDocumentResponseModel>() {
                @Override
                public void onResponse(Call<GroupDocumentResponseModel> call, Response<GroupDocumentResponseModel> response) {
                    nachFormProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            UiUtils.setDocumentUploaded(GroupDocumentsActivity.this,nachFormStatusTV);
                            isNachFormUploaded = true;
                            nachFormUploadedUrl = response.body().getLoanTakerModel().getPostApprovalDocumentModel().getNach_photo().getOriginal();
                            Toast.makeText(GroupDocumentsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(GroupDocumentsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(GroupDocumentsActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<GroupDocumentResponseModel> call, Throwable t) {
                    nachFormProgressBar.setVisibility(View.GONE);
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(GroupDocumentsActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

}
