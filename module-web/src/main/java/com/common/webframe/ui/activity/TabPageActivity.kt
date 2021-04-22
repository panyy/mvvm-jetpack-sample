package com.common.webframe.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.common.webframe.R
import com.common.webframe.databinding.ActivityTabpageBinding
import com.common.webframe.entity.RefreshTabRightBtnEntity
import com.common.webframe.entity.TabPageEntity.TabEntity
import com.common.webframe.ui.adapter.TabPagerAdapter
import com.common.webframe.ui.fragment.WebPageFrgment
import com.common.webframe.view.BaseDWebView
import kotlinx.android.synthetic.main.activity_tabpage.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.util.*

/**
 * desc :TabPageActivity
 * author：panyy
 * data：2018/3/14
 */
class TabPageActivity : WebPageActivity<ActivityTabpageBinding>() {

    private var tabs: ArrayList<TabEntity>? = null
    private val tabFragments: MutableList<WebPageFrgment> = ArrayList()
    private var currPosition = 0
    private var btnRights: List<RefreshTabRightBtnEntity.BtnrightEntity>? = null

    override fun initData(savedInstanceState: Bundle?) {
        tabs = this.intent.getSerializableExtra("btntabs") as ArrayList<TabEntity>?
        iv_webframe_back.setOnClickListener { v -> activity.onBackPressed() }
        radio_group.visibility = View.VISIBLE
        for (i in tabs!!.indices) {
            val tab = tabs!![i]
            tabFragments.add(WebPageFrgment.newInstance(tab.url))
            val rb = LayoutInflater.from(activity).inflate(R.layout.layout_radiobutton, null) as RadioButton
            rb.text = tab.name
            radio_group.addView(rb)
        }
        val pagerAdapter = TabPagerAdapter(supportFragmentManager, tabFragments)
        viewpager?.adapter = pagerAdapter
        viewpager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                val rb = radio_group.getChildAt(position) as RadioButton
                rb.isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        radio_group.setOnCheckedChangeListener { group, checkedId ->
            for (i in 0 until group.childCount) {
                val view = group.getChildAt(i)
                if (view.id == checkedId) {
                    currPosition = i
                    break
                }
            }
            refreshTabRightBtn()
            viewpager?.currentItem = currPosition
        }
        val rb = radio_group.getChildAt(0) as RadioButton
        rb.isChecked = true
    }

    fun setAndRefreshTabRightBtn(btnRights: List<RefreshTabRightBtnEntity.BtnrightEntity>?) {
        this.btnRights = btnRights
        refreshTabRightBtn()
    }

    private fun refreshTabRightBtn() {
        runOnUiThread {
            if (btnRights != null) {
                val name = btnRights!![currPosition].name
                if (!TextUtils.isEmpty(name)) {
                    tv_webframe_right_text.visibility = View.VISIBLE
                    tv_webframe_right_text.text = name
                    tv_webframe_right_text.setOnClickListener(View.OnClickListener {
                        val dWebView: BaseDWebView? = tabFragments[currPosition].getDWebView()
                        dWebView?.callHandler(btnRights!![currPosition].data, arrayOf())
                    })
                }
                val image = btnRights!![currPosition].image
                if (!TextUtils.isEmpty(image)) {
                    iv_right_image!!.visibility = View.VISIBLE
                    Glide.with(this@TabPageActivity).load(image).into(iv_right_image!!)
                    iv_right_image!!.setOnClickListener {
                        tabFragments[currPosition].getDWebView()?.callHandler(btnRights!![currPosition].data, arrayOf())
                    }
                }
            } else {
                tv_webframe_right_text.visibility = View.GONE
            }
        }
    }

    companion object {
        fun openTabPage(context: Context?, tabs: ArrayList<TabEntity>?) {
            val intent = Intent(context, TabPageActivity::class.java)
            intent.putExtra("btntabs", tabs)
            context?.startActivity(intent)
        }
    }
}