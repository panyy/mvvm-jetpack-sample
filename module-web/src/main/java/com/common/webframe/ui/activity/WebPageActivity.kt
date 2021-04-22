package com.common.webframe.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.common.arms.base.BaseActivity
import com.common.res.entity.SendEventEntity
import com.common.res.event.WEBFRAME_EVENT
import com.common.res.utils.bindViewClickListener
import com.common.res.utils.eventBusPost
import com.common.webframe.R
import com.common.webframe.databinding.ActivityWebpageBinding
import com.common.webframe.ui.fragment.WebPageFrgment
import kotlinx.android.synthetic.main.layout_toolbar.*

/**
 * desc :WebPageActivity
 * author：panyy
 * data：2020/10/27
 */
open class WebPageActivity<T> : BaseActivity<ActivityWebpageBinding>() {

    private var url: String? = null
    private var isHideToolbar = false
    private var title: String? = null
    private var webPageFrgment: WebPageFrgment? = null
    private var event: SendEventEntity? = null

    override fun initData(savedInstanceState: Bundle?) {
        url = this.intent.getStringExtra("url")
        title = this.intent.getStringExtra("title")
        isHideToolbar = this.intent.getBooleanExtra("isHideToolbar", false)
        event = intent.getSerializableExtra("event") as SendEventEntity?
        if (isHideToolbar) {
            webframe_toolbar.visibility = View.GONE
        }
        if (!TextUtils.isEmpty(title)) {
            tv_webframe_title.visibility = View.VISIBLE
            tv_webframe_title.text = title
        }
        val transaction = supportFragmentManager.beginTransaction()
        webPageFrgment = WebPageFrgment.newInstance(url)
        transaction.add(R.id.fragment_container, webPageFrgment!!)
        transaction.commit()
    }

    fun getWebPageFrgment(): WebPageFrgment? {
        return webPageFrgment
    }

    override fun bindViewClick() {
        bindViewClickListener(iv_webframe_back) {
            when (this) {
                iv_webframe_back -> {
                    activity.onBackPressed()
                }
            }
        }
    }

    fun setStyle(titleColor: Int, toolbarColor: Int) {
        tv_webframe_title!!.setTextColor(resources.getColor(titleColor))
        tv_webframe_right_text!!.setTextColor(resources.getColor(titleColor))
        webframe_toolbar!!.setBackgroundColor(resources.getColor(toolbarColor))
        if (titleColor == R.color.white) {
            iv_webframe_back!!.setImageResource(R.drawable.ic_webframe_back_white)
            iv_webframe_more!!.setImageResource(R.drawable.ic_add_white_24dp)
        } else {
            iv_webframe_back!!.setImageResource(R.drawable.ic_webframe_back_black)
            iv_webframe_more!!.setImageResource(R.drawable.ic_add_black_24dp)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
        }
    }

    override fun onBackPressed() {
        if (webPageFrgment != null && webPageFrgment?.getJsApi() != null) {
            if (webPageFrgment?.getJsApi()?.isCanBack!!) {
                super.onBackPressed()
            } else {
                webPageFrgment?.getJsApi()?.delegateBackEventH5()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (event != null) {
            eventBusPost(WEBFRAME_EVENT, event)
        }
        super.onDestroy()
    }

    companion object {
        fun start(context: Context?, url: String?, title: String? = "", isHideToolbar: Boolean = false, event: SendEventEntity? = null) {
            val intent = Intent(context, WebPageActivity::class.java).apply {
                putExtra("url", url)
                putExtra("title", title)
                putExtra("isHideToolbar", isHideToolbar)
                putExtra("event", event)
            }
            context?.startActivity(intent)
        }
    }
}