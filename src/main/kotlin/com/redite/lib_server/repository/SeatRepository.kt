package com.redite.lib_server.repository

import com.redite.lib_server.entity.Seat
import com.redite.lib_server.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface SeatRepository : JpaRepository<Seat, String> {
    fun findBySeatID(seatID: String): Seat

    fun findByFree(Free: Boolean): Seat

    // 为预约寻找空座位
    @Query("from Seat s where s.tomorrowstart1 >= :endtime or s.tomorrowend1 <= :starttime " +
            "and s.tomorrowstart2 >= :endtime or s.tomorrowend2 <= :starttime")
    fun findSpareSeatByTimePeriod(@Param("starttime") starttime: Int, @Param("endtime") endtime: Int): MutableList<Seat>?

    // 为实时展示寻找空座位
    @Query("from Seat s where s.free = true and :nowtime not between s.todaystart1 and s.todayend1 " +
            "and :nowtime not between s.todaystart2 and s.todayend2")
    fun findSpareSeatNow(@Param("nowtime") nowtime: Int):MutableList<Seat>?

    @Query("select s.seatID from Seat s where s.free= :free")
    fun findSeatByIsFree(@Param("free") free: Boolean): Seat

    /*@Modifying
    @Query("update User u set u.password = :password where u.userID = :userID")
    fun updatePasswordByID(@Param("userID") userID: Int, @Param("password") password: String)

    @Modifying
    @Query("delete from User u where u.name = :name")
    fun deleteByName(@Param("name") name: String)

    @Query("select u.password from User u where u.name = :name")
    fun findPasswordByName(@Param("name") name: String): String

    @Query("select u.password from User u where u.userID = :userID")
    fun findPasswordByID(@Param("userID") userId: Int): String

    @Modifying
    @Query("update User u set u.gender = :gender where u.userID = :userID")
    fun updateGenderByID(@Param("userID") userID: Int, @Param("gender") gender: Boolean)

    @Modifying
    @Query("update User u set u.email = :email where u.userID = :userID")
    fun updateEmailByID(@Param("userID") userID: Int, @Param("email") email: String)

    @Modifying
    @Query("update User u set u.phone = :phone where u.userID = :userID")
    fun updatePhoneByID(@Param("userID") userID: Int, @Param("phone") phone: String)

    @Modifying
    @Query("update User u set u.token = :token where u.userID = :userID")
    fun updateTokenByID(@Param("userID") userID: Int, @Param("token") token: String)*/




}