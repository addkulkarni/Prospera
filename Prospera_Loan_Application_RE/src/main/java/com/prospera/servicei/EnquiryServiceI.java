package com.prospera.servicei;

import com.prospera.model.Enquiry;

public interface EnquiryServiceI
{
	Enquiry getEnquiry(int eid);

	void setEnquiryStatus(int eid);

	
}
