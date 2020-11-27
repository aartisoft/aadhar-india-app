package com.tailwebs.aadharindia.member.applicant;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.models.LanguageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahana Amina on 30/12/18.
 */

public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<LanguageModel> contactList;
    private List<LanguageModel> contactListFiltered;
    private LanguageAdapterListener listener;
    private int lastSelectedPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public RadioButton selectionState;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            selectionState = view.findViewById(R.id.choose_language);

            selectionState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    listener.onLanguageSelected(contactListFiltered.get(lastSelectedPosition));

                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    listener.onLanguageSelected(contactListFiltered.get(lastSelectedPosition));
                }
            });
        }
    }


    public LanguagesAdapter(Context context, List<LanguageModel> contactList, LanguageAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_language, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final LanguageModel contact = contactListFiltered.get(position);
        holder.name.setText(contact.getName());
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
                    List<LanguageModel> filteredList = new ArrayList<>();
                    for (LanguageModel row : contactList) {

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
                contactListFiltered = (ArrayList<LanguageModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface LanguageAdapterListener {
        void onLanguageSelected(LanguageModel languageModel);
    }
}
