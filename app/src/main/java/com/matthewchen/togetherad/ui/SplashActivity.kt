package com.matthewchen.togetherad.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.matthewchen.togetherad.R
import com.matthewchen.togetherad.config.Config
import com.matthewchen.togetherad.config.TogetherAdConst
import com.matthewchen.togetherad.utils.Kits
import com.rumtel.ad.helper.splash.TogetherAdSplash
import kotlinx.android.synthetic.main.activity_splash.*

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2018/12/21.
 */
class SplashActivity : AppCompatActivity() {

    private var canJumpImmediately = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Kits.StatuBar.immersive(this)
        Kits.StatuBar.darkMode(this)
        Kits.StatuBar.setPaddingSmart(this, mFlAdContainer)

        requestAd()

    }

    override fun onPause() {
        super.onPause()
        canJumpImmediately = false
    }

    override fun onResume() {
        super.onResume()
        if (canJumpImmediately) {
            actionHome(0)
        }
        canJumpImmediately = true
    }

    /**
     * 跳转 Main, 延迟多少毫秒
     */
    private fun actionHome(delayMillis: Long) {
        mFlAdContainer.postDelayed({
            MainActivity.MainAct.action(this)
            finish()
        }, delayMillis)
    }

    private fun requestAd() {
        val splashConfigAd = Config.splashAdConfig()
        TogetherAdSplash.showAdFull(activity = this, splashConfigStr = splashConfigAd, adConstStr = TogetherAdConst.AD_SPLASH, adsParentLayout = mFlAdContainer, adListener = object : TogetherAdSplash.AdListenerSplashFull {
            override fun onStartRequest(channel: String) {
                Log.e("ifmvo", "onStartRequest:channel:$channel")
            }

            override fun onAdClick(channel: String) {
                Log.e("ifmvo", "onAdClick:channel:$channel")
            }

            override fun onAdFailed(failedMsg: String?) {
                Log.e("ifmvo", "onAdFailed:failedMsg:$failedMsg")
                actionHome(0)
            }

            override fun onAdFailedSingle(channel: String, failedMsg: String?) {
                Log.e("ifmvo", "onAdFailedSingle:channel:$channel failedMsg:$failedMsg")
            }

            override fun onAdDismissed() {
                Log.e("ifmvo", "onAdDismissed")
                if (canJumpImmediately) {
                    actionHome(0)
                }
                canJumpImmediately = true
            }

            override fun onAdPrepared(channel: String) {
                Log.e("ifmvo", "onAdPrepared:channel:$channel")
            }
        })
    }
}