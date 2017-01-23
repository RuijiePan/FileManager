package com.jiepier.filemanager.manager;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by panruijie on 17/1/20.
 * Email : zquprj@gmail.com
 * 手机运行内存管理类
 */

public class MemoryManager {

    public static MemoryManager sInstance;
    private Context mContext;
    private String TAG = getClass().getSimpleName();
    private ActivityManager mActivityManager;

    public static void init(Context context){

        if (sInstance == null)
            sInstance = new MemoryManager(context);
    }

    private MemoryManager(Context context){

        this.mContext = context;
        mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public static MemoryManager getInstance(){

        if (sInstance == null){
            throw new IllegalStateException("You must be init MemoryManager first");
        }

        return sInstance;
    }

    public long getTotalMemory(){

        long totalMemory = 0L;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        try {
            fileReader = new FileReader("/proc/meminfo");
            bufferedReader = new BufferedReader(fileReader,4096);

            String cat = bufferedReader.readLine();

            if (cat != null) {
                String[] arrays = cat.split("\\s+");
                /*for (String num : arrays){
                    Log.w(TAG,num);
                }*/

                if (arrays.length > 1){
                    totalMemory = Long.parseLong(arrays[1]);
                }
            }
        } catch (NumberFormatException e){
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (fileReader != null){
                try {
                    fileReader.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            if (bufferedReader != null){
                try {
                    bufferedReader.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        return totalMemory << 10;
    }

    public Observable<Long> getTotalMemoryUsingObservable(){

        return Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                subscriber.onNext(getTotalMemory());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public long getAvaliableMemory(){

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        mActivityManager.getMemoryInfo(memoryInfo);

        return memoryInfo.availMem ;
    }

    public Observable<Long> getAvaliableMemoryUsingObservable(){

        return Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                subscriber.onNext(getAvaliableMemory());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
