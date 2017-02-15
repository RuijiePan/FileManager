package com.jiepier.filemanager.ui.setting;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.jiepier.filemanager.Constant.AppConstant;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.App;
import com.jiepier.filemanager.event.ChangeDefaultDirEvent;
import com.jiepier.filemanager.event.NewDirEvent;
import com.jiepier.filemanager.util.RxBus.RxBus;
import com.jiepier.filemanager.util.Settings;
import com.jiepier.filemanager.util.SharedUtil;
import com.jiepier.filemanager.util.ToastUtil;

/**
 * Created by JiePier on 16/12/7.
 */

public class SettingFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener {

    private String TAG = getClass().getSimpleName();
    private Preference pDefaultDir;
    private Preference pDefaultScanDir;
    private CheckBoxPreference pAutoUpdate;
    private CheckBoxPreference pNotification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        pDefaultDir = findPreference("pDefaultDir");
        pDefaultScanDir = findPreference("pDefaultScanDir");
        pAutoUpdate = (CheckBoxPreference) findPreference("pAutoUpdate");
        pNotification = (CheckBoxPreference) findPreference("pNotification");

        pDefaultDir.setOnPreferenceClickListener(this);
        pDefaultScanDir.setOnPreferenceClickListener(this);
        pNotification.setOnPreferenceClickListener(this);
        pAutoUpdate.setOnPreferenceClickListener(this);
        pDefaultDir.setOnPreferenceClickListener(this);

        pDefaultDir.setSummary(Settings.getDefaultDir());
        pDefaultScanDir.setSummary(SharedUtil.getString(App.sContext, AppConstant.DEFAULT_SCAN_PATH));

        RxBus.getDefault().add(this, RxBus.getDefault()
                .IoToUiObservable(NewDirEvent.class)
                .subscribe(event -> {
                    if (event.getType().equals("pDefaultDir")) {
                        Settings.setDefaultDir(event.getPath());
                        pDefaultDir.setSummary(event.getPath());
                    } else {
                        SharedUtil.putString(App.sContext
                                , AppConstant.DEFAULT_SCAN_PATH
                                , event.getPath());
                        pDefaultScanDir.setSummary(event.getPath());
                    }
                }, Throwable::printStackTrace));
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if ("pDefaultDir".equals(preference.getKey())) {
            RxBus.getDefault().post(new ChangeDefaultDirEvent("pDefaultDir"));
        } else if ("pDefaultScanDir".equals(preference.getKey())) {
            RxBus.getDefault().post(new ChangeDefaultDirEvent("pDefaultScanDir"));
        } else if ("pAutoUpdate".equals(preference.getKey())) {
            ToastUtil.showToast(getActivity(), "auto update : " + pAutoUpdate.isChecked());
        } else if ("pNotification".equals(preference.getKey())) {
            ToastUtil.showToast(getActivity(), "notification : " + pNotification.isChecked());
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unsubscribeAll();
    }
}
