package com.tailwebs.aadharindia.center;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.center.searchinmap.models.GroupModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahana Amina on 30/12/18.
 */

public class ListOfGroupsInCityAdapter extends RecyclerView.Adapter<ListOfGroupsInCityAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<GroupModel> contactList;
    private List<GroupModel> contactListFiltered;
    private ListOfGroupsInCityAdapterListener listener;
    private int lastSelectedPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id,group_id;

        public RadioButton selectionState;

        public MyViewHolder(View view) {
            super(view);
            id = view.findViewById(R.id.id_tv);
            group_id = view.findViewById(R.id.group_id_tv);
            selectionState = view.findViewById(R.id.choose_group);

            selectionState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                    listener.onGroupSelected(contactListFiltered.get(lastSelectedPosition));

                }
            });

        }
    }


    public ListOfGroupsInCityAdapter(Context context, List<GroupModel> contactList, ListOfGroupsInCityAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_of_group_city_center, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final GroupModel contact = contactListFiltered.get(position);
        holder.id.setText(contact.getId());
        holder.group_id.setText(contact.getGroup_id());


//        holder.id.setText(contact.getCenterModel().getCenterAddressModel().getFull_address());
//        holder.group_id.setText(contact.getGroup_id()+"/"+contact.getCenterModel().getContactPersonModel().getName());
        holder.selectionState.setChecked(lastSelectedPosition == position);

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
                    List<GroupModel> filteredList = new ArrayList<>();
                    for (GroupModel row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getGroup_id().toLowerCase().contains(charString.toLowerCase())) {
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
                contactListFiltered = (ArrayList<GroupModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ListOfGroupsInCityAdapterListener {
        void onGroupSelected(GroupModel languageModel);
    }
}
