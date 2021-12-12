package org.home.todobackend.repo

import org.springframework.data.repository.CrudRepository
import org.home.todobackend.entity.Stat
import org.springframework.stereotype.Repository

@Repository
interface StatRepository : CrudRepository<Stat, Long> {
    fun findByUserEmail(email: String): Stat
}