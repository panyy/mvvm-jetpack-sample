package com.common.webframe.entity

import java.io.Serializable
import java.util.*

/**
 * desc :
 * author：panyy
 * data：2018/4/12
 */
class TabPageEntity : Serializable {
    var closenumpage = 0
    var tabs: ArrayList<TabEntity>? = null

    class TabEntity : Serializable {
        var name: String? = null
        var url: String? = null
    }
}