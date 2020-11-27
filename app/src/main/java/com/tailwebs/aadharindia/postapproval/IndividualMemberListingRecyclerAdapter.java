package com.tailwebs.aadharindia.postapproval;

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
import android.widget.Toast;

import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.AddNewMemberScanActivity;
import com.tailwebs.aadharindia.member.GroupMemberListingActivity;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.models.LoanTakerCalculatedEMIModel;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.custom.HFRecyclerView;

public class IndividualMemberListingRecyclerAdapter extends HFRecyclerView<LoanTakerCalculatedEMIModel> {

    String placeName;

    public IndividualMemberListingRecyclerAdapter(String placeName) {
        // With Header & With Footer

        super(true, true);
        this.placeName= placeName;
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
        return new ItemViewHolder(inflater.inflate(R.layout.item_group_member_list, parent, false));
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
        TextView userIdTV,userNameTV,userCoTV,formStatusTV;
        LinearLayout userLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            userIdTV = itemView.findViewById(R.id.user_id_tv);
            userNameTV = itemView.findViewById(R.id.user_name_tv);
            userCoTV = itemView.findViewById(R.id.user_co_tv);
            userLayout = itemView.findViewById(R.id.user_layout);
            formStatusTV = itemView.findViewById(R.id.form_status_tv);

        }

        void bind(final LoanTakerCalculatedEMIModel item) {
            String valueForCustomer=null;

            if(item.getIs_fresh_customer()){
                valueForCustomer = "Fresh";

            }else{
                valueForCustomer = "Repeat";
            }

            userIdTV.setText(item.getLoan_taker_id()+"/"+valueForCustomer+"/"+item.getLoan_cycle());
            userNameTV.setText(item.getName());
            userCoTV.setText(item.getAadhar_co());
            formStatusTV.setText(item.getDisplay_status());
            if(item.getDisplay_status().equalsIgnoreCase("Pending")){
                formStatusTV.setTextColor(IndividualDocumentsActivity.getInstance().getResources().getColor(R.color.errorColor));
            }else if(item.getDisplay_status().equalsIgnoreCase("Completed")){
                formStatusTV.setTextColor(IndividualDocumentsActivity.getInstance().getResources().getColor(R.color.successColor));
            }

            userLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(item.getState().equalsIgnoreCase("Rejected")){
                        Toast.makeText(IndividualDocumentsActivity.getInstance(), "Member Rejected", Toast.LENGTH_SHORT).show();
                    }else {

                        Intent intent = new Intent(IndividualDocumentsActivity.getInstance(), IndividualMemberDetailActivity.class);
                        GlobalValue.groupId = item.getGroup_id();
                        GlobalValue.loanTakerId = item.getId();
                        Bundle bundle = new Bundle();
                        bundle.putString("page_value", "from_group_listing");
                        intent.putExtras(bundle);
                        IndividualDocumentsActivity.getInstance().startActivity(intent);
                        Log.d("Aadhar", item.getAadhar_number());
                    }

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
