package com.common.res.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * desc :http获取part
 * author：panyy
 * date：2021/04/22
 */
object HttpPartUtil {
    const val IMAGETYPE = "image/jpeg"
    const val AUDIOTYPE = "audio/wav"
    const val VIDEOTYPE = "video/mp4"

    /**
     * 获取图片part
     *
     * @param filepath  文件路径
     * @param paramName 参数名
     * @return
     */
    fun getImagePart(filepath: String?, paramName: String?): MultipartBody.Part {
        val file = File(filepath)
        val requestBody = RequestBody.create(IMAGETYPE.toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(paramName!!, file.name, requestBody)
    }

    /**
     * 获取音频part
     *
     * @param filepath  文件路径
     * @param paramName 参数名
     * @return
     */
    fun getAudioPart(filepath: String?, paramName: String?): MultipartBody.Part {
        val file = File(filepath)
        val requestBody = RequestBody.create(AUDIOTYPE.toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(paramName!!, file.name, requestBody)
    }

    /**
     * 获取视频part
     *
     * @param filepath  文件路径
     * @param paramName 参数名
     * @return
     */
    fun getVideoPart(filepath: String?, paramName: String?): MultipartBody.Part {
        val file = File(filepath)
        val requestBody = RequestBody.create(VIDEOTYPE.toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(paramName!!, file.name, requestBody)
    }
}