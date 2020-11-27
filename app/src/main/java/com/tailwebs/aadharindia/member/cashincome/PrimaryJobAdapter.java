package com.tailwebs.aadharindia.member.cashincome;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.cashincome.models.PrimaryJobModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahana Amina on 30/12/18.
 */

public class PrimaryJobAdapter extends RecyclerView.Adapter<PrimaryJobAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<PrimaryJobModel> contactList;
    private List<PrimaryJobModel> contactListFiltered;
    private int lastSelectedPosition = -1;


    onItemClickListner onItemClickListner;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public RadioButton selectionState;
        public ImageView chooseInner;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            selectionState = view.findViewById(R.id.choose_job);
            chooseInner = view.findViewById(R.id.choose_inner);


            selectionState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    onItemClickListner.onClick(contactListFiltered.get(lastSelectedPosition));

                }
            });




            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback

                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    onItemClickListner.onClick(contactListFiltered.get(lastSelectedPosition));
                }
            });
        }
    }


    public PrimaryJobAdapter(Context context, List<PrimaryJobModel> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_primary_job, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final PrimaryJobModel contact = contactListFiltered.get(position);
        holder.name.setText(contact.getName());
        if (!contact.getIs_leaf()){
            holder.chooseInner.setVisibility(View.VISIBLE);
            holder.selectionState.setVisibility(View.GONE);

        }else{
            holder.chooseInner.setVisibility(View.GONE);
            holder.selectionState.setVisibility(View.VISIBLE);
        }

        holder.selectionState.setChecked(lastSelectedPosition == position);

        holder.chooseInner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("Aadhar codde",""+contact.getCode());
                onItemClickListner.onClick(contactListFiltered.get(position));

            }
        });

    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<PrimaryJobModel> filteredList = new ArrayList<>();
                    for (PrimaryJobModel row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<PrimaryJobModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setOnItemClickListner(PrimaryJobAdapter.onItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public interface onItemClickListner{
        void onClick(PrimaryJobModel primaryJobModel);//pass your object types.
    }

}
