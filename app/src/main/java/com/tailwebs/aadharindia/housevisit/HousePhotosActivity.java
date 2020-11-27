package com.tailwebs.aadharindia.housevisit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.housevisit.models.houseimages.HouseImagesResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.ScalingUtilities;
import com.tailwebs.aadharindia.utils.UiUtils;
import com.tailwebs.aadharindia.utils.custom.newmultipleimageupload.NavigatorImage;
import com.tailwebs.aadharindia.utils.custom.newmultipleimageupload.model.Image;
import com.tailwebs.aadharindia.utils.custom.newmultipleimageupload.view.ImageGridView;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HousePhotosActivity extends BaseActivity implements View.OnClickListener {


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;


    @BindView(R.id.house_photos_grid)
    ImageGridView housePhotosGrid;
    
    @BindView(R.id.house_photos_status_tv)
    TextView housePhotosStatusTv;

    @BindView(R.id.continue_button)
    Button continueButton;

    List<Image> selectedImages;
    ArrayList<String> houseVisitIdArrayList;
    private boolean isValidHousePhotos = false;

    String loanTakerID=null;

    private ProgressDialog mProgressDialog;


    int desiredWidth = 800,desiredHeight = 800,desiredWidthSmall = 300,desiredHeightSmall=300;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_photos);
        ButterKnife.bind(this);

        loanTakerID = GlobalValue.loanTakerId;
        housePhotosGrid.setGRID_TYPE(2);
        housePhotosGrid.setMaxValue(GlobalValue.houseImageCount);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("House Photos");
        headingTV.setTextAppearance(getApplicationContext(), R.style.MyActionBarHeading);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "HV House Photos", null);



        continueButton.setOnClickListener(this);


        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(HousePhotosActivity.this);
            getHousePhotos();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
    }

    private void getHousePhotos() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<HouseImagesResponseModel> call = apiService.getHouseImages(loanTakerID);
            call.enqueue(new Callback<HouseImagesResponseModel>() {
                @Override
                public void onResponse(Call<HouseImagesResponseModel> call, final Response<HouseImagesResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                            Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                            setValuesFromResponse(response.body());
                        } else {
                            NetworkUtils.handleErrorsForAPICalls(HousePhotosActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(HousePhotosActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<HouseImagesResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(HousePhotosActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(HouseImagesResponseModel body) {

        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(body));
            if (jsonObject.has("house_visit")){
                ArrayList<String> result = new ArrayList<>();
                houseVisitIdArrayList = new ArrayList<>();
                if(body.getHouseImagesModel().getHouseImageModels().size()>0) {
                    isValidHousePhotos=true;

                    for (int i = 0; i < body.getHouseImagesModel().getHouseImageModels().size(); i++) {
                        Log.i("Shahana", "--" + body.getHouseImagesModel().getHouseImageModels()
                                .get(i)
                                .getUploadImages().getMedium());

                        result.add(body.getHouseImagesModel().getHouseImageModels()
                                .get(i)
                                .getUploadImages().getOriginal());
                        houseVisitIdArrayList.add(body.getHouseImagesModel().getHouseImageModels().get(i).getId()
                              );

                    }
                }
                housePhotosGrid.setNetworkPhotosWithKey(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
      if(resultCode == RESULT_OK){
          ArrayList<String> deletedIdsArrayList=new ArrayList<>();
            housePhotosGrid.onParentResult(requestCode, data);
          if(data!=null) {
//              List<Image> images = (List<Image>) data.getSerializableExtra(NavigatorImage.EXTRA_PHOTOS_URL);
//              ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
//              if (requestCode == NavigatorImage.RESULT_SELECT_PHOTOS && null != images) {
//
//                  selectedImages = images;
//                  Log.i("CHeck 1", "size" + images.size());
//                  for (Image image : images) {
//                      Log.i("CHeck 2", "size path" + image.url);
//                  }
//
//                  isValidHousePhotos = true;
//                  saveHousePhotos();
//
//              }

              List<Image> images = (List<Image>) data.getSerializableExtra(NavigatorImage.EXTRA_PHOTOS_URL);
              ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
              if (requestCode == NavigatorImage.RESULT_SELECT_PHOTOS && null != images) {



                  selectedImages = images;
                  Log.i("CHeck 1", "size" + images.size());
                  for (Image image : images) {
                      Log.i("CHeck 2", "size path" + image.url);
                  }


                  if(selectedImages!=null){
                      isValidHousePhotos = true;
                      saveHousePhotos();
                  }else{
                      isValidHousePhotos  = false;
                      Toast.makeText(this, "Add House images", Toast.LENGTH_SHORT).show();
                  }


              } else if (requestCode == NavigatorImage.RESULT_IMAGE_SWITCHER && null != positions) {

                  Log.i("Suhail", "positionss sizes" + positions.size());
                  Log.i("Suhail", "positionsss" + positions);


                  if(positions.size()>0){
                      for (int position : positions) {
                          Log.i("Suhail", "removed pos" + position);
                          for(int j=0;j<houseVisitIdArrayList.size();j++){
                              Log.i("Suhail", "removed"+ j + houseVisitIdArrayList.get(j));

                              if(position==j){
                                  Log.i("Suhail", "delete"+houseVisitIdArrayList.get(j) );

                                  deletedIdsArrayList.add(houseVisitIdArrayList.get(j));

//
                              }


                          }

                      }

                      if(deletedIdsArrayList.size()>0){
                          deleteHouseDocuments(deletedIdsArrayList);
                      }


                  }

              }

          }


        }
    }

    private void deleteHouseDocuments(ArrayList<String> deletedIdsArrayList) {
        showProgressDialog(HousePhotosActivity.this);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);


        for(int i=0;i<deletedIdsArrayList.size();i++){

            Log.d("Aadhar onResponse", "" + "document[house_images_attributes]["+i+"][_destroy]");

            Log.d("Aadhar onResponse", "" + deletedIdsArrayList.get(i));

            builder.addFormDataPart("house_visit[house_images_attributes]["+i+"][_destroy]", "true");
            builder.addFormDataPart("house_visit[house_images_attributes]["+i+"][id]", deletedIdsArrayList.get(i));
        }


        RequestBody finalRequestBody = builder.build();
        ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
        Call<HouseImagesResponseModel> call = apiService.deleteHousePhotos(loanTakerID,finalRequestBody
        );
        call.enqueue(new Callback<HouseImagesResponseModel>() {
            @Override
            public void onResponse(Call<HouseImagesResponseModel> call, Response<HouseImagesResponseModel> response) {
                hideProgressDialog();
                Log.d("Aadhar onResponse", "" + response.code() + "--" + response.message());
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        //API Success is true
                        for(int i=0;i<response.body().getHouseImagesModel().getHouseImageModels().size();i++) {
                            houseVisitIdArrayList.remove(response.body().getHouseImagesModel().getHouseImageModels().get(i).getId()
                                  );
                        }
                        Toast.makeText(HousePhotosActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        if(response.body().getHouseImagesModel().getHouseImageModels().size()==0){
                            UiUtils.setDocumentNotUploaded(HousePhotosActivity.this,housePhotosStatusTv);
                            isValidHousePhotos=false;
                        }else{
                            UiUtils.setDocumentUploaded(HousePhotosActivity.this,housePhotosStatusTv);
                        }
                    } else {
                        Log.d("Aadhar onResponse", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsCasesForAPICalls(HousePhotosActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Aadhar onResponse", "" + response.body().getErrors());
                    NetworkUtils.handleErrorsForAPICalls(HousePhotosActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<HouseImagesResponseModel> call, Throwable t) {
                hideProgressDialog();
                NetworkUtils.handleErrorsForAPICalls(HousePhotosActivity.this, null, null);
            }
        });
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



    private void saveHousePhotos() {

      
        UiUtils.setDocumentUploading(HousePhotosActivity.this,housePhotosStatusTv);

        try {
            Log.d("CHeck 3 Sec image", "start");
            //For Image
            File file = null;
            MultipartBody.Part[] fileToUpload = new MultipartBody.Part[selectedImages.size()];
            if (selectedImages != null) {
                for (int i=0;i<selectedImages.size();i++) {
                    Log.i("CHeck 4","size path"+ selectedImages.get(i).url);
                    Log.i("CHeck 5","house_visit[house_images_attributes][" + i + "][image]");
                    file = new File(decodeFile(selectedImages.get(i).url,desiredWidth,desiredHeight));
                    RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
                    fileToUpload[i] = MultipartBody.Part.createFormData("house_visit[house_images_attributes][" + i + "][image]", file.getName(), mFile);

                }
            }

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<HouseImagesResponseModel> call;
            call = apiService.addHousePhotos(
                    loanTakerID,
                    fileToUpload);

            call.enqueue(new Callback<HouseImagesResponseModel>() {
                @Override
                public void onResponse(Call<HouseImagesResponseModel> call, Response<HouseImagesResponseModel> response) {

                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            UiUtils.setDocumentUploaded(HousePhotosActivity.this,housePhotosStatusTv);
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/AadharFolder";
                            File file = new File(path);

                            try {
                                FileUtils.deleteDirectory(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(HousePhotosActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(HousePhotosActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(HousePhotosActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<HouseImagesResponseModel> call, Throwable t) {
                    NetworkUtils.handleErrorsForAPICalls(HousePhotosActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void goToSignaturePage() {
        if( GlobalValue.isHouseVisitDeclarationCompleted){
            startActivity(new Intent(HousePhotosActivity.this,SignatureShowActivity.class));
        }else{
            startActivity(new Intent(HousePhotosActivity.this,HouseVisitDeclarationActivity.class));
        }

    }

    private void updateTheStatusInHouseDetailPage() {
        HouseDetailsActivity.getInstance().init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.continue_button:
                if(isValidHousePhotos) {

                    Bundle params = new Bundle();
                    params.putString("status","house_visit_house_photos_created");
                    params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
                    mFirebaseAnalytics.logEvent("house_visit_house_photos", params);

                    updateTheStatusInHouseDetailPage();
                    goToSignaturePage();
                }else{
                    if(!isValidHousePhotos){
                        UiUtils.setDocumentNotUploaded(HousePhotosActivity.this,housePhotosStatusTv);

                    }
                }
                break;

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

//            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
            // Part 2: Scale image
            scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
//            } else {
//                unscaledBitmap.recycle();
//                return path;
//            }

            // Store to tmp file

            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "AadharFolder");
            if (!file.exists()) {
                file.mkdir();
            }

            String s = System.currentTimeMillis()+"tmp.png";

            File f = new File(file.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 80, fos);
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
}
