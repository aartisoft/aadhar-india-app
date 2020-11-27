package com.tailwebs.aadharindia.aadharscan;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.center.CenterShowActivity;
import com.tailwebs.aadharindia.center.CreateNewCenterActivity;
import com.tailwebs.aadharindia.center.searchinmap.models.CityCenterResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.NetworkUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AadharScanSampleActivity extends BaseActivity {

    // variables to store extracted xml data
    String uid,name,gname,gender,yearOfBirth,dateOfBirth,dateOfBirthGuess,careOf,villageTehsil,postOffice,district,state,postCode,lm
            ,house,street,sub_district,loc;


    private ProgressDialog mProgressDialog;

    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aadhar_scan_sample);

        // we need to check if the user has granted the camera permissions
        // otherwise scanner will not work
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            return;
        }

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("");
//        integrator.setResultDisplayDuration(500);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }


    /**
     * function handle scan result
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result

            Log.d("AadharScan",scanningResult.getContents());
            Log.d("AadharScan",scanningResult.getFormatName());
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            // process received data
            if(scanContent != null && !scanContent.isEmpty()){
                processScannedData(scanContent);
            }else{
                Toast toast = Toast.makeText(getApplicationContext(),"Scan Cancelled", Toast.LENGTH_SHORT);
                toast.show();
            }

        }else{
            Toast toast = Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    /**
     * process xml string received from aadhaar card QR code
     * @param scanData
     */
    protected void processScannedData(String scanData){
        Log.d("AadharScan",scanData);
        XmlPullParserFactory pullParserFactory;

        try {
            // init the parserfactory
            pullParserFactory = XmlPullParserFactory.newInstance();
            // get the parser
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(scanData));

            // parse the XML
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d("AadharScan","Start document");
                } else if(eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(parser.getName())) {
                    // extract data from tag
                    //uid
                    uid = parser.getAttributeValue(null,DataAttributes.AADHAR_UID_ATTR);
                    Log.d("AadharScan","Start document"+uid);
                    //name
                    name = parser.getAttributeValue(null,DataAttributes.AADHAR_NAME_ATTR);
                    Log.d("AadharScan","Start document"+name);
                    //gname
                    gname = parser.getAttributeValue(null,DataAttributes.AADHAR_GNAME_ATTR);
                    //gender
                    gender = parser.getAttributeValue(null,DataAttributes.AADHAR_GENDER_ATTR);
                    // year of birth
                    yearOfBirth = parser.getAttributeValue(null,DataAttributes.AADHAR_YOB_ATTR);
                    // date of birth
                    dateOfBirth = parser.getAttributeValue(null,DataAttributes.AADHAR_DOB_ATTR);
                    // date of birth guess
                    dateOfBirthGuess = parser.getAttributeValue(null,DataAttributes.AADHAR_DOB_GUESS_ATTR);
                    // care of
                    careOf = parser.getAttributeValue(null,DataAttributes.AADHAR_CO_ATTR);
                    // lm
                    lm = parser.getAttributeValue(null,DataAttributes.AADHAR_LM_ATTR);
                    // house
                    house = parser.getAttributeValue(null,DataAttributes.AADHAR_HOUSE_ATTR);
                    // street
                    street = parser.getAttributeValue(null,DataAttributes.AADHAR_STREET_ATTR);
                    // locality
                    loc = parser.getAttributeValue(null,DataAttributes.AADHAR_LOC_ATTR);
                    // village Tehsil
                    villageTehsil = parser.getAttributeValue(null,DataAttributes.AADHAR_VTC_ATTR);
                    // Post Office
                    postOffice = parser.getAttributeValue(null,DataAttributes.AADHAR_PO_ATTR);
                    // district
                    district = parser.getAttributeValue(null,DataAttributes.AADHAR_DIST_ATTR);
                    // sub-district
                    sub_district = parser.getAttributeValue(null,DataAttributes.AADHAR_SUB_DIST_ATTR);
                    // state
                    state = parser.getAttributeValue(null,DataAttributes.AADHAR_STATE_ATTR);
                    // Post Code
                    postCode = parser.getAttributeValue(null,DataAttributes.AADHAR_PC_ATTR);

                } else if(eventType == XmlPullParser.END_TAG) {
                    Log.d("AadharScan","End tag "+parser.getName());

                } else if(eventType == XmlPullParser.TEXT) {
                    Log.d("AadharScan","Text "+parser.getText());

                }
                // update eventType
                eventType = parser.next();
            }

            // display the data on screen
            
            checkIfPresent();
            Log.d("AadharScan","checkIfPresent");

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }// EO function

    private void checkIfPresent() {

        showProgressDialog(AadharScanSampleActivity.this);

        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<CityCenterResponseModel> call = apiService.getCityCentersByAadharNumber(uid);
            call.enqueue(new Callback<CityCenterResponseModel>() {
                @Override
                public void onResponse(Call<CityCenterResponseModel> call, Response<CityCenterResponseModel> response) {
                    hideProgressDialog();
                    Log.i("AadharScan", "" + response.code()+response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                           Intent intent = new Intent(AadharScanSampleActivity.this,CenterShowActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", response.body().getCenterModels().getContactPersonModel().getName());
                    bundle.putString("user_co_name", response.body().getCenterModels().getContactPersonModel().getAadhar_co());
                    bundle.putString("phone", response.body().getCenterModels().getContactPersonModel().getPhone_number());
                    bundle.putString("aadhar_no", response.body().getCenterModels().getContactPersonModel().getAadhar_number());
                    bundle.putString("dob", response.body().getCenterModels().getContactPersonModel().getDob());
                    bundle.putString("gender", response.body().getCenterModels().getContactPersonModel().getGender());
                    bundle.putString("center_address", response.body().getCenterModels().getCenterAddressModel().getFull_address());
                    bundle.putString("profile_image", response.body().getCenterModels().getContactPersonModel().getProfileImages().getOriginal());
//                    bundle.putString("center_images", item.getCityCenterImages().t);
                    intent.putExtras(bundle);
                   startActivity(intent);
                        } else {
                           startActivity(new Intent(AadharScanSampleActivity.this,CreateNewCenterActivity.class));
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(AadharScanSampleActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<CityCenterResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("AadharScan", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(AadharScanSampleActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
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
