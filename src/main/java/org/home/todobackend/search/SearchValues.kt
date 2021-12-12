package org.home.todobackend.search

import java.util.*

//Возможные значения по которым будем искать категорию
data class CategorySearchValues (val email: String, val title: String?)

//Возможные значения по которым будем искать приоритет
data class SearchValues (val email: String, val title: String?)

// возможные значения, по которым можно искать задачи + значения сортировки
data class TaskSearchValues (val email: String,
                             val pageNumber: Int,
                             val pageSize: Int,
                             val sortColumn: String,
                             val sortDirection: String) {
    val title: String? = null
    val completed: Int? = null
    val priorityId: Long? = null
    val categoryId: Long? = null
    val dateFrom : Date? = null // для задания периода по датам
    val dateTo: Date? = null
}
