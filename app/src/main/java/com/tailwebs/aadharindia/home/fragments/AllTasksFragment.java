package com.tailwebs.aadharindia.home.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.home.dashboard.AllTaskResponseModel;
import com.tailwebs.aadharindia.home.dashboard.TaskListingRecyclerAdapter;
import com.tailwebs.aadharindia.home.models.TaskModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllTasksFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.task_listing_recycler_view)
    RecyclerView taskListingRecyclerView;

    @BindView(R.id.swipe_container_layout)
    SwipeRefreshLayout swipeContainer;

    private FirebaseAnalytics mFirebaseAnalytics;



    private ProgressDialog mProgressDialog;

    ArrayList<TaskModel> houseVisitTaskModelArrayList = null,groupCreationTaskModelArrayList = null,
            postApprovalTaskModelArrayList=null,allTasksModelArrayList=null;


    public AllTasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_tasks, container, false);
        ButterKnife.bind(this,view);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Dashboard - All Tasks" , null);

        if (NetworkUtils.haveNetworkConnection(getActivity())) {
            showProgressDialog(getActivity());
            getAllTasks();
        } else {
            UiUtils.showAlertDialogWithOKButton(getActivity(), getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

//        swipeContainer.setEnabled(true);
//        swipeContainer.setColorSchemeResources(R.color.colorAccent);
//        swipeContainer.setOnRefreshListener(this);

//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Your code to refresh the list here.
//                // Make sure you call swipeContainer.setRefreshing(false)
//                // once the network request has completed successfully.
//                swipeContainer.setRefreshing(true);
////                if (NetworkUtils.haveNetworkConnection(getActivity())) {
//////                    showProgressDialog(getActivity());
////                    getAllTasks();
////                } else {
////                    UiUtils.showAlertDialogWithOKButton(getActivity(), getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialogInterface, int i) {
////                            dialogInterface.cancel();
////                        }
////                    });
////                }
//            }
//        });


        return  view;
    }



    private void getAllTasks() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);

            Date currentTime = Calendar.getInstance().getTime();
            Log.i("Drools", "" + currentTime);
            Call<AllTaskResponseModel> call = apiService.getAllTasks();
            call.enqueue(new Callback<AllTaskResponseModel>() {
                @Override
                public void onResponse(Call<AllTaskResponseModel> call, final Response<AllTaskResponseModel> response) {
                    hideProgressDialog();
                    swipeContainer.setRefreshing(false);
                    Log.i("Drools", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            Date currentTime = Calendar.getInstance().getTime();
                            Log.i("Drools success", "" + currentTime);
                            //API Success is true
                            JSONObject jsonObject= null;
                            try {
                                jsonObject = new JSONObject(new Gson().toJson(response.body()));

                                if(jsonObject.has("tasks")) {


                                    allTasksModelArrayList = response.body().getTaskModel();

                                    GlobalValue. allTasksModelArrayList = allTasksModelArrayList;


                                    setValuesInAllTasks(response.body().getTaskModel());

                                        // Toast for task is completed
//                                        Toast.makeText(getActivity(), "Items refreshed.",
//                                            Toast.LENGTH_SHORT).show();
                                }else{
//                                    Toast.makeText(getActivity(), "Nothing to display", Toast.LENGTH_SHORT).show();
                                    GlobalValue. allTasksModelArrayList = null;
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        } else {
                            Date currentTime = Calendar.getInstance().getTime();
                            Log.i("Drools error", "" + currentTime);
                            NetworkUtils.handleErrorsForAPICalls(getActivity(), response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        Date currentTime = Calendar.getInstance().getTime();
                        Log.i("Drools erroe2", "" + currentTime);
                        NetworkUtils.handleErrorsCasesForAPICalls(getActivity(), response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<AllTaskResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    Date currentTime = Calendar.getInstance().getTime();
                    Log.i("Drools fail", "" + currentTime);
                    NetworkUtils.handleErrorsForAPICalls(getActivity(), null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesInAllTasks(ArrayList<TaskModel> taskModel) {

        TaskListingRecyclerAdapter adapter = new TaskListingRecyclerAdapter();
        adapter.setData(taskModel, false);
        taskListingRecyclerView.setAdapter(adapter);



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

    @Override
    public void onDestroyView() {
        swipeContainer.removeAllViews();
        super.onDestroyView();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeContainer.setRefreshing(true);
                if (NetworkUtils.haveNetworkConnection(getActivity())) {
//                    showProgressDialog(getActivity());
                    getAllTasks();
                } else {
                    UiUtils.showAlertDialogWithOKButton(getActivity(), getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                }
            }
        }, 5000);
    }
}
