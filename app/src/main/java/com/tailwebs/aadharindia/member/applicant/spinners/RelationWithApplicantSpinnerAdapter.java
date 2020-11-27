package com.tailwebs.aadharindia.member.applicant.spinners;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.models.common.CACDLoanTakerRelationsModel;
import com.tailwebs.aadharindia.models.common.CCoARelationModel;

public class RelationWithApplicantSpinnerAdapter extends ArrayAdapter<CCoARelationModel> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private CCoARelationModel[] values;
    LayoutInflater inflator;

    public RelationWithApplicantSpinnerAdapter(Context context, int textViewResourceId,
                                               CCoARelationModel[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount(){
        return values.length;
    }

    @Override
    public CCoARelationModel getItem(int position){
        return values[position];
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        convertView = inflator.inflate(R.layout.ms__list_item, null);
        TextView tv = (TextView) convertView.findViewById(R.id.tv_tinted_spinner);
        tv.setText(values[position].getName());
        if(position ==0){
            tv.setTextColor(Color.GRAY);
        }else{
            tv.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {


        convertView = inflator.inflate(R.layout.ms__list_item, null);
        TextView tv = (TextView) convertView.findViewById(R.id.tv_tinted_spinner);
        if(position ==0){
            tv.setTextColor(Color.GRAY);
        }else{
            tv.setTextColor(Color.BLACK);
        }
        tv.setText(values[position].getName());
        tv.setPadding(24,0,0,0);
        return convertView;
    }
}
