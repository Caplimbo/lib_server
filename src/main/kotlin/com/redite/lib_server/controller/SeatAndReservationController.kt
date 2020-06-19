package com.redite.lib_server.controller

import com.redite.lib_server.entity.Reservation
import com.redite.lib_server.others.SeatStatus
import com.redite.lib_server.repository.ReservationRepository
import com.redite.lib_server.repository.SeatRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/seatwithreservation")
class SeatAndReservationController {
    @Autowired
    lateinit var reservationRepository: ReservationRepository

    @Autowired
    lateinit var seatRepository: SeatRepository

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
        val seatid = reservation.seatID
        val starttime = reservation.starttime
        val endtime = reservation.endtime
        reservationRepository.deleteByReservationid(reservationid)
        seatRepository.resetSeatByIDAndTime(seatid, starttime, endtime)
        return "revise succeeded!"
    }

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
            return if (s1 == 0 && s2 == 0 && e1 == 0 && e2 == 0) "Succeed" //没有预定
            else if (s1 == 0 && e1 == 0) findResultByStartAndEndAndSeatId(userid, seatid, s2, e2, now)
            else if (s2 == 0 && e2 == 0) findResultByStartAndEndAndSeatId(userid, seatid, s1, e1, now)
            else {
                if (minOf(e1, e2) <= now) findResultByStartAndEndAndSeatId(userid, seatid, maxOf(s1, s2), maxOf(e1, e2), now)
                else findResultByStartAndEndAndSeatId(userid, seatid, minOf(s1, s2), minOf(e1,e2), now)
            }
        }
    }

    fun findResultByStartAndEndAndSeatId(userid: Int, seatid: Int, starttime: Int, endtime: Int, now:Int): String {
        return if (endtime <= now) "Succeed"
        else {
            val reservation = reservationRepository.findBySeatIDAndStarttime(seatid, starttime)
            when {
                reservation.userID == userid -> "Succeed"
                starttime <= (now-1) -> {
                    reservationRepository.delete(reservation)
                    "Killed" //占座，取消原订单
                }
                starttime == now -> "Booked"
                else -> "Note" //可以占座，但在将来starttime时间有预定
            }
        }
    }
}