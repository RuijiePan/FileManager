package com.jiepier.filemanager.base;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiePier on 16/12/6.
 */

public abstract class BaseAdapter<T,K extends BaseViewHolder> extends RecyclerView.Adapter<K>{

    protected List<T> mData;
    protected int mLayoutResId;
    protected Resources mResources;
    protected Context mContext;
    private OnRecyclerViewItemClickListener mListener;

    public BaseAdapter(int layoutResId, List<T> data){
        this.mData = data == null ? new ArrayList<T>():data;
        if (mLayoutResId != 0){
            this.mLayoutResId = layoutResId;
        }
    }

    public BaseAdapter(int layoutResId){
        this(layoutResId,new ArrayList<T>());
    }

    public BaseAdapter(List<T> data){
        this(0,data);
    }

    @Override
    public int getItemCount() {
        return mData == null? 0 : mData.size();
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        mResources = mContext.getResources();
        View item = LayoutInflater.from(mContext).inflate(mLayoutResId,parent,false);
        final K holder = (K) new BaseViewHolder(item);

        if (mListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(holder.getLayoutPosition());
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(K holder, int position) {
        convert(holder , mData.get(position));
    }

    public void add(List<T> data){
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    public void add(T data,int position){
        this.mData.add(position,data);
        notifyItemInserted(position);
    }

    public void remove(int position){
        this.mData.remove(position);
        notifyItemRemoved(position);
    }

    public void clear(){
        this.mData.clear();
        notifyDataSetChanged();
    }

    public interface OnRecyclerViewItemClickListener{
        public void onItemClick(int pisition);
    }

    public void setItemClickListner(OnRecyclerViewItemClickListener listner){
        this.mListener = listner;
    }

    protected abstract void convert(BaseViewHolder holder,T item);


}
