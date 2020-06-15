package com.redite.lib_server.controller

import com.redite.lib_server.entity.User
import com.redite.lib_server.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {
    @Autowired
    lateinit var userRepository: UserRepository

    //获取用户对象
    @RequestMapping("/findbyuserid")
    fun findUserById(userId: Int): User{
        return userRepository.findByUserID(userId)
    }

    @RequestMapping("/findbyname")
    fun findUserByName(name: String): User{
        return userRepository.findByName(name)
    }

    @RequestMapping("/findpasswordbyid")
    fun findPasswordByID(userId: Int): String{
        return userRepository.findPasswordByID(userId)
    }

    @RequestMapping("/findpasswordbyname")
    fun findPasswordByID(name: String): String{
        return userRepository.findPasswordByName(name)
    }

    @RequestMapping("/revisepasswordbyid")
    fun revisePasswordByID(@Param("userID") userId: Int, @Param("password") password: String): String {
        userRepository.updatePasswordByID(userId, password)
        return "Password Revised"
    }

    @RequestMapping("/revisegenderbyid")
    fun reviseGenderByID(@Param("userID") userId: Int, @Param("gender") gender: Boolean): String {
        userRepository.updateGenderByID(userId, gender)
        return "Gender Revised"
    }

    @RequestMapping("/revisephonebyid")
    fun revisePhoneByID(@Param("userID") userId: Int, @Param("phone") phone: String): String {
        userRepository.updatePhoneByID(userId, phone)
        return "Phone Revised"
    }

    @RequestMapping("/reviseemailbyid")
    fun reviseEmailByID(@Param("userID") userId: Int, @Param("email") email: String): String {
        userRepository.updateEmailByID(userId, email)
        return "Email Revised"
    }

    @RequestMapping("/revisetokenbyid")
    fun reviseTokenByID(@Param("userID") userId: Int, @Param("token") token: String): String {
        userRepository.updateTokenByID(userId, token)
        return "Token Revised"
    }

    @RequestMapping("all")
    fun findAll(): MutableList<User>{
        return userRepository.findAll()
    }

    //新建User
    @RequestMapping("/add")
    fun add(@Param("name")name: String, @Param("password")password: String): Int {
        //psd为url里面写的，@Param是注明对应的column
        val user = User(userID = -1, name = name, password = password)

        userRepository.save(user)
        return userRepository.findByName(name).userID
    }
}