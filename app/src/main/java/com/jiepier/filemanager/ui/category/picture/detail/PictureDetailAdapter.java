package com.jiepier.filemanager.ui.category.picture.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by panruijie on 17/1/22.
 * Email : zquprj@gmail.com
 */

public class PictureDetailAdapter extends FragmentStatePagerAdapter {

    private List<String> mList;

    public PictureDetailAdapter(FragmentManager fm, List<String> list) {
        super(fm);
        mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        String url = mList.get(position);
        return PictureDetailFragment.newInstance(url);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }
}
