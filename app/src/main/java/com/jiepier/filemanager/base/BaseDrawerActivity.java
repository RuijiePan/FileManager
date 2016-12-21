package com.jiepier.filemanager.base;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.ui.about.AboutActivity;
import com.jiepier.filemanager.ui.about.AboutFragment;
import com.jiepier.filemanager.ui.sdcard.SDCardFragment;
import com.jiepier.filemanager.ui.root.RootFragment;
import com.jiepier.filemanager.ui.setting.SettingActivity;
import com.jiepier.filemanager.ui.setting.SettingFragment;
import com.jiepier.filemanager.ui.system.SystemFragment;
import com.jiepier.filemanager.util.ResourceUtil;
import com.jiepier.filemanager.util.SettingPrefUtil;
import com.jiepier.filemanager.util.Settings;
import com.jiepier.filemanager.util.StatusBarUtil;
import com.jiepier.filemanager.widget.ColorsDialog;
import com.jiepier.filemanager.widget.IconPreview;

import butterknife.BindView;

/**
 * Created by JiePier on 16/12/6.
 */

public abstract class BaseDrawerActivity extends BaseToolbarActivity{

    @BindView(R.id.drawerLayout)
    protected DrawerLayout drawerLayout;
    @BindView(R.id.vNavigation)
    protected NavigationView mNavigation;
    public static final String SDCARD = "1";
    public static final String ROOT = "2";
    public static final String SYSTEM = "3";
    public static final String TAG_DIALOG = "dialog";
    protected String OLDTAG = SDCARD;
    protected android.app.FragmentManager fm_v4;
    private FragmentManager fm;
    private ImageView mIvtheme;
    private SwitchCompat mSwitch;
    private boolean isReload;
    private ActionBarDrawerToggle mDrawerToggle;
    //private NavigationClickListener mListener;
    protected SDCardFragment mSdCardFragment;
    private RootFragment mRootFragment;
    private SystemFragment mSystemFragment;

    @Override
    public int initContentView() {
        return R.layout.activity_drawer;
    }

    @Override
    public void initUiAndListener() {
        Settings.updatePreferences(this);
        new IconPreview(this);

        mSdCardFragment = new SDCardFragment();
        mRootFragment = new RootFragment();
        mSystemFragment = new SystemFragment();
        addFragment();
        transformFragment(SDCARD);

        setUpNavigationClickListener();
        StatusBarUtil.setColorForDrawerLayout(this, drawerLayout, ResourceUtil.getThemeColor(this), 0);

        this.mDrawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, R.string.app_menu,
                R.string.app_name);
        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mIvtheme = (ImageView) mNavigation.getHeaderView(0).findViewById(R.id.ivTheme);
        mSwitch = (SwitchCompat) mNavigation.getMenu().findItem(R.id.light_model)
                .getActionView().findViewById(R.id.switchForActionBar);

        if (SettingPrefUtil.getNightModel(this)) {
            mIvtheme.setImageResource(R.drawable.ic_brightness_3_white_24dp);
            mSwitch.setChecked(true);
        }

        mSwitch.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked){
                SettingPrefUtil.setNightModel(this, true);
                mIvtheme.setImageResource(R.drawable.ic_wb_sunny_white_24dp);
            }else {
                SettingPrefUtil.setNightModel(this, false);
                mIvtheme.setImageResource(R.drawable.ic_brightness_3_white_24dp);
            }
            isReload = true;
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        isReload = false;
        drawerLayout.setDrawerListener(new FileDrawerListener());
    }

    protected void addFragment(){
        fm = getSupportFragmentManager();
        fm_v4 = getFragmentManager();
        fm.beginTransaction()
                .add(R.id.flContentRoot,mSdCardFragment,SDCARD)
                .commit();
        fm.beginTransaction()
                .add(R.id.flContentRoot,mRootFragment,ROOT)
                .commit();
        fm.beginTransaction()
                .add(R.id.flContentRoot,mSystemFragment,SYSTEM)
                .commit();
        fm.beginTransaction().hide(mRootFragment).commit();
        fm.beginTransaction().hide(mSystemFragment).commit();

    }

    private void transformFragment(String TAG){
        if (OLDTAG.equals(TAG))
            return;
        else {
            if (TAG.equals(SDCARD))
                fm.beginTransaction().show(mSdCardFragment).commit();
            if (TAG.equals(ROOT))
                fm.beginTransaction().show(mRootFragment).commit();
            if (TAG.equals(SYSTEM))
                fm.beginTransaction().show(mSystemFragment).commit();

            if (OLDTAG.equals(SDCARD))
                fm.beginTransaction().hide(mSdCardFragment).commit();
            if (OLDTAG.equals(ROOT))
                fm.beginTransaction().hide(mRootFragment).commit();
            if (OLDTAG.equals(SYSTEM))
                fm.beginTransaction().hide(mSystemFragment).commit();

        }
        OLDTAG = TAG;
    }

    private void setUpNavigationClickListener() {
        mNavigation.setNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.menu_sdcard:
                    transformFragment(SDCARD);
                    break;
                case R.id.menu_root:
                    transformFragment(ROOT);
                    break;
                case R.id.menu_system:
                    transformFragment(SYSTEM);
                    break;
                case R.id.menu_setting:
                    Intent intent = new Intent(this, SettingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.menu_about:
                    intent = new Intent(this, AboutActivity.class);
                    startActivity(intent);
                    break;
                case R.id.menu_theme:
                    ColorsDialog.launch(this).show(fm_v4,TAG_DIALOG);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        });
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return false;
    }

    /*public void setNavigationClickListener(NavigationClickListener listener){
        this.mListener = listener;
    }

    public interface NavigationClickListener{
        void onClickSDCard();

        void onClickRoot();

        void onClickSystem();

        void onClickSetting();

        void onClickAbout();
    }*/

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

        if (OLDTAG.equals(SDCARD)&&keyCode == KeyEvent.KEYCODE_BACK)
            return mSdCardFragment.onBackPressed();

        return super.onKeyDown(keyCode, event);
    }

    /**
     * When using ActionBarDrawerToggle, all DrawerLayout listener methods should be forwarded
     * if the ActionBarDrawerToggle is not used as the DrawerLayout listener directly.
     */
    private class FileDrawerListener implements DrawerLayout.DrawerListener{

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
            if (isReload) {
                reload();
                isReload = false;
            }
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            BaseDrawerActivity.this.mDrawerToggle.onDrawerStateChanged(newState);
        }
    }

}
