package com.jiepier.filemanager.ui.main;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseDrawerActivity;
import com.jiepier.filemanager.event.AllChoiceEvent;
import com.jiepier.filemanager.event.ChoiceFolderEvent;
import com.jiepier.filemanager.event.CleanActionModeEvent;
import com.jiepier.filemanager.event.CleanChoiceEvent;
import com.jiepier.filemanager.event.MutipeChoiceEvent;
import com.jiepier.filemanager.event.RefreshEvent;
import com.jiepier.filemanager.task.PasteTaskExecutor;
import com.jiepier.filemanager.task.UnzipTask;
import com.jiepier.filemanager.task.ZipTask;
import com.jiepier.filemanager.util.ClipBoard;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.ResourceUtil;
import com.jiepier.filemanager.util.RxBus;
import com.jiepier.filemanager.util.Settings;
import com.jiepier.filemanager.util.StatusBarUtil;
import com.jiepier.filemanager.util.UUIDUtil;
import com.jiepier.filemanager.widget.DeleteFilesDialog;
import com.jiepier.filemanager.widget.DirectoryInfoDialog;
import com.jiepier.filemanager.widget.RenameDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseDrawerActivity implements ActionMode.Callback,FolderChooserDialog.FolderCallback{

    private ActionMode mActionMode;
    private String[] mFiles;
    private List<String> mList;
    public static final String ZIP = "zip";
    public static final String UNZIP = "unzip";
    private String unZipPath = "";

    @Override
    public void initUiAndListener() {
        super.initUiAndListener();

        setNavigationClickListener(new NavigationClickListener() {
            @Override
            public void onClickSDCard() {

            }

            @Override
            public void onClickRoot() {

            }

            @Override
            public void onClickSystem() {

            }

            @Override
            public void onClickSetting() {

            }

            @Override
            public void onClickAbout() {

            }
        });


        RxBus.getDefault()
                .toObservable(MutipeChoiceEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mutipeChoiceEvent -> {

                    mList = mutipeChoiceEvent.getList();
                    mFiles = new String[mList.size()];
                    for (int i=0;i<mFiles.length;i++){
                        mFiles[i] = mList.get(i);
                    }

                    if (mActionMode == null){
                        mActionMode = startActionMode(MainActivity.this);
                        StatusBarUtil.setColor(this, ResourceUtil.getThemeColor(this), 0);
                    }

                    final String mSelected = getString(R.string._selected);
                    mActionMode.setTitle(mList.size()+mSelected);

                    if (mList.size() == 0)
                        mActionMode.finish();

                }, Throwable::printStackTrace);

        RxBus.getDefault()
                .toObservable(CleanActionModeEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (mActionMode != null)
                       mActionMode.finish();
                    invalidateOptionsMenu();
                }, Throwable::printStackTrace);

        RxBus.getDefault()
                .toObservable(ChoiceFolderEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    unZipPath = event.getFilePath();
                    new FolderChooserDialog.Builder(this)
                            .chooseButton(R.string.md_choose_label)  // changes label of the choose button
                            .initialPath(event.getParentPath())  // changes initial path, defaults to external storage directory
                            .tag(UNZIP)
                            .goUpLabel("Up") // custom go up label, default label is "..."
                            .show();
                }, Throwable::printStackTrace);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);

        menu.findItem(R.id.paste).setVisible(!ClipBoard.isEmpty());
        menu.findItem(R.id.cancel).setVisible(!ClipBoard.isEmpty());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.cancel:
                ClipBoard.clear();
                invalidateOptionsMenu();
                return true;
            case R.id.paste:
                PasteTaskExecutor ptc = new PasteTaskExecutor(this, mSdCardFragment.getCurrentPath());
                ptc.start();
                return true;
            case R.id.folderinfo:
                DirectoryInfoDialog dialog = DirectoryInfoDialog.create(mSdCardFragment.getPath());
                dialog.show(fm_v4,TAG_DIALOG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        this.mActionMode = actionMode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.actionmode,menu);

        if (mFiles.length > 1) {
            menu.removeItem(R.id.actionrename);
        }
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.actionmove:
                ClipBoard.cutMove(mFiles);
                RxBus.getDefault().post(new CleanActionModeEvent());
                RxBus.getDefault().post(new CleanChoiceEvent());
                return true;
            case R.id.actioncopy:
                ClipBoard.cutCopy(mFiles);
                RxBus.getDefault().post(new CleanActionModeEvent());
                RxBus.getDefault().post(new CleanChoiceEvent());
                return true;
            case R.id.actiondelete:
                DialogFragment deleteDialog = DeleteFilesDialog.instantiate(mFiles);
                deleteDialog.show(fm_v4,TAG_DIALOG);
                RxBus.getDefault().post(new CleanActionModeEvent());
                //删除文件
                return true;
            case R.id.actionshare:
                final ArrayList<Uri> uris = new ArrayList<>(mFiles.length);
                for (String mFile : mFiles) {
                    final File selected = new File(mFile);
                    if (!selected.isDirectory()) {
                        uris.add(Uri.fromFile(selected));
                    }
                }
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("*/*");
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                startActivity(Intent.createChooser(intent, getString(R.string.share)));
                RxBus.getDefault().post(new CleanActionModeEvent());
                return true;
            case R.id.actionshortcut:
                for (String file:mFiles){
                    FileUtil.createShortcut(this,file);
                }
                RxBus.getDefault().post(new CleanActionModeEvent());
                return true;
            case R.id.actionzip:
                new FolderChooserDialog.Builder(this)
                        .chooseButton(R.string.md_choose_label)  // changes label of the choose button
                        .initialPath(mSdCardFragment.getCurrentPath())  // changes initial path, defaults to external storage directory
                        .tag(ZIP)
                        .goUpLabel("Up") // custom go up label, default label is "..."
                        .show();
                return true;
            case R.id.actionrename:
                DialogFragment renameDialog = RenameDialog.instantiate(mSdCardFragment.getCurrentPath(),mList.get(0));
                renameDialog.show(fm_v4,TAG_DIALOG);
                RxBus.getDefault().post(new CleanActionModeEvent());
                return true;
            case R.id.actionall:
                //RxBus.getDefault().post(new CleanActionModeEvent());
                RxBus.getDefault().post(new AllChoiceEvent(mSdCardFragment.getCurrentPath()));
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        this.mActionMode = null;
        RxBus.getDefault().post(new CleanChoiceEvent());
    }

    @Override
    public void onFolderSelection(@NonNull FolderChooserDialog dialog, @NonNull File folder) {

        String tag = dialog.getTag();
        if (tag.equals(ZIP)) {
            final ZipTask task = new ZipTask(this,
                    //zip文件新名字
                    folder.getAbsolutePath() + File.separator + UUIDUtil.createUUID() + ".zip");
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mList.get(0));
        }else {
            UnzipTask task = new UnzipTask(this);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new File(unZipPath),folder);
        }
        RxBus.getDefault().post(new CleanActionModeEvent());
        RxBus.getDefault().post(new CleanChoiceEvent());
        RxBus.getDefault().post(new RefreshEvent());
    }
}

