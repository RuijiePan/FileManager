package com.jiepier.filemanager.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.bean.entity.LanguageType;
import com.jiepier.filemanager.constant.AppConstant;
import com.jiepier.filemanager.event.LanguageEvent;
import com.jiepier.filemanager.util.RxBus.RxBus;

import java.util.Locale;

/**
 * Created by panruijie on 2017/3/15.
 * Email : zquprj@gmail.com
 */

public class LanguageUtil {

    private static LanguageUtil sInstacne;
    private Context mContext;

    public static void init(Context mContext) {
        if (sInstacne == null) {
            synchronized (LanguageUtil.class) {
                if (sInstacne == null) {
                    sInstacne = new LanguageUtil(mContext);
                }
            }
        }
    }

    public static LanguageUtil getInstance() {

        if (sInstacne == null) {
            throw new IllegalStateException("You must be init LanguageUtil first");
        }
        return sInstacne;
    }

    private LanguageUtil(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void setConfiguration() {
        Locale targetLocale = getTragetLocale();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Locale.setDefault(targetLocale);
        } else {
            Resources resources = mContext.getResources();
            Configuration config = resources.getConfiguration();
            DisplayMetrics dm = resources.getDisplayMetrics();
            config.locale = targetLocale;
            resources.updateConfiguration(config, dm);
        }

    }

    /**
     * 如果不是英文、简体中文、繁体中文，默认返回英文
     *
     * @return
     */
    private Locale getTragetLocale() {
        int userType = SharedUtil.getInt(mContext, AppConstant.LANGUAGE);

        if (userType == LanguageType.FOLLOW_SYSTEM) {
            Locale sysType = getSysLocale();
            if (sysType.equals(Locale.ENGLISH)) {
                return Locale.ENGLISH;
            } else if (sysType.equals(Locale.SIMPLIFIED_CHINESE)) {
                return Locale.SIMPLIFIED_CHINESE;
            } else if (sysType.equals(Locale.TRADITIONAL_CHINESE)) {
                return Locale.TRADITIONAL_CHINESE;
            }
            return Locale.ENGLISH;
        } else if (userType == LanguageType.ENGLISH) {
            return Locale.ENGLISH;
        } else if (userType == LanguageType.SIMPLE_CHINISE) {
            return Locale.SIMPLIFIED_CHINESE;
        } else if (userType == LanguageType.TRADITIONAL_CHINESE) {
            return Locale.TRADITIONAL_CHINESE;
        }

        return Locale.ENGLISH;
    }

    //6.0以上获取方式需要特殊处理一下
    private Locale getSysLocale() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return mContext.getResources().getConfiguration().locale;
        } else {
            return mContext.getResources().getConfiguration().getLocales().get(0);
        }
    }

    public void updateLanguage(int languageType) {
        if (languageType != SharedUtil.getInt(mContext, AppConstant.LANGUAGE)) {
            SharedUtil.putInt(mContext, AppConstant.LANGUAGE, languageType);
            RxBus.getDefault().post(new LanguageEvent());
        }
    }

    public String getLanguageName() {
        int languageType = SharedUtil.getInt(mContext, AppConstant.LANGUAGE);
        if (languageType == LanguageType.ENGLISH) {
            return mContext.getString(R.string.settings_language_english);
        } else if (languageType == LanguageType.SIMPLE_CHINISE) {
            return mContext.getString(R.string.settings_language_simple_chinise);
        } else if (languageType == LanguageType.TRADITIONAL_CHINESE) {
            return mContext.getString(R.string.settings_language_traditional_chinise);
        }

        return mContext.getString(R.string.settings_language_follow_system);
    }
}
