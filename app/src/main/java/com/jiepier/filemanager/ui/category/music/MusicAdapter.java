package com.jiepier.filemanager.ui.category.music;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseAdapter;
import com.jiepier.filemanager.base.BaseViewHolder;
import com.jiepier.filemanager.bean.Music;
import com.jiepier.filemanager.preview.IconPreview;
import com.jiepier.filemanager.util.TimeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by panruijie on 17/1/12.
 * Email : zquprj@gmail.com
 */

public class MusicAdapter extends BaseAdapter<Music,BaseViewHolder> {

    private SparseBooleanArray selectedItems;
    private boolean isLongClick;

    public MusicAdapter(Context context) {
        super(R.layout.item_music);
        mContext = context;
        selectedItems = new SparseBooleanArray();
        isLongClick = false;
    }

    public void setData(List<Music> data){
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder holder, Music music) {

        File file = new File(music.getUrl());
        IconPreview.getFileIcon(file, holder.getView(R.id.row_image));

        holder.setText(R.id.tv_music_list_title,music.getTitle())
                .setText(R.id.tv_music_list_time, TimeUtil.getTime(music.getDuration()))
                .setText(R.id.tv_music_list_artist,music.getArtist())
                .setText(R.id.tv_music_list_album,music.getAlbum());

        int position = holder.getLayoutPosition();
        if (selectedItems.get(position, false)){
            holder.setVisibility(R.id.iv_check,View.VISIBLE)
                    .setVisibility(R.id.bottom_view,View.INVISIBLE);
        }else {
            holder.setVisibility(R.id.iv_check,View.GONE)
                    .setVisibility(R.id.bottom_view,View.GONE);
        }

        holder.itemView.setOnClickListener(view -> {
            if (isLongClick) {
                toggleSelection(position);
                if (getSelectedItemCount()==0) {
                    isLongClick = false;
                    mListener.onMultipeChoiceCancel();
                }
                else {
                    mListener.onMultipeChoice(getSelectedFilesPath());
                    if (getSelectedItemCount()==1)
                        mListener.onMultipeChoiceStart();
                }
                mListener.onMultipeChoice(getSelectedFilesPath());
            }else {
                mListener.onItemClick(position);
            }
        });

        holder.itemView.setOnLongClickListener(view -> {
            toggleSelection(position);
            if (getSelectedItemCount()!=0) {
                isLongClick = true;
                if (getSelectedItemCount()==1)
                    mListener.onMultipeChoiceStart();
            }
            else {
                isLongClick = false;
                mListener.onMultipeChoiceCancel();
            }
            mListener.onMultipeChoice(getSelectedFilesPath());
            return false;
        });
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public void setAllSelections(){
        for (int i = 0;i < getItemCount();i++){
            selectedItems.put(i,true);
        }
        mListener.onMultipeChoice(getSelectedFilesPath());
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public List<String> getSelectedFilesPath(){

        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }

        List<String> list = new ArrayList<>();
        for (int i=0;i<items.size();i++){
            list.add(getData(items.get(i)).getUrl());
        }
        return list;
    }

    public void isLongClick(boolean click){
        isLongClick = click;
    }
}
