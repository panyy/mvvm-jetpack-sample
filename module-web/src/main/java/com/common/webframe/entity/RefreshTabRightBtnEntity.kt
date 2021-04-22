package com.common.webframe.entity

/**
 * desc :
 * author：panyy
 * data：2018/4/12
 */
class RefreshTabRightBtnEntity {
    var btnrights: List<BtnrightEntity>? = null

    class BtnrightEntity {
        var name: String? = null
        var image: String? = null
        var data: String? = null
    }
}