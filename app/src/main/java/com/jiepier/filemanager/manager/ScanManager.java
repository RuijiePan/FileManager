package com.jiepier.filemanager.manager;

import android.content.Context;

import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.bean.JunkInfo;
import com.jiepier.filemanager.task.OverScanTask;
import com.jiepier.filemanager.task.SysCacheScanTask;
import com.jiepier.filemanager.task.callback.IScanCallBack;

import java.util.ArrayList;

import rx.schedulers.Schedulers;

/**
 * Created by panruijie on 2017/2/14.
 * Email : zquprj@gmail.com
 * 手机扫描管理类：系统缓存扫描、进程扫描、其他扫描
 */

public class ScanManager {

    private static ScanManager sInstance;
    private OverScanTask mOverScanTask;
    private SysCacheScanTask mSysCacheScanTask;
    private ProcessManager mProcessManager;
    private ArrayList<JunkInfo> mOverList;
    private ArrayList<JunkInfo> mSysCacheList;
    private ArrayList<AppProcessInfo> mProcessList;
    private boolean mIsOverScanFinish;
    private boolean mIsSystemScanFinish;
    private boolean mIsProcessScanFinsh;
    private IScanListener mScanListener;
    private Context mContext;

    private ScanManager(Context context) {

        mContext = context.getApplicationContext();
        mOverList = new ArrayList<>();
        mSysCacheList = new ArrayList<>();
        mProcessList = new ArrayList<>();
    }

    public static void init(Context context) {

        if (sInstance == null) {
            synchronized (ScanManager.class) {
                if (sInstance == null) {
                    sInstance = new ScanManager(context);
                }
            }
        }
    }

    public static ScanManager getInstance() {

        if (sInstance == null) {
            throw new IllegalStateException("You must be init CleanManager first");
        }
        return sInstance;
    }

    public void startScanTask() {

        if (mScanListener != null) {
            mScanListener.startScan();
        }

        mOverScanTask = new OverScanTask(new IScanCallBack() {
            @Override
            public void onBegin() {
                mIsOverScanFinish = false;
            }

            @Override
            public void onProgress(JunkInfo junkInfo) {
                if (mScanListener != null) {
                    mScanListener.currentOverScanJunk(junkInfo);
                }
            }

            @Override
            public void onFinish(ArrayList<JunkInfo> children) {
                mIsOverScanFinish = true;
                mOverList = children;
                if (mScanListener != null) {
                    mScanListener.isOverScanFinish();
                    checkAllScanFinish();
                }
            }
        });
        mOverScanTask.execute();

        mSysCacheScanTask = new SysCacheScanTask(new IScanCallBack() {
            @Override
            public void onBegin() {
                mIsSystemScanFinish = false;
            }

            @Override
            public void onProgress(JunkInfo junkInfo) {
                if (mScanListener != null) {
                    mScanListener.currentSysCacheScanJunk(junkInfo);
                }
            }

            @Override
            public void onFinish(ArrayList<JunkInfo> children) {
                mIsOverScanFinish = true;
                mSysCacheList = children;
                if (mScanListener != null) {
                    mScanListener.isSysCacheScanFinish();
                    checkAllScanFinish();
                }
            }
        });
        mSysCacheScanTask.execute();

        mIsProcessScanFinsh = false;
        mProcessManager = ProcessManager.getInstance();
        mProcessManager.getRunningAppListUsingObservable()
                .observeOn(Schedulers.io())
                .subscribe(appProcessInfos -> {
                    mIsProcessScanFinsh = true;
                    mProcessList = (ArrayList<AppProcessInfo>) appProcessInfos;
                    if (mScanListener != null) {
                        mScanListener.isProcessScanFinish();
                        checkAllScanFinish();
                    }
                });
    }

    public void setScanListener(IScanListener listener) {
        this.mScanListener = listener;
    }

    private void checkAllScanFinish() {
        if (mIsOverScanFinish && mIsSystemScanFinish && mIsProcessScanFinsh) {
            mScanListener.isAllScanFinish(mOverList, mSysCacheList, mProcessList);
        }
    }

    public interface IScanListener {

        void startScan();

        void isOverScanFinish();

        void isSysCacheScanFinish();

        void isProcessScanFinish();

        void isAllScanFinish(ArrayList<JunkInfo> overList, ArrayList<JunkInfo> sysCacheList, ArrayList<AppProcessInfo> mProcessInfo);

        void currentOverScanJunk(JunkInfo junkInfo);

        void currentSysCacheScanJunk(JunkInfo junkInfo);
    }
}
