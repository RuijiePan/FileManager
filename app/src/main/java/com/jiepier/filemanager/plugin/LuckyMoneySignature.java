package com.jiepier.filemanager.plugin;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by panruijie on 2017/3/17.
 * Email : zquprj@gmail.com
 */

public class LuckyMoneySignature {

    public String sender;
    public String content;
    public String time;
    public String contentDescription = "";
    public String commentString;
    public boolean others;

    public boolean generateSignature(AccessibilityNodeInfo node, String excludeWords) {
        try {
            //布局的根结点必须是linearlayout才可以。
            AccessibilityNodeInfo luckyMoneyNode = node.getParent();
            if (!"android.widget.LinearLayout".equals(luckyMoneyNode.getClassName())) {
                return false;
            }

            String luckyMoneyContent = luckyMoneyNode.getChild(0).getText().toString();
            if (luckyMoneyContent == null || "查看红包".equals(luckyMoneyContent)) {
                return false;
            }

            String[] excludeWordsArray = excludeWords.split(" +");
            for (String word : excludeWordsArray) {
                if (word.length() > 0 && luckyMoneyContent.contains(word)) {
                    return false;
                }
            }

            AccessibilityNodeInfo messageNode = luckyMoneyNode.getParent();
            Rect bounds = new Rect();
            messageNode.getBoundsInScreen(bounds);
            if (bounds.top < 0) {
                return false;
            }

            String[] luckyMoneyInfo = getSenderContentDescriptionFromNode(messageNode);
            if (getSignature(luckyMoneyInfo[0], luckyMoneyContent, luckyMoneyInfo[1]).equals(this.toString())) {
                return false;
            }

            this.sender = luckyMoneyInfo[0];
            this.time = luckyMoneyInfo[1];
            this.content = luckyMoneyContent;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String[] getSenderContentDescriptionFromNode(AccessibilityNodeInfo node) {
        int count = node.getChildCount();
        String[] result = {"unknowSender", "unknowTime"};

        for (int i = 0; i < count; i++) {
            AccessibilityNodeInfo info = node.getChild(i);
            if ("android.widget.ImageView".equals(info.getClassName()) && "unknowSender".equals(result[0])) {
                CharSequence contentDescription = info.getContentDescription();
                if (contentDescription != null) {
                    result[0] = contentDescription.toString().replaceAll("头像$", "");
                }
            } else if ("android.widget.TextView".equals(info.getClassName()) && "unknownTime".equals(result[1])) {
                CharSequence nodeText = info.getText();
                if (nodeText != null) {
                    result[1] = nodeText.toString();
                }
            }
        }

        return result;
    }

    private String getSignature(String... strings) {
        String signature = "";
        for (String str : strings) {
            if (str == null) {
                return null;
            }
            signature += str + "|";
        }

        return signature.substring(0, signature.length() - 1);
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public void setContentDescription(String contentDescription) {
        this.contentDescription = contentDescription;
    }

    public void cleanSignature() {
        this.content = "";
        this.time = "";
        this.sender = "";
    }
}