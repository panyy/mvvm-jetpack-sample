package com.common.res.entity

import java.io.Serializable

/**
 * desc :发送事件实体类
 * author：panyy
 * data：2018/4/12
 */
class SendEventEntity : Serializable {
    var tag: String? = null
    var event: String? = null
    var args: Any? = null
}