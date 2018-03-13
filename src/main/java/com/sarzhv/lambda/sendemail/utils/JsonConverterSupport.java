package com.sarzhv.lambda.sendemail.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
public class JsonConverterSupport {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> Map<String, T> convertFromJsonToMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, T>>(){});
        }
        catch (IOException e) {
            StringWriter errorWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(errorWriter));
            log.error(e.toString());

            throw new IllegalArgumentException("Input should represent json map object");
        }
    }

    public static <T> Map<String, T> convertFromInputStreamToMap(InputStream inputStream) {
        try {
            return objectMapper.readValue(inputStream, new TypeReference<Map<String, T>>(){});
        }
        catch (IOException e) {
            StringWriter errorWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(errorWriter));
            log.error(e.toString());

            throw new IllegalArgumentException("Input should represent json map object");
        }
    }

    public static String convertFromMapToJson(Map<String, ?> map) {
        try {
            return objectMapper.writeValueAsString(map);
        }
        catch (IOException e) {
            StringWriter errorWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(errorWriter));
            log.error(e.toString());

            throw new IllegalArgumentException("Value can't be serialized");
        }
    }

    private JsonConverterSupport() {
        throw new IllegalStateException("Utility class");
    }

}
