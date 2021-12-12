package org.home.todobackend.controller;
import org.home.todobackend.entity.Priority;
import org.home.todobackend.search.PrioritySearchValues;
import org.home.todobackend.service.PriorityService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/*
Используем @RestController вместо обычного @Controller, чтобы все ответы сразу оборачивались в JSON,
иначе пришлось бы добавлять лишние объекты в код, использовать @ResponseBody для ответа, указывать тип отправки JSON
Названия методов могут быть любыми, главное не дублировать их имена внутри класса и URL mapping
*/

@RestController
@RequestMapping("/priority") // базовый URI
public class PriorityController {
    // доступ к данным из БД
    private PriorityService priorityService;

    // автоматическое внедрение экземпляра класса через конструктор
    // не используем @Autowired ля переменной класса, т.к. "Field injection is not recommended "
    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

//    @GetMapping("/id")
//    public Priority findById() {
//        return PriorityService.findById(120127L);
//    }

    @PostMapping("/all")
    public List<Priority> findAll(@RequestBody String email) {
        return priorityService.findAll(email);
    }

    @PostMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody Priority priority) {
        //проверка на обязательные параметры
        if (priority.getId() != null && priority.getId() != 0) { //Уже не новый объект
            return new ResponseEntity("redundant param: id mast be null!", HttpStatus.NOT_ACCEPTABLE);
        }
        //если передали пустые значимые поля
        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title mast be not null!", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(priorityService.add(priority));
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Priority priority) {
        //проверка на обязательные параметры
        if (priority.getId() == null || priority.getId() == 0) { //Еще новый объект
            return new ResponseEntity("missed param: id mast be not null!", HttpStatus.NOT_ACCEPTABLE);
        }
        //если передали пустые значимые поля
        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title mast be not null!", HttpStatus.NOT_ACCEPTABLE);
        }
        priorityService.update(priority);
        return new ResponseEntity(HttpStatus.OK);
    }

    //    @DeleteMapping("/delete/{id}/{email}")
//    public ResponseEntity delete(@PathVariable("id") Long id, @PathVariable("email") String email) {
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            priorityService.delete(id);
        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
            return new ResponseEntity("id = "+id+" not found!", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/id")
    public ResponseEntity<Priority> getByID(@RequestBody Long id) {
        Priority priority = null;
        try {
            priority = priorityService.findById(id);
        } catch (NoSuchElementException ex) {
            ex.printStackTrace();
            return new ResponseEntity("id = "+id+" not found!", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(priority);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Priority>> search(@RequestBody PrioritySearchValues prioritySearchValues) {
        if (prioritySearchValues.getEmail() == null || prioritySearchValues.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param: email mast be not null!", HttpStatus.NOT_ACCEPTABLE);
        }
        List<Priority> priorities = priorityService.findByTitle(prioritySearchValues.getTitle(), prioritySearchValues.getEmail());
        return ResponseEntity.ok(priorities);
    }
    
}
