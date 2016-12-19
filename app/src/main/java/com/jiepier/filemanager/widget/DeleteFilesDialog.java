package com.jiepier.filemanager.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.task.DeleteTask;


public final class DeleteFilesDialog extends DialogFragment {

    private static String[] files;

    public static DialogFragment instantiate(String[] files1) {
        files = files1;
        return new DeleteFilesDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle state) {
        final Activity a = getActivity();
        final int size = files.length;

        MaterialDialog dialog = new MaterialDialog.Builder(a)
                .title(String.valueOf(size) + getString(R.string._files))
                .content(R.string.cannotbeundoneareyousureyouwanttodelete)
                .positiveText(R.string.delete)
                .onPositive((dialog1, which) -> {
                    DeleteTask task = new DeleteTask(a);
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, files);
                }).negativeText(R.string.cancel)
                .onNegative((dialog12, which) -> dialog12.dismiss()).build();

        return dialog;
    }
}
