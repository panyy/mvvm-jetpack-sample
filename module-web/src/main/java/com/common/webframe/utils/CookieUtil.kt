package com.common.webframe.utils

import android.content.Context
import android.os.Build
import android.text.TextUtils
import com.common.res.config.AppConfig
import com.common.webframe.view.agentweb.AgentWebConfig
import com.tencent.smtt.sdk.CookieManager
import com.tencent.smtt.sdk.CookieSyncManager
import java.net.URL

/**
 * desc :Cookie工具类
 * author：panyy
 * data：2018/5/5
 */
object CookieUtil {
    /**
     * 同步指定url的Cookie
     *
     * @param context
     */
    fun syncCookie(url: String, context: Context?) {
        if (TextUtils.isEmpty(url) || !url.startsWith("http")) {
            return
        }
        try {
            val host = URL(url).host
            CookieSyncManager.createInstance(context)
            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            cookieManager.setCookie(host, "token=" + AppConfig.accessToken)
            cookieManager.setCookie(host, "hybrid=" + "\"true\"")
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.getInstance().sync()
            } else {
                CookieManager.getInstance().flush()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 同步指定url和内容的Cookie
     *
     * @param context
     */
    fun syncCookie(url: String, context: Context?, key: String, content: String) {
        if (TextUtils.isEmpty(url) || !url.startsWith("http")) {
            return
        }
        try {
            val host = URL(url).host
            CookieSyncManager.createInstance(context)
            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            cookieManager.setCookie(host, "$key=$content")
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                CookieSyncManager.getInstance().sync()
            } else {
                CookieManager.getInstance().flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 清除Cookie
     *
     * @param context
     */
    fun removeCookie(context: Context?) {
        CookieSyncManager.createInstance(context)
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookie()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync()
        } else {
            CookieManager.getInstance().flush()
        }
    }

    fun clearDiskCache(context: Context?) {
        AgentWebConfig.clearDiskCache(context)
    }
}