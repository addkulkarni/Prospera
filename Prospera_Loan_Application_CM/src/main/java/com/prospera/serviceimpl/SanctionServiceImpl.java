package com.prospera.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prospera.repository.SanctionRepository;
import com.prospera.servicei.SanctionServiceI;

@Service
public class SanctionServiceImpl implements SanctionServiceI
{
	@Autowired
	SanctionRepository sr;

	
}
