package com.jiepier.filemanager.ui.category.memory;

import android.util.SparseBooleanArray;
import android.widget.CheckBox;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseAdapter;
import com.jiepier.filemanager.base.BaseViewHolder;
import com.jiepier.filemanager.bean.AppProcessInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by panruijie on 17/1/9.
 * Email : zquprj@gmail.com
 */

public class MemoryAdapter extends BaseAdapter<AppProcessInfo, BaseViewHolder> {

    private SparseBooleanArray mCheckBoxArray;
    private Set<String> mChooseSet;
    private OnCheckBoxClickListener mListener;

    public MemoryAdapter() {
        super(R.layout.item_memory_list);
        mCheckBoxArray = new SparseBooleanArray();
        mChooseSet = new HashSet<>();
    }

    @Override
    protected void convert(BaseViewHolder holder, AppProcessInfo item) {

        int position = holder.getLayoutPosition();

        holder.setImageDrawable(R.id.left_image, item.getIcon())
                .setText(R.id.center_textview, item.getAppName())
                .setChceked(R.id.right_checkbox, mCheckBoxArray.get(position));

        if (mListener != null) {
            ((CheckBox) holder.getView(R.id.right_checkbox))
               .setOnCheckedChangeListener((buttonView, isChecked) -> {
                   if (isChecked) {
                       mCheckBoxArray.put(position, true);
                       mChooseSet.add(item.getProcessName());
                       if (mListener != null) {
                           mListener.check(position);
                       }
                   } else {
                       mCheckBoxArray.put(position, false);
                       mChooseSet.remove(item.getProcessName());
                       if (mListener != null) {
                           mListener.unCheck(position);
                       }
                   }
               });
        }

        holder.itemView.setOnClickListener(v -> {
            if (mCheckBoxArray.get(position)) {
                mCheckBoxArray.put(position, false);
                mChooseSet.remove(item.getProcessName());
                if (mListener != null) {
                    mListener.unCheck(position);
                }
                notifyItemChanged(position);
            } else {
                mCheckBoxArray.put(position, true);
                mChooseSet.add(item.getProcessName());
                if (mListener != null) {
                    mListener.check(position);
                }
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public void add(List<AppProcessInfo> data) {
        super.add(data);

        for (int i = 0; i < data.size(); i++) {
            AppProcessInfo info = data.get(i);
            mCheckBoxArray.put(i, true);
            mChooseSet.add(info.getProcessName());
        }
    }

    public void notifityItem() {

        if (mData.size() == 0) {
            return;
        }

        for (int i = mData.size() - 1; i >= 0; i--) {

            if (mCheckBoxArray.get(i)) {
                mData.remove(i);
                notifyItemRemoved(i);
            }
        }

        for (int i = 0; i < mData.size(); i++) {
            if (mCheckBoxArray.get(i)) {
                mCheckBoxArray.put(i, false);
                mChooseSet.remove(mData.get(i).getProcessName());
            }
        }

        notifyDataSetChanged();
    }

    public Set<String> getChooseSet() {
        return mChooseSet;
    }

    public SparseBooleanArray getCheckedArray() {
        return mCheckBoxArray;
    }

    public void setOnCheckBoxClickListener(OnCheckBoxClickListener listener) {
        mListener = listener;
    }

    public interface OnCheckBoxClickListener {

        void check(int position);

        void unCheck(int position);

    }
}
