package com.prospera.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prospera.model.Enquiry;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Integer>{

	List<Enquiry> findAllByLoanStatus(String loanStatus);

	List<Enquiry> findAllByEnquiryStatus(String enquiryStatus);

	
}
