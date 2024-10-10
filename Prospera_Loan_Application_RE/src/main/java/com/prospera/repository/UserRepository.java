package com.prospera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prospera.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{

}
