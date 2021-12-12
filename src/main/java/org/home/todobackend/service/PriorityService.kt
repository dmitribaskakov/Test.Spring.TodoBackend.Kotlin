package org.home.todobackend.service;
import org.home.todobackend.entity.Priority;
import org.home.todobackend.repo.PriorityRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
// все методы класса должны выполниться без ошибки, чтобы транзакция завершилась
// если в методе выполняются несколько SQL запросов и возникнет исключение - то все выполненные операции откатятся (Rollback)
@Transactional
public class PriorityService {
    // работает встроенный механизм DI из Spring, который при старте приложения подставит в эту переменную нужные класс-реализацию
    private final PriorityRepository repository; // сервис имеет право обращаться к репозиторию (БД)

    public PriorityService(PriorityRepository repository) {
        this.repository = repository;
    }

    public Priority findById(Long id) {
        return repository.findById(id).get();
    }

    public List<Priority> findAll(String email) {
        return repository.findByUserEmailOrderByTitleAsc(email);
    }
    public List<Priority> findByTitle(String title, String email) {
        return repository.findByTitle(title, email);
    }
    public Priority add(Priority priority) {
        return repository.save(priority); // создает или обновляет объект
    }
    public Priority update(Priority priority) {
        return repository.save(priority); // создает или обновляет объект
    }
    public void delete(Long id) {
        repository.deleteById(id); // удаляет объект
        return;
    }
}
