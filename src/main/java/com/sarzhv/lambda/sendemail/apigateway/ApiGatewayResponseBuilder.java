package com.sarzhv.lambda.sendemail.apigateway;

import com.sarzhv.lambda.sendemail.utils.JsonConverterSupport;

import java.util.HashMap;
import java.util.Map;

public class ApiGatewayResponseBuilder {

    public static Map<String, Object> buildApiGatewayResponse(Map<String, String> responseBody, int statusCode) {
        Map<String, String> responseHeaders = buildApiGatewayResponseHeaders();
        String responseBodyJson = JsonConverterSupport.convertFromMapToJson(responseBody);

        Map<String, Object> apiGatewayResponse = new HashMap<>();
        apiGatewayResponse.put("isBase64Encoded", false);
        apiGatewayResponse.put("statusCode", statusCode);
        apiGatewayResponse.put("headers", responseHeaders);
        apiGatewayResponse.put("body", responseBodyJson);

        return apiGatewayResponse;
    }

    private static Map<String, String> buildApiGatewayResponseHeaders() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Access-Control-Allow-Origin", "*");
        return responseHeaders;
    }

    private ApiGatewayResponseBuilder() {
        throw new IllegalStateException("Utility class");
    }

}
