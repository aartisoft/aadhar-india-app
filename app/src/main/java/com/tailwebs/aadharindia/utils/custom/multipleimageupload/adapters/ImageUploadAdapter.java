package com.tailwebs.aadharindia.utils.custom.multipleimageupload.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.utils.custom.multipleimageupload.models.ImageUploadModel;

import java.util.ArrayList;

/**
 * Created by Oclemy on 8/4/2016 for ProgrammingWizards Channel and http://www.camposha.com.
 */
public class ImageUploadAdapter extends BaseAdapter {

    Context c;
    ArrayList<ImageUploadModel> spacecrafts;

    public ImageUploadAdapter(Context c, ArrayList<ImageUploadModel> spacecrafts) {
        this.c = c;
        this.spacecrafts = spacecrafts;
    }

    @Override
    public int getCount() {
        return spacecrafts.size();
    }

    @Override
    public Object getItem(int i) {
        return spacecrafts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            //INFLATE CUSTOM LAYOUT
            view = LayoutInflater.from(c).inflate(R.layout.image_model, viewGroup, false);
        }

        final ImageUploadModel s = (ImageUploadModel) this.getItem(i);

        TextView nameTxt = (TextView) view.findViewById(R.id.nameTxt);
        ImageView img = (ImageView) view.findViewById(R.id.spacecraftImg);

        //BIND DATA
        nameTxt.setText(s.getName());
        Picasso.with(c).load(s.getUri()).placeholder(R.drawable.img_placeholder).into(img);

        //VIEW ITEM CLICK
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(c, s.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
