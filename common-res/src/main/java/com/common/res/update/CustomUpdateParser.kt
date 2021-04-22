package com.common.res.update

import com.common.res.entity.UpdateResultEntity
import com.common.res.utils.GsonUtil
import com.xuexiang.xupdate.entity.UpdateEntity
import com.xuexiang.xupdate.listener.IUpdateParseCallback
import com.xuexiang.xupdate.proxy.IUpdateParser

class CustomUpdateParser : IUpdateParser {

    override fun parseJson(json: String): UpdateEntity {
        return getParseResult(json)!!
    }

    override fun parseJson(json: String, callback: IUpdateParseCallback) {
        //当isAsyncParser为 true时调用该方法, 所以当isAsyncParser为false可以不实现
        callback.onParseResult(getParseResult(json))
    }

    private fun getParseResult(json: String): UpdateEntity? {
        val result = GsonUtil.fromJson(json, UpdateResultEntity::class.java)
        if (result != null) {
            val latestversion = result.data?.latestversion
            if (latestversion != null) {
                val updateitem = result.data?.updateitems?.get(0)
                return UpdateEntity()
                        .setHasUpdate(true)
                        .setIsIgnorable(false)
                        .setVersionCode(latestversion.code)
                        .setVersionName(latestversion.name)
                        .setUpdateContent(latestversion.description)
                        .setDownloadUrl(updateitem?.address)
                        .setForce(latestversion.mandatory)
                        .setSize(updateitem?.filesize!!)
            }
        }
        return UpdateEntity().setHasUpdate(false)
    }

    override fun isAsyncParser(): Boolean {
        return false
    }
}