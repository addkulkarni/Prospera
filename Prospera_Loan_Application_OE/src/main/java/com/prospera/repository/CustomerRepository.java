package com.prospera.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import com.prospera.model.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>
{

//    @Transactional
//    @Query(value = "from Customer c,Enquiry e where e.loanStatus=Pending Verfication and c.enquiryID=e.enquiryID")
//    public List<Customer> findByAll();
//
//    @Query(value = "from Customer c, Enquiry e where e.lo")
//	public List<Customer> findByEnquiryStatus();


}
