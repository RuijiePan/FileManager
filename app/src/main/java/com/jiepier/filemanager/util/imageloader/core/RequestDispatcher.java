/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 bboyfeiyu@gmail.com ( Mr.Simple )
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.jiepier.filemanager.util.imageloader.core;

import android.util.Log;

import com.jiepier.filemanager.util.imageloader.loader.Loader;
import com.jiepier.filemanager.util.imageloader.loader.LoaderManager;
import com.jiepier.filemanager.util.imageloader.request.BitmapRequest;

import java.util.concurrent.BlockingQueue;

/**
 * 网络请求Dispatcher,继承自Thread,从网络请求队列中循环读取请求并且执行
 * 
 * @author mrsimple
 */
final class RequestDispatcher extends Thread {

    /**
     * 网络请求队列
     */
    private BlockingQueue<BitmapRequest> mRequestQueue;

    /**
     * @param queue
     */
    public RequestDispatcher(BlockingQueue<BitmapRequest> queue) {
        mRequestQueue = queue;
    }

    @Override
    public void run() {
        try {
            while (!this.isInterrupted()) {
                final BitmapRequest request = mRequestQueue.take();
                if (request.isCancel) {
                    continue;
                }

                final String schema = parseSchema(request.imageUri);
                Loader imageLoader = LoaderManager.getInstance().getLoader(schema);
                imageLoader.loadImage(request);
            }
        } catch (InterruptedException e) {
            Log.i("", "### 请求分发器退出");
        }
    }

    private String parseSchema(String uri) {
        if (uri.contains("://")) {
            return uri.split("://")[0];
        } else {
            Log.e(getName(), "### wrong scheme, image uri is : " + uri);
        }

        return "";
    }

}
