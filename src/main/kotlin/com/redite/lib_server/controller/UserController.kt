package com.redite.lib_server.controller

import com.redite.lib_server.entity.Friend
import com.redite.lib_server.entity.User
import com.redite.lib_server.repository.FriendRepository
import com.redite.lib_server.repository.UserRepository
import com.redite.lib_server.rongcloud.registerToRongCloud
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import io.rong.RongCloud
import io.rong.models.response.TokenResult
import io.rong.models.user.UserModel

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

    /*
    @RequestMapping("/revisepasswordbyid")
    fun revisePasswordByID(@Param("userID") userId: Int, @Param("password") password: String): String {
        userRepository.updatePasswordByID(userId, password)
        return "Password Revised"
    }*/

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
        val userId = userRepository.findByName(name).userID
        val token = registerToRongCloud(userId.toString(),name,"null")
        reviseTokenByID(userId, token)
        return userId
    }

    @RequestMapping("/reviseinfobyid")
    fun reviseInfoByID(@Param("userID") userId: Int, @Param("phone") phone:String?, @Param("email") email: String?,
    @Param("gender") gender: Boolean?, @Param("faversubject") favorsubject:String?):String{
        if(phone != null){
            userRepository.updatePhoneByID(userId, phone)
        }
        if(email != null){
            userRepository.updateEmailByID(userId, email)
        }
        if(gender != null){
            userRepository.updateGenderByID(userId, gender)
        }
        if(favorsubject != null){
            userRepository.updateFavorSubjectByID(userId,favorsubject)
        }
        return "Info Revised"
    }
}

