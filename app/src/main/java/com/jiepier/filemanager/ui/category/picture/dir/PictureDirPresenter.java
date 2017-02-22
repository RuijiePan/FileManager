package com.jiepier.filemanager.ui.category.picture.dir;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.jiepier.filemanager.ui.category.picture.detail.PictureDetailActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by panruijie on 17/1/19.
 * Email : zquprj@gmail.com
 */

public class PictureDirPresenter implements PictureDirContact.Presenter {

    private String TAG = getClass().getSimpleName();
    private CompositeSubscription mCompositeSubscription;
    private PictureDirContact.View mView;
    private File mDirFile;
    private String mPath;
    private List<String> mList;
    private Context mContext;

    public PictureDirPresenter(Context context, String path) {
        mContext = context;
        mPath = path;
        mDirFile = new File(path);
        mList = new ArrayList<>();
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getData() {

        mView.showDialog();

        mView.setDataUsingObservable(Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {

                mList = Arrays.asList(mDirFile.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.endsWith(".jpg") || filename.endsWith(".png")
                                || filename.endsWith(".jpeg")) {
                            return true;
                        }
                        return false;
                    }
                }));

                //对获取到对路径进行拼接
                for (int i = 0; i < mList.size(); i++) {
                    mList.set(i, mPath + File.separator + mList.get(i));
                }

                mView.setTotalCount(mList.size());
                mView.dimissDialog();

                Collections.reverse(mList);
                subscriber.onNext(mList);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()));

    }

    @Override
    public void onItemClick(int position, List<String> data) {
        Intent intent = new Intent(mContext, PictureDetailActivity.class);
        intent.putExtra(PictureDetailActivity.EXTRA_IMAGE_POSITION, position);

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            list.add(data.get(i));
        }
        intent.putStringArrayListExtra(PictureDetailActivity.EXTRA_IMAGE_URLS, list);
        mContext.startActivity(intent);
    }

    @Override
    public void attachView(@NonNull PictureDirContact.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        if (mCompositeSubscription.isUnsubscribed()) {
            this.mCompositeSubscription.unsubscribe();
        }
        this.mCompositeSubscription = null;
    }
}
