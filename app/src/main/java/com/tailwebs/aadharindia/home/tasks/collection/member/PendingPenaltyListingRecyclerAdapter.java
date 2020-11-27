package com.tailwebs.aadharindia.home.tasks.collection.member;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.home.models.PendingPenaltyModel;
import com.tailwebs.aadharindia.member.models.LoanTakerCalculatedEMIModel;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.custom.HFRecyclerView;

public class PendingPenaltyListingRecyclerAdapter extends HFRecyclerView<PendingPenaltyModel> {


    boolean isTotalPenaltyPresent;

    public PendingPenaltyListingRecyclerAdapter() {
        // With Header & With Footer

        super(true, true);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.bind(getItem(position));
            isTotalPenaltyPresent =getPenaltyPresent();

        } else if (holder instanceof HeaderViewHolder) {

        } else if (holder instanceof FooterViewHolder) {

        }
    }

    //region Override Get ViewHolder
    @Override
    protected RecyclerView.ViewHolder getItemView(LayoutInflater inflater, ViewGroup parent) {
        return new ItemViewHolder(inflater.inflate(R.layout.item_pending_penalty_list, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderView(LayoutInflater inflater, ViewGroup parent) {
        return new HeaderViewHolder(inflater.inflate(R.layout.item_header, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getFooterView(LayoutInflater inflater, ViewGroup parent) {
        return new FooterViewHolder(inflater.inflate(R.layout.item_pending_penalty_footer, parent, false));
    }
    //endregion

    //region ViewHolder Header and Footer
    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView headingTv,amountTv;
        LinearLayout userLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            headingTv = itemView.findViewById(R.id.heading_tv);
            amountTv = itemView.findViewById(R.id.amount_tv);


        }

        void bind(final PendingPenaltyModel item) {
            String valueForCustomer=null;

            headingTv.setText(item.getHeading());
            amountTv.setText(item.getAmount_in_format());



        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView offerDiscount;
        FooterViewHolder(View view) {
            super(view);
            offerDiscount = view.findViewById(R.id.offer_discount_tv);
            if(isTotalPenaltyPresent){
                offerDiscount.setVisibility(View.VISIBLE);
            }else{
                offerDiscount.setVisibility(View.GONE);
            }
            offerDiscount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MemberOverviewActivity.getInstance(),PenaltyDiscountActivity.class);
                    MemberOverviewActivity.getInstance().startActivity(intent);

                }
            });
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {




        HeaderViewHolder(View view) {
            super(view);




        }

    }
    //endregion

}
