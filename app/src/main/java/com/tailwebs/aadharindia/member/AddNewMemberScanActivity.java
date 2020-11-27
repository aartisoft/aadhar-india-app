package com.tailwebs.aadharindia.member;

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
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.aadharscan.AadharScanActivity;
import com.tailwebs.aadharindia.aadharscan.DataAttributes;
import com.tailwebs.aadharindia.aadharscan.basicscanutils.BeepManager;
import com.tailwebs.aadharindia.aadharscan.customscancodeutils.BarcodeCallback;
import com.tailwebs.aadharindia.aadharscan.customscancodeutils.BarcodeResult;
import com.tailwebs.aadharindia.aadharscan.customscancodeutils.DecoratedBarcodeView;
import com.tailwebs.aadharindia.aadharscan.customscancodeutils.DefaultDecoderFactory;
import com.tailwebs.aadharindia.member.applicant.AddCustomerDetailsActivity;
import com.tailwebs.aadharindia.member.models.CustomerResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;
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

public class AddNewMemberScanActivity extends BaseActivity implements View.OnClickListener {

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

    boolean isRead=false,isScanned=false;
    private ProgressDialog mProgressDialog;


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;


    @BindView(R.id.barcode_scanner)
    DecoratedBarcodeView barcodeView;

    private BeepManager beepManager;
    private String lastText,groupId;
    private boolean isValidAadhar = false;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_member_scan);
        ButterKnife.bind(this);

        GlobalValue.loanTakerId=null;
        groupId = GlobalValue.groupId;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }
        }

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Add New Member");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "New Applicant Aadhaar Scan ", null);


        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);
        placeTV.setText(GlobalValue.placeName);

        continueButton.setOnClickListener(this);
        aadharNoET.addTextChangedListener(new InputLayoutTextWatcher(aadharNoET));
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {

            Log .i("AadharScan","resut"+result);
//            if(!isRead){
                processScannedData(result.getText());
                isRead=true;
//            }

            beepManager.playBeepSoundAndVibrate();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }}


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

        showProgressDialog(AddNewMemberScanActivity.this);

        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Log.i("AadharScan", "" + groupId);
            Call<CustomerResponseModel> call = apiService.getCustomerByAadharNumber(aadharNoET.getText().toString().trim(),groupId);
            call.enqueue(new Callback<CustomerResponseModel>() {
                @Override
                public void onResponse(Call<CustomerResponseModel> call, Response<CustomerResponseModel> response) {
                    hideProgressDialog();
                    Log.i("AadharScan", "" + response.code()+response.message());
                    Log.d("AadharScan onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            try {
                                JSONObject jsonObject = new JSONObject( new Gson().toJson(response.body()));
                                if (jsonObject.has("type")){

                                    if(response.body().getType()!=null){
                                        if(response.body().getType().equalsIgnoreCase("already_in_group")){

                                            showDialogBox(response.body());

                                        }else{
                                            goToScanResponsePage(response.body());
                                        }
                                    }
                                }else{
                                    goToScanResponsePage(response.body());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }





                        } else {
                            goToCustomerDetailsPage(response.body());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(AddNewMemberScanActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<CustomerResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("AadharScan", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(AddNewMemberScanActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void showDialogBox(CustomerResponseModel body) {

        View view = getLayoutInflater().inflate(R.layout.custom_message_dialog, null);

        TextView messageTV =(TextView)view.findViewById(R.id.message_tv);
        messageTV.setText(body.getNotice());
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewMemberScanActivity.this);
        builder.setCancelable(false)
                .setTitle("Already Present")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //disable button
//                        continueButton.setEnabled(false);
//                        continueButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));
//                        aadharNoET.setText("");
//                        isValidAadhar = false;
//                        aadharNoLayout.setErrorEnabled(true);
                        dialog.dismiss();
                        finish();

                    }
                })
                .setNeutralButton("", null)
                .setView(view);


        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    private void goToScanResponsePage(CustomerResponseModel body) {

        Intent intent = new Intent(AddNewMemberScanActivity.this,MemberScanResponseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("customer_name",body.getCustomerModel().getName());
        bundle.putString("customer_id",body.getCustomerModel().getId());
        bundle.putString("customer_status",body.getCustomerModel().getLoanStatusModel().getCustomer_status());
        bundle.putString("rating", body.getCustomerModel().getLoanStatusModel().getRating());
        bundle.putString("status_type", body.getCustomerModel().getLoanStatusModel().getStatus_type());
        if( body.getCustomerModel().getLoanStatusModel().getStatus_type().equalsIgnoreCase("on_going")){
            bundle.putString("loan_status", body.getCustomerModel().getLoanStatusModel().getLoan_status());
            bundle.putString("outstanding_amount", body.getCustomerModel().getLoanStatusModel().getOutstanding_amount());
            bundle.putString("overdue_amount", body.getCustomerModel().getLoanStatusModel().getOverdue_amount());
        }
        bundle.putString("status", body.getCustomerModel().getLoanStatusModel().getStatus());
        bundle.putString("rejection_reason", body.getCustomerModel().getLoanStatusModel().getLoan_reason());
        bundle.putString("rejection_description", body.getCustomerModel().getLoanStatusModel().getLoan_description());
        bundle.putString("profile_image", body.getCustomerModel().getProfileImages().getMedium());
        bundle.putBoolean("is_scanned",isScanned);
        bundle.putString("aadhar_no", aadharNoET.getText().toString().trim());

        if(isScanned) {
            Bundle aadhar_bundle = new Bundle();

            aadhar_bundle.putString("username", name);
            aadhar_bundle.putString("user_co_name", gname);
            aadhar_bundle.putString("gender", gender);
            aadhar_bundle.putString("yob", yearOfBirth);
            aadhar_bundle.putString("dob", dateOfBirth);
            aadhar_bundle.putString("dobGuess", dateOfBirthGuess);
            aadhar_bundle.putString("co", careOf);
            aadhar_bundle.putString("lm", lm);
            aadhar_bundle.putString("house", house);
            aadhar_bundle.putString("street", street);
            aadhar_bundle.putString("loc", loc);
            aadhar_bundle.putString("vtc", villageTehsil);
            aadhar_bundle.putString("po", postOffice);
            aadhar_bundle.putString("dist", district);
            aadhar_bundle.putString("subdist", sub_district);
            aadhar_bundle.putString("state", state);
            aadhar_bundle.putString("pc", postCode);
            intent.putExtras(aadhar_bundle);
        }
        intent.putExtras(bundle);
        startActivity(intent);
        finish();


    }

    private void goToCustomerDetailsPage(CustomerResponseModel body) {


        Intent intent = new Intent(AddNewMemberScanActivity.this,MemberDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_scanned",isScanned);
        bundle.putString("aadhar_no", aadharNoET.getText().toString().trim());
        if(isScanned){
            Bundle aadhar_bundle = new Bundle();
            aadhar_bundle.putString("username",name);
            aadhar_bundle.putString("user_co_name",gname);
            aadhar_bundle.putString("gender",gender);
            aadhar_bundle.putString("yob",yearOfBirth);
            aadhar_bundle.putString("dob",dateOfBirth);
            aadhar_bundle.putString("dobGuess",dateOfBirthGuess);
            aadhar_bundle.putString("co",careOf);
            aadhar_bundle.putString("lm",lm);
            aadhar_bundle.putString("house",house);
            aadhar_bundle.putString("street",street);
            aadhar_bundle.putString("loc",loc);
            aadhar_bundle.putString("vtc",villageTehsil);
            aadhar_bundle.putString("po",postOffice);
            aadhar_bundle.putString("dist",district);
            aadhar_bundle.putString("subdist",sub_district);
            aadhar_bundle.putString("state",state);
            aadhar_bundle.putString("pc",postCode);
            intent.putExtras(aadhar_bundle);
        }
        intent.putExtras(bundle);

        startActivity(intent);
        finish();

    }

    protected void processScannedData(String scanData){
        Log.d("AadharScan",scanData);
        isScanned=true;
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
                    boolean aadharStatus = UiUtils.checkValidation(AddNewMemberScanActivity.this, aadharNoET, aadharNoLayout, itemPassed);

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
            AddNewMemberScanActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
