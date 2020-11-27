package com.tailwebs.aadharindia.housevisit;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.housevisit.models.PastEventsModel;
import com.tailwebs.aadharindia.housevisit.models.UpcomingEventsModel;
import com.tailwebs.aadharindia.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by sonu on 08/02/17.
 */

public class PastEventsListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PastEventsModel> arrayList;
    private LayoutInflater inflater;
    private boolean isListView;
    private SparseBooleanArray mSelectedItemsIds;



    public PastEventsListAdapter(Context context, ArrayList<PastEventsModel> arrayList, boolean isListView) {
        this.context = context;
        this.arrayList = arrayList;
        this.isListView = isListView;
        inflater = LayoutInflater.from(context);
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();

            //inflate the layout on basis of boolean
            view = inflater.inflate(R.layout.item_personal_list_custom_row, viewGroup, false);
            viewHolder.label = (TextView) view.findViewById(R.id.label);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            viewHolder.noteET = (TextInputEditText) view.findViewById(R.id.input_notes);
            viewHolder.noteETLayout = (TextInputLayout) view.findViewById(R.id.input_layout_notes);
            viewHolder.noteLayout = (LinearLayout) view.findViewById(R.id.noteLayout);

            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();

        viewHolder.label.setText(arrayList.get(i).getName());
//        viewHolder.checkBox.setChecked(mSelectedItemsIds.get(i));


        if(arrayList.get(i).getChecked()){
            viewHolder.checkBox.setChecked(true);
            viewHolder.noteLayout.setVisibility(View.VISIBLE);
            viewHolder.noteET.setText(arrayList.get(i).getNote());
            if(arrayList.get(i).getCode().equalsIgnoreCase("none")){
                viewHolder.noteLayout.setVisibility(View.GONE);
            }else{
                viewHolder.noteLayout.setVisibility(View.VISIBLE);
                viewHolder.noteET.setText(arrayList.get(i).getNote());
            }
        }

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(arrayList.get(i).getCode().equalsIgnoreCase("none")){
                    viewHolder.noteLayout.setVisibility(View.GONE);
                }else{
                    if(viewHolder.checkBox.isChecked()){
                        viewHolder.noteLayout.setVisibility(View.VISIBLE);
                        PersonalInformationActivity.getInstance().updatePastListviewsize();
                    }else{
                        viewHolder.noteLayout.setVisibility(View.GONE);
                        PersonalInformationActivity.getInstance().updatePastListviewsize();
                    }
                }


            }
        });

//        viewHolder.label.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(arrayList.get(i).getCode().equalsIgnoreCase("none")){
//                    viewHolder.noteLayout.setVisibility(View.GONE);
//                }else{
//                    if(viewHolder.checkBox.isChecked()){
//                        viewHolder.noteLayout.setVisibility(View.VISIBLE);
//                    }else{
//                        viewHolder.noteLayout.setVisibility(View.GONE);
//                    }
//                }
//            }
//        });

        viewHolder.noteET.setText(arrayList.get(i).getNote());

        viewHolder.noteET.addTextChangedListener(new TextWatcher() {
            ArrayList<String> itemPassed = new ArrayList<String>();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayList.get(i).setNote(viewHolder.noteET.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

                itemPassed.add("");
                boolean status = UiUtils.checkValidation(context, viewHolder.noteET, viewHolder.noteETLayout, itemPassed);

                if (status == false) {

                } else {

                    viewHolder.noteETLayout.setErrorEnabled(false);
                }



            }
        });




        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked){
                    Log.i("Aadhar India","is checked"+arrayList.get(i).getName());
                    arrayList.get(i).setChecked(true);


                } else {

                    arrayList.get(i).setChecked(false);


                }
            }
        });

        return view;
    }

    private class ViewHolder {
        private TextView label;
        private CheckBox checkBox;
        private LinearLayout noteLayout;
        private TextInputEditText noteET;
        private TextInputLayout noteETLayout;
    }




//    /**
//     * Check the Checkbox if not checked
//     **/
//    public void checkCheckBox(int position, boolean value) {
//        if (value)
//            mSelectedItemsIds.put(position, true);
//        else
//            mSelectedItemsIds.delete(position);
//
//        notifyDataSetChanged();
//    }

    /**
     * Return the selected Checkbox IDs
     **/
    public ArrayList<PastEventsModel> getSelectedIds() {
        return arrayList;
    }

}
