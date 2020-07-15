package com.rumtel.ad.helper.preMovie.view

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.bytedance.sdk.openadsdk.*

import com.ifmvo.imageloader.ILFactory
import com.rumtel.ad.other.*
import com.rumtel.ad.other.AdNameType

import java.util.ArrayList


/*
 * (●ﾟωﾟ●) 穿山甲
 *
 * Created by Matthew_Chen on 2018/8/14.
 */
class AdViewVerticalPreMovieCsj : AdViewVerticalPreMovieBase {
    var activity: Activity? = null

    constructor(context: Activity, needTimer: Boolean) : super(context, needTimer) {
        activity = context
    }

    constructor(context: Activity, attrs: AttributeSet?) : super(context, attrs) {
        activity = context
    }

    constructor(context: Activity, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        activity = context
    }

    override fun start(locationId: String) {
        try {
            var adSlot: AdSlot
            adSlot = AdSlot.Builder()
                .setCodeId(locationId)
                .setSupportDeepLink(true)
                .setOrientation(TTAdConstant.VERTICAL)//必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build()
            var mTTAdNative: TTAdNative = TTAdSdk.getAdManager().createAdNative(activity)
            mTTAdNative.loadFullScreenVideoAd(
                adSlot,
                object : TTAdNative.FullScreenVideoAdListener {
                    override fun onError(code: Int, message: String) {
                        logd("Callback --> onError: $code, $message")
                        if (adViewListener != null) {
                            adViewListener.onAdFailed(message)
                        }
                    }

                    override fun onFullScreenVideoAdLoad(ad: TTFullScreenVideoAd) {
                        logd("Callback --> onFullScreenVideoAdLoad")
                        if (stop) {
                            return
                        }
                        if (adViewListener != null) {
                            adViewListener.onAdPrepared()
                        }
                        ad.setFullScreenVideoAdInteractionListener(object :
                            TTFullScreenVideoAd.FullScreenVideoAdInteractionListener {

                            override fun onAdShow() {
                                logd("Callback --> onAdShow")
                                if (adViewListener != null) {
                                    adViewListener.onExposured()
                                }
                            }

                            override fun onAdVideoBarClick() {
                                logd("Callback --> onAdVideoBarClick")
                                if (adViewListener != null) {
                                    adViewListener.onAdClick()
                                }
                            }

                            override fun onAdClose() {
                                logd("Callback --> onAdClose")
                                if (needTimer && adViewListener != null) {
                                    adViewListener.onAdDismissed()
                                }
                            }

                            override fun onVideoComplete() {
                                logd("Callback --> onVideoComplete")
                                /*if (needTimer && adViewListener != null) {
                                    adViewListener.onAdDismissed()
                                }*/
                            }

                            override fun onSkippedVideo() {
                                logd("Callback --> onSkippedVideo")
                                /*if (needTimer && adViewListener != null) {
                                    adViewListener.onAdDismissed()
                                }*/
                            }
                        })
                        ad.showFullScreenVideoAd(activity)
                    }

                    override fun onFullScreenVideoCached() {
                        logd("Callback --> onFullScreenVideoCached")
                    }
                })
        } catch (e: Exception) {
            logd("崩溃异常")
            if (adViewListener != null) {
                adViewListener.onAdFailed("崩溃异常")
            }
        }

    }

    override fun resume() {

    }

    override fun pause() {

    }

    override fun destroy() {
        super.destroy()
    }
}
