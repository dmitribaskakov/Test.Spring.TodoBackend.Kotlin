package org.home.todobackend.controller

import org.home.todobackend.search.TaskSearchValues
import org.home.todobackend.entity.Task
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.home.todobackend.service.TaskService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.dao.EmptyResultDataAccessException
import kotlin.Throws
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.text.ParseException
import java.util.*

@RestController
@RequestMapping("/task") // базовый URI
// используем автоматическое внедрение экземпляра класса через конструктор
// не используем @Autowired ля переменной класса, т.к. "Field injection is not recommended "
class TaskController(private val taskService: TaskService) {

    // получение всех данных
    @PostMapping("/all")
    fun findAll(@RequestBody email: String): ResponseEntity<List<Task>> {
        return ResponseEntity.ok(taskService.findAll(email)) // поиск всех задач конкретного пользователя
    }

    // добавление
    @PostMapping("/add")
    fun add(@RequestBody task: Task): ResponseEntity<Any> {
        // проверка на обязательные параметры
        if (task.id != null && task.id != 0L) {
            // id создается автоматически в БД (autoincrement), поэтому его передавать не нужно, иначе может быть конфликт уникальности значения
            return ResponseEntity<Any>("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE)
        }
        // если передали пустое значение title
        if (task.title == null || task.title.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: title", HttpStatus.NOT_ACCEPTABLE)
        }
        // возвращаем созданный объект со сгенерированным id
        return ResponseEntity.ok(taskService.add(task))
    }

    // обновление
    @PutMapping("/update")
    fun update(@RequestBody task: Task): ResponseEntity<Any> {
        // проверка на обязательные параметры
        if (task.id == null || task.id == 0L) {
            return ResponseEntity<Any>("missed param: id", HttpStatus.NOT_ACCEPTABLE)
        }
        // если передали пустое значение title
        if (task.title == null || task.title.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: title", HttpStatus.NOT_ACCEPTABLE)
        }
        // save работает как на добавление, так и на обновление
        taskService.update(task)
        return ResponseEntity<Any>(HttpStatus.OK) // просто отправляем статус 200 (операция прошла успешно)
    }

    // для удаления используем типа запроса put, а не delete, т.к. он позволяет передавать значение в body, а не в адресной строке
    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<Any> {
        // можно обойтись и без try-catch, тогда будет возвращаться полная ошибка (stacktrace)
        // здесь показан пример, как можно обрабатывать исключение и отправлять свой текст/статус
        try {
            taskService.deleteById(id)
        } catch (e: EmptyResultDataAccessException) {
            e.printStackTrace()
            return ResponseEntity<Any>("id=$id not found", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity<Any>(HttpStatus.OK) // просто отправляем статус 200 (операция прошла успешно)
    }

    // получение объекта по id
    @PostMapping("/id")
    fun findById(@RequestBody id: Long): ResponseEntity<Any> {
        // можно обойтись и без try-catch, тогда будет возвращаться полная ошибка (stacktrace)
        // здесь показан пример, как можно обрабатывать исключение и отправлять свой текст/статус
        var task: Task? = try {
            taskService.findById(id)
        } catch (e: NoSuchElementException) { // если объект не будет найден
            e.printStackTrace()
            return ResponseEntity<Any>("id=$id not found", HttpStatus.NOT_ACCEPTABLE)
        }
        return ResponseEntity.ok(task)
    }

    // поиск по любым параметрам TaskSearchValues
    @PostMapping("/search")
    @Throws(ParseException::class)
    fun search(@RequestBody taskSearchValues: TaskSearchValues): ResponseEntity<Any> {
        // исключить NullPointerException
        val title = taskSearchValues.title
        // конвертируем Boolean в Integer
        val completed = if (taskSearchValues.completed != null && taskSearchValues.completed == 1) true else false
        val priorityId = taskSearchValues.priorityId
        val categoryId = taskSearchValues.categoryId
        val sortColumn = taskSearchValues.sortColumn
        val sortDirection = taskSearchValues.sortDirection
        val pageNumber = taskSearchValues.pageNumber
        val pageSize = taskSearchValues.pageSize
        val email =
            if (taskSearchValues.email != null) taskSearchValues.email else null // для показа задач только этого пользователя

        // проверка на обязательные параметры
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity<Any>("missed param: email", HttpStatus.NOT_ACCEPTABLE)
        }
        // чтобы захватить в выборке все задачи по датам, независимо от времени - можно выставить время с 00:00 до 23:59
        var dateFrom: Date? = null
        var dateTo: Date? = null
        // выставить 00:01 для начальной даты (если она указана)
        if (taskSearchValues.dateFrom != null) {
            val calendarFrom = Calendar.getInstance()
            calendarFrom.time = taskSearchValues.dateFrom
            calendarFrom[Calendar.HOUR_OF_DAY] = 0
            calendarFrom[Calendar.MINUTE] = 1
            calendarFrom[Calendar.SECOND] = 1
            calendarFrom[Calendar.MILLISECOND] = 1
            dateFrom = calendarFrom.time // записываем начальную дату с 00:01
        }
        // выставить 23:59 для конечной даты (если она указана)
        if (taskSearchValues.dateTo != null) {
            val calendarTo = Calendar.getInstance()
            calendarTo.time = taskSearchValues.dateTo
            calendarTo[Calendar.HOUR_OF_DAY] = 23
            calendarTo[Calendar.MINUTE] = 59
            calendarTo[Calendar.SECOND] = 59
            calendarTo[Calendar.MILLISECOND] = 999
            dateTo = calendarTo.time // записываем конечную дату с 23:59
        }
        // направление сортировки
        val direction =
            if (sortDirection == null || sortDirection.trim().isEmpty() || sortDirection.trim { it <= ' ' } == "asc") Sort.Direction.ASC else Sort.Direction.DESC
        /* Вторым полем для сортировки добавляем id, чтобы всегда сохранялся строгий порядок.
            Например, если у 2-х задач одинаковое значение приоритета и мы сортируем по этому полю.
            Порядок следования этих 2-х записей после выполнения запроса может каждый раз меняться, т.к. не указано второе поле сортировки.
            Поэтому и используем ID - тогда все записи с одинаковым значением приоритета будут следовать в одном порядке по ID.
         */
        // объект сортировки, который содержит стобец и направление
        val sort = Sort.by(direction, sortColumn, ID_COLUMN)
        // объект постраничности
        val pageRequest = PageRequest.of(pageNumber, pageSize, sort)
        // результат запроса с постраничным выводом
        val result =
            taskService.findByParams(title, completed, priorityId, categoryId, email, dateFrom, dateTo, pageRequest)
        // результат запроса
        return ResponseEntity.ok(result)
    }

    companion object {
        const val ID_COLUMN = "id" // имя столбца id
    }
}