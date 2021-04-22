package com.common.arms.base.mvvm

import com.common.arms.integration.AppManager
import com.common.arms.utils.ArmsUtil

/**
 * desc：Repository基类
 * author：panyy
 * date：2021/04/22
 */
abstract class BaseRepository {

    inline fun <reified T> apiService(): T =
            ArmsUtil.obtainAppComponentFromContext(AppManager.getAppManager().application).repositoryManager().obtainRetrofitService(T::class.java)

}