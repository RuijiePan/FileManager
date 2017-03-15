package com.jiepier.filemanager.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jiepier.filemanager.R;

public final class FileExistsDialog extends MaterialDialog {

    public FileExistsDialog(final Context context, final String source,
                            final String target, final View.OnClickListener abortAction,
                            final View.OnClickListener skipAction,
                            final View.OnClickListener skipAllAction,
                            final View.OnClickListener replaceAction,
                            final View.OnClickListener replaceAllAction) {

        super(new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_exists, true)
                .title(R.string.overwrite_title));

        this.initView(source, target, abortAction, skipAction, skipAllAction,
                replaceAction, replaceAllAction);
    }

    private void initView(final String sourcePath, final String targetPath,
                          final View.OnClickListener abortAction,
                          final View.OnClickListener skipAction,
                          final View.OnClickListener skipAllAction,
                          final View.OnClickListener replaceAction,
                          final View.OnClickListener replaceAllAction) {

        final TextView source = (TextView) this
                .findViewById(android.R.id.text1);
        source.setText(sourcePath);

        final TextView target = (TextView) this
                .findViewById(android.R.id.text2);
        target.setText(targetPath);

        this.findViewById(android.R.id.button1).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        replaceAction.onClick(v);
                    }
                });
        this.findViewById(android.R.id.button2).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        replaceAllAction.onClick(v);
                    }
                });
        this.findViewById(android.R.id.button3).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        skipAction.onClick(v);
                    }
                });
        this.findViewById(R.id.button4).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        skipAllAction.onClick(v);
                    }
                });
        this.findViewById(R.id.button5).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        abortAction.onClick(v);
                    }
                });
        this.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                skipAllAction.onClick(findViewById(R.id.button5));
            }
        });
    }
}
