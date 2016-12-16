package com.jiepier.filemanager.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.MediaStoreUtils;
import com.jiepier.filemanager.util.ZipUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ExtractionTask extends AsyncTask<File, Void, List<String>> {

    private final WeakReference<Activity> activity;
    private ProgressDialog dialog;

    public ExtractionTask(final Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
        final Activity activity = this.activity.get();

        if (activity != null) {
            this.dialog = new ProgressDialog(activity);
            this.dialog.setMessage(activity.getString(R.string.unzipping));
            this.dialog.setCancelable(true);
            this.dialog
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            cancel(false);
                        }
                    });
            if (!activity.isFinishing()) {
                this.dialog.show();
            }
        }
    }

    @Override
    protected List<String> doInBackground(File... files) {
        final Activity activity = this.activity.get();
        final List<String> failed = new ArrayList<>();
        final String ext = FileUtil.getExtension(files[0].getName());

        try {
            if (ext.equals("zip")) {
                ZipUtils.unpackZip(files[0], files[1]);
            }
        } catch (Exception e) {
            failed.add(Arrays.toString(files));
        }

        if (files[1].canRead()) {
            for (File file : files[1].listFiles())
                MediaStoreUtils.addFileToMediaStore(file.getPath(), activity);
        }
        return failed;
    }

    @Override
    protected void onPostExecute(final List<String> failed) {
        super.onPostExecute(failed);
        this.finish(failed);
    }

    @Override
    protected void onCancelled(final List<String> failed) {
        super.onCancelled(failed);
        this.finish(failed);
    }

    private void finish(final List<String> failed) {
        if (this.dialog != null) {
            this.dialog.dismiss();
        }

        final Activity activity = this.activity.get();

        if (failed.isEmpty())
            Toast.makeText(activity, activity.getString(R.string.extractionsuccess),
                    Toast.LENGTH_SHORT).show();

        if (activity != null && !failed.isEmpty()) {
            Toast.makeText(activity, activity.getString(R.string.cantopenfile),
                    Toast.LENGTH_SHORT).show();
            if (!activity.isFinishing()) {
                dialog.show();
            }
        }
    }
}
