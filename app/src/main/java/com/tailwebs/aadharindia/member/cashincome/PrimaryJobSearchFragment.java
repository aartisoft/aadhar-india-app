package com.tailwebs.aadharindia.member.cashincome;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.applicant.MyLanguageDividerItemDecoration;
import com.tailwebs.aadharindia.member.cashincome.models.PrimaryJobModel;
import com.tailwebs.aadharindia.member.cashincome.models.PrimaryJobResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrimaryJobSearchFragment extends Fragment {

    @BindView(R.id.job_recycler_view)
    RecyclerView jobRecyclerView;


    private ProgressDialog mProgressDialog;
    private List<PrimaryJobModel> jobModelList;
    private PrimaryJobAdapter primaryJobAdapter;



    public PrimaryJobSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_primary_job_search, container, false);
        ButterKnife.bind(this,view);

        if (NetworkUtils.haveNetworkConnection(getActivity())) {
            showProgressDialog(getActivity());
            Bundle bundle = this.getArguments();
            searchPrimaryJobs(bundle.getString("string"));
        } else {
            UiUtils.showAlertDialogWithOKButton(getActivity(), getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

        return view;
    }

    private void searchPrimaryJobs(String code) {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<PrimaryJobResponseModel> call = apiService.searchPrimaryJobs(code);
            call.enqueue(new Callback<PrimaryJobResponseModel>() {
                @Override
                public void onResponse(Call<PrimaryJobResponseModel> call, final Response<PrimaryJobResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + new Gson().toJson(response.body()));
                    try {
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        if(jsonObject.has("primary_jobs")){
                            jobModelList = response.body().getPrimaryJobModelArrayList();
                            primaryJobAdapter = new PrimaryJobAdapter(getActivity(), jobModelList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            jobRecyclerView.setLayoutManager(mLayoutManager);
                            jobRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            jobRecyclerView.addItemDecoration(new MyLanguageDividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL, 16));
                            jobRecyclerView.setAdapter(primaryJobAdapter);
                            primaryJobAdapter.setOnItemClickListner(new PrimaryJobAdapter.onItemClickListner() {
                                @Override
                                public void onClick(PrimaryJobModel primaryJobModel) {

                                    if(primaryJobModel.getIs_leaf()){
                                        SelectPrimaryJobActivity.getInstance().setContinueButtonClicked(primaryJobModel.getId(),primaryJobModel.getName());
                                    }else{

                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                        // Create FragmentOne instance.
                                        PrimaryJobFragment fragmentOne = new PrimaryJobFragment();

                                        Bundle bundle = new Bundle();
                                        bundle.putString("code", primaryJobModel.getCode());
                                        fragmentOne.setArguments(bundle);

                                        // Add fragment one with tag name.
                                        fragmentTransaction.add(R.id.fragment_back_stack_frame_layout, fragmentOne, "Fragment"+primaryJobModel.getCode());
                                        fragmentTransaction.addToBackStack("Fragment"+primaryJobModel.getCode());
                                        fragmentTransaction.commit();

                                        FragmentUtil.printActivityFragmentList(fragmentManager);

                                    }
                                }


                            });
                            
                        }else{
                            Toast.makeText(getActivity(), "No job available", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                   

                   


                }

                @Override
                public void onFailure(Call <PrimaryJobResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(getActivity(), null, null);
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
