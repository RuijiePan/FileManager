package com.jiepier.filemanager.ui.main;

import com.jiepier.filemanager.base.BaseDrawerActivity;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.ui.sdcard.SDCardFragment;

public class MainActivity extends BaseDrawerActivity {

    @Override
    public void initUiAndListener() {
        super.initUiAndListener();

        setNavigationClickListener(new NavigationClickListener() {
            @Override
            public void onClickSDCard() {

            }

            @Override
            public void onClickRoot() {

            }

            @Override
            public void onClickSystem() {

            }

            @Override
            public void onClickSetting() {

            }

            @Override
            public void onClickAbout() {

            }
        });
    }
}
