package com.prospera.serviceimpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.prospera.exception.InvalidCustomerException;
import com.prospera.model.Customer;
import com.prospera.model.Ledger;
import com.prospera.model.Sanction;
import com.prospera.repository.CustomerRepository;
import com.prospera.servicei.CustomerServiceI;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class CustomerServiceImpl implements CustomerServiceI
{
	@Autowired
	CustomerRepository cr;

	@Autowired
	JavaMailSender sender;
	
	@Override
	public List<Customer> getAllForwaredtoAH()
	{
		List<Customer>c = cr.findAllByEnquiryEnquiryStatus("Forwarded to Account Head");
		return c;
	}

	@Override
	public float disburseLoanAmount(int cid) throws MessagingException
	{
		Optional<Customer> o = cr.findById(cid);
		if(!(o.isPresent()))
		{
			throw new InvalidCustomerException("Invalid customer");
		}
		else
		{
			Customer c = o.get();
			if(c.getEnquiry().getEnquiryStatus().equals("Forwarded to Account Head"))
			{
				c.getEnquiry().setEnquiryStatus("Loan Disbursed");
				c.getEnquiry().setLoanStatus("Loan Disbursed");
				cr.save(c);
				
				MimeMessage mm = sender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mm,true);
				helper.setTo(c.getEmail());
				helper.setSubject("Loan Disbursement Notification");
				helper.setText("Hello "+c.getFirstName()+",\nYour loan amount of rs. "+c.getSanction().getLoanamount()+"has been disbursed to the bank account.\n"
						+ "Attached to this email is the reciept for the transaction.\nPlease find the attached document\nTeam Prospera Finance");
				helper.addAttachment("Reciept.pdf", new ByteArrayResource(c.getDoc().getAdhar()));
				sender.send(mm);
				return c.getSanction().getLoanamount();
			}
			else
			{
				throw new InvalidCustomerException("Invalid customer");
			}
		}
	}

	@Override
	public void createLedger(int cid)
	{
		Optional<Customer> o = cr.findById(cid);
		if(!(o.isPresent()))
		{
			throw new InvalidCustomerException("Customer do not exist");
		}
		else
		{	Customer c = o.get();
			if(c.getEnquiry().getEnquiryStatus().equals("Loan Disbursed"))
			{
				Calendar current_date_cal = Calendar.getInstance();
				Date current_date = new Date();
				current_date_cal.setTime(current_date);
				
				Calendar start_date_calendar = Calendar.getInstance();
				start_date_calendar.set(2024, Calendar.AUGUST, 15);
				Date emi_start_date = start_date_calendar.getTime();
				
				Calendar end_date_calendar = Calendar.getInstance();
				end_date_calendar.set(2024, Calendar.AUGUST, 20);
				Date emi_end_date = end_date_calendar.getTime();
				
				current_date_cal.add(current_date_cal.MONTH, 12);
				Date loan_end_date = current_date_cal.getTime();
			
				List<Ledger> ledgerList = new ArrayList<>();
				for(int i=1; i<=12; i++)
				{
					Sanction sanction = c.getSanction();
					Ledger l = new Ledger();
					l.setLedgerCreatedDate(current_date);
					l.setTotalPrincipalAmount(sanction.getLoanamount());
					l.setPayableAmount(sanction.getLoanamount()+(sanction.getLoanamount()*sanction.getInterestRate()/100));
					l.setTenure(sanction.getTenure());
					l.setMonthlyEmi(sanction.getEmiAmount());
					l.setAmountPaidTillDate(0);
					l.setRemainingAmount(sanction.getLoanamount()+(sanction.getLoanamount()*sanction.getInterestRate()/100));
					l.setNextEmiStartDate(emi_start_date);
					start_date_calendar.add(Calendar.MONTH, 1);
					emi_start_date = start_date_calendar.getTime();
					l.setNextEmiEndDate(emi_end_date);
					end_date_calendar.add(Calendar.MONTH, 1);
					emi_end_date = end_date_calendar.getTime();
					l.setPreviousEmiStatus("Unpaid");
					l.setCurrentMonthEmiStatus("Unpaid");
					l.setLoanEndDate(loan_end_date);
					l.setLoanStatus("Ongoing");
					ledgerList.add(l);
					c.setLedger(ledgerList);
					cr.save(c);
				}
			}
			else
			{
				throw new InvalidCustomerException("Customer loan not yet disbursed");
			}
		}
	}

	@Override
	public String updateLedgerList(int cid, Ledger l)
	{
		Optional<Customer> o = cr.findById(cid);
		if(!(o.isPresent()))
		{
			throw new InvalidCustomerException("Customer not found");
		}
		else
		{
			Customer c = o.get();
			if(l.getLoanStatus().equals("Defaulter"))
			{
				return "You have failed to pay consecutive three EMIs and hence you've been marked as defaulter";
			}
			else
			{
				List<Ledger> ledgerList =c.getLedger();
				l.setCurrentMonthEmiStatus("Paid");
				float remainingAmount = l.getRemainingAmount()-l.getMonthlyEmi();
				float amountPaidTillDate = l.getAmountPaidTillDate()+l.getMonthlyEmi();
				for(Ledger le:ledgerList)
				{
					le.setAmountPaidTillDate(amountPaidTillDate);
					le.setRemainingAmount(remainingAmount);
				}
				c.setLedger(ledgerList);
				cr.save(c);
				return "EMI for the current month has been paid.";
			}
		}
	}

	@Override
	public List<Ledger> getLedgerList(int cid)
	{
		Customer c = cr.findById(cid).get();
		List<Ledger> ledgerlist = c.getLedger();
		return ledgerlist;
	}

	@Override
	public void updateLedgerList(int cid, List<Ledger> updatedLedgerList)
	{
		Customer c = cr.findById(cid).get();
		c.setLedger(updatedLedgerList);
		List<Ledger> ledgerList = c.getLedger();
		boolean flag=false;
		for(Ledger l: ledgerList)
		{
			if(l.getLoanStatus().equals("Defaulter"))
			{
				flag=true;
			}
		}
		if(flag)
		{
			c.getEnquiry().setLoanStatus("Defaulter");
			c.getEnquiry().setEnquiryStatus("Defaulter");
		}
		cr.save(c);
	}
}
