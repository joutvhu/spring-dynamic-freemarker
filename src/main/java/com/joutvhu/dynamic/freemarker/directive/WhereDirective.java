package com.joutvhu.dynamic.freemarker.directive;

import com.joutvhu.dynamic.commons.directive.TrimSymbol;
import com.joutvhu.dynamic.freemarker.io.TrimWriter;
import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.util.Map;

/**
 * The where directive knows to only insert "WHERE" if there is any content returned by the containing tags,
 * If that content begins or ends with "AND" or "OR", it knows to strip it off.
 * They are used in templates like {@code <@where>...</@where>}
 *
 * @author Giao Ho
 * @since 1.0.0
 */
public class WhereDirective implements TemplateDirectiveModel {
    private static final TrimSymbol symbol = new TrimSymbol(
            "where", TrimSymbol.getOverrides(true, "and", "or"),
            null, TrimSymbol.getOverrides(false, "and", "or"));

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        if (!params.isEmpty())
            throw new TemplateModelException("This directive doesn't allow parameters.");

        if (body != null)
            TrimWriter.of(env.getOut(), symbol).render(body);
    }
}
