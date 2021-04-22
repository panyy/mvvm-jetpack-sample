package com.common.webframe.utils

import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.coder.zzq.smartshow.dialog.EnsureDialog
import com.common.arms.utils.dismissLoadingDialog
import com.common.arms.utils.hideLoadingState
import com.common.arms.utils.showLoadingDialog
import com.common.res.utils.GsonUtil
import com.common.webframe.entity.DialogEntity
import com.common.webframe.jsapi.JsApi
import com.common.webframe.ui.activity.WebPageActivity
import com.luck.picture.lib.entity.LocalMedia

object JsApiHelper {

    fun showPopupMenu(jsApi: JsApi, view: View?) {
        if (jsApi.morebtns == null || jsApi.morebtns!!.size == 0) {
            return
        }
        val popupMenu = PopupMenu(jsApi.activity!!, view!!)
        val size = jsApi.morebtns!!.size
        for (i in 0 until size) {
            popupMenu.menu.add(Menu.NONE, i, i, jsApi.morebtns!![i].name)
        }
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            val morebtn = jsApi.morebtns!![item.itemId]
            if (morebtn.newpage) {
                WebPageActivity.start(context = jsApi.activity!!, url = morebtn.data, isHideToolbar = morebtn.hidetoolbar)
            } else {
                jsApi.dWebView!!.callHandler(morebtn.data, arrayOf())
            }
            true
        }
        popupMenu.show()
    }

    fun showDialog(jsApi: JsApi, data: Any?) {
        val entity = GsonUtil.fromJson(data.toString(), DialogEntity::class.java)
        if ("progressdialog" == entity.type) {
            jsApi.loadingDialog = showLoadingDialog(activity = jsApi.activity, message = entity.message)
        } else if ("alertdialog" == entity.type) {
            val btn1 = entity.buttons?.get(0)
            val btn2 = entity.buttons?.get(1)
            var message: String = entity.message!!
            if (!TextUtils.isEmpty(message)) {
                message += "\n" + entity.messageDetial
            }
            EnsureDialog()
                    .message(message)
                    .confirmBtn(btn1?.name) { dialog, which, data ->
                        dialog.dismiss()
                        if (btn1?.newpage == "true") {
                            WebPageActivity.start(jsApi.activity!!, btn1.data)
                        } else {
                            jsApi.dWebView?.callHandler(btn1?.data, arrayOf())
                        }
                    }
                    .cancelBtn(btn2?.name) { dialog, which, data ->
                        dialog.dismiss()
                        if (btn2?.newpage == "true") {
                            WebPageActivity.start(jsApi.activity!!, btn2.data)
                        } else {
                            jsApi.dWebView?.callHandler(btn2?.data, arrayOf())
                        }
                    }
                    .showInActivity(jsApi.activity)
        }
    }

    fun dismissDialog(jsApi: JsApi, data: Any?) {
        val entity = GsonUtil.fromJson(data.toString(), DialogEntity::class.java)
        if ("progressdialog" == entity.type) {
            dismissLoadingDialog(jsApi.loadingDialog)
        }
    }

}