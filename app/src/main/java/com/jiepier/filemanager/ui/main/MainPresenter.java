package com.jiepier.filemanager.ui.main;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.event.ActionModeTitleEvent;
import com.jiepier.filemanager.event.AllChoiceEvent;
import com.jiepier.filemanager.event.ChangeThemeEvent;
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
import com.jiepier.filemanager.util.RxBus.RxBus;
import com.jiepier.filemanager.util.StatusBarUtil;
import com.jiepier.filemanager.util.UUIDUtil;
import com.jiepier.filemanager.widget.DeleteFilesDialog;
import com.jiepier.filemanager.widget.DirectoryInfoDialog;
import com.jiepier.filemanager.widget.RenameDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by JiePier on 16/12/21.
 */

public class MainPresenter implements MainContact.Presenter {

    private CompositeSubscription mCompositeSubscription;
    private MainContact.View mView;
    private String[] mFiles;
    private List<String> mList;
    public static final String ZIP = "zip";
    public static final String UNZIP = "unzip";
    private String unZipPath = "";

    public MainPresenter(Context context){
        this.mCompositeSubscription = new CompositeSubscription();

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(MutipeChoiceEvent.class)
                .subscribe(mutipeChoiceEvent -> {

                    mList = mutipeChoiceEvent.getList();
                    mFiles = new String[mList.size()];
                    for (int i=0;i<mFiles.length;i++){
                        mFiles[i] = mList.get(i);
                    }

                    mView.cretaeActionMode();

                    final String mSelected = context.getString(R.string._selected);
                    mView.setActionModeTitle(mList.size()+mSelected);

                    if (mList.size() == 0)
                        mView.finishActionMode();

                    mView.setChoiceCount(mList.size());
                }, Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(CleanActionModeEvent.class)
                .subscribe(event -> {
                    mView.finishActionMode();
                }, Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(ChoiceFolderEvent.class)
                .subscribe(event -> {
                    unZipPath = event.getFilePath();
                    mView.showFolderDialog(UNZIP);
                }, Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(ChangeThemeEvent.class)
                .subscribe(event-> {
                    mView.reload();
                },Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(ActionModeTitleEvent.class)
                .subscribe(event-> {
                    final String mSelected = context.getString(R.string._selected);
                    mView.setActionModeTitle(event.getCount()+mSelected);
                },Throwable::printStackTrace));
    }

    @Override
    public void attachView(@NonNull MainContact.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        if (mCompositeSubscription.isUnsubscribed())
            this.mCompositeSubscription.unsubscribe();
        this.mCompositeSubscription = null;
    }

    @Override
    public void clickCancel() {
        ClipBoard.clear();
        mView.refreshMenu();
    }

    @Override
    public void clickFloderInfo(String currentPath) {
        DirectoryInfoDialog dialog = DirectoryInfoDialog.create(currentPath);
        mView.showDialog(dialog);
    }

    @Override
    public void clickMove() {
        ClipBoard.cutMove(mFiles);
        mView.finishActionMode();
        mView.refreshMenu();
        RxBus.getDefault().post(new CleanChoiceEvent());
    }

    @Override
    public void clickCopy() {
        ClipBoard.cutCopy(mFiles);
        mView.finishActionMode();
        mView.refreshMenu();
        RxBus.getDefault().post(new CleanChoiceEvent());
    }

    @Override
    public void clickDelete() {
        DialogFragment deleteDialog = DeleteFilesDialog.instantiate(mFiles);
        mView.showDialog(deleteDialog);
        RxBus.getDefault().post(new CleanActionModeEvent());
    }

    @Override
    public void clickShare() {
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
        mView.startShareActivity(intent);
        RxBus.getDefault().post(new CleanActionModeEvent());
    }

    @Override
    public void clickShortCut() {
        mView.createShortCut(mFiles);
        RxBus.getDefault().post(new CleanActionModeEvent());
    }

    @Override
    public void clickZip(String currentPath) {
        mView.showFolderDialog(ZIP);
    }

    @Override
    public void clickRename(String currentPath) {
        Log.w("hahaMain",currentPath);
        DialogFragment renameDialog = RenameDialog.instantiate(currentPath,mList.get(0));
        mView.showDialog(renameDialog);
        RxBus.getDefault().post(new CleanActionModeEvent());
    }

    @Override
    public void clickSelectAll(String currentPath) {
        RxBus.getDefault().post(new AllChoiceEvent(currentPath));
    }

    @Override
    public void folderSelect(FolderChooserDialog dialog, @NonNull File folder) {

        String tag = dialog.getTag();
        if (tag.equals(ZIP)) {
            mView.startZipTask(folder.getAbsolutePath() + File.separator + UUIDUtil.createUUID() + ".zip",
                    mFiles);
        }else {
            mView.startUnZipTask(new File(unZipPath),folder);
        }
        mView.finishActionMode();
        RxBus.getDefault().post(new CleanChoiceEvent());
        RxBus.getDefault().post(new RefreshEvent());
    }

}
