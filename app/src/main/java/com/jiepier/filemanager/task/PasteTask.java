package com.jiepier.filemanager.task;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.utils.FileUtils;
import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.event.RefreshEvent;
import com.jiepier.filemanager.event.TypeEvent;
import com.jiepier.filemanager.sqlite.SqlUtil;
import com.jiepier.filemanager.util.ClipBoard;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.MediaStoreUtils;
import com.jiepier.filemanager.util.RxBus.RxBus;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public final class PasteTask extends AsyncTask<String, Void, List<String>> {

    private final WeakReference<Activity> activity;

    private MaterialDialog dialog;

    private final File location;

    private boolean success = false;

    public PasteTask(final Activity activity, File currentDir) {
        this.activity = new WeakReference<>(activity);
        this.location = currentDir;
    }

    @Override
    protected void onPreExecute() {
        final Activity activity = this.activity.get();

        if (activity != null) {
            this.dialog = new MaterialDialog.Builder(activity).progress(true, 0).build();

            if (ClipBoard.isMove()) {
                this.dialog.setContent(activity.getString(R.string.moving));
            } else {
                this.dialog.setContent(activity.getString(R.string.copying));
            }

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
    protected List<String> doInBackground(String... content) {
        final List<String> failed = new ArrayList<>();
        final Activity activity = this.activity.get();
        ClipBoard.lock();

        for (String s : content) {
            String fileName = s.substring(s.lastIndexOf("/"), s.length());
            if (ClipBoard.isMove()) {
                FileUtil.moveToDirectory(new File(s), new File(location, fileName), activity);
                if (!new File(s).isDirectory()) {
                    SqlUtil.update(s, location + File.separator + fileName);
                }
                success = true;
            } else {
                if (new File(s).isDirectory()) {
                    success = FileUtils.copyDir(s, location + File.separator + fileName);
                } else {
                    success = FileUtils.copyFile(new File(s), new File(location, fileName));
                    SqlUtil.insert(location + File.separator + fileName);
                }
                //FileUtil.copyFile(new File(s), new File(location, fileName), activity);
            }
        }

        if (location.canRead()) {
            for (File file : location.listFiles()) {
                MediaStoreUtils.addFileToMediaStore(file.getPath(), activity);
            }
        }
        return failed;
    }

    @Override
    protected void onPostExecute(final List<String> failed) {
        super.onPostExecute(failed);
        this.finish(failed);
        RxBus.getDefault().post(new RefreshEvent());
        RxBus.getDefault().post(new TypeEvent(AppConstant.REFRESH));
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

        if (ClipBoard.isMove()) {
            if (success) {
                Toast.makeText(activity,
                        activity.getString(R.string.movesuccsess),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, activity.getString(R.string.movefail),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            if (success) {
                Toast.makeText(activity,
                        activity.getString(R.string.copysuccsess),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, activity.getString(R.string.copyfail),
                        Toast.LENGTH_SHORT).show();
            }
        }

        ClipBoard.unlock();
        ClipBoard.clear();
        activity.invalidateOptionsMenu();

        if (!failed.isEmpty()) {
            Toast.makeText(activity, activity.getString(R.string.cantopenfile),
                    Toast.LENGTH_SHORT).show();
            if (!activity.isFinishing()) {
                dialog.show();
            }
        }
    }

}
