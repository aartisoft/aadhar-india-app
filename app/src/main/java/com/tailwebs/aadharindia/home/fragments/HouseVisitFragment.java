package com.tailwebs.aadharindia.home.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.home.HouseVisitListingRecyclerAdapter;
import com.tailwebs.aadharindia.home.models.HouseVisitTaskModel;
import com.tailwebs.aadharindia.home.models.TaskModel;
import com.tailwebs.aadharindia.utils.GlobalValue;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HouseVisitFragment extends Fragment {

    ArrayList<TaskModel> allTasksModelArrayList=null;
    ArrayList<HouseVisitTaskModel> houseVisitTaskModelArrayList;



    @BindView(R.id.task_listing_recycler_view)
    RecyclerView taskListingRecyclerView;



    private ProgressDialog mProgressDialog;

    private FirebaseAnalytics mFirebaseAnalytics;


    public HouseVisitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_house_visit, container, false);
        ButterKnife.bind(this,view);
        houseVisitTaskModelArrayList = new ArrayList<HouseVisitTaskModel>();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Dashboard - House Visit" , null);

        allTasksModelArrayList =GlobalValue.allTasksModelArrayList;
        if(allTasksModelArrayList!=null) {
            for (int i = 0; i < allTasksModelArrayList.size(); i++) {

                if (allTasksModelArrayList.get(i).getTask_type().getCode().equalsIgnoreCase("house_visit")) {

                    HouseVisitTaskModel houseVisitTaskModel = new HouseVisitTaskModel();
                    houseVisitTaskModel.setId(allTasksModelArrayList.get(i).getId());
                    houseVisitTaskModel.setName(allTasksModelArrayList.get(i).getName());
                    houseVisitTaskModel.setCity(allTasksModelArrayList.get(i).getCity());
                    houseVisitTaskModel.setTask_type_name(allTasksModelArrayList.get(i).getTask_type().getName());
                    houseVisitTaskModel.setState(allTasksModelArrayList.get(i).getState());
                    houseVisitTaskModel.setCreated_on(allTasksModelArrayList.get(i).getCreated_at_in_format());
                    houseVisitTaskModel.setScheduled_on(allTasksModelArrayList.get(i).getScheduled_on());
                    houseVisitTaskModel.setTask_number(allTasksModelArrayList.get(i).getTask_number());
                    houseVisitTaskModelArrayList.add(houseVisitTaskModel);

                }
            }
            loadListView(houseVisitTaskModelArrayList);
        }


        return view;
    }

    private void loadListView(ArrayList<HouseVisitTaskModel> houseVisitTaskModelArrayList) {
        HouseVisitListingRecyclerAdapter adapter = new HouseVisitListingRecyclerAdapter();
        adapter.setData(houseVisitTaskModelArrayList, false);
        taskListingRecyclerView.setAdapter(adapter);
    }

}
