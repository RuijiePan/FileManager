package com.jiepier.filemanager.ui.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.ui.common.CommonFragment;
import com.jiepier.filemanager.util.Settings;

import java.util.ArrayList;

/**
 * Created by panruijie on 17/1/2.
 * Email : zquprj@gmail.com
 */

public abstract class BaseCategoryFragment extends BaseFragment {

    protected ArrayList<String> mList;

    public static CommonFragment newInstance(ArrayList<String> apkList){
        CommonFragment instance = new CommonFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("list",apkList);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = getArguments().getStringArrayList("list");
        //mList = getArguments() != null ? getArguments().getStringArrayList("list"):new ArrayList<>();
    }
}
