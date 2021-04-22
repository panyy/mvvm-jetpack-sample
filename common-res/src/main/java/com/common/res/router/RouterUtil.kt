package com.common.res.router

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import com.alibaba.android.arouter.facade.template.IProvider
import com.alibaba.android.arouter.launcher.ARouter

fun routerNavigation(path: String?): Any? {
    return ARouter.getInstance().build(path).navigation()
}

fun routerNavigation(path: String?, bundle: Bundle?): Any? {
    return ARouter.getInstance().build(path).with(bundle).navigation()
}

fun routerNavigation(context: Context?, path: String?): Any? {
    return ARouter.getInstance().build(path).navigation(context)
}

fun routerNavigation(context: Context?, path: String?, bundle: Bundle?): Any? {
    return ARouter.getInstance().build(path).with(bundle).navigation(context)
}

inline fun <reified T : IProvider?> routerProvide(path: String?): T? {
    if (TextUtils.isEmpty(path)) {
        return null
    }
    var provider: IProvider? = null
    try {
        provider = ARouter.getInstance()
                .build(path)
                .navigation() as IProvider
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return provider as T?
}

inline fun <reified T : IProvider?> routerProvide(): T? {
    var provider: IProvider? = null
    try {
        provider = ARouter.getInstance().navigation(T::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return provider as T?
}