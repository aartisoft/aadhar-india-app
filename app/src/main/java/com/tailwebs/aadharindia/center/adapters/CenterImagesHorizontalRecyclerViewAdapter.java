package com.tailwebs.aadharindia.center.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.center.CenterShowActivity;
import com.tailwebs.aadharindia.utils.custom.horizontalfullscreen.ImagePreviewActivity;
import com.tailwebs.aadharindia.utils.custom.horizontalfullscreen.PreviewFile;

import java.util.ArrayList;


/**
 * Created by User on 2/12/2018.
 */

public class CenterImagesHorizontalRecyclerViewAdapter extends RecyclerView.Adapter<CenterImagesHorizontalRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Context mContext;

    private String fromPage;

    public CenterImagesHorizontalRecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<String> imageUrls,String from) {
        mNames = names;
        mImageUrls = imageUrls;
        mContext = context;
        fromPage = from;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_center_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        if(fromPage.equalsIgnoreCase("fromCenterShow")){
            Uri imgUri=Uri.parse(mImageUrls.get(position));

        Picasso.with(mContext)
                .load(imgUri)
                .placeholder(R.drawable.userimg_placeholder)
                .into(holder.image);

        }else{
            holder.image.setImageBitmap(BitmapFactory.decodeFile(mImageUrls.get(position)));
        }





        holder.name.setText(mNames.get(position));

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: " + mNames.get(position));

                ArrayList<PreviewFile> imageList = new ArrayList<>();

                for(int i =0 ;i<mImageUrls.size();i++){
                    imageList.add(new PreviewFile(mImageUrls.get(i), ""));

                }

                Intent intent = new Intent(mContext,
                        ImagePreviewActivity.class);
                intent.putExtra(ImagePreviewActivity.IMAGE_LIST,
                        imageList);
                intent.putExtra(ImagePreviewActivity.CURRENT_ITEM, position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
        }
    }
}
