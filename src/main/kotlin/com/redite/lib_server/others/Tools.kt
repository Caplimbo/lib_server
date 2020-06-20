package com.redite.lib_server.others

import java.text.SimpleDateFormat
import java.util.*



    //获取某个日期的前一天
    fun getSpecifiedDayBefore(specifiedDay: String): String {
        //输出的日期格式
        var calendar: Calendar = Calendar.getInstance()
        var sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        //自定义过来的String格式的日期
        var date: Date = SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay)
        calendar.time = date
        var day = calendar.get(Calendar.DATE)
        calendar.set(Calendar.DATE, day - 1)
        return sdf.format(calendar.time).toString()
    }

    fun getSpecifiedDayAfter(specifiedDay: String): String {
        var calendar: Calendar = Calendar.getInstance()
        //输出的日期格式
        var sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        //自定义过来的String格式的日期
        var date: Date = SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay)
        calendar.time = date
        var day = calendar.get(Calendar.DATE)
        calendar.set(Calendar.DATE, day + 1)
        return sdf.format(calendar.time).toString()
    }

