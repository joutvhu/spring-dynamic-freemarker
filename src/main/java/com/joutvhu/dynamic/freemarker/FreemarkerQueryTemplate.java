package com.joutvhu.dynamic.freemarker;

import com.joutvhu.dynamic.commons.DynamicQueryTemplate;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.Map;

@RequiredArgsConstructor
public class FreemarkerQueryTemplate implements DynamicQueryTemplate {
    private final Template template;

    @SneakyThrows
    @Override
    public String process(Map<String, Object> params) {
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
    }
}
