package com.common.res.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Type

object GsonUtil {

    private var filterNullGson: Gson? = null
    private var nullableGson: Gson? = null

    init {
        nullableGson = GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                .create()
        filterNullGson = GsonBuilder()
                .enableComplexMapKeySerialization()
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")
                .create()
    }

    /**
     * 根据对象返回json   不过滤空值字段
     */
    fun toJson(obj: Any?): String {
        return nullableGson!!.toJson(obj)
    }

    /**
     * 根据对象返回json  过滤空值字段
     */
    fun toJsonFilterNull(obj: Any?): String {
        return filterNullGson!!.toJson(obj)
    }

    /**
     * 将json转化为对应的实体对象
     */
    fun <T> fromJson(json: String?, classOfT: Class<T>?): T {
        return nullableGson!!.fromJson(json, classOfT)
    }

    /**
     * 将json转化为对应的实体对象
     */
    fun <T> fromJson(json: String?, type: Type?): T {
        return nullableGson!!.fromJson(json, type)
    }

    /**
     * 将对象值赋值给目标对象
     *
     * @param source 源对象
     * @param <T>    目标对象类型
     * @return 目标对象实例
    </T> */
    fun <T> convert(source: Any?, clz: Class<T>?): T {
        val json = toJsonFilterNull(source)
        return fromJson(json, clz)
    }

}