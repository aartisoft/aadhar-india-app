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
import com.tailwebs.aadharindia.home.GroupCreationListingRecyclerAdapter;
import com.tailwebs.aadharindia.home.models.creategroup.CreateGroupTaskModel;
import com.tailwebs.aadharindia.home.models.TaskModel;
import com.tailwebs.aadharindia.utils.GlobalValue;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupCreationFragment extends Fragment {

    ArrayList<TaskModel>  allTasksModelArrayList=null;
    ArrayList<CreateGroupTaskModel> groupCreationTaskModelArrayList;


    @BindView(R.id.task_listing_recycler_view)
    RecyclerView taskListingRecyclerView;

    private FirebaseAnalytics mFirebaseAnalytics;

    private ProgressDialog mProgressDialog;


    public GroupCreationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_group_creation, container, false);
        ButterKnife.bind(this,view);
        groupCreationTaskModelArrayList = new ArrayList<CreateGroupTaskModel>();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Dashboard - Create Group" , null);

        allTasksModelArrayList =GlobalValue.allTasksModelArrayList;

        if(allTasksModelArrayList!=null) {

        for (int i=0;i<allTasksModelArrayList.size();i++){

            if(allTasksModelArrayList.get(i).getTask_type().getCode().equalsIgnoreCase("create_group")){

                CreateGroupTaskModel createGroupTaskModel = new CreateGroupTaskModel();
                createGroupTaskModel.setId(allTasksModelArrayList.get(i).getId());
                createGroupTaskModel.setName(allTasksModelArrayList.get(i).getName());
                createGroupTaskModel.setTask_type_name(allTasksModelArrayList.get(i).getTask_type().getName());
                createGroupTaskModel.setCity(allTasksModelArrayList.get(i).getCity());
                createGroupTaskModel.setGoogle_place_id(  allTasksModelArrayList.get(i).getCity().getGoogle_place_id());
                createGroupTaskModel.setState(allTasksModelArrayList.get(i).getState());
                createGroupTaskModel.setCreated_on(allTasksModelArrayList.get(i).getCreated_at_in_format());
                createGroupTaskModel.setScheduled_on(allTasksModelArrayList.get(i).getScheduled_on());
                createGroupTaskModel.setTask_number(allTasksModelArrayList.get(i).getTask_number());


                groupCreationTaskModelArrayList.add(createGroupTaskModel);

            }
        }
            loadListView(groupCreationTaskModelArrayList);
        }


        return  view;
    }

    private void loadListView(ArrayList<CreateGroupTaskModel> groupCreationTaskModelArrayList) {
        GroupCreationListingRecyclerAdapter adapter = new GroupCreationListingRecyclerAdapter();
        adapter.setData(groupCreationTaskModelArrayList, false);
        taskListingRecyclerView.setAdapter(adapter);
    }

}
