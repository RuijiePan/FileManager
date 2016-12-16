package com.jiepier.filemanager.task;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.util.ClipBoard;
import com.jiepier.filemanager.widget.FileExistsDialog;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by JiePier on 16/12/16.
 */

public class PasteTaskExecutor implements View.OnClickListener {

    private final WeakReference<Activity> mActivityReference;

    private final File mLocation;
    private final LinkedList<String> mToProcess;
    private final HashMap<String, String> mExisting;

    private String current;

    public PasteTaskExecutor(final Activity activity, final String location) {
        this.mActivityReference = new WeakReference<>(activity);
        this.mLocation = new File(location);
        this.mToProcess = new LinkedList<>();
        this.mExisting = new HashMap<>();
    }

    public void start(){
        final String[] contents = ClipBoard.getClipBoardContents();
        if (contents == null){
            return;
        }

        for (String path : contents){
            File file = new File(path);

            if (file.exists()){
                File oldFile = new File(mLocation,file.getName());

                if (oldFile.exists()){
                    mExisting.put(oldFile.getPath(),file.getPath());
                }else {
                    mToProcess.add(file.getPath());
                }
            }
        }

        next();
    }

    private void next() {
        Activity activity = mActivityReference.get();
        if (activity != null){
            if (mExisting.isEmpty()){
                if (mToProcess.isEmpty())
                    ClipBoard.clear();
                else {
                    String[] array =  new String[mToProcess.size()];
                    for (int i=0 ; i<mToProcess.size() ; i++){
                        array[i] = mToProcess.get(i);
                    }

                    mToProcess.toArray(array);
                    PasteTask task = new PasteTask(activity,mLocation);
                    task.execute(array);
                }
            }else {
                String key = mExisting.keySet().iterator().next();
                current = mExisting.get(key);
                mExisting.remove(key);

                final Dialog dialog = new FileExistsDialog(activity, current, key,
                        this, this, this, this, this);
                if (!activity.isFinishing()) {
                    dialog.show();
                }
            }
            activity.invalidateOptionsMenu();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.button1:
                // replace
                mToProcess.add(current);
                break;

            case android.R.id.button2:
                // replace all;
                mToProcess.add(current);
                for (String f : mExisting.keySet()) {
                    mToProcess.add(mExisting.get(f));
                }
                mExisting.clear();
                break;

            case R.id.button4:
                // skip all
                mExisting.clear();
                break;

            case R.id.button5:
                // abort
                mExisting.clear();
                mToProcess.clear();
                return;
        }

        next();
    }
}
