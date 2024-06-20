package com.eric.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eric.demo.domain.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    
}
