package com.tailwebs.aadharindia.utils.custom.newmultipleimageupload.view;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.utils.custom.newmultipleimageupload.model.SquareImage;

import java.util.Collections;
import java.util.List;

public class DragRecycleAdapter extends RecyclerView.Adapter<DragRecycleAdapter.DragViewHolder> implements View.OnClickListener, ItemTouchHelperAdapter {

    private Context context;
    public List<SquareImage> mData;//九宫格图片
    private static int hidePosition = AdapterView.INVALID_POSITION;

    public DragRecycleAdapter(Context context, List<SquareImage> data) {
        this.context = context;
        this.mData = data;
    }

    @Override
    public DragViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        return new DragViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DragViewHolder holder, int position) {
        holder.squareView.setImageData(mData.get(position));
        holder.itemView.setTag(position);//将position保存在itemView的Tag中，以便点击时进行获取
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int fromPosition = source.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < mData.size() && toPosition < mData.size()) {
            //交换数据位置
            Collections.swap(mData, fromPosition, toPosition);
            //刷新位置交换
            int n = 0;
            for (SquareImage s : mData
                    ) {
                Log.d("log ", "mdata  " + n + s.localUrl);
                n++;
            }
            Log.d("log ", "=============================================================");
            notifyItemMoved(fromPosition, toPosition);
        }
        //移动过程中移除view的放大效果
        onItemClear(source);
    }

    @Override
    public void onItemDissmiss(RecyclerView.ViewHolder source) {
        int position = source.getAdapterPosition();
        mData.remove(position); //移除数据
        notifyItemRemoved(position);//刷新数据移除
    }

    @Override
    public void onItemSelect(RecyclerView.ViewHolder source) {
        //当拖拽选中时放大选中的view
        source.itemView.setScaleX(1.2f);
        source.itemView.setScaleY(1.2f);
    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder source) {
        //拖拽结束后恢复view的状态
        source.itemView.setScaleX(1.0f);
        source.itemView.setScaleY(1.0f);
    }

    @Override
    public void onClick(View v) {

    }

    static class DragViewHolder extends RecyclerView.ViewHolder {
        SquareImageView squareView;
        View mView;

        DragViewHolder(View itemView) {
            super(itemView);
            this.mView = itemView;
            squareView = (SquareImageView) itemView.findViewById(R.id.sq);
            squareView.setClickAble(true);
        }

    }

    //更新拖动时的gridView
    void swapView(int draggedPos, int destPos) {
        //从前向后拖动，其他item依次前移
        if (draggedPos < destPos) {
            mData.add(destPos + 1, mData.get(draggedPos));
            mData.remove(draggedPos);
        }
        //从后向前拖动，其他item依次后移
        else if (draggedPos > destPos) {
            mData.add(destPos, mData.get(draggedPos));
            mData.remove(draggedPos + 1);
        }
    }

    void removeView(int position) {
        mData.remove(position);
        int n = 0;
        for (SquareImage s : mData
                ) {
            Log.d("log ", "mdata  " + n + s.localUrl);
            n++;
        }
        Log.d("log ", "=============================================================");
        notifyItemChanged(position);
        notifyDataSetChanged();
//                    notifyItemChanged(position);
//        Collections.
    }
}
