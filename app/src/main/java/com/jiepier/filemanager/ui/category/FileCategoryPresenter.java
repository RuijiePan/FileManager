package com.jiepier.filemanager.ui.category;

import android.content.Context;
import android.content.Intent;

import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.base.App;
import com.jiepier.filemanager.base.BaseRxPresenter;
import com.jiepier.filemanager.manager.CategoryManager;
import com.jiepier.filemanager.ui.category.categorybottom.CategoryBottomActivity;
import com.jiepier.filemanager.ui.category.memory.MemoryActivity;
import com.jiepier.filemanager.ui.category.music.MusicActivity;
import com.jiepier.filemanager.util.SortUtil;

import java.util.ArrayList;

/**
 * Created by panruijie on 17/1/2.
 * Email : zquprj@gmail.com
 */

public class FileCategoryPresenter extends BaseRxPresenter implements FileCategoryContact.Presenter {

    private Context mContext;
    private ArrayList mApkList;
    private ArrayList mDocList;
    private ArrayList mZipList;

    public FileCategoryPresenter(Context context){
        super();
        this.mContext = context;

        CategoryManager.getInstance().getApkListUsingObservable()
                .subscribe(apkList -> {
                    mApkList = (ArrayList) apkList;
                }, Throwable::printStackTrace);

        CategoryManager.getInstance().getDocListUsingObservable()
                .subscribe(docList -> {
                    mDocList = (ArrayList) docList;
                }, Throwable::printStackTrace);

        CategoryManager.getInstance().getZipListUsingObservable()
                .subscribe(zipList -> {
                    mZipList = (ArrayList) zipList;
                }, Throwable::printStackTrace);
    }

    @Override
    public void clickMemoryProgressbar() {
        Intent intent = new Intent(mContext, MemoryActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void clickStorageProgressbar() {

    }

    @Override
    public void clickMusic() {
        Intent intent = new Intent(mContext, MusicActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void clickVideo() {
        Intent intent = new Intent(mContext, CategoryBottomActivity.class);
        intent.putExtra(AppConstant.INDEX,AppConstant.VIDEO_INDEX);
        mContext.startActivity(intent);
    }

    @Override
    public void clickApk() {
        Intent intent = new Intent(mContext, CategoryBottomActivity.class);
        intent.putExtra(AppConstant.INDEX,AppConstant.APK_INDEX);
        mContext.startActivity(intent);
    }

    @Override
    public void clickDoc() {
        Intent intent = new Intent(mContext, CategoryBottomActivity.class);
        intent.putExtra(AppConstant.INDEX,AppConstant.DOC_INDEX);
        mContext.startActivity(intent);
    }

    @Override
    public void clickPicture() {

    }

    @Override
    public void clickZip() {
        Intent intent = new Intent(mContext, CategoryBottomActivity.class);
        intent.putExtra(AppConstant.INDEX,AppConstant.ZIP_INDEX);
        mContext.startActivity(intent);
    }
}
