package com.jiepier.filemanager.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.utils.FileUtils;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.task.RenameTask;

import java.io.File;


public final class RenameDialog extends DialogFragment {

    private static String mFilePath;
    private static String mParentPath;

    public static DialogFragment instantiate(String parentPath,String filePath) {
        mParentPath = parentPath;
        mFilePath = filePath;
        return new RenameDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle state) {
        final Activity a = getActivity();
        final String fileName = FileUtils.getFileName(mFilePath);

        MaterialDialog dialog = new MaterialDialog.Builder(a)
                .title(R.string.rename)
                .input(getString(R.string.enter_name), fileName, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        RenameTask task = new RenameTask(a,mParentPath);
                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                fileName, input.toString());
                    }
                })
                .positiveText(getString(R.string.ok))
                .negativeText(R.string.cancel)
                .build();

        return dialog;
    }
}
