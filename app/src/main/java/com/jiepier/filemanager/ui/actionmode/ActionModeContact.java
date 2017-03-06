package com.jiepier.filemanager.ui.actionmode;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.jiepier.filemanager.base.BasePresenter;
import com.jiepier.filemanager.base.BaseView;

import java.io.File;

/**
 * Created by panruijie on 17/1/3.
 * Email : zquprj@gmail.com
 */

public class ActionModeContact {

    interface View extends BaseView {

        void cretaeActionMode();

        void finishActionMode();

        void refreshMenu();

        void executePaste(String path);

        void setActionModeTitle(String title);

        void showDialog(DialogFragment dialog);

        void showFolderDialog(String TAG);

        void startShareActivity(Intent intent);

        void startZipTask(String fileName, String[] files);

        void startUnZipTask(File unZipFile, File folder);
    }

    interface Presenter extends BasePresenter<View> {

        void clickMove();

        void clickCopy();

        void clickDelete();

        void clickShare();

        void clickZip(String currentPath);

        void clickRename();

        void clickSetlectAll();

        void folderSelect(FolderChooserDialog dialog, @NonNull File folder);

        int getSlectItemCount();
    }
}
