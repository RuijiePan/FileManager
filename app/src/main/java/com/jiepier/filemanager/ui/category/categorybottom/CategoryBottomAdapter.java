package com.jiepier.filemanager.ui.category.categorybottom;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseAdapter;
import com.jiepier.filemanager.base.BaseViewHolder;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.Settings;
import com.jiepier.filemanager.widget.IconPreview;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by JiePier on 16/12/14.
 */

public class CategoryBottomAdapter extends BaseAdapter<String,BaseViewHolder> {

    private SparseBooleanArray selectedItems;
    private boolean isLongClick;

    public CategoryBottomAdapter(Context context) {
        super(R.layout.item_category_bottom);
        mContext = context;
        selectedItems = new SparseBooleanArray();
        isLongClick = false;
    }

    public void setData(List<String> data){
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder holder, String path) {

        int numItems = 0;
        File file = new File(path);

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT, Locale.getDefault());

        if (Settings.getListAppearance() > 0){
            holder.setVisibility(R.id.dateview, View.VISIBLE);
        }else {
            holder.setVisibility(R.id.dateview, View.GONE);
        }

        IconPreview.getFileIcon(file, holder.getView(R.id.row_image));
        if (file.isFile()){
            holder.setText(R.id.bottom_view, FileUtil.formatCalculatedSize(file.length()));
        }else {
            String[] list = file.list();

            if (list != null){
                numItems = list.length;
            }
            holder.setText(R.id.bottom_view,numItems+mResources.getString(R.string.files));
        }

        holder.setText(R.id.top_view,file.getName())
                .setText(R.id.dateview,df.format(file.lastModified()));


        int position = holder.getLayoutPosition();
        if (selectedItems.get(position, false)){
            holder.setVisibility(R.id.bottom_view,View.GONE);
            holder.setVisibility(R.id.iv_check,View.VISIBLE);
        }else {
            holder.setVisibility(R.id.bottom_view,View.VISIBLE);
            holder.setVisibility(R.id.iv_check,View.GONE);
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
            list.add(getData(items.get(i)));
        }
        return list;
    }

    public void isLongClick(boolean click){
        isLongClick = click;
    }
}
