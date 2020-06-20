package com.redite.lib_server.controller

import com.redite.lib_server.entity.Reservation
import com.redite.lib_server.repository.ReservationRepository
import com.redite.lib_server.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat
import java.util.*

@RestController
@RequestMapping("/reservation")
class ReservationController {
    @Autowired
    lateinit var reservationRepository: ReservationRepository


    // 根据时段、性别、科目筛选
    @RequestMapping("/getreservation")
    fun getValidReservationsByAll(starttime: Int, endtime: Int, targetgender: Boolean?, selfgender: Boolean?, subject: String?): MutableList<Reservation>? {
        if (starttime >= endtime || starttime < 8 || endtime >= 23) throw Exception("invalid time period")
        if (targetgender == null) {
            return if (subject == null) {
                reservationRepository.findReservationsByTime(starttime, endtime)
            } else reservationRepository.findReservationsBySubjectAndTime(subject, starttime, endtime)
        }
        return if (subject == null) {
            reservationRepository.findReservationsByGenderAndTime(targetgender, selfgender, starttime, endtime)
        }
        else reservationRepository.findReservationsBySubjectAndGenderAndTime(subject, targetgender, selfgender, starttime, endtime)
    }

    @RequestMapping("all")
    fun findAll(): MutableList<Reservation>{
        return reservationRepository.findAll()
    }

    // 新建Reservation
    @RequestMapping("/add")
    fun addReservation(userid: Int, seatid: Int, starttime: Int, endtime: Int,
                       pair: Boolean, hang: Boolean, subject: String?,
                        targetgender: Boolean?, selfgender: Boolean?, companion: Int?): Int {
        //psd为url里面写的，@Param是注明对应的column
        val reservation = Reservation(0, userid, seatid, SimpleDateFormat("yyyy-MM-dd").format(Date()), starttime, endtime,
                pair, hang, subject, targetgender, selfgender, companion)
        reservationRepository.save(reservation)
        return reservation.reservationid
    }

    // 本订单找到成功匹配
    @RequestMapping("/release")
    fun release(reservationid: Int, companion: Int?): String{
        reservationRepository.updateHangingStatusAndSetCompanion(reservationid, companion)
        return "Update Success!"
    }

    //查找订单
    @RequestMapping("/findbyorderid")
    fun findByOrderID(orderid: Int): Reservation {
        return reservationRepository.findByReservationid(orderid)
    }

    @RequestMapping("/findbyuserid")
    fun findByUserID(userid: Int): MutableList<Reservation> {
        return reservationRepository.findReservationsByUserID(userid)
    }

    @RequestMapping("/findcompanionsbyuserid")
    fun findCompanionsByUserID(userid: Int): MutableList<Int> {
        return reservationRepository.findCompanionsByUserID(userid)
    }

    @RequestMapping("/finddatesbyuserid")
    fun findDatesByUserID(userid: Int): MutableList<String> {
        return reservationRepository.findDatesByUserID(userid)
    }

    @RequestMapping("/deletebyreservationid")
    fun deleteByReservationid(reservationid: Int) {
        reservationRepository.deleteByReservationid(reservationid)
    }



}