package com.tailwebs.aadharindia.aadharscan;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.aadharscan.basicscanutils.BeepManager;
import com.tailwebs.aadharindia.aadharscan.customscancodeutils.BarcodeCallback;
import com.tailwebs.aadharindia.aadharscan.customscancodeutils.BarcodeResult;
import com.tailwebs.aadharindia.aadharscan.customscancodeutils.DecoratedBarcodeView;
import com.tailwebs.aadharindia.aadharscan.customscancodeutils.DefaultDecoderFactory;
import com.tailwebs.aadharindia.center.CenterShowActivity;
import com.tailwebs.aadharindia.center.CreateNewCenterActivity;
import com.tailwebs.aadharindia.center.searchinmap.models.CityCenterResponseModel;
import com.tailwebs.aadharindia.member.AddNewMemberScanActivity;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AadharScanActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.input_layout_aadhar_no)
    TextInputLayout aadharNoLayout;

    @BindView(R.id.input_aadhar_no)
    TextInputEditText aadharNoET;

    @BindView(R.id.place_tv)
    TextView placeTV;


    @BindView(R.id.continue_button)
    Button continueButton;

    // variables to store extracted xml data
    String uid,name,gname,gender,yearOfBirth,dateOfBirth,dateOfBirthGuess,careOf,villageTehsil,postOffice,district,state,postCode,lm
            ,house,street,sub_district,loc;

    boolean isRead=false,isScanned=false;;

    static AadharScanActivity instance;



    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    private BeepManager beepManager;
    private String lastText;
    private ProgressDialog mProgressDialog;

   @BindView(R.id.barcode_scanner)
   DecoratedBarcodeView barcodeView;

    private boolean isValidAadhar = false;


    private static final int MY_CAMERA_REQUEST_CODE = 100;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aadhar_scan);
        ButterKnife.bind(this);
        instance=this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Center Aadhaar Scan", null);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Create New Center");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        continueButton.setOnClickListener(this);
        aadharNoET.addTextChangedListener(new InputLayoutTextWatcher(aadharNoET));

        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);
        beepManager = new BeepManager(this);
        setIsReadToFalse();
        placeTV.setText(getIntent().getExtras().getString("place_name"));


    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }}//end onRequestPermissionsResult
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {

            Log.i("AadharScan","resut"+result);
//            if(!isRead){
                processScannedData(result.getText());
                isRead=true;
//            }

//            if(result.getText() == null || result.getText().equals(lastText)) {
//                // Prevent duplicate scans
//                return;
//            }
//
//            lastText = result.getText();
//            barcodeView.setStatusText(result.getText());

            beepManager.playBeepSoundAndVibrate();

//            //Added preview of scanned barcode
//            ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
//            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    public void setIsReadToFalse(){
        isRead =false;
    }


    public static AadharScanActivity getInstance() {
        return instance;
    }


    protected void processScannedData(String scanData){
        Log.d("AadharScan",scanData);
        XmlPullParserFactory pullParserFactory;
        isScanned=true;

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

            aadharNoET.setText(uid);

            // set Button enabled

            continueButton.setEnabled(true);
            continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));


            Log.d("AadharScan","checkIfPresent");

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }// EO function

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){


            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.continue_button:
                continueButtonCall();
                break;
        }

    }

    private void continueButtonCall() {

        checkIfPresent();
    }


    private void checkIfPresent() {

        showProgressDialog(AadharScanActivity.this);

        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<CityCenterResponseModel> call = apiService.getCityCentersByAadharNumber(aadharNoET.getText().toString().trim());
            call.enqueue(new Callback<CityCenterResponseModel>() {
                @Override
                public void onResponse(Call<CityCenterResponseModel> call, Response<CityCenterResponseModel> response) {
                    hideProgressDialog();
                    Log.i("AadharScan", "" + response.code()+response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            showPasswordDialog(response);
                        } else {
                            Intent intent = new Intent(AadharScanActivity.this,CreateNewCenterActivity.class);
                            Bundle bundle = new Bundle();
                            if(isScanned) {
                                bundle.putString("username", name);
                                bundle.putString("user_co_name",careOf);
                                bundle.putString("dob", dateOfBirth);
                                bundle.putString("yob", yearOfBirth);
                                bundle.putString("gender", gender);
                            }
                            bundle.putBoolean("is_scanned",isScanned);
                            bundle.putString("aadhar_no", aadharNoET.getText().toString().trim());
                            bundle.putString("lm",lm);
                            bundle.putString("house",house);
                            bundle.putString("loc",loc);
                            bundle.putString("street", street);
                            bundle.putString("postOffice",postOffice);
                            bundle.putString("pin_code", postCode);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(AadharScanActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<CityCenterResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("AadharScan", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(AadharScanActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
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
                case R.id.input_aadhar_no:
                    itemPassed.clear();
                    itemPassed.add("aadhaar");
                    boolean aadharStatus = UiUtils.checkValidation(AadharScanActivity.this, aadharNoET, aadharNoLayout, itemPassed);

                    if (aadharStatus == false) {
                        isValidAadhar = false;
                        requestFocus(aadharNoET);

                        //disable button
                        continueButton.setEnabled(false);
                        continueButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

                    } else {
                        isValidAadhar = true;
                        aadharNoLayout.setErrorEnabled(false);

                        //enable button
                        continueButton.setEnabled(true);
                        continueButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    }

                    break;


            }
        }

    }

    public void requestFocus(View view) {
        if (view.requestFocus())
            AadharScanActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    private void showPasswordDialog(final Response<CityCenterResponseModel> response){
        // title, custom view, actions dialog


        View view = getLayoutInflater().inflate(R.layout.custom_message_dialog, null);

         TextView messageTV =(TextView)view.findViewById(R.id.message_tv);
         messageTV.setText(response.body().getNotice());
        AlertDialog.Builder builder = new AlertDialog.Builder(AadharScanActivity.this);
        builder.setCancelable(false)
                .setTitle("Center Present")
                .setPositiveButton("SHOW CENTER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        goToShowCenterPage(response);
                        dialog.dismiss();

                    }
                })
                .setNeutralButton("", null)
                .setView(view);


        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();



    }




    public void goToShowCenterPage(Response<CityCenterResponseModel> response){
        Intent intent = new Intent(AadharScanActivity.this,CenterShowActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("username", response.body().getCenterModels().getContactPersonModel().getName());
        bundle.putString("user_co_name", response.body().getCenterModels().getContactPersonModel().getAadhar_co());
        bundle.putString("phone", response.body().getCenterModels().getContactPersonModel().getPhone_number());
        bundle.putString("aadhar_no", response.body().getCenterModels().getContactPersonModel().getAadhar_number());
        bundle.putString("dob", response.body().getCenterModels().getContactPersonModel().getDob());
        bundle.putString("gender", response.body().getCenterModels().getContactPersonModel().getGender());
        bundle.putString("center_address", response.body().getCenterModels().getCenterAddressModel().getFull_address());
        bundle.putString("profile_image", response.body().getCenterModels().getContactPersonModel().getProfileImages().getOriginal());
        GlobalValue.ciTyCenterenterImages = response.body().getCenterModels().getCityCenterImages();
//                    bundle.putString("center_images", item.getCityCenterImages().t);
        intent.putExtras(bundle);
        startActivity(intent);
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
