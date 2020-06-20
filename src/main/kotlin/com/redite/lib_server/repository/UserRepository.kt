package com.redite.lib_server.repository

import com.redite.lib_server.entity.User
import com.redite.lib_server.others.UserStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface UserRepository : JpaRepository<User, Int> {
    fun findByUserID(userID: Int): User

    fun findByName(name: String): User

    fun findByNameAndPassword(name: String, password: String?): User

    @Query("from User u where u.name=:name")
    fun findUser(@Param("name") name: String): User

    @Transactional
    @Modifying
    @Query("update User u set u.password = :password where u.name = :name")
    fun updatePasswordByName(@Param("name") name: String, @Param("password") password: String)

    @Transactional
    @Modifying
    @Query("delete from User u where u.name = :name")
    fun deleteByName(@Param("name") name: String)

    @Query("select u.password from User u where u.name = :name")
    fun findPasswordByName(@Param("name") name: String): String

    @Query("select u.password from User u where u.userID = :userID")
    fun findPasswordByID(@Param("userID") userId: Int): String

    @Transactional
    @Modifying
    @Query("update User u set u.password = :password where u.userID = :userID")
    fun updatePasswordByID(@Param("userID") userID: Int, @Param("password") password: String)

    @Transactional
    @Modifying
    @Query("update User u set u.gender = :gender where u.userID = :userID")
    fun updateGenderByID(@Param("userID") userID: Int, @Param("gender") gender: Boolean)

    @Transactional
    @Modifying
    @Query("update User u set u.email = :email where u.userID = :userID")
    fun updateEmailByID(@Param("userID") userID: Int, @Param("email") email: String)

    @Transactional
    @Modifying
    @Query("update User u set u.phone = :phone where u.userID = :userID")
    fun updatePhoneByID(@Param("userID") userID: Int, @Param("phone") phone: String)

    @Transactional
    @Modifying
    @Query("update User u set u.token = :token where u.userID = :userID")
    fun updateTokenByID(@Param("userID") userID: Int, @Param("token") token: String)

    @Transactional
    @Modifying
    @Query("update User u set u.status = :status where u.userID = :userID")
    fun updateStatusByID(@Param("userID") userID: Int, @Param("status") status: UserStatus)

    @Transactional
    @Modifying
    @Query("update User u set u.favorsubject = :favorsubject where u.userID = :userID")
    fun updateFavorSubjectByID(@Param("userID") userID: Int, @Param("favorsubject") favorsubject:String)

    @Transactional
    @Modifying
    @Query("update User u set u.reserved = :reserved where u.userID = :userID")
    fun updateReservedByID(@Param("userID") userID: Int, @Param("reserved") reserved: Boolean)
}