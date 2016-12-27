package com.jiepier.filemanager.ui.category;

import android.support.v7.widget.RecyclerView;

import com.jiepier.filemanager.base.BaseAdapter;
import com.jiepier.filemanager.base.BaseViewHolder;

import java.util.List;

/**
 * Created by panruijie on 16/12/27.
 * Email : zquprj@gmail.com
 */

public class CategoryAdapter extends BaseAdapter<Integer,BaseViewHolder> {


    public CategoryAdapter(List<Integer> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Integer item) {

    }
}
