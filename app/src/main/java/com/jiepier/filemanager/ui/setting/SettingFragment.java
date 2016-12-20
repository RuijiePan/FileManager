package com.jiepier.filemanager.ui.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.event.ChangeDefaultDirEvent;
import com.jiepier.filemanager.event.NewDirEvent;
import com.jiepier.filemanager.util.RxBus.RxBus;
import com.jiepier.filemanager.util.SettingPrefUtil;
import com.jiepier.filemanager.util.Settings;
import com.jiepier.filemanager.widget.ColorsDialog;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JiePier on 16/12/7.
 */

public class SettingFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener{

    private Preference pTheme;
    private Preference pDefaultDir;
    private ColorsDialog mThemeDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        pTheme = findPreference("pTheme");
        pTheme.setOnPreferenceClickListener(this);
        pTheme.setSummary(
                getResources().getStringArray(R.array.mdColorNames)[SettingPrefUtil.getThemeIndex(
                        getActivity().getApplicationContext())]);

        pDefaultDir = findPreference("pDefaultDir");
        pDefaultDir.setOnPreferenceClickListener(this);
        pDefaultDir.setSummary(Settings.getDefaultDir());

        RxBus.getDefault().add(this,RxBus.getDefault()
                .toObservable(NewDirEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(NewDirEvent::getPath)
                .subscribe(path -> {
                    Settings.setDefaultDir(path);
                    pDefaultDir.setSummary(path);
                }, Throwable::printStackTrace));

        mThemeDialog = ColorsDialog.launch(getActivity());
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if ("pTheme".equals(preference.getKey())){
            mThemeDialog.show(getFragmentManager(),"DialogFragment");
        }else if ("pDefaultDir".equals(preference.getKey())){
            RxBus.getDefault().post(new ChangeDefaultDirEvent());
        }
        return true;
    }

}
