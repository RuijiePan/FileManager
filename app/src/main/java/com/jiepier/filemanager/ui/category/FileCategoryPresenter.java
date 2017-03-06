package com.jiepier.filemanager.ui.category;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.event.UpdateMemoryInfoEvent;
import com.jiepier.filemanager.manager.CategoryManager;
import com.jiepier.filemanager.ui.category.categorybottom.CategoryBottomActivity;
import com.jiepier.filemanager.ui.category.memory.MemoryActivity;
import com.jiepier.filemanager.ui.category.music.MusicActivity;
import com.jiepier.filemanager.ui.category.picture.PictureActivity;
import com.jiepier.filemanager.ui.category.storage.StorageActivity;
import com.jiepier.filemanager.util.FormatUtil;
import com.jiepier.filemanager.util.RxBus.RxBus;
import com.jiepier.filemanager.util.StorageUtil;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by panruijie on 17/1/2.
 * Email : zquprj@gmail.com
 */

public class FileCategoryPresenter implements FileCategoryContact.Presenter {

    private FileCategoryContact.View mView;
    private CategoryManager mCategoryManager;
    private CompositeSubscription mCompositeSubscription;
    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private ArrayList mApkList;
    private ArrayList mDocList;
    private ArrayList mZipList;

    public FileCategoryPresenter(Context context){
        super();
        this.mContext = context;
        mCategoryManager = CategoryManager.getInstance();
        mCompositeSubscription = new CompositeSubscription();

        mCategoryManager.getApkListUsingObservable()
                .subscribe(apkList -> {
                    mApkList = (ArrayList) apkList;
                }, Throwable::printStackTrace);

        mCategoryManager.getDocListUsingObservable()
                .subscribe(docList -> {
                    mDocList = (ArrayList) docList;
                }, Throwable::printStackTrace);

        mCategoryManager.getZipListUsingObservable()
                .subscribe(zipList -> {
                    mZipList = (ArrayList) zipList;
                }, Throwable::printStackTrace);

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(UpdateMemoryInfoEvent.class)
                .observeOn(Schedulers.io())
                .subscribe(updateMemoryInfoEvent -> {
                    updateMemoryInfo();
                }, Throwable::printStackTrace));
    }

    @Override
    public void clickMemoryProgressbar() {
        Intent intent = new Intent(mContext, MemoryActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void clickStorageProgressbar() {
        Intent intent = new Intent(mContext, StorageActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void updateMemoryInfo() {

        mCategoryManager.getAvaliableMemoryUsingObservable()
                .zipWith(mCategoryManager.getTotalMemoryUsingObservable(),
                        (avaliable, total) -> {
                            long usedMemory = total - avaliable;
                            mView.setMemoryProgress(usedMemory * 100 / total);
                            return FormatUtil.formatFileSize(usedMemory).toString() + "/" +
                                    FormatUtil.formatFileSize(total).toString();
                        })
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(memory -> {
                    mView.setMemoryText(memory);
                }, Throwable::printStackTrace);
    }

    @Override
    public void updateStorageInfo() {

        StorageUtil.SDCardInfo sdCardInfo = StorageUtil.getSDCardInfo(mContext);
        String storageInfo = FormatUtil.formatFileSize(sdCardInfo.mTotal - sdCardInfo.mFree).toString() + "/" +
                FormatUtil.formatFileSize(sdCardInfo.mTotal).toString();
        float storagePercent = (sdCardInfo.mTotal - sdCardInfo.mFree) * 100 / sdCardInfo.mTotal;

        mView.setMemoryText(storageInfo);
        mView.setStorageProgress(storagePercent);
    }

    @Override
    public void clickMusic() {
        Intent intent = new Intent(mContext, MusicActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void clickVideo() {
        Intent intent = new Intent(mContext, CategoryBottomActivity.class);
        intent.putExtra(AppConstant.INDEX, AppConstant.VIDEO_INDEX);
        mContext.startActivity(intent);
    }

    @Override
    public void clickApk() {
        Intent intent = new Intent(mContext, CategoryBottomActivity.class);
        intent.putExtra(AppConstant.INDEX, AppConstant.APK_INDEX);
        mContext.startActivity(intent);
    }

    @Override
    public void clickDoc() {
        Intent intent = new Intent(mContext, CategoryBottomActivity.class);
        intent.putExtra(AppConstant.INDEX, AppConstant.DOC_INDEX);
        mContext.startActivity(intent);
    }

    @Override
    public void clickPicture() {
        Intent intent = new Intent(mContext, PictureActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void clickZip() {
        Intent intent = new Intent(mContext, CategoryBottomActivity.class);
        intent.putExtra(AppConstant.INDEX, AppConstant.ZIP_INDEX);
        mContext.startActivity(intent);
    }

    @Override
    public void attachView(@NonNull FileCategoryContact.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        if (mCompositeSubscription.isUnsubscribed()) {
            this.mCompositeSubscription.unsubscribe();
        }
        this.mCompositeSubscription = null;
    }
}
