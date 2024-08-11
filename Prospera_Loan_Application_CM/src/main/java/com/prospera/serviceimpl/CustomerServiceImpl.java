package com.prospera.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prospera.exception.InvalidCustomerException;
import com.prospera.model.Customer;
import com.prospera.model.Sanction;
import com.prospera.repository.CustomerRepository;
import com.prospera.servicei.CustomerServiceI;

@Service
public class CustomerServiceImpl implements CustomerServiceI
{
	@Autowired
	CustomerRepository cr;
	
	@Override
	public List<Customer> getAllPendingSanction()
	{
		List<Customer> l = cr.findAllByEnquiryEnquiryStatusAndEnquiryLoanStatus("Pending Sanction","Verification Approved");
		return l;
	}

	@Override
	public float calculateEMI(Customer c)
	{
		float emiAmount = ((c.getSanction().getLoanamount()*c.getSanction().getInterestRate())/1200)+(c.getSanction().getLoanamount()/c.getSanction().getTenure());
		c.getSanction().setEmiAmount(emiAmount);
		c.getEnquiry().setEnquiryStatus("EMI calculated");
		cr.save(c);
		return emiAmount;
	}

	@Override
	public Customer getById(int cid)
	{
		Optional<Customer> o  = cr.findById(cid);
		if(!(o.isPresent()))
		{
			throw new InvalidCustomerException("Invalid Customer");
		}
		else
		{
			if(o.get().getEnquiry().getEnquiryStatus().equals("Pending Sanction"))
			{
				return o.get();
			}
			else
			{
				throw new InvalidCustomerException("Invalid Customer");
			}
		}
		
	}

	@Override
	public Sanction setLoanDetails(Customer c, Sanction s)
	{
		Sanction sanction = new Sanction();
		sanction.setDate(new Date());
		sanction.setFirstName(c.getFirstName());
		sanction.setLastName(c.getLastName());
		sanction.setLoanamount(s.getLoanamount());
		sanction.setInterestRate(s.getInterestRate());
		sanction.setTenure(s.getTenure());
		sanction.setEmiAmount(0);
		c.getEnquiry().setEnquiryStatus("Sanction Process In Progress");
		c.setSanction(sanction);
		cr.save(c);
		return sanction;
	}

	@Override
	public Customer getCustomerforEMI(int cid)
	{
		Optional<Customer> o  = cr.findById(cid);
		if(!(o.isPresent()))
		{
			throw new InvalidCustomerException("Invalid Customer");
		}
		else
		{
			if(o.get().getEnquiry().getEnquiryStatus().equals("Sanction Process In Progress"))
			{
				return o.get();
			}
			else
			{
				throw new InvalidCustomerException("Saction process has either not started or already completed for the customer ");
			}
		}
	}

	@Override
	public Customer getSanctionRejected(int cid)
	{
		Optional<Customer> o  = cr.findById(cid);
		if(!(o.isPresent()))
		{
			throw new InvalidCustomerException("Invalid Customer");
		}
		else
		{
			if(o.get().getEnquiry().getEnquiryStatus().equals("Sanction rejected by customer"))
			{
				return o.get();
			}
			else
			{
				throw new InvalidCustomerException("Customer sanction is not in rejected status");
			}
		}
		
	}

	@Override
	public void reopenSanctionProcess(Customer c)
	{
		c.getEnquiry().setEnquiryStatus("Pending Sanction");
		cr.save(c);
	}

	@Override
	public Customer getForRejectSanction(int cid)
	{
		Optional<Customer> o  = cr.findById(cid);
		if(!(o.isPresent()))
		{
			throw new InvalidCustomerException("Invalid Customer");
		}
		else
		{
			if(o.get().getEnquiry().getEnquiryStatus().equals("EMI calculated"))
			{
				return o.get();
			}
			else if(o.get().getEnquiry().getEnquiryStatus().equals("Sanction approved by customer"))
			{
				throw new InvalidCustomerException("Sanction has already been approved by the customer");
			}
			else
			{
				throw new InvalidCustomerException("EMI has not been calculated yet");
			}
		}
	}

	@Override
	public void sanction(Customer c, String enquiryStatus)
	{
		if(enquiryStatus.equals("rejected"))
		{
			c.getEnquiry().setEnquiryStatus("Sanction rejected by customer");
		}
		else if(enquiryStatus.equals("approved"))
		{
			c.getEnquiry().setEnquiryStatus("Sanction approved by customer");
			c.getEnquiry().setLoanStatus("Loan Approved");
		}
		cr.save(c);
	}

}
