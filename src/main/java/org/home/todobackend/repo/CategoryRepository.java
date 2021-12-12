package org.home.todobackend.repo;

import org.home.todobackend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Можно сразу использовать все методы CRUD (Create, Read, Update, Delete)
// принцип ООП: абстракция-реализация - здесь описываем все доступные способы доступа к данным
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // поиск категорий пользователя (по email)
    List<Category> findByUserEmailOrderByTitleAsc(String email);

    @Query("SELECT c From Category as c where "+
        "(:title is null or :title = '' or lower(c.title) like lower(concat('%', :title, '%')) ) "+
        "and c.user.email = :email "+
        "order by c.title asc")
    List<Category> findByTitle(@Param("title") String title, @Param("email") String email);
}
