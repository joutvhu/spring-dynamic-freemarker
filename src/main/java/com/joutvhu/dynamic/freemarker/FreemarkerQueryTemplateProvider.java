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
 * Freemarker dynamic query template provider.
 *
 * @author Giao Ho
 * @since 1.0.0
 */
@NoArgsConstructor
public class FreemarkerQueryTemplateProvider extends DynamicQueryTemplateProvider {
    private static final Log log = LogFactory.getLog(FreemarkerQueryTemplateProvider.class);

    private final StringTemplateLoader sqlTemplateLoader = new StringTemplateLoader();
    private FreemarkerTemplateConfiguration configuration;
    private Configuration cfg;

    public void setConfiguration(FreemarkerTemplateConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public DynamicQueryTemplate createTemplate(String name, String content) {
        try {
            return new FreemarkerQueryTemplate(name, content, cfg, encoding);
        } catch (IOException e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public DynamicQueryTemplate findTemplate(String name) {
        try {
            Template template = cfg.getTemplate(name, encoding);
            if (template != null)
                return new FreemarkerQueryTemplate(template);
        } catch (IOException e) {
            log.error(e);
        }
        return null;
    }

    @Override
    protected void putTemplate(String name, String content) {
        Object src = sqlTemplateLoader.findTemplateSource(name);
        if (src != null)
            log.warn("Found duplicate template key, will replace the value, key: " + name);
        sqlTemplateLoader.putTemplate(name, content);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (configuration == null)
            configuration = FreemarkerTemplateConfiguration.instanceWithDefault();
        configuration = configuration.templateLoader(sqlTemplateLoader);
        cfg = configuration.configuration();
        super.afterPropertiesSet();
    }
}
