package org.home.todobackend.controller

import org.home.todobackend.service.StatService
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus

@RestController //@RequestMapping("/stat") // базовый URI
class StatController(private val statService: StatService) {
    @PostMapping("/stat")
    fun findByEmail(@RequestBody email: String): ResponseEntity<Any> {
        return if (email == null || email.trim().isEmpty()) {
            ResponseEntity<Any>("missed param: email mast be not null!", HttpStatus.NOT_ACCEPTABLE)
        } else ResponseEntity.ok(statService.findStat(email))
    }
}