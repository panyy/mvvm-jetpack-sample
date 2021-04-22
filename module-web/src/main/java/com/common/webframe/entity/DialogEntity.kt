package com.common.webframe.entity

/**
 * desc :
 * author：panyy
 * data：2018/5/3
 */
class DialogEntity {
    var type: String? = null
    var title: String? = null
    var message: String? = null
    var messageDetial: String? = null
    var buttons: List<ButtonEntity>? = null

    class ButtonEntity {
        var name: String? = null
        var data: String? = null
        var newpage: String? = null
    }
}