package com.prospera.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prospera.exception.InvalidLedgerException;
import com.prospera.model.Ledger;
import com.prospera.repository.LedgerRepository;
import com.prospera.servicei.LedgerServiceI;

@Service
public class LedgerServiceImpl implements LedgerServiceI
{
	@Autowired
	LedgerRepository lr;

	@Override
	public Ledger getLedger(int ledgerId)
	{
		Optional<Ledger> o = lr.findById(ledgerId);
		if(!(o.isPresent()))
		{
			throw new InvalidLedgerException("Ledger ID Invalid");
		}
		else
		{
			Ledger le = o.get();
			return le;
		}
	}

	@Override
	public List<Ledger> skipEMI(int ledgerId, List<Ledger> ledger)
	{
		Optional<Ledger> o = lr.findById(ledgerId);
		if(!(o.isPresent()))
		{
			throw new InvalidLedgerException("Invalid Ledger");
		}
		else
		{
			Ledger l = o.get();
			l.setCurrentMonthEmiStatus("Skipped");
			int count = 0;
			for(Ledger led:ledger)
			{
				if(led.getCurrentMonthEmiStatus().equals("Skipped"))
				{
					count++;
					System.out.println(led.getLedgerId()+" "+count);
				}
				else if(led.getCurrentMonthEmiStatus().equals("Paid"))
				{
					count=0;
				}
			}
			System.out.println(count);
			if(count>=3)
			{
				for(Ledger led:ledger)
				{
					led.setLoanStatus("Defaulter");
				}
				return ledger;
			}
			else
			{
				return ledger;
			}
			
		}
		
	}
}