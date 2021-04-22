package com.common.webframe.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import java.util.*

class PreLoadX5Service : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        initX5()
        preinitX5WebCore()
    }

    private fun initX5() {
        val settings: MutableMap<String, Any> = HashMap<String, Any>()
        settings[TbsCoreSettings.TBS_SETTINGS_USE_PRIVATE_CLASSLOADER] = true
        settings[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        settings[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(settings)
        QbSdk.initX5Environment(applicationContext, cb)
    }

    private val cb: PreInitCallback = object : PreInitCallback {
        override fun onViewInitFinished(arg0: Boolean) {
            Log.e("QbSdk", "onViewInitFinished is $arg0")
        }

        override fun onCoreInitFinished() {
            Log.e("QbSdk", "onCoreInitFinished")
        }
    }

    private fun preinitX5WebCore() {
        if (!QbSdk.isTbsCoreInited()) {
            QbSdk.preInit(applicationContext, null)
        }
    }
}