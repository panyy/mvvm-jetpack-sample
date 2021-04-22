package com.common.res.config

import cn.vove7.smartkey.AConfig
import cn.vove7.smartkey.annotation.Config
import cn.vove7.smartkey.key.smartKey
import com.common.res.smartkey.MmkvSettingsImpl

@Config(implCls = MmkvSettingsImpl::class)
object AppConfig : AConfig() {
    var login: Boolean by smartKey(false)
    var userName: String? by smartKey(null)
    var accessToken: String? by smartKey(null)

    fun clearUserData() {
        login = false
        accessToken = null
    }
}