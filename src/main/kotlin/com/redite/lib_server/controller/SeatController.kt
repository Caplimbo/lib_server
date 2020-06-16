package com.redite.lib_server.controller

import com.redite.lib_server.entity.Seat
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
        else return seatRepository.findSpareSeatByTimePeriod(starttime, endtime)
    }

    // 现在座位情况
    @RequestMapping("/getspareseats/now")
    fun getSpareSeatsForNow(): MutableList<Int>? {
        val nowtime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return seatRepository.findSpareSeatNow(nowtime)
    }

    // 设置占座
    @RequestMapping("setseatoccupied")
    fun setSeatOccupied(@Param("seatid") seatid: Int) {
        seatRepository.updateSeatStatusWhenOccupy(seatid)
    }

    // 设置暂离
    @RequestMapping("setseatleave")
    fun setSeatLeave(@Param("seatid") seatid: Int) {
        seatRepository.updateSeatStatusWhenLeave(seatid)
    }

    // 设置结束
    @RequestMapping("setseatfinish")
    fun setSeatFinish(@Param("seatid") seatid: Int) {
        seatRepository.updateSeatStatusWhenFinish(seatid)
    }

    // 设置预定
    @RequestMapping("/setseat/book")
    fun seatSeatWhenBook(@Param("seatid") seatid: Int, starttime: Int, endtime: Int) {
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




    @RequestMapping("all")
    fun findAll(): MutableList<Seat>{
        return seatRepository.findAll()
    }

}