package com.rumtel.ad

import android.app.Application
import android.support.annotation.NonNull
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.qq.e.comm.managers.GDTADManager
import com.rumtel.ad.other.AdNameType
import com.rumtel.ad.other.logd

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2018/12/26.
 */
object TogetherAd {

    var idMapGDT = mutableMapOf<String, String>()

    var idMapCsj = mutableMapOf<String, String>()

    /**
     * 保存application
     */
    lateinit var mContext: Application

    /**
     * 广点通的 AppId
     */
    var appIdGDT = ""

    /**
     * 超时时间
     */
    var timeOutMillis: Long = 5000

    /**
     * 前贴
     */
    var preMoivePaddingSize = 0
        private set

    //广点通
    fun initGDTAd(@NonNull context: Application, @NonNull gdtAdAppId: String) {
        mContext = context
        appIdGDT = gdtAdAppId
        GDTADManager.getInstance().initWith(context, gdtAdAppId)
        logd("初始化${AdNameType.GDT.type}")
    }

    //穿山甲
    fun initCsjAd(@NonNull context: Application, @NonNull csjAdAppId: String, @NonNull appName: String, useTextureView: Boolean = false) {
        mContext = context
        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        TTAdSdk.init(context, TTAdConfig.Builder()
                .appId(csjAdAppId)
                .appName(appName)
                .useTextureView(useTextureView) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                .debug(BuildConfig.DEBUG) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI) //允许直接下载的网络状态集合
                .supportMultiProcess(false) //是否支持多进程，true支持
                //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                .build()
        )
        logd("初始化${AdNameType.CSJ.type}")
    }

    fun setAdTimeOutMillis(millis: Long) {
        timeOutMillis = millis
        logd("全局设置超时时间：$millis")
    }

    fun setPreMoiveMarginTopSize(height: Int) {
        preMoivePaddingSize = height
    }


}