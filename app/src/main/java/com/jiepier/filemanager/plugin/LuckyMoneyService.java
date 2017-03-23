package com.jiepier.filemanager.plugin;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jiepier.filemanager.constant.AppConstant;
import com.jiepier.filemanager.util.SharedUtil;

import java.util.List;

/**
 * Created by panruijie on 2017/3/16.
 * Email : zquprj@gmail.com
 * 抢红包辅助功能
 */

public class LuckyMoneyService extends AccessibilityService {

    private static final String WECHAT_DETAIL_EN = "Details";
    private static final String WECHAT_DETAIL_CN = "红包详情";
    private static final String WECHAT_BETTER_LUCK_EN = "Better luck next time!";
    private static final String WECHAT_BETTER_LUCK_CN = "手慢了";
    private static final String WECHAT_EXPIRES_CN = "已超过24小时";
    private static final String WECHAT_VIEW_SELF_CN = "查看红包";
    private static final String WECHAT_VIEW_OTHERS_CN = "领取红包";
    private static final String WECHAT_NOTIFICATION_TIP = "[微信红包]";
    private static final String WECHAT_LUCKMONEY_RECEIVE_ACTIVITY = "LuckyMoneyReceiveUI";
    private static final String WECHAT_LUCKMONEY_DETAIL_ACTIVITY = "LuckyMoneyDetailUI";
    private static final String WECHAT_LUCKMONEY_GENERAL_ACTIVITY = "LauncherUI";
    private static final String WECHAT_LUCKMONEY_CHATTING_ACTIVITY = "ChattingUI";
    private String mCurrentActivityName = WECHAT_LUCKMONEY_GENERAL_ACTIVITY;

    private AccessibilityNodeInfo mRootNodeInfo, mReceiveNode, mUnpackNode;
    private boolean mLuckyMoneyPicked, mLuckyMoneyReceived;
    private int mUnpackCount = 0;
    private boolean mMutex = false;
    private boolean mListMutex = false;
    private boolean mChatMutex = false;
    private LuckyMoneySignature mSignature = new LuckyMoneySignature();
    private PowerUtil mPowerUtil;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        setCurrentActivityName(event);

        //检测通知消息
        if (!mMutex) {
            if (SharedUtil.getBoolean(AppConstant.WATCH_NOTIFICATION, false) &&
                    watchNotifications(event)) {
                return;
            }
            if (SharedUtil.getBoolean(AppConstant.WATCH_LIST, false) &&
                    watchList(event)) {
                return;
            }

            mListMutex = true;
        }

        if (!mChatMutex) {
            mChatMutex = true;
            if (SharedUtil.getBoolean(AppConstant.WATCH_CHAT, false)) {
                watchChat(event);
            }
            mChatMutex = false;
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        watchFlagsFromPreference();
    }

    @Override
    public void onDestroy() {
        mPowerUtil.handleWakeLock(false);
        super.onDestroy();
    }

    private void watchChat(AccessibilityEvent event) {
        this.mRootNodeInfo = getRootInActiveWindow();
        if (mRootNodeInfo == null) {
            return;
        }

        mReceiveNode = null;
        mUnpackNode = null;

        checkNodeInfo(event.getEventType());

        //如果已经收到红包并且还没有戳开
        if (mLuckyMoneyReceived && !mLuckyMoneyPicked && (mReceiveNode != null)) {
            mMutex = true;

            mReceiveNode.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
            mLuckyMoneyReceived = false;
            mLuckyMoneyPicked = true;
        }

        //如果戳开但是还没领取
        if (mUnpackCount == 1 && (mUnpackNode != null)) {
            int delayFlag = SharedUtil.getInt(AppConstant.OPEN_DELAY) * 1000;
            new android.os.Handler().postDelayed(() -> {
                try {
                    openPacket();
                } catch (Exception e) {
                    mMutex = false;
                    mLuckyMoneyPicked = false;
                    mUnpackCount = 0;
                    e.printStackTrace();
                }
            }, delayFlag);
        }
    }

    private void openPacket() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float dpi = metrics.density;

        if (Build.VERSION.SDK_INT <= 23) {
            mUnpackNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            if (Build.VERSION.SDK_INT > 23) {
                Path path = new Path();
                if (dpi == 640) {
                    path.moveTo(720, 1575);
                } else {
                    path.moveTo(540, 1060);
                }

                GestureDescription.Builder builder = new GestureDescription.Builder();
                GestureDescription gestureDescription = builder.addStroke(new GestureDescription.StrokeDescription(path, 450, 50)).build();
                dispatchGesture(gestureDescription, new GestureResultCallback() {
                    @Override
                    public void onCompleted(GestureDescription gestureDescription) {
                        Log.w("ruijie", "on Completed");
                        mMutex = false;
                        super.onCompleted(gestureDescription);
                    }

                    @Override
                    public void onCancelled(GestureDescription gestureDescription) {
                        Log.w("ruijie", "on Cancelled");
                        mMutex = false;
                        super.onCancelled(gestureDescription);
                    }
                }, null);
            }
        }
    }

    private void checkNodeInfo(int eventType) {
        if (mRootNodeInfo == null) {
            return;
        }

        if (mSignature.commentString != null) {
            sendComment();
            mSignature.commentString = null;
        }

        AccessibilityNodeInfo nodel = SharedUtil.getBoolean(AppConstant.WATCH_SELF, false) ?
                getTheLastNode(WECHAT_VIEW_OTHERS_CN, WECHAT_VIEW_SELF_CN) : getTheLastNode(WECHAT_VIEW_OTHERS_CN);
        if (nodel != null && (mCurrentActivityName.contains(WECHAT_LUCKMONEY_CHATTING_ACTIVITY)
                || mCurrentActivityName.contains(WECHAT_LUCKMONEY_GENERAL_ACTIVITY))) {
            String excludeWords = SharedUtil.getString(AppConstant.WATCH_EXCLUDE_WORDS);
            if (mSignature.generateSignature(nodel, excludeWords)) {
                mLuckyMoneyReceived = true;
                mReceiveNode = nodel;
            }
            return;
        }

        //戳开红包，红包还没抢完，遍历节点匹配"拆红包"
        AccessibilityNodeInfo node2 = findOpenButton(mRootNodeInfo);
        if (node2 != null && "android.widget.Button".equals(node2.getClassName()) && mCurrentActivityName.contains(WECHAT_LUCKMONEY_RECEIVE_ACTIVITY)) {
            mUnpackNode = node2;
            mUnpackCount += 1;
            return;
        }

        boolean hasNodes = hasOneOfThoseNodes(
                WECHAT_BETTER_LUCK_CN, WECHAT_DETAIL_CN,
                WECHAT_BETTER_LUCK_EN, WECHAT_DETAIL_EN,
                WECHAT_EXPIRES_CN);
        if (mMutex && eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED && hasNodes &&
                mCurrentActivityName.contains(WECHAT_LUCKMONEY_DETAIL_ACTIVITY) &&
                mCurrentActivityName.contains(WECHAT_LUCKMONEY_RECEIVE_ACTIVITY)) {
            mMutex = false;
            mLuckyMoneyPicked = false;
            mUnpackCount = 0;
            performGlobalAction(GLOBAL_ACTION_BACK);
            mSignature.commentString = generateCommentString();
        }
    }

    private String generateCommentString() {
        if (!mSignature.others) {
            return null;
        }

        Boolean needComment = SharedUtil.getBoolean(AppConstant.COMMENT_SWITCH, false);
        if (!needComment) {
            return null;
        }

        String[] wordsArray = SharedUtil.getString(AppConstant.COMMENT_WORDS).split(" +");
        if (wordsArray.length == 0) {
            return null;
        }

        Boolean atSender = SharedUtil.getBoolean(AppConstant.COMMENT_AT, false);
        if (atSender) {
            return "@" + mSignature.sender + " " + wordsArray[(int) (Math.random() * wordsArray.length)];
        } else {
            return wordsArray[(int) (Math.random() * wordsArray.length)];
        }
    }

    private boolean hasOneOfThoseNodes(String... texts) {
        List<AccessibilityNodeInfo> nodes;
        for (String text : texts) {
            if (text == null) {
                continue;
            }

            nodes = mRootNodeInfo.findAccessibilityNodeInfosByText(text);
            if (nodes != null && !nodes.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private AccessibilityNodeInfo findOpenButton(AccessibilityNodeInfo node) {
        if (node == null) {
            return null;
        }

        //非lyout元素
        if (node.getChildCount() == 0) {
            if ("android.widget.Button".equals(node.getClassName())) {
                return node;
            } else {
                return null;
            }
        }

        AccessibilityNodeInfo button;
        for (int i = 0; i < node.getChildCount(); i++) {
            button = findOpenButton(node.getChild(i));
            if (button != null) {
                return button;
            }
        }
        return null;
    }

    private AccessibilityNodeInfo getTheLastNode(String... texts) {
        int bottom = 0;
        AccessibilityNodeInfo lastNode = null;
        AccessibilityNodeInfo tempNode;
        List<AccessibilityNodeInfo> nodes;

        for (String text : texts) {
            if (text == null) {
                continue;
            }

            nodes = mRootNodeInfo.findAccessibilityNodeInfosByText(text);
            if (nodes != null && !nodes.isEmpty()) {
                tempNode = nodes.get(nodes.size() - 1);
                if (tempNode == null) {
                    return null;
                }

                Rect bounds = new Rect();
                tempNode.getBoundsInScreen(bounds);
                if (bounds.bottom > bottom) {
                    bottom = bounds.bottom;
                    lastNode = tempNode;
                    mSignature.others = text.endsWith(WECHAT_VIEW_OTHERS_CN);
                }
            }
        }

        return lastNode;
    }

    private void sendComment() {
        try {
            AccessibilityNodeInfo outNode = getRootInActiveWindow().getChild(0).getChild(0);
            AccessibilityNodeInfo nodeToInput = outNode.getChild(outNode.getChildCount() - 1).getChild(0).getChild(1);

            if ("android.widget.EditText".equals(nodeToInput.getClassName())) {
                Bundle arguments = new Bundle();
                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, mSignature.commentString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean watchNotifications(AccessibilityEvent event) {
        if (event.getEventType() != AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            return false;
        }

        String tip = event.getText().toString();
        if (!tip.contains(WECHAT_NOTIFICATION_TIP)) {
            return true;
        }

        Parcelable parcelable = event.getParcelableData();
        if (parcelable instanceof Notification) {
            Notification notification = (Notification) parcelable;
            try {
                //清楚对话，避免进入对话后误判
                mSignature.cleanSignature();
                notification.contentIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    private boolean watchList(AccessibilityEvent event) {
        if (mListMutex) {
            return false;
        }

        mListMutex = true;
        AccessibilityNodeInfo eventSource = event.getSource();

        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED || eventSource == null) {
            return false;
        }

        List<AccessibilityNodeInfo> nodes = eventSource.findAccessibilityNodeInfosByText(WECHAT_NOTIFICATION_TIP);
        if (!nodes.isEmpty() && mCurrentActivityName.contains(WECHAT_LUCKMONEY_GENERAL_ACTIVITY)) {
            AccessibilityNodeInfo nodeToClick = nodes.get(0);
            if (nodeToClick == null) {
                return false;
            }

            CharSequence contentDescription = nodeToClick.getContentDescription();
            if (contentDescription != null && !mSignature.getContentDescription().equals(contentDescription)) {
                nodeToClick.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                mSignature.setContentDescription(contentDescription.toString());
                return true;
            }
        }

        return false;
    }

    @Override
    public void onInterrupt() {

    }

    public void setCurrentActivityName(AccessibilityEvent event) {
        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return;
        }

        try {
            ComponentName componentName = new ComponentName(
                    event.getPackageName().toString(),
                    event.getClassName().toString()
            );
            getPackageManager().getActivityInfo(componentName, 0);
            mCurrentActivityName = componentName.flattenToShortString();
        } catch (PackageManager.NameNotFoundException e) {
            mCurrentActivityName = WECHAT_LUCKMONEY_GENERAL_ACTIVITY;
        }
    }

    private void watchFlagsFromPreference() {
        mPowerUtil = new PowerUtil(this);
        Boolean watchOnLockFlag = SharedUtil.getBoolean(AppConstant.WATCH_ON_LOCK, false);
        mPowerUtil.handleWakeLock(watchOnLockFlag);
    }
}
