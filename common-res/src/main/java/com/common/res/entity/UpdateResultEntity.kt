package com.common.res.entity

/**
 * desc :
 * author：panyy
 * data：2020/12/4
 */
class UpdateResultEntity {
    var code: String? = null
    var message: String? = null
    var data: DataEntity? = null
    var sign: String? = null

    class DataEntity {
        var latestversion: LatestversionEntity? = null
        var updateitems: List<UpdateitemsEntity>? = null

        class LatestversionEntity {
            var productcode: Any? = null
            var code: Int = 0
            var name: String = ""
            var description: String? = null
            var versionid: String? = null
            var createtime: String? = null
            var mandatory = false
            var whole = false

        }

        class UpdateitemsEntity {
            var id = 0
            var versionid: String? = null
            var filename: String? = null
            var address: String? = null
            var location: String? = null
            var filetype = 0
            var filesize: Long = 0L
            var isreg = 0
            var deccompressfile = 0
            var createtime: Long = 0
            var decompressfile = 0

        }
    }
}