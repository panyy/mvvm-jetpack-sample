package com.common.res.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface

class CProgressDialogUtil private constructor() {

    companion object {
        private val TAG = CProgressDialogUtil::class.java.simpleName
        private var sCircleProgressDialog: ProgressDialog? = null
        fun showProgressDialog(activity: Activity?, listener: DialogInterface.OnCancelListener?) {
            showProgressDialog(activity, "加载中", true, listener)
        }

        fun showProgressDialog(activity: Activity?, msg: String?, listener: DialogInterface.OnCancelListener?) {
            showProgressDialog(activity, msg, true, listener)
        }

        @JvmOverloads
        fun showProgressDialog(activity: Activity?, msg: String? = "加载中", cancelable: Boolean = false, listener: DialogInterface.OnCancelListener? = null) {
            if (activity == null || activity.isFinishing) {
                return
            }
            if (sCircleProgressDialog == null) {
                sCircleProgressDialog = ProgressDialog(activity)
                sCircleProgressDialog!!.setMessage(msg)
                sCircleProgressDialog!!.setOwnerActivity(activity)
                sCircleProgressDialog!!.setOnCancelListener(listener)
                sCircleProgressDialog!!.setCancelable(cancelable)
            } else {
                if (activity == sCircleProgressDialog!!.ownerActivity) {
                    sCircleProgressDialog!!.setMessage(msg)
                    sCircleProgressDialog!!.setCancelable(cancelable)
                    sCircleProgressDialog!!.setOnCancelListener(listener)
                } else {
                    //不相等,所以取消任何ProgressDialog
                    cancelProgressDialog()
                    sCircleProgressDialog = ProgressDialog(activity)
                    sCircleProgressDialog!!.setMessage(msg)
                    sCircleProgressDialog!!.setCancelable(cancelable)
                    sCircleProgressDialog!!.setOwnerActivity(activity)
                    sCircleProgressDialog!!.setOnCancelListener(listener)
                }
            }
            if (!sCircleProgressDialog!!.isShowing) {
                sCircleProgressDialog!!.show()
            }
        }

        fun cancelProgressDialog(activity: Activity) {
            if (sCircleProgressDialog != null && sCircleProgressDialog!!.isShowing) {
                if (sCircleProgressDialog!!.ownerActivity === activity) {
                    sCircleProgressDialog!!.cancel()
                    sCircleProgressDialog = null
                }
            }
        }

        fun cancelProgressDialog() {
            if (sCircleProgressDialog != null && sCircleProgressDialog!!.isShowing) {
                sCircleProgressDialog!!.cancel()
                sCircleProgressDialog = null
            }
        }
    }

    init {
        throw UnsupportedOperationException("cannot be instantiated")
    }

}