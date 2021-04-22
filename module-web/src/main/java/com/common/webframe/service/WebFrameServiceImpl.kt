package com.common.webframe.service

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.common.arms.integration.AppManager
import com.common.res.router.RouterHub
import com.common.res.router.service.IWebFrameService
import com.common.webframe.ui.activity.WebPageActivity
import com.common.webframe.ui.fragment.WebPageFrgment

@Route(path = RouterHub.WEBFRAME_ERVICE, name = "web框架服务")
class WebFrameServiceImpl : IWebFrameService {

    private var context: Context? = null

    override fun init(context: Context?) {
        this.context = context
    }

    /**
     * 打开web页面
     * @param context Context
     * @param url String?
     * @param title String?
     * @param isHideToolbar Boolean
     */
    override fun start(context: Context, url: String?, title: String?, isHideToolbar: Boolean) {
        WebPageActivity.start(context, url, title, isHideToolbar)
    }

    /**
     * 创建WebPageFragment
     *
     * @param url
     */
    override fun newWebPageFragment(url: String): Fragment {
        return WebPageFrgment.newInstance(url)
    }

    /**
     * web框架回调通知H5
     * @param data
     */
    override fun onCallBack(data: String?) {
        val activityList: List<Activity> = AppManager.getAppManager().activityList ?: return
        for (i in activityList.indices.reversed()) {
            val activity = activityList[i]
            if (activity != null && activity.javaClass == WebPageActivity::class.java) {
                val webPageActivity: WebPageActivity<*> = activity as WebPageActivity<*>
                webPageActivity.getWebPageFrgment()?.getJsApi()?.onProgressCallBack(data!!)
            }
        }
    }

}