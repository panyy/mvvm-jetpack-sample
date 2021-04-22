package com.common.res.utils

import android.R
import android.app.Activity
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout

class AndroidBug5497Workaround {
    private var mChildOfContent: View
    private var usableHeightPrevious = 0
    private var frameLayoutParams: FrameLayout.LayoutParams
    private var isImmersive = false

    private constructor(activity: Activity) {
        val content = activity.findViewById<View>(R.id.content) as FrameLayout
        mChildOfContent = content.getChildAt(0)
        mChildOfContent.viewTreeObserver.addOnGlobalLayoutListener { // possiblyResizeChildOfContent();
            possiblyResizeChildOfContent(activity, checkDeviceHasNavigationBar(activity))
        }
        frameLayoutParams = mChildOfContent.layoutParams as FrameLayout.LayoutParams
    }

    private constructor(activity: Activity, isImmersive: Boolean) {
        this.isImmersive = isImmersive
        val content = activity.findViewById<View>(R.id.content) as FrameLayout
        mChildOfContent = content.getChildAt(0)
        mChildOfContent.viewTreeObserver.addOnGlobalLayoutListener { // possiblyResizeChildOfContent();
            possiblyResizeChildOfContent(activity, checkDeviceHasNavigationBar(activity))
        }
        frameLayoutParams = mChildOfContent.layoutParams as FrameLayout.LayoutParams
    }

    private fun possiblyResizeChildOfContent() {
        val usableHeightNow = computeUsableHeight()
        if (usableHeightNow != usableHeightPrevious) {
            val usableHeightSansKeyboard = mChildOfContent.rootView.height
            val heightDifference = usableHeightSansKeyboard - usableHeightNow
            if (heightDifference > usableHeightSansKeyboard / 4) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard
            }
            mChildOfContent.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    private fun computeUsableHeight(): Int {
        val r = Rect()
        mChildOfContent.getWindowVisibleDisplayFrame(r)
        return if (isImmersive) r.bottom else r.bottom - r.top // 全屏模式下： return r.bottom
    }

    /**
     * 重新调整布局高度
     * 获取界面可用高度，如果软键盘弹起后，Activity的xml布局可用高度需要减去键盘高度
     *
     * @param hasNav
     */
    private fun possiblyResizeChildOfContent(activity: Activity, hasNav: Boolean) {
        //1､获取当前界面可用高度，键盘弹起后，当前界面可用布局会减少键盘的高度
        val usableHeightNow = computeUsableHeight()
        //2､如果当前可用高度和原始值不一样
        if (usableHeightNow != usableHeightPrevious) {
            //3､获取Activity中xml中布局在当前界面显示的高度
            var usableHeightSansKeyboard: Int
            if (hasNav) usableHeightSansKeyboard = mChildOfContent.height //兼容华为等机型
            else {
                usableHeightSansKeyboard = mChildOfContent.rootView.height
                //这个判断是为了解决19之前的版本不支持沉浸式状态栏导致布局显示不完全的问题
                val frame = Rect()
                activity.window.decorView.getWindowVisibleDisplayFrame(frame)
                val statusBarHeight = frame.top
                if (!isImmersive) usableHeightSansKeyboard -= statusBarHeight
            }
            //4､Activity中xml布局的高度-当前可用高度
            val heightDifference = usableHeightSansKeyboard - usableHeightNow
            //5､高度差大于屏幕1/4时，说明键盘弹出
            if (heightDifference > usableHeightSansKeyboard / 4) {
                // keyboard probably just became visible
                // 6､键盘弹出了，Activity的xml布局高度应当减去键盘高度
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference
            } else {
                if (hasNav) frameLayoutParams.height = usableHeightNow else frameLayoutParams.height = usableHeightSansKeyboard
            }
            //7､ 重绘Activity的xml布局
            mChildOfContent.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    /**
     * 计算mChildOfContent可见高度 ** @return
     */
    private fun computeUsableHeight(activity: Activity, hasNav: Boolean): Int {
        return if (hasNav) {
            val r = Rect()
            mChildOfContent.getWindowVisibleDisplayFrame(r)
            // 全屏模式下：直接返回r.bottom，r.top其实是状态栏的高度
            r.bottom
        } else {
            val frame = Rect()
            activity.window.decorView.getWindowVisibleDisplayFrame(frame)
            val statusBarHeight = frame.top
            val r = Rect()
            mChildOfContent.getWindowVisibleDisplayFrame(r)

            //这个判断是为了解决19之后的版本在弹出软键盘时，键盘和推上去的布局（adjustResize）之间有黑色区域的问题
            r.bottom - r.top + statusBarHeight
        }
    }

    companion object {
        // For more information, see https://issuetracker.google.com/issues/36911528
        // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.
        @JvmStatic
        @JvmOverloads
        fun assistActivity(activity: Activity, isImmersive: Boolean = false) {
            AndroidBug5497Workaround(activity, isImmersive)
        }

        /**
         * 通过"qemu.hw.mainkeys"判断是否存在NavigationBar
         *
         * @return 是否有NavigationBar
         */
        private fun checkDeviceHasNavigationBar(activity: Activity): Boolean {
            var hasNavigationBar = false
            val rs = activity.resources
            val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
            if (id > 0) {
                hasNavigationBar = rs.getBoolean(id)
            }
            try {
                val systemPropertiesClass = Class.forName("android.os.SystemProperties")
                val m = systemPropertiesClass.getMethod("get", String::class.java)
                val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
                hasNavigationBar = if ("1" == navBarOverride) {
                    false
                } else if ("0" == navBarOverride) {
                    true
                } else {
                    hasNavBar(activity)
                }
            } catch (e: Exception) {
            }
            return hasNavigationBar
        }

        /**
         * 根据屏幕真实宽高-可用宽高>0来判断是否存在NavigationBar
         *
         * @param activity 上下文
         * @return 是否有NavigationBar
         */
        private fun hasNavBar(activity: Activity): Boolean {
            val windowManager = activity.windowManager
            val d = windowManager.defaultDisplay
            val realDisplayMetrics = DisplayMetrics()
            d.getRealMetrics(realDisplayMetrics)
            val realHeight = realDisplayMetrics.heightPixels
            val realWidth = realDisplayMetrics.widthPixels
            val displayMetrics = DisplayMetrics()
            d.getMetrics(displayMetrics)
            val displayHeight = displayMetrics.heightPixels
            val displayWidth = displayMetrics.widthPixels
            return realWidth - displayWidth > 0 || realHeight - displayHeight > 0
        }
    }
}