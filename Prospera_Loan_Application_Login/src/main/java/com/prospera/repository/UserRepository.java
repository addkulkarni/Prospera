package com.prospera.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prospera.model.Customer;
import com.prospera.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{

	Optional<User> findByUsername(String username);

}
