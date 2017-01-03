package com.jiepier.filemanager.ui.actionmode;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.blankj.utilcode.utils.FileUtils;
import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.App;
import com.jiepier.filemanager.base.BasePresenter;
import com.jiepier.filemanager.base.BaseView;
import com.jiepier.filemanager.event.ActionChoiceFolderEvent;
import com.jiepier.filemanager.event.ActionModeTitleEvent;
import com.jiepier.filemanager.event.ActionMutipeChoiceEvent;
import com.jiepier.filemanager.event.AllChoiceEvent;
import com.jiepier.filemanager.event.ChangeThemeEvent;
import com.jiepier.filemanager.event.ChoiceFolderEvent;
import com.jiepier.filemanager.event.CleanActionModeEvent;
import com.jiepier.filemanager.event.CleanChoiceEvent;
import com.jiepier.filemanager.event.MutipeChoiceEvent;
import com.jiepier.filemanager.event.RefreshEvent;
import com.jiepier.filemanager.event.TypeEvent;
import com.jiepier.filemanager.task.PasteTaskExecutor;
import com.jiepier.filemanager.util.ClipBoard;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.RxBus.RxBus;
import com.jiepier.filemanager.util.Settings;
import com.jiepier.filemanager.util.UUIDUtil;
import com.jiepier.filemanager.widget.DeleteFilesDialog;
import com.jiepier.filemanager.widget.RenameDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by panruijie on 17/1/3.
 * Email : zquprj@gmail.com
 */

public class ActionModePresenter implements ActionModeContact.Presenter {

    private ActionModeContact.View mView;
    private CompositeSubscription mCompositeSubscription;
    private String[] mFiles;
    private List<String> mList;
    private Context mContext;
    private static final String ZIP = "zip";
    private static final String UNZIP = "unzip";
    private String unZipPath = "";

    public ActionModePresenter(Context context){

        mContext = context;
        mCompositeSubscription = new CompositeSubscription();

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(ActionMutipeChoiceEvent.class)
                .subscribe(event -> {

                    mList = event.getList();
                    mFiles = new String[mList.size()];
                    for (int i=0;i<mFiles.length;i++){
                        mFiles[i] = mList.get(i);
                    }

                    mView.cretaeActionMode();

                    final String mSelected = context.getString(R.string._selected);
                    mView.setActionModeTitle(mList.size()+mSelected);

                    if (mList.size() == 0)
                        mView.finishActionMode();

                }, Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(TypeEvent.class)
                .subscribe(event -> {
                    if (event.getType() == AppConstant.CLEAN_ACTIONMODE)
                        mView.finishActionMode();
                    else if (event.getType() == AppConstant.CLEAN_CHOICE)
                        mView.finishActionMode();
                }, Throwable::printStackTrace));

        mCompositeSubscription.add(RxBus.getDefault()
                .IoToUiObservable(ActionChoiceFolderEvent.class)
                .subscribe(event -> {
                    unZipPath = event.getFilePath();
                    mView.showFolderDialog(UNZIP);
                }, Throwable::printStackTrace));

    }

    @Override
    public void clickMove() {
        ClipBoard.cutMove(mFiles);
        mView.showFolderDialog(AppConstant.PATH);
    }

    @Override
    public void clickCopy() {
        ClipBoard.cutCopy(mFiles);
        mView.showFolderDialog(AppConstant.PATH);
    }

    @Override
    public void clickDelete() {
        DialogFragment deleteDialog = DeleteFilesDialog.instantiate(mFiles);
        mView.showDialog(deleteDialog);
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
        RxBus.getDefault().post(new TypeEvent(AppConstant.CLEAN_ACTIONMODE));
    }

    @Override
    public void clickZip(String currentPath) {
        mView.showFolderDialog(ZIP);
    }

    @Override
    public void clickRename() {
        DialogFragment renameDialog = RenameDialog.instantiate(
                FileUtils.getDirName(mFiles[0]),mList.get(0));
        mView.showDialog(renameDialog);
        RxBus.getDefault().post(new TypeEvent(AppConstant.CLEAN_ACTIONMODE));
    }

    @Override
    public void clickSetlectAll() {
        RxBus.getDefault().post(new TypeEvent(AppConstant.SELECT_ALL));
    }

    @Override
    public void folderSelect(FolderChooserDialog dialog, @NonNull File folder) {
        String tag = dialog.getTag();
        if (tag.equals(ZIP)) {
            mView.startZipTask(folder.getAbsolutePath() + File.separator + UUIDUtil.createUUID() + ".zip",
                    mFiles);
        }else if (tag.equals(UNZIP)){
            mView.startUnZipTask(new File(unZipPath),folder);
        }else if (tag.equals(AppConstant.PATH)) {
            mView.executePaste(folder.getAbsolutePath());
        }
        mView.finishActionMode();
        RxBus.getDefault().post(new TypeEvent(AppConstant.CLEAN_ACTIONMODE));
        RxBus.getDefault().post(new TypeEvent(AppConstant.CLEAN_CHOICE));
        RxBus.getDefault().post(new TypeEvent(AppConstant.REFRESH));
    }

    @Override
    public int getSlectItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void attachView(@NonNull ActionModeContact.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        if (mCompositeSubscription != null)
            mCompositeSubscription.unsubscribe();
        mCompositeSubscription = null;
    }
}
