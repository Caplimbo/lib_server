package com.redite.lib_server.repository

import com.redite.lib_server.entity.Friend
import com.redite.lib_server.entity.Invite
import com.redite.lib_server.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface InviteRepository: JpaRepository<Invite, Int> {
    fun findByReceiveID(receiveID: Int): MutableList<Invite>

    @Transactional
    @Modifying
    @Query("delete from Invite v where v.receiveID = :receiveID")
    fun deleteByReceiveID(@Param("receiveID") userID: Int)
}