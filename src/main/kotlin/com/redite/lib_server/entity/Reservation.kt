package com.redite.lib_server.entity

import com.redite.lib_server.others.SeatStatus
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "reservation")
class Reservation (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var reservationid: Int,
        @Column(nullable = false)
        var userID: Int,
        @Column(nullable = false)
        var seatID:Int,
        @Temporal(TemporalType.TIMESTAMP)
        @Column(nullable = false)
        var ordertime: Date,
        @Column(nullable = false)
        var starttime: Int,
        @Column(nullable = false)
        var endtime: Int,
        @Column(nullable = false)
        var pairstatus:Boolean, // 是否需要组队 true for yes
        @Column(nullable = false, columnDefinition = "bool default true")
        var hang: Boolean,  // 是否正在等待配对
        @Column(nullable = true)
        var subject:String?, // 科目选择
        @Column(nullable = true)
        var gender:Boolean? //true for male

)