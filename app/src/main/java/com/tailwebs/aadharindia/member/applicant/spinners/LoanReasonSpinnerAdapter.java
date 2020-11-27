package com.tailwebs.aadharindia.member.applicant.spinners;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.models.common.CACDLoanReasonsModel;
import com.tailwebs.aadharindia.models.common.CACDLoanTakerRelationsModel;

public class LoanReasonSpinnerAdapter extends ArrayAdapter<CACDLoanReasonsModel> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private CACDLoanReasonsModel[] values;
    LayoutInflater inflator;

    public LoanReasonSpinnerAdapter(Context context, int textViewResourceId,
                                    CACDLoanReasonsModel[] values) {
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
    public CACDLoanReasonsModel getItem(int position){
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
        tv.setText(values[position].getTitle());
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
        tv.setText(values[position].getTitle());
        tv.setPadding(24,0,0,0);
        return convertView;
    }
}
