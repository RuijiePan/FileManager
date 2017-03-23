package com.jiepier.filemanager.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.constant.AppConstant;
import com.jiepier.filemanager.event.TypeEvent;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.MediaStoreUtils;
import com.jiepier.filemanager.util.RxBus.RxBus;
import com.jiepier.filemanager.util.ZipUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class UnzipTask extends AsyncTask<File, Void, List<String>> {

    private final WeakReference<Activity> activity;
    private MaterialDialog dialog;

    public UnzipTask(final Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
        final Activity activity = this.activity.get();

        if (activity != null) {
            this.dialog = new MaterialDialog.Builder(activity)
                    .progress(true,0)
                    .content(activity.getString(R.string.unzipping))
                    .cancelable(true)
                    .build();

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

        if (failed.isEmpty()) {
            Toast.makeText(activity, activity.getString(R.string.extractionsuccess),
                    Toast.LENGTH_SHORT).show();
            RxBus.getDefault().post(new TypeEvent(AppConstant.REFRESH));
        }

        if (activity != null && !failed.isEmpty()) {
            Toast.makeText(activity, activity.getString(R.string.cantopenfile),
                    Toast.LENGTH_SHORT).show();
            if (!activity.isFinishing()) {
                dialog.show();
            }
        }
    }
}
