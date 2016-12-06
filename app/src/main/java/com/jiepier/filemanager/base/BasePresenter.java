package com.jiepier.filemanager.base;

import android.support.annotation.NonNull;

/**
 * Created by JiePier on 16/11/12.
 */
public interface BasePresenter<T extends BaseView> {

  void attachView(@NonNull T view);

  void detachView();
}
