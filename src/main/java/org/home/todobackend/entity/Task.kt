package org.home.todobackend.entity

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import org.hibernate.annotations.CacheConcurrencyStrategy
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Getter
import lombok.Setter
import org.hibernate.annotations.Cache
import org.hibernate.annotations.Type
import java.util.*
import javax.persistence.*

/*
задачи пользователя
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Table(name = "task", schema = "todolist")
class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val title: String? = null

    @Type(type = "org.hibernate.type.NumericBooleanType") // для автоматической конвертации числа в true/false
    val completed : Boolean? = null // 1 = true, 0 = false

    @Column(name = "task_date")
    val taskDate: Date? = null

    // задача может иметь только один приоритет (с обратной стороны - один и тот же приоритет может быть использоваться в множестве задач)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "priority_id", referencedColumnName = "id") // по каким полям связывать (foreign key)
    val priority: Priority? = null

    // задача может иметь только одну категорию (с обратной стороны - одна и та же категория может быть использоваться в множестве задач)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id") // по каким полям связывать (foreign key)
    val category: Category? = null

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id") // по каким полям связывать (foreign key)
    val user : User? = null // для какого пользователя задача

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val task = o as Task
        return id == task.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun toString(): String {
        return title!!
    }
}