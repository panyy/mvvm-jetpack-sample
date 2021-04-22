package com.common.webframe.entity

import java.util.*

/**
 * desc :ToolBar实体类
 * author：panyy
 * data：2018/4/4
 */
class ToolBarEntity {
    var visible = false
    var bgcolor: String? = null
    var title: TitleEntity? = null
    var btnback: BtnbackEntity? = null
    var btnsearch: BtnsearchEntity? = null
    var btnleft: BtnLeftEntity? = null
    var btnright: BtnrightEntity? = null
    var btnmore: ArrayList<BtnmoreEntity>? = null

    class TitleEntity {
        var content: String? = null
        var textsize: String? = null
        var textcolor: String? = null
    }

    class BtnbackEntity
    class BtnsearchEntity
    class BtnmoreEntity {
        var name: String? = null
        var data: String? = null
        var hidetoolbar = false
        var newpage = false
    }

    class BtnrightEntity {
        var name: String? = null
        var image: String? = null
        var data: String? = null
    }

    class BtnLeftEntity {
        var data: String? = null
    }
}