package org.home.todobackend.service;

import org.home.todobackend.entity.Stat;
import org.home.todobackend.repo.StatRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class StatService {
    private final StatRepository repository;
    public StatService(StatRepository repository) {
        this.repository = repository;
    }
    public Stat findStat(String email) {
        return repository.findByUserEmail(email);
    }
}
