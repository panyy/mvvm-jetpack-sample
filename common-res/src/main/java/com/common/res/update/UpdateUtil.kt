package com.common.res.update

import android.app.Activity
import com.xuexiang.xupdate.XUpdate
import com.xuexiang.xupdate.utils.UpdateUtils

/**
 * desc :应用更新工具类
 * author：panyy
 * data：2020/12/4
 */
object UpdateUtil {

    private const val updateUrl = "http://www.baidu.com"

    fun checkUpdate(activity: Activity, isShowDialog: Boolean = false) {
        XUpdate.newBuild(activity)
                .updateUrl(updateUrl)
                .param("productType", "2")
                .param("productCode", activity.packageName)
                .param("versionCode", UpdateUtils.getVersionCode(activity))
                .supportBackgroundUpdate(true)
                .promptWidthRatio(0.75F)
                .updateParser(CustomUpdateParser())
                .updateChecker(CustomUpdateChecker(activity, isShowDialog))
                .update()
    }

}