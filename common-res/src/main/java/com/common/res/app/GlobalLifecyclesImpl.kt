package com.common.res.app

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.coder.zzq.smartshow.core.SmartShow
import com.common.arms.base.delegate.AppLifecycles
import com.common.arms.callback.EmptyCallBack
import com.common.arms.callback.ErrorCallBack
import com.common.arms.callback.LoadingCallBack
import com.common.res.config.AppConfig
import com.common.res.page.PageDataHelper
import com.common.res.update.OKHttpUpdateHttpService
import com.common.res.view.RefreshLottieHeader
import com.kingja.loadsir.core.LoadSir
import com.liulishuo.filedownloader.FileDownloader
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.mmkv.MMKV
import com.xuexiang.xupdate.XUpdate
import com.xuexiang.xupdate.utils.UpdateUtils
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module

class GlobalLifecyclesImpl : AppLifecycles {

    override fun attachBaseContext(base: Context) {
    }

    override fun onCreate(application: Application) {
        //初始化MMKV
        MMKV.initialize(application)
        //初始化ARouter
        ARouter.init(application)
        //初始化Koin
        startKoin {
            androidLogger()
            androidContext(application)
            var modules = module {
                factory { PageDataHelper() }
            }
            loadKoinModules(modules)
        }
        //初始化LoadSir
        LoadSir.beginBuilder()
                .addCallback(ErrorCallBack())
                .addCallback(LoadingCallBack())
                .addCallback(EmptyCallBack())
                .commit()
        //初始化SmartShow
        SmartShow.init(application)
        //初始化SmartRefreshLayout,设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer { _, layout -> //全局设置（优先级最低）
            layout.setEnableAutoLoadMore(true)
            layout.setEnableOverScrollDrag(false)
            layout.setEnableOverScrollBounce(true)
            layout.setEnableLoadMoreWhenContentNotFull(true)
            layout.setEnableScrollContentWhenRefreshed(true)
            layout.setFooterMaxDragRate(2.0f)
            layout.setFooterHeight(20f)
            layout.setRefreshHeader(RefreshLottieHeader(application))
            layout.setRefreshFooter(ClassicsFooter(application).apply {
                setFinishDuration(0)
            })
        }
        //初始化XUpdate
        initUpdate(application)
        //初始化FileDownloader
        val config = FileDownloadUrlConnection.Configuration().apply {
            connectTimeout(20000)
            readTimeout(20000)
        }
        FileDownloader.setupOnApplicationOnCreate(application).connectionCreator(FileDownloadUrlConnection.Creator(config)).commit()
    }

    override fun onTerminate(application: Application) {}


    private fun initUpdate(application: Application) {
        XUpdate.get()
                .debug(false)
                .isWifiOnly(false) //默认设置只在wifi下检查版本更新
                .isGet(true) //默认设置使用get请求检查版本
                .isAutoMode(false) //默认设置非自动模式，可根据具体使用配置
                .param("versionCode", UpdateUtils.getVersionCode(application)) //设置默认公共请求参数
                .param("appKey", application.packageName)
                .supportSilentInstall(true) //设置是否支持静默安装，默认是true
                .setIUpdateHttpService(OKHttpUpdateHttpService()) //这个必须设置！实现网络请求功能。
                .init(application) //这个必须初始化
    }

}