package com.sarzhv.lambda.sendemail;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.sarzhv.lambda.sendemail.apigateway.ApiGatewayRequestExtractor;
import com.sarzhv.lambda.sendemail.apigateway.ApiGatewayResponseBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EmailSenderLambdaRequestHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> inputData, Context context) {
        log.info("EmailSenderLambdaRequestHandler inputData: " + inputData);

        Map<String, Object> transferBookData = ApiGatewayRequestExtractor.extractBodyFromInput(inputData);
        Map<String, String> headers = ApiGatewayRequestExtractor.extractHeadersFromInput(inputData);

        Map<String, String> responseBody;
        int statusCode;

        try {
            EmailSenderDelegate emailSenderDelegate = new EmailSenderDelegate();
            emailSenderDelegate.sendTransferBookEmail(transferBookData);

            responseBody = buildSuccessResponseBody();
            statusCode = 200;
        }
        catch (Exception e) {
            log.error("Error occurred during handling request", e);
            responseBody = buildFailedResponseBody(e);
            statusCode = getResponseStatusCodeByException(e);
        }

        return ApiGatewayResponseBuilder.buildApiGatewayResponse(responseBody, statusCode);
    }

    private static Map<String, String> buildSuccessResponseBody() {
        Map<String, String> response = new HashMap<>();
        response.put("result", "success");
        return response;
    }

    private static Map<String, String> buildFailedResponseBody(Exception e) {
        String exceptionMessage = e.getMessage();
        String errorType = "unknown";
        if (exceptionMessage.contains("email address")) {
            errorType = "EmailValidation";
        }

        Map<String, String> response = new HashMap<>();
        response.put("result", "failed");
        response.put("errorMessage", exceptionMessage);
        response.put("errorType", errorType);

        return response;
    }

    private static int getResponseStatusCodeByException(Exception e) {
        if (e instanceof MessagingException) {
            return 500;
        } else {
            return 400;
        }
    }


}
