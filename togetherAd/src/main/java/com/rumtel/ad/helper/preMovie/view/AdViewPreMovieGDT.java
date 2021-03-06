package com.rumtel.ad.helper.preMovie.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.ifmvo.imageloader.ILFactory;
import com.ifmvo.imageloader.progress.LoaderOptions;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.NativeADEventListener;
import com.qq.e.ads.nativ.NativeADMediaListener;
import com.qq.e.ads.nativ.NativeADUnifiedListener;
import com.qq.e.ads.nativ.NativeUnifiedAD;
import com.qq.e.ads.nativ.NativeUnifiedADData;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;
import com.rumtel.ad.R;
import com.rumtel.ad.other.AdExtKt;
import com.rumtel.ad.other.AdNameType;

import java.util.ArrayList;
import java.util.List;


/*
 * (●ﾟωﾟ●)
 *
 * Created by Matthew_Chen on 2018/8/14.
 */
public class AdViewPreMovieGDT extends AdViewPreMovieBase {

    public AdViewPreMovieGDT(@NonNull Context context, boolean needTimer) {
        super(context, needTimer);
    }

    public AdViewPreMovieGDT(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdViewPreMovieGDT(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private NativeUnifiedADData mAD;

    @Override
    public void start(String locationId) {

        if (TextUtils.isEmpty(locationId)) {
            if (adViewListener != null) {
                adViewListener.onAdFailed("ID是空的");
            }
            return;
        }

        NativeADUnifiedListener listener = new NativeADUnifiedListener() {
            @Override
            public void onNoAD(AdError adError) {
                if (adViewListener != null) {
                    String errorMsg = adError != null ? adError.getErrorMsg() : "没有广告了";
                    adViewListener.onAdFailed(errorMsg);
                }
            }

            @Override
            public void onADLoaded(List<NativeUnifiedADData> list) {
                if (stop) {
                    return;
                }
                if (list == null || list.size() == 0) {
                    if (adViewListener != null) {
                        AdExtKt.logd(AdViewPreMovieGDT.this, "请求成功但是数量为空");
                        adViewListener.onAdFailed("请求成功但是数量为空");
                    }
                    return;
                }

                AdExtKt.logd(AdViewPreMovieGDT.this, "list.size():" + list.size());

                mAD = list.get(0);
                if (mAD == null) {
                    if (adViewListener != null) {
                        AdExtKt.logd(AdViewPreMovieGDT.this, "请求成功但是mAD==null");
                        adViewListener.onAdFailed("请求成功但是mAD==null");
                    }
                    return;
                }

                AdExtKt.logd(AdViewPreMovieGDT.this, "ecpm: " + mAD.getECPM() + ", ecpmLevel: " + mAD.getECPMLevel());
                mFlDesc.setVisibility(View.VISIBLE);
                mTvDesc.setText(mAD.getTitle());

//                isVideoAd = mAD.getAdPatternType() == AdPatternType.NATIVE_VIDEO;

                if (mAD.getAdPatternType() == AdPatternType.NATIVE_2IMAGE_2TEXT || mAD.getAdPatternType() == AdPatternType.NATIVE_3IMAGE) {
                    mLlAdContainer.setVisibility(View.VISIBLE);
                    mIvImg0.setVisibility(View.VISIBLE);
                    try {
                        ILFactory.getLoader().load(AdViewPreMovieGDT.super.getContext(), mIvImg0, mAD.getImgUrl(), new LoaderOptions());
                    } catch (Exception e) {
                        //忽略即可
                    }
                } else if (mAD.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                    mMediaView.setVisibility(View.VISIBLE);
                }

                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(nativeAdContainer);
                clickableViews.add(mIvImg0);
                clickableViews.add(mMediaView);
                mAD.bindAdToView(AdViewPreMovieGDT.super.getContext(), nativeAdContainer, null, clickableViews);
                mAD.setNativeAdEventListener(new NativeADEventListener() {
                    @Override
                    public void onADExposed() {
                        AdExtKt.logd(AdViewPreMovieGDT.this, AdNameType.GDT.getType() + ":前贴：" + AdViewPreMovieGDT.super.getContext().getString(R.string.exposure));
                    }

                    @Override
                    public void onADClicked() {
                        AdExtKt.logd(AdViewPreMovieGDT.this, AdNameType.GDT.getType() + ":前贴：" + AdViewPreMovieGDT.super.getContext().getString(R.string.clicked));
                        if (adViewListener != null) {
                            adViewListener.onAdClick();
                        }
                    }

                    @Override
                    public void onADError(AdError adError) {
                        if (adViewListener != null) {
                            adViewListener.onAdFailed(adError.getErrorMsg());
                        }
                    }

                    @Override
                    public void onADStatusChanged() {
                    }
                });

                if (mAD.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                    VideoOption videoOption = new VideoOption.Builder().setAutoPlayMuted(true).setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS).build();
                    mAD.bindMediaView(mMediaView, videoOption, new NativeADMediaListener() {
                        @Override
                        public void onVideoInit() {
                            AdExtKt.logd(AdViewPreMovieGDT.this, AdNameType.GDT.getType() + "：onVideoInit");
                        }

                        @Override
                        public void onVideoLoading() {
                            AdExtKt.logd(AdViewPreMovieGDT.this, AdNameType.GDT.getType() + "：onVideoLoading");
                        }

                        @Override
                        public void onVideoReady() {
                            AdExtKt.logd(AdViewPreMovieGDT.this, AdNameType.GDT.getType() + "：onVideoReady");
                        }

                        @Override
                        public void onVideoLoaded(int i) {
                            AdExtKt.logd(AdViewPreMovieGDT.this, AdNameType.GDT.getType() + "：onVideoLoaded:" + i);
                        }

                        @Override
                        public void onVideoStart() {
                            AdExtKt.logd(AdViewPreMovieGDT.this, AdNameType.GDT.getType() + "：onVideoStart");
                        }

                        @Override
                        public void onVideoPause() {
                            AdExtKt.logd(AdViewPreMovieGDT.this, AdNameType.GDT.getType() + "：onVideoPause");
                        }

                        @Override
                        public void onVideoResume() {
                            AdExtKt.logd(AdViewPreMovieGDT.this, AdNameType.GDT.getType() + "：onVideoResume");
                        }

                        @Override
                        public void onVideoCompleted() {
                            AdExtKt.logd(AdViewPreMovieGDT.this, AdNameType.GDT.getType() + "：onVideoCompleted");
                            //视频广告的情况下，播放完成之后，自动消失 && 需要倒计时的情况（没有倒计时的情况，不自动消失）
                            if (needTimer && adViewListener != null) {
                                adViewListener.onAdDismissed();
                            }
                        }

                        @Override
                        public void onVideoError(AdError adError) {
                            if (adViewListener != null) {
                                adViewListener.onAdFailed(adError.getErrorMsg());
                            }
                            AdExtKt.logd(AdViewPreMovieGDT.this, AdNameType.GDT.getType() + "：onVideoError");
                        }

                        @Override
                        public void onVideoStop() {
                            AdExtKt.logd(AdViewPreMovieGDT.this, AdNameType.GDT.getType() + "：onVideoStop");
                        }

                        @Override
                        public void onVideoClicked() {
                            AdExtKt.logd(AdViewPreMovieGDT.this, AdNameType.GDT.getType() + "：onVideoClicked");
                        }
                    });
                    mAD.startVideo();
                }
                if (adViewListener != null) {
                    adViewListener.onAdPrepared();
                }
                //开始计时
                if (needTimer) {
                    startTimerCount(6000);
                }
            }
        };

        NativeUnifiedAD mAdManager = new NativeUnifiedAD(super.getContext(), locationId, listener);
        //有效值就是 5-60
        mAdManager.setMaxVideoDuration(60);
        mAdManager.setMinVideoDuration(5);
        mAdManager.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO); // 本次拉回的视频广告，在用户看来是否为自动播放的
        mAdManager.setVideoADContainerRender(VideoOption.VideoADContainerRender.SDK); // 视频播放前，用户看到的广告容器是由SDK渲染的
        mAdManager.loadData(1);
    }

    @Override
    public void resume() {
        if (mAD != null) {
            mAD.resume();
            if (mAD.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                mAD.resumeVideo();
            }
        }
    }

    @Override
    public void pause() {
        if (mAD != null) {
            if (mAD.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                mAD.pauseVideo();
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (mAD != null) {
            mAD.destroy();
            mAD = null;
        }
    }
}
