package com.prospera.serviceimpl;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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
import com.prospera.model.Sanction;
import com.prospera.repository.CustomerRepository;
import com.prospera.servicei.CustomerServiceI;

import jakarta.mail.internet.MimeMessage;

@Service
public class CustomerServiceImpl implements CustomerServiceI
{
	@Autowired
	CustomerRepository cr;
	
	@Autowired
	JavaMailSender sender;
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
			if(o.get().getEnquiry().getEnquiryStatus().equals("Sanction Letter Generated"))
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

	@Override
	public byte[] generateSanctionLetter(int cid)
	{
		Optional<Customer> cd = cr.findById(cid);
		Customer cd1=cd.get();
		if(cd.isPresent()) 
		{	
			String title = "Prospera Finance Ltd.";

			Document document = new Document(PageSize.A4);

			String content1 = "\n\n Dear " + cd1.getFirstName()
					+ ","
					+ "\nProspera Finance Ltd. is Happy to informed you that your loan application has been approved. ";

			String content2 = "\n\nWe hope that you find the terms and conditions of this loan satisfactory "
					+ "and that it will help you meet your financial needs.\n\nIf you have any questions or need any assistance regarding your loan, "
					+ "please do not hesitate to contact us.\n\nWe wish you all the best and thank you for choosing us."
					+ "\n\nSincerely,\n\n" + "Vijay Chaudhari (Credit Manager)";

			ByteArrayOutputStream opt = new ByteArrayOutputStream();
			
			PdfWriter.getInstance(document, opt);
			document.open();

			Image img = null;
			try {

				img = Image.getInstance("C:\\Users\\gayat\\OneDrive\\Pictures\\prospera.png");
				
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

			cell.setPhrase(new Phrase("Loan Amount Sanctioned", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(String.valueOf("₹ " + cd1.getSanction().getLoanamount()),font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Loan Tenure", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(String.valueOf(cd1.getSanction().getTenure()), font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Interest Rate", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(String.valueOf(cd1.getSanction().getInterestRate()) + " %", font1));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Sanction letter generated Date", font));
			table.addCell(cell);

			cd1.getSanction().setDate(new Date());
			cell.setPhrase(new Phrase(String.valueOf(cd1.getSanction().getDate()), font1));
			table.addCell(cell);
			
			cell.setPhrase(new Phrase("EMI Amount", font));
			table.addCell(cell);
			
			cell.setPhrase(new Phrase(String.valueOf(cd1.getSanction().getEmiAmount()),font1));
			table.addCell(cell);

			document.add(table);

			Font titlefont3 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10);
			Paragraph paracontent2 = new Paragraph(content2, titlefont3);
			document.add(paracontent2);
			document.close();
			
			
			ByteArrayInputStream byt = new ByteArrayInputStream(opt.toByteArray());
			byte[] bytes = byt.readAllBytes();
			cd1.getSanction().setSanctionLetter(bytes);
			cd1.getEnquiry().setEnquiryStatus("Sanction Letter Generated");
			cr.save(cd1);
			return bytes;
		}
		else 
		{
			return null;
		}	
	}


	@Override
	public void emailSanctionLetter(int cid) throws Exception
	{
		Optional<Customer> o = cr.findById(cid);
			if(!(o.isPresent()))
			{
				throw new InvalidCustomerException("Customer is not present in database");
			}
			else
			{
				Customer c = o.get();
				if(c.getEnquiry().getEnquiryStatus().equals("Sanction Letter Generated")&& c.getEnquiry().getLoanStatus().equals("Verification Approved"))
				{
					MimeMessage mm = sender.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(mm,true);
					helper.setTo(c.getEmail());
					helper.setSubject("Loan Sanction Letter");
					helper.setText("Hello "+c.getFirstName()+",\nWe are please to share with you the sanction letter for your loan application.\n"
							+ "Please go through the document carefully and please sign the letter and submit it to the Relationship Excecutive.\n"
							+ "If you have any doubts or if you wish to reject the sanction letter please let us know about it as we may revise the letter by taking into consideration your conditions as well.\n"
							+ "Please find attached.\nTeam Prospera Finance");
					helper.addAttachment("Invoice.pdf", new ByteArrayResource(c.getSanction().getSanctionLetter()));
					sender.send(mm);
				}
				else if(c.getEnquiry().getEnquiryStatus().equals("EMI calculated")&& c.getEnquiry().getLoanStatus().equals("Verification Approved"))
				{
					throw new InvalidCustomerException("Sanction letter not yet generated");
				}
				else
				{
					throw new InvalidCustomerException("Invalid Customer");
				}
			}
					
		}

	@Override
	public void forwardToAH(int cid)
	{
		Optional<Customer> o = cr.findById(cid);
		if(!(o.isPresent()))
		{
			throw new InvalidCustomerException("Invalid customer");
		}
		else
		{
			Customer c = o.get();
			if(c.getEnquiry().getEnquiryStatus().equals("Sanction approved by customer"))
			{
				c.getEnquiry().setEnquiryStatus("Forwarded to Account Head");
				cr.save(c);
			}
			else
			{
				throw new InvalidCustomerException("Invalid customer");
			}
		}
	}
}
