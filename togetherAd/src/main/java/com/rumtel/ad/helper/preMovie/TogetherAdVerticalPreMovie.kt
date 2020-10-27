package com.rumtel.ad.helper.preMovie

import android.app.Activity
import android.support.annotation.NonNull
import android.view.View
import android.view.ViewGroup
import com.rumtel.ad.R
import com.rumtel.ad.TogetherAd
import com.rumtel.ad.helper.AdBase
import com.rumtel.ad.helper.preMovie.view.*
import com.rumtel.ad.other.*
import com.rumtel.ad.other.logd
import com.rumtel.ad.other.loge
import java.lang.ref.WeakReference
import java.util.*

/* 
 * (●ﾟωﾟ●) 前贴的广告 （ 视频播放之前展示、可配置是否倒计时 ）
 * 
 * Created by Matthew_Chen on 2018/8/17.
 */
object TogetherAdVerticalPreMovie : AdBase() {

    private var weak: WeakReference<AdViewVerticalPreMovieBase>? = null
    private var mChannel: String = ""

    fun showAdVerticalPreMovie(@NonNull activity: Activity, configVerticalPreMovie: String?, @NonNull adConstStr: String,
                               @NonNull adsParentLayout: ViewGroup, @NonNull adListener: AdListenerVerticalPreMovie, @NonNull needTimer: Boolean = true,
                               @NonNull present: String) {
        startTimerTask(activity, adsParentLayout, adListener)
        //如果存在，首先销毁上一个广告
        destroy()
        adsParentLayout.visibility = View.VISIBLE
        if (adsParentLayout.childCount > 0) {
            adsParentLayout.removeAllViews()
        }
        when (AdRandomUtil.getRandomAdName(configVerticalPreMovie)) {
            AdNameType.GDT -> {
                showAdVerticalPreMovieGDT(activity, needTimer,present)
            }
            AdNameType.CSJ -> {
                showAdVerticalPreMovieCsj(activity, needTimer)
            }
            else -> {
                cancelTimerTask()
                loge(activity.getString(R.string.all_ad_error))
                adListener.onAdFailed(activity.getString(R.string.all_ad_error))
                destroy()
                adsParentLayout.removeAllViews()
                return
            }
        }
        adListener.onStartRequest(mChannel)
        val adView = weak?.get()
        if (adView != null) {
            adsParentLayout.addView(adView)
        }
        adView?.setAdViewVerticalPreMovieListener(object : AdViewVerticalPreMovieBase.AdViewVerticalPreMovieListener {
            override fun onExposured() {
                logd("$mChannel: ${activity.getString(R.string.exposure)}")
            }

            override fun onAdClick() {
                adListener.onAdClick(mChannel)
                logd("$mChannel: ${activity.getString(R.string.clicked)}")
            }

            override fun onAdFailed(failedMsg: String) {
                loge("$mChannel: $failedMsg")

                activity.runOnUiThread {
                    adListener.onAdFailedSingle(mChannel, failedMsg)
                }

                var newConfigPreMovie: String? = null
                when (mChannel) {
                    AdNameType.GDT.type -> {
                        newConfigPreMovie = configVerticalPreMovie?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                    }
                    AdNameType.CSJ.type -> {
                        newConfigPreMovie = configVerticalPreMovie?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                    }
                    else -> {
                        adListener.onAdFailed(failedMsg)
                    }
                }
                activity.runOnUiThread {
                    showAdVerticalPreMovie(activity, newConfigPreMovie, adConstStr, adsParentLayout, adListener, needTimer,present)
                }
            }

            override fun onAdDismissed() {
                adListener.onAdDismissed()
                /**
                 * 这里不能销毁广告
                 * 如果销毁广告，广告详情页就无法正常观看了
                 * Activity 的 onDestroy 会将其销毁
                 * 并且下次再请求广告也会先销毁上一个广告
                 * 所以不用担心内存泄漏
                 */
                adsParentLayout.removeAllViews()
                adsParentLayout.visibility = View.GONE
                logd("$mChannel: ${activity.getString(R.string.dismiss)}")
            }

            override fun onAdPrepared() {
                adListener.onAdPrepared(mChannel)
                adsParentLayout.visibility = View.VISIBLE
                cancelTimerTask()
                logd("$mChannel: ${activity.getString((R.string.prepared))}")
            }
        })?.start(getLocationIdFromMap(adConstStr))
    }

    private fun getLocationIdFromMap(adConstStr: String): String? {
        return when (mChannel) {
            AdNameType.GDT.type -> {
                TogetherAd.idMapGDT[adConstStr]
            }
            AdNameType.CSJ.type -> {
                TogetherAd.idMapCsj[adConstStr]
            }
            else -> {
                loge("发生了不可能的灵异事件")
                ""
            }
        }
    }


    /**
     * 腾讯广点通
     */
    private fun showAdVerticalPreMovieGDT(activity: Activity, @NonNull needTimer: Boolean, @NonNull present: String) {
        mChannel = AdNameType.GDT.type
        when (present) {
            AdPresentType.RewardVideo.present -> {
                weak = WeakReference(AdViewRewardVideoGDT(activity,needTimer))
            }
            AdPresentType.NativeVideo.present -> {
                weak = WeakReference(AdViewVerticalPreMovieGDT(activity, needTimer))
            }
        }
    }

    /**
     * 穿山甲
     */
    private fun showAdVerticalPreMovieCsj(activity: Activity, @NonNull needTimer: Boolean) {
        mChannel = AdNameType.CSJ.type
        weak = WeakReference(AdViewVerticalPreMovieCsj(activity, needTimer))
    }

    private var timer: Timer? = null
    private var overTimerTask: OverTimerTask? = null

    /**
     * 取消计时任务
     */
    private fun cancelTimerTask() {
        timer?.cancel()
        overTimerTask?.cancel()
    }

    /**
     * 开始计时任务
     */
    private fun startTimerTask(activity: Activity, adsParentLayout: ViewGroup, adListener: AdListenerVerticalPreMovie) {
        cancelTimerTask()
        timer = Timer()
        overTimerTask = OverTimerTask(activity, adsParentLayout, adListener)
        timer?.schedule(overTimerTask, TogetherAd.timeOutMillis)
    }

    /**
     * 请求超时处理的任务
     */
    private class OverTimerTask(activity: Activity, adsParentLayout: ViewGroup, adListener: AdListenerVerticalPreMovie) : TimerTask() {

        private val weakReference: WeakReference<AdListenerVerticalPreMovie>?
        private val weakRefContext: WeakReference<Activity>?
        private val weakRefView: WeakReference<ViewGroup>?

        init {
            weakReference = WeakReference(adListener)
            weakRefContext = WeakReference(activity)
            weakRefView = WeakReference(adsParentLayout)
        }

        override fun run() {
            weakRefContext?.get()?.runOnUiThread {
                weak?.get()?.destroy()
                weak = null
                weakReference?.get()?.onAdFailed(weakRefContext.get()?.getString(R.string.timeout))
                loge(weakRefContext.get()?.getString(R.string.timeout))
                weakRefView?.get()?.visibility = View.GONE
            }
        }
    }

    /**
     * Activity / Fragment 里面的 onDestroy（） 调用
     */
    fun destroy() {
        val lastAdView = weak?.get()
        lastAdView?.destroy()
        weak = null
    }

    fun resume() {
        val lastAdView = weak?.get()
        lastAdView?.resume()
    }

    fun pause() {
        val lastAdView = weak?.get()
        lastAdView?.pause()
    }

    interface AdListenerVerticalPreMovie {

        fun onAdClick(channel: String)

        fun onAdFailed(failedMsg: String?)

        fun onAdFailedSingle(channel: String, failedMsg: String?)

        fun onAdDismissed()

        fun onAdPrepared(channel: String)

        fun onStartRequest(channel: String)
    }
}

