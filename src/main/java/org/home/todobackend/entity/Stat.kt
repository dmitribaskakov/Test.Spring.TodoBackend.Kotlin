package org.home.todobackend.entity

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import org.hibernate.annotations.CacheConcurrencyStrategy
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Getter
import lombok.Setter
import org.hibernate.annotations.Cache
import org.home.todobackend.entity.Stat
import java.util.Objects
import javax.persistence.*

/*
общая статистика по задачам (независимо от категорий задач)
*/
@Entity
@Table(name = "stat", schema = "todolist")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null

    @Column(name = "completed_total", updatable = false)
    val completedTotal: Long? = null

    @Column(name = "uncompleted_total", updatable = false)
    val uncompletedTotal: Long? = null

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    val user: User? = null

    override fun toString(): String {
        return "Stat{" +
                "id=" + id!! +
                ", completedTotal=" + completedTotal!! +
                ", uncompletedTotal=" + uncompletedTotal!! +
                '}'
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val stat = o as Stat
        return id == stat.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}