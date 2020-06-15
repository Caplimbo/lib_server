package com.redite.lib_server.entity

import javax.persistence.*


@Entity
@Table(name="user", uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("name"))],indexes= [Index(columnList = "name")])
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var userID: Int,
        @Column(nullable = false, unique = true)
        var name: String = "",
        @Column(nullable = false)
        var password: String = "",
        @Column(nullable = true)
        var phone: String? = "",
        @Column(nullable = true)
        var email: String? = "",
        @Column(nullable = true)
        var gender: Boolean? = null,
        @Column(nullable = true)
        var token: String? = ""  // for test only set as nullable
)