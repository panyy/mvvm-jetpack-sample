package com.common.res.qrcode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.common.arms.base.BaseActivity
import com.common.res.databinding.ResActivityQrcodeBinding
import com.common.res.immersionbar.BindFullScreen

class QrCodeActivity : BaseActivity<ResActivityQrcodeBinding>(), QRCodeView.Delegate, BindFullScreen {

    override fun initData(savedInstanceState: Bundle?) {
        vBinding.zxingview.setDelegate(this)
    }

    public override fun onStart() {
        super.onStart()
        vBinding.zxingview.startCamera() // 打开后置摄像头开始预览，但是并未开始识别
        vBinding.zxingview.startSpotAndShowRect() // 显示扫描框，并开始识别
    }

    public override fun onStop() {
        vBinding.zxingview.stopCamera() // 关闭摄像头预览，并且隐藏扫描框
        super.onStop()
    }

    override fun onDestroy() {
        vBinding.zxingview.onDestroy() // 销毁二维码扫描控件
        super.onDestroy()
    }

    override fun onScanQRCodeSuccess(result: String?) {
        var intent = Intent().apply { putExtra("result", result) }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        var tipText = vBinding.zxingview.scanBoxView.tipText
        val ambientBrightnessTip = "\n环境过暗，请打开闪光灯"
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                vBinding.zxingview.scanBoxView.tipText = tipText + ambientBrightnessTip
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip))
                vBinding.zxingview.scanBoxView.tipText = tipText
            }
        }
    }

    override fun onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错")
    }

    companion object {
        fun start(context: FragmentActivity, requestCode: Int) {
            context.startActivityForResult(Intent(context, QrCodeActivity::class.java), requestCode)
        }
    }

}