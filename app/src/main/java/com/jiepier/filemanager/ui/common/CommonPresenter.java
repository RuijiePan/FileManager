package com.jiepier.filemanager.ui.common;

import android.support.annotation.NonNull;
import android.util.Log;

import com.jiepier.filemanager.base.BasePresenter;
import com.jiepier.filemanager.base.BaseView;
import com.jiepier.filemanager.event.NewTabEvent;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.RxBus;

import java.io.File;

/**
 * Created by JiePier on 16/12/14.
 */

public class CommonPresenter implements CommonContact.Presenter {

    private CommonContact.View mView;


    @Override
    public void onItemClick(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            //if (file.list() != nu)
            RxBus.getDefault().post(new NewTabEvent(filePath));
            /*else
                mView.showSnackbar("文件夹为空");*/
        }else {
            if (FileUtil.isSupportedArchive(file)){

            }else {
                mView.openFile(file);
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
