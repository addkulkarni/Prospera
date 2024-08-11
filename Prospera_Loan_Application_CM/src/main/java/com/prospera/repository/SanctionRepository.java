package com.prospera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prospera.model.Sanction;

@Repository
public interface SanctionRepository extends JpaRepository<Sanction, Integer>
{

}
