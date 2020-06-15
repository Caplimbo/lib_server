package com.redite.lib_server.controller

import com.redite.lib_server.entity.Seat
import com.redite.lib_server.entity.User
import com.redite.lib_server.repository.SeatRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/seat")
class SeatController {
    @Autowired
    lateinit var seatRepository: SeatRepository

    //获取用户对象
    @RequestMapping("/findbyfree")
    fun findUserById(free: Boolean): Seat {
        return seatRepository.findByFree(free)
    }

    //根据时段筛选
    @RequestMapping("/getspareseats/tomorrow")
    fun getSpareSeatsForOrder(@Param("starttime") starttime: Int, @Param("endtime") endtime: Int): MutableList<Seat>? {
        if (starttime >= endtime || starttime < 8 || endtime > 23) throw Exception("invalid time period")
        else return seatRepository.findSpareSeatByTimePeriod(starttime, endtime)
    }

    @RequestMapping("/getspareseats/now")
    fun getSpareSeatsForNow(): MutableList<Seat>? {
        val nowtime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return seatRepository.findSpareSeatNow(nowtime)
    }

    @RequestMapping("all")
    fun findAll(): MutableList<Seat>{
        return seatRepository.findAll()
    }

    //新建User
}