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
        gdtIdMap[TogetherAdConst.AD_SPLASH] = "8863364436303842593"
        gdtIdMap[TogetherAdConst.AD_INTER] = "6040749702835933"
        gdtIdMap[TogetherAdConst.AD_FLOW_INDEX] = "6040749702835933"
        gdtIdMap[TogetherAdConst.AD_TIEPIAN_LIVE] = "6040749702835933"
        gdtIdMap[TogetherAdConst.AD_WEBVIEW_BANNER] = "4080052898050840"
        gdtIdMap[TogetherAdConst.AD_BACK] = "6040749702835933"
        gdtIdMap[TogetherAdConst.AD_MID] = "6040749702835933"
//        gdtIdMap[TogetherAdConst.AD_VERTICAL_VIDEO] = "3051315501066888"
        TogetherAd.initGDTAd(this, "1101152570", gdtIdMap)

        val csjIdMap = HashMap<String, String>()
        csjIdMap[TogetherAdConst.AD_SPLASH] = "801121648"
        csjIdMap[TogetherAdConst.AD_INTER] = "901121435"
        csjIdMap[TogetherAdConst.AD_FLOW_INDEX] = "901121737"
        csjIdMap[TogetherAdConst.AD_TIEPIAN_LIVE] = "901121737"
        csjIdMap[TogetherAdConst.AD_WEBVIEW_BANNER] = "901121987"
        csjIdMap[TogetherAdConst.AD_BACK] = "901121737"
        csjIdMap[TogetherAdConst.AD_MID] = "901121737"
//        csjIdMap[TogetherAdConst.AD_VERTICAL_VIDEO] = "945223808"
        TogetherAd.initCsjAd(this, "5001121", this.getString(R.string.app_name), csjIdMap, useTextureView = true)
        TogetherAd.setAdTimeOutMillis(5000)
    }
}