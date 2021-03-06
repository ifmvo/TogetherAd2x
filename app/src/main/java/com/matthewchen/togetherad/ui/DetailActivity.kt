package com.matthewchen.togetherad.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.matthewchen.togetherad.R
import com.matthewchen.togetherad.config.Config
import com.matthewchen.togetherad.config.TogetherAdConst
import com.rumtel.ad.helper.banner.TogetherAdBanner
import com.rumtel.ad.helper.inter.TogetherAdInter
import com.rumtel.ad.helper.mid.TogetherAdMid
import com.rumtel.ad.helper.preMovie.TogetherAdPreMovie
import com.rumtel.ad.helper.preMovie.TogetherAdVerticalPreMovie
import com.rumtel.ad.other.AdPresentType
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    object DetailAct {
        fun action(context: Context) {
            val intent = Intent(context, DetailActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        mBtnPreMoiveNeedTimer.setOnClickListener {
            preMovieAd(needTimer = true)
        }

        mBtnPreMoiveNoTimer.setOnClickListener {
            preMovieAd(needTimer = false)
        }

        mBtnInter.setOnClickListener {
            interstitialAd()
        }

        mBtnWebPlay2.setOnClickListener {
            webViewBanner2()
        }

        mBtnMid.setOnClickListener {
            midAd()
        }

        mBtnVerticalAd.setOnClickListener {
            fullVerticalVideo(true)
        }
    }

    private fun fullVerticalVideo(needTimer: Boolean) {
        TogetherAdVerticalPreMovie.showAdVerticalPreMovie(this, Config.preVerticalMoiveAdConfig(), TogetherAdConst.AD_VERTICAL_VIDEO, ll_ad_vertical, object : TogetherAdVerticalPreMovie.AdListenerVerticalPreMovie {
            override fun onAdClick(channel: String) {
                Log.e("ifmvo", "onAdClick:channel:$channel")
            }

            override fun onAdFailed(failedMsg: String?) {
                Log.e("ifmvo", "onAdFailed:failedMsg:$failedMsg")
            }

            override fun onAdFailedSingle(channel: String, failedMsg: String?) {
                Log.e("ifmvo", "onAdFailedSingle:channel:$channel failedMsg:$failedMsg")
            }

            override fun onAdDismissed() {
                Log.e("ifmvo", "onAdDismissed")
            }

            override fun onAdPrepared(channel: String) {
                Log.e("ifmvo", "onAdPrepared:channel:$channel")
            }

            override fun onStartRequest(channel: String) {
                Log.e("ifmvo", "onStartRequest:channel:$channel")
            }
        }, needTimer = needTimer, present = AdPresentType.NativeVideo.present)
    }

    private fun preMovieAd(needTimer: Boolean) {
        //baidu:2,gdt:8
        TogetherAdPreMovie.showAdPreMovie(this, Config.preMoiveAdConfig(), TogetherAdConst.AD_TIEPIAN_LIVE, ll_ad, object : TogetherAdPreMovie.AdListenerPreMovie {
            override fun onAdClick(channel: String) {
                Log.e("ifmvo", "onAdClick:channel:$channel")
            }

            override fun onAdFailed(failedMsg: String?) {
                Log.e("ifmvo", "onAdFailed:failedMsg:$failedMsg")
            }

            override fun onAdFailedSingle(channel: String, failedMsg: String?) {
                Log.e("ifmvo", "onAdFailedSingle:channel:$channel failedMsg:$failedMsg")
            }

            override fun onAdDismissed() {
                Log.e("ifmvo", "onAdDismissed")
            }

            override fun onAdPrepared(channel: String) {
                Log.e("ifmvo", "onAdPrepared:channel:$channel")
            }

            override fun onStartRequest(channel: String) {
                Log.e("ifmvo", "onStartRequest:channel:$channel")
            }
        }, needTimer = needTimer)
    }

    private fun interstitialAd() {

        TogetherAdInter.showAdInter(this, Config.interAdConfig(), TogetherAdConst.AD_INTER, false, mRlInterAd, object : TogetherAdInter.AdListenerInter {
            override fun onStartRequest(channel: String) {
                Log.e("ifmvo", "onStartRequest:channel:$channel")
            }

            override fun onAdClick(channel: String) {
                Log.e("ifmvo", "onAdClick:channel:$channel")
            }

            override fun onAdFailed(failedMsg: String?) {
                Log.e("ifmvo", "onAdFailed:failedMsg:$failedMsg")
            }

            override fun onAdFailedSingle(channel: String, failedMsg: String?) {
                Log.e("ifmvo", "onAdFailedSingle:channel:$channel failedMsg:$failedMsg")
            }

            override fun onAdDismissed() {
                Log.e("ifmvo", "onAdDismissed")
            }

            override fun onAdPrepared(channel: String) {
                Log.e("ifmvo", "onAdPrepared:channel:$channel")
            }
        })
    }

    private fun webViewBanner2() {
        TogetherAdBanner.requestBanner(this, Config.webViewAdConfig(), TogetherAdConst.AD_WEBVIEW_BANNER, mFlAdBannerContainer, object : TogetherAdBanner.AdListenerList {
            override fun onStartRequest(channel: String) {
                Log.e("ifmvo", "onStartRequest:channel:$channel")
            }

            override fun onAdClick(channel: String) {
                Log.e("ifmvo", "onAdClick:channel:$channel")
            }

            override fun onAdFailed(failedMsg: String?) {
                Log.e("ifmvo", "onAdFailed:failedMsg:$failedMsg")
            }

            override fun onAdFailedSingle(channel: String, failedMsg: String?) {
                Log.e("ifmvo", "onAdFailedSingle:channel:$channel failedMsg:$failedMsg")
            }

            override fun onAdDismissed() {
                Log.e("ifmvo", "onAdDismissed")
            }

            override fun onAdPrepared(channel: String) {
                Log.e("ifmvo", "onAdPrepared:channel:$channel")
            }
        })
    }

    private fun midAd() {
        TogetherAdMid.showAdMid(this, Config.midAdConfig(), TogetherAdConst.AD_MID, mFlAdMidContainer, object : TogetherAdMid.AdListenerMid {
            override fun onStartRequest(channel: String) {
                Log.d("ifmvo", "TogetherAdMid.onStartRequest")
            }

            override fun onAdClick(channel: String) {
                Log.d("ifmvo", "TogetherAdMid.onAdClick")
            }

            override fun onAdFailed(failedMsg: String?) {
                Log.d("ifmvo", "TogetherAdMid.onAdFailed")
            }

            override fun onAdFailedSingle(channel: String, failedMsg: String?) {
                Log.e("ifmvo", "onAdFailedSingle:channel:$channel failedMsg:$failedMsg")
            }

            override fun onAdDismissed() {
                Log.d("ifmvo", "TogetherAdMid.onAdDismissed")
            }

            override fun onAdPrepared(channel: String) {
                Log.d("ifmvo", "TogetherAdMid.onAdPrepared")
            }
        })
    }

    override fun onPause() {
        super.onPause()
        TogetherAdPreMovie.pause()
        TogetherAdVerticalPreMovie.pause()
        TogetherAdInter.pause()
        TogetherAdMid.pause()
    }

    override fun onResume() {
        super.onResume()
        TogetherAdPreMovie.resume()
        TogetherAdVerticalPreMovie.resume()
        TogetherAdInter.resume()
        TogetherAdMid.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        TogetherAdPreMovie.destroy()
        TogetherAdVerticalPreMovie.destroy()
        TogetherAdInter.destroy()
        TogetherAdMid.destroy()
    }
}
