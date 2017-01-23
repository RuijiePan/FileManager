package com.jiepier.filemanager.manager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.jiepier.filemanager.bean.ImageFolder;
import com.jiepier.filemanager.util.SortUtil;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by panruijie on 16/12/28.
 * Email : zquprj@gmail.com
 * 安装包管理
 */

public class PictureManager {

    private static PictureManager sInstance;
    private Context mContext;
    private ArrayList<ImageFolder> mPictureList;

    private PictureManager(Context context){
        this.mContext = context;
        mPictureList = new ArrayList<>();
    }

    public static void init(Context context){

        if (sInstance == null)
            sInstance = new PictureManager(context);
    }

    public static PictureManager getInstance(){

        if (sInstance == null){
            throw new IllegalStateException("You must be init PictureManager first");
        }
        return sInstance;
    }

    public ArrayList<ImageFolder> getPictureListBySort(SortUtil.SortMethod sort){

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Images.Media.MIME_TYPE + "=? or "
                + MediaStore.Images.Media.MIME_TYPE + "=?";
        String[] selectionArgs = new String[] { "image/jpeg", "image/png" };
        String sortOrder = SortUtil.buildSortOrder(sort);

        Cursor cursor = null;
        HashSet<String> dirPathSet = new HashSet<>();
        String firstImage = null;
        mPictureList.clear();

        try {
            cursor = mContext.getContentResolver().query(
                    uri,null,selection,selectionArgs,sortOrder);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        String path = cursor.getString(cursor.getColumnIndex(
                                MediaStore.Images.Media.DATA));

                        //获取文件夹下的文件夹路径和首张图片
                        if (firstImage == null)
                            firstImage = path;

                        File parentFile = new File(path).getParentFile();
                        if (parentFile == null)
                            continue;

                        String dirPath = parentFile.getAbsolutePath();
                        ImageFolder imageFolder = null;

                        if (dirPathSet.contains(dirPath)){
                            continue;
                        }else {
                            dirPathSet.add(dirPath);
                            imageFolder = new ImageFolder();
                            imageFolder.setDir(dirPath);
                            imageFolder.setFirstImagePath(path);
                        }

                        //获取特定文件夹下的照片张数
                        int picSize = parentFile.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File file, String filename) {
                                return filename.endsWith(".jpg")
                                        || filename.endsWith(".png")
                                        || filename.endsWith(".jpeg");
                            }
                        }).length;
                        imageFolder.setCount(picSize);
                        mPictureList.add(imageFolder);

                    } while (cursor.moveToNext());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null)
            cursor.close();
        }

        return mPictureList;
    }

    public Observable<ArrayList<ImageFolder>> getPictureListUsingObservable(SortUtil.SortMethod sort){

        return Observable.create(new Observable.OnSubscribe<ArrayList<ImageFolder>>(){

            @Override
            public void call(Subscriber<? super ArrayList<ImageFolder>> subscriber) {

                subscriber.onNext(getPictureListBySort(sort));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public List<ImageFolder> getPictureList(){
        return mPictureList;
    }
}
