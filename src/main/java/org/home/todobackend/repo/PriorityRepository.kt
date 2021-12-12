package org.home.todobackend.repo

import org.home.todobackend.entity.Priority
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

// Можно сразу использовать все методы CRUD (Create, Read, Update, Delete)
// принцип ООП: абстракция-реализация - здесь описываем все доступные способы доступа к данным
@Repository
interface PriorityRepository : JpaRepository<Priority, Long> {
    // поиск приоритетов пользователя (по email)
    fun findByUserEmailOrderByTitleAsc(email: String): List<Priority>

    @Query(
        "SELECT p From Priority as p where " +
                "(:title is null or :title = '' or lower(p.title) like lower(concat('%', :title, '%')) ) " +
                "and p.user.email = :email " +
                "order by p.title asc"
    )
    fun findByTitle(@Param("title") title: String?, @Param("email") email: String): List<Priority>
}