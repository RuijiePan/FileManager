package com.jiepier.filemanager.ui.category.storage;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.bean.JunkInfo;
import com.jiepier.filemanager.manager.CleanManager;
import com.jiepier.filemanager.manager.ProcessManager;
import com.jiepier.filemanager.manager.ScanManager;

import java.util.ArrayList;
import java.util.Set;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by panruijie on 2017/2/19.
 * Email : zquprj@gmail.com
 */

public class StoragePresenter implements StorageContact.Presenter {

    private StorageContact.View mView;
    private Context mContext;
    private CompositeSubscription mCompositeSubscription;
    private ScanManager mScanManger;
    private CleanManager mCleanManager;
    private ProcessManager mProcessManager;
    private long mTotalJunkSize;

    public StoragePresenter(Context context) {
        this.mContext = context;
        mCompositeSubscription = new CompositeSubscription();
        mScanManger = ScanManager.getInstance();
        mCleanManager = CleanManager.getInstance();
        mProcessManager = ProcessManager.getInstance();
    }

    @Override
    public void startScanTask() {

        mScanManger.startScanTask();
        mScanManger.setScanListener(new ScanManager.IScanListener() {

            @Override
            public void startScan() {
                mView.showOverDialog();
                mView.showSystemCacheDialog();
                mView.showProcessDialog();
                mTotalJunkSize = 0L;
            }

            @Override
            public void isOverScanFinish() {
                mView.dimissOverDialog();
            }

            @Override
            public void isSysCacheScanFinish() {
                mView.dimissSystemCacheDialog();
            }

            @Override
            public void isProcessScanFinish() {
                mView.dimissProcessDialog();
            }

            @Override
            public void isAllScanFinish(ArrayList<JunkInfo> overList, ArrayList<JunkInfo> sysCacheList, ArrayList<AppProcessInfo> processList) {
                mView.setData(overList, sysCacheList, processList);
                for (JunkInfo junkInfo : overList) {
                    mTotalJunkSize += junkInfo.getSize();
                }
                for (JunkInfo junkInfo : sysCacheList) {
                    mTotalJunkSize += junkInfo.getSize();
                }
                for (AppProcessInfo appInfo : processList) {
                    mTotalJunkSize += appInfo.getMemory();
                }
                mView.setTotalJunk(mTotalJunkSize);
            }

            @Override
            public void currentOverScanJunk(JunkInfo junkInfo) {
                mView.setCurrenOverScanJunk(junkInfo);
            }

            @Override
            public void currentSysCacheScanJunk(JunkInfo junkInfo) {
                mView.setCurrenSysCacheScanJunk(junkInfo);
            }
        });

        mProcessManager.getRunningAppListUsingObservable()
                .subscribe(appProcessInfos -> {
                    mView.setRunningAppData((ArrayList<AppProcessInfo>) appProcessInfos);
                }, Throwable::printStackTrace);
    }

    @Override
    public void startCleanTask(ArrayList<JunkInfo> overList,
                               ArrayList<JunkInfo> sysCacheList,
                               Set<String> processSet) {

        //合并清理rx监听
        mCleanManager.cleanJunksUsingObservable(overList)
                .zipWith(mCleanManager.cleanJunksUsingObservable(sysCacheList),
                        (aBoolean, aBoolean2) -> aBoolean && aBoolean2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .zipWith(mProcessManager.killRunningAppUsingObservable(processSet),
                        (aBoolean, aLong) -> aBoolean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {

                }, Throwable::printStackTrace);

    }

    @Override
    public void attachView(@NonNull StorageContact.View view) {
        this.mView = view;
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
