package com.common.res.utils

import android.os.Bundle
import com.common.arms.integration.AppManager
import com.common.res.config.AppConfig
import com.common.res.router.RouterHub
import com.common.res.router.routerNavigation

/**
 * 登出
 */
fun appLogout() {
    checkLogin {
        if (it) {
            AppConfig.clearUserData()
            AppManager.getAppManager().killAll()
        }
    }
}

/**
 * 登出并跳转到登录页面
 */
fun appLogoutToLogin() {
    checkLogin {
        if (it) {
            appLogout()
            routerNavigation(RouterHub.PUBLIC_LOGIN)
        }
    }
}

/**
 * 登出并跳转到登录界面,并自动登录
 * @param username
 * @param password
 */
fun appLogoutAutoLogin(username: String?, password: String?) {
    checkLogin {
        if (it) {
            appLogout()
            var bundle = Bundle().apply {
                putString("username", username)
                putString("password", password)
            }
            routerNavigation(RouterHub.PUBLIC_LOGIN, bundle)
        }
    }
}

/**
 * 检查登录
 */
fun checkLogin(block: (it: Boolean) -> Unit) {
    block(AppConfig.login)
}