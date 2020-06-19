package com.redite.lib_server.repository

import com.redite.lib_server.entity.Seat
import com.redite.lib_server.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface SeatRepository : JpaRepository<Seat, Int> {
    fun findBySeatID(seatID: Int): Seat

    @Query("select s.free from Seat s where s.seatID = :seatID")
    fun findFreeBySeatID(@Param("seatID") seatID: Int): Boolean

    // 为预约寻找空座位
    @Query("select s.seatID from Seat s where (s.tomorrowstart1 >= :endtime or s.tomorrowend1 <= :starttime) " +
            "and (s.tomorrowstart2 >= :endtime or s.tomorrowend2 <= :starttime)")
    fun findSpareSeatByTimePeriod(@Param("starttime") starttime: Int, @Param("endtime") endtime: Int): MutableList<Int>?

    @Query("select s.seatID from Seat s where s.wait = false and (s.tomorrowstart1 >= :endtime or s.tomorrowend1 <= :starttime) " +
            "and (s.tomorrowstart2 >= :endtime or s.tomorrowend2 <= :starttime)")
    fun findSpareSeatByTimePeriodwithWaitExcluded(@Param("starttime") starttime: Int, @Param("endtime") endtime: Int): MutableList<Int>?

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

    // 设置座位wait状态
    @Transactional
    @Modifying
    @Query("update Seat s set s.wait = true where s.seatID = :seatID")
    fun updateSeatStatusWhenAdjacentBeReserved(@Param("seatID") seatID: Int)

    @Transactional
    @Modifying
    @Query("update Seat s set s.tomorrowstart2 = case when (s.tomorrowstart2 = :starttime) then 0 else s.tomorrowstart2 END, s.tomorrowend2 = case when (s.tomorrowend2 = :endtime) then 0 else s.tomorrowend2 END, s.tomorrowend1 = case when (s.tomorrowend1 = :endtime) then 0 else s.tomorrowend1 END, s.tomorrowstart1 = case when (s.tomorrowstart1 = :starttime) then 0 else s.tomorrowstart1 END where s.seatID = :seatID")
    fun resetSeatByIDAndTime(@Param("seatID") seatID: Int, @Param("starttime") starttime: Int, @Param("endtime") endtime: Int)
}