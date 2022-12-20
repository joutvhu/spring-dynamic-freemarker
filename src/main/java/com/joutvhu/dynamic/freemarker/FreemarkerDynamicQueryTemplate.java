package com.joutvhu.dynamic.freemarker;

import com.joutvhu.dynamic.commons.DynamicQueryTemplate;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FreemarkerDynamicQueryTemplate implements DynamicQueryTemplate<Template> {
    private final Template template;

    @Override
    public Template getTemplate() {
        return this.template;
    }
}
