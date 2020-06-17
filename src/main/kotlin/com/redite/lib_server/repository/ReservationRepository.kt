package com.redite.lib_server.repository

import com.redite.lib_server.entity.Reservation
import com.redite.lib_server.entity.Seat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface ReservationRepository : JpaRepository<Reservation, Int> {
    fun findByReservationid(reservationID: Int): Reservation

    fun findReservationsByUserID(userID: Int): MutableList<Reservation>

    @Query("select r.ordertime from Reservation r where r.userID = :userid")
    fun findDatesByUserID(@Param("userid") userid: Int): MutableList<Date>

    @Query("select r.companion from Reservation r where r.userID = :userid")
    fun findCompanionsByUserID(@Param("userid") userid: Int): MutableList<Int>

    // 只按照时间寻找
    @Query("from Reservation v where v.hang = true and not (v.endtime < :starttime or v.starttime > :endtime)")
    fun findReservationsByTime(@Param("starttime") starttime: Int, @Param("endtime") endtime: Int): MutableList<Reservation>

    // 按照性别条件寻找
    @Query("from Reservation v where v.hang = true and (v.targetgender = :selfgender or v.targetgender is null) and v.selfgender = :targetgender " +
            "and not (v.endtime < :starttime or v.starttime > :endtime)")
    fun findReservationsByGenderAndTime(@Param("targetgender") targetgender: Boolean, @Param("selfgender") selfgender: Boolean?, @Param("starttime")
    starttime: Int, @Param("endtime") endtime: Int): MutableList<Reservation>


    // 按照科目条件寻找
    @Query("from Reservation v where v.hang = true and v.subject = :subject and not (v.endtime < :starttime or v.starttime > :endtime)")
    fun findReservationsBySubjectAndTime(@Param("subject") subject: String, @Param("starttime")
    starttime: Int, @Param("endtime") endtime: Int): MutableList<Reservation>

    // 按照性别和科目条件寻找
    @Query("from Reservation v where v.hang = true and v.subject = :subject and (v.targetgender = :selfgender or v.targetgender is null) and v.selfgender = :targetgender " +
            "and not (v.endtime < :starttime or v.starttime > :endtime)")
    fun findReservationsBySubjectAndGenderAndTime(@Param("subject") subject: String,
                                                  @Param("targetgender") targetgender: Boolean,
                                                  @Param("selfgender") selfgender: Boolean?,
                                                  @Param("starttime") starttime: Int,
                                                  @Param("endtime") endtime: Int): MutableList<Reservation>


    // 设置挂起状态
    @Transactional
    @Modifying
    @Query("update Reservation r set r.hang = false, r.companion = :companion where r.reservationid = :reservationid")
    fun updateHangingStatusAndSetCompanion(@Param("reservationid") reservationID: Int, @Param("companion") companion: Int?)



}