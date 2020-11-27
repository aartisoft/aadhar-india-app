package com.tailwebs.aadharindia.home.tasks.housevisit;

import android.content.DialogInterface;
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

import com.google.gson.Gson;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.housevisit.HouseDetailsActivity;
import com.tailwebs.aadharindia.housevisit.HouseVisitMemberListingActivity;
import com.tailwebs.aadharindia.member.AddNewMemberScanActivity;
import com.tailwebs.aadharindia.member.GroupMemberListingActivity;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.models.LoanTakerCalculatedEMIModel;
import com.tailwebs.aadharindia.member.models.LoanTakerCustomerDetailResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;
import com.tailwebs.aadharindia.utils.custom.HFRecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseVisitMemberGroupReviewListingRecyclerAdapter extends HFRecyclerView<LoanTakerCalculatedEMIModel> {



    public HouseVisitMemberGroupReviewListingRecyclerAdapter() {
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

            if(item.getIs_fresh_customer()){
                valueForCustomer = "Fresh";

            }else{
                valueForCustomer = "Repeat";
            }

            userIdTV.setText(item.getLoan_taker_id()+"/"+valueForCustomer+"/"+item.getLoan_cycle());
            userNameTV.setText(item.getName());
            userCoTV.setText(item.getAadhar_co());
            formStatusTV.setText(item.getDisplay_status());
            if(item.getState().equalsIgnoreCase("Approved")){
                formStatusTV.setTextColor(HouseVisitGroupReviewActivity.getInstance().getResources().getColor(R.color.successColor));
            }else{
                formStatusTV.setTextColor(HouseVisitGroupReviewActivity.getInstance().getResources().getColor(R.color.errorColor));
            }

            if(item.getIncorrect_document_count()>0){
                incorrectDocsTv.setText("Incorrect Docs "+item.getIncorrect_document_count());
                incorrectDocsTv.setTextColor(HouseVisitGroupReviewActivity.getInstance().getResources().getColor(R.color.errorColor));
            }else{
                incorrectDocsTv.setVisibility(View.GONE);
            }


            userLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(item.getState().equalsIgnoreCase("Rejected")){
                        Toast.makeText(HouseVisitGroupReviewActivity.getInstance(), "Member Rejected", Toast.LENGTH_SHORT).show();
                    }
                    else if ("Form Incomplete".equalsIgnoreCase(item.getDisplay_status())) {
                        Intent intent = new Intent(HouseVisitGroupReviewActivity.getInstance(),MemberDetailActivity.class);
                        GlobalValue.groupId = item.getGroup_id();
                        GlobalValue.loanTakerId = item.getId();
                        Bundle bundle = new Bundle();
                        bundle.putString("page_value","from_group_listing");
                        intent.putExtras(bundle);
                        HouseVisitGroupReviewActivity.getInstance().startActivity(intent);
                        Log.d("Aadhar",item.getAadhar_number());
                    }
                    else if ("Form Completed".equalsIgnoreCase(item.getDisplay_status())) {
                        if (NetworkUtils.haveNetworkConnection(HouseVisitGroupReviewActivity.getInstance())) {
                            HouseVisitGroupReviewActivity.getInstance().showProgressDialog(HouseVisitGroupReviewActivity.getInstance());
                            pre_approve_loan_taker(item.getId());
                        }
                        else {
                            UiUtils.showAlertDialogWithOKButton(HouseVisitGroupReviewActivity.getInstance(),
                                    HouseVisitGroupReviewActivity.getInstance().getString(R.string.error_no_internet),
                                    new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                        }
                    }
                    else {
                        Intent intent = new Intent(HouseVisitGroupReviewActivity.getInstance(), HouseDetailsActivity.class);
                        GlobalValue.groupId = item.getGroup_id();
                        GlobalValue.loanTakerId = item.getId();
                        Bundle bundle = new Bundle();
//                        bundle.putString("page_value","from_group_listing");
                        bundle.putString("name", item.getName());
                        bundle.putString("status", item.getState());
                        bundle.putString("phone", item.getPrimary_phone_number());
                        intent.putExtras(bundle);
                        HouseVisitGroupReviewActivity.getInstance().startActivity(intent);
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

    private void pre_approve_loan_taker(String loanTakerID) {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerCustomerDetailResponseModel> call = apiService.preApproveLoanTaker(loanTakerID);
            call.enqueue(new Callback<LoanTakerCustomerDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCustomerDetailResponseModel> call, final Response<LoanTakerCustomerDetailResponseModel> response) {
                    HouseVisitGroupReviewActivity.getInstance().hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Log.i("Drools", "" + new Gson().toJson(response.body()));
                            HouseVisitGroupReviewActivity.getInstance().onBackPressed();
                        }
                        else {
                            NetworkUtils.handleErrorsForAPICalls(HouseVisitGroupReviewActivity.getInstance(), response.body().getErrors(), response.body().getNotice());
                        }
                    }
                    else {
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitGroupReviewActivity.getInstance(), response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCustomerDetailResponseModel> call, Throwable t) {
                    HouseVisitGroupReviewActivity.getInstance().hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitGroupReviewActivity.getInstance(), null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

}
