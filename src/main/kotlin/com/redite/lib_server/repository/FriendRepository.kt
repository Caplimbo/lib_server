package com.redite.lib_server.repository

import com.redite.lib_server.entity.Friend
import com.redite.lib_server.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface FriendRepository : JpaRepository<Friend, Int> {
    fun findByUserID(userID: Int): Friend

    @Transactional
    @Modifying
    @Query("update Friend f set f.friend_names = CONCAT(friend_names, :name) where f.userID = :userID", nativeQuery = true)
    fun addFriend(@Param("userID") userID: Int, @Param("name") name:String)

    @Transactional
    @Modifying
    @Query("insert into friend(userID,friend_names) values(:userID,:friend_name)", nativeQuery = true)
    fun add(@Param("userID") userID: Int, @Param("friend_name") friendname:String?)
}