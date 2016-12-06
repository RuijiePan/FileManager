package com.jiepier.filemanager;

import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jiepier.filemanager.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JiePier on 16/12/6.
 */

public abstract class BaseDrawerActivity extends BaseActivity{

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.vNavigation)
    NavigationView vNavigation;
    private NavigationClickListener mListener;

    @Override
    public int initContentView() {
        return R.layout.activity_drawer;
    }

    @Override
    public void initUiAndListener() {
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.flContentRoot);
        LayoutInflater.from(this).inflate(setupContentView(),viewGroup,true);
        ButterKnife.bind(this);
        setUpNavigationClickListener();
    }

    private void setUpNavigationClickListener() {
        vNavigation.setNavigationItemSelectedListener(item -> {

            if (mListener!=null) {
                switch (item.getItemId()) {
                    case R.id.menu_local:
                        mListener.onClickLocal();
                        break;
                    case R.id.menu_star:
                        mListener.onClickStar();
                        break;
                    case R.id.menu_setting:
                        mListener.onClickSetting();
                        break;
                    case R.id.menu_about:
                        mListener.onClickAbout();
                        break;
                }
            }
            drawerLayout.closeDrawer(Gravity.LEFT);
            return false;
        });
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }

    abstract int setupContentView();

    public void setNavigationClickListener(NavigationClickListener listener){
        this.mListener = listener;
    }

    public interface NavigationClickListener{
        void onClickLocal();

        void onClickStar();

        void onClickSetting();

        void onClickAbout();
    }
}
