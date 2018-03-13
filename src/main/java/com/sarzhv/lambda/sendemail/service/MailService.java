package com.sarzhv.lambda.sendemail.service;

import javax.mail.MessagingException;

public interface MailService {

    void sendHtmlEmail(String subject, String htmlBodyContent, String textBodyContent,
                       String fromEmailAddress, String toEmailAddress) throws MessagingException;

}