package com.prospera.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prospera.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>
{
	Customer findByUsernameAndPassword(String username, String password);

	Optional<Customer> findByUsername(String username);

}
