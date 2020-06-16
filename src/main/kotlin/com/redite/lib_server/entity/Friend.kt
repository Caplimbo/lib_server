package com.redite.lib_server.entity

import io.rong.models.response.TokenResult
import io.rong.models.user.UserModel
import javax.persistence.*


@Entity
@Table(name="friend")
data class Friend(
        @Id
        var userID: Int,
        @Column(nullable = true)
        var friend_names:String? = null
)