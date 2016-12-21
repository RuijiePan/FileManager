package com.jiepier.filemanager.ui.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.event.ChangeDefaultDirEvent;
import com.jiepier.filemanager.event.NewDirEvent;
import com.jiepier.filemanager.util.RxBus.RxBus;
import com.jiepier.filemanager.util.SettingPrefUtil;
import com.jiepier.filemanager.util.Settings;
import com.jiepier.filemanager.util.ToastUtil;
import com.jiepier.filemanager.widget.ColorsDialog;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JiePier on 16/12/7.
 */

public class SettingFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener{

    private Preference pDefaultDir;
    private CheckBoxPreference pAutoUpdate;
    private CheckBoxPreference pNotification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        pDefaultDir = findPreference("pDefaultDir");
        pDefaultDir.setOnPreferenceClickListener(this);
        pDefaultDir.setSummary(Settings.getDefaultDir());

        pAutoUpdate = (CheckBoxPreference) findPreference("pAutoUpdate");
        pNotification = (CheckBoxPreference) findPreference("pNotification");
        pDefaultDir.setOnPreferenceClickListener(this);
        pNotification.setOnPreferenceClickListener(this);

        RxBus.getDefault().add(this,RxBus.getDefault()
                .toObservable(NewDirEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(NewDirEvent::getPath)
                .subscribe(path -> {
                    Settings.setDefaultDir(path);
                    pDefaultDir.setSummary(path);
                }, Throwable::printStackTrace));
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if ("pDefaultDir".equals(preference.getKey())){
            RxBus.getDefault().post(new ChangeDefaultDirEvent());
        }else if ("pAutoUpdate".equals(preference.getKey())){
            ToastUtil.showToast(getActivity(),"auto update : "+ pAutoUpdate.isChecked());
        }else if ("pNotification".equals(preference.getKey())){
            ToastUtil.showToast(getActivity(),"auto update : "+ pNotification.isChecked());
        }
        return true;
    }

}
