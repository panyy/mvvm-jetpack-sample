package com.common.res.entity

data class ListEntity<T>(
        var curPage: Int? = null,
        var offset: Int? = null,
        var over: Boolean? = null,
        var pageCount: Int? = null,
        var size: Int? = null,
        var total: Int? = null,
        var datas: MutableList<T>? = null
)