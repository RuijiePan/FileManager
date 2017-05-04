package com.jiepier.filemanager.ui.category.memory.accessibility;

import android.content.Context;

import com.jiepier.filemanager.event.ForceStopFinishEvent;
import com.jiepier.filemanager.util.AccessibilityUtil;
import com.jiepier.filemanager.util.Loger;
import com.jiepier.filemanager.util.RxBus.RxBus;

import java.util.Iterator;
import java.util.Set;


/**
 * Created by panruijie on 2017/3/31.
 * Email : zquprj@gmail.com
 * 辅助功能加速管理类
 */

public class MemoryAccessibilityManager {

    public static MemoryAccessibilityManager sInstance;
    private Iterator mIterator;
    private Context mContext;

    public static MemoryAccessibilityManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (MemoryAccessibilityManager.class) {
                if (sInstance == null) {
                    sInstance = new MemoryAccessibilityManager(context);
                }
            }
        }
        return sInstance;
    }

    public void startTask(Set<String> pckNameSet) {
        mIterator = pckNameSet.iterator();
        String pckName = (String) mIterator.next();
        AccessibilityUtil.gotoInstalledAppDetails(mContext, pckName);

        for (String pckName1 : pckNameSet) {
            Loger.w("ruijie", pckName1);
        }
    }

    private MemoryAccessibilityManager(Context context) {
        mContext = context;

        RxBus.getDefault().IoToUiObservable(ForceStopFinishEvent.class)
                .subscribe(event -> {
                    String pckName = (String) mIterator.next();
                    if (pckName != null) {
                        Loger.w("ruijie", pckName);
                        AccessibilityUtil.gotoInstalledAppDetails(context, pckName);
                    }
                }, Throwable::printStackTrace);
    }
}
