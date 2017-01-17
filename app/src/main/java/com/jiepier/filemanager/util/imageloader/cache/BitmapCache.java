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

package com.jiepier.filemanager.util.imageloader.cache;

import android.graphics.Bitmap;

import com.jiepier.filemanager.util.imageloader.request.BitmapRequest;


/**
 * 图片缓存抽象类,具体的子类有不使用缓存{@see NoCache}、内存缓存{@see MemoryCache}、sd卡缓存{@see
 * DiskCache}以及内存和sd卡双缓存{@see DoubleCache}
 * 
 * @author mrsimple
 */
/**
 * 请求缓存接口
 * 
 * @author mrsimple
 * @param <K> key的类型
 * @param <V> value类型
 */
public interface BitmapCache {

    public Bitmap get(BitmapRequest key);

    public void put(BitmapRequest key, Bitmap value);

    public void remove(BitmapRequest key);

}
