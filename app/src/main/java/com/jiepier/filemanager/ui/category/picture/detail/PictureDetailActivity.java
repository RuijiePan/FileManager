package com.jiepier.filemanager.ui.category.picture.detail;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.BaseActivity;
import com.jiepier.filemanager.ui.category.picture.PictureAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by panruijie on 17/1/22.
 * Email : zquprj@gmail.com
 */

public class PictureDetailActivity extends BaseActivity {

    public static final String EXTRA_IMAGE_POSITION = "image_position";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    private List<String> mUrlList;
    private int mPosition;
    private PictureDetailAdapter mAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imagepager)
    ViewPager imagepager;
    @BindView(R.id.tv_index)
    TextView tvIndex;

    @Override
    public int initContentView() {
        return R.layout.activity_picture_detail;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        if (null != toolbar) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void initUiAndListener() {
        mUrlList = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
        mPosition = getIntent().getIntExtra(EXTRA_IMAGE_POSITION,0);

        mAdapter = new PictureDetailAdapter(getSupportFragmentManager(),mUrlList);
        imagepager.setAdapter(mAdapter);

        imagepager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvIndex.setText(position+1+"/"+mUrlList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        imagepager.setCurrentItem(mPosition);
        tvIndex.setText(mPosition+1+"/"+mUrlList.size());
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }

}
