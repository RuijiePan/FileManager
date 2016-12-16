package com.jiepier.filemanager.ui.main;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseDrawerActivity;
import com.jiepier.filemanager.event.CleanActionModeEvent;
import com.jiepier.filemanager.event.CleanChoiceEvent;
import com.jiepier.filemanager.event.MutipeChoiceEvent;
import com.jiepier.filemanager.task.PasteTaskExecutor;
import com.jiepier.filemanager.util.ClipBoard;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.ResourceUtil;
import com.jiepier.filemanager.util.RxBus;
import com.jiepier.filemanager.util.Settings;
import com.jiepier.filemanager.util.StatusBarUtil;
import com.jiepier.filemanager.util.ToastUtil;
import com.jiepier.filemanager.widget.DirectoryInfoDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.R.attr.mode;

public class MainActivity extends BaseDrawerActivity implements ActionMode.Callback{

    private ActionMode mActionMode;
    private String[] mFiles;
    private List<String> mList;

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
        if (!Settings.rootAccess())
            menu.removeItem(R.id.actiongroupowner);

        if (mFiles.length > 1) {
            menu.removeItem(R.id.actionrename);
            menu.removeItem(R.id.actiongroupowner);
            menu.removeItem(R.id.actiondetails);
        }
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.actionmove:
                ClipBoard.cutMove(mFiles);
                actionMode.finish();
                invalidateOptionsMenu();
                RxBus.getDefault().post(new CleanChoiceEvent());
                return true;
            case R.id.actioncopy:
                ClipBoard.cutCopy(mFiles);
                actionMode.finish();
                invalidateOptionsMenu();
                RxBus.getDefault().post(new CleanChoiceEvent());
                return true;
            case R.id.actiongroupowner:
                return true;
            case R.id.actiondelete:
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
                actionMode.finish();
                startActivity(Intent.createChooser(intent, getString(R.string.share)));
                return true;
            case R.id.actionshortcut:
                actionMode.finish();
                return true;
            case R.id.actionbookmark:
                actionMode.finish();
                return true;
            case R.id.actionzip:
                actionMode.finish();
                return true;
            case R.id.actionrename:
                return true;
            case R.id.actiondetails:
                return true;
            case R.id.actionall:
                actionMode.invalidate();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        this.mActionMode = null;
    }

}

