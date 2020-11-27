package com.tailwebs.aadharindia.member;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.models.common.CACDLoanTakerRelationsModel;

import java.util.ArrayList;
import java.util.List;

public class RelationsSpinnerAdapter extends ArrayAdapter<CACDLoanTakerRelationsModel> {

    private Context mContext;
    int layoutResourceId;
    private List<CACDLoanTakerRelationsModel> cacdLoanTakerRelationsModels = new ArrayList<>();

    public RelationsSpinnerAdapter(@NonNull Context context,int layoutResourceId, ArrayList<CACDLoanTakerRelationsModel> list) {
        super(context, layoutResourceId , list);
        mContext = context;
        this.layoutResourceId = layoutResourceId;
        cacdLoanTakerRelationsModels = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(layoutResourceId,parent,false);

        CACDLoanTakerRelationsModel cacdLoanTakerRelationsModel = cacdLoanTakerRelationsModels.get(position);
        TextView name = (TextView) listItem.findViewById(R.id.item_spinner);
        name.setText(cacdLoanTakerRelationsModel.getName());

        return listItem;
    }
}
