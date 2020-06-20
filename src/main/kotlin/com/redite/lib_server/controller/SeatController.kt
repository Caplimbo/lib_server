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

    //根据时段筛选
    @RequestMapping("/getspareseats/tomorrow")
    fun getSpareSeatsForOrder(starttime: Int, endtime: Int): MutableList<Int>? {
        if (starttime >= endtime || starttime < 8 || endtime > 23) throw Exception("invalid time period")
            val firstTrySeats = seatRepository.findSpareSeatByTimePeriodwithWaitExcluded(starttime, endtime)
            return if (firstTrySeats == mutableListOf<Int>()) {
                seatRepository.findSpareSeatByTimePeriod(starttime, endtime)
        }
        else firstTrySeats
    }

    // 现在座位情况
    @RequestMapping("/getspareseats/now")
    fun getSpareSeatsForNow(): MutableList<Int>? {
        val nowtime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (nowtime < 8 || nowtime >= 23) throw Exception("Invalid time")
        return seatRepository.findSpareSeatNow(nowtime)
    }

    // 设置占座
    @RequestMapping("setseatoccupied")
    fun setSeatOccupied(@Param("seatid") seatid: Int): Int {
        seatRepository.updateSeatStatusWhenOccupy(seatid)
        val seat = seatRepository.findBySeatID(seatid)
        return if (seat.todaystart1 < seat.todaystart2) seat.todaystart1 else seat.todaystart2
    }

    // 设置暂离
    @RequestMapping("setseat/leave")
    fun setSeatLeave(@Param("seatid") seatid: Int) {
        seatRepository.updateSeatStatusWhenLeave(seatid)
    }

    // 设置结束
    @RequestMapping("setseat/finish")
    fun setSeatFinish(@Param("seatid") seatid: Int) {
        seatRepository.updateSeatStatusWhenFinish(seatid)
    }

    // 设置预定
    @RequestMapping("/setseat/book")
    fun seatSeatWhenBook(@Param("seatid") seatid: Int, starttime: Int, endtime: Int, reservationid: Int) {
        seatRepository.updateSeatStatusWhenBook(seatid, starttime, endtime)
    }

    // 寻找有相邻座位的空座，用于初次匹配没有得到结果时寻找可选的座位
    @RequestMapping("/getspareseat/withspareadjacent")
    fun getSpareSeatWithSpareAdjacent(starttime: Int, endtime: Int):MutableList<Int>? {
        var adjacentSpareSeatList = mutableListOf<Int>()
        val spareSeatsIDList = seatRepository.findSpareSeatByTimePeriod(starttime, endtime)
        if (spareSeatsIDList != null) {
            for (seatID in spareSeatsIDList) {
                var adjacentSeatID = if (seatID % 2 == 1) seatID + 1
                else seatID - 1
                if (adjacentSeatID in spareSeatsIDList) {
                    adjacentSpareSeatList.add(adjacentSeatID)
                }
            }
        }
        return adjacentSpareSeatList
    }

    // 当相邻位置被有条件预定时，用来更新这个座位的状态
    @RequestMapping("/setseatwait")
    fun setSeatWait(seatid: Int) {
        seatRepository.updateSeatStatusWhenAdjacentBeReserved(seatid)
    }



    @RequestMapping("/all")
    fun findAll(): MutableList<Seat>{
        return seatRepository.findAll()
    }

    @RequestMapping("/findbyid")
    fun getSeatStatus(seatid: Int): Seat {
        return seatRepository.findBySeatID(seatid)
    }


}