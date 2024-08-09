package com.prospera.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.prospera.model.Customer;
import com.prospera.model.Enquiry;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Integer>
{

	List<Enquiry> findByEnquiryStatus(String string);



	
	

	
}
