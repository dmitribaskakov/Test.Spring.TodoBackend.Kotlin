package org.home.todobackend.entity


import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.Type
import java.util.Objects
import javax.persistence.*

/*
Вся активность пользователя (активация аккаунта, другие действия по необходимости)
*/
@Entity
@Table(name = "activity", schema = "todolist")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(name = "activated", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    val activated = false // 1 = true, 0 = false

    @Column(updatable = false)
    val uuid: String? = null

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    val user: User? = null

    override fun toString(): String {
        return uuid!!
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val activity = o as Activity
        return id == activity.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }
}