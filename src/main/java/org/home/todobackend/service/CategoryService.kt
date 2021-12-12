package org.home.todobackend.service;

import org.springframework.stereotype.Service;
import org.home.todobackend.entity.Category;
import org.home.todobackend.repo.CategoryRepository;

import javax.transaction.Transactional;
import java.util.List;


@Service
// все методы класса должны выполниться без ошибки, чтобы транзакция завершилась
// если в методе выполняются несколько SQL запросов и возникнет исключение - то все выполненные операции откатятся (Rollback)
@Transactional
public class CategoryService {

    // работает встроенный механизм DI из Spring, который при старте приложения подставит в эту переменную нужные класс-реализацию
    private final CategoryRepository repository; // сервис имеет право обращаться к репозиторию (БД)

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category findById(Long id) {
        return repository.findById(id).get();
    }

    public List<Category> findAll(String email) {
        return repository.findByUserEmailOrderByTitleAsc(email);
    }
    public List<Category> findByTitle(String title, String email) {
        return repository.findByTitle(title, email);
    }
    public Category add(Category category) {
        return repository.save(category); // создает или обновляет объект
    }
    public Category update(Category category) {
        return repository.save(category); // создает или обновляет объект
    }
    public void delete(Long id) {
        repository.deleteById(id); // удаляет объект
        return;
    }
}
