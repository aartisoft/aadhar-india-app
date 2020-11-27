package com.tailwebs.aadharindia.member;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.models.common.CACDLoanReasonsModel;
import com.tailwebs.aadharindia.models.common.CACDLoanTakerRelationsModel;

import java.util.ArrayList;
import java.util.List;

public class LoanReasonSpinnerAdapter extends ArrayAdapter<CACDLoanReasonsModel> {

    private Context mContext;
    int layoutResourceId;
    private List<CACDLoanReasonsModel> cacdLoanReasonsModels = new ArrayList<>();

    public LoanReasonSpinnerAdapter(@NonNull Context context, int layoutResourceId, ArrayList<CACDLoanReasonsModel> list) {
        super(context, layoutResourceId , list);
        mContext = context;
        this.layoutResourceId = layoutResourceId;
        cacdLoanReasonsModels = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(layoutResourceId,parent,false);

        CACDLoanReasonsModel cacdLoanReasonsModel = cacdLoanReasonsModels.get(position);
        TextView name = (TextView) listItem.findViewById(R.id.item_spinner);
        name.setText(cacdLoanReasonsModel.getTitle());

        return listItem;
    }
}
