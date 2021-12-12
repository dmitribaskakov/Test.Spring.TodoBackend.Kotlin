package org.home.todobackend.service

import org.home.todobackend.entity.Priority
import org.home.todobackend.repo.PriorityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service // все методы класса должны выполниться без ошибки, чтобы транзакция завершилась
// если в методе выполняются несколько SQL запросов и возникнет исключение - то все выполненные операции откатятся (Rollback)
@Transactional
// работает встроенный механизм DI из Spring, который при старте приложения подставит в эту переменную нужные класс-реализацию
class PriorityService(private val repository : PriorityRepository) { // сервис имеет право обращаться к репозиторию (БД)

    fun findById(id: Long): Priority {
        return repository.findById(id).get()
    }

    fun findAll(email: String): List<Priority> {
        return repository.findByUserEmailOrderByTitleAsc(email)
    }

    fun findByTitle(title: String?, email: String): List<Priority> {
        return repository.findByTitle(title, email)
    }

    fun add(priority: Priority): Priority {
        return repository.save(priority) // создает или обновляет объект
    }

    fun update(priority: Priority): Priority {
        return repository.save(priority) // создает или обновляет объект
    }

    fun delete(id: Long) {
        repository.deleteById(id) // удаляет объект
        return
    }
}