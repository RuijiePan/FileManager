package com.jiepier.filemanager.widget;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.Permissions;
import com.jiepier.filemanager.util.RootCommands;
import com.jiepier.filemanager.util.Settings;
import com.jiepier.filemanager.util.StatFsCompat;
import com.stericson.RootTools.RootTools;

import java.io.File;
import java.lang.ref.WeakReference;

public final class DirectoryInfoDialog extends DialogFragment {

    private View mView;
    private String path;

    public static DirectoryInfoDialog create(String path) {
        DirectoryInfoDialog instance = new DirectoryInfoDialog();
        Bundle args = new Bundle();
        args.putString("path", path);
        instance.setArguments(args);
        return instance;
    }

    public MaterialDialog onCreateDialog(final Bundle savedInstanceState) {
        path = getArguments() != null ? getArguments().getString("path") : Settings.getDefaultDir();
        final Activity activity = this.getActivity();
        mView = activity.getLayoutInflater().inflate(
                R.layout.dialog_directory_info, null);
        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .title(R.string.dir_info)
                .customView(mView, true)
                .positiveText(R.string.ok)
                .build();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        File mFile = new File(path);
        PartitionInfoTask mTask = new PartitionInfoTask(mView);
        mTask.execute(mFile);
    }

    private static final class PartitionInfo {
        final CharSequence mPath;
        final CharSequence mPermissionText;
        final CharSequence mTotalBytesText;
        final CharSequence mBlockSizeText;
        final CharSequence mFreeBytesText;
        final CharSequence mUsedSpaceText;

        final long mTotalBytes;
        final long mBlockSize;
        final long mFreeBytes;
        final long mUsedSpace;

        private PartitionInfo(final CharSequence path,
                              final CharSequence permission, final long totalBytes,
                              final long blockSize, final long freeBytes, final long usedSpace) {
            this.mPath = path;
            this.mPermissionText = permission;
            this.mTotalBytes = totalBytes;
            this.mBlockSize = blockSize;
            this.mFreeBytes = freeBytes;
            this.mUsedSpace = usedSpace;

            this.mTotalBytesText = FileUtil.formatCalculatedSize(totalBytes);
            this.mBlockSizeText = Long.toString(blockSize);
            this.mFreeBytesText = FileUtil.formatCalculatedSize(freeBytes);

            if (totalBytes != 0L) {
                this.mUsedSpaceText = FileUtil.formatCalculatedSize(usedSpace) + ' ' + '(' + usedSpace * 100L / totalBytes + '%' + ')';
            } else {
                this.mUsedSpaceText = null;
            }
        }
    }

    private static final class PartitionInfoTask extends
            AsyncTask<File, Void, PartitionInfo> {

        private final WeakReference<View> mViewRef;

        private PartitionInfoTask(final View view) {
            this.mViewRef = new WeakReference<>(view);
        }

        @Override
        protected PartitionInfo doInBackground(final File... params) {
            final String path = params[0].getAbsolutePath();
            final StatFsCompat statFs = new StatFsCompat(path);
            final long valueTotal = statFs.getTotalBytes();
            final long valueAvail = statFs.getAvailableBytes();
            final long valueUsed = valueTotal - valueAvail;
            String[] permission = null;
            String perm;

            if (RootTools.isAccessGiven())
                permission = RootCommands.getFileProperties(params[0]);

            perm = permission != null ? permission[0] : Permissions.getBasicPermission(params[0]);

            return new PartitionInfo(path, perm, valueTotal,
                    statFs.getBlockSizeLong(), statFs.getFreeBytes(), valueUsed);
        }

        @Override
        protected void onPostExecute(final PartitionInfo partitionInfo) {
            final View view = mViewRef.get();
            if (view != null) {
                final TextView title = (TextView) view
                        .findViewById(R.id.location);
                title.setText(partitionInfo.mPath);

                if (partitionInfo.mPermissionText.length() != 0L) {
                    final TextView perm = (TextView) view
                            .findViewById(R.id.dir_permission);
                    perm.setText(partitionInfo.mPermissionText);
                }

                if (partitionInfo.mTotalBytes != 0L) {
                    final TextView total = (TextView) view
                            .findViewById(R.id.total);
                    total.setText(partitionInfo.mTotalBytesText);
                }

                if (partitionInfo.mBlockSize != 0L) {
                    final TextView block = (TextView) view
                            .findViewById(R.id.block_size);
                    block.setText(partitionInfo.mBlockSizeText);
                }

                if (partitionInfo.mFreeBytes != 0L) {
                    final TextView free = (TextView) view
                            .findViewById(R.id.free);
                    free.setText(partitionInfo.mFreeBytesText);
                }

                if (partitionInfo.mUsedSpace != 0L) {
                    final TextView used = (TextView) view
                            .findViewById(R.id.used);
                    used.setText(partitionInfo.mUsedSpaceText);
                }
            }
        }
    }

}
