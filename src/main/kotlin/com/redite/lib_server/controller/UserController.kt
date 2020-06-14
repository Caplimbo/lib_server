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

    @RequestMapping("/findpasswordbyname")
    fun findPasswordByName(name: String): String{
        return userRepository.findPasswordByName(name)
    }

    @RequestMapping("/findByName")
    fun findUser(name:String): User{
        return userRepository.findUser(name)

    }

    @RequestMapping("/revisepassword")
    fun revisePasswordByName(@Param("name") name: String, @Param("password")psd: String): String {
        userRepository.updatePasswordByName(name, psd)
        return "Password Revised！"
    }

    @RequestMapping("all")
    fun findAll(): MutableList<User>{
        return userRepository.findAll()
    }

    @RequestMapping("/add")
    fun add(@Param("name")name: String, @Param("password")psd: String): String {
        //psd为url里面写的，@Param是注明对应的column
        val user = User(userID = null, name = name, password = psd)

        userRepository.save(user)
        return "success"
    }
}