package com.redite.lib_server.controller

import com.redite.lib_server.entity.Reservation
import com.redite.lib_server.entity.Seat
import com.redite.lib_server.entity.User
import com.redite.lib_server.repository.ReservationRepository
import com.redite.lib_server.repository.SeatRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/order")
class ReservationController {
    @Autowired
    lateinit var reservationRepository: ReservationRepository

    //根据时段、性别、科目筛选
    @RequestMapping("/getreservation")
    fun getValidReservationsByAll(starttime: Int, endtime: Int, gender: Boolean?, subject: String?): MutableList<Reservation>? {
        if (starttime >= endtime || starttime < 8 || endtime > 23) throw Exception("invalid time period")
        if (gender == null) {
            return if (subject == null) {
                reservationRepository.findReservationsByTime(starttime, endtime)
            } else reservationRepository.findReservationsBySubjectAndTime(subject, starttime, endtime)
        }
        return if (subject == null) {
            reservationRepository.findReservationsByGenderAndTime(gender, starttime, endtime)
        }
        else reservationRepository.findReservationsBySubjectAndGenderAndTime(subject, gender, starttime, endtime)
    }

    @RequestMapping("all")
    fun findAll(): MutableList<Reservation>{
        return reservationRepository.findAll()
    }



}