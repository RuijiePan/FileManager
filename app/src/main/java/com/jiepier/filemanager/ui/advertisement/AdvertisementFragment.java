package com.jiepier.filemanager.ui.advertisement;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.jiepier.filemanager.R;
import com.jiepier.filemanager.base.App;
import com.jiepier.filemanager.base.BaseFragment;
import com.jiepier.filemanager.manager.AdManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JiePier on 16/12/7.
 */

public class AdvertisementFragment extends BaseFragment implements AdListener {

    @BindView(R.id.native_ad_icon)
    ImageView nativeAdIcon;
    @BindView(R.id.native_ad_title)
    TextView nativeAdTitle;
    @BindView(R.id.native_ad_media)
    MediaView nativeAdMedia;
    @BindView(R.id.native_ad_social_context)
    TextView nativeAdSocialContext;
    @BindView(R.id.native_ad_body)
    TextView nativeAdBody;
    @BindView(R.id.native_ad_call_to_action)
    Button nativeAdCallToAction;
    @BindView(R.id.ad_choices_container)
    LinearLayout adChoicesContainer;
    @BindView(R.id.native_ad_container)
    LinearLayout nativeAdContainer;
    private NativeAd mNativeAd;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_advertisement;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {
        mNativeAd = AdManager.getInstance(getContext()).getNativeAd();
        //mNativeAd.loadAd();
    }

    @Override
    protected void initListeners() {
        mNativeAd.setAdListener(this);
    }

    @Override
    public void onError(Ad ad, AdError adError) {
        Log.w(TAG,"error");
        Log.w(TAG,adError.getErrorMessage());
    }

    @Override
    public void onAdLoaded(Ad ad) {
        Log.w(TAG,"load");

        // Set the Text.
        nativeAdTitle.setText(mNativeAd.getAdTitle());
        nativeAdSocialContext.setText(mNativeAd.getAdSocialContext());
        nativeAdBody.setText(mNativeAd.getAdBody());
        nativeAdCallToAction.setText(mNativeAd.getAdCallToAction());

        // Download and display the ad icon.
        NativeAd.Image adIcon = mNativeAd.getAdIcon();
        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Download and display the cover image.
        nativeAdMedia.setNativeAd(mNativeAd);

        AdChoicesView adChoicesView = new AdChoicesView(getContext(), mNativeAd, true);
        adChoicesContainer.addView(adChoicesView);

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        mNativeAd.registerViewForInteraction(nativeAdContainer, clickableViews);
    }

    @Override
    public void onAdClicked(Ad ad) {

    }

}
