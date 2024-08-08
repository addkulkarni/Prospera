package com.prospera.servicei;

import java.util.Optional;

import com.prospera.model.Enquiry;

public interface EnquiryServiceI
{
	Optional<Enquiry> getEnquiry(int eid);

	void setEnquiryStatus(int eid);

	
}
