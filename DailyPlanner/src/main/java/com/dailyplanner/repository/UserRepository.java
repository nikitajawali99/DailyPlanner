package com.dailyplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dailyplanner.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	User findByName(String name);

	Boolean existsByEmail(String email);

	@Query("select p from User p where p.email=:username")
	User findByUsername(@Param("username") String username);

	@Query("select p from User p where p.id=:id")
	User findByUserId(@Param("id") Long id);

}

