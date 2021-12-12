package org.home.todobackend.repo

import org.home.todobackend.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

// Можно сразу использовать все методы CRUD (Create, Read, Update, Delete)
// принцип ООП: абстракция-реализация - здесь описываем все доступные способы доступа к данным
@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    // поиск категорий пользователя (по email)
    fun findByUserEmailOrderByTitleAsc(email: String): List<Category>

    @Query("SELECT c From Category as c where " +
            "(:title is null or :title = '' or lower(c.title) like lower(concat('%', :title, '%')) ) " +
            "and c.user.email = :email " +
            "order by c.title asc")
    fun findByTitle(@Param("title") title: String?, @Param("email") email: String): List<Category>
}