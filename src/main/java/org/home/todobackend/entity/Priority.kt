package org.home.todobackend.entity

import org.hibernate.annotations.CacheConcurrencyStrategy
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Cache
import java.util.Objects
import javax.persistence.*

/*
справочноное значение - приоритет пользователя
может использовать для своих задач
*/
@Entity
@Table(name = "priority", schema = "todolist")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class Priority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val title: String? = null
    val color: String? = null

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id") // по каким полям связывать (foreign key)
    val user: User? = null

    override fun toString(): String {
        return title!!
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val priority = o as Priority
        return id == priority.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}