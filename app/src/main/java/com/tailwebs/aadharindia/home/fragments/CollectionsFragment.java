package com.tailwebs.aadharindia.home.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.home.CollectionListingRecyclerAdapter;
import com.tailwebs.aadharindia.home.dashboard.AllTaskResponseModel;
import com.tailwebs.aadharindia.home.dashboard.TaskListingRecyclerAdapter;
import com.tailwebs.aadharindia.home.models.CollectionTaskModel;
import com.tailwebs.aadharindia.home.models.TaskModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionsFragment extends Fragment {


    ArrayList<TaskModel> allTasksModelArrayList=null;
    ArrayList<CollectionTaskModel> collectionTaskModelArrayList;


    @BindView(R.id.task_listing_recycler_view)
    RecyclerView taskListingRecyclerView;


    private ProgressDialog mProgressDialog;
    private FirebaseAnalytics mFirebaseAnalytics;


    public CollectionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_collections, container, false);
        ButterKnife.bind(this,view);
        collectionTaskModelArrayList = new ArrayList<CollectionTaskModel>();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Dashboard - Collections" , null);


        if (NetworkUtils.haveNetworkConnection(getActivity())) {
            showProgressDialog(getActivity());
            getCollectionTasks();
        } else {
            UiUtils.showAlertDialogWithOKButton(getActivity(), getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

//        allTasksModelArrayList =GlobalValue.allTasksModelArrayList;
//
//        if(allTasksModelArrayList.size()>0){
//
//            for (int i=0;i<allTasksModelArrayList.size();i++){
//
//                if(allTasksModelArrayList.get(i).getTask_type().getCode().equalsIgnoreCase("collection")){
//
//                    CollectionTaskModel collectionTaskModel = new CollectionTaskModel();
//                    collectionTaskModel.setId(allTasksModelArrayList.get(i).getId());
//                    collectionTaskModel.setName(allTasksModelArrayList.get(i).getName());
//                    collectionTaskModel.setCity(allTasksModelArrayList.get(i).getCity().getLong_name());
//                    collectionTaskModel.setGoogle_place_id(  allTasksModelArrayList.get(i).getCity().getGoogle_place_id());
//                    collectionTaskModel.setState(allTasksModelArrayList.get(i).getState());
//                    collectionTaskModel.setCreated_on(allTasksModelArrayList.get(i).getCreated_at_in_format());
//                    collectionTaskModel.setScheduled_on(allTasksModelArrayList.get(i).getScheduled_on());
//                    collectionTaskModel.setTask_number(allTasksModelArrayList.get(i).getTask_number());
//
//                    collectionTaskModelArrayList.add(collectionTaskModel);
//
//                }
//            }
//            loadListView(collectionTaskModelArrayList);
//        }


        return view;
    }

    private void getCollectionTasks() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<AllTaskResponseModel> call = apiService.getAllCollections();
            call.enqueue(new Callback<AllTaskResponseModel>() {
                @Override
                public void onResponse(Call<AllTaskResponseModel> call, final Response<AllTaskResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            JSONObject jsonObject= null;
                            try {
                                jsonObject = new JSONObject(new Gson().toJson(response.body()));

                                if(jsonObject.has("tasks")) {


                                    allTasksModelArrayList = response.body().getTaskModel();

                                    GlobalValue. allTasksModelArrayList = allTasksModelArrayList;


                                    setValuesInAllTasks(response.body().getTaskModel());
                                }else{
//                                    Toast.makeText(getActivity(), "Nothing to display", Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        } else {
                            NetworkUtils.handleErrorsForAPICalls(getActivity(), response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        if(response.body() !=null) {
                            NetworkUtils.handleErrorsCasesForAPICalls(getActivity(), response.code(), response.body().getErrors());
                        }else{
                            NetworkUtils.handleErrorsCasesForAPICalls(getActivity(), response.code(), null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<AllTaskResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
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


    private void loadListView(ArrayList<CollectionTaskModel> collectionTaskModelArrayList) {
        CollectionListingRecyclerAdapter adapter = new CollectionListingRecyclerAdapter();
        adapter.setData(collectionTaskModelArrayList, false);
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

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
