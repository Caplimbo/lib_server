package com.redite.lib_server.entity

import javax.persistence.*

@Entity
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var userID: Int?,
        @Column(nullable = false)
        var name: String = "",
        @Column(nullable = false)
        var password: String = ""
)