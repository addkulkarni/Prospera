package com.prospera.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prospera.model.Enquiry;
@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Integer>
{
	Enquiry findByEnquiryIDAndEnquiryStatus(int enquiryID, String string);

//	List<Enquiry> findAllByLoanStatus(String string);

	List<Enquiry> findAllByEnquiryStatus(String string);

	List<Enquiry> findByEnquiryStatusIn(List<String> asList);		
}



