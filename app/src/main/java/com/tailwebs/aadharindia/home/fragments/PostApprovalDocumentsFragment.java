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
import com.tailwebs.aadharindia.home.PostApprovalDocumentListingRecyclerAdapter;
import com.tailwebs.aadharindia.home.models.HouseVisitTaskModel;
import com.tailwebs.aadharindia.home.models.PostApprovalDocumentsTaskModel;
import com.tailwebs.aadharindia.home.models.TaskModel;
import com.tailwebs.aadharindia.utils.GlobalValue;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostApprovalDocumentsFragment extends Fragment {

    ArrayList<TaskModel> allTasksModelArrayList=null;
    ArrayList<PostApprovalDocumentsTaskModel> postApprovalDocumentModelArrayList;



    @BindView(R.id.task_listing_recycler_view)
    RecyclerView taskListingRecyclerView;


    private ProgressDialog mProgressDialog;

    private FirebaseAnalytics mFirebaseAnalytics;


    public PostApprovalDocumentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_approval_documents, container, false);
        ButterKnife.bind(this,view);
        postApprovalDocumentModelArrayList = new ArrayList<PostApprovalDocumentsTaskModel>();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Dashboard - Post Approval" , null);

        allTasksModelArrayList =GlobalValue.allTasksModelArrayList;
        if(allTasksModelArrayList!=null) {
            for (int i = 0; i < allTasksModelArrayList.size(); i++) {

                if (allTasksModelArrayList.get(i).getTask_type().getCode().equalsIgnoreCase("post_approval")) {

                    PostApprovalDocumentsTaskModel postApprovalDocumentsTaskModel = new PostApprovalDocumentsTaskModel();
                    postApprovalDocumentsTaskModel.setId(allTasksModelArrayList.get(i).getId());
                    postApprovalDocumentsTaskModel.setName(allTasksModelArrayList.get(i).getName());
                    postApprovalDocumentsTaskModel.setCity(allTasksModelArrayList.get(i).getCity());
                    postApprovalDocumentsTaskModel.setTask_type_name(allTasksModelArrayList.get(i).getTask_type().getName());
                    postApprovalDocumentsTaskModel.setState(allTasksModelArrayList.get(i).getState());
                    postApprovalDocumentsTaskModel.setCreated_on(allTasksModelArrayList.get(i).getCreated_at_in_format());
                    postApprovalDocumentsTaskModel.setScheduled_on(allTasksModelArrayList.get(i).getScheduled_on());
                    postApprovalDocumentsTaskModel.setTask_number(allTasksModelArrayList.get(i).getTask_number());
                    postApprovalDocumentModelArrayList.add(postApprovalDocumentsTaskModel);

                }
            }
            loadListView(postApprovalDocumentModelArrayList);
        }
        return view;
    }

    private void loadListView(ArrayList<PostApprovalDocumentsTaskModel> postApprovalDocumentModelArrayList) {
        PostApprovalDocumentListingRecyclerAdapter adapter = new PostApprovalDocumentListingRecyclerAdapter();
        adapter.setData(postApprovalDocumentModelArrayList, false);
        taskListingRecyclerView.setAdapter(adapter);
    }

}
