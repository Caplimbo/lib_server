package com.redite.lib_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LibServerApplication

fun main(args: Array<String>) {
    runApplication<LibServerApplication>(*args)
}
