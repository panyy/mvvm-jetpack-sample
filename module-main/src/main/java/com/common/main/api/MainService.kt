package com.common.main.api

import com.common.arms.base.net.BaseResponse
import com.common.main.entity.ArticleEntity
import com.common.res.entity.ListEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface MainService {

    /**
     * 首页文章列表
     * 参数：页码，拼接在连接中，从0开始。
     */
    @GET("article/list/{page}/json")
    suspend fun getArticleList(
            @Path("page") page: Int
    ): BaseResponse<ListEntity<ArticleEntity>>

}