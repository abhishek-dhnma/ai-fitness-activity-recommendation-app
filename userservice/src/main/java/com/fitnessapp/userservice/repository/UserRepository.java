package com.fitnessapp.userservice.repository;

import com.fitnessapp.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Boolean existsByEmail(String email);
}
