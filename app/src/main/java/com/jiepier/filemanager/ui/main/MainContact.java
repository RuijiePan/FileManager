package com.jiepier.filemanager.ui.main;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.Menu;

import com.afollestad.materialdialogs.folderselector.FolderChooserDialog;
import com.jiepier.filemanager.base.BasePresenter;
import com.jiepier.filemanager.base.BaseView;
import com.jiepier.filemanager.widget.DirectoryInfoDialog;

import java.io.File;

/**
 * Created by JiePier on 16/12/21.
 */

public interface MainContact {

    interface View extends BaseView{

        void cretaeActionMode();

        void finishActionMode();

        void refreshMenu();

        void setActionModeTitle(String title);

        void createShortCut(String[] files);

        void showDialog(DialogFragment dialog);

        void showFolderDialog(String TAG);

        void startShareActivity(Intent intent);

        void reload();

        void setChoiceCount(int count);

        void startZipTask(String fileName,String[] files);

        void startUnZipTask(File unZipFile,File folder);
    }

    interface Presenter extends BasePresenter<View> {

        void clickCancel();

        void clickFloderInfo(String currentPath);

        void clickMove();

        void clickCopy();

        void clickDelete();

        void clickShare();

        void clickShortCut();

        void clickZip(String currentPath);

        void clickRename(String currentPath);

        void clickSelectAll(String currentPath);

        void folderSelect(FolderChooserDialog dialog, @NonNull File folder);

    }
}
