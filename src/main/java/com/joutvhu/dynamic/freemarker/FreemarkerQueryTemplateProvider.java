package com.joutvhu.dynamic.freemarker;

import com.joutvhu.dynamic.commons.DynamicQueryTemplate;
import com.joutvhu.dynamic.commons.DynamicQueryTemplateProvider;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.NoArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * FreemarkerQueryTemplateProvider
 *
 * @author Giao Ho
 * @since 1.0.0
 */
@NoArgsConstructor
public class FreemarkerQueryTemplateProvider extends DynamicQueryTemplateProvider {
    private static final Log log = LogFactory.getLog(FreemarkerQueryTemplateProvider.class);

    private static StringTemplateLoader sqlTemplateLoader = new StringTemplateLoader();
    private static Configuration cfg = FreemarkerTemplateConfiguration
            .instanceWithDefault()
            .templateLoader(sqlTemplateLoader)
            .configuration();

    @Override
    public DynamicQueryTemplate createTemplate(String name, String content) {
        try {
            return new FreemarkerQueryTemplate(new Template(name, content, cfg));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DynamicQueryTemplate findTemplate(String name) {
        try {
            Template template = cfg.getTemplate(name, encoding);
            return new FreemarkerQueryTemplate(template);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void putTemplate(String name, String content) {
        Object src = sqlTemplateLoader.findTemplateSource(name);
        if (src != null)
            log.warn("Found duplicate template key, will replace the value, key: " + name);
        sqlTemplateLoader.putTemplate(name, content);
    }
}
