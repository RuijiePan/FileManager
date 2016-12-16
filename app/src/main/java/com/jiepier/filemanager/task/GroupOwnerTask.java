package com.jiepier.filemanager.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


import com.jiepier.filemanager.R;
import com.jiepier.filemanager.util.RootCommands;

import java.io.File;

public class GroupOwnerTask extends AsyncTask<File, Void, Boolean> {

    private final Context context;
    private final String group, owner;

    public GroupOwnerTask(Context context, String group1, String owner1) {
        this.context = context;
        this.group = group1;
        this.owner = owner1;
    }

    @Override
    protected Boolean doInBackground(final File... params) {
        return RootCommands.changeGroupOwner(params[0], owner, group);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        this.finish(result);
    }

    @Override
    protected void onCancelled(Boolean result) {
        super.onCancelled(result);
        this.finish(result);
    }

    private void finish(Boolean result) {
        if (result)
            Toast.makeText(this.context,
                    this.context.getString(R.string.permissionschanged),
                    Toast.LENGTH_SHORT).show();
    }
}