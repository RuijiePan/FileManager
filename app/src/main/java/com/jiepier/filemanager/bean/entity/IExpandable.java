package com.jiepier.filemanager.bean.entity;

import java.util.List;

/**
 * implement the interface if the item is expandable
 * Created by panruijie on 2017/3/3.
 * Email : zquprj@gmail.com
 */

public interface IExpandable<T> {
    boolean isExpanded();

    void setExpanded(boolean expanded);

    List<T> getSubItems();

    /**
     * Get the level of this item. The level start from 0.
     * If you don't care about the level, just return a negative.
     */
    int getLevel();
}
