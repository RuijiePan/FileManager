package com.jiepier.filemanager.util.imageloader.core;

import android.util.Log;

import com.jiepier.filemanager.util.imageloader.request.BitmapRequest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by panruijie on 17/1/13.
 * Email : zquprj@gmail.com
 * 请求队列, 使用优先队列,使得请求可以按照优先级进行处理. [ Thread Safe ]
 */

public class RequestQueue {

    /**
     * 请求队列 [ Thread-safe ]
     */
    private BlockingQueue<BitmapRequest> mRequestQueue = new PriorityBlockingQueue<>();
    /**
     * 请求的序列化生成器
     */
    private AtomicInteger mSerialNumGenerator = new AtomicInteger(0);
    /**
     * 默认的核心数
     */
    public static int DEFAULT_CORE_NUMS = Runtime.getRuntime().availableProcessors() + 1;
    /**
     * CPU核心数 + 1个分发线程数
     */
    private int mDispatcherNums = DEFAULT_CORE_NUMS;
    /**
     * NetworkExecutor,执行网络请求的线程
     */
    private RequestDispatcher[] mDispatchers = null;

    /**
     *
     */
    protected RequestQueue() {
        this(DEFAULT_CORE_NUMS);
    }

    /**
     * @param coreNums 线程核心数
     * @param httpStack http执行器
     */
    protected RequestQueue(int coreNums) {
        mDispatcherNums = coreNums;
    }

    /**
     * 启动RequestDispatcher
     */
    private final void startDispatchers() {
        mDispatchers = new RequestDispatcher[mDispatcherNums];
        for (int i = 0; i < mDispatcherNums; i++) {
            Log.e("", "### 启动线程 " + i);
            mDispatchers[i] = new RequestDispatcher(mRequestQueue);
            mDispatchers[i].start();
        }
    }

    public void start() {
        stop();
        startDispatchers();
    }

    /**
     * 停止RequestDispatcher
     */
    public void stop() {
        if (mDispatchers != null && mDispatchers.length > 0) {
            for (int i = 0; i < mDispatchers.length; i++) {
                mDispatchers[i].interrupt();
            }
        }
    }

    /**
     * 不能重复添加请求
     *
     * @param request
     */
    public void addRequest(BitmapRequest request) {
        if (!mRequestQueue.contains(request)) {
            request.serialNum = this.generateSerialNumber();
            mRequestQueue.add(request);
        } else {
            Log.d("", "### 请求队列中已经含有");
        }
    }

    public void clear() {
        mRequestQueue.clear();
    }

    public BlockingQueue<BitmapRequest> getAllRequests() {
        return mRequestQueue;
    }

    /**
     * 为每个请求生成一个系列号
     *
     * @return 序列号
     */
    private int generateSerialNumber() {
        return mSerialNumGenerator.incrementAndGet();
    }

}
