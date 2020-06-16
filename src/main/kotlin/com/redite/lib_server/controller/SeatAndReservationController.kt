package com.redite.lib_server.controller

import com.redite.lib_server.entity.Reservation
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
    fun findSeatWithAdjacentReservation(starttime: Int, endtime: Int, subject: String?, gender: Boolean?): HashMap<Int, List<Int>> {
        val spareSeats = seatRepository.findSpareSeatByTimePeriod(starttime, endtime)
                ?: throw Exception("No spare Seats!")  // seatid 列表
        val validReservations = if (gender == null) {
            if (subject == null) {
                reservationRepository.findReservationsByTime(starttime, endtime)
            } else reservationRepository.findReservationsBySubjectAndTime(subject, starttime, endtime)
        }
        else if (subject == null) {
            reservationRepository.findReservationsByGenderAndTime(gender, starttime, endtime)
        }
        else reservationRepository.findReservationsBySubjectAndGenderAndTime(subject, gender, starttime, endtime)

        val validSeatIDWithRespectiveReservation = hashMapOf<Int, List<Int>>()
        for (reservation in validReservations) {
            var seatID = reservation.seatID
            if (seatID % 2 == 1) seatID += 1
            else seatID -= 1
            if (seatID in spareSeats) validSeatIDWithRespectiveReservation[seatID] = listOf(reservation.userID, reservation.reservationid)
        }
        return validSeatIDWithRespectiveReservation
    }
}