package com.tailwebs.aadharindia.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.internal.LinkedTreeMap;
import com.tailwebs.aadharindia.DashboardActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.loginandforgot.LoginActivity;

import java.util.ArrayList;
import java.util.Map;

public class NetworkUtils {


    /**
     * To check Network Connection
     * @param context
     * @return
     */
    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return haveConnectedWifi = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                // check NetworkInfo subtype
                Log.d("Internet drools --",""+activeNetwork.getSubtype());

                switch (activeNetwork.getSubtype()){

//                    2G
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        Log.d("Internet drools 2g"," NETWORK_TYPE_GPRS");
                        return haveConnectedMobile = false;
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        Log.d("Internet drools 2g","NETWORK_TYPE_EDGE");
                        return haveConnectedMobile = false;
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        Log.d("Internet drools 2g","NETWORK_TYPE_CDMA");
                        return haveConnectedMobile = false;
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        Log.d("Internet drools 2g","NETWORK_TYPE_1xRTT");
                        return haveConnectedMobile = false;
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        Log.d("Internet drools 2g","NETWORK_TYPE_IDEN");
                        return haveConnectedMobile = false;
//                       3G
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        Log.d("Internet drools 3g","NETWORK_TYPE_UMTS");
                        return haveConnectedMobile = true;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        Log.d("Internet drools 3g","NETWORK_TYPE_EVDO_0");
                        return haveConnectedMobile = true;
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        Log.d("Internet drools 3g","NETWORK_TYPE_EVDO_A");
                        return haveConnectedMobile = true;
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        Log.d("Internet drools 3g","NETWORK_TYPE_HSDPA");
                        return haveConnectedMobile = true;
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        Log.d("Internet drools 3g","NETWORK_TYPE_HSUPA");
                        return haveConnectedMobile = true;
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        Log.d("Internet drools 3g","NETWORK_TYPE_HSPA");
                        return haveConnectedMobile = true;
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        Log.d("Internet drools 3g","NETWORK_TYPE_EVDO_B");
                        return haveConnectedMobile = true;
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        Log.d("Internet drools 3g","NETWORK_TYPE_EHRPD");
                        return haveConnectedMobile = true;
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        Log.d("Internet drools 3g","NETWORK_TYPE_HSPAP");
                        return haveConnectedMobile = true;
//                       4G
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        Log.d("Internet drools 4g","NETWORK_TYPE_LTE");
                        return haveConnectedMobile = true;
//                      default
                    default:
                        return haveConnectedMobile = false;
                }


//                if(activeNetwork.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS){
//                    // Bandwidth between 100 kbps and below
//                    Log.d("Internet drools","between 100 kbps and below");
//                    return haveConnectedMobile = false;
//                } else if(activeNetwork.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE){
//                    // Bandwidth between 50-100 kbps
//                    Log.d("Internet drools","between 50-100 kbps");
//                    return haveConnectedMobile = false;
//                } else if(activeNetwork.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_0){
//                    // Bandwidth between 400-1000 kbps
//                    Log.d("Internet drools","between 400-1000 kbps");
//                    return haveConnectedMobile = true;
//                } else if(activeNetwork.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_A){
//                    // Bandwidth between 600-1400 kbps
//                    Log.d("Internet drools","between 600-1400 kbps");
//                    return haveConnectedMobile = true;
//                } else if(activeNetwork.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE){
//                    // Bandwidth above 1400 kbps
//                    Log.d("Internet drools","above 1400 kbps");
//                    return haveConnectedMobile = true;
//                }


            }
        } else {
            // not connected to the internet
            haveConnectedMobile = false;
            haveConnectedWifi = false;
        }

        return haveConnectedWifi || haveConnectedMobile;
    }

    /**
     * For handling errors in API calls
     * @param context
     * @param errors
     * @param notice
     */

    public static void handleErrorsForAPICalls(Context context, Object errors, Object notice) {
        Log.e("responseCode = ", "--" +notice+"--"+errors);

        try {
            if(errors != null && notice != null){
                String errorTxt = parseError(errors);
                String msg = errorTxt + "\n"+ notice.toString();
                UiUtils.showAlertDialogWithOKButton(context, msg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

            }else if (errors != null) {
                Log.i("error","5");
                Log.i("error",""+parseError(errors));
                if(errors.toString().equalsIgnoreCase("Login Credentials Failed.")){
                    Log.i("error","yes");
                    SharedPreferenceUtils.clearUserLoggedIn(context);
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);

                }else{
                    Log.i("error","no");
                    UiUtils.showAlertDialogWithOKButton(context, parseError(errors), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                }

            } else if (notice != null && !notice.toString().isEmpty()) {
                UiUtils.showAlertDialogWithOKButton(context, (String) notice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            } else {
                UiUtils.showAlertDialogWithOKButton(context, context.getResources().getString(R.string.error_api_failure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            }
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }


    public static void handleErrorsForAPICallsForLoginOnly(Context context, Object errors, Object notice) {
        Log.e("responseCode = ", "--" +notice+"--"+errors);

        try {
            if(errors != null && notice != null){
                String errorTxt = parseError(errors);
                String msg = errorTxt + "\n"+ notice.toString();
                UiUtils.showAlertDialogWithOKButton(context, msg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

            }else if (errors != null) {
                Log.i("error","5");
                Log.i("error",""+parseError(errors));

                    Log.i("error","no");
                    UiUtils.showAlertDialogWithOKButton(context, parseError(errors), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });


            } else if (notice != null && !notice.toString().isEmpty()) {
                UiUtils.showAlertDialogWithOKButton(context, (String) notice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            } else {
                UiUtils.showAlertDialogWithOKButton(context, context.getResources().getString(R.string.error_api_failure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            }
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    /**
     * Parse the error coming from the API calls
     * @param object
     * @return
     */

    public static String parseError(Object object) {
        try {
            if (object != null) {
                try {
                    LinkedTreeMap<String, Object> errorMap = (LinkedTreeMap<String, Object>) object;
                    for (Map.Entry<String, Object> entry : errorMap.entrySet()) {
                        try {
                            ArrayList<String> values = (ArrayList<String>) entry.getValue();
                            Log.e("Key = ", "" + entry.getKey() + ", Value = " + entry.getValue());
                            return values.get(0);
                        } catch (Exception e) {
                            String[] values = (String[]) entry.getValue();
                            Log.e("Key = ", "" + entry.getKey() + ", Value = " + entry.getValue());
                            return values[0];
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if(object instanceof ArrayList) {
                        return ((ArrayList) object).get(0).toString();
                    }else{
                        return (String) object;
                    }
                }
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void handleErrorsCasesForAPICalls(Context context, int responseCode, Object errors) {

        try {
            Log.e("responseCode = ", "--" +responseCode);
            switch (responseCode){


                case 400:{
                    handleErrorsForAPICalls(context, errors, null);
                }
                break;
                case 401:{
                    handleErrorsForAPICalls(context, errors, null);
                }
                break;
                case 404:{
                    handleErrorsForAPICalls(context, errors, null);

                }
                break;
                case 405:{
                    handleErrorsForAPICalls(context, errors, null);
                }
                break;
                case 500:{
                    handleErrorsForAPICalls(context, errors, null);
                }
                break;
                case 503:{
                    handleErrorsForAPICalls(context, errors, null);
                }
                break;
            }

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }


}
