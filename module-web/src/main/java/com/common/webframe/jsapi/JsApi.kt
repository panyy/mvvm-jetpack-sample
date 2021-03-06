package com.common.webframe.jsapi

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.JavascriptInterface
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.coder.zzq.smartshow.dialog.LoadingDialog
import com.common.arms.integration.AppManager
import com.common.res.config.AppConfig
import com.common.res.entity.SendEventEntity
import com.common.res.event.WEBFRAME_EVENT
import com.common.res.utils.GsonUtil
import com.common.res.utils.appLogoutToLogin
import com.common.res.utils.eventBusPost
import com.common.webframe.entity.*
import com.common.webframe.entity.ToolBarEntity.BtnLeftEntity
import com.common.webframe.entity.ToolBarEntity.BtnmoreEntity
import com.common.webframe.ui.activity.TabPageActivity
import com.common.webframe.ui.activity.WebPageActivity
import com.common.webframe.ui.fragment.WebPageFrgment
import com.common.webframe.utils.JsApiHelper
import com.common.webframe.view.BaseDWebView
import com.common.webframe.view.CompletionHandler
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.util.*

class JsApi {

    var isCanBack = true
    var dWebView: BaseDWebView? = null
    var morebtns: ArrayList<BtnmoreEntity>? = null
    var fragment: WebPageFrgment? = null
    var activity: FragmentActivity? = null
    var loadingDialog: LoadingDialog? = null
    private var webframeToolbar: CardView? = null
    private var tvWebframeTitle: TextView? = null
    private var ivWebframeBack: ImageView? = null
    private var ivWebframeMore: ImageView? = null
    private var tvWebframeRightText: TextView? = null
    private var ivRightImage: ImageView? = null
    private var handler: CompletionHandler<String>? = null
    private var btnleft: BtnLeftEntity? = null

    fun init(fragment: WebPageFrgment?, dWebView: BaseDWebView?) {
        this.fragment = fragment
        this.dWebView = dWebView
        activity = fragment?.activity
        if (activity is WebPageActivity<*>) {
            webframeToolbar = activity?.webframe_toolbar
            tvWebframeTitle = activity?.tv_webframe_title
            ivWebframeMore = activity?.iv_webframe_more
            ivWebframeBack = activity?.iv_webframe_back
            ivRightImage = activity?.iv_right_image
            tvWebframeRightText = activity?.tv_webframe_right_text
        }
    }

    /**
     * ???????????????
     *
     * @param data
     */
    @JavascriptInterface
    fun initWebPage(data: Any) {
        activity?.runOnUiThread {
            val entity = GsonUtil.fromJson(data.toString(), WebPageEntity::class.java)
            if (entity != null) {
                val toolBar = entity.toolbar
                if (toolBar != null && toolBar.visible) {
                    //??????toolbar
                    if (webframeToolbar != null) webframeToolbar?.visibility = View.VISIBLE

                    //????????????????????????
                    btnleft = toolBar.btnleft
                    isCanBack = btnleft == null
                    if (ivWebframeBack != null) ivWebframeBack?.setOnClickListener { v: View? -> activity?.onBackPressed() }

                    //??????title
                    val title = toolBar.title
                    if (tvWebframeTitle != null) {
                        if (title != null) {
                            tvWebframeTitle?.visibility = View.VISIBLE
                            tvWebframeTitle?.text = title.content
                        } else {
                            tvWebframeTitle?.visibility = View.GONE
                            tvWebframeTitle?.text = ""
                        }
                    }

                    //??????????????????
                    morebtns = toolBar.btnmore
                    if (ivWebframeMore != null) {
                        if (morebtns != null) {
                            ivWebframeMore?.visibility = View.VISIBLE
                            ivWebframeMore?.setOnClickListener { v: View? -> JsApiHelper.showPopupMenu(this@JsApi, v) }
                        } else {
                            ivWebframeMore?.visibility = View.GONE
                        }
                    }

                    //??????????????????
                    val btnright = toolBar.btnright
                    if (btnright != null) {
                        if (tvWebframeRightText != null && !TextUtils.isEmpty(btnright.name)) {
                            tvWebframeRightText?.visibility = View.VISIBLE
                            tvWebframeRightText?.text = btnright.name
                            tvWebframeRightText?.setOnClickListener { view: View? -> dWebView?.callHandler(btnright.data, arrayOf()) }
                        }
                        if (ivRightImage != null && !TextUtils.isEmpty(btnright.image)) {
                            ivRightImage?.visibility = View.VISIBLE
                            Glide.with(activity!!).load(btnright.image).into(ivRightImage!!)
                            ivRightImage?.setOnClickListener { view: View? -> dWebView?.callHandler(btnright.data, arrayOf()) }
                        }
                    } else {
                        if (tvWebframeRightText != null) tvWebframeRightText?.visibility = View.GONE
                    }
                } else {
                    if (webframeToolbar != null) webframeToolbar?.visibility = View.GONE
                }
            }
        }
    }

    /**
     * ??????????????????H5??????
     */
    fun delegateBackEventH5() {
        if (dWebView != null && btnleft != null) {
            dWebView?.callHandler(btnleft?.data, arrayOf())
        }
    }

    /**
     * ????????????
     */
    @JavascriptInterface
    fun openNewPage(data: Any?) {
        val entity = GsonUtil.fromJson(data.toString(), NewPageEntity::class.java)
        WebPageActivity.start(context = activity!!, url = entity.data, title = entity.title, isHideToolbar = entity.hidetoolbar)
        if (entity.closenumpage > 0) {
            for (i in 0 until entity.closenumpage) {
                val activity = AppManager.getAppManager().topActivity
                if (activity is WebPageActivity<*>) {
                    AppManager.getAppManager().removeActivity(activity)
                    activity.finish()
                }
            }
        }
    }

    /**
     * ??????Tab??????
     */
    @JavascriptInterface
    fun openTabPage(data: Any?) {
        val tabPage = Gson().fromJson(data.toString(), TabPageEntity::class.java)
        TabPageActivity.openTabPage(activity, tabPage.tabs)
        if (tabPage.closenumpage > 0) {
            for (i in 0 until tabPage.closenumpage) {
                val activity = AppManager.getAppManager().topActivity
                if (activity is WebPageActivity<*>) {
                    AppManager.getAppManager().removeActivity(activity)
                    activity.finish()
                }
            }
        }
    }

    /**
     * ??????Tab???????????????
     */
    @JavascriptInterface
    fun refreshTabRightBtn(data: Any?) {
        val entity = GsonUtil.fromJson(data.toString(), RefreshTabRightBtnEntity::class.java)
        (activity as TabPageActivity).setAndRefreshTabRightBtn(entity.btnrights)
    }

    /**
     * ???????????????
     */
    @JavascriptInterface
    fun closeCurrPage(data: Any?) {
        activity?.runOnUiThread { activity?.finish() }
    }

    /**
     * ???????????????
     */
    @JavascriptInterface
    fun closeAllPage(data: Any?) {
        AppManager.getAppManager().killActivity(WebPageActivity::class.java)
    }

    /**
     * ????????????????????????
     */
    @JavascriptInterface
    fun closeNumPage(data: Any?) {
        val entity = GsonUtil.fromJson(data.toString(), CloseNumPageEntity::class.java)
        for (i in 0 until entity.num) {
            val activity = AppManager.getAppManager().topActivity
            AppManager.getAppManager().removeActivity(activity)
            activity?.finish()
        }
    }

    /**
     * ??????Toast
     */
    @JavascriptInterface
    fun showToast(data: Any?) {
        val entity = GsonUtil.fromJson(data.toString(), ShowToastEntity::class.java)
        Toast.makeText(activity, entity.data, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun getToken(data: Any?): String? {
        return AppConfig.accessToken
    }

    /**
     * ????????????
     */
    @JavascriptInterface
    fun sendEvent(data: Any?) {
        val entity = GsonUtil.fromJson(data.toString(), SendEventEntity::class.java)
        eventBusPost(WEBFRAME_EVENT, entity)
    }

    /**
     * ???????????????
     */
    @JavascriptInterface
    fun showDialog(data: Any?) {
        JsApiHelper.showDialog(this, data)
    }

    /**
     * ???????????????
     */
    @JavascriptInterface
    fun dismissDialog(data: Any?) {
        JsApiHelper.dismissDialog(this, data)
    }

    /**
     * ????????????????????????
     */
    @JavascriptInterface
    fun toggleSoftInput(data: Any?) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * ???????????????
     */
    @JavascriptInterface
    fun openBrowser(data: Any?) {
        val entity = GsonUtil.fromJson(data.toString(), BrowserEntity::class.java)
        val uri = Uri.parse(entity.url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        activity?.startActivity(intent)
    }

    /**
     * ??????
     */
    @JavascriptInterface
    fun logout(data: Any?) {
        appLogoutToLogin()
    }

    /**
     * ??????????????????
     */
    @JavascriptInterface
    fun setData(data: Any) {
        val entity = GsonUtil.fromJson(data.toString(), SetDataEntity::class.java)
        MMKV.defaultMMKV()?.encode(entity.key, entity.value)
    }

    /**
     * ??????????????????
     */
    @JavascriptInterface
    fun getData(data: Any?): String? {
        return MMKV.defaultMMKV()?.decodeString(data.toString(), "")
    }

    fun onCallBack(data: String?) {
        if (handler != null) {
            handler?.complete(data)
        }
    }

    fun onProgressCallBack(data: String?) {
        if (handler != null) {
            handler?.setProgressData(data)
        }
    }
}