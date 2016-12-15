package com.jiepier.filemanager.ui.main;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseDrawerActivity;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.dialog.DirectoryInfoDialog;
import com.jiepier.filemanager.event.NewTabEvent;
import com.jiepier.filemanager.ui.sdcard.SDCardFragment;
import com.jiepier.filemanager.util.RxBus;
import com.jiepier.filemanager.util.Settings;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.paste:
                return true;
            case R.id.folderinfo:
                DirectoryInfoDialog dialog = DirectoryInfoDialog.create(mSdCardFragment.getPath());
                dialog.show(fm_v4,TAG_DIALOG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

