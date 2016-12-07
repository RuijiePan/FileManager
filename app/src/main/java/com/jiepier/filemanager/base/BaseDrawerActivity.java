package com.jiepier.filemanager.base;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseActivity;
import com.jiepier.filemanager.ui.about.AboutFragment;
import com.jiepier.filemanager.ui.category.CategoryFragment;
import com.jiepier.filemanager.ui.manager.FileManagerFragment;
import com.jiepier.filemanager.ui.setting.SettingFragment;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JiePier on 16/12/6.
 */

public abstract class BaseDrawerActivity extends BaseActivity{

    @BindView(R.id.drawerLayout)
    protected DrawerLayout drawerLayout;
    @BindView(R.id.vNavigation)
    protected NavigationView mNavigation;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationClickListener mListener;

    @Override
    public int initContentView() {
        return R.layout.activity_drawer;
    }

    @Override
    public void initUiAndListener() {
        transformFragment(setFragment());
        setUpNavigationClickListener();

        if (mListener != null){
            drawerLayout.setDrawerListener(new FileDrawerListener());
        }

        this.mDrawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, R.string.app_menu,
                R.string.app_name);
    }

    private void transformFragment(BaseFragment fragment){
        getFragmentManager().beginTransaction()
                .replace(R.id.flContentRoot, fragment)
                .commit();
    }

    protected abstract BaseFragment setFragment();

    private void setUpNavigationClickListener() {
        mNavigation.setNavigationItemSelectedListener(item -> {

            if (mListener!=null) {
                switch (item.getItemId()) {
                    case R.id.menu_local:
                        mListener.onClickLocal();
                        transformFragment(new CategoryFragment());
                        break;
                    case R.id.menu_star:
                        mListener.onClickStar();
                        transformFragment(new FileManagerFragment());
                        break;
                    case R.id.menu_setting:
                        mListener.onClickSetting();
                        transformFragment(new SettingFragment());
                        break;
                    case R.id.menu_about:
                        mListener.onClickAbout();
                        transformFragment(new AboutFragment());
                        break;
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return false;
    }

    public void setNavigationClickListener(NavigationClickListener listener){
        this.mListener = listener;
    }

    public interface NavigationClickListener{
        void onClickLocal();

        void onClickStar();

        void onClickSetting();

        void onClickAbout();
    }

    /**
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.drawerLayout.openDrawer(GravityCompat.START);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Take care of calling onBackPressed() for pre-Eclair platforms.
     *
     * @param keyCode keyCode
     * @param event event
     */
    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 如果抽屉打开了
        if (keyCode == KeyEvent.KEYCODE_BACK &&
                this.drawerLayout.isDrawerOpen(this.mNavigation)) {
            this.drawerLayout.closeDrawer(this.mNavigation);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class FileDrawerListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            BaseDrawerActivity.this.mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            BaseDrawerActivity.this.mDrawerToggle.onDrawerOpened(drawerView);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            BaseDrawerActivity.this.mDrawerToggle.onDrawerClosed(drawerView);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            BaseDrawerActivity.this.mDrawerToggle.onDrawerStateChanged(newState);
        }
    }

    @Override protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
}
