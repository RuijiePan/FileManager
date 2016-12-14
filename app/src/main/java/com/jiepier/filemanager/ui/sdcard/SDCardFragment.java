package com.jiepier.filemanager.ui.sdcard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.base.BaseFragmentPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JiePier on 16/12/7.
 */

public class SDCardFragment extends BaseFragment {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private ArrayList<Fragment> fragmentList;
    private ArrayList<String> titleList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            fragmentList = new ArrayList<>();
            BaseFragment baseFragment = new ContentFragment();
            BaseFragment baseFragment2 = new ContentFragment();
            //baseFragment.getArguments().putBundle("file",);
            fragmentList.add(baseFragment);
            fragmentList.add(baseFragment2);
            titleList = new ArrayList<>();
            titleList.add("/SDCard");
            titleList.add("/Android");
            BaseFragmentPagerAdapter adapter = new BaseFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList, titleList);

            viewpager.setAdapter(adapter);
            tablayout.setupWithViewPager(viewpager);
            tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

}
