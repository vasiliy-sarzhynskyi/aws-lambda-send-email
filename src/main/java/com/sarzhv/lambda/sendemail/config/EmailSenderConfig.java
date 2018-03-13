package com.sarzhv.lambda.sendemail.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class EmailSenderConfig {

    private static final EmailSenderConfig config;

    static {
        config = new EmailSenderConfig();
    }

    public static EmailSenderConfig getConfig() {
        return config;
    }

    private Properties properties;

    private EmailSenderConfig() {
        String basePropertyFile = "/config/config.properties";
        loadAndMergeProperties(basePropertyFile);
    }

    private void loadAndMergeProperties(final String basePropertyFile) {
        properties = new Properties();
        try {
            properties.load(EmailSenderConfig.class.getResourceAsStream(basePropertyFile));
        } catch (IOException e) {
            log.error("Error occurred during loading properties", e);
        }
    }

    public String getSmtpUserName() {
        return properties.getProperty("aws.ses.username");
    }

    public String getSmtpPassword() {
        return properties.getProperty("aws.ses.password");
    }

    public String getSmtpHost() {
        return properties.getProperty("aws.ses.mail.host");
    }

    public String getSmtpPort() {
        return properties.getProperty("aws.ses.mail.port");
    }

    public String getTransportProtocol() {
        return properties.getProperty("aws.ses.mail.transport.protocol");
    }

    public String getFromEmailAddress() {
        return properties.getProperty("app.fromEmailAddress");
    }

}
