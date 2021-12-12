package org.home.todobackend.service

import org.home.todobackend.entity.Category
import org.home.todobackend.repo.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service // все методы класса должны выполниться без ошибки, чтобы транзакция завершилась
// если в методе выполняются несколько SQL запросов и возникнет исключение - то все выполненные операции откатятся (Rollback)
@Transactional
// работает встроенный механизм DI из Spring, который при старте приложения подставит в эту переменную нужные класс-реализацию
// сервис имеет право обращаться к репозиторию (БД)
class CategoryService(private val repository : CategoryRepository) {

    fun findById(id: Long): Category {
        return repository.findById(id).get()
    }

    fun findAll(email: String): List<Category> {
        return repository.findByUserEmailOrderByTitleAsc(email)
    }

    fun findByTitle(title: String?, email: String): List<Category> {
        return repository.findByTitle(title, email)
    }

    fun add(category: Category): Category {
        return repository.save(category) // создает или обновляет объект
    }

    fun update(category: Category): Category {
        return repository.save(category) // создает или обновляет объект
    }

    fun delete(id: Long) {
        repository.deleteById(id) // удаляет объект
        return
    }
}