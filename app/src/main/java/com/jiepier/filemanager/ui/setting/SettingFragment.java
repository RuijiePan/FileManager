package com.jiepier.filemanager.ui.setting;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.App;
import com.jiepier.filemanager.constant.AppConstant;
import com.jiepier.filemanager.event.ChangeDefaultDirEvent;
import com.jiepier.filemanager.event.NewDirEvent;
import com.jiepier.filemanager.util.LanguageUtil;
import com.jiepier.filemanager.util.RxBus.RxBus;
import com.jiepier.filemanager.util.SettingPrefUtil;
import com.jiepier.filemanager.util.Settings;
import com.jiepier.filemanager.util.SharedUtil;
import com.jiepier.filemanager.util.ToastUtil;
import com.jiepier.filemanager.widget.ColorsDialog;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by JiePier on 16/12/7.
 */

public class SettingFragment extends PreferenceFragment
        implements Preference.OnPreferenceClickListener {

    private String TAG = getClass().getSimpleName();
    private Preference pDefaultDir;
    private Preference pDefaultScanDir;
    private Preference pThemeColor;
    private Preference pLanguage;
    private CheckBoxPreference pAutoUpdate;
    private CheckBoxPreference pNotification;
    private CompositeSubscription mCompositeSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);

        pDefaultDir = findPreference("pDefaultDir");
        pDefaultScanDir = findPreference("pDefaultScanDir");
        pAutoUpdate = (CheckBoxPreference) findPreference("pAutoUpdate");
        pNotification = (CheckBoxPreference) findPreference("pNotification");
        pThemeColor = findPreference("pThemeColor");
        pLanguage = findPreference("pLanguage");

        pDefaultDir.setOnPreferenceClickListener(this);
        pDefaultScanDir.setOnPreferenceClickListener(this);
        pNotification.setOnPreferenceClickListener(this);
        pAutoUpdate.setOnPreferenceClickListener(this);
        pDefaultDir.setOnPreferenceClickListener(this);
        pThemeColor.setOnPreferenceClickListener(this);
        pLanguage.setOnPreferenceClickListener(this);

        pDefaultDir.setSummary(Settings.getDefaultDir());
        pDefaultScanDir.setSummary(SharedUtil.getString(App.sContext, AppConstant.DEFAULT_SCAN_PATH));
        pLanguage.setSummary(LanguageUtil.getInstance().getLanguageName());
        pThemeColor.setSummary(SettingPrefUtil.getThemeName(App.sContext));

        mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(RxBus.getDefault()
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
        } else if ("pThemeColor".endsWith(preference.getKey())) {
            ColorsDialog.launch(getActivity()).show(getFragmentManager(), "DialogFragment");
        } else if ("pLanguage".equals(preference.getKey())) {
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.settings_language_select)
                    .items(R.array.settings_language_array)
                    .itemsCallback((dialog, itemView, position, text) -> {
                        LanguageUtil.getInstance().updateLanguage(position);
                    })
                    .negativeText(R.string.cancel)
                    .show();

        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
        mCompositeSubscription = null;
    }
}
