package com.common.sample.app

import android.app.Application
import android.content.Context
import com.common.arms.base.delegate.AppLifecycles
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

class AppLifecyclesImpl : AppLifecycles {

    override fun attachBaseContext(base: Context) {
    }

    override fun onCreate(application: Application) {
        //初始化友盟SDK
        UMConfigure.setLogEnabled(true)
        UMConfigure.init(application, "1234567890", "xxx", UMConfigure.DEVICE_TYPE_PHONE, "")
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
    }

    override fun onTerminate(application: Application) {}

}