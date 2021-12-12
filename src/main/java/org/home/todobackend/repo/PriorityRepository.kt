package org.home.todobackend.repo;

import org.home.todobackend.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Можно сразу использовать все методы CRUD (Create, Read, Update, Delete)
// принцип ООП: абстракция-реализация - здесь описываем все доступные способы доступа к данным
@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {
    // поиск приоритетов пользователя (по email)
    List<Priority> findByUserEmailOrderByTitleAsc(String email);

    @Query("SELECT p From Priority as p where "+
            "(:title is null or :title = '' or lower(p.title) like lower(concat('%', :title, '%')) ) "+
            "and p.user.email = :email "+
            "order by p.title asc")
    List<Priority> findByTitle(@Param("title") String title, @Param("email") String email);
}
