package com.jiepier.filemanager.preview;

import android.graphics.Bitmap;
import android.util.LruCache;

public final class BitmapLruCache<T> extends LruCache<T, Bitmap> {

    public BitmapLruCache() {
        super(512 * 1024);
    }

    @Override
    protected int sizeOf(T key, Bitmap value) {
        return value.getByteCount() / 1024;
    }
}
