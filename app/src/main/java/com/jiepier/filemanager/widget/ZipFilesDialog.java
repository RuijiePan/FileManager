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
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.ZipUtils;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.task.ZipTask;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.UUIDUtil;


import java.io.File;
import java.util.UUID;

public final class ZipFilesDialog extends DialogFragment {

    private static String[] files;
    private static File zipfile;

    public static DialogFragment instantiate(String[] files1,File floder1) {
        files = files1;
        zipfile = floder1;
        return new ZipFilesDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle state) {
        final Activity a = getActivity();

        String newPath = FileUtils.getFileName(zipfile)+File.separator+UUIDUtil.createUUID();

        final MaterialDialog dialog = new MaterialDialog.Builder(a)
                .progress(true,0)
                .title(R.string.zip)
                .positiveText(getString(R.string.ok))
                .onPositive((dialog1, which) -> {
                    final ZipTask task = new ZipTask(a, newPath);
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, files);
                })
                .negativeText(R.string.cancel)
                .build();

        return dialog;
    }
}
