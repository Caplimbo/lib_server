package com.redite.lib_server.controller

import com.redite.lib_server.entity.Friend
import com.redite.lib_server.entity.User
import com.redite.lib_server.repository.FriendRepository
import com.redite.lib_server.others.UserStatus
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
    fun findUserById(userid: Int): User{
        return userRepository.findByUserID(userid)
    }

    @RequestMapping("/findbyname")
    fun findUserByName(name: String): User{
        return userRepository.findByName(name)
    }

    @RequestMapping("/findpasswordbyid")
    fun findPasswordByID(userid: Int): String{
        return userRepository.findPasswordByID(userid)
    }

    @RequestMapping("/findpasswordbyname")
    fun findPasswordByName(name: String): String{
        return userRepository.findPasswordByName(name)
    }


    @RequestMapping("/revisepasswordbyid")
    fun revisePasswordByID(@Param("userID") userid: Int, @Param("password") password: String): String {
        userRepository.updatePasswordByID(userid, password)
        return "Password Revised"
    }

    @RequestMapping("/revisegenderbyid")
    fun reviseGenderByID(@Param("userID") userid: Int, @Param("gender") gender: Boolean): String {
        userRepository.updateGenderByID(userid, gender)
        return "Gender Revised"
    }

    @RequestMapping("/revisephonebyid")
    fun revisePhoneByID(@Param("userID") userid: Int, @Param("phone") phone: String): String {
        userRepository.updatePhoneByID(userid, phone)
        return "Phone Revised"
    }

    @RequestMapping("/reviseemailbyid")
    fun reviseEmailByID(@Param("userID") userid: Int, @Param("email") email: String): String {
        userRepository.updateEmailByID(userid, email)
        return "Email Revised"
    }



    @RequestMapping("/revisetokenbyid")
    fun reviseTokenByID(@Param("userID") userid: Int, @Param("token") token: String): String {
        userRepository.updateTokenByID(userid, token)
        return "Token Revised"
    }

    @RequestMapping("/revisestatusbyid")
    fun reviseStatusByID(@Param("userID") userid: Int, @Param("status") status: UserStatus): String {
        userRepository.updateStatusByID(userid,status)
        return "Status Revised"
    }

    @RequestMapping("all")
    fun findAll(): MutableList<User>{
        return userRepository.findAll()
    }

    //新建User
    @RequestMapping("/add")
    fun add(@Param("name")name: String, @Param("password")password: String): Int {
        //psd为url里面写的，@Param是注明对应的column
        val user = User(userID = -1, name = name, password = password, status = UserStatus.FREE)
        userRepository.save(user)
        val userId = userRepository.findByName(name).userID
        val token = registerToRongCloud(userId.toString(),name,"null")
        reviseTokenByID(userId, token)
        return userId
    }

    @RequestMapping("/reviseinfobyid")
    fun reviseInfoByID(@Param("userID") userid: Int, @Param("phone") phone:String?, @Param("email") email: String?,
                       @Param("gender") gender: Boolean?, @Param("favorsubject") favorsubject:String?, @Param("password")password: String?):String{
        if(phone != null){
            userRepository.updatePhoneByID(userid, phone)
        }
        if(email != null){
            userRepository.updateEmailByID(userid, email)
        }
        if(gender != null){
            userRepository.updateGenderByID(userid, gender)
        }
        if(favorsubject != null){
            userRepository.updateFavorSubjectByID(userid,favorsubject)
        }
        return "Info Revised"
    }
}

