package com.jiepier.filemanager.ui.category.storage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseViewHolder;
import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.bean.JunkGroup;
import com.jiepier.filemanager.bean.JunkInfo;
import com.jiepier.filemanager.bean.JunkProcessInfo;
import com.jiepier.filemanager.bean.JunkType;
import com.jiepier.filemanager.bean.entity.MultiItemEntity;
import com.jiepier.filemanager.event.JunkTypeClickEvent;
import com.jiepier.filemanager.event.TotalJunkSizeEvent;
import com.jiepier.filemanager.preview.IconPreview;
import com.jiepier.filemanager.util.DisplayUtil;
import com.jiepier.filemanager.util.FormatUtil;
import com.jiepier.filemanager.util.RxBus.RxBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by panruijie on 2017/2/27.
 * Email : zquprj@gmail.com
 */

public class StorageExpandAdapter extends BaseExpandableListAdapter {

    private List<MultiItemEntity> mData;
    private Context mContext;

    public StorageExpandAdapter(Context context, List<MultiItemEntity> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getGroupCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (((JunkType) (mData.get(groupPosition))).getSubItems() == null) {
            return 0;
        }
        return ((JunkType) (mData.get(groupPosition))).getSubItems().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return ((JunkType) (mData.get(groupPosition))).getSubItems().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        BaseViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_junk_type, null);
            convertView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mContext, 60)));
            holder = new BaseViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (BaseViewHolder) convertView.getTag();
        }

        JunkType junkType = (JunkType) mData.get(groupPosition);
        holder.setText(R.id.tv_title, junkType.getTitle())
                .setText(R.id.tv_total_size, junkType.getTotalSize())
                .setImageResource(R.id.iv_icon, junkType.getIconResourceId())
                .setChecked(R.id.cb_junk, junkType.isCheck())
                .setVisibility(R.id.pb_junk, junkType.isProgressVisible())
                .setVisibility(R.id.cb_junk, !junkType.isProgressVisible());
        convertView.setFocusable(true);

        convertView.setOnClickListener(v -> RxBus.getDefault().post(new JunkTypeClickEvent(isExpanded, groupPosition)));

        ((CheckBox) holder.getView(R.id.cb_junk))
                .setOnCheckedChangeListener((buttonView, isChecked) -> {
                    junkType.setCheck(isChecked);
                    if (junkType.getSubItems() != null) {
                        if (junkType.isCheck()) {
                            for (int i = 0; i < junkType.getSubItems().size(); i++) {
                                junkType.getSubItem(i).setCheck(true);
                            }
                        } else {
                            for (int i = 0; i < junkType.getSubItems().size(); i++) {
                                junkType.getSubItem(i).setCheck(false);
                            }
                        }
                    }
                    mData.set(groupPosition, junkType);
                    computerJunSize();
                    notifyDataSetChanged();
                });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        BaseViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_junk_child, null);
            convertView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mContext, 60)));
            holder = new BaseViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (BaseViewHolder) convertView.getTag();
        }

        JunkProcessInfo junkProcessInfo = ((JunkType) (mData.get(groupPosition))).getSubItem(childPosition);
        JunkInfo junkInfo = junkProcessInfo.getJunkInfo();
        AppProcessInfo appProcessInfo = junkProcessInfo.getAppProcessInfo();
        if (junkInfo != null) {
            holder.setText(R.id.tv_title, junkInfo.getName())
                    .setText(R.id.tv_total_size, FormatUtil.formatFileSize(junkInfo.getSize()).toString())
                    .setChecked(R.id.cb_junk, junkProcessInfo.isCheck())
                    .setVisibility(R.id.pb_junk, false);
            if (junkProcessInfo.getType() != JunkType.CACHE) {
                IconPreview.getFileIcon(new File(junkInfo.getPath()), holder.getView(R.id.iv_icon));
            } else {
                holder.setImageResource(R.id.iv_icon, R.drawable.icon_cache_blue_24dp);
            }
        } else if (appProcessInfo != null) {
            holder.setText(R.id.tv_title, appProcessInfo.getAppName())
                    .setText(R.id.tv_total_size, FormatUtil.formatFileSize(appProcessInfo.getMemory()).toString())
                    .setImageDrawable(R.id.iv_icon, appProcessInfo.getIcon())
                    .setChecked(R.id.cb_junk, junkProcessInfo.isCheck())
                    .setVisibility(R.id.pb_junk, false);
        }

        ((CheckBox) holder.getView(R.id.cb_junk)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            junkProcessInfo.setCheck(isChecked);
            computerJunSize();
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void computerJunSize() {

        long size = 0L;

        for (int i = 0; i < mData.size(); i++) {
            JunkType junkType1 = (JunkType) mData.get(i);
            if (junkType1.getSubItems() != null) {
                for (JunkProcessInfo info : junkType1.getSubItems()) {
                    if (info.isCheck()) {
                        if (info.getJunkInfo() != null) {
                            size += info.getJunkInfo().getSize();
                        } else if (info.getAppProcessInfo() != null) {
                            size += info.getAppProcessInfo().getMemory();
                        }
                    }
                }
            }
        }
        RxBus.getDefault().post(new TotalJunkSizeEvent(FormatUtil.formatFileSize(size).toString()));

    }

    public void showItemDialog() {
        for (int i = 0; i < 6; i++) {
            ((JunkType) mData.get(i)).setProgressVisible(true);
        }
        notifyDataSetChanged();
    }

    public void dismissItemDialog(int index) {
        ((JunkType) mData.get(index)).setProgressVisible(false);
        notifyDataSetChanged();
    }

    public JunkType getItem(int index) {
        return (JunkType) (mData.get(index));
    }

    public void setItemTotalSize(int index, String size) {
        getItem(index).setTotalSize(size);
        notifyDataSetChanged();
    }

    public void setData(JunkGroup junkGroup) {

        for (int i = 0; i < 6; i++) {
            JunkType junkType = (JunkType) mData.get(i);
            ArrayList<JunkProcessInfo> infos = junkGroup.getJunkList(i);

            for (int j = 0; j < infos.size(); j++) {
                junkType.addSubItem(infos.get(j));
            }
            mData.set(i, junkType);
        }
        notifyDataSetChanged();
    }

    public List<MultiItemEntity> getData() {
        return mData;
    }
}
