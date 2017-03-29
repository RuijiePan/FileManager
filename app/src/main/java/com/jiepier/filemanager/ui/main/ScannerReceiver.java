package com.jiepier.filemanager.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jiepier.filemanager.manager.CategoryManager;
import com.jiepier.filemanager.util.Loger;

/**
 * Created by panruijie on 2017/3/29.
 * Email : zquprj@gmail.com
 */

public class ScannerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)) {
            Loger.w("ScannerReceiver", "Start scan file");
        }
        // handle intents related to external storage
        else if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) {
            CategoryManager.getInstance().update();
            Log.w("ScannerReceiver", "Sacn finish");
        }
    }
}