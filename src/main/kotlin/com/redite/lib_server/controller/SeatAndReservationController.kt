package com.redite.lib_server.controller

import com.redite.lib_server.entity.Reservation
import com.redite.lib_server.others.SeatStatus
import com.redite.lib_server.others.UserStatus
import com.redite.lib_server.repository.ReservationRepository
import com.redite.lib_server.repository.SeatRepository
import com.redite.lib_server.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

@RestController
@RequestMapping("/seatwithreservation")
class SeatAndReservationController {
    @Autowired
    lateinit var reservationRepository: ReservationRepository

    @Autowired
    lateinit var seatRepository: SeatRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @RequestMapping("/findseatwithadjacentreservation")
    fun findSeatWithAdjacentReservation(starttime: Int, endtime: Int, subject: String?,
                                        targetgender: Boolean?, selfgender: Boolean?): HashMap<Int, List<Int>> {
        val spareSeats = seatRepository.findSpareSeatByTimePeriod(starttime, endtime)
                ?: throw Exception("No spare Seats!")  // seatid 列表
        println(spareSeats)
        val validReservations = if (targetgender == null) {
            if (subject == null) {
                reservationRepository.findReservationsByTime(starttime, endtime)
            } else reservationRepository.findReservationsBySubjectAndTime(subject, starttime, endtime)
        }
        else if (subject == null) {
            reservationRepository.findReservationsByGenderAndTime(targetgender,selfgender, starttime, endtime)
        }
        else reservationRepository.findReservationsBySubjectAndGenderAndTime(subject, targetgender, selfgender, starttime, endtime)

        val validSeatIDWithRespectiveReservation = hashMapOf<Int, List<Int>>()
        for (reservation in validReservations) {
            var seatID = reservation.seatID
            if (seatID % 2 == 1) seatID += 1
            else seatID -= 1
            println(seatID)
            if (seatID in spareSeats) validSeatIDWithRespectiveReservation[seatID] = listOf(reservation.userID, reservation.reservationid)
            println(seatID)
        }
        return validSeatIDWithRespectiveReservation
    }

    @RequestMapping("deletebyreservationid")
    fun deleteOrderAndResetSeatStatusByReservationID(reservationid: Int): String {
        val reservation = reservationRepository.findByReservationid(reservationid)
        reservationRepository.delete(reservation)
        seatRepository.resetSeatByReservationID(reservationid)
        return "revise succeeded!"
    }

    // 整个扫码流程需要下面三个函数
    @RequestMapping("tryoccupyseat")
    fun tryoccupyseat(userid: Int, seatid: Int, now: Int): String{
        val seat = seatRepository.findBySeatID(seatid)
        // 若seat状态不是free
        if (!seat.free) {
            return if (seat.status == SeatStatus.OCCUPIED) "Occupied"
            else "Leave"
        }
        else { // seat 为free，检查其后的预定造成的状态
            val s1 = seat.todaystart1
            val s2 = seat.todaystart2
            val e1 = seat.todayend1
            val e2 = seat.todayend2

            // 进行判断
            return if (s1 == 0 && s2 == 0 && e1 == 0 && e2 == 0) {
                // 没有预定，直接成功占座，修改用户状态
                occupySeat(seatid, userid)
            } else if (s1 == 0 && e1 == 0) findResultByStartAndEndAndSeatId(userid, seatid, s2, e2, seat.reservationid2, now)
            else if (s2 == 0 && e2 == 0) findResultByStartAndEndAndSeatId(userid, seatid, s1, e1, seat.reservationid1, now)
            else {
                if (minOf(e1, e2) <= now){
                    if (e1 < e2) findResultByStartAndEndAndSeatId(userid, seatid, maxOf(s1, s2), maxOf(e1, e2), seat.reservationid2, now)
                    else findResultByStartAndEndAndSeatId(userid, seatid, maxOf(s1, s2), maxOf(e1, e2), seat.reservationid1, now)
                }

                else {
                    if (minOf(s1, s2) <= now - 1) maxOf(s1, s2).toString()
                    else{
                        if (e1 < e2) findResultByStartAndEndAndSeatId(userid, seatid, minOf(s1, s2), minOf(e1, e2),seat.reservationid1, now)
                        else findResultByStartAndEndAndSeatId(userid, seatid, minOf(s1, s2), minOf(e1, e2),seat.reservationid2, now)
                    }
                }
            }
        }
    }

    fun findResultByStartAndEndAndSeatId(userid: Int, seatid: Int, starttime: Int, endtime: Int, reservationid: Int, now:Int): String {
        if (endtime <= now) { //前一个订单已经结束，占座
            return occupySeat(seatid, userid)
        }
        else {
            val reservation = reservationRepository.findByReservationid(reservationid)
            return when {
                reservation.userID == userid -> { //自己的预订，直接占座
                    occupySeat(seatid, userid)
                }
                starttime <= (now-1) -> {
                    reservationRepository.delete(reservation) //删除原有订单
                    occupySeat(seatid, userid) // 返回的还是succeed
                }
                starttime == now -> "Booked" //本时段有预定，占座失败并提示被预定
                else -> { // 之后有预定，成功占座并提醒
                    userRepository.updateStatusByID(userid, UserStatus.ACTIVE)
                    seatRepository.updateSeatStatusWhenOccupy(seatid)
                    starttime.toString()
                }
            }
        }
    }

    fun occupySeat(seatid: Int, userid:Int): String { //成功占座且不需提醒
        userRepository.updateStatusByID(userid, UserStatus.ACTIVE)
        seatRepository.updateSeatStatusWhenOccupy(seatid)
        return "Succeed"
    }

    // 预定选座后的流程
    @RequestMapping("book")
    fun book(userid: Int, seatid: Int, starttime: Int, endtime: Int,
             pair: Boolean, hang: Boolean, wait: Boolean, subject: String?,
             targetgender: Boolean?, selfgender: Boolean?, companion: Int?): String {
        val reservation = Reservation(0, userid, seatid, Date(), starttime, endtime,
                pair, hang, subject, targetgender, selfgender, companion)
        val id = reservation.reservationid
        seatRepository.updateSeatStatusWhenBook(seatid, starttime, endtime, id)
        if (wait) {
            val adjacentid = if (id % 2 == 0)id-1 else id+1
            seatRepository.updateSeatStatusWhenAdjacentBeReserved(seatid)
        }
        return "Succeed"
    }
}