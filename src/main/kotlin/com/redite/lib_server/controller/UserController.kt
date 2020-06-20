package com.redite.lib_server.controller

import RSACrypt
import com.redite.lib_server.entity.User
import com.redite.lib_server.others.UserStatus
import com.redite.lib_server.repository.UserRepository
import com.redite.lib_server.rongcloud.registerToRongCloud
import org.apache.commons.codec.binary.Base64
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec

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
        val privateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCISnPbDpVogCS6o7qnakZQXtWd2xuGsFd6iExoVtWeAiz6BKlZRm0x+FNOiCRk3DG/cVHLQ2imE84Fj4sjR0EfzAqk2yp4uGpSQRVB5AZsvXHPEop+g7UfQ2ditKxpgY3NwAwyiJ8WLSF1M7DupPRak7rUQVbLdnNRIojg8sVW02ZyrBaCQvWXC2ij0yGFJki2xB70cbO/wVHQCaHR6l8Qmr5dVZQCe9vOPfDrSdhbxxMxV2jA6thAEswfZlChn4o0sQm+YZ7jjACBGTr5XxRmnEUbYSaPp2ImiFwbtqO/7sQ3k2HbKbpCGpU0DpIkFqBUmayBReZGzeRCSp69qqlhAgMBAAECggEAfK/IoG8OJ2WctJeX6xPiMiMgwWM0Ipvv3C0X4hkXI5CP/0gGeqNgwOeTp3QL/64nYSLWFuSbt9zEjffN7j+BCAsO7dWIXJsywf/C594qNo92sGAhDi+Go4xx6C6s0XVx+vso7Y5qJMqzIlthEmMdHkSrFwaT1l6Oj3rNySaxfYVhjONROdYzE6nbbWqJo7KZwQebpMD2k1c/cMgB7ZZLTHyivVFqbOs7ehPF+dt9JoJb7mk5DgnI559HUj5u6OOZWOXqi0ZN5aqfmAXQ2IiicCLNH0WFFh4a5psbxEACNr4YdOIB+0nf/Cdl3Jhj9j33dLHxiKpBiEcXKh4Y/KZxwQKBgQDFzxIhNO4rITS6b1qxsfi6B7qI3lULIOaCw1z1rjBNzkURhv6Gk4taKtJRpj3uuoVKXYKDesjZRBBU3KdAh9Ri7NXuAuo0Uqge5sotXt+H06dqolpwFLg7im+tg3RQscNfU38w/w6owWkkI1saA7IlY7vhtJBDaOwt1oC001/weQKBgQCwYnnPmp/PcZPjhi4sWFu2D4x+Q5cqDT2fvr3VbN7dSfgexVZbi+yBBJb7ImvcpDNumtfJ2ke6VGndtwDlq82g/cveJ0pfyygqd5gb1UB8QZH3Jvaux3fc9UysQfgHelzxhMxFrGaTyxU70DBNBVx1bhvKuo43nPmUFliacIjWKQKBgBCqu+pUXWUA7UJwM4IZbs2t7QevRBcl9IY8E88XmDZWFPe/Gh8Yu3sjdIkwqrM8wBJeAqs1Y0r7My0TUnxbdYfIUNY3JKi39jZ0V8Cu58K4ELkpaNlcxZEuvu4tz1UlRUL49BSayQ9rDILg/8IXYaFI4AHKhwVHB+9szXdu//xxAoGAGnNqGPvzI0TBDMm+et/1QOQsCXNRY/kAWMhd53egJEdHcjXdsXTWTrLFgXuz+S6Wl+uYmKinQQYPqjQbalXNTGtC+1mhCaSJrXSp1uKv/PJIVWv+ak93ZidTDDIIY/axoXZsu7YS2+8EjCe4VpSLPQD6IfjAAw8DTnXba62cl6ECgYEAgD74ljg4YWkAPn+4kd3G2lg4QXwY9l8i7S4gQfuUxKICZpo3YC0vda6Gww5MGkA6JUEfMqR+ghtSU8pPYrImLdHFw8Wfx6LwgEiigkiYTV0bcr3C3FzTP9cbLUfum2wI4Y2icpPwBRWQ0fbpJt47/OLqMFaaKbwz+Zz7r+VxUTE="
        val keyFactory = KeyFactory.getInstance("RSA")
        val privateKey = keyFactory.generatePrivate(PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyStr)))
        val decryptPassword = RSACrypt.decryptByPrivateKey(password, privateKey)
        val user = User(userID = -1, name = name, password = decryptPassword, status = UserStatus.FREE, reserved = false)
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

    @RequestMapping("/verify")
    fun verify(@Param("name") name:String, @Param("password") password: String):User{
        val privateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCISnPbDpVogCS6o7qnakZQXtWd2xuGsFd6iExoVtWeAiz6BKlZRm0x+FNOiCRk3DG/cVHLQ2imE84Fj4sjR0EfzAqk2yp4uGpSQRVB5AZsvXHPEop+g7UfQ2ditKxpgY3NwAwyiJ8WLSF1M7DupPRak7rUQVbLdnNRIojg8sVW02ZyrBaCQvWXC2ij0yGFJki2xB70cbO/wVHQCaHR6l8Qmr5dVZQCe9vOPfDrSdhbxxMxV2jA6thAEswfZlChn4o0sQm+YZ7jjACBGTr5XxRmnEUbYSaPp2ImiFwbtqO/7sQ3k2HbKbpCGpU0DpIkFqBUmayBReZGzeRCSp69qqlhAgMBAAECggEAfK/IoG8OJ2WctJeX6xPiMiMgwWM0Ipvv3C0X4hkXI5CP/0gGeqNgwOeTp3QL/64nYSLWFuSbt9zEjffN7j+BCAsO7dWIXJsywf/C594qNo92sGAhDi+Go4xx6C6s0XVx+vso7Y5qJMqzIlthEmMdHkSrFwaT1l6Oj3rNySaxfYVhjONROdYzE6nbbWqJo7KZwQebpMD2k1c/cMgB7ZZLTHyivVFqbOs7ehPF+dt9JoJb7mk5DgnI559HUj5u6OOZWOXqi0ZN5aqfmAXQ2IiicCLNH0WFFh4a5psbxEACNr4YdOIB+0nf/Cdl3Jhj9j33dLHxiKpBiEcXKh4Y/KZxwQKBgQDFzxIhNO4rITS6b1qxsfi6B7qI3lULIOaCw1z1rjBNzkURhv6Gk4taKtJRpj3uuoVKXYKDesjZRBBU3KdAh9Ri7NXuAuo0Uqge5sotXt+H06dqolpwFLg7im+tg3RQscNfU38w/w6owWkkI1saA7IlY7vhtJBDaOwt1oC001/weQKBgQCwYnnPmp/PcZPjhi4sWFu2D4x+Q5cqDT2fvr3VbN7dSfgexVZbi+yBBJb7ImvcpDNumtfJ2ke6VGndtwDlq82g/cveJ0pfyygqd5gb1UB8QZH3Jvaux3fc9UysQfgHelzxhMxFrGaTyxU70DBNBVx1bhvKuo43nPmUFliacIjWKQKBgBCqu+pUXWUA7UJwM4IZbs2t7QevRBcl9IY8E88XmDZWFPe/Gh8Yu3sjdIkwqrM8wBJeAqs1Y0r7My0TUnxbdYfIUNY3JKi39jZ0V8Cu58K4ELkpaNlcxZEuvu4tz1UlRUL49BSayQ9rDILg/8IXYaFI4AHKhwVHB+9szXdu//xxAoGAGnNqGPvzI0TBDMm+et/1QOQsCXNRY/kAWMhd53egJEdHcjXdsXTWTrLFgXuz+S6Wl+uYmKinQQYPqjQbalXNTGtC+1mhCaSJrXSp1uKv/PJIVWv+ak93ZidTDDIIY/axoXZsu7YS2+8EjCe4VpSLPQD6IfjAAw8DTnXba62cl6ECgYEAgD74ljg4YWkAPn+4kd3G2lg4QXwY9l8i7S4gQfuUxKICZpo3YC0vda6Gww5MGkA6JUEfMqR+ghtSU8pPYrImLdHFw8Wfx6LwgEiigkiYTV0bcr3C3FzTP9cbLUfum2wI4Y2icpPwBRWQ0fbpJt47/OLqMFaaKbwz+Zz7r+VxUTE="
        val keyFactory = KeyFactory.getInstance("RSA")
        val privateKey = keyFactory.generatePrivate(PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyStr)))
        val decryptPassword = RSACrypt.decryptByPrivateKey(password, privateKey)
        val user = findUserByName(name)
        if(user.password == decryptPassword){
            user.password= 0.toString()
            return user
        }
        return User(userID = -1, name = name, password = 0.toString(), status = UserStatus.FREE)
    }
}

