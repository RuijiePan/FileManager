package com.jiepier.filemanager.ui.category.memory.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.event.ForceStopFinishEvent;
import com.jiepier.filemanager.util.RxBus.RxBus;

import java.util.List;

/**
 * Created by panruijie on 2017/3/31.
 * Email : zquprj@gmail.com
 * 辅助功能加速清理模块
 */

public class MemoryAccessibilityService extends AccessibilityService {

    private AccessibilityNodeInfo mRootNodeInfo = null;
    private AccessibilityNodeInfo mForceStopNode = null;
    private AccessibilityNodeInfo mCertainNode = null;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        mRootNodeInfo = event.getSource();

        if (event.getClassName().equals("com.android.settings.applications.InstalledAppDetailsTop")) {
            switch (event.getEventType()) {
                case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                    //进行强制停止后的检查
                    checkStop();
                    break;
                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                    //查找停止按钮
                    mForceStopNode = getTextNode(getString(R.string.accessibility_stop));
                    if (mForceStopNode != null && mCertainNode == null) {
                        //判断是否可用点击，不可以点击直接跳过
                        if (mForceStopNode.isClickable() && mForceStopNode.isEnabled()) {
                            mForceStopNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }
                    }

                    //查找确定按钮,直接点击结束。这里需要附带forcestop按钮的原因是因为某些设置页面也会有"确定"按钮，为了防止误点。
                    //当两个条件都满足的时候才认为是进程关闭页面
                    mCertainNode = getTextNode(getString(R.string.accessibility_ok));
                    if (mCertainNode != null) {
                        mCertainNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                    break;
            }
        }
    }

    private boolean checkStop() {
        //查找停止按钮
        mForceStopNode = getTextNode(getString(R.string.accessibility_stop));
        if (mForceStopNode != null && (!mForceStopNode.isClickable() || !mForceStopNode.isEnabled())) {
            //进入下个进程停止页面，如果没有了会直接返回界面。代码看MemoryAccessibilityManager
            RxBus.getDefault().post(new ForceStopFinishEvent());
            performGlobalAction(GLOBAL_ACTION_BACK);
            return true;
        }

        return false;
    }

    private void handleForceStopEvent() {
        mForceStopNode = getTextNode(getString(R.string.accessibility_stop));
        if (mForceStopNode != null && mCertainNode == null) {
            //判断是否可用点击，不可以点击直接跳过
            if (mForceStopNode.isClickable() && mForceStopNode.isEnabled()) {
                mForceStopNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }

    private void handleCertainEvent() {
        //查找确定按钮,直接点击结束。这里需要附带forcestop按钮的原因是因为某些设置页面也会有"确定"按钮，为了防止误点。
        //当两个条件都满足的时候才认为是进程关闭页面
        mCertainNode = getTextNode(getString(R.string.accessibility_ok));
        if (mCertainNode != null) {
            mCertainNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    @Override
    public void onInterrupt() {

    }

    private AccessibilityNodeInfo getTextNode(String... texts) {
        AccessibilityNodeInfo node = null;
        AccessibilityNodeInfo tempNode;
        List<AccessibilityNodeInfo> nodes;

        for (String text : texts) {
            try {
                nodes = mRootNodeInfo.findAccessibilityNodeInfosByText(text);

                if (nodes != null && !nodes.isEmpty()) {
                    tempNode = nodes.get(nodes.size() - 1);
                    if (tempNode == null) {
                        return null;
                    }
                    node = tempNode;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return node;
    }
}
