package com.jiepier.filemanager.ui.category.picture.detail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.App;
import com.jiepier.filemanager.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by panruijie on 17/1/22.
 * Email : zquprj@gmail.com
 */

public class PictureDetailFragment extends BaseFragment {

    @BindView(R.id.image)
    PhotoView image;
    @BindView(R.id.memory_Progressbar)
    ContentLoadingProgressBar memoryProgressbar;
    private String mImageUrl;

    public static PictureDetailFragment newInstance(String imageUrl) {
        final PictureDetailFragment f = new PictureDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
        getArguments().remove("url");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_picture_detail;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        Glide.with(App.getAppContext())
                .load("file://" + mImageUrl)
                .fitCenter()
                //禁止磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //禁止内存缓存
                //.skipMemoryCache( true )
                //.placeholder(R.drawable.image_loading)
                .error(R.drawable.image_load_failure)
                .into(new GlideDrawableImageViewTarget(image) {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        memoryProgressbar.show();
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        memoryProgressbar.hide();
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        memoryProgressbar.hide();
                    }
                });
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

}
