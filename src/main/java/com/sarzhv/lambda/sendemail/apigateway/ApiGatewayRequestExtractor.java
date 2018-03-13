package com.sarzhv.lambda.sendemail.apigateway;

import com.sarzhv.lambda.sendemail.utils.JsonConverterSupport;

import java.util.Map;

public class ApiGatewayRequestExtractor {

    public static Map<String, Object> extractBodyFromInput(Map<String, Object> inputData) {
        Object bodyObject = inputData.get("body");
        Map<String, Object> body;
        // when request triggered from API Gateway, 'body' value has String type
        if (bodyObject instanceof String) {
            body = JsonConverterSupport.convertFromJsonToMap(bodyObject.toString());
        } else {
            body = (Map<String, Object>) bodyObject;
        }

        return body;
    }

    public static Map<String, String> extractHeadersFromInput(Map<String, Object> inputData) {
        Object headersObject = inputData.get("headers");
        Map<String, String> headers;
        if (headersObject instanceof String) {
            headers = JsonConverterSupport.convertFromJsonToMap(headersObject.toString());
        } else {
            headers = (Map<String, String>) headersObject;
        }

        return headers;
    }

    private ApiGatewayRequestExtractor() {
        throw new IllegalStateException("Utility class");
    }

}
