package com.jiepier.filemanager.manager;

import android.content.Context;
import android.os.AsyncTask;

import com.jiepier.filemanager.bean.AppProcessInfo;
import com.jiepier.filemanager.bean.JunkGroup;
import com.jiepier.filemanager.bean.JunkInfo;
import com.jiepier.filemanager.bean.JunkProcessInfo;
import com.jiepier.filemanager.bean.JunkType;
import com.jiepier.filemanager.task.OverScanTask;
import com.jiepier.filemanager.task.SysCacheScanTask;
import com.jiepier.filemanager.task.callback.IScanCallBack;
import com.jiepier.filemanager.task.callback.ISysScanCallBack;

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
    private ArrayList<JunkInfo> mApkList;
    private ArrayList<JunkInfo> mLogList;
    private ArrayList<JunkInfo> mTempList;
    private ArrayList<JunkInfo> mBigFileList;
    private ArrayList<JunkInfo> mSysCacheList;
    private ArrayList<AppProcessInfo> mProcessList;
    private JunkGroup mJunkGroup;
    private boolean mIsOverScanFinish;
    private boolean mIsSystemScanFinish;
    private boolean mIsProcessScanFinsh;
    private IScanListener mScanListener;
    private Context mContext;

    private ScanManager(Context context) {

        mContext = context.getApplicationContext();
        mApkList = new ArrayList<>();
        mLogList = new ArrayList<>();
        mTempList = new ArrayList<>();
        mBigFileList = new ArrayList<>();
        mSysCacheList = new ArrayList<>();
        mProcessList = new ArrayList<>();
        mJunkGroup = new JunkGroup();
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
            public void onCancel() {

            }

            @Override
            public void onFinish(ArrayList<JunkInfo> apkList, ArrayList<JunkInfo> logList, ArrayList<JunkInfo> tempList, ArrayList<JunkInfo> bigFileList) {
                mIsOverScanFinish = true;
                mApkList = apkList;
                mLogList = logList;
                mTempList = tempList;
                mBigFileList = bigFileList;

                if (mScanListener != null) {
                    mScanListener.isOverScanFinish(mApkList, mLogList, mTempList, mBigFileList);
                    checkAllScanFinish();
                }
            }

            @Override
            public void onOverTime() {
                cancelScanTask();
                mIsOverScanFinish = true;
                checkAllScanFinish();
            }
        });
        mOverScanTask.execute();

        mSysCacheScanTask = new SysCacheScanTask(new ISysScanCallBack() {
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
            public void onCancel() {

            }

            @Override
            public void onFinish(ArrayList<JunkInfo> children) {
                mIsSystemScanFinish = true;
                mSysCacheList = children;
                if (mScanListener != null) {
                    mScanListener.isSysCacheScanFinish(children);
                    checkAllScanFinish();
                }
            }

            @Override
            public void onOverTime() {
                cancelScanTask();
                mIsSystemScanFinish = true;
                checkAllScanFinish();
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
                        mScanListener.isProcessScanFinish(mProcessList);
                        checkAllScanFinish();
                    }
                });
    }

    public void setScanListener(IScanListener listener) {
        this.mScanListener = listener;
    }

    private void checkAllScanFinish() {
        if (mIsOverScanFinish && mIsSystemScanFinish && mIsProcessScanFinsh) {

            //添加进程list
            ArrayList<JunkProcessInfo> list = new ArrayList<>();
            for (AppProcessInfo info : mProcessList) {
                JunkProcessInfo junkProcessInfo = new JunkProcessInfo(info);
                list.add(junkProcessInfo);
            }
            mJunkGroup.setProcessList(list);

            /*ArrayList<JunkProcessInfo> mJunkSysCacheList = getJunkProcessList(mSysCacheList, JunkType.CACHE);
            ArrayList<JunkProcessInfo> mJunkApkCacheList = getJunkProcessList(mApkList, JunkType.APK);
            ArrayList<JunkProcessInfo> mJunkLogCacheList = getJunkProcessList(mLogList, JunkType.LOG);
            ArrayList<JunkProcessInfo> mJunkTempCacheList = getJunkProcessList(mTempList, JunkType.TEMP);
            ArrayList<JunkProcessInfo> mJunkBigFileCacheList = getJunkProcessList(mBigFileList, JunkType.BIG_FILE);
            mJunkGroup.setSysCacheList(mJunkSysCacheList)
                    .setApkList(mJunkApkCacheList)
                    .setLogList(mJunkLogCacheList)
                    .setTempList(mJunkTempCacheList)
                    .setBigFileList(mJunkBigFileCacheList);*/
            mJunkGroup.setSysCacheList(getJunkProcessList(mSysCacheList, JunkType.CACHE))
                    .setApkList(getJunkProcessList(mApkList, JunkType.APK))
                    .setLogList(getJunkProcessList(mLogList, JunkType.LOG))
                    .setTempList(getJunkProcessList(mTempList, JunkType.TEMP))
                    .setBigFileList(getJunkProcessList(mBigFileList, JunkType.BIG_FILE));
            mScanListener.isAllScanFinish(mJunkGroup);

        }
    }

    private ArrayList<JunkProcessInfo> getJunkProcessList(ArrayList<JunkInfo> list, int type) {

        ArrayList<JunkProcessInfo> tempList = new ArrayList<>();
        for (JunkInfo junkInfo : list) {
            for (int i = 0; i < junkInfo.getChildren().size(); i++) {
                JunkInfo info = junkInfo.getChildren().get(i);
                JunkProcessInfo junkProcessInfo = new JunkProcessInfo(info, type);
                tempList.add(junkProcessInfo);
            }
        }
        return tempList;
    }

    public void cancelScanTask() {
        //判断当前的异步任务是否为空，并且判断当前的异步任务的状态是否是运行状态{RUNNING(运行),PENDING(准备),FINISHED(完成)}
        if (mOverScanTask != null && mOverScanTask.getStatus() == AsyncTask.Status.RUNNING) {
            /**
             *cancel(true) 取消当前的异步任务，传入的true,表示当中断异步任务时继续已经运行的线程的操作，
             *但是为了线程的安全一般为让它继续设为true
             * */
            mOverScanTask.cancel(true);
        }

        if (mSysCacheScanTask != null && mSysCacheScanTask.getStatus() == AsyncTask.Status.RUNNING) {
            mSysCacheScanTask.cancel(true);
        }
    }

    public interface IScanListener {

        void startScan();

        void isOverScanFinish(ArrayList<JunkInfo> apkList, ArrayList<JunkInfo> logList, ArrayList<JunkInfo> tempList, ArrayList<JunkInfo> bigFileList);

        void isSysCacheScanFinish(ArrayList<JunkInfo> sysCacheList);

        void isProcessScanFinish(ArrayList<AppProcessInfo> processList);

        void isAllScanFinish(JunkGroup junkGroup);

        void currentOverScanJunk(JunkInfo junkInfo);

        void currentSysCacheScanJunk(JunkInfo junkInfo);
    }
}
