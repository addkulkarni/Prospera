package com.prospera.serviceimpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.prospera.exception.InvalidCustomerException;
import com.prospera.exception.InvalidLedgerException;
import com.prospera.model.Customer;
import com.prospera.model.Ledger;
import com.prospera.repository.LedgerRepository;
import com.prospera.servicei.LedgerServiceI;

import jakarta.mail.internet.MimeMessage;

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
			Ledger l = o.get();
			l.setCurrentMonthEmiStatus("Skipped");
			SimpleMailMessage message=new SimpleMailMessage();
			     message.setTo(c.getFirstName());
			     message.setSubject("Notification of Missed EMI");
			     message.setText("\n We would like to bring to your attention that your recent EMI payment, due on " +l.getAmountPaidTillDate()+ " ,appears to have been missed.\n" +
			     "\n If you have already made the payment or if you believe there is an error, please contact us immediately so we can review and update our records.\n");
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
			     message1.setTo(c.getFirstName());
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