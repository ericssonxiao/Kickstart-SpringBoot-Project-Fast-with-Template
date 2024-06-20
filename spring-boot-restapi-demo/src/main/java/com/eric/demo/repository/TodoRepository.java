package com.eric.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eric.demo.domain.Todos;

@Repository
public interface TodoRepository extends JpaRepository<Todos, Integer>{
    
}
