package org.home.todobackend.service

import org.home.todobackend.repo.StatRepository
import org.home.todobackend.entity.Stat
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class StatService(private val repository: StatRepository) {
    fun findStat(email: String): Stat {
        return repository.findByUserEmail(email)
    }
}