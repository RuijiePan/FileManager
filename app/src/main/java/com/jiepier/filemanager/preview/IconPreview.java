package com.jiepier.filemanager.preview;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.preview.BitmapLruCache;
import com.jiepier.filemanager.preview.DrawableLruCache;
import com.jiepier.filemanager.preview.MimeTypes;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.Settings;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IconPreview {

    private static DrawableLruCache<String> mMimeTypeIconCache;
    private static BitmapLruCache<String> mBitmapCache;
    private static ExecutorService pool = Executors.newFixedThreadPool(6);
    private static final Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new ConcurrentHashMap<ImageView, String>());
    private static PackageManager pm;
    private static int mWidth;

    private static Context mContext;
    private static Resources mResources;

    public IconPreview(Activity activity) {
        mContext = activity;
        mWidth = (int) mContext.getResources().getDimension(R.dimen.item_height);
        mResources = activity.getResources();
        pm = mContext.getPackageManager();

        mMimeTypeIconCache = new DrawableLruCache<>();
        mBitmapCache = new BitmapLruCache<>();
    }

    public static void getFileIcon(File file, final ImageView icon) {
        if (Settings.showThumbnail() & isvalidMimeType(file)) {
            loadBitmap(file, icon);
        } else {
            loadFromRes(file, icon);
        }
    }

    private static boolean isvalidMimeType(File file) {
        boolean isImage = MimeTypes.isPicture(file);
        boolean isVideo = MimeTypes.isVideo(file);
        boolean isApk = file.getName().endsWith(".apk");

        return isImage || isVideo || isApk;
    }

    private static void loadFromRes(final File file, final ImageView icon) {
        Drawable mimeIcon = null;

        if (file != null && file.isDirectory()) {
            String[] files = file.list();
            if (file.canRead() && files != null && files.length > 0)
                mimeIcon = mResources.getDrawable(R.drawable.type_folder);
            else
                mimeIcon = mResources.getDrawable(R.drawable.type_folder_empty);
        } else if (file != null && file.isFile()) {
            final String fileExt = FileUtil.getExtension(file.getName());
            mimeIcon = mMimeTypeIconCache.get(fileExt);

            if (mimeIcon == null) {
                final int mimeIconId = MimeTypes.getIconForExt(fileExt);
                if (mimeIconId != 0) {
                    mimeIcon = mResources.getDrawable(mimeIconId);
                    mMimeTypeIconCache.put(fileExt, mimeIcon);
                }
            }
        }

        if (mimeIcon != null) {
            icon.setImageDrawable(mimeIcon);
        } else {
            // default icon
            icon.setImageResource(R.drawable.type_unknown);
        }
    }

    private static void loadBitmap(final File file, final ImageView imageView) {
        imageView.setTag(file.getAbsolutePath());
        imageViews.put(imageView, file.getAbsolutePath());
        Bitmap mimeIcon = getBitmapFromMemCache(file.getAbsolutePath());

        // check in UI thread, so no concurrency issues
        if (mimeIcon != null) {
            // Item loaded from cache
            imageView.setImageBitmap(mimeIcon);
        } else {
            // here you can set a placeholder
            imageView.setImageBitmap(null);

            // Create handler in UI thread
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    String tag = imageViews.get(imageView);
                    if (tag != null && tag.equals(file.getAbsolutePath()) && msg.obj != null) {
                        imageView.setImageBitmap((Bitmap) msg.obj);
                    }
                }
            };

            pool.submit(new Runnable() {
                public void run() {
                    Message message = Message.obtain();
                    message.obj = getPreview(file);

                    handler.sendMessage(message);
                }
            });
        }
    }

    public static Drawable getBitmapDrawableFromFile(File file) {
        if (isvalidMimeType(file)) {
            return new BitmapDrawable(mResources, getBitmapFromMemCache(file.getAbsolutePath()));
        } else {
            return null;
        }
    }

    private static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mBitmapCache.put(key, bitmap);
        }
    }

    private static Bitmap getBitmapFromMemCache(String key) {
        return mBitmapCache.get(key);
    }

    private static Bitmap getPreview(File file) {
        final boolean isImage = MimeTypes.isPicture(file);
        final boolean isVideo = MimeTypes.isVideo(file);
        final boolean isApk = file.getName().endsWith(".apk");
        Bitmap mBitmap = null;
        String path = file.getAbsolutePath();

        if (isImage) {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeFile(path, o);
            o.inJustDecodeBounds = false;

            if (o.outWidth != -1 && o.outHeight != -1) {
                final int originalSize = (o.outHeight > o.outWidth) ? o.outWidth
                        : o.outHeight;
                o.inSampleSize = originalSize / mWidth;
            }

            mBitmap = BitmapFactory.decodeFile(path, o);

            addBitmapToMemoryCache(path, mBitmap);
            return mBitmap;
        } else if (isVideo) {
            mBitmap = ThumbnailUtils.createVideoThumbnail(path,
                    MediaStore.Video.Thumbnails.MICRO_KIND);

            addBitmapToMemoryCache(path, mBitmap);
            return mBitmap;
        } else if (isApk) {
            final PackageInfo packageInfo = pm.getPackageArchiveInfo(path,
                    PackageManager.GET_ACTIVITIES);

            if (packageInfo != null) {
                final ApplicationInfo appInfo = packageInfo.applicationInfo;

                if (appInfo != null) {
                    appInfo.sourceDir = path;
                    appInfo.publicSourceDir = path;
                    final Drawable icon = appInfo.loadIcon(pm);

                    if (icon != null) {
                        mBitmap = ((BitmapDrawable) icon).getBitmap();
                    }
                }
            } else {
                // load apk icon from /res/drawable/..
                mBitmap = BitmapFactory.decodeResource(mContext.getResources(),
                        R.drawable.type_apk);
            }

            addBitmapToMemoryCache(path, mBitmap);
            return mBitmap;
        }
        return null;
    }

    public static void clearCache() {
        mMimeTypeIconCache.evictAll();
        mBitmapCache.evictAll();
    }
}