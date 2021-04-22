package com.common.res.router

object RouterHub {

    //服务组件, 用于给每个组件暴露特有的服务
    const val SERVICE = "/service"

    //login组件
    const val LOGIN: String = "/login"

    //main组件
    const val MAIN: String = "/main"

    //uniapp组件
    const val UNIAPP: String = "/uniapp"

    //webframe组件
    const val WEBFRAME: String = "/webframe"

    //login页
    const val PUBLIC_LOGIN: String = "$LOGIN/loginPage"

    //main页
    const val PUBLIC_MAIN: String = "$MAIN/mainPage"

    //web框架服务
    const val WEBFRAME_ERVICE: String = "$WEBFRAME$SERVICE/WebframeService"
}