package com.tailwebs.aadharindia.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.home.dashboard.TaskDashboardActivity;
import com.tailwebs.aadharindia.home.models.HouseVisitTaskModel;
import com.tailwebs.aadharindia.home.models.PostApprovalDocumentsTaskModel;
import com.tailwebs.aadharindia.home.tasks.housevisit.HouseVisitTaskDetailsActivity;
import com.tailwebs.aadharindia.postapproval.PostApprovalTaskDetailsActivity;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.custom.HFRecyclerView;

public class PostApprovalDocumentListingRecyclerAdapter extends HFRecyclerView<PostApprovalDocumentsTaskModel> {

    String placeName;

    public PostApprovalDocumentListingRecyclerAdapter() {
        // With Header & With Footer

        super(true, true);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.bind(getItem(position));

        } else if (holder instanceof HeaderViewHolder) {

        } else if (holder instanceof FooterViewHolder) {

        }
    }

    //region Override Get ViewHolder
    @Override
    protected RecyclerView.ViewHolder getItemView(LayoutInflater inflater, ViewGroup parent) {
        return new ItemViewHolder(inflater.inflate(R.layout.item_task_detail, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderView(LayoutInflater inflater, ViewGroup parent) {
        return new HeaderViewHolder(inflater.inflate(R.layout.item_header, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getFooterView(LayoutInflater inflater, ViewGroup parent) {
        return new FooterViewHolder(inflater.inflate(R.layout.item_footer, parent, false));
    }
    //endregion

    //region ViewHolder Header and Footer
    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv,distanceTv,taskTv,statusTv,byTv,dateTv;
        LinearLayout userLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name_tv);
            distanceTv = itemView.findViewById(R.id.distance_tv);
            taskTv = itemView.findViewById(R.id.task_tv);
            statusTv = itemView.findViewById(R.id.status_tv);
            byTv = itemView.findViewById(R.id.by_tv);
            dateTv = itemView.findViewById(R.id.date_tv);
            userLayout = itemView.findViewById(R.id.user_layout);

        }

        void bind(final PostApprovalDocumentsTaskModel item) {

            nameTv.setText(item.getTask_type_name());
            if(item.getName().length()>0){
                taskTv.setText(item.getCity().getLong_name()+" - "+item.getName());
            }else{
                taskTv.setText(item.getCity().getLong_name());
            }

            if(item.getState().equalsIgnoreCase("New")){
                statusTv.setText("Pending");
                statusTv.setTextColor(TaskDashboardActivity.getInstance().getResources().getColor(R.color.errorColor));
            }else{
                statusTv.setVisibility(View.GONE);
            }

            dateTv.setText(item.getCreated_on());
            userLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    GlobalValue.selectedCenter = false;
                    GlobalValue.chooseCenterFromList=false;
                    GlobalValue.isGroupPresent = false;
                    GlobalValue.placeId = item.getCity().getGoogle_place_id();
                    GlobalValue.placeAddress = item.getCity().getLong_name();
                    Intent intent = new Intent(TaskDashboardActivity.getInstance(),PostApprovalTaskDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    GlobalValue.taskId = item.getId();
                    bundle.putString("task_id",item.getId());
                    intent.putExtras(bundle);
                    TaskDashboardActivity.getInstance().startActivity(intent);





                }
            });
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View view) {
            super(view);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderViewHolder(View view) {
            super(view);

        }

    }
    //endregion

}
