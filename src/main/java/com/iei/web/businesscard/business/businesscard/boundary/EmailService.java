/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iei.web.businesscard.business.businesscard.boundary;

import com.iei.web.businesscard.business.businesscard.entity.BusinessCard;
import com.iei.web.businesscard.business.businesscard.entity.CompanyEnum;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.FdfWriter;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

/**
 *
 * @author rabhala
 */
@Stateless

public class EmailService {

    private final String USERNAME = "email.username";
    private final String PASSWORD = "email.password";
    private final String BODY_MESSAGE = "email.body.message";
    private final String SUBJECT = "email.body.subject";
    private final String IEI_TEMPLATE = "pdf.template.iei";
    private final String VE_TEMPLATE = "pdf.template.ve";
    private final String SMTP_HOST = "email.smtp.host";
    private final String SMTP_PORT = "email.smtp.port";
    private final String RESOURCE_NAME = "email.resource.name";
    private final String ATTACHMENT_NAME = "email.attachment.name";
    @Resource(name = RESOURCE_NAME)
    private Session session;

    ResourceBundle content = ResourceBundle.getBundle("com.iei.web.businesscard.content");
    ResourceBundle config = ResourceBundle.getBundle("com.iei.web.businesscard.config");

    public void sendEmail(BusinessCard businessCard) throws DocumentException, Exception {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", config.getString(SMTP_HOST));
        props.put("mail.smtp.port", config.getString(SMTP_PORT));

        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(config.getString(USERNAME), config.getString(PASSWORD));
                    }
                });
        ByteArrayOutputStream outputStream;
        try {

            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(content.getString(BODY_MESSAGE) + businessCard.getCompany().getLabel());

            outputStream = new ByteArrayOutputStream();
            byte[] bytes = writePdf(outputStream, businessCard);

            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName(content.getString(ATTACHMENT_NAME) + businessCard.getName() + ".pdf");

            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textBodyPart);
            mimeMultipart.addBodyPart(pdfBodyPart);

            InternetAddress iaSender = new InternetAddress(config.getString(USERNAME));
            InternetAddress iaRecipient = new InternetAddress(businessCard.getEmail());

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setSender(iaSender);
            mimeMessage.setSubject(content.getString(SUBJECT));
            mimeMessage.setRecipient(Message.RecipientType.TO, iaRecipient);
            mimeMessage.setContent(mimeMultipart);

            Transport.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTemplate(BusinessCard businessCard) {
        String template;
        if (businessCard.getCompany().equals(CompanyEnum.IE)) {
            template = content.getString(IEI_TEMPLATE);
        } else {
            template = content.getString(VE_TEMPLATE);
        }
        return template;
    }

    public String convertEmail(BusinessCard businessCard) {
        String email;
        if (businessCard.getEmail().toLowerCase().contains("infiniteenergy")) {
            email = businessCard.getEmail().replaceAll("(?i)infiniteenergy", "InfiniteEnergy");
        } else {
            email = businessCard.getEmail().replaceAll("(?i)veteranenergy", "VeteranEnergy");
        }
        return email;
    }

    public byte[] writePdf(ByteArrayOutputStream outputStream, BusinessCard businessCard) throws IOException, DocumentException {
        PdfStamper stamper = null;
        ByteArrayOutputStream byteStream = null;
        String template = getTemplate(businessCard);
        String email = convertEmail(businessCard);
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(template);
        PdfReader outReader = new PdfReader(is);
        byteStream = new ByteArrayOutputStream();
        stamper = new PdfStamper(outReader, byteStream);
        AcroFields acroField = stamper.getAcroFields();
        acroField.setFieldProperty("Name", "textsize", new Float(6), null);
        acroField.setField("Name", businessCard.getName());
        acroField.setField("Title", businessCard.getDesignation());
        acroField.setField("Cell", businessCard.getCell());
        acroField.setField("Phone", businessCard.getCompany().getPhone() + "  Ext." + businessCard.getExtension());
        acroField.setField("Web", businessCard.getCompany().getCompanyURL());
        acroField.setField("Email", email);

        stamper.setFormFlattening(true);
        FdfWriter fdfWriter = new FdfWriter();
        acroField.exportAsFdf(fdfWriter);
        stamper.close();
        return byteStream.toByteArray();
    }
}
