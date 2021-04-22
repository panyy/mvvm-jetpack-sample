package com.common.webframe.app

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.text.TextUtils
import com.common.arms.base.delegate.AppLifecycles
import com.common.arms.integration.AppManager
import com.common.webframe.service.PreLoadX5Service
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module


class WebLifecyclesImpl : AppLifecycles {

    override fun attachBaseContext(base: Context) {
    }

    override fun onCreate(application: Application) {
        startPreLoadX5Service()
    }

    override fun onTerminate(application: Application) {
    }

    /**
     * 前台启动X5预加载服务
     */
    private fun startPreLoadX5Service() {
        var handler = Handler()
        handler.postDelayed({
            var activity = AppManager.getAppManager().currentActivity
            if (activity != null && isForeground(activity, activity.localClassName)) {
                activity.startService(Intent(activity, PreLoadX5Service::class.java))
            } else {
                startPreLoadX5Service()
            }
        }, 1000)
    }

    private fun isForeground(context: Activity?, className: String): Boolean {
        if (context == null || TextUtils.isEmpty(className)) return false
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = am.getRunningTasks(1)
        if (list != null && list.size > 0) {
            val cpn = list[0].topActivity
            if (className == cpn!!.className) return true
        }
        return false
    }

}
