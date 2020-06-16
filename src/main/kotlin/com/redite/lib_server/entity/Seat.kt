package com.redite.lib_server.entity

import com.redite.lib_server.others.SeatStatus
import javax.persistence.*

@Entity
@Table(name = "seat")
class Seat (
    @Id
    var seatID: Int,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: SeatStatus,
    @Column(name = "free", nullable = false, columnDefinition="bool default true")
    var free: Boolean = true,
    @Column(nullable = true, columnDefinition = "INT default 0")
    var todaystart1: Int,
    @Column(nullable = true, columnDefinition = "INT default 0")
    var todaystart2: Int,
    @Column(nullable = true, columnDefinition = "INT default 0")
    var todayend1: Int,
    @Column(nullable = true, columnDefinition = "INT default 0")
    var todayend2: Int,
    @Column(nullable = true, columnDefinition = "INT default 0")
    var tomorrowstart1: Int,
    @Column(nullable = true, columnDefinition = "INT default 0")
    var tomorrowstart2: Int,
    @Column(nullable = true, columnDefinition = "INT default 0")
    var tomorrowend1: Int,
    @Column(nullable = true, columnDefinition = "INT default 0")
    var tomorrowend2: Int
)