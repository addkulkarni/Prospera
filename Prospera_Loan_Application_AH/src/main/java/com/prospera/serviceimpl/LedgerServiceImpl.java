package com.prospera.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.prospera.exception.InvalidLedgerException;
import com.prospera.model.Customer;
import com.prospera.model.Ledger;
import com.prospera.repository.LedgerRepository;
import com.prospera.servicei.LedgerServiceI;

@Service
public class LedgerServiceImpl implements LedgerServiceI
{
	@Autowired
	LedgerRepository lr;

	@Autowired
	JavaMailSender sender;
	
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
	public List<Ledger> skipEMI(int ledgerId, List<Ledger> ledger,Customer c)
	{
		Optional<Ledger> o = lr.findById(ledgerId);
		if(!(o.isPresent()))
		{
			throw new InvalidLedgerException("Invalid Ledger");
		}
		else
		{
			if(o.get().getInstallmentNumber()==o.get().getTenure())
			{
				throw new InvalidLedgerException("Last installment cannot be skipped. It must be paid");
			}
			else
			{
				Ledger l = o.get();
				l.setCurrentMonthEmiStatus("Skipped");
				
				double newEMI = (l.getRemainingAmount()*l.getMonthlyEmi())/(l.getRemainingAmount()-l.getMonthlyEmi());
				float newE = (float)newEMI;
				System.out.println("Remaining amount: "+l.getRemainingAmount());
				System.out.println("Monthly EMI: "+l.getMonthlyEmi());
				System.out.println("---------"+newEMI+"------------------");
				for(Ledger led:ledger)
				{
					led.setMonthlyEmi(newE);
				}
				
				SimpleMailMessage message=new SimpleMailMessage();
				     message.setTo(c.getEmail());
				     message.setSubject("Notification of Missed EMI");
				     message.setText("Hello "+c.getFirstName()+",\nWe would like to bring to your attention that your recent EMI payment, due on " +l.getAmountPaidTillDate()+ " ,appears to have been missed.\n" +
				     "\nIf you have already made the payment or if you believe there is an error, please contact us immediately so we can review and update our records.\n"
				     + "Also your monthly EMIs have been revised to pay the complete amount in the tenure set from your end.\n\nTeam Prospera Finance");
					 sender.send(message);
					
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
					SimpleMailMessage message1=new SimpleMailMessage();
				    message1.setTo(c.getEmail());
				    message1.setSubject("Important Notice: Account Marked as Defaulter Due to Missed EMIs");
				    message1.setText("\n We regret to inform you that, due to the non-payment of three consecutive EMIs, your account has been marked as a defaulter");
					sender.send(message1);
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
}