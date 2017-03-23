package com.jiepier.filemanager.ui.plugin;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.constant.AppConstant;

import java.util.List;

import butterknife.BindView;

/**
 * Created by JiePier on 16/12/7.
 */

public class PluginFragment extends BaseFragment implements AccessibilityManager.AccessibilityStateChangeListener {

    @BindView(R.id.layout_control_accessibility)
    LinearLayout mPluginLayout;
    @BindView(R.id.layout_control_accessibility_text)
    TextView mPluginText;
    private AccessibilityManager mAccessibilityManager;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_advertisement;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        mAccessibilityManager = (AccessibilityManager) getActivity().getSystemService(Context.ACCESSIBILITY_SERVICE);
        mAccessibilityManager.addAccessibilityStateChangeListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        updateServiceStatus();
    }

    @Override
    protected void initListeners() {
        mPluginLayout.setOnClickListener(v -> {
            try {
                Toast.makeText(getContext(), getString(R.string.turn_on_toast) + mPluginText.getText(), Toast.LENGTH_SHORT).show();
                Intent accessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(accessibleIntent);
            } catch (Exception e) {
                Toast.makeText(getContext(), getString(R.string.turn_on_error_toast), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onAccessibilityStateChanged(boolean enabled) {
        updateServiceStatus();
    }

    private void updateServiceStatus() {
        if (isServiceEnabled()) {
            mPluginText.setText(getResources().getString(R.string.service_close));
        } else {
            mPluginText.setText(getResources().getString(R.string.service_on));
        }
    }

    @Override
    public void onDestroy() {
        mAccessibilityManager.removeAccessibilityStateChangeListener(this);
        super.onDestroy();
    }

    private boolean isServiceEnabled() {
        List<AccessibilityServiceInfo> accessibilityServices =
                mAccessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(AppConstant.PACKAGE_NAME + "/.plugin.LuckyMoneyService")) {
                return true;
            }
        }
        return false;
    }

}
