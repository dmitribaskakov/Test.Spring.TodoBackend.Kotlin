package org.home.todobackend.controller;

import org.home.todobackend.entity.Stat;
import org.home.todobackend.service.StatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/stat") // базовый URI
public class StatController {
    public final StatService statService;

    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/stat")
    public ResponseEntity<Stat> findByEmail(@RequestBody String email) {
        if (email == null || email.trim().length() == 0) {
            return new ResponseEntity("missed param: email mast be not null!", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(statService.findStat(email));
    }

}
