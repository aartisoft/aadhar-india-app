package com.tailwebs.aadharindia.home.dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.DashboardActivity;
import com.tailwebs.aadharindia.MyProfileActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.geotracking.TrackerService;
import com.tailwebs.aadharindia.home.NewGroupTaskActivity;
import com.tailwebs.aadharindia.home.NewTaskActivity;
import com.tailwebs.aadharindia.home.fragments.AllTasksFragment;
import com.tailwebs.aadharindia.home.fragments.CollectionsFragment;
import com.tailwebs.aadharindia.home.fragments.GroupCreationFragment;
import com.tailwebs.aadharindia.home.fragments.HouseVisitFragment;
import com.tailwebs.aadharindia.home.fragments.OneFragment;
import com.tailwebs.aadharindia.home.fragments.PostApprovalDocumentsFragment;
import com.tailwebs.aadharindia.home.models.CheckUpdateResponseModel;
import com.tailwebs.aadharindia.home.models.TaskModel;
import com.tailwebs.aadharindia.loginandforgot.LoginActivity;
import com.tailwebs.aadharindia.member.SelectGroupLeaderActivity;
import com.tailwebs.aadharindia.models.Logout;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.Constants;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.SharedPreferenceUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.*;

public class TaskDashboardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnClickListener {


    @BindView(R.id.tabs)
    TabLayout tabLayout;

    private Animation animationUp, animationDown;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer ;

    @BindView(R.id.nav_view)
    NavigationView navigationView ;

    @BindView(R.id.SlidingDrawer)
    SlidingDrawer slidingDrawer;

    @BindView(R.id.add_title_button)
    Button addButton;

    @BindView(R.id.new_task_tv)
    TextView newTaskTv;

    @BindView(R.id.new_group_task_tv)
    TextView newGroupTaskTv;

    private ProgressDialog mProgressDialog;

    static TaskDashboardActivity instance;

    boolean doubleBackToExitPressedOnce = false;


//    @BindView(R.id.app_bar_layout)
//    RelativeLayout appBarLayout;


    int versionCode;


    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.content_layout)
    LinearLayout contentLayout;




    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_dashboard);
        ButterKnife.bind(this);

        String tkn = FirebaseInstanceId.getInstance().getToken();
//        Toast.makeText(TaskDashboardActivity.this, "Current token ["+tkn+"]", Toast.LENGTH_LONG).show();
        Log.d("App", "Token ["+tkn+"]");
        instance=this;
        GlobalValue.secret = SharedPreferenceUtils.getValue(TaskDashboardActivity.this, Constants.KEY_SECRET);
        GlobalValue.secret_id = SharedPreferenceUtils.getValue(TaskDashboardActivity.this, Constants.KEY_SECRET_ID);


        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        Toast.makeText(this,
//                "PackageName = " + info.packageName + "\nVersionCode = "
//                        + info.versionCode + "\nVersionName = "
//                        + info.versionName + "\nPermissions = " + info.permissions, Toast.LENGTH_SHORT).show();
        Log.d("App", "Package"+info.packageName + "\nVersionCode = "
                + info.versionCode + "\nVersionName = "
                + info.versionName + "\nPermissions = " + info.permissions);
        init();



    }

    public void init() {

        viewPager.setOffscreenPageLimit(0);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        animationUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        setListeners();

        // Listeners for sliding drawer
        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contentLayout.setBackgroundColor(getResources().getColor(R.color.trans));
                        contentLayout.setClickable(true);
                        contentLayout.setEnabled(true);
//                        appBarLayout.setBackgroundColor(getResources().getColor(R.color.trans));
//                        appBarLayout.setClickable(true);
//                        appBarLayout.setEnabled(true);
                    }
                });


            }
        });

        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        contentLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        contentLayout.setClickable(false);
                        contentLayout.setEnabled(false);
//                        appBarLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//                        appBarLayout.setClickable(false);
//                        appBarLayout.setEnabled(false);
                    }
                });


            }
        });


        tabLayout.setupWithViewPager(viewPager);

        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(slidingDrawer.isOpened()){
//                    slidingDrawer.startAnimation(animationDown);
//                    slidingDrawer.close();
                }else{
                    slidingDrawer.startAnimation(animationUp);
                    slidingDrawer.open();
                    fab.setVisibility(GONE);


                }

            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.username);
        TextView navContact = (TextView) headerView.findViewById(R.id.contact);
        navUsername.setText(SharedPreferenceUtils.getValue(TaskDashboardActivity.this, Constants.KEY_USER_NAME));
        navContact.setText(SharedPreferenceUtils.getValue(TaskDashboardActivity.this, Constants.KEY_USER_ID));


        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(TaskDashboardActivity.this);
            checkUpdate();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

        setupViewPager(viewPager);
    }

    private void checkUpdate() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<CheckUpdateResponseModel> call = apiService.checkUpdate(versionCode,"android");
            call.enqueue(new Callback<CheckUpdateResponseModel>() {
                @Override
                public void onResponse(Call<CheckUpdateResponseModel> call, final Response<CheckUpdateResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.code()+response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Log.i("Drools", "" + new Gson().toJson(response.body()));

                            try {
                                JSONObject jsonObject =  new JSONObject(new Gson().toJson(response.body()));
                                
                                if(jsonObject.has("update_required")){
                                    if(response.body().getUpdate_required()){
//                                        Toast.makeText(TaskDashboardActivity.this, "Please Update App", Toast.LENGTH_SHORT).show();
                                        showVersionPopup();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(TaskDashboardActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(TaskDashboardActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<CheckUpdateResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(TaskDashboardActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
        
    }


    private void showVersionPopup() {

        View view = getLayoutInflater().inflate(R.layout.custom_message_yes_no_dialog, null);
        TextView messageTV =(TextView)view.findViewById(R.id.message_tv);
        Button yesButton =(Button)view.findViewById(R.id.yes_button);
        Button noButton =(Button)view.findViewById(R.id.no_button);
        messageTV.setText("A new Aadhaar India is now available.Please update the app");
        yesButton.setText(getResources().getString(R.string.action_ok));
        noButton.setText(getResources().getString(R.string.hint_no));
        noButton.setVisibility(GONE);

        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDashboardActivity.this);
        builder.setCancelable(false)
                .setTitle("Aadhaar India Update ?")
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

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

    }


    public static TaskDashboardActivity getInstance() {
        return instance;
    }



    // Listeners method
    void setListeners() {
        addButton.setOnClickListener(this);
        newTaskTv.setOnClickListener(this);
        newGroupTaskTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        // Toast shown on sliding drawer items click

        switch (v.getId()){
            case R.id.add_title_button:

                if(slidingDrawer.isOpened()){
                    slidingDrawer.startAnimation(animationDown);
                    slidingDrawer.close();
                    fab.setVisibility(VISIBLE);
                }
                break;

            case R.id.new_task_tv:


                if(slidingDrawer.isOpened()){
                    slidingDrawer.startAnimation(animationDown);
                    slidingDrawer.close();
                    fab.setVisibility(VISIBLE);
                }
                startActivity(new Intent(TaskDashboardActivity.this,NewTaskActivity.class));

                break;

            case R.id.new_group_task_tv:

                if(slidingDrawer.isOpened()){
                    slidingDrawer.startAnimation(animationDown);
                    slidingDrawer.close();
                    fab.setVisibility(VISIBLE);
                }
                startActivity(new Intent(TaskDashboardActivity.this,NewGroupTaskActivity.class));
                break;
        }
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AllTasksFragment(), "All");
        adapter.addFrag(new CollectionsFragment(), "Collection");
        adapter.addFrag(new PostApprovalDocumentsFragment(), "Signing");
        adapter.addFrag(new HouseVisitFragment(), "House Visit");
        adapter.addFrag(new GroupCreationFragment(), "Group Creation");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.task_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(TaskDashboardActivity.this,MyProfileActivity.class));

        }  else if (id == R.id.nav_send) {

            if (NetworkUtils.haveNetworkConnection(TaskDashboardActivity.this)) {


                doLogoutAndNavigateToLoginScreen();
            } else {
                UiUtils.showAlertDialogWithOKButton(TaskDashboardActivity.this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        finish();
                    }
                });
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void doLogoutAndNavigateToLoginScreen() {
        showProgressDialog(TaskDashboardActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<Logout> call = apiService.doLogout();
            call.enqueue(new Callback<Logout>() {
                @Override
                public void onResponse(Call<Logout> call, Response<Logout> response) {
                    SharedPreferenceUtils.clearUserLoggedIn(TaskDashboardActivity.this);
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            UiUtils.showAlertDialogWithOKButton(TaskDashboardActivity.this, response.body().getNotice().toString(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    stopLocationService();
                                    SharedPreferenceUtils.clearUserLoggedIn(TaskDashboardActivity.this);
                                    Intent intent = new Intent(TaskDashboardActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {

                            NetworkUtils.handleErrorsCasesForAPICalls(TaskDashboardActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {

                        NetworkUtils.handleErrorsForAPICalls(TaskDashboardActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<Logout> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(TaskDashboardActivity.this, null, null);
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


    private void stopLocationService() {
        stopService(new Intent(this, TrackerService.class));
    }

}
