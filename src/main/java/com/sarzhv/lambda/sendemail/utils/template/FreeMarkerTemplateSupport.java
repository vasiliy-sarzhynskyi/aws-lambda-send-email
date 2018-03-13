package com.sarzhv.lambda.sendemail.utils.template;

import com.sarzhv.lambda.sendemail.service.MailServiceImpl;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

@Slf4j
public class FreeMarkerTemplateSupport {

    private static Configuration cfg;

    static {
        cfg = new Configuration(Configuration.VERSION_2_3_23);
        cfg.setClassForTemplateLoading(MailServiceImpl.class, "/template");
    }

    public static String processTemplate(Map<String, Object> dataModel, String templateName) throws IOException, TemplateException {
        Writer out = new StringWriter();
        Template template = cfg.getTemplate(templateName);
        template.process(dataModel, out);

        return out.toString();
    }

    private FreeMarkerTemplateSupport() {
        throw new IllegalStateException("Utility class");
    }

}
