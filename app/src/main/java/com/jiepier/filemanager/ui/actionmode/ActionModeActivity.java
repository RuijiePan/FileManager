package com.jiepier.filemanager.ui.actionmode;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseActivity;
import com.jiepier.filemanager.constant.AppConstant;
import com.jiepier.filemanager.task.PasteTaskExecutor;
import com.jiepier.filemanager.task.UnzipTask;
import com.jiepier.filemanager.task.ZipTask;
import com.jiepier.filemanager.util.ResourceUtil;
import com.jiepier.filemanager.util.Settings;
import com.jiepier.filemanager.util.StatusBarUtil;

import java.io.File;

/**
 * Created by panruijie on 17/1/3.
 * Email : zquprj@gmail.com
 */

public abstract class ActionModeActivity extends BaseActivity implements
        ActionMode.Callback, ActionModeContact.View, FolderChooserDialog.FolderCallback {

    protected ActionMode mActionMode;
    protected ActionModePresenter mPresenter;

    @Override
    public void initUiAndListener() {
        mPresenter = new ActionModePresenter(this);
        mPresenter.attachView(this);

        init();
    }

    public abstract void init();

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mActionMode = mode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.actionmode, menu);
        if (mPresenter.getSlectItemCount() > 1) {
            menu.removeItem(R.id.actionrename);
        }

        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.actionmove:
                mPresenter.clickMove();
                return true;
            case R.id.actioncopy:
                mPresenter.clickCopy();
                return true;
            case R.id.actiondelete:
                mPresenter.clickDelete();
                return true;
            case R.id.actionshare:
                mPresenter.clickShare();
                return true;
            case R.id.actionzip:
                mPresenter.clickZip(Settings.getDefaultDir());
                return true;
            case R.id.actionrename:
                mPresenter.clickRename();
                return true;
            case R.id.actionall:
                mPresenter.clickSetlectAll();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        this.mActionMode = null;
    }

    @Override
    public void cretaeActionMode() {
        if (mActionMode == null) {
            mActionMode = startSupportActionMode(this);
            StatusBarUtil.setColor(this, ResourceUtil.getThemeColor(this), 0);
        }
    }

    @Override
    public void finishActionMode() {
        if (mActionMode != null) {
            mActionMode.finish();
        }
    }

    @Override
    public void refreshMenu() {
        invalidateOptionsMenu();
    }

    @Override
    public void setActionModeTitle(String title) {
        mActionMode.setTitle(title);
    }

    @Override
    public void showDialog(DialogFragment dialog) {
        dialog.show(getFragmentManager(), AppConstant.DIALOG_TAG);
    }

    @Override
    public void showFolderDialog(String TAG) {
        new FolderChooserDialog.Builder(this)
                .chooseButton(R.string.md_choose_label)  // changes label of the choose button
                .cancelButton(R.string.md_cancel_label)
                .initialPath(Settings.getDefaultDir())  // changes initial path, defaults to external storage directory
                .tag(TAG)
                .goUpLabel("Up") // custom go up label, default label is "..."
                .show();
    }

    @Override
    public void startShareActivity(Intent intent) {
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

    @Override
    public void startZipTask(String fileName, String[] files) {
        final ZipTask task = new ZipTask(this, fileName);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, files);
    }

    @Override
    public void startUnZipTask(File unZipFile, File folder) {
        UnzipTask task = new UnzipTask(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, unZipFile, folder);
    }

    @Override
    public void onFolderSelection(@NonNull FolderChooserDialog dialog, @NonNull File folder) {

        mPresenter.folderSelect(dialog, folder);
    }

    @Override
    public void executePaste(String path) {
        new PasteTaskExecutor(this, path).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}
