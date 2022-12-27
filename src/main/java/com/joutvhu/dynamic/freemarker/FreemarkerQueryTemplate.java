package com.joutvhu.dynamic.freemarker;

import com.joutvhu.dynamic.commons.DynamicQueryTemplate;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

@RequiredArgsConstructor
public class FreemarkerQueryTemplate implements DynamicQueryTemplate {
    private final Template template;

    public FreemarkerQueryTemplate(String name, String content, Configuration cfg, String encoding) throws IOException {
        this.template = new Template(name, new StringReader(content), cfg, encoding);
    }

    @SneakyThrows
    @Override
    public String process(Map<String, Object> params) {
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
    }
}
