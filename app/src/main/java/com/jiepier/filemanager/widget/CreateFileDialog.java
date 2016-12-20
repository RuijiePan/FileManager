package com.jiepier.filemanager.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.Toast;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.event.RefreshEvent;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.RxBus;
import com.jiepier.filemanager.util.Settings;

import java.io.File;

public final class CreateFileDialog extends DialogFragment {

    private static String path;
    public static CreateFileDialog create(String path1){
        path = path1;
        CreateFileDialog instance = new CreateFileDialog();
        Bundle args = new Bundle();
        args.putString("path",path);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        path = getArguments() != null ? getArguments().getString("path") : Settings.getDefaultDir();

        final Activity a = getActivity();

        // Set an EditText view to get user input
        final EditText inputf = new EditText(a);
        inputf.setHint(R.string.enter_name);

        final MaterialDialog dialog = new MaterialDialog.Builder(a)
                .title(R.string.newfile)
                .customView(inputf,true)
                .positiveText(R.string.create)
                .onPositive((dialog1, which) -> {
                    String name = inputf.getText().toString();

                    if (name.length() >= 1) {
                        boolean success = FileUtil.createFile(new File(path, name));

                        if (success) {
                            Toast.makeText(a, R.string.filecreated, Toast.LENGTH_SHORT).show();
                            RxBus.getDefault().post(new RefreshEvent());
                        }
                        else
                            Toast.makeText(a, R.string.error, Toast.LENGTH_SHORT).show();
                    }
                }).negativeText(R.string.cancel).build();

        return dialog;
    }
}
