package com.rumtel.ad.helper.preMovie.view;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.ifmvo.imageloader.ILFactory;
import com.ifmvo.imageloader.progress.LoaderOptions;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.NativeADEventListener;
import com.qq.e.ads.nativ.NativeADMediaListener;
import com.qq.e.ads.nativ.NativeADUnifiedListener;
import com.qq.e.ads.nativ.NativeUnifiedAD;
import com.qq.e.ads.nativ.NativeUnifiedADData;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
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
public class AdViewRewardVideoGDT extends AdViewVerticalPreMovieBase {

    public AdViewRewardVideoGDT(@NonNull Context context, boolean needTimer) {
        super(context, needTimer);
    }

    public AdViewRewardVideoGDT(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdViewRewardVideoGDT(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private RewardVideoAD rewardVideoAD;
    private boolean adLoaded;//广告加载成功标志
    private boolean videoCached;//视频素材文件下载完成标志

    @Override
    public void start(String locationId) {
        RewardVideoADListener listener = new RewardVideoADListener() {
            @Override
            public void onADLoad() {
                if (!rewardVideoAD.hasShown()) {//广告展示检查2：当前广告数据还没有展示过
                    long delta = 1000;//建议给广告过期时间加个buffer，单位ms，这里demo采用1000ms的buffer
                    //广告展示检查3：展示广告前判断广告数据未过期
                    if (SystemClock.elapsedRealtime() < (rewardVideoAD.getExpireTimestamp() - delta)) {
                            rewardVideoAD.showAD();
                    } else {
                        AdExtKt.logd(AdViewRewardVideoGDT.this,"激励视频广告已过期，请再次请求广告后进行广告展示");
                        adViewListener.onAdFailed("激励视频广告已过期，请再次请求广告后进行广告展示");
                    }
                } else {
                    AdExtKt.logd(AdViewRewardVideoGDT.this,"此条广告已经展示过，请再次请求广告后进行广告展示！");
                    adViewListener.onAdFailed("此条广告已经展示过，请再次请求广告后进行广告展示");
                }
            }

            @Override
            public void onVideoCached() {
                AdExtKt.logd(AdViewRewardVideoGDT.this,"onVideoCached");
            }

            @Override
            public void onADShow() {
                AdExtKt.logd(AdViewRewardVideoGDT.this,"onADShow");
                adViewListener.onAdPrepared();
            }

            @Override
            public void onADExpose() {
                AdExtKt.logd(AdViewRewardVideoGDT.this,"onADExpose");
                adViewListener.onExposured();
            }

            @Override
            public void onReward() {
                AdExtKt.logd(AdViewRewardVideoGDT.this,"onReward");
            }

            @Override
            public void onADClick() {
                AdExtKt.logd(AdViewRewardVideoGDT.this,"onADClick");
                adViewListener.onAdClick();
            }

            @Override
            public void onVideoComplete() {
                AdExtKt.logd(AdViewRewardVideoGDT.this,"onVideoComplete");
                adViewListener.onAdDismissed();
            }

            @Override
            public void onADClose() {
                AdExtKt.logd(AdViewRewardVideoGDT.this,"onADClose");
                adViewListener.onAdDismissed();
            }

            @Override
            public void onError(AdError adError) {
                AdExtKt.logd(AdViewRewardVideoGDT.this,"onError");
                adViewListener.onAdFailed(adError.getErrorMsg());
            }
        };
        rewardVideoAD = new RewardVideoAD(super.getContext(), locationId, listener, true);
        adLoaded = false;
        videoCached = false;
        rewardVideoAD.loadAD();
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
