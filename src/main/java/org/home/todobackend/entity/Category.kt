package org.home.todobackend.entity

import org.hibernate.annotations.CacheConcurrencyStrategy
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Cache
import java.util.Objects
import javax.persistence.*

/*
справочное значение - категория пользователя
может использовать для своих задач
содержит статистику по каждой категории
*/
@Entity
@Table(name = "category", schema = "todolist")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val title: String? = null

    @Column(name = "completed_count", updatable = false)
    val completedCount: Long? = null

    @Column(name = "uncompleted_count", updatable = false)
    val uncompletedCount: Long? = null

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id") // по каким полям связаны эти 2 объекта (foreign key)
    val user: User? = null

    override fun toString(): String {
        return title!!
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val category = o as Category
        return id == category.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}