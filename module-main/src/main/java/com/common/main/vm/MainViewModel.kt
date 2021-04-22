package com.common.main.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.common.arms.base.mvvm.BaseViewModel
import com.common.arms.base.net.BaseResponse
import com.common.main.entity.ArticleEntity
import com.common.main.repository.MainRepository
import com.common.res.entity.ListEntity
import com.common.res.page.PageDataHelper
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository, private val pageDataHelper: PageDataHelper) : BaseViewModel() {

    val articleListLiveData: MutableLiveData<BaseResponse<ListEntity<ArticleEntity>>> = MutableLiveData()

    fun getArticleList(isRefresh: Boolean) {
        viewModelScope.launch {
            pageDataHelper.pageDataFront(isRefresh, loadState)
            repository.getArticleList(pageDataHelper.currentPage).apply {
                articleListLiveData.postValue(pageDataHelper.pageDataBehind(isRefresh, this))
            }
        }
    }

}