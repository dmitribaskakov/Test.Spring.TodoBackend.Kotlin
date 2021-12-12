package org.home.todobackend.controller

import org.home.todobackend.entity.Priority
import org.home.todobackend.search.SearchValues
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.home.todobackend.service.PriorityService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.dao.EmptyResultDataAccessException
import java.util.NoSuchElementException


/*
Используем @RestController вместо обычного @Controller, чтобы все ответы сразу оборачивались в JSON,
иначе пришлось бы добавлять лишние объекты в код, использовать @ResponseBody для ответа, указывать тип отправки JSON
Названия методов могут быть любыми, главное не дублировать их имена внутри класса и URL mapping
*/
@RestController
@RequestMapping("/priority") // базовый URI
// автоматическое внедрение экземпляра класса через конструктор
// не используем @Autowired ля переменной класса, т.к. "Field injection is not recommended "
class PriorityController (private val priorityService: PriorityService) {

    //    @GetMapping("/id")
    //    public Priority findById() {
    //        return PriorityService.findById(120127L);
    //    }
    @PostMapping("/all")
    fun findAll(@RequestBody email: String): List<Priority> {
        return priorityService.findAll(email)
    }

    @PostMapping("/add")
    fun add(@RequestBody priority: Priority): ResponseEntity<Any> {
        //проверка на обязательные параметры
        if (priority.id != null && priority.id != 0L) { //Уже не новый объект
            return ResponseEntity<Any>("redundant param: id mast be null!", HttpStatus.NOT_ACCEPTABLE)
        }
        //если передали пустые значимые поля
        if (priority.title == null || priority.title.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: title mast be not null!", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity.ok(priorityService.add(priority))
    }

    @PutMapping("/update")
    fun update(@RequestBody priority: Priority): ResponseEntity<Any> {
        //проверка на обязательные параметры
        if (priority.id == null || priority.id == 0L) { //Еще новый объект
            return ResponseEntity<Any>("missed param: id mast be not null!", HttpStatus.NOT_ACCEPTABLE)
        }
        //если передали пустые значимые поля
        if (priority.title == null || priority.title.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: title mast be not null!", HttpStatus.NOT_ACCEPTABLE)
        }
        priorityService.update(priority)
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    //    @DeleteMapping("/delete/{id}/{email}")
    //    public ResponseEntity delete(@PathVariable("id") Long id, @PathVariable("email") String email) {
    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        try {
            priorityService.delete(id)
        } catch (ex: EmptyResultDataAccessException) {
            ex.printStackTrace()
            return ResponseEntity<Any>("id = $id not found!", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity<Any>(HttpStatus.OK)
    }

    @PostMapping("/id")
    fun getByID(@RequestBody id: Long): ResponseEntity<Any> {
        var priority: Priority = try {
            priorityService.findById(id)
        } catch (ex: NoSuchElementException) {
            ex.printStackTrace()
            return ResponseEntity<Any>("id = $id not found!", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity.ok(priority)
    }

    @PostMapping("/search")
    fun search(@RequestBody prioritySearchValues: SearchValues): ResponseEntity<Any> {
        if (prioritySearchValues.email == null || prioritySearchValues.email.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: email mast be not null!", HttpStatus.NOT_ACCEPTABLE)
        }
        val priorities = priorityService.findByTitle(prioritySearchValues.title, prioritySearchValues.email)
        return ResponseEntity.ok(priorities)
    }
}