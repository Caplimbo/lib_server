package com.redite.lib_server.entity

import io.rong.models.response.TokenResult
import io.rong.models.user.UserModel
import javax.persistence.*


@Entity
@Table(name="invite")
data class Invite(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var inviteID: Int,
        @Column(nullable = true)
        var sendID:Int,
        @Column(nullable = true)
        var sendname:String,
        @Column(nullable = true)
        var receiveID:Int
)