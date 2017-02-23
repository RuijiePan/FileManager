package com.jiepier.filemanager.ui.category.storage;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.bean.JunkGroup;
import com.jiepier.filemanager.bean.JunkInfo;
import com.jiepier.filemanager.bean.JunkProcessInfo;
import com.jiepier.filemanager.bean.JunkType;
import com.jiepier.filemanager.preview.IconPreview;
import com.jiepier.filemanager.util.FormatUtil;

import java.io.File;
import java.util.ArrayList;
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
        addItemType(AppConstant.TYPE_CHILD, R.layout.item_junk_child);
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
                    int pos = holder.getAdapterPosition();
                    if (junkType.isExpanded()) {
                        collapse(pos);
                    } else {
                        expand(pos);
                    }
                });
                break;
            case AppConstant.TYPE_CHILD:
                //child有两类：一类是进程、一类是普通junk，当为安装包当时候需要特殊处理图片图标
                JunkProcessInfo junkProcessInfo = (JunkProcessInfo) item;
                JunkInfo junkInfo = junkProcessInfo.getJunkInfo();
                AppProcessInfo appProcessInfo = junkProcessInfo.getAppProcessInfo();
                if (junkInfo != null) {
                    holder.setText(R.id.tv_title, junkInfo.getName())
                            .setText(R.id.tv_total_size, FormatUtil.formatFileSize(junkInfo.getSize()).toString())
                            .setChecked(R.id.iv_check, junkProcessInfo.isCheck())
                            .setVisible(R.id.pb_junk, false);
                    if (junkInfo.getPath() != null) {
                        IconPreview.getFileIcon(new File(junkInfo.getPath()), holder.getView(R.id.iv_icon));
                    }
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

    public void setData(JunkGroup junkGroup) {

        for (int i = 0; i < 6; i++) {
            JunkType junkType = (JunkType) mData.get(i);
            ArrayList<JunkProcessInfo> infos = junkGroup.getJunkList(i);
            //Log.w("ruijie", "size= " + infos.size());
            for (int j = 0; j < infos.size(); j++) {
                junkType.addSubItem(infos.get(j));
            }
            mData.set(i, junkType);
        }
        notifyDataSetChanged();
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
