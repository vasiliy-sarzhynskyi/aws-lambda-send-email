package com.sarzhv.lambda.sendemail;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.http.HttpException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class TransferBookValidator {

    private static final String BOOK_NAME_KEY = "bookName";
    private static final String CHAPTERS_KEY = "chapters";
    private static final String EMAIL_ADDRESS_KEY = "emailAddress";

    public static void validateTransferBookData(Map<String, Object> transferBookData)
            throws IOException, HttpException, GeneralSecurityException {
        validateTransferBookRequiredFields(transferBookData);
        validateEmailAddress(transferBookData.get(EMAIL_ADDRESS_KEY).toString());
    }

    private static void validateTransferBookRequiredFields(Map<String, Object> transferBookData) {
        boolean isRequiredFieldsPresent = Objects.nonNull(transferBookData)
                && transferBookData.containsKey(CHAPTERS_KEY)
                && transferBookData.containsKey(BOOK_NAME_KEY)
                && transferBookData.containsKey(EMAIL_ADDRESS_KEY);

        if (!isRequiredFieldsPresent) {
            throw new IllegalArgumentException("Transfer Book data should have the following fields: "
                    + String.join(", ", CHAPTERS_KEY, BOOK_NAME_KEY, EMAIL_ADDRESS_KEY));
        }
    }

    private static void validateEmailAddress(String emailAddress) {
        if (!EmailValidator.getInstance().isValid(emailAddress)) {
            throw new IllegalArgumentException(String.format("Provided email address '%s' is not valid", emailAddress));
        }
    }

}
