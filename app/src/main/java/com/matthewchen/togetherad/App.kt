package com.matthewchen.togetherad

import android.app.Application
import com.matthewchen.togetherad.config.TogetherAdConst
import com.rumtel.ad.TogetherAd
import java.util.*

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2018/12/26.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val gdtIdMap = HashMap<String, String>()
        gdtIdMap[TogetherAdConst.AD_SPLASH] = "8091314403575929"
        gdtIdMap[TogetherAdConst.AD_INTER] = "5061916465709924"
        gdtIdMap[TogetherAdConst.AD_FLOW_INDEX] = "8001038580082818"
        gdtIdMap[TogetherAdConst.AD_TIEPIAN_LIVE] = "5091910455119071"
        gdtIdMap[TogetherAdConst.AD_WEBVIEW_BANNER] = "9001611465613466"
        gdtIdMap[TogetherAdConst.AD_BACK] = "9001919475210551"
        gdtIdMap[TogetherAdConst.AD_MID] = "1021415465902958"
        gdtIdMap[TogetherAdConst.AD_VERTICAL_VIDEO] = "3051315501066888"
        TogetherAd.initGDTAd(this, "1105965856")

        val csjIdMap = HashMap<String, String>()
        csjIdMap[TogetherAdConst.AD_SPLASH] = "820413685"
        csjIdMap[TogetherAdConst.AD_INTER] = "920413056"
        csjIdMap[TogetherAdConst.AD_FLOW_INDEX] = "945513352"
        csjIdMap[TogetherAdConst.AD_TIEPIAN_LIVE] = "920413238"
        csjIdMap[TogetherAdConst.AD_WEBVIEW_BANNER] = "920413358"
        csjIdMap[TogetherAdConst.AD_BACK] = "920413512"
        csjIdMap[TogetherAdConst.AD_MID] = "945513352"
        csjIdMap[TogetherAdConst.AD_VERTICAL_VIDEO] = "945223808"
        TogetherAd.initCsjAd(this, "5020413", this.getString(R.string.app_name), useTextureView = true)

        TogetherAd.idMapGDT = gdtIdMap
        TogetherAd.idMapCsj = csjIdMap

        TogetherAd.setAdTimeOutMillis(7000)
    }
}