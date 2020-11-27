package com.tailwebs.aadharindia.housevisit;

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
import com.tailwebs.aadharindia.member.AddNewMemberScanActivity;
import com.tailwebs.aadharindia.member.GroupMemberListingActivity;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.models.LoanTakerCalculatedEMIModel;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.custom.HFRecyclerView;

public class HouseVisitListingRecyclerAdapter extends HFRecyclerView<LoanTakerCalculatedEMIModel> {

    String placeName;

    public HouseVisitListingRecyclerAdapter(String placeName) {
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
        TextView userIdTV,userNameTV,userCoTV,formStatusTV,incorrectDocCountTv;
        LinearLayout userLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            userIdTV = itemView.findViewById(R.id.user_id_tv);
            userNameTV = itemView.findViewById(R.id.user_name_tv);
            userCoTV = itemView.findViewById(R.id.user_co_tv);
            userLayout = itemView.findViewById(R.id.user_layout);
            formStatusTV = itemView.findViewById(R.id.form_status_tv);
            incorrectDocCountTv=itemView.findViewById(R.id.incorrect_doc_count_tv);

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
            formStatusTV.setText(item.getState());
            if(item.getState().equalsIgnoreCase("New")){
                formStatusTV.setText("Form Incomplete");
                formStatusTV.setTextColor(HouseVisitMemberListingActivity.getInstance().getResources().getColor(R.color.errorColor));
            }else{
                formStatusTV.setVisibility(View.GONE);
            }

           if(item.getIncorrect_document_count()>0){
                incorrectDocCountTv.setVisibility(View.VISIBLE);
                incorrectDocCountTv.setText("Incorrect Docs "+item.getIncorrect_document_count());
               incorrectDocCountTv.setTextColor(HouseVisitMemberListingActivity.getInstance().getResources().getColor(R.color.errorColor));


           }

            userLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(HouseVisitMemberListingActivity.getInstance(),HouseDetailsActivity.class);

                    GlobalValue.groupId = item.getGroup_id();
                    GlobalValue.loanTakerId = item.getId();
                    Bundle bundle = new Bundle();
//                    bundle.putString("page_value","from_group_listing");
                    bundle.putString("name",item.getName());
                    bundle.putString("status",item.getState());
                    bundle.putString("phone",item.getPrimary_phone_number());
                    intent.putExtras(bundle);
                    HouseVisitMemberListingActivity.getInstance().startActivity(intent);
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
