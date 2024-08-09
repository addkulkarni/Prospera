package com.prospera.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prospera.model.Enquiry;

@Repository
public interface EnquiryRepository extends JpaRepository<Enquiry, Integer>
{
	List<Enquiry> findAllByLoanStatus(String loanStatus);

	List<Enquiry> findAllByEnquiryStatus(String enquiryStatus);

	Optional<Enquiry> findByPancardNo(String pancardNo);

	Optional<Enquiry> findByEnquiryIDAndEnquiryStatus(int enquiryID, String string);

	List<Enquiry> findAllByEmail(String email);

	void deleteByEmail(String email);
}
