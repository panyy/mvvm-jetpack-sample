package com.common.main.ui.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.common.arms.base.mvvm.BaseVMActivity
import com.common.arms.base.state.StateType
import com.common.arms.callback.LoadingCallBack
import com.common.main.databinding.ActivityMainBinding
import com.common.main.ui.adapter.ArticleAdapter
import com.common.main.vm.MainViewModel
import com.common.res.event.LIST_CLICK_EVENT
import com.common.res.page.PageDataHelper
import com.common.res.router.RouterHub
import com.common.res.router.routerProvide
import com.common.res.router.service.IWebFrameService
import com.common.res.utils.appLogoutToLogin
import com.common.res.utils.bindViewClickListener
import com.common.res.utils.eventBusObserve
import com.common.res.utils.eventBusPost
import org.koin.androidx.viewmodel.ext.android.getViewModel

@Route(path = RouterHub.PUBLIC_MAIN)
class MainActivity : BaseVMActivity<ActivityMainBinding, MainViewModel>() {

    private var adapter = ArticleAdapter()

    override fun initVM(): MainViewModel = getViewModel()

    override fun initData(savedInstanceState: Bundle?) {
        vBinding.apply {
            toolbar.tvRightText.text = "退出登录"
            toolbar.tvRightText.visibility = View.VISIBLE
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.adapter = adapter
            refreshLayout.setOnRefreshListener { getArticleList(true) }
            refreshLayout.setOnLoadMoreListener { getArticleList(false) }
        }
        adapter.setOnItemClickListener { _, _, position ->
            //eventbus发送demo
            eventBusPost(LIST_CLICK_EVENT, position)
        }
        dataObserve()
        getArticleList(true)
    }

    override fun reload() {
        getArticleList(true)
    }

    override fun getTargetView(): Any {
        return vBinding.refreshLayout
    }

    private fun getArticleList(isRefresh: Boolean) {
        viewModel.getArticleList(isRefresh)
    }

    private fun dataObserve() {
        viewModel.articleListLiveData.observe(activity, Observer {
            PageDataHelper.loadPageData(vBinding.refreshLayout, loadService, adapter, it)
        })
        viewModel.loadState.observe(this, Observer {
            when (it?.code) {
                StateType.LOADING -> {
                    loadService.showCallback(LoadingCallBack::class.java)
                }
            }
        })
    }

    override fun eventObserve() {
        eventBusObserve(LIST_CLICK_EVENT, this) {
            var entity = adapter.data[it as Int]
            routerProvide<IWebFrameService>()?.start(activity, entity.link, entity.title)
        }
    }

    override fun bindViewClick() {
        bindViewClickListener(vBinding.toolbar.tvRightText) {
            when (this) {
                vBinding.toolbar.tvRightText -> {
                    appLogoutToLogin()
                }
            }
        }
    }
}