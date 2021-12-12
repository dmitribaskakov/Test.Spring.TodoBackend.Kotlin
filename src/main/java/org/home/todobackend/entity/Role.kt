package org.home.todobackend.entity

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import org.hibernate.annotations.CacheConcurrencyStrategy
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Getter
import lombok.Setter
import org.hibernate.annotations.Cache
import java.util.Objects
import javax.persistence.*

/*
Все доступные роли, которые будут привязаны к пользователю
*/
@Entity
@Table(name = "role_data", schema = "todolist")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val name: String? = null

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val users: Set<User>? = null

    override fun toString(): String {
        return name!!
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val role = o as Role
        return id == role.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}