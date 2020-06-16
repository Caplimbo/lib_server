package com.redite.lib_server.controller

import com.redite.lib_server.entity.Friend
import com.redite.lib_server.entity.Invite
import com.redite.lib_server.entity.User
import com.redite.lib_server.repository.FriendRepository
import com.redite.lib_server.repository.InviteRepository
import com.redite.lib_server.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/invite")
class InviteController {
    @Autowired
    lateinit var inviteRepository: InviteRepository

    @RequestMapping("all")
    fun findAll(): MutableList<Invite>{
        return inviteRepository.findAll()
    }

    @RequestMapping("/getInvitation")
    fun getInvitation(receiveID:Int):MutableList<Invite>{
        val invitations = inviteRepository.findByReceiveID(receiveID)
        inviteRepository.deleteByReceiveID(receiveID)
        return invitations
    }

    @RequestMapping("/add")
    fun add(sendID:Int, sendname:String, receiveID:Int):String{
        val invite = Invite(-1,sendID,sendname,receiveID)
        inviteRepository.save(invite)
        return "add successful"
    }

}