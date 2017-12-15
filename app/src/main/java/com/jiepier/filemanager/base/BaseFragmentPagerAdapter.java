package com.jiepier.filemanager.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by prj on 2016/9/13.
 */
public class BaseFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private LinkedList<Fragment> fragmentsList;
    public LinkedList<String> titleList;

    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public BaseFragmentPagerAdapter(FragmentManager fm, LinkedList<Fragment> fragments, LinkedList<String> titleList) {
        super(fm);
        this.fragmentsList = fragments;
        this.titleList = titleList;
    }

    public BaseFragmentPagerAdapter(FragmentManager fm, LinkedList<Fragment> fragments) {
        super(fm);
        this.fragmentsList = fragments;
    }

    @Override
    public int getCount() {
        return fragmentsList == null? 0 : fragmentsList.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentsList.get(arg0);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

}
