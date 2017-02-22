package com.jiepier.filemanager.ui.category.storage;


import android.util.Log;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.bean.JunkInfo;
import com.jiepier.filemanager.bean.JunkProcessInfo;
import com.jiepier.filemanager.bean.JunkType;
import com.jiepier.filemanager.util.FormatUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by panruijie on 2017/2/20.
 * Email : zquprj@gmail.com
 */

public class StorageAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public StorageAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(AppConstant.TYPE_TITLE, R.layout.item_junk_type);
        addItemType(AppConstant.TYPE_CHILD, R.layout.item_junk_type);
    }

    @Override
    protected void convert(BaseViewHolder holder, MultiItemEntity item) {

        switch (holder.getItemViewType()) {
            case AppConstant.TYPE_TITLE:
                JunkType junkType = (JunkType) item;
                holder.setText(R.id.tv_title, junkType.getTitle())
                        .setText(R.id.tv_total_size, junkType.getTotalSize())
                        .setImageResource(R.id.iv_icon, junkType.getIconResourceId())
                        .setChecked(R.id.cb_junk, junkType.isCheck())
                        .setVisible(R.id.pb_junk, junkType.isProgressVisible())
                        .setVisible(R.id.cb_junk, !junkType.isProgressVisible());
                holder.itemView.setOnClickListener(v -> {
                    Log.w("ruijie", "click");
                    int pos = holder.getAdapterPosition();
                    if (junkType.isExpanded()) {
                        collapse(pos);
                    } else {
                        expand(pos);
                    }
                });
                break;
            case AppConstant.TYPE_CHILD:
                JunkProcessInfo junkProcessInfo = (JunkProcessInfo) item;
                JunkInfo junkInfo = junkProcessInfo.getJunkInfo();
                AppProcessInfo appProcessInfo = junkProcessInfo.getAppProcessInfo();
                if (junkInfo != null) {
                    holder.setText(R.id.tv_title, junkInfo.getName())
                            .setText(R.id.tv_total_size, FormatUtil.formatFileSize(junkInfo.getSize()).toString())
                            .setImageResource(R.id.iv_icon, junkProcessInfo.getIconResourceId())
                            .setChecked(R.id.iv_check, junkProcessInfo.isCheck())
                            .setVisible(R.id.pb_junk, false);
                } else if (appProcessInfo != null) {
                    holder.setText(R.id.tv_title, appProcessInfo.getAppName())
                            .setText(R.id.tv_total_size, FormatUtil.formatFileSize(appProcessInfo.getMemory()).toString())
                            .setImageDrawable(R.id.iv_icon, appProcessInfo.getIcon())
                            .setChecked(R.id.iv_check, junkProcessInfo.isCheck())
                            .setVisible(R.id.pb_junk, false);
                }
                break;
        }
    }

    public JunkType getItem(int index) {
        return (JunkType) (getData().get(index));
    }

    public void setItemTotalSize(int index, String size) {
        getItem(index).setTotalSize(size);
        notifyDataSetChanged();
    }

    public void setData(HashMap<Integer, ArrayList<JunkProcessInfo>> hashMap) {

        /*for (int i = 0 ; i < 6 ; i ++) {
            //list.add(mData.set(i, setItemData(i, hashMap.get(i))));
            Log.w("ruijie", "size=" + setItemData(i, hashMap.get(i)).getSubItems().size() + "");
        }*/

        notifyDataSetChanged();
    }

    private JunkType setItemData(int index, ArrayList<JunkProcessInfo> list) {
        JunkType junkType = getItem(index);
        for (JunkProcessInfo info : list) {
            junkType.addSubItem(info);
        }
        return junkType;
    }

    public void showItemDialog() {
        for (int i = 0; i < 6; i++) {
            ((JunkType) getData().get(i)).setProgressVisible(true);
        }
        notifyDataSetChanged();
    }

    public void dismissItemDialog(int index) {
        ((JunkType) getData().get(index)).setProgressVisible(false);
        notifyItemChanged(index);
    }
}
