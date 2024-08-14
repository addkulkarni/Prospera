package com.prospera.serviceimpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
import com.prospera.model.Customer;
import com.prospera.model.Disbursement;
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
				for(int i=1; i<=c.getSanction().getTenure(); i++)
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

	@Override
	public String updateLedgerList(int cid, Ledger l) throws Exception
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
				
				//getting generated EMI reciept through an another method
				
				CustomerServiceImpl csi = new CustomerServiceImpl();
				byte[] emiReciept = csi.generateEMIReciept(c);
				
				MimeMessage mm = sender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mm, true);
				helper.setTo(c.getEnquiry().getEmail());
				helper.setSubject("EMI payment reciept");
				helper.setText("Hello "+c.getFirstName()+",\nYour EMI for the current month has been paid successfully.\n"
						+ "Attached is the reciept for the transaction.\nThank you for banking with us.\nTeam Prospera Finance");
				helper.addAttachment("EMI Reciept.pdf", new ByteArrayResource(emiReciept));
				
				sender.send(mm);
				c.setLedger(ledgerList);
				cr.save(c);
				return "EMI for the current month has been paid.";
			}
		}
	}

	//method to create EMI reciept
	public byte[] generateEMIReciept(Customer c)
	{
		if(c!=null) 
		{
//			cd1.getSanction().setDate(new Date());
//			cd1.getSanction().setSanctionId(s.getSanctionId());
//			cd1.getSanction().setFirstName(cd1.getFirstName());
//			cd1.getSanction().setLastName(cd1.getLastName());
//			cd1.getSanction().setLoanamount(s.getLoanamount());
//			cd1.getSanction().setTenure(s.getTenure());
//			cd1.getSanction().setInterestRate(s.getInterestRate());
//			cd1.getSanction().setEmiAmount(s.getEmiAmount());
	
			String title = "Prospera Finance Ltd.";

			Document document = new Document(PageSize.A4);

			String content1 = "\n\nDear " + c.getFirstName()
					+ ","
					+ "\nProspera Finance Ltd. is Happy to informed you that you have successfully paid your EMI for this month";

			String content2 = "\nAbove are the details about the transaction. Please contact the bank in case of any discrepencies in the transaction.\n"
					+ "Thank you for banking with us.\nTeam Prospera Finance";

			ByteArrayOutputStream opt = new ByteArrayOutputStream();
			
			PdfWriter.getInstance(document, opt);
			document.open();

			Image img = null;
			try {

				img = Image.getInstance("C:\\Users\\omkar\\Desktop\\Git\\prosperalogo.jpeg");
				
				img.scalePercent(50, 50);
				img.setAlignment(Element.ALIGN_RIGHT);
				document.add(img);

			} 
			catch (BadElementException e1)
			{
				e1.printStackTrace();
			}
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}

			Font titlefont = FontFactory.getFont(FontFactory.COURIER, 25);
			Paragraph titlepara = new Paragraph(title, titlefont);
			titlepara.setAlignment(Element.ALIGN_CENTER);
			document.add(titlepara);

			Font titlefont2 = FontFactory.getFont(FontFactory.COURIER, 10);
			Paragraph paracontent1 = new Paragraph(content1, titlefont2);
			document.add(paracontent1);

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100f);
			table.setWidths(new int[] { 2, 2 });
			table.setSpacingBefore(10);

			PdfPCell cell = new PdfPCell();
			cell.setBackgroundColor(CMYKColor.WHITE);
			cell.setPadding(5);

			Font font = FontFactory.getFont(FontFactory.COURIER);
			font.setColor(5, 5, 161);

			Font font1 = FontFactory.getFont(FontFactory.COURIER);
			font.setColor(5, 5, 161);

			cell.setPhrase(new Phrase("EMI Amount Paid", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(String.valueOf("₹ " + c.getSanction().getEmiAmount()),font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Transaction ID", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(String.valueOf(new Random().nextInt(1000000,9999999)), font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Transaction timestamp", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(String.valueOf(new Date()), font1));
			table.addCell(cell);

			document.add(table);

			Font titlefont3 = FontFactory.getFont(FontFactory.COURIER, 10);
			Paragraph paracontent2 = new Paragraph(content2, titlefont3);
			document.add(paracontent2);
			document.close();
			
			
			ByteArrayInputStream byt = new ByteArrayInputStream(opt.toByteArray());
			byte[] bytes = byt.readAllBytes();
			return bytes;
		}
		else 
		{
			return null;
		}	
	}

	@Override
	public byte[] generateDisbursementletter(int cid) throws MessagingException {
		Optional<Customer> cd = cr.findById(cid);
		Customer cd1=cd.get();
		if(cd.isPresent()) 
		{
			String title = "Prospera Finance Ltd.";

			Document document = new Document(PageSize.A4);

			String content1 = "\n\n Dear " + cd1.getFirstName()
					+ ","
					+ "\nProspera Finance Ltd. is Happy to informed you that your Disbursement Amount has been completed . ";

			String content2 = "\n\nThe funds have been transferred to your designated account and should be available for use shortly. "
					+ "Should you have any questions or require further assistance, please do not hesitate to contact us., "
					+ "please do not hesitate to contact us.\n\nWe wish you all the best and thank you for choosing us."
					+ "\n\nSincerely,\n\n" + "Vijay Chaudhari (Credit Manager)";

			ByteArrayOutputStream opt = new ByteArrayOutputStream();
			
			PdfWriter.getInstance(document, opt);
			document.open();

			Image img = null;
			try {

				img = Image.getInstance("C:\\Users\\addku\\Desktop\\ProsperaConfig\\prospera.png");
				
				img.scalePercent(50, 50);
				img.setAlignment(Element.ALIGN_RIGHT);
				document.add(img);

			} 
			catch (BadElementException e1)
			{
				e1.printStackTrace();
			}
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}

			Font titlefont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 25);
			Paragraph titlepara = new Paragraph(title, titlefont);
			titlepara.setAlignment(Element.ALIGN_CENTER);
			document.add(titlepara);

			Font titlefont2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10);
			Paragraph paracontent1 = new Paragraph(content1, titlefont2);
			document.add(paracontent1);

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100f);
			table.setWidths(new int[] { 2, 2 });
			table.setSpacingBefore(10);

			PdfPCell cell = new PdfPCell();
			cell.setBackgroundColor(CMYKColor.WHITE);
			cell.setPadding(5);

			Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			font.setColor(5, 5, 161);

			Font font1 = FontFactory.getFont(FontFactory.HELVETICA);
			font.setColor(5, 5, 161);

			cell.setPhrase(new Phrase("Disbursement ID", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(String.valueOf(new Random().nextInt(100000,99999999)), font1));
			table.addCell(cell);
			
			cell.setPhrase(new Phrase("Total Disbursement Amount", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(String.valueOf("₹ " + cd1.getSanction().getLoanamount()),font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Disbursement Account Number", font));
			table.addCell(cell);
                 
//			 set random DisbursementAccountNo
			 int disAcc=new Random().nextInt(10000000,999999999);
			 Disbursement d = new Disbursement();
			 d.setDisbursementAccountNo(disAcc);;
			 cd1.setDisbursement(d);
			 cr.save(cd1);
			 
			cell.setPhrase(new Phrase(String.valueOf(disAcc), font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Disbursement timestamp", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(String.valueOf(new Date()), font1));
			table.addCell(cell);

			document.add(table);

			Font titlefont3 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10);
			Paragraph paracontent2 = new Paragraph(content2, titlefont3);
			document.add(paracontent2);
			document.close();
			
			
			ByteArrayInputStream byt = new ByteArrayInputStream(opt.toByteArray());
			byte[] bytes = byt.readAllBytes();
			cd1.getDisbursement().setDisbursementLetter(bytes);
			cr.save(cd1);
				MimeMessage mm = sender.createMimeMessage();
				MimeMessageHelper helper=new MimeMessageHelper(mm,true);
				helper.setTo(cd1.getEmail());
				helper.setSubject("Disbursement Letter");
				helper.setText("Hello "+cd1.getFirstName()+",\nWe are please to share with you the Disbursement letter for your loan application.\n"
						+ "\nTeam Prospera Finance");
				helper.addAttachment("Disbursement Letter.pdf", new ByteArrayResource(bytes));
				sender.send(mm);
		       return bytes;
		}
		else
		{
			return null ;
		}
	}

	@Override
	public Customer getCustomer(int cid) {
		
		return cr.findById(cid).get();
	}

	@Override
	public void closeLoan(int cid)
	{
		Customer c = cr.findById(cid).get();
		List<Ledger>ledgerList = c.getLedger();
		for(Ledger l:ledgerList)
		{
			l.setLoanStatus("Closed");
		}
		c.setLedger(ledgerList);
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(c.getEmail());
		msg.setSubject("Loan closing update");
		msg.setText("Hello "+c.getFirstName()+",\nYour loan has been closed after the last payment\nThank you");
		sender.send(msg);
		cr.save(c);
	}

	
	


	
	
}
