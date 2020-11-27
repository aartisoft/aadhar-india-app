package com.tailwebs.aadharindia.home.tasks.collection.member;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.home.tasks.housevisit.HouseVisitGroupReviewActivity;
import com.tailwebs.aadharindia.housevisit.HouseDetailsActivity;
import com.tailwebs.aadharindia.member.models.LoanTakerCalculatedEMIModel;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.custom.HFRecyclerView;

public class CollectionGroupMembersOverviewListingRecyclerAdapter extends HFRecyclerView<LoanTakerCalculatedEMIModel> {



    public CollectionGroupMembersOverviewListingRecyclerAdapter() {
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
        return new ItemViewHolder(inflater.inflate(R.layout.item_house_visit_member_list, parent, false));
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
        TextView userIdTV,userNameTV,userCoTV,formStatusTV,incorrectDocsTv;
        LinearLayout userLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            userIdTV = itemView.findViewById(R.id.user_id_tv);
            userNameTV = itemView.findViewById(R.id.user_name_tv);
            userCoTV = itemView.findViewById(R.id.user_co_tv);
            userLayout = itemView.findViewById(R.id.user_layout);
            formStatusTV = itemView.findViewById(R.id.form_status_tv);
            incorrectDocsTv = itemView.findViewById(R.id.incorrect_docs_tv);

        }

        void bind(final LoanTakerCalculatedEMIModel item) {
            String valueForCustomer=null;

            incorrectDocsTv.setVisibility(View.GONE);


            userIdTV.setText("Bal : "+item.getCollectionModel().getPending_amount_in_format());
            userNameTV.setText(item.getName());
            userCoTV.setText(item.getAadhar_co());
            formStatusTV.setText(item.getCollectionModel().getDisplay_status());
            if(item.getCollectionModel().getState().equalsIgnoreCase("Overdue")){
                formStatusTV.setTextColor(GroupMembersOverVIewActivity.getInstance().getResources().getColor(R.color.errorColor));
            }else if(item.getCollectionModel().getState().equalsIgnoreCase("Ongoing - Collection")){
                formStatusTV.setTextColor(GroupMembersOverVIewActivity.getInstance().getResources().getColor(R.color.headingPopupMessageColor));
            }else if(item.getCollectionModel().getState().equalsIgnoreCase("PaidBack")){
                formStatusTV.setTextColor(GroupMembersOverVIewActivity.getInstance().getResources().getColor(R.color.successColor));
            }else if(item.getCollectionModel().getState().equalsIgnoreCase("Ongoing - Paidback")){
                formStatusTV.setTextColor(GroupMembersOverVIewActivity.getInstance().getResources().getColor(R.color.successColor));
            }




            userLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(GroupMembersOverVIewActivity.getInstance(),MemberOverviewActivity.class);
                    GlobalValue.groupId = item.getGroup_id();
                    GlobalValue.loanTakerId = item.getId();
                    GlobalValue.collectionId = item.getCollectionModel().getId();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("name",item.getName());
//                    bundle.putString("co_name",item.getAadhar_co());
//                    bundle.putString("city",item.getCity().getLong_name());
//                    bundle.putString("status",item.getDisplay_status());
//                    bundle.putString("emi_remaining",item.getCollectionModel().getEmi_remaining());
//                    intent.putExtras(bundle);
                    GroupMembersOverVIewActivity.getInstance().startActivity(intent);
                    Log.d("Aadhar",item.getAadhar_number());

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
