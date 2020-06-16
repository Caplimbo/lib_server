package com.redite.lib_server.repository

import com.redite.lib_server.entity.Reservation
import com.redite.lib_server.entity.Seat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface ReservationRepository : JpaRepository<Reservation, Int> {
    fun findByReservationID(reservationID: Int): Reservation


    // 只按照时间寻找
    @Query("from Reservation v where v.starttime between (:starttime+1) AND (:endtime-1) " +
            "or v.endtime between (:starttime+1) AND (:endtime-1)")
    fun findReservationsByTime(@Param("starttime") starttime: Int, @Param("endtime") endtime: Int): MutableList<Reservation>

    // 按照性别条件寻找
    @Query("from Reservation v where v.gender = :gender and v.starttime between (:starttime+1) AND (:endtime-1) " +
            "or v.endtime between (:starttime+1) AND (:endtime-1)")
    fun findReservationsByGenderAndTime(@Param("gender") gender: Boolean, @Param("starttime")
    starttime: Int, @Param("endtime") endtime: Int): MutableList<Reservation>


    // 按照科目条件寻找
    @Query("from Reservation v where v.subject = :subject and v.starttime between (:starttime+1) AND (:endtime-1) " +
            "or v.endtime between (:starttime+1) AND (:endtime-1)")
    fun findReservationsBySubjectAndTime(@Param("subject") subject: String, @Param("starttime")
    starttime: Int, @Param("endtime") endtime: Int): MutableList<Reservation>

    // 按照性别和科目条件寻找
    @Query("from Reservation v where v.subject = :subject and v.gender = :gender and v.starttime between (:starttime+1) AND (:endtime-1)" +
            " or v.endtime between (:starttime+1) AND (:endtime-1)")
    fun findReservationsBySubjectAndGenderAndTime(@Param("subject") subject: String, @Param("gender") gender: Boolean,
                                                  @Param("starttime") starttime: Int, @Param("endtime") endtime: Int): MutableList<Reservation>

    // 为实时展示寻找空座位
    @Query("select s.seatID from Seat s where s.free = true and :nowtime not between s.todaystart1 and s.todayend1 " +
            "and :nowtime not between s.todaystart2 and s.todayend2")
    fun findSpareSeatNow(@Param("nowtime") nowtime: Int):MutableList<Int>?

    // 现场占座处理，暂离返回同样调用此函数
    @Transactional
    @Modifying
    @Query("update Seat s set s.free = false, s.status = 'OCCUPIED' where s.seatID = :seatID")
    fun updateSeatStatusWhenOccupy(@Param("seatID") seatID: Int)


    // 现场暂离处理
    @Transactional
    @Modifying
    @Query("update Seat s set s.free = false, s.status = 'LEAVE' where s.seatID = :seatID")
    fun updateSeatStatusWhenLeave(@Param("seatID") seatID: Int)

    // 现场离开处理
    @Transactional
    @Modifying
    @Query("update Seat s set s.free = true, s.status = 'FREE' where s.seatID = :seatID")
    fun updateSeatStatusWhenFinish(@Param("seatID") seatID: Int)

    // 预约占座处理
    @Transactional
    @Modifying
    @Query("update Seat s set s.tomorrowstart2 = case when (s.tomorrowstart1 <> 0 and s.tomorrowstart2 = 0) then :starttime else s.tomorrowstart2 END, s.tomorrowend2 = case when (s.tomorrowend1 <> 0 and s.tomorrowend2 = 0) then :endtime else s.tomorrowend2 END, s.tomorrowend1 = case when (s.tomorrowend1 = 0) then :endtime else s.tomorrowend1 END, s.tomorrowstart1 = case when (s.tomorrowstart1 = 0) then :starttime else s.tomorrowstart1 END where s.seatID = :seatID")
    fun updateSeatStatusWhenBook(@Param("seatID") seatID: Int, @Param("starttime") starttime:Int, @Param("endtime") endtime: Int)


}