package com.common.main.repository

import com.common.arms.base.mvvm.BaseRepository
import com.common.arms.base.net.apiCall
import com.common.main.api.MainService

class MainRepository : BaseRepository() {

    suspend fun getArticleList(page: Int) =
            apiCall {
                apiService<MainService>().getArticleList(page)
            }

}