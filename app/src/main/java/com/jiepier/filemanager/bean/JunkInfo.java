package com.jiepier.filemanager.bean;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.App;

import java.util.ArrayList;

/**
 * Created by panruijie on 2017/2/15.
 * Email : zquprj@gmail.com
 */

public class JunkInfo implements Comparable<JunkInfo> {

    private String mName;
    private long mSize;
    private String mPackageName;
    private String mPath;
    private ArrayList<JunkInfo> mChildren = new ArrayList<>();
    private boolean mIsVisible = false;
    private boolean mIsChild = true;

    public String getName() {
        return mName;
    }

    public JunkInfo setName(String mName) {
        this.mName = mName;
        return this;
    }

    public long getSize() {
        return mSize;
    }

    public JunkInfo setSize(long mSize) {
        this.mSize = mSize;
        return this;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public JunkInfo setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
        return this;
    }

    public String getPath() {
        return mPath;
    }

    public JunkInfo setPath(String mPath) {
        this.mPath = mPath;
        return this;
    }

    public ArrayList<JunkInfo> getChildren() {
        return mChildren;
    }

    public JunkInfo setChildren(ArrayList<JunkInfo> mChildren) {
        this.mChildren = mChildren;
        return this;
    }

    public boolean isVisible() {
        return mIsVisible;
    }

    public JunkInfo setVisible(boolean mIsVisible) {
        this.mIsVisible = mIsVisible;
        return this;
    }

    public boolean isChild() {
        return mIsChild;
    }

    public JunkInfo setChild(boolean mIsChild) {
        this.mIsChild = mIsChild;
        return this;
    }

    @Override
    public int compareTo(JunkInfo another) {

        String systemCache = App.getAppContext().getString(R.string.system_cache);

        if (this.mName != null && this.mName.equals(systemCache)) {
            return 1;
        }

        if (another.mName != null && another.mName.equals(systemCache)) {
            return -1;
        }

        if (this.mSize > another.mSize) {
            return 1;
        } else if (this.mSize < another.mSize) {
            return -1;
        } else {
            return 0;
        }
    }
}
