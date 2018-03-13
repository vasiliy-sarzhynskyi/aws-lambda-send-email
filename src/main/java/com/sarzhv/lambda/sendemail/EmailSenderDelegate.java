package com.sarzhv.lambda.sendemail;

import com.sarzhv.lambda.sendemail.config.EmailSenderConfig;
import com.sarzhv.lambda.sendemail.service.MailService;
import com.sarzhv.lambda.sendemail.service.MailServiceImpl;
import com.sarzhv.lambda.sendemail.utils.template.FreeMarkerTemplateSupport;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class EmailSenderDelegate {

    private static final String BOOK_ID_KEY = "bookId";
    private static final String BOOK_NAME_KEY = "bookName";
    private static final String CHAPTERS_KEY = "chapters";
    private static final String EMAIL_ADDRESS_KEY = "emailAddress";

    private static final String EMAIL_SUBJECT_TEMPLATE = "Amazing Book Service - \"%s\"";
    private static final String WEB_TRANSFER_BOOK_URL_TEMPLATE = "https://www.example.com?transferBookId=%s";

    private MailService mailService = new MailServiceImpl();

    private EmailSenderConfig config = EmailSenderConfig.getConfig();

    public void sendTransferBookEmail(Map<String, Object> transferBookData) throws Exception {
        TransferBookValidator.validateTransferBookData(transferBookData);

        if (StringUtils.isEmpty((String) transferBookData.get(BOOK_ID_KEY))) {
            transferBookData.put(BOOK_ID_KEY, generateBookId());
        }

        Map<String, Object> emailTemplateDataModel = buildDataModelForEmailTemplate(transferBookData);

        String htmlBodyContent = buildHtmlEmailContent(emailTemplateDataModel);
        log.debug("htmlBodyContent:\n" + htmlBodyContent);

        String textBodyContent = buildTextEmailContent(emailTemplateDataModel);

        String bookName = (String) transferBookData.get(BOOK_NAME_KEY);
        String subject = String.format(EMAIL_SUBJECT_TEMPLATE, bookName);
        String fromEmailAddress = config.getFromEmailAddress();
        String toEmailAddress = (String) transferBookData.get(EMAIL_ADDRESS_KEY);

        mailService.sendHtmlEmail(subject, htmlBodyContent, textBodyContent, fromEmailAddress, toEmailAddress);
    }

    private static Map<String, Object> buildDataModelForEmailTemplate(Map<String, Object> transferBookData) {
        String bookId = (String) transferBookData.getOrDefault(BOOK_ID_KEY, "");
        String bookName = (String) transferBookData.get(BOOK_NAME_KEY);
        List<Map<String, Object>> bookChapters = (List) transferBookData.get(CHAPTERS_KEY);

        List<Map<String, String>> chapterDetails = bookChapters.stream()
                .map(chapter -> {
                    Map<String, String> details = new HashMap<>();
                    details.put("description", (String) chapter.get("description"));
                    return details;
                })
                .collect(Collectors.toList());

        String currentYear = Year.now().toString();
        String bookWebLink = String.format(WEB_TRANSFER_BOOK_URL_TEMPLATE, bookId);

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put(BOOK_NAME_KEY, bookName);
        dataModel.put(CHAPTERS_KEY, chapterDetails);
        dataModel.put("bookWebLink", bookWebLink);
        dataModel.put("currentYear", currentYear);

        return dataModel;
    }

    private static String buildHtmlEmailContent(Map<String, Object> dataModel) throws IOException, TemplateException {
        return FreeMarkerTemplateSupport.processTemplate(dataModel, "transfer_book_email_template.html");
    }

    private static String buildTextEmailContent(Map<String, Object> dataModel) throws IOException, TemplateException {
        return FreeMarkerTemplateSupport.processTemplate(dataModel, "transfer_book_email_template.txt");
    }

    private String generateBookId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
