package com.rumtel.ad.helper.flow

import android.app.Activity
import android.support.annotation.NonNull
import android.util.DisplayMetrics
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTNativeAd
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.nativ.NativeADUnifiedListener
import com.qq.e.ads.nativ.NativeUnifiedAD
import com.qq.e.ads.nativ.NativeUnifiedADData
import com.qq.e.comm.util.AdError
import com.rumtel.ad.R
import com.rumtel.ad.TogetherAd
import com.rumtel.ad.helper.AdBase
import com.rumtel.ad.other.AdNameType
import com.rumtel.ad.other.AdRandomUtil
import com.rumtel.ad.other.logd
import com.rumtel.ad.other.loge
import java.lang.ref.WeakReference
import java.util.*


/*
 * (●ﾟωﾟ●) 信息流的广告 （ 获取到广告之后自己配置界面和展示 ）
 * 
 * Created by Matthew_Chen on 2018/12/25.
 */
object TogetherAdFlow : AdBase() {

    private var timer: Timer? = null
    private var overTimerTask: OverTimerTask? = null

    @Volatile
    private var stop = false

    fun getAdList(@NonNull activity: Activity, listConfigStr: String?, @NonNull adConstStr: String, @NonNull adListener: AdListenerList) {
        stop = false
        startTimerTask(activity, adListener)

        when (AdRandomUtil.getRandomAdName(listConfigStr)) {
            AdNameType.GDT -> {
                getAdListTecentGDT(activity, listConfigStr, adConstStr, adListener)
            }
            AdNameType.CSJ -> {
                getAdListCsj(activity, listConfigStr, adConstStr, adListener)
            }
            else -> {
                if (stop) {
                    return
                }
                cancelTimerTask()

                activity.runOnUiThread {
                    adListener.onAdFailed(activity.getString(R.string.all_ad_error))
                }
                loge(activity.getString(R.string.all_ad_error))
            }
        }
    }

    private fun getAdListCsj(@NonNull activity: Activity, listConfigStr: String?, @NonNull adConstStr: String, @NonNull adListener: AdListenerList) {
        adListener.onStartRequest(AdNameType.CSJ.type)

        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAd.idMapCsj[adConstStr])
                .setSupportDeepLink(true)
                .setImageAcceptedSize(dm.widthPixels, (dm.widthPixels * 9 / 16))
                .setNativeAdType(AdSlot.TYPE_FEED)
                .setAdCount(3)
                .build()
        TTAdSdk.getAdManager().createAdNative(activity).loadNativeAd(adSlot, object : TTAdNative.NativeAdListener {

            override fun onNativeAdLoad(adList: MutableList<TTNativeAd>?) {
                if (stop) {
                    return
                }
                if (adList.isNullOrEmpty()) {
                    loge("${AdNameType.CSJ.type}: 返回的广告是空的")
                    val newListConfig = listConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                    getAdList(activity, newListConfig, adConstStr, adListener)
                    return
                }

                cancelTimerTask()

                adListener.onAdLoaded(AdNameType.CSJ.type, adList)
                logd("${AdNameType.CSJ.type}: list.size: " + adList.size)
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                if (stop) {
                    return
                }
                cancelTimerTask()

                loge("${AdNameType.CSJ.type}: errorCode: $errorCode, errorMsg: $errorMsg")
                val newListConfig = listConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                getAdList(activity, newListConfig, adConstStr, adListener)
            }
        })
    }

    private fun getAdListTecentGDT(@NonNull activity: Activity, listConfigStr: String?, @NonNull adConstStr: String, @NonNull adListener: AdListenerList) {
        adListener.onStartRequest(AdNameType.GDT.type)

        val listener = object : NativeADUnifiedListener {
            override fun onADLoaded(adList: List<NativeUnifiedADData>?) {
                if (stop) {
                    return
                }
                cancelTimerTask()

                //list是空的，按照错误来处理
                if (adList?.isEmpty() != false) {
                    loge("${AdNameType.GDT.type}: 请求成功，但是返回的list为空")
                    val newListConfig = listConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                    activity.runOnUiThread {
                        getAdList(activity, newListConfig, adConstStr, adListener)
                    }
                    return
                }

                logd("${AdNameType.GDT.type}: list.size: " + adList.size)
                activity.runOnUiThread {
                    adListener.onAdLoaded(AdNameType.GDT.type, adList)
                }
            }

            override fun onNoAD(adError: AdError?) {
                if (stop) {
                    return
                }
                cancelTimerTask()

                loge("${AdNameType.GDT.type}: ${adError?.errorCode}, ${adError?.errorMsg}")
                val newListConfig = listConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                getAdList(activity, newListConfig, adConstStr, adListener)
            }
        }

        val mAdManager = NativeUnifiedAD(activity, TogetherAd.appIdGDT, TogetherAd.idMapGDT[adConstStr], listener)
        //有效值就是 5-60
        mAdManager.setMaxVideoDuration(60)
        mAdManager.setMinVideoDuration(5)
        mAdManager.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO) // 本次拉回的视频广告，在用户看来是否为自动播放的
        mAdManager.setVideoADContainerRender(VideoOption.VideoADContainerRender.SDK) // 视频播放前，用户看到的广告容器是由SDK渲染的
        mAdManager.loadData(4)
    }

    interface AdListenerList {

        fun onAdFailed(failedMsg: String?)

        fun onAdLoaded(channel: String, adList: List<*>)

        fun onStartRequest(channel: String)
    }

    /**
     * 取消超时任务
     */
    private fun cancelTimerTask() {
        stop = false
        timer?.cancel()
        overTimerTask?.cancel()
    }

    /**
     * 开始超时任务
     */
    private fun startTimerTask(activity: Activity, listener: AdListenerList) {
        cancelTimerTask()
        timer = Timer()
        overTimerTask = OverTimerTask(activity, listener)
        timer?.schedule(overTimerTask, TogetherAd.timeOutMillis)
    }

    /**
     * 超时任务
     */
    private class OverTimerTask(activity: Activity, listener: AdListenerList) : TimerTask() {

        private val weakReference: WeakReference<AdListenerList>?
        private val weakRefContext: WeakReference<Activity>?

        init {
            weakReference = WeakReference(listener)
            weakRefContext = WeakReference(activity)
        }

        override fun run() {
            stop = true
            weakRefContext?.get()?.runOnUiThread {
                weakReference?.get()?.onAdFailed(weakRefContext.get()?.getString(R.string.timeout))
                loge(weakRefContext.get()?.getString(R.string.timeout))
                timer = null
                overTimerTask = null
            }
        }
    }
}