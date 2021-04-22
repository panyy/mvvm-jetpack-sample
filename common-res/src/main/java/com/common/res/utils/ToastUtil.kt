package com.common.res.utils

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import com.common.arms.integration.AppManager
import java.text.MessageFormat

/**
 * @param activity
 *
 * @param resId string资源id
 *
 * @param duration
 */
private fun showToast(
        activity: Activity?, resId: Int,
        duration: Int
) {
    if (activity == null)
        return
    val currentActivity = AppManager.getAppManager().currentActivity
    currentActivity?.runOnUiThread {
        Toast.makeText(currentActivity, resId, duration).show()
    }
}

/**
 * @param activity
 *
 * @param message toast的内容
 *
 * @param duration
 */
private fun showToast(
        activity: Activity?, message: String,
        duration: Int
) {
    if (activity == null)
        return
    if (TextUtils.isEmpty(message))
        return

    val currentActivity = AppManager.getAppManager().currentActivity
    currentActivity?.runOnUiThread {
        Toast.makeText(currentActivity, message, duration).show()
    }
}

fun showToast(
        context: Context?, resId: Int,
        duration: Int
) {
    if (context == null)
        return
    val currentActivity = AppManager.getAppManager().currentActivity
    currentActivity?.runOnUiThread {
        Toast.makeText(currentActivity, resId, duration).show()
    }
}

fun showToast(
        context: Context?, message: String,
        duration: Int
) {
    if (context == null)
        return
    val currentActivity = AppManager.getAppManager().currentActivity
    currentActivity?.runOnUiThread {
        Toast.makeText(currentActivity, message, duration).show()
    }

}

fun showLong(activity: Activity?, resId: Int) {
    showToast(activity, resId, Toast.LENGTH_LONG)
}

fun showLong(activity: Activity?, message: String) {
    showToast(activity, message, Toast.LENGTH_LONG)
}

fun showLong(
        activity: Activity?, message: String,
        vararg args: Any
) {
    val formatted = MessageFormat.format(message, *args)
    showToast(activity, formatted, Toast.LENGTH_LONG)
}

fun showLong(
        activity: Activity?, resId: Int,
        vararg args: Any
) {
    if (activity == null)
        return

    val message = activity.getString(resId)
    showLong(activity, message, *args)
}

fun showLong(context: Context?, resId: Int) {
    showToast(context, resId, Toast.LENGTH_LONG)
}

fun showLong(context: Context?, message: String) {
    showToast(context, message, Toast.LENGTH_LONG)
}

fun showShort(activity: Activity?, resId: Int) {
    showToast(activity, resId, Toast.LENGTH_SHORT)
}

fun showShort(activity: Activity?, message: String) {
    showToast(activity, message, Toast.LENGTH_SHORT)
}

fun showShort(
        activity: Activity?, message: String,
        vararg args: Any
) {
    val formatted = MessageFormat.format(message, *args)
    showToast(activity, formatted, Toast.LENGTH_SHORT)
}

fun showShort(
        activity: Activity?, resId: Int,
        vararg args: Any
) {
    if (activity == null)
        return

    val message = activity.getString(resId)
    showShort(activity, message, *args)
}

fun showShort(context: Context?, resId: Int) {
    showToast(context, resId, Toast.LENGTH_SHORT)
}

fun showShort(context: Context?, message: String) {
    showToast(context, message, Toast.LENGTH_SHORT)
}
