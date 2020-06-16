package com.redite.lib_server.controller

import com.redite.lib_server.entity.Friend
import com.redite.lib_server.entity.User
import com.redite.lib_server.repository.FriendRepository
import com.redite.lib_server.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/friend")
class FriendController {
    @Autowired
    lateinit var friendRepository: FriendRepository

    @RequestMapping("all")
    fun findAll(): MutableList<Friend>{
        return friendRepository.findAll()
    }

    @RequestMapping("/add")
    fun add(@Param("userID")userID: Int):String{
        friendRepository.add(userID,"")
        return "add successful"
    }

    @RequestMapping("/getallfriendnames")
    fun getAllFriendNames(@Param("userID")userID: Int):Friend?{
        return friendRepository.findByUserID(userID)
    }

    @RequestMapping("/addfriend")
    fun addFriend(@Param("userID")userID: Int,@Param("name")name:String):String{
        val new_name = ','+name
        friendRepository.addFriend(userID,new_name)
        return "add friend successful"
    }
}