package com.sarzhv.lambda.sendemail.service;

import com.sarzhv.lambda.sendemail.config.EmailSenderConfig;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Slf4j
public class MailServiceImpl implements MailService {

    private EmailSenderConfig config;

    public MailServiceImpl() {
        config = EmailSenderConfig.getConfig();
    }

    @Override
    public void sendHtmlEmail(String subject, String htmlBodyContent, String textBodyContent,
                              String fromEmailAddress, String toEmailAddress) throws MessagingException {
        Session session = createMailSession();
        MimeMessage msg = createHtmlMessage(session, fromEmailAddress, toEmailAddress, subject, htmlBodyContent, textBodyContent);
        log.info("Sending email to '{}' ...", toEmailAddress);
        sendEmailMessage(session, msg);
    }


    private Session createMailSession() {
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", config.getTransportProtocol());
        props.put("mail.smtp.port", config.getSmtpPort());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");

        // Create a Session object to represent a mail session with the specified properties.
        return Session.getDefaultInstance(props);
    }

    private MimeMessage createHtmlMessage(Session session, String fromEmailAddress, String recipientEmailAddress,
                                          String subject, String htmlBodyContent, String textBodyContent) throws MessagingException {
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(fromEmailAddress));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmailAddress));
        msg.setSubject(subject);

        Multipart multiPart = new MimeMultipart("alternative");

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(textBodyContent, "utf-8");

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBodyContent, "text/html; charset=utf-8");

        // order of adding body parts into multiPart is important
        multiPart.addBodyPart(textPart);
        multiPart.addBodyPart(htmlPart);
        msg.setContent(multiPart);
        return msg;
    }

    private void sendEmailMessage(Session session, MimeMessage msg) throws MessagingException {
        Transport transport = session.getTransport();

        try {
            transport.connect(config.getSmtpHost(), config.getSmtpUserName(), config.getSmtpPassword());
            transport.sendMessage(msg, msg.getAllRecipients());

            log.info("Email has been sent!");
        } catch (MessagingException ex) {
            log.error("Email was not sent. Error message: " + ex.getMessage());
            throw ex;
        } finally {
            transport.close();
        }
    }
}
