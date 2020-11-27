package com.tailwebs.aadharindia.member.expenditure;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mukesh.OtpView;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.center.CenterConfirmationActivity;
import com.tailwebs.aadharindia.member.AddNewMemberScanActivity;
import com.tailwebs.aadharindia.member.GroupMemberListingActivity;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.models.LoanTakerCalculatedEMIModel;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.UiUtils;
import com.tailwebs.aadharindia.utils.custom.HFRecyclerView;

import java.util.ArrayList;

public class OutstandingLoanListingRecyclerAdapter extends HFRecyclerView<OutstandingLoanModel> {

    String placeName;

    TextInputLayout bankNameLayout;
    TextInputEditText bankET;

    TextInputLayout amountLayout;
    TextInputEditText amountET;

    boolean isValidAmount=false,isValidBank=false;
    public OutstandingLoanListingRecyclerAdapter(String placeName) {
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
        return new ItemViewHolder(inflater.inflate(R.layout.item_loan_list, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderView(LayoutInflater inflater, ViewGroup parent) {
        return new HeaderViewHolder(inflater.inflate(R.layout.item_loan_listing_header, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getFooterView(LayoutInflater inflater, ViewGroup parent) {
        return new FooterViewHolder(inflater.inflate(R.layout.item_footer, parent, false));
    }
    //endregion

    //region ViewHolder Header and Footer
    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView amountTV,bankTV;
        LinearLayout userLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            amountTV = itemView.findViewById(R.id.amount_tv);
            bankTV = itemView.findViewById(R.id.bank_tv);
            userLayout = itemView.findViewById(R.id.user_layout);

        }

        void bind(final OutstandingLoanModel item) {
            String valueForCustomer=null;

            amountTV.setText(item.getAmount());
            bankTV.setText(item.getName());

//            userLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent intent = new Intent(GroupMemberListingActivity.getInstance(),MemberDetailActivity.class);
//
//                    GlobalValue.groupId = item.getGroup_id();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("page_value","from_group_listing");
//                    intent.putExtras(bundle);
//                    GroupMemberListingActivity.getInstance().startActivity(intent);
//                    Log.d("Aadhar",item.getAadhar_number());
//
//                }
//            });
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View view) {
            super(view);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        LinearLayout createNewLoanLayout;
        HeaderViewHolder(View view) {
            super(view);

            createNewLoanLayout = view.findViewById(R.id.create_new_loan_layout);
            createNewLoanLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    showLoanAddDialog();



                }
            });
        }

    }


    private class InputLayoutTextWatcher implements TextWatcher {

        private View view;
        ArrayList<String> itemPassed = new ArrayList<String>();

        private InputLayoutTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {


            switch (view.getId()) {

                case R.id.input_bank_name:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean bankStatus = UiUtils.checkValidation(OutsideBorrowingActivity.getInstance(), bankET,bankNameLayout,itemPassed);

                    if (bankStatus == false) {

                        isValidBank = false;

                    } else {

                        isValidBank = true;
                        bankNameLayout.setErrorEnabled(false);

                    }
                    break;

                case R.id.input_amount:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean amountStatus = UiUtils.checkValidation(OutsideBorrowingActivity.getInstance(), amountET,amountLayout,itemPassed);

                    if (amountStatus == false) {

                        isValidAmount = false;

                    } else {

                        isValidAmount = true;
                        amountLayout.setErrorEnabled(false);

                    }
                    break;
            }
        }

    }


    private void showLoanAddDialog(){
        // title, custom view, actions dialog


        View view = OutsideBorrowingActivity.getInstance().getLayoutInflater().inflate(R.layout.custom_loan_dialog, null);
        bankNameLayout=(TextInputLayout) view.findViewById(R.id.input_layout_bank_name);
         bankET=(TextInputEditText) view.findViewById(R.id.input_bank_name);

         amountLayout=(TextInputLayout) view.findViewById(R.id.input_layout_amount);
         amountET=(TextInputEditText) view.findViewById(R.id.input_amount);

        bankET.addTextChangedListener(new InputLayoutTextWatcher(bankET));
        amountET.addTextChangedListener(new InputLayoutTextWatcher(amountET));


        AlertDialog.Builder builder = new AlertDialog.Builder(OutsideBorrowingActivity.getInstance());
        builder.setCancelable(false)
                .setPositiveButton("ADD LOAN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(OutsideBorrowingActivity.getInstance(), "yyy", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("", null)
                .setView(view);


        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialog));

    }


    private class CustomListener implements View.OnClickListener {
        private final Dialog dialog;
        public CustomListener(AlertDialog alertDialog) {
            this.dialog = alertDialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here

            if((isValidAmount)&&(isValidBank)) {

                OutsideBorrowingActivity.getInstance().callAPIForSubmittingOutsideBorrowing(bankET.getText().toString(), amountET.getText().toString());
                dialog.dismiss();
            }else{
                UiUtils.checkValidation(OutsideBorrowingActivity.getInstance(), bankET, bankNameLayout, new ArrayList<String>());
                UiUtils.checkValidation(OutsideBorrowingActivity.getInstance(), amountET, amountLayout, new ArrayList<String>());
            }
        }
    }




    //endregion

}
