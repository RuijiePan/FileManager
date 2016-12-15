package com.jiepier.filemanager.ui.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseAdapter;
import com.jiepier.filemanager.base.BaseViewHolder;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.Settings;
import com.jiepier.filemanager.util.SortUtils;
import com.jiepier.filemanager.widget.IconPreview;

import java.io.File;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by JiePier on 16/12/14.
 */

public class BrowserListAdapter extends BaseAdapter<String,BaseViewHolder> {


    public BrowserListAdapter(Context context) {
        super(R.layout.item_browserlist);
        mContext = context;
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
    }

    public void addFiles(String path){
        mData = FileUtil.listFiles(path,mContext);

        if (mData != null)
            SortUtils.sortList(mData,path);

        notifyDataSetChanged();
    }
}
