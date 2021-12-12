package org.home.todobackend.controller;

import org.home.todobackend.entity.Category;
import org.home.todobackend.search.CategorySearchValues;
import org.home.todobackend.service.CategoryService;
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
@RequestMapping("/category") // базовый URI
public class CategoryController {

    // доступ к данным из БД
    private CategoryService categoryService;

    // автоматическое внедрение экземпляра класса через конструктор
    // не используем @Autowired ля переменной класса, т.к. "Field injection is not recommended "
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

//    @GetMapping("/id")
//    public Category findById() {
//        return categoryService.findById(120127L);
//    }

    @PostMapping("/all")
    public List<Category> findAll(@RequestBody String email) {
        return categoryService.findAll(email);
    }

    @PostMapping("/add")
    public ResponseEntity<Category> add(@RequestBody Category category) {
        //проверка на обязательные параметры
        if (category.getId() != null && category.getId() != 0) { //Уже не новый объект
            return new ResponseEntity("redundant param: id mast be null!", HttpStatus.NOT_ACCEPTABLE);
        }
        //если передали пустые значимые поля
        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title mast be not null!", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(categoryService.add(category));
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody Category category) {
        //проверка на обязательные параметры
        if (category.getId() == null || category.getId() == 0) { //Еще новый объект
            return new ResponseEntity("missed param: id mast be not null!", HttpStatus.NOT_ACCEPTABLE);
        }
        //если передали пустые значимые поля
        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title mast be not null!", HttpStatus.NOT_ACCEPTABLE);
        }
        categoryService.update(category);
        return new ResponseEntity(HttpStatus.OK);
    }

//    @DeleteMapping("/delete/{id}/{email}")
//    public ResponseEntity delete(@PathVariable("id") Long id, @PathVariable("email") String email) {
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        try {
            categoryService.delete(id);
        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
            return new ResponseEntity("id = "+id+" not found!", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/id")
    public ResponseEntity<Category> getByID(@RequestBody Long id) {
        Category category = null;
        try {
            category = categoryService.findById(id);
        } catch (NoSuchElementException ex) {
            ex.printStackTrace();
            return new ResponseEntity("id = "+id+" not found!", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(category);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Category>> search(@RequestBody CategorySearchValues categorySearchValues) {
        if (categorySearchValues.getEmail() == null || categorySearchValues.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param: email mast be not null!", HttpStatus.NOT_ACCEPTABLE);
        }
        List<Category> categories = categoryService.findByTitle(categorySearchValues.getTitle(), categorySearchValues.getEmail());
        return ResponseEntity.ok(categories);
    }

}