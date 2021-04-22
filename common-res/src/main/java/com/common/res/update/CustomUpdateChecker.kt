package com.common.res.update

import android.app.Activity
import android.text.TextUtils
import com.common.res.utils.CProgressDialogUtil
import com.common.res.utils.showShort
import com.xuexiang.xupdate._XUpdate
import com.xuexiang.xupdate.entity.UpdateError
import com.xuexiang.xupdate.proxy.IUpdateChecker
import com.xuexiang.xupdate.proxy.IUpdateHttpService
import com.xuexiang.xupdate.proxy.IUpdateProxy
import com.xuexiang.xupdate.service.DownloadService
import com.xuexiang.xupdate.utils.UpdateUtils

/**
 * 自定义版本更新检查者
 */
class CustomUpdateChecker(private val activity: Activity, private val isShowDialog: Boolean) : IUpdateChecker {

    override fun onBeforeCheck() {
        if (isShowDialog) {
            CProgressDialogUtil.showProgressDialog(activity, "正在检查新版本...")
        }
    }

    override fun checkVersion(isGet: Boolean, url: String, params: Map<String, Any>, updateProxy: IUpdateProxy) {
        if (DownloadService.isRunning() || _XUpdate.isShowUpdatePrompter()) {
            updateProxy.onAfterCheck()
            _XUpdate.onUpdateError(UpdateError.ERROR.CHECK_UPDATING)
            return
        }
        if (isGet) {
            updateProxy.iUpdateHttpService.asyncGet(url, params, object : IUpdateHttpService.Callback {
                override fun onSuccess(result: String) {
                    onCheckSuccess(result, updateProxy)
                }

                override fun onError(error: Throwable) {
                    onCheckError(updateProxy, error)
                }
            })
        } else {
            updateProxy.iUpdateHttpService.asyncPost(url, params, object : IUpdateHttpService.Callback {
                override fun onSuccess(result: String) {
                    onCheckSuccess(result, updateProxy)
                }

                override fun onError(error: Throwable) {
                    onCheckError(updateProxy, error)
                }
            })
        }
    }

    override fun onAfterCheck() {
        if (isShowDialog) {
            CProgressDialogUtil.cancelProgressDialog(activity)
        }
    }

    /**
     * 查询成功
     *
     * @param result      查询结果
     * @param updateProxy 更新代理
     */
    private fun onCheckSuccess(result: String, updateProxy: IUpdateProxy) {
        updateProxy.onAfterCheck()
        if (!TextUtils.isEmpty(result)) {
            processCheckResult(result, updateProxy)
        } else {
            _XUpdate.onUpdateError(UpdateError.ERROR.CHECK_JSON_EMPTY)
        }
    }

    /**
     * 查询失败
     *
     * @param updateProxy 更新代理
     * @param error       错误
     */
    private fun onCheckError(updateProxy: IUpdateProxy, error: Throwable) {
        updateProxy.onAfterCheck()
        _XUpdate.onUpdateError(UpdateError.ERROR.CHECK_NET_REQUEST, error.message)
    }

    override fun processCheckResult(result: String, updateProxy: IUpdateProxy) {
        try {
            if (updateProxy.isAsyncParser) {
                //异步解析
                updateProxy.parseJson(result) { updateEntity ->
                    try {
                        UpdateUtils.processUpdateEntity(updateEntity, result, updateProxy)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        _XUpdate.onUpdateError(UpdateError.ERROR.CHECK_PARSE, e.message)
                    }
                }
            } else {
                //同步解析
                UpdateUtils.processUpdateEntity(updateProxy.parseJson(result), result, updateProxy)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _XUpdate.onUpdateError(UpdateError.ERROR.CHECK_PARSE, e.message)
        }
    }

    override fun noNewVersion(throwable: Throwable?) {
        if (isShowDialog) {
            showShort(activity, "已是最新版本")
        }
    }
}