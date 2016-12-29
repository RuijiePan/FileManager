package com.jiepier.filemanager.util;

import android.provider.MediaStore;

/**
 * Created by panruijie on 16/12/28.
 * Email : zquprj@gmail.com
 */

public class SortUtil {

    public enum SortMethod {
        NAME, SIZE, DATE, TYPE
    }

    public static String buildSortOrder(SortMethod sort) {
        String sortOrder = null;
        switch (sort) {
            case NAME:
                sortOrder = MediaStore.Files.FileColumns.TITLE + " asc";
                break;
            case SIZE:
                sortOrder = MediaStore.Files.FileColumns.SIZE + " asc";
                break;
            case DATE:
                sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED + " desc";
                break;
            case TYPE:
                sortOrder = MediaStore.Files.FileColumns.MIME_TYPE + " asc, " + MediaStore.Files.FileColumns.TITLE + " asc";
                break;
        }
        return sortOrder;
    }
}
