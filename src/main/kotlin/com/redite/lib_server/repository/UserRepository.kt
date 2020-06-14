package com.redite.lib_server.repository

import com.redite.lib_server.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface UserRepository : JpaRepository<User, Int> {

    fun findByName(name: String): List<User>

    fun findByNameAndPassword(name: String, password: String?): User

    @Query("from User u where u.name=:name")
    fun findUser(@Param("name") name: String): User

    @Modifying
    @Query("update User u set u.password = :password where u.name = :name")
    fun updatePasswordByName(@Param("name") name: String, @Param("password") password: String)

    @Modifying
    @Query("delete form User u where u.name = :name")
    fun deleteByName(@Param("name") name: String)


}