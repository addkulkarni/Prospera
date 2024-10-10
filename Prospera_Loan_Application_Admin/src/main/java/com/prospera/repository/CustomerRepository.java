package com.prospera.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.prospera.model.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>
{

	List<Customer> findAllByEnquiryLoanStatus(String loanStatus);

	List<Customer> findAllByEnquiryEmail(String email);

	List<Customer> findAllByEnquiryEnquiryStatus(String enquiryStatus);

//	List<Customer> findAllByEnquiryCibilCibilscore();
	
}

