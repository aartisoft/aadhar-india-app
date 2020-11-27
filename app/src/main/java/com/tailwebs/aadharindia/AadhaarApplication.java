package com.tailwebs.aadharindia;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tailwebs.aadharindia.utils.Constants;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.SharedPreferenceUtils;

import io.fabric.sdk.android.Fabric;

public class AadhaarApplication extends MultiDexApplication
{
    @Override
    public void onCreate() {
        super.onCreate();


        GlobalValue.secret = SharedPreferenceUtils.getValue(AadhaarApplication.this, Constants.KEY_SECRET);
        GlobalValue.secret_id = SharedPreferenceUtils.getValue(AadhaarApplication.this, Constants.KEY_SECRET_ID);
        Log.i("Drools secret", "-   GlobalValue.secret-" + GlobalValue.secret + "-GlobalValue.secret_id-" + GlobalValue.secret_id);
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);

        // the following line is important
        Fresco.initialize(getApplicationContext());


        Context context = getApplicationContext();

        // register to be informed of activities starting up
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity,
                                          Bundle savedInstanceState) {



                // new activity created; force its orientation to portrait

//                if(activity.getLocalClassName().equalsIgnoreCase("housevisit.SignaturePadActivity")){
//                    activity.setRequestedOrientation(
//                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                }else if(activity.getLocalClassName().equalsIgnoreCase("member.declaration.FormFillingSignaturePadActivity")){
//                    activity.setRequestedOrientation(
//                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                }
//                else if(activity.getLocalClassName().equalsIgnoreCase("postapproval.PostApprovalSignatureActivity")){
//                    activity.setRequestedOrientation(
//                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                }

//                else {
//                    activity.setRequestedOrientation(
//                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }


        });



    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}