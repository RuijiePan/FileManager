package com.jiepier.filemanager.ui.common;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jiepier.filemanager.event.ChoiceFolderEvent;
import com.jiepier.filemanager.event.NewTabEvent;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.RxBus.RxBus;

import java.io.File;

/**
 * Created by JiePier on 16/12/14.
 */

public class CommonBasePresenter implements CommonContact.BasePresenter {

    private CommonContact.View mView;
    private Context mContext;

    public CommonBasePresenter(Context context){
        this.mContext = context;
    }

    @Override
    public void onItemClick(String filePath,String parentPath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            //if (file.list() != nu)
            RxBus.getDefault().post(new NewTabEvent(filePath));
        }else {
            //解压文件
            if (FileUtil.isSupportedArchive(file)){
                RxBus.getDefault().post(new ChoiceFolderEvent(filePath,parentPath));
            }else {
                FileUtil.openFile(mContext,file);
            }
        }
    }

    @Override
    public void attachView(@NonNull CommonContact.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }
}
