package com.sarzhv.lambda.sendemail;

import com.sarzhv.lambda.sendemail.utils.JsonConverterSupport;

import java.io.InputStream;
import java.util.Map;

public class EmailSenderRunner {

    public static void main(String[] args) throws Exception {
        InputStream resourceAsStream = EmailSenderRunner.class.getResourceAsStream("/mocked_incoming_data/manual_or_lambda_event_transfer_book_mock.json");
        Map<String, Object> testTransferBookData = JsonConverterSupport.convertFromInputStreamToMap(resourceAsStream);

        Map<String, Object> transferBook = (Map<String, Object>) testTransferBookData.get("body");
        new EmailSenderDelegate().sendTransferBookEmail(transferBook);
    }

}
