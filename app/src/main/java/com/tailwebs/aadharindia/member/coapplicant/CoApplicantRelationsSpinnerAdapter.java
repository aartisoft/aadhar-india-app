package com.tailwebs.aadharindia.member.coapplicant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.models.common.CACDLoanTakerRelationsModel;
import com.tailwebs.aadharindia.models.common.CCoARelationModel;

import java.util.ArrayList;
import java.util.List;

public class CoApplicantRelationsSpinnerAdapter extends ArrayAdapter<CCoARelationModel> {

    private Context mContext;
    int layoutResourceId;
    private List<CCoARelationModel> relationModels = new ArrayList<>();

    public CoApplicantRelationsSpinnerAdapter(@NonNull Context context, int layoutResourceId, ArrayList<CCoARelationModel> list) {
        super(context, layoutResourceId , list);
        mContext = context;
        this.layoutResourceId = layoutResourceId;
        relationModels = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(layoutResourceId,parent,false);

        CCoARelationModel cCoARelationModel = relationModels.get(position);
        TextView name = (TextView) listItem.findViewById(R.id.item_spinner);
        name.setText(cCoARelationModel.getName());

        return listItem;
    }
}
