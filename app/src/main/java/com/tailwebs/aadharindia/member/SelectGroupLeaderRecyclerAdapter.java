package com.tailwebs.aadharindia.member;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.models.LoanTakerCalculatedEMIModel;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.custom.HFRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SelectGroupLeaderRecyclerAdapter extends HFRecyclerView<LoanTakerCalculatedEMIModel> {


    List<LoanTakerCalculatedEMIModel> loanTakerCalculatedEMIModelArrayList,loanTakerCalculatedEMIModelArrayListFiltered;

    private int lastSelectedPosition = -1;
    private SelectGroupLeaderAdapterListener listener;
    public SelectGroupLeaderRecyclerAdapter(ArrayList<LoanTakerCalculatedEMIModel> loanTakerCalculatedEMIModelArrayList,SelectGroupLeaderAdapterListener listener) {
        // With Header & With Footer

        super(true, true);
        this.loanTakerCalculatedEMIModelArrayList= loanTakerCalculatedEMIModelArrayList;
        this.listener = listener;
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
        return new ItemViewHolder(inflater.inflate(R.layout.item_group_member_select_leader, parent, false));
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
        TextView userIdTV,userNameTV,userCoTV;


        RadioButton chooseLeaderRB;

        ItemViewHolder(View itemView) {
            super(itemView);
            userIdTV = itemView.findViewById(R.id.user_id_tv);
            userNameTV = itemView.findViewById(R.id.user_name_tv);
            userCoTV = itemView.findViewById(R.id.user_co_tv);
            chooseLeaderRB = itemView.findViewById(R.id.choose_leader);


        }

        void bind(final LoanTakerCalculatedEMIModel item, final int position) {

            String valueForCustomer=null;

            if(item.getIs_fresh_customer()){
                valueForCustomer = "Fresh";

            }else{
                valueForCustomer = "Repeat";
            }

            userIdTV.setText(item.getLoan_taker_id()+"/"+valueForCustomer+"/"+item.getLoan_cycle());
            userNameTV.setText(item.getName());
            userCoTV.setText(item.getAadhar_co());

            Log.d("aadhar onResponse json", "" + lastSelectedPosition);
            Log.d("aadhar onRes position", "" + position);

            chooseLeaderRB.setChecked(lastSelectedPosition == position-1);

            chooseLeaderRB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition()-1;
                    notifyDataSetChanged();

                    Log.d("aadhar onResponse json", "" + loanTakerCalculatedEMIModelArrayList.get(lastSelectedPosition).getName());

                    listener.onLeaderSelected(loanTakerCalculatedEMIModelArrayList.get(lastSelectedPosition));

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

    public interface SelectGroupLeaderAdapterListener {
        void onLeaderSelected(LoanTakerCalculatedEMIModel loanTakerCalculatedEMIModel);
    }
    //endregion

}
