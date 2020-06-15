package com.redite.lib_server.repository

import com.redite.lib_server.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface UserRepository : JpaRepository<User, Int> {
    fun findByUserID(userID: Int): User

    fun findByName(name: String): User

    fun findByNameAndPassword(name: String, password: String?): User

    @Query("from User u where u.name=:name")
    fun findUser(@Param("name") name: String): User

    @Modifying
    @Query("update User u set u.password = :password where u.userID = :userID")
    fun updatePasswordByID(@Param("userID") userID: Int, @Param("password") password: String)

    @Modifying
    @Query("delete from User u where u.name = :name")
    fun deleteByName(@Param("name") name: String)

    @Query("select u.password from User u where u.name = :name")
    fun findPasswordByName(@Param("name") name: String): String

    @Query("select u.password from User u where u.userID = :userID")
    fun findPasswordByID(@Param("userID") userId: Int): String

    @Modifying
    @Query("update User u set u.gender = :gender where u.userID = :userID")
    fun updateGenderByID(@Param("userID") userID: Int, @Param("gender") gender: Boolean)

    @Modifying
    @Query("update User u set u.email = :email where u.userID = :userID")
    fun updateEmailByID(@Param("userID") userID: Int, @Param("email") email: String)

    @Modifying
    @Query("update User u set u.phone = :phone where u.userID = :userID")
    fun updatePhoneByID(@Param("userID") userID: Int, @Param("phone") phone: String)

    @Modifying
    @Query("update User u set u.token = :token where u.userID = :userID")
    fun updateTokenByID(@Param("userID") userID: Int, @Param("token") token: String)




}