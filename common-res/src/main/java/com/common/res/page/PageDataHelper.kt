package com.common.res.page

import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.common.arms.base.net.BaseResponse
import com.common.arms.base.state.State
import com.common.arms.base.state.StateType
import com.common.arms.callback.EmptyCallBack
import com.common.arms.callback.ErrorCallBack
import com.common.arms.utils.hideLoadingState
import com.common.arms.utils.showLoadingState
import com.common.res.entity.ListEntity
import com.kingja.loadsir.core.LoadService
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class PageDataHelper {

    var startPage: Int = 1
    var currentPage: Int = startPage

    fun pageDataFront(isRefresh: Boolean, loadState: MutableLiveData<State>) {
        if (isRefresh) {
            if (currentPage == startPage) {
                showLoadingState(loadState, showDialog = false)
            }
            currentPage = startPage
        }
    }

    fun <T> pageDataBehind(isRefresh: Boolean, response: BaseResponse<ListEntity<T>>): BaseResponse<ListEntity<T>> {
        response.isRefresh = isRefresh
        if (response.state?.code == StateType.SUCCESS) {
            if (currentPage == startPage && response.data?.datas.isNullOrEmpty()) {
                response.state = State(StateType.EMPTY)
            } else {
                currentPage++
            }
        }
        return response
    }

    companion object {
        fun <T, VH : BaseViewHolder> loadPageData(
                refreshLayout: SmartRefreshLayout,
                loadService: LoadService<*>,
                adapter: BaseQuickAdapter<T, VH>,
                response: BaseResponse<ListEntity<T>>? = null,
        ) {
            when (response?.state?.code) {
                StateType.EMPTY -> {
                    loadService.showCallback(EmptyCallBack::class.java)
                }
                StateType.ERROR -> {
                    refreshLayout.finishRefresh(false)
                    refreshLayout.finishLoadMore(false)
                    if (response.isRefresh) {
                        loadService.showCallback(ErrorCallBack::class.java)
                    }
                }
                StateType.SUCCESS -> {
                    if (response?.isRefresh!!) {
                        adapter.setNewInstance(response.data?.datas!!)
                        refreshLayout.finishRefresh()
                    } else {
                        adapter.addData(response.data?.datas!!)
                        refreshLayout.finishLoadMore()
                    }
                    if (response.data?.curPage == response.data?.pageCount) {
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    }
                    hideLoadingState()
                    loadService.showSuccess()
                }
            }
        }
    }

}
