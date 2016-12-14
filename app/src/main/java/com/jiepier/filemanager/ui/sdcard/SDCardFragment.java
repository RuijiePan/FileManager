package com.jiepier.filemanager.ui.sdcard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.base.BaseFragmentPagerAdapter;
import com.jiepier.filemanager.event.NewTabEvent;
import com.jiepier.filemanager.ui.common.CommonFragment;
import com.jiepier.filemanager.util.FileUtil;
import com.jiepier.filemanager.util.RxBus;
import com.jiepier.filemanager.util.Settings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by JiePier on 16/12/7.
 */

public class SDCardFragment extends BaseFragment {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private LinkedList<Fragment> fragmentList;
    private LinkedList<String> titleList;
    private BaseFragmentPagerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {

        BaseFragment baseFragment = CommonFragment.newInstance("/");
        BaseFragment baseFragment2 = CommonFragment.newInstance("/storage");
        BaseFragment baseFragment3 = CommonFragment.newInstance("/storage/emulated");
        BaseFragment baseFragment4 = CommonFragment.newInstance(Settings.getDefaultDir());

        fragmentList = new LinkedList<>();
        fragmentList.add(baseFragment);
        fragmentList.add(baseFragment2);
        fragmentList.add(baseFragment3);
        fragmentList.add(baseFragment4);

        titleList = new LinkedList<>();
        titleList.add("/root");
        titleList.add("/storage");
        titleList.add("/emulated");
        titleList.add("/0");

        mAdapter = new BaseFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList, titleList);

        viewpager.setAdapter(mAdapter);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewpager.setCurrentItem(3);
    }

    @Override
    protected void initListeners() {

        RxBus.getDefault()
                .toObservable(NewTabEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    removeFrgament(viewpager.getCurrentItem()+1);
                    BaseFragment baseFragment = CommonFragment.newInstance(event.getPath());
                    fragmentList.add(baseFragment);
                    titleList.add(FileUtil.getFileName(event.getPath()));
                    this.mAdapter.notifyDataSetChanged();
                    viewpager.setCurrentItem(mAdapter.getCount()-1);
                });
    }

    private void removeFrgament(int currentItem) {

        int count = mAdapter.getCount();
        for (int i=currentItem; i < count; i++){
            fragmentList.remove(currentItem);
            titleList.remove(currentItem);
        }
    }

    @Override
    protected void initData() {

    }

}
