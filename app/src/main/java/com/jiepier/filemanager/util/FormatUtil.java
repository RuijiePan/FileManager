package com.jiepier.filemanager.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.util.Locale;

/**
 * 文件尺寸转换
 */
public class FormatUtil {

    /**
     * 单位
     */
    public static enum Unit {
        B("B", "B"), KB("KB", "K"), MB("MB", "M"), GB("GB", "G");

        public String mFullValue;
        public String mShortValue;

        private Unit(String fullValue, String shortValue) {
            mFullValue = fullValue;
            mShortValue = shortValue;
        }
    }

    /**
     * 文件大小
     */
    public static class FileSize {
        public String mSize;
        public Unit mUnit;

        public FileSize(String size, Unit unit) {
            mSize = size;
            mUnit = unit;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            return sb.append(mSize).append(" ").append(mUnit.mShortValue)
                    .toString();
        }

        /**
         * 返回全称单位的字符串
         */
        public String toFullString() {
            StringBuilder sb = new StringBuilder();
            return sb.append(mSize).append(" ").append(mUnit.mFullValue)
                    .toString();
        }
    }

    /**
     * 用于转换文件单位
     *
     * @param size 以Byte为单位的文件大小
     * @return FileSize 包含了文件转换后的大小及单位
     */
    @SuppressLint("DefaultLocale")
    public static FileSize formatFileSize(long size) {
        return formatFileSize(size, null);
    }

    /**
     * 只有超过1024才回生成单位的方法
     *
     * @param size
     * @return
     */
    public static FileSize formatFileSizeBy1024(long size) {
        return formatFileSizeBy1024(size, null);
    }


    /**
     * 只有超过1024才回生成单位的方法
     *
     * @param size
     * @return
     */
    public static FileSize formatFileSizeBy1024(long size, Locale locale) {

        float result = size;
        Unit unit = Unit.B;
        if (result >= 1024) {
            unit = Unit.KB;
            result = result / 1024;
        }
        if (result >= 1024) {
            unit = Unit.MB;
            result = result / 1024;
        }
        if (result >= 1024) {
            unit = Unit.GB;
            result = result / 1024;
        }
        String value = null;
        switch (unit) {
            case B:
            case KB:
                value = String.valueOf((int) result);
                break;
            case MB:
                if (locale == null) {
                    value = String.format("%.1f", result);
                } else {
                    value = String.format(locale, "%.1f", result);
                }
                break;
            case GB:
                if (locale == null) {
                    value = String.format("%.2f", result);
                } else {
                    value = String.format(locale, "%.2f", result);
                }
                break;
            default:
                break;
        }
        return new FileSize(value, unit);


    }

    /**
     * 用于转换文件单位
     *
     * @param size 以Byte为单位的文件大小
     * @return FileSize 包含了文件转换后的大小及单位
     */
    @SuppressLint("DefaultLocale")
    public static FileSize formatFileSize(long size, Locale locale) {
        float result = size;
        Unit unit = Unit.B;
        if (result > 900) {
            unit = Unit.KB;
            result = result / 1024;
        }
        if (result > 900) {
            unit = Unit.MB;
            result = result / 1024;
        }
        if (result > 900) {
            unit = Unit.GB;
            result = result / 1024;
        }
        String value = null;
        switch (unit) {
            case B:
            case KB:
                value = String.valueOf((int) result);
                break;
            case MB:
                if (locale == null) {
                    value = String.format("%.1f", result);
                } else {
                    value = String.format(locale, "%.1f", result);
                }
                break;
            case GB:
                if (locale == null) {
                    value = String.format("%.2f", result);
                } else {
                    value = String.format(locale, "%.2f", result);
                }
                break;
            default:
                break;
        }
        return new FileSize(value, unit);
    }

    /**
     * RAM单位换算.<br>
     *
     * @param ramSize (KB)
     * @return
     */
    public static FileSize convertRamSize(long ramSize) {
        final FileSize fileSize = FormatUtil
                .formatFileSize(ramSize * 1024);
        if (TextUtils.isEmpty(fileSize.mSize)) {
            fileSize.mUnit = FormatUtil.Unit.KB;
        }
        return fileSize;
    }

}
