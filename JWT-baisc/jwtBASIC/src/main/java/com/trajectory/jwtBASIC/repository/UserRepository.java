package com.trajectory.jwtBASIC.repository;

import com.trajectory.jwtBASIC.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

}
