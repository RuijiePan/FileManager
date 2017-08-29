package com.jiepier.filemanager.ui.category.storage;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.bean.JunkGroup;
import com.jiepier.filemanager.bean.JunkInfo;
import com.jiepier.filemanager.bean.JunkProcessInfo;
import com.jiepier.filemanager.bean.JunkType;
import com.jiepier.filemanager.bean.entity.MultiItemEntity;
import com.jiepier.filemanager.event.CurrenScanJunkEvent;
import com.jiepier.filemanager.event.CurrentJunSizeEvent;
import com.jiepier.filemanager.event.ItemTotalJunkSizeEvent;
import com.jiepier.filemanager.event.JunkDataEvent;
import com.jiepier.filemanager.event.JunkDismissDialogEvent;
import com.jiepier.filemanager.event.JunkShowDialogEvent;
import com.jiepier.filemanager.event.JunkTypeClickEvent;
import com.jiepier.filemanager.event.TotalJunkSizeEvent;
import com.jiepier.filemanager.manager.CleanManager;
import com.jiepier.filemanager.manager.ProcessManager;
import com.jiepier.filemanager.manager.ScanManager;
import com.jiepier.filemanager.preview.MimeTypes;
import com.jiepier.filemanager.util.FormatUtil;
import com.jiepier.filemanager.util.RxBus.RxBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
    private boolean mOverScanFinish;

    public StoragePresenter(Context context) {
        this.mContext = context.getApplicationContext();
        mCompositeSubscription = new CompositeSubscription();
        mScanManger = ScanManager.getInstance();
        mCleanManager = CleanManager.getInstance();
        mProcessManager = ProcessManager.getInstance();

        //生产者速度太快，加上sample对事件进行过滤。否则会出现rx.exceptions.MissingBackpressureException
        //60帧
        mCompositeSubscription.add(RxBus.getDefault()
                .toObservable(TotalJunkSizeEvent.class)
                .sample(16, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(totalJunkSizeEvent -> {
                    mView.setTotalJunk(totalJunkSizeEvent.getTotalSize());
                }, Throwable::printStackTrace));

        //当前扫描类名
        mCompositeSubscription.add(RxBus.getDefault()
                .toObservable(CurrenScanJunkEvent.class)
                .sample(16, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (event.getType() == CurrenScanJunkEvent.OVER_CACHE) {
                        mView.setCurrenOverScanJunk(event.getJunkInfo());
                    } else if (event.getType() == CurrenScanJunkEvent.SYS_CAHCE) {
                        mView.setCurrenSysCacheScanJunk(event.getJunkInfo());
                    }
                }, Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(CurrentJunSizeEvent.class)
                .subscribe(event -> {
                    mTotalJunkSize = event.getTotalSize();
                    mView.setTotalJunk(FormatUtil.formatFileSize(mTotalJunkSize).toString());
                }, Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(ItemTotalJunkSizeEvent.class)
                .subscribe(event -> {
                    mView.setItemTotalJunk(event.getIndex(), event.getTotalSize());
                }, Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(JunkDataEvent.class)
                .subscribe(junkDataEvent -> {
                    mView.setData(junkDataEvent.getJunkGroup());
                }, Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(JunkShowDialogEvent.class)
                .subscribe(event -> {
                    mView.showDialog();
                }, Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(JunkDismissDialogEvent.class)
                .subscribe(event -> {
                    mView.dimissDialog(event.getIndex());
                }, Throwable::printStackTrace));


        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(JunkTypeClickEvent.class)
                .subscribe(event -> {
                    mView.groupClick(event.isExpand(), event.getPosition());
                }, Throwable::printStackTrace));

    }

    @Override
    public void startScanTask() {

        mScanManger.startScanTask();
        mScanManger.setScanListener(new ScanManager.IScanListener() {

            @Override
            public void startScan() {
                RxBus.getDefault().post(new JunkShowDialogEvent());
                mTotalJunkSize = 0L;
                mOverScanFinish = false;
            }

            @Override
            public void isOverScanFinish(ArrayList<JunkInfo> apkList, ArrayList<JunkInfo> logList, ArrayList<JunkInfo> tempList, ArrayList<JunkInfo> bigFileList) {
                RxBus.getDefault().post(new JunkDismissDialogEvent(JunkType.APK));
                RxBus.getDefault().post(new JunkDismissDialogEvent(JunkType.LOG));
                RxBus.getDefault().post(new JunkDismissDialogEvent(JunkType.TEMP));
                RxBus.getDefault().post(new JunkDismissDialogEvent(JunkType.BIG_FILE));

                RxBus.getDefault().post(new ItemTotalJunkSizeEvent(
                        JunkType.APK, getFilterJunkSize(apkList)));
                RxBus.getDefault().post(new ItemTotalJunkSizeEvent(
                        JunkType.LOG, getFilterJunkSize(logList)));
                RxBus.getDefault().post(new ItemTotalJunkSizeEvent(
                        JunkType.TEMP, getFilterJunkSize(tempList)));
                RxBus.getDefault().post(new ItemTotalJunkSizeEvent(
                        JunkType.BIG_FILE, getFilterJunkSize(bigFileList)));

                mOverScanFinish = true;
            }

            @Override
            public void isSysCacheScanFinish(ArrayList<JunkInfo> sysCacheList) {

                RxBus.getDefault().post(new JunkDismissDialogEvent(JunkType.CACHE));
                RxBus.getDefault().post(new ItemTotalJunkSizeEvent(
                        JunkType.CACHE, getFilterJunkSize(sysCacheList)));
            }

            @Override
            public void isProcessScanFinish(ArrayList<AppProcessInfo> processList) {
                RxBus.getDefault().post(new JunkDismissDialogEvent(JunkType.PROCESS));
                long size = 0L;
                for (AppProcessInfo info : processList) {
                    size += info.getMemory();
                }
                mTotalJunkSize += size;
                RxBus.getDefault().post(new ItemTotalJunkSizeEvent(JunkType.PROCESS, FormatUtil.formatFileSize(size).toString()));
                RxBus.getDefault().post(new TotalJunkSizeEvent(FormatUtil.formatFileSize(mTotalJunkSize).toString()));
            }

            @Override
            public void isAllScanFinish(JunkGroup junkGroup) {
                RxBus.getDefault().post(new JunkDataEvent(junkGroup));
                RxBus.getDefault().post(new CurrentJunSizeEvent(mTotalJunkSize));
                /*RxBus.getDefault().post(new ItemTotalJunkSizeEvent(JunkType.PROCESS, getJunkSize(hashMap.get(JunkType.PROCESS))));
                RxBus.getDefault().post(new ItemTotalJunkSizeEvent(JunkType.PROCESS, getJunkSize(hashMap.get(JunkType.PROCESS))));
                RxBus.getDefault().post(new ItemTotalJunkSizeEvent(JunkType.CACHE, getJunkSize(hashMap.get(JunkType.CACHE))));
                RxBus.getDefault().post(new ItemTotalJunkSizeEvent(JunkType.APK, getJunkSize(hashMap.get(JunkType.APK))));
                RxBus.getDefault().post(new ItemTotalJunkSizeEvent(JunkType.LOG, getJunkSize(hashMap.get(JunkType.LOG))));
                RxBus.getDefault().post(new ItemTotalJunkSizeEvent(JunkType.TEMP, getJunkSize(hashMap.get(JunkType.TEMP))));
                RxBus.getDefault().post(new ItemTotalJunkSizeEvent(JunkType.BIG_FILE, getJunkSize(hashMap.get(JunkType.BIG_FILE))));*/
            }

            @Override
            public void currentOverScanJunk(JunkInfo junkInfo) {
                //大文件默认不勾选
                File file = new File(junkInfo.getPath());
                if (MimeTypes.isApk(file) || MimeTypes.isTempFile(file) || MimeTypes.isLog(file)) {
                    mTotalJunkSize += junkInfo.getSize();
                    RxBus.getDefault().post(new TotalJunkSizeEvent(FormatUtil.formatFileSize(mTotalJunkSize).toString()));
                }
                RxBus.getDefault().post(new CurrenScanJunkEvent(CurrenScanJunkEvent.OVER_CACHE, junkInfo));
            }

            @Override
            public void currentSysCacheScanJunk(JunkInfo junkInfo) {
                //缓存文件默认不勾选
                //mTotalJunkSize += junkInfo.getSize();
                if (mOverScanFinish) {
                    RxBus.getDefault().post(new CurrenScanJunkEvent(CurrenScanJunkEvent.SYS_CAHCE, junkInfo));
                }
                //RxBus.getDefault().post(new TotalJunkSizeEvent(FormatUtil.formatFileSize(mTotalJunkSize).toString()));
            }
        });
    }

    @Override
    public void startCleanTask(List<MultiItemEntity> list) {

        List<String> junkList = getJunkList(list);
        List<String> appCacheList = getJunkTypeList(list.get(1));
        Set<String> processSet = getJunkProcessSet(list.get(0));

        //合并清理rx监听
        mCleanManager.cleanJunksUsingObservable(junkList)
                .zipWith(mCleanManager.cleanAppsCacheUsingObservable(appCacheList),
                        (aBoolean, aBoolean2) -> aBoolean && aBoolean2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .zipWith(mProcessManager.killRunningAppUsingObservable(processSet),
                        (aBoolean, aLong) -> aBoolean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    /*删除文件过程中，少部分会删除失败，但是这不影响全局删除过程
                    if (aBoolean) {
                        mView.cleanFinish();
                    } else {
                        mView.cleanFailure();
                    }*/
                    mView.cleanFinish();
                }, Throwable::printStackTrace);
    }

    private List<String> getJunkList(List<MultiItemEntity> list) {
        List<String> tempList = new ArrayList<>();
        for (int i = 2; i < 6; i++) {
            tempList.addAll(getJunkTypeList(list.get(i)));
        }

        return tempList;
    }

    private List<String> getJunkTypeList(MultiItemEntity entity) {
        List<JunkProcessInfo> appCacheList = ((JunkType) entity).getSubItems();
        List<String> tempList = new ArrayList<>();

        //appCacheList可能为空
        if (appCacheList != null) {
            for (JunkProcessInfo info : appCacheList) {
                if (info.isCheck()) {
                    tempList.add(info.getJunkInfo().getPath());
                }
            }
        }
        return tempList;
    }

    private Set<String> getJunkProcessSet(MultiItemEntity entity) {
        List<JunkProcessInfo> appCacheList = ((JunkType) entity).getSubItems();
        Set<String> tempSet = new HashSet<>();
        if (appCacheList != null) {
            for (JunkProcessInfo info : appCacheList) {
                if (info.isCheck()) {
                    tempSet.add(info.getAppProcessInfo().getProcessName());
                }
            }
        }
        return tempSet;
    }

    private String getFilterJunkSize(ArrayList<JunkInfo> list) {

        long size = 0L;
        for (JunkInfo info : list) {
            size += info.getSize();
        }
        return FormatUtil.formatFileSize(size).toString();
    }

    @Override
    public void initAdapterData() {
        ArrayList<MultiItemEntity> list = new ArrayList<>();
        int title[] = {R.string.running_app, R.string.cache_junk, R.string.unuseful_apk,
                R.string.temp_file, R.string.log, R.string.big_file};
        int resourceId[] = {R.drawable.icon_process_white_24dp, R.drawable.icon_cache_white_24dp,
                R.drawable.icon_apk_white_24dp, R.drawable.icon_temp_file_white_24dp,
                R.drawable.icon_log_white_24dp, R.drawable.icon_big_file_white_24dp};
        for (int i = 0; i < 6; i++) {
            JunkType junkType = new JunkType();
            junkType.setTitle(mContext.getString(title[i]))
                    .setCheck(true)
                    .setIconResourceId(resourceId[i])
                    .setTotalSize("")
                    .setProgressVisible(true);
            if (JunkType.BIG_FILE == i || JunkType.CACHE == i) {
                junkType.setCheck(false);
            }
            list.add(junkType);
        }

        mView.setAdapterData(list);
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
        //可能还没扫描完就退出了界面。任务还在后台跑呢。。。。
        mScanManger.cancelScanTask();
    }
}
