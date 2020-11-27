package com.tailwebs.aadharindia.center;

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

import com.tailwebs.aadharindia.aadharscan.AadharScanActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.center.searchinmap.models.CenterModel;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.custom.HFRecyclerView;

public class CenterListingRecyclerAdapter extends HFRecyclerView<CenterModel> {

    String placeName;

    public CenterListingRecyclerAdapter(String placeName) {
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
        return new ItemViewHolder(inflater.inflate(R.layout.item_center_list, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderView(LayoutInflater inflater, ViewGroup parent) {
        return new HeaderViewHolder(inflater.inflate(R.layout.item_header, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getFooterView(LayoutInflater inflater, ViewGroup parent) {
        return new FooterViewHolder(inflater.inflate(R.layout.item_center_listing_footer, parent, false));
    }
    //endregion

    //region ViewHolder Header and Footer
    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView userIdTV,userNameTV,userAddressTV;
        LinearLayout userLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            userIdTV = itemView.findViewById(R.id.user_id_tv);
            userNameTV = itemView.findViewById(R.id.user_name_tv);
            userAddressTV = itemView.findViewById(R.id.user_address_tv);
            userLayout = itemView.findViewById(R.id.user_layout);






        }

        void bind(final CenterModel item) {
            userIdTV.setText(item.getCenter_id());
            userNameTV.setText(item.getContactPersonModel().getName());
            userAddressTV.setText(item.getCenterAddressModel().getFull_address());

            userLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(CenterListingActivity.getInstance(),CenterShowActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id",item.getId());
                    bundle.putString("center_id",item.getCenter_id());
                    bundle.putString("username", item.getContactPersonModel().getName());
                    bundle.putString("user_co_name", item.getContactPersonModel().getAadhar_co());
                    bundle.putString("phone", item.getContactPersonModel().getPhone_number());
                    bundle.putString("aadhar_no", item.getContactPersonModel().getAadhar_number());
                    bundle.putString("dob", item.getContactPersonModel().getDob());
                    bundle.putString("gender", item.getContactPersonModel().getGender());
                    bundle.putString("center_address", item.getCenterAddressModel().getFull_address());
                    bundle.putString("profile_image", item.getContactPersonModel().getProfileImages().getMedium());
                    GlobalValue.ciTyCenterenterImages = item.getCityCenterImages();
                    intent.putExtras(bundle);
                    CenterListingActivity.getInstance().startActivity(intent);


                    Log.d("Aadhar",item.getAadhar_number());

                }
            });
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderViewHolder(View view) {
            super(view);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        LinearLayout createNewCenterLayout;
        FooterViewHolder(View view) {
            super(view);

            createNewCenterLayout = view.findViewById(R.id.create_new_center_layout);
            createNewCenterLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(CenterListingActivity.getInstance(),AadharScanActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("place_name", placeName);
                    intent.putExtras(bundle);
                    CenterListingActivity.getInstance().startActivity(intent);

                }
            });
        }

    }
    //endregion

}
