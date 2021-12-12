package org.home.todobackend.service

import org.home.todobackend.entity.Task
import org.home.todobackend.repo.TaskRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class TaskService(private val repository: TaskRepository) {

    fun findAll(email: String): List<Task> {
        return repository.findByUserEmailOrderByTitleAsc(email)
    }

    fun add(task: Task): Task {
        return repository.save(task) // метод save обновляет или создает новый объект, если его не было
    }

    fun update(task: Task): Task {
        return repository.save(task) // метод save обновляет или создает новый объект, если его не было
    }

    fun deleteById(id: Long) {
        repository.deleteById(id)
    }

    fun findByParams(text: String?, completed: Boolean?, priorityId: Long?, categoryId: Long?, email: String, dateFrom: Date?, dateTo: Date?, paging: PageRequest): Page<Task> {
        return repository.findByParams(text, completed, priorityId, categoryId, email, dateFrom, dateTo, paging)
    }

    fun findById(id: Long): Task {
        return repository.findById(id).get() // т.к. возвращается Optional - можно получить объект методом get()
    }
}