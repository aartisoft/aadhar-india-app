package com.tailwebs.aadharindia.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.mukesh.OtpView;
import com.tailwebs.aadharindia.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UiUtils {

    private static ProgressDialog mProgressDialog;

    public static void showAlertDialogWithOKButton(final Context context, String msg, DialogInterface.OnClickListener onClickListener) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(msg);
            builder.setPositiveButton(context.getString(R.string.action_ok), onClickListener);
            builder.setCancelable(false);
            final AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button btnPositive = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
                    TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                    textView.setTextSize(18);
                    btnPositive.setTextSize(18);
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    public static boolean isValidPan(String pan) {
        boolean check = false;
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        Matcher matcher = pattern .matcher(pan);

        if (!matcher.matches()) {
        if (pan.length() != 10) {
            // if(zip.length() != 6) {
            check = false;
        } else {
            check = true;
        }
        } else {
            check = false;
        }
        return check;
    }

    public static boolean isValidZipCode(String zip) {
        boolean check = false;

        if (zip.length() != 6) {
            // if(zip.length() != 6) {
            check = false;
        } else {
            check = true;
        }

        return check;
    }

    public static boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() != 10) {
                // if(phone.length() != 10) {
                check = false;
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }



    public static void ifNoDataAvailable(Context context, TextView itemLabel, TextView itemValue){
        itemLabel.setTextColor(context.getResources().getColor(R.color.editTextNotSelectedColor));
        itemLabel.setVisibility(View.VISIBLE);
        itemValue.setVisibility(View.VISIBLE);
        itemValue.setTextColor(context.getResources().getColor(R.color.editTextNotSelectedColor));
        itemValue.setText("-");
    }


    public static boolean checkValidationForOTP(Context context, OtpView passwordOTPView, TextView errorTV){
        String eTValue = passwordOTPView.getText().toString().trim();

        Log.i("Drools Res","inside else"+eTValue.length());

        if (eTValue.length() == 0) {
            errorTV.setText(context.getString(R.string.field_required));
            errorTV.setVisibility(View.VISIBLE);
            passwordOTPView.setLineColor(context.getResources().getColor(R.color.errorColor));
            return false;

        } else if (eTValue.length() > 0 && eTValue.length() < 6 && !eTValue.isEmpty()) {

                errorTV.setText(context.getString(R.string.error_invalid_pwd));
                errorTV.setVisibility(View.VISIBLE);
                passwordOTPView.setLineColor(context.getResources().getColor(R.color.errorColor));

                return false;
            } else {

                errorTV.setVisibility(View.GONE);
                passwordOTPView.setLineColor(context.getResources().getColor(R.color.primaryColor));
            }
        return true;
    }



    public static boolean checkValidationForCustomErrorTV(Context context, TextInputEditText inputEditText, TextView errorTV){
        String eTValue = inputEditText.getText().toString().trim();

        Log.i("Drools Res","inside else"+eTValue.length());

//        if (eTValue.length() == 0) {
//            errorTV.setText(context.getString(R.string.field_required));
//            errorTV.setVisibility(View.VISIBLE);
//            inputEditText.(context.getResources().getColor(R.color.errorColor));
//            return false;
//
//        } else if (eTValue.length() > 0 && eTValue.length() < 6 && !eTValue.isEmpty()) {
//
//            errorTV.setText(context.getString(R.string.error_invalid_pwd));
//            errorTV.setVisibility(View.VISIBLE);
//            passwordOTPView.setLineColor(context.getResources().getColor(R.color.errorColor));
//
//            return false;
//        } else {
//
//            errorTV.setVisibility(View.GONE);
//            passwordOTPView.setLineColor(context.getResources().getColor(R.color.primaryColor));
//        }
        return true;
    }



    public static boolean setProcessNotCompleted(Context context, TextView inputTV){
        inputTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        inputTV.setTextAppearance(context,R.style.MyDisabledFormHeadingTextView);
        inputTV.setEnabled(false);

        return true;
    }

    public static boolean setProcessCompleted(Context context, TextView inputTV){
        inputTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
        inputTV.setTextAppearance(context,R.style.MyFormHeadingTextView);
        inputTV.setEnabled(true);

        return true;
    }

    public static boolean setProcessNotCompleteWithImaged(Context context, TextView inputTV){
        inputTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_cross, 0);
        inputTV.setTextAppearance(context,R.style.MyFormHeadingTextView);
        inputTV.setEnabled(true);

        return true;
    }



    public static boolean setProcessActivated(Context context, TextView inputTV){
        inputTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        inputTV.setTextAppearance(context,R.style.MyFormHeadingTextView);
        inputTV.setEnabled(true);

        return true;
    }

    public static boolean setButtonEnabled(Context context, Button button){
        button.setEnabled(true);
        button.setBackground(context.getResources().getDrawable(R.drawable.disabled_button_bg));

        return true;
    }

    public static boolean setButtonDisabled(Context context, Button button){
        button.setEnabled(false);
        button.setBackground(context.getResources().getDrawable(R.drawable.primary_button_bg));

        return true;
    }

    public static boolean setDocumentNotUploaded(Context context, TextView inputTV){

        inputTV.setTextColor(context.getResources().getColor(R.color.errorColor));
        inputTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        inputTV.setText(context.getResources().getString(R.string.field_required));
        inputTV.setVisibility(View.VISIBLE);

        return true;
    }


    public static boolean setMarkedNotAvailable(Context context, TextView inputTV){

        inputTV.setTextColor(context.getResources().getColor(R.color.editTextTitleColor));
        inputTV.setText(context.getResources().getString(R.string.hint_marked_not_available));
        inputTV.setVisibility(View.VISIBLE);

        return true;
    }


    public static boolean setDocumentUploading(Context context, TextView inputTV){
        inputTV.setVisibility(View.VISIBLE);
        inputTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        inputTV.setTextColor(context.getResources().getColor(R.color.editTextTitleColor));
        inputTV.setText("Uploading...");


        return true;
    }

    public static boolean setDocumentUploaded(Context context, TextView inputTV){
        inputTV.setVisibility(View.VISIBLE);
        inputTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_tick, 0, 0, 0);
        inputTV.setTextColor(context.getResources().getColor(R.color.successColor));
        inputTV.setText(context.getResources().getString(R.string.hint_document_uploaded));
        inputTV.invalidate();


        return true;
    }

    public static boolean setDocumentApproved(Context context, TextView inputTV){
        inputTV.setVisibility(View.VISIBLE);
        inputTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_tick, 0, 0, 0);
        inputTV.setTextColor(context.getResources().getColor(R.color.successColor));
        inputTV.setText(context.getResources().getString(R.string.hint_document_approved));


        return true;
    }


    public static boolean setDocumentUploadedisMissing(Context context, TextView inputTV){

        inputTV.setTextColor(context.getResources().getColor(R.color.errorColor));
        inputTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_cross, 0, 0, 0);
        inputTV.setText(context.getResources().getString(R.string.hint_document_missing));
        inputTV.setVisibility(View.VISIBLE);

        return true;
    }

    public static boolean setDocumentUploadedisRejected(Context context, TextView inputTV,String rejectReason){

        inputTV.setTextColor(context.getResources().getColor(R.color.errorColor));
        inputTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.red_cross, 0, 0, 0);
        inputTV.setText(context.getResources().getString(R.string.hint_document_rejected)+" "+rejectReason);
        inputTV.setVisibility(View.VISIBLE);

        return true;
    }


    public static boolean checkValidationForAutoCompleteTV(Context context, AutoCompleteTextView inputET, TextInputLayout inputLayoutET, ArrayList<String> itemPassedArray) {
        String eTValue = inputET.getText().toString().trim();
        if(itemPassedArray.size()==0){
            if (eTValue.length() >= 0 && eTValue.isEmpty() != false) {

                inputLayoutET.setError(context.getString(R.string.field_required));
                return false;
            } else {

                inputLayoutET.setErrorEnabled(false);
            }
            return true;
        }else{
            for(int i=0;i<itemPassedArray.size();i++){
                String itemPassed = itemPassedArray.get(i);

                switch (itemPassed) {
                    case "email": {
                        try {
                            if (eTValue.isEmpty() || !isValidEmail(eTValue)) {
                                inputLayoutET.setError(context.getString(R.string.error_invalid_email_id));
                                inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                return false;
                            } else {
                                inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                                inputLayoutET.setErrorEnabled(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;

                    }
                    case "password": {
                        if (eTValue.length() >= 0 && eTValue.length() < 6 && eTValue.isEmpty() != false) {
                            inputLayoutET.setError(context.getString(R.string.error_invalid_pwd));
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            return false;
                        } else {
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                            inputLayoutET.setErrorEnabled(false);
                        }
                        return true;
                    }

                    case "aadhaar": {
                        if (eTValue.length() > 0 && eTValue.length() < 12 && eTValue.isEmpty() != false) {
                            inputLayoutET.setError(context.getString(R.string.error_invalid_aadhar));
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            return false;
                        } else {
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                            inputLayoutET.setErrorEnabled(false);
                        }
                        return true;
                    }
                    case "address": {
                        if (eTValue.length() > 10 && eTValue.length() < 220 && !eTValue.isEmpty()  && eTValue != null && !eTValue.matches("")) {
                            inputLayoutET.setError(context.getString(R.string.error_invalid_address));
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            return false;
                        } else {
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                            inputLayoutET.setErrorEnabled(false);
                        }
                        return true;
                    }
                    case "pan":{
                        if (eTValue.isEmpty() != false && !isValidPan(eTValue)) {
                            inputLayoutET.setError(context.getString(R.string.error_invalid_pan));
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            return false;
                        } else {
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                            inputLayoutET.setErrorEnabled(false);
                        }
                        return true;
                    }
                    case "mobile": {
                        if (eTValue.isEmpty() != false && !isValidMobile(eTValue)) {
                            inputLayoutET.setError(context.getString(R.string.error_invalid_mobile));
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            return false;
                        } else {
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                            inputLayoutET.setErrorEnabled(false);
                        }
                        return true;
                    }
                    case "otp": {
                        if (eTValue.isEmpty() != false && eTValue.length() > 0 && eTValue.length() < 6) {
                            inputLayoutET.setError(context.getString(R.string.error_invalid_otp));
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            return false;
                        } else {
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                            inputLayoutET.setErrorEnabled(false);
                        }
                        return true;
                    }
                    case "zipCode": {
                        if (eTValue.isEmpty() != false &&!isValidZipCode(eTValue) && eTValue.length() > 0 ) {
                            inputLayoutET.setError(context.getString(R.string.error_invalid_zip_code));
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            return false;
                        } else {
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                            inputLayoutET.setErrorEnabled(false);
                        }
                        return true;
                    }
                    case "": {

                        if (eTValue.length() >= 0 && eTValue.isEmpty() != false) {
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            inputLayoutET.setError(context.getString(R.string.field_required));
                            return false;
                        } else {
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                            inputLayoutET.setErrorEnabled(false);
                        }
                        return true;
                    }
                }
            }
//            default:
//                if (eTValue.length() >= 0) {
//                    inputLayoutET.setError(context.getString(R.string.field_required));
//                    return false;
//                } else {
//                    inputLayoutET.setErrorEnabled(false);
//                }
//                return true;
        }


        return true;

    }

    public static boolean checkValidation(Context context, TextInputEditText inputET, TextInputLayout inputLayoutET, ArrayList<String> itemPassedArray) {
        String eTValue = inputET.getText().toString().trim();
        if(itemPassedArray.size()==0){
            if (eTValue.length() >= 0 && eTValue.isEmpty() != false) {

                inputLayoutET.setError(context.getString(R.string.field_required));
                return false;
            } else {

                inputLayoutET.setErrorEnabled(false);
            }
            return true;
        }else{
            for(int i=0;i<itemPassedArray.size();i++){
                String itemPassed = itemPassedArray.get(i);

                switch (itemPassed) {
                    case "email": {
                        try {
                            if (eTValue.isEmpty() || !isValidEmail(eTValue)) {
                                inputLayoutET.setError(context.getString(R.string.error_invalid_email_id));
                                inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                return false;
                            } else {
                                inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                                inputLayoutET.setErrorEnabled(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;

                    }
                    case "password": {
                        if (eTValue.length() >= 0 && eTValue.length() < 6 && eTValue.isEmpty() != false) {
                            inputLayoutET.setError(context.getString(R.string.error_invalid_pwd));
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            return false;
                        } else {
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                            inputLayoutET.setErrorEnabled(false);
                        }
                        return true;
                    }
                    case "address": {
                        if (eTValue.length() >= 10 && eTValue.length() < 221) {
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                            inputLayoutET.setErrorEnabled(false);
                        } else {
                            inputLayoutET.setError(context.getString(R.string.error_invalid_address));
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            return false;

                        }
                        return true;
                    }
                    case "aadhaar": {
                        if (eTValue.length() > 0 && eTValue.length() < 12 && !eTValue.isEmpty()  && eTValue != null && !eTValue.matches("")) {
                            inputLayoutET.setError(context.getString(R.string.error_invalid_aadhar));
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            return false;
                        } else {
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                            inputLayoutET.setErrorEnabled(false);
                        }
                        return true;
                    }
                    case "mobile": {
//                        if (eTValue.isEmpty() ||eTValue.length() == 0 || eTValue.equals("") || eTValue == null && !isValidMobile(eTValue) ) {
                        if ( !isValidMobile(eTValue) ) {
                            inputLayoutET.setError(context.getString(R.string.error_invalid_mobile));
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            return false;
                        } else {
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                            inputLayoutET.setErrorEnabled(false);
                        }
                        return true;
                    }
                    case "otp": {
                        if (eTValue.isEmpty() != false && eTValue.length() > 0 && eTValue.length() < 6) {
                            inputLayoutET.setError(context.getString(R.string.error_invalid_otp));
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            return false;
                        } else {
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                            inputLayoutET.setErrorEnabled(false);
                        }
                        return true;
                    }
                    case "zipCode": {
                        if (!isValidZipCode(eTValue)) {
                            inputLayoutET.setError(context.getString(R.string.error_invalid_zip_code));
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            return false;
                        } else {
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                            inputLayoutET.setErrorEnabled(false);
                        }
                        return true;
                    }
                    case "": {

//                        if (eTValue.isEmpty() ||eTValue.length() == 0 || eTValue.equals("") || eTValue == null && !isValidMobile(eTValue) ) {
                        if (eTValue.length() >= 0 && eTValue.isEmpty() != false ) {
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            inputLayoutET.setError(context.getString(R.string.field_required));
                            return false;
                        } else {
                            inputET.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                            inputLayoutET.setErrorEnabled(false);
                        }
                        return true;
                    }
                }
            }
//            default:
//                if (eTValue.length() >= 0) {
//                    inputLayoutET.setError(context.getString(R.string.field_required));
//                    return false;
//                } else {
//                    inputLayoutET.setErrorEnabled(false);
//                }
//                return true;
        }


        return true;

    }

    public static String saveImageToSDcard(Bitmap finalBitmap, String folderPath) {
        File myDir = new File(folderPath);
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public static ArrayList<String> getAllImagesFromSdcard(String folderPath) {
        ArrayList<String> fileNames = new ArrayList<String>();
        File[] listFile;
        File file = new File(folderPath);
        if (file.isDirectory()) {
            listFile = file.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                fileNames.add(listFile[i].getAbsolutePath());
            }
        }
        return fileNames;
    }

    public static boolean deleteDirectory(String folderPath) {
        File path = new File(folderPath);
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(folderPath);
                    } else {
                        files[i].delete();
                    }
                }
            }
        }
        return (path.delete());
    }



    public static String getDate(String dob){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date d = sdf.parse(dob);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            return checkDigit(cal.get(Calendar.DATE));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getMonth(String dob){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date d = sdf.parse(dob);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            return checkDigit(cal.get(Calendar.MONTH)+1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getYear(String dob){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date d = sdf.parse(dob);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);

            return checkDigit(cal.get(Calendar.YEAR));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getDateDash(String dob){

        boolean checkFormat;
        Calendar cal;
        try {

            if (dob.matches("([0-9]{2})-([0-9]{2})-([0-9]{4})"))
                checkFormat=true;
            else
                checkFormat=false;



            if(checkFormat){
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date d = sdf.parse(dob);
                cal = Calendar.getInstance();
                cal.setTime(d);
            }else{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date d = sdf.parse(dob);
                cal = Calendar.getInstance();
                cal.setTime(d);
            }


            return checkDigit(cal.get(Calendar.DATE));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getMonthDash(String dob){

        boolean checkFormat;
        Calendar cal;
        try {

            if (dob.matches("([0-9]{2})-([0-9]{2})-([0-9]{4})"))
                checkFormat=true;
            else
                checkFormat=false;



            if(checkFormat){
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date d = sdf.parse(dob);
                cal = Calendar.getInstance();
                cal.setTime(d);


            }else{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date d = sdf.parse(dob);
                cal = Calendar.getInstance();
                cal.setTime(d);

            }


           return checkDigit(cal.get(Calendar.MONTH)+1);

        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//            Date d = sdf.parse(dob);
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(d);
//            return checkDigit(cal.get(Calendar.MONTH)+1);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return null;
    }

    public static String getYearDash(String dob){



        boolean checkFormat;
        Calendar cal;
        try {

            if (dob.matches("([0-9]{2})-([0-9]{2})-([0-9]{4})"))
                checkFormat=true;
            else
                checkFormat=false;



            if(checkFormat){
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date d = sdf.parse(dob);
                 cal = Calendar.getInstance();
                cal.setTime(d);


            }else{
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date d = sdf.parse(dob);
                 cal = Calendar.getInstance();
                cal.setTime(d);

            }


            return checkDigit(cal.get(Calendar.YEAR));

        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//            Date d = sdf.parse(dob);
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(d);
//
//            return checkDigit(cal.get(Calendar.YEAR));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return null;
    }



    public static String checkDigit (int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }


    public static String parseDateToAadhaarFormat(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd/MM/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }



    public static String parseDateToTaskFormat(String time) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "dd MMM, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


}
