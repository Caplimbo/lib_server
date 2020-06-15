package com.redite.lib_server.rongcloud

import io.rong.RongCloud
import io.rong.models.response.TokenResult
import io.rong.models.user.UserModel

fun registerToRongCloud(userID:String,name:String,portraitUri:String): String {
    val appKey = "bmdehs6pba5ps"
    val appSecret = "M9h3JaECSqSyLi"

    val rongCloud = RongCloud.getInstance(appKey, appSecret)
    val user: io.rong.methods.user.User = rongCloud.user

    /**
     * API 文档: http://www.rongcloud.cn/docs/server_sdk_api/user/user.html#register
     *
     * 注册用户，生成用户在融云的唯一身份标识 Token
     */
    val userModel = UserModel()
            .setId(userID)
            .setName(name)
            .setPortrait(portraitUri)
    val result: TokenResult = user.register(userModel)
    return if(result.code == 200){
        result.token
    } else{
        "Error with $result"
    }
}