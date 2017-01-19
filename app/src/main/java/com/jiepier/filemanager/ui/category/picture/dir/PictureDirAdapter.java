package com.jiepier.filemanager.ui.category.picture.dir;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.App;
import com.jiepier.filemanager.base.BaseAdapter;
import com.jiepier.filemanager.base.BaseViewHolder;

import java.util.List;

/**
 * Created by panruijie on 17/1/19.
 * Email : zquprj@gmail.com
 */

public class PictureDirAdapter extends BaseAdapter<String,BaseViewHolder>{

    public PictureDirAdapter(Context context) {
        super(R.layout.item_image);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {

        View view = holder.getView(R.id.root);
        view.setLayoutParams(new RelativeLayout.LayoutParams(
                App.sWidth/3, App.sWidth/3));

        holder.loadLocal(R.id.image,item);
    }
}
