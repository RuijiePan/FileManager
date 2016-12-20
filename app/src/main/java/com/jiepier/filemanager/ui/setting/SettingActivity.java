package com.jiepier.filemanager.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseActivity;
import com.jiepier.filemanager.event.ChangeDefaultDirEvent;
import com.jiepier.filemanager.event.ChangeThemeEvent;
import com.jiepier.filemanager.event.NewDirEvent;
import com.jiepier.filemanager.util.RxBus.RxBus;
import com.jiepier.filemanager.util.Settings;

import java.io.File;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JiePier on 16/12/14.
 */

public class SettingActivity extends BaseActivity implements FolderChooserDialog.FolderCallback{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int initContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        if (null != toolbar) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.settings);
    }

    @Override
    public void initUiAndListener() {
        getFragmentManager().beginTransaction().replace(R.id.content, new SettingFragment()).commit();

        RxBus.getDefault().add(this,RxBus.getDefault()
                .IoToUiObservable(ChangeDefaultDirEvent.class)
                .subscribe(event -> {
                    new FolderChooserDialog.Builder(this)
                            .chooseButton(R.string.md_choose_label)  // changes label of the choose button
                            .initialPath(Settings.getDefaultDir())  // changes initial path, defaults to external storage directory
                            .cancelButton(R.string.cancel)
                            .goUpLabel("Up") // custom go up label, default label is "..."
                            .show();
                }, Throwable::printStackTrace));

        RxBus.getDefault().add(this,RxBus.getDefault()
                .IoToUiObservable(ChangeThemeEvent.class)
                .subscribe(event -> {
                    RxBus.getDefault().unsubscribeAll();
                    reload();
                }, Throwable::printStackTrace));

    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }

    @Override
    public void onFolderSelection(@NonNull FolderChooserDialog dialog, @NonNull File folder) {
        RxBus.getDefault().post(new NewDirEvent(folder.getAbsolutePath()));
    }

}

