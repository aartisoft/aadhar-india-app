package com.tailwebs.aadharindia.member.cashincome;

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
import com.tailwebs.aadharindia.member.cashincome.models.FamilyModel;
import com.tailwebs.aadharindia.member.models.LoanTakerCalculatedEMIModel;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.custom.HFRecyclerView;

public class FamilyMemberListingRecyclerAdapter extends HFRecyclerView<FamilyModel> {

    String placeName;

    public FamilyMemberListingRecyclerAdapter(String placeName) {
        // With Header & With Footer

        super(true, true);
        this.placeName= placeName;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.bind(getItem(position),position);

        } else if (holder instanceof HeaderViewHolder) {

        } else if (holder instanceof FooterViewHolder) {

        }
    }

    //region Override Get ViewHolder
    @Override
    protected RecyclerView.ViewHolder getItemView(LayoutInflater inflater, ViewGroup parent) {
        return new ItemViewHolder(inflater.inflate(R.layout.item_family_member_list, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderView(LayoutInflater inflater, ViewGroup parent) {
        return new HeaderViewHolder(inflater.inflate(R.layout.item_family_listing_header, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getFooterView(LayoutInflater inflater, ViewGroup parent) {
        return new FooterViewHolder(inflater.inflate(R.layout.item_footer, parent, false));
    }
    //endregion

    //region ViewHolder Header and Footer
    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView userIdTV,statusTV;
        LinearLayout userLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            userIdTV = itemView.findViewById(R.id.user_name_tv);
            statusTV = itemView.findViewById(R.id.statustv);
            userLayout = itemView.findViewById(R.id.user_layout);

        }

        void bind(final FamilyModel item, final int position) {
            String valueForCustomer=null;

            userIdTV.setText(item.getName());

            if(item.getEducation_level_id()!=0){
                statusTV.setText(item.getRelationModel().getName()+" . "+item.getAge()+" . "+item.getHealthStatusModel().getName()
                        +" . "+item.getEducationLevelsStatusModel().getName());
            }else {
                statusTV.setText(item.getRelationModel().getName() + " . " + item.getAge() + " . " + item.getHealthStatusModel().getName()
                        + " . " + item.getEducationStatusModel().getName());
            }

            userLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(FamilyMembersListingActivity.getInstance(),EditMemberInformationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("family_member_id",item.getId());
                    bundle.putInt("position",position);
                    intent.putExtras(bundle);
                    FamilyMembersListingActivity.getInstance().startActivity(intent);


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

        LinearLayout createNewMemberLayout;
        TextView member_text;
        HeaderViewHolder(View view) {
            super(view);

            createNewMemberLayout = view.findViewById(R.id.create_new_member_layout);
            member_text = view.findViewById(R.id.member_text);
            member_text.setText("Add Family Member");
            createNewMemberLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(FamilyMembersListingActivity.getInstance(),MemberInformationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", FamilyMembersListingActivity.getInstance().getMembersCount());
                    intent.putExtras(bundle);
                    FamilyMembersListingActivity.getInstance().startActivity(intent);

                }
            });
        }

    }
    //endregion

}
