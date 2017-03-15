package com.jiepier.filemanager.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseActivity;
import com.jiepier.filemanager.event.ChangeDefaultDirEvent;
import com.jiepier.filemanager.event.ChangeThemeEvent;
import com.jiepier.filemanager.event.LanguageEvent;
import com.jiepier.filemanager.event.NewDirEvent;
import com.jiepier.filemanager.util.AnimationUtil;
import com.jiepier.filemanager.util.RxBus.RxBus;
import com.jiepier.filemanager.util.Settings;

import java.io.File;

import butterknife.BindView;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by JiePier on 16/12/14.
 */

public class SettingActivity extends BaseActivity implements FolderChooserDialog.FolderCallback {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.content)
    FrameLayout mContentView;
    private CompositeSubscription mCompositeSubscription;

    @Override
    public int initContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        if (null != mToolbar) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle(R.string.settings);
    }

    @Override
    public void initUiAndListener() {
        getFragmentManager().beginTransaction().replace(R.id.content, new SettingFragment()).commit();

        AnimationUtil.showCircularReveal(mContentView, 0, 0, 2, 1500);

        mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(ChangeDefaultDirEvent.class)
                .map(ChangeDefaultDirEvent::getType)
                .subscribe(type -> {
                    new FolderChooserDialog.Builder(this)
                            .tag(type)
                            .chooseButton(R.string.md_choose_label)  // changes label of the choose button
                            .initialPath(Settings.getDefaultDir())  // changes initial path, defaults to external storage directory
                            .cancelButton(R.string.cancel)
                            .goUpLabel("Up") // custom go up label, default label is "..."
                            .show();
                }, Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(ChangeThemeEvent.class)
                .subscribe(event -> {
                    reload();
                }, Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(LanguageEvent.class)
                .subscribe(event -> {
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

        String type = dialog.getTag();
        RxBus.getDefault().post(new NewDirEvent(type, folder.getAbsolutePath()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription.isUnsubscribed()) {
            this.mCompositeSubscription.unsubscribe();
        }
        this.mCompositeSubscription = null;
    }
}

